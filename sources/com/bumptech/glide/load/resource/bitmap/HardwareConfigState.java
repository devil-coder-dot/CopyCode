package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;

public final class HardwareConfigState {
    public static final int DEFAULT_MAXIMUM_FDS_FOR_HARDWARE_CONFIGS = 700;
    public static final int DEFAULT_MIN_HARDWARE_DIMENSION = 128;
    private static final File FD_SIZE_LIST = new File("/proc/self/fd");
    private static final int MINIMUM_DECODES_BETWEEN_FD_CHECKS = 50;
    private static volatile int fdSizeLimit = DEFAULT_MAXIMUM_FDS_FOR_HARDWARE_CONFIGS;
    private static volatile HardwareConfigState instance;
    private static volatile int minHardwareDimension = 128;
    private int decodesSinceLastFdCheck;
    private boolean isFdSizeBelowHardwareLimit = true;
    private final boolean isHardwareConfigAllowedByDeviceModel = isHardwareConfigAllowedByDeviceModel();

    public static HardwareConfigState getInstance() {
        if (instance == null) {
            synchronized (HardwareConfigState.class) {
                if (instance == null) {
                    instance = new HardwareConfigState();
                }
            }
        }
        return instance;
    }

    HardwareConfigState() {
    }

    public boolean isHardwareConfigAllowed(int i, int i2, boolean z, boolean z2) {
        if (!z || !this.isHardwareConfigAllowedByDeviceModel || VERSION.SDK_INT < 26 || z2 || i < minHardwareDimension || i2 < minHardwareDimension || !isFdSizeBelowHardwareLimit()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean setHardwareConfigIfAllowed(int i, int i2, Options options, boolean z, boolean z2) {
        boolean isHardwareConfigAllowed = isHardwareConfigAllowed(i, i2, z, z2);
        if (isHardwareConfigAllowed) {
            options.inPreferredConfig = Config.HARDWARE;
            options.inMutable = false;
        }
        return isHardwareConfigAllowed;
    }

    private static boolean isHardwareConfigAllowedByDeviceModel() {
        boolean z = true;
        if (Build.MODEL != null && Build.MODEL.length() >= 7) {
            String substring = Build.MODEL.substring(0, 7);
            char c = 65535;
            switch (substring.hashCode()) {
                case -1398613787:
                    if (substring.equals("SM-A520")) {
                        c = 6;
                        break;
                    }
                    break;
                case -1398431166:
                    if (substring.equals("SM-G930")) {
                        c = 5;
                        break;
                    }
                    break;
                case -1398431161:
                    if (substring.equals("SM-G935")) {
                        c = 4;
                        break;
                    }
                    break;
                case -1398431073:
                    if (substring.equals("SM-G960")) {
                        c = 2;
                        break;
                    }
                    break;
                case -1398431068:
                    if (substring.equals("SM-G965")) {
                        c = 3;
                        break;
                    }
                    break;
                case -1398343746:
                    if (substring.equals("SM-J720")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1398222624:
                    if (substring.equals("SM-N935")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    if (VERSION.SDK_INT == 26) {
                        z = false;
                        break;
                    }
                    break;
                default:
                    return true;
            }
        }
        return z;
    }

    private synchronized boolean isFdSizeBelowHardwareLimit() {
        boolean z = true;
        int i = this.decodesSinceLastFdCheck + 1;
        this.decodesSinceLastFdCheck = i;
        if (i >= 50) {
            this.decodesSinceLastFdCheck = 0;
            int length = FD_SIZE_LIST.list().length;
            if (length >= fdSizeLimit) {
                z = false;
            }
            this.isFdSizeBelowHardwareLimit = z;
            if (!z && Log.isLoggable("Downsampler", 5)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Excluding HARDWARE bitmap config because we're over the file descriptor limit, file descriptors ");
                sb.append(length);
                sb.append(", limit ");
                sb.append(fdSizeLimit);
                Log.w("Downsampler", sb.toString());
            }
        }
        return this.isFdSizeBelowHardwareLimit;
    }
}
