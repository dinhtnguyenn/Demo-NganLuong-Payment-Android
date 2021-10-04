package com.dinhnt.demo_nganluong_payment_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dinhnt.demo_nganluong_payment_android.helpers.CheckOrderBean;
import com.dinhnt.demo_nganluong_payment_android.helpers.CheckOrderRequest;
import com.dinhnt.demo_nganluong_payment_android.helpers.Commons;
import com.dinhnt.demo_nganluong_payment_android.helpers.Constant;
import com.dinhnt.demo_nganluong_payment_android.helpers.SendOrderBean;
import com.dinhnt.demo_nganluong_payment_android.helpers.SendOrderRequest;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements SendOrderRequest.SendOrderRequestOnResult {

    private Button btnPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPayment = findViewById(R.id.btnPayment);

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = "Định NT";
                String amount = "10000";
                String email = "ntdinh@dinhnt.com";
                String phoneNumber = "0987654321";
                String address = "HCM";
                sendOrderObject(fullName, amount, email, phoneNumber, address);
            }
        });
    }

    private void sendOrderObject(String fullName, String amount, String email, String phoneNumber, String address) {
        SendOrderBean sendOrderBean = new SendOrderBean();
        sendOrderBean.setFunc("sendOrder");
        sendOrderBean.setVersion("1.0");
        sendOrderBean.setMerchantID(Constant.MERCHANT_ID);
        sendOrderBean.setMerchantAccount(Constant.MERCHANT_ACCOUNT);
        sendOrderBean.setOrderCode("123456DEMO");
        sendOrderBean.setTotalAmount(Integer.parseInt(amount));
        sendOrderBean.setCurrency("vnd");
        sendOrderBean.setLanguage("vi");
        sendOrderBean.setReturnUrl(Constant.RETURN_URL);
        sendOrderBean.setCancelUrl(Constant.CANCEL_URL);
        sendOrderBean.setNotifyUrl(Constant.NOTIFY_URL);
        sendOrderBean.setBuyerFullName(fullName);
        sendOrderBean.setBuyerEmail(email);
        sendOrderBean.setBuyerMobile(phoneNumber);
        sendOrderBean.setBuyerAddress(address);

        String checksum = getChecksumSendOrderBean(sendOrderBean);
        sendOrderBean.setChecksum(checksum);

        SendOrderRequest sendOrderRequest = new SendOrderRequest();
        sendOrderRequest.execute(getApplicationContext(), sendOrderBean);
        sendOrderRequest.getSendOrderRequestOnResult(this);
    }

    private String getChecksumSendOrderBean(SendOrderBean sendOrderBean) {
        String stringSendOrder = sendOrderBean.getFunc() + "|" +
                sendOrderBean.getVersion() + "|" +
                sendOrderBean.getMerchantID() + "|" +
                sendOrderBean.getMerchantAccount() + "|" +
                sendOrderBean.getOrderCode() + "|" +
                sendOrderBean.getTotalAmount() + "|" +
                sendOrderBean.getCurrency() + "|" +
                sendOrderBean.getLanguage() + "|" +
                sendOrderBean.getReturnUrl() + "|" +
                sendOrderBean.getCancelUrl() + "|" +
                sendOrderBean.getNotifyUrl() + "|" +
                sendOrderBean.getBuyerFullName() + "|" +
                sendOrderBean.getBuyerEmail() + "|" +
                sendOrderBean.getBuyerMobile() + "|" +
                sendOrderBean.getBuyerAddress() + "|" +
                Constant.MERCHANT_PASSWORD;
        String checksum = Commons.md5(stringSendOrder);

        return checksum;
    }

    @Override
    public void onSendOrderRequestOnResult(boolean result, String data) {
        if (result) {
            try {
                JSONObject objResult = new JSONObject(data);
                String responseCode = objResult.getString("response_code");
                if (responseCode.equalsIgnoreCase("00")) {
                    String tokenCode = objResult.getString("token_code");
                    String checkoutUrl = objResult.getString("checkout_url");

                    Intent intentCheckout = new Intent(getApplicationContext(), CheckOutActivity.class);
                    intentCheckout.putExtra(CheckOutActivity.TOKEN_CODE, tokenCode);
                    intentCheckout.putExtra(CheckOutActivity.CHECKOUT_URL, checkoutUrl);
                    startActivity(intentCheckout);
                } else {
                    Toast.makeText(MainActivity.this, Commons.getCodeError(getApplicationContext(), responseCode), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.fillInStackTrace();
            }
        }
    }

}