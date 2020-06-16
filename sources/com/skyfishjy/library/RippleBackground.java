package com.skyfishjy.library;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import java.util.ArrayList;
import java.util.Iterator;

public class RippleBackground extends RelativeLayout {
    private static final int DEFAULT_DURATION_TIME = 3000;
    private static final int DEFAULT_FILL_TYPE = 0;
    private static final int DEFAULT_RIPPLE_COUNT = 6;
    private static final float DEFAULT_SCALE = 6.0f;
    private boolean animationRunning = false;
    private ArrayList<Animator> animatorList;
    private AnimatorSet animatorSet;
    /* access modifiers changed from: private */
    public Paint paint;
    private int rippleAmount;
    private int rippleColor;
    private int rippleDelay;
    private int rippleDurationTime;
    private LayoutParams rippleParams;
    private float rippleRadius;
    private float rippleScale;
    /* access modifiers changed from: private */
    public float rippleStrokeWidth;
    private int rippleType;
    private ArrayList<RippleView> rippleViewList = new ArrayList<>();

    private class RippleView extends View {
        public RippleView(Context context) {
            super(context);
            setVisibility(4);
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            float min = (float) (Math.min(getWidth(), getHeight()) / 2);
            canvas.drawCircle(min, min, min - RippleBackground.this.rippleStrokeWidth, RippleBackground.this.paint);
        }
    }

    public RippleBackground(Context context) {
        super(context);
    }

    public RippleBackground(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public RippleBackground(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        if (!isInEditMode()) {
            if (attributeSet != null) {
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RippleBackground);
                this.rippleColor = obtainStyledAttributes.getColor(R.styleable.RippleBackground_rb_color, getResources().getColor(R.color.rippelColor));
                this.rippleStrokeWidth = obtainStyledAttributes.getDimension(R.styleable.RippleBackground_rb_strokeWidth, getResources().getDimension(R.dimen.rippleStrokeWidth));
                this.rippleRadius = obtainStyledAttributes.getDimension(R.styleable.RippleBackground_rb_radius, getResources().getDimension(R.dimen.rippleRadius));
                this.rippleDurationTime = obtainStyledAttributes.getInt(R.styleable.RippleBackground_rb_duration, 3000);
                this.rippleAmount = obtainStyledAttributes.getInt(R.styleable.RippleBackground_rb_rippleAmount, 6);
                this.rippleScale = obtainStyledAttributes.getFloat(R.styleable.RippleBackground_rb_scale, DEFAULT_SCALE);
                this.rippleType = obtainStyledAttributes.getInt(R.styleable.RippleBackground_rb_type, 0);
                obtainStyledAttributes.recycle();
                this.rippleDelay = this.rippleDurationTime / this.rippleAmount;
                Paint paint2 = new Paint();
                this.paint = paint2;
                paint2.setAntiAlias(true);
                if (this.rippleType == 0) {
                    this.rippleStrokeWidth = 0.0f;
                    this.paint.setStyle(Style.FILL);
                } else {
                    this.paint.setStyle(Style.STROKE);
                }
                this.paint.setColor(this.rippleColor);
                float f = this.rippleRadius;
                float f2 = this.rippleStrokeWidth;
                LayoutParams layoutParams = new LayoutParams((int) ((f + f2) * 2.0f), (int) ((f + f2) * 2.0f));
                this.rippleParams = layoutParams;
                layoutParams.addRule(13, -1);
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.animatorSet = animatorSet2;
                animatorSet2.setDuration((long) this.rippleDurationTime);
                this.animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                this.animatorList = new ArrayList<>();
                for (int i = 0; i < this.rippleAmount; i++) {
                    RippleView rippleView = new RippleView(getContext());
                    addView(rippleView, this.rippleParams);
                    this.rippleViewList.add(rippleView);
                    ObjectAnimator ofFloat = ObjectAnimator.ofFloat(rippleView, "ScaleX", new float[]{1.0f, this.rippleScale});
                    ofFloat.setRepeatCount(-1);
                    ofFloat.setRepeatMode(1);
                    ofFloat.setStartDelay((long) (this.rippleDelay * i));
                    this.animatorList.add(ofFloat);
                    ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(rippleView, "ScaleY", new float[]{1.0f, this.rippleScale});
                    ofFloat2.setRepeatCount(-1);
                    ofFloat2.setRepeatMode(1);
                    ofFloat2.setStartDelay((long) (this.rippleDelay * i));
                    this.animatorList.add(ofFloat2);
                    ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(rippleView, "Alpha", new float[]{1.0f, 0.0f});
                    ofFloat3.setRepeatCount(-1);
                    ofFloat3.setRepeatMode(1);
                    ofFloat3.setStartDelay((long) (this.rippleDelay * i));
                    this.animatorList.add(ofFloat3);
                }
                this.animatorSet.playTogether(this.animatorList);
                return;
            }
            throw new IllegalArgumentException("Attributes should be provided to this view,");
        }
    }

    public void startRippleAnimation() {
        if (!isRippleAnimationRunning()) {
            Iterator it = this.rippleViewList.iterator();
            while (it.hasNext()) {
                ((RippleView) it.next()).setVisibility(0);
            }
            this.animatorSet.start();
            this.animationRunning = true;
        }
    }

    public void stopRippleAnimation() {
        if (isRippleAnimationRunning()) {
            this.animatorSet.end();
            this.animationRunning = false;
        }
    }

    public boolean isRippleAnimationRunning() {
        return this.animationRunning;
    }
}
