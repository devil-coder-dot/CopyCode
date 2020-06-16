package com.cosafe.android.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.cosafe.android.R;

public abstract class ToolbarBinding extends ViewDataBinding {
    public final Toolbar toolbar;

    protected ToolbarBinding(Object obj, View view, int i, Toolbar toolbar2) {
        super(obj, view, i);
        this.toolbar = toolbar2;
    }

    public static ToolbarBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ToolbarBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (ToolbarBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.toolbar, viewGroup, z, obj);
    }

    public static ToolbarBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ToolbarBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (ToolbarBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.toolbar, null, false, obj);
    }

    public static ToolbarBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ToolbarBinding bind(View view, Object obj) {
        return (ToolbarBinding) bind(obj, view, R.layout.toolbar);
    }
}
