package com.cosafe.android.utils.retrofit;

import android.content.Context;
import android.util.Log;
import com.cosafe.android.utils.PreferenceManager;
import com.google.gson.JsonObject;
import okhttp3.internal.cache.DiskLruCache;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitAPIManager {
    /* access modifiers changed from: private */
    public static final String TAG = RetrofitAPIManager.class.getSimpleName();
    private Context mContext;
    /* access modifiers changed from: private */
    public RetrofitResponseListener mRetrofitResponseListener;
    private String user_auth_mobile;
    private String user_key_token;

    public RetrofitAPIManager(Context context, RetrofitResponseListener retrofitResponseListener) {
        this.mContext = context;
        this.mRetrofitResponseListener = retrofitResponseListener;
        String str = "";
        this.user_auth_mobile = PreferenceManager.read(PreferenceManager.USER_MOBILE_NO, str);
        this.user_key_token = PreferenceManager.read(PreferenceManager.KEY_TOKEN, str);
    }

    private <T> void sendRequest(Call<T> call, final int i) {
        call.enqueue(new Callback<T>() {
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    String access$000 = RetrofitAPIManager.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onResponse: ");
                    sb.append(response.body());
                    Log.e(access$000, sb.toString());
                    RetrofitAPIManager.this.mRetrofitResponseListener.isSuccess(response.body(), i);
                    return;
                }
                response.code();
                RetrofitAPIManager.this.mRetrofitResponseListener.isError(null);
            }

            public void onFailure(Call<T> call, Throwable th) {
                RetrofitAPIManager.this.mRetrofitResponseListener.isError(th.getMessage());
            }
        });
    }

    public void getOTPRequest(String str) {
        sendRequest(RetrofitClient.getAPIInterface().getOTP(str), 100);
    }

    public void verifyOTPRequest(String str, String str2, String str3) {
        sendRequest(RetrofitClient.getAPIInterface().verifyOTP(str, str2, str3), 101);
    }

    public void downloadRequest(String str) {
        sendRequest(RetrofitClient.getAPIInterface().getAllDownloadData(this.user_auth_mobile, this.user_key_token, DiskLruCache.VERSION_1, str), 102);
    }

    public void getAllupdateflag(String str, String str2, String str3, String str4) {
        sendRequest(RetrofitClient.getAPIInterface().getAllupdateflag(this.user_auth_mobile, this.user_key_token, str, str2, str3, str4), 105);
    }

    public void submitQnA(JsonObject jsonObject) {
        sendRequest(RetrofitClient.getAPIInterface().submitQnA(this.user_auth_mobile, this.user_key_token, jsonObject), 103);
    }

    public void getUserIDRequest(String str) {
        sendRequest(RetrofitClient.getAPIInterface().getMyUserID(this.user_auth_mobile, this.user_key_token, str), 104);
    }

    public void checkAppVersionRequest() {
        sendRequest(RetrofitClient.getAPIInterface().checkAppVersion(this.user_auth_mobile, this.user_key_token, "Android"), 107);
    }
}
