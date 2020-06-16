package okhttp3.internal.connection;

import androidx.core.app.NotificationCompat;
import com.google.android.gms.common.internal.ImagesContract;
import java.io.IOException;
import java.lang.ref.Reference;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownServiceException;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.LongCompanionObject;
import kotlin.text.StringsKt;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.CertificatePinner;
import okhttp3.Connection;
import okhttp3.ConnectionSpec;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.HttpUrl;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.http1.Http1ExchangeCodec;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.Http2Connection;
import okhttp3.internal.http2.Http2Connection.Builder;
import okhttp3.internal.http2.Http2Connection.Listener;
import okhttp3.internal.http2.Http2ExchangeCodec;
import okhttp3.internal.http2.Http2Stream;
import okhttp3.internal.http2.StreamResetException;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.tls.OkHostnameVerifier;
import okhttp3.internal.ws.RealWebSocket.Streams;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000ä\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 p2\u00020\u00012\u00020\u0002:\u0001pB\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0006\u00105\u001a\u000206J>\u00107\u001a\u0002062\u0006\u00108\u001a\u00020\t2\u0006\u00109\u001a\u00020\t2\u0006\u0010:\u001a\u00020\t2\u0006\u0010;\u001a\u00020\t2\u0006\u0010<\u001a\u00020\u00172\u0006\u0010=\u001a\u00020>2\u0006\u0010?\u001a\u00020@J(\u0010A\u001a\u0002062\u0006\u00108\u001a\u00020\t2\u0006\u00109\u001a\u00020\t2\u0006\u0010=\u001a\u00020>2\u0006\u0010?\u001a\u00020@H\u0002J\u0010\u0010B\u001a\u0002062\u0006\u0010C\u001a\u00020DH\u0002J0\u0010E\u001a\u0002062\u0006\u00108\u001a\u00020\t2\u0006\u00109\u001a\u00020\t2\u0006\u0010:\u001a\u00020\t2\u0006\u0010=\u001a\u00020>2\u0006\u0010?\u001a\u00020@H\u0002J*\u0010F\u001a\u0004\u0018\u00010G2\u0006\u00109\u001a\u00020\t2\u0006\u0010:\u001a\u00020\t2\u0006\u0010H\u001a\u00020G2\u0006\u0010I\u001a\u00020JH\u0002J\b\u0010K\u001a\u00020GH\u0002J(\u0010L\u001a\u0002062\u0006\u0010C\u001a\u00020D2\u0006\u0010;\u001a\u00020\t2\u0006\u0010=\u001a\u00020>2\u0006\u0010?\u001a\u00020@H\u0002J\n\u0010\f\u001a\u0004\u0018\u00010\rH\u0016J%\u0010M\u001a\u00020\u00172\u0006\u0010N\u001a\u00020O2\u000e\u0010P\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010QH\u0000¢\u0006\u0002\bRJ\u000e\u0010S\u001a\u00020\u00172\u0006\u0010T\u001a\u00020\u0017J\u001d\u0010U\u001a\u00020V2\u0006\u0010W\u001a\u00020X2\u0006\u0010Y\u001a\u00020ZH\u0000¢\u0006\u0002\b[J\u0015\u0010\\\u001a\u00020]2\u0006\u0010^\u001a\u00020_H\u0000¢\u0006\u0002\b`J\u0006\u0010\u0019\u001a\u000206J\u0010\u0010a\u001a\u0002062\u0006\u0010b\u001a\u00020\u000fH\u0016J\u0010\u0010c\u001a\u0002062\u0006\u0010d\u001a\u00020eH\u0016J\b\u0010\u001d\u001a\u00020\u001eH\u0016J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\u0016\u0010f\u001a\u00020\u00172\f\u0010g\u001a\b\u0012\u0004\u0012\u00020\u00060QH\u0002J\b\u0010)\u001a\u00020 H\u0016J\u0010\u0010h\u001a\u0002062\u0006\u0010;\u001a\u00020\tH\u0002J\u000e\u0010i\u001a\u00020\u00172\u0006\u0010I\u001a\u00020JJ\b\u0010j\u001a\u00020kH\u0016J\u0017\u0010l\u001a\u0002062\b\u0010m\u001a\u0004\u0018\u00010nH\u0000¢\u0006\u0002\boR\u000e\u0010\b\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\u00020\u0011X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0016\u001a\u00020\u00178F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0018R\u001a\u0010\u0019\u001a\u00020\u0017X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0018\"\u0004\b\u001b\u0010\u001cR\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001f\u001a\u0004\u0018\u00010 X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\"\u001a\u00020\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u0010\u0010'\u001a\u0004\u0018\u00010(X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u0004\u0018\u00010 X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u0004\u0018\u00010+X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010,\u001a\u00020\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010$\"\u0004\b.\u0010&R\u001d\u0010/\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002020100¢\u0006\b\n\u0000\u001a\u0004\b3\u00104¨\u0006q"}, d2 = {"Lokhttp3/internal/connection/RealConnection;", "Lokhttp3/internal/http2/Http2Connection$Listener;", "Lokhttp3/Connection;", "connectionPool", "Lokhttp3/internal/connection/RealConnectionPool;", "route", "Lokhttp3/Route;", "(Lokhttp3/internal/connection/RealConnectionPool;Lokhttp3/Route;)V", "allocationLimit", "", "getConnectionPool", "()Lokhttp3/internal/connection/RealConnectionPool;", "handshake", "Lokhttp3/Handshake;", "http2Connection", "Lokhttp3/internal/http2/Http2Connection;", "idleAtNanos", "", "getIdleAtNanos$okhttp", "()J", "setIdleAtNanos$okhttp", "(J)V", "isMultiplexed", "", "()Z", "noNewExchanges", "getNoNewExchanges", "setNoNewExchanges", "(Z)V", "protocol", "Lokhttp3/Protocol;", "rawSocket", "Ljava/net/Socket;", "refusedStreamCount", "routeFailureCount", "getRouteFailureCount$okhttp", "()I", "setRouteFailureCount$okhttp", "(I)V", "sink", "Lokio/BufferedSink;", "socket", "source", "Lokio/BufferedSource;", "successCount", "getSuccessCount$okhttp", "setSuccessCount$okhttp", "transmitters", "", "Ljava/lang/ref/Reference;", "Lokhttp3/internal/connection/Transmitter;", "getTransmitters", "()Ljava/util/List;", "cancel", "", "connect", "connectTimeout", "readTimeout", "writeTimeout", "pingIntervalMillis", "connectionRetryEnabled", "call", "Lokhttp3/Call;", "eventListener", "Lokhttp3/EventListener;", "connectSocket", "connectTls", "connectionSpecSelector", "Lokhttp3/internal/connection/ConnectionSpecSelector;", "connectTunnel", "createTunnel", "Lokhttp3/Request;", "tunnelRequest", "url", "Lokhttp3/HttpUrl;", "createTunnelRequest", "establishProtocol", "isEligible", "address", "Lokhttp3/Address;", "routes", "", "isEligible$okhttp", "isHealthy", "doExtensiveChecks", "newCodec", "Lokhttp3/internal/http/ExchangeCodec;", "client", "Lokhttp3/OkHttpClient;", "chain", "Lokhttp3/Interceptor$Chain;", "newCodec$okhttp", "newWebSocketStreams", "Lokhttp3/internal/ws/RealWebSocket$Streams;", "exchange", "Lokhttp3/internal/connection/Exchange;", "newWebSocketStreams$okhttp", "onSettings", "connection", "onStream", "stream", "Lokhttp3/internal/http2/Http2Stream;", "routeMatchesAny", "candidates", "startHttp2", "supportsUrl", "toString", "", "trackFailure", "e", "Ljava/io/IOException;", "trackFailure$okhttp", "Companion", "okhttp"}, k = 1, mv = {1, 1, 15})
/* compiled from: RealConnection.kt */
public final class RealConnection extends Listener implements Connection {
    public static final Companion Companion = new Companion(null);
    private static final int MAX_TUNNEL_ATTEMPTS = 21;
    private static final String NPE_THROW_WITH_NULL = "throw with null exception";
    private int allocationLimit = 1;
    private final RealConnectionPool connectionPool;
    /* access modifiers changed from: private */
    public Handshake handshake;
    private Http2Connection http2Connection;
    private long idleAtNanos = LongCompanionObject.MAX_VALUE;
    private boolean noNewExchanges;
    private Protocol protocol;
    private Socket rawSocket;
    private int refusedStreamCount;
    private final Route route;
    private int routeFailureCount;
    private BufferedSink sink;
    /* access modifiers changed from: private */
    public Socket socket;
    private BufferedSource source;
    private int successCount;
    private final List<Reference<Transmitter>> transmitters = new ArrayList();

    @Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J&\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lokhttp3/internal/connection/RealConnection$Companion;", "", "()V", "MAX_TUNNEL_ATTEMPTS", "", "NPE_THROW_WITH_NULL", "", "newTestConnection", "Lokhttp3/internal/connection/RealConnection;", "connectionPool", "Lokhttp3/internal/connection/RealConnectionPool;", "route", "Lokhttp3/Route;", "socket", "Ljava/net/Socket;", "idleAtNanos", "", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: RealConnection.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final RealConnection newTestConnection(RealConnectionPool realConnectionPool, Route route, Socket socket, long j) {
            Intrinsics.checkParameterIsNotNull(realConnectionPool, "connectionPool");
            Intrinsics.checkParameterIsNotNull(route, "route");
            Intrinsics.checkParameterIsNotNull(socket, "socket");
            RealConnection realConnection = new RealConnection(realConnectionPool, route);
            realConnection.socket = socket;
            realConnection.setIdleAtNanos$okhttp(j);
            return realConnection;
        }
    }

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 15})
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;

        static {
            int[] iArr = new int[Type.values().length];
            $EnumSwitchMapping$0 = iArr;
            iArr[Type.DIRECT.ordinal()] = 1;
            $EnumSwitchMapping$0[Type.HTTP.ordinal()] = 2;
            int[] iArr2 = new int[ErrorCode.values().length];
            $EnumSwitchMapping$1 = iArr2;
            iArr2[ErrorCode.REFUSED_STREAM.ordinal()] = 1;
            $EnumSwitchMapping$1[ErrorCode.CANCEL.ordinal()] = 2;
        }
    }

    public final RealConnectionPool getConnectionPool() {
        return this.connectionPool;
    }

    public RealConnection(RealConnectionPool realConnectionPool, Route route2) {
        Intrinsics.checkParameterIsNotNull(realConnectionPool, "connectionPool");
        Intrinsics.checkParameterIsNotNull(route2, "route");
        this.connectionPool = realConnectionPool;
        this.route = route2;
    }

    public final boolean getNoNewExchanges() {
        return this.noNewExchanges;
    }

    public final void setNoNewExchanges(boolean z) {
        this.noNewExchanges = z;
    }

    public final int getRouteFailureCount$okhttp() {
        return this.routeFailureCount;
    }

    public final void setRouteFailureCount$okhttp(int i) {
        this.routeFailureCount = i;
    }

    public final int getSuccessCount$okhttp() {
        return this.successCount;
    }

    public final void setSuccessCount$okhttp(int i) {
        this.successCount = i;
    }

    public final List<Reference<Transmitter>> getTransmitters() {
        return this.transmitters;
    }

    public final long getIdleAtNanos$okhttp() {
        return this.idleAtNanos;
    }

    public final void setIdleAtNanos$okhttp(long j) {
        this.idleAtNanos = j;
    }

    public final boolean isMultiplexed() {
        return this.http2Connection != null;
    }

    public final void noNewExchanges() {
        boolean z = !Thread.holdsLock(this.connectionPool);
        if (!_Assertions.ENABLED || z) {
            synchronized (this.connectionPool) {
                this.noNewExchanges = true;
                Unit unit = Unit.INSTANCE;
            }
            return;
        }
        throw new AssertionError("Assertion failed");
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x0104  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0124  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x012b  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0165  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x016c  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0171  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0179 A[EDGE_INSN: B:71:0x0179->B:64:0x0179 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:74:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    public final void connect(int i, int i2, int i3, int i4, boolean z, Call call, EventListener eventListener) {
        Socket socket2;
        Socket socket3;
        Http2Connection http2Connection2;
        Call call2 = call;
        EventListener eventListener2 = eventListener;
        Intrinsics.checkParameterIsNotNull(call2, NotificationCompat.CATEGORY_CALL);
        Intrinsics.checkParameterIsNotNull(eventListener2, "eventListener");
        if (this.protocol == null) {
            RouteException routeException = null;
            List connectionSpecs = this.route.address().connectionSpecs();
            ConnectionSpecSelector connectionSpecSelector = new ConnectionSpecSelector(connectionSpecs);
            if (this.route.address().sslSocketFactory() == null) {
                if (connectionSpecs.contains(ConnectionSpec.CLEARTEXT)) {
                    String host = this.route.address().url().host();
                    if (!Platform.Companion.get().isCleartextTrafficPermitted(host)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("CLEARTEXT communication to ");
                        sb.append(host);
                        sb.append(" not permitted by network security policy");
                        throw new RouteException(new UnknownServiceException(sb.toString()));
                    }
                } else {
                    throw new RouteException(new UnknownServiceException("CLEARTEXT communication not enabled for client"));
                }
            } else if (this.route.address().protocols().contains(Protocol.H2_PRIOR_KNOWLEDGE)) {
                throw new RouteException(new UnknownServiceException("H2_PRIOR_KNOWLEDGE cannot be used with HTTPS"));
            }
            RouteException routeException2 = routeException;
            do {
                try {
                    if (this.route.requiresTunnel()) {
                        connectTunnel(i, i2, i3, call, eventListener);
                        if (this.rawSocket != null) {
                            int i5 = i;
                            int i6 = i2;
                        } else if (this.route.requiresTunnel() || this.rawSocket != null) {
                            http2Connection2 = this.http2Connection;
                            if (http2Connection2 == null) {
                                synchronized (this.connectionPool) {
                                    this.allocationLimit = http2Connection2.maxConcurrentStreams();
                                    Unit unit = Unit.INSTANCE;
                                }
                                return;
                            }
                            return;
                        } else {
                            throw new RouteException(new ProtocolException("Too many tunnel connections attempted: 21"));
                        }
                    } else {
                        try {
                            connectSocket(i, i2, call2, eventListener2);
                        } catch (IOException e) {
                            e = e;
                            int i7 = i4;
                            socket2 = this.socket;
                            if (socket2 != null) {
                            }
                            socket3 = this.rawSocket;
                            if (socket3 != null) {
                            }
                            Socket socket4 = null;
                            this.socket = socket4;
                            this.rawSocket = socket4;
                            this.source = null;
                            this.sink = null;
                            this.handshake = null;
                            this.protocol = null;
                            this.http2Connection = null;
                            eventListener.connectFailed(call, this.route.socketAddress(), this.route.proxy(), null, e);
                            if (routeException2 != null) {
                            }
                            if (z) {
                            }
                            throw routeException2;
                        }
                    }
                    try {
                        establishProtocol(connectionSpecSelector, i4, call2, eventListener2);
                        eventListener2.connectEnd(call2, this.route.socketAddress(), this.route.proxy(), this.protocol);
                        if (this.route.requiresTunnel()) {
                        }
                        http2Connection2 = this.http2Connection;
                        if (http2Connection2 == null) {
                        }
                    } catch (IOException e2) {
                        e = e2;
                    }
                } catch (IOException e3) {
                    e = e3;
                    int i8 = i;
                    int i9 = i2;
                    int i72 = i4;
                    socket2 = this.socket;
                    if (socket2 != null) {
                        Util.closeQuietly(socket2);
                    }
                    socket3 = this.rawSocket;
                    if (socket3 != null) {
                        Util.closeQuietly(socket3);
                    }
                    Socket socket42 = null;
                    this.socket = socket42;
                    this.rawSocket = socket42;
                    this.source = null;
                    this.sink = null;
                    this.handshake = null;
                    this.protocol = null;
                    this.http2Connection = null;
                    eventListener.connectFailed(call, this.route.socketAddress(), this.route.proxy(), null, e);
                    if (routeException2 != null) {
                        routeException2 = new RouteException(e);
                    } else {
                        routeException2.addConnectException(e);
                    }
                    if (z) {
                        break;
                    } else if (!connectionSpecSelector.connectionFailed(e)) {
                    }
                    throw routeException2;
                }
            } while (!connectionSpecSelector.connectionFailed(e));
            throw routeException2;
        }
        throw new IllegalStateException("already connected".toString());
    }

    private final void connectTunnel(int i, int i2, int i3, Call call, EventListener eventListener) throws IOException {
        Request createTunnelRequest = createTunnelRequest();
        HttpUrl url = createTunnelRequest.url();
        int i4 = 0;
        while (i4 < 21) {
            connectSocket(i, i2, call, eventListener);
            createTunnelRequest = createTunnel(i2, i3, createTunnelRequest, url);
            if (createTunnelRequest != null) {
                Socket socket2 = this.rawSocket;
                if (socket2 != null) {
                    Util.closeQuietly(socket2);
                }
                this.rawSocket = null;
                this.sink = null;
                this.source = null;
                eventListener.connectEnd(call, this.route.socketAddress(), this.route.proxy(), null);
                i4++;
            } else {
                return;
            }
        }
    }

    private final void connectSocket(int i, int i2, Call call, EventListener eventListener) throws IOException {
        Socket socket2;
        Proxy proxy = this.route.proxy();
        Address address = this.route.address();
        Type type = proxy.type();
        if (type != null) {
            int i3 = WhenMappings.$EnumSwitchMapping$0[type.ordinal()];
            if (i3 == 1 || i3 == 2) {
                socket2 = address.socketFactory().createSocket();
                if (socket2 == null) {
                    Intrinsics.throwNpe();
                }
                this.rawSocket = socket2;
                eventListener.connectStart(call, this.route.socketAddress(), proxy);
                socket2.setSoTimeout(i2);
                Platform.Companion.get().connectSocket(socket2, this.route.socketAddress(), i);
                this.source = Okio.buffer(Okio.source(socket2));
                this.sink = Okio.buffer(Okio.sink(socket2));
            }
        }
        socket2 = new Socket(proxy);
        this.rawSocket = socket2;
        eventListener.connectStart(call, this.route.socketAddress(), proxy);
        socket2.setSoTimeout(i2);
        try {
            Platform.Companion.get().connectSocket(socket2, this.route.socketAddress(), i);
            try {
                this.source = Okio.buffer(Okio.source(socket2));
                this.sink = Okio.buffer(Okio.sink(socket2));
            } catch (NullPointerException e) {
                if (Intrinsics.areEqual((Object) e.getMessage(), (Object) NPE_THROW_WITH_NULL)) {
                    throw new IOException(e);
                }
            }
        } catch (ConnectException e2) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to connect to ");
            sb.append(this.route.socketAddress());
            ConnectException connectException = new ConnectException(sb.toString());
            connectException.initCause(e2);
            throw connectException;
        }
    }

    private final void establishProtocol(ConnectionSpecSelector connectionSpecSelector, int i, Call call, EventListener eventListener) throws IOException {
        if (this.route.address().sslSocketFactory() != null) {
            eventListener.secureConnectStart(call);
            connectTls(connectionSpecSelector);
            eventListener.secureConnectEnd(call, this.handshake);
            if (this.protocol == Protocol.HTTP_2) {
                startHttp2(i);
            }
        } else if (this.route.address().protocols().contains(Protocol.H2_PRIOR_KNOWLEDGE)) {
            this.socket = this.rawSocket;
            this.protocol = Protocol.H2_PRIOR_KNOWLEDGE;
            startHttp2(i);
        } else {
            this.socket = this.rawSocket;
            this.protocol = Protocol.HTTP_1_1;
        }
    }

    private final void startHttp2(int i) throws IOException {
        Socket socket2 = this.socket;
        if (socket2 == null) {
            Intrinsics.throwNpe();
        }
        BufferedSource bufferedSource = this.source;
        if (bufferedSource == null) {
            Intrinsics.throwNpe();
        }
        BufferedSink bufferedSink = this.sink;
        if (bufferedSink == null) {
            Intrinsics.throwNpe();
        }
        socket2.setSoTimeout(0);
        Http2Connection build = new Builder(true).socket(socket2, this.route.address().url().host(), bufferedSource, bufferedSink).listener(this).pingIntervalMillis(i).build();
        this.http2Connection = build;
        Http2Connection.start$default(build, false, 1, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x01a4  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x01af  */
    private final void connectTls(ConnectionSpecSelector connectionSpecSelector) throws IOException {
        Address address = this.route.address();
        SSLSocketFactory sslSocketFactory = address.sslSocketFactory();
        String str = null;
        SSLSocket sSLSocket = null;
        if (sslSocketFactory == null) {
            try {
                Intrinsics.throwNpe();
            } catch (Throwable th) {
                th = th;
                if (sSLSocket != null) {
                    Platform.Companion.get().afterHandshake(sSLSocket);
                }
                if (sSLSocket != null) {
                    Util.closeQuietly((Socket) sSLSocket);
                }
                throw th;
            }
        }
        Socket createSocket = sslSocketFactory.createSocket(this.rawSocket, address.url().host(), address.url().port(), true);
        if (createSocket != null) {
            SSLSocket sSLSocket2 = (SSLSocket) createSocket;
            try {
                ConnectionSpec configureSecureSocket = connectionSpecSelector.configureSecureSocket(sSLSocket2);
                if (configureSecureSocket.supportsTlsExtensions()) {
                    Platform.Companion.get().configureTlsExtensions(sSLSocket2, address.url().host(), address.protocols());
                }
                sSLSocket2.startHandshake();
                SSLSession session = sSLSocket2.getSession();
                okhttp3.Handshake.Companion companion = Handshake.Companion;
                Intrinsics.checkExpressionValueIsNotNull(session, "sslSocketSession");
                Handshake handshake2 = companion.get(session);
                HostnameVerifier hostnameVerifier = address.hostnameVerifier();
                if (hostnameVerifier == null) {
                    Intrinsics.throwNpe();
                }
                if (!hostnameVerifier.verify(address.url().host(), session)) {
                    List peerCertificates = handshake2.peerCertificates();
                    if (!peerCertificates.isEmpty()) {
                        Object obj = peerCertificates.get(0);
                        if (obj == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.security.cert.X509Certificate");
                        }
                        X509Certificate x509Certificate = (X509Certificate) obj;
                        StringBuilder sb = new StringBuilder();
                        sb.append("\n              |Hostname ");
                        sb.append(address.url().host());
                        sb.append(" not verified:\n              |    certificate: ");
                        sb.append(CertificatePinner.Companion.pin(x509Certificate));
                        sb.append("\n              |    DN: ");
                        Principal subjectDN = x509Certificate.getSubjectDN();
                        Intrinsics.checkExpressionValueIsNotNull(subjectDN, "cert.subjectDN");
                        sb.append(subjectDN.getName());
                        sb.append("\n              |    subjectAltNames: ");
                        sb.append(OkHostnameVerifier.INSTANCE.allSubjectAltNames(x509Certificate));
                        sb.append("\n              ");
                        throw new SSLPeerUnverifiedException(StringsKt.trimMargin$default(sb.toString(), null, 1, null));
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Hostname ");
                    sb2.append(address.url().host());
                    sb2.append(" not verified (no certificates)");
                    throw new SSLPeerUnverifiedException(sb2.toString());
                }
                CertificatePinner certificatePinner = address.certificatePinner();
                if (certificatePinner == null) {
                    Intrinsics.throwNpe();
                }
                this.handshake = new Handshake(handshake2.tlsVersion(), handshake2.cipherSuite(), handshake2.localCertificates(), new RealConnection$connectTls$1(certificatePinner, handshake2, address));
                certificatePinner.check$okhttp(address.url().host(), new RealConnection$connectTls$2(this));
                if (configureSecureSocket.supportsTlsExtensions()) {
                    str = Platform.Companion.get().getSelectedProtocol(sSLSocket2);
                }
                this.socket = sSLSocket2;
                this.source = Okio.buffer(Okio.source((Socket) sSLSocket2));
                this.sink = Okio.buffer(Okio.sink((Socket) sSLSocket2));
                this.protocol = str != null ? Protocol.Companion.get(str) : Protocol.HTTP_1_1;
                if (sSLSocket2 != null) {
                    Platform.Companion.get().afterHandshake(sSLSocket2);
                }
            } catch (Throwable th2) {
                th = th2;
                sSLSocket = sSLSocket2;
                if (sSLSocket != null) {
                }
                if (sSLSocket != null) {
                }
                throw th;
            }
        } else {
            throw new TypeCastException("null cannot be cast to non-null type javax.net.ssl.SSLSocket");
        }
    }

    private final Request createTunnel(int i, int i2, Request request, HttpUrl httpUrl) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("CONNECT ");
        sb.append(Util.toHostHeader(httpUrl, true));
        sb.append(" HTTP/1.1");
        String sb2 = sb.toString();
        while (true) {
            BufferedSource bufferedSource = this.source;
            if (bufferedSource == null) {
                Intrinsics.throwNpe();
            }
            BufferedSink bufferedSink = this.sink;
            if (bufferedSink == null) {
                Intrinsics.throwNpe();
            }
            Http1ExchangeCodec http1ExchangeCodec = new Http1ExchangeCodec(null, null, bufferedSource, bufferedSink);
            bufferedSource.timeout().timeout((long) i, TimeUnit.MILLISECONDS);
            bufferedSink.timeout().timeout((long) i2, TimeUnit.MILLISECONDS);
            http1ExchangeCodec.writeRequest(request.headers(), sb2);
            http1ExchangeCodec.finishRequest();
            Response.Builder readResponseHeaders = http1ExchangeCodec.readResponseHeaders(false);
            if (readResponseHeaders == null) {
                Intrinsics.throwNpe();
            }
            Response build = readResponseHeaders.request(request).build();
            http1ExchangeCodec.skipConnectBody(build);
            int code = build.code();
            if (code != 200) {
                if (code == 407) {
                    Request authenticate = this.route.address().proxyAuthenticator().authenticate(this.route, build);
                    if (authenticate != null) {
                        if (StringsKt.equals("close", Response.header$default(build, "Connection", null, 2, null), true)) {
                            return authenticate;
                        }
                        request = authenticate;
                    } else {
                        throw new IOException("Failed to authenticate with proxy");
                    }
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Unexpected response code for CONNECT: ");
                    sb3.append(build.code());
                    throw new IOException(sb3.toString());
                }
            } else if (bufferedSource.getBuffer().exhausted() && bufferedSink.getBuffer().exhausted()) {
                return null;
            } else {
                throw new IOException("TLS tunnel buffered too many bytes!");
            }
        }
    }

    private final Request createTunnelRequest() throws IOException {
        Request build = new Request.Builder().url(this.route.address().url()).method("CONNECT", null).header("Host", Util.toHostHeader(this.route.address().url(), true)).header("Proxy-Connection", "Keep-Alive").header("User-Agent", Version.userAgent).build();
        Request authenticate = this.route.address().proxyAuthenticator().authenticate(this.route, new Response.Builder().request(build).protocol(Protocol.HTTP_1_1).code(407).message("Preemptive Authenticate").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1).receivedResponseAtMillis(-1).header("Proxy-Authenticate", "OkHttp-Preemptive").build());
        return authenticate != null ? authenticate : build;
    }

    public final boolean isEligible$okhttp(Address address, List<Route> list) {
        Intrinsics.checkParameterIsNotNull(address, "address");
        if (this.transmitters.size() >= this.allocationLimit || this.noNewExchanges || !this.route.address().equalsNonHost$okhttp(address)) {
            return false;
        }
        if (Intrinsics.areEqual((Object) address.url().host(), (Object) route().address().url().host())) {
            return true;
        }
        if (this.http2Connection == null || list == null || !routeMatchesAny(list) || address.hostnameVerifier() != OkHostnameVerifier.INSTANCE || !supportsUrl(address.url())) {
            return false;
        }
        try {
            CertificatePinner certificatePinner = address.certificatePinner();
            if (certificatePinner == null) {
                Intrinsics.throwNpe();
            }
            String host = address.url().host();
            Handshake handshake2 = handshake();
            if (handshake2 == null) {
                Intrinsics.throwNpe();
            }
            certificatePinner.check(host, handshake2.peerCertificates());
            return true;
        } catch (SSLPeerUnverifiedException unused) {
            return false;
        }
    }

    private final boolean routeMatchesAny(List<Route> list) {
        boolean z;
        Iterable<Route> iterable = list;
        if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
            for (Route route2 : iterable) {
                if (route2.proxy().type() == Type.DIRECT && this.route.proxy().type() == Type.DIRECT && Intrinsics.areEqual((Object) this.route.socketAddress(), (Object) route2.socketAddress())) {
                    z = true;
                    continue;
                } else {
                    z = false;
                    continue;
                }
                if (z) {
                    return true;
                }
            }
        }
        return false;
    }

    public final boolean supportsUrl(HttpUrl httpUrl) {
        Intrinsics.checkParameterIsNotNull(httpUrl, ImagesContract.URL);
        HttpUrl url = this.route.address().url();
        boolean z = false;
        if (httpUrl.port() != url.port()) {
            return false;
        }
        if (Intrinsics.areEqual((Object) httpUrl.host(), (Object) url.host())) {
            return true;
        }
        if (this.handshake != null) {
            OkHostnameVerifier okHostnameVerifier = OkHostnameVerifier.INSTANCE;
            String host = httpUrl.host();
            Handshake handshake2 = this.handshake;
            if (handshake2 == null) {
                Intrinsics.throwNpe();
            }
            Object obj = handshake2.peerCertificates().get(0);
            if (obj == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.security.cert.X509Certificate");
            } else if (okHostnameVerifier.verify(host, (X509Certificate) obj)) {
                z = true;
            }
        }
        return z;
    }

    public final ExchangeCodec newCodec$okhttp(OkHttpClient okHttpClient, Chain chain) throws SocketException {
        Intrinsics.checkParameterIsNotNull(okHttpClient, "client");
        Intrinsics.checkParameterIsNotNull(chain, "chain");
        Socket socket2 = this.socket;
        if (socket2 == null) {
            Intrinsics.throwNpe();
        }
        BufferedSource bufferedSource = this.source;
        if (bufferedSource == null) {
            Intrinsics.throwNpe();
        }
        BufferedSink bufferedSink = this.sink;
        if (bufferedSink == null) {
            Intrinsics.throwNpe();
        }
        Http2Connection http2Connection2 = this.http2Connection;
        if (http2Connection2 != null) {
            return new Http2ExchangeCodec(okHttpClient, this, chain, http2Connection2);
        }
        socket2.setSoTimeout(chain.readTimeoutMillis());
        bufferedSource.timeout().timeout((long) chain.readTimeoutMillis(), TimeUnit.MILLISECONDS);
        bufferedSink.timeout().timeout((long) chain.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
        return new Http1ExchangeCodec(okHttpClient, this, bufferedSource, bufferedSink);
    }

    public final Streams newWebSocketStreams$okhttp(Exchange exchange) throws SocketException {
        Intrinsics.checkParameterIsNotNull(exchange, "exchange");
        Socket socket2 = this.socket;
        if (socket2 == null) {
            Intrinsics.throwNpe();
        }
        BufferedSource bufferedSource = this.source;
        if (bufferedSource == null) {
            Intrinsics.throwNpe();
        }
        BufferedSink bufferedSink = this.sink;
        if (bufferedSink == null) {
            Intrinsics.throwNpe();
        }
        socket2.setSoTimeout(0);
        noNewExchanges();
        RealConnection$newWebSocketStreams$1 realConnection$newWebSocketStreams$1 = new RealConnection$newWebSocketStreams$1(exchange, bufferedSource, bufferedSink, true, bufferedSource, bufferedSink);
        return realConnection$newWebSocketStreams$1;
    }

    public Route route() {
        return this.route;
    }

    public final void cancel() {
        Socket socket2 = this.rawSocket;
        if (socket2 != null) {
            Util.closeQuietly(socket2);
        }
    }

    public Socket socket() {
        Socket socket2 = this.socket;
        if (socket2 == null) {
            Intrinsics.throwNpe();
        }
        return socket2;
    }

    public final boolean isHealthy(boolean z) {
        int soTimeout;
        Socket socket2 = this.socket;
        if (socket2 == null) {
            Intrinsics.throwNpe();
        }
        BufferedSource bufferedSource = this.source;
        if (bufferedSource == null) {
            Intrinsics.throwNpe();
        }
        if (socket2.isClosed() || socket2.isInputShutdown() || socket2.isOutputShutdown()) {
            return false;
        }
        Http2Connection http2Connection2 = this.http2Connection;
        if (http2Connection2 != null) {
            return !http2Connection2.isShutdown();
        }
        if (z) {
            try {
                soTimeout = socket2.getSoTimeout();
                socket2.setSoTimeout(1);
                boolean z2 = !bufferedSource.exhausted();
                socket2.setSoTimeout(soTimeout);
                return z2;
            } catch (SocketTimeoutException unused) {
            } catch (IOException unused2) {
                return false;
            } catch (Throwable th) {
                socket2.setSoTimeout(soTimeout);
                throw th;
            }
        }
        return true;
    }

    public void onStream(Http2Stream http2Stream) throws IOException {
        Intrinsics.checkParameterIsNotNull(http2Stream, "stream");
        http2Stream.close(ErrorCode.REFUSED_STREAM, null);
    }

    public void onSettings(Http2Connection http2Connection2) {
        Intrinsics.checkParameterIsNotNull(http2Connection2, "connection");
        synchronized (this.connectionPool) {
            this.allocationLimit = http2Connection2.maxConcurrentStreams();
            Unit unit = Unit.INSTANCE;
        }
    }

    public Handshake handshake() {
        return this.handshake;
    }

    public final void trackFailure$okhttp(IOException iOException) {
        boolean z = !Thread.holdsLock(this.connectionPool);
        if (!_Assertions.ENABLED || z) {
            synchronized (this.connectionPool) {
                if (iOException instanceof StreamResetException) {
                    int i = WhenMappings.$EnumSwitchMapping$1[((StreamResetException) iOException).errorCode.ordinal()];
                    if (i == 1) {
                        int i2 = this.refusedStreamCount + 1;
                        this.refusedStreamCount = i2;
                        if (i2 > 1) {
                            this.noNewExchanges = true;
                            this.routeFailureCount++;
                        }
                    } else if (i != 2) {
                        this.noNewExchanges = true;
                        this.routeFailureCount++;
                    }
                } else if (!isMultiplexed() || (iOException instanceof ConnectionShutdownException)) {
                    this.noNewExchanges = true;
                    if (this.successCount == 0) {
                        if (iOException != null) {
                            this.connectionPool.connectFailed(this.route, iOException);
                        }
                        this.routeFailureCount++;
                    }
                }
                Unit unit = Unit.INSTANCE;
            }
            return;
        }
        throw new AssertionError("Assertion failed");
    }

    public Protocol protocol() {
        Protocol protocol2 = this.protocol;
        if (protocol2 == null) {
            Intrinsics.throwNpe();
        }
        return protocol2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x005f, code lost:
        if (r1 != null) goto L_0x0064;
     */
    public String toString() {
        Object obj;
        StringBuilder sb = new StringBuilder();
        sb.append("Connection{");
        sb.append(this.route.address().url().host());
        sb.append(':');
        sb.append(this.route.address().url().port());
        sb.append(',');
        sb.append(" proxy=");
        sb.append(this.route.proxy());
        sb.append(" hostAddress=");
        sb.append(this.route.socketAddress());
        sb.append(" cipherSuite=");
        Handshake handshake2 = this.handshake;
        if (handshake2 != null) {
            obj = handshake2.cipherSuite();
        }
        obj = "none";
        sb.append(obj);
        sb.append(" protocol=");
        sb.append(this.protocol);
        sb.append('}');
        return sb.toString();
    }
}
