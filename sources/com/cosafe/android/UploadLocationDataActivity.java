package com.cosafe.android;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.cosafe.android.utils.MyPreferences;
import com.cosafe.android.utils.PreferenceManager;
import com.cosafe.android.utils.Utility;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.internal.cache.DiskLruCache;
import org.json.JSONObject;

public class UploadLocationDataActivity extends AppCompatActivity {
    public static final String FILE_NAME_BLUETOOTH = "bluetooth_file.txt";
    private static final String FILE_NAME_LOCATION = "location_file.txt";
    public static final String FOLDER_NAME = "SafeTogetherLogger";
    private final String TAG = "UploadLocationData";
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private Toolbar toolbar;
    private View view;

    private class UploadBluetoothTask extends AsyncTask<Void, Void, String> {
        private UploadBluetoothTask() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            UploadLocationDataActivity.this.progressDialog = new ProgressDialog(UploadLocationDataActivity.this);
            UploadLocationDataActivity.this.progressDialog.setMessage("Loading...");
            UploadLocationDataActivity.this.progressDialog.show();
        }

        /* access modifiers changed from: protected */
        public String doInBackground(Void... voidArr) {
            String str = "bluetooth_file.txt";
            String str2 = "";
            try {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "SafeTogetherLogger");
                StringBuilder sb = new StringBuilder();
                sb.append(file);
                sb.append(File.separator);
                sb.append(str);
                File file2 = new File(sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("File...::::");
                sb2.append(file2);
                sb2.append(" : ");
                sb2.append(file2.exists());
                Log.d("UploadLocationData", sb2.toString());
                MediaType parse = MediaType.parse("text/plain");
                MyPreferences myPreferences = new MyPreferences(UploadLocationDataActivity.this);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("https://test1.ncog.gov.in/CoSafe_key/uploadfile?mobile=");
                sb3.append(myPreferences.getPref(MyPreferences.PREF_MOBILE_NUMBER, str2));
                sb3.append("&type=blth&flag=2");
                String sb4 = sb3.toString();
                return new OkHttpClient().newCall(new Builder().addHeader("mobile", PreferenceManager.read(PreferenceManager.USER_MOBILE_NO, str2)).addHeader("authkey", PreferenceManager.read(PreferenceManager.KEY_TOKEN, str2)).url(sb4).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file", str, RequestBody.create(parse, file2)).build()).build()).execute().body().string();
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            try {
                UploadLocationDataActivity.this.progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String str2 = "Something went wrong. Please try again.";
            if (str != null) {
                String str3 = "UploadLocationData";
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("response....");
                    sb.append(str);
                    Log.d(str3, sb.toString());
                    if (new JSONObject(str).optString("statusCode").equalsIgnoreCase(DiskLruCache.VERSION_1)) {
                        Utility.showAlertDialog(UploadLocationDataActivity.this, "File uploaded successfully");
                    } else {
                        Toast.makeText(UploadLocationDataActivity.this, str2, 1).show();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    Toast.makeText(UploadLocationDataActivity.this, str2, 1).show();
                }
            } else {
                Toast.makeText(UploadLocationDataActivity.this, str2, 1).show();
            }
        }
    }

    private class UploadLocationTask extends AsyncTask<Void, Void, String> {
        private UploadLocationTask() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            UploadLocationDataActivity.this.progressDialog = new ProgressDialog(UploadLocationDataActivity.this);
            UploadLocationDataActivity.this.progressDialog.setMessage("Loading...");
            UploadLocationDataActivity.this.progressDialog.show();
        }

        /* access modifiers changed from: protected */
        public String doInBackground(Void... voidArr) {
            String str = UploadLocationDataActivity.FILE_NAME_LOCATION;
            String str2 = "";
            try {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "SafeTogetherLogger");
                StringBuilder sb = new StringBuilder();
                sb.append(file);
                sb.append(File.separator);
                sb.append(str);
                File file2 = new File(sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("File...::::");
                sb2.append(file2);
                sb2.append(" : ");
                sb2.append(file2.exists());
                Log.d("UploadLocationData", sb2.toString());
                MediaType parse = MediaType.parse("text/plain");
                MyPreferences myPreferences = new MyPreferences(UploadLocationDataActivity.this);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("https://test1.ncog.gov.in/CoSafe_key/uploadfile?mobile=");
                sb3.append(myPreferences.getPref(MyPreferences.PREF_MOBILE_NUMBER, str2));
                sb3.append("&type=latlon&flag=2");
                String sb4 = sb3.toString();
                return new OkHttpClient().newCall(new Builder().addHeader("mobile", PreferenceManager.read(PreferenceManager.USER_MOBILE_NO, str2)).addHeader("authkey", PreferenceManager.read(PreferenceManager.KEY_TOKEN, str2)).url(sb4).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file", str, RequestBody.create(parse, file2)).build()).build()).execute().body().string();
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            try {
                UploadLocationDataActivity.this.progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String str2 = "Something went wrong. Please try again.";
            if (str != null) {
                String str3 = "UploadLocationData";
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("response....");
                    sb.append(str);
                    Log.d(str3, sb.toString());
                    if (new JSONObject(str).optString("statusCode").equalsIgnoreCase(DiskLruCache.VERSION_1)) {
                        Utility.showAlertDialog(UploadLocationDataActivity.this, "File uploaded successfully");
                    } else {
                        Toast.makeText(UploadLocationDataActivity.this, str2, 1).show();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    Toast.makeText(UploadLocationDataActivity.this, str2, 1).show();
                }
            } else {
                Toast.makeText(UploadLocationDataActivity.this, str2, 1).show();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.upload_location_data_activity);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.view = this.toolbar.getRootView();
        findViewById(R.id.uploadLocationBtn).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new UploadLocationTask().execute(new Void[0]);
            }
        });
        findViewById(R.id.uploadBluetoothBtn).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new UploadBluetoothTask().execute(new Void[0]);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }
}
