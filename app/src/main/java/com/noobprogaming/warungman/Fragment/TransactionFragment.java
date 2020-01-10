package com.noobprogaming.warungman.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.noobprogaming.warungman.Activity.CartListActivity;
import com.noobprogaming.warungman.R;
import com.noobprogaming.warungman.Service.BaseApiService;
import com.noobprogaming.warungman.Service.ConfigApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionFragment extends Fragment {

    ProgressDialog progressDialog;
    String token;
    View view;
    Context mContext;
    BaseApiService mApiService;
    SwipeRefreshLayout sdRefresh;
    ListView lvTransaction;

    public TransactionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transaction, container, false);

        token = getArguments().getString(ConfigApi.TAG_TOKEN);

        mContext = getActivity();
        mApiService = ConfigApi.getAPIService();

        lvTransaction = (ListView) view.findViewById(R.id.lvTransaction);
        lvTransaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent i = new Intent(mContext, CartListActivity.class);
                HashMap<String, String> map = (HashMap) adapterView.getItemAtPosition(position);
                i.putExtra(ConfigApi.TAG_TOKEN, token);
                i.putExtra(ConfigApi.TAG_PURCHASE_ID, map.get(ConfigApi.TAG_PURCHASE_ID).toString());
                startActivity(i);
            }
        });


        sdRefresh = (SwipeRefreshLayout) view.findViewById(R.id.sdRefresh);
        sdRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                transactionListRequest();
            }
        });

        loading();
        transactionListRequest();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void transactionListRequest() {
        mApiService.transactionListRequest("Bearer " + token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            progressDialog.dismiss();
                            sdRefresh.setRefreshing(false);
                            try {
                                ArrayList<HashMap<String, String>> listTransaction = new ArrayList<>();
                                JSONObject jsonResult = new JSONObject(response.body().string());
                                if (jsonResult.has(ConfigApi.JSON_CART_SELLER)) {
                                    JSONArray jsonData = jsonResult.getJSONArray(ConfigApi.JSON_CART_SELLER);
                                    for (int i = 0; i < jsonData.length(); i++) {
                                        JSONObject jData = jsonData.getJSONObject(i);
                                        String purchase_id = jData.getString(ConfigApi.TAG_PURCHASE_ID);
                                        String seller = jData.getString(ConfigApi.TAG_SELLER);
                                        String confirm_id = jData.getString(ConfigApi.TAG_CONFIRM_ID);
                                        String total_price = jData.getString(ConfigApi.TAG_TOTAL_PRICE);

                                        if (Integer.parseInt(confirm_id) == 1) {
                                            confirm_id = getString(R.string.confirm_1);
                                        } else if (Integer.parseInt(confirm_id) == 2) {
                                            confirm_id = getString(R.string.confirm_2);
                                        } else if (Integer.parseInt(confirm_id) == 3) {
                                            confirm_id = getString(R.string.confirm_3);
                                        } else if (Integer.parseInt(confirm_id) == 4) {
                                            confirm_id = getString(R.string.confirm_4);
                                        } else if (Integer.parseInt(confirm_id) == 5) {
                                            confirm_id = getString(R.string.confirm_5);
                                        } else if (Integer.parseInt(confirm_id) == 6) {
                                            confirm_id = getString(R.string.confirm_6);
                                        }

                                        if(total_price == "null") {
                                            total_price = "0";
                                        }

                                        HashMap<String, String> transaction = new HashMap<>();
                                        transaction.put(ConfigApi.TAG_PURCHASE_ID, purchase_id);
                                        transaction.put(ConfigApi.TAG_SELLER, seller);
                                        transaction.put(ConfigApi.TAG_CONFIRM_ID, confirm_id);
                                        transaction.put(ConfigApi.TAG_TOTAL_PRICE, "Rp" + total_price);
                                        listTransaction.add(transaction);
                                    }
                                } else if (jsonResult.has(ConfigApi.JSON_ERROR)) {
                                    Toast.makeText(mContext, getString(R.string.error), Toast.LENGTH_SHORT).show();
                                }
                                ListAdapter adapter = new SimpleAdapter(
                                        mContext, listTransaction, R.layout.list_transaction,
                                        new String[]{ConfigApi.TAG_SELLER, ConfigApi.TAG_CONFIRM_ID, ConfigApi.TAG_TOTAL_PRICE},
                                        new int[]{R.id.tvSeller, R.id.tvConfirm, R.id.tvTotalPrice});
                                lvTransaction.setAdapter(adapter);
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
                        progressDialog.dismiss();
                        Toast.makeText(mContext, getString(R.string.badConnection), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loading() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
    }

}
