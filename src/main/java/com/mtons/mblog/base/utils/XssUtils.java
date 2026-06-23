package com.mtons.mblog.base.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.regex.Pattern;

/**
 * XSS 输入过滤
 */
public final class XssUtils {
    private static final Pattern SCRIPT_PATTERN = Pattern.compile(
            "<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern EVENT_HANDLER_PATTERN = Pattern.compile(
            "on[a-zA-Z]+\\s*=", Pattern.CASE_INSENSITIVE);
    private static final Pattern JS_PROTOCOL_PATTERN = Pattern.compile(
            "javascript\\s*:", Pattern.CASE_INSENSITIVE);

    private XssUtils() {
    }

    public static String clean(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        String cleaned = value;
        cleaned = SCRIPT_PATTERN.matcher(cleaned).replaceAll("");
        cleaned = EVENT_HANDLER_PATTERN.matcher(cleaned).replaceAll("");
        cleaned = JS_PROTOCOL_PATTERN.matcher(cleaned).replaceAll("");
        return HtmlUtils.htmlEscape(cleaned);
    }
}
