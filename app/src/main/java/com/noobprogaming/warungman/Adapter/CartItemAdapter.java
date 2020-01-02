package com.noobprogaming.warungman.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.noobprogaming.warungman.Activity.ItemDetailActivity;
import com.noobprogaming.warungman.Model.CartItemModel;
import com.noobprogaming.warungman.R;
import com.noobprogaming.warungman.Service.BaseApiService;
import com.noobprogaming.warungman.Service.ConfigApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ItemViewHolder> {

    private ArrayList<CartItemModel> dataList;
    public CartItemAdapter(ArrayList<CartItemModel> dataList) {
        this.dataList = dataList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_cart_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final String seller_id = dataList.get(position).getSeller_id();
        final String name = dataList.get(position).getName();
        final String item_id = dataList.get(position).getItem_id();
        final String amount = dataList.get(position).getAmount();
        final String stock = dataList.get(position).getStock();
        final String selling_price = dataList.get(position).getSelling_price();
        final ItemViewHolder finalholder = holder;

        String gambarUrl = ConfigApi.URL_DATA_FILE + item_id;
        Glide.with(holder.itemView.getContext()).load(gambarUrl).into(holder.tvItemId);

        holder.tvName.setText(name);
        holder.tvSellingPrice.setText("Rp" + selling_price);

        int mAmount = Integer.parseInt(amount);

        if (mAmount >= 1) {
            holder.llAdd.setVisibility(View.INVISIBLE);
            holder.llAmount.setVisibility(View.VISIBLE);
            finalholder.tvAmount.setText(Integer.toString(mAmount));
        } else {
            holder.llAmount.setVisibility(View.INVISIBLE);
            holder.llAdd.setVisibility(View.VISIBLE);
        }

        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = v.getContext().getSharedPreferences(ConfigApi.TAG_TOKEN, Context.MODE_PRIVATE);
                String token = sp.getString(ConfigApi.TAG_TOKEN, null);

//               Toast.makeText(v.getContext(), token, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(), ItemDetailActivity.class);
                i.putExtra(ConfigApi.TAG_TOKEN, token);
                i.putExtra(ConfigApi.TAG_ITEM_ID, item_id);
                v.getContext().startActivity(i);
            }
        });

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = v.getContext().getSharedPreferences(ConfigApi.TAG_TOKEN, Context.MODE_PRIVATE);
                String token = sp.getString(ConfigApi.TAG_TOKEN, null);

                String amountText = finalholder.tvAmount.getText().toString();
                int mAmount = Integer.parseInt(amountText);

                finalholder.llAmount.setVisibility(View.VISIBLE);
                finalholder.llAdd.setVisibility(View.INVISIBLE);

                mAmount++;
                if (mAmount <= Integer.parseInt(stock)) {
                    finalholder.tvAmount.setText(Integer.toString(mAmount));
                    finalholder.btnDecrease.setEnabled(true);
                } else {
                    mAmount = Integer.parseInt(stock);
                    finalholder.tvAmount.setText(Integer.toString(mAmount));
                    finalholder.btnIncrease.setEnabled(false);
                }
                finalholder.tvAmount.setText(Integer.toString(mAmount));

                storeCartRequest(token, seller_id, item_id, "1");

                Toast.makeText(v.getContext(), "Berhasil menambahkan " + dataList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = v.getContext().getSharedPreferences(ConfigApi.TAG_TOKEN, Context.MODE_PRIVATE);
                String token = sp.getString(ConfigApi.TAG_TOKEN, null);

                String amountText = finalholder.tvAmount.getText().toString();
                int mAmount = Integer.parseInt(amountText);

                finalholder.llAmount.setVisibility(View.VISIBLE);
                finalholder.llAdd.setVisibility(View.INVISIBLE);

                mAmount++;
                if (mAmount <= Integer.parseInt(stock)) {
                    finalholder.tvAmount.setText(Integer.toString(mAmount));
                    finalholder.btnDecrease.setEnabled(true);
                } else {
                    mAmount = Integer.parseInt(stock);
                    finalholder.tvAmount.setText(Integer.toString(mAmount));
                    finalholder.btnIncrease.setEnabled(false);
                }
                finalholder.tvAmount.setText(Integer.toString(mAmount));

                storeCartRequest(token, seller_id, item_id, "1");

                Toast.makeText(v.getContext(), "Berhasil menambahkan " + dataList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = v.getContext().getSharedPreferences(ConfigApi.TAG_TOKEN, Context.MODE_PRIVATE);
                String token = sp.getString(ConfigApi.TAG_TOKEN, null);

                String amountText = finalholder.tvAmount.getText().toString();
                int mAmount = Integer.parseInt(amountText);

                mAmount--;
                if (mAmount >= 1) {
                    finalholder.tvAmount.setText(Integer.toString(mAmount));
                    finalholder.btnIncrease.setEnabled(true);
                } else {
                    mAmount = 0;
                    finalholder.tvAmount.setText(Integer.toString(mAmount));
                    finalholder.btnDecrease.setEnabled(false);
                    finalholder.llAmount.setVisibility(View.INVISIBLE);
                    finalholder.llAdd.setVisibility(View.VISIBLE);
                }

                storeCartRequest(token, seller_id, item_id, "-1");

                Toast.makeText(v.getContext(), "Berhasil mengurangi " + dataList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView tvItemId;
        private TextView tvName, tvAmount, tvSellingPrice;
        private CardView cvItem;
        private Button btnAdd, btnIncrease, btnDecrease;
        private LinearLayout llAdd, llAmount;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvItemId = (ImageView) itemView.findViewById(R.id.ivItem);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            tvSellingPrice = (TextView) itemView.findViewById(R.id.tvSellingPrice);
            cvItem = (CardView) itemView.findViewById(R.id.cvItem);
            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
            btnIncrease = (Button) itemView.findViewById(R.id.btnIncrease);
            btnDecrease = (Button) itemView.findViewById(R.id.btnDecrease);
            llAdd = (LinearLayout) itemView.findViewById(R.id.llAdd);
            llAmount = (LinearLayout) itemView.findViewById(R.id.llAmount);
        }
    }

    //    Context mContext;
    BaseApiService mApiService;

    public void storeCartRequest(String token, String seller_id, String item_id, String amount) {
        mApiService = ConfigApi.getAPIService();
        mApiService.storeCartRequest("Bearer " + token, seller_id, item_id, amount)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonResult = new JSONObject(response.body().string());
                                JSONObject jsonSuccess = jsonResult.getJSONObject(ConfigApi.JSON_STATUS);
                                if (!jsonSuccess.isNull(ConfigApi.TAG_SUCCESS)) {
                                    String status = (String) jsonSuccess.get(ConfigApi.TAG_SUCCESS);
//                                    Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
                                } else {
//                                    loading();
//                                    itemDetailRequest();
                                    String status = (String) jsonSuccess.get(ConfigApi.TAG_ERROR);
//                                    Toast.makeText(mContext, status, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
//                                Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
//                                Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
//                            progressDialog.dismiss();
//                            Toast.makeText(mContext, getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Toast.makeText(mContext, getString(R.string.badConnection), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
