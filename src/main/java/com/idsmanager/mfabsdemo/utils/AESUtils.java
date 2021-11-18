/*
 * Copyright (c) 2016 BeiJing JZYT Technology Co. Ltd
 * www.idsmanager.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * BeiJing JZYT Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with BeiJing JZYT Technology Co. Ltd.
 */
package com.idsmanager.mfabsdemo.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @since 1.0.0
 */
public abstract class AESUtils {

    private static final String ENCODING = "UTF-8";

    private static final String AES = "AES";

    private static final String AES_PADDING = "AES/ECB/PKCS5Padding";

    /**
     * 使用AES/ECB/PKCS5Padding加密实现
     *
     * @since 1.0.0
     */
    public static String aesEncrypt(String str, String key) throws Exception {
        if (str == null || key == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance(AES_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(ENCODING), AES));
        byte[] bytes = cipher.doFinal(str.getBytes(ENCODING));
        return new BASE64Encoder().encode(bytes);
    }

    /**
     * 使用AES/ECB/PKCS5Padding解密实现
     *
     * @since 1.0.0
     */
    public static String aesDecrypt(String str, String key) throws Exception {
        if (str == null || key == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance(AES_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(ENCODING), AES));
        byte[] bytes = new BASE64Decoder().decodeBuffer(str);
        bytes = cipher.doFinal(bytes);
        return new String(bytes, ENCODING);
    }
}
