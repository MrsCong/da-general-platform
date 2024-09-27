package com.dgp.file.service;

import com.dgp.common.exception.BaseException;
import com.dgp.file.config.FileObsConfig;
import com.dgp.file.dto.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public interface IStorageService {

    /**
     * 文件上传
     *
     * @param file   {@link MultipartFile} 文件
     * @param config {@link FileObsConfig} 文件配置
     * @return {@link FileInfo} 文件信息
     */
    FileInfo uploadFile(MultipartFile file, FileObsConfig config);

    /**
     * 文件下载
     *
     * @param response {@link HttpServletResponse} 响应
     * @param config   {@link FileObsConfig} 文件配置
     * @param info     {@link FileInfo} 文件信息
     */
    void downloadFile(HttpServletResponse response, FileObsConfig config, FileInfo info);

    /**
     * 生成临时链接
     *
     * @param config     {@link FileObsConfig} 文件配置
     * @param info       {@link FileInfo} 文件信息
     * @param expireTime {@link Long} 过期时间
     * @return {@link String} 文件链接
     */
    String buildLink(FileObsConfig config, FileInfo info, long expireTime);

    /**
     * 删除文件
     *
     * @param config {@link FileObsConfig} 文件配置
     * @param info   {@link FileInfo} 文件信息
     * @return {@link Boolean} 是否删除成功
     */
    Boolean deleteFile(FileObsConfig config, FileInfo info);

    /**
     * 获取桶名称
     *
     * @param config {@link FileObsConfig} 文件配置
     * @return {@link String} 桶名称
     */
    default String getBucketName(FileObsConfig config) {
        int index = config.getConfigPath().indexOf("/");
        if (index == -1) {
            throw new BaseException("获取桶配置异常");
        }
        return config.getConfigPath().substring(0, index);
    }

    /**
     * 获取文件路径
     *
     * @param config {@link FileObsConfig} 文件配置
     * @return {@link String} 文件路径
     */
    default String getFilePath(FileObsConfig config) {
        int index = config.getConfigPath().indexOf("/");
        if (index == -1) {
            throw new BaseException("获取文件路径异常");
        }
        return config.getConfigPath().substring(index + 1);
    }

    /**
     * 获取文件名 - 带路径
     *
     * @param config   {@link FileObsConfig} 文件配置
     * @param filename {@link String} 文件名称
     * @return {@link String} 文件路径
     */
    default String newFileName(FileObsConfig config, String filename) {
        return getFilePath(config) + filename;
    }

    /**
     * 获取文件名称 - 不带路径
     *
     * @param filename {@link String} 文件名称
     * @return {@link String} 文件路径
     */
    default String newFileName(String filename) {
        String fileSuffix = filename.substring(filename.lastIndexOf("."));
        String uuIdStr = UUID.randomUUID().toString().replaceAll("-", "");
        return uuIdStr + fileSuffix;
    }

}
