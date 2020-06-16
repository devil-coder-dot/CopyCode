package com.cosafe.android;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;
import com.cosafe.android.base.BaseActivity;
import com.cosafe.android.databinding.ActivityUploadBinding;
import com.cosafe.android.utils.UploadManager;
import com.cosafe.android.utils.Utility;

public class UploadActivity extends BaseActivity implements OnClickListener {
    private ActivityUploadBinding binding;
    private UploadManager uploadManager;

    public void initUpload() {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityUploadBinding activityUploadBinding = (ActivityUploadBinding) DataBindingUtil.setContentView(this, R.layout.activity_upload);
        this.binding = activityUploadBinding;
        setToolbarTitle(activityUploadBinding.layoutToolbar.toolbar, "Upload", true);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_upload) {
            String str = "";
            ((TextView) findViewById(R.id.textView2)).setText(str);
            if (Utility.isNetworkConnectivityExists(this)) {
                EditText editText = (EditText) findViewById(R.id.edt_test_id);
                if (editText.getText().toString().equals("asdfg")) {
                    editText.setText(str);
                    initUpload();
                    ((TextView) findViewById(R.id.textView2)).setText("Uploaded");
                    ((Button) findViewById(R.id.btn_upload)).setEnabled(false);
                    return;
                }
                ((TextView) findViewById(R.id.textView2)).setText("Please Enter correct Pin");
                return;
            }
            Utility.showSnackBar(this.binding.getRoot(), (int) R.string.toast_no_internet_connection);
        }
    }
}
