package com.dgp.common.security.sm;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.digests.SM3Digest;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class SM3Utils {

    public static final String param_signature = "signature";

    public static final String EQUAL = "=";

    public static final String AMPERSAND = "&";

    /**
     * encode By SM3
     *
     * @param data source data
     * @return JSONObject
     */
    public static JSONObject Sm3Sign(JSONObject data) {
        if (data == null) {
            return null;
        }
        String result = filterBlank(data);
        String resultHaStr = "";
        try {
            byte[] srcData = result.getBytes("UTF-8");
            byte[] resultHa = hash(srcData);
            resultHaStr = ByteUtils.byteToHex(resultHa);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data.put(param_signature, resultHaStr.toUpperCase());
        return data;
    }

    public static boolean Sm3Valign(JSONObject data) {
        if (data == null) {
            return false;
        }
        String signature = data.getString(param_signature);
        String result = filterBlank(data);
        String resultHaStr = "";
        try {
            byte[] srcData = result.getBytes("UTF-8");
            byte[] resultHa = hash(srcData);
            resultHaStr = ByteUtils.byteToHex(resultHa);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signature.equals(resultHaStr.toUpperCase());
    }

    private static byte[] hash(byte[] srcData) {
        SM3Digest digest = new SM3Digest();
        digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    /**
     * 剔除空字符
     *
     * @param contentData source
     * @return 剔除空字符后的字符串
     */
    @SuppressWarnings("unchecked")
    public static String filterBlank(JSONObject contentData) {
        JSONObject submitFromData = new JSONObject();
        Set<String> keySet = contentData.keySet();

        for (String key : keySet) {
            String value = contentData.getString(key);
            if (StringUtils.isEmpty(value)) {
                // 对value值进行去除前后空处理
                submitFromData.put(key, value.trim());
            }
        }
        String data = coverMap2String(submitFromData);
        return data;
    }

    /**
     * 签名字段排序
     *
     * @param data 数据
     * @return 签名字段排序
     */
    @SuppressWarnings("unchecked")
    public static String coverMap2String(JSONObject data) {
        // 将JSONObject信息转换成key1=value1&key2=value2的形式
        TreeMap<String, Object> tree = new TreeMap<>();
        Iterator<Entry<String, Object>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> en = it.next();
            if (param_signature.equals(en.getKey().trim())) {
                continue;
            }
            tree.put(en.getKey(), en.getValue().toString());
        }
        it = tree.entrySet().iterator();
        StringBuilder sf = new StringBuilder();
        while (it.hasNext()) {
            Entry<String, Object> en = it.next();
            sf.append(en.getKey()).append(EQUAL).append(en.getValue()).append(AMPERSAND);
        }
        return sf.substring(0, sf.length() - 1);
    }

    public static void main(String[] args) throws IOException {
        String pubKey = "041DB0D82281421B3631F81948DB7FD0E7E19E5234CDAF30885B58AC1C2263D2EA0701377E23C26F8D2CE1985D3D21767366AEA3F68CC765E967504360174C19F6";
        String pri = "5E6F47E2502108274DAB10A3C97E1CB1101BABEB771E7B4373A2FD3A92348423";
        JSONObject data1 = new JSONObject();
        String phone = "18279661176";
        String enPhone = SM2Utils.encrypt(pubKey, phone);
        data1.put("phone", enPhone);
        data1.put("abc", "测试顺序");
        data1.put("hn", "");
        data1.put("name", "国康");
        JSONObject sign = SM3Utils.Sm3Sign(data1);
        System.out.println("数据签名结果:" + sign);
        System.out.println("验签结果:" + SM3Utils.Sm3Valign(sign));
        System.out.println("数据加密结果:" + enPhone);
        System.out.println("数据解密结果:" + SM2Utils.decrypt(pri, enPhone));
    }

}
