package com.cosafe.android;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.cosafe.android.databinding.ActivityFormBindingImpl;
import com.cosafe.android.databinding.ActivityLoginBindingImpl;
import com.cosafe.android.databinding.ActivityMainBindingImpl;
import com.cosafe.android.databinding.ActivityMoreOptionBindingImpl;
import com.cosafe.android.databinding.ActivityPermissionBindingImpl;
import com.cosafe.android.databinding.ActivitySplashBindingImpl;
import com.cosafe.android.databinding.ActivityUploadBindingImpl;
import com.cosafe.android.databinding.DialogDownloadBindingImpl;
import com.cosafe.android.databinding.DialogLocationBindingImpl;
import com.cosafe.android.databinding.ToolbarBindingImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
    private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP;
    private static final int LAYOUT_ACTIVITYFORM = 1;
    private static final int LAYOUT_ACTIVITYLOGIN = 2;
    private static final int LAYOUT_ACTIVITYMAIN = 3;
    private static final int LAYOUT_ACTIVITYMOREOPTION = 4;
    private static final int LAYOUT_ACTIVITYPERMISSION = 5;
    private static final int LAYOUT_ACTIVITYSPLASH = 6;
    private static final int LAYOUT_ACTIVITYUPLOAD = 7;
    private static final int LAYOUT_DIALOGDOWNLOAD = 8;
    private static final int LAYOUT_DIALOGLOCATION = 9;
    private static final int LAYOUT_TOOLBAR = 10;

    private static class InnerBrLookup {
        static final SparseArray<String> sKeys;

        private InnerBrLookup() {
        }

        static {
            SparseArray<String> sparseArray = new SparseArray<>(1);
            sKeys = sparseArray;
            sparseArray.put(0, "_all");
        }
    }

    private static class InnerLayoutIdLookup {
        static final HashMap<String, Integer> sKeys;

        private InnerLayoutIdLookup() {
        }

        static {
            HashMap<String, Integer> hashMap = new HashMap<>(10);
            sKeys = hashMap;
            hashMap.put("layout/activity_form_0", Integer.valueOf(R.layout.activity_form));
            sKeys.put("layout/activity_login_0", Integer.valueOf(R.layout.activity_login));
            sKeys.put("layout/activity_main_0", Integer.valueOf(R.layout.activity_main));
            sKeys.put("layout/activity_more_option_0", Integer.valueOf(R.layout.activity_more_option));
            sKeys.put("layout/activity_permission_0", Integer.valueOf(R.layout.activity_permission));
            sKeys.put("layout/activity_splash_0", Integer.valueOf(R.layout.activity_splash));
            sKeys.put("layout/activity_upload_0", Integer.valueOf(R.layout.activity_upload));
            sKeys.put("layout/dialog_download_0", Integer.valueOf(R.layout.dialog_download));
            sKeys.put("layout/dialog_location_0", Integer.valueOf(R.layout.dialog_location));
            sKeys.put("layout/toolbar_0", Integer.valueOf(R.layout.toolbar));
        }
    }

    static {
        SparseIntArray sparseIntArray = new SparseIntArray(10);
        INTERNAL_LAYOUT_ID_LOOKUP = sparseIntArray;
        sparseIntArray.put(R.layout.activity_form, 1);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_login, 2);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_main, 3);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_more_option, 4);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_permission, 5);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_splash, 6);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_upload, 7);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.dialog_download, 8);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.dialog_location, 9);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.toolbar, 10);
    }

    public ViewDataBinding getDataBinder(DataBindingComponent dataBindingComponent, View view, int i) {
        int i2 = INTERNAL_LAYOUT_ID_LOOKUP.get(i);
        if (i2 > 0) {
            Object tag = view.getTag();
            if (tag != null) {
                switch (i2) {
                    case 1:
                        if ("layout/activity_form_0".equals(tag)) {
                            return new ActivityFormBindingImpl(dataBindingComponent, view);
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("The tag for activity_form is invalid. Received: ");
                        sb.append(tag);
                        throw new IllegalArgumentException(sb.toString());
                    case 2:
                        if ("layout/activity_login_0".equals(tag)) {
                            return new ActivityLoginBindingImpl(dataBindingComponent, view);
                        }
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("The tag for activity_login is invalid. Received: ");
                        sb2.append(tag);
                        throw new IllegalArgumentException(sb2.toString());
                    case 3:
                        if ("layout/activity_main_0".equals(tag)) {
                            return new ActivityMainBindingImpl(dataBindingComponent, view);
                        }
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("The tag for activity_main is invalid. Received: ");
                        sb3.append(tag);
                        throw new IllegalArgumentException(sb3.toString());
                    case 4:
                        if ("layout/activity_more_option_0".equals(tag)) {
                            return new ActivityMoreOptionBindingImpl(dataBindingComponent, view);
                        }
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("The tag for activity_more_option is invalid. Received: ");
                        sb4.append(tag);
                        throw new IllegalArgumentException(sb4.toString());
                    case 5:
                        if ("layout/activity_permission_0".equals(tag)) {
                            return new ActivityPermissionBindingImpl(dataBindingComponent, view);
                        }
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("The tag for activity_permission is invalid. Received: ");
                        sb5.append(tag);
                        throw new IllegalArgumentException(sb5.toString());
                    case 6:
                        if ("layout/activity_splash_0".equals(tag)) {
                            return new ActivitySplashBindingImpl(dataBindingComponent, view);
                        }
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("The tag for activity_splash is invalid. Received: ");
                        sb6.append(tag);
                        throw new IllegalArgumentException(sb6.toString());
                    case 7:
                        if ("layout/activity_upload_0".equals(tag)) {
                            return new ActivityUploadBindingImpl(dataBindingComponent, view);
                        }
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("The tag for activity_upload is invalid. Received: ");
                        sb7.append(tag);
                        throw new IllegalArgumentException(sb7.toString());
                    case 8:
                        if ("layout/dialog_download_0".equals(tag)) {
                            return new DialogDownloadBindingImpl(dataBindingComponent, view);
                        }
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("The tag for dialog_download is invalid. Received: ");
                        sb8.append(tag);
                        throw new IllegalArgumentException(sb8.toString());
                    case 9:
                        if ("layout/dialog_location_0".equals(tag)) {
                            return new DialogLocationBindingImpl(dataBindingComponent, view);
                        }
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("The tag for dialog_location is invalid. Received: ");
                        sb9.append(tag);
                        throw new IllegalArgumentException(sb9.toString());
                    case 10:
                        if ("layout/toolbar_0".equals(tag)) {
                            return new ToolbarBindingImpl(dataBindingComponent, view);
                        }
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append("The tag for toolbar is invalid. Received: ");
                        sb10.append(tag);
                        throw new IllegalArgumentException(sb10.toString());
                }
            } else {
                throw new RuntimeException("view must have a tag");
            }
        }
        return null;
    }

    public ViewDataBinding getDataBinder(DataBindingComponent dataBindingComponent, View[] viewArr, int i) {
        if (viewArr == null || viewArr.length == 0 || INTERNAL_LAYOUT_ID_LOOKUP.get(i) <= 0 || viewArr[0].getTag() != null) {
            return null;
        }
        throw new RuntimeException("view must have a tag");
    }

    public int getLayoutId(String str) {
        int i = 0;
        if (str == null) {
            return 0;
        }
        Integer num = (Integer) InnerLayoutIdLookup.sKeys.get(str);
        if (num != null) {
            i = num.intValue();
        }
        return i;
    }

    public String convertBrIdToString(int i) {
        return (String) InnerBrLookup.sKeys.get(i);
    }

    public List<DataBinderMapper> collectDependencies() {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
        return arrayList;
    }
}
