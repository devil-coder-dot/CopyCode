package okhttp3.logging;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.LongCompanionObject;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.StringsKt;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u00002\u00020\u0001:\u0002\u001e\u001fB\u0011\b\u0007\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002J\r\u0010\u000b\u001a\u00020\tH\u0007¢\u0006\u0002\b\u0012J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0018\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J\u000e\u0010\u001b\u001a\u00020\u00182\u0006\u0010\u001c\u001a\u00020\u0007J\u0010\u0010\u001d\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\tH\u0007R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u000e¢\u0006\u0002\n\u0000R$\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t@GX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\n\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000\u0002\u0007\n\u0005\bF0\u0001¨\u0006 "}, d2 = {"Lokhttp3/logging/HttpLoggingInterceptor;", "Lokhttp3/Interceptor;", "logger", "Lokhttp3/logging/HttpLoggingInterceptor$Logger;", "(Lokhttp3/logging/HttpLoggingInterceptor$Logger;)V", "headersToRedact", "", "", "<set-?>", "Lokhttp3/logging/HttpLoggingInterceptor$Level;", "level", "getLevel", "()Lokhttp3/logging/HttpLoggingInterceptor$Level;", "(Lokhttp3/logging/HttpLoggingInterceptor$Level;)V", "bodyHasUnknownEncoding", "", "headers", "Lokhttp3/Headers;", "-deprecated_level", "intercept", "Lokhttp3/Response;", "chain", "Lokhttp3/Interceptor$Chain;", "logHeader", "", "i", "", "redactHeader", "name", "setLevel", "Level", "Logger", "okhttp-logging-interceptor"}, k = 1, mv = {1, 1, 15})
/* compiled from: HttpLoggingInterceptor.kt */
public final class HttpLoggingInterceptor implements Interceptor {
    private volatile Set<String> headersToRedact;
    private volatile Level level;
    private final Logger logger;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006¨\u0006\u0007"}, d2 = {"Lokhttp3/logging/HttpLoggingInterceptor$Level;", "", "(Ljava/lang/String;I)V", "NONE", "BASIC", "HEADERS", "BODY", "okhttp-logging-interceptor"}, k = 1, mv = {1, 1, 15})
    /* compiled from: HttpLoggingInterceptor.kt */
    public enum Level {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u0000 \u00062\u00020\u0001:\u0001\u0006J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u0002\u0007\n\u0005\bF0\u0001¨\u0006\u0007"}, d2 = {"Lokhttp3/logging/HttpLoggingInterceptor$Logger;", "", "log", "", "message", "", "Companion", "okhttp-logging-interceptor"}, k = 1, mv = {1, 1, 15})
    /* compiled from: HttpLoggingInterceptor.kt */
    public interface Logger {
        public static final Companion Companion = new Companion(null);
        public static final Logger DEFAULT = new HttpLoggingInterceptor$Logger$Companion$DEFAULT$1();

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\u00020\u00048\u0006X\u0004ø\u0001\u0000¢\u0006\u0002\n\u0000¨\u0006\u0001\u0002\u0007\n\u0005\bF0\u0001¨\u0006\u0005"}, d2 = {"Lokhttp3/logging/HttpLoggingInterceptor$Logger$Companion;", "", "()V", "DEFAULT", "Lokhttp3/logging/HttpLoggingInterceptor$Logger;", "okhttp-logging-interceptor"}, k = 1, mv = {1, 1, 15})
        /* compiled from: HttpLoggingInterceptor.kt */
        public static final class Companion {
            static final /* synthetic */ Companion $$INSTANCE = null;

            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }

        void log(String str);
    }

    public HttpLoggingInterceptor() {
        this(null, 1, null);
    }

    public HttpLoggingInterceptor(Logger logger2) {
        Intrinsics.checkParameterIsNotNull(logger2, "logger");
        this.logger = logger2;
        this.headersToRedact = SetsKt.emptySet();
        this.level = Level.NONE;
    }

    public /* synthetic */ HttpLoggingInterceptor(Logger logger2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            logger2 = Logger.DEFAULT;
        }
        this(logger2);
    }

    public final Level getLevel() {
        return this.level;
    }

    public final void level(Level level2) {
        Intrinsics.checkParameterIsNotNull(level2, "<set-?>");
        this.level = level2;
    }

    public final void redactHeader(String str) {
        Intrinsics.checkParameterIsNotNull(str, "name");
        TreeSet treeSet = new TreeSet(StringsKt.getCASE_INSENSITIVE_ORDER(StringCompanionObject.INSTANCE));
        Collection collection = treeSet;
        CollectionsKt.addAll(collection, (Iterable<? extends T>) this.headersToRedact);
        collection.add(str);
        this.headersToRedact = treeSet;
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Moved to var. Replace setLevel(...) with level(...) to fix Java", replaceWith = @ReplaceWith(expression = "apply { this.level = level }", imports = {}))
    public final HttpLoggingInterceptor setLevel(Level level2) {
        Intrinsics.checkParameterIsNotNull(level2, "level");
        HttpLoggingInterceptor httpLoggingInterceptor = this;
        httpLoggingInterceptor.level = level2;
        return httpLoggingInterceptor;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to var", replaceWith = @ReplaceWith(expression = "level", imports = {}))
    /* renamed from: -deprecated_level reason: not valid java name */
    public final Level m1112deprecated_level() {
        return this.level;
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x0349, code lost:
        if (r2 != null) goto L_0x0351;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x016c, code lost:
        if (r10 != null) goto L_0x0174;
     */
    public Response intercept(Chain chain) throws IOException {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        Charset charset;
        Throwable th;
        Charset charset2;
        Chain chain2 = chain;
        Intrinsics.checkParameterIsNotNull(chain2, "chain");
        Level level2 = this.level;
        Request request = chain.request();
        if (level2 == Level.NONE) {
            return chain2.proceed(request);
        }
        boolean z = level2 == Level.BODY;
        boolean z2 = z || level2 == Level.HEADERS;
        RequestBody body = request.body();
        Connection connection = chain.connection();
        StringBuilder sb = new StringBuilder();
        sb.append("--> ");
        sb.append(request.method());
        sb.append(' ');
        sb.append(request.url());
        String str6 = "";
        if (connection != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" ");
            sb2.append(connection.protocol());
            str = sb2.toString();
        } else {
            str = str6;
        }
        sb.append(str);
        String sb3 = sb.toString();
        String str7 = "-byte body)";
        String str8 = " (";
        if (!z2 && body != null) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(sb3);
            sb4.append(str8);
            sb4.append(body.contentLength());
            sb4.append(str7);
            sb3 = sb4.toString();
        }
        this.logger.log(sb3);
        String str9 = "-byte body omitted)";
        String str10 = "UTF_8";
        if (z2) {
            Headers headers = request.headers();
            if (body != null) {
                MediaType contentType = body.contentType();
                if (contentType != null && headers.get("Content-Type") == null) {
                    Logger logger2 = this.logger;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Content-Type: ");
                    sb5.append(contentType);
                    logger2.log(sb5.toString());
                }
                if (body.contentLength() != -1 && headers.get("Content-Length") == null) {
                    Logger logger3 = this.logger;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("Content-Length: ");
                    sb6.append(body.contentLength());
                    logger3.log(sb6.toString());
                }
            }
            int size = headers.size();
            for (int i = 0; i < size; i++) {
                logHeader(headers, i);
            }
            String str11 = "--> END ";
            if (!z || body == null) {
                Logger logger4 = this.logger;
                StringBuilder sb7 = new StringBuilder();
                sb7.append(str11);
                sb7.append(request.method());
                logger4.log(sb7.toString());
            } else if (bodyHasUnknownEncoding(request.headers())) {
                Logger logger5 = this.logger;
                StringBuilder sb8 = new StringBuilder();
                sb8.append(str11);
                sb8.append(request.method());
                sb8.append(" (encoded body omitted)");
                logger5.log(sb8.toString());
            } else if (body.isDuplex()) {
                Logger logger6 = this.logger;
                StringBuilder sb9 = new StringBuilder();
                sb9.append(str11);
                sb9.append(request.method());
                sb9.append(" (duplex request body omitted)");
                logger6.log(sb9.toString());
            } else {
                Buffer buffer = new Buffer();
                body.writeTo(buffer);
                MediaType contentType2 = body.contentType();
                if (contentType2 != null) {
                    charset2 = contentType2.charset(StandardCharsets.UTF_8);
                }
                charset2 = StandardCharsets.UTF_8;
                Intrinsics.checkExpressionValueIsNotNull(charset2, str10);
                this.logger.log(str6);
                if (Utf8Kt.isProbablyUtf8(buffer)) {
                    this.logger.log(buffer.readString(charset2));
                    Logger logger7 = this.logger;
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append(str11);
                    sb10.append(request.method());
                    sb10.append(str8);
                    sb10.append(body.contentLength());
                    sb10.append(str7);
                    logger7.log(sb10.toString());
                } else {
                    Logger logger8 = this.logger;
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append(str11);
                    sb11.append(request.method());
                    sb11.append(" (binary ");
                    sb11.append(body.contentLength());
                    sb11.append(str9);
                    logger8.log(sb11.toString());
                }
            }
        }
        long nanoTime = System.nanoTime();
        try {
            Response proceed = chain2.proceed(request);
            long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - nanoTime);
            ResponseBody body2 = proceed.body();
            if (body2 == null) {
                Intrinsics.throwNpe();
            }
            long contentLength = body2.contentLength();
            if (contentLength != -1) {
                StringBuilder sb12 = new StringBuilder();
                sb12.append(contentLength);
                sb12.append("-byte");
                str2 = sb12.toString();
            } else {
                str2 = "unknown-length";
            }
            Logger logger9 = this.logger;
            String str12 = str7;
            StringBuilder sb13 = new StringBuilder();
            long j = contentLength;
            sb13.append("<-- ");
            sb13.append(proceed.code());
            if (proceed.message().length() == 0) {
                str3 = str9;
                str4 = str6;
            } else {
                String message = proceed.message();
                StringBuilder sb14 = new StringBuilder();
                str3 = str9;
                sb14.append(String.valueOf(' '));
                sb14.append(message);
                str4 = sb14.toString();
            }
            sb13.append(str4);
            sb13.append(' ');
            sb13.append(proceed.request().url());
            sb13.append(str8);
            sb13.append(millis);
            sb13.append("ms");
            if (!z2) {
                StringBuilder sb15 = new StringBuilder();
                sb15.append(", ");
                sb15.append(str2);
                sb15.append(" body");
                str5 = sb15.toString();
            } else {
                str5 = str6;
            }
            sb13.append(str5);
            sb13.append(')');
            logger9.log(sb13.toString());
            if (z2) {
                Headers headers2 = proceed.headers();
                int size2 = headers2.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    logHeader(headers2, i2);
                }
                if (!z || !HttpHeaders.promisesBody(proceed)) {
                    this.logger.log("<-- END HTTP");
                } else if (bodyHasUnknownEncoding(proceed.headers())) {
                    this.logger.log("<-- END HTTP (encoded body omitted)");
                } else {
                    BufferedSource source = body2.source();
                    source.request(LongCompanionObject.MAX_VALUE);
                    Buffer buffer2 = source.getBuffer();
                    Long l = null;
                    if (StringsKt.equals("gzip", headers2.get("Content-Encoding"), true)) {
                        l = Long.valueOf(buffer2.size());
                        Closeable gzipSource = new GzipSource(buffer2.clone());
                        Throwable th2 = null;
                        try {
                            GzipSource gzipSource2 = (GzipSource) gzipSource;
                            Buffer buffer3 = new Buffer();
                            buffer3.writeAll(gzipSource2);
                            CloseableKt.closeFinally(gzipSource, th2);
                            buffer2 = buffer3;
                        } catch (Throwable th3) {
                            Throwable th4 = th3;
                            CloseableKt.closeFinally(gzipSource, th);
                            throw th4;
                        }
                    }
                    MediaType contentType3 = body2.contentType();
                    if (contentType3 != null) {
                        charset = contentType3.charset(StandardCharsets.UTF_8);
                    }
                    charset = StandardCharsets.UTF_8;
                    Intrinsics.checkExpressionValueIsNotNull(charset, str10);
                    if (!Utf8Kt.isProbablyUtf8(buffer2)) {
                        this.logger.log(str6);
                        Logger logger10 = this.logger;
                        StringBuilder sb16 = new StringBuilder();
                        sb16.append("<-- END HTTP (binary ");
                        sb16.append(buffer2.size());
                        sb16.append(str3);
                        logger10.log(sb16.toString());
                        return proceed;
                    }
                    if (j != 0) {
                        this.logger.log(str6);
                        this.logger.log(buffer2.clone().readString(charset));
                    }
                    String str13 = "<-- END HTTP (";
                    if (l != null) {
                        Logger logger11 = this.logger;
                        StringBuilder sb17 = new StringBuilder();
                        sb17.append(str13);
                        sb17.append(buffer2.size());
                        sb17.append("-byte, ");
                        sb17.append(l);
                        sb17.append("-gzipped-byte body)");
                        logger11.log(sb17.toString());
                    } else {
                        Logger logger12 = this.logger;
                        StringBuilder sb18 = new StringBuilder();
                        sb18.append(str13);
                        sb18.append(buffer2.size());
                        sb18.append(str12);
                        logger12.log(sb18.toString());
                    }
                }
            }
            return proceed;
        } catch (Exception e) {
            Exception exc = e;
            Logger logger13 = this.logger;
            StringBuilder sb19 = new StringBuilder();
            sb19.append("<-- HTTP FAILED: ");
            sb19.append(exc);
            logger13.log(sb19.toString());
            throw exc;
        }
    }

    private final void logHeader(Headers headers, int i) {
        String value = this.headersToRedact.contains(headers.name(i)) ? "██" : headers.value(i);
        Logger logger2 = this.logger;
        StringBuilder sb = new StringBuilder();
        sb.append(headers.name(i));
        sb.append(": ");
        sb.append(value);
        logger2.log(sb.toString());
    }

    private final boolean bodyHasUnknownEncoding(Headers headers) {
        String str = headers.get("Content-Encoding");
        if (str == null || StringsKt.equals(str, "identity", true) || StringsKt.equals(str, "gzip", true)) {
            return false;
        }
        return true;
    }
}
