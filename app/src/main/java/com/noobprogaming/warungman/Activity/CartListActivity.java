package com.noobprogaming.warungman.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.noobprogaming.warungman.Adapter.CartItemAdapter;
import com.noobprogaming.warungman.Model.CartItemModel;
import com.noobprogaming.warungman.R;
import com.noobprogaming.warungman.Service.BaseApiService;
import com.noobprogaming.warungman.Service.ConfigApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartListActivity extends AppCompatActivity {

    String token, confirm_id, purchase_id, note, total_price;
    Context mContext;
    BaseApiService mApiService;
    Spinner spinnerPos;
    Button btnOrder;
    TextView tvPurchase_id, tvSeller, tvTotalPrice;
    EditText etNote;
//    ListView lvCartItem;

    private RecyclerView rvItem;
    private CartItemAdapter adapter;
    private ArrayList<CartItemModel> itemArrayList;

//    BottomNavigationView bnvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        token = getIntent().getStringExtra(ConfigApi.TAG_TOKEN);
        purchase_id = getIntent().getStringExtra(ConfigApi.TAG_PURCHASE_ID);

        mContext = CartListActivity.this;
        mApiService = ConfigApi.getAPIService();

        rvItem = (RecyclerView) findViewById(R.id.rvItem);

        etNote = (EditText) findViewById(R.id.etNote);
        tvPurchase_id = (TextView) findViewById(R.id.tvPurchase_id);
        tvSeller = (TextView) findViewById(R.id.tvSeller);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);

        spinnerPos = (Spinner) findViewById(R.id.spinnerPos);

        btnOrder = (Button) findViewById(R.id.btnOrder);
        btnOrder.setEnabled(false);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(confirm_id) == 1) {
                    orderNow();
                } else if (Integer.parseInt(confirm_id) == 2) {
                    cancelOrderAlertDialog();
                } else if (Integer.parseInt(confirm_id) == 3) {

                } else if (Integer.parseInt(confirm_id) == 4) {

                } else if (Integer.parseInt(confirm_id) == 5) {

                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        cartListRequest();
    }

    private void cartListRequest() {
        mApiService.cartListRequest("Bearer " + token, purchase_id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            pdLoading.dismiss();
                            try {
                                itemArrayList = new ArrayList<>();
                                ArrayList<String> spinnerArray = new ArrayList<String>();
                                JSONObject jsonResult = new JSONObject(response.body().string());
                                if (jsonResult.has(ConfigApi.JSON_CONFIRM)) {
                                    JSONArray jsonConfirm = jsonResult.getJSONArray(ConfigApi.JSON_CONFIRM);
                                    JSONObject jConfirm = jsonConfirm.getJSONObject(0);
                                    confirm_id = jConfirm.getString(ConfigApi.TAG_CONFIRM_ID);
                                    tvPurchase_id.setText("Order #" + jConfirm.getString(ConfigApi.TAG_PURCHASE_ID));
                                    tvSeller.setText("Warung " + jConfirm.getString(ConfigApi.TAG_SELLER));

                                    if (Integer.parseInt(confirm_id) == 1) {
                                        btnOrder.setEnabled(true);
                                        btnOrder.setText(getString(R.string.orderNow));
//                                        Toast.makeText(mContext, getString(R.string.confirm_1), Toast.LENGTH_SHORT).show();
                                    } else if (Integer.parseInt(confirm_id) == 2) {
                                        btnOrder.setEnabled(true);
                                        btnOrder.setText(getString(R.string.cancelOrder));
                                        btnOrder.setBackgroundColor(getResources().getColor(R.color.white));
                                        btnOrder.setTextColor(getResources().getColor(R.color.red));
//                                        Toast.makeText(mContext, getString(R.string.confirm_2), Toast.LENGTH_SHORT).show();
                                    } else if (Integer.parseInt(confirm_id) == 3) {
                                        btnOrder.setEnabled(true);
                                        btnOrder.setText(getString(R.string.orderAgain));
                                        btnOrder.setBackgroundColor(getResources().getColor(R.color.white));
                                        btnOrder.setTextColor(getResources().getColor(R.color.colorAccent));
//                                        Toast.makeText(mContext, getString(R.string.confirm_3), Toast.LENGTH_SHORT).show();
                                    } else if (Integer.parseInt(confirm_id) == 4) {
                                        btnOrder.setEnabled(true);
                                        btnOrder.setText(getString(R.string.orderAgain));
                                        btnOrder.setBackgroundColor(getResources().getColor(R.color.white));
                                        btnOrder.setTextColor(getResources().getColor(R.color.colorAccent));
//                                        Toast.makeText(mContext, getString(R.string.confirm_4), Toast.LENGTH_SHORT).show();
                                    } else if (Integer.parseInt(confirm_id) == 5) {
                                        btnOrder.setText(getString(R.string.confirm_5a));
                                        btnOrder.setBackgroundColor(getResources().getColor(R.color.white));
                                        btnOrder.setTextColor(getResources().getColor(R.color.grey));
//                                        Toast.makeText(mContext, getString(R.string.confirm_5), Toast.LENGTH_SHORT).show();
                                    } else if (Integer.parseInt(confirm_id) == 6) {
                                        btnOrder.setText(getString(R.string.confirm_6a));
                                        btnOrder.setBackgroundColor(getResources().getColor(R.color.white));
                                        btnOrder.setTextColor(getResources().getColor(R.color.grey));
//                                        Toast.makeText(mContext, getString(R.string.confirm_6), Toast.LENGTH_SHORT).show();
                                    }

                                    if (Integer.parseInt(confirm_id) != 1) {
                                        etNote.setText(jConfirm.getString(ConfigApi.TAG_NOTE));
                                        etNote.setEnabled(false);
                                    }

                                    JSONObject jsonTotalPrice = jsonResult.getJSONObject(ConfigApi.JSON_TOTAL_PRICE);
                                    total_price = String.valueOf(jsonTotalPrice.get(ConfigApi.TAG_TOTAL_PRICE));
                                    tvTotalPrice.setText("Rp"+total_price);

                                    JSONArray jsonItem = jsonResult.getJSONArray(ConfigApi.JSON_CART_LIST);
                                    for (int j = 0; j < jsonItem.length(); j++) {
                                        JSONObject jItem = jsonItem.getJSONObject(j);
                                        String item_id = jItem.getString(ConfigApi.TAG_ITEM_ID);
                                        String name = jItem.getString(ConfigApi.TAG_NAME);
                                        String stock = jItem.getString(ConfigApi.TAG_STOCK);
                                        String amount = jItem.getString(ConfigApi.TAG_AMOUNT);
                                        String selling_price = jItem.getString(ConfigApi.TAG_SELLING_PRICE);
                                        String seller_id = jItem.getString(ConfigApi.TAG_SELLER_ID);
                                        String confirm_id = jItem.getString(ConfigApi.TAG_CONFIRM_ID);

                                        itemArrayList.add(new CartItemModel(item_id, name, stock, amount, selling_price, seller_id, confirm_id));
                                    }

                                    if (Integer.parseInt(confirm_id) == 1) {
                                        spinnerPos.setEnabled(true);
                                        JSONArray jsonPos = jsonResult.getJSONArray(ConfigApi.JSON_POS);
                                        for (int j = 0; j < jsonPos.length(); j++) {
                                            JSONObject jPos = jsonPos.getJSONObject(j);
                                            spinnerArray.add(jPos.getString(ConfigApi.TAG_POS_ID));
                                        }

                                    } else {
                                        etNote.setPaintFlags(0);
                                        etNote.setBackground(null);
                                        JSONArray jsonPosCurrent = jsonResult.getJSONArray(ConfigApi.JSON_POS_CURRENT);
                                        JSONObject jPosCurrent = jsonPosCurrent.getJSONObject(0);
                                        spinnerArray.add(jPosCurrent.getString(ConfigApi.TAG_POS_ID));
                                        spinnerPos.setEnabled(false);
                                    }

                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_pos, spinnerArray);
                                    spinnerPos.setAdapter(spinnerArrayAdapter);

                                    adapter = new CartItemAdapter(itemArrayList);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
                                    rvItem.setLayoutManager(layoutManager);
                                    rvItem.setAdapter(adapter);

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
                        Toast.makeText(mContext, getString(R.string.badConnection), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void orderNow() {
        note = etNote.getText().toString();
        String submitPos = spinnerPos.getSelectedItem().toString();

        Intent i = new Intent(mContext, QrScanActivity.class);
        i.putExtra(ConfigApi.TAG_TOKEN, token);
        i.putExtra(ConfigApi.TAG_PURCHASE_ID, purchase_id);
        i.putExtra(ConfigApi.TAG_NOTE, note);
        i.putExtra(ConfigApi.TAG_POS_ID, submitPos);
        i.putExtra(ConfigApi.TAG_TOTAL_PRICE, total_price);
        startActivity(i);
    }

    private void cancelOrderAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("ANDA AKAN MEMBATALKAN ORDER?");
        builder.setCancelable(true);

        builder.setNegativeButton(
                "TETAP ORDER",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.setPositiveButton(
                "YA, BATALKAN",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cancelOrder();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.lightGrey));

    }

    private void cancelOrder() {
        mApiService.cancelOrderRequest("Bearer " + token, purchase_id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            pdLoading.dismiss();
                            try {
                                JSONObject jsonResult = new JSONObject(response.body().string());
                                JSONObject jsonSuccess = jsonResult.getJSONObject(ConfigApi.JSON_STATUS);
                                if (!jsonSuccess.isNull(ConfigApi.TAG_SUCCESS)) {
                                    String status = (String) jsonSuccess.get(ConfigApi.TAG_SUCCESS);
                                    Toast.makeText(mContext, status, Toast.LENGTH_LONG).show();

                                    cartListRequest();
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

    private void deleteCartRequest() {
        mApiService.deleteCartRequest("Bearer " + token, "")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            pdLoading.dismiss();
                            try {
                                JSONObject jsonResult = new JSONObject(response.body().string());
                                if (jsonResult.has(ConfigApi.JSON_CART_LIST)) {
                                    JSONArray jsonItem = jsonResult.getJSONArray(ConfigApi.JSON_CART_LIST);

                                    Toast.makeText(mContext, "OK", Toast.LENGTH_SHORT).show();
                                } else if (jsonResult.has(ConfigApi.JSON_ERROR)) {
                                    Toast.makeText(mContext, "ERROR", Toast.LENGTH_SHORT).show();
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
