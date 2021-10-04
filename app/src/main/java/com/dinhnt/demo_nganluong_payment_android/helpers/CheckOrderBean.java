package com.dinhnt.demo_nganluong_payment_android.helpers;

public class CheckOrderBean {

    private String mFunc;
    private String mVersion;
    private String mMerchantID;
    private String mTokenCode;
    private String mChecksum;

    public CheckOrderBean() {

    }

    public void setFunc(String pFunc) {
        this.mFunc = pFunc;
    }

    public String getFunc() {
        return mFunc;
    }

    public void setVersion(String pVersion) {
        this.mVersion = pVersion;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setMerchantID(String pMerchantID) {
        this.mMerchantID = pMerchantID;
    }

    public String getMerchantID() {
        return mMerchantID;
    }

    public void setTokenCode(String pTokenCode) {
        this.mTokenCode = pTokenCode;
    }

    public String getTokenCode() {
        return mTokenCode;
    }

    public void setChecksum(String pChecksum) {
        this.mChecksum = pChecksum;
    }

    public String getChecksum() {
        return mChecksum;
    }
}
