package com.rainng.jerry.mouse.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlEncoding {
    public static String Encode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);

    }


    public static String Decode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);

    }
}