package com.noobprogaming.warungman.Activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.noobprogaming.warungman.R;
import com.noobprogaming.warungman.Service.ConfigApi;

public class TopupActivity extends AppCompatActivity {
    String user_id;
    TextView tvVirtualAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        tvVirtualAccount = (TextView) findViewById(R.id.tvVirtualAccount);

        user_id = getIntent().getStringExtra(ConfigApi.TAG_USER_ID);
        tvVirtualAccount.setText("538 000 000" + user_id);
    }

}
