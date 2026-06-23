package com.mtons.mblog.base.utils;

import com.mtons.mblog.base.lang.MtonsException;
import org.apache.commons.lang3.StringUtils;

/**
 * 密码强度校验
 */
public final class PasswordValidator {
    private PasswordValidator() {
    }

    public static void validate(String password) {
        if (StringUtils.length(password) <= 6) {
            throw new MtonsException("密码长度必须大于6位");
        }
    }
}
