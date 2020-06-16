package androidx.core.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.Typeface.CustomFallbackBuilder;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily.Builder;
import android.graphics.fonts.FontStyle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import androidx.core.content.res.FontResourcesParserCompat.FontFamilyFilesResourceEntry;
import androidx.core.content.res.FontResourcesParserCompat.FontFileResourceEntry;
import androidx.core.provider.FontsContractCompat.FontInfo;
import com.bumptech.glide.load.resource.bitmap.HardwareConfigState;
import java.io.IOException;
import java.io.InputStream;

public class TypefaceCompatApi29Impl extends TypefaceCompatBaseImpl {
    /* access modifiers changed from: protected */
    public FontInfo findBestInfo(FontInfo[] fontInfoArr, int i) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    /* access modifiers changed from: protected */
    public Typeface createFromInputStream(Context context, InputStream inputStream) {
        throw new RuntimeException("Do not use this function in API 29 or later.");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0056, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0057, code lost:
        if (r7 != null) goto L_0x0059;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        r7.close();
     */
    public Typeface createFromFontInfo(Context context, CancellationSignal cancellationSignal, FontInfo[] fontInfoArr, int i) {
        ContentResolver contentResolver = context.getContentResolver();
        int length = fontInfoArr.length;
        int i2 = 0;
        Builder builder = null;
        int i3 = 0;
        while (true) {
            int i4 = 1;
            if (i3 < length) {
                FontInfo fontInfo = fontInfoArr[i3];
                try {
                    ParcelFileDescriptor openFileDescriptor = contentResolver.openFileDescriptor(fontInfo.getUri(), "r", cancellationSignal);
                    if (openFileDescriptor != null) {
                        Font.Builder weight = new Font.Builder(openFileDescriptor).setWeight(fontInfo.getWeight());
                        if (!fontInfo.isItalic()) {
                            i4 = 0;
                        }
                        Font build = weight.setSlant(i4).setTtcIndex(fontInfo.getTtcIndex()).build();
                        if (builder == null) {
                            builder = new Builder(build);
                        } else {
                            builder.addFont(build);
                        }
                        if (openFileDescriptor == null) {
                            i3++;
                        }
                    } else if (openFileDescriptor == null) {
                        i3++;
                    }
                    openFileDescriptor.close();
                } catch (IOException unused) {
                } catch (Throwable th) {
                    r5.addSuppressed(th);
                    break;
                }
                i3++;
            } else if (builder == null) {
                return null;
            } else {
                int i5 = (i & 1) != 0 ? HardwareConfigState.DEFAULT_MAXIMUM_FDS_FOR_HARDWARE_CONFIGS : 400;
                if ((i & 2) != 0) {
                    i2 = 1;
                }
                return new CustomFallbackBuilder(builder.build()).setStyle(new FontStyle(i5, i2)).build();
            }
        }
        throw th;
    }

    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, Resources resources, int i) {
        FontFileResourceEntry[] entries = fontFamilyFilesResourceEntry.getEntries();
        int length = entries.length;
        int i2 = 0;
        Builder builder = null;
        int i3 = 0;
        while (true) {
            int i4 = 1;
            if (i3 >= length) {
                break;
            }
            FontFileResourceEntry fontFileResourceEntry = entries[i3];
            try {
                Font.Builder weight = new Font.Builder(resources, fontFileResourceEntry.getResourceId()).setWeight(fontFileResourceEntry.getWeight());
                if (!fontFileResourceEntry.isItalic()) {
                    i4 = 0;
                }
                Font build = weight.setSlant(i4).setTtcIndex(fontFileResourceEntry.getTtcIndex()).setFontVariationSettings(fontFileResourceEntry.getVariationSettings()).build();
                if (builder == null) {
                    builder = new Builder(build);
                } else {
                    builder.addFont(build);
                }
            } catch (IOException unused) {
            }
            i3++;
        }
        if (builder == null) {
            return null;
        }
        int i5 = (i & 1) != 0 ? HardwareConfigState.DEFAULT_MAXIMUM_FDS_FOR_HARDWARE_CONFIGS : 400;
        if ((i & 2) != 0) {
            i2 = 1;
        }
        return new CustomFallbackBuilder(builder.build()).setStyle(new FontStyle(i5, i2)).build();
    }

    public Typeface createFromResourcesFontFile(Context context, Resources resources, int i, String str, int i2) {
        try {
            return new CustomFallbackBuilder(new Builder(new Font.Builder(resources, i).build()).build()).setStyle(new FontStyle((i2 & 1) != 0 ? HardwareConfigState.DEFAULT_MAXIMUM_FDS_FOR_HARDWARE_CONFIGS : 400, (i2 & 2) != 0 ? 1 : 0)).build();
        } catch (IOException unused) {
            return null;
        }
    }
}
