package com.noobprogaming.warungman;

public class ConfigApi {

    public static final String URL_API = "http://192.168.43.254:8000/api/";
    public static final String URL_DATA_FILE = "http://192.168.43.254:8000/data_file/";

    public static final String JSON_SUCCESS = "success";
    public static final String JSON_DATA = "data";
    public static final String JSON_ERROR = "error";

    public static final String TAG_TOKEN = "token";
    public static final String TAG_NAME = "name";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_PHONE_NUMBER = "phone_number";
    public static final String TAG_BALANCE = "balance";


    public static BaseApiService getAPIService() {
        return RetrofitClient.getClient(URL_API).create(BaseApiService.class);
    }

}
