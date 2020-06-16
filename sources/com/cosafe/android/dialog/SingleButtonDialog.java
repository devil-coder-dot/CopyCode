package com.cosafe.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;

public class SingleButtonDialog extends Dialog {
    private AlertDialog mAlertDialog;
    private String mDialogMsg;
    private boolean mIsCancellable;
    private String mPositiveBtnText;
    private SingleButtonDialogListener mSingleButtonDialogListener;
    private String mTitle;

    public interface SingleButtonDialogListener {
        void onSingleButtonDialogButtonClick();
    }

    public SingleButtonDialog(Context context) {
        super(context);
    }

    public void setData(String str, String str2, String str3, boolean z) {
        this.mTitle = str;
        this.mDialogMsg = str2;
        this.mPositiveBtnText = str3;
        this.mIsCancellable = z;
    }

    public void showDialog() {
        Builder builder = new Builder(getContext());
        String str = this.mTitle;
        if (str != null) {
            builder.setTitle((CharSequence) str);
        }
        builder.setMessage((CharSequence) this.mDialogMsg);
        builder.setPositiveButton((CharSequence) this.mPositiveBtnText, (OnClickListener) new OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                SingleButtonDialog.this.lambda$showDialog$0$SingleButtonDialog(dialogInterface, i);
            }
        });
        AlertDialog create = builder.create();
        this.mAlertDialog = create;
        create.setOnShowListener(new OnShowListener() {
            public final void onShow(DialogInterface dialogInterface) {
                SingleButtonDialog.this.lambda$showDialog$1$SingleButtonDialog(dialogInterface);
            }
        });
        this.mAlertDialog.show();
    }

    public /* synthetic */ void lambda$showDialog$0$SingleButtonDialog(DialogInterface dialogInterface, int i) {
        SingleButtonDialogListener singleButtonDialogListener = this.mSingleButtonDialogListener;
        if (singleButtonDialogListener != null) {
            singleButtonDialogListener.onSingleButtonDialogButtonClick();
        }
        dismiss();
    }

    public /* synthetic */ void lambda$showDialog$1$SingleButtonDialog(DialogInterface dialogInterface) {
        if (this.mIsCancellable) {
            this.mAlertDialog.setCancelable(true);
            this.mAlertDialog.setCanceledOnTouchOutside(true);
            return;
        }
        this.mAlertDialog.setCancelable(false);
        this.mAlertDialog.setCanceledOnTouchOutside(false);
    }

    public void setSingleButtonDialogListener(SingleButtonDialogListener singleButtonDialogListener) {
        this.mSingleButtonDialogListener = singleButtonDialogListener;
    }
}
