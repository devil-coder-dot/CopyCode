package com.cosafe.android.introslider;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import com.cosafe.android.LoginActivity;
import com.cosafe.android.MainActivity;
import com.cosafe.android.PermissionActivity;
import com.cosafe.android.R;
import com.cosafe.android.utils.PreferenceManager;
import com.cosafe.android.utils.Utility;

public class WelcomeActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public Button btnNext;
    /* access modifiers changed from: private */
    public Button btnSkip;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    Editor editor;
    /* access modifiers changed from: private */
    public int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;
    private PrefManager prefManager;
    /* access modifiers changed from: private */
    public ViewPager viewPager;
    OnPageChangeListener viewPagerPageChangeListener = new OnPageChangeListener() {
        public void onPageScrollStateChanged(int i) {
        }

        public void onPageScrolled(int i, float f, int i2) {
        }

        public void onPageSelected(int i) {
            WelcomeActivity.this.addBottomDots(i);
            if (i == WelcomeActivity.this.layouts.length - 1) {
                WelcomeActivity.this.btnNext.setText(WelcomeActivity.this.getString(R.string.start));
                WelcomeActivity.this.btnNext.setTextColor(WelcomeActivity.this.getResources().getColor(R.color.colorWhite));
                WelcomeActivity.this.btnSkip.setVisibility(8);
                return;
            }
            WelcomeActivity.this.btnNext.setText(WelcomeActivity.this.getString(R.string.next));
            WelcomeActivity.this.btnSkip.setVisibility(0);
        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        public MyViewPagerAdapter() {
        }

        public Object instantiateItem(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater2 = (LayoutInflater) WelcomeActivity.this.getSystemService("layout_inflater");
            this.layoutInflater = layoutInflater2;
            View inflate = layoutInflater2.inflate(WelcomeActivity.this.layouts[i], viewGroup, false);
            viewGroup.addView(inflate);
            return inflate;
        }

        public int getCount() {
            return WelcomeActivity.this.layouts.length;
        }

        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        PrefManager prefManager2 = new PrefManager(this);
        this.prefManager = prefManager2;
        if (!prefManager2.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        if (VERSION.SDK_INT >= 14) {
            getWindow().getDecorView().setSystemUiVisibility(2);
        }
        setContentView((int) R.layout.activity_welcome);
        this.viewPager = (ViewPager) findViewById(R.id.view_pager);
        this.dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        this.btnSkip = (Button) findViewById(R.id.btn_skip);
        this.btnNext = (Button) findViewById(R.id.btn_next);
        this.layouts = new int[]{R.layout.welcome_slide1, R.layout.welcome_slide2, R.layout.welcome_slide3, R.layout.welcome_slide4};
        addBottomDots(0);
        changeStatusBarColor();
        MyViewPagerAdapter myViewPagerAdapter2 = new MyViewPagerAdapter();
        this.myViewPagerAdapter = myViewPagerAdapter2;
        this.viewPager.setAdapter(myViewPagerAdapter2);
        this.viewPager.addOnPageChangeListener(this.viewPagerPageChangeListener);
        this.btnSkip.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                WelcomeActivity.this.launchHomeScreen();
            }
        });
        this.btnNext.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                int access$100 = WelcomeActivity.this.getItem(1);
                if (access$100 < WelcomeActivity.this.layouts.length) {
                    WelcomeActivity.this.viewPager.setCurrentItem(access$100);
                } else {
                    WelcomeActivity.this.launchHomeScreen();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void addBottomDots(int i) {
        TextView[] textViewArr;
        this.dots = new TextView[this.layouts.length];
        int[] intArray = getResources().getIntArray(R.array.array_dot_active);
        int[] intArray2 = getResources().getIntArray(R.array.array_dot_inactive);
        this.dotsLayout.removeAllViews();
        int i2 = 0;
        while (true) {
            textViewArr = this.dots;
            if (i2 >= textViewArr.length) {
                break;
            }
            textViewArr[i2] = new TextView(this);
            this.dots[i2].setText(Html.fromHtml("&#8226;"));
            this.dots[i2].setTextSize(35.0f);
            this.dots[i2].setTextColor(intArray2[i]);
            this.dotsLayout.addView(this.dots[i2]);
            i2++;
        }
        if (textViewArr.length > 0) {
            textViewArr[i].setTextColor(intArray[i]);
        }
    }

    /* access modifiers changed from: private */
    public int getItem(int i) {
        return this.viewPager.getCurrentItem() + i;
    }

    /* access modifiers changed from: private */
    public void launchHomeScreen() {
        this.prefManager.setFirstTimeLaunch(false);
        if (!Utility.isStoragePermissionGranted(this) || !Utility.isLocationPermissionGranted(this)) {
            startActivity(new Intent(this, PermissionActivity.class));
            finish();
        } else if (PreferenceManager.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void changeStatusBarColor() {
        if (VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        }
    }
}
