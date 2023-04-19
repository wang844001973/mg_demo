package com.security.authentication.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class KeyStoreUtil {

    private static String JKSPATH;
    private static String JKSPASSWORD;
    private static String ALIAS;


//    public static final String SM2SIGN_WITH_SM3 = GMObjectIdentifiers.sm2sign_with_sm3.toString();
//
//
//    public static final String SIGALG_SHA1_WITH_ECDSA = "SHA1withECDSA";
//    public static final String SIGALG_SHA224_WITH_ECDSA = "SHA224withECDSA";
//    public static final String SIGALG_SHA256_WITH_ECDSA = "SHA256withECDSA";
//    public static final String SIGALG_SHA384_WITH_ECDSA = "SHA384withECDSA";
//    public static final String SIGALG_SHA512_WITH_ECDSA = "SHA512WithECDSA";
//
//
//
    public static final String SIGALG_SHA1_WITH_RSA = "sha1WithRSAEncryption";
    public static final String SIGALG_SHA224_WITH_RSA = "sha224WithRSAEncryption";
    public static final String SIGALG_SHA256_WITH_RSA = "sha256WithRSAEncryption";
    public static final String SIGALG_SHA384_WITH_RSA = "sha384WithRSAEncryption";
    public static final String SIGALG_SHA512_WITH_RSA = "sha512WithRSAEncryption";
    public static final Map<String,String> signAlgorithmMap = new HashMap();


    static {
        //添加BC实现
        Security.addProvider(new BouncyCastleProvider());
        signAlgorithmMap.put("SHA1WITHRSA", "sha1WithRSAEncryption");
        signAlgorithmMap.put("SHA224WITHRSA", "sha224WithRSAEncryption");
        signAlgorithmMap.put("SHA256WITHRSA", "sha256WithRSAEncryption");
        signAlgorithmMap.put("SHA384WITHRSA", "sha384WithRSAEncryption");
        signAlgorithmMap.put("SHA512WITHRSA", "sha512WithRSAEncryption");
    }

    protected static void init(String jksPath, String jksPassword, String alias){
        JKSPATH = jksPath;
        JKSPASSWORD = jksPassword;
        ALIAS = alias;
    }

    private static KeyStore getKeyStore(String jksPath, String jksPassword) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
        File file = new File(jksPath);
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(jksPath);
            keyStore.load(fis, jksPassword.toCharArray());
        } else {
            keyStore.load(null, null);
        }

        return keyStore;
    }

    protected static AuthSignResult p1Sign(String authCode)throws Exception{
        byte[] plainByte = authCode.getBytes();
        String digestalg = null;
        String signAlgorithm = null;
        PrivateKey privateKey = null;
        try {
            KeyStore keyStore = getKeyStore(JKSPATH,JKSPASSWORD);
            privateKey = (PrivateKey) keyStore.getKey(ALIAS, JKSPASSWORD.toCharArray());
            Certificate cert = keyStore.getCertificate(ALIAS);
            signAlgorithm = ((X509Certificate)cert).getSigAlgName();
            if(signAlgorithm.contains("SM3")){
                digestalg = GMObjectIdentifiers.sm2sign_with_sm3.toString();
                signAlgorithm = "SM3withSM2Encryption";
            }else{
                signAlgorithm = signAlgorithmMap.get(digestalg);
            }
        }catch (Exception e) {
            if(e.getMessage().contains("1.2.156.10197.1.301")){
                digestalg = "1.2.156.10197.1.301";
            }else{
                throw e;
            }
        }
        String algorithm = privateKey.getAlgorithm();
        if(signAlgorithm==null){
            throw  new Exception("不支持的算法。algorithm:"+algorithm+"   digestalg "+digestalg);
        }
        try {
            Signature sign = Signature.getInstance(digestalg, BouncyCastleProvider.PROVIDER_NAME);
            sign.initSign(privateKey);
            sign.update(plainByte);
            String signature = new String(Base64.encodeBase64(sign.sign()),"utf-8");
            return new AuthSignResult(authCode, signature, signAlgorithm);
        }catch (Exception e){
            throw new Exception("密码服务平台认证请求签名失败:"+e.getMessage());
        }
    }

}

class AuthSignResult{
    private String code;
    private String authCode;
    private String signature;
    private String signAlgorithm;

    public AuthSignResult(String authCode, String signature, String signAlgorithm) {
        this.authCode = authCode;
        this.signature = signature;
        this.signAlgorithm = signAlgorithm;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignAlgorithm() {
        return signAlgorithm;
    }

    public void setSignAlgorithm(String signAlgorithm) {
        this.signAlgorithm = signAlgorithm;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
