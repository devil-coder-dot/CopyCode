package okhttp3.internal.connection;

import androidx.core.app.NotificationCompat;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref.ObjectRef;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.CertificatePinner;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.internal.Util;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.platform.Platform;
import okio.Timeout;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006*\u0001 \u0018\u00002\u00020\u0001:\u0001FB\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010#\u001a\u00020$2\u0006\u0010\n\u001a\u00020\u000bJ\u0006\u0010%\u001a\u00020$J\u0006\u0010&\u001a\u00020\tJ\u0006\u0010'\u001a\u00020$J\u0010\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+H\u0002J\u0006\u0010,\u001a\u00020$J;\u0010-\u001a\u0002H.\"\n\b\u0000\u0010.*\u0004\u0018\u00010/2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u00100\u001a\u00020\t2\u0006\u00101\u001a\u00020\t2\u0006\u00102\u001a\u0002H.H\u0000¢\u0006\u0004\b3\u00104J\u0006\u00105\u001a\u00020\tJ)\u00106\u001a\u0002H.\"\n\b\u0000\u0010.*\u0004\u0018\u00010/2\u0006\u00102\u001a\u0002H.2\u0006\u00107\u001a\u00020\tH\u0002¢\u0006\u0002\u00108J\u001d\u00109\u001a\u00020\u00152\u0006\u0010:\u001a\u00020;2\u0006\u0010<\u001a\u00020\tH\u0000¢\u0006\u0002\b=J\u0012\u0010\u001c\u001a\u0004\u0018\u00010/2\b\u00102\u001a\u0004\u0018\u00010/J\u000e\u0010>\u001a\u00020$2\u0006\u0010\u001d\u001a\u00020\u001eJ\b\u0010?\u001a\u0004\u0018\u00010@J\u0006\u0010\u001f\u001a\u00020AJ\u0006\u0010\"\u001a\u00020$J\u0006\u0010B\u001a\u00020$J!\u0010C\u001a\u0002H.\"\n\b\u0000\u0010.*\u0004\u0018\u00010/2\u0006\u0010D\u001a\u0002H.H\u0002¢\u0006\u0002\u0010ER\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0001X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u0004\u0018\u00010\u000bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u001a\u001a\u00020\t8F¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001f\u001a\u00020 X\u0004¢\u0006\u0004\n\u0002\u0010!R\u000e\u0010\"\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000¨\u0006G"}, d2 = {"Lokhttp3/internal/connection/Transmitter;", "", "client", "Lokhttp3/OkHttpClient;", "call", "Lokhttp3/Call;", "(Lokhttp3/OkHttpClient;Lokhttp3/Call;)V", "callStackTrace", "canceled", "", "connection", "Lokhttp3/internal/connection/RealConnection;", "getConnection", "()Lokhttp3/internal/connection/RealConnection;", "setConnection", "(Lokhttp3/internal/connection/RealConnection;)V", "connectionPool", "Lokhttp3/internal/connection/RealConnectionPool;", "eventListener", "Lokhttp3/EventListener;", "exchange", "Lokhttp3/internal/connection/Exchange;", "exchangeFinder", "Lokhttp3/internal/connection/ExchangeFinder;", "exchangeRequestDone", "exchangeResponseDone", "isCanceled", "()Z", "noMoreExchanges", "request", "Lokhttp3/Request;", "timeout", "okhttp3/internal/connection/Transmitter$timeout$1", "Lokhttp3/internal/connection/Transmitter$timeout$1;", "timeoutEarlyExit", "acquireConnectionNoEvents", "", "callStart", "canRetry", "cancel", "createAddress", "Lokhttp3/Address;", "url", "Lokhttp3/HttpUrl;", "exchangeDoneDueToException", "exchangeMessageDone", "E", "Ljava/io/IOException;", "requestDone", "responseDone", "e", "exchangeMessageDone$okhttp", "(Lokhttp3/internal/connection/Exchange;ZZLjava/io/IOException;)Ljava/io/IOException;", "hasExchange", "maybeReleaseConnection", "force", "(Ljava/io/IOException;Z)Ljava/io/IOException;", "newExchange", "chain", "Lokhttp3/Interceptor$Chain;", "doExtensiveHealthChecks", "newExchange$okhttp", "prepareToConnect", "releaseConnectionNoEvents", "Ljava/net/Socket;", "Lokio/Timeout;", "timeoutEnter", "timeoutExit", "cause", "(Ljava/io/IOException;)Ljava/io/IOException;", "TransmitterReference", "okhttp"}, k = 1, mv = {1, 1, 15})
/* compiled from: Transmitter.kt */
public final class Transmitter {
    private final Call call;
    private Object callStackTrace;
    private boolean canceled;
    private final OkHttpClient client;
    private RealConnection connection;
    private final RealConnectionPool connectionPool;
    private final EventListener eventListener = this.client.eventListenerFactory().create(this.call);
    private Exchange exchange;
    private ExchangeFinder exchangeFinder;
    private boolean exchangeRequestDone;
    private boolean exchangeResponseDone;
    private boolean noMoreExchanges;
    private Request request;
    private final Transmitter$timeout$1 timeout;
    private boolean timeoutEarlyExit;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\b\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0017\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"Lokhttp3/internal/connection/Transmitter$TransmitterReference;", "Ljava/lang/ref/WeakReference;", "Lokhttp3/internal/connection/Transmitter;", "referent", "callStackTrace", "", "(Lokhttp3/internal/connection/Transmitter;Ljava/lang/Object;)V", "getCallStackTrace", "()Ljava/lang/Object;", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: Transmitter.kt */
    public static final class TransmitterReference extends WeakReference<Transmitter> {
        private final Object callStackTrace;

        public final Object getCallStackTrace() {
            return this.callStackTrace;
        }

        public TransmitterReference(Transmitter transmitter, Object obj) {
            Intrinsics.checkParameterIsNotNull(transmitter, "referent");
            super(transmitter);
            this.callStackTrace = obj;
        }
    }

    public Transmitter(OkHttpClient okHttpClient, Call call2) {
        Intrinsics.checkParameterIsNotNull(okHttpClient, "client");
        Intrinsics.checkParameterIsNotNull(call2, NotificationCompat.CATEGORY_CALL);
        this.client = okHttpClient;
        this.call = call2;
        this.connectionPool = okHttpClient.connectionPool().getDelegate$okhttp();
        Transmitter$timeout$1 transmitter$timeout$1 = new Transmitter$timeout$1(this);
        transmitter$timeout$1.timeout((long) this.client.callTimeoutMillis(), TimeUnit.MILLISECONDS);
        this.timeout = transmitter$timeout$1;
    }

    public final RealConnection getConnection() {
        return this.connection;
    }

    public final void setConnection(RealConnection realConnection) {
        this.connection = realConnection;
    }

    public final boolean isCanceled() {
        boolean z;
        synchronized (this.connectionPool) {
            z = this.canceled;
        }
        return z;
    }

    public final Timeout timeout() {
        return this.timeout;
    }

    public final void timeoutEnter() {
        this.timeout.enter();
    }

    public final void timeoutEarlyExit() {
        if (!this.timeoutEarlyExit) {
            this.timeoutEarlyExit = true;
            this.timeout.exit();
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    private final <E extends IOException> E timeoutExit(E e) {
        if (this.timeoutEarlyExit || !this.timeout.exit()) {
            return e;
        }
        E interruptedIOException = new InterruptedIOException("timeout");
        if (e != null) {
            interruptedIOException.initCause((Throwable) e);
        }
        return (IOException) interruptedIOException;
    }

    public final void callStart() {
        this.callStackTrace = Platform.Companion.get().getStackTraceForCloseable("response.body().close()");
        this.eventListener.callStart(this.call);
    }

    public final void prepareToConnect(Request request2) {
        Intrinsics.checkParameterIsNotNull(request2, "request");
        Request request3 = this.request;
        if (request3 != null) {
            if (request3 == null) {
                Intrinsics.throwNpe();
            }
            if (Util.canReuseConnectionFor(request3.url(), request2.url())) {
                ExchangeFinder exchangeFinder2 = this.exchangeFinder;
                if (exchangeFinder2 == null) {
                    Intrinsics.throwNpe();
                }
                if (exchangeFinder2.hasRouteToTry()) {
                    return;
                }
            }
            if (!(this.exchange == null)) {
                throw new IllegalStateException("Check failed.".toString());
            } else if (this.exchangeFinder != null) {
                maybeReleaseConnection(null, true);
                this.exchangeFinder = null;
            }
        }
        this.request = request2;
        ExchangeFinder exchangeFinder3 = new ExchangeFinder(this, this.connectionPool, createAddress(request2.url()), this.call, this.eventListener);
        this.exchangeFinder = exchangeFinder3;
    }

    private final Address createAddress(HttpUrl httpUrl) {
        SSLSocketFactory sSLSocketFactory = null;
        HostnameVerifier hostnameVerifier = null;
        CertificatePinner certificatePinner = null;
        if (httpUrl.isHttps()) {
            sSLSocketFactory = this.client.sslSocketFactory();
            hostnameVerifier = this.client.hostnameVerifier();
            certificatePinner = this.client.certificatePinner();
        }
        SSLSocketFactory sSLSocketFactory2 = sSLSocketFactory;
        HostnameVerifier hostnameVerifier2 = hostnameVerifier;
        Address address = new Address(httpUrl.host(), httpUrl.port(), this.client.dns(), this.client.socketFactory(), sSLSocketFactory2, hostnameVerifier2, certificatePinner, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
        return address;
    }

    public final Exchange newExchange$okhttp(Chain chain, boolean z) {
        Intrinsics.checkParameterIsNotNull(chain, "chain");
        synchronized (this.connectionPool) {
            boolean z2 = true;
            if (!this.noMoreExchanges) {
                if (this.exchange != null) {
                    z2 = false;
                }
                if (z2) {
                    Unit unit = Unit.INSTANCE;
                } else {
                    throw new IllegalStateException("cannot make a new request because the previous response is still open: please call response.close()".toString());
                }
            } else {
                throw new IllegalStateException("released".toString());
            }
        }
        ExchangeFinder exchangeFinder2 = this.exchangeFinder;
        if (exchangeFinder2 == null) {
            Intrinsics.throwNpe();
        }
        ExchangeCodec find = exchangeFinder2.find(this.client, chain, z);
        Call call2 = this.call;
        EventListener eventListener2 = this.eventListener;
        ExchangeFinder exchangeFinder3 = this.exchangeFinder;
        if (exchangeFinder3 == null) {
            Intrinsics.throwNpe();
        }
        Exchange exchange2 = new Exchange(this, call2, eventListener2, exchangeFinder3, find);
        synchronized (this.connectionPool) {
            this.exchange = exchange2;
            this.exchangeRequestDone = false;
            this.exchangeResponseDone = false;
        }
        return exchange2;
    }

    public final void acquireConnectionNoEvents(RealConnection realConnection) {
        Intrinsics.checkParameterIsNotNull(realConnection, "connection");
        boolean holdsLock = Thread.holdsLock(this.connectionPool);
        if (!_Assertions.ENABLED || holdsLock) {
            if (this.connection == null) {
                this.connection = realConnection;
                realConnection.getTransmitters().add(new TransmitterReference(this, this.callStackTrace));
                return;
            }
            throw new IllegalStateException("Check failed.".toString());
        }
        throw new AssertionError("Assertion failed");
    }

    public final Socket releaseConnectionNoEvents() {
        boolean holdsLock = Thread.holdsLock(this.connectionPool);
        if (!_Assertions.ENABLED || holdsLock) {
            RealConnection realConnection = this.connection;
            if (realConnection == null) {
                Intrinsics.throwNpe();
            }
            Iterator it = realConnection.getTransmitters().iterator();
            boolean z = false;
            int i = 0;
            while (true) {
                if (!it.hasNext()) {
                    i = -1;
                    break;
                } else if (Intrinsics.areEqual((Object) (Transmitter) ((Reference) it.next()).get(), (Object) this)) {
                    break;
                } else {
                    i++;
                }
            }
            if (i != -1) {
                z = true;
            }
            if (z) {
                RealConnection realConnection2 = this.connection;
                if (realConnection2 == null) {
                    Intrinsics.throwNpe();
                }
                realConnection2.getTransmitters().remove(i);
                this.connection = null;
                if (realConnection2.getTransmitters().isEmpty()) {
                    realConnection2.setIdleAtNanos$okhttp(System.nanoTime());
                    if (this.connectionPool.connectionBecameIdle(realConnection2)) {
                        return realConnection2.socket();
                    }
                }
                return null;
            }
            throw new IllegalStateException("Check failed.".toString());
        }
        throw new AssertionError("Assertion failed");
    }

    public final void exchangeDoneDueToException() {
        synchronized (this.connectionPool) {
            if (!this.noMoreExchanges) {
                this.exchange = null;
                Unit unit = Unit.INSTANCE;
            } else {
                throw new IllegalStateException("Check failed.".toString());
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0053, code lost:
        if (r1 == false) goto L_0x0059;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0055, code lost:
        r6 = maybeReleaseConnection(r6, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0059, code lost:
        return r6;
     */
    public final <E extends IOException> E exchangeMessageDone$okhttp(Exchange exchange2, boolean z, boolean z2, E e) {
        boolean z3;
        Intrinsics.checkParameterIsNotNull(exchange2, "exchange");
        synchronized (this.connectionPool) {
            boolean z4 = true;
            if (!Intrinsics.areEqual((Object) exchange2, (Object) this.exchange)) {
                return e;
            }
            if (z) {
                z3 = !this.exchangeRequestDone;
                this.exchangeRequestDone = true;
            } else {
                z3 = false;
            }
            if (z2) {
                if (!this.exchangeResponseDone) {
                    z3 = true;
                }
                this.exchangeResponseDone = true;
            }
            if (!this.exchangeRequestDone || !this.exchangeResponseDone || !z3) {
                z4 = false;
            } else {
                Exchange exchange3 = this.exchange;
                if (exchange3 == null) {
                    Intrinsics.throwNpe();
                }
                RealConnection connection2 = exchange3.connection();
                if (connection2 == null) {
                    Intrinsics.throwNpe();
                }
                connection2.setSuccessCount$okhttp(connection2.getSuccessCount$okhttp() + 1);
                this.exchange = null;
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public final IOException noMoreExchanges(IOException iOException) {
        synchronized (this.connectionPool) {
            this.noMoreExchanges = true;
            Unit unit = Unit.INSTANCE;
        }
        return maybeReleaseConnection(iOException, false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0019 A[Catch:{ all -> 0x0013 }] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0085  */
    private final <E extends IOException> E maybeReleaseConnection(E e, boolean z) {
        boolean z2;
        boolean z3;
        Socket releaseConnectionNoEvents;
        boolean z4;
        ObjectRef objectRef = new ObjectRef();
        synchronized (this.connectionPool) {
            z2 = false;
            if (z) {
                try {
                    if (this.exchange != null) {
                        z3 = false;
                        if (!z3) {
                            objectRef.element = (Connection) this.connection;
                            releaseConnectionNoEvents = (this.connection == null || this.exchange != null || (!z && !this.noMoreExchanges)) ? null : releaseConnectionNoEvents();
                            if (this.connection != null) {
                                objectRef.element = (Connection) null;
                            }
                            z4 = this.noMoreExchanges && this.exchange == null;
                            Unit unit = Unit.INSTANCE;
                        } else {
                            throw new IllegalStateException("cannot release connection while it is in use".toString());
                        }
                    }
                } finally {
                }
            }
            z3 = true;
            if (!z3) {
            }
        }
        if (releaseConnectionNoEvents != null) {
            Util.closeQuietly(releaseConnectionNoEvents);
        }
        if (((Connection) objectRef.element) != null) {
            EventListener eventListener2 = this.eventListener;
            Call call2 = this.call;
            Connection connection2 = (Connection) objectRef.element;
            if (connection2 == null) {
                Intrinsics.throwNpe();
            }
            eventListener2.connectionReleased(call2, connection2);
        }
        if (z4) {
            if (e != null) {
                z2 = true;
            }
            e = timeoutExit(e);
            if (z2) {
                EventListener eventListener3 = this.eventListener;
                Call call3 = this.call;
                if (e == null) {
                    Intrinsics.throwNpe();
                }
                eventListener3.callFailed(call3, e);
            } else {
                this.eventListener.callEnd(this.call);
            }
        }
        return e;
    }

    public final boolean canRetry() {
        ExchangeFinder exchangeFinder2 = this.exchangeFinder;
        if (exchangeFinder2 == null) {
            Intrinsics.throwNpe();
        }
        if (exchangeFinder2.hasStreamFailure()) {
            ExchangeFinder exchangeFinder3 = this.exchangeFinder;
            if (exchangeFinder3 == null) {
                Intrinsics.throwNpe();
            }
            if (exchangeFinder3.hasRouteToTry()) {
                return true;
            }
        }
        return false;
    }

    public final boolean hasExchange() {
        boolean z;
        synchronized (this.connectionPool) {
            z = this.exchange != null;
        }
        return z;
    }

    public final void cancel() {
        Exchange exchange2;
        RealConnection realConnection;
        synchronized (this.connectionPool) {
            this.canceled = true;
            exchange2 = this.exchange;
            ExchangeFinder exchangeFinder2 = this.exchangeFinder;
            if (exchangeFinder2 != null) {
                realConnection = exchangeFinder2.connectingConnection();
                if (realConnection != null) {
                    Unit unit = Unit.INSTANCE;
                }
            }
            realConnection = this.connection;
            Unit unit2 = Unit.INSTANCE;
        }
        if (exchange2 != null) {
            exchange2.cancel();
        } else if (realConnection != null) {
            realConnection.cancel();
        }
    }
}
