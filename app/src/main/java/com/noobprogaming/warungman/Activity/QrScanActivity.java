package com.noobprogaming.warungman.Activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.noobprogaming.warungman.R;
import com.noobprogaming.warungman.Service.BaseApiService;
import com.noobprogaming.warungman.Service.ConfigApi;

import static android.provider.ContactsContract.CommonDataKinds.Note.NOTE;

public class QrScanActivity extends AppCompatActivity {
    String token, qr_code, purchase_id, note, total_price;
    Context mContext;
    BaseApiService mApiService;
    ImageView ivBgContent;
    CodeScanner mCodeScanner;
    CodeScannerView scannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        token =getIntent().getStringExtra(ConfigApi.TAG_TOKEN);
        purchase_id = getIntent().getStringExtra(ConfigApi.TAG_PURCHASE_ID);
        note = getIntent().getStringExtra(ConfigApi.TAG_NOTE);
        total_price = getIntent().getStringExtra(ConfigApi.TAG_TOTAL_PRICE);

        mContext= QrScanActivity.this;
        mApiService =ConfigApi.getAPIService();
        ivBgContent = findViewById(R.id.ivBgContent);
        scannerView = findViewById(R.id.scannerView);


        ivBgContent.bringToFront();

        mCodeScanner = new CodeScanner(mContext, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                qr_code = result.getText();
//             showAlertDialog(message);
//             Toast.makeText(mContext, qr_code, Toast.LENGTH_LONG.show();

                storePaymentRequest();
                mCodeScanner.startPreview();
            }

            checkCameraPermision();
        });

        @Override
        protected void onResume(){
            super.onResume();
            checkCameraPermision();
        }

        private void checkCameraPermision() {
            Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse response) {
                    mCodeScanner.startPreview();
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse response) {

                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            }).check();
        }

        private void showAlertDialog(String message) {
            AlertDialog.Builder builder = AlertDialog.Builder();
            builder.setMessage(message);
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "SCAN LAGI",
                    (dialog,id) - {
                        dialog.cancel();
                        mCodeScanner.startPreview();
            });

            builder.setNegativeButton(
                    "CANCEL", dialog.id(); 
            )

        }
    }
}
