package com.cosafe.android;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.cosafe.android.base.PermissionBaseActivity;
import com.cosafe.android.databinding.ActivityMainBinding;
import com.cosafe.android.dialog.DownloadDialog;
import com.cosafe.android.dialog.LocationDialog;
import com.cosafe.android.models.GetAllDownLoadFiles;
import com.cosafe.android.models.GetAllDownloadData;
import com.cosafe.android.models.LocationBean;
import com.cosafe.android.service.AppService;
import com.cosafe.android.utils.AppLocationManager;
import com.cosafe.android.utils.AppLocationManager.LocationListener;
import com.cosafe.android.utils.BluetoothManager;
import com.cosafe.android.utils.DownloadDataManager;
import com.cosafe.android.utils.DownloadDataManager.UpdateAPIListener;
import com.cosafe.android.utils.DownloadLocationDataManager;
import com.cosafe.android.utils.GpsUtils;
import com.cosafe.android.utils.GpsUtils.onGpsListener;
import com.cosafe.android.utils.LocationFeedback;
import com.cosafe.android.utils.PreferenceManager;
import com.cosafe.android.utils.Utility;
import com.cosafe.android.utils.retrofit.RetrofitAPIManager;
import com.cosafe.android.utils.retrofit.RetrofitResponseListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.internal.cache.DiskLruCache;

public class MainActivity extends PermissionBaseActivity implements LocationListener, OnClickListener, RetrofitResponseListener {
    private static final String BASE_URL = "https://test1.ncog.gov.in/";
    private static final int DIALOG_ONE = 100;
    private static final int DIALOG_TWO = 101;
    /* access modifiers changed from: private */
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String WEB_VIEW_URL = "https://api.ncog.gov.in/files/index/";
    private AppLocationManager appLocationManager;
    /* access modifiers changed from: private */
    public ActivityMainBinding binding;
    private DownloadDialog dialog;
    private DownloadDataManager downloadDataManager;
    private DownloadLocationDataManager downloadLocationDataManager;
    /* access modifiers changed from: private */
    public boolean errorReceived = false;
    private List<GetAllDownLoadFiles> getAllDownLoadFilesList;
    private String homelocation;
    /* access modifiers changed from: private */
    public boolean isContinue = false;
    /* access modifiers changed from: private */
    public boolean isGPS = false;
    /* access modifiers changed from: private */
    public LocationCallback locationCallback;
    /* access modifiers changed from: private */
    public LocationRequest locationRequest;
    private RetrofitAPIManager mApiManager;
    private int mDialogType;
    /* access modifiers changed from: private */
    public FusedLocationProviderClient mFusedLocationClient;
    private LocationDialog mLocationFetchDialog;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.adapter.action.STATE_CHANGED") && intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE) == 13) {
                MainActivity.this.setBluetooth(true);
            }
        }
    };
    private MyCountDownTimer myCountDownTimer;
    private String quarentineFlag;
    /* access modifiers changed from: private */
    public double wayAccuracy = 0.0d;
    /* access modifiers changed from: private */
    public double wayBearing = 0.0d;
    /* access modifiers changed from: private */
    public double wayLatitude = 0.0d;
    /* access modifiers changed from: private */
    public double wayLongitude = 0.0d;

    private class MyCountDownTimer extends CountDownTimer {
        MyCountDownTimer(long j, long j2) {
            super(j, j2);
        }

        public void onTick(long j) {
            String format = String.format("%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(j))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j)))});
            if (MainActivity.this.binding.tvCounter != null) {
                MainActivity.this.binding.tvCounter.setText(format);
                MainActivity.this.binding.layoutGifLogo.setClickable(false);
                MainActivity.this.binding.layoutGifLogo.setEnabled(false);
            }
        }

        public void onFinish() {
            if (MainActivity.this.binding.tvCounter != null) {
                MainActivity.this.binding.tvCounter.setText("00:00");
                MainActivity.this.binding.layoutGifLogo.setClickable(true);
                MainActivity.this.binding.layoutGifLogo.setEnabled(true);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = (ActivityMainBinding) DataBindingUtil.setContentView(this, R.layout.activity_main);
        Glide.with((FragmentActivity) this).asGif().load(Integer.valueOf(R.drawable.ic_logo_gif)).into(this.binding.ivLogo);
        this.mApiManager = new RetrofitAPIManager(this, this);
        startService();
        setBluetooth(true);
        registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
        String str = "0";
        this.quarentineFlag = PreferenceManager.read(PreferenceManager.QURENTINE, str);
        this.homelocation = PreferenceManager.read(PreferenceManager.HOME_LOCATION, str);
        initViews();
        loadWebView();
        showIndicatorBtnBlinkAnimation();
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient((Activity) this);
        LocationRequest create = LocationRequest.create();
        this.locationRequest = create;
        create.setPriority(102);
        this.locationRequest.setInterval(10000);
        this.locationRequest.setFastestInterval(5000);
        new GpsUtils(this).turnGPSOn(new onGpsListener() {
            public final void gpsStatus(boolean z) {
                MainActivity.this.lambda$onCreate$0$MainActivity(z);
            }
        });
        this.locationCallback = new LocationCallback() {
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            MainActivity.this.wayLatitude = location.getLatitude();
                            MainActivity.this.wayLongitude = location.getLongitude();
                            MainActivity.this.wayAccuracy = (double) location.getAccuracy();
                            MainActivity.this.wayBearing = (double) location.getBearing();
                            MainActivity.this.isContinue;
                            if (!MainActivity.this.isContinue && MainActivity.this.mFusedLocationClient != null) {
                                MainActivity.this.mFusedLocationClient.removeLocationUpdates(MainActivity.this.locationCallback);
                            }
                        }
                    }
                }
            }
        };
        if (Utility.isStoragePermissionGranted(this)) {
            initLocationManager();
        } else {
            requestStoragePermission();
        }
        if (!PreferenceManager.isInstructionShown()) {
            this.mDialogType = 100;
            showSingleButtonDialog(this, null, getString(R.string.dialog_msg_1), "Ok", false);
        }
    }

    public /* synthetic */ void lambda$onCreate$0$MainActivity(boolean z) {
        this.isGPS = z;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
        super.onResume();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_retry) {
            this.errorReceived = false;
            this.binding.webView.loadUrl(WEB_VIEW_URL);
            showLoading();
        } else if (id != R.id.layout_upload) {
            switch (id) {
                case R.id.layout_gif_logo /*2131296449*/:
                    try {
                        showDownloadDialog();
                        MyCountDownTimer myCountDownTimer2 = new MyCountDownTimer(7200000, 1000);
                        this.myCountDownTimer = myCountDownTimer2;
                        myCountDownTimer2.start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getLocationLatLog();
                    this.mApiManager.downloadRequest("0");
                    return;
                case R.id.layout_indicator_btn /*2131296450*/:
                    showConfirmationDialog(null, getString(R.string.dialog_set_location_msg), getString(R.string.label_set_now), getString(R.string.label_not_now), true);
                    return;
                case R.id.layout_more_option /*2131296451*/:
                    startActivity(new Intent(this, MoreOptionActivity.class));
                    return;
                default:
                    return;
            }
        } else {
            startActivity(new Intent(this, UploadActivityNew.class));
        }
    }

    public void startService() {
        ContextCompat.startForegroundService(this, new Intent(this, AppService.class));
    }

    private void initViews() {
        this.binding.webView.getSettings().setJavaScriptEnabled(true);
        this.binding.webView.getSettings().setDomStorageEnabled(true);
        this.binding.tvScrollText.setText("CODE GREEN: You are all good");
        this.binding.layoutIndicatorBtn.setVisibility(8);
        showLoading();
    }

    private void loadWebView() {
        this.binding.webView.loadUrl(WEB_VIEW_URL);
        this.binding.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                Log.e(MainActivity.TAG, str);
                if (str.startsWith("http:") || str.startsWith("https:")) {
                    webView.loadUrl(str);
                } else if (str.startsWith("tel:")) {
                    MainActivity.this.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(str)));
                } else if (str.startsWith("mailto:")) {
                    MailTo parse = MailTo.parse(str);
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("application/octet-stream");
                    intent.putExtra("android.intent.extra.EMAIL", new String[]{parse.getTo()});
                    intent.putExtra("android.intent.extra.SUBJECT", parse.getSubject());
                    intent.putExtra("android.intent.extra.TEXT", parse.getBody());
                    MainActivity.this.startActivity(intent);
                } else if (str.startsWith("geo:")) {
                    Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(str));
                    intent2.setPackage("com.google.android.apps.maps");
                    MainActivity.this.startActivity(intent2);
                }
                return true;
            }

            public void onReceivedError(WebView webView, int i, String str, String str2) {
                Log.e(MainActivity.TAG, "onReceivedError");
                MainActivity.this.errorReceived = true;
                MainActivity.this.showRetryView();
            }

            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                Log.e(MainActivity.TAG, "onReceivedError");
                MainActivity.this.errorReceived = true;
                onReceivedError(webView, webResourceError.getErrorCode(), webResourceError.getDescription().toString(), webResourceRequest.getUrl().toString());
                MainActivity.this.showRetryView();
            }

            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                if (!MainActivity.this.errorReceived) {
                    MainActivity.this.binding.loader.setVisibility(8);
                    MainActivity.this.binding.webView.setVisibility(0);
                    MainActivity.this.binding.layoutRetry.setVisibility(8);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void showRetryView() {
        this.binding.layoutRetry.setVisibility(0);
        this.binding.loader.setVisibility(8);
        this.binding.webView.setVisibility(8);
    }

    private void showLoading() {
        this.binding.layoutRetry.setVisibility(8);
        this.binding.loader.setVisibility(0);
        this.binding.webView.setVisibility(8);
    }

    private void showIndicatorBtnBlinkAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setRepeatCount(-1);
        alphaAnimation.setRepeatMode(2);
        this.binding.layoutIndicatorBtn.startAnimation(alphaAnimation);
    }

    private void initLocationManager() {
        AppLocationManager appLocationManager2 = new AppLocationManager(this, true);
        this.appLocationManager = appLocationManager2;
        appLocationManager2.init();
    }

    public void initDownload(String str) {
        this.downloadDataManager = new DownloadDataManager(this, new UpdateAPIListener() {
            public final void onUpdateAPIResult() {
                MainActivity.this.lambda$initDownload$1$MainActivity();
            }
        });
        if (this.getAllDownLoadFilesList.size() > 0) {
            DownloadDataManager downloadDataManager2 = this.downloadDataManager;
            StringBuilder sb = new StringBuilder();
            sb.append(BASE_URL);
            sb.append(str);
            downloadDataManager2.execute(new String[]{sb.toString()});
        }
    }

    public /* synthetic */ void lambda$initDownload$1$MainActivity() {
        this.binding.layoutIndicatorBtn.setVisibility(0);
    }

    public void initLocationDownload(String str) {
        this.downloadLocationDataManager = new DownloadLocationDataManager(this);
        if (this.getAllDownLoadFilesList.size() > 0) {
            DownloadLocationDataManager downloadLocationDataManager2 = this.downloadLocationDataManager;
            StringBuilder sb = new StringBuilder();
            sb.append(BASE_URL);
            sb.append(str);
            downloadLocationDataManager2.execute(new String[]{sb.toString()});
        }
    }

    private void showDownloadDialog() throws InterruptedException {
        DownloadDialog downloadDialog = new DownloadDialog(this);
        this.dialog = downloadDialog;
        downloadDialog.show();
    }

    public void onSingleButtonDialogButtonClick() {
        int i = this.mDialogType;
        if (i == 100) {
            this.mDialogType = 101;
            showSingleButtonDialog(this, null, getString(R.string.dialog_msg_2), "Ok", false);
        } else if (i == 101) {
            showInstructionDialog();
        }
    }

    public void onPositiveBtnClick() {
        LocationDialog locationDialog = new LocationDialog(this);
        this.mLocationFetchDialog = locationDialog;
        locationDialog.show();
        new Handler().postDelayed(new Runnable() {
            public final void run() {
                MainActivity.this.lambda$onPositiveBtnClick$2$MainActivity();
            }
        }, 1500);
    }

    public /* synthetic */ void lambda$onPositiveBtnClick$2$MainActivity() {
        AppLocationManager appLocationManager2 = new AppLocationManager(this, false);
        this.appLocationManager = appLocationManager2;
        appLocationManager2.init();
    }

    private void closeLocationFetchDialog() {
        LocationDialog locationDialog = this.mLocationFetchDialog;
        if (locationDialog != null && locationDialog.isShowing()) {
            this.mLocationFetchDialog.dismiss();
        }
    }

    public void onLocationPermissionOFF() {
        requestLocationPermission();
        closeLocationFetchDialog();
    }

    public void onLocationError() {
        Toast.makeText(this, "Error fetching location", 0).show();
        closeLocationFetchDialog();
    }

    public void onLocationSuccess(Location location) {
        closeLocationFetchDialog();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        double accuracy = (double) location.getAccuracy();
        LocationBean locationBean = new LocationBean();
        locationBean.setLat(String.valueOf(latitude));
        locationBean.setLog(String.valueOf(longitude));
        locationBean.setAccuracy(String.valueOf(accuracy));
        locationBean.setHome_location(this.homelocation);
        locationBean.setQurentine(this.quarentineFlag);
    }

    public void onContinuousLocationUpdate(Location location) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onLocationSuccess: lat : ");
        sb.append(location.getLatitude());
        sb.append(" lon : ");
        sb.append(location.getLongitude());
        Log.e(str, sb.toString());
        String str2 = "0";
        if (!this.quarentineFlag.equals(str2) || !this.homelocation.equals(str2)) {
            String str3 = this.quarentineFlag;
            String str4 = DiskLruCache.VERSION_1;
            if (str3.equals(str4) && this.homelocation.equals(str2)) {
                try {
                    BluetoothManager.getManager(this).getContactCount();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e2) {
                    e2.printStackTrace();
                }
            } else if (this.quarentineFlag.equals(str4) && this.homelocation.equals(str4)) {
                DownloadLocationDataManager.geoDistance(location.getLatitude(), location.getLongitude(), location.getLatitude(), location.getLongitude());
                try {
                    BluetoothManager.getManager(this).getContactCount();
                } catch (IOException e3) {
                    e3.printStackTrace();
                } catch (NoSuchAlgorithmException e4) {
                    e4.printStackTrace();
                }
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.wayLatitude);
        String str5 = " , ";
        sb2.append(str5);
        sb2.append(this.wayLongitude);
        sb2.append(str5);
        sb2.append(this.wayAccuracy);
        sb2.append(str5);
        sb2.append(this.wayBearing);
        sb2.append(str5);
        sb2.append(System.currentTimeMillis());
        LocationFeedback.writeToFile(sb2.toString());
    }

    public void onPermissionChanged(int i, boolean z) {
        if (i == 200) {
            if (z) {
                this.appLocationManager.init();
            } else {
                Toast.makeText(this, "Permission Denied", 0).show();
            }
        } else if (i != 201) {
        } else {
            if (z) {
                initLocationManager();
            } else {
                Toast.makeText(this, "Without permission app wont work", 0).show();
            }
        }
    }

    private void getLocationLatLog() {
        String str = "android.permission.ACCESS_FINE_LOCATION";
        String str2 = "android.permission.ACCESS_COARSE_LOCATION";
        if (ActivityCompat.checkSelfPermission(this, str) != 0 || ActivityCompat.checkSelfPermission(this, str2) != 0) {
            ActivityCompat.requestPermissions(this, new String[]{str, str2}, 420);
        } else if (!this.isGPS) {
            Toast.makeText(this, "Please turn on GPS", 0).show();
            new GpsUtils(this).turnGPSOn(new onGpsListener() {
                public void gpsStatus(boolean z) {
                    MainActivity.this.isGPS = z;
                }
            });
        } else {
            this.isContinue = false;
            getLocation();
            if (this.wayLatitude == 0.0d || this.wayLongitude == 0.0d) {
                getLocationForNavigate();
            }
        }
    }

    private void getLocation() {
        String str = "android.permission.ACCESS_FINE_LOCATION";
        if (ActivityCompat.checkSelfPermission(this, str) != 0) {
            String str2 = "android.permission.ACCESS_COARSE_LOCATION";
            if (ActivityCompat.checkSelfPermission(this, str2) != 0) {
                ActivityCompat.requestPermissions(this, new String[]{str, str2}, 1000);
                return;
            }
        }
        if (this.isContinue) {
            this.mFusedLocationClient.requestLocationUpdates(this.locationRequest, this.locationCallback, null);
        } else {
            this.mFusedLocationClient.getLastLocation().addOnSuccessListener((Activity) this, (OnSuccessListener<? super TResult>) new OnSuccessListener<Location>() {
                public void onSuccess(Location location) {
                    if (location != null) {
                        MainActivity.this.wayLatitude = location.getLatitude();
                        MainActivity.this.wayLongitude = location.getLongitude();
                        MainActivity.this.wayAccuracy = (double) location.getAccuracy();
                        MainActivity.this.wayBearing = (double) location.getBearing();
                        return;
                    }
                    String str = "android.permission.ACCESS_FINE_LOCATION";
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, str) != 0) {
                        String str2 = "android.permission.ACCESS_COARSE_LOCATION";
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, str2) != 0) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{str, str2}, 1000);
                            return;
                        }
                    }
                    MainActivity.this.mFusedLocationClient.requestLocationUpdates(MainActivity.this.locationRequest, MainActivity.this.locationCallback, null);
                }
            });
        }
    }

    private void getLocationForNavigate() {
        String str = "android.permission.ACCESS_FINE_LOCATION";
        if (ActivityCompat.checkSelfPermission(this, str) != 0) {
            String str2 = "android.permission.ACCESS_COARSE_LOCATION";
            if (ActivityCompat.checkSelfPermission(this, str2) != 0) {
                ActivityCompat.requestPermissions(this, new String[]{str, str2}, 1000);
                return;
            }
        }
        if (this.isContinue) {
            this.mFusedLocationClient.requestLocationUpdates(this.locationRequest, this.locationCallback, null);
        } else {
            this.mFusedLocationClient.getLastLocation().addOnSuccessListener((Activity) this, (OnSuccessListener<? super TResult>) new OnSuccessListener<Location>() {
                public void onSuccess(Location location) {
                    if (location != null) {
                        MainActivity.this.wayLatitude = location.getLatitude();
                        MainActivity.this.wayLongitude = location.getLongitude();
                        MainActivity.this.wayAccuracy = (double) location.getAccuracy();
                        MainActivity.this.wayBearing = (double) location.getBearing();
                        return;
                    }
                    String str = "android.permission.ACCESS_FINE_LOCATION";
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, str) != 0) {
                        String str2 = "android.permission.ACCESS_COARSE_LOCATION";
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, str2) != 0) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{str, str2}, 1000);
                            return;
                        }
                    }
                    MainActivity.this.mFusedLocationClient.requestLocationUpdates(MainActivity.this.locationRequest, MainActivity.this.locationCallback, null);
                }
            });
        }
    }

    private void showInstructionDialog() {
        Dialog dialog2 = new Dialog(this);
        dialog2.requestWindowFeature(1);
        dialog2.setContentView(R.layout.dialog_instruction);
        Window window = dialog2.getWindow();
        if (window != null) {
            window.setLayout(-1, -1);
            window.setBackgroundDrawable(new ColorDrawable(0));
        }
        ((RelativeLayout) dialog2.findViewById(R.id.dialog_container)).setOnTouchListener(new OnTouchListener(dialog2) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return this.f$0.dismiss();
            }
        });
        dialog2.show();
        PreferenceManager.write(PreferenceManager.IS_INSTRUCTION_SHOWN, true);
    }

    public boolean setBluetooth(boolean z) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = defaultAdapter.isEnabled();
        if (z && !isEnabled) {
            return defaultAdapter.enable();
        }
        if (z || !isEnabled) {
            return true;
        }
        return defaultAdapter.disable();
    }

    public void isError(String str) {
        Utility.showSnackBar(this.binding.getRoot(), str);
    }

    public void isSuccess(Object obj, int i) {
        if (i == 102) {
            dismissLoader();
            GetAllDownloadData getAllDownloadData = (GetAllDownloadData) obj;
            if (getAllDownloadData != null && getAllDownloadData.getStatusCode().equals(DiskLruCache.VERSION_1)) {
                List<GetAllDownLoadFiles> getAllDownLoadFilesList2 = getAllDownloadData.getGetAllDownLoadFilesList();
                this.getAllDownLoadFilesList = getAllDownLoadFilesList2;
                if (getAllDownLoadFilesList2.size() > 0) {
                    for (int i2 = 0; i2 < this.getAllDownLoadFilesList.size(); i2++) {
                        String filelocation = ((GetAllDownLoadFiles) this.getAllDownLoadFilesList.get(i2)).getFilelocation();
                        String filelocationBluetooth = ((GetAllDownLoadFiles) this.getAllDownLoadFilesList.get(i2)).getFilelocationBluetooth();
                        String str = "";
                        if (filelocationBluetooth != null && !filelocationBluetooth.equals(str)) {
                            initDownload(filelocationBluetooth);
                        }
                        if (filelocation != null && !filelocation.equals(str)) {
                            initLocationDownload(filelocation);
                        }
                    }
                }
            }
        }
    }

    public void onBackPressed() {
        if (!this.binding.webView.isFocused() || !this.binding.webView.canGoBack()) {
            super.onBackPressed();
            finish();
            return;
        }
        this.binding.webView.goBack();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        unregisterReceiver(this.mReceiver);
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        MyCountDownTimer myCountDownTimer2 = this.myCountDownTimer;
        if (myCountDownTimer2 != null) {
            myCountDownTimer2.cancel();
        }
    }
}
