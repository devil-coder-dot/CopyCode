package com.cosafe.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.animation.AnimationUtils;
import com.cosafe.android.R;
import com.cosafe.android.databinding.DialogLocationBinding;

public class LocationDialog extends Dialog {
    private DialogLocationBinding binding;
    private Context mContext;

    public LocationDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        DialogLocationBinding inflate = DialogLocationBinding.inflate(LayoutInflater.from(getContext()));
        this.binding = inflate;
        setContentView(inflate.getRoot());
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        this.binding.ivLocationPin.startAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.bounce));
    }
}
