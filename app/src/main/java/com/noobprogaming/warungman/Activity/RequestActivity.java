package com.noobprogaming.warungman.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.noobprogaming.warungman.R;
import com.noobprogaming.warungman.Service.BaseApiService;
import com.noobprogaming.warungman.Service.ConfigApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RequestActivity extends AppCompatActivity {

    String token, user_id;
    Context mContext;
    BaseApiService mApiService;
    Button btnRequest;
    EditText etRequestAmount;
    TextView tvRequestAmount;
    ImageView ivQrCode;
    CardView cvQrHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        token = getIntent().getStringExtra(ConfigApi.TAG_TOKEN);
        user_id = getIntent().getStringExtra(ConfigApi.TAG_USER_ID);

        mContext = RequestActivity.this;
        mApiService = ConfigApi.getAPIService();

        etRequestAmount = (EditText) findViewById(R.id.etRequestAmount);
        tvRequestAmount = (TextView) findViewById(R.id.tvRequestAmount);
        cvQrHolder = (CardView) findViewById(R.id.cvQrHolder);
        ivQrCode = (ImageView) findViewById(R.id.ivQrCode);

        btnRequest = (Button) findViewById(R.id.btnRequest);
        btnRequest.setEnabled(false);
        cvQrHolder.setVisibility(View.INVISIBLE);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = user_id + "&&" + etRequestAmount.getText().toString();
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,480,480);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    ivQrCode.setImageBitmap(bitmap);
                    tvRequestAmount.setText("Rp" + etRequestAmount.getText().toString());
                    cvQrHolder.setVisibility(View.VISIBLE);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        typingDelay();
    }

    private void typingDelay() {

        etRequestAmount.addTextChangedListener(new TextWatcher() {
                                                   @Override
                                                   public void beforeTextChanged(CharSequence s, int start, int count,
                                                                                 int after) {
                                                   }

                                                   @Override
                                                   public void onTextChanged(final CharSequence s, int start, int before,
                                                                             int count) {

                                                   }

                                                   @Override
                                                   public void afterTextChanged(final Editable s) {
                                                       if (s.length() > 0) {
                                                           btnRequest.setEnabled(true);
                                                       } else {
                                                           btnRequest.setEnabled(false);
                                                       }
                                                   }
                                               }

        );

    }

}
