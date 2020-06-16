package com.cosafe.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender.SendIntentException;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build.VERSION;
import android.os.Looper;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsRequest.Builder;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class AppLocationManager {
    public static final String TAG = AppLocationManager.class.getSimpleName();
    private Activity mActivity;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mIsContinuous;
    private LocationListener mListener;
    private LocationCallback mLocationCallback = new LocationCallback() {
        public void onLocationResult(LocationResult locationResult) {
            Location lastLocation = locationResult.getLastLocation();
            if (lastLocation != null) {
                AppLocationManager.this.onLocationChanged(lastLocation);
            }
        }
    };

    public interface LocationListener {
        void onContinuousLocationUpdate(Location location);

        void onLocationError();

        void onLocationPermissionOFF();

        void onLocationSuccess(Location location);
    }

    public AppLocationManager(Activity activity, boolean z) {
        this.mActivity = activity;
        this.mListener = (LocationListener) activity;
        this.mIsContinuous = z;
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public void init() {
        if (!isLocationPermissionGranted(this.mActivity)) {
            LocationListener locationListener = this.mListener;
            if (locationListener != null) {
                locationListener.onLocationPermissionOFF();
            }
        } else if (!isGpsEnabled()) {
            turnOnGPS();
        } else {
            requestNewLocationData();
        }
    }

    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) this.mActivity.getSystemService("location");
        return locationManager != null && locationManager.isProviderEnabled("gps");
    }

    private void turnOnGPS() {
        SettingsClient settingsClient = LocationServices.getSettingsClient(this.mActivity);
        LocationRequest create = LocationRequest.create();
        create.setPriority(100);
        create.setInterval(10000);
        create.setFastestInterval(2000);
        Builder addLocationRequest = new Builder().addLocationRequest(create);
        LocationSettingsRequest build = addLocationRequest.build();
        addLocationRequest.setAlwaysShow(true);
        settingsClient.checkLocationSettings(build).addOnSuccessListener(this.mActivity, (OnSuccessListener<? super TResult>) new OnSuccessListener() {
            public final void onSuccess(Object obj) {
                AppLocationManager.this.lambda$turnOnGPS$0$AppLocationManager((LocationSettingsResponse) obj);
            }
        }).addOnFailureListener(this.mActivity, (OnFailureListener) new OnFailureListener() {
            public final void onFailure(Exception exc) {
                AppLocationManager.this.lambda$turnOnGPS$1$AppLocationManager(exc);
            }
        });
    }

    public /* synthetic */ void lambda$turnOnGPS$0$AppLocationManager(LocationSettingsResponse locationSettingsResponse) {
        init();
    }

    public /* synthetic */ void lambda$turnOnGPS$1$AppLocationManager(Exception exc) {
        int statusCode = ((ApiException) exc).getStatusCode();
        if (statusCode == 6) {
            try {
                ((ResolvableApiException) exc).startResolutionForResult(this.mActivity, Constants.REQUEST_CHECK_SETTINGS);
            } catch (SendIntentException e) {
                e.printStackTrace();
                LocationListener locationListener = this.mListener;
                if (locationListener != null) {
                    locationListener.onLocationError();
                }
            }
        } else if (statusCode == 8502) {
            LocationListener locationListener2 = this.mListener;
            if (locationListener2 != null) {
                locationListener2.onLocationError();
            }
        }
    }

    private void requestNewLocationData() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(100);
        locationRequest.setInterval(60000);
        locationRequest.setFastestInterval(60000);
        this.mFusedLocationClient.requestLocationUpdates(locationRequest, this.mLocationCallback, Looper.myLooper());
    }

    /* access modifiers changed from: private */
    public void onLocationChanged(Location location) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Lat : ");
        sb.append(location.getLatitude());
        sb.append(" Long : ");
        sb.append(location.getLongitude());
        Log.e(str, sb.toString());
        if (!this.mIsContinuous) {
            stopLocationUpdates();
            LocationListener locationListener = this.mListener;
            if (locationListener != null) {
                locationListener.onLocationSuccess(location);
                return;
            }
            return;
        }
        LocationListener locationListener2 = this.mListener;
        if (locationListener2 != null) {
            locationListener2.onContinuousLocationUpdate(location);
        }
    }

    public void stopLocationUpdates() {
        FusedLocationProviderClient fusedLocationProviderClient = this.mFusedLocationClient;
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(this.mLocationCallback);
        }
    }

    private boolean isLocationPermissionGranted(Context context) {
        return VERSION.SDK_INT < 23 || (ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION") == 0 && ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_COARSE_LOCATION") == 0);
    }
}
