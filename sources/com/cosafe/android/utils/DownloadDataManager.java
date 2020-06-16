package com.cosafe.android.utils;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.cosafe.android.MainActivity;
import com.cosafe.android.R;
import com.cosafe.android.models.GetUserID;
import com.cosafe.android.models.SaveUserDetails;
import com.cosafe.android.models.getAllupdateflag;
import com.cosafe.android.utils.retrofit.RetrofitAPIManager;
import com.cosafe.android.utils.retrofit.RetrofitResponseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.internal.cache.DiskLruCache;
import org.json.JSONObject;

public class DownloadDataManager extends AsyncTask<String, Integer, String> implements RetrofitResponseListener {
    private static final String FILE_NAME = "bluetooth_file_down.txt";
    public static final String FILE_NAME_BLUETOOTH = "bluetooth_file.txt";
    private static final String FILE_NAME_LOCATION = "location_file.txt";
    private static final String FOLDER_NAME = "SafeTogetherLogger";
    private String Flag1;
    private String Flag2;
    private String Flag3;
    /* access modifiers changed from: private */
    public MainActivity activity;
    private CompareDataFile compareDataFile;
    private RetrofitAPIManager mApiManager;
    private UpdateAPIListener mListener;
    private String updatedFlag1;
    private String updatedFlag2;
    private String updatedFlag3;

    public interface UpdateAPIListener {
        void onUpdateAPIResult();
    }

    private class UploadBluetoothTask extends AsyncTask<Void, Void, String> {
        private UploadBluetoothTask() {
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
                MediaType parse = MediaType.parse("text/plain");
                MyPreferences myPreferences = new MyPreferences(DownloadDataManager.this.activity);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("https://test1.ncog.gov.in/CoSafe_key/uploadfile?mobile=");
                sb2.append(myPreferences.getPref(MyPreferences.PREF_MOBILE_NUMBER, str2));
                sb2.append("&type=blth&flag=1");
                String sb3 = sb2.toString();
                return new OkHttpClient().newCall(new Builder().addHeader("mobile", PreferenceManager.read(PreferenceManager.USER_MOBILE_NO, str2)).addHeader("authkey", PreferenceManager.read(PreferenceManager.KEY_TOKEN, str2)).url(sb3).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file", str, RequestBody.create(parse, file2)).build()).build()).execute().body().string();
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            if (str != null) {
                try {
                    new JSONObject(str).optString("statusCode").equalsIgnoreCase(DiskLruCache.VERSION_1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class UploadLocationTask extends AsyncTask<Void, Void, String> {
        private UploadLocationTask() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(Void... voidArr) {
            String str = DownloadDataManager.FILE_NAME_LOCATION;
            String str2 = "";
            try {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "SafeTogetherLogger");
                StringBuilder sb = new StringBuilder();
                sb.append(file);
                sb.append(File.separator);
                sb.append(str);
                File file2 = new File(sb.toString());
                MediaType parse = MediaType.parse("text/plain");
                MyPreferences myPreferences = new MyPreferences(DownloadDataManager.this.activity);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("https://test1.ncog.gov.in/CoSafe_key/uploadfile?mobile=");
                sb2.append(myPreferences.getPref(MyPreferences.PREF_MOBILE_NUMBER, str2));
                sb2.append("&type=latlon&flag=1");
                String sb3 = sb2.toString();
                return new OkHttpClient().newCall(new Builder().addHeader("mobile", PreferenceManager.read(PreferenceManager.USER_MOBILE_NO, str2)).addHeader("authkey", PreferenceManager.read(PreferenceManager.KEY_TOKEN, str2)).url(sb3).post(new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file", str, RequestBody.create(parse, file2)).build()).build()).execute().body().string();
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            if (str != null) {
                try {
                    new JSONObject(str).optString("statusCode").equalsIgnoreCase(DiskLruCache.VERSION_1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void isError(String str) {
    }

    public DownloadDataManager(MainActivity mainActivity, UpdateAPIListener updateAPIListener) {
        this.activity = mainActivity;
        this.mListener = updateAPIListener;
        this.mApiManager = new RetrofitAPIManager(mainActivity, this);
    }

    private String compare(String str) throws IOException, NoSuchAlgorithmException {
        return BluetoothManager.getManager(this.activity).compare(new File(str)) == -1 ? "GREEN" : "YELLOW";
    }

    /* JADX WARNING: type inference failed for: r3v1 */
    /* JADX WARNING: type inference failed for: r3v2, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r3v3, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r7v0, types: [java.io.OutputStream, java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r3v4 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: type inference failed for: r3v14 */
    /* JADX WARNING: type inference failed for: r3v15 */
    /* JADX WARNING: type inference failed for: r3v16 */
    /* JADX WARNING: type inference failed for: r3v17 */
    /* JADX WARNING: type inference failed for: r3v18 */
    /* JADX WARNING: type inference failed for: r3v20 */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:102:0x0127 A[SYNTHETIC, Splitter:B:102:0x0127] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0109 A[SYNTHETIC, Splitter:B:84:0x0109] */
    /* JADX WARNING: Unknown variable types count: 5 */
    public String doInBackground(String... strArr) {
        InputStream inputStream;
        HttpURLConnection httpURLConnection;
        Throwable th;
        ? r3;
        ? r32;
        ? fileOutputStream;
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "SafeTogetherLogger");
        StringBuilder sb = new StringBuilder();
        sb.append(file);
        sb.append(File.separator);
        sb.append(FILE_NAME);
        String sb2 = sb.toString();
        ? r33 = 0;
        try {
            httpURLConnection = (HttpURLConnection) new URL(strArr[0]).openConnection();
            try {
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() != 200) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Server returned HTTP ");
                    sb3.append(httpURLConnection.getResponseCode());
                    sb3.append(" ");
                    sb3.append(httpURLConnection.getResponseMessage());
                    String sb4 = sb3.toString();
                    try {
                        return compare(sb2);
                    } catch (Exception e) {
                        try {
                            e.printStackTrace();
                        } catch (IOException unused) {
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        return sb4;
                    }
                } else {
                    int contentLength = httpURLConnection.getContentLength();
                    inputStream = httpURLConnection.getInputStream();
                    try {
                        fileOutputStream = new FileOutputStream(sb2);
                    } catch (Exception e2) {
                        e = e2;
                        r32 = r33;
                        try {
                            String exc = e.toString();
                            if (r32 != 0) {
                                try {
                                    r32.close();
                                } catch (IOException unused2) {
                                }
                            }
                            try {
                                return compare(sb2);
                            } catch (Exception e3) {
                                e3.printStackTrace();
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (httpURLConnection != null) {
                                    httpURLConnection.disconnect();
                                }
                                return exc;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            r3 = r32;
                            if (r3 != 0) {
                                try {
                                    r3.close();
                                } catch (IOException unused3) {
                                }
                            }
                            try {
                                return compare(sb2);
                            } catch (Exception e4) {
                                e4.printStackTrace();
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (httpURLConnection != null) {
                                    httpURLConnection.disconnect();
                                }
                                throw th;
                            }
                        }
                    }
                    try {
                        byte[] bArr = new byte[4096];
                        long j = 0;
                        String str = r33;
                        while (true) {
                            int read = inputStream.read(bArr);
                            if (read == -1) {
                                try {
                                    fileOutputStream.close();
                                    try {
                                        return compare(sb2);
                                    } catch (Exception e5) {
                                        e5.printStackTrace();
                                        if (inputStream != null) {
                                            inputStream.close();
                                        }
                                        if (httpURLConnection != null) {
                                            httpURLConnection.disconnect();
                                        }
                                        return null;
                                    }
                                } catch (IOException unused4) {
                                }
                            } else if (isCancelled()) {
                                inputStream.close();
                                try {
                                    fileOutputStream.close();
                                    try {
                                        return compare(sb2);
                                    } catch (Exception e6) {
                                        e6.printStackTrace();
                                        if (inputStream != null) {
                                            inputStream.close();
                                        }
                                        if (httpURLConnection != null) {
                                            httpURLConnection.disconnect();
                                        }
                                        return str;
                                    }
                                } catch (IOException unused5) {
                                }
                            } else {
                                j += (long) read;
                                if (contentLength > 0) {
                                    publishProgress(new Integer[]{Integer.valueOf((int) ((100 * j) / ((long) contentLength)))});
                                }
                                fileOutputStream.write(bArr, 0, read);
                                str = 0;
                            }
                        }
                    } catch (Exception e7) {
                        e = e7;
                        r32 = fileOutputStream;
                        String exc2 = e.toString();
                        if (r32 != 0) {
                        }
                        return compare(sb2);
                    } catch (Throwable th3) {
                        th = th3;
                        r3 = fileOutputStream;
                        if (r3 != 0) {
                        }
                        return compare(sb2);
                    }
                }
            } catch (Exception e8) {
                e = e8;
                inputStream = null;
                r32 = r33;
                String exc22 = e.toString();
                if (r32 != 0) {
                }
                return compare(sb2);
            } catch (Throwable th4) {
                th = th4;
                inputStream = null;
                r3 = r33;
                if (r3 != 0) {
                }
                return compare(sb2);
            }
        } catch (Exception e9) {
            e = e9;
            httpURLConnection = null;
            inputStream = null;
            r32 = r33;
            String exc222 = e.toString();
            if (r32 != 0) {
            }
            return compare(sb2);
        } catch (Throwable th5) {
            th = th5;
            httpURLConnection = null;
            inputStream = null;
            r3 = r33;
            if (r3 != 0) {
            }
            return compare(sb2);
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(String str) {
        TextView textView = (TextView) this.activity.findViewById(R.id.tv_scroll_text);
        if (str.equals("YELLOW")) {
            textView.setBackgroundColor(-16711936);
            showDialog("Don't panic everything looks good but we are analysing your report for more accurate feedback .");
            new UploadBluetoothTask().execute(new Void[0]);
            new UploadLocationTask().execute(new Void[0]);
            this.mApiManager.getAllupdateflag(new MyPreferences(this.activity).getPref(MyPreferences.PREF_MOBILE_NUMBER, ""), DiskLruCache.VERSION_1, "4", "blth");
        } else if (str.equals("GREEN")) {
            textView.setBackgroundColor(-16711936);
            textView.setText("CODE GREEN: ");
            showDialog("ALL is Well");
        }
    }

    private void showDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setTitle((CharSequence) "YOUR REPORT");
        builder.setMessage((CharSequence) str).setPositiveButton((CharSequence) "OK", (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog create = builder.create();
        create.setCancelable(false);
        create.show();
    }

    public void isSuccess(Object obj, int i) {
        String str = "Please take survey and see a doctor at your convenience if have any symptoms";
        String str2 = "All is Well";
        String str3 = "";
        String str4 = MyPreferences.PREF_MOBILE_NUMBER;
        String str5 = DiskLruCache.VERSION_1;
        switch (i) {
            case 104:
                GetUserID getUserID = (GetUserID) obj;
                if (getUserID == null || getUserID.getStatusCode() == null || getUserID.getMessage().equalsIgnoreCase("success")) {
                    this.Flag1 = getUserID.getRows().getFlag1();
                    this.Flag2 = getUserID.getRows().getFlag2();
                    String flag3 = getUserID.getRows().getFlag3();
                    this.Flag3 = flag3;
                    if (flag3.equals(str5)) {
                        this.mApiManager.getUserIDRequest(new MyPreferences(this.activity).getPref(str4, str3));
                    }
                    if (this.Flag1.equals(str5)) {
                        showDialog(str);
                    } else {
                        showDialog(str2);
                    }
                    if (this.Flag2.equals(str5)) {
                        showDialog("Quarantine with 14 Days till quarantine.");
                        PreferenceManager.write(PreferenceManager.QURENTINE, str5);
                        PreferenceManager.write(PreferenceManager.HOME_LOCATION, str5);
                        return;
                    }
                    return;
                }
                return;
            case 105:
                getAllupdateflag getallupdateflag = (getAllupdateflag) obj;
                if (getallupdateflag != null && getallupdateflag.getStatusCode().equals(str5)) {
                    this.updatedFlag1 = getallupdateflag.getRows().getFlag1();
                    this.updatedFlag2 = getallupdateflag.getRows().getFlag2();
                    this.updatedFlag3 = getallupdateflag.getRows().getFlag3();
                    if (this.updatedFlag1.equals(str5)) {
                        showDialog(str);
                    } else {
                        showDialog(str2);
                    }
                    if (this.updatedFlag2.equals(str5)) {
                        showDialog("Please set your quarantine zone, after pressing the location button");
                        UpdateAPIListener updateAPIListener = this.mListener;
                        if (updateAPIListener != null) {
                            updateAPIListener.onUpdateAPIResult();
                        }
                    }
                    if (this.updatedFlag3.equals(str5)) {
                        this.mApiManager.getUserIDRequest(new MyPreferences(this.activity).getPref(str4, str3));
                        return;
                    }
                    return;
                }
                return;
            case 106:
                SaveUserDetails saveUserDetails = (SaveUserDetails) obj;
                if (saveUserDetails != null && saveUserDetails.getStatusCode().equals(str5)) {
                    saveUserDetails.getRows().getIsactive();
                    saveUserDetails.getRows().getQuarantine_arr_ppl_count();
                    saveUserDetails.getRows().getQuarantine_curr_location();
                    return;
                }
                return;
            default:
                return;
        }
    }
}
