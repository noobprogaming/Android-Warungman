package com.noobprogaming.warungman;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    String token;

    EditText etEmail, etPassword;
    Button btnLogin;
    Context mContex;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiry_login);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

    mContex = this;
    mApiService = ConfigApi.getAPIService();
//        initComponents();

        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLogin();
            }
        })



    }

    private void requestLogin() {
        mApiService.LoginRequest(etEmail.getText().toString(),etPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            pdLoading.dismiss();
                            try {
                                JSONObject jsonResult = new JSONObject(response.body().string());
                                if (jsonResult.has(ConfigApi.JSON_SUCCESS)) {
                                    JSONObject jsonSuccess = jsonResult.getJSONObject(ConfigApi.JSON_SUCCESS);
                                    token = (String) jsonSuccess.get(ConfigApi.TAG_TOKEN);

                                    JSONArray jsonData = jsonResult.getJSONArray(ConfigApi.JSON_DATA);
                                    JSONObject jData =  jsonData.getJSONObject(0);
                                    String name = jData.getString(ConfigApi.TAG_NAME);
                                    String email = jData.getString(ConfigApi.TAG_EMAIL);
                                    String phone_number = jData.getString(ConfigApi.TAG_PHONE_NUMBER);
                                    String balance = jData.getString(ConfigApi.TAG_BALANCE);

                                    Intent i = new Intent(mContex, MainActivity.class);
                                    i.putExtra(ConfigApi.TAG_TOKEN, token);
                                    i.putExtra(ConfigApi.TAG_NAME, name);
                                    i.putExtra(ConfigApi.TAG_EMAIL, email);
                                    i.putExtra(ConfigApi.TAG_PHONE_NUMBER, phone_number);
                                    i.putExtra(ConfigApi.TAG_BALANCE, balance);
                                    startActivity(i);

                                }else if (jsonResult.has(ConfigApi.JSON_ERROR)) {
                                    Toast.makeText(mContex, "Error", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
                    }
                });
    }
}
