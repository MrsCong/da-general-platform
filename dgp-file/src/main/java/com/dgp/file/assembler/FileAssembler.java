package com.dgp.file.assembler;

import com.dgp.file.config.FileObsConfig;
import com.dgp.file.dto.FileInfo;
import com.dgp.file.enums.FileEnum;
import com.dgp.file.enums.FileStorageTypeEnum;

public class FileAssembler {

    /**
     * 私有构造方法
     */
    private FileAssembler() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 转换为文件信息
     *
     * @param filename {@link String} 文件名
     * @param name     {@link String} 文件名
     * @param filesize {@link Long} 文件大小
     * @param url      {@link String} 文件url
     * @param key      {@link String} 文件key
     * @param config   {@link FileObsConfig} 文件配置
     * @return {@link FileInfo} 文件信息
     */
    public static FileInfo convertToFileInfo(String filename, String name, Long filesize, String url, String key, FileStorageTypeEnum typeEnum, FileObsConfig config) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(filename);
        fileInfo.setFileSize(getFileSize(filesize));
        String suffixName = filename.substring(filename.lastIndexOf(".") + 1);
        fileInfo.setFileSuffix(suffixName);
        fileInfo.setFileType(FileEnum.getFileTypeBySuffix(suffixName));
        fileInfo.setStorageType(typeEnum.getType());
        fileInfo.setSaveName(name);
        fileInfo.setFileUrl(url);
        fileInfo.setFileKey(key);
        fileInfo.setConfigCode(config.getConfigCode());
        return fileInfo;
    }

    /**
     * 获取文件大小
     *
     * @param size {@link Long} 文件大小
     * @return {@link String} 文件大小
     */
    @SuppressWarnings("all")
    private static String getFileSize(long size) {
        String fileSize;
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            fileSize = size + "B";
        } else {
            size = size / 1024;
            //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
            if (size < 1024) {
                fileSize = size + "KB";
            } else {
                size = size / 1024;
                if (size < 1024) {
                    //因为如果以MB为单位的话，要保留最后1位小数，因此，把此数乘以100之后再取余
                    size = size * 100;
                    fileSize = size / 100 + "." + size % 100 + "MB";
                } else {
                    //否则如果要以GB为单位的，先除于1024再作同样的处理
                    size = size * 100 / 1024;
                    fileSize = size / 100 + "." + size % 100 + "GB";
                }
            }
        }
        return fileSize;
    }

}
