package com.noobprogaming.warungman.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.noobprogaming.warungman.Adapter.ItemAdapter;
import com.noobprogaming.warungman.Model.CartItemModel;
import com.noobprogaming.warungman.Model.ItemModel;
import com.noobprogaming.warungman.R;
import com.noobprogaming.warungman.Service.BaseApiService;
import com.noobprogaming.warungman.Service.ConfigApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class HomeFragment extends Fragment {

    ProgressDialog progressDialog;
    String token;
    View view;
    Context mContext;
    BaseApiService mApiService;


    private RecyclerView rvItem;
    private ItemAdapter adapter;
    private ArrayList<ItemModel> itemArrayList;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        token = getArguments().getString(ConfigApi.TAG_TOKEN);

        mContext = getActivity();
        mApiService = ConfigApi.getAPIService();

        rvItem = (RecyclerView) view.findViewById(R.id.rvItem);
        adapter = new ItemAdapter(itemArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvItem.setLayoutManager(layoutManager);
        rvItem.setAdapter(adapter);

        final SwipeRefreshLayout sdRefresh = (SwipeRefreshLayout) view.findViewById(R.id.sdRefresh);
        sdRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemListRequest();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sdRefresh.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        loading();
        itemListRequest();

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void itemListRequest() {
        mApiService.itemListRequest("Bearer " + token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            progressDialog.dismiss();
                            try {
                                itemArrayList = new ArrayList<>();
                                JSONObject jsonResult = new JSONObject(response.body().string());
                                if (jsonResult.has(ConfigApi.JSON_ITEM)) {
                                    JSONArray jsonData = jsonResult.getJSONArray(ConfigApi.JSON_ITEM);
                                    for (int i = 0; i < jsonData.length(); i++) {
                                        JSONObject jData = jsonData.getJSONObject(i);
                                        String item_id = jData.getString(ConfigApi.TAG_ITEM_ID);
                                        String name = jData.getString(ConfigApi.TAG_NAME);
                                        String selling_price = jData.getString(ConfigApi.TAG_SELLING_PRICE);

                                        itemArrayList.add(new ItemModel(item_id, name, selling_price));
                                    }
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
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loading() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Memuat data");
        progressDialog.show();
    }

}