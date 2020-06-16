package com.cosafe.android;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.cosafe.android.utils.Preset;
import com.cosafe.android.utils.SettingsUtils;

public class SettingsDialog extends Dialog {
    private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            Preset preset;
            switch (i) {
                case R.id.rb_1 /*2131296563*/:
                    preset = Preset.WARM_FLAME;
                    break;
                case R.id.rb_2 /*2131296564*/:
                    preset = Preset.NIGHT_FADE;
                    break;
                case R.id.rb_3 /*2131296565*/:
                    preset = Preset.WINTER_NEVA;
                    break;
                case R.id.rb_4 /*2131296566*/:
                    preset = Preset.MORNING_SALAD;
                    break;
                case R.id.rb_5 /*2131296567*/:
                    preset = Preset.SOFT_GRASS;
                    break;
                default:
                    preset = Preset.WARM_FLAME;
                    break;
            }
            SettingsUtils.saveSelectedPreset(preset.ordinal());
            SettingsDialog.this.listener.onPresetChanged(preset.getResId());
        }
    };
    private OnClickListener closeBtnClickListener = new OnClickListener() {
        public void onClick(View view) {
            SettingsDialog.this.dismiss();
        }
    };
    private OnSeekBarChangeListener exhaleSeekBarChangeListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            int i2 = 1000;
            SettingsDialog.this.listener.onExhaleValueChanged(i != 0 ? i * 1000 : 1000);
            if (i != 0) {
                i2 = i * 1000;
            }
            SettingsUtils.saveSelectedExhaleDuration(i2);
        }
    };
    private OnSeekBarChangeListener holdSeekBarChangeListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            int i2 = i * 1000;
            SettingsDialog.this.listener.onHoldValueChanged(i2);
            SettingsUtils.saveSelectedHoldDuration(i2);
        }
    };
    private OnSeekBarChangeListener inhaleSeekBarChangeListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            int i2 = 1000;
            SettingsDialog.this.listener.onInhaleValueChanged(i != 0 ? i * 1000 : 1000);
            if (i != 0) {
                i2 = i * 1000;
            }
            SettingsUtils.saveSelectedInhaleDuration(i2);
        }
    };
    /* access modifiers changed from: private */
    public SettingsChangeListener listener;
    private RadioGroup radioGroup;

    public interface SettingsChangeListener {
        void onExhaleValueChanged(int i);

        void onHoldValueChanged(int i);

        void onInhaleValueChanged(int i);

        void onPresetChanged(int i);
    }

    public SettingsDialog(Context context, SettingsChangeListener settingsChangeListener) {
        super(context, R.style.Theme_SettingsDialog);
        this.listener = settingsChangeListener;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.v_overlay);
        getWindow().getAttributes().windowAnimations = R.style.Theme_SettingsDialog;
        this.radioGroup = (RadioGroup) findViewById(R.id.rg_gradients);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar_inhale);
        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seekBar_exhale);
        SeekBar seekBar3 = (SeekBar) findViewById(R.id.seekBar_hold);
        Button button = (Button) findViewById(R.id.btn_close);
        this.radioGroup.setOnCheckedChangeListener(this.checkedChangeListener);
        seekBar.setOnSeekBarChangeListener(this.inhaleSeekBarChangeListener);
        seekBar2.setOnSeekBarChangeListener(this.exhaleSeekBarChangeListener);
        seekBar3.setOnSeekBarChangeListener(this.holdSeekBarChangeListener);
        button.setOnClickListener(this.closeBtnClickListener);
        ((RadioButton) this.radioGroup.getChildAt(SettingsUtils.getSelectedPreset())).setChecked(true);
        seekBar.setProgress(SettingsUtils.getSelectedInhaleDuration() / 1000);
        seekBar2.setProgress(SettingsUtils.getSelectedExhaleDuration() / 1000);
        seekBar3.setProgress(SettingsUtils.getSelectedHoldDuration() / 1000);
    }
}
