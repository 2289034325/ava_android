package com.acxca.ava.service;

public class ServiceConsts {
    public static final String API_BASE_URL = "https://api.acxca.com/";
    public static final String API_AUTH_KAPTCHA = API_BASE_URL+"kaptcha/";
    public static final String API_AUTH_LOGIN = API_BASE_URL+"auth/login/%s/%s";
    public static final String API_DIC_STAT_LIST = API_BASE_URL+"app/dictionary/word/stat";
    public static final String API_READ_SEARCH_WORD = API_BASE_URL+"app/dictionary/word/search?lang=%s&form=%s";
    public static final String API_READ_BOOKMARK_LIST = API_BASE_URL+"app/reading/bookmark/list";
    public static final String API_READ_BOOKMARK_SAVE_MODIFY = API_BASE_URL+"app/reading/bookmark";
    public static final String API_READ_BOOKMARK_DELETE = API_BASE_URL+"app/reading/bookmark/%s";
    public static final String API_SPEECH_LIST = API_BASE_URL+"app/speech/article/list";
    public static final String API_WRITING_LIST = API_BASE_URL+"app/writing/article/list";



    public static final String API_HEADER_CONTENT_TYPE_LABEL = "Content-Type";
    public static final String API_HEADER_CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";
    public static final String API_HEADER_AUTHORIZATION_TYPE_LABEL = "Authorization";

    public static final String SB_KEY_TOKEN = "SB_KEY_TOKEN";
}
