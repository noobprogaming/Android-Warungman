package com.noobprogaming.warungman.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.noobprogaming.warungman.Fragment.AccountFragment;
import com.noobprogaming.warungman.Fragment.HomeFragment;
import com.noobprogaming.warungman.Fragment.TransactionFragment;
import com.noobprogaming.warungman.Fragment.WalletFragment;
import com.noobprogaming.warungman.R;
import com.noobprogaming.warungman.Service.ConfigApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    String token, user_id, name, email, phone_number, balance;

    AccountFragment aboutFragment;
    HomeFragment homeFragment;
    TransactionFragment transactionFragment;
    WalletFragment walletFragment;

    BottomNavigationView bnvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        token = getIntent().getStringExtra(ConfigApi.TAG_TOKEN);
        user_id = getIntent().getStringExtra(ConfigApi.TAG_USER_ID);
        name = getIntent().getStringExtra(ConfigApi.TAG_NAME);
        email = getIntent().getStringExtra(ConfigApi.TAG_EMAIL);
        phone_number = getIntent().getStringExtra(ConfigApi.TAG_PHONE_NUMBER);
        balance = getIntent().getStringExtra(ConfigApi.TAG_BALANCE);

        SharedPreferences sp = MainActivity.this.getSharedPreferences(ConfigApi.TAG_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed=sp.edit();
        ed.putString(ConfigApi.TAG_TOKEN, token);
        ed.commit();

//        Toast.makeText(MainActivity.this, "Selamat datang, " + name + "!", Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString(ConfigApi.TAG_TOKEN, token);
        bundle.putString(ConfigApi.TAG_USER_ID, user_id);
        bundle.putString(ConfigApi.TAG_NAME, name);
        bundle.putString(ConfigApi.TAG_EMAIL, email);
        bundle.putString(ConfigApi.TAG_PHONE_NUMBER, phone_number);
        bundle.putString(ConfigApi.TAG_BALANCE, balance);
//        bundle.putString(ConfigApi.TAG_, );

        aboutFragment = new AccountFragment();
        homeFragment = new HomeFragment();
        transactionFragment = new TransactionFragment();
        walletFragment = new WalletFragment();

        aboutFragment.setArguments(bundle);
        homeFragment.setArguments(bundle);
        walletFragment.setArguments(bundle);
        transactionFragment.setArguments(bundle);

        bnvMain = findViewById(R.id.bnvMain);

        changeFragment(homeFragment);

        bnvMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuAccount:
                        changeFragment(aboutFragment);
                        break;
                    case R.id.menuCenter:
                        Intent i = new Intent(MainActivity.this, QrPayActivity.class);
                        i.putExtra(ConfigApi.TAG_TOKEN, token);
                        i.putExtra(ConfigApi.TAG_USER_ID, user_id);
                        startActivity(i);
                        break;
                    case R.id.menuHome:
                        changeFragment(homeFragment);
                        break;
                    case R.id.menuTransaction:
                        changeFragment(transactionFragment);
                        break;
                    case R.id.menuWallet:
                        changeFragment(walletFragment);
                        break;
                }
                return true;
            }
        });
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameMain, fragment).commit();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(MainActivity.this, getString(R.string.exitConfirm), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                System.exit(0);
            }
        }, 2000);
    }
}
