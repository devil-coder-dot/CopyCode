package com.cosafe.android;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;
import com.cosafe.android.base.BaseActivity;
import com.cosafe.android.databinding.ActivitySplashBinding;
import com.cosafe.android.introslider.WelcomeActivity;
import com.cosafe.android.models.gson.CheckVersionObject;
import com.cosafe.android.utils.Utility;
import com.cosafe.android.utils.retrofit.RetrofitAPIManager;
import com.cosafe.android.utils.retrofit.RetrofitResponseListener;
import okhttp3.internal.cache.DiskLruCache;

public class SplashActivity extends BaseActivity implements RetrofitResponseListener {
    private final String TAG = SplashActivity.class.getSimpleName();
    private ActivitySplashBinding binding;
    private RetrofitAPIManager mApiManager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = (ActivitySplashBinding) DataBindingUtil.setContentView(this, R.layout.activity_splash);
        invokeVersionCheckAPI();
    }

    private void invokeVersionCheckAPI() {
        RetrofitAPIManager retrofitAPIManager = new RetrofitAPIManager(this, this);
        this.mApiManager = retrofitAPIManager;
        retrofitAPIManager.checkAppVersionRequest();
    }

    private void showVersionUpdateDialog(boolean z) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_new_update);
        TextView textView = (TextView) dialog.findViewById(R.id.updateTxt);
        TextView textView2 = (TextView) dialog.findViewById(R.id.cancelTxt);
        dialog.setCanceledOnTouchOutside(false);
        if (z) {
            dialog.setCancelable(false);
            textView2.setVisibility(8);
        } else {
            dialog.setCancelable(true);
            textView2.setVisibility(0);
        }
        textView2.setOnClickListener(new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                SplashActivity.this.lambda$showVersionUpdateDialog$0$SplashActivity(this.f$1, view);
            }
        });
        textView.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                SplashActivity.this.lambda$showVersionUpdateDialog$1$SplashActivity(view);
            }
        });
        dialog.show();
    }

    public /* synthetic */ void lambda$showVersionUpdateDialog$0$SplashActivity(Dialog dialog, View view) {
        dialog.dismiss();
        startNextScreen();
    }

    public /* synthetic */ void lambda$showVersionUpdateDialog$1$SplashActivity(View view) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://play.google.com/store/apps/details?id=");
        sb.append(Utility.getPackageName(this));
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
    }

    private void startNextScreen() {
        new Handler().postDelayed(new Runnable() {
            public final void run() {
                SplashActivity.this.lambda$startNextScreen$2$SplashActivity();
            }
        }, 2000);
    }

    public /* synthetic */ void lambda$startNextScreen$2$SplashActivity() {
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }

    public void isError(String str) {
        startNextScreen();
    }

    public void isSuccess(Object obj, int i) {
        CheckVersionObject checkVersionObject = (CheckVersionObject) obj;
        if (checkVersionObject == null) {
            startNextScreen();
        } else if (checkVersionObject.getStatusCode().equals(DiskLruCache.VERSION_1)) {
            String latestVersion = checkVersionObject.getRows().getLatestVersion();
            String appVersion = Utility.getAppVersion(this);
            boolean booleanValue = checkVersionObject.getRows().getIsItMandatory().booleanValue();
            if (appVersion != null) {
                String str = ".";
                if (appVersion.contains(str) && latestVersion != null && latestVersion.contains(str)) {
                    String str2 = "";
                    if (((long) Integer.parseInt(appVersion.replace(str, str2))) < ((long) Integer.parseInt(latestVersion.replace(str, str2)))) {
                        showVersionUpdateDialog(booleanValue);
                        return;
                    } else {
                        startNextScreen();
                        return;
                    }
                }
            }
            startNextScreen();
        } else {
            startNextScreen();
        }
    }
}
