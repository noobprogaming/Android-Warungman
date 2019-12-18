package com.noobprogaming.warungman.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.noobprogaming.warungman.R;
import com.noobprogaming.warungman.Service.BaseApiService;
import com.noobprogaming.warungman.Service.ConfigApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class CartListActivity extends AppCompatActivity {

    String token, confirm_id, purchase_id, note, total_price;
    Context mContext;
    BaseApiService mApiService;
    Button btnOrder, btnDelete;
    TextView tvTotalPrice;
    EditText etNote;
    ListView lvCartItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        token = getIntent().getStringExtra(ConfigApi.TAG_TOKEN);
        purchase_id = getIntent().getStringExtra(ConfigApi.TAG_PURCHASE_ID);

        mContext = CartListActivity.this;
        mApiService = ConfigApi.getAPIService();

        lvCartItem = (ListView) findViewById(R.id.lvCartItem);
        btnDelete = (Button) findViewById(R.id.lvCartItem);
        etNote = (EditText) findViewById(R.id.etNote);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);

        final LayoutInflater LayoutInflater = getLayoutInflater();
        final View v = LayoutInflater.inflate(R.layout.list_cart_item, null);
        btnDelete = v.findViewById(R.id.btndelete);
        btnOrder = (Button) findViewById(R.id.btnOrder);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnOrder.setEnabled(false);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(confirm_id) == 1) {
                    orderNow();
                } else if (Integer.parseInt(confirm_id) == 2) {
//                    cancelOrderAlertDialog();
                } else if (Integer.parseInt(confirm_id) == 3) {

                } else if (Integer.parseInt(confirm_id) == 4) {

                } else if (Integer.parseInt(confirm_id) == 5) {

                }
            }
        });

    }

    private void orderNow() {
        note = etNote.getText().toString();

        Intent i = new Intent(mContext, QrScanActivity.class);
        i.putExtra(ConfigApi.TAG_TOKEN, token);
        i.putExtra(ConfigApi.TAG_PURCHASE_ID, purchase_id);
        i.putExtra(ConfigApi.TAG_NOTE, note;
        i.putExtra(ConfigApi.TAG_TOTAL_PRICE, total_price);
        startActivity(i);
    }

}
