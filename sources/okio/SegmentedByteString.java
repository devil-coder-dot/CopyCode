package okio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0010\u0005\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u0000 J2\u00020\u0001:\u0001JB\u001d\b\u0002\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\r\u001a\n \u000f*\u0004\u0018\u00010\u000e0\u000eH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\u0011H\u0016J\u0015\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u0011H\u0010¢\u0006\u0002\b\u0015J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0002JV\u0010\u001a\u001a\u00020\u001b2K\u0010\u001c\u001aG\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b( \u0012\u0013\u0012\u00110!¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b(\"\u0012\u0013\u0012\u00110!¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b(#\u0012\u0004\u0012\u00020\u001b0\u001dH\bJf\u0010\u001a\u001a\u00020\u001b2\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020!2K\u0010\u001c\u001aG\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b( \u0012\u0013\u0012\u00110!¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b(\"\u0012\u0013\u0012\u00110!¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b(#\u0012\u0004\u0012\u00020\u001b0\u001dH\bJ\r\u0010&\u001a\u00020!H\u0010¢\u0006\u0002\b'J\b\u0010(\u001a\u00020!H\u0016J\b\u0010)\u001a\u00020\u0011H\u0016J\u001d\u0010*\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u00112\u0006\u0010+\u001a\u00020\u0001H\u0010¢\u0006\u0002\b,J\u0018\u0010-\u001a\u00020!2\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010.\u001a\u00020!H\u0016J\r\u0010/\u001a\u00020\u0004H\u0010¢\u0006\u0002\b0J\u0015\u00101\u001a\u0002022\u0006\u00103\u001a\u00020!H\u0010¢\u0006\u0002\b4J\u0018\u00105\u001a\u00020!2\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010.\u001a\u00020!H\u0016J(\u00106\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020!2\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u00107\u001a\u00020!2\u0006\u0010#\u001a\u00020!H\u0016J(\u00106\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020!2\u0006\u0010\u0018\u001a\u00020\u00012\u0006\u00107\u001a\u00020!2\u0006\u0010#\u001a\u00020!H\u0016J\u0010\u00108\u001a\u00020!2\u0006\u00103\u001a\u00020!H\u0002J\u0010\u00109\u001a\u00020\u00112\u0006\u0010:\u001a\u00020;H\u0016J\u0018\u0010<\u001a\u00020\u00012\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020!H\u0016J\b\u0010=\u001a\u00020\u0001H\u0016J\b\u0010>\u001a\u00020\u0001H\u0016J\b\u0010?\u001a\u00020\u0004H\u0016J\b\u0010@\u001a\u00020\u0001H\u0002J\b\u0010A\u001a\u00020\u0011H\u0016J\u0010\u0010B\u001a\u00020\u001b2\u0006\u0010C\u001a\u00020DH\u0016J\u0015\u0010B\u001a\u00020\u001b2\u0006\u0010E\u001a\u00020FH\u0010¢\u0006\u0002\bGJ\b\u0010H\u001a\u00020IH\u0002R\u0016\u0010\u0005\u001a\u00020\u00068\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00038\u0006X\u0004¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000b¨\u0006K"}, d2 = {"Lokio/SegmentedByteString;", "Lokio/ByteString;", "segments", "", "", "directory", "", "([[B[I)V", "getDirectory", "()[I", "getSegments", "()[[B", "[[B", "asByteBuffer", "Ljava/nio/ByteBuffer;", "kotlin.jvm.PlatformType", "base64", "", "base64Url", "digest", "algorithm", "digest$jvm", "equals", "", "other", "", "forEachSegment", "", "action", "Lkotlin/Function3;", "Lkotlin/ParameterName;", "name", "data", "", "offset", "byteCount", "beginIndex", "endIndex", "getSize", "getSize$jvm", "hashCode", "hex", "hmac", "key", "hmac$jvm", "indexOf", "fromIndex", "internalArray", "internalArray$jvm", "internalGet", "", "pos", "internalGet$jvm", "lastIndexOf", "rangeEquals", "otherOffset", "segment", "string", "charset", "Ljava/nio/charset/Charset;", "substring", "toAsciiLowercase", "toAsciiUppercase", "toByteArray", "toByteString", "toString", "write", "out", "Ljava/io/OutputStream;", "buffer", "Lokio/Buffer;", "write$jvm", "writeReplace", "Ljava/lang/Object;", "Companion", "jvm"}, k = 1, mv = {1, 1, 11})
/* compiled from: SegmentedByteString.kt */
public final class SegmentedByteString extends ByteString {
    public static final Companion Companion = new Companion(null);
    private final transient int[] directory;
    private final transient byte[][] segments;

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b¨\u0006\t"}, d2 = {"Lokio/SegmentedByteString$Companion;", "", "()V", "of", "Lokio/ByteString;", "buffer", "Lokio/Buffer;", "byteCount", "", "jvm"}, k = 1, mv = {1, 1, 11})
    /* compiled from: SegmentedByteString.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final ByteString of(Buffer buffer, int i) {
            Intrinsics.checkParameterIsNotNull(buffer, "buffer");
            Util.checkOffsetAndCount(buffer.size(), 0, (long) i);
            Segment segment = buffer.head;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            while (i3 < i) {
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                if (segment.limit != segment.pos) {
                    i3 += segment.limit - segment.pos;
                    i4++;
                    segment = segment.next;
                } else {
                    throw new AssertionError("s.limit == s.pos");
                }
            }
            byte[][] bArr = new byte[i4][];
            int[] iArr = new int[(i4 * 2)];
            Segment segment2 = buffer.head;
            int i5 = 0;
            while (i2 < i) {
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
                bArr[i5] = segment2.data;
                i2 += segment2.limit - segment2.pos;
                iArr[i5] = Math.min(i2, i);
                iArr[((Object[]) bArr).length + i5] = segment2.pos;
                segment2.shared = true;
                i5++;
                segment2 = segment2.next;
            }
            return new SegmentedByteString(bArr, iArr, null);
        }
    }

    public /* synthetic */ SegmentedByteString(byte[][] bArr, int[] iArr, DefaultConstructorMarker defaultConstructorMarker) {
        this(bArr, iArr);
    }

    public final byte[][] getSegments() {
        return this.segments;
    }

    public final int[] getDirectory() {
        return this.directory;
    }

    private SegmentedByteString(byte[][] bArr, int[] iArr) {
        super(ByteString.EMPTY.getData$jvm());
        this.segments = bArr;
        this.directory = iArr;
    }

    public String string(Charset charset) {
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        return toByteString().string(charset);
    }

    public String base64() {
        return toByteString().base64();
    }

    public String hex() {
        return toByteString().hex();
    }

    public ByteString toAsciiLowercase() {
        return toByteString().toAsciiLowercase();
    }

    public ByteString toAsciiUppercase() {
        return toByteString().toAsciiUppercase();
    }

    public ByteString digest$jvm(String str) {
        Intrinsics.checkParameterIsNotNull(str, "algorithm");
        MessageDigest instance = MessageDigest.getInstance(str);
        int length = ((Object[]) getSegments()).length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int i3 = getDirectory()[length + i];
            int i4 = getDirectory()[i];
            instance.update(getSegments()[i], i3, i4 - i2);
            i++;
            i2 = i4;
        }
        byte[] digest = instance.digest();
        Intrinsics.checkExpressionValueIsNotNull(digest, "digest.digest()");
        return new ByteString(digest);
    }

    public ByteString hmac$jvm(String str, ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(str, "algorithm");
        Intrinsics.checkParameterIsNotNull(byteString, "key");
        try {
            Mac instance = Mac.getInstance(str);
            instance.init(new SecretKeySpec(byteString.toByteArray(), str));
            int length = ((Object[]) getSegments()).length;
            int i = 0;
            int i2 = 0;
            while (i < length) {
                int i3 = getDirectory()[length + i];
                int i4 = getDirectory()[i];
                instance.update(getSegments()[i], i3, i4 - i2);
                i++;
                i2 = i4;
            }
            byte[] doFinal = instance.doFinal();
            Intrinsics.checkExpressionValueIsNotNull(doFinal, "mac.doFinal()");
            return new ByteString(doFinal);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String base64Url() {
        return toByteString().base64Url();
    }

    public ByteString substring(int i, int i2) {
        int i3 = 0;
        if (i >= 0) {
            String str = "endIndex=";
            if (i2 <= size()) {
                int i4 = i2 - i;
                if (!(i4 >= 0)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(i2);
                    sb.append(" < beginIndex=");
                    sb.append(i);
                    throw new IllegalArgumentException(sb.toString().toString());
                } else if (i == 0 && i2 == size()) {
                    return this;
                } else {
                    if (i == i2) {
                        return ByteString.EMPTY;
                    }
                    int segment = segment(i);
                    int segment2 = segment(i2 - 1);
                    Object[] copyOfRange = Arrays.copyOfRange((Object[]) this.segments, segment, segment2 + 1);
                    Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
                    byte[][] bArr = (byte[][]) copyOfRange;
                    Object[] objArr = (Object[]) bArr;
                    int[] iArr = new int[(objArr.length * 2)];
                    if (segment <= segment2) {
                        int i5 = segment;
                        int i6 = 0;
                        while (true) {
                            iArr[i6] = Math.min(this.directory[i5] - i, i4);
                            int i7 = i6 + 1;
                            iArr[i6 + objArr.length] = this.directory[((Object[]) this.segments).length + i5];
                            if (i5 == segment2) {
                                break;
                            }
                            i5++;
                            i6 = i7;
                        }
                    }
                    if (segment != 0) {
                        i3 = this.directory[segment - 1];
                    }
                    int length = objArr.length;
                    iArr[length] = iArr[length] + (i - i3);
                    return new SegmentedByteString(bArr, iArr);
                }
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(i2);
                sb2.append(" > length(");
                sb2.append(size());
                sb2.append(')');
                throw new IllegalArgumentException(sb2.toString().toString());
            }
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("beginIndex=");
            sb3.append(i);
            sb3.append(" < 0");
            throw new IllegalArgumentException(sb3.toString().toString());
        }
    }

    public byte internalGet$jvm(int i) {
        int i2;
        Util.checkOffsetAndCount((long) this.directory[((Object[]) this.segments).length - 1], (long) i, 1);
        int segment = segment(i);
        if (segment == 0) {
            i2 = 0;
        } else {
            i2 = this.directory[segment - 1];
        }
        int[] iArr = this.directory;
        byte[][] bArr = this.segments;
        return bArr[segment][(i - i2) + iArr[((Object[]) bArr).length + segment]];
    }

    /* access modifiers changed from: private */
    public final int segment(int i) {
        int binarySearch = Arrays.binarySearch(this.directory, 0, ((Object[]) this.segments).length, i + 1);
        return binarySearch >= 0 ? binarySearch : ~binarySearch;
    }

    public int getSize$jvm() {
        return this.directory[((Object[]) this.segments).length - 1];
    }

    public byte[] toByteArray() {
        byte[] bArr = new byte[size()];
        int length = ((Object[]) getSegments()).length;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i < length) {
            int i4 = getDirectory()[length + i];
            int i5 = getDirectory()[i];
            int i6 = i5 - i2;
            Platform.arraycopy(getSegments()[i], i4, bArr, i3, i6);
            i3 += i6;
            i++;
            i2 = i5;
        }
        return bArr;
    }

    public ByteBuffer asByteBuffer() {
        return ByteBuffer.wrap(toByteArray()).asReadOnlyBuffer();
    }

    public boolean rangeEquals(int i, ByteString byteString, int i2, int i3) {
        int i4;
        Intrinsics.checkParameterIsNotNull(byteString, "other");
        if (i < 0 || i > size() - i3) {
            return false;
        }
        int i5 = i3 + i;
        int access$segment = segment(i);
        while (i < i5) {
            if (access$segment == 0) {
                i4 = 0;
            } else {
                i4 = getDirectory()[access$segment - 1];
            }
            int i6 = getDirectory()[access$segment] - i4;
            int min = Math.min(i5, i6 + i4) - i;
            if (!byteString.rangeEquals(i2, getSegments()[access$segment], getDirectory()[((Object[]) getSegments()).length + access$segment] + (i - i4), min)) {
                return false;
            }
            i2 += min;
            i += min;
            access$segment++;
        }
        return true;
    }

    public boolean rangeEquals(int i, byte[] bArr, int i2, int i3) {
        int i4;
        Intrinsics.checkParameterIsNotNull(bArr, "other");
        if (i < 0 || i > size() - i3 || i2 < 0 || i2 > bArr.length - i3) {
            return false;
        }
        int i5 = i3 + i;
        int access$segment = segment(i);
        while (i < i5) {
            if (access$segment == 0) {
                i4 = 0;
            } else {
                i4 = getDirectory()[access$segment - 1];
            }
            int i6 = getDirectory()[access$segment] - i4;
            int min = Math.min(i5, i6 + i4) - i;
            if (!Util.arrayRangeEquals(getSegments()[access$segment], getDirectory()[((Object[]) getSegments()).length + access$segment] + (i - i4), bArr, i2, min)) {
                return false;
            }
            i2 += min;
            i += min;
            access$segment++;
        }
        return true;
    }

    public int indexOf(byte[] bArr, int i) {
        Intrinsics.checkParameterIsNotNull(bArr, "other");
        return toByteString().indexOf(bArr, i);
    }

    public int lastIndexOf(byte[] bArr, int i) {
        Intrinsics.checkParameterIsNotNull(bArr, "other");
        return toByteString().lastIndexOf(bArr, i);
    }

    private final ByteString toByteString() {
        return new ByteString(toByteArray());
    }

    public byte[] internalArray$jvm() {
        return toByteArray();
    }

    private final void forEachSegment(Function3<? super byte[], ? super Integer, ? super Integer, Unit> function3) {
        int length = ((Object[]) getSegments()).length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int i3 = getDirectory()[length + i];
            int i4 = getDirectory()[i];
            function3.invoke(getSegments()[i], Integer.valueOf(i3), Integer.valueOf(i4 - i2));
            i++;
            i2 = i4;
        }
    }

    private final void forEachSegment(int i, int i2, Function3<? super byte[], ? super Integer, ? super Integer, Unit> function3) {
        int i3;
        int access$segment = segment(i);
        while (i < i2) {
            if (access$segment == 0) {
                i3 = 0;
            } else {
                i3 = getDirectory()[access$segment - 1];
            }
            int i4 = getDirectory()[access$segment] - i3;
            int min = Math.min(i2, i4 + i3) - i;
            function3.invoke(getSegments()[access$segment], Integer.valueOf(getDirectory()[((Object[]) getSegments()).length + access$segment] + (i - i3)), Integer.valueOf(min));
            i += min;
            access$segment++;
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ByteString) {
            ByteString byteString = (ByteString) obj;
            if (byteString.size() == size() && rangeEquals(0, byteString, 0, size())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int hashCode$jvm = getHashCode$jvm();
        if (hashCode$jvm != 0) {
            return hashCode$jvm;
        }
        int length = ((Object[]) getSegments()).length;
        int i = 0;
        int i2 = 0;
        int i3 = 1;
        while (i < length) {
            int i4 = getDirectory()[length + i];
            int i5 = getDirectory()[i];
            byte[] bArr = getSegments()[i];
            int i6 = (i5 - i2) + i4;
            while (i4 < i6) {
                i3 = (i3 * 31) + bArr[i4];
                i4++;
            }
            i++;
            i2 = i5;
        }
        setHashCode$jvm(i3);
        return i3;
    }

    public String toString() {
        return toByteString().toString();
    }

    private final Object writeReplace() {
        ByteString byteString = toByteString();
        if (byteString != null) {
            return byteString;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
    }

    public void write(OutputStream outputStream) throws IOException {
        Intrinsics.checkParameterIsNotNull(outputStream, "out");
        int length = ((Object[]) getSegments()).length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int i3 = getDirectory()[length + i];
            int i4 = getDirectory()[i];
            outputStream.write(getSegments()[i], i3, i4 - i2);
            i++;
            i2 = i4;
        }
    }

    public void write$jvm(Buffer buffer) {
        Intrinsics.checkParameterIsNotNull(buffer, "buffer");
        int length = ((Object[]) getSegments()).length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int i3 = getDirectory()[length + i];
            int i4 = getDirectory()[i];
            Segment segment = new Segment(getSegments()[i], i3, i3 + (i4 - i2), true, false);
            if (buffer.head == null) {
                segment.prev = segment;
                segment.next = segment.prev;
                buffer.head = segment.next;
            } else {
                Segment segment2 = buffer.head;
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
                Segment segment3 = segment2.prev;
                if (segment3 == null) {
                    Intrinsics.throwNpe();
                }
                segment3.push(segment);
            }
            i++;
            i2 = i4;
        }
        buffer.setSize$jvm(buffer.size() + ((long) size()));
    }
}
