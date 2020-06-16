package com.cosafe.android.utils.retrofit;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientDownload {
    private static final String BASE_URL = "https://test1.ncog.gov.in/";
    public static RetrofitClientDownload sInstance;
    private Retrofit retrofit = new Builder().baseUrl(BASE_URL).client(new OkHttpClient.Builder().build()).addConverterFactory(GsonConverterFactory.create()).build();

    private class ApplicationInterceptor implements Interceptor {
        private ApplicationInterceptor() {
        }

        public Response intercept(Chain chain) throws IOException {
            return chain.proceed(chain.request().newBuilder().build());
        }
    }

    public static void create() {
        if (sInstance == null) {
            synchronized (RetrofitClientDownload.class) {
                if (sInstance == null) {
                    sInstance = new RetrofitClientDownload();
                }
            }
            return;
        }
        throw new IllegalStateException("RetrofitClient instance is already been created.");
    }

    private RetrofitClientDownload() {
    }

    public static Retrofit retrofit() {
        synchronized (RetrofitClientDownload.class) {
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
