package com.cosafe.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.databinding.DataBindingUtil;
import com.cosafe.android.base.BaseActivity;
import com.cosafe.android.databinding.ActivityMoreOptionBinding;
import com.cosafe.android.utils.PreferenceManager;

public class MoreOptionActivity extends BaseActivity implements OnClickListener {
    private ActivityMoreOptionBinding binding;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityMoreOptionBinding activityMoreOptionBinding = (ActivityMoreOptionBinding) DataBindingUtil.setContentView(this, R.layout.activity_more_option);
        this.binding = activityMoreOptionBinding;
        setToolbarTitle(activityMoreOptionBinding.layoutToolbar.toolbar, "Options", true);
        PreferenceManager.read(PreferenceManager.USER_ID, "");
        findViewById(R.id.breatheBtn).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MoreOptionActivity.this.startActivity(new Intent(MoreOptionActivity.this, BreathingActivityNew.class));
            }
        });
        findViewById(R.id.formBtn).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MoreOptionActivity.this.startActivity(new Intent(MoreOptionActivity.this, FormActivity.class));
            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_logout) {
            PreferenceManager.write(PreferenceManager.IS_LOGGED_IN, false);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(335544320);
            startActivity(intent);
            finish();
        }
    }
}
