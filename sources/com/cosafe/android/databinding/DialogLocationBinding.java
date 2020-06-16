package com.cosafe.android.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.cosafe.android.R;

public abstract class DialogLocationBinding extends ViewDataBinding {
    public final ImageView ivLocationPin;
    public final TextView tvHeading;

    protected DialogLocationBinding(Object obj, View view, int i, ImageView imageView, TextView textView) {
        super(obj, view, i);
        this.ivLocationPin = imageView;
        this.tvHeading = textView;
    }

    public static DialogLocationBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static DialogLocationBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (DialogLocationBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.dialog_location, viewGroup, z, obj);
    }

    public static DialogLocationBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static DialogLocationBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (DialogLocationBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.dialog_location, null, false, obj);
    }

    public static DialogLocationBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static DialogLocationBinding bind(View view, Object obj) {
        return (DialogLocationBinding) bind(obj, view, R.layout.dialog_location);
    }
}
