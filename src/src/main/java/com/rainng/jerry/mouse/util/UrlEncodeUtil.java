package com.rainng.jerry.mouse.util;

import com.rainng.jerry.util.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlEncodeUtil {
    private UrlEncodeUtil() {

    }

    public static String encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.ex("Unsupported encoding", ex);
            return s;
        }
    }


    public static String decode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.ex("Unsupported encoding", ex);
            return s;
        }
    }
}