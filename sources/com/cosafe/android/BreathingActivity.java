package com.cosafe.android;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.cosafe.android.SettingsDialog.SettingsChangeListener;
import com.cosafe.android.utils.BreathePreferences;
import com.cosafe.android.utils.Constants;
import com.cosafe.android.utils.SettingsUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BreathingActivity extends AppCompatActivity implements SettingsChangeListener {
    /* access modifiers changed from: private */
    public static final String TAG = BreathingActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public Animation animationExhaleInnerCircle;
    /* access modifiers changed from: private */
    public Animation animationExhaleText;
    /* access modifiers changed from: private */
    public Animation animationInhaleInnerCircle;
    /* access modifiers changed from: private */
    public Animation animationInhaleText;
    private ConstraintLayout contentLayout;
    private AnimationListener exhaleAnimationListener = new AnimationListener() {
        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            Log.d(BreathingActivity.TAG, "exhale animation end");
            BreathingActivity.this.statusText.setText(Constants.HOLD);
            BreathingActivity.this.handler.postDelayed(new Runnable() {
                public void run() {
                    BreathingActivity.this.statusText.setText(Constants.INHALE);
                    BreathingActivity.this.statusText.startAnimation(BreathingActivity.this.animationInhaleText);
                    BreathingActivity.this.innerCircleView.startAnimation(BreathingActivity.this.animationInhaleInnerCircle);
                }
            }, (long) BreathingActivity.this.holdDuration);
        }
    };
    private FloatingActionButton fab;
    private OnClickListener fabClickListener = new OnClickListener() {
        public void onClick(View view) {
            BreathingActivity.this.showSettingsDialog();
        }
    };
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    /* access modifiers changed from: private */
    public int holdDuration = 0;
    private AnimationListener inhaleAnimationListener = new AnimationListener() {
        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            Log.d(BreathingActivity.TAG, "inhale animation end");
            BreathingActivity.this.statusText.setText(Constants.HOLD);
            BreathingActivity.this.handler.postDelayed(new Runnable() {
                public void run() {
                    BreathingActivity.this.statusText.setText(Constants.EXHALE);
                    BreathingActivity.this.statusText.startAnimation(BreathingActivity.this.animationExhaleText);
                    BreathingActivity.this.innerCircleView.startAnimation(BreathingActivity.this.animationExhaleInnerCircle);
                }
            }, (long) BreathingActivity.this.holdDuration);
        }
    };
    /* access modifiers changed from: private */
    public View innerCircleView;
    private View outerCircleView;
    /* access modifiers changed from: private */
    public TextView statusText;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.breathing_activity);
        BreathePreferences.init(this);
        this.contentLayout = (ConstraintLayout) findViewById(R.id.lt_content);
        TextView textView = (TextView) findViewById(R.id.txt_status);
        this.statusText = textView;
        textView.setText(Constants.INHALE);
        this.outerCircleView = findViewById(R.id.v_circle_outer);
        this.innerCircleView = findViewById(R.id.v_circle_inner);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        this.fab = floatingActionButton;
        floatingActionButton.setOnClickListener(this.fabClickListener);
        setupBackgroundColor();
        prepareAnimations();
        this.statusText.startAnimation(this.animationInhaleText);
        this.innerCircleView.startAnimation(this.animationInhaleInnerCircle);
    }

    private void setupBackgroundColor() {
        setOuterCircleBackground(SettingsUtils.getBackgroundByPresetPosition(SettingsUtils.getSelectedPreset()));
    }

    private void setOuterCircleBackground(int i) {
        this.outerCircleView.setBackgroundResource(i);
    }

    private void setInhaleDuration(int i) {
        long j = (long) i;
        this.animationInhaleText.setDuration(j);
        this.animationInhaleInnerCircle.setDuration(j);
    }

    private void setExhaleDuration(int i) {
        long j = (long) i;
        this.animationExhaleText.setDuration(j);
        this.animationExhaleInnerCircle.setDuration(j);
    }

    private void prepareAnimations() {
        int selectedInhaleDuration = SettingsUtils.getSelectedInhaleDuration();
        int selectedExhaleDuration = SettingsUtils.getSelectedExhaleDuration();
        this.holdDuration = SettingsUtils.getSelectedHoldDuration();
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_text_inhale);
        this.animationInhaleText = loadAnimation;
        loadAnimation.setFillAfter(true);
        this.animationInhaleText.setAnimationListener(this.inhaleAnimationListener);
        Animation loadAnimation2 = AnimationUtils.loadAnimation(this, R.anim.anim_inner_circle_inhale);
        this.animationInhaleInnerCircle = loadAnimation2;
        loadAnimation2.setFillAfter(true);
        this.animationInhaleInnerCircle.setAnimationListener(this.inhaleAnimationListener);
        setInhaleDuration(selectedInhaleDuration);
        Animation loadAnimation3 = AnimationUtils.loadAnimation(this, R.anim.anim_text_exhale);
        this.animationExhaleText = loadAnimation3;
        loadAnimation3.setFillAfter(true);
        this.animationExhaleText.setAnimationListener(this.exhaleAnimationListener);
        Animation loadAnimation4 = AnimationUtils.loadAnimation(this, R.anim.anim_inner_circle_exhale);
        this.animationExhaleInnerCircle = loadAnimation4;
        loadAnimation4.setFillAfter(true);
        this.animationExhaleInnerCircle.setAnimationListener(this.exhaleAnimationListener);
        setExhaleDuration(selectedExhaleDuration);
    }

    /* access modifiers changed from: private */
    public void showSettingsDialog() {
        new SettingsDialog(this, this).show();
    }

    public void onPresetChanged(int i) {
        setOuterCircleBackground(i);
    }

    public void onInhaleValueChanged(int i) {
        setInhaleDuration(i);
    }

    public void onExhaleValueChanged(int i) {
        setExhaleDuration(i);
    }

    public void onHoldValueChanged(int i) {
        this.holdDuration = i;
    }
}
