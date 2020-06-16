package com.cosafe.android.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.cosafe.android.R;

public abstract class ActivityFormBinding extends ViewDataBinding {
    public final AppCompatButton btnSubmit;
    public final TextView currTempTv;
    public final ToolbarBinding layoutToolbar;
    public final ConstraintLayout mainCl;
    public final ConstraintLayout question1Cl;
    public final TextView question1NumTv;
    public final RadioGroup question1Rg;
    public final TextView question1Tv;
    public final RadioButton question1option1Rb;
    public final RadioButton question1option2Rb;
    public final ConstraintLayout question2Cl;
    public final TextView question2NumTv;
    public final SeekBar question2Sb;
    public final TextView question2Tv;
    public final ConstraintLayout question3Cl;
    public final TextView question3NumTv;
    public final RadioGroup question3Rg;
    public final TextView question3Tv;
    public final RadioButton question3option1Rb;
    public final RadioButton question3option2Rb;
    public final ConstraintLayout question4Cl;
    public final TextView question4NumTv;
    public final RadioGroup question4Rg;
    public final TextView question4Tv;
    public final RadioButton question4option1Rb;
    public final RadioButton question4option2Rb;
    public final ConstraintLayout question5Cl;
    public final TextView question5NumTv;
    public final RadioGroup question5Rg;
    public final TextView question5Tv;
    public final RadioButton question5option1Rb;
    public final RadioButton question5option2Rb;
    public final ConstraintLayout question6Cl;
    public final TextView question6NumTv;
    public final RadioGroup question6Rg;
    public final TextView question6Tv;
    public final RadioButton question6option1Rb;
    public final RadioButton question6option2Rb;

    protected ActivityFormBinding(Object obj, View view, int i, AppCompatButton appCompatButton, TextView textView, ToolbarBinding toolbarBinding, ConstraintLayout constraintLayout, ConstraintLayout constraintLayout2, TextView textView2, RadioGroup radioGroup, TextView textView3, RadioButton radioButton, RadioButton radioButton2, ConstraintLayout constraintLayout3, TextView textView4, SeekBar seekBar, TextView textView5, ConstraintLayout constraintLayout4, TextView textView6, RadioGroup radioGroup2, TextView textView7, RadioButton radioButton3, RadioButton radioButton4, ConstraintLayout constraintLayout5, TextView textView8, RadioGroup radioGroup3, TextView textView9, RadioButton radioButton5, RadioButton radioButton6, ConstraintLayout constraintLayout6, TextView textView10, RadioGroup radioGroup4, TextView textView11, RadioButton radioButton7, RadioButton radioButton8, ConstraintLayout constraintLayout7, TextView textView12, RadioGroup radioGroup5, TextView textView13, RadioButton radioButton9, RadioButton radioButton10) {
        ToolbarBinding toolbarBinding2 = toolbarBinding;
        super(obj, view, i);
        this.btnSubmit = appCompatButton;
        this.currTempTv = textView;
        this.layoutToolbar = toolbarBinding2;
        setContainedBinding(toolbarBinding);
        this.mainCl = constraintLayout;
        this.question1Cl = constraintLayout2;
        this.question1NumTv = textView2;
        this.question1Rg = radioGroup;
        this.question1Tv = textView3;
        this.question1option1Rb = radioButton;
        this.question1option2Rb = radioButton2;
        this.question2Cl = constraintLayout3;
        this.question2NumTv = textView4;
        this.question2Sb = seekBar;
        this.question2Tv = textView5;
        this.question3Cl = constraintLayout4;
        this.question3NumTv = textView6;
        this.question3Rg = radioGroup2;
        this.question3Tv = textView7;
        this.question3option1Rb = radioButton3;
        this.question3option2Rb = radioButton4;
        this.question4Cl = constraintLayout5;
        this.question4NumTv = textView8;
        this.question4Rg = radioGroup3;
        this.question4Tv = textView9;
        this.question4option1Rb = radioButton5;
        this.question4option2Rb = radioButton6;
        this.question5Cl = constraintLayout6;
        this.question5NumTv = textView10;
        this.question5Rg = radioGroup4;
        this.question5Tv = textView11;
        this.question5option1Rb = radioButton7;
        this.question5option2Rb = radioButton8;
        this.question6Cl = constraintLayout7;
        this.question6NumTv = textView12;
        this.question6Rg = radioGroup5;
        this.question6Tv = textView13;
        this.question6option1Rb = radioButton9;
        this.question6option2Rb = radioButton10;
    }

    public static ActivityFormBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityFormBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z, Object obj) {
        return (ActivityFormBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.activity_form, viewGroup, z, obj);
    }

    public static ActivityFormBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityFormBinding inflate(LayoutInflater layoutInflater, Object obj) {
        return (ActivityFormBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.activity_form, null, false, obj);
    }

    public static ActivityFormBinding bind(View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }

    @Deprecated
    public static ActivityFormBinding bind(View view, Object obj) {
        return (ActivityFormBinding) bind(obj, view, R.layout.activity_form);
    }
}
