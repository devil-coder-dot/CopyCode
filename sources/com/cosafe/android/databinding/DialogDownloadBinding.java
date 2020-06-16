package com.cosafe.android.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.cosafe.android.R;
import com.skyfishjy.library.RippleBackground;

public abstract class DialogDownloadBinding extends ViewDataBinding {
    public final AppCompatButton btnClose;
    public final ImageView logo;
    public final RippleBackground rippleBng;
    public final TextView textView;

    protected DialogDownloadBinding(Object obj, View view, int i, AppCompatButton appCompatButton, ImageView imageView, RippleBackground rippleBackground, TextView textView2) {
        super(obj, view, i);
        this.btnClose = appCompatButton;
        this.logo = imageView;
        this.rippleBng = rippleBackground;
        this.textView = textView2;
    }

    public static DialogDownloadBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static DialogDownloadBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (DialogDownloadBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.dialog_download, viewGroup, z, obj);
    }

    public static DialogDownloadBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static DialogDownloadBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (DialogDownloadBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.dialog_download, null, false, obj);
    }

    public static DialogDownloadBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static DialogDownloadBinding bind(View view, Object obj) {
        return (DialogDownloadBinding) bind(obj, view, R.layout.dialog_download);
    }
}
