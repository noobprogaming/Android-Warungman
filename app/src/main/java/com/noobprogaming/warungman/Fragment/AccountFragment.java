package com.noobprogaming.warungman.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.noobprogaming.warungman.LoginActivity;
import com.noobprogaming.warungman.R;
import com.noobprogaming.warungman.Service.BaseApiService;
import com.noobprogaming.warungman.Service.ConfigApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {

    String token;
    Context mContext;
    BaseApiService mApiService;
    SwipeRefreshLayout sdRefresh;
    TextView tvName, tvPhoneNumber;
    Button btnLogout;
    View view;

    public AccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);

        token = getArguments().getString(ConfigApi.TAG_TOKEN);

        mContext = getActivity();
        mApiService = ConfigApi.getAPIService();

        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLogout();
            }
        });

        tvName = (TextView) view.findViewById(R.id.tvName);
        tvPhoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumber);

        tvName.setText(getArguments().getString(ConfigApi.TAG_NAME));
        tvPhoneNumber.setText(getArguments().getString(ConfigApi.TAG_PHONE_NUMBER));

        sdRefresh = (SwipeRefreshLayout) view.findViewById(R.id.sdRefresh);
        sdRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                detailAccountRequest();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setLogout() {
        SharedPreferences sp = getActivity().getSharedPreferences(ConfigApi.KEY_LOGIN_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed=sp.edit();
        ed.putString(ConfigApi.KEY_USERNAME, null);
        ed.putString(ConfigApi.KEY_PASSWORD, null);
        ed.commit();

        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);

        Toast.makeText(getActivity(), getString(R.string.logoutNotification), Toast.LENGTH_SHORT).show();

        getActivity().finish();
    }

    private void detailAccountRequest() {
        mApiService.detailAccountRequest("Bearer " + token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            progressDialog.dismiss();
                            sdRefresh.setRefreshing(false);
                            try {
                                JSONObject jsonResult = new JSONObject(response.body().string());
                                if (jsonResult.has(ConfigApi.JSON_DETAIL_ACCOUNT)) {
                                    JSONArray jsonData = jsonResult.getJSONArray(ConfigApi.JSON_DETAIL_ACCOUNT);
                                    JSONObject jData = jsonData.getJSONObject(0);

//                                    String user_id = jData.getString(ConfigApi.TAG_USER_ID);
//                                    String name = jData.getString(ConfigApi.TAG_NAME);
//                                    String email = jData.getString(ConfigApi.TAG_EMAIL);
//                                    String phone_number = jData.getString(ConfigApi.TAG_PHONE_NUMBER);
//                                    String balance = jData.getString(ConfigApi.TAG_BALANCE);
//                                    String created_at = jData.getString(ConfigApi.TAG_CREATED_AT);

//                                    user_id.setText(jData.getString(ConfigApi.TAG_USER_ID));
                                    tvName.setText(jData.getString(ConfigApi.TAG_NAME));
//                                    email.setText(jData.getString(ConfigApi.TAG_EMAIL));
                                    tvPhoneNumber.setText(jData.getString(ConfigApi.TAG_PHONE_NUMBER));
//                                    tvBalance.setText("Rp" + jData.getString(ConfigApi.TAG_BALANCE));
//                                    created_at.setText(jData.getString(ConfigApi.TAG_CREATED_AT));

                                } else if (jsonResult.has(ConfigApi.JSON_ERROR)) {
                                    Toast.makeText(mContext, getString(R.string.error), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        progressDialog.dismiss();
                        Toast.makeText(mContext, getString(R.string.badConnection), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
