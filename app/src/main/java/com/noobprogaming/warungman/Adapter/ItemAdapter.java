package com.noobprogaming.warungman.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.noobprogaming.warungman.Activity.ItemDetailActivity;
import com.noobprogaming.warungman.Model.ItemModel;
import com.noobprogaming.warungman.R;
import com.noobprogaming.warungman.Service.ConfigApi;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private ArrayList<ItemModel> dataList;
    public ItemAdapter(ArrayList<ItemModel> dataList) {
        this.dataList = dataList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        String gambarUrl = ConfigApi.URL_DATA_FILE + dataList.get(position).getItem_id();
        Glide.with(holder.itemView.getContext()).load(gambarUrl).into(holder.tvItemId);

        holder.tvName.setText(dataList.get(position).getName());
        holder.tvSellingPrice.setText(dataList.get(position).getSelling_price());

        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = v.getContext().getSharedPreferences(ConfigApi.TAG_TOKEN, Context.MODE_PRIVATE);
                String token = sp.getString(ConfigApi.TAG_TOKEN, null);

//               Toast.makeText(v.getContext(), token, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(), ItemDetailActivity.class);
                i.putExtra(ConfigApi.TAG_TOKEN, token);
                i.putExtra(ConfigApi.TAG_ITEM_ID, dataList.get(position).getItem_id());
                v.getContext().startActivity(i);
            }
        });

        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = v.getContext().getSharedPreferences(ConfigApi.TAG_TOKEN, Context.MODE_PRIVATE);
                String token = sp.getString(ConfigApi.TAG_TOKEN, null);
                Toast.makeText(v.getContext(), "Tambah - " + dataList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = v.getContext().getSharedPreferences(ConfigApi.TAG_TOKEN, Context.MODE_PRIVATE);
                String token = sp.getString(ConfigApi.TAG_TOKEN, null);
                Toast.makeText(v.getContext(), "Kurang - " + dataList.get(position).getName(), Toast.LENGTH_SHORT).show();
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
        private Button btnIncrease, btnDecrease;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvItemId = (ImageView) itemView.findViewById(R.id.ivItem);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            tvSellingPrice = (TextView) itemView.findViewById(R.id.tvSellingPrice);
            cvItem = (CardView) itemView.findViewById(R.id.cvItem);
            btnIncrease = (Button) itemView.findViewById(R.id.btnIncrease);
            btnDecrease = (Button) itemView.findViewById(R.id.btnDecrease);
        }
    }

}
