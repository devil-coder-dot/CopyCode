package com.cosafe.android.service;

import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat.Builder;
import com.cosafe.android.MainActivity;
import com.cosafe.android.R;
import com.cosafe.android.base.AppController;
import com.cosafe.android.utils.BluetoothManager;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class AppService extends Service {
    public static final String TAG = AppService.class.getSimpleName();
    private BluetoothManager bluetoothManager;
    private boolean done = false;
    private Handler mHandler = new Handler();
    private boolean working = true;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        try {
            createBluetoothManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        notificationMessage("Co Safe Service", "Keeping you safe from corona");
        return 1;
    }

    private void notificationMessage(String str, String str2) {
        startForeground(1, new Builder(this, AppController.CHANNEL_ID).setContentTitle(str).setContentText(str2).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0)).build());
    }

    public void onDestroy() {
        super.onDestroy();
        this.done = true;
        this.bluetoothManager.stop();
    }

    private void createBluetoothManager() throws NoSuchAlgorithmException, IOException {
        BluetoothManager manager = BluetoothManager.getManager(this);
        this.bluetoothManager = manager;
        manager.start();
    }

    /* access modifiers changed from: private */
    public void work() {
        if (!this.done) {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            String str = "BluetoothManager";
            if (defaultAdapter == null || !defaultAdapter.isEnabled()) {
                Log.d(str, "Updating notification 1");
                if (this.working) {
                    notificationMessage("Warning", "Enable Bluetooth To Stay Safe.");
                    this.working = false;
                }
            } else {
                Log.d(str, "Updating notification 2");
                if (!this.working) {
                    notificationMessage("Co Safe Service", "Keeping you safe from corona");
                    this.working = true;
                }
            }
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    this.work();
                }
            }, 5000);
        }
    }
}
