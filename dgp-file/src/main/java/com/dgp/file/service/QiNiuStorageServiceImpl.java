package com.dgp.file.service;

import com.dgp.common.exception.NormalException;
import com.dgp.file.assembler.FileAssembler;
import com.dgp.file.client.QiNiuClient;
import com.dgp.file.config.FileObsConfig;
import com.dgp.file.config.ObsConfigProperties;
import com.dgp.file.dto.FileInfo;
import com.dgp.file.enums.FileStorageTypeEnum;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;

@Slf4j
@Component
public class QiNiuStorageServiceImpl implements IStorageService {

    @Autowired
    private ObsConfigProperties obsConfigProperties;

    private QiNiuClient qiNiuClient;

    private QiNiuClient getClient() {
        if (qiNiuClient == null) {
            qiNiuClient = new QiNiuClient(obsConfigProperties.getAccessKey(), obsConfigProperties.getSecretKey());
        }
        return qiNiuClient;
    }

    /**
     * 文件上传
     *
     * @param file   {@link MultipartFile} 文件
     * @param config {@link FileObsConfig} 文件配置
     * @return
     */
    @Override
    public FileInfo uploadFile(MultipartFile file, FileObsConfig config) {
        String filename = file.getOriginalFilename();
        // 飞书文件名特殊处理
        if (StringUtils.isNotBlank(filename) && filename.startsWith("ttfile://")) {
            filename = filename.substring(filename.lastIndexOf("/") + 1);
        }
        String name = newFileName(filename);
        String newName = newFileName(config, name);
        try (InputStream stream = file.getInputStream()) {
            QiNiuClient client = getClient();
            UploadManager uploadManager = client.getUploadManager();
            Response response = uploadManager.put(stream, newName, client.updateToken(getBucketName(config)), null, null);
            log.info("文件上传成功, 文件名:{}, 文件大小:{}, 响应信息:{}", filename, file.getSize(), response.bodyString());
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            String url = config.getConfigUrl() + name;
            return FileAssembler.convertToFileInfo(filename, name, file.getSize(), url, putRet.key, FileStorageTypeEnum.QINIU, config);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new NormalException("文件上传失败");
        }
    }

    /**
     * 文件下载
     *
     * @param response {@link HttpServletResponse} 响应
     * @param config   {@link FileObsConfig} 文件配置
     * @param info     {@link FileInfo} 文件信息
     */
    @Override
    public void downloadFile(HttpServletResponse response, FileObsConfig config, FileInfo info) {
        String url = buildLink(config, info, 3600L);
        try (InputStream input = new URL(url).openStream();
             OutputStream outputStream = response.getOutputStream()) {
            response.setHeader("content-type", "application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(info.getFileName(), "UTF-8"));
            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }
            outputStream.flush();
            log.info("obs文件下载成功回传！");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("obs下载失败");
        }
    }

    /**
     * 生成临时链接
     *
     * @param config     {@link FileObsConfig} 文件配置
     * @param info       {@link FileInfo} 文件信息
     * @param expireTime {@link Long} 过期时间
     * @return
     */
    @Override
    public String buildLink(FileObsConfig config, FileInfo info, long expireTime) {
        return getClient().getAuth().privateDownloadUrl(info.getFileUrl(), expireTime);
    }

    /**
     * 删除文件
     *
     * @param config {@link FileObsConfig} 文件配置
     * @param info   {@link FileInfo} 文件信息
     * @return
     */
    @Override
    public Boolean deleteFile(FileObsConfig config, FileInfo info) {
        BucketManager manager = getClient().getBucketManager();
        try {
            manager.delete(getBucketName(config), info.getFileKey());
        } catch (QiniuException e) {
            if (!(e.response != null && e.response.statusCode == 612)) {
                e.printStackTrace();
                log.error("obs删除文件失败！");
            }
        }
        return false;
    }
}
