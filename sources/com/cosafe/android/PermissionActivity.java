package com.cosafe.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import com.cosafe.android.base.PermissionBaseActivity;
import com.cosafe.android.databinding.ActivityPermissionBinding;
import com.cosafe.android.utils.PreferenceManager;
import com.cosafe.android.utils.Utility;

public class PermissionActivity extends PermissionBaseActivity implements OnClickListener {
    private ActivityPermissionBinding binding;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = (ActivityPermissionBinding) DataBindingUtil.setContentView(this, R.layout.activity_permission);
    }

    public void onClick(View view) {
        if (view.getId() != R.id.tv_get_started) {
            return;
        }
        if (Utility.isLocationPermissionGranted(this)) {
            requestStoragePermission();
        } else if (Utility.isStoragePermissionGranted(this)) {
            requestBluetoothPermission();
        } else {
            requestLocationPermission();
        }
    }

    public void onPermissionChanged(int i, boolean z) {
        String str = "Permission Denied. Cannot Proceed";
        if (i == 200) {
            if (z) {
                requestStoragePermission();
            } else {
                Toast.makeText(this, str, 0).show();
            }
        } else if (i != 201) {
        } else {
            if (!z) {
                Toast.makeText(this, str, 0).show();
            } else if (PreferenceManager.isLoggedIn()) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }
    }
}
