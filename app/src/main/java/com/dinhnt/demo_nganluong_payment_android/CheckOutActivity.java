package com.dinhnt.demo_nganluong_payment_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.dinhnt.demo_nganluong_payment_android.helpers.CheckOrderBean;
import com.dinhnt.demo_nganluong_payment_android.helpers.CheckOrderRequest;
import com.dinhnt.demo_nganluong_payment_android.helpers.Commons;
import com.dinhnt.demo_nganluong_payment_android.helpers.Constant;

import org.json.JSONObject;

public class CheckOutActivity extends AppCompatActivity implements CheckOrderRequest.CheckOrderRequestOnResult {

    public static final String TOKEN_CODE = "token_code";
    public static final String CHECKOUT_URL = "checkout_url";

    private WebView webData;

    private String mTokenCode = "";
    private String mCheckoutUrl = "";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTokenCode = extras.getString(TOKEN_CODE, "");
            mCheckoutUrl = extras.getString(CHECKOUT_URL, "");
        }

        webData = (WebView) findViewById(R.id.activity_checkout_webView);
        webData.getSettings().setJavaScriptEnabled(true);
        webData.setWebChromeClient(new WebChromeClient());
        webData.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if (url.equalsIgnoreCase(Constant.RETURN_URL)) {
                    checkOrderObject(mTokenCode);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        if (!mCheckoutUrl.equalsIgnoreCase("")) {
            webData.loadUrl(mCheckoutUrl);
        }
    }


    private void checkOrderObject(String tokenCode) {
        CheckOrderBean checkOrderBean = new CheckOrderBean();
        checkOrderBean.setFunc("checkOrder");
        checkOrderBean.setVersion("1.0");
        checkOrderBean.setMerchantID(Constant.MERCHANT_ID);
        checkOrderBean.setTokenCode(tokenCode);

        String checksum = getChecksumCheckOrderBean(checkOrderBean);
        checkOrderBean.setChecksum(checksum);

        CheckOrderRequest checkOrderRequest = new CheckOrderRequest();
        checkOrderRequest.execute(getApplicationContext(), checkOrderBean);
        checkOrderRequest.getCheckOrderRequestOnResult(this);
    }

    private String getChecksumCheckOrderBean(CheckOrderBean checkOrderBean) {
        String stringSendOrder = checkOrderBean.getFunc() + "|" +
                checkOrderBean.getVersion() + "|" +
                checkOrderBean.getMerchantID() + "|" +
                checkOrderBean.getTokenCode() + "|" +
                Constant.MERCHANT_PASSWORD;

        return Commons.md5(stringSendOrder);
    }

    @Override
    public void onCheckOrderRequestOnResult(boolean result, String data) {
        if (result) {
            try {
                JSONObject objResult = new JSONObject(data);
                String responseCode = objResult.getString("response_code");
                if (responseCode.equalsIgnoreCase("00")) {
                    String response_code = objResult.getString("response_code");
                    String receiver_email = objResult.getString("receiver_email");
                    String order_code = objResult.getString("order_code");
                    int total_amount = objResult.getInt("total_amount");
                    String currency = objResult.getString("currency");
                    String language = objResult.getString("language");
                    String return_url = objResult.getString("return_url");
                    String cancel_url = objResult.getString("cancel_url");
                    String notify_url = objResult.getString("notify_url");
                    String buyer_full_name = objResult.getString("buyer_fullname");
                    String buyer_email = objResult.getString("buyer_email");
                    String buyer_mobile = objResult.getString("buyer_mobile");
                    String buyer_address = objResult.getString("buyer_address");
                    int transaction_id = objResult.getInt("transaction_id");
                    int transaction_status = objResult.getInt("transaction_status");
                    int transaction_amount = objResult.getInt("transaction_amount");
                    String transaction_currency = objResult.getString("transaction_currency");
                    int transaction_escrow = objResult.getInt("transaction_escrow");

//                    String dataCheckOrder =
//                            "response_code:  " + response_code + "\n\n" +
//                                    "receiver_email:  " + receiver_email + "\n\n" +
//                                    "order_code:  " + order_code + "\n\n" +
//                                    "total_amount:  " + total_amount + "\n\n" +
//                                    "currency:  " + currency + "\n\n" +
//                                    "language:  " + language + "\n\n" +
//                                    "return_url:  " + return_url + "\n\n" +
//                                    "cancel_url:  " + cancel_url + "\n\n" +
//                                    "notify_url:  " + notify_url + "\n\n" +
//                                    "buyer_full_name:  " + buyer_full_name + "\n\n" +
//                                    "buyer_email:  " + buyer_email + "\n\n" +
//                                    "buyer_mobile:  " + buyer_mobile + "\n\n" +
//                                    "buyer_address:  " + buyer_address + "\n\n" +
//                                    "transaction_id:  " + transaction_id + "\n\n" +
//                                    "transaction_status:  " + transaction_status + "\n\n" +
//                                    "transaction_amount:  " + transaction_amount + "\n\n" +
//                                    "transaction_currency:  " + transaction_currency + "\n\n" +
//                                    "transaction_escrow:  " + transaction_escrow + "\n\n";
                    startActivity(new Intent(CheckOutActivity.this, SuccessActivity.class));
                    finish();

                } else {
                    Toast.makeText(CheckOutActivity.this, Commons.getCodeError(getApplicationContext(), responseCode), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.fillInStackTrace();
            }
        }
    }
}