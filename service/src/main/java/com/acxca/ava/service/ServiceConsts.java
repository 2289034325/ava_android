package com.acxca.ava.service;

public class ServiceConsts {
    public static final String API_BASE_URL = "http://10.0.2.2:9000/";
    public static final String API_AUTH_KAPTCHA = API_BASE_URL+"kaptcha/";
    public static final String API_AUTH_LOGIN = API_BASE_URL+"auth/login/%s/%s";
    public static final String API_DIC_STAT_LIST = API_BASE_URL+"app/dictionary/word/stat";


    public static final String API_HEADER_CONTENT_TYPE_LABEL = "Content-Type";
    public static final String API_HEADER_CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";
    public static final String API_HEADER_AUTHORIZATION_TYPE_LABEL = "Authorization";

    public static final String SB_KEY_TOKEN = "SB_KEY_TOKEN";
}
