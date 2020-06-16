package okhttp3.internal.connection;

import androidx.core.app.NotificationCompat;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref.ObjectRef;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RouteSelector.Selection;
import okhttp3.internal.http.ExchangeCodec;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\b\u0010\r\u001a\u0004\u0018\u00010\u000eJ\u001e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0010J0\u0010\u001e\u001a\u00020\u000e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020 2\u0006\u0010\"\u001a\u00020 2\u0006\u0010#\u001a\u00020 2\u0006\u0010$\u001a\u00020\u0010H\u0002J8\u0010%\u001a\u00020\u000e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020 2\u0006\u0010\"\u001a\u00020 2\u0006\u0010#\u001a\u00020 2\u0006\u0010$\u001a\u00020\u00102\u0006\u0010\u001d\u001a\u00020\u0010H\u0002J\u0006\u0010&\u001a\u00020\u0010J\u0006\u0010\u000f\u001a\u00020\u0010J\b\u0010'\u001a\u00020\u0010H\u0002J\u0006\u0010(\u001a\u00020)R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006*"}, d2 = {"Lokhttp3/internal/connection/ExchangeFinder;", "", "transmitter", "Lokhttp3/internal/connection/Transmitter;", "connectionPool", "Lokhttp3/internal/connection/RealConnectionPool;", "address", "Lokhttp3/Address;", "call", "Lokhttp3/Call;", "eventListener", "Lokhttp3/EventListener;", "(Lokhttp3/internal/connection/Transmitter;Lokhttp3/internal/connection/RealConnectionPool;Lokhttp3/Address;Lokhttp3/Call;Lokhttp3/EventListener;)V", "connectingConnection", "Lokhttp3/internal/connection/RealConnection;", "hasStreamFailure", "", "nextRouteToTry", "Lokhttp3/Route;", "routeSelection", "Lokhttp3/internal/connection/RouteSelector$Selection;", "routeSelector", "Lokhttp3/internal/connection/RouteSelector;", "find", "Lokhttp3/internal/http/ExchangeCodec;", "client", "Lokhttp3/OkHttpClient;", "chain", "Lokhttp3/Interceptor$Chain;", "doExtensiveHealthChecks", "findConnection", "connectTimeout", "", "readTimeout", "writeTimeout", "pingIntervalMillis", "connectionRetryEnabled", "findHealthyConnection", "hasRouteToTry", "retryCurrentRoute", "trackFailure", "", "okhttp"}, k = 1, mv = {1, 1, 15})
/* compiled from: ExchangeFinder.kt */
public final class ExchangeFinder {
    private final Address address;
    private final Call call;
    private RealConnection connectingConnection;
    private final RealConnectionPool connectionPool;
    private final EventListener eventListener;
    private boolean hasStreamFailure;
    private Route nextRouteToTry;
    private Selection routeSelection;
    private final RouteSelector routeSelector = new RouteSelector(this.address, this.connectionPool.getRouteDatabase(), this.call, this.eventListener);
    private final Transmitter transmitter;

    public ExchangeFinder(Transmitter transmitter2, RealConnectionPool realConnectionPool, Address address2, Call call2, EventListener eventListener2) {
        Intrinsics.checkParameterIsNotNull(transmitter2, "transmitter");
        Intrinsics.checkParameterIsNotNull(realConnectionPool, "connectionPool");
        Intrinsics.checkParameterIsNotNull(address2, "address");
        Intrinsics.checkParameterIsNotNull(call2, NotificationCompat.CATEGORY_CALL);
        Intrinsics.checkParameterIsNotNull(eventListener2, "eventListener");
        this.transmitter = transmitter2;
        this.connectionPool = realConnectionPool;
        this.address = address2;
        this.call = call2;
        this.eventListener = eventListener2;
    }

    public final ExchangeCodec find(OkHttpClient okHttpClient, Chain chain, boolean z) {
        Intrinsics.checkParameterIsNotNull(okHttpClient, "client");
        Intrinsics.checkParameterIsNotNull(chain, "chain");
        try {
            return findHealthyConnection(chain.connectTimeoutMillis(), chain.readTimeoutMillis(), chain.writeTimeoutMillis(), okHttpClient.pingIntervalMillis(), okHttpClient.retryOnConnectionFailure(), z).newCodec$okhttp(okHttpClient, chain);
        } catch (RouteException e) {
            trackFailure();
            throw e;
        } catch (IOException e2) {
            trackFailure();
            throw new RouteException(e2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0016, code lost:
        if (r0.isHealthy(r9) != false) goto L_0x001c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001c, code lost:
        return r0;
     */
    private final RealConnection findHealthyConnection(int i, int i2, int i3, int i4, boolean z, boolean z2) throws IOException {
        while (true) {
            RealConnection findConnection = findConnection(i, i2, i3, i4, z);
            synchronized (this.connectionPool) {
                if (findConnection.getSuccessCount$okhttp() == 0) {
                    return findConnection;
                }
                Unit unit = Unit.INSTANCE;
            }
            findConnection.noNewExchanges();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00d9, code lost:
        if (r4.hasNext() == false) goto L_0x00db;
     */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x004d  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x005b  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00ec A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0136  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x014b  */
    private final RealConnection findConnection(int i, int i2, int i3, int i4, boolean z) throws IOException {
        Socket socket;
        boolean z2;
        boolean z3;
        RealConnection realConnection = null;
        Route route = null;
        ObjectRef objectRef = new ObjectRef();
        synchronized (this.connectionPool) {
            if (!this.transmitter.isCanceled()) {
                this.hasStreamFailure = false;
                objectRef.element = this.transmitter.getConnection();
                if (this.transmitter.getConnection() != null) {
                    RealConnection connection = this.transmitter.getConnection();
                    if (connection == null) {
                        Intrinsics.throwNpe();
                    }
                    if (connection.getNoNewExchanges()) {
                        socket = this.transmitter.releaseConnectionNoEvents();
                        if (this.transmitter.getConnection() != null) {
                            realConnection = this.transmitter.getConnection();
                            objectRef.element = (RealConnection) null;
                        }
                        if (realConnection == null) {
                            if (this.connectionPool.transmitterAcquirePooledConnection(this.address, this.transmitter, null, false)) {
                                realConnection = this.transmitter.getConnection();
                                z2 = true;
                                Unit unit = Unit.INSTANCE;
                            } else if (this.nextRouteToTry != null) {
                                route = this.nextRouteToTry;
                                this.nextRouteToTry = null;
                            } else if (retryCurrentRoute()) {
                                RealConnection connection2 = this.transmitter.getConnection();
                                if (connection2 == null) {
                                    Intrinsics.throwNpe();
                                }
                                route = connection2.route();
                            }
                        }
                        z2 = false;
                        Unit unit2 = Unit.INSTANCE;
                    }
                }
                socket = null;
                if (this.transmitter.getConnection() != null) {
                }
                if (realConnection == null) {
                }
                z2 = false;
                Unit unit22 = Unit.INSTANCE;
            } else {
                throw new IOException("Canceled");
            }
        }
        if (socket != null) {
            Util.closeQuietly(socket);
        }
        if (((RealConnection) objectRef.element) != null) {
            EventListener eventListener2 = this.eventListener;
            Call call2 = this.call;
            RealConnection realConnection2 = (RealConnection) objectRef.element;
            if (realConnection2 == null) {
                Intrinsics.throwNpe();
            }
            eventListener2.connectionReleased(call2, realConnection2);
        }
        if (z2) {
            EventListener eventListener3 = this.eventListener;
            Call call3 = this.call;
            if (realConnection == null) {
                Intrinsics.throwNpe();
            }
            eventListener3.connectionAcquired(call3, realConnection);
        }
        if (realConnection != null) {
            if (realConnection == null) {
                Intrinsics.throwNpe();
            }
            return realConnection;
        }
        if (route == null) {
            Selection selection = this.routeSelection;
            if (selection != null) {
                if (selection == null) {
                    Intrinsics.throwNpe();
                }
            }
            this.routeSelection = this.routeSelector.next();
            z3 = true;
            List list = null;
            synchronized (this.connectionPool) {
                if (!this.transmitter.isCanceled()) {
                    if (z3) {
                        Selection selection2 = this.routeSelection;
                        if (selection2 == null) {
                            Intrinsics.throwNpe();
                        }
                        list = selection2.getRoutes();
                        if (this.connectionPool.transmitterAcquirePooledConnection(this.address, this.transmitter, list, false)) {
                            realConnection = this.transmitter.getConnection();
                            z2 = true;
                        }
                    }
                    if (!z2) {
                        if (route == null) {
                            Selection selection3 = this.routeSelection;
                            if (selection3 == null) {
                                Intrinsics.throwNpe();
                            }
                            route = selection3.next();
                        }
                        RealConnectionPool realConnectionPool = this.connectionPool;
                        if (route == null) {
                            Intrinsics.throwNpe();
                        }
                        realConnection = new RealConnection(realConnectionPool, route);
                        this.connectingConnection = realConnection;
                    }
                    Unit unit3 = Unit.INSTANCE;
                } else {
                    throw new IOException("Canceled");
                }
            }
            if (!z2) {
                EventListener eventListener4 = this.eventListener;
                Call call4 = this.call;
                if (realConnection == null) {
                    Intrinsics.throwNpe();
                }
                eventListener4.connectionAcquired(call4, realConnection);
                if (realConnection == null) {
                    Intrinsics.throwNpe();
                }
                return realConnection;
            }
            if (realConnection == null) {
                Intrinsics.throwNpe();
            }
            realConnection.connect(i, i2, i3, i4, z, this.call, this.eventListener);
            RouteDatabase routeDatabase = this.connectionPool.getRouteDatabase();
            if (realConnection == null) {
                Intrinsics.throwNpe();
            }
            routeDatabase.connected(realConnection.route());
            Socket socket2 = null;
            synchronized (this.connectionPool) {
                this.connectingConnection = null;
                if (this.connectionPool.transmitterAcquirePooledConnection(this.address, this.transmitter, list, true)) {
                    if (realConnection == null) {
                        Intrinsics.throwNpe();
                    }
                    realConnection.setNoNewExchanges(true);
                    if (realConnection == null) {
                        Intrinsics.throwNpe();
                    }
                    socket2 = realConnection.socket();
                    realConnection = this.transmitter.getConnection();
                    this.nextRouteToTry = route;
                } else {
                    RealConnectionPool realConnectionPool2 = this.connectionPool;
                    if (realConnection == null) {
                        Intrinsics.throwNpe();
                    }
                    realConnectionPool2.put(realConnection);
                    Transmitter transmitter2 = this.transmitter;
                    if (realConnection == null) {
                        Intrinsics.throwNpe();
                    }
                    transmitter2.acquireConnectionNoEvents(realConnection);
                }
                Unit unit4 = Unit.INSTANCE;
            }
            if (socket2 != null) {
                Util.closeQuietly(socket2);
            }
            EventListener eventListener5 = this.eventListener;
            Call call5 = this.call;
            if (realConnection == null) {
                Intrinsics.throwNpe();
            }
            eventListener5.connectionAcquired(call5, realConnection);
            if (realConnection == null) {
                Intrinsics.throwNpe();
            }
            return realConnection;
        }
        z3 = false;
        List list2 = null;
        synchronized (this.connectionPool) {
        }
        if (!z2) {
        }
    }

    public final RealConnection connectingConnection() {
        boolean holdsLock = Thread.holdsLock(this.connectionPool);
        if (!_Assertions.ENABLED || holdsLock) {
            return this.connectingConnection;
        }
        throw new AssertionError("Assertion failed");
    }

    public final void trackFailure() {
        boolean z = !Thread.holdsLock(this.connectionPool);
        if (!_Assertions.ENABLED || z) {
            synchronized (this.connectionPool) {
                this.hasStreamFailure = true;
                Unit unit = Unit.INSTANCE;
            }
            return;
        }
        throw new AssertionError("Assertion failed");
    }

    public final boolean hasStreamFailure() {
        boolean z;
        synchronized (this.connectionPool) {
            z = this.hasStreamFailure;
        }
        return z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x003b, code lost:
        return r2;
     */
    public final boolean hasRouteToTry() {
        synchronized (this.connectionPool) {
            boolean z = true;
            if (this.nextRouteToTry != null) {
                return true;
            }
            if (retryCurrentRoute()) {
                RealConnection connection = this.transmitter.getConnection();
                if (connection == null) {
                    Intrinsics.throwNpe();
                }
                this.nextRouteToTry = connection.route();
                return true;
            }
            Selection selection = this.routeSelection;
            if (!(selection != null ? selection.hasNext() : false) && !this.routeSelector.hasNext()) {
                z = false;
            }
        }
    }

    private final boolean retryCurrentRoute() {
        if (this.transmitter.getConnection() != null) {
            RealConnection connection = this.transmitter.getConnection();
            if (connection == null) {
                Intrinsics.throwNpe();
            }
            if (connection.getRouteFailureCount$okhttp() == 0) {
                RealConnection connection2 = this.transmitter.getConnection();
                if (connection2 == null) {
                    Intrinsics.throwNpe();
                }
                if (Util.canReuseConnectionFor(connection2.route().address().url(), this.address.url())) {
                    return true;
                }
            }
        }
        return false;
    }
}
