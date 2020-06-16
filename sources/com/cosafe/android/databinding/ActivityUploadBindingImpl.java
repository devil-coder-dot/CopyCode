package com.cosafe.android.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingComponent;
import androidx.lifecycle.LifecycleOwner;
import com.cosafe.android.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ActivityUploadBindingImpl extends ActivityUploadBinding {
    private static final IncludedLayouts sIncludes;
    private static final SparseIntArray sViewsWithIds;
    private long mDirtyFlags;
    private final LinearLayout mboundView0;

    public boolean setVariable(int i, Object obj) {
        return true;
    }

    static {
        IncludedLayouts includedLayouts = new IncludedLayouts(6);
        sIncludes = includedLayouts;
        includedLayouts.setIncludes(0, new String[]{"toolbar"}, new int[]{1}, new int[]{R.layout.toolbar});
        SparseIntArray sparseIntArray = new SparseIntArray();
        sViewsWithIds = sparseIntArray;
        sparseIntArray.put(R.id.input_test_id, 2);
        sViewsWithIds.put(R.id.edt_test_id, 3);
        sViewsWithIds.put(R.id.btn_upload, 4);
        sViewsWithIds.put(R.id.textView2, 5);
    }

    public ActivityUploadBindingImpl(DataBindingComponent dataBindingComponent, View view) {
        this(dataBindingComponent, view, mapBindings(dataBindingComponent, view, 6, sIncludes, sViewsWithIds));
    }

    private ActivityUploadBindingImpl(DataBindingComponent dataBindingComponent, View view, Object[] objArr) {
        super(dataBindingComponent, view, 1, (AppCompatButton) objArr[4], (TextInputEditText) objArr[3], (TextInputLayout) objArr[2], (ToolbarBinding) objArr[1], (TextView) objArr[5]);
        this.mDirtyFlags = -1;
        LinearLayout linearLayout = objArr[0];
        this.mboundView0 = linearLayout;
        linearLayout.setTag(null);
        setRootTag(view);
        invalidateAll();
    }

    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 2;
        }
        this.layoutToolbar.invalidateAll();
        requestRebind();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0017, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0013, code lost:
        if (r6.layoutToolbar.hasPendingBindings() == false) goto L_0x0016;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0015, code lost:
        return true;
     */
    public boolean hasPendingBindings() {
        synchronized (this) {
            if (this.mDirtyFlags != 0) {
                return true;
            }
        }
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        this.layoutToolbar.setLifecycleOwner(lifecycleOwner);
    }

    /* access modifiers changed from: protected */
    public boolean onFieldChange(int i, Object obj, int i2) {
        if (i != 0) {
            return false;
        }
        return onChangeLayoutToolbar((ToolbarBinding) obj, i2);
    }

    private boolean onChangeLayoutToolbar(ToolbarBinding toolbarBinding, int i) {
        if (i != 0) {
            return false;
        }
        synchronized (this) {
            this.mDirtyFlags |= 1;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void executeBindings() {
        synchronized (this) {
            this.mDirtyFlags = 0;
        }
        executeBindingsOn(this.layoutToolbar);
    }
}
