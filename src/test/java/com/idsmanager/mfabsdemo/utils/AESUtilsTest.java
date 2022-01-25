package com.idsmanager.mfabsdemo.utils;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 2021/11/18 2:46 PM
 *
 * @author Shengzhao Li
 * @since 1.0.0
 */
class AESUtilsTest {


    @Test
    void aesEncrypt() throws Exception {
        String key = "mtNihhP7Q774HLKN";

        String encrypt = AESUtils.aesEncrypt("ABDDE", key);
        assertNotNull(encrypt);
        assertEquals("I6aUkSIUJVl1ylgR9r7BBQ==", encrypt);
//        System.out.println(encrypt);

    }


    @Test
    void aesDecrypt() throws Exception {
        String key = "mtNihhP7Q774HLKN";

        String decrypt = AESUtils.aesDecrypt("I6aUkSIUJVl1ylgR9r7BBQ==", key);
        assertNotNull(decrypt);
        assertEquals("ABDDE", decrypt);


    }

}