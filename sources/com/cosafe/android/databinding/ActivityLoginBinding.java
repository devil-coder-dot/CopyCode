package com.cosafe.android.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.cosafe.android.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public abstract class ActivityLoginBinding extends ViewDataBinding {
    public final AppCompatButton btnGetOtp;
    public final Button btnResendOtp;
    public final AppCompatButton btnVerifyOtp;
    public final TextInputEditText edtOtp;
    public final TextInputEditText edtPhoneNumber;
    public final TextInputLayout inputOtp;
    public final TextInputLayout inputPhoneNumber;
    public final LinearLayout layoutGetOtp;
    public final LinearLayout layoutVerifyOtp;
    public final TextView tvDidNotReceiveOtp;
    public final TextView tvHeading;
    public final TextView tvSubHeading;
    public final TextView tvTitle;

    protected ActivityLoginBinding(Object obj, View view, int i, AppCompatButton appCompatButton, Button button, AppCompatButton appCompatButton2, TextInputEditText textInputEditText, TextInputEditText textInputEditText2, TextInputLayout textInputLayout, TextInputLayout textInputLayout2, LinearLayout linearLayout, LinearLayout linearLayout2, TextView textView, TextView textView2, TextView textView3, TextView textView4) {
        super(obj, view, i);
        this.btnGetOtp = appCompatButton;
        this.btnResendOtp = button;
        this.btnVerifyOtp = appCompatButton2;
        this.edtOtp = textInputEditText;
        this.edtPhoneNumber = textInputEditText2;
        this.inputOtp = textInputLayout;
        this.inputPhoneNumber = textInputLayout2;
        this.layoutGetOtp = linearLayout;
        this.layoutVerifyOtp = linearLayout2;
        this.tvDidNotReceiveOtp = textView;
        this.tvHeading = textView2;
        this.tvSubHeading = textView3;
        this.tvTitle = textView4;
    }

    public static ActivityLoginBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityLoginBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (ActivityLoginBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.activity_login, viewGroup, z, obj);
    }

    public static ActivityLoginBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityLoginBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (ActivityLoginBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.activity_login, null, false, obj);
    }

    public static ActivityLoginBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityLoginBinding bind(View view, Object obj) {
        return (ActivityLoginBinding) bind(obj, view, R.layout.activity_login);
    }
}
