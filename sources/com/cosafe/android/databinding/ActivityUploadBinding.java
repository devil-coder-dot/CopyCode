package com.cosafe.android.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.cosafe.android.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public abstract class ActivityUploadBinding extends ViewDataBinding {
    public final AppCompatButton btnUpload;
    public final TextInputEditText edtTestId;
    public final TextInputLayout inputTestId;
    public final ToolbarBinding layoutToolbar;
    public final TextView textView2;

    protected ActivityUploadBinding(Object obj, View view, int i, AppCompatButton appCompatButton, TextInputEditText textInputEditText, TextInputLayout textInputLayout, ToolbarBinding toolbarBinding, TextView textView) {
        super(obj, view, i);
        this.btnUpload = appCompatButton;
        this.edtTestId = textInputEditText;
        this.inputTestId = textInputLayout;
        this.layoutToolbar = toolbarBinding;
        setContainedBinding(toolbarBinding);
        this.textView2 = textView;
    }

    public static ActivityUploadBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityUploadBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (ActivityUploadBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.activity_upload, viewGroup, z, obj);
    }

    public static ActivityUploadBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityUploadBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (ActivityUploadBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.activity_upload, null, false, obj);
    }

    public static ActivityUploadBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityUploadBinding bind(View view, Object obj) {
        return (ActivityUploadBinding) bind(obj, view, R.layout.activity_upload);
    }
}
