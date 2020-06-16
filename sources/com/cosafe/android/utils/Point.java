package com.cosafe.android.utils;

/* compiled from: DownloadLocationDataManager */
class Point {
    double accuracy;
    double lat;
    double lng;
    long time;

    Point(double d, double d2, double d3, long j) {
        this.lat = d;
        this.lng = d2;
        this.accuracy = d3;
        this.time = j;
    }
}
