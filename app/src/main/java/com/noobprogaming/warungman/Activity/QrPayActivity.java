package com.noobprogaming.warungman.Activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrPayActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    String token, qr_code, purchase_id, note, total_price;
    Context mContext;
    BaseApiService mApiService;

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        token = getIntent().getStringExtra(ConfigApi.TAG_TOKEN);
        purchase_id = getIntent().getStringExtra(ConfigApi.TAG_PURCHASE_ID);
        note = getIntent().getStringExtra(ConfigApi.TAG_NOTE);
        total_price = getIntent().getStringExtra(ConfigApi.TAG_TOTAL_PRICE);

        mContext = QrPayActivity.this;
        mApiService = ConfigApi.getAPIService();

        checkCameraPermission();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        qr_code = result.getText();
        storePaymentRequest();
//        MainActivity.tvAmount.setText(rawResult.getText());
//        onBackPressed();
    }

    private void checkCameraPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mScannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    private void storePaymentRequest() {
        mApiService.storePaymentRequest("Bearer " + token, qr_code, purchase_id, note, total_price)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonResult = new JSONObject(response.body().string());
                                JSONObject jsonSuccess = jsonResult.getJSONObject(ConfigApi.JSON_STATUS);
                                if (!jsonSuccess.isNull(ConfigApi.TAG_SUCCESS)) {
                                    String status = (String) jsonSuccess.get(ConfigApi.TAG_SUCCESS);
                                    Toast.makeText(mContext, status, Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    String status = (String) jsonSuccess.get(ConfigApi.TAG_ERROR);
                                    Toast.makeText(mContext, status, Toast.LENGTH_SHORT).show();
                                    mScannerView.resumeCameraPreview(QrPayActivity.this);
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
                        Toast.makeText(mContext, getString(R.string.badConnection), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
