package com.cosafe.android;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings.Secure;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import com.cosafe.android.base.BaseActivity;
import com.cosafe.android.databinding.ActivityLoginBinding;
import com.cosafe.android.models.gson.GetOtpResponse;
import com.cosafe.android.models.gson.VerifyOtpResponse;
import com.cosafe.android.utils.MyPreferences;
import com.cosafe.android.utils.PreferenceManager;
import com.cosafe.android.utils.TextInputWatcher;
import com.cosafe.android.utils.Utility;
import com.cosafe.android.utils.retrofit.RetrofitAPIManager;
import com.cosafe.android.utils.retrofit.RetrofitResponseListener;
import java.util.List;

public class LoginActivity extends BaseActivity implements OnClickListener, RetrofitResponseListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public ActivityLoginBinding binding;
    private String device_id;
    private RetrofitAPIManager mApiManager;
    private CountDownTimer mTimer;
    private int resendCount;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_login);
        ActivityLoginBinding activityLoginBinding = (ActivityLoginBinding) DataBindingUtil.setContentView(this, R.layout.activity_login);
        this.binding = activityLoginBinding;
        activityLoginBinding.edtPhoneNumber.addTextChangedListener(new TextInputWatcher(this.binding.inputPhoneNumber));
        this.binding.edtOtp.addTextChangedListener(new TextInputWatcher(this.binding.inputOtp));
        this.mApiManager = new RetrofitAPIManager(this, this);
        this.binding.layoutVerifyOtp.setVisibility(8);
        getDeviceID();
        this.resendCount = 0;
    }

    private void showOtpVerificationView() {
        this.binding.tvTitle.setText(R.string.label_verification_code);
        this.binding.tvHeading.setText(R.string.label_otp_sent_to_mobile);
        this.binding.tvSubHeading.setVisibility(8);
        this.binding.layoutGetOtp.setVisibility(8);
        this.binding.layoutVerifyOtp.setVisibility(0);
    }

    private void getDeviceID() {
        this.device_id = Secure.getString(getContentResolver(), "android_id");
        if (VERSION.SDK_INT >= 22) {
            SubscriptionManager from = SubscriptionManager.from(getApplicationContext());
            String str = "android.permission.READ_PHONE_STATE";
            if (ActivityCompat.checkSelfPermission(this, str) != 0) {
                ActivityCompat.requestPermissions(this, new String[]{str}, 1);
            }
            List<SubscriptionInfo> activeSubscriptionInfoList = from.getActiveSubscriptionInfoList();
            StringBuilder sb = new StringBuilder();
            sb.append("Current list = ");
            sb.append(activeSubscriptionInfoList);
            Log.d("Test", sb.toString());
            for (SubscriptionInfo number : activeSubscriptionInfoList) {
                number.getNumber();
            }
            return;
        }
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_get_otp || id == R.id.btn_resend_otp) {
            if (Utility.isNetworkConnectivityExists(this)) {
                String trim = this.binding.edtPhoneNumber.getText().toString().trim();
                if (trim.isEmpty()) {
                    this.binding.inputPhoneNumber.setError("Please enter phone number");
                    return;
                } else if (trim.length() < 10) {
                    this.binding.inputPhoneNumber.setError("Please enter valid number");
                    return;
                } else {
                    Utility.dismissKeyboard(this, getCurrentFocus());
                    showLoader(this, "Please wait...Sending OTP..");
                    this.mApiManager.getOTPRequest(trim);
                }
            } else {
                Utility.showSnackBar(this.binding.getRoot(), (int) R.string.toast_no_internet_connection);
            }
            disableResendButton();
        } else if (id == R.id.btn_verify_otp) {
            if (Utility.isNetworkConnectivityExists(this)) {
                String trim2 = this.binding.edtPhoneNumber.getText().toString().trim();
                String trim3 = this.binding.edtOtp.getText().toString().trim();
                if (trim3.isEmpty()) {
                    this.binding.inputOtp.setError("Please enter OTP");
                    return;
                }
                Utility.dismissKeyboard(this, getCurrentFocus());
                showLoader(this, "Verifying OTP..");
                this.mApiManager.verifyOTPRequest(trim2, trim3, Utility.getDeviceId(this));
                new MyPreferences(this).savePref(MyPreferences.PREF_MOBILE_NUMBER, trim2);
                PreferenceManager.write(PreferenceManager.USER_MOBILE_NO, trim2);
            } else {
                Utility.showSnackBar(this.binding.getRoot(), (int) R.string.toast_no_internet_connection);
            }
        }
    }

    public void isError(String str) {
        Utility.showSnackBar(this.binding.getRoot(), str);
    }

    public void isSuccess(Object obj, int i) {
        if (i == 100) {
            dismissLoader();
            showOtpVerificationView();
            getDeviceID();
            GetOtpResponse getOtpResponse = (GetOtpResponse) obj;
            if (getOtpResponse == null || getOtpResponse.getType() == null || getOtpResponse.getType().equalsIgnoreCase("success")) {
                Utility.showSnackBar(this.binding.getRoot(), "OTP Sent");
            } else {
                Utility.showSnackBar(this.binding.getRoot(), "Error sending OTP. Check number and try again!");
            }
        } else if (i == 101) {
            dismissLoader();
            VerifyOtpResponse verifyOtpResponse = (VerifyOtpResponse) obj;
            if (verifyOtpResponse == null) {
                return;
            }
            if (verifyOtpResponse.getOk().intValue() == 1) {
                PreferenceManager.write(PreferenceManager.USER_ID, verifyOtpResponse.getResult());
                PreferenceManager.write(PreferenceManager.KEY_TOKEN, verifyOtpResponse.getKey());
                PreferenceManager.write(PreferenceManager.IS_LOGGED_IN, true);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            }
            Utility.showSnackBar(this.binding.getRoot(), verifyOtpResponse.getResult());
        }
    }

    private void disableResendButton() {
        this.binding.btnResendOtp.setEnabled(false);
        this.binding.btnResendOtp.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
        int i = this.resendCount + 1;
        this.resendCount = i;
        if (i <= 5) {
            AnonymousClass1 r2 = new CountDownTimer(20000, 1000) {
                public void onTick(long j) {
                }

                public void onFinish() {
                    LoginActivity.this.binding.btnResendOtp.setEnabled(true);
                    LoginActivity.this.binding.btnResendOtp.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                }
            };
            this.mTimer = r2;
            r2.start();
        }
    }
}
