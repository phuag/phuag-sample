package com.phuag.sample.common.security;

import org.apache.commons.lang3.Validate;

import java.security.SecureRandom;

/**
 * Created by Administrator on 2016/10/13.
 */
public class Salt {

    private static final SecureRandom random = new SecureRandom();
    /**
     * 生成随机的Byte[]作为salt.
     *
     * @param numBytes byte数组的大小
     */
    public static byte[] generateSalt(int numBytes) {
        Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);

        byte[] bytes = new byte[numBytes];
        random.nextBytes(bytes);
        return bytes;
    }
}
