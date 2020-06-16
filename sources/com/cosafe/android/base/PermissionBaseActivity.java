package com.cosafe.android.base;

import androidx.core.app.ActivityCompat;
import com.cosafe.android.utils.Constants;

public abstract class PermissionBaseActivity extends BaseActivity {
    public abstract void onPermissionChanged(int i, boolean z);

    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 200);
    }

    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, Constants.REQUEST_STORAGE_PERMISSION);
    }

    public void requestBluetoothPermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.BLUETOOTH", "android.permission.BLUETOOTH_ADMIN"}, Constants.REQUEST_BLUETOOTH_PERMISSION);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 200) {
            if (i != 201) {
                if (i == 301) {
                    if (iArr.length <= 0 || iArr[0] != 0) {
                        onPermissionChanged(Constants.REQUEST_BLUETOOTH_PERMISSION, false);
                    } else {
                        onPermissionChanged(Constants.REQUEST_BLUETOOTH_PERMISSION, true);
                    }
                }
            } else if (iArr.length <= 0 || iArr[0] != 0) {
                onPermissionChanged(Constants.REQUEST_STORAGE_PERMISSION, false);
            } else {
                onPermissionChanged(Constants.REQUEST_STORAGE_PERMISSION, true);
            }
        } else if (iArr.length <= 0 || iArr[0] != 0) {
            onPermissionChanged(200, false);
        } else {
            onPermissionChanged(200, true);
        }
    }
}
