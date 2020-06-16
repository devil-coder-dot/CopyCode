package com.cosafe.android.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.cosafe.android.R;

public abstract class ActivityMoreOptionBinding extends ViewDataBinding {
    public final Button breatheBtn;
    public final AppCompatButton btnLogout;
    public final Button formBtn;
    public final ToolbarBinding layoutToolbar;
    public final TextView tvUniqueId;

    protected ActivityMoreOptionBinding(Object obj, View view, int i, Button button, AppCompatButton appCompatButton, Button button2, ToolbarBinding toolbarBinding, TextView textView) {
        super(obj, view, i);
        this.breatheBtn = button;
        this.btnLogout = appCompatButton;
        this.formBtn = button2;
        this.layoutToolbar = toolbarBinding;
        setContainedBinding(toolbarBinding);
        this.tvUniqueId = textView;
    }

    public static ActivityMoreOptionBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityMoreOptionBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (ActivityMoreOptionBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.activity_more_option, viewGroup, z, obj);
    }

    public static ActivityMoreOptionBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityMoreOptionBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (ActivityMoreOptionBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.activity_more_option, null, false, obj);
    }

    public static ActivityMoreOptionBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityMoreOptionBinding bind(View view, Object obj) {
        return (ActivityMoreOptionBinding) bind(obj, view, R.layout.activity_more_option);
    }
}
