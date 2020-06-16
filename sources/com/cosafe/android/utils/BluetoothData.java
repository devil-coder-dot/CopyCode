package com.cosafe.android.utils;

import java.util.ArrayList;

/* compiled from: BluetoothManager */
class BluetoothData {
    String id;
    ArrayList<BluetoothPoint> records;

    BluetoothData(String str, ArrayList<BluetoothPoint> arrayList) {
        this.id = str;
        this.records = arrayList;
    }
}
