package com.noobprogaming.warungman;

<<<<<<< HEAD
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BaseApiService {
    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> LoginRequest (@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> RegisterRequest (@Field("nama") String nama, @Field("email") String email, @Field("password") String password);

}