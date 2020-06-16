package okio.internal;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.UByte;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okio.Base64;
import okio.ByteString;
import okio.Platform;
import okio.Util;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000B\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0019\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0017\u001a\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0007H\u0002\u001a\u0010\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\tH\u0000\u001a\u0010\u0010\r\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u000fH\u0002\u001a\f\u0010\u0010\u001a\u00020\u0011*\u00020\u0001H\u0000\u001a\f\u0010\u0012\u001a\u00020\u0011*\u00020\u0001H\u0000\u001a\u0014\u0010\u0013\u001a\u00020\u0007*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u0001H\u0000\u001a\u000e\u0010\u0015\u001a\u0004\u0018\u00010\u0001*\u00020\u0011H\u0000\u001a\f\u0010\u0016\u001a\u00020\u0001*\u00020\u0011H\u0000\u001a\f\u0010\u0017\u001a\u00020\u0001*\u00020\u0011H\u0000\u001a\u0014\u0010\u0018\u001a\u00020\u0019*\u00020\u00012\u0006\u0010\u001a\u001a\u00020\tH\u0000\u001a\u0014\u0010\u0018\u001a\u00020\u0019*\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u0001H\u0000\u001a\u0016\u0010\u001b\u001a\u00020\u0019*\u00020\u00012\b\u0010\u0014\u001a\u0004\u0018\u00010\u001cH\u0000\u001a\u0014\u0010\u001d\u001a\u00020\u001e*\u00020\u00012\u0006\u0010\u001f\u001a\u00020\u0007H\u0000\u001a\f\u0010 \u001a\u00020\u0007*\u00020\u0001H\u0000\u001a\f\u0010!\u001a\u00020\u0007*\u00020\u0001H\u0000\u001a\f\u0010\"\u001a\u00020\u0011*\u00020\u0001H\u0000\u001a\u001c\u0010#\u001a\u00020\u0007*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010$\u001a\u00020\u0007H\u0000\u001a\f\u0010%\u001a\u00020\t*\u00020\u0001H\u0000\u001a\u001c\u0010&\u001a\u00020\u0007*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010$\u001a\u00020\u0007H\u0000\u001a,\u0010'\u001a\u00020\u0019*\u00020\u00012\u0006\u0010(\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010)\u001a\u00020\u00072\u0006\u0010*\u001a\u00020\u0007H\u0000\u001a,\u0010'\u001a\u00020\u0019*\u00020\u00012\u0006\u0010(\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00012\u0006\u0010)\u001a\u00020\u00072\u0006\u0010*\u001a\u00020\u0007H\u0000\u001a\u0014\u0010+\u001a\u00020\u0019*\u00020\u00012\u0006\u0010,\u001a\u00020\tH\u0000\u001a\u0014\u0010+\u001a\u00020\u0019*\u00020\u00012\u0006\u0010,\u001a\u00020\u0001H\u0000\u001a\u001c\u0010-\u001a\u00020\u0001*\u00020\u00012\u0006\u0010.\u001a\u00020\u00072\u0006\u0010/\u001a\u00020\u0007H\u0000\u001a\f\u00100\u001a\u00020\u0001*\u00020\u0001H\u0000\u001a\f\u00101\u001a\u00020\u0001*\u00020\u0001H\u0000\u001a\f\u00102\u001a\u00020\t*\u00020\u0001H\u0000\u001a\f\u00103\u001a\u00020\u0011*\u00020\u0001H\u0000\u001a\f\u00104\u001a\u00020\u0011*\u00020\u0001H\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u00065"}, d2 = {"COMMON_EMPTY", "Lokio/ByteString;", "getCOMMON_EMPTY", "()Lokio/ByteString;", "HEX_DIGITS", "", "codePointIndexToCharIndex", "", "s", "", "codePointCount", "commonOf", "data", "decodeHexDigit", "c", "", "commonBase64", "", "commonBase64Url", "commonCompareTo", "other", "commonDecodeBase64", "commonDecodeHex", "commonEncodeUtf8", "commonEndsWith", "", "suffix", "commonEquals", "", "commonGetByte", "", "pos", "commonGetSize", "commonHashCode", "commonHex", "commonIndexOf", "fromIndex", "commonInternalArray", "commonLastIndexOf", "commonRangeEquals", "offset", "otherOffset", "byteCount", "commonStartsWith", "prefix", "commonSubstring", "beginIndex", "endIndex", "commonToAsciiLowercase", "commonToAsciiUppercase", "commonToByteArray", "commonToString", "commonUtf8", "jvm"}, k = 2, mv = {1, 1, 11})
/* compiled from: ByteString.kt */
public final class ByteStringKt {
    private static final ByteString COMMON_EMPTY = ByteString.Companion.of(new byte[0]);
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static final String commonUtf8(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        String utf8$jvm = byteString.getUtf8$jvm();
        if (utf8$jvm != null) {
            return utf8$jvm;
        }
        String utf8String = Platform.toUtf8String(byteString.internalArray$jvm());
        byteString.setUtf8$jvm(utf8String);
        return utf8String;
    }

    public static final String commonBase64(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        return Base64.encodeBase64$default(byteString.getData$jvm(), null, 1, null);
    }

    public static final String commonBase64Url(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        return Base64.encodeBase64(byteString.getData$jvm(), Base64.getBASE64_URL_SAFE());
    }

    public static final String commonHex(ByteString byteString) {
        byte[] data$jvm;
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        char[] cArr = new char[(byteString.getData$jvm().length * 2)];
        int i = 0;
        for (byte b : byteString.getData$jvm()) {
            int i2 = i + 1;
            char[] cArr2 = HEX_DIGITS;
            cArr[i] = cArr2[(b >> 4) & 15];
            i = i2 + 1;
            cArr[i2] = cArr2[b & 15];
        }
        return new String(cArr);
    }

    public static final ByteString commonToAsciiLowercase(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        for (int i = 0; i < byteString.getData$jvm().length; i++) {
            byte b = byteString.getData$jvm()[i];
            byte b2 = (byte) 65;
            if (b >= b2) {
                byte b3 = (byte) 90;
                if (b <= b3) {
                    byte[] data$jvm = byteString.getData$jvm();
                    byte[] copyOf = Arrays.copyOf(data$jvm, data$jvm.length);
                    Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
                    copyOf[i] = (byte) (b + 32);
                    for (int i2 = i + 1; i2 < copyOf.length; i2++) {
                        byte b4 = copyOf[i2];
                        if (b4 >= b2 && b4 <= b3) {
                            copyOf[i2] = (byte) (b4 + 32);
                        }
                    }
                    return new ByteString(copyOf);
                }
            }
        }
        return byteString;
    }

    public static final ByteString commonToAsciiUppercase(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        for (int i = 0; i < byteString.getData$jvm().length; i++) {
            byte b = byteString.getData$jvm()[i];
            byte b2 = (byte) 97;
            if (b >= b2) {
                byte b3 = (byte) 122;
                if (b <= b3) {
                    byte[] data$jvm = byteString.getData$jvm();
                    byte[] copyOf = Arrays.copyOf(data$jvm, data$jvm.length);
                    Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
                    copyOf[i] = (byte) (b - 32);
                    for (int i2 = i + 1; i2 < copyOf.length; i2++) {
                        byte b4 = copyOf[i2];
                        if (b4 >= b2 && b4 <= b3) {
                            copyOf[i2] = (byte) (b4 - 32);
                        }
                    }
                    return new ByteString(copyOf);
                }
            }
        }
        return byteString;
    }

    public static final ByteString commonSubstring(ByteString byteString, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        boolean z = true;
        if (i >= 0) {
            if (i2 <= byteString.getData$jvm().length) {
                int i3 = i2 - i;
                if (i3 < 0) {
                    z = false;
                }
                if (!z) {
                    throw new IllegalArgumentException("endIndex < beginIndex".toString());
                } else if (i == 0 && i2 == byteString.getData$jvm().length) {
                    return byteString;
                } else {
                    byte[] bArr = new byte[i3];
                    Platform.arraycopy(byteString.getData$jvm(), i, bArr, 0, i3);
                    return new ByteString(bArr);
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("endIndex > length(");
                sb.append(byteString.getData$jvm().length);
                sb.append(')');
                throw new IllegalArgumentException(sb.toString().toString());
            }
        } else {
            throw new IllegalArgumentException("beginIndex < 0".toString());
        }
    }

    public static final byte commonGetByte(ByteString byteString, int i) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        return byteString.getData$jvm()[i];
    }

    public static final int commonGetSize(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        return byteString.getData$jvm().length;
    }

    public static final byte[] commonToByteArray(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        byte[] data$jvm = byteString.getData$jvm();
        byte[] copyOf = Arrays.copyOf(data$jvm, data$jvm.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    public static final byte[] commonInternalArray(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        return byteString.getData$jvm();
    }

    public static final boolean commonRangeEquals(ByteString byteString, int i, ByteString byteString2, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        Intrinsics.checkParameterIsNotNull(byteString2, "other");
        return byteString2.rangeEquals(i2, byteString.getData$jvm(), i, i3);
    }

    public static final boolean commonRangeEquals(ByteString byteString, int i, byte[] bArr, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        Intrinsics.checkParameterIsNotNull(bArr, "other");
        return i >= 0 && i <= byteString.getData$jvm().length - i3 && i2 >= 0 && i2 <= bArr.length - i3 && Util.arrayRangeEquals(byteString.getData$jvm(), i, bArr, i2, i3);
    }

    public static final boolean commonStartsWith(ByteString byteString, ByteString byteString2) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        Intrinsics.checkParameterIsNotNull(byteString2, "prefix");
        return byteString.rangeEquals(0, byteString2, 0, byteString2.size());
    }

    public static final boolean commonStartsWith(ByteString byteString, byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        Intrinsics.checkParameterIsNotNull(bArr, "prefix");
        return byteString.rangeEquals(0, bArr, 0, bArr.length);
    }

    public static final boolean commonEndsWith(ByteString byteString, ByteString byteString2) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        Intrinsics.checkParameterIsNotNull(byteString2, "suffix");
        return byteString.rangeEquals(byteString.size() - byteString2.size(), byteString2, 0, byteString2.size());
    }

    public static final boolean commonEndsWith(ByteString byteString, byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        Intrinsics.checkParameterIsNotNull(bArr, "suffix");
        return byteString.rangeEquals(byteString.size() - bArr.length, bArr, 0, bArr.length);
    }

    public static final int commonIndexOf(ByteString byteString, byte[] bArr, int i) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        Intrinsics.checkParameterIsNotNull(bArr, "other");
        int length = byteString.getData$jvm().length - bArr.length;
        int max = Math.max(i, 0);
        if (max <= length) {
            while (!Util.arrayRangeEquals(byteString.getData$jvm(), max, bArr, 0, bArr.length)) {
                if (max != length) {
                    max++;
                }
            }
            return max;
        }
        return -1;
    }

    public static final int commonLastIndexOf(ByteString byteString, byte[] bArr, int i) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        Intrinsics.checkParameterIsNotNull(bArr, "other");
        for (int min = Math.min(i, byteString.getData$jvm().length - bArr.length); min >= 0; min--) {
            if (Util.arrayRangeEquals(byteString.getData$jvm(), min, bArr, 0, bArr.length)) {
                return min;
            }
        }
        return -1;
    }

    public static final boolean commonEquals(ByteString byteString, Object obj) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        if (obj == byteString) {
            return true;
        }
        if (obj instanceof ByteString) {
            ByteString byteString2 = (ByteString) obj;
            if (byteString2.size() == byteString.getData$jvm().length && byteString2.rangeEquals(0, byteString.getData$jvm(), 0, byteString.getData$jvm().length)) {
                return true;
            }
        }
        return false;
    }

    public static final int commonHashCode(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        int hashCode$jvm = byteString.getHashCode$jvm();
        if (hashCode$jvm != 0) {
            return hashCode$jvm;
        }
        byteString.setHashCode$jvm(Arrays.hashCode(byteString.getData$jvm()));
        return byteString.getHashCode$jvm();
    }

    public static final int commonCompareTo(ByteString byteString, ByteString byteString2) {
        Intrinsics.checkParameterIsNotNull(byteString, "$receiver");
        Intrinsics.checkParameterIsNotNull(byteString2, "other");
        int size = byteString.size();
        int size2 = byteString2.size();
        int min = Math.min(size, size2);
        int i = 0;
        while (true) {
            int i2 = -1;
            if (i < min) {
                byte b = byteString.getByte(i) & UByte.MAX_VALUE;
                byte b2 = byteString2.getByte(i) & UByte.MAX_VALUE;
                if (b == b2) {
                    i++;
                } else {
                    if (b >= b2) {
                        i2 = 1;
                    }
                    return i2;
                }
            } else if (size == size2) {
                return 0;
            } else {
                if (size >= size2) {
                    i2 = 1;
                }
                return i2;
            }
        }
    }

    public static final ByteString getCOMMON_EMPTY() {
        return COMMON_EMPTY;
    }

    public static final ByteString commonOf(byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(bArr, "data");
        byte[] copyOf = Arrays.copyOf(bArr, bArr.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return new ByteString(copyOf);
    }

    public static final ByteString commonEncodeUtf8(String str) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        ByteString byteString = new ByteString(Platform.asUtf8ToByteArray(str));
        byteString.setUtf8$jvm(str);
        return byteString;
    }

    public static final ByteString commonDecodeBase64(String str) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        byte[] decodeBase64ToArray = Base64.decodeBase64ToArray(str);
        if (decodeBase64ToArray != null) {
            return new ByteString(decodeBase64ToArray);
        }
        return null;
    }

    public static final ByteString commonDecodeHex(String str) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        if (str.length() % 2 == 0) {
            int length = str.length() / 2;
            byte[] bArr = new byte[length];
            for (int i = 0; i < length; i++) {
                int i2 = i * 2;
                bArr[i] = (byte) ((decodeHexDigit(str.charAt(i2)) << 4) + decodeHexDigit(str.charAt(i2 + 1)));
            }
            return new ByteString(bArr);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected hex string: ");
        sb.append(str);
        throw new IllegalArgumentException(sb.toString().toString());
    }

    private static final int decodeHexDigit(char c) {
        if ('0' <= c && '9' >= c) {
            return c - '0';
        }
        char c2 = 'a';
        if ('a' > c || 'f' < c) {
            c2 = 'A';
            if ('A' > c || 'F' < c) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected hex digit: ");
                sb.append(c);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        return (c - c2) + 10;
    }

    public static final String commonToString(ByteString byteString) {
        String str;
        String str2;
        ByteString byteString2 = byteString;
        Intrinsics.checkParameterIsNotNull(byteString2, "$receiver");
        if (byteString.getData$jvm().length == 0) {
            return "[size=0]";
        }
        int codePointIndexToCharIndex = codePointIndexToCharIndex(byteString.getData$jvm(), 64);
        String str3 = "…]";
        String str4 = "[size=";
        if (codePointIndexToCharIndex == -1) {
            if (byteString.getData$jvm().length <= 64) {
                StringBuilder sb = new StringBuilder();
                sb.append("[hex=");
                sb.append(byteString.hex());
                sb.append(']');
                str2 = sb.toString();
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str4);
                sb2.append(byteString.getData$jvm().length);
                sb2.append(" hex=");
                sb2.append(commonSubstring(byteString2, 0, 64).hex());
                sb2.append(str3);
                str2 = sb2.toString();
            }
            return str2;
        }
        String utf8 = byteString.utf8();
        if (utf8 != null) {
            String substring = utf8.substring(0, codePointIndexToCharIndex);
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            String replace$default = StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(substring, "\\", "\\\\", false, 4, (Object) null), "\n", "\\n", false, 4, (Object) null), "\r", "\\r", false, 4, (Object) null);
            if (codePointIndexToCharIndex < utf8.length()) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str4);
                sb3.append(byteString.getData$jvm().length);
                sb3.append(" text=");
                sb3.append(replace$default);
                sb3.append(str3);
                str = sb3.toString();
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("[text=");
                sb4.append(replace$default);
                sb4.append(']');
                str = sb4.toString();
            }
            return str;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0068, code lost:
        return -1;
     */
    private static final int codePointIndexToCharIndex(byte[] bArr, int i) {
        int i2;
        int i3;
        int i4;
        byte[] bArr2 = bArr;
        int i5 = i;
        int length = bArr2.length;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        loop0:
        while (i6 < length) {
            byte b = bArr2[i6];
            if (b >= 0) {
                int i9 = i8 + 1;
                if (i8 != i5) {
                    if (!(b == 10 || b == 13)) {
                        if ((b >= 0 && 31 >= b) || (Byte.MAX_VALUE <= b && 159 >= b)) {
                            return -1;
                        }
                    }
                    if (b != 65533) {
                        i7 += b < 65536 ? 1 : 2;
                        i6++;
                        while (true) {
                            i8 = i9;
                            if (i6 >= length || bArr2[i6] < 0) {
                                break;
                            }
                            int i10 = i6 + 1;
                            byte b2 = bArr2[i6];
                            i9 = i8 + 1;
                            if (i8 == i5) {
                                return i7;
                            }
                            if (!(b2 == 10 || b2 == 13)) {
                                if ((b2 >= 0 && 31 >= b2) || (Byte.MAX_VALUE <= b2 && 159 >= b2)) {
                                    break loop0;
                                }
                            }
                            if (b2 == 65533) {
                                break loop0;
                            }
                            i7 += b2 < 65536 ? 1 : 2;
                            i6 = i10;
                        }
                    } else {
                        return -1;
                    }
                } else {
                    return i7;
                }
            } else {
                if ((b >> 5) == -2) {
                    int i11 = i6 + 1;
                    if (length > i11) {
                        byte b3 = bArr2[i6];
                        byte b4 = bArr2[i11];
                        if ((b4 & 192) == 128) {
                            byte b5 = (b4 ^ ByteCompanionObject.MIN_VALUE) ^ (b3 << 6);
                            if (b5 >= 128) {
                                i2 = i8 + 1;
                                if (i8 == i5) {
                                    return i7;
                                }
                                if (!(b5 == 10 || b5 == 13)) {
                                    if ((b5 >= 0 && 31 >= b5) || (Byte.MAX_VALUE <= b5 && 159 >= b5)) {
                                        return -1;
                                    }
                                }
                                if (b5 == 65533) {
                                    return -1;
                                }
                                i3 = i7 + (b5 < 65536 ? 1 : 2);
                                i4 = i6 + 2;
                            } else if (i8 == i5) {
                                return i7;
                            } else {
                                return -1;
                            }
                        } else if (i8 == i5) {
                            return i7;
                        } else {
                            return -1;
                        }
                    } else if (i8 == i5) {
                        return i7;
                    } else {
                        return -1;
                    }
                } else if ((b >> 4) == -2) {
                    int i12 = i6 + 2;
                    if (length > i12) {
                        byte b6 = bArr2[i6];
                        byte b7 = bArr2[i6 + 1];
                        if ((b7 & 192) == 128) {
                            byte b8 = bArr2[i12];
                            if ((b8 & 192) == 128) {
                                byte b9 = ((b8 ^ ByteCompanionObject.MIN_VALUE) ^ (b7 << 6)) ^ (b6 << 12);
                                if (b9 < 2048) {
                                    if (i8 == i5) {
                                        return i7;
                                    }
                                    return -1;
                                } else if (55296 > b9 || 57343 < b9) {
                                    i2 = i8 + 1;
                                    if (i8 == i5) {
                                        return i7;
                                    }
                                    if (!(b9 == 10 || b9 == 13)) {
                                        if ((b9 >= 0 && 31 >= b9) || (Byte.MAX_VALUE <= b9 && 159 >= b9)) {
                                            return -1;
                                        }
                                    }
                                    if (b9 == 65533) {
                                        return -1;
                                    }
                                    i3 = i7 + (b9 < 65536 ? 1 : 2);
                                    i4 = i6 + 3;
                                } else if (i8 == i5) {
                                    return i7;
                                } else {
                                    return -1;
                                }
                            } else if (i8 == i5) {
                                return i7;
                            } else {
                                return -1;
                            }
                        } else if (i8 == i5) {
                            return i7;
                        } else {
                            return -1;
                        }
                    } else if (i8 == i5) {
                        return i7;
                    } else {
                        return -1;
                    }
                } else if ((b >> 3) == -2) {
                    int i13 = i6 + 3;
                    if (length > i13) {
                        byte b10 = bArr2[i6];
                        byte b11 = bArr2[i6 + 1];
                        if ((b11 & 192) == 128) {
                            byte b12 = bArr2[i6 + 2];
                            if ((b12 & 192) == 128) {
                                byte b13 = bArr2[i13];
                                if ((b13 & 192) == 128) {
                                    byte b14 = (((b13 ^ ByteCompanionObject.MIN_VALUE) ^ (b12 << 6)) ^ (b11 << 12)) ^ (b10 << 18);
                                    if (b14 > 1114111) {
                                        if (i8 == i5) {
                                            return i7;
                                        }
                                        return -1;
                                    } else if (55296 <= b14 && 57343 >= b14) {
                                        if (i8 == i5) {
                                            return i7;
                                        }
                                        return -1;
                                    } else if (b14 >= 65536) {
                                        i2 = i8 + 1;
                                        if (i8 == i5) {
                                            return i7;
                                        }
                                        if (!(b14 == 10 || b14 == 13)) {
                                            if ((b14 >= 0 && 31 >= b14) || (Byte.MAX_VALUE <= b14 && 159 >= b14)) {
                                                return -1;
                                            }
                                        }
                                        if (b14 == 65533) {
                                            return -1;
                                        }
                                        i3 = i7 + (b14 < 65536 ? 1 : 2);
                                        i4 = i6 + 4;
                                    } else if (i8 == i5) {
                                        return i7;
                                    } else {
                                        return -1;
                                    }
                                } else if (i8 == i5) {
                                    return i7;
                                } else {
                                    return -1;
                                }
                            } else if (i8 == i5) {
                                return i7;
                            } else {
                                return -1;
                            }
                        } else if (i8 == i5) {
                            return i7;
                        } else {
                            return -1;
                        }
                    } else if (i8 == i5) {
                        return i7;
                    } else {
                        return -1;
                    }
                } else if (i8 == i5) {
                    return i7;
                } else {
                    return -1;
                }
                i8 = i2;
            }
        }
        return i7;
    }
}
