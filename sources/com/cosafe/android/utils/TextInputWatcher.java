package com.cosafe.android.utils;

import android.text.Editable;
import android.text.TextWatcher;
import com.google.android.material.textfield.TextInputLayout;

public class TextInputWatcher implements TextWatcher {
    private TextInputLayout textInputLayout;

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public TextInputWatcher(TextInputLayout textInputLayout2) {
        this.textInputLayout = textInputLayout2;
    }

    public void afterTextChanged(Editable editable) {
        this.textInputLayout.setErrorEnabled(false);
        this.textInputLayout.setError("");
    }
}
