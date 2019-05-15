package com.rainng.jerry.mouse.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlEncoding {
    public static String Encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");

        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return s;
        }
    }


    public static String Decode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");

        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return s;
        }
    }
}