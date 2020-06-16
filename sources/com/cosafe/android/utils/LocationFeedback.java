package com.cosafe.android.utils;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LocationFeedback {
    static final String FILE_NAME = "location_file.txt";
    private static final String FOLDER_NAME = "SafeTogetherLogger";
    public static final String TAG = LocationFeedback.class.getSimpleName();

    private static void createNewFile() {
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "SafeTogetherLogger");
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Root Folder created status:");
                sb.append(mkdirs);
                Log.e(str, sb.toString());
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(file);
            sb2.append(File.separator);
            sb2.append(FILE_NAME);
            boolean createNewFile = new File(sb2.toString()).createNewFile();
            String str2 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Log File Created status:");
            sb3.append(createNewFile);
            Log.e(str2, sb3.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File getFile() {
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        StringBuilder sb = new StringBuilder();
        sb.append("SafeTogetherLogger");
        sb.append(File.separator);
        sb.append(FILE_NAME);
        File file = new File(absolutePath, sb.toString());
        if (!file.exists()) {
            createNewFile();
        }
        return file;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0059 A[SYNTHETIC, Splitter:B:21:0x0059] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0064 A[SYNTHETIC, Splitter:B:26:0x0064] */
    /* JADX WARNING: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    public static void writeToFile(String str) {
        File file = getFile();
        if (file.exists()) {
            FileWriter fileWriter = null;
            try {
                FileWriter fileWriter2 = new FileWriter(file, true);
                try {
                    if (file.length() > 10485760) {
                        boolean delete = file.delete();
                        String str2 = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Log file delete status :");
                        sb.append(delete);
                        Log.e(str2, sb.toString());
                        createNewFile();
                    }
                    fileWriter2.append(str);
                    fileWriter2.append("\n");
                    fileWriter2.flush();
                    fileWriter2.close();
                    try {
                        fileWriter2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e2) {
                    e = e2;
                    fileWriter = fileWriter2;
                    try {
                        e.printStackTrace();
                        if (fileWriter == null) {
                        }
                    } catch (Throwable th) {
                        th = th;
                        if (fileWriter != null) {
                            try {
                                fileWriter.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileWriter = fileWriter2;
                    if (fileWriter != null) {
                    }
                    throw th;
                }
            } catch (IOException e4) {
                e = e4;
                e.printStackTrace();
                if (fileWriter == null) {
                    fileWriter.close();
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0051 A[SYNTHETIC, Splitter:B:28:0x0051] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x005e A[SYNTHETIC, Splitter:B:36:0x005e] */
    public static String readFromFile() {
        File file = getFile();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            if (file.isFile() && file.exists()) {
                BufferedReader bufferedReader2 = new BufferedReader(new FileReader(file));
                try {
                    for (String readLine = bufferedReader2.readLine(); readLine != null; readLine = bufferedReader2.readLine()) {
                        sb.append(readLine);
                        sb.append("\n");
                    }
                    bufferedReader = bufferedReader2;
                } catch (FileNotFoundException unused) {
                    bufferedReader = bufferedReader2;
                    String str = "File not found";
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return str;
                } catch (IOException unused2) {
                    bufferedReader = bufferedReader2;
                    String str2 = "Error reading log file";
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    return str2;
                } catch (Throwable th) {
                    BufferedReader bufferedReader3 = bufferedReader2;
                    if (bufferedReader3 != null) {
                        try {
                            bufferedReader3.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            return sb.toString();
        } catch (FileNotFoundException unused3) {
            String str3 = "File not found";
            if (bufferedReader != null) {
            }
            return str3;
        } catch (IOException unused4) {
            String str22 = "Error reading log file";
            if (bufferedReader != null) {
            }
            return str22;
        }
    }
}
