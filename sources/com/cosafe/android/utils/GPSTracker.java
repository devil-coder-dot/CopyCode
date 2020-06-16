package com.cosafe.android.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GPSTracker extends Service {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000;
    private static final long MIN_TIME_BW_UPDATES = 60000;
    Activity activity;
    boolean canGetLocation = false;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    double latitude;
    Location location;
    protected LocationManager locationManager;
    double longitude;
    /* access modifiers changed from: private */
    public Context mContext;
    private final LocationListener mLocationListener = new LocationListener() {
        public void onProviderDisabled(String str) {
        }

        public void onProviderEnabled(String str) {
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
        }

        public void onLocationChanged(Location location) {
            if (location != null) {
                GPSTracker.this.latitude = location.getLatitude();
                GPSTracker.this.longitude = location.getLongitude();
            }
        }
    };

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void stopUsingGPS() {
    }

    public GPSTracker() {
    }

    public GPSTracker(Context context, Activity activity2) {
        this.mContext = context;
        this.activity = activity2;
        getLocation();
    }

    public Location getLocation() {
        String str = "GPS Enabled";
        String str2 = "android.permission.ACCESS_FINE_LOCATION";
        String str3 = "Network";
        String str4 = "network";
        String str5 = "gps";
        try {
            LocationManager locationManager2 = (LocationManager) this.mContext.getSystemService("location");
            this.locationManager = locationManager2;
            this.isGPSEnabled = locationManager2.isProviderEnabled(str5);
            boolean isProviderEnabled = this.locationManager.isProviderEnabled(str4);
            this.isNetworkEnabled = isProviderEnabled;
            if (this.isGPSEnabled || isProviderEnabled) {
                this.canGetLocation = true;
                if (this.isNetworkEnabled) {
                    this.locationManager.requestLocationUpdates("network", MIN_TIME_BW_UPDATES, 1000.0f, this.mLocationListener);
                    Log.d(str3, str3);
                    if (this.locationManager != null) {
                        Location lastKnownLocation = this.locationManager.getLastKnownLocation(str4);
                        this.location = lastKnownLocation;
                        if (lastKnownLocation != null) {
                            this.latitude = lastKnownLocation.getLatitude();
                            this.longitude = this.location.getLongitude();
                        }
                    }
                }
            }
            if (this.isGPSEnabled && this.location == null) {
                if (ContextCompat.checkSelfPermission(this.activity, str2) == 0 || ActivityCompat.checkSelfPermission(this.activity, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                    this.locationManager.requestLocationUpdates("gps", MIN_TIME_BW_UPDATES, 1000.0f, this.mLocationListener);
                    Log.d(str, str);
                    if (this.locationManager != null) {
                        Location lastKnownLocation2 = this.locationManager.getLastKnownLocation(str5);
                        this.location = lastKnownLocation2;
                        if (lastKnownLocation2 != null) {
                            this.latitude = lastKnownLocation2.getLatitude();
                            this.longitude = this.location.getLongitude();
                        }
                    }
                } else {
                    ActivityCompat.requestPermissions(this.activity, new String[]{str2}, 50);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.location;
    }

    public double getLatitude() {
        Location location2 = this.location;
        if (location2 != null) {
            this.latitude = location2.getLatitude();
        }
        return this.latitude;
    }

    public double getLongitude() {
        Location location2 = this.location;
        if (location2 != null) {
            this.longitude = location2.getLongitude();
        }
        return this.longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        Builder builder = new Builder(this.mContext);
        builder.setTitle("GPS is settings");
        builder.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        builder.setPositiveButton("Settings", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                GPSTracker.this.mContext.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        });
        builder.setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}
