package com.cosafe.android.databinding;

import android.util.SparseIntArray;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingComponent;
import androidx.lifecycle.LifecycleOwner;
import com.cosafe.android.R;

public class ActivityFormBindingImpl extends ActivityFormBinding {
    private static final IncludedLayouts sIncludes;
    private static final SparseIntArray sViewsWithIds;
    private long mDirtyFlags;
    private final ConstraintLayout mboundView0;

    public boolean setVariable(int i, Object obj) {
        return true;
    }

    static {
        IncludedLayouts includedLayouts = new IncludedLayouts(39);
        sIncludes = includedLayouts;
        includedLayouts.setIncludes(0, new String[]{"toolbar"}, new int[]{1}, new int[]{R.layout.toolbar});
        SparseIntArray sparseIntArray = new SparseIntArray();
        sViewsWithIds = sparseIntArray;
        sparseIntArray.put(R.id.mainCl, 2);
        sViewsWithIds.put(R.id.question1Cl, 3);
        sViewsWithIds.put(R.id.question1NumTv, 4);
        sViewsWithIds.put(R.id.question1Tv, 5);
        sViewsWithIds.put(R.id.question1Rg, 6);
        sViewsWithIds.put(R.id.question1option1Rb, 7);
        sViewsWithIds.put(R.id.question1option2Rb, 8);
        sViewsWithIds.put(R.id.question2Cl, 9);
        sViewsWithIds.put(R.id.question2NumTv, 10);
        sViewsWithIds.put(R.id.question2Tv, 11);
        sViewsWithIds.put(R.id.question2Sb, 12);
        sViewsWithIds.put(R.id.currTempTv, 13);
        sViewsWithIds.put(R.id.question3Cl, 14);
        sViewsWithIds.put(R.id.question3NumTv, 15);
        sViewsWithIds.put(R.id.question3Tv, 16);
        sViewsWithIds.put(R.id.question3Rg, 17);
        sViewsWithIds.put(R.id.question3option1Rb, 18);
        sViewsWithIds.put(R.id.question3option2Rb, 19);
        sViewsWithIds.put(R.id.question4Cl, 20);
        sViewsWithIds.put(R.id.question4NumTv, 21);
        sViewsWithIds.put(R.id.question4Tv, 22);
        sViewsWithIds.put(R.id.question4Rg, 23);
        sViewsWithIds.put(R.id.question4option1Rb, 24);
        sViewsWithIds.put(R.id.question4option2Rb, 25);
        sViewsWithIds.put(R.id.question5Cl, 26);
        sViewsWithIds.put(R.id.question5NumTv, 27);
        sViewsWithIds.put(R.id.question5Tv, 28);
        sViewsWithIds.put(R.id.question5Rg, 29);
        sViewsWithIds.put(R.id.question5option1Rb, 30);
        sViewsWithIds.put(R.id.question5option2Rb, 31);
        sViewsWithIds.put(R.id.question6Cl, 32);
        sViewsWithIds.put(R.id.question6NumTv, 33);
        sViewsWithIds.put(R.id.question6Tv, 34);
        sViewsWithIds.put(R.id.question6Rg, 35);
        sViewsWithIds.put(R.id.question6option1Rb, 36);
        sViewsWithIds.put(R.id.question6option2Rb, 37);
        sViewsWithIds.put(R.id.btn_submit, 38);
    }

    public ActivityFormBindingImpl(DataBindingComponent dataBindingComponent, View view) {
        this(dataBindingComponent, view, mapBindings(dataBindingComponent, view, 39, sIncludes, sViewsWithIds));
    }

    private ActivityFormBindingImpl(DataBindingComponent dataBindingComponent, View view, Object[] objArr) {
        super(dataBindingComponent, view, 1, objArr[38], objArr[13], objArr[1], objArr[2], objArr[3], objArr[4], objArr[6], objArr[5], objArr[7], objArr[8], objArr[9], objArr[10], objArr[12], objArr[11], objArr[14], objArr[15], objArr[17], objArr[16], objArr[18], objArr[19], objArr[20], objArr[21], objArr[23], objArr[22], objArr[24], objArr[25], objArr[26], objArr[27], objArr[29], objArr[28], objArr[30], objArr[31], objArr[32], objArr[33], objArr[35], objArr[34], objArr[36], objArr[37]);
        this.mDirtyFlags = -1;
        ConstraintLayout constraintLayout = objArr[0];
        this.mboundView0 = constraintLayout;
        constraintLayout.setTag(null);
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
