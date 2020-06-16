package com.cosafe.android.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings.Builder;
import android.content.Context;
import android.os.Environment;
import android.os.ParcelUuid;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

public class BluetoothManager {
    private static BluetoothManager manager;
    private BluetoothLeAdvertiser advertiser;
    private AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
        public void onStartSuccess(AdvertiseSettings advertiseSettings) {
            super.onStartSuccess(advertiseSettings);
            Log.d("BluetoothManager", "Advertisement Successful");
        }

        public void onStartFailure(int i) {
            super.onStartFailure(i);
            Log.d("BluetoothManager", "Advertisement Failed");
        }
    };
    /* access modifiers changed from: private */
    public TreeSet<String> contacts = new TreeSet<>();
    private File file;
    /* access modifiers changed from: private */
    public FileOutputStream output;
    private ScanCallback scanCallback = new ScanCallback() {
        public void onScanResult(int i, ScanResult scanResult) {
            super.onScanResult(i, scanResult);
            String str = "BluetoothManager";
            Log.d(str, "ScanResult");
            ScanRecord scanRecord = scanResult.getScanRecord();
            if (scanRecord == null) {
                Log.d(str, "No Scan Record found");
            } else if (scanResult.getRssi() >= -100) {
                List serviceUuids = scanRecord.getServiceUuids();
                if (serviceUuids == null || serviceUuids.size() == 0) {
                    Log.d(str, "No service Id found");
                } else {
                    byte[] access$000 = BluetoothManager.getBytesFromUUID(((ParcelUuid) serviceUuids.get(0)).getUuid());
                    for (int i2 = 0; i2 < 10; i2++) {
                        if (access$000[i2] != 4) {
                            Log.d(str, "Unknown service");
                            return;
                        }
                    }
                    ByteBuffer allocate = ByteBuffer.allocate(10);
                    allocate.put(access$000, 10, 6);
                    allocate.putInt((int) (System.currentTimeMillis() / 1000));
                    Log.d(str, "Writing bluetooth id");
                    try {
                        BluetoothManager.this.output.write(allocate.array());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BluetoothManager.this.contacts.add(BluetoothManager.bytesToString(access$000, 10, 6));
                }
            }
        }

        public void onScanFailed(int i) {
            super.onScanFailed(i);
            StringBuilder sb = new StringBuilder();
            sb.append("Scan failed");
            sb.append(i);
            Log.d("BluetoothManager", sb.toString());
        }
    };
    private BluetoothLeScanner scanner;
    private ParcelUuid serviceId;

    private BluetoothManager(byte[] bArr, File file2) throws IOException {
        this.file = file2;
        byte[] bArr2 = new byte[16];
        for (int i = 0; i < 10; i++) {
            bArr2[i] = 4;
        }
        System.arraycopy(bArr, 0, bArr2, 10, 6);
        this.serviceId = new ParcelUuid(getUUIDFromBytes(bArr2));
        if (this.file.exists()) {
            this.output = new FileOutputStream(this.file, true);
            return;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(this.file);
        this.output = fileOutputStream;
        fileOutputStream.write(bArr);
    }

    private void discover() {
        String str = "BluetoothManager";
        Log.d(str, "Discovering");
        if (this.scanner == null) {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (defaultAdapter == null) {
                Log.d(str, "Bluetooth not available");
                return;
            }
            BluetoothLeScanner bluetoothLeScanner = defaultAdapter.getBluetoothLeScanner();
            this.scanner = bluetoothLeScanner;
            if (bluetoothLeScanner == null) {
                Log.d(str, "Bluetooth advertiser not available");
                return;
            }
        }
        this.scanner.startScan(null, new Builder().setScanMode(0).build(), this.scanCallback);
    }

    private void advertise() {
        String str = "BluetoothManager";
        Log.d(str, "Advertising");
        if (this.advertiser == null) {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (defaultAdapter == null) {
                Log.d(str, "Bluetooth not available");
                return;
            }
            BluetoothLeAdvertiser bluetoothLeAdvertiser = defaultAdapter.getBluetoothLeAdvertiser();
            this.advertiser = bluetoothLeAdvertiser;
            if (bluetoothLeAdvertiser == null) {
                Log.d(str, "Bluetooth advertiser not available");
                return;
            }
        }
        this.advertiser.startAdvertising(new AdvertiseSettings.Builder().setAdvertiseMode(1).setTxPowerLevel(0).setConnectable(false).build(), new AdvertiseData.Builder().addServiceUuid(this.serviceId).setIncludeTxPowerLevel(true).build(), this.advertisingCallback);
    }

    /* access modifiers changed from: private */
    public static byte[] getBytesFromUUID(UUID uuid) {
        ByteBuffer wrap = ByteBuffer.wrap(new byte[16]);
        wrap.putLong(uuid.getMostSignificantBits());
        wrap.putLong(uuid.getLeastSignificantBits());
        return wrap.array();
    }

    private static UUID getUUIDFromBytes(byte[] bArr) {
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        return new UUID(wrap.getLong(), wrap.getLong());
    }

    public static BluetoothManager getManager(Context context) throws IOException, NoSuchAlgorithmException {
        BluetoothManager bluetoothManager = manager;
        if (bluetoothManager != null) {
            return bluetoothManager;
        }
        MessageDigest instance = MessageDigest.getInstance("SHA");
        instance.update(Secure.getString(context.getContentResolver(), "android_id").getBytes(Charset.forName("utf-8")));
        File file2 = new File(Environment.getExternalStorageDirectory(), "SafeTogetherLogger");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File file3 = new File(file2, "bluetooth_file.txt");
        byte[] bArr = new byte[6];
        System.arraycopy(instance.digest(), 0, bArr, 0, 6);
        BluetoothManager bluetoothManager2 = new BluetoothManager(bArr, file3);
        manager = bluetoothManager2;
        return bluetoothManager2;
    }

    public void start() {
        advertise();
        discover();
    }

    public void stop() {
        BluetoothLeAdvertiser bluetoothLeAdvertiser = this.advertiser;
        if (bluetoothLeAdvertiser != null) {
            bluetoothLeAdvertiser.stopAdvertising(this.advertisingCallback);
        }
        BluetoothLeScanner bluetoothLeScanner = this.scanner;
        if (bluetoothLeScanner != null) {
            bluetoothLeScanner.stopScan(this.scanCallback);
        }
    }

    public int getContactCount() {
        return this.contacts.size();
    }

    /* access modifiers changed from: 0000 */
    public int compare(File file2) throws IOException {
        BluetoothData readFile = readFile(file2);
        BluetoothData readFile2 = readFile(this.file);
        StringBuilder sb = new StringBuilder();
        sb.append(readFile2.records.size());
        sb.append(" ");
        sb.append(readFile.records.size());
        Log.d("BluetoothManager", sb.toString());
        Iterator it = readFile.records.iterator();
        while (it.hasNext()) {
            BluetoothPoint bluetoothPoint = (BluetoothPoint) it.next();
            if (bluetoothPoint.id.compareTo(readFile2.id) == 0) {
                return bluetoothPoint.time;
            }
        }
        Iterator it2 = readFile2.records.iterator();
        while (it2.hasNext()) {
            BluetoothPoint bluetoothPoint2 = (BluetoothPoint) it2.next();
            if (bluetoothPoint2.id.compareTo(readFile.id) == 0) {
                return bluetoothPoint2.time;
            }
        }
        return -1;
    }

    private static BluetoothData readFile(File file2) throws IOException {
        ArrayList arrayList = new ArrayList();
        FileInputStream fileInputStream = new FileInputStream(file2);
        byte[] bArr = new byte[6];
        int i = -1;
        do {
            int read = fileInputStream.read();
            if (read == -1) {
                break;
            }
            i++;
            bArr[i] = (byte) read;
        } while (i != 5);
        byte[] bArr2 = new byte[10];
        while (true) {
            int read2 = fileInputStream.read();
            if (read2 == -1) {
                return new BluetoothData(bytesToString(bArr), arrayList);
            }
            i++;
            int i2 = (i - 6) % 10;
            bArr2[i2] = (byte) read2;
            if (i2 == 9) {
                arrayList.add(new BluetoothPoint(bytesToString(bArr2, 0, 6), ByteBuffer.wrap(bArr2, 6, 4).getInt()));
            }
        }
    }

    /* access modifiers changed from: private */
    public static String bytesToString(byte[] bArr, int i, int i2) {
        return Base64.encodeToString(bArr, i, i2, 1);
    }

    private static String bytesToString(byte[] bArr) {
        return Base64.encodeToString(bArr, 1);
    }

    private static byte[] stringToBytes(String str) {
        return Base64.decode(str, 1);
    }
}
