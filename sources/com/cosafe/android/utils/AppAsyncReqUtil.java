package com.cosafe.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppAsyncReqUtil {
    public static final String ERROR_DEF_RESP = null;
    public static String RESPONSE_BODY = "";
    private static final String TAG = "AppAsyncReqUtil";
    public static final int TIMEOUT = 60;

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        }
        Log.e(TAG, "Network Not Available...");
        return false;
    }

    public static String readHttpRtrResp(String str, int i, String str2, Context context) {
        String str3 = "text/plain";
        String str4 = Constants.NETWORK_ERROR_CODE;
        String str5 = "]";
        String str6 = "Reading HTTP API Response Done........[";
        String str7 = TAG;
        boolean z = true;
        String str8 = null;
        int i2 = 0;
        while (z) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("URL : ");
                sb.append(str);
                Log.d(str7, sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("JSON : ");
                sb2.append(str2);
                Log.d(str7, sb2.toString());
                Response execute = new Builder().connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build().newCall(new Request.Builder().addHeader("Content-Type", str3).url(str).post(RequestBody.create(MediaType.parse(str3), str2)).build()).execute();
                String string = execute.body().string();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("resp code.....");
                sb3.append(execute.code());
                Log.d(str7, sb3.toString());
                if (execute.code() != 200) {
                    string = str4;
                }
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str6);
                sb4.append(string);
                sb4.append(str5);
                Log.d(str7, sb4.toString());
                str8 = string;
                z = false;
            } catch (Exception e) {
                Log.e(str7, "HTTP API Read Error:- ", e);
                StringBuilder sb5 = new StringBuilder();
                sb5.append("ex...............");
                sb5.append(e.getMessage());
                Log.e(str7, sb5.toString());
                e.printStackTrace();
                if (i2 >= i) {
                    z = false;
                }
                i2++;
                StringBuilder sb6 = new StringBuilder();
                sb6.append(str6);
                sb6.append(str4);
                sb6.append(str5);
                Log.d(str7, sb6.toString());
                str8 = str4;
            } catch (Throwable th) {
                StringBuilder sb7 = new StringBuilder();
                sb7.append(str6);
                sb7.append(str8);
                sb7.append(str5);
                Log.d(str7, sb7.toString());
                throw th;
            }
        }
        return str8;
    }

    public static String readHttpResp(String str, String str2, Context context) {
        return readHttpRtrResp(str, 0, str2, context);
    }

    public static String readHttpRtrRespGet(String str, int i, String str2, Context context) {
        String str3 = Constants.NETWORK_ERROR_CODE;
        String str4 = "]";
        String str5 = "Reading HTTP API Response Done........[";
        String str6 = TAG;
        boolean z = true;
        String str7 = null;
        int i2 = 0;
        while (z) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("URL : ");
                sb.append(str);
                Log.d(str6, sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("JSON : ");
                sb2.append(str2);
                Log.d(str6, sb2.toString());
                Response execute = new Builder().connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build().newCall(new Request.Builder().addHeader("Content-Type", "text/plain").url(str).get().build()).execute();
                String string = execute.body().string();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("resp code.....");
                sb3.append(execute.code());
                Log.d(str6, sb3.toString());
                if (execute.code() != 200) {
                    string = str3;
                }
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str5);
                sb4.append(string);
                sb4.append(str4);
                Log.d(str6, sb4.toString());
                str7 = string;
                z = false;
            } catch (Exception e) {
                Log.e(str6, "HTTP API Read Error:- ", e);
                StringBuilder sb5 = new StringBuilder();
                sb5.append("ex...............");
                sb5.append(e.getMessage());
                Log.e(str6, sb5.toString());
                e.printStackTrace();
                if (i2 >= i) {
                    z = false;
                }
                i2++;
                StringBuilder sb6 = new StringBuilder();
                sb6.append(str5);
                sb6.append(str3);
                sb6.append(str4);
                Log.d(str6, sb6.toString());
                str7 = str3;
            } catch (Throwable th) {
                StringBuilder sb7 = new StringBuilder();
                sb7.append(str5);
                sb7.append(str7);
                sb7.append(str4);
                Log.d(str6, sb7.toString());
                throw th;
            }
        }
        return str7;
    }

    public static String readHttpRespGet(String str, String str2, Context context) {
        return readHttpRtrRespGet(str, 0, str2, context);
    }
}
