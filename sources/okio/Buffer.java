package okio;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.TypeCastException;
import kotlin.UByte;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.CharCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.LongCompanionObject;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.Charsets;
import kotlin.text.Typography;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000¶\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u001a\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0018\u0018\u0000 \u00012\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004:\u0004\u0001\u0001B\u0005¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0000H\u0016J\u0006\u0010\u0011\u001a\u00020\u0012J\b\u0010\u0013\u001a\u00020\u0000H\u0016J\b\u0010\u0014\u001a\u00020\u0012H\u0016J\u0006\u0010\u0015\u001a\u00020\fJ$\u0010\u0016\u001a\u00020\u00002\u0006\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u0019\u001a\u00020\f2\b\b\u0002\u0010\u001a\u001a\u00020\fH\u0007J\"\u0010\u0016\u001a\u00020\u00002\u0006\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0019\u001a\u00020\f2\b\b\u0002\u0010\u001a\u001a\u00020\fJ\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0002J\b\u0010\u001f\u001a\u00020\u0000H\u0016J\b\u0010 \u001a\u00020\u0000H\u0016J\u0013\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010$H\u0002J\b\u0010%\u001a\u00020\"H\u0016J\b\u0010&\u001a\u00020\u0012H\u0016J\u0016\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020\fH\u0002¢\u0006\u0002\b*J\u0015\u0010*\u001a\u00020(2\u0006\u0010+\u001a\u00020\fH\u0007¢\u0006\u0002\b,J\b\u0010-\u001a\u00020.H\u0016J\u0018\u0010/\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u00100\u001a\u00020\u001cH\u0002J\u000e\u00101\u001a\u00020\u001c2\u0006\u00100\u001a\u00020\u001cJ\u000e\u00102\u001a\u00020\u001c2\u0006\u00100\u001a\u00020\u001cJ\u000e\u00103\u001a\u00020\u001c2\u0006\u00100\u001a\u00020\u001cJ\u0010\u00104\u001a\u00020\f2\u0006\u00105\u001a\u00020(H\u0016J\u0018\u00104\u001a\u00020\f2\u0006\u00105\u001a\u00020(2\u0006\u00106\u001a\u00020\fH\u0016J \u00104\u001a\u00020\f2\u0006\u00105\u001a\u00020(2\u0006\u00106\u001a\u00020\f2\u0006\u00107\u001a\u00020\fH\u0016J\u0010\u00104\u001a\u00020\f2\u0006\u00108\u001a\u00020\u001cH\u0016J\u0018\u00104\u001a\u00020\f2\u0006\u00108\u001a\u00020\u001c2\u0006\u00106\u001a\u00020\fH\u0016J\u0010\u00109\u001a\u00020\f2\u0006\u0010:\u001a\u00020\u001cH\u0016J\u0018\u00109\u001a\u00020\f2\u0006\u0010:\u001a\u00020\u001c2\u0006\u00106\u001a\u00020\fH\u0016J\b\u0010;\u001a\u00020<H\u0016J\b\u0010=\u001a\u00020\"H\u0016J\u0006\u0010>\u001a\u00020\u001cJ\b\u0010?\u001a\u00020\u0018H\u0016J\b\u0010@\u001a\u00020\u0001H\u0016J\u0018\u0010A\u001a\u00020\"2\u0006\u0010\u0019\u001a\u00020\f2\u0006\u00108\u001a\u00020\u001cH\u0016J(\u0010A\u001a\u00020\"2\u0006\u0010\u0019\u001a\u00020\f2\u0006\u00108\u001a\u00020\u001c2\u0006\u0010B\u001a\u00020.2\u0006\u0010\u001a\u001a\u00020.H\u0016J0\u0010A\u001a\u00020\"2\u0006\u0010C\u001a\u00020\n2\u0006\u0010D\u001a\u00020.2\u0006\u00108\u001a\u00020E2\u0006\u0010B\u001a\u00020.2\u0006\u0010F\u001a\u00020.H\u0002J\u0010\u0010G\u001a\u00020.2\u0006\u0010H\u001a\u00020IH\u0016J\u0010\u0010G\u001a\u00020.2\u0006\u0010H\u001a\u00020EH\u0016J \u0010G\u001a\u00020.2\u0006\u0010H\u001a\u00020E2\u0006\u0010\u0019\u001a\u00020.2\u0006\u0010\u001a\u001a\u00020.H\u0016J\u0018\u0010G\u001a\u00020\f2\u0006\u0010H\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\fH\u0016J\u0010\u0010J\u001a\u00020\f2\u0006\u0010H\u001a\u00020KH\u0016J\u0012\u0010L\u001a\u00020M2\b\b\u0002\u0010N\u001a\u00020MH\u0007J\b\u0010O\u001a\u00020(H\u0016J\b\u0010P\u001a\u00020EH\u0016J\u0010\u0010P\u001a\u00020E2\u0006\u0010\u001a\u001a\u00020\fH\u0016J\b\u0010Q\u001a\u00020\u001cH\u0016J\u0010\u0010Q\u001a\u00020\u001c2\u0006\u0010\u001a\u001a\u00020\fH\u0016J\b\u0010R\u001a\u00020\fH\u0016J\u000e\u0010S\u001a\u00020\u00002\u0006\u0010T\u001a\u00020<J\u0016\u0010S\u001a\u00020\u00002\u0006\u0010T\u001a\u00020<2\u0006\u0010\u001a\u001a\u00020\fJ \u0010S\u001a\u00020\u00122\u0006\u0010T\u001a\u00020<2\u0006\u0010\u001a\u001a\u00020\f2\u0006\u0010U\u001a\u00020\"H\u0002J\u0010\u0010V\u001a\u00020\u00122\u0006\u0010H\u001a\u00020EH\u0016J\u0018\u0010V\u001a\u00020\u00122\u0006\u0010H\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\fH\u0016J\b\u0010W\u001a\u00020\fH\u0016J\b\u0010X\u001a\u00020.H\u0016J\b\u0010Y\u001a\u00020.H\u0016J\b\u0010Z\u001a\u00020\fH\u0016J\b\u0010[\u001a\u00020\fH\u0016J\b\u0010\\\u001a\u00020]H\u0016J\b\u0010^\u001a\u00020]H\u0016J\u0010\u0010_\u001a\u00020\u001e2\u0006\u0010`\u001a\u00020aH\u0016J\u0018\u0010_\u001a\u00020\u001e2\u0006\u0010\u001a\u001a\u00020\f2\u0006\u0010`\u001a\u00020aH\u0016J\u0012\u0010b\u001a\u00020M2\b\b\u0002\u0010N\u001a\u00020MH\u0007J\b\u0010c\u001a\u00020\u001eH\u0016J\u0010\u0010c\u001a\u00020\u001e2\u0006\u0010\u001a\u001a\u00020\fH\u0016J\b\u0010d\u001a\u00020.H\u0016J\n\u0010e\u001a\u0004\u0018\u00010\u001eH\u0016J\u0015\u0010e\u001a\u00020\u001e2\u0006\u0010f\u001a\u00020\fH\u0000¢\u0006\u0002\bgJ\b\u0010h\u001a\u00020\u001eH\u0016J\u0010\u0010h\u001a\u00020\u001e2\u0006\u0010i\u001a\u00020\fH\u0016J\u0010\u0010j\u001a\u00020\"2\u0006\u0010\u001a\u001a\u00020\fH\u0016J\u0010\u0010k\u001a\u00020\u00122\u0006\u0010\u001a\u001a\u00020\fH\u0016J8\u0010l\u001a\u0002Hm\"\u0004\b\u0000\u0010m2\u0006\u00106\u001a\u00020\f2\u001a\u0010n\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u00010\n\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u0002Hm0oH\b¢\u0006\u0002\u0010pJ\u0010\u0010q\u001a\u00020.2\u0006\u0010r\u001a\u00020sH\u0016J\u001f\u0010t\u001a\u00020.2\u0006\u0010r\u001a\u00020s2\b\b\u0002\u0010u\u001a\u00020\"H\u0000¢\u0006\u0002\bvJ\u0006\u0010w\u001a\u00020\u001cJ\u0006\u0010x\u001a\u00020\u001cJ\u0006\u0010y\u001a\u00020\u001cJ\r\u0010\r\u001a\u00020\fH\u0007¢\u0006\u0002\bzJ\u0010\u0010{\u001a\u00020\u00122\u0006\u0010\u001a\u001a\u00020\fH\u0016J\u0006\u0010|\u001a\u00020\u001cJ\u000e\u0010|\u001a\u00020\u001c2\u0006\u0010\u001a\u001a\u00020.J\b\u0010}\u001a\u00020~H\u0016J\b\u0010\u001a\u00020\u001eH\u0016J\u0018\u0010\u0001\u001a\u00020\n2\u0007\u0010\u0001\u001a\u00020.H\u0000¢\u0006\u0003\b\u0001J\u0012\u0010\u0001\u001a\u00020.2\u0007\u0010\u0001\u001a\u00020IH\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020EH\u0016J\"\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020E2\u0006\u0010\u0019\u001a\u00020.2\u0006\u0010\u001a\u001a\u00020.H\u0016J\u001a\u0010\u0001\u001a\u00020\u00122\u0007\u0010\u0001\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\fH\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020\u001cH\u0016J\u001b\u0010\u0001\u001a\u00020\u00022\b\u0010\u0001\u001a\u00030\u00012\u0006\u0010\u001a\u001a\u00020\fH\u0016J\u0013\u0010\u0001\u001a\u00020\f2\b\u0010\u0001\u001a\u00030\u0001H\u0016J\u0011\u0010\u0001\u001a\u00020\u00002\u0006\u00105\u001a\u00020.H\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020\fH\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020\fH\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020.H\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020.H\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020\fH\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020\fH\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020.H\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020.H\u0016J\u001a\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020\u001e2\u0006\u0010`\u001a\u00020aH\u0016J,\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020\u001e2\u0007\u0010\u0001\u001a\u00020.2\u0007\u0010\u0001\u001a\u00020.2\u0006\u0010`\u001a\u00020aH\u0016J\u001b\u0010\u0001\u001a\u00020\u00002\u0006\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u001a\u001a\u00020\fH\u0007J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020\u001eH\u0016J$\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020\u001e2\u0007\u0010\u0001\u001a\u00020.2\u0007\u0010\u0001\u001a\u00020.H\u0016J\u0012\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020.H\u0016R\u0014\u0010\u0006\u001a\u00020\u00008VX\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u0004\u0018\u00010\n8\u0000@\u0000X\u000e¢\u0006\u0002\n\u0000R&\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f8\u0007@@X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010¨\u0006\u0001"}, d2 = {"Lokio/Buffer;", "Lokio/BufferedSource;", "Lokio/BufferedSink;", "", "Ljava/nio/channels/ByteChannel;", "()V", "buffer", "getBuffer", "()Lokio/Buffer;", "head", "Lokio/Segment;", "<set-?>", "", "size", "()J", "setSize$jvm", "(J)V", "clear", "", "clone", "close", "completeSegmentByteCount", "copyTo", "out", "Ljava/io/OutputStream;", "offset", "byteCount", "digest", "Lokio/ByteString;", "algorithm", "", "emit", "emitCompleteSegments", "equals", "", "other", "", "exhausted", "flush", "get", "", "pos", "getByte", "index", "-deprecated_getByte", "hashCode", "", "hmac", "key", "hmacSha1", "hmacSha256", "hmacSha512", "indexOf", "b", "fromIndex", "toIndex", "bytes", "indexOfElement", "targetBytes", "inputStream", "Ljava/io/InputStream;", "isOpen", "md5", "outputStream", "peek", "rangeEquals", "bytesOffset", "segment", "segmentPos", "", "bytesLimit", "read", "sink", "Ljava/nio/ByteBuffer;", "readAll", "Lokio/Sink;", "readAndWriteUnsafe", "Lokio/Buffer$UnsafeCursor;", "unsafeCursor", "readByte", "readByteArray", "readByteString", "readDecimalLong", "readFrom", "input", "forever", "readFully", "readHexadecimalUnsignedLong", "readInt", "readIntLe", "readLong", "readLongLe", "readShort", "", "readShortLe", "readString", "charset", "Ljava/nio/charset/Charset;", "readUnsafe", "readUtf8", "readUtf8CodePoint", "readUtf8Line", "newline", "readUtf8Line$jvm", "readUtf8LineStrict", "limit", "request", "require", "seek", "T", "lambda", "Lkotlin/Function2;", "(JLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "select", "options", "Lokio/Options;", "selectPrefix", "selectTruncated", "selectPrefix$jvm", "sha1", "sha256", "sha512", "-deprecated_size", "skip", "snapshot", "timeout", "Lokio/Timeout;", "toString", "writableSegment", "minimumCapacity", "writableSegment$jvm", "write", "source", "byteString", "Lokio/Source;", "writeAll", "writeByte", "writeDecimalLong", "v", "writeHexadecimalUnsignedLong", "writeInt", "i", "writeIntLe", "writeLong", "writeLongLe", "writeShort", "s", "writeShortLe", "writeString", "string", "beginIndex", "endIndex", "writeTo", "writeUtf8", "writeUtf8CodePoint", "codePoint", "Companion", "UnsafeCursor", "jvm"}, k = 1, mv = {1, 1, 11})
/* compiled from: Buffer.kt */
public final class Buffer implements BufferedSource, BufferedSink, Cloneable, ByteChannel {
    public static final Companion Companion = new Companion(null);
    private static final byte[] DIGITS;
    public Segment head;
    private long size;

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lokio/Buffer$Companion;", "", "()V", "DIGITS", "", "jvm"}, k = 1, mv = {1, 1, 11})
    /* compiled from: Buffer.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u000e\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\bJ\u0006\u0010\u0014\u001a\u00020\bJ\u000e\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\nJ\u000e\u0010\u0017\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u00020\b8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\t\u001a\u00020\n8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u00020\f8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000f\u001a\u00020\b8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lokio/Buffer$UnsafeCursor;", "Ljava/io/Closeable;", "()V", "buffer", "Lokio/Buffer;", "data", "", "end", "", "offset", "", "readWrite", "", "segment", "Lokio/Segment;", "start", "close", "", "expandBuffer", "minByteCount", "next", "resizeBuffer", "newSize", "seek", "jvm"}, k = 1, mv = {1, 1, 11})
    /* compiled from: Buffer.kt */
    public static final class UnsafeCursor implements Closeable {
        public Buffer buffer;
        public byte[] data;
        public int end = -1;
        public long offset = -1;
        public boolean readWrite;
        private Segment segment;
        public int start = -1;

        public final int next() {
            long j = this.offset;
            Buffer buffer2 = this.buffer;
            if (buffer2 == null) {
                Intrinsics.throwNpe();
            }
            if (j != buffer2.size()) {
                long j2 = this.offset;
                return seek(j2 == -1 ? 0 : j2 + ((long) (this.end - this.start)));
            }
            throw new IllegalStateException("no more bytes".toString());
        }

        public final int seek(long j) {
            Buffer buffer2 = this.buffer;
            if (buffer2 == null) {
                throw new IllegalStateException("not attached to a buffer".toString());
            } else if (j < ((long) -1) || j > buffer2.size()) {
                StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
                String str = "offset=%s > size=%s";
                String format = String.format(str, Arrays.copyOf(new Object[]{Long.valueOf(j), Long.valueOf(buffer2.size())}, 2));
                Intrinsics.checkExpressionValueIsNotNull(format, "java.lang.String.format(format, *args)");
                throw new ArrayIndexOutOfBoundsException(format);
            } else if (j == -1 || j == buffer2.size()) {
                this.segment = null;
                this.offset = j;
                this.data = null;
                this.start = -1;
                this.end = -1;
                return -1;
            } else {
                long j2 = 0;
                long size = buffer2.size();
                Segment segment2 = buffer2.head;
                Segment segment3 = buffer2.head;
                Segment segment4 = this.segment;
                if (segment4 != null) {
                    long j3 = this.offset;
                    int i = this.start;
                    if (segment4 == null) {
                        Intrinsics.throwNpe();
                    }
                    long j4 = j3 - ((long) (i - segment4.pos));
                    if (j4 > j) {
                        segment3 = this.segment;
                        size = j4;
                    } else {
                        segment2 = this.segment;
                        j2 = j4;
                    }
                }
                if (size - j > j - j2) {
                    while (true) {
                        if (segment2 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (j < ((long) (segment2.limit - segment2.pos)) + j2) {
                            break;
                        }
                        j2 += (long) (segment2.limit - segment2.pos);
                        segment2 = segment2.next;
                    }
                } else {
                    while (size > j) {
                        if (segment3 == null) {
                            Intrinsics.throwNpe();
                        }
                        segment3 = segment3.prev;
                        if (segment3 == null) {
                            Intrinsics.throwNpe();
                        }
                        size -= (long) (segment3.limit - segment3.pos);
                    }
                    j2 = size;
                    segment2 = segment3;
                }
                if (this.readWrite) {
                    if (segment2 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (segment2.shared) {
                        Segment unsharedCopy = segment2.unsharedCopy();
                        if (buffer2.head == segment2) {
                            buffer2.head = unsharedCopy;
                        }
                        segment2 = segment2.push(unsharedCopy);
                        Segment segment5 = segment2.prev;
                        if (segment5 == null) {
                            Intrinsics.throwNpe();
                        }
                        segment5.pop();
                    }
                }
                this.segment = segment2;
                this.offset = j;
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
                this.data = segment2.data;
                this.start = segment2.pos + ((int) (j - j2));
                int i2 = segment2.limit;
                this.end = i2;
                return i2 - this.start;
            }
        }

        public final long resizeBuffer(long j) {
            long j2 = j;
            Buffer buffer2 = this.buffer;
            if (buffer2 == null) {
                throw new IllegalStateException("not attached to a buffer".toString());
            } else if (this.readWrite) {
                long size = buffer2.size();
                int i = 1;
                int i2 = (j2 > size ? 1 : (j2 == size ? 0 : -1));
                if (i2 <= 0) {
                    if (j2 >= 0) {
                        long j3 = size - j2;
                        while (true) {
                            if (j3 <= 0) {
                                break;
                            }
                            Segment segment2 = buffer2.head;
                            if (segment2 == null) {
                                Intrinsics.throwNpe();
                            }
                            Segment segment3 = segment2.prev;
                            if (segment3 == null) {
                                Intrinsics.throwNpe();
                            }
                            long j4 = (long) (segment3.limit - segment3.pos);
                            if (j4 > j3) {
                                segment3.limit -= (int) j3;
                                break;
                            }
                            buffer2.head = segment3.pop();
                            SegmentPool.recycle(segment3);
                            j3 -= j4;
                        }
                        this.segment = null;
                        this.offset = j2;
                        this.data = null;
                        this.start = -1;
                        this.end = -1;
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("newSize < 0: ");
                        sb.append(j2);
                        throw new IllegalArgumentException(sb.toString().toString());
                    }
                } else if (i2 > 0) {
                    long j5 = j2 - size;
                    boolean z = true;
                    while (j5 > 0) {
                        Segment writableSegment$jvm = buffer2.writableSegment$jvm(i);
                        int min = (int) Math.min(j5, (long) (8192 - writableSegment$jvm.limit));
                        writableSegment$jvm.limit += min;
                        j5 -= (long) min;
                        if (z) {
                            this.segment = writableSegment$jvm;
                            this.offset = size;
                            this.data = writableSegment$jvm.data;
                            this.start = writableSegment$jvm.limit - min;
                            this.end = writableSegment$jvm.limit;
                            z = false;
                        }
                        i = 1;
                    }
                }
                buffer2.setSize$jvm(j2);
                return size;
            } else {
                throw new IllegalStateException("resizeBuffer() only permitted for read/write buffers".toString());
            }
        }

        public final long expandBuffer(int i) {
            boolean z = true;
            if (i > 0) {
                if (i > 8192) {
                    z = false;
                }
                if (z) {
                    Buffer buffer2 = this.buffer;
                    if (buffer2 == null) {
                        throw new IllegalStateException("not attached to a buffer".toString());
                    } else if (this.readWrite) {
                        long size = buffer2.size();
                        Segment writableSegment$jvm = buffer2.writableSegment$jvm(i);
                        int i2 = 8192 - writableSegment$jvm.limit;
                        writableSegment$jvm.limit = 8192;
                        long j = (long) i2;
                        buffer2.setSize$jvm(size + j);
                        this.segment = writableSegment$jvm;
                        this.offset = size;
                        this.data = writableSegment$jvm.data;
                        this.start = 8192 - i2;
                        this.end = 8192;
                        return j;
                    } else {
                        throw new IllegalStateException("expandBuffer() only permitted for read/write buffers".toString());
                    }
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("minByteCount > Segment.SIZE: ");
                    sb.append(i);
                    throw new IllegalArgumentException(sb.toString().toString());
                }
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("minByteCount <= 0: ");
                sb2.append(i);
                throw new IllegalArgumentException(sb2.toString().toString());
            }
        }

        public void close() {
            if (this.buffer != null) {
                this.buffer = null;
                this.segment = null;
                this.offset = -1;
                this.data = null;
                this.start = -1;
                this.end = -1;
                return;
            }
            throw new IllegalStateException("not attached to a buffer".toString());
        }
    }

    public Buffer buffer() {
        return this;
    }

    public void close() {
    }

    public final Buffer copyTo(OutputStream outputStream) throws IOException {
        return copyTo$default(this, outputStream, 0, 0, 6, (Object) null);
    }

    public final Buffer copyTo(OutputStream outputStream, long j) throws IOException {
        return copyTo$default(this, outputStream, j, 0, 4, (Object) null);
    }

    public Buffer emit() {
        return this;
    }

    public Buffer emitCompleteSegments() {
        return this;
    }

    public void flush() {
    }

    public Buffer getBuffer() {
        return this;
    }

    public boolean isOpen() {
        return true;
    }

    public final UnsafeCursor readAndWriteUnsafe() {
        return readAndWriteUnsafe$default(this, null, 1, null);
    }

    public final UnsafeCursor readUnsafe() {
        return readUnsafe$default(this, null, 1, null);
    }

    public final Buffer writeTo(OutputStream outputStream) throws IOException {
        return writeTo$default(this, outputStream, 0, 2, null);
    }

    public final void setSize$jvm(long j) {
        this.size = j;
    }

    public final long size() {
        return this.size;
    }

    public OutputStream outputStream() {
        return new Buffer$outputStream$1(this);
    }

    public boolean exhausted() {
        return this.size == 0;
    }

    public void require(long j) throws EOFException {
        if (this.size < j) {
            throw new EOFException();
        }
    }

    public boolean request(long j) {
        return this.size >= j;
    }

    public BufferedSource peek() {
        return Okio.buffer((Source) new PeekSource(this));
    }

    public InputStream inputStream() {
        return new Buffer$inputStream$1(this);
    }

    public static /* bridge */ /* synthetic */ Buffer copyTo$default(Buffer buffer, OutputStream outputStream, long j, long j2, int i, Object obj) throws IOException {
        if ((i & 2) != 0) {
            j = 0;
        }
        long j3 = j;
        if ((i & 4) != 0) {
            j2 = buffer.size - j3;
        }
        return buffer.copyTo(outputStream, j3, j2);
    }

    public final Buffer copyTo(OutputStream outputStream, long j, long j2) throws IOException {
        Intrinsics.checkParameterIsNotNull(outputStream, "out");
        Util.checkOffsetAndCount(this.size, j, j2);
        if (j2 == 0) {
            return this;
        }
        Segment segment = this.head;
        while (true) {
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            if (j < ((long) (segment.limit - segment.pos))) {
                break;
            }
            j -= (long) (segment.limit - segment.pos);
            segment = segment.next;
        }
        while (j2 > 0) {
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            int i = (int) (((long) segment.pos) + j);
            int min = (int) Math.min((long) (segment.limit - i), j2);
            outputStream.write(segment.data, i, min);
            j2 -= (long) min;
            segment = segment.next;
            j = 0;
        }
        return this;
    }

    public static /* bridge */ /* synthetic */ Buffer copyTo$default(Buffer buffer, Buffer buffer2, long j, long j2, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 0;
        }
        long j3 = j;
        if ((i & 4) != 0) {
            j2 = buffer.size - j3;
        }
        return buffer.copyTo(buffer2, j3, j2);
    }

    public final Buffer copyTo(Buffer buffer, long j, long j2) {
        Intrinsics.checkParameterIsNotNull(buffer, "out");
        Util.checkOffsetAndCount(this.size, j, j2);
        if (j2 == 0) {
            return this;
        }
        buffer.size += j2;
        Segment segment = this.head;
        while (true) {
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            if (j < ((long) (segment.limit - segment.pos))) {
                break;
            }
            j -= (long) (segment.limit - segment.pos);
            segment = segment.next;
        }
        while (j2 > 0) {
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            Segment sharedCopy = segment.sharedCopy();
            sharedCopy.pos += (int) j;
            sharedCopy.limit = Math.min(sharedCopy.pos + ((int) j2), sharedCopy.limit);
            Segment segment2 = buffer.head;
            if (segment2 == null) {
                sharedCopy.prev = sharedCopy;
                sharedCopy.next = sharedCopy.prev;
                buffer.head = sharedCopy.next;
            } else {
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
                Segment segment3 = segment2.prev;
                if (segment3 == null) {
                    Intrinsics.throwNpe();
                }
                segment3.push(sharedCopy);
            }
            j2 -= (long) (sharedCopy.limit - sharedCopy.pos);
            segment = segment.next;
            j = 0;
        }
        return this;
    }

    public static /* bridge */ /* synthetic */ Buffer writeTo$default(Buffer buffer, OutputStream outputStream, long j, int i, Object obj) throws IOException {
        if ((i & 2) != 0) {
            j = buffer.size;
        }
        return buffer.writeTo(outputStream, j);
    }

    public final Buffer writeTo(OutputStream outputStream, long j) throws IOException {
        Intrinsics.checkParameterIsNotNull(outputStream, "out");
        Util.checkOffsetAndCount(this.size, 0, j);
        Segment segment = this.head;
        while (j > 0) {
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            int min = (int) Math.min(j, (long) (segment.limit - segment.pos));
            outputStream.write(segment.data, segment.pos, min);
            segment.pos += min;
            long j2 = (long) min;
            this.size -= j2;
            j -= j2;
            if (segment.pos == segment.limit) {
                Segment pop = segment.pop();
                this.head = pop;
                SegmentPool.recycle(segment);
                segment = pop;
            }
        }
        return this;
    }

    public final Buffer readFrom(InputStream inputStream) throws IOException {
        Intrinsics.checkParameterIsNotNull(inputStream, "input");
        readFrom(inputStream, LongCompanionObject.MAX_VALUE, true);
        return this;
    }

    public final Buffer readFrom(InputStream inputStream, long j) throws IOException {
        Intrinsics.checkParameterIsNotNull(inputStream, "input");
        if (j >= 0) {
            readFrom(inputStream, j, false);
            return this;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("byteCount < 0: ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString().toString());
    }

    private final void readFrom(InputStream inputStream, long j, boolean z) throws IOException {
        while (true) {
            if (j > 0 || z) {
                Segment writableSegment$jvm = writableSegment$jvm(1);
                int read = inputStream.read(writableSegment$jvm.data, writableSegment$jvm.limit, (int) Math.min(j, (long) (8192 - writableSegment$jvm.limit)));
                if (read != -1) {
                    writableSegment$jvm.limit += read;
                    long j2 = (long) read;
                    this.size += j2;
                    j -= j2;
                } else if (!z) {
                    throw new EOFException();
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public final long completeSegmentByteCount() {
        long j = this.size;
        if (j == 0) {
            return 0;
        }
        Segment segment = this.head;
        if (segment == null) {
            Intrinsics.throwNpe();
        }
        Segment segment2 = segment.prev;
        if (segment2 == null) {
            Intrinsics.throwNpe();
        }
        if (segment2.limit < 8192 && segment2.owner) {
            j -= (long) (segment2.limit - segment2.pos);
        }
        return j;
    }

    public byte readByte() throws EOFException {
        if (this.size != 0) {
            Segment segment = this.head;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            int i = segment.pos;
            int i2 = segment.limit;
            int i3 = i + 1;
            byte b = segment.data[i];
            this.size--;
            if (i3 == i2) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i3;
            }
            return b;
        }
        throw new EOFException();
    }

    public final byte getByte(long j) {
        Util.checkOffsetAndCount(this.size, j, 1);
        Segment segment = this.head;
        if (segment == null) {
            Segment segment2 = null;
            Intrinsics.throwNpe();
            return segment2.data[(int) ((((long) segment2.pos) + j) - -1)];
        } else if (size() - j < j) {
            long size2 = size();
            while (size2 > j) {
                segment = segment.prev;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                size2 -= (long) (segment.limit - segment.pos);
            }
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            return segment.data[(int) ((((long) segment.pos) + j) - size2)];
        } else {
            long j2 = 0;
            while (true) {
                long j3 = ((long) (segment.limit - segment.pos)) + j2;
                if (j3 > j) {
                    break;
                }
                segment = segment.next;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                j2 = j3;
            }
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            return segment.data[(int) ((((long) segment.pos) + j) - j2)];
        }
    }

    public short readShort() throws EOFException {
        if (this.size >= 2) {
            Segment segment = this.head;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            int i = segment.pos;
            int i2 = segment.limit;
            if (i2 - i < 2) {
                return (short) (((readByte() & UByte.MAX_VALUE) << 8) | (readByte() & UByte.MAX_VALUE));
            }
            byte[] bArr = segment.data;
            int i3 = i + 1;
            int i4 = i3 + 1;
            byte b = ((bArr[i] & UByte.MAX_VALUE) << 8) | (bArr[i3] & UByte.MAX_VALUE);
            this.size -= 2;
            if (i4 == i2) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i4;
            }
            return (short) b;
        }
        throw new EOFException();
    }

    public int readInt() throws EOFException {
        if (this.size >= 4) {
            Segment segment = this.head;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            int i = segment.pos;
            int i2 = segment.limit;
            if (((long) (i2 - i)) < 4) {
                return ((readByte() & UByte.MAX_VALUE) << 24) | ((readByte() & UByte.MAX_VALUE) << 16) | ((readByte() & UByte.MAX_VALUE) << 8) | (readByte() & UByte.MAX_VALUE);
            }
            byte[] bArr = segment.data;
            int i3 = i + 1;
            int i4 = i3 + 1;
            byte b = ((bArr[i] & UByte.MAX_VALUE) << 24) | ((bArr[i3] & UByte.MAX_VALUE) << 16);
            int i5 = i4 + 1;
            byte b2 = b | ((bArr[i4] & UByte.MAX_VALUE) << 8);
            int i6 = i5 + 1;
            byte b3 = b2 | (bArr[i5] & UByte.MAX_VALUE);
            this.size -= 4;
            if (i6 == i2) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i6;
            }
            return b3;
        }
        throw new EOFException();
    }

    public long readLong() throws EOFException {
        if (this.size >= 8) {
            Segment segment = this.head;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            int i = segment.pos;
            int i2 = segment.limit;
            if (((long) (i2 - i)) < 8) {
                return ((((long) readInt()) & 4294967295L) << 32) | (4294967295L & ((long) readInt()));
            }
            byte[] bArr = segment.data;
            int i3 = i + 1;
            int i4 = i3 + 1;
            int i5 = i4 + 1;
            long j = ((((long) bArr[i]) & 255) << 56) | ((((long) bArr[i3]) & 255) << 48) | ((((long) bArr[i4]) & 255) << 40);
            int i6 = i5 + 1;
            long j2 = ((((long) bArr[i5]) & 255) << 32) | j;
            int i7 = i6 + 1;
            int i8 = i7 + 1;
            long j3 = j2 | ((((long) bArr[i6]) & 255) << 24) | ((((long) bArr[i7]) & 255) << 16);
            int i9 = i8 + 1;
            int i10 = i9 + 1;
            long j4 = j3 | ((((long) bArr[i8]) & 255) << 8) | (((long) bArr[i9]) & 255);
            this.size -= 8;
            if (i10 == i2) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i10;
            }
            return j4;
        }
        throw new EOFException();
    }

    public short readShortLe() throws EOFException {
        return Util.reverseBytes(readShort());
    }

    public int readIntLe() throws EOFException {
        return Util.reverseBytes(readInt());
    }

    public long readLongLe() throws EOFException {
        return Util.reverseBytes(readLong());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0050, code lost:
        if (r8 != false) goto L_0x0055;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0052, code lost:
        r1.readByte();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0055, code lost:
        r3 = new java.lang.StringBuilder();
        r3.append("Number too large: ");
        r3.append(r1.readUtf8());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0071, code lost:
        throw new java.lang.NumberFormatException(r3.toString());
     */
    public long readDecimalLong() throws EOFException {
        long j = 0;
        if (this.size != 0) {
            long j2 = -7;
            int i = 0;
            boolean z = false;
            boolean z2 = false;
            loop0:
            do {
                Segment segment = this.head;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                byte[] bArr = segment.data;
                int i2 = segment.pos;
                int i3 = segment.limit;
                while (true) {
                    if (i2 >= i3) {
                        break;
                    }
                    byte b = bArr[i2];
                    byte b2 = (byte) 48;
                    if (b >= b2 && b <= ((byte) 57)) {
                        int i4 = b2 - b;
                        int i5 = (j > -922337203685477580L ? 1 : (j == -922337203685477580L ? 0 : -1));
                        if (i5 < 0 || (i5 == 0 && ((long) i4) < j2)) {
                            Buffer writeByte = new Buffer().writeDecimalLong(j).writeByte((int) b);
                        } else {
                            j = (j * 10) + ((long) i4);
                        }
                    } else if (b == ((byte) 45) && i == 0) {
                        j2--;
                        z = true;
                    } else if (i != 0) {
                        z2 = true;
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Expected leading [0-9] or '-' character but was 0x");
                        sb.append(Integer.toHexString(b));
                        throw new NumberFormatException(sb.toString());
                    }
                    i2++;
                    i++;
                }
                if (i2 == i3) {
                    this.head = segment.pop();
                    SegmentPool.recycle(segment);
                } else {
                    segment.pos = i2;
                }
                if (z2) {
                    break;
                }
            } while (this.head != null);
            this.size -= (long) i;
            return z ? j : -j;
        }
        throw new EOFException();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:0x009e, code lost:
        if (r8 != r9) goto L_0x00aa;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00a0, code lost:
        r15.head = r6.pop();
        okio.SegmentPool.recycle(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00aa, code lost:
        r6.pos = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00ac, code lost:
        if (r1 != false) goto L_0x00b2;
     */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0081 A[SYNTHETIC] */
    public long readHexadecimalUnsignedLong() throws EOFException {
        int i;
        if (this.size != 0) {
            int i2 = 0;
            long j = 0;
            boolean z = false;
            do {
                Segment segment = this.head;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                byte[] bArr = segment.data;
                int i3 = segment.pos;
                int i4 = segment.limit;
                while (true) {
                    if (i3 >= i4) {
                        break;
                    }
                    byte b = bArr[i3];
                    byte b2 = (byte) 48;
                    if (b < b2 || b > ((byte) 57)) {
                        byte b3 = (byte) 97;
                        if (b < b3 || b > ((byte) 102)) {
                            b3 = (byte) 65;
                            if (b < b3 || b > ((byte) 70)) {
                                if (i2 == 0) {
                                    z = true;
                                } else {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Expected leading [0-9a-fA-F] character but was 0x");
                                    sb.append(Integer.toHexString(b));
                                    throw new NumberFormatException(sb.toString());
                                }
                            }
                        }
                        i = (b - b3) + 10;
                    } else {
                        i = b - b2;
                    }
                    if ((-1152921504606846976L & j) == 0) {
                        j = (j << 4) | ((long) i);
                        i3++;
                        i2++;
                    } else {
                        Buffer writeByte = new Buffer().writeHexadecimalUnsignedLong(j).writeByte((int) b);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Number too large: ");
                        sb2.append(writeByte.readUtf8());
                        throw new NumberFormatException(sb2.toString());
                    }
                }
                if (i2 == 0) {
                }
            } while (this.head != null);
            this.size -= (long) i2;
            return j;
        }
        throw new EOFException();
    }

    public ByteString readByteString() {
        return new ByteString(readByteArray());
    }

    public ByteString readByteString(long j) throws EOFException {
        return new ByteString(readByteArray(j));
    }

    public int select(Options options) {
        Intrinsics.checkParameterIsNotNull(options, "options");
        int selectPrefix$jvm$default = selectPrefix$jvm$default(this, options, false, 2, null);
        if (selectPrefix$jvm$default == -1) {
            return -1;
        }
        skip((long) options.getByteStrings$jvm()[selectPrefix$jvm$default].size());
        return selectPrefix$jvm$default;
    }

    public static /* bridge */ /* synthetic */ int selectPrefix$jvm$default(Buffer buffer, Options options, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return buffer.selectPrefix$jvm(options, z);
    }

    public final int selectPrefix$jvm(Options options, boolean z) {
        int i;
        int i2;
        int i3;
        Segment segment;
        int i4;
        Intrinsics.checkParameterIsNotNull(options, "options");
        Segment segment2 = this.head;
        int i5 = -2;
        if (segment2 != null) {
            byte[] bArr = segment2.data;
            int i6 = segment2.pos;
            int i7 = segment2.limit;
            int[] trie$jvm = options.getTrie$jvm();
            Segment segment3 = segment2;
            int i8 = 0;
            int i9 = -1;
            loop0:
            while (true) {
                int i10 = i8 + 1;
                int i11 = trie$jvm[i8];
                int i12 = i10 + 1;
                int i13 = trie$jvm[i10];
                if (i13 != -1) {
                    i9 = i13;
                }
                if (segment3 == null) {
                    break;
                }
                if (i11 < 0) {
                    int i14 = i12 + (i11 * -1);
                    while (true) {
                        int i15 = i6 + 1;
                        int i16 = i12 + 1;
                        if ((bArr[i6] & UByte.MAX_VALUE) != trie$jvm[i12]) {
                            return i9;
                        }
                        boolean z2 = i16 == i14;
                        if (i15 == i7) {
                            if (segment3 == null) {
                                Intrinsics.throwNpe();
                            }
                            Segment segment4 = segment3.next;
                            if (segment4 == null) {
                                Intrinsics.throwNpe();
                            }
                            i4 = segment4.pos;
                            byte[] bArr2 = segment4.data;
                            i3 = segment4.limit;
                            if (segment4 == segment2) {
                                if (!z2) {
                                    break loop0;
                                }
                                segment4 = null;
                            }
                            byte[] bArr3 = bArr2;
                            segment = segment4;
                            bArr = bArr3;
                        } else {
                            Segment segment5 = segment3;
                            i3 = i7;
                            i4 = i15;
                            segment = segment5;
                        }
                        if (z2) {
                            i = trie$jvm[i16];
                            i2 = i4;
                            i7 = i3;
                            segment3 = segment;
                            break;
                        }
                        i6 = i4;
                        i7 = i3;
                        i12 = i16;
                        segment3 = segment;
                    }
                } else {
                    int i17 = i6 + 1;
                    byte b = bArr[i6] & UByte.MAX_VALUE;
                    int i18 = i12 + i11;
                    while (i12 != i18) {
                        if (b == trie$jvm[i12]) {
                            i = trie$jvm[i12 + i11];
                            if (i17 == i7) {
                                segment3 = segment3.next;
                                if (segment3 == null) {
                                    Intrinsics.throwNpe();
                                }
                                i2 = segment3.pos;
                                bArr = segment3.data;
                                i7 = segment3.limit;
                                if (segment3 == segment2) {
                                    segment3 = null;
                                }
                            } else {
                                i2 = i17;
                            }
                        } else {
                            i12++;
                        }
                    }
                    return i9;
                }
                if (i >= 0) {
                    return i;
                }
                i8 = -i;
                i6 = i2;
                i5 = -2;
            }
            return z ? i5 : i9;
        }
        return z ? -2 : -1;
    }

    public void readFully(Buffer buffer, long j) throws EOFException {
        Intrinsics.checkParameterIsNotNull(buffer, "sink");
        long j2 = this.size;
        if (j2 >= j) {
            buffer.write(this, j);
        } else {
            buffer.write(this, j2);
            throw new EOFException();
        }
    }

    public long readAll(Sink sink) throws IOException {
        Intrinsics.checkParameterIsNotNull(sink, "sink");
        long j = this.size;
        if (j > 0) {
            sink.write(this, j);
        }
        return j;
    }

    public String readUtf8() {
        return readString(this.size, Charsets.UTF_8);
    }

    public String readUtf8(long j) throws EOFException {
        return readString(j, Charsets.UTF_8);
    }

    public String readString(Charset charset) {
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        return readString(this.size, charset);
    }

    public String readString(long j, Charset charset) throws EOFException {
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (!(i >= 0 && j <= ((long) Integer.MAX_VALUE))) {
            StringBuilder sb = new StringBuilder();
            sb.append("byteCount: ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString().toString());
        } else if (this.size < j) {
            throw new EOFException();
        } else if (i == 0) {
            return "";
        } else {
            Segment segment = this.head;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            if (((long) segment.pos) + j > ((long) segment.limit)) {
                return new String(readByteArray(j), charset);
            }
            int i2 = (int) j;
            String str = new String(segment.data, segment.pos, i2, charset);
            segment.pos += i2;
            this.size -= j;
            if (segment.pos == segment.limit) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            }
            return str;
        }
    }

    public String readUtf8Line() throws EOFException {
        long indexOf = indexOf((byte) 10);
        if (indexOf != -1) {
            return readUtf8Line$jvm(indexOf);
        }
        long j = this.size;
        if (j != 0) {
            return readUtf8(j);
        }
        return null;
    }

    public String readUtf8LineStrict() throws EOFException {
        return readUtf8LineStrict(LongCompanionObject.MAX_VALUE);
    }

    public String readUtf8LineStrict(long j) throws EOFException {
        if (j >= 0) {
            long j2 = LongCompanionObject.MAX_VALUE;
            if (j != LongCompanionObject.MAX_VALUE) {
                j2 = j + 1;
            }
            byte b = (byte) 10;
            long indexOf = indexOf(b, 0, j2);
            if (indexOf != -1) {
                return readUtf8Line$jvm(indexOf);
            }
            if (j2 < this.size && getByte(j2 - 1) == ((byte) 13) && getByte(j2) == b) {
                return readUtf8Line$jvm(j2);
            }
            Buffer buffer = new Buffer();
            copyTo(buffer, 0, Math.min((long) 32, this.size));
            StringBuilder sb = new StringBuilder();
            sb.append("\\n not found: limit=");
            sb.append(Math.min(this.size, j));
            sb.append(" content=");
            sb.append(buffer.readByteString().hex());
            sb.append(Typography.ellipsis);
            throw new EOFException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("limit < 0: ");
        sb2.append(j);
        throw new IllegalArgumentException(sb2.toString().toString());
    }

    public final String readUtf8Line$jvm(long j) throws EOFException {
        if (j > 0) {
            long j2 = j - 1;
            if (getByte(j2) == ((byte) 13)) {
                String readUtf8 = readUtf8(j2);
                skip(2);
                return readUtf8;
            }
        }
        String readUtf82 = readUtf8(j);
        skip(1);
        return readUtf82;
    }

    public int readUtf8CodePoint() throws EOFException {
        byte b;
        int i;
        byte b2;
        if (this.size != 0) {
            byte b3 = getByte(0);
            int i2 = 1;
            byte b4 = 65533;
            if ((b3 & ByteCompanionObject.MIN_VALUE) == 0) {
                b2 = b3 & ByteCompanionObject.MAX_VALUE;
                i = 1;
                b = 0;
            } else if ((b3 & 224) == 192) {
                b2 = b3 & 31;
                i = 2;
                b = ByteCompanionObject.MIN_VALUE;
            } else if ((b3 & 240) == 224) {
                b2 = b3 & 15;
                i = 3;
                b = UByte.MIN_VALUE;
            } else if ((b3 & 248) == 240) {
                b2 = b3 & 7;
                i = 4;
                b = UByte.MIN_VALUE;
            } else {
                skip(1);
                return Utf8.REPLACEMENT_CODE_POINT;
            }
            long j = (long) i;
            if (this.size >= j) {
                while (i2 < i) {
                    long j2 = (long) i2;
                    byte b5 = getByte(j2);
                    if ((b5 & 192) == 128) {
                        b2 = (b2 << 6) | (b5 & Utf8.REPLACEMENT_BYTE);
                        i2++;
                    } else {
                        skip(j2);
                        return Utf8.REPLACEMENT_CODE_POINT;
                    }
                }
                skip(j);
                if (b2 <= 1114111 && ((55296 > b2 || 57343 < b2) && b2 >= b)) {
                    b4 = b2;
                }
                return b4;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("size < ");
            sb.append(i);
            sb.append(": ");
            sb.append(this.size);
            sb.append(" (to read code point prefixed 0x");
            sb.append(Integer.toHexString(b3));
            sb.append(")");
            throw new EOFException(sb.toString());
        }
        throw new EOFException();
    }

    public byte[] readByteArray() {
        return readByteArray(this.size);
    }

    public byte[] readByteArray(long j) throws EOFException {
        if (!(j >= 0 && j <= ((long) Integer.MAX_VALUE))) {
            StringBuilder sb = new StringBuilder();
            sb.append("byteCount: ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString().toString());
        } else if (this.size >= j) {
            byte[] bArr = new byte[((int) j)];
            readFully(bArr);
            return bArr;
        } else {
            throw new EOFException();
        }
    }

    public int read(byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(bArr, "sink");
        return read(bArr, 0, bArr.length);
    }

    public void readFully(byte[] bArr) throws EOFException {
        Intrinsics.checkParameterIsNotNull(bArr, "sink");
        int i = 0;
        while (i < bArr.length) {
            int read = read(bArr, i, bArr.length - i);
            if (read != -1) {
                i += read;
            } else {
                throw new EOFException();
            }
        }
    }

    public int read(byte[] bArr, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(bArr, "sink");
        Util.checkOffsetAndCount((long) bArr.length, (long) i, (long) i2);
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        int min = Math.min(i2, segment.limit - segment.pos);
        System.arraycopy(segment.data, segment.pos, bArr, i, min);
        segment.pos += min;
        this.size -= (long) min;
        if (segment.pos == segment.limit) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        }
        return min;
    }

    public int read(ByteBuffer byteBuffer) throws IOException {
        Intrinsics.checkParameterIsNotNull(byteBuffer, "sink");
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        int min = Math.min(byteBuffer.remaining(), segment.limit - segment.pos);
        byteBuffer.put(segment.data, segment.pos, min);
        segment.pos += min;
        this.size -= (long) min;
        if (segment.pos == segment.limit) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        }
        return min;
    }

    public final void clear() {
        skip(this.size);
    }

    public void skip(long j) throws EOFException {
        while (j > 0) {
            Segment segment = this.head;
            if (segment != null) {
                int min = (int) Math.min(j, (long) (segment.limit - segment.pos));
                long j2 = (long) min;
                this.size -= j2;
                j -= j2;
                segment.pos += min;
                if (segment.pos == segment.limit) {
                    this.head = segment.pop();
                    SegmentPool.recycle(segment);
                }
            } else {
                throw new EOFException();
            }
        }
    }

    public Buffer write(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "byteString");
        byteString.write$jvm(this);
        return this;
    }

    public Buffer writeUtf8(String str) {
        Intrinsics.checkParameterIsNotNull(str, "string");
        return writeUtf8(str, 0, str.length());
    }

    public Buffer writeUtf8(String str, int i, int i2) {
        int i3;
        char c;
        Intrinsics.checkParameterIsNotNull(str, "string");
        if (i >= 0) {
            if (i2 >= i) {
                if (i2 <= str.length()) {
                    while (i < i2) {
                        char charAt = str.charAt(i);
                        if (charAt < 128) {
                            Segment writableSegment$jvm = writableSegment$jvm(1);
                            byte[] bArr = writableSegment$jvm.data;
                            int i4 = writableSegment$jvm.limit - i;
                            int min = Math.min(i2, 8192 - i4);
                            i3 = i + 1;
                            bArr[i + i4] = (byte) charAt;
                            while (i3 < min) {
                                char charAt2 = str.charAt(i3);
                                if (charAt2 >= 128) {
                                    break;
                                }
                                int i5 = i3 + 1;
                                bArr[i3 + i4] = (byte) charAt2;
                                i3 = i5;
                            }
                            int i6 = (i4 + i3) - writableSegment$jvm.limit;
                            writableSegment$jvm.limit += i6;
                            this.size += (long) i6;
                        } else {
                            if (charAt < 2048) {
                                Segment writableSegment$jvm2 = writableSegment$jvm(2);
                                writableSegment$jvm2.data[writableSegment$jvm2.limit] = (byte) ((charAt >> 6) | 192);
                                writableSegment$jvm2.data[writableSegment$jvm2.limit + 1] = (byte) ((charAt & '?') | 128);
                                writableSegment$jvm2.limit += 2;
                                this.size += 2;
                            } else if (charAt < 55296 || charAt > 57343) {
                                Segment writableSegment$jvm3 = writableSegment$jvm(3);
                                writableSegment$jvm3.data[writableSegment$jvm3.limit] = (byte) ((charAt >> 12) | 224);
                                writableSegment$jvm3.data[writableSegment$jvm3.limit + 1] = (byte) ((63 & (charAt >> 6)) | 128);
                                writableSegment$jvm3.data[writableSegment$jvm3.limit + 2] = (byte) ((charAt & '?') | 128);
                                writableSegment$jvm3.limit += 3;
                                this.size += 3;
                            } else {
                                i3 = i + 1;
                                if (i3 < i2) {
                                    c = str.charAt(i3);
                                } else {
                                    c = 0;
                                }
                                if (charAt > 56319 || 56320 > c || 57343 < c) {
                                    writeByte(63);
                                } else {
                                    int i7 = (((charAt & 1023) << 10) | (c & 1023)) + CharCompanionObject.MIN_VALUE;
                                    Segment writableSegment$jvm4 = writableSegment$jvm(4);
                                    writableSegment$jvm4.data[writableSegment$jvm4.limit] = (byte) ((i7 >> 18) | 240);
                                    writableSegment$jvm4.data[writableSegment$jvm4.limit + 1] = (byte) (((i7 >> 12) & 63) | 128);
                                    writableSegment$jvm4.data[writableSegment$jvm4.limit + 2] = (byte) (((i7 >> 6) & 63) | 128);
                                    writableSegment$jvm4.data[writableSegment$jvm4.limit + 3] = (byte) ((i7 & 63) | 128);
                                    writableSegment$jvm4.limit += 4;
                                    this.size += 4;
                                    i += 2;
                                }
                            }
                            i++;
                        }
                        i = i3;
                    }
                    return this;
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

    public Buffer writeUtf8CodePoint(int i) {
        if (i < 128) {
            writeByte(i);
        } else if (i < 2048) {
            Segment writableSegment$jvm = writableSegment$jvm(2);
            writableSegment$jvm.data[writableSegment$jvm.limit] = (byte) ((i >> 6) | 192);
            writableSegment$jvm.data[writableSegment$jvm.limit + 1] = (byte) ((i & 63) | 128);
            writableSegment$jvm.limit += 2;
            this.size += 2;
        } else if (55296 <= i && 57343 >= i) {
            writeByte(63);
        } else if (i < 65536) {
            Segment writableSegment$jvm2 = writableSegment$jvm(3);
            writableSegment$jvm2.data[writableSegment$jvm2.limit] = (byte) ((i >> 12) | 224);
            writableSegment$jvm2.data[writableSegment$jvm2.limit + 1] = (byte) (((i >> 6) & 63) | 128);
            writableSegment$jvm2.data[writableSegment$jvm2.limit + 2] = (byte) ((i & 63) | 128);
            writableSegment$jvm2.limit += 3;
            this.size += 3;
        } else if (i <= 1114111) {
            Segment writableSegment$jvm3 = writableSegment$jvm(4);
            writableSegment$jvm3.data[writableSegment$jvm3.limit] = (byte) ((i >> 18) | 240);
            writableSegment$jvm3.data[writableSegment$jvm3.limit + 1] = (byte) (((i >> 12) & 63) | 128);
            writableSegment$jvm3.data[writableSegment$jvm3.limit + 2] = (byte) (((i >> 6) & 63) | 128);
            writableSegment$jvm3.data[writableSegment$jvm3.limit + 3] = (byte) ((i & 63) | 128);
            writableSegment$jvm3.limit += 4;
            this.size += 4;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected code point: ");
            sb.append(Integer.toHexString(i));
            throw new IllegalArgumentException(sb.toString());
        }
        return this;
    }

    public Buffer writeString(String str, Charset charset) {
        Intrinsics.checkParameterIsNotNull(str, "string");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        return writeString(str, 0, str.length(), charset);
    }

    public Buffer writeString(String str, int i, int i2, Charset charset) {
        Intrinsics.checkParameterIsNotNull(str, "string");
        Intrinsics.checkParameterIsNotNull(charset, "charset");
        boolean z = true;
        if (i >= 0) {
            if (i2 >= i) {
                if (i2 > str.length()) {
                    z = false;
                }
                if (!z) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("endIndex > string.length: ");
                    sb.append(i2);
                    sb.append(" > ");
                    sb.append(str.length());
                    throw new IllegalArgumentException(sb.toString().toString());
                } else if (Intrinsics.areEqual((Object) charset, (Object) Charsets.UTF_8)) {
                    return writeUtf8(str, i, i2);
                } else {
                    String substring = str.substring(i, i2);
                    Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    if (substring != null) {
                        byte[] bytes = substring.getBytes(charset);
                        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
                        return write(bytes, 0, bytes.length);
                    }
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("endIndex < beginIndex: ");
                sb2.append(i2);
                sb2.append(" < ");
                sb2.append(i);
                throw new IllegalArgumentException(sb2.toString().toString());
            }
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("beginIndex < 0: ");
            sb3.append(i);
            throw new IllegalArgumentException(sb3.toString().toString());
        }
    }

    public Buffer write(byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(bArr, "source");
        return write(bArr, 0, bArr.length);
    }

    public Buffer write(byte[] bArr, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(bArr, "source");
        long j = (long) i2;
        Util.checkOffsetAndCount((long) bArr.length, (long) i, j);
        int i3 = i2 + i;
        while (i < i3) {
            Segment writableSegment$jvm = writableSegment$jvm(1);
            int min = Math.min(i3 - i, 8192 - writableSegment$jvm.limit);
            System.arraycopy(bArr, i, writableSegment$jvm.data, writableSegment$jvm.limit, min);
            i += min;
            writableSegment$jvm.limit += min;
        }
        this.size += j;
        return this;
    }

    public int write(ByteBuffer byteBuffer) throws IOException {
        Intrinsics.checkParameterIsNotNull(byteBuffer, "source");
        int remaining = byteBuffer.remaining();
        int i = remaining;
        while (i > 0) {
            Segment writableSegment$jvm = writableSegment$jvm(1);
            int min = Math.min(i, 8192 - writableSegment$jvm.limit);
            byteBuffer.get(writableSegment$jvm.data, writableSegment$jvm.limit, min);
            i -= min;
            writableSegment$jvm.limit += min;
        }
        this.size += (long) remaining;
        return remaining;
    }

    public long writeAll(Source source) throws IOException {
        Intrinsics.checkParameterIsNotNull(source, "source");
        long j = 0;
        while (true) {
            long read = source.read(this, (long) 8192);
            if (read == -1) {
                return j;
            }
            j += read;
        }
    }

    public BufferedSink write(Source source, long j) throws IOException {
        Intrinsics.checkParameterIsNotNull(source, "source");
        while (j > 0) {
            long read = source.read(this, j);
            if (read != -1) {
                j -= read;
            } else {
                throw new EOFException();
            }
        }
        return this;
    }

    public Buffer writeByte(int i) {
        Segment writableSegment$jvm = writableSegment$jvm(1);
        byte[] bArr = writableSegment$jvm.data;
        int i2 = writableSegment$jvm.limit;
        writableSegment$jvm.limit = i2 + 1;
        bArr[i2] = (byte) i;
        this.size++;
        return this;
    }

    public Buffer writeShort(int i) {
        Segment writableSegment$jvm = writableSegment$jvm(2);
        byte[] bArr = writableSegment$jvm.data;
        int i2 = writableSegment$jvm.limit;
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((i >>> 8) & 255);
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i & 255);
        writableSegment$jvm.limit = i4;
        this.size += 2;
        return this;
    }

    public Buffer writeShortLe(int i) {
        return writeShort((int) Util.reverseBytes((short) i));
    }

    public Buffer writeInt(int i) {
        Segment writableSegment$jvm = writableSegment$jvm(4);
        byte[] bArr = writableSegment$jvm.data;
        int i2 = writableSegment$jvm.limit;
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((i >>> 24) & 255);
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((i >>> 16) & 255);
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((i >>> 8) & 255);
        int i6 = i5 + 1;
        bArr[i5] = (byte) (i & 255);
        writableSegment$jvm.limit = i6;
        this.size += 4;
        return this;
    }

    public Buffer writeIntLe(int i) {
        return writeInt(Util.reverseBytes(i));
    }

    public Buffer writeLong(long j) {
        Segment writableSegment$jvm = writableSegment$jvm(8);
        byte[] bArr = writableSegment$jvm.data;
        int i = writableSegment$jvm.limit;
        int i2 = i + 1;
        bArr[i] = (byte) ((int) ((j >>> 56) & 255));
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((int) ((j >>> 48) & 255));
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((int) ((j >>> 40) & 255));
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((int) ((j >>> 32) & 255));
        int i6 = i5 + 1;
        bArr[i5] = (byte) ((int) ((j >>> 24) & 255));
        int i7 = i6 + 1;
        bArr[i6] = (byte) ((int) ((j >>> 16) & 255));
        int i8 = i7 + 1;
        bArr[i7] = (byte) ((int) ((j >>> 8) & 255));
        int i9 = i8 + 1;
        bArr[i8] = (byte) ((int) (j & 255));
        writableSegment$jvm.limit = i9;
        this.size += 8;
        return this;
    }

    public Buffer writeLongLe(long j) {
        return writeLong(Util.reverseBytes(j));
    }

    public Buffer writeDecimalLong(long j) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i == 0) {
            return writeByte(48);
        }
        boolean z = false;
        int i2 = 1;
        if (i < 0) {
            j = -j;
            if (j < 0) {
                return writeUtf8("-9223372036854775808");
            }
            z = true;
        }
        if (j >= 100000000) {
            i2 = j < 1000000000000L ? j < 10000000000L ? j < 1000000000 ? 9 : 10 : j < 100000000000L ? 11 : 12 : j < 1000000000000000L ? j < 10000000000000L ? 13 : j < 100000000000000L ? 14 : 15 : j < 100000000000000000L ? j < 10000000000000000L ? 16 : 17 : j < 1000000000000000000L ? 18 : 19;
        } else if (j >= 10000) {
            i2 = j < 1000000 ? j < 100000 ? 5 : 6 : j < 10000000 ? 7 : 8;
        } else if (j >= 100) {
            i2 = j < 1000 ? 3 : 4;
        } else if (j >= 10) {
            i2 = 2;
        }
        if (z) {
            i2++;
        }
        Segment writableSegment$jvm = writableSegment$jvm(i2);
        byte[] bArr = writableSegment$jvm.data;
        int i3 = writableSegment$jvm.limit + i2;
        while (j != 0) {
            long j2 = (long) 10;
            i3--;
            bArr[i3] = DIGITS[(int) (j % j2)];
            j /= j2;
        }
        if (z) {
            bArr[i3 - 1] = (byte) 45;
        }
        writableSegment$jvm.limit += i2;
        this.size += (long) i2;
        return this;
    }

    public Buffer writeHexadecimalUnsignedLong(long j) {
        if (j == 0) {
            return writeByte(48);
        }
        int numberOfTrailingZeros = (Long.numberOfTrailingZeros(Long.highestOneBit(j)) / 4) + 1;
        Segment writableSegment$jvm = writableSegment$jvm(numberOfTrailingZeros);
        byte[] bArr = writableSegment$jvm.data;
        int i = writableSegment$jvm.limit;
        for (int i2 = (writableSegment$jvm.limit + numberOfTrailingZeros) - 1; i2 >= i; i2--) {
            bArr[i2] = DIGITS[(int) (15 & j)];
            j >>>= 4;
        }
        writableSegment$jvm.limit += numberOfTrailingZeros;
        this.size += (long) numberOfTrailingZeros;
        return this;
    }

    public final Segment writableSegment$jvm(int i) {
        boolean z = true;
        if (i < 1 || i > 8192) {
            z = false;
        }
        if (z) {
            Segment segment = this.head;
            if (segment == null) {
                Segment take = SegmentPool.take();
                this.head = take;
                take.prev = take;
                take.next = take;
                return take;
            }
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            Segment segment2 = segment.prev;
            if (segment2 == null) {
                Intrinsics.throwNpe();
            }
            if (segment2.limit + i > 8192 || !segment2.owner) {
                segment2 = segment2.push(SegmentPool.take());
            }
            return segment2;
        }
        throw new IllegalArgumentException("unexpected capacity".toString());
    }

    public void write(Buffer buffer, long j) {
        Segment segment;
        Intrinsics.checkParameterIsNotNull(buffer, "source");
        if (buffer != this) {
            Util.checkOffsetAndCount(buffer.size, 0, j);
            while (j > 0) {
                Segment segment2 = buffer.head;
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
                int i = segment2.limit;
                Segment segment3 = buffer.head;
                if (segment3 == null) {
                    Intrinsics.throwNpe();
                }
                if (j < ((long) (i - segment3.pos))) {
                    Segment segment4 = this.head;
                    if (segment4 != null) {
                        if (segment4 == null) {
                            Intrinsics.throwNpe();
                        }
                        segment = segment4.prev;
                    } else {
                        segment = null;
                    }
                    if (segment != null && segment.owner) {
                        if ((((long) segment.limit) + j) - ((long) (segment.shared ? 0 : segment.pos)) <= ((long) 8192)) {
                            Segment segment5 = buffer.head;
                            if (segment5 == null) {
                                Intrinsics.throwNpe();
                            }
                            segment5.writeTo(segment, (int) j);
                            buffer.size -= j;
                            this.size += j;
                            return;
                        }
                    }
                    Segment segment6 = buffer.head;
                    if (segment6 == null) {
                        Intrinsics.throwNpe();
                    }
                    buffer.head = segment6.split((int) j);
                }
                Segment segment7 = buffer.head;
                if (segment7 == null) {
                    Intrinsics.throwNpe();
                }
                long j2 = (long) (segment7.limit - segment7.pos);
                buffer.head = segment7.pop();
                Segment segment8 = this.head;
                if (segment8 == null) {
                    this.head = segment7;
                    segment7.prev = segment7;
                    segment7.next = segment7.prev;
                } else {
                    if (segment8 == null) {
                        Intrinsics.throwNpe();
                    }
                    Segment segment9 = segment8.prev;
                    if (segment9 == null) {
                        Intrinsics.throwNpe();
                    }
                    segment9.push(segment7).compact();
                }
                buffer.size -= j2;
                this.size += j2;
                j -= j2;
            }
            return;
        }
        throw new IllegalArgumentException("source == this".toString());
    }

    public long read(Buffer buffer, long j) {
        Intrinsics.checkParameterIsNotNull(buffer, "sink");
        if (j >= 0) {
            long j2 = this.size;
            if (j2 == 0) {
                return -1;
            }
            if (j > j2) {
                j = j2;
            }
            buffer.write(this, j);
            return j;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("byteCount < 0: ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString().toString());
    }

    public long indexOf(byte b) {
        return indexOf(b, 0, LongCompanionObject.MAX_VALUE);
    }

    private final <T> T seek(long j, Function2<? super Segment, ? super Long, ? extends T> function2) {
        Segment segment = this.head;
        if (segment == null) {
            return function2.invoke(null, Long.valueOf(-1));
        }
        if (size() - j < j) {
            long size2 = size();
            while (size2 > j) {
                segment = segment.prev;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                size2 -= (long) (segment.limit - segment.pos);
            }
            return function2.invoke(segment, Long.valueOf(size2));
        }
        long j2 = 0;
        while (true) {
            long j3 = ((long) (segment.limit - segment.pos)) + j2;
            if (j3 > j) {
                return function2.invoke(segment, Long.valueOf(j2));
            }
            segment = segment.next;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
            j2 = j3;
        }
    }

    public long indexOf(byte b, long j) {
        return indexOf(b, j, LongCompanionObject.MAX_VALUE);
    }

    public long indexOf(byte b, long j, long j2) {
        long j3;
        int i;
        long j4 = 0;
        if (0 <= j && j2 >= j) {
            long j5 = this.size;
            if (j2 > j5) {
                j2 = j5;
            }
            if (j == j2) {
                return -1;
            }
            Segment segment = this.head;
            if (segment != null) {
                if (size() - j < j) {
                    j3 = size();
                    while (j3 > j) {
                        segment = segment.prev;
                        if (segment == null) {
                            Intrinsics.throwNpe();
                        }
                        j3 -= (long) (segment.limit - segment.pos);
                    }
                    if (segment != null) {
                        while (j3 < j2) {
                            byte[] bArr = segment.data;
                            int min = (int) Math.min((long) segment.limit, (((long) segment.pos) + j2) - j3);
                            i = (int) ((((long) segment.pos) + j) - j3);
                            while (i < min) {
                                if (bArr[i] != b) {
                                    i++;
                                }
                            }
                            j3 += (long) (segment.limit - segment.pos);
                            segment = segment.next;
                            if (segment == null) {
                                Intrinsics.throwNpe();
                            }
                            j = j3;
                        }
                    }
                    return -1;
                }
                while (true) {
                    long j6 = ((long) (segment.limit - segment.pos)) + j4;
                    if (j6 > j) {
                        break;
                    }
                    segment = segment.next;
                    if (segment == null) {
                        Intrinsics.throwNpe();
                    }
                    j4 = j6;
                }
                if (segment != null) {
                    while (j3 < j2) {
                        byte[] bArr2 = segment.data;
                        int min2 = (int) Math.min((long) segment.limit, (((long) segment.pos) + j2) - j3);
                        int i2 = (int) ((((long) segment.pos) + j) - j3);
                        while (i < min2) {
                            if (bArr2[i] != b) {
                                i2 = i + 1;
                            }
                        }
                        j4 = j3 + ((long) (segment.limit - segment.pos));
                        segment = segment.next;
                        if (segment == null) {
                            Intrinsics.throwNpe();
                        }
                        j = j4;
                    }
                }
                return -1;
                return ((long) (i - segment.pos)) + j3;
            }
            Segment segment2 = null;
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("size=");
        sb.append(this.size);
        sb.append(" fromIndex=");
        sb.append(j);
        sb.append(" toIndex=");
        sb.append(j2);
        throw new IllegalArgumentException(sb.toString().toString());
    }

    public long indexOf(ByteString byteString) throws IOException {
        Intrinsics.checkParameterIsNotNull(byteString, "bytes");
        return indexOf(byteString, 0);
    }

    public long indexOf(ByteString byteString, long j) throws IOException {
        Buffer buffer = this;
        long j2 = j;
        Intrinsics.checkParameterIsNotNull(byteString, "bytes");
        boolean z = true;
        if (byteString.size() > 0) {
            long j3 = 0;
            if (j2 < 0) {
                z = false;
            }
            if (z) {
                Segment segment = buffer.head;
                if (segment == null) {
                    Segment segment2 = null;
                    return -1;
                } else if (size() - j2 < j2) {
                    long size2 = size();
                    while (size2 > j2) {
                        segment = segment.prev;
                        if (segment == null) {
                            Intrinsics.throwNpe();
                        }
                        size2 -= (long) (segment.limit - segment.pos);
                    }
                    if (segment == null) {
                        return -1;
                    }
                    byte[] internalArray$jvm = byteString.internalArray$jvm();
                    byte b = internalArray$jvm[0];
                    int size3 = byteString.size();
                    long j4 = (buffer.size - ((long) size3)) + 1;
                    long j5 = size2;
                    Segment segment3 = segment;
                    while (j5 < j4) {
                        byte[] bArr = segment3.data;
                        long j6 = j4;
                        int min = (int) Math.min((long) segment3.limit, (((long) segment3.pos) + j4) - j5);
                        for (int i = (int) ((((long) segment3.pos) + j2) - j5); i < min; i++) {
                            if (bArr[i] == b) {
                                if (rangeEquals(segment3, i + 1, internalArray$jvm, 1, size3)) {
                                    return ((long) (i - segment3.pos)) + j5;
                                }
                            }
                        }
                        j5 += (long) (segment3.limit - segment3.pos);
                        segment3 = segment3.next;
                        if (segment3 == null) {
                            Intrinsics.throwNpe();
                        }
                        j2 = j5;
                        j4 = j6;
                    }
                    return -1;
                } else {
                    while (true) {
                        long j7 = ((long) (segment.limit - segment.pos)) + j3;
                        if (j7 > j2) {
                            break;
                        }
                        segment = segment.next;
                        if (segment == null) {
                            Intrinsics.throwNpe();
                        }
                        buffer = this;
                        j3 = j7;
                    }
                    if (segment == null) {
                        return -1;
                    }
                    byte[] internalArray$jvm2 = byteString.internalArray$jvm();
                    byte b2 = internalArray$jvm2[0];
                    int size4 = byteString.size();
                    long j8 = (buffer.size - ((long) size4)) + 1;
                    Segment segment4 = segment;
                    while (j3 < j8) {
                        byte[] bArr2 = segment4.data;
                        long j9 = j8;
                        int min2 = (int) Math.min((long) segment4.limit, (((long) segment4.pos) + j8) - j3);
                        for (int i2 = (int) ((((long) segment4.pos) + j2) - j3); i2 < min2; i2++) {
                            if (bArr2[i2] == b2) {
                                if (rangeEquals(segment4, i2 + 1, internalArray$jvm2, 1, size4)) {
                                    return ((long) (i2 - segment4.pos)) + j3;
                                }
                            }
                        }
                        j3 += (long) (segment4.limit - segment4.pos);
                        segment4 = segment4.next;
                        if (segment4 == null) {
                            Intrinsics.throwNpe();
                        }
                        j2 = j3;
                        j8 = j9;
                    }
                    return -1;
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("fromIndex < 0: ");
                sb.append(j2);
                throw new IllegalArgumentException(sb.toString().toString());
            }
        } else {
            throw new IllegalArgumentException("bytes is empty".toString());
        }
    }

    public long indexOfElement(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "targetBytes");
        return indexOfElement(byteString, 0);
    }

    public boolean rangeEquals(long j, ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "bytes");
        return rangeEquals(j, byteString, 0, byteString.size());
    }

    public boolean rangeEquals(long j, ByteString byteString, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(byteString, "bytes");
        if (j < 0 || i < 0 || i2 < 0 || this.size - j < ((long) i2) || byteString.size() - i < i2) {
            return false;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            if (getByte(((long) i3) + j) != byteString.getByte(i + i3)) {
                return false;
            }
        }
        return true;
    }

    private final boolean rangeEquals(Segment segment, int i, byte[] bArr, int i2, int i3) {
        int i4 = segment.limit;
        byte[] bArr2 = segment.data;
        while (i2 < i3) {
            if (i == i4) {
                segment = segment.next;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                byte[] bArr3 = segment.data;
                int i5 = segment.pos;
                bArr2 = bArr3;
                i = i5;
                i4 = segment.limit;
            }
            if (bArr2[i] != bArr[i2]) {
                return false;
            }
            i++;
            i2++;
        }
        return true;
    }

    public Timeout timeout() {
        return Timeout.NONE;
    }

    public final ByteString md5() {
        return digest("MD5");
    }

    public final ByteString sha1() {
        return digest("SHA-1");
    }

    public final ByteString sha256() {
        return digest("SHA-256");
    }

    public final ByteString sha512() {
        return digest("SHA-512");
    }

    private final ByteString digest(String str) {
        MessageDigest instance = MessageDigest.getInstance(str);
        Segment segment = this.head;
        if (segment != null) {
            instance.update(segment.data, segment.pos, segment.limit - segment.pos);
            Segment segment2 = segment.next;
            if (segment2 == null) {
                Intrinsics.throwNpe();
            }
            while (segment2 != segment) {
                instance.update(segment2.data, segment2.pos, segment2.limit - segment2.pos);
                segment2 = segment2.next;
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
            }
        }
        byte[] digest = instance.digest();
        Intrinsics.checkExpressionValueIsNotNull(digest, "messageDigest.digest()");
        return new ByteString(digest);
    }

    public final ByteString hmacSha1(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "key");
        return hmac("HmacSHA1", byteString);
    }

    public final ByteString hmacSha256(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "key");
        return hmac("HmacSHA256", byteString);
    }

    public final ByteString hmacSha512(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "key");
        return hmac("HmacSHA512", byteString);
    }

    private final ByteString hmac(String str, ByteString byteString) {
        try {
            Mac instance = Mac.getInstance(str);
            instance.init(new SecretKeySpec(byteString.internalArray$jvm(), str));
            Segment segment = this.head;
            if (segment != null) {
                instance.update(segment.data, segment.pos, segment.limit - segment.pos);
                Segment segment2 = segment.next;
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
                while (segment2 != segment) {
                    instance.update(segment2.data, segment2.pos, segment2.limit - segment2.pos);
                    segment2 = segment2.next;
                    if (segment2 == null) {
                        Intrinsics.throwNpe();
                    }
                }
            }
            byte[] doFinal = instance.doFinal();
            Intrinsics.checkExpressionValueIsNotNull(doFinal, "mac.doFinal()");
            return new ByteString(doFinal);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (this == obj2) {
            return true;
        }
        if (!(obj2 instanceof Buffer)) {
            return false;
        }
        long j = this.size;
        Buffer buffer = (Buffer) obj2;
        if (j != buffer.size) {
            return false;
        }
        if (j == 0) {
            return true;
        }
        Segment segment = this.head;
        if (segment == null) {
            Intrinsics.throwNpe();
        }
        Segment segment2 = buffer.head;
        if (segment2 == null) {
            Intrinsics.throwNpe();
        }
        int i = segment.pos;
        int i2 = segment2.pos;
        long j2 = 0;
        while (j2 < this.size) {
            long min = (long) Math.min(segment.limit - i, segment2.limit - i2);
            long j3 = 0;
            while (j3 < min) {
                int i3 = i + 1;
                int i4 = i2 + 1;
                if (segment.data[i] != segment2.data[i2]) {
                    return false;
                }
                j3++;
                i = i3;
                i2 = i4;
            }
            if (i == segment.limit) {
                segment = segment.next;
                if (segment == null) {
                    Intrinsics.throwNpe();
                }
                i = segment.pos;
            }
            if (i2 == segment2.limit) {
                segment2 = segment2.next;
                if (segment2 == null) {
                    Intrinsics.throwNpe();
                }
                i2 = segment2.pos;
            }
            j2 += min;
        }
        return true;
    }

    public int hashCode() {
        Segment segment = this.head;
        if (segment == null) {
            return 0;
        }
        int i = 1;
        do {
            for (int i2 = segment.pos; i2 < segment.limit; i2++) {
                i = (i * 31) + segment.data[i2];
            }
            segment = segment.next;
            if (segment == null) {
                Intrinsics.throwNpe();
            }
        } while (segment != this.head);
        return i;
    }

    public String toString() {
        return snapshot().toString();
    }

    public Buffer clone() {
        Buffer buffer = new Buffer();
        if (this.size == 0) {
            return buffer;
        }
        Segment segment = this.head;
        if (segment == null) {
            Intrinsics.throwNpe();
        }
        Segment sharedCopy = segment.sharedCopy();
        buffer.head = sharedCopy;
        if (sharedCopy == null) {
            Intrinsics.throwNpe();
        }
        sharedCopy.prev = buffer.head;
        Segment segment2 = buffer.head;
        if (segment2 == null) {
            Intrinsics.throwNpe();
        }
        Segment segment3 = buffer.head;
        if (segment3 == null) {
            Intrinsics.throwNpe();
        }
        segment2.next = segment3.prev;
        Segment segment4 = this.head;
        if (segment4 == null) {
            Intrinsics.throwNpe();
        }
        for (Segment segment5 = segment4.next; segment5 != this.head; segment5 = segment5.next) {
            Segment segment6 = buffer.head;
            if (segment6 == null) {
                Intrinsics.throwNpe();
            }
            Segment segment7 = segment6.prev;
            if (segment7 == null) {
                Intrinsics.throwNpe();
            }
            if (segment5 == null) {
                Intrinsics.throwNpe();
            }
            segment7.push(segment5.sharedCopy());
        }
        buffer.size = this.size;
        return buffer;
    }

    public final ByteString snapshot() {
        if (this.size <= ((long) Integer.MAX_VALUE)) {
            return snapshot((int) this.size);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("size > Integer.MAX_VALUE: ");
        sb.append(this.size);
        throw new IllegalStateException(sb.toString().toString());
    }

    public final ByteString snapshot(int i) {
        return i == 0 ? ByteString.EMPTY : SegmentedByteString.Companion.of(this, i);
    }

    public static /* bridge */ /* synthetic */ UnsafeCursor readUnsafe$default(Buffer buffer, UnsafeCursor unsafeCursor, int i, Object obj) {
        if ((i & 1) != 0) {
            unsafeCursor = new UnsafeCursor();
        }
        return buffer.readUnsafe(unsafeCursor);
    }

    public final UnsafeCursor readUnsafe(UnsafeCursor unsafeCursor) {
        Intrinsics.checkParameterIsNotNull(unsafeCursor, "unsafeCursor");
        if (unsafeCursor.buffer == null) {
            unsafeCursor.buffer = this;
            unsafeCursor.readWrite = false;
            return unsafeCursor;
        }
        throw new IllegalStateException("already attached to a buffer".toString());
    }

    public static /* bridge */ /* synthetic */ UnsafeCursor readAndWriteUnsafe$default(Buffer buffer, UnsafeCursor unsafeCursor, int i, Object obj) {
        if ((i & 1) != 0) {
            unsafeCursor = new UnsafeCursor();
        }
        return buffer.readAndWriteUnsafe(unsafeCursor);
    }

    public final UnsafeCursor readAndWriteUnsafe(UnsafeCursor unsafeCursor) {
        Intrinsics.checkParameterIsNotNull(unsafeCursor, "unsafeCursor");
        if (unsafeCursor.buffer == null) {
            unsafeCursor.buffer = this;
            unsafeCursor.readWrite = true;
            return unsafeCursor;
        }
        throw new IllegalStateException("already attached to a buffer".toString());
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to operator function", replaceWith = @ReplaceWith(expression = "this[index]", imports = {}))
    /* renamed from: -deprecated_getByte reason: not valid java name */
    public final byte m1113deprecated_getByte(long j) {
        return getByte(j);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "size", imports = {}))
    /* renamed from: -deprecated_size reason: not valid java name */
    public final long m1114deprecated_size() {
        return this.size;
    }

    static {
        byte[] bytes = "0123456789abcdef".getBytes(Charsets.UTF_8);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        DIGITS = bytes;
    }

    public long indexOfElement(ByteString byteString, long j) {
        long j2;
        int i;
        int i2;
        int i3;
        Intrinsics.checkParameterIsNotNull(byteString, "targetBytes");
        long j3 = 0;
        if (j >= 0) {
            Segment segment = this.head;
            if (segment != null) {
                if (size() - j < j) {
                    j2 = size();
                    while (j2 > j) {
                        segment = segment.prev;
                        if (segment == null) {
                            Intrinsics.throwNpe();
                        }
                        j2 -= (long) (segment.limit - segment.pos);
                    }
                    if (segment != null) {
                        if (byteString.size() == 2) {
                            byte b = byteString.getByte(0);
                            byte b2 = byteString.getByte(1);
                            while (j2 < this.size) {
                                byte[] bArr = segment.data;
                                i2 = (int) ((((long) segment.pos) + j) - j2);
                                int i4 = segment.limit;
                                while (i2 < i4) {
                                    byte b3 = bArr[i2];
                                    if (!(b3 == b || b3 == b2)) {
                                        i2++;
                                    }
                                }
                                j2 += (long) (segment.limit - segment.pos);
                                segment = segment.next;
                                if (segment == null) {
                                    Intrinsics.throwNpe();
                                }
                                j = j2;
                            }
                        } else {
                            byte[] internalArray$jvm = byteString.internalArray$jvm();
                            while (j2 < this.size) {
                                byte[] bArr2 = segment.data;
                                i = (int) ((((long) segment.pos) + j) - j2);
                                int i5 = segment.limit;
                                while (i < i5) {
                                    byte b4 = bArr2[i];
                                    for (byte b5 : internalArray$jvm) {
                                        if (b4 == b5) {
                                            i3 = segment.pos;
                                            return ((long) (i2 - i3)) + j2;
                                        }
                                    }
                                    i++;
                                }
                                j2 += (long) (segment.limit - segment.pos);
                                segment = segment.next;
                                if (segment == null) {
                                    Intrinsics.throwNpe();
                                }
                                j = j2;
                            }
                        }
                    }
                    return -1;
                }
                while (true) {
                    long j4 = ((long) (segment.limit - segment.pos)) + j3;
                    if (j4 > j) {
                        break;
                    }
                    segment = segment.next;
                    if (segment == null) {
                        Intrinsics.throwNpe();
                    }
                    j3 = j4;
                }
                if (segment != null) {
                    if (byteString.size() == 2) {
                        byte b6 = byteString.getByte(0);
                        byte b7 = byteString.getByte(1);
                        while (j2 < this.size) {
                            byte[] bArr3 = segment.data;
                            int i6 = (int) ((((long) segment.pos) + j) - j2);
                            int i7 = segment.limit;
                            while (i2 < i7) {
                                byte b8 = bArr3[i2];
                                if (!(b8 == b6 || b8 == b7)) {
                                    i6 = i2 + 1;
                                }
                            }
                            j3 = j2 + ((long) (segment.limit - segment.pos));
                            segment = segment.next;
                            if (segment == null) {
                                Intrinsics.throwNpe();
                            }
                            j = j3;
                        }
                    } else {
                        byte[] internalArray$jvm2 = byteString.internalArray$jvm();
                        while (j2 < this.size) {
                            byte[] bArr4 = segment.data;
                            int i8 = (int) ((((long) segment.pos) + j) - j2);
                            int i9 = segment.limit;
                            while (i < i9) {
                                byte b9 = bArr4[i];
                                for (byte b10 : internalArray$jvm2) {
                                    if (b9 == b10) {
                                        i3 = segment.pos;
                                        return ((long) (i2 - i3)) + j2;
                                    }
                                }
                                i8 = i + 1;
                            }
                            j3 = j2 + ((long) (segment.limit - segment.pos));
                            segment = segment.next;
                            if (segment == null) {
                                Intrinsics.throwNpe();
                            }
                            j = j3;
                        }
                    }
                }
                return -1;
                i3 = segment.pos;
                return ((long) (i2 - i3)) + j2;
            }
            Segment segment2 = null;
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("fromIndex < 0: ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString().toString());
    }
}
