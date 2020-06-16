package com.cosafe.android.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.databinding.DataBindingComponent;
import com.cosafe.android.R;

public class ActivityMainBindingImpl extends ActivityMainBinding {
    private static final IncludedLayouts sIncludes = null;
    private static final SparseIntArray sViewsWithIds;
    private long mDirtyFlags;
    private final RelativeLayout mboundView0;

    /* access modifiers changed from: protected */
    public boolean onFieldChange(int i, Object obj, int i2) {
        return false;
    }

    public boolean setVariable(int i, Object obj) {
        return true;
    }

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        sViewsWithIds = sparseIntArray;
        sparseIntArray.put(R.id.tv_scroll_text, 1);
        sViewsWithIds.put(R.id.view_pager, 2);
        sViewsWithIds.put(R.id.webView, 3);
        sViewsWithIds.put(R.id.layout_indicator_btn, 4);
        sViewsWithIds.put(R.id.loader, 5);
        sViewsWithIds.put(R.id.layout_retry, 6);
        sViewsWithIds.put(R.id.btn_retry, 7);
        sViewsWithIds.put(R.id.layout_bottom, 8);
        sViewsWithIds.put(R.id.layout_upload, 9);
        sViewsWithIds.put(R.id.layout_more_option, 10);
        sViewsWithIds.put(R.id.layout_gif_logo, 11);
        sViewsWithIds.put(R.id.iv_logo, 12);
        sViewsWithIds.put(R.id.tv_counter, 13);
    }

    public ActivityMainBindingImpl(DataBindingComponent dataBindingComponent, View view) {
        this(dataBindingComponent, view, mapBindings(dataBindingComponent, view, 14, sIncludes, sViewsWithIds));
    }

    private ActivityMainBindingImpl(DataBindingComponent dataBindingComponent, View view, Object[] objArr) {
        super(dataBindingComponent, view, 0, objArr[7], objArr[12], objArr[8], objArr[11], objArr[4], objArr[10], objArr[6], objArr[9], objArr[5], objArr[13], objArr[1], objArr[2], objArr[3]);
        this.mDirtyFlags = -1;
        RelativeLayout relativeLayout = objArr[0];
        this.mboundView0 = relativeLayout;
        relativeLayout.setTag(null);
        setRootTag(view);
        invalidateAll();
    }

    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 1;
        }
        requestRebind();
    }

    public boolean hasPendingBindings() {
        synchronized (this) {
            if (this.mDirtyFlags != 0) {
                return true;
            }
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void executeBindings() {
        synchronized (this) {
            this.mDirtyFlags = 0;
        }
    }
}
