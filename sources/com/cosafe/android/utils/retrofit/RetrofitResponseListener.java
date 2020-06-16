package com.cosafe.android.utils.retrofit;

public interface RetrofitResponseListener {
    void isError(String str);

    void isSuccess(Object obj, int i);
}
