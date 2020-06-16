package okio;

import kotlin.Metadata;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000D\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\u001a\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0001H\b\u001a\u0011\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0007H\b\u001a1\u0010\u0010\u001a\u00020\u0001*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\b\u001a1\u0010\u0017\u001a\u00020\u0001*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\b\u001a1\u0010\u0018\u001a\u00020\u0001*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\b\u001a1\u0010\u0019\u001a\u00020\u0016*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00160\u0015H\b\u001a1\u0010\u001a\u001a\u00020\u0016*\u00020\u001b2\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00160\u0015H\b\u001a1\u0010\u001c\u001a\u00020\u0016*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\b\u001a%\u0010\u001d\u001a\u00020\u001e*\u00020\u001b2\b\b\u0002\u0010\u0012\u001a\u00020\u00012\b\b\u0002\u0010\u0013\u001a\u00020\u0001H\u0007¢\u0006\u0002\b\u001f\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\tXT¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"HIGH_SURROGATE_HEADER", "", "LOG_SURROGATE_HEADER", "MASK_2BYTES", "MASK_3BYTES", "MASK_4BYTES", "REPLACEMENT_BYTE", "", "REPLACEMENT_CHARACTER", "", "REPLACEMENT_CODE_POINT", "isIsoControl", "", "codePoint", "isUtf8Continuation", "byte", "process2Utf8Bytes", "", "beginIndex", "endIndex", "yield", "Lkotlin/Function1;", "", "process3Utf8Bytes", "process4Utf8Bytes", "processUtf16Chars", "processUtf8Bytes", "", "processUtf8CodePoints", "utf8Size", "", "size", "jvm"}, k = 2, mv = {1, 1, 11})
/* compiled from: Utf8.kt */
public final class Utf8 {
    public static final int HIGH_SURROGATE_HEADER = 55232;
    public static final int LOG_SURROGATE_HEADER = 56320;
    public static final int MASK_2BYTES = 3968;
    public static final int MASK_3BYTES = -123008;
    public static final int MASK_4BYTES = 3678080;
    public static final byte REPLACEMENT_BYTE = 63;
    public static final char REPLACEMENT_CHARACTER = '�';
    public static final int REPLACEMENT_CODE_POINT = 65533;

    public static final boolean isIsoControl(int i) {
        return (i >= 0 && 31 >= i) || (127 <= i && 159 >= i);
    }

    public static final boolean isUtf8Continuation(byte b) {
        return (b & 192) == 128;
    }

    public static final long size(String str) {
        return size$default(str, 0, 0, 3, null);
    }

    public static final long size(String str, int i) {
        return size$default(str, i, 0, 2, null);
    }

    public static /* bridge */ /* synthetic */ long size$default(String str, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        return size(str, i, i2);
    }

    public static final long size(String str, int i, int i2) {
        int i3;
        char c;
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        boolean z = true;
        if (i >= 0) {
            if (i2 >= i) {
                if (i2 > str.length()) {
                    z = false;
                }
                if (z) {
                    long j = 0;
                    while (i < i2) {
                        char charAt = str.charAt(i);
                        if (charAt < 128) {
                            j++;
                        } else {
                            if (charAt < 2048) {
                                i3 = 2;
                            } else if (charAt < 55296 || charAt > 57343) {
                                i3 = 3;
                            } else {
                                int i4 = i + 1;
                                if (i4 < i2) {
                                    c = str.charAt(i4);
                                } else {
                                    c = 0;
                                }
                                if (charAt > 56319 || c < 56320 || c > 57343) {
                                    j++;
                                    i = i4;
                                } else {
                                    j += (long) 4;
                                    i += 2;
                                }
                            }
                            j += (long) i3;
                        }
                        i++;
                    }
                    return j;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("endIndex > string.length: ");
                sb.append(i2);
                sb.append(" > ");
                sb.append(str.length());
                throw new IllegalArgumentException(sb.toString().toString());
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("endIndex < beginIndex: ");
            sb2.append(i2);
            sb2.append(" < ");
            sb2.append(i);
            throw new IllegalArgumentException(sb2.toString().toString());
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("beginIndex < 0: ");
        sb3.append(i);
        throw new IllegalArgumentException(sb3.toString().toString());
    }

    public static final void processUtf8Bytes(String str, int i, int i2, Function1<? super Byte, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "yield");
        while (i < i2) {
            char charAt = str.charAt(i);
            if (charAt < 128) {
                function1.invoke(Byte.valueOf((byte) charAt));
                i++;
                while (i < i2 && str.charAt(i) < 128) {
                    int i3 = i + 1;
                    function1.invoke(Byte.valueOf((byte) str.charAt(i)));
                    i = i3;
                }
            } else {
                if (charAt < 2048) {
                    function1.invoke(Byte.valueOf((byte) ((charAt >> 6) | 192)));
                    function1.invoke(Byte.valueOf((byte) ((charAt & '?') | 128)));
                } else if (55296 > charAt || 57343 < charAt) {
                    function1.invoke(Byte.valueOf((byte) ((charAt >> 12) | 224)));
                    function1.invoke(Byte.valueOf((byte) (((charAt >> 6) & 63) | 128)));
                    function1.invoke(Byte.valueOf((byte) ((charAt & '?') | 128)));
                } else {
                    if (charAt <= 56319) {
                        int i4 = i + 1;
                        if (i2 > i4) {
                            char charAt2 = str.charAt(i4);
                            if (56320 <= charAt2 && 57343 >= charAt2) {
                                int charAt3 = ((charAt << 10) + str.charAt(i4)) - 56613888;
                                function1.invoke(Byte.valueOf((byte) ((charAt3 >> 18) | 240)));
                                function1.invoke(Byte.valueOf((byte) (((charAt3 >> 12) & 63) | 128)));
                                function1.invoke(Byte.valueOf((byte) (((charAt3 >> 6) & 63) | 128)));
                                function1.invoke(Byte.valueOf((byte) ((charAt3 & 63) | 128)));
                                i += 2;
                            }
                        }
                    }
                    function1.invoke(Byte.valueOf(REPLACEMENT_BYTE));
                }
                i++;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0090, code lost:
        if (r8 == false) goto L_0x004b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0109, code lost:
        if (r8 == false) goto L_0x006c;
     */
    public static final void processUtf8CodePoints(byte[] bArr, int i, int i2, Function1<? super Integer, Unit> function1) {
        int i3;
        byte[] bArr2 = bArr;
        int i4 = i2;
        Function1<? super Integer, Unit> function12 = function1;
        Intrinsics.checkParameterIsNotNull(bArr2, "$receiver");
        Intrinsics.checkParameterIsNotNull(function12, "yield");
        int i5 = i;
        while (i5 < i4) {
            byte b = bArr2[i5];
            if (b >= 0) {
                function12.invoke(Integer.valueOf(b));
                i5++;
                while (i5 < i4 && bArr2[i5] >= 0) {
                    int i6 = i5 + 1;
                    function12.invoke(Integer.valueOf(bArr2[i5]));
                    i5 = i6;
                }
            } else {
                boolean z = false;
                if ((b >> 5) == -2) {
                    int i7 = i5 + 1;
                    if (i4 > i7) {
                        byte b2 = bArr2[i5];
                        byte b3 = bArr2[i7];
                        if ((b3 & 192) == 128) {
                            z = true;
                        }
                        if (z) {
                            byte b4 = (b3 ^ ByteCompanionObject.MIN_VALUE) ^ (b2 << 6);
                            function12.invoke(b4 < 128 ? Integer.valueOf(REPLACEMENT_CODE_POINT) : Integer.valueOf(b4));
                            i3 = 2;
                            i5 += i3;
                        }
                    }
                    function12.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                } else {
                    if ((b >> 4) == -2) {
                        int i8 = i5 + 2;
                        if (i4 <= i8) {
                            function12.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                            int i9 = i5 + 1;
                            if (i4 > i9) {
                                if ((bArr2[i9] & 192) == 128) {
                                    z = true;
                                }
                            }
                        } else {
                            byte b5 = bArr2[i5];
                            byte b6 = bArr2[i5 + 1];
                            if (!((b6 & 192) == 128)) {
                                function12.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                            } else {
                                byte b7 = bArr2[i8];
                                if ((b7 & 192) == 128) {
                                    z = true;
                                }
                                if (!z) {
                                    function12.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                                    i3 = 2;
                                    i5 += i3;
                                } else {
                                    byte b8 = ((b7 ^ ByteCompanionObject.MIN_VALUE) ^ (b6 << 6)) ^ (b5 << 12);
                                    function12.invoke((b8 >= 2048 && (55296 > b8 || 57343 < b8)) ? Integer.valueOf(b8) : Integer.valueOf(REPLACEMENT_CODE_POINT));
                                }
                            }
                        }
                    } else if ((b >> 3) == -2) {
                        int i10 = i5 + 3;
                        if (i4 <= i10) {
                            function12.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                            int i11 = i5 + 1;
                            if (i4 > i11) {
                                if ((bArr2[i11] & 192) == 128) {
                                    int i12 = i5 + 2;
                                    if (i4 > i12) {
                                        if ((bArr2[i12] & 192) == 128) {
                                            z = true;
                                        }
                                    }
                                    i3 = 2;
                                    i5 += i3;
                                }
                            }
                        } else {
                            byte b9 = bArr2[i5];
                            byte b10 = bArr2[i5 + 1];
                            if (!((b10 & 192) == 128)) {
                                function12.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                            } else {
                                byte b11 = bArr2[i5 + 2];
                                if (!((b11 & 192) == 128)) {
                                    function12.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                                    i3 = 2;
                                    i5 += i3;
                                } else {
                                    byte b12 = bArr2[i10];
                                    if ((b12 & 192) == 128) {
                                        z = true;
                                    }
                                    if (!z) {
                                        function12.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                                    } else {
                                        byte b13 = (((b12 ^ ByteCompanionObject.MIN_VALUE) ^ (b11 << 6)) ^ (b10 << 12)) ^ (b9 << 18);
                                        function12.invoke((b13 <= 1114111 && (55296 > b13 || 57343 < b13) && b13 >= 65536) ? Integer.valueOf(b13) : Integer.valueOf(REPLACEMENT_CODE_POINT));
                                        i3 = 4;
                                        i5 += i3;
                                    }
                                }
                            }
                        }
                    } else {
                        function12.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                        i5++;
                    }
                    i3 = 3;
                    i5 += i3;
                }
                i3 = 1;
                i5 += i3;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0092, code lost:
        if (r8 == false) goto L_0x004e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x010b, code lost:
        if (r8 == false) goto L_0x006d;
     */
    public static final void processUtf16Chars(byte[] bArr, int i, int i2, Function1<? super Character, Unit> function1) {
        int i3;
        byte[] bArr2 = bArr;
        int i4 = i2;
        Function1<? super Character, Unit> function12 = function1;
        Intrinsics.checkParameterIsNotNull(bArr2, "$receiver");
        Intrinsics.checkParameterIsNotNull(function12, "yield");
        int i5 = i;
        while (i5 < i4) {
            byte b = bArr2[i5];
            if (b >= 0) {
                function12.invoke(Character.valueOf((char) b));
                i5++;
                while (i5 < i4 && bArr2[i5] >= 0) {
                    int i6 = i5 + 1;
                    function12.invoke(Character.valueOf((char) bArr2[i5]));
                    i5 = i6;
                }
            } else {
                boolean z = false;
                if ((b >> 5) == -2) {
                    int i7 = i5 + 1;
                    if (i4 > i7) {
                        byte b2 = bArr2[i5];
                        byte b3 = bArr2[i7];
                        if ((b3 & 192) == 128) {
                            z = true;
                        }
                        if (z) {
                            byte b4 = (b3 ^ ByteCompanionObject.MIN_VALUE) ^ (b2 << 6);
                            function12.invoke(Character.valueOf(b4 < 128 ? (char) REPLACEMENT_CODE_POINT : (char) b4));
                            i3 = 2;
                            i5 += i3;
                        }
                    }
                    function12.invoke(Character.valueOf((char) REPLACEMENT_CODE_POINT));
                } else {
                    if ((b >> 4) == -2) {
                        int i8 = i5 + 2;
                        if (i4 <= i8) {
                            function12.invoke(Character.valueOf((char) REPLACEMENT_CODE_POINT));
                            int i9 = i5 + 1;
                            if (i4 > i9) {
                                if ((bArr2[i9] & 192) == 128) {
                                    z = true;
                                }
                            }
                        } else {
                            byte b5 = bArr2[i5];
                            byte b6 = bArr2[i5 + 1];
                            if (!((b6 & 192) == 128)) {
                                function12.invoke(Character.valueOf((char) REPLACEMENT_CODE_POINT));
                            } else {
                                byte b7 = bArr2[i8];
                                if ((b7 & 192) == 128) {
                                    z = true;
                                }
                                if (!z) {
                                    function12.invoke(Character.valueOf((char) REPLACEMENT_CODE_POINT));
                                    i3 = 2;
                                    i5 += i3;
                                } else {
                                    byte b8 = ((b7 ^ ByteCompanionObject.MIN_VALUE) ^ (b6 << 6)) ^ (b5 << 12);
                                    function12.invoke(Character.valueOf((b8 >= 2048 && (55296 > b8 || 57343 < b8)) ? (char) b8 : (char) REPLACEMENT_CODE_POINT));
                                }
                            }
                        }
                    } else if ((b >> 3) == -2) {
                        int i10 = i5 + 3;
                        if (i4 <= i10) {
                            function12.invoke(Character.valueOf(REPLACEMENT_CHARACTER));
                            int i11 = i5 + 1;
                            if (i4 > i11) {
                                if ((bArr2[i11] & 192) == 128) {
                                    int i12 = i5 + 2;
                                    if (i4 > i12) {
                                        if ((bArr2[i12] & 192) == 128) {
                                            z = true;
                                        }
                                    }
                                    i3 = 2;
                                    i5 += i3;
                                }
                            }
                        } else {
                            byte b9 = bArr2[i5];
                            byte b10 = bArr2[i5 + 1];
                            if (!((b10 & 192) == 128)) {
                                function12.invoke(Character.valueOf(REPLACEMENT_CHARACTER));
                            } else {
                                byte b11 = bArr2[i5 + 2];
                                if (!((b11 & 192) == 128)) {
                                    function12.invoke(Character.valueOf(REPLACEMENT_CHARACTER));
                                    i3 = 2;
                                    i5 += i3;
                                } else {
                                    byte b12 = bArr2[i10];
                                    if ((b12 & 192) == 128) {
                                        z = true;
                                    }
                                    if (!z) {
                                        function12.invoke(Character.valueOf(REPLACEMENT_CHARACTER));
                                    } else {
                                        byte b13 = (((b12 ^ ByteCompanionObject.MIN_VALUE) ^ (b11 << 6)) ^ (b10 << 12)) ^ (b9 << 18);
                                        if (b13 <= 1114111 && ((55296 > b13 || 57343 < b13) && b13 >= 65536 && b13 != 65533)) {
                                            function12.invoke(Character.valueOf((char) ((b13 >>> 10) + HIGH_SURROGATE_HEADER)));
                                            function12.invoke(Character.valueOf((char) ((b13 & UByte.MAX_VALUE) + LOG_SURROGATE_HEADER)));
                                        } else {
                                            function12.invoke(Character.valueOf(REPLACEMENT_CHARACTER));
                                        }
                                        i3 = 4;
                                        i5 += i3;
                                    }
                                }
                            }
                        }
                    } else {
                        function12.invoke(Character.valueOf(REPLACEMENT_CHARACTER));
                        i5++;
                    }
                    i3 = 3;
                    i5 += i3;
                }
                i3 = 1;
                i5 += i3;
            }
        }
    }

    public static final int process2Utf8Bytes(byte[] bArr, int i, int i2, Function1<? super Integer, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(bArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "yield");
        int i3 = i + 1;
        Integer valueOf = Integer.valueOf(REPLACEMENT_CODE_POINT);
        if (i2 <= i3) {
            function1.invoke(valueOf);
            return 1;
        }
        byte b = bArr[i];
        byte b2 = bArr[i3];
        if (!((b2 & 192) == 128)) {
            function1.invoke(valueOf);
            return 1;
        }
        byte b3 = (b2 ^ ByteCompanionObject.MIN_VALUE) ^ (b << 6);
        if (b3 < 128) {
            function1.invoke(valueOf);
        } else {
            function1.invoke(Integer.valueOf(b3));
        }
        return 2;
    }

    public static final int process3Utf8Bytes(byte[] bArr, int i, int i2, Function1<? super Integer, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(bArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "yield");
        int i3 = i + 2;
        boolean z = false;
        Integer valueOf = Integer.valueOf(REPLACEMENT_CODE_POINT);
        if (i2 <= i3) {
            function1.invoke(valueOf);
            int i4 = i + 1;
            if (i2 > i4) {
                if ((bArr[i4] & 192) == 128) {
                    z = true;
                }
                return !z ? 1 : 2;
            }
        }
        byte b = bArr[i];
        byte b2 = bArr[i + 1];
        if (!((b2 & 192) == 128)) {
            function1.invoke(valueOf);
            return 1;
        }
        byte b3 = bArr[i3];
        if ((b3 & 192) == 128) {
            z = true;
        }
        if (!z) {
            function1.invoke(valueOf);
            return 2;
        }
        byte b4 = ((b3 ^ ByteCompanionObject.MIN_VALUE) ^ (b2 << 6)) ^ (b << 12);
        if (b4 < 2048) {
            function1.invoke(valueOf);
        } else if (55296 <= b4 && 57343 >= b4) {
            function1.invoke(valueOf);
        } else {
            function1.invoke(Integer.valueOf(b4));
        }
        return 3;
    }

    public static final int process4Utf8Bytes(byte[] bArr, int i, int i2, Function1<? super Integer, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(bArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "yield");
        int i3 = i + 3;
        boolean z = false;
        Integer valueOf = Integer.valueOf(REPLACEMENT_CODE_POINT);
        if (i2 <= i3) {
            function1.invoke(valueOf);
            int i4 = i + 1;
            if (i2 > i4) {
                if ((bArr[i4] & 192) == 128) {
                    int i5 = i + 2;
                    if (i2 > i5) {
                        if ((bArr[i5] & 192) == 128) {
                            z = true;
                        }
                        return !z ? 2 : 3;
                    }
                }
            }
            return 1;
        }
        byte b = bArr[i];
        byte b2 = bArr[i + 1];
        if (!((b2 & 192) == 128)) {
            function1.invoke(valueOf);
            return 1;
        }
        byte b3 = bArr[i + 2];
        if (!((b3 & 192) == 128)) {
            function1.invoke(valueOf);
            return 2;
        }
        byte b4 = bArr[i3];
        if ((b4 & 192) == 128) {
            z = true;
        }
        if (!z) {
            function1.invoke(valueOf);
            return 3;
        }
        byte b5 = (((b4 ^ ByteCompanionObject.MIN_VALUE) ^ (b3 << 6)) ^ (b2 << 12)) ^ (b << 18);
        if (b5 > 1114111) {
            function1.invoke(valueOf);
        } else if (55296 <= b5 && 57343 >= b5) {
            function1.invoke(valueOf);
        } else if (b5 < 65536) {
            function1.invoke(valueOf);
        } else {
            function1.invoke(Integer.valueOf(b5));
        }
        return 4;
    }
}
