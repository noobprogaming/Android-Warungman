package com.noobprogaming.warungman.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.noobprogaming.warungman.R;
import com.noobprogaming.warungman.Service.BaseApiService;
import com.noobprogaming.warungman.Service.ConfigApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferActivity extends AppCompatActivity {

    String token, balance, password;
    Context mContext;
    BaseApiService mApiService;
    Button btnTransfer;
    TextView tvBalance, tvReceiverName, tvReceiverPhoneNumber;
    EditText etReceiverId, etTransferAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        token = getIntent().getStringExtra(ConfigApi.TAG_TOKEN);
        balance = getIntent().getStringExtra(ConfigApi.TAG_BALANCE);

        mContext = TransferActivity.this;
        mApiService = ConfigApi.getAPIService();

        tvBalance = (TextView) findViewById(R.id.tvBalance);
        tvReceiverName = (TextView) findViewById(R.id.tvReceiverName);
        tvReceiverPhoneNumber = (TextView) findViewById(R.id.tvReceiverPhoneNumber);
        etReceiverId = (EditText) findViewById(R.id.etReceiverId);
        etTransferAmount = (EditText) findViewById(R.id.etTransferAmount);

        tvBalance.setText("Rp" + balance);

        btnTransfer = (Button) findViewById(R.id.btnTransfer);
        btnTransfer.setEnabled(false);
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPassword();
            }
        });

        typingDelay();
    }

    @Override
    protected void onResume() {
        super.onResume();

        detailAccountRequest();
    }

    private void detailAccountRequest() {
        mApiService.detailAccountRequest("Bearer " + token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            progressDialog.dismiss();
                            try {
                                JSONObject jsonResult = new JSONObject(response.body().string());
                                if (jsonResult.has(ConfigApi.JSON_DETAIL_ACCOUNT)) {
                                    JSONArray jsonData = jsonResult.getJSONArray(ConfigApi.JSON_DETAIL_ACCOUNT);
                                    JSONObject jData = jsonData.getJSONObject(0);

                                    balance = jData.getString(ConfigApi.TAG_BALANCE);
                                    tvBalance.setText("Rp" + balance);

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
//                        progressDialog.dismiss();
                        Toast.makeText(mContext, getString(R.string.badConnection), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void inputPassword() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Masukkan Password");
//        alertDialog.setMessage("Enter Password");

        final EditText inputPassword = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        inputPassword.setLayoutParams(lp);
        inputPassword.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        alertDialog.setView(inputPassword);
//        alertDialog.setIcon(R.drawable.key);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        password = inputPassword.getText().toString();
                        storeTransferRequest();
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void storeTransferRequest() {
        mApiService.storeTransferRequest("Bearer " + token, etReceiverId.getText().toString(), etTransferAmount.getText().toString(), password)
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
                                } else {
                                    String status = (String) jsonSuccess.get(ConfigApi.TAG_ERROR);
                                    Toast.makeText(mContext, status, Toast.LENGTH_SHORT).show();
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

    private void checkAccountRequest() {
        mApiService.checkAccountRequest("Bearer " + token, etReceiverId.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            progressDialog.dismiss();
                            try {
                                JSONObject jsonResult = new JSONObject(response.body().string());
                                if (jsonResult.has(ConfigApi.JSON_DETAIL_ACCOUNT)) {
                                    JSONArray jsonData = jsonResult.getJSONArray(ConfigApi.JSON_DETAIL_ACCOUNT);
                                    JSONObject jData = jsonData.getJSONObject(0);

                                    tvReceiverName.setTextColor(getResources().getColor(R.color.colorAccent));
                                    tvReceiverName.setText(jData.getString(ConfigApi.TAG_NAME));
                                    tvReceiverPhoneNumber.setText(jData.getString(ConfigApi.TAG_PHONE_NUMBER));

                                } else if (jsonResult.has(ConfigApi.JSON_ERROR)) {
                                    Toast.makeText(mContext, getString(R.string.error), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                tvReceiverName.setTextColor(getResources().getColor(R.color.lightRed));
                                tvReceiverName.setText(getString(R.string.usernameNotFound));
                                tvReceiverPhoneNumber.setText("");
//                                Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        progressDialog.dismiss();
                        Toast.makeText(mContext, getString(R.string.badConnection), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void typingDelay() {

        etReceiverId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {
                handler.removeCallbacks(input_finish_checker);

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker2, delay);
                } else {

                }
            }
        }

        );

        etTransferAmount.addTextChangedListener(new TextWatcher() {
                                                    @Override
                                                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                                                  int after) {
                                                    }

                                                    @Override
                                                    public void onTextChanged(final CharSequence s, int start, int before,
                                                                              int count) {
                                                        handler.removeCallbacks(input_finish_checker);

                                                    }

                                                    @Override
                                                    public void afterTextChanged(final Editable s) {
                                                        if (s.length() > 0) {
                                                            last_text_edit = System.currentTimeMillis();
                                                            handler.postDelayed(input_finish_checker, delay);
                                                        } else {
                                                            btnTransfer.setEnabled(false);
                                                        }
                                                    }
                                                }

        );
    }

    long delay = 1000;
    long last_text_edit = 0;
    Handler handler = new Handler();

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                // TODO: do what you need here
                btnTransfer.setEnabled(true);
            }
        }
    };

    private Runnable input_finish_checker2 = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                // TODO: do what you need here
                checkAccountRequest();
            }
        }
    };

}
