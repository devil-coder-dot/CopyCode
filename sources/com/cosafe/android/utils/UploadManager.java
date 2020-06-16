package com.cosafe.android.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadManager extends AsyncTask<String, Void, String> {
    public static final String FILE_NAME = "bluetooth_file.txt";
    public static final String FOLDER_NAME = "SafeTogetherLogger";
    private static final String TAG = "Upload";
    private Context context;

    /* access modifiers changed from: protected */
    public void onPostExecute(String str) {
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(Void... voidArr) {
    }

    public UploadManager(Context context2) {
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public String doInBackground(String... strArr) {
        String str = TAG;
        try {
            Log.v(str, "no");
            String str2 = "\r\n";
            String str3 = "--";
            String str4 = "*****";
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "SafeTogetherLogger");
            StringBuilder sb = new StringBuilder();
            sb.append(file);
            sb.append(File.separator);
            sb.append("bluetooth_file.txt");
            String sb2 = sb.toString();
            File file2 = new File(sb2);
            Log.v(str, sb2);
            if (file2.isFile()) {
                try {
                    MyPreferences myPreferences = new MyPreferences(this.context);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("https://test1.ncog.gov.in/CoSafe/uploadfile?mobile=");
                    sb3.append(myPreferences.getPref(MyPreferences.PREF_MOBILE_NUMBER, ""));
                    sb3.append("&type=latlon");
                    String sb4 = sb3.toString();
                    FileInputStream fileInputStream = new FileInputStream(file2);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(sb4).openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                    httpURLConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("multipart/form-data;boundary=");
                    sb5.append(str4);
                    httpURLConnection.setRequestProperty("Content-Type", sb5.toString());
                    httpURLConnection.setRequestProperty("bill", sb2);
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(str3);
                    sb6.append(str4);
                    sb6.append(str2);
                    dataOutputStream.writeBytes(sb6.toString());
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("Content-Disposition: form-data; name=\"filename\";filename=\"");
                    sb7.append(sb2);
                    sb7.append("\"");
                    sb7.append(str2);
                    dataOutputStream.writeBytes(sb7.toString());
                    dataOutputStream.writeBytes(str2);
                    int min = Math.min(fileInputStream.available(), 1048576);
                    byte[] bArr = new byte[min];
                    int read = fileInputStream.read(bArr, 0, min);
                    while (read > 0) {
                        dataOutputStream.write(bArr, 0, min);
                        min = Math.min(fileInputStream.available(), 1048576);
                        read = fileInputStream.read(bArr, 0, min);
                    }
                    dataOutputStream.writeBytes(str2);
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(str3);
                    sb8.append(str4);
                    sb8.append(str3);
                    sb8.append(str2);
                    dataOutputStream.writeBytes(sb8.toString());
                    int responseCode = httpURLConnection.getResponseCode();
                    String responseMessage = httpURLConnection.getResponseMessage();
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append("serverResponseCode................");
                    sb9.append(responseCode);
                    Log.v(str, sb9.toString());
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append("serverResponseMessage................");
                    sb10.append(responseMessage);
                    Log.v(str, sb10.toString());
                    if (responseCode == 200) {
                        Log.v(str, "200");
                    }
                    fileInputStream.close();
                    dataOutputStream.flush();
                    dataOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return "Executed";
    }
}
