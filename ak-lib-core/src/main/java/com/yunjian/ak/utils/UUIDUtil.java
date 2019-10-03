package com.yunjian.ak.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/22 11:20
 * @Version 1.0
 */
public class UUIDUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(UUIDUtil.class);
    private static final Base64 BASE64 = new Base64(true);

    public UUIDUtil() {
    }

    public static final String createUUID() {
        UUID uuid = UUID.randomUUID();
        byte[] uuidArr = asByteArray(uuid);

        try {
            return (new String(BASE64.encode(uuidArr))).trim();
        } catch (Exception e) {
            LOGGER.error("为uuid编码时发生异常，原UUID信息：" + uuid.toString(), e);
            return uuid.toString().trim();
        }
    }

    private static byte[] asByteArray(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] buffer = new byte[16];

        int i;
        for(i = 0; i < 8; ++i) {
            buffer[i] = (byte)((int)(msb >>> 8 * (7 - i)));
        }

        for(i = 8; i < 16; ++i) {
            buffer[i] = (byte)((int)(lsb >>> 8 * (7 - i)));
        }

        return buffer;
    }
}

