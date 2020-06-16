package com.cosafe.android.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.cosafe.android.dialog.ConfirmationDialog;
import com.cosafe.android.dialog.ConfirmationDialog.ConfirmationDialogListener;
import com.cosafe.android.dialog.SingleButtonDialog;
import com.cosafe.android.dialog.SingleButtonDialog.SingleButtonDialogListener;

public abstract class BaseActivity extends AppCompatActivity implements SingleButtonDialogListener, ConfirmationDialogListener {
    private ProgressDialog mProgressDialog;

    public void onNegativeBtnClick() {
    }

    public void onPositiveBtnClick() {
    }

    public void onSingleButtonDialogButtonClick() {
    }

    public void setToolbarTitle(Toolbar toolbar, String str, boolean z) {
        toolbar.setTitle((CharSequence) str);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null && z) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void showLoader(Context context, String str) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        this.mProgressDialog = progressDialog;
        progressDialog.setCancelable(false);
        this.mProgressDialog.setCanceledOnTouchOutside(false);
        this.mProgressDialog.setMessage(str);
        this.mProgressDialog.setIndeterminate(true);
        this.mProgressDialog.show();
    }

    public void dismissLoader() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
    }

    public void showSingleButtonDialog(Context context, String str, String str2, String str3, boolean z) {
        SingleButtonDialog singleButtonDialog = new SingleButtonDialog(context);
        singleButtonDialog.setData(str, str2, str3, z);
        singleButtonDialog.setSingleButtonDialogListener(this);
        singleButtonDialog.showDialog();
    }

    public void showConfirmationDialog(String str, String str2, String str3, String str4, boolean z) {
        ConfirmationDialog newInstance = ConfirmationDialog.newInstance(str, str2, str3, str4, z);
        newInstance.setConfirmationListener(this);
        newInstance.show(getSupportFragmentManager(), "Confirmation Dialog");
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
