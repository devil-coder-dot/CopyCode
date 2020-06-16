package com.cosafe.android;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.databinding.DataBindingUtil;
import com.cosafe.android.base.BaseActivity;
import com.cosafe.android.databinding.ActivityFormBinding;
import com.cosafe.android.models.GetUserID;
import com.cosafe.android.models.SubmitFormResponse;
import com.cosafe.android.utils.MyPreferences;
import com.cosafe.android.utils.PreferenceManager;
import com.cosafe.android.utils.Utility;
import com.cosafe.android.utils.retrofit.RetrofitAPIManager;
import com.cosafe.android.utils.retrofit.RetrofitResponseListener;
import com.google.gson.JsonObject;

public class FormActivity extends BaseActivity implements RetrofitResponseListener {
    /* access modifiers changed from: private */
    public ActivityFormBinding binding;
    /* access modifiers changed from: private */
    public RetrofitAPIManager mApiManager;
    /* access modifiers changed from: private */
    public double numResizer = 10.0d;
    double numTemp = -1.0d;
    /* access modifiers changed from: private */
    public double offSet = 95.0d;
    private RadioGroup question1Rg;
    private RadioGroup question3Rg;
    private RadioGroup question4Rg;
    private RadioGroup question5Rg;
    private RadioGroup question6Rg;
    private SeekBar seekBar;
    int selectedId1;
    int selectedId3;
    int selectedId4;
    int selectedId5;
    int selectedId6;
    /* access modifiers changed from: private */
    public TextView selectedTemp;
    private Button submitBtn;
    /* access modifiers changed from: private */
    public String user_id;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityFormBinding activityFormBinding = (ActivityFormBinding) DataBindingUtil.setContentView(this, R.layout.activity_form);
        this.binding = activityFormBinding;
        setToolbarTitle(activityFormBinding.layoutToolbar.toolbar, "Questionnaire", true);
        this.mApiManager = new RetrofitAPIManager(this, this);
        this.mApiManager.getUserIDRequest(new MyPreferences(this).getPref(MyPreferences.PREF_MOBILE_NUMBER, "9910233316"));
        this.seekBar = (SeekBar) findViewById(R.id.question2Sb);
        this.selectedTemp = (TextView) findViewById(R.id.currTempTv);
        this.submitBtn = (Button) findViewById(R.id.btn_submit);
        this.question1Rg = (RadioGroup) findViewById(R.id.question1Rg);
        this.question3Rg = (RadioGroup) findViewById(R.id.question3Rg);
        this.question4Rg = (RadioGroup) findViewById(R.id.question4Rg);
        this.question5Rg = (RadioGroup) findViewById(R.id.question5Rg);
        this.question6Rg = (RadioGroup) findViewById(R.id.question6Rg);
        initListeners();
    }

    public void initListeners() {
        this.seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                FormActivity formActivity = FormActivity.this;
                formActivity.numTemp = (((double) i) / formActivity.numResizer) + FormActivity.this.offSet;
                TextView access$200 = FormActivity.this.selectedTemp;
                StringBuilder sb = new StringBuilder();
                sb.append(FormActivity.this.numTemp);
                sb.append(" °F");
                access$200.setText(sb.toString());
            }
        });
        this.submitBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!Utility.isNetworkConnectivityExists(FormActivity.this.getApplicationContext())) {
                    Utility.showSnackBar(FormActivity.this.binding.getRoot(), (int) R.string.toast_no_internet_connection);
                } else if (FormActivity.this.checkDataValidation()) {
                    FormActivity formActivity = FormActivity.this;
                    FormActivity formActivity2 = FormActivity.this;
                    FormActivity formActivity3 = FormActivity.this;
                    FormActivity formActivity4 = FormActivity.this;
                    FormActivity formActivity5 = FormActivity.this;
                    String[] strArr = {((RadioButton) formActivity.findViewById(formActivity.selectedId1)).getText().toString(), Double.toString(FormActivity.this.numTemp), ((RadioButton) formActivity2.findViewById(formActivity2.selectedId3)).getText().toString(), ((RadioButton) formActivity3.findViewById(formActivity3.selectedId4)).getText().toString(), ((RadioButton) formActivity4.findViewById(formActivity4.selectedId5)).getText().toString(), ((RadioButton) formActivity5.findViewById(formActivity5.selectedId6)).getText().toString()};
                    JsonObject jsonObject = new JsonObject();
                    String str = "userid";
                    if (FormActivity.this.user_id != null) {
                        jsonObject.addProperty(str, FormActivity.this.user_id);
                    } else {
                        jsonObject.addProperty(str, (Number) Integer.valueOf(3));
                    }
                    jsonObject.addProperty("answer1", strArr[0]);
                    jsonObject.addProperty("answer2", strArr[1]);
                    jsonObject.addProperty("answer3", strArr[2]);
                    jsonObject.addProperty("answer4", strArr[3]);
                    jsonObject.addProperty("answer5", strArr[4]);
                    jsonObject.addProperty("answer6", strArr[5]);
                    PreferenceManager.read(PreferenceManager.USER_ID, "ere");
                    try {
                        FormActivity.this.showLoader(FormActivity.this, "Sending..");
                        FormActivity.this.mApiManager.submitQnA(jsonObject);
                    } catch (Exception unused) {
                        Utility.showSnackBar(FormActivity.this.binding.getRoot(), "Error Occurred. Please try again later.");
                    }
                }
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public boolean checkDataValidation() {
        this.selectedId1 = this.question1Rg.getCheckedRadioButtonId();
        this.selectedId3 = this.question3Rg.getCheckedRadioButtonId();
        this.selectedId4 = this.question4Rg.getCheckedRadioButtonId();
        this.selectedId5 = this.question5Rg.getCheckedRadioButtonId();
        int checkedRadioButtonId = this.question6Rg.getCheckedRadioButtonId();
        this.selectedId6 = checkedRadioButtonId;
        if (this.selectedId1 != -1 && this.selectedId3 != -1 && this.selectedId4 != -1 && this.selectedId5 != -1 && checkedRadioButtonId != -1 && this.numTemp != -1.0d) {
            return true;
        }
        Utility.showSnackBar(this.binding.getRoot(), "Please fill all details!");
        return false;
    }

    public void isError(String str) {
        Utility.showSnackBar(this.binding.getRoot(), str);
    }

    public void isSuccess(Object obj, int i) {
        String str = "success";
        if (i == 103) {
            dismissLoader();
            SubmitFormResponse submitFormResponse = (SubmitFormResponse) obj;
            if (submitFormResponse == null || submitFormResponse.getStatusCode() == null || submitFormResponse.getMessage().equalsIgnoreCase(str)) {
                showDialog("Thank you for taking out time to fill this Questionnaire. Stay safe and follow all precautions!");
            } else {
                Utility.showSnackBar(this.binding.getRoot(), "Error Sending Responses. Please try again");
            }
        } else if (i == 104) {
            dismissLoader();
            GetUserID getUserID = (GetUserID) obj;
            if (getUserID == null || getUserID.getStatusCode() == null || getUserID.getMessage().equalsIgnoreCase(str)) {
                this.user_id = getUserID.getRows().getId();
            }
        }
    }

    private void showDialog(String str) {
        Builder builder = new Builder(this);
        builder.setTitle((CharSequence) "Questionnaire Status");
        builder.setMessage((CharSequence) str).setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                FormActivity.this.finish();
            }
        });
        AlertDialog create = builder.create();
        create.setCancelable(false);
        create.show();
    }
}
