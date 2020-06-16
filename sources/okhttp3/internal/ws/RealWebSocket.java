package okhttp3.internal.ws;

import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs;
import java.io.Closeable;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RealCall;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.ws.WebSocketReader.FrameCallback;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000°\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u001d\u0018\u0000 ]2\u00020\u00012\u00020\u0002:\u0006[\\]^_`B%\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ\u0016\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020#2\u0006\u00101\u001a\u000202J\b\u00103\u001a\u00020/H\u0016J\u001f\u00104\u001a\u00020/2\u0006\u00105\u001a\u0002062\b\u00107\u001a\u0004\u0018\u000108H\u0000¢\u0006\u0002\b9J\u001a\u0010:\u001a\u00020\r2\u0006\u0010;\u001a\u00020#2\b\u0010<\u001a\u0004\u0018\u00010\u0017H\u0016J \u0010:\u001a\u00020\r2\u0006\u0010;\u001a\u00020#2\b\u0010<\u001a\u0004\u0018\u00010\u00172\u0006\u0010=\u001a\u00020\nJ\u000e\u0010>\u001a\u00020/2\u0006\u0010?\u001a\u00020@J\u001c\u0010A\u001a\u00020/2\n\u0010B\u001a\u00060Cj\u0002`D2\b\u00105\u001a\u0004\u0018\u000106J\u0016\u0010E\u001a\u00020/2\u0006\u0010F\u001a\u00020\u00172\u0006\u0010(\u001a\u00020)J\u0006\u0010G\u001a\u00020/J\u0018\u0010H\u001a\u00020/2\u0006\u0010;\u001a\u00020#2\u0006\u0010<\u001a\u00020\u0017H\u0016J\u0010\u0010I\u001a\u00020/2\u0006\u0010J\u001a\u00020\u0017H\u0016J\u0010\u0010I\u001a\u00020/2\u0006\u0010K\u001a\u00020\u001eH\u0016J\u0010\u0010L\u001a\u00020/2\u0006\u0010M\u001a\u00020\u001eH\u0016J\u0010\u0010N\u001a\u00020/2\u0006\u0010M\u001a\u00020\u001eH\u0016J\u000e\u0010O\u001a\u00020\r2\u0006\u0010M\u001a\u00020\u001eJ\u0006\u0010P\u001a\u00020\rJ\b\u0010\u001f\u001a\u00020\nH\u0016J\u0006\u0010%\u001a\u00020#J\u0006\u0010&\u001a\u00020#J\b\u0010Q\u001a\u00020\u0004H\u0016J\b\u0010R\u001a\u00020/H\u0002J\u0010\u0010S\u001a\u00020\r2\u0006\u0010J\u001a\u00020\u0017H\u0016J\u0010\u0010S\u001a\u00020\r2\u0006\u0010K\u001a\u00020\u001eH\u0016J\u0018\u0010S\u001a\u00020\r2\u0006\u0010T\u001a\u00020\u001e2\u0006\u0010U\u001a\u00020#H\u0002J\u0006\u0010'\u001a\u00020#J\u0006\u0010V\u001a\u00020/J\r\u0010W\u001a\u00020\rH\u0000¢\u0006\u0002\bXJ\r\u0010Y\u001a\u00020/H\u0000¢\u0006\u0002\bZR\u000e\u0010\f\u001a\u00020\rX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u0011X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\rX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\rX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u0004\u0018\u00010!X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u0004\u0018\u00010\u0017X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020#X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020#X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020#X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010(\u001a\u0004\u0018\u00010)X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u0004\u0018\u00010+X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020-X\u0004¢\u0006\u0002\n\u0000¨\u0006a"}, d2 = {"Lokhttp3/internal/ws/RealWebSocket;", "Lokhttp3/WebSocket;", "Lokhttp3/internal/ws/WebSocketReader$FrameCallback;", "originalRequest", "Lokhttp3/Request;", "listener", "Lokhttp3/WebSocketListener;", "random", "Ljava/util/Random;", "pingIntervalMillis", "", "(Lokhttp3/Request;Lokhttp3/WebSocketListener;Ljava/util/Random;J)V", "awaitingPong", "", "call", "Lokhttp3/Call;", "cancelFuture", "Ljava/util/concurrent/ScheduledFuture;", "enqueuedClose", "executor", "Ljava/util/concurrent/ScheduledExecutorService;", "failed", "key", "", "getListener$okhttp", "()Lokhttp3/WebSocketListener;", "messageAndCloseQueue", "Ljava/util/ArrayDeque;", "", "pongQueue", "Lokio/ByteString;", "queueSize", "reader", "Lokhttp3/internal/ws/WebSocketReader;", "receivedCloseCode", "", "receivedCloseReason", "receivedPingCount", "receivedPongCount", "sentPingCount", "streams", "Lokhttp3/internal/ws/RealWebSocket$Streams;", "writer", "Lokhttp3/internal/ws/WebSocketWriter;", "writerRunnable", "Ljava/lang/Runnable;", "awaitTermination", "", "timeout", "timeUnit", "Ljava/util/concurrent/TimeUnit;", "cancel", "checkUpgradeSuccess", "response", "Lokhttp3/Response;", "exchange", "Lokhttp3/internal/connection/Exchange;", "checkUpgradeSuccess$okhttp", "close", "code", "reason", "cancelAfterCloseMillis", "connect", "client", "Lokhttp3/OkHttpClient;", "failWebSocket", "e", "Ljava/lang/Exception;", "Lkotlin/Exception;", "initReaderAndWriter", "name", "loopReader", "onReadClose", "onReadMessage", "text", "bytes", "onReadPing", "payload", "onReadPong", "pong", "processNextFrame", "request", "runWriter", "send", "data", "formatOpcode", "tearDown", "writeOneFrame", "writeOneFrame$okhttp", "writePingFrame", "writePingFrame$okhttp", "CancelRunnable", "Close", "Companion", "Message", "PingRunnable", "Streams", "okhttp"}, k = 1, mv = {1, 1, 15})
/* compiled from: RealWebSocket.kt */
public final class RealWebSocket implements WebSocket, FrameCallback {
    private static final long CANCEL_AFTER_CLOSE_MILLIS = 60000;
    public static final Companion Companion = new Companion(null);
    private static final long MAX_QUEUE_SIZE = 16777216;
    private static final List<Protocol> ONLY_HTTP1 = CollectionsKt.listOf(Protocol.HTTP_1_1);
    private boolean awaitingPong;
    private Call call;
    private ScheduledFuture<?> cancelFuture;
    private boolean enqueuedClose;
    private ScheduledExecutorService executor;
    private boolean failed;
    private final String key;
    private final WebSocketListener listener;
    private final ArrayDeque<Object> messageAndCloseQueue = new ArrayDeque<>();
    private final Request originalRequest;
    private final long pingIntervalMillis;
    private final ArrayDeque<ByteString> pongQueue = new ArrayDeque<>();
    private long queueSize;
    private final Random random;
    private WebSocketReader reader;
    private int receivedCloseCode = -1;
    private String receivedCloseReason;
    private int receivedPingCount;
    private int receivedPongCount;
    private int sentPingCount;
    private Streams streams;
    private WebSocketWriter writer;
    private final Runnable writerRunnable;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016¨\u0006\u0005"}, d2 = {"Lokhttp3/internal/ws/RealWebSocket$CancelRunnable;", "Ljava/lang/Runnable;", "(Lokhttp3/internal/ws/RealWebSocket;)V", "run", "", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: RealWebSocket.kt */
    public final class CancelRunnable implements Runnable {
        public CancelRunnable() {
        }

        public void run() {
            RealWebSocket.this.cancel();
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\b\b\u0000\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u000f"}, d2 = {"Lokhttp3/internal/ws/RealWebSocket$Close;", "", "code", "", "reason", "Lokio/ByteString;", "cancelAfterCloseMillis", "", "(ILokio/ByteString;J)V", "getCancelAfterCloseMillis", "()J", "getCode", "()I", "getReason", "()Lokio/ByteString;", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: RealWebSocket.kt */
    public static final class Close {
        private final long cancelAfterCloseMillis;
        private final int code;
        private final ByteString reason;

        public Close(int i, ByteString byteString, long j) {
            this.code = i;
            this.reason = byteString;
            this.cancelAfterCloseMillis = j;
        }

        public final int getCode() {
            return this.code;
        }

        public final ByteString getReason() {
            return this.reason;
        }

        public final long getCancelAfterCloseMillis() {
            return this.cancelAfterCloseMillis;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lokhttp3/internal/ws/RealWebSocket$Companion;", "", "()V", "CANCEL_AFTER_CLOSE_MILLIS", "", "MAX_QUEUE_SIZE", "ONLY_HTTP1", "", "Lokhttp3/Protocol;", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: RealWebSocket.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"Lokhttp3/internal/ws/RealWebSocket$Message;", "", "formatOpcode", "", "data", "Lokio/ByteString;", "(ILokio/ByteString;)V", "getData", "()Lokio/ByteString;", "getFormatOpcode", "()I", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: RealWebSocket.kt */
    public static final class Message {
        private final ByteString data;
        private final int formatOpcode;

        public Message(int i, ByteString byteString) {
            Intrinsics.checkParameterIsNotNull(byteString, "data");
            this.formatOpcode = i;
            this.data = byteString;
        }

        public final int getFormatOpcode() {
            return this.formatOpcode;
        }

        public final ByteString getData() {
            return this.data;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016¨\u0006\u0005"}, d2 = {"Lokhttp3/internal/ws/RealWebSocket$PingRunnable;", "Ljava/lang/Runnable;", "(Lokhttp3/internal/ws/RealWebSocket;)V", "run", "", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: RealWebSocket.kt */
    private final class PingRunnable implements Runnable {
        public PingRunnable() {
        }

        public void run() {
            RealWebSocket.this.writePingFrame$okhttp();
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b&\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u000f"}, d2 = {"Lokhttp3/internal/ws/RealWebSocket$Streams;", "Ljava/io/Closeable;", "client", "", "source", "Lokio/BufferedSource;", "sink", "Lokio/BufferedSink;", "(ZLokio/BufferedSource;Lokio/BufferedSink;)V", "getClient", "()Z", "getSink", "()Lokio/BufferedSink;", "getSource", "()Lokio/BufferedSource;", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: RealWebSocket.kt */
    public static abstract class Streams implements Closeable {
        private final boolean client;
        private final BufferedSink sink;
        private final BufferedSource source;

        public Streams(boolean z, BufferedSource bufferedSource, BufferedSink bufferedSink) {
            Intrinsics.checkParameterIsNotNull(bufferedSource, "source");
            Intrinsics.checkParameterIsNotNull(bufferedSink, "sink");
            this.client = z;
            this.source = bufferedSource;
            this.sink = bufferedSink;
        }

        public final boolean getClient() {
            return this.client;
        }

        public final BufferedSource getSource() {
            return this.source;
        }

        public final BufferedSink getSink() {
            return this.sink;
        }
    }

    public RealWebSocket(Request request, WebSocketListener webSocketListener, Random random2, long j) {
        Intrinsics.checkParameterIsNotNull(request, "originalRequest");
        Intrinsics.checkParameterIsNotNull(webSocketListener, CastExtraArgs.LISTENER);
        Intrinsics.checkParameterIsNotNull(random2, "random");
        this.originalRequest = request;
        this.listener = webSocketListener;
        this.random = random2;
        this.pingIntervalMillis = j;
        if (Intrinsics.areEqual((Object) "GET", (Object) this.originalRequest.method())) {
            okio.ByteString.Companion companion = ByteString.Companion;
            byte[] bArr = new byte[16];
            this.random.nextBytes(bArr);
            this.key = okio.ByteString.Companion.of$default(companion, bArr, 0, 0, 3, null).base64();
            this.writerRunnable = new Runnable(this) {
                final /* synthetic */ RealWebSocket this$0;

                {
                    this.this$0 = r1;
                }

                public final void run() {
                    do {
                        try {
                        } catch (IOException e) {
                            this.this$0.failWebSocket(e, null);
                            return;
                        }
                    } while (this.this$0.writeOneFrame$okhttp());
                }
            };
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Request must be GET: ");
        sb.append(this.originalRequest.method());
        throw new IllegalArgumentException(sb.toString().toString());
    }

    public final WebSocketListener getListener$okhttp() {
        return this.listener;
    }

    public Request request() {
        return this.originalRequest;
    }

    public synchronized long queueSize() {
        return this.queueSize;
    }

    public void cancel() {
        Call call2 = this.call;
        if (call2 == null) {
            Intrinsics.throwNpe();
        }
        call2.cancel();
    }

    public final void connect(OkHttpClient okHttpClient) {
        Intrinsics.checkParameterIsNotNull(okHttpClient, "client");
        OkHttpClient build = okHttpClient.newBuilder().eventListener(EventListener.NONE).protocols(ONLY_HTTP1).build();
        String str = "Upgrade";
        Request build2 = this.originalRequest.newBuilder().header(str, "websocket").header("Connection", str).header("Sec-WebSocket-Key", this.key).header("Sec-WebSocket-Version", "13").build();
        Call newRealCall = RealCall.Companion.newRealCall(build, build2, true);
        this.call = newRealCall;
        if (newRealCall == null) {
            Intrinsics.throwNpe();
        }
        newRealCall.enqueue(new RealWebSocket$connect$1(this, build2));
    }

    public final void checkUpgradeSuccess$okhttp(Response response, Exchange exchange) throws IOException {
        Intrinsics.checkParameterIsNotNull(response, "response");
        if (response.code() == 101) {
            String header$default = Response.header$default(response, "Connection", null, 2, null);
            String str = "Upgrade";
            if (StringsKt.equals(str, header$default, true)) {
                String header$default2 = Response.header$default(response, str, null, 2, null);
                if (StringsKt.equals("websocket", header$default2, true)) {
                    String header$default3 = Response.header$default(response, "Sec-WebSocket-Accept", null, 2, null);
                    okio.ByteString.Companion companion = ByteString.Companion;
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.key);
                    sb.append(WebSocketProtocol.ACCEPT_MAGIC);
                    String base64 = companion.encodeUtf8(sb.toString()).sha1().base64();
                    if (!Intrinsics.areEqual((Object) base64, (Object) header$default3)) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Expected 'Sec-WebSocket-Accept' header value '");
                        sb2.append(base64);
                        sb2.append("' but was '");
                        sb2.append(header$default3);
                        sb2.append('\'');
                        throw new ProtocolException(sb2.toString());
                    } else if (exchange == null) {
                        throw new ProtocolException("Web Socket exchange missing: bad interceptor?");
                    }
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Expected 'Upgrade' header value 'websocket' but was '");
                    sb3.append(header$default2);
                    sb3.append('\'');
                    throw new ProtocolException(sb3.toString());
                }
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Expected 'Connection' header value 'Upgrade' but was '");
                sb4.append(header$default);
                sb4.append('\'');
                throw new ProtocolException(sb4.toString());
            }
        } else {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Expected HTTP 101 response but was '");
            sb5.append(response.code());
            sb5.append(' ');
            sb5.append(response.message());
            sb5.append('\'');
            throw new ProtocolException(sb5.toString());
        }
    }

    public final void initReaderAndWriter(String str, Streams streams2) throws IOException {
        Intrinsics.checkParameterIsNotNull(str, "name");
        Intrinsics.checkParameterIsNotNull(streams2, "streams");
        synchronized (this) {
            this.streams = streams2;
            this.writer = new WebSocketWriter(streams2.getClient(), streams2.getSink(), this.random);
            ScheduledExecutorService scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(str, false));
            this.executor = scheduledThreadPoolExecutor;
            if (this.pingIntervalMillis != 0) {
                if (scheduledThreadPoolExecutor == null) {
                    Intrinsics.throwNpe();
                }
                scheduledThreadPoolExecutor.scheduleAtFixedRate(new PingRunnable(), this.pingIntervalMillis, this.pingIntervalMillis, TimeUnit.MILLISECONDS);
            }
            if (!this.messageAndCloseQueue.isEmpty()) {
                runWriter();
            }
            Unit unit = Unit.INSTANCE;
        }
        this.reader = new WebSocketReader(streams2.getClient(), streams2.getSource(), this);
    }

    public final void loopReader() throws IOException {
        while (this.receivedCloseCode == -1) {
            WebSocketReader webSocketReader = this.reader;
            if (webSocketReader == null) {
                Intrinsics.throwNpe();
            }
            webSocketReader.processNextFrame();
        }
    }

    public final boolean processNextFrame() throws IOException {
        try {
            WebSocketReader webSocketReader = this.reader;
            if (webSocketReader == null) {
                Intrinsics.throwNpe();
            }
            webSocketReader.processNextFrame();
            if (this.receivedCloseCode == -1) {
                return true;
            }
            return false;
        } catch (Exception e) {
            failWebSocket(e, null);
            return false;
        }
    }

    public final void awaitTermination(int i, TimeUnit timeUnit) throws InterruptedException {
        Intrinsics.checkParameterIsNotNull(timeUnit, "timeUnit");
        ScheduledExecutorService scheduledExecutorService = this.executor;
        if (scheduledExecutorService == null) {
            Intrinsics.throwNpe();
        }
        scheduledExecutorService.awaitTermination((long) i, timeUnit);
    }

    public final void tearDown() throws InterruptedException {
        ScheduledFuture<?> scheduledFuture = this.cancelFuture;
        if (scheduledFuture != null) {
            if (scheduledFuture == null) {
                Intrinsics.throwNpe();
            }
            scheduledFuture.cancel(false);
        }
        ScheduledExecutorService scheduledExecutorService = this.executor;
        if (scheduledExecutorService == null) {
            Intrinsics.throwNpe();
        }
        scheduledExecutorService.shutdown();
        ScheduledExecutorService scheduledExecutorService2 = this.executor;
        if (scheduledExecutorService2 == null) {
            Intrinsics.throwNpe();
        }
        scheduledExecutorService2.awaitTermination(10, TimeUnit.SECONDS);
    }

    public final synchronized int sentPingCount() {
        return this.sentPingCount;
    }

    public final synchronized int receivedPingCount() {
        return this.receivedPingCount;
    }

    public final synchronized int receivedPongCount() {
        return this.receivedPongCount;
    }

    public void onReadMessage(String str) throws IOException {
        Intrinsics.checkParameterIsNotNull(str, "text");
        this.listener.onMessage((WebSocket) this, str);
    }

    public void onReadMessage(ByteString byteString) throws IOException {
        Intrinsics.checkParameterIsNotNull(byteString, "bytes");
        this.listener.onMessage((WebSocket) this, byteString);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0028, code lost:
        return;
     */
    public synchronized void onReadPing(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "payload");
        if (!this.failed) {
            if (!this.enqueuedClose || !this.messageAndCloseQueue.isEmpty()) {
                this.pongQueue.add(byteString);
                runWriter();
                this.receivedPingCount++;
            }
        }
    }

    public synchronized void onReadPong(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "payload");
        this.receivedPongCount++;
        this.awaitingPong = false;
    }

    /* JADX INFO: finally extract failed */
    public void onReadClose(int i, String str) {
        Intrinsics.checkParameterIsNotNull(str, "reason");
        boolean z = true;
        if (i != -1) {
            Streams streams2 = null;
            synchronized (this) {
                if (this.receivedCloseCode != -1) {
                    z = false;
                }
                if (z) {
                    this.receivedCloseCode = i;
                    this.receivedCloseReason = str;
                    if (this.enqueuedClose && this.messageAndCloseQueue.isEmpty()) {
                        streams2 = this.streams;
                        this.streams = null;
                        if (this.cancelFuture != null) {
                            ScheduledFuture<?> scheduledFuture = this.cancelFuture;
                            if (scheduledFuture == null) {
                                Intrinsics.throwNpe();
                            }
                            scheduledFuture.cancel(false);
                        }
                        ScheduledExecutorService scheduledExecutorService = this.executor;
                        if (scheduledExecutorService == null) {
                            Intrinsics.throwNpe();
                        }
                        scheduledExecutorService.shutdown();
                    }
                    Unit unit = Unit.INSTANCE;
                } else {
                    throw new IllegalStateException("already closed".toString());
                }
            }
            try {
                this.listener.onClosing(this, i, str);
                if (streams2 != null) {
                    this.listener.onClosed(this, i, str);
                }
                if (streams2 != null) {
                    Util.closeQuietly((Closeable) streams2);
                }
            } catch (Throwable th) {
                if (streams2 != null) {
                    Util.closeQuietly((Closeable) streams2);
                }
                throw th;
            }
        } else {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
    }

    public boolean send(String str) {
        Intrinsics.checkParameterIsNotNull(str, "text");
        return send(ByteString.Companion.encodeUtf8(str), 1);
    }

    public boolean send(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "bytes");
        return send(byteString, 2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003d, code lost:
        return false;
     */
    private final synchronized boolean send(ByteString byteString, int i) {
        if (!this.failed) {
            if (!this.enqueuedClose) {
                if (this.queueSize + ((long) byteString.size()) > MAX_QUEUE_SIZE) {
                    close(1001, null);
                    return false;
                }
                this.queueSize += (long) byteString.size();
                this.messageAndCloseQueue.add(new Message(i, byteString));
                runWriter();
                return true;
            }
        }
    }

    public final synchronized boolean pong(ByteString byteString) {
        Intrinsics.checkParameterIsNotNull(byteString, "payload");
        if (!this.failed) {
            if (!this.enqueuedClose || !this.messageAndCloseQueue.isEmpty()) {
                this.pongQueue.add(byteString);
                runWriter();
                return true;
            }
        }
        return false;
    }

    public boolean close(int i, String str) {
        return close(i, str, CANCEL_AFTER_CLOSE_MILLIS);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005c, code lost:
        return false;
     */
    public final synchronized boolean close(int i, String str, long j) {
        WebSocketProtocol.INSTANCE.validateCloseCode(i);
        ByteString byteString = null;
        if (str != null) {
            byteString = ByteString.Companion.encodeUtf8(str);
            if (!(((long) byteString.size()) <= 123)) {
                StringBuilder sb = new StringBuilder();
                sb.append("reason.size() > 123: ");
                sb.append(str);
                throw new IllegalArgumentException(sb.toString().toString());
            }
        }
        if (!this.failed) {
            if (!this.enqueuedClose) {
                this.enqueuedClose = true;
                this.messageAndCloseQueue.add(new Close(i, byteString, j));
                runWriter();
                return true;
            }
        }
    }

    private final void runWriter() {
        boolean holdsLock = Thread.holdsLock(this);
        if (!_Assertions.ENABLED || holdsLock) {
            ScheduledExecutorService scheduledExecutorService = this.executor;
            if (scheduledExecutorService != null) {
                scheduledExecutorService.execute(this.writerRunnable);
                return;
            }
            return;
        }
        throw new AssertionError("Assertion failed");
    }

    /* JADX WARNING: type inference failed for: r0v18 */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0065, code lost:
        if (r5 == null) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0067, code lost:
        if (r3 != null) goto L_0x006c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        kotlin.jvm.internal.Intrinsics.throwNpe();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006c, code lost:
        r3.writePong(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0072, code lost:
        if ((r0 instanceof okhttp3.internal.ws.RealWebSocket.Message) == false) goto L_0x00ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0074, code lost:
        r1 = r0.getData();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x007b, code lost:
        if (r3 != null) goto L_0x0080;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x007d, code lost:
        kotlin.jvm.internal.Intrinsics.throwNpe();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0080, code lost:
        r0 = okio.Okio.buffer(r3.newMessageSink(r0.getFormatOpcode(), (long) r1.size()));
        r0.write(r1);
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0099, code lost:
        monitor-enter(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
        r11.queueSize -= (long) r1.size();
        r0 = kotlin.Unit.INSTANCE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:?, code lost:
        monitor-exit(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00ad, code lost:
        if ((r0 instanceof okhttp3.internal.ws.RealWebSocket.Close) == false) goto L_0x00d9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00af, code lost:
        r0 = (okhttp3.internal.ws.RealWebSocket.Close) r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00b1, code lost:
        if (r3 != null) goto L_0x00b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00b3, code lost:
        kotlin.jvm.internal.Intrinsics.throwNpe();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00b6, code lost:
        r3.writeClose(r0.getCode(), r0.getReason());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00c1, code lost:
        if (r2 == null) goto L_0x00d0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00c3, code lost:
        r0 = r11.listener;
        r3 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00c8, code lost:
        if (r1 != null) goto L_0x00cd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00ca, code lost:
        kotlin.jvm.internal.Intrinsics.throwNpe();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00cd, code lost:
        r0.onClosed(r3, r6, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00d1, code lost:
        if (r2 == null) goto L_0x00d8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00d3, code lost:
        okhttp3.internal.Util.closeQuietly((java.io.Closeable) r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00d8, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x00e0, code lost:
        throw new java.lang.AssertionError();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00e1, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00e2, code lost:
        if (r2 != null) goto L_0x00e4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00e4, code lost:
        okhttp3.internal.Util.closeQuietly((java.io.Closeable) r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x00e9, code lost:
        throw r0;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    public final boolean writeOneFrame$okhttp() throws IOException {
        Message message = null;
        String str = null;
        Streams streams2 = null;
        synchronized (this) {
            if (this.failed) {
                return false;
            }
            WebSocketWriter webSocketWriter = this.writer;
            ByteString byteString = (ByteString) this.pongQueue.poll();
            int i = -1;
            if (byteString == null) {
                Object poll = this.messageAndCloseQueue.poll();
                if (poll instanceof Close) {
                    int i2 = this.receivedCloseCode;
                    String str2 = this.receivedCloseReason;
                    if (i2 != -1) {
                        streams2 = this.streams;
                        this.streams = null;
                        ScheduledExecutorService scheduledExecutorService = this.executor;
                        if (scheduledExecutorService == null) {
                            Intrinsics.throwNpe();
                        }
                        scheduledExecutorService.shutdown();
                    } else {
                        ScheduledExecutorService scheduledExecutorService2 = this.executor;
                        if (scheduledExecutorService2 == null) {
                            Intrinsics.throwNpe();
                        }
                        this.cancelFuture = scheduledExecutorService2.schedule(new CancelRunnable(), ((Close) poll).getCancelAfterCloseMillis(), TimeUnit.MILLISECONDS);
                    }
                    i = i2;
                    str = str2;
                } else if (poll == 0) {
                    return false;
                }
                message = poll;
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001e, code lost:
        if (r1 == -1) goto L_0x004c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0020, code lost:
        r2 = new java.lang.StringBuilder();
        r2.append("sent ping but didn't receive pong within ");
        r2.append(r7.pingIntervalMillis);
        r2.append("ms (after ");
        r2.append(r1 - 1);
        r2.append(" successful ping/pongs)");
        failWebSocket(new java.net.SocketTimeoutException(r2.toString()), null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004b, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004c, code lost:
        if (r0 != null) goto L_0x0051;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        kotlin.jvm.internal.Intrinsics.throwNpe();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0051, code lost:
        r0.writePing(okio.ByteString.EMPTY);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0057, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0058, code lost:
        failWebSocket(r0, null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005d, code lost:
        return;
     */
    public final void writePingFrame$okhttp() {
        synchronized (this) {
            if (!this.failed) {
                WebSocketWriter webSocketWriter = this.writer;
                int i = this.awaitingPong ? this.sentPingCount : -1;
                this.sentPingCount++;
                this.awaitingPong = true;
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        r3.listener.onFailure(r3, r4, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0039, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003a, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003b, code lost:
        if (r0 != null) goto L_0x003d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003d, code lost:
        okhttp3.internal.Util.closeQuietly((java.io.Closeable) r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0042, code lost:
        throw r4;
     */
    public final void failWebSocket(Exception exc, Response response) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        synchronized (this) {
            if (!this.failed) {
                this.failed = true;
                Streams streams2 = this.streams;
                this.streams = null;
                ScheduledFuture<?> scheduledFuture = this.cancelFuture;
                if (scheduledFuture != null) {
                    scheduledFuture.cancel(false);
                }
                ScheduledExecutorService scheduledExecutorService = this.executor;
                if (scheduledExecutorService != null) {
                    scheduledExecutorService.shutdown();
                    Unit unit = Unit.INSTANCE;
                }
            }
        }
    }
}
