package com.cosafe.android.interfaces;

public interface AsyncCallback {
    void processFinish(Object obj);

    void processStart();

    void showNetworkError();
}
