package com.cosafe.android.utils;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PorterDuff.Mode;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.cosafe.android.R;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;
import java.util.Locale;

public class Utility {
    private static final String TAG = Utility.class.getSimpleName();

    public static boolean isNetworkConnectivityExists(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showSnackBar(View view, String str) {
        Snackbar.make(view, (CharSequence) str, -1).show();
    }

    public static void showSnackBar(View view, int i) {
        Snackbar.make(view, i, -1).show();
    }

    public static void showCustomToast(Context context, String str, int i) {
        Toast makeText = Toast.makeText(context, str, 0);
        View view = makeText.getView();
        if (i == 120) {
            view.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorToastSuccess), Mode.SRC_IN);
        } else if (i == 121) {
            view.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorToastError), Mode.SRC_IN);
        }
        ((TextView) view.findViewById(16908299)).setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        makeText.show();
    }

    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, 0).show();
    }

    public static void showToast(Context context, int i) {
        Toast.makeText(context, i, 0).show();
    }

    public static String getCityName(Context context, double d, double d2) {
        try {
            List fromLocation = new Geocoder(context, Locale.getDefault()).getFromLocation(d, d2, 1);
            if (fromLocation.size() > 0) {
                return ((Address) fromLocation.get(0)).getLocality();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isStoragePermissionGranted(Context context) {
        return VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public static boolean isLocationPermissionGranted(Context context) {
        return VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_COARSE_LOCATION") == 0;
    }

    public static boolean isBlueToothPermissionGranted(Context context) {
        return VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(context, "android.permission.BLUETOOTH") == 0 || ContextCompat.checkSelfPermission(context, "android.permission.BLUETOOTH_ADMIN") == 0;
    }

    public static boolean isValidEmail(CharSequence charSequence) {
        return !TextUtils.isEmpty(charSequence) && Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
    }

    public static String getDeviceId(Context context) {
        String str;
        try {
            str = Secure.getString(context.getContentResolver(), "android_id");
        } catch (Exception unused) {
            str = "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("DEVICE ID : ");
        sb.append(str);
        Log.d("Utility", sb.toString());
        return str;
    }

    public static void dismissKeyboard(Context context, View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService("input_method");
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAlertDialog(Context context, String str) {
        new Builder(context).setMessage(str).setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Version Number exception :");
            sb.append(e.toString());
            Log.e(str, sb.toString());
            return null;
        }
    }

    public static String getPackageName(Context context) {
        String packageName = context.getPackageName();
        StringBuilder sb = new StringBuilder();
        sb.append("packageName............");
        sb.append(packageName);
        Log.d("Utility", sb.toString());
        return packageName;
    }
}
