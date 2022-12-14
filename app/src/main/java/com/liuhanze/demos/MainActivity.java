package com.liuhanze.demos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Base64InputStream;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.liuhanze.demos.bean.Sheep;
import com.liuhanze.iutil.data.ISharedPreferences;
import com.liuhanze.iutil.file.IFile;
import com.liuhanze.iutil.lang.IByte;
import com.liuhanze.iutil.lang.IString;
import com.liuhanze.iutil.log.ILog;
import com.liuhanze.iutil.net.INetWork;
import com.liuhanze.iutil.resource.IResource;
import com.liuhanze.iutil.security.IBase64;
import com.liuhanze.iutil.security.IDES;
import com.liuhanze.iutil.security.IRSA;
import com.liuhanze.iutil.security.ISHA;
import com.liuhanze.iutil.toast.IToast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            //test1();
           // test4();
           // test3();
            test5();

        } catch (Exception e) {
            e.printStackTrace();
            ILog.LogDebug("rsa????????????...");
        }
    }

    private void test5(){
        String key = "11111111111111112222222222222222";
        String data = "6DC1B51373A8EFD92C38647C92BF1A9D"; //D31BA2A154537297F357D6CB0C19045A
        String DES_PADDING = "DESede/ECB/NoPadding";
        //String DES_PADDING = "DES/ECB/NoPadding";
        String code = IDES.encrypt3DES2HexString(IByte.hexStringToBytes(data), IByte.hexStringToBytes(key),DES_PADDING,null);

        ILog.LogDebug("?????? code = "+code);
        String uncode = IDES.decrypt3DES2HexString(IByte.hexStringToBytes(code),IByte.hexStringToBytes(key),DES_PADDING,null);
        ILog.LogDebug("?????? code = "+uncode);
    }

    public void test3(){
        String key = "F4702CBFCE2A89C129452A0DD6C23E46";
        String tunnel = "0lMQHU9feG4oL6AyANTV2Kuv8LyM/eDd4XTY40/mujFPM3/DHFjndcOPD5TarlBsGlqfDj7IS9/4idssM3D1ZNu2ho1j2ngu7e7kquIoc2o=";
        getTunnel(tunnel,key);
    }

    public static String getTunnel(String json, String key){
        ILog.LogDebug("tunnel = "+json);
        byte[] base64Code = IBase64.decode(json);
        ILog.LogDebug("key = "+key);
        String key1 = key.substring(0,16);
        ILog.LogDebug("key1 = "+key1);
        String key2 = key.substring(16,32);
        ILog.LogDebug("key2 = "+key2);
        byte[] key1Byte = IByte.hexStringToByteArray(key1);
        byte[] key2Byte = IByte.hexStringToByteArray(key2);

        byte[] des1 = IDES.decryptDES(base64Code,key1Byte,"DES/ECB/PKCS5Padding",null);
        byte[] des2 = IDES.encryptDES(des1,key2Byte,"DES/ECB/PKCS5Padding",null);
        byte[] des3 = IDES.decryptDES(des2,key1Byte,"DES/ECB/PKCS5Padding",null);
        String data = new String(des3);
        ILog.LogDebug("data json = "+data);
        return data;
    }

    public String test2(){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            keyGenerator.init(64);
            SecretKey secretKey = keyGenerator.generateKey();
            DESKeySpec deSedeKeySpec = new DESKeySpec(secretKey.getEncoded());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
            Key key =  secretKeyFactory.generateSecret(deSedeKeySpec);
            String keyData = IByte.bytes2HexString(key.getEncoded());

            ILog.LogDebug(IString.concat("publickey data = ",keyData));
            return  keyData;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        }

    }

    public void test1(){
        Sheep sheep = new Sheep();
        sheep.setAge(12);
        sheep.setName("??????");
        ISharedPreferences.putObject("huahua",sheep);

        ILog.LogDebug("????????????");
        IToast.shortToast("?????????");
        ILog.LogDebug("??????????????????");
        Sheep huahua = ISharedPreferences.getObject("huahua",Sheep.class);
        ILog.LogDebug(IString.concat("???????????? = ",huahua.getAge(),"???????????? = ",huahua.getName()));

        ISharedPreferences.put("one","?????? ");
        ISharedPreferences.put("two","123");
        String hh  = ISharedPreferences.get("one","default");
        int a = ISharedPreferences.get("aa",1);
        ILog.LogDebug(IString.concat("one text",hh,a));
        ISharedPreferences.get("two","123");

    }

    public void test4(){
        InputStream publicKeyIn = IResource.openAssetsFile("Newpos_pubkey.pem");
        IBase64.decode(IResource.getFileFromAssets("Newpos_pubkey.pem"),"US-ASCII");

        try {
            PublicKey publicKey = IRSA.loadPublicKey(publicKeyIn);
            HashMap<String,String> map = new HashMap<>();
            map.put("k1","value1");
            map.put("k2","value2");
            map.put("k3","value3");
            String gosnS = new Gson().toJson(map);
            String data = IString.concat(gosnS, IFile.readCertificateKeyInputStream(publicKeyIn));
            byte[] signByte = ISHA.encryptSHA256(data.getBytes());
            ILog.LogDebug(IString.concat("sign data= ",IByte.bytes2HexString(signByte)));

            byte[]  encodeData = IRSA.encryptData(signByte,publicKey,"RSA/ECB/PKCS1Padding");
            String  afterEncode = IBase64.encode(encodeData);
            ILog.LogDebug(IString.concat("sign data encode = ",afterEncode));

        } catch (Exception e) {
            e.printStackTrace();
            ILog.LogError(" RSAUtils.loadPublicKey(publicKeyIn) error");
        }
    }
}