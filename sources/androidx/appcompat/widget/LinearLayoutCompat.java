package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.appcompat.R;
import androidx.core.view.GravityCompat;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.badge.BadgeDrawable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
    private static final String ACCESSIBILITY_CLASS_NAME = "androidx.appcompat.widget.LinearLayoutCompat";
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DividerMode {
    }

    public static class LayoutParams extends MarginLayoutParams {
        public int gravity;
        public float weight;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.gravity = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LinearLayoutCompat_Layout);
            this.weight = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.gravity = -1;
            this.weight = 0.0f;
        }

        public LayoutParams(int i, int i2, float f) {
            super(i, i2);
            this.gravity = -1;
            this.weight = f;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
        }

        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.gravity = -1;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
            this.weight = layoutParams.weight;
            this.gravity = layoutParams.gravity;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    /* access modifiers changed from: 0000 */
    public int getChildrenSkipCount(View view, int i) {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int getLocationOffset(View view) {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int getNextLocationOffset(View view) {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int measureNullChild(int i) {
        return 0;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public LinearLayoutCompat(Context context) {
        this(context, null);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = BadgeDrawable.TOP_START;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.LinearLayoutCompat, i, 0);
        int i2 = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (i2 >= 0) {
            setOrientation(i2);
        }
        int i3 = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (i3 >= 0) {
            setGravity(i3);
        }
        boolean z = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!z) {
            setBaselineAligned(z);
        }
        this.mWeightSum = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(obtainStyledAttributes.getDrawable(R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
        obtainStyledAttributes.recycle();
    }

    public void setShowDividers(int i) {
        if (i != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = i;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable != this.mDivider) {
            this.mDivider = drawable;
            boolean z = false;
            if (drawable != null) {
                this.mDividerWidth = drawable.getIntrinsicWidth();
                this.mDividerHeight = drawable.getIntrinsicHeight();
            } else {
                this.mDividerWidth = 0;
                this.mDividerHeight = 0;
            }
            if (drawable == null) {
                z = true;
            }
            setWillNotDraw(z);
            requestLayout();
        }
    }

    public void setDividerPadding(int i) {
        this.mDividerPadding = i;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mDivider != null) {
            if (this.mOrientation == 1) {
                drawDividersVertical(canvas);
            } else {
                drawDividersHorizontal(canvas);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawDividersVertical(Canvas canvas) {
        int i;
        int virtualChildCount = getVirtualChildCount();
        for (int i2 = 0; i2 < virtualChildCount; i2++) {
            View virtualChildAt = getVirtualChildAt(i2);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i2))) {
                drawHorizontalDivider(canvas, (virtualChildAt.getTop() - ((LayoutParams) virtualChildAt.getLayoutParams()).topMargin) - this.mDividerHeight);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 == null) {
                i = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                i = virtualChildAt2.getBottom() + ((LayoutParams) virtualChildAt2.getLayoutParams()).bottomMargin;
            }
            drawHorizontalDivider(canvas, i);
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawDividersHorizontal(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int i4;
        int virtualChildCount = getVirtualChildCount();
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        for (int i5 = 0; i5 < virtualChildCount; i5++) {
            View virtualChildAt = getVirtualChildAt(i5);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i5))) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (isLayoutRtl) {
                    i4 = virtualChildAt.getRight() + layoutParams.rightMargin;
                } else {
                    i4 = (virtualChildAt.getLeft() - layoutParams.leftMargin) - this.mDividerWidth;
                }
                drawVerticalDivider(canvas, i4);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 != null) {
                LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                if (isLayoutRtl) {
                    i3 = virtualChildAt2.getLeft() - layoutParams2.leftMargin;
                    i2 = this.mDividerWidth;
                } else {
                    i = virtualChildAt2.getRight() + layoutParams2.rightMargin;
                    drawVerticalDivider(canvas, i);
                }
            } else if (isLayoutRtl) {
                i = getPaddingLeft();
                drawVerticalDivider(canvas, i);
            } else {
                i3 = getWidth() - getPaddingRight();
                i2 = this.mDividerWidth;
            }
            i = i3 - i2;
            drawVerticalDivider(canvas, i);
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawHorizontalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, i, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + i);
        this.mDivider.draw(canvas);
    }

    /* access modifiers changed from: 0000 */
    public void drawVerticalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(i, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + i, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public void setBaselineAligned(boolean z) {
        this.mBaselineAligned = z;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    public void setMeasureWithLargestChildEnabled(boolean z) {
        this.mUseLargestChild = z;
    }

    public int getBaseline() {
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i = this.mBaselineAlignedChildIndex;
        if (childCount > i) {
            View childAt = getChildAt(i);
            int baseline = childAt.getBaseline();
            if (baseline != -1) {
                int i2 = this.mBaselineChildTop;
                if (this.mOrientation == 1) {
                    int i3 = this.mGravity & 112;
                    if (i3 != 48) {
                        if (i3 == 16) {
                            i2 += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2;
                        } else if (i3 == 80) {
                            i2 = ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength;
                        }
                    }
                }
                return i2 + ((LayoutParams) childAt.getLayoutParams()).topMargin + baseline;
            } else if (this.mBaselineAlignedChildIndex == 0) {
                return -1;
            } else {
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
        } else {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    public void setBaselineAlignedChildIndex(int i) {
        if (i < 0 || i >= getChildCount()) {
            StringBuilder sb = new StringBuilder();
            sb.append("base aligned child index out of range (0, ");
            sb.append(getChildCount());
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
        this.mBaselineAlignedChildIndex = i;
    }

    /* access modifiers changed from: 0000 */
    public View getVirtualChildAt(int i) {
        return getChildAt(i);
    }

    /* access modifiers changed from: 0000 */
    public int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    public void setWeightSum(float f) {
        this.mWeightSum = Math.max(0.0f, f);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.mOrientation == 1) {
            measureVertical(i, i2);
        } else {
            measureHorizontal(i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public boolean hasDividerBeforeChildAt(int i) {
        boolean z = false;
        if (i == 0) {
            if ((this.mShowDividers & 1) != 0) {
                z = true;
            }
            return z;
        } else if (i == getChildCount()) {
            if ((this.mShowDividers & 4) != 0) {
                z = true;
            }
            return z;
        } else {
            if ((this.mShowDividers & 2) != 0) {
                int i2 = i - 1;
                while (true) {
                    if (i2 < 0) {
                        break;
                    } else if (getChildAt(i2).getVisibility() != 8) {
                        z = true;
                        break;
                    } else {
                        i2--;
                    }
                }
            }
            return z;
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x0327  */
    public void measureVertical(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        boolean z;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        View view;
        int i18;
        boolean z2;
        int i19;
        int i20;
        int i21 = i;
        int i22 = i2;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        int i23 = this.mBaselineAlignedChildIndex;
        boolean z3 = this.mUseLargestChild;
        float f = 0.0f;
        int i24 = 0;
        int i25 = 0;
        int i26 = 0;
        int i27 = 0;
        int i28 = 0;
        int i29 = 0;
        boolean z4 = false;
        boolean z5 = true;
        boolean z6 = false;
        while (true) {
            int i30 = 8;
            int i31 = i27;
            if (i29 < virtualChildCount) {
                View virtualChildAt = getVirtualChildAt(i29);
                if (virtualChildAt == null) {
                    this.mTotalLength += measureNullChild(i29);
                    i12 = virtualChildCount;
                    i11 = mode2;
                    i27 = i31;
                } else {
                    int i32 = i24;
                    if (virtualChildAt.getVisibility() == 8) {
                        i29 += getChildrenSkipCount(virtualChildAt, i29);
                        i12 = virtualChildCount;
                        i27 = i31;
                        i24 = i32;
                        i11 = mode2;
                    } else {
                        if (hasDividerBeforeChildAt(i29)) {
                            this.mTotalLength += this.mDividerHeight;
                        }
                        LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                        float f2 = f + layoutParams.weight;
                        if (mode2 == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0f) {
                            int i33 = this.mTotalLength;
                            int i34 = i25;
                            this.mTotalLength = Math.max(i33, layoutParams.topMargin + i33 + layoutParams.bottomMargin);
                            i18 = i26;
                            view = virtualChildAt;
                            i13 = i28;
                            i12 = virtualChildCount;
                            i17 = i32;
                            i14 = i34;
                            z4 = true;
                            i16 = i29;
                            i11 = mode2;
                            i15 = i31;
                        } else {
                            int i35 = i25;
                            if (layoutParams.height != 0 || layoutParams.weight <= 0.0f) {
                                i20 = Integer.MIN_VALUE;
                            } else {
                                layoutParams.height = -2;
                                i20 = 0;
                            }
                            i17 = i32;
                            int i36 = i20;
                            i14 = i35;
                            int i37 = i26;
                            i12 = virtualChildCount;
                            i11 = mode2;
                            i15 = i31;
                            View view2 = virtualChildAt;
                            i13 = i28;
                            i16 = i29;
                            measureChildBeforeLayout(virtualChildAt, i29, i, 0, i2, f2 == 0.0f ? this.mTotalLength : 0);
                            int i38 = i36;
                            if (i38 != Integer.MIN_VALUE) {
                                layoutParams.height = i38;
                            }
                            int measuredHeight = view2.getMeasuredHeight();
                            int i39 = this.mTotalLength;
                            view = view2;
                            this.mTotalLength = Math.max(i39, i39 + measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
                            i18 = z3 ? Math.max(measuredHeight, i37) : i37;
                        }
                        if (i23 >= 0 && i23 == i16 + 1) {
                            this.mBaselineChildTop = this.mTotalLength;
                        }
                        if (i16 >= i23 || layoutParams.weight <= 0.0f) {
                            if (mode == 1073741824 || layoutParams.width != -1) {
                                z2 = false;
                            } else {
                                z2 = true;
                                z6 = true;
                            }
                            int i40 = layoutParams.leftMargin + layoutParams.rightMargin;
                            int measuredWidth = view.getMeasuredWidth() + i40;
                            int max = Math.max(i14, measuredWidth);
                            int combineMeasuredStates = View.combineMeasuredStates(i17, view.getMeasuredState());
                            z5 = z5 && layoutParams.width == -1;
                            if (layoutParams.weight > 0.0f) {
                                if (!z2) {
                                    i40 = measuredWidth;
                                }
                                i27 = Math.max(i15, i40);
                                i19 = i13;
                            } else {
                                if (!z2) {
                                    i40 = measuredWidth;
                                }
                                i19 = Math.max(i13, i40);
                                i27 = i15;
                            }
                            i26 = i18;
                            f = f2;
                            int i41 = max;
                            i28 = i19;
                            i24 = combineMeasuredStates;
                            i29 = getChildrenSkipCount(view, i16) + i16;
                            i25 = i41;
                        } else {
                            throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                        }
                    }
                }
                i29++;
                int i42 = i;
                int i43 = i2;
                virtualChildCount = i12;
                mode2 = i11;
            } else {
                int i44 = i24;
                int i45 = i26;
                int i46 = i28;
                int i47 = virtualChildCount;
                int i48 = mode2;
                int i49 = i31;
                int i50 = i25;
                if (this.mTotalLength > 0) {
                    i3 = i47;
                    if (hasDividerBeforeChildAt(i3)) {
                        this.mTotalLength += this.mDividerHeight;
                    }
                } else {
                    i3 = i47;
                }
                int i51 = i48;
                if (z3 && (i51 == Integer.MIN_VALUE || i51 == 0)) {
                    this.mTotalLength = 0;
                    int i52 = 0;
                    while (i52 < i3) {
                        View virtualChildAt2 = getVirtualChildAt(i52);
                        if (virtualChildAt2 == null) {
                            this.mTotalLength += measureNullChild(i52);
                        } else if (virtualChildAt2.getVisibility() == i30) {
                            i52 += getChildrenSkipCount(virtualChildAt2, i52);
                        } else {
                            LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                            int i53 = this.mTotalLength;
                            this.mTotalLength = Math.max(i53, i53 + i45 + layoutParams2.topMargin + layoutParams2.bottomMargin + getNextLocationOffset(virtualChildAt2));
                        }
                        i52++;
                        i30 = 8;
                    }
                }
                int paddingTop = this.mTotalLength + getPaddingTop() + getPaddingBottom();
                this.mTotalLength = paddingTop;
                int i54 = i2;
                int i55 = i45;
                int resolveSizeAndState = View.resolveSizeAndState(Math.max(paddingTop, getSuggestedMinimumHeight()), i54, 0);
                int i56 = (16777215 & resolveSizeAndState) - this.mTotalLength;
                if (z4 || (i56 != 0 && f > 0.0f)) {
                    float f3 = this.mWeightSum;
                    if (f3 > 0.0f) {
                        f = f3;
                    }
                    this.mTotalLength = 0;
                    int i57 = i56;
                    int i58 = i46;
                    i5 = i44;
                    int i59 = 0;
                    while (i59 < i3) {
                        View virtualChildAt3 = getVirtualChildAt(i59);
                        if (virtualChildAt3.getVisibility() == 8) {
                            i7 = i57;
                            int i60 = i;
                        } else {
                            LayoutParams layoutParams3 = (LayoutParams) virtualChildAt3.getLayoutParams();
                            float f4 = layoutParams3.weight;
                            if (f4 > 0.0f) {
                                int i61 = (int) ((((float) i57) * f4) / f);
                                float f5 = f - f4;
                                i7 = i57 - i61;
                                int childMeasureSpec = getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight() + layoutParams3.leftMargin + layoutParams3.rightMargin, layoutParams3.width);
                                if (layoutParams3.height == 0) {
                                    i10 = 1073741824;
                                    if (i51 == 1073741824) {
                                        if (i61 <= 0) {
                                            i61 = 0;
                                        }
                                        virtualChildAt3.measure(childMeasureSpec, MeasureSpec.makeMeasureSpec(i61, 1073741824));
                                        i5 = View.combineMeasuredStates(i5, virtualChildAt3.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                                        f = f5;
                                    }
                                } else {
                                    i10 = 1073741824;
                                }
                                int measuredHeight2 = virtualChildAt3.getMeasuredHeight() + i61;
                                if (measuredHeight2 < 0) {
                                    measuredHeight2 = 0;
                                }
                                virtualChildAt3.measure(childMeasureSpec, MeasureSpec.makeMeasureSpec(measuredHeight2, i10));
                                i5 = View.combineMeasuredStates(i5, virtualChildAt3.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                                f = f5;
                            } else {
                                int i62 = i57;
                                int i63 = i;
                                i7 = i62;
                            }
                            int i64 = layoutParams3.leftMargin + layoutParams3.rightMargin;
                            int measuredWidth2 = virtualChildAt3.getMeasuredWidth() + i64;
                            i50 = Math.max(i50, measuredWidth2);
                            float f6 = f;
                            if (mode != 1073741824) {
                                i8 = i5;
                                i9 = -1;
                                if (layoutParams3.width == -1) {
                                    z = true;
                                    if (!z) {
                                        i64 = measuredWidth2;
                                    }
                                    int max2 = Math.max(i58, i64);
                                    boolean z7 = !z5 && layoutParams3.width == i9;
                                    int i65 = this.mTotalLength;
                                    this.mTotalLength = Math.max(i65, virtualChildAt3.getMeasuredHeight() + i65 + layoutParams3.topMargin + layoutParams3.bottomMargin + getNextLocationOffset(virtualChildAt3));
                                    z5 = z7;
                                    i5 = i8;
                                    i58 = max2;
                                    f = f6;
                                }
                            } else {
                                i8 = i5;
                                i9 = -1;
                            }
                            z = false;
                            if (!z) {
                            }
                            int max22 = Math.max(i58, i64);
                            if (!z5) {
                            }
                            int i652 = this.mTotalLength;
                            this.mTotalLength = Math.max(i652, virtualChildAt3.getMeasuredHeight() + i652 + layoutParams3.topMargin + layoutParams3.bottomMargin + getNextLocationOffset(virtualChildAt3));
                            z5 = z7;
                            i5 = i8;
                            i58 = max22;
                            f = f6;
                        }
                        i59++;
                        i57 = i7;
                    }
                    i4 = i;
                    this.mTotalLength += getPaddingTop() + getPaddingBottom();
                    i6 = i58;
                } else {
                    i6 = Math.max(i46, i49);
                    if (z3 && i51 != 1073741824) {
                        for (int i66 = 0; i66 < i3; i66++) {
                            View virtualChildAt4 = getVirtualChildAt(i66);
                            if (!(virtualChildAt4 == null || virtualChildAt4.getVisibility() == 8 || ((LayoutParams) virtualChildAt4.getLayoutParams()).weight <= 0.0f)) {
                                virtualChildAt4.measure(MeasureSpec.makeMeasureSpec(virtualChildAt4.getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(i55, 1073741824));
                            }
                        }
                    }
                    i4 = i;
                    i5 = i44;
                }
                if (z5 || mode == 1073741824) {
                    i6 = i50;
                }
                setMeasuredDimension(View.resolveSizeAndState(Math.max(i6 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i4, i5), resolveSizeAndState);
                if (z6) {
                    forceUniformWidth(i3, i54);
                    return;
                }
                return;
            }
        }
    }

    private void forceUniformWidth(int i, int i2) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.width == -1) {
                    int i4 = layoutParams.height;
                    layoutParams.height = virtualChildAt.getMeasuredHeight();
                    measureChildWithMargins(virtualChildAt, makeMeasureSpec, 0, i2, 0);
                    layoutParams.height = i4;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x044a  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x018e  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x01d0  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x01db  */
    public void measureHorizontal(int i, int i2) {
        int[] iArr;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        float f;
        int i12;
        boolean z;
        int i13;
        int i14;
        boolean z2;
        boolean z3;
        int i15;
        int i16;
        View view;
        int i17;
        boolean z4;
        int i18;
        int i19 = i;
        int i20 = i2;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        if (this.mMaxAscent == null || this.mMaxDescent == null) {
            this.mMaxAscent = new int[4];
            this.mMaxDescent = new int[4];
        }
        int[] iArr2 = this.mMaxAscent;
        int[] iArr3 = this.mMaxDescent;
        iArr2[3] = -1;
        iArr2[2] = -1;
        iArr2[1] = -1;
        iArr2[0] = -1;
        iArr3[3] = -1;
        iArr3[2] = -1;
        iArr3[1] = -1;
        iArr3[0] = -1;
        boolean z5 = this.mBaselineAligned;
        boolean z6 = this.mUseLargestChild;
        int i21 = 1073741824;
        boolean z7 = mode == 1073741824;
        float f2 = 0.0f;
        int i22 = 0;
        int i23 = 0;
        int i24 = 0;
        int i25 = 0;
        int i26 = 0;
        boolean z8 = false;
        int i27 = 0;
        boolean z9 = true;
        boolean z10 = false;
        while (true) {
            iArr = iArr3;
            if (i22 >= virtualChildCount) {
                break;
            }
            View virtualChildAt = getVirtualChildAt(i22);
            if (virtualChildAt == null) {
                this.mTotalLength += measureNullChild(i22);
            } else if (virtualChildAt.getVisibility() == 8) {
                i22 += getChildrenSkipCount(virtualChildAt, i22);
            } else {
                if (hasDividerBeforeChildAt(i22)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                float f3 = f2 + layoutParams.weight;
                if (mode == i21 && layoutParams.width == 0 && layoutParams.weight > 0.0f) {
                    if (z7) {
                        this.mTotalLength += layoutParams.leftMargin + layoutParams.rightMargin;
                    } else {
                        int i28 = this.mTotalLength;
                        this.mTotalLength = Math.max(i28, layoutParams.leftMargin + i28 + layoutParams.rightMargin);
                    }
                    if (z5) {
                        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
                        virtualChildAt.measure(makeMeasureSpec, makeMeasureSpec);
                        i16 = i22;
                        z3 = z6;
                        z2 = z5;
                        view = virtualChildAt;
                    } else {
                        i16 = i22;
                        z3 = z6;
                        z2 = z5;
                        view = virtualChildAt;
                        i17 = 1073741824;
                        z8 = true;
                        if (mode2 == i17 && layoutParams.height == -1) {
                            z4 = true;
                            z10 = true;
                        } else {
                            z4 = false;
                        }
                        int i29 = layoutParams.topMargin + layoutParams.bottomMargin;
                        int measuredHeight = view.getMeasuredHeight() + i29;
                        i27 = View.combineMeasuredStates(i27, view.getMeasuredState());
                        if (z2) {
                            int baseline = view.getBaseline();
                            if (baseline != -1) {
                                int i30 = ((((layoutParams.gravity < 0 ? this.mGravity : layoutParams.gravity) & 112) >> 4) & -2) >> 1;
                                iArr2[i30] = Math.max(iArr2[i30], baseline);
                                iArr[i30] = Math.max(iArr[i30], measuredHeight - baseline);
                            }
                        }
                        i24 = Math.max(i24, measuredHeight);
                        z9 = !z9 && layoutParams.height == -1;
                        if (layoutParams.weight <= 0.0f) {
                            if (!z4) {
                                i29 = measuredHeight;
                            }
                            i26 = Math.max(i26, i29);
                        } else {
                            int i31 = i26;
                            if (!z4) {
                                i29 = measuredHeight;
                            }
                            i25 = Math.max(i25, i29);
                            i26 = i31;
                        }
                        int i32 = i16;
                        i15 = getChildrenSkipCount(view, i32) + i32;
                        f2 = f3;
                        int i33 = i2;
                        i22 = i15 + 1;
                        iArr3 = iArr;
                        z6 = z3;
                        z5 = z2;
                        i21 = 1073741824;
                    }
                } else {
                    if (layoutParams.width != 0 || layoutParams.weight <= 0.0f) {
                        i18 = Integer.MIN_VALUE;
                    } else {
                        layoutParams.width = -2;
                        i18 = 0;
                    }
                    i16 = i22;
                    int i34 = i18;
                    z3 = z6;
                    z2 = z5;
                    View view2 = virtualChildAt;
                    measureChildBeforeLayout(virtualChildAt, i16, i, f3 == 0.0f ? this.mTotalLength : 0, i2, 0);
                    int i35 = i34;
                    if (i35 != Integer.MIN_VALUE) {
                        layoutParams.width = i35;
                    }
                    int measuredWidth = view2.getMeasuredWidth();
                    if (z7) {
                        view = view2;
                        this.mTotalLength += layoutParams.leftMargin + measuredWidth + layoutParams.rightMargin + getNextLocationOffset(view);
                    } else {
                        view = view2;
                        int i36 = this.mTotalLength;
                        this.mTotalLength = Math.max(i36, i36 + measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin + getNextLocationOffset(view));
                    }
                    if (z3) {
                        i23 = Math.max(measuredWidth, i23);
                    }
                }
                i17 = 1073741824;
                if (mode2 == i17) {
                }
                z4 = false;
                int i292 = layoutParams.topMargin + layoutParams.bottomMargin;
                int measuredHeight2 = view.getMeasuredHeight() + i292;
                i27 = View.combineMeasuredStates(i27, view.getMeasuredState());
                if (z2) {
                }
                i24 = Math.max(i24, measuredHeight2);
                if (!z9) {
                }
                if (layoutParams.weight <= 0.0f) {
                }
                int i322 = i16;
                i15 = getChildrenSkipCount(view, i322) + i322;
                f2 = f3;
                int i332 = i2;
                i22 = i15 + 1;
                iArr3 = iArr;
                z6 = z3;
                z5 = z2;
                i21 = 1073741824;
            }
            i15 = i22;
            z3 = z6;
            z2 = z5;
            int i3322 = i2;
            i22 = i15 + 1;
            iArr3 = iArr;
            z6 = z3;
            z5 = z2;
            i21 = 1073741824;
        }
        boolean z11 = z6;
        boolean z12 = z5;
        int i37 = i24;
        int i38 = i25;
        int i39 = i26;
        int i40 = i27;
        if (this.mTotalLength > 0 && hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerWidth;
        }
        if (iArr2[1] == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) {
            i4 = i37;
            i3 = i40;
        } else {
            i3 = i40;
            i4 = Math.max(i37, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(iArr2[1], iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
        }
        if (z11 && (mode == Integer.MIN_VALUE || mode == 0)) {
            this.mTotalLength = 0;
            int i41 = 0;
            while (i41 < virtualChildCount) {
                View virtualChildAt2 = getVirtualChildAt(i41);
                if (virtualChildAt2 == null) {
                    this.mTotalLength += measureNullChild(i41);
                } else if (virtualChildAt2.getVisibility() == 8) {
                    i41 += getChildrenSkipCount(virtualChildAt2, i41);
                } else {
                    LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                    if (z7) {
                        this.mTotalLength += layoutParams2.leftMargin + i23 + layoutParams2.rightMargin + getNextLocationOffset(virtualChildAt2);
                    } else {
                        int i42 = this.mTotalLength;
                        i14 = i4;
                        this.mTotalLength = Math.max(i42, i42 + i23 + layoutParams2.leftMargin + layoutParams2.rightMargin + getNextLocationOffset(virtualChildAt2));
                        i41++;
                        i4 = i14;
                    }
                }
                i14 = i4;
                i41++;
                i4 = i14;
            }
        }
        int i43 = i4;
        int paddingLeft = this.mTotalLength + getPaddingLeft() + getPaddingRight();
        this.mTotalLength = paddingLeft;
        int resolveSizeAndState = View.resolveSizeAndState(Math.max(paddingLeft, getSuggestedMinimumWidth()), i19, 0);
        int i44 = (16777215 & resolveSizeAndState) - this.mTotalLength;
        if (z8 || (i44 != 0 && f2 > 0.0f)) {
            float f4 = this.mWeightSum;
            if (f4 > 0.0f) {
                f2 = f4;
            }
            iArr2[3] = -1;
            iArr2[2] = -1;
            iArr2[1] = -1;
            iArr2[0] = -1;
            iArr[3] = -1;
            iArr[2] = -1;
            iArr[1] = -1;
            iArr[0] = -1;
            this.mTotalLength = 0;
            int i45 = i38;
            int i46 = i3;
            int i47 = -1;
            int i48 = 0;
            while (i48 < virtualChildCount) {
                View virtualChildAt3 = getVirtualChildAt(i48);
                if (virtualChildAt3 == null || virtualChildAt3.getVisibility() == 8) {
                    i11 = i44;
                    i10 = virtualChildCount;
                    int i49 = i2;
                } else {
                    LayoutParams layoutParams3 = (LayoutParams) virtualChildAt3.getLayoutParams();
                    float f5 = layoutParams3.weight;
                    if (f5 > 0.0f) {
                        int i50 = (int) ((((float) i44) * f5) / f2);
                        float f6 = f2 - f5;
                        int i51 = i44 - i50;
                        i10 = virtualChildCount;
                        int childMeasureSpec = getChildMeasureSpec(i2, getPaddingTop() + getPaddingBottom() + layoutParams3.topMargin + layoutParams3.bottomMargin, layoutParams3.height);
                        if (layoutParams3.width == 0) {
                            i13 = 1073741824;
                            if (mode == 1073741824) {
                                if (i50 <= 0) {
                                    i50 = 0;
                                }
                                virtualChildAt3.measure(MeasureSpec.makeMeasureSpec(i50, 1073741824), childMeasureSpec);
                                i46 = View.combineMeasuredStates(i46, virtualChildAt3.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                                f2 = f6;
                                i11 = i51;
                            }
                        } else {
                            i13 = 1073741824;
                        }
                        int measuredWidth2 = virtualChildAt3.getMeasuredWidth() + i50;
                        if (measuredWidth2 < 0) {
                            measuredWidth2 = 0;
                        }
                        virtualChildAt3.measure(MeasureSpec.makeMeasureSpec(measuredWidth2, i13), childMeasureSpec);
                        i46 = View.combineMeasuredStates(i46, virtualChildAt3.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                        f2 = f6;
                        i11 = i51;
                    } else {
                        i11 = i44;
                        i10 = virtualChildCount;
                        int i52 = i2;
                    }
                    if (z7) {
                        this.mTotalLength += virtualChildAt3.getMeasuredWidth() + layoutParams3.leftMargin + layoutParams3.rightMargin + getNextLocationOffset(virtualChildAt3);
                        f = f2;
                    } else {
                        int i53 = this.mTotalLength;
                        f = f2;
                        this.mTotalLength = Math.max(i53, virtualChildAt3.getMeasuredWidth() + i53 + layoutParams3.leftMargin + layoutParams3.rightMargin + getNextLocationOffset(virtualChildAt3));
                    }
                    boolean z13 = mode2 != 1073741824 && layoutParams3.height == -1;
                    int i54 = layoutParams3.topMargin + layoutParams3.bottomMargin;
                    int measuredHeight3 = virtualChildAt3.getMeasuredHeight() + i54;
                    i47 = Math.max(i47, measuredHeight3);
                    if (!z13) {
                        i54 = measuredHeight3;
                    }
                    int max = Math.max(i45, i54);
                    if (z9) {
                        i12 = -1;
                        if (layoutParams3.height == -1) {
                            z = true;
                            if (z12) {
                                int baseline2 = virtualChildAt3.getBaseline();
                                if (baseline2 != i12) {
                                    int i55 = ((((layoutParams3.gravity < 0 ? this.mGravity : layoutParams3.gravity) & 112) >> 4) & -2) >> 1;
                                    iArr2[i55] = Math.max(iArr2[i55], baseline2);
                                    iArr[i55] = Math.max(iArr[i55], measuredHeight3 - baseline2);
                                }
                            }
                            z9 = z;
                            i45 = max;
                            f2 = f;
                        }
                    } else {
                        i12 = -1;
                    }
                    z = false;
                    if (z12) {
                    }
                    z9 = z;
                    i45 = max;
                    f2 = f;
                }
                i48++;
                int i56 = i;
                i44 = i11;
                virtualChildCount = i10;
            }
            i7 = i2;
            i5 = virtualChildCount;
            this.mTotalLength += getPaddingLeft() + getPaddingRight();
            if (iArr2[1] == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) {
                i9 = i47;
            } else {
                i9 = Math.max(i47, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(iArr2[1], iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
            }
            i6 = i9;
            i8 = i45;
            i3 = i46;
        } else {
            i8 = Math.max(i38, i39);
            if (z11 && mode != 1073741824) {
                for (int i57 = 0; i57 < virtualChildCount; i57++) {
                    View virtualChildAt4 = getVirtualChildAt(i57);
                    if (!(virtualChildAt4 == null || virtualChildAt4.getVisibility() == 8 || ((LayoutParams) virtualChildAt4.getLayoutParams()).weight <= 0.0f)) {
                        virtualChildAt4.measure(MeasureSpec.makeMeasureSpec(i23, 1073741824), MeasureSpec.makeMeasureSpec(virtualChildAt4.getMeasuredHeight(), 1073741824));
                    }
                }
            }
            i7 = i2;
            i5 = virtualChildCount;
            i6 = i43;
        }
        if (z9 || mode2 == 1073741824) {
            i8 = i6;
        }
        setMeasuredDimension(resolveSizeAndState | (i3 & ViewCompat.MEASURED_STATE_MASK), View.resolveSizeAndState(Math.max(i8 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i7, i3 << 16));
        if (z10) {
            forceUniformHeight(i5, i);
        }
    }

    private void forceUniformHeight(int i, int i2) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.height == -1) {
                    int i4 = layoutParams.width;
                    layoutParams.width = virtualChildAt.getMeasuredWidth();
                    measureChildWithMargins(virtualChildAt, i2, 0, makeMeasureSpec, 0);
                    layoutParams.width = i4;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void measureChildBeforeLayout(View view, int i, int i2, int i3, int i4, int i5) {
        measureChildWithMargins(view, i2, i3, i4, i5);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mOrientation == 1) {
            layoutVertical(i, i2, i3, i4);
        } else {
            layoutHorizontal(i, i2, i3, i4);
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x009f  */
    public void layoutVertical(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int paddingLeft = getPaddingLeft();
        int i9 = i3 - i;
        int paddingRight = i9 - getPaddingRight();
        int paddingRight2 = (i9 - paddingLeft) - getPaddingRight();
        int virtualChildCount = getVirtualChildCount();
        int i10 = this.mGravity;
        int i11 = i10 & 112;
        int i12 = i10 & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if (i11 == 16) {
            i5 = getPaddingTop() + (((i4 - i2) - this.mTotalLength) / 2);
        } else if (i11 != 80) {
            i5 = getPaddingTop();
        } else {
            i5 = ((getPaddingTop() + i4) - i2) - this.mTotalLength;
        }
        int i13 = 0;
        while (i13 < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(i13);
            if (virtualChildAt == null) {
                i5 += measureNullChild(i13);
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i14 = layoutParams.gravity;
                if (i14 < 0) {
                    i14 = i12;
                }
                int absoluteGravity = GravityCompat.getAbsoluteGravity(i14, ViewCompat.getLayoutDirection(this)) & 7;
                if (absoluteGravity == 1) {
                    i8 = ((paddingRight2 - measuredWidth) / 2) + paddingLeft + layoutParams.leftMargin;
                    i7 = layoutParams.rightMargin;
                } else if (absoluteGravity != 5) {
                    i6 = layoutParams.leftMargin + paddingLeft;
                    int i15 = i6;
                    if (hasDividerBeforeChildAt(i13)) {
                        i5 += this.mDividerHeight;
                    }
                    int i16 = i5 + layoutParams.topMargin;
                    LayoutParams layoutParams2 = layoutParams;
                    setChildFrame(virtualChildAt, i15, i16 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                    i13 += getChildrenSkipCount(virtualChildAt, i13);
                    i5 = i16 + measuredHeight + layoutParams2.bottomMargin + getNextLocationOffset(virtualChildAt);
                } else {
                    i8 = paddingRight - measuredWidth;
                    i7 = layoutParams.rightMargin;
                }
                i6 = i8 - i7;
                int i152 = i6;
                if (hasDividerBeforeChildAt(i13)) {
                }
                int i162 = i5 + layoutParams.topMargin;
                LayoutParams layoutParams22 = layoutParams;
                setChildFrame(virtualChildAt, i152, i162 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                i13 += getChildrenSkipCount(virtualChildAt, i13);
                i5 = i162 + measuredHeight + layoutParams22.bottomMargin + getNextLocationOffset(virtualChildAt);
            }
            i13++;
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00b0  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00e3  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00f7  */
    public void layoutHorizontal(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int paddingTop = getPaddingTop();
        int i15 = i4 - i2;
        int paddingBottom = i15 - getPaddingBottom();
        int paddingBottom2 = (i15 - paddingTop) - getPaddingBottom();
        int virtualChildCount = getVirtualChildCount();
        int i16 = this.mGravity;
        int i17 = 8388615 & i16;
        int i18 = i16 & 112;
        boolean z = this.mBaselineAligned;
        int[] iArr = this.mMaxAscent;
        int[] iArr2 = this.mMaxDescent;
        int absoluteGravity = GravityCompat.getAbsoluteGravity(i17, ViewCompat.getLayoutDirection(this));
        if (absoluteGravity == 1) {
            i5 = getPaddingLeft() + (((i3 - i) - this.mTotalLength) / 2);
        } else if (absoluteGravity != 5) {
            i5 = getPaddingLeft();
        } else {
            i5 = ((getPaddingLeft() + i3) - i) - this.mTotalLength;
        }
        if (isLayoutRtl) {
            i7 = virtualChildCount - 1;
            i6 = -1;
        } else {
            i7 = 0;
            i6 = 1;
        }
        int i19 = 0;
        while (i19 < virtualChildCount) {
            int i20 = i7 + (i6 * i19);
            View virtualChildAt = getVirtualChildAt(i20);
            if (virtualChildAt == null) {
                i5 += measureNullChild(i20);
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i21 = i19;
                if (z) {
                    i10 = virtualChildCount;
                    if (layoutParams.height != -1) {
                        i11 = virtualChildAt.getBaseline();
                        i12 = layoutParams.gravity;
                        if (i12 < 0) {
                            i12 = i18;
                        }
                        i13 = i12 & 112;
                        i9 = i18;
                        if (i13 != 16) {
                            i14 = ((((paddingBottom2 - measuredHeight) / 2) + paddingTop) + layoutParams.topMargin) - layoutParams.bottomMargin;
                        } else if (i13 == 48) {
                            i14 = layoutParams.topMargin + paddingTop;
                            if (i11 != -1) {
                                i14 += iArr[1] - i11;
                            }
                        } else if (i13 != 80) {
                            i14 = paddingTop;
                        } else {
                            i14 = (paddingBottom - measuredHeight) - layoutParams.bottomMargin;
                            if (i11 != -1) {
                                i14 -= iArr2[2] - (virtualChildAt.getMeasuredHeight() - i11);
                            }
                        }
                        if (hasDividerBeforeChildAt(i20)) {
                            i5 += this.mDividerWidth;
                        }
                        int i22 = layoutParams.leftMargin + i5;
                        View view = virtualChildAt;
                        View view2 = view;
                        int i23 = i20;
                        int locationOffset = i22 + getLocationOffset(virtualChildAt);
                        i8 = paddingTop;
                        LayoutParams layoutParams2 = layoutParams;
                        setChildFrame(view, locationOffset, i14, measuredWidth, measuredHeight);
                        View view3 = view2;
                        i19 = i21 + getChildrenSkipCount(view3, i23);
                        i5 = i22 + measuredWidth + layoutParams2.rightMargin + getNextLocationOffset(view3);
                        i19++;
                        virtualChildCount = i10;
                        i18 = i9;
                        paddingTop = i8;
                    }
                } else {
                    i10 = virtualChildCount;
                }
                i11 = -1;
                i12 = layoutParams.gravity;
                if (i12 < 0) {
                }
                i13 = i12 & 112;
                i9 = i18;
                if (i13 != 16) {
                }
                if (hasDividerBeforeChildAt(i20)) {
                }
                int i222 = layoutParams.leftMargin + i5;
                View view4 = virtualChildAt;
                View view22 = view4;
                int i232 = i20;
                int locationOffset2 = i222 + getLocationOffset(virtualChildAt);
                i8 = paddingTop;
                LayoutParams layoutParams22 = layoutParams;
                setChildFrame(view4, locationOffset2, i14, measuredWidth, measuredHeight);
                View view32 = view22;
                i19 = i21 + getChildrenSkipCount(view32, i232);
                i5 = i222 + measuredWidth + layoutParams22.rightMargin + getNextLocationOffset(view32);
                i19++;
                virtualChildCount = i10;
                i18 = i9;
                paddingTop = i8;
            } else {
                int i24 = i19;
            }
            i8 = paddingTop;
            i10 = virtualChildCount;
            i9 = i18;
            i19++;
            virtualChildCount = i10;
            i18 = i9;
            paddingTop = i8;
        }
    }

    private void setChildFrame(View view, int i, int i2, int i3, int i4) {
        view.layout(i, i2, i3 + i, i4 + i2);
    }

    public void setOrientation(int i) {
        if (this.mOrientation != i) {
            this.mOrientation = i;
            requestLayout();
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setGravity(int i) {
        if (this.mGravity != i) {
            if ((8388615 & i) == 0) {
                i |= GravityCompat.START;
            }
            if ((i & 112) == 0) {
                i |= 48;
            }
            this.mGravity = i;
            requestLayout();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setHorizontalGravity(int i) {
        int i2 = i & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int i3 = this.mGravity;
        if ((8388615 & i3) != i2) {
            this.mGravity = i2 | (-8388616 & i3);
            requestLayout();
        }
    }

    public void setVerticalGravity(int i) {
        int i2 = i & 112;
        int i3 = this.mGravity;
        if ((i3 & 112) != i2) {
            this.mGravity = i2 | (i3 & -113);
            requestLayout();
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        int i = this.mOrientation;
        if (i == 0) {
            return new LayoutParams(-2, -2);
        }
        if (i == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(ACCESSIBILITY_CLASS_NAME);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(ACCESSIBILITY_CLASS_NAME);
    }
}
