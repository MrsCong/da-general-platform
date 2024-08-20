package com.dgp.common.utils;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * IO操作帮助类
 */
@Slf4j
public class IOUtils {

    /**
     * 通过输入流获取流中的字符串
     *
     * @throws IOException
     */
    public static String getStrByInputStream(InputStream inputStream) throws IOException {
        log.info("正在通过流获取数据！");
        StringBuilder str = new StringBuilder();
        byte[] buff = new byte[1000];
        int len;
        while ((len = inputStream.read(buff)) != -1) {
            str.append(new String(buff, 0, len, StandardCharsets.UTF_8));
        }
        log.info("通过流获取数据结束！");
        return str.toString();
    }

    /**
     * base64转pdf
     *
     * @param base64Content base64字符串
     * @param filePath      输出文件路径
     */
    public static void base64StringToPdf(String base64Content, String filePath) {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            byte[] bytes = Base64.decode(base64Content);//base64编码内容转换为字节数组
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            bis = new BufferedInputStream(byteInputStream);
            File file = new File(filePath);
            File path = file.getParentFile();
            if (!path.exists()) {
                path.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while (length != -1) {
                bos.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从网络Url中下载文件
     *
     * @throws IOException
     */
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath)
            throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        fos.close();
        inputStream.close();
        System.out.println("info:" + url + " download success");
    }

    /**
     * 从输入流中获取字节数组
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * inputStream转化为file流
     */
    public static File InputStreamToFile(InputStream inputStream, String fileName) {
        FileOutputStream fileOutputStream = null;
        try {
            String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
            File file = File.createTempFile("tmp", "." + suffixName);
            fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            return file;
        } catch (IOException e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("流转换文件流关闭失败");
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    log.error("流转换文件流关闭失败");
                }
            }
        }
    }

}
