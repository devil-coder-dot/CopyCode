package com.cosafe.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import com.cosafe.android.databinding.DialogDownloadBinding;

public class DownloadDialog extends Dialog {
    private DialogDownloadBinding binding;
    private Context context;
    int flag = 0;

    public DownloadDialog(Context context2) {
        super(context2);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        DialogDownloadBinding inflate = DialogDownloadBinding.inflate(LayoutInflater.from(getContext()));
        this.binding = inflate;
        setContentView(inflate.getRoot());
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        if (window != null) {
            window.setLayout(-1, -1);
            window.setBackgroundDrawable(new ColorDrawable(0));
        }
        this.binding.rippleBng.startRippleAnimation();
        this.binding.btnClose.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                DownloadDialog.this.lambda$onCreate$0$DownloadDialog(view);
            }
        });
    }

    public /* synthetic */ void lambda$onCreate$0$DownloadDialog(View view) {
        this.binding.rippleBng.stopRippleAnimation();
        dismiss();
    }
}
