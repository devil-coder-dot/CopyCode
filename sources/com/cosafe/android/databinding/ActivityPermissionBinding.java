package com.cosafe.android.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.cosafe.android.R;

public abstract class ActivityPermissionBinding extends ViewDataBinding {
    public final TextView tvGetStarted;

    protected ActivityPermissionBinding(Object obj, View view, int i, TextView textView) {
        super(obj, view, i);
        this.tvGetStarted = textView;
    }

    public static ActivityPermissionBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityPermissionBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (ActivityPermissionBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.activity_permission, viewGroup, z, obj);
    }

    public static ActivityPermissionBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityPermissionBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (ActivityPermissionBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.activity_permission, null, false, obj);
    }

    public static ActivityPermissionBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityPermissionBinding bind(View view, Object obj) {
        return (ActivityPermissionBinding) bind(obj, view, R.layout.activity_permission);
    }
}
