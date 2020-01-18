package com.acxca.ava.service;

public class ServiceConsts {
    public static final String API_BASE_URL = "http://10.0.2.2:9000/";

    public static final String API_AUTH_KAPTCHA = API_BASE_URL+"kaptcha/";

    public static final String API_AUTH_LOGIN = API_BASE_URL+"%s/auth/login/%s/%s";

    public static final String API_DIC_STAT_LIST = API_BASE_URL+"app/dictionary/word/stat";
}
