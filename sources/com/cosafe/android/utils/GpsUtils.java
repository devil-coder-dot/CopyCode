package com.cosafe.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender.SendIntentException;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsRequest.Builder;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class GpsUtils {
    /* access modifiers changed from: private */
    public Context context;
    private LocationManager locationManager;
    private LocationRequest locationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private SettingsClient mSettingsClient;

    public interface onGpsListener {
        void gpsStatus(boolean z);
    }

    public GpsUtils(Context context2) {
        this.context = context2;
        this.locationManager = (LocationManager) context2.getSystemService("location");
        this.mSettingsClient = LocationServices.getSettingsClient(context2);
        LocationRequest create = LocationRequest.create();
        this.locationRequest = create;
        create.setPriority(100);
        this.locationRequest.setInterval(10000);
        this.locationRequest.setFastestInterval(2000);
        Builder addLocationRequest = new Builder().addLocationRequest(this.locationRequest);
        this.mLocationSettingsRequest = addLocationRequest.build();
        addLocationRequest.setAlwaysShow(true);
    }

    public void turnGPSOn(final onGpsListener ongpslistener) {
        if (!this.locationManager.isProviderEnabled("gps")) {
            this.mSettingsClient.checkLocationSettings(this.mLocationSettingsRequest).addOnSuccessListener((Activity) this.context, (OnSuccessListener<? super TResult>) new OnSuccessListener<LocationSettingsResponse>() {
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    onGpsListener ongpslistener = ongpslistener;
                    if (ongpslistener != null) {
                        ongpslistener.gpsStatus(true);
                    }
                }
            }).addOnFailureListener((Activity) this.context, (OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception exc) {
                    int statusCode = ((ApiException) exc).getStatusCode();
                    String str = "ContentValues";
                    if (statusCode == 6) {
                        try {
                            ((ResolvableApiException) exc).startResolutionForResult((Activity) GpsUtils.this.context, 1001);
                        } catch (SendIntentException unused) {
                            Log.i(str, "PendingIntent unable to execute request.");
                        }
                    } else if (statusCode == 8502) {
                        String str2 = "Location settings are inadequate, and cannot be fixed here. Fix in Settings.";
                        Log.e(str, str2);
                        Toast.makeText(GpsUtils.this.context, str2, 1).show();
                    }
                }
            });
        } else if (ongpslistener != null) {
            ongpslistener.gpsStatus(true);
        }
    }
}
