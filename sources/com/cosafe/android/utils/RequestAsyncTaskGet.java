package com.cosafe.android.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.cosafe.android.interfaces.AsyncCallback;
import org.json.JSONObject;

public class RequestAsyncTaskGet extends AsyncTask<Object, Object, Object> {
    private String TAG = "RequestAsyncTask";
    private Context context;
    private AsyncCallback delegate = null;
    private JSONObject requestParams;
    private String requestUrl;

    public RequestAsyncTaskGet(AsyncCallback asyncCallback, String str, JSONObject jSONObject, Context context2) {
        this.delegate = asyncCallback;
        this.requestParams = jSONObject;
        this.requestUrl = str;
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        this.delegate.processStart();
    }

    /* access modifiers changed from: protected */
    public Object doInBackground(Object... objArr) {
        Log.d(this.TAG, "calling doInBackground");
        return AppAsyncReqUtil.readHttpRespGet(this.requestUrl, this.requestParams.toString(), this.context);
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Object obj) {
        if (obj != null) {
            String str = (String) obj;
            StringBuilder sb = new StringBuilder();
            sb.append("=============");
            sb.append(str);
            Log.d("resp", sb.toString());
            if (str.equalsIgnoreCase(Constants.NETWORK_ERROR_CODE)) {
                this.delegate.showNetworkError();
            } else {
                this.delegate.processFinish(obj);
            }
        } else {
            this.delegate.processFinish(obj);
        }
    }
}
