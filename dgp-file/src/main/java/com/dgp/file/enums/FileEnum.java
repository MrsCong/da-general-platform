package com.dgp.file.enums;


import com.dgp.common.exception.NormalException;

public enum FileEnum {

    /**
     * 图片类型
     */
    IMAGE_JPG(1, "jpg"),

    IMAGE_PNG(1, "png"),

    IMAGE_JPEG(1, "jpeg"),

    /**
     * word文档
     */
    WORD_DOC(2, "doc"),

    WORD_DOCX(2, "docx"),

    /**
     * Excel表格
     */
    EXCEL_XLS(3, "xls"),

    EXCEL_XLSX(3, "xlsx"),

    /**
     * PDF
     */
    PDF(4, "pdf"),

    /**
     * 音频
     */
    AUDIO(5, "mp3"),

    /**
     * 视频
     */
    VIDEO(6, "mp4"),

    /**
     * ppt
     */
    PPT(7, "pptx"),


    /**
     * wav
     */
    AUDIO_WAV(8, "wav"),

    /**
     * json
     */
    JSON(9, "json"),

    /**
     * gif
     */
    GIF(10, "gif"),

    ;

    /**
     * 文件类型
     */
    private Integer fileType;

    /**
     * 文件后缀名
     */
    private String suffixName;

    public Integer getFileType() {
        return this.fileType;
    }

    public static Integer getFileTypeBySuffix(String suffixName) {
        FileEnum[] values = FileEnum.values();
        for (FileEnum value : values) {
            if (value.suffixName.equals(suffixName)) {
                return value.getFileType();
            }
        }
        throw new NormalException("文件类型未匹配");
    }

    FileEnum(Integer fileType, String suffixName) {
        this.fileType = fileType;
        this.suffixName = suffixName;
    }
}
