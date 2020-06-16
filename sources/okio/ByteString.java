package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import okio.internal.ByteStringKt;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u001a\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0016\u0018\u0000 Z2\u00020\u00012\b\u0012\u0004\u0012\u00020\u00000\u0002:\u0001ZB\u000f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0010H\u0016J\b\u0010\u0018\u001a\u00020\u0010H\u0016J\u0011\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u0000H\u0002J\u0015\u0010\u001b\u001a\u00020\u00002\u0006\u0010\u001c\u001a\u00020\u0010H\u0010¢\u0006\u0002\b\u001dJ\u000e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0004J\u000e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0000J\u0013\u0010!\u001a\u00020\u001f2\b\u0010\u001a\u001a\u0004\u0018\u00010\"H\u0002J\u0016\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\tH\u0002¢\u0006\u0002\b&J\u0015\u0010&\u001a\u00020$2\u0006\u0010%\u001a\u00020\tH\u0007¢\u0006\u0002\b'J\r\u0010(\u001a\u00020\tH\u0010¢\u0006\u0002\b)J\b\u0010\b\u001a\u00020\tH\u0016J\b\u0010*\u001a\u00020\u0010H\u0016J\u001d\u0010+\u001a\u00020\u00002\u0006\u0010\u001c\u001a\u00020\u00102\u0006\u0010,\u001a\u00020\u0000H\u0010¢\u0006\u0002\b-J\u0010\u0010.\u001a\u00020\u00002\u0006\u0010,\u001a\u00020\u0000H\u0016J\u0010\u0010/\u001a\u00020\u00002\u0006\u0010,\u001a\u00020\u0000H\u0016J\u0010\u00100\u001a\u00020\u00002\u0006\u0010,\u001a\u00020\u0000H\u0016J\u001a\u00101\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00042\b\b\u0002\u00102\u001a\u00020\tH\u0017J\u001a\u00101\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u00102\u001a\u00020\tH\u0007J\r\u00103\u001a\u00020\u0004H\u0010¢\u0006\u0002\b4J\u0015\u00105\u001a\u00020$2\u0006\u00106\u001a\u00020\tH\u0010¢\u0006\u0002\b7J\u001a\u00108\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00042\b\b\u0002\u00102\u001a\u00020\tH\u0017J\u001a\u00108\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u00102\u001a\u00020\tH\u0007J\b\u00109\u001a\u00020\u0000H\u0016J(\u0010:\u001a\u00020\u001f2\u0006\u0010;\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00042\u0006\u0010<\u001a\u00020\t2\u0006\u0010=\u001a\u00020\tH\u0016J(\u0010:\u001a\u00020\u001f2\u0006\u0010;\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00002\u0006\u0010<\u001a\u00020\t2\u0006\u0010=\u001a\u00020\tH\u0016J\u0010\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020AH\u0002J\b\u0010B\u001a\u00020\u0000H\u0016J\b\u0010C\u001a\u00020\u0000H\u0016J\b\u0010D\u001a\u00020\u0000H\u0016J\r\u0010\u000e\u001a\u00020\tH\u0007¢\u0006\u0002\bEJ\u000e\u0010F\u001a\u00020\u001f2\u0006\u0010G\u001a\u00020\u0004J\u000e\u0010F\u001a\u00020\u001f2\u0006\u0010G\u001a\u00020\u0000J\u0010\u0010H\u001a\u00020\u00102\u0006\u0010I\u001a\u00020JH\u0016J\u001c\u0010K\u001a\u00020\u00002\b\b\u0002\u0010L\u001a\u00020\t2\b\b\u0002\u0010M\u001a\u00020\tH\u0017J\b\u0010N\u001a\u00020\u0000H\u0016J\b\u0010O\u001a\u00020\u0000H\u0016J\b\u0010P\u001a\u00020\u0004H\u0016J\b\u0010Q\u001a\u00020\u0010H\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010R\u001a\u00020?2\u0006\u0010S\u001a\u00020TH\u0016J\u0015\u0010R\u001a\u00020?2\u0006\u0010U\u001a\u00020VH\u0010¢\u0006\u0002\bWJ\u0010\u0010X\u001a\u00020?2\u0006\u0010S\u001a\u00020YH\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001e\u0010\b\u001a\u00020\t8\u0000@\u0000X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\t8G¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000bR \u0010\u000f\u001a\u0004\u0018\u00010\u00108\u0000@\u0000X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014¨\u0006["}, d2 = {"Lokio/ByteString;", "Ljava/io/Serializable;", "", "data", "", "([B)V", "getData$jvm", "()[B", "hashCode", "", "getHashCode$jvm", "()I", "setHashCode$jvm", "(I)V", "size", "utf8", "", "getUtf8$jvm", "()Ljava/lang/String;", "setUtf8$jvm", "(Ljava/lang/String;)V", "asByteBuffer", "Ljava/nio/ByteBuffer;", "base64", "base64Url", "compareTo", "other", "digest", "algorithm", "digest$jvm", "endsWith", "", "suffix", "equals", "", "get", "", "index", "getByte", "-deprecated_getByte", "getSize", "getSize$jvm", "hex", "hmac", "key", "hmac$jvm", "hmacSha1", "hmacSha256", "hmacSha512", "indexOf", "fromIndex", "internalArray", "internalArray$jvm", "internalGet", "pos", "internalGet$jvm", "lastIndexOf", "md5", "rangeEquals", "offset", "otherOffset", "byteCount", "readObject", "", "in", "Ljava/io/ObjectInputStream;", "sha1", "sha256", "sha512", "-deprecated_size", "startsWith", "prefix", "string", "charset", "Ljava/nio/charset/Charset;", "substring", "beginIndex", "endIndex", "toAsciiLowercase", "toAsciiUppercase", "toByteArray", "toString", "write", "out", "Ljava/io/OutputStream;", "buffer", "Lokio/Buffer;", "write$jvm", "writeObject", "Ljava/io/ObjectOutputStream;", "Companion", "jvm"}, k = 1, mv = {1, 1, 11})
/* compiled from: ByteString.kt */
public class ByteString implements Serializable, Comparable<ByteString> {
    public static final Companion Companion = new Companion(null);
    public static final ByteString EMPTY = ByteStringKt.getCOMMON_EMPTY();
    private static final long serialVersionUID = 1;
    private final byte[] data;
    private transient int hashCode;
    private transient String utf8;

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0017\u0010\u0007\u001a\u0004\u0018\u00010\u00042\u0006\u0010\b\u001a\u00020\tH\u0007¢\u0006\u0002\b\nJ\u0015\u0010\u000b\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0007¢\u0006\u0002\b\fJ\u001d\u0010\r\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007¢\u0006\u0002\b\u0010J\u0015\u0010\u0011\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0007¢\u0006\u0002\b\u0012J\u0015\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u0015H\u0007¢\u0006\u0002\b\u0016J\u0014\u0010\u0013\u001a\u00020\u00042\n\u0010\u0017\u001a\u00020\u0018\"\u00020\u0019H\u0007J%\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001cH\u0007¢\u0006\u0002\b\u0016J\u001d\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010\u001d\u001a\u00020\u001cH\u0007¢\u0006\u0002\b!J\u000e\u0010\u0007\u001a\u0004\u0018\u00010\u0004*\u00020\tH\u0007J\f\u0010\u000b\u001a\u00020\u0004*\u00020\tH\u0007J\u001b\u0010\"\u001a\u00020\u0004*\u00020\t2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0007¢\u0006\u0002\b\rJ\f\u0010\u0011\u001a\u00020\u0004*\u00020\tH\u0007J\u0019\u0010#\u001a\u00020\u0004*\u00020 2\u0006\u0010\u001d\u001a\u00020\u001cH\u0007¢\u0006\u0002\b\u001eJ\u0011\u0010$\u001a\u00020\u0004*\u00020\u0015H\u0007¢\u0006\u0002\b\u0013J%\u0010$\u001a\u00020\u0004*\u00020\u00182\b\b\u0002\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u001cH\u0007¢\u0006\u0002\b\u0013R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006%"}, d2 = {"Lokio/ByteString$Companion;", "", "()V", "EMPTY", "Lokio/ByteString;", "serialVersionUID", "", "decodeBase64", "string", "", "-deprecated_decodeBase64", "decodeHex", "-deprecated_decodeHex", "encodeString", "charset", "Ljava/nio/charset/Charset;", "-deprecated_encodeString", "encodeUtf8", "-deprecated_encodeUtf8", "of", "buffer", "Ljava/nio/ByteBuffer;", "-deprecated_of", "data", "", "", "array", "offset", "", "byteCount", "read", "inputstream", "Ljava/io/InputStream;", "-deprecated_read", "encode", "readByteString", "toByteString", "jvm"}, k = 1, mv = {1, 1, 11})
    /* compiled from: ByteString.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public final ByteString of(byte... bArr) {
            Intrinsics.checkParameterIsNotNull(bArr, "data");
            return ByteStringKt.commonOf(bArr);
        }

        @JvmStatic
        public static /* bridge */ /* synthetic */ ByteString of$default(Companion companion, byte[] bArr, int i, int i2, int i3, Object obj) {
            if ((i3 & 1) != 0) {
                i = 0;
            }
            if ((i3 & 2) != 0) {
                i2 = bArr.length;
            }
            return companion.of(bArr, i, i2);
        }

        @JvmStatic
        public final ByteString of(byte[] bArr, int i, int i2) {
            Intrinsics.checkParameterIsNotNull(bArr, "$receiver");
            Util.checkOffsetAndCount((long) bArr.length, (long) i, (long) i2);
            byte[] bArr2 = new byte[i2];
            Platform.arraycopy(bArr, i, bArr2, 0, i2);
            return new ByteString(bArr2);
        }

        @JvmStatic
        public final ByteString of(ByteBuffer byteBuffer) {
            Intrinsics.checkParameterIsNotNull(byteBuffer, "$receiver");
            byte[] bArr = new byte[byteBuffer.remaining()];
            byteBuffer.get(bArr);
            return new ByteString(bArr);
        }

        @JvmStatic
        public final ByteString encodeUtf8(String str) {
            Intrinsics.checkParameterIsNotNull(str, "$receiver");
            return ByteStringKt.commonEncodeUtf8(str);
        }

        @JvmStatic
        public static /* bridge */ /* synthetic */ ByteString encodeString$default(Companion companion, String str, Charset charset, int i, Object obj) {
            if ((i & 1) != 0) {
                charset = Charsets.UTF_8;
            }
            return companion.encodeString(str, charset);
        }

        @JvmStatic
        public final ByteString encodeString(String str, Charset charset) {
            Intrinsics.checkParameterIsNotNull(str, "$receiver");
            Intrinsics.checkParameterIsNotNull(charset, "charset");
            byte[] bytes = str.getBytes(charset);
            Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
            return new ByteString(bytes);
        }

        @JvmStatic
        public final ByteString decodeBase64(String str) {
            Intrinsics.checkParameterIsNotNull(str, "$receiver");
            return ByteStringKt.commonDecodeBase64(str);
        }

        @JvmStatic
        public final ByteString decodeHex(String str) {
            Intrinsics.checkParameterIsNotNull(str, "$receiver");
            return ByteStringKt.commonDecodeHex(str);
        }

        @JvmStatic
        public final ByteString read(InputStream inputStream, int i) throws IOException {
            Intrinsics.checkParameterIsNotNull(inputStream, "$receiver");
            int i2 = 0;
            if (i >= 0) {
                byte[] bArr = new byte[i];
                while (i2 < i) {
                    int read = inputStream.read(bArr, i2, i - i2);
                    if (read != -1) {
                        i2 += read;
                    } else {
                        throw new EOFException();
                    }
                }
                return new ByteString(bArr);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("byteCount < 0: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString().toString());
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to extension function", replaceWith = @ReplaceWith(expression = "string.decodeBase64()", imports = {"okio.ByteString.Companion.decodeBase64"}))
        /* renamed from: -deprecated_decodeBase64 reason: not valid java name */
        public final ByteString m1117deprecated_decodeBase64(String str) {
            Intrinsics.checkParameterIsNotNull(str, "string");
            return decodeBase64(str);
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to extension function", replaceWith = @ReplaceWith(expression = "string.decodeHex()", imports = {"okio.ByteString.Companion.decodeHex"}))
        /* renamed from: -deprecated_decodeHex reason: not valid java name */
        public final ByteString m1118deprecated_decodeHex(String str) {
            Intrinsics.checkParameterIsNotNull(str, "string");
            return decodeHex(str);
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to extension function", replaceWith = @ReplaceWith(expression = "string.encode(charset)", imports = {"okio.ByteString.Companion.encode"}))
        /* renamed from: -deprecated_encodeString reason: not valid java name */
        public final ByteString m1119deprecated_encodeString(String str, Charset charset) {
            Intrinsics.checkParameterIsNotNull(str, "string");
            Intrinsics.checkParameterIsNotNull(charset, "charset");
            return encodeString(str, charset);
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to extension function", replaceWith = @ReplaceWith(expression = "string.encodeUtf8()", imports = {"okio.ByteString.Companion.encodeUtf8"}))
        /* renamed from: -deprecated_encodeUtf8 reason: not valid java name */
        public final ByteString m1120deprecated_encodeUtf8(String str) {
            Intrinsics.checkParameterIsNotNull(str, "string");
            return encodeUtf8(str);
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to extension function", replaceWith = @ReplaceWith(expression = "buffer.toByteString()", imports = {"okio.ByteString.Companion.toByteString"}))
        /* renamed from: -deprecated_of reason: not valid java name */
        public final ByteString m1121deprecated_of(ByteBuffer byteBuffer) {
            Intrinsics.checkParameterIsNotNull(byteBuffer, "buffer");
            return of(byteBuffer);
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to extension function", replaceWith = @ReplaceWith(expression = "array.toByteString(offset, byteCount)", imports = {"okio.ByteString.Companion.toByteString"}))
        /* renamed from: -deprecated_of reason: not valid java name */
        public final ByteString m1122deprecated_of(byte[] bArr, int i, int i2) {
            Intrinsics.checkParameterIsNotNull(bArr, "array");
            return of(bArr, i, i2);
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to extension function", replaceWith = @ReplaceWith(expression = "inputstream.readByteString(byteCount)", imports = {"okio.ByteString.Companion.readByteString"}))
        /* renamed from: -deprecated_read reason: not valid java name */
        public final ByteString m1123deprecated_read(InputStream inputStream, int i) {
            Intrinsics.checkParameterIsNotNull(inputStream, "inputstream");
            return read(inputStream, i);
        }
    }

    @JvmStatic
    public static final ByteString decodeBase64(String str) {
        return Companion.decodeBase64(str);
    }

    @JvmStatic
    public static final ByteString decodeHex(String str) {
        return Companion.decodeHex(str);
    }

    @JvmStatic
    public static final ByteString encodeString(String str, Charset charset) {
        return Companion.encodeString(str, charset);
    }

    @JvmStatic
    public static final ByteString encodeUtf8(String str) {
        return Companion.encodeUtf8(str);
    }

    @JvmStatic
    public static final ByteString of(ByteBuffer byteBuffer) {
        return Companion.of(byteBuffer);
    }

    @JvmStatic
    public static final ByteString of(byte... bArr) {
        return Companion.of(bArr);
    }

    @JvmStatic
    public static final ByteString of(byte[] bArr, int i, int i2) {
        return Companion.of(bArr, i, i2);
    }

    @JvmStatic
    public static final ByteString read(InputStream inputStream, int i) throws IOException {
        return Companion.read(inputStream, i);
    }

    public final int indexOf(ByteString byteString) {
        return indexOf$default(this, byteString, 0, 2, (Object) null);
    }

    public int indexOf(byte[] bArr) {
        return indexOf$default(this, bArr, 0, 2, (Object) null);
    }

    public final int lastIndexOf(ByteString byteString) {
        return lastIndexOf$default(this, byteString, 0, 2, (Object) null);
    }

    public int lastIndexOf(byte[] bArr) {
        return lastIndexOf$default(this, bArr, 0, 2, (Object) null);
    }

    public ByteString substring() {
        return substring$default(this, 0, 0, 3, null);
    }

    public ByteString substring(int i) {
        return substring$default(this, i, 0, 2, null);
    }

    public ByteString(byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(bArr, "data");
        this.data = bArr;
    }

    public final byte[] getData$jvm() {
        return this.data;
    }

    public final int getHashCode$jvm() {
        return this.hashCode;
    }

    public final void setHashCode$jvm(int i) {
        this.hashCode = i;
    }

    public final String getUtf8$jvm() {
        return this.utf8;
    }

    public final void setUtf8$jvm(String str) {
        this.utf8 = str;
    }

    public String utf8() {
        return ByteStringKt.commonUtf8(this);
    }

    public String string(Charset charset) {
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        return new String(this.data, charset);
    }

    public String base64() {
        return ByteStringKt.commonBase64(this);
    }

    public ByteString md5() {
        return digest$jvm("MD5");
    }

    public ByteString sha1() {
        return digest$jvm("SHA-1");
    }

    public ByteString sha256() {
        return digest$jvm("SHA-256");
    }

    public ByteString sha512() {
        return digest$jvm("SHA-512");
    }

    public ByteString digest$jvm(String str) {
        Intrinsics.checkParameterIsNotNull(str, "algorithm");
        byte[] digest = MessageDigest.getInstance(str).digest(this.data);
        Intrinsics.checkExpressionValueIsNotNull(digest, "MessageDigest.getInstance(algorithm).digest(data)");
        return new ByteString(digest);
    }

    public ByteString hmacSha1(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "key");
        return hmac$jvm("HmacSHA1", byteString);
    }

    public static /* bridge */ /* synthetic */ int indexOf$default(ByteString byteString, ByteString byteString2, int i, int i2, Object obj) {
        if (obj == null) {
            if ((i2 & 2) != 0) {
                i = 0;
            }
            return byteString.indexOf(byteString2, i);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: indexOf");
    }

    public ByteString hmacSha256(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "key");
        return hmac$jvm("HmacSHA256", byteString);
    }

    public static /* bridge */ /* synthetic */ int indexOf$default(ByteString byteString, byte[] bArr, int i, int i2, Object obj) {
        if (obj == null) {
            if ((i2 & 2) != 0) {
                i = 0;
            }
            return byteString.indexOf(bArr, i);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: indexOf");
    }

    public ByteString hmacSha512(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "key");
        return hmac$jvm("HmacSHA512", byteString);
    }

    public ByteString hmac$jvm(String str, ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(str, "algorithm");
        Intrinsics.checkParameterIsNotNull(byteString, "key");
        try {
            Mac instance = Mac.getInstance(str);
            instance.init(new SecretKeySpec(byteString.toByteArray(), str));
            byte[] doFinal = instance.doFinal(this.data);
            Intrinsics.checkExpressionValueIsNotNull(doFinal, "mac.doFinal(data)");
            return new ByteString(doFinal);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String base64Url() {
        return ByteStringKt.commonBase64Url(this);
    }

    public String hex() {
        return ByteStringKt.commonHex(this);
    }

    public ByteString toAsciiLowercase() {
        return ByteStringKt.commonToAsciiLowercase(this);
    }

    public ByteString toAsciiUppercase() {
        return ByteStringKt.commonToAsciiUppercase(this);
    }

    public static /* bridge */ /* synthetic */ ByteString substring$default(ByteString byteString, int i, int i2, int i3, Object obj) {
        if (obj == null) {
            if ((i3 & 1) != 0) {
                i = 0;
            }
            if ((i3 & 2) != 0) {
                i2 = byteString.size();
            }
            return byteString.substring(i, i2);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: substring");
    }

    public ByteString substring(int i, int i2) {
        return ByteStringKt.commonSubstring(this, i, i2);
    }

    public byte internalGet$jvm(int i) {
        return ByteStringKt.commonGetByte(this, i);
    }

    public final byte getByte(int i) {
        return internalGet$jvm(i);
    }

    public final int size() {
        return getSize$jvm();
    }

    public int getSize$jvm() {
        return ByteStringKt.commonGetSize(this);
    }

    public byte[] toByteArray() {
        return ByteStringKt.commonToByteArray(this);
    }

    public byte[] internalArray$jvm() {
        return ByteStringKt.commonInternalArray(this);
    }

    public ByteBuffer asByteBuffer() {
        ByteBuffer asReadOnlyBuffer = ByteBuffer.wrap(this.data).asReadOnlyBuffer();
        Intrinsics.checkExpressionValueIsNotNull(asReadOnlyBuffer, "ByteBuffer.wrap(data).asReadOnlyBuffer()");
        return asReadOnlyBuffer;
    }

    public void write(OutputStream outputStream) throws IOException {
        Intrinsics.checkParameterIsNotNull(outputStream, "out");
        outputStream.write(this.data);
    }

    public void write$jvm(Buffer buffer) {
        Intrinsics.checkParameterIsNotNull(buffer, "buffer");
        byte[] bArr = this.data;
        buffer.write(bArr, 0, bArr.length);
    }

    public boolean rangeEquals(int i, ByteString byteString, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(byteString, "other");
        return ByteStringKt.commonRangeEquals(this, i, byteString, i2, i3);
    }

    public boolean rangeEquals(int i, byte[] bArr, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(bArr, "other");
        return ByteStringKt.commonRangeEquals(this, i, bArr, i2, i3);
    }

    public final boolean startsWith(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "prefix");
        return ByteStringKt.commonStartsWith(this, byteString);
    }

    public final boolean startsWith(byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(bArr, "prefix");
        return ByteStringKt.commonStartsWith(this, bArr);
    }

    public final boolean endsWith(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "suffix");
        return ByteStringKt.commonEndsWith(this, byteString);
    }

    public final boolean endsWith(byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(bArr, "suffix");
        return ByteStringKt.commonEndsWith(this, bArr);
    }

    public final int indexOf(ByteString byteString, int i) {
        Intrinsics.checkParameterIsNotNull(byteString, "other");
        return indexOf(byteString.internalArray$jvm(), i);
    }

    public int indexOf(byte[] bArr, int i) {
        Intrinsics.checkParameterIsNotNull(bArr, "other");
        return ByteStringKt.commonIndexOf(this, bArr, i);
    }

    public static /* bridge */ /* synthetic */ int lastIndexOf$default(ByteString byteString, ByteString byteString2, int i, int i2, Object obj) {
        if (obj == null) {
            if ((i2 & 2) != 0) {
                i = byteString.size();
            }
            return byteString.lastIndexOf(byteString2, i);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: lastIndexOf");
    }

    public final int lastIndexOf(ByteString byteString, int i) {
        Intrinsics.checkParameterIsNotNull(byteString, "other");
        return lastIndexOf(byteString.internalArray$jvm(), i);
    }

    public static /* bridge */ /* synthetic */ int lastIndexOf$default(ByteString byteString, byte[] bArr, int i, int i2, Object obj) {
        if (obj == null) {
            if ((i2 & 2) != 0) {
                i = byteString.size();
            }
            return byteString.lastIndexOf(bArr, i);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: lastIndexOf");
    }

    public int lastIndexOf(byte[] bArr, int i) {
        Intrinsics.checkParameterIsNotNull(bArr, "other");
        return ByteStringKt.commonLastIndexOf(this, bArr, i);
    }

    public boolean equals(Object obj) {
        return ByteStringKt.commonEquals(this, obj);
    }

    public int hashCode() {
        return ByteStringKt.commonHashCode(this);
    }

    public int compareTo(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "other");
        return ByteStringKt.commonCompareTo(this, byteString);
    }

    public String toString() {
        return ByteStringKt.commonToString(this);
    }

    private final void readObject(ObjectInputStream objectInputStream) throws IOException {
        ByteString read = Companion.read(objectInputStream, objectInputStream.readInt());
        Field declaredField = ByteString.class.getDeclaredField("data");
        Intrinsics.checkExpressionValueIsNotNull(declaredField, "field");
        declaredField.setAccessible(true);
        declaredField.set(this, read.data);
    }

    private final void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeInt(this.data.length);
        objectOutputStream.write(this.data);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to operator function", replaceWith = @ReplaceWith(expression = "this[index]", imports = {}))
    /* renamed from: -deprecated_getByte reason: not valid java name */
    public final byte m1115deprecated_getByte(int i) {
        return getByte(i);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "size", imports = {}))
    /* renamed from: -deprecated_size reason: not valid java name */
    public final int m1116deprecated_size() {
        return size();
    }
}
