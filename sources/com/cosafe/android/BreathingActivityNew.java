package com.cosafe.android;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.cosafe.android.database.DatabaseManager;
import com.cosafe.android.models.BreathingBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class BreathingActivityNew extends AppCompatActivity {
    private final String TAG = "BreathingActivityNew";
    /* access modifiers changed from: private */
    public CustomBreatheAdapter adapter;
    /* access modifiers changed from: private */
    public DatabaseManager dbManager;
    private Button exhaleBtn;
    private Button exhaleHoldBtn;
    /* access modifiers changed from: private */
    public String exhaleHoldTime;
    /* access modifiers changed from: private */
    public String exhaleTime;
    private Button inhaleBtn;
    private Button inhaleHoldBtn;
    /* access modifiers changed from: private */
    public String inhaleHoldTime;
    /* access modifiers changed from: private */
    public String inhaleTime;
    private ListView listView;
    long startTime = 0;
    private Button stopBtn;
    /* access modifiers changed from: private */
    public Handler timerHandler = new Handler();
    /* access modifiers changed from: private */
    public Runnable timerRunnable = new Runnable() {
        public void run() {
            long currentTimeMillis = System.currentTimeMillis() - BreathingActivityNew.this.startTime;
            int i = (int) (currentTimeMillis / 1000);
            int i2 = ((int) (currentTimeMillis / 100)) % 10;
            BreathingActivityNew.this.timerTxt.setText(String.format("%d.%d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}));
            BreathingActivityNew.this.timerHandler.postDelayed(this, 100);
        }
    };
    /* access modifiers changed from: private */
    public TextView timerTxt;
    private Toolbar toolbar;
    private View view;

    private class CustomBreatheAdapter extends BaseAdapter {
        ArrayList<BreathingBean> dataAlist;

        private class ViewHolder {
            /* access modifiers changed from: private */
            public TextView dateTimeTxt;
            /* access modifiers changed from: private */
            public TextView exhaleHoldTxt;
            /* access modifiers changed from: private */
            public TextView exhaleTxt;
            /* access modifiers changed from: private */
            public TextView inhaleHoldTxt;
            /* access modifiers changed from: private */
            public TextView inhaleTxt;

            private ViewHolder() {
            }
        }

        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return 0;
        }

        public CustomBreatheAdapter(ArrayList<BreathingBean> arrayList) {
            this.dataAlist = arrayList;
        }

        /* access modifiers changed from: private */
        public void setData(ArrayList<BreathingBean> arrayList) {
            this.dataAlist = arrayList;
        }

        public int getCount() {
            return this.dataAlist.size();
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = ((LayoutInflater) BreathingActivityNew.this.getSystemService("layout_inflater")).inflate(R.layout.custom_breathe_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.dateTimeTxt = (TextView) view.findViewById(R.id.dateTimeTxt);
                viewHolder.inhaleTxt = (TextView) view.findViewById(R.id.inhaleTxt);
                viewHolder.inhaleHoldTxt = (TextView) view.findViewById(R.id.inhaleHoldTxt);
                viewHolder.exhaleTxt = (TextView) view.findViewById(R.id.exhaleTxt);
                viewHolder.exhaleHoldTxt = (TextView) view.findViewById(R.id.exhaleHoldTxt);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.dateTimeTxt.setText(((BreathingBean) this.dataAlist.get(i)).getLogTime());
            viewHolder.inhaleTxt.setText(((BreathingBean) this.dataAlist.get(i)).getInhale());
            viewHolder.inhaleHoldTxt.setText(((BreathingBean) this.dataAlist.get(i)).getInhaleHold());
            viewHolder.exhaleTxt.setText(((BreathingBean) this.dataAlist.get(i)).getExhale());
            viewHolder.exhaleHoldTxt.setText(((BreathingBean) this.dataAlist.get(i)).getExhaleHold());
            return view;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.breathing_activity_new);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.view = this.toolbar.getRootView();
        setupUi();
        this.inhaleBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BreathingActivityNew.this.enableInhaleHoldBtn();
                BreathingActivityNew.this.startTime = System.currentTimeMillis();
                BreathingActivityNew.this.timerHandler.postDelayed(BreathingActivityNew.this.timerRunnable, 0);
            }
        });
        this.inhaleHoldBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BreathingActivityNew.this.enableExhaleBtn();
                BreathingActivityNew.this.timerHandler.removeCallbacks(BreathingActivityNew.this.timerRunnable);
                BreathingActivityNew breathingActivityNew = BreathingActivityNew.this;
                breathingActivityNew.inhaleTime = breathingActivityNew.timerTxt.getText().toString();
                StringBuilder sb = new StringBuilder();
                sb.append("inhaleTime...........");
                sb.append(BreathingActivityNew.this.inhaleTime);
                Log.d("BreathingActivityNew", sb.toString());
                BreathingActivityNew.this.startTime = System.currentTimeMillis();
                BreathingActivityNew.this.timerHandler.postDelayed(BreathingActivityNew.this.timerRunnable, 0);
            }
        });
        this.exhaleBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BreathingActivityNew.this.enableExhaleHoldBtn();
                BreathingActivityNew.this.timerHandler.removeCallbacks(BreathingActivityNew.this.timerRunnable);
                BreathingActivityNew breathingActivityNew = BreathingActivityNew.this;
                breathingActivityNew.inhaleHoldTime = breathingActivityNew.timerTxt.getText().toString();
                StringBuilder sb = new StringBuilder();
                sb.append("inhaleHoldTime...........");
                sb.append(BreathingActivityNew.this.inhaleHoldTime);
                Log.d("BreathingActivityNew", sb.toString());
                BreathingActivityNew.this.startTime = System.currentTimeMillis();
                BreathingActivityNew.this.timerHandler.postDelayed(BreathingActivityNew.this.timerRunnable, 0);
            }
        });
        this.exhaleHoldBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BreathingActivityNew.this.enableStopBtn();
                BreathingActivityNew.this.timerHandler.removeCallbacks(BreathingActivityNew.this.timerRunnable);
                BreathingActivityNew breathingActivityNew = BreathingActivityNew.this;
                breathingActivityNew.exhaleTime = breathingActivityNew.timerTxt.getText().toString();
                StringBuilder sb = new StringBuilder();
                sb.append("exhaleTime...........");
                sb.append(BreathingActivityNew.this.exhaleTime);
                Log.d("BreathingActivityNew", sb.toString());
                BreathingActivityNew.this.startTime = System.currentTimeMillis();
                BreathingActivityNew.this.timerHandler.postDelayed(BreathingActivityNew.this.timerRunnable, 0);
            }
        });
        this.stopBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BreathingActivityNew.this.enableInhaleBtn();
                BreathingActivityNew.this.timerHandler.removeCallbacks(BreathingActivityNew.this.timerRunnable);
                BreathingActivityNew breathingActivityNew = BreathingActivityNew.this;
                breathingActivityNew.exhaleHoldTime = breathingActivityNew.timerTxt.getText().toString();
                StringBuilder sb = new StringBuilder();
                sb.append("exhaleHoldTime...........");
                sb.append(BreathingActivityNew.this.exhaleHoldTime);
                String str = "BreathingActivityNew";
                Log.d(str, sb.toString());
                BreathingActivityNew.this.timerTxt.setText("0.0");
                String format = new SimpleDateFormat("dd MMM yy HH:mm:ss", Locale.getDefault()).format(new Date());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("currentDateandTime....");
                sb2.append(format);
                Log.d(str, sb2.toString());
                BreathingBean breathingBean = new BreathingBean();
                breathingBean.setLogTime(format);
                breathingBean.setInhale(BreathingActivityNew.this.inhaleTime);
                breathingBean.setInhaleHold(BreathingActivityNew.this.inhaleHoldTime);
                breathingBean.setExhale(BreathingActivityNew.this.exhaleTime);
                breathingBean.setExhaleHold(BreathingActivityNew.this.exhaleHoldTime);
                BreathingActivityNew.this.dbManager.insertBreathingData(breathingBean);
                ArrayList breathingData = BreathingActivityNew.this.dbManager.getBreathingData();
                Collections.reverse(breathingData);
                BreathingActivityNew.this.adapter.setData(breathingData);
                BreathingActivityNew.this.adapter.notifyDataSetChanged();
            }
        });
    }

    private void setupUi() {
        this.dbManager = DatabaseManager.getDBAdapterInstance(this);
        this.timerTxt = (TextView) findViewById(R.id.timerTxt);
        this.inhaleBtn = (Button) findViewById(R.id.inhaleBtn);
        this.inhaleHoldBtn = (Button) findViewById(R.id.inhaleHoldBtn);
        this.exhaleBtn = (Button) findViewById(R.id.exhaleBtn);
        this.exhaleHoldBtn = (Button) findViewById(R.id.exhaleHoldBtn);
        this.stopBtn = (Button) findViewById(R.id.stopBtn);
        this.listView = (ListView) findViewById(R.id.listView);
        enableInhaleBtn();
        String str = "";
        this.inhaleTime = str;
        this.inhaleHoldTime = str;
        this.exhaleTime = str;
        this.exhaleHoldTime = str;
        ArrayList breathingData = this.dbManager.getBreathingData();
        Collections.reverse(breathingData);
        CustomBreatheAdapter customBreatheAdapter = new CustomBreatheAdapter(breathingData);
        this.adapter = customBreatheAdapter;
        this.listView.setAdapter(customBreatheAdapter);
    }

    /* access modifiers changed from: private */
    public void enableInhaleBtn() {
        this.inhaleBtn.setEnabled(true);
        this.inhaleHoldBtn.setEnabled(false);
        this.exhaleBtn.setEnabled(false);
        this.exhaleHoldBtn.setEnabled(false);
        this.inhaleBtn.setBackgroundResource(R.drawable.bg_primary_btn_round);
        this.inhaleHoldBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.exhaleBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.exhaleHoldBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.stopBtn.setVisibility(4);
    }

    /* access modifiers changed from: private */
    public void enableInhaleHoldBtn() {
        this.inhaleBtn.setEnabled(false);
        this.inhaleHoldBtn.setEnabled(true);
        this.exhaleBtn.setEnabled(false);
        this.exhaleHoldBtn.setEnabled(false);
        this.inhaleBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.inhaleHoldBtn.setBackgroundResource(R.drawable.bg_primary_btn_round);
        this.exhaleBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.exhaleHoldBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.stopBtn.setVisibility(4);
    }

    /* access modifiers changed from: private */
    public void enableExhaleBtn() {
        this.inhaleBtn.setEnabled(false);
        this.inhaleHoldBtn.setEnabled(false);
        this.exhaleBtn.setEnabled(true);
        this.exhaleHoldBtn.setEnabled(false);
        this.inhaleBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.inhaleHoldBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.exhaleBtn.setBackgroundResource(R.drawable.bg_primary_btn_round);
        this.exhaleHoldBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.stopBtn.setVisibility(4);
    }

    /* access modifiers changed from: private */
    public void enableExhaleHoldBtn() {
        this.inhaleBtn.setEnabled(false);
        this.inhaleHoldBtn.setEnabled(false);
        this.exhaleBtn.setEnabled(false);
        this.exhaleHoldBtn.setEnabled(true);
        this.inhaleBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.inhaleHoldBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.exhaleBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.exhaleHoldBtn.setBackgroundResource(R.drawable.bg_primary_btn_round);
        this.stopBtn.setVisibility(4);
    }

    /* access modifiers changed from: private */
    public void enableStopBtn() {
        this.inhaleBtn.setEnabled(false);
        this.inhaleHoldBtn.setEnabled(false);
        this.exhaleBtn.setEnabled(false);
        this.exhaleHoldBtn.setEnabled(false);
        this.inhaleBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.inhaleHoldBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.exhaleBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.exhaleHoldBtn.setBackgroundResource(R.drawable.bg_grey_btn_round);
        this.stopBtn.setVisibility(0);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        try {
            this.timerHandler.removeCallbacks(this.timerRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
