package com.cosafe.android.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.LinearLayout;
import androidx.databinding.DataBindingComponent;
import com.cosafe.android.R;

public class ActivityLoginBindingImpl extends ActivityLoginBinding {
    private static final IncludedLayouts sIncludes = null;
    private static final SparseIntArray sViewsWithIds;
    private long mDirtyFlags;
    private final LinearLayout mboundView0;

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
        sparseIntArray.put(R.id.tv_title, 1);
        sViewsWithIds.put(R.id.tv_heading, 2);
        sViewsWithIds.put(R.id.tv_sub_heading, 3);
        sViewsWithIds.put(R.id.layout_get_otp, 4);
        sViewsWithIds.put(R.id.input_phone_number, 5);
        sViewsWithIds.put(R.id.edt_phone_number, 6);
        sViewsWithIds.put(R.id.btn_get_otp, 7);
        sViewsWithIds.put(R.id.layout_verify_otp, 8);
        sViewsWithIds.put(R.id.input_otp, 9);
        sViewsWithIds.put(R.id.edt_otp, 10);
        sViewsWithIds.put(R.id.btn_verify_otp, 11);
        sViewsWithIds.put(R.id.tv_did_not_receive_otp, 12);
        sViewsWithIds.put(R.id.btn_resend_otp, 13);
    }

    public ActivityLoginBindingImpl(DataBindingComponent dataBindingComponent, View view) {
        this(dataBindingComponent, view, mapBindings(dataBindingComponent, view, 14, sIncludes, sViewsWithIds));
    }

    private ActivityLoginBindingImpl(DataBindingComponent dataBindingComponent, View view, Object[] objArr) {
        super(dataBindingComponent, view, 0, objArr[7], objArr[13], objArr[11], objArr[10], objArr[6], objArr[9], objArr[5], objArr[4], objArr[8], objArr[12], objArr[2], objArr[3], objArr[1]);
        this.mDirtyFlags = -1;
        LinearLayout linearLayout = objArr[0];
        this.mboundView0 = linearLayout;
        linearLayout.setTag(null);
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
