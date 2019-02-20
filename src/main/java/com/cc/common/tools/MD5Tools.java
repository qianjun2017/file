package com.cc.common.tools;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5加密
 * Created by yuanwenshu on 2018/8/28.
 */
public class MD5Tools {

    /**
     * md5加密
     * @param content 待加密内容
     * @return
     */
    public static String encrypt(String content){
        if (StringTools.isNullOrNone(content)) {
            return null;
        }
        return DigestUtils.md5Hex(content);
    }

    /**
     * md5校验
     * @param content 待校验内容
     * @param md5 MD5值
     * @return
     */
    public static Boolean check(String content, String md5){
        if (StringTools.isAnyNullOrNone(new String[]{content, md5})) {
            return false;
        }
        return md5.equals(MD5Tools.encrypt(content));
    }
}
