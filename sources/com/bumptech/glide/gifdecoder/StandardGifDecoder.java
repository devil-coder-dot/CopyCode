package com.bumptech.glide.gifdecoder;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.gifdecoder.GifDecoder.BitmapProvider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;
import kotlin.UByte;

public class StandardGifDecoder implements GifDecoder {
    private static final int BYTES_PER_INTEGER = 4;
    private static final int COLOR_TRANSPARENT_BLACK = 0;
    private static final int INITIAL_FRAME_POINTER = -1;
    private static final int MASK_INT_LOWEST_BYTE = 255;
    private static final int MAX_STACK_SIZE = 4096;
    private static final int NULL_CODE = -1;
    private static final String TAG = StandardGifDecoder.class.getSimpleName();
    private int[] act;
    private Config bitmapConfig;
    private final BitmapProvider bitmapProvider;
    private byte[] block;
    private int downsampledHeight;
    private int downsampledWidth;
    private int framePointer;
    private GifHeader header;
    private Boolean isFirstFrameTransparent;
    private byte[] mainPixels;
    private int[] mainScratch;
    private GifHeaderParser parser;
    private final int[] pct;
    private byte[] pixelStack;
    private short[] prefix;
    private Bitmap previousImage;
    private ByteBuffer rawData;
    private int sampleSize;
    private boolean savePrevious;
    private int status;
    private byte[] suffix;

    public StandardGifDecoder(BitmapProvider bitmapProvider2, GifHeader gifHeader, ByteBuffer byteBuffer) {
        this(bitmapProvider2, gifHeader, byteBuffer, 1);
    }

    public StandardGifDecoder(BitmapProvider bitmapProvider2, GifHeader gifHeader, ByteBuffer byteBuffer, int i) {
        this(bitmapProvider2);
        setData(gifHeader, byteBuffer, i);
    }

    public StandardGifDecoder(BitmapProvider bitmapProvider2) {
        this.pct = new int[256];
        this.bitmapConfig = Config.ARGB_8888;
        this.bitmapProvider = bitmapProvider2;
        this.header = new GifHeader();
    }

    public int getWidth() {
        return this.header.width;
    }

    public int getHeight() {
        return this.header.height;
    }

    public ByteBuffer getData() {
        return this.rawData;
    }

    public int getStatus() {
        return this.status;
    }

    public void advance() {
        this.framePointer = (this.framePointer + 1) % this.header.frameCount;
    }

    public int getDelay(int i) {
        if (i < 0 || i >= this.header.frameCount) {
            return -1;
        }
        return ((GifFrame) this.header.frames.get(i)).delay;
    }

    public int getNextDelay() {
        if (this.header.frameCount > 0) {
            int i = this.framePointer;
            if (i >= 0) {
                return getDelay(i);
            }
        }
        return 0;
    }

    public int getFrameCount() {
        return this.header.frameCount;
    }

    public int getCurrentFrameIndex() {
        return this.framePointer;
    }

    public void resetFrameIndex() {
        this.framePointer = -1;
    }

    @Deprecated
    public int getLoopCount() {
        if (this.header.loopCount == -1) {
            return 1;
        }
        return this.header.loopCount;
    }

    public int getNetscapeLoopCount() {
        return this.header.loopCount;
    }

    public int getTotalIterationCount() {
        if (this.header.loopCount == -1) {
            return 1;
        }
        if (this.header.loopCount == 0) {
            return 0;
        }
        return this.header.loopCount + 1;
    }

    public int getByteSize() {
        return this.rawData.limit() + this.mainPixels.length + (this.mainScratch.length * 4);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00f3, code lost:
        return null;
     */
    public synchronized Bitmap getNextFrame() {
        if (this.header.frameCount <= 0 || this.framePointer < 0) {
            if (Log.isLoggable(TAG, 3)) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to decode frame, frameCount=");
                sb.append(this.header.frameCount);
                sb.append(", framePointer=");
                sb.append(this.framePointer);
                Log.d(str, sb.toString());
            }
            this.status = 1;
        }
        if (this.status != 1) {
            if (this.status != 2) {
                this.status = 0;
                if (this.block == null) {
                    this.block = this.bitmapProvider.obtainByteArray(255);
                }
                GifFrame gifFrame = (GifFrame) this.header.frames.get(this.framePointer);
                int i = this.framePointer - 1;
                GifFrame gifFrame2 = i >= 0 ? (GifFrame) this.header.frames.get(i) : null;
                int[] iArr = gifFrame.lct != null ? gifFrame.lct : this.header.gct;
                this.act = iArr;
                if (iArr == null) {
                    if (Log.isLoggable(TAG, 3)) {
                        String str2 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("No valid color table found for frame #");
                        sb2.append(this.framePointer);
                        Log.d(str2, sb2.toString());
                    }
                    this.status = 1;
                    return null;
                }
                if (gifFrame.transparency) {
                    System.arraycopy(this.act, 0, this.pct, 0, this.act.length);
                    int[] iArr2 = this.pct;
                    this.act = iArr2;
                    iArr2[gifFrame.transIndex] = 0;
                    if (gifFrame.dispose == 2 && this.framePointer == 0) {
                        this.isFirstFrameTransparent = Boolean.valueOf(true);
                    }
                }
                return setPixels(gifFrame, gifFrame2);
            }
        }
        if (Log.isLoggable(TAG, 3)) {
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Unable to decode frame, status=");
            sb3.append(this.status);
            Log.d(str3, sb3.toString());
        }
    }

    public int read(InputStream inputStream, int i) {
        if (inputStream != null) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(i > 0 ? i + 4096 : 16384);
                byte[] bArr = new byte[16384];
                while (true) {
                    int read = inputStream.read(bArr, 0, 16384);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                byteArrayOutputStream.flush();
                read(byteArrayOutputStream.toByteArray());
            } catch (IOException e) {
                Log.w(TAG, "Error reading data from stream", e);
            }
        } else {
            this.status = 2;
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e2) {
                Log.w(TAG, "Error closing stream", e2);
            }
        }
        return this.status;
    }

    public void clear() {
        this.header = null;
        byte[] bArr = this.mainPixels;
        if (bArr != null) {
            this.bitmapProvider.release(bArr);
        }
        int[] iArr = this.mainScratch;
        if (iArr != null) {
            this.bitmapProvider.release(iArr);
        }
        Bitmap bitmap = this.previousImage;
        if (bitmap != null) {
            this.bitmapProvider.release(bitmap);
        }
        this.previousImage = null;
        this.rawData = null;
        this.isFirstFrameTransparent = null;
        byte[] bArr2 = this.block;
        if (bArr2 != null) {
            this.bitmapProvider.release(bArr2);
        }
    }

    public synchronized void setData(GifHeader gifHeader, byte[] bArr) {
        setData(gifHeader, ByteBuffer.wrap(bArr));
    }

    public synchronized void setData(GifHeader gifHeader, ByteBuffer byteBuffer) {
        setData(gifHeader, byteBuffer, 1);
    }

    public synchronized void setData(GifHeader gifHeader, ByteBuffer byteBuffer, int i) {
        if (i > 0) {
            int highestOneBit = Integer.highestOneBit(i);
            this.status = 0;
            this.header = gifHeader;
            this.framePointer = -1;
            ByteBuffer asReadOnlyBuffer = byteBuffer.asReadOnlyBuffer();
            this.rawData = asReadOnlyBuffer;
            asReadOnlyBuffer.position(0);
            this.rawData.order(ByteOrder.LITTLE_ENDIAN);
            this.savePrevious = false;
            Iterator it = gifHeader.frames.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (((GifFrame) it.next()).dispose == 3) {
                        this.savePrevious = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            this.sampleSize = highestOneBit;
            this.downsampledWidth = gifHeader.width / highestOneBit;
            this.downsampledHeight = gifHeader.height / highestOneBit;
            this.mainPixels = this.bitmapProvider.obtainByteArray(gifHeader.width * gifHeader.height);
            this.mainScratch = this.bitmapProvider.obtainIntArray(this.downsampledWidth * this.downsampledHeight);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Sample size must be >=0, not: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    private GifHeaderParser getHeaderParser() {
        if (this.parser == null) {
            this.parser = new GifHeaderParser();
        }
        return this.parser;
    }

    public synchronized int read(byte[] bArr) {
        GifHeader parseHeader = getHeaderParser().setData(bArr).parseHeader();
        this.header = parseHeader;
        if (bArr != null) {
            setData(parseHeader, bArr);
        }
        return this.status;
    }

    public void setDefaultBitmapConfig(Config config) {
        if (config == Config.ARGB_8888 || config == Config.RGB_565) {
            this.bitmapConfig = config;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unsupported format: ");
        sb.append(config);
        sb.append(", must be one of ");
        sb.append(Config.ARGB_8888);
        sb.append(" or ");
        sb.append(Config.RGB_565);
        throw new IllegalArgumentException(sb.toString());
    }

    private Bitmap setPixels(GifFrame gifFrame, GifFrame gifFrame2) {
        int[] iArr = this.mainScratch;
        int i = 0;
        if (gifFrame2 == null) {
            Bitmap bitmap = this.previousImage;
            if (bitmap != null) {
                this.bitmapProvider.release(bitmap);
            }
            this.previousImage = null;
            Arrays.fill(iArr, 0);
        }
        if (gifFrame2 != null && gifFrame2.dispose == 3 && this.previousImage == null) {
            Arrays.fill(iArr, 0);
        }
        if (gifFrame2 != null && gifFrame2.dispose > 0) {
            if (gifFrame2.dispose == 2) {
                if (!gifFrame.transparency) {
                    int i2 = this.header.bgColor;
                    if (gifFrame.lct == null || this.header.bgIndex != gifFrame.transIndex) {
                        i = i2;
                    }
                }
                int i3 = gifFrame2.ih / this.sampleSize;
                int i4 = gifFrame2.iy / this.sampleSize;
                int i5 = gifFrame2.iw / this.sampleSize;
                int i6 = gifFrame2.ix / this.sampleSize;
                int i7 = this.downsampledWidth;
                int i8 = (i4 * i7) + i6;
                int i9 = (i3 * i7) + i8;
                while (i8 < i9) {
                    int i10 = i8 + i5;
                    for (int i11 = i8; i11 < i10; i11++) {
                        iArr[i11] = i;
                    }
                    i8 += this.downsampledWidth;
                }
            } else if (gifFrame2.dispose == 3) {
                Bitmap bitmap2 = this.previousImage;
                if (bitmap2 != null) {
                    int i12 = this.downsampledWidth;
                    bitmap2.getPixels(iArr, 0, i12, 0, 0, i12, this.downsampledHeight);
                }
            }
        }
        decodeBitmapData(gifFrame);
        if (gifFrame.interlace || this.sampleSize != 1) {
            copyCopyIntoScratchRobust(gifFrame);
        } else {
            copyIntoScratchFast(gifFrame);
        }
        if (this.savePrevious && (gifFrame.dispose == 0 || gifFrame.dispose == 1)) {
            if (this.previousImage == null) {
                this.previousImage = getNextBitmap();
            }
            Bitmap bitmap3 = this.previousImage;
            int i13 = this.downsampledWidth;
            bitmap3.setPixels(iArr, 0, i13, 0, 0, i13, this.downsampledHeight);
        }
        Bitmap nextBitmap = getNextBitmap();
        int i14 = this.downsampledWidth;
        nextBitmap.setPixels(iArr, 0, i14, 0, 0, i14, this.downsampledHeight);
        return nextBitmap;
    }

    private void copyIntoScratchFast(GifFrame gifFrame) {
        GifFrame gifFrame2 = gifFrame;
        int[] iArr = this.mainScratch;
        int i = gifFrame2.ih;
        int i2 = gifFrame2.iy;
        int i3 = gifFrame2.iw;
        int i4 = gifFrame2.ix;
        boolean z = this.framePointer == 0;
        int i5 = this.downsampledWidth;
        byte[] bArr = this.mainPixels;
        int[] iArr2 = this.act;
        int i6 = 0;
        byte b = -1;
        while (i6 < i) {
            int i7 = (i6 + i2) * i5;
            int i8 = i7 + i4;
            int i9 = i8 + i3;
            int i10 = i7 + i5;
            if (i10 < i9) {
                i9 = i10;
            }
            int i11 = gifFrame2.iw * i6;
            int i12 = i8;
            while (i12 < i9) {
                byte b2 = bArr[i11];
                int i13 = i;
                byte b3 = b2 & UByte.MAX_VALUE;
                if (b3 != b) {
                    int i14 = iArr2[b3];
                    if (i14 != 0) {
                        iArr[i12] = i14;
                    } else {
                        b = b2;
                    }
                }
                i11++;
                i12++;
                GifFrame gifFrame3 = gifFrame;
                i = i13;
            }
            int i15 = i;
            i6++;
            gifFrame2 = gifFrame;
        }
        Boolean bool = this.isFirstFrameTransparent;
        this.isFirstFrameTransparent = Boolean.valueOf((bool != null && bool.booleanValue()) || (this.isFirstFrameTransparent == null && z && b != -1));
    }

    private void copyCopyIntoScratchRobust(GifFrame gifFrame) {
        boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        GifFrame gifFrame2 = gifFrame;
        int[] iArr = this.mainScratch;
        int i6 = gifFrame2.ih / this.sampleSize;
        int i7 = gifFrame2.iy / this.sampleSize;
        int i8 = gifFrame2.iw / this.sampleSize;
        int i9 = gifFrame2.ix / this.sampleSize;
        int i10 = this.framePointer;
        Boolean valueOf = Boolean.valueOf(true);
        boolean z2 = i10 == 0;
        int i11 = this.sampleSize;
        int i12 = this.downsampledWidth;
        int i13 = this.downsampledHeight;
        byte[] bArr = this.mainPixels;
        int[] iArr2 = this.act;
        Boolean bool = this.isFirstFrameTransparent;
        int i14 = 8;
        Boolean bool2 = valueOf;
        int i15 = 0;
        int i16 = 0;
        int i17 = 1;
        while (i15 < i6) {
            Boolean bool3 = bool;
            if (gifFrame2.interlace) {
                if (i16 >= i6) {
                    i = i6;
                    int i18 = i17 + 1;
                    if (i18 == 2) {
                        i17 = i18;
                        i16 = 4;
                    } else if (i18 == 3) {
                        i17 = i18;
                        i16 = 2;
                        i14 = 4;
                    } else if (i18 != 4) {
                        i17 = i18;
                    } else {
                        i17 = i18;
                        i16 = 1;
                        i14 = 2;
                    }
                } else {
                    i = i6;
                }
                i2 = i16 + i14;
            } else {
                i = i6;
                i2 = i16;
                i16 = i15;
            }
            int i19 = i16 + i7;
            boolean z3 = i11 == 1;
            if (i19 < i13) {
                int i20 = i19 * i12;
                int i21 = i20 + i9;
                int i22 = i21 + i8;
                int i23 = i20 + i12;
                if (i23 < i22) {
                    i22 = i23;
                }
                i3 = i2;
                int i24 = i15 * i11 * gifFrame2.iw;
                if (z3) {
                    int i25 = i21;
                    while (i25 < i22) {
                        int i26 = i7;
                        int i27 = iArr2[bArr[i24] & UByte.MAX_VALUE];
                        if (i27 != 0) {
                            iArr[i25] = i27;
                        } else if (z2 && bool3 == null) {
                            bool3 = bool2;
                        }
                        i24 += i11;
                        i25++;
                        i7 = i26;
                    }
                } else {
                    i5 = i7;
                    int i28 = ((i22 - i21) * i11) + i24;
                    int i29 = i21;
                    while (true) {
                        i4 = i8;
                        if (i29 >= i22) {
                            break;
                        }
                        int averageColorsNear = averageColorsNear(i24, i28, gifFrame2.iw);
                        if (averageColorsNear != 0) {
                            iArr[i29] = averageColorsNear;
                        } else if (z2 && bool3 == null) {
                            bool3 = bool2;
                        }
                        i24 += i11;
                        i29++;
                        i8 = i4;
                    }
                    bool = bool3;
                    i15++;
                    i7 = i5;
                    i8 = i4;
                    i6 = i;
                    i16 = i3;
                }
            } else {
                i3 = i2;
            }
            i5 = i7;
            i4 = i8;
            bool = bool3;
            i15++;
            i7 = i5;
            i8 = i4;
            i6 = i;
            i16 = i3;
        }
        Boolean bool4 = bool;
        if (this.isFirstFrameTransparent == null) {
            if (bool4 == null) {
                z = false;
            } else {
                z = bool4.booleanValue();
            }
            this.isFirstFrameTransparent = Boolean.valueOf(z);
        }
    }

    private int averageColorsNear(int i, int i2, int i3) {
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        for (int i9 = i; i9 < this.sampleSize + i; i9++) {
            byte[] bArr = this.mainPixels;
            if (i9 >= bArr.length || i9 >= i2) {
                break;
            }
            int i10 = this.act[bArr[i9] & UByte.MAX_VALUE];
            if (i10 != 0) {
                i4 += (i10 >> 24) & 255;
                i5 += (i10 >> 16) & 255;
                i6 += (i10 >> 8) & 255;
                i7 += i10 & 255;
                i8++;
            }
        }
        int i11 = i + i3;
        for (int i12 = i11; i12 < this.sampleSize + i11; i12++) {
            byte[] bArr2 = this.mainPixels;
            if (i12 >= bArr2.length || i12 >= i2) {
                break;
            }
            int i13 = this.act[bArr2[i12] & UByte.MAX_VALUE];
            if (i13 != 0) {
                i4 += (i13 >> 24) & 255;
                i5 += (i13 >> 16) & 255;
                i6 += (i13 >> 8) & 255;
                i7 += i13 & 255;
                i8++;
            }
        }
        if (i8 == 0) {
            return 0;
        }
        return ((i4 / i8) << 24) | ((i5 / i8) << 16) | ((i6 / i8) << 8) | (i7 / i8);
    }

    /* JADX WARNING: type inference failed for: r3v1, types: [short[]] */
    /* JADX WARNING: type inference failed for: r25v0 */
    /* JADX WARNING: type inference failed for: r25v1 */
    /* JADX WARNING: type inference failed for: r7v3 */
    /* JADX WARNING: type inference failed for: r7v4, types: [int] */
    /* JADX WARNING: type inference failed for: r25v3 */
    /* JADX WARNING: type inference failed for: r25v4 */
    /* JADX WARNING: type inference failed for: r18v4 */
    /* JADX WARNING: type inference failed for: r7v7 */
    /* JADX WARNING: type inference failed for: r25v5 */
    /* JADX WARNING: type inference failed for: r7v13, types: [short] */
    /* JADX WARNING: type inference failed for: r25v7 */
    /* JADX WARNING: type inference failed for: r7v18 */
    /* JADX WARNING: type inference failed for: r25v8 */
    /* JADX WARNING: type inference failed for: r18v6 */
    /* JADX WARNING: type inference failed for: r7v19 */
    /* JADX WARNING: type inference failed for: r7v20 */
    /* JADX WARNING: Incorrect type for immutable var: ssa=short, code=int, for r7v13, types: [short] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=short[], code=null, for r3v1, types: [short[]] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r25v4
  assigns: []
  uses: []
  mth insns count: 167
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 8 */
    private void decodeBitmapData(GifFrame gifFrame) {
        int i;
        ? r25;
        int i2;
        ? r252;
        ? r7;
        ? r72;
        int i3;
        StandardGifDecoder standardGifDecoder = this;
        GifFrame gifFrame2 = gifFrame;
        if (gifFrame2 != null) {
            standardGifDecoder.rawData.position(gifFrame2.bufferFrameStart);
        }
        if (gifFrame2 == null) {
            i = standardGifDecoder.header.width * standardGifDecoder.header.height;
        } else {
            i = gifFrame2.ih * gifFrame2.iw;
        }
        byte[] bArr = standardGifDecoder.mainPixels;
        if (bArr == null || bArr.length < i) {
            standardGifDecoder.mainPixels = standardGifDecoder.bitmapProvider.obtainByteArray(i);
        }
        byte[] bArr2 = standardGifDecoder.mainPixels;
        if (standardGifDecoder.prefix == null) {
            standardGifDecoder.prefix = new short[4096];
        }
        ? r3 = standardGifDecoder.prefix;
        if (standardGifDecoder.suffix == null) {
            standardGifDecoder.suffix = new byte[4096];
        }
        byte[] bArr3 = standardGifDecoder.suffix;
        if (standardGifDecoder.pixelStack == null) {
            standardGifDecoder.pixelStack = new byte[FragmentTransaction.TRANSIT_FRAGMENT_OPEN];
        }
        byte[] bArr4 = standardGifDecoder.pixelStack;
        int readByte = readByte();
        int i4 = 1 << readByte;
        int i5 = i4 + 1;
        int i6 = i4 + 2;
        int i7 = readByte + 1;
        int i8 = (1 << i7) - 1;
        int i9 = 0;
        for (int i10 = 0; i10 < i4; i10++) {
            r3[i10] = 0;
            bArr3[i10] = (byte) i10;
        }
        byte[] bArr5 = standardGifDecoder.block;
        int i11 = i7;
        int i12 = i6;
        int i13 = i8;
        int i14 = 0;
        int i15 = 0;
        ? r18 = 0;
        int i16 = 0;
        int i17 = 0;
        int i18 = -1;
        int i19 = 0;
        ? r253 = 0;
        while (true) {
            if (i9 >= i) {
                break;
            }
            if (i14 == 0) {
                i14 = readBlock();
                if (i14 <= 0) {
                    standardGifDecoder.status = 3;
                    break;
                }
                i15 = 0;
            }
            i16 += (bArr5[i15] & UByte.MAX_VALUE) << r18;
            i15++;
            i14--;
            int i20 = r18 + 8;
            int i21 = i12;
            int i22 = i11;
            int i23 = i18;
            int i24 = i7;
            ? r73 = r253;
            while (true) {
                if (i20 < i22) {
                    byte[] bArr6 = bArr4;
                    i18 = i23;
                    i12 = i21;
                    i2 = i20;
                    standardGifDecoder = this;
                    r25 = r73;
                    i7 = i24;
                    i11 = i22;
                    break;
                }
                int i25 = i6;
                byte b = i16 & i13;
                i16 >>= i22;
                i20 -= i22;
                if (b == i4) {
                    i13 = i8;
                    i22 = i24;
                    i21 = i25;
                    i6 = i21;
                    i23 = -1;
                    r7 = r73;
                } else if (b == i5) {
                    i2 = i20;
                    r25 = r73;
                    i12 = i21;
                    i7 = i24;
                    i6 = i25;
                    i18 = i23;
                    i11 = i22;
                    standardGifDecoder = this;
                    break;
                } else {
                    int i26 = i20;
                    if (i23 == -1) {
                        bArr2[i17] = bArr3[b];
                        i17++;
                        i9++;
                        i23 = b;
                        r7 = i23;
                        i6 = i25;
                        i20 = i26;
                    } else {
                        if (b >= i21) {
                            bArr4[i19] = (byte) r73;
                            i19++;
                            i3 = i23;
                        } else {
                            i3 = b;
                        }
                        while (r72 >= i4) {
                            bArr4[i19] = bArr3[r72];
                            i19++;
                            r72 = r3[r72];
                        }
                        r7 = bArr3[r72] & UByte.MAX_VALUE;
                        byte b2 = (byte) r7;
                        bArr2[i17] = b2;
                        while (true) {
                            i17++;
                            i9++;
                            if (i19 <= 0) {
                                break;
                            }
                            i19--;
                            bArr2[i17] = bArr4[i19];
                        }
                        byte[] bArr7 = bArr4;
                        if (i21 < 4096) {
                            r3[i21] = (short) i23;
                            bArr3[i21] = b2;
                            i21++;
                            if ((i21 & i13) == 0 && i21 < 4096) {
                                i22++;
                                i13 += i21;
                            }
                        }
                        i23 = b;
                        i6 = i25;
                        i20 = i26;
                        bArr4 = bArr7;
                    }
                }
                r73 = r7;
            }
            r253 = r252;
            r18 = i2;
        }
        Arrays.fill(bArr2, i17, i, 0);
    }

    private int readByte() {
        return this.rawData.get() & UByte.MAX_VALUE;
    }

    private int readBlock() {
        int readByte = readByte();
        if (readByte <= 0) {
            return readByte;
        }
        ByteBuffer byteBuffer = this.rawData;
        byteBuffer.get(this.block, 0, Math.min(readByte, byteBuffer.remaining()));
        return readByte;
    }

    private Bitmap getNextBitmap() {
        Boolean bool = this.isFirstFrameTransparent;
        Bitmap obtain = this.bitmapProvider.obtain(this.downsampledWidth, this.downsampledHeight, (bool == null || bool.booleanValue()) ? Config.ARGB_8888 : this.bitmapConfig);
        obtain.setHasAlpha(true);
        return obtain;
    }
}
