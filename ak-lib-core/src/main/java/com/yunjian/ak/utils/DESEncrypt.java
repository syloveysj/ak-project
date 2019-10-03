package com.yunjian.ak.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/22 11:18
 * @Version 1.0
 */
public class DESEncrypt {
    private Key key;
    private byte[] byteMi = null;
    private byte[] byteMing = null;
    private String strMi = "";
    private String strM = "";

    public DESEncrypt() {
    }

    public void setKey(String strKey) {
        try {
            KeyGenerator _generator = KeyGenerator.getInstance("DES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strKey.getBytes());
            _generator.init(secureRandom);
            this.key = _generator.generateKey();
            _generator = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setEncString(String strMing) {
        BASE64Encoder base64en = new BASE64Encoder();

        try {
            this.byteMing = strMing.getBytes("UTF8");
            this.byteMi = this.getEncCode(this.byteMing);
            this.strMi = base64en.encode(this.byteMi);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.byteMing = null;
            this.byteMi = null;
        }

    }

    public byte[] getEncCode(byte[] byteS) {
        byte[] byteFina = null;

        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(1, this.key);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cipher = null;
        }

        return byteFina;
    }

    public void setDesString(String strMi) {
        BASE64Decoder base64De = new BASE64Decoder();

        try {
            this.byteMi = base64De.decodeBuffer(strMi);
            this.byteMing = this.getDesCode(this.byteMi);
            this.strM = new String(this.byteMing, "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            base64De = null;
            this.byteMing = null;
            this.byteMi = null;
        }

    }

    public byte[] getDesCode(byte[] byteD) {
        byte[] byteFina = null;

        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(2, this.key);
            byteFina = cipher.doFinal(byteD);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cipher = null;
        }

        return byteFina;
    }

    public String getStrMi() {
        return this.strMi;
    }

    public String getStrM() {
        return this.strM;
    }
}
