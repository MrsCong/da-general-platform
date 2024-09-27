package com.dgp.file.service;

import com.dgp.common.exception.NormalException;
import com.dgp.file.assembler.FileAssembler;
import com.dgp.file.config.FileObsConfig;
import com.dgp.file.config.ObsConfigProperties;
import com.dgp.file.dto.FileInfo;
import com.dgp.file.enums.FileStorageTypeEnum;
import com.obs.services.ObsClient;
import com.obs.services.model.HttpMethodEnum;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import com.obs.services.model.TemporarySignatureRequest;
import com.obs.services.model.TemporarySignatureResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Objects;

@Slf4j
@Component
public class HuaWeiStorageServiceImpl implements IStorageService {

    @Autowired
    private ObsConfigProperties obsConfigProperties;

    private ObsClient obsClient;

    /**
     * 上传文件
     *
     * @param file   {@link MultipartFile} 文件
     * @param config {@link FileObsConfig} 文件配置
     * @return
     */
    @Override
    public FileInfo uploadFile(MultipartFile file, FileObsConfig config) {
        PutObjectResult result;
        String filename = file.getOriginalFilename();
        // 飞书文件名特殊处理
        if (StringUtils.isNotBlank(filename) && filename.startsWith("ttfile://")) {
            filename = filename.substring(filename.lastIndexOf("/") + 1);
        }
        String name = newFileName(filename);
        try (InputStream stream = file.getInputStream()) {
            result = uploadObs(name, stream, config);
        } catch (IOException e) {
            log.error("上传文件失败", e);
            throw new NormalException("文件上传失败");
        }
        return FileAssembler.convertToFileInfo(filename, name, file.getSize(), result.getObjectUrl(), result.getObjectKey(), FileStorageTypeEnum.HUAWEI, config);
    }

    @Override
    public void downloadFile(HttpServletResponse response, FileObsConfig config, FileInfo info) {
        try (InputStream input = downloadObs(config, info).getObjectContent();
             OutputStream outputStream = response.getOutputStream()) {
            // 输出流设置
            response.setHeader("content-type", "application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(info.getFileName(), "UTF-8"));
            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }
            outputStream.flush();
            log.info("obs文件下载成功回传！");
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    @Override
    public String buildLink(FileObsConfig config, FileInfo info, long expireTime) {
        try (ObsClient obsClient = getClient()) {
            checkFile(config, info);
            TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.PUT, expireTime);
            request.setBucketName(getBucketName(config));
            request.setObjectKey(info.getFileKey());
            TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
            return response.getSignedUrl();
        } catch (Exception e) {
            log.error("obs生成临时链接失败！", e);
            throw new NormalException("obs生成临时链接失败！");
        }
    }

    @Override
    public Boolean deleteFile(FileObsConfig config, FileInfo info) {
        try (ObsClient obsClient = getClient()) {
            String bucketName = getBucketName(config);
            checkFolder(bucketName, getFilePath(config));
            if (obsClient.doesObjectExist(bucketName, info.getFileKey())) {
                obsClient.deleteObject(bucketName, info.getFileKey());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("obs删除文件失败！", e);
        }
        return false;
    }

    /**
     * 文件上传obs
     *
     * @param name
     * @param inputStream
     * @param config
     * @return
     */
    public PutObjectResult uploadObs(String name, InputStream inputStream, FileObsConfig config) {
        try (ObsClient obsClient = getClient()) {
            String bucketName = getBucketName(config);
            String newName = newFileName(config, name);
            PutObjectResult result = obsClient.putObject(bucketName, newName, inputStream);
            if (Objects.isNull(result)) {
                throw new NormalException("obs上传失败");
            }
            return result;
        } catch (Exception e) {
            log.error("obs上传失败", e);
            throw new NormalException("obs上传失败");
        }
    }

    /**
     * 文件下载
     *
     * @param config
     * @param info
     * @return
     */
    public ObsObject downloadObs(FileObsConfig config, FileInfo info) {
        try (ObsClient obsClient = getClient()) {
            return obsClient.getObject(getBucketName(config), info.getFileKey());
        } catch (Exception e) {
            log.error("obs下载失败！", e);
            throw new NormalException("obs下载失败！");
        }
    }

    /**
     * 获取obs客户端
     *
     * @return
     */
    private ObsClient getClient() {
        if (obsClient == null) {
            obsClient = new ObsClient(obsConfigProperties.getAccessKey(), obsConfigProperties.getSecretKey(), obsConfigProperties.getEndpoint());
        }
        return obsClient;
    }

    /**
     * 检验文件是否存在
     *
     * @param config
     * @param info
     */
    private void checkFile(FileObsConfig config, FileInfo info) {
        String bucketName = getBucketName(config);
        checkFolder(bucketName, getFilePath(config));
        if (!obsClient.doesObjectExist(bucketName, info.getFileKey())) {
            throw new NormalException("文件不存在");
        }

    }

    /**
     * 检验文件夹是否存在
     *
     * @param bucketName
     * @param path
     */
    private void checkFolder(String bucketName, String path) {
        ObsObject object = getClient().getObject(bucketName, path);
        if (Objects.isNull(object)) {
            throw new NormalException("存储文件夹不存在");
        }
    }
}
