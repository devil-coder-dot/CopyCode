package com.cosafe.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.cosafe.android.interfaces.AsyncCallback;
import com.cosafe.android.utils.RequestAsyncTaskGet;
import com.cosafe.android.utils.Utility;
import okhttp3.internal.cache.DiskLruCache;
import org.json.JSONObject;

public class UploadActivityNew extends AppCompatActivity {
    private final String TAG = "UploadActivityNew";
    /* access modifiers changed from: private */
    public EditText otpEdit;
    private LinearLayout otpLay;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private Toolbar toolbar;
    /* access modifiers changed from: private */
    public EditText uniqueIdEdit;
    private LinearLayout uniqueIdLay;
    private Button verifyOtpBtn;
    private Button verifyUniqueIdBtn;
    private View view;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.upload_activity_new);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.view = this.toolbar.getRootView();
        setupUi();
        this.verifyUniqueIdBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String trim = UploadActivityNew.this.uniqueIdEdit.getText().toString().trim();
                if (trim.equalsIgnoreCase("")) {
                    Toast.makeText(UploadActivityNew.this, "Please enter Unique ID", 0).show();
                } else {
                    UploadActivityNew.this.sendVerifyUniqueIdRequest(trim);
                }
            }
        });
        this.verifyOtpBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String trim = UploadActivityNew.this.otpEdit.getText().toString().trim();
                if (trim.equalsIgnoreCase("")) {
                    Toast.makeText(UploadActivityNew.this, "Please enter OTP", 0).show();
                } else {
                    UploadActivityNew.this.sendVerifyOTPRequest(trim);
                }
            }
        });
    }

    private void setupUi() {
        this.uniqueIdLay = (LinearLayout) findViewById(R.id.uniqueIdLay);
        this.uniqueIdEdit = (EditText) findViewById(R.id.uniqueIdEdit);
        this.otpLay = (LinearLayout) findViewById(R.id.otpLay);
        this.otpEdit = (EditText) findViewById(R.id.otpEdit);
        this.verifyUniqueIdBtn = (Button) findViewById(R.id.verifyUniqueIdBtn);
        this.verifyOtpBtn = (Button) findViewById(R.id.verifyOtpBtn);
    }

    /* access modifiers changed from: private */
    public void sendVerifyUniqueIdRequest(String str) {
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject();
        } catch (Exception e) {
            e.printStackTrace();
            jSONObject = null;
        }
        if (jSONObject != null) {
            AnonymousClass3 r2 = new AsyncCallback() {
                public void processStart() {
                    UploadActivityNew.this.progressDialog = new ProgressDialog(UploadActivityNew.this);
                    UploadActivityNew.this.progressDialog.setMessage("Loading...");
                    UploadActivityNew.this.progressDialog.show();
                }

                public void showNetworkError() {
                    try {
                        UploadActivityNew.this.progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(UploadActivityNew.this, "Something went wrong. Please try again.", 1).show();
                }

                public void processFinish(Object obj) {
                    String str = "Something went wrong. Please try again.";
                    try {
                        UploadActivityNew.this.progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        String str2 = (String) obj;
                        if (str2 == null) {
                            Toast.makeText(UploadActivityNew.this, str, 1).show();
                        } else if (new JSONObject(str2).optString("type").equalsIgnoreCase("success")) {
                            UploadActivityNew.this.showVerifyOtpLayout();
                            Toast.makeText(UploadActivityNew.this, "OTP sent", 0).show();
                        } else {
                            Utility.showAlertDialog(UploadActivityNew.this, "Invalid Unique ID");
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        Toast.makeText(UploadActivityNew.this, str, 1).show();
                    }
                }
            };
            StringBuilder sb = new StringBuilder();
            sb.append("https://test1.ncog.gov.in/CoSafe_key/sendotpBaseOnUniqueId?uniqueId=");
            sb.append(str);
            new RequestAsyncTaskGet(r2, sb.toString(), jSONObject, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
            return;
        }
        Log.d("UploadActivityNew", "reqJson == null");
    }

    /* access modifiers changed from: private */
    public void sendVerifyOTPRequest(String str) {
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject();
        } catch (Exception e) {
            e.printStackTrace();
            jSONObject = null;
        }
        if (jSONObject != null) {
            AnonymousClass4 r2 = new AsyncCallback() {
                public void processStart() {
                    UploadActivityNew.this.progressDialog = new ProgressDialog(UploadActivityNew.this);
                    UploadActivityNew.this.progressDialog.setMessage("Loading...");
                    UploadActivityNew.this.progressDialog.show();
                }

                public void showNetworkError() {
                    try {
                        UploadActivityNew.this.progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(UploadActivityNew.this, "Something went wrong. Please try again.", 1).show();
                }

                public void processFinish(Object obj) {
                    String str = "Something went wrong. Please try again.";
                    try {
                        UploadActivityNew.this.progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        String str2 = (String) obj;
                        if (str2 == null) {
                            Toast.makeText(UploadActivityNew.this, str, 1).show();
                        } else if (new JSONObject(str2).optString("ok").equalsIgnoreCase(DiskLruCache.VERSION_1)) {
                            UploadActivityNew.this.startActivity(new Intent(UploadActivityNew.this, UploadLocationDataActivity.class));
                            UploadActivityNew.this.finish();
                        } else {
                            Utility.showAlertDialog(UploadActivityNew.this, "Invalid OTP");
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        Toast.makeText(UploadActivityNew.this, str, 1).show();
                    }
                }
            };
            StringBuilder sb = new StringBuilder();
            sb.append("https://test1.ncog.gov.in/CoSafe_key/verifiedotpUI?uniqueId=");
            sb.append(this.uniqueIdEdit.getText().toString().trim());
            sb.append("&otp=");
            sb.append(str);
            sb.append("&deviceId=");
            sb.append(Utility.getDeviceId(this));
            new RequestAsyncTaskGet(r2, sb.toString(), jSONObject, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
            return;
        }
        Log.d("UploadActivityNew", "reqJson == null");
    }

    /* access modifiers changed from: private */
    public void showVerifyOtpLayout() {
        this.uniqueIdLay.setVisibility(8);
        this.otpLay.setVisibility(0);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }
}
