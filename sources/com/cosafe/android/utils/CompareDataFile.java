package com.cosafe.android.utils;

import android.os.Environment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class CompareDataFile {
    private static final String FILE_NAME_1 = "location_file.txt";
    private static final String FILE_NAME_2 = "location_file_down.txt";
    private static final String FILE_NAME_3 = "compare.txt";
    private static final String FOLDER_NAME = "AppLogger";

    public static double distance(double d, double d2, double d3, double d4) {
        double radians = Math.toRadians(d2 - d);
        double d5 = radians / 2.0d;
        double radians2 = Math.toRadians(d4 - d3) / 2.0d;
        double sin = (Math.sin(d5) * Math.sin(d5)) + (Math.cos(Math.toRadians(d)) * Math.cos(Math.toRadians(d2)) * Math.sin(radians2) * Math.sin(radians2));
        return Math.sqrt(Math.pow(Math.atan2(Math.sqrt(sin), Math.sqrt(1.0d - sin)) * 2.0d * 6371.0d * 1000.0d, 2.0d) + Math.pow(0.0d, 2.0d));
    }

    public static String main() throws IOException {
        char c;
        String str;
        String str2;
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), FOLDER_NAME);
        StringBuilder sb = new StringBuilder();
        sb.append(file);
        sb.append(File.separator);
        sb.append(FILE_NAME_1);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(file);
        sb3.append(File.separator);
        sb3.append(FILE_NAME_2);
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(file);
        sb5.append(File.separator);
        sb5.append(FILE_NAME_3);
        String sb6 = sb5.toString();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(sb2));
        BufferedReader bufferedReader2 = new BufferedReader(new FileReader(sb4));
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        String readLine = bufferedReader.readLine();
        while (true) {
            c = 2;
            str = ",";
            str2 = " ";
            if (readLine == null) {
                break;
            }
            String[] split = readLine.split(str);
            rowData rowdata = new rowData(Double.valueOf(split[0].split(str2)[0]).doubleValue(), Double.valueOf(split[1].split(str2)[1]).doubleValue(), Long.valueOf(split[2].split(str2)[1]).longValue());
            arrayList.add(rowdata);
            readLine = bufferedReader.readLine();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sb6));
        String readLine2 = bufferedReader2.readLine();
        boolean z = false;
        while (readLine2 != null) {
            String[] split2 = readLine2.split(str);
            String str3 = split2[0].split(str2)[0];
            String str4 = split2[1].split(str2)[1];
            String str5 = split2[c].split(str2)[1];
            double doubleValue = Double.valueOf(str3).doubleValue();
            double doubleValue2 = Double.valueOf(str4).doubleValue();
            long longValue = Long.valueOf(str5).longValue();
            rowData rowdata2 = new rowData(doubleValue, doubleValue2, longValue);
            arrayList2.add(rowdata2);
            readLine2 = bufferedReader2.readLine();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                rowData rowdata3 = (rowData) it.next();
                String str6 = str;
                if (Math.abs((int) ((double) (rowdata3.timestamp - longValue))) < 20000) {
                    double distance = distance(rowdata3.lat, doubleValue, rowdata3.lon, doubleValue2);
                    if (((int) distance) < 3) {
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("");
                        sb7.append(distance);
                        bufferedWriter.write(sb7.toString());
                        bufferedWriter.newLine();
                        z = true;
                    }
                }
                str = str6;
                c = 2;
            }
        }
        bufferedReader.close();
        bufferedReader2.close();
        bufferedWriter.close();
        return z ? "CODE YELLOW: You need to see a doctor." : "CODE GREEN: You don't need to see a doctor.";
    }
}
