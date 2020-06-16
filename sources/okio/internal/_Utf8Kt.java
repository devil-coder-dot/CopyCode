package okio.internal;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import okio.Utf8;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0012\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0003\u001a\u00020\u0002*\u00020\u0001¨\u0006\u0004"}, d2 = {"commonAsUtf8ToByteArray", "", "", "commonToUtf8String", "jvm"}, k = 2, mv = {1, 1, 11})
/* compiled from: -Utf8.kt */
public final class _Utf8Kt {
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x008d, code lost:
        if (((r0[r5] & 192) == 128) == false) goto L_0x0042;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0105, code lost:
        if (((r0[r5] & 192) == 128) == false) goto L_0x010b;
     */
    public static final String commonToUtf8String(byte[] bArr) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        byte[] bArr2 = bArr;
        Intrinsics.checkParameterIsNotNull(bArr2, "$receiver");
        char[] cArr = new char[bArr2.length];
        int length = bArr2.length;
        int i9 = 0;
        int i10 = 0;
        while (i9 < length) {
            byte b = bArr2[i9];
            if (b >= 0) {
                i8 = i10 + 1;
                cArr[i10] = (char) b;
                i9++;
                while (i9 < length && bArr2[i9] >= 0) {
                    int i11 = i9 + 1;
                    int i12 = i8 + 1;
                    cArr[i8] = (char) bArr2[i9];
                    i9 = i11;
                    i8 = i12;
                }
            } else {
                if ((b >> 5) == -2) {
                    int i13 = i9 + 1;
                    if (length <= i13) {
                        i5 = i10 + 1;
                        cArr[i10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                    } else {
                        byte b2 = bArr2[i9];
                        byte b3 = bArr2[i13];
                        if (!((b3 & 192) == 128)) {
                            i5 = i10 + 1;
                            cArr[i10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                        } else {
                            byte b4 = (b3 ^ ByteCompanionObject.MIN_VALUE) ^ (b2 << 6);
                            if (b4 < 128) {
                                i5 = i10 + 1;
                                cArr[i10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                            } else {
                                i5 = i10 + 1;
                                cArr[i10] = (char) b4;
                            }
                            i6 = 2;
                            i9 += i6;
                        }
                    }
                } else if ((b >> 4) == -2) {
                    int i14 = i9 + 2;
                    if (length <= i14) {
                        i5 = i10 + 1;
                        cArr[i10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                        int i15 = i9 + 1;
                        if (length > i15) {
                        }
                    } else {
                        byte b5 = bArr2[i9];
                        byte b6 = bArr2[i9 + 1];
                        if (!((b6 & 192) == 128)) {
                            i5 = i10 + 1;
                            cArr[i10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                        } else {
                            byte b7 = bArr2[i14];
                            if (!((b7 & 192) == 128)) {
                                i5 = i10 + 1;
                                cArr[i10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                                i6 = 2;
                                i9 += i6;
                            } else {
                                byte b8 = ((b7 ^ ByteCompanionObject.MIN_VALUE) ^ (b6 << 6)) ^ (b5 << 12);
                                if (b8 < 2048) {
                                    i7 = i10 + 1;
                                    cArr[i10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                                } else if (55296 <= b8 && 57343 >= b8) {
                                    i7 = i10 + 1;
                                    cArr[i10] = (char) Utf8.REPLACEMENT_CODE_POINT;
                                } else {
                                    i7 = i10 + 1;
                                    cArr[i10] = (char) b8;
                                }
                                i6 = 3;
                                i9 += i6;
                            }
                        }
                    }
                } else {
                    if ((b >> 3) == -2) {
                        int i16 = i9 + 3;
                        if (length <= i16) {
                            i = i10 + 1;
                            cArr[i10] = Utf8.REPLACEMENT_CHARACTER;
                            int i17 = i9 + 1;
                            if (length > i17) {
                                if ((bArr2[i17] & 192) == 128) {
                                    int i18 = i9 + 2;
                                    if (length > i18) {
                                    }
                                    i3 = 2;
                                    i2 = i9 + i3;
                                }
                            }
                            i3 = 1;
                            i2 = i9 + i3;
                        } else {
                            byte b9 = bArr2[i9];
                            byte b10 = bArr2[i9 + 1];
                            if (!((b10 & 192) == 128)) {
                                i = i10 + 1;
                                cArr[i10] = Utf8.REPLACEMENT_CHARACTER;
                                i3 = 1;
                                i2 = i9 + i3;
                            } else {
                                byte b11 = bArr2[i9 + 2];
                                if (!((b11 & 192) == 128)) {
                                    i = i10 + 1;
                                    cArr[i10] = Utf8.REPLACEMENT_CHARACTER;
                                    i3 = 2;
                                    i2 = i9 + i3;
                                } else {
                                    byte b12 = bArr2[i16];
                                    if (!((b12 & 192) == 128)) {
                                        i = i10 + 1;
                                        cArr[i10] = Utf8.REPLACEMENT_CHARACTER;
                                    } else {
                                        byte b13 = (((b12 ^ ByteCompanionObject.MIN_VALUE) ^ (b11 << 6)) ^ (b10 << 12)) ^ (b9 << 18);
                                        if (b13 > 1114111) {
                                            i4 = i10 + 1;
                                            cArr[i10] = Utf8.REPLACEMENT_CHARACTER;
                                        } else if (55296 <= b13 && 57343 >= b13) {
                                            i4 = i10 + 1;
                                            cArr[i10] = Utf8.REPLACEMENT_CHARACTER;
                                        } else if (b13 < 65536) {
                                            i4 = i10 + 1;
                                            cArr[i10] = Utf8.REPLACEMENT_CHARACTER;
                                        } else if (b13 != 65533) {
                                            int i19 = i10 + 1;
                                            cArr[i10] = (char) ((b13 >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                            i4 = i19 + 1;
                                            cArr[i19] = (char) ((b13 & UByte.MAX_VALUE) + Utf8.LOG_SURROGATE_HEADER);
                                        } else {
                                            i4 = i10 + 1;
                                            cArr[i10] = Utf8.REPLACEMENT_CHARACTER;
                                        }
                                        i3 = 4;
                                        i2 = i9 + i3;
                                    }
                                }
                            }
                        }
                        i3 = 3;
                        i2 = i9 + i3;
                    } else {
                        i = i10 + 1;
                        cArr[i10] = Utf8.REPLACEMENT_CHARACTER;
                        i2 = i9 + 1;
                    }
                    i10 = i;
                }
                i6 = 1;
                i9 += i6;
            }
            i10 = i8;
        }
        return new String(cArr, 0, i10);
    }

    public static final byte[] commonAsUtf8ToByteArray(String str) {
        int i;
        int i2;
        int i3;
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        byte[] bArr = new byte[(str.length() * 4)];
        int length = str.length();
        int i4 = 0;
        while (true) {
            String str2 = "java.util.Arrays.copyOf(this, newSize)";
            if (i < length) {
                char charAt = str.charAt(i);
                if (charAt >= 128) {
                    int length2 = str.length();
                    int i5 = i;
                    while (i < length2) {
                        char charAt2 = str.charAt(i);
                        if (charAt2 < 128) {
                            int i6 = i5 + 1;
                            bArr[i5] = (byte) charAt2;
                            i++;
                            while (i < length2 && str.charAt(i) < 128) {
                                int i7 = i + 1;
                                int i8 = i6 + 1;
                                bArr[i6] = (byte) str.charAt(i);
                                i = i7;
                                i6 = i8;
                            }
                            i5 = i6;
                        } else {
                            if (charAt2 < 2048) {
                                int i9 = i5 + 1;
                                bArr[i5] = (byte) ((charAt2 >> 6) | 192);
                                i2 = i9 + 1;
                                bArr[i9] = (byte) ((charAt2 & '?') | 128);
                            } else if (55296 > charAt2 || 57343 < charAt2) {
                                int i10 = i5 + 1;
                                bArr[i5] = (byte) ((charAt2 >> 12) | 224);
                                int i11 = i10 + 1;
                                bArr[i10] = (byte) (((charAt2 >> 6) & 63) | 128);
                                i2 = i11 + 1;
                                bArr[i11] = (byte) ((charAt2 & '?') | 128);
                            } else {
                                if (charAt2 <= 56319) {
                                    int i12 = i + 1;
                                    if (length2 > i12) {
                                        char charAt3 = str.charAt(i12);
                                        if (56320 <= charAt3 && 57343 >= charAt3) {
                                            int charAt4 = ((charAt2 << 10) + str.charAt(i12)) - 56613888;
                                            int i13 = i5 + 1;
                                            bArr[i5] = (byte) ((charAt4 >> 18) | 240);
                                            int i14 = i13 + 1;
                                            bArr[i13] = (byte) (((charAt4 >> 12) & 63) | 128);
                                            int i15 = i14 + 1;
                                            bArr[i14] = (byte) (((charAt4 >> 6) & 63) | 128);
                                            i2 = i15 + 1;
                                            bArr[i15] = (byte) ((charAt4 & 63) | 128);
                                            i3 = i + 2;
                                            i5 = i2;
                                        }
                                    }
                                }
                                i2 = i5 + 1;
                                bArr[i5] = Utf8.REPLACEMENT_BYTE;
                            }
                            i3 = i + 1;
                            i5 = i2;
                        }
                    }
                    byte[] copyOf = Arrays.copyOf(bArr, i5);
                    Intrinsics.checkExpressionValueIsNotNull(copyOf, str2);
                    return copyOf;
                }
                bArr[i] = (byte) charAt;
                i4 = i + 1;
            } else {
                byte[] copyOf2 = Arrays.copyOf(bArr, str.length());
                Intrinsics.checkExpressionValueIsNotNull(copyOf2, str2);
                return copyOf2;
            }
        }
    }
}
