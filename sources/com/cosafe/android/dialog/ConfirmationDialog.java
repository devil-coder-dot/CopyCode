package com.cosafe.android.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.DialogFragment;

public class ConfirmationDialog extends DialogFragment {
    private static final String KEY_DIALOG_MSG = "message";
    private static final String KEY_HEADING = "heading";
    private static final String KEY_IS_CANCELLABLE = "isCancellable";
    private static final String KEY_NEGATIVE_BTN_TEXT = "btnNegativeText";
    private static final String KEY_POSITIVE_BTN_TEXT = "btnPositiveText";
    private AlertDialog mAlertDialog;
    private ConfirmationDialogListener mConfirmationDialogListener;
    private String mDialogMsg;
    private boolean mIsCancellable;
    private String mNegativeBtnText;
    private String mPositiveBtnText;
    private String mTitle;

    public interface ConfirmationDialogListener {
        void onNegativeBtnClick();

        void onPositiveBtnClick();
    }

    public static ConfirmationDialog newInstance(String str, String str2, String str3, String str4, boolean z) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_HEADING, str);
        bundle.putString(KEY_DIALOG_MSG, str2);
        bundle.putString(KEY_POSITIVE_BTN_TEXT, str3);
        bundle.putString(KEY_NEGATIVE_BTN_TEXT, str4);
        bundle.putBoolean(KEY_IS_CANCELLABLE, z);
        confirmationDialog.setArguments(bundle);
        return confirmationDialog;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mTitle = getArguments().getString(KEY_HEADING);
            this.mDialogMsg = getArguments().getString(KEY_DIALOG_MSG);
            this.mPositiveBtnText = getArguments().getString(KEY_POSITIVE_BTN_TEXT);
            this.mNegativeBtnText = getArguments().getString(KEY_NEGATIVE_BTN_TEXT);
            this.mIsCancellable = getArguments().getBoolean(KEY_IS_CANCELLABLE);
        }
    }

    public Dialog onCreateDialog(Bundle bundle) {
        if (getActivity() != null) {
            Builder builder = new Builder(getActivity());
            String str = this.mTitle;
            if (str != null) {
                builder.setTitle((CharSequence) str);
            }
            builder.setMessage((CharSequence) this.mDialogMsg);
            builder.setPositiveButton((CharSequence) this.mPositiveBtnText, (OnClickListener) new OnClickListener() {
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ConfirmationDialog.this.lambda$onCreateDialog$0$ConfirmationDialog(dialogInterface, i);
                }
            });
            builder.setNegativeButton((CharSequence) this.mNegativeBtnText, (OnClickListener) new OnClickListener() {
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ConfirmationDialog.this.lambda$onCreateDialog$1$ConfirmationDialog(dialogInterface, i);
                }
            });
            AlertDialog create = builder.create();
            this.mAlertDialog = create;
            create.setOnShowListener(new OnShowListener() {
                public final void onShow(DialogInterface dialogInterface) {
                    ConfirmationDialog.this.lambda$onCreateDialog$2$ConfirmationDialog(dialogInterface);
                }
            });
        }
        return this.mAlertDialog;
    }

    public /* synthetic */ void lambda$onCreateDialog$0$ConfirmationDialog(DialogInterface dialogInterface, int i) {
        ConfirmationDialogListener confirmationDialogListener = this.mConfirmationDialogListener;
        if (confirmationDialogListener != null) {
            confirmationDialogListener.onPositiveBtnClick();
        }
        dismiss();
    }

    public /* synthetic */ void lambda$onCreateDialog$1$ConfirmationDialog(DialogInterface dialogInterface, int i) {
        ConfirmationDialogListener confirmationDialogListener = this.mConfirmationDialogListener;
        if (confirmationDialogListener != null) {
            confirmationDialogListener.onNegativeBtnClick();
        }
        dismiss();
    }

    public /* synthetic */ void lambda$onCreateDialog$2$ConfirmationDialog(DialogInterface dialogInterface) {
        if (this.mIsCancellable) {
            this.mAlertDialog.setCancelable(true);
            this.mAlertDialog.setCanceledOnTouchOutside(true);
            return;
        }
        this.mAlertDialog.setCancelable(false);
        this.mAlertDialog.setCanceledOnTouchOutside(false);
    }

    public void setConfirmationListener(ConfirmationDialogListener confirmationDialogListener) {
        this.mConfirmationDialogListener = confirmationDialogListener;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.mConfirmationDialogListener = null;
    }
}
