package com.dgp.file.client;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.Data;

@Data
public class QiNiuClient {

    /**
     * 访问码
     */
    private String accessKey;
    /**
     * 密钥
     */
    private String secretKey;
    /**
     * 认证
     */
    private Auth auth;
    /**
     * 上传管理器
     */
    private UploadManager uploadManager;

    /**
     * 桶管理器
     */
    private BucketManager bucketManager;

    /**
     * 构建七牛云客户端
     *
     * @param accessKey
     * @param secretKey
     */
    public QiNiuClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 获取认证
     *
     * @return
     */
    public Auth getAuth() {
        if (auth == null) {
            auth = Auth.create(accessKey, secretKey);
        }
        return auth;
    }

    /**
     * 获取桶管理器
     *
     * @return
     */
    public BucketManager getBucketManager() {
        if (bucketManager == null) {
            bucketManager = new BucketManager(getAuth(), new Configuration(Region.autoRegion()));
        }
        return bucketManager;
    }

    /**
     * 获取上传管理器
     * @return
     */
    public UploadManager getUploadManager() {
        if (uploadManager == null) {
            uploadManager = new UploadManager(new Configuration(Region.region2()));
        }
        return uploadManager;
    }

    /**
     * 获取上传凭证
     * @param bucketName
     * @return
     */
    public String updateToken(String bucketName) {
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize),\"ext\":$(ext),\"fprefix\":$(fprefix)}");
        long expireSeconds = 3600;
        return getAuth().uploadToken(bucketName, null, expireSeconds, putPolicy);
    }
}
