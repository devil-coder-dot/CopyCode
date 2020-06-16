package com.cosafe.android.utils.retrofit;

import com.cosafe.android.utils.PreferenceManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://test1.ncog.gov.in/";
    public static RetrofitClient sInstance;
    private Retrofit retrofit = new Builder().baseUrl(BASE_URL).client(new OkHttpClient.Builder().build()).addConverterFactory(GsonConverterFactory.create()).build();

    private class ApplicationInterceptor implements Interceptor {
        private ApplicationInterceptor() {
        }

        public Response intercept(Chain chain) throws IOException {
            Request.Builder newBuilder = chain.request().newBuilder();
            String str = "";
            newBuilder.addHeader("mobile", PreferenceManager.read(PreferenceManager.USER_MOBILE_NO, str));
            newBuilder.addHeader("authkey", PreferenceManager.read(PreferenceManager.KEY_TOKEN, str));
            return chain.proceed(newBuilder.build());
        }
    }

    public static void create() {
        if (sInstance == null) {
            synchronized (RetrofitClient.class) {
                if (sInstance == null) {
                    sInstance = new RetrofitClient();
                }
            }
            return;
        }
        throw new IllegalStateException("RetrofitClient instance is already been created.");
    }

    private RetrofitClient() {
    }

    public static Retrofit retrofit() {
        synchronized (RetrofitClient.class) {
            if (sInstance == null) {
                throw new IllegalStateException("RetrofitClient instance is not created yet. Call RetrofitClient.create() before calling getInstance()");
            }
        }
        return sInstance.retrofit;
    }

    public static APIInterface getAPIInterface() {
        return (APIInterface) retrofit().create(APIInterface.class);
    }
}
