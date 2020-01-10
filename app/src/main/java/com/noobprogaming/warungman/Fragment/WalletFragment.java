package com.noobprogaming.warungman.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.noobprogaming.warungman.Activity.QrPayActivity;
import com.noobprogaming.warungman.Activity.RequestActivity;
import com.noobprogaming.warungman.Activity.TopupActivity;
import com.noobprogaming.warungman.Activity.TransferActivity;
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


public class WalletFragment extends Fragment {

    String token, user_id, name, balance;
    Context mContext;
    BaseApiService mApiService;
    SwipeRefreshLayout sdRefresh;
    TextView tvName, tvBalance;
    LinearLayout llBayar, llTransfer, llTopUp, llRequest;
    View view;

    public WalletFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wallet, container, false);

        token = getArguments().getString(ConfigApi.TAG_TOKEN);
        user_id = getArguments().getString(ConfigApi.TAG_USER_ID);
        name = getArguments().getString(ConfigApi.TAG_NAME);
        balance = getArguments().getString(ConfigApi.TAG_BALANCE);

        mContext = getActivity();
        mApiService = ConfigApi.getAPIService();

        llBayar = (LinearLayout) view.findViewById(R.id.llBayar);
        llTransfer = (LinearLayout) view.findViewById(R.id.llTransfer);
        llTopUp = (LinearLayout) view.findViewById(R.id.llTopUp);
        llRequest = (LinearLayout) view.findViewById(R.id.llRequest);

        tvName = (TextView) view.findViewById(R.id.tvName);
        tvBalance = (TextView) view.findViewById(R.id.tvBalance);

        tvName.setText("Hai, " + name);
        tvBalance.setText("Rp" + balance);

        sdRefresh = (SwipeRefreshLayout) view.findViewById(R.id.sdRefresh);
        sdRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                detailAccountRequest();
            }
        });

        llBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), QrPayActivity.class);
                i.putExtra(ConfigApi.TAG_TOKEN, token);
                i.putExtra(ConfigApi.TAG_USER_ID, user_id);
                startActivity(i);
            }
        });

        llTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TransferActivity.class);
                i.putExtra(ConfigApi.TAG_TOKEN, token);
                i.putExtra(ConfigApi.TAG_BALANCE, balance);
                i.putExtra(ConfigApi.TAG_USER_ID, user_id);
                startActivity(i);
            }
        });

        llTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TopupActivity.class);
                i.putExtra(ConfigApi.TAG_TOKEN, token);
                i.putExtra(ConfigApi.TAG_USER_ID, user_id);
                startActivity(i);
            }
        });

        llRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), RequestActivity.class);
                i.putExtra(ConfigApi.TAG_TOKEN, token);
                i.putExtra(ConfigApi.TAG_USER_ID, user_id);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                                    name = jData.getString(ConfigApi.TAG_NAME);
//                                    String email = jData.getString(ConfigApi.TAG_EMAIL);
//                                    String phone_number = jData.getString(ConfigApi.TAG_PHONE_NUMBER);
                                    balance = jData.getString(ConfigApi.TAG_BALANCE);
//                                    String created_at = jData.getString(ConfigApi.TAG_CREATED_AT);

//                                    user_id.setText(jData.getString(ConfigApi.TAG_USER_ID));
                                    tvName.setText("Hai, " + name);
//                                    email.setText(jData.getString(ConfigApi.TAG_EMAIL));
//                                    phone_number.setText(jData.getString(ConfigApi.TAG_PHONE_NUMBER));
                                    tvBalance.setText("Rp" + balance);
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
