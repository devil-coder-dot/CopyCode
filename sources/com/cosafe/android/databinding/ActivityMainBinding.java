package com.cosafe.android.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.viewpager2.widget.ViewPager2;
import com.cosafe.android.R;

public abstract class ActivityMainBinding extends ViewDataBinding {
    public final AppCompatButton btnRetry;
    public final ImageView ivLogo;
    public final RelativeLayout layoutBottom;
    public final LinearLayout layoutGifLogo;
    public final RelativeLayout layoutIndicatorBtn;
    public final LinearLayout layoutMoreOption;
    public final LinearLayout layoutRetry;
    public final LinearLayout layoutUpload;
    public final ProgressBar loader;
    public final TextView tvCounter;
    public final TextView tvScrollText;
    public final ViewPager2 viewPager;
    public final WebView webView;

    protected ActivityMainBinding(Object obj, View view, int i, AppCompatButton appCompatButton, ImageView imageView, RelativeLayout relativeLayout, LinearLayout linearLayout, RelativeLayout relativeLayout2, LinearLayout linearLayout2, LinearLayout linearLayout3, LinearLayout linearLayout4, ProgressBar progressBar, TextView textView, TextView textView2, ViewPager2 viewPager2, WebView webView2) {
        super(obj, view, i);
        this.btnRetry = appCompatButton;
        this.ivLogo = imageView;
        this.layoutBottom = relativeLayout;
        this.layoutGifLogo = linearLayout;
        this.layoutIndicatorBtn = relativeLayout2;
        this.layoutMoreOption = linearLayout2;
        this.layoutRetry = linearLayout3;
        this.layoutUpload = linearLayout4;
        this.loader = progressBar;
        this.tvCounter = textView;
        this.tvScrollText = textView2;
        this.viewPager = viewPager2;
        this.webView = webView2;
    }

    public static ActivityMainBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityMainBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (ActivityMainBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.activity_main, viewGroup, z, obj);
    }

    public static ActivityMainBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityMainBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (ActivityMainBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.activity_main, null, false, obj);
    }

    public static ActivityMainBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityMainBinding bind(View view, Object obj) {
        return (ActivityMainBinding) bind(obj, view, R.layout.activity_main);
    }
}
