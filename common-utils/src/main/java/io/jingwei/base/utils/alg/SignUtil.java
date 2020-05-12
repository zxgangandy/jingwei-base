package io.jingwei.base.utils.alg;

import com.google.common.collect.Maps;
import io.jingwei.base.utils.string.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;


@Slf4j
public class SignUtil {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String PUBLIC_KEY = "RSAPublicKey";
    public static final String PRIVATE_KEY = "RSAPrivateKey";



    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.decodeBase64(key);
    }

    public static String encryptBASE64(byte[] key) throws Exception {
        return Base64.encodeBase64String(key);
    }

    public static Map<String, String> genKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, String> keyMap = new HashMap<String, String>(2);
        keyMap.put(PUBLIC_KEY, encryptBASE64(publicKey.getEncoded()));
        keyMap.put(PRIVATE_KEY, encryptBASE64(privateKey.getEncoded()));

        return keyMap;
    }


    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuilder content = new StringBuilder();
        List<String> keys = new ArrayList<>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (String key : keys) {
            String value = sortedParams.get(key);
            if (StringUtils.areNotEmpty(key, value)) {
                content.append(index == 0 ? "" : "&").append(key).append("=").append(value);
                index++;
            }
        }
        return content.toString();
    }


    public static String sign(String content, String privateKey) {
        try {
            //解密私钥
            byte[] keyBytes = decryptBASE64(privateKey);
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(keyBytes);
            //指定加密算法
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            //取私钥匙对象
            PrivateKey privateKey2 = keyFactory.generatePrivate(priPKCS8);
            //用私钥对信息生成数字签名
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey2);
            signature.update(content.getBytes("utf-8"));

            return encryptBASE64(signature.sign()).replaceAll("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        try {
            //解密公钥
            byte[] keyBytes = decryptBASE64(publicKey);
            //构造X509EncodedKeySpec对象
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            //指定加密算法
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            //取公钥匙对象
            PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey2);
            signature.update(data.getBytes("utf-8"));

            //验证签名是否正常
            return signature.verify(decryptBASE64(sign));
        } catch (Exception e) {
            log.warn("verify fail, data:{}, publicKey:{}, sign:{}", data, publicKey, sign);
            return false;
        }

    }


    public static void main(String[] args) throws Exception {
        Map map = genKey();
        System.out.println("public key:");
        System.out.println(map.get(PUBLIC_KEY));
        System.out.println("privatee key:");
        System.out.println(map.get(PRIVATE_KEY));

        HashMap dataMap = Maps.newHashMap();
        dataMap.put("currency", "USDT");

        HashMap finalDataMap = Maps.newHashMap();
        finalDataMap.put("lang", "ZH-cn");
        finalDataMap.put("data", dataMap);


        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIsY70ocv89XLWXKAh3aOgvznCKd2Hv/hZ9ul54Ho43BRL9GS5F813+OQHka2YQjjfquMsG5dQsBBsAEW0ZGGtVcVvGNpBe80OpSG/Cidj/8dXRIYgS7zL1uh2aMPWs/RW9+TBbRPkgrD/Bi3Asu8tGiLBH0t6zJf3tjlcp+nm7hAgMBAAECgYAtBm8+0DuOhCufzVoOC7vKbprV4b5XtQit6QCPGO3qTutP9xMzDvQ6x/M6wGQ0j8W/pW+sobmvTC/BLCfXgY3hkVIfSrnKV3KEd70dZVPqPr8Gnjnd5glLA6gTefd92T5O7zaH8Npj763fEo3DmOcYbrOD/SK77VQ7al3EuUO3WQJBAO1lNja41a02bhB5LaDdopuFwVWsgPQoX1k4GQEmuMMLyXb1u3b9CrqP3g3LvfqzA03K6wDI8veJFtMGR7d5EXsCQQCV/5oemVS5/Hl6T1A2/bixBmrwMXJOqbyoKHrqps8+zLmhzsz80wGC6uiATbL/XHAoQucVcoOpE2sS7HEM5AxTAkAjppl3qlZchtoMm0Yg7GfpFzMrnJZZFF8NVQFcTOZeVDzTIqAehG3CCqz8QWasaYnLevfDzGnAhtPoqP7ILORVAkEAlGDE1zH8EwLCNBBTHCK92Tes/y8akN/tBDcO6CHcbpXbp0lHk20zWIsBNTInN938AIe1yYx+2vifeZe0ePj5iwJANZbNCorhU26N/99cMzVrbyzwuDKs9+JUGTvdBoL1oPzYfRb/7RBgZQFkzyTDfIdCG10Uefj7BR6uJM0ZF+Pr1g==";
        String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCIevROWLiMhI3MGJCr60+bpU1m8xPcrqFGvIQobGeYFYbFSpkb8ul4iJZnGmw+WbvY0fMb0gFissTtvyeQuYy6Vg/wPADejHtPEwgX7LxmM4g1KA2ZP2KyvZ6xxTudvtmEeyonmnyTrFQTAJhXsvTHpYFlX9ED8A+yWpEGTbJpwIDAQAB";
        String sign = "EAkXTYtLBNySf/OE42WIIk+u07N/E+k5TTmwybSsKtnt0xaMTSpdb3dyCD2fFRAY4C9ZKWIrE12GPtP6zLbBc2frtIWX3bwWcUDd3UD09iCXtFtowmS7f2X0oFnBEGpVbfTk5VTSNZrLh5mbPYs25vDGu0agiEsgbIMyVfNSHEY=";
        System.out.println("sign is " + sign);
        System.out.println("checkSign = [" + verify("helloworld", pubKey, sign) + "]");
    }


}
