package com.noobprogaming.warungman.Activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.noobprogaming.warungman.R;
import com.noobprogaming.warungman.Service.BaseApiService;

import androidx.appcompat.app.AppCompatActivity;

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
    }
}
