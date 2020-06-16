package okhttp3.internal.connection;

import java.io.IOException;
import java.lang.ref.Reference;
import java.net.Proxy.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Address;
import okhttp3.ConnectionPool;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Transmitter.TransmitterReference;
import okhttp3.internal.platform.Platform;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000i\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003*\u0001\n\u0018\u0000 12\u00020\u0001:\u00011B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u000e\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u001b\u001a\u00020\u0005J\u0016\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!J\u000e\u0010\"\u001a\u00020\r2\u0006\u0010#\u001a\u00020\u0014J\u0006\u0010$\u001a\u00020\u0003J\u0006\u0010%\u001a\u00020\u001dJ\u0006\u0010&\u001a\u00020\u0003J\u0018\u0010'\u001a\u00020\u00032\u0006\u0010#\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u0005H\u0002J\u000e\u0010(\u001a\u00020\u001d2\u0006\u0010#\u001a\u00020\u0014J.\u0010)\u001a\u00020\r2\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020-2\u000e\u0010.\u001a\n\u0012\u0004\u0012\u00020\u001f\u0018\u00010/2\u0006\u00100\u001a\u00020\rR\u0010\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0004\n\u0002\u0010\u000bR\u001a\u0010\f\u001a\u00020\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0016\u001a\u00020\u0017¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019¨\u00062"}, d2 = {"Lokhttp3/internal/connection/RealConnectionPool;", "", "maxIdleConnections", "", "keepAliveDuration", "", "timeUnit", "Ljava/util/concurrent/TimeUnit;", "(IJLjava/util/concurrent/TimeUnit;)V", "cleanupRunnable", "okhttp3/internal/connection/RealConnectionPool$cleanupRunnable$1", "Lokhttp3/internal/connection/RealConnectionPool$cleanupRunnable$1;", "cleanupRunning", "", "getCleanupRunning", "()Z", "setCleanupRunning", "(Z)V", "connections", "Ljava/util/ArrayDeque;", "Lokhttp3/internal/connection/RealConnection;", "keepAliveDurationNs", "routeDatabase", "Lokhttp3/internal/connection/RouteDatabase;", "getRouteDatabase", "()Lokhttp3/internal/connection/RouteDatabase;", "cleanup", "now", "connectFailed", "", "failedRoute", "Lokhttp3/Route;", "failure", "Ljava/io/IOException;", "connectionBecameIdle", "connection", "connectionCount", "evictAll", "idleConnectionCount", "pruneAndGetAllocationCount", "put", "transmitterAcquirePooledConnection", "address", "Lokhttp3/Address;", "transmitter", "Lokhttp3/internal/connection/Transmitter;", "routes", "", "requireMultiplexed", "Companion", "okhttp"}, k = 1, mv = {1, 1, 15})
/* compiled from: RealConnectionPool.kt */
public final class RealConnectionPool {
    public static final Companion Companion = new Companion(null);
    private static final ThreadPoolExecutor executor;
    private final RealConnectionPool$cleanupRunnable$1 cleanupRunnable = new RealConnectionPool$cleanupRunnable$1(this);
    private boolean cleanupRunning;
    private final ArrayDeque<RealConnection> connections = new ArrayDeque<>();
    private final long keepAliveDurationNs;
    private final int maxIdleConnections;
    private final RouteDatabase routeDatabase = new RouteDatabase();

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lokhttp3/internal/connection/RealConnectionPool$Companion;", "", "()V", "executor", "Ljava/util/concurrent/ThreadPoolExecutor;", "get", "Lokhttp3/internal/connection/RealConnectionPool;", "connectionPool", "Lokhttp3/ConnectionPool;", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: RealConnectionPool.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final RealConnectionPool get(ConnectionPool connectionPool) {
            Intrinsics.checkParameterIsNotNull(connectionPool, "connectionPool");
            return connectionPool.getDelegate$okhttp();
        }
    }

    public RealConnectionPool(int i, long j, TimeUnit timeUnit) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "timeUnit");
        this.maxIdleConnections = i;
        this.keepAliveDurationNs = timeUnit.toNanos(j);
        if (!(j > 0)) {
            StringBuilder sb = new StringBuilder();
            sb.append("keepAliveDuration <= 0: ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString().toString());
        }
    }

    public final RouteDatabase getRouteDatabase() {
        return this.routeDatabase;
    }

    public final boolean getCleanupRunning() {
        return this.cleanupRunning;
    }

    public final void setCleanupRunning(boolean z) {
        this.cleanupRunning = z;
    }

    public final synchronized int idleConnectionCount() {
        int i;
        Iterable<RealConnection> iterable = this.connections;
        i = 0;
        if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
            for (RealConnection transmitters : iterable) {
                if (transmitters.getTransmitters().isEmpty()) {
                    i++;
                    if (i < 0) {
                        CollectionsKt.throwCountOverflow();
                    }
                }
            }
        }
        return i;
    }

    public final synchronized int connectionCount() {
        return this.connections.size();
    }

    public final boolean transmitterAcquirePooledConnection(Address address, Transmitter transmitter, List<Route> list, boolean z) {
        Intrinsics.checkParameterIsNotNull(address, "address");
        Intrinsics.checkParameterIsNotNull(transmitter, "transmitter");
        boolean holdsLock = Thread.holdsLock(this);
        if (!_Assertions.ENABLED || holdsLock) {
            Iterator it = this.connections.iterator();
            while (it.hasNext()) {
                RealConnection realConnection = (RealConnection) it.next();
                if ((!z || realConnection.isMultiplexed()) && realConnection.isEligible$okhttp(address, list)) {
                    Intrinsics.checkExpressionValueIsNotNull(realConnection, "connection");
                    transmitter.acquireConnectionNoEvents(realConnection);
                    return true;
                }
            }
            return false;
        }
        throw new AssertionError("Assertion failed");
    }

    public final void put(RealConnection realConnection) {
        Intrinsics.checkParameterIsNotNull(realConnection, "connection");
        boolean holdsLock = Thread.holdsLock(this);
        if (!_Assertions.ENABLED || holdsLock) {
            if (!this.cleanupRunning) {
                this.cleanupRunning = true;
                executor.execute(this.cleanupRunnable);
            }
            this.connections.add(realConnection);
            return;
        }
        throw new AssertionError("Assertion failed");
    }

    public final boolean connectionBecameIdle(RealConnection realConnection) {
        Intrinsics.checkParameterIsNotNull(realConnection, "connection");
        boolean holdsLock = Thread.holdsLock(this);
        if (_Assertions.ENABLED && !holdsLock) {
            throw new AssertionError("Assertion failed");
        } else if (realConnection.getNoNewExchanges() || this.maxIdleConnections == 0) {
            this.connections.remove(realConnection);
            return true;
        } else {
            notifyAll();
            return false;
        }
    }

    public final void evictAll() {
        List<RealConnection> arrayList = new ArrayList<>();
        synchronized (this) {
            Iterator it = this.connections.iterator();
            Intrinsics.checkExpressionValueIsNotNull(it, "connections.iterator()");
            while (it.hasNext()) {
                RealConnection realConnection = (RealConnection) it.next();
                if (realConnection.getTransmitters().isEmpty()) {
                    realConnection.setNoNewExchanges(true);
                    Intrinsics.checkExpressionValueIsNotNull(realConnection, "connection");
                    arrayList.add(realConnection);
                    it.remove();
                }
            }
            Unit unit = Unit.INSTANCE;
        }
        for (RealConnection socket : arrayList) {
            Util.closeQuietly(socket.socket());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x005c, code lost:
        if (r0 != null) goto L_0x0061;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x005e, code lost:
        kotlin.jvm.internal.Intrinsics.throwNpe();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0061, code lost:
        okhttp3.internal.Util.closeQuietly(r0.socket());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x006a, code lost:
        return 0;
     */
    public final long cleanup(long j) {
        RealConnection realConnection = null;
        synchronized (this) {
            Iterator it = this.connections.iterator();
            long j2 = Long.MIN_VALUE;
            int i = 0;
            int i2 = 0;
            while (it.hasNext()) {
                RealConnection realConnection2 = (RealConnection) it.next();
                Intrinsics.checkExpressionValueIsNotNull(realConnection2, "connection");
                if (pruneAndGetAllocationCount(realConnection2, j) > 0) {
                    i2++;
                } else {
                    i++;
                    long idleAtNanos$okhttp = j - realConnection2.getIdleAtNanos$okhttp();
                    if (idleAtNanos$okhttp > j2) {
                        realConnection = realConnection2;
                        j2 = idleAtNanos$okhttp;
                    }
                }
            }
            if (j2 < this.keepAliveDurationNs) {
                if (i <= this.maxIdleConnections) {
                    if (i > 0) {
                        long j3 = this.keepAliveDurationNs - j2;
                        return j3;
                    } else if (i2 > 0) {
                        long j4 = this.keepAliveDurationNs;
                        return j4;
                    } else {
                        this.cleanupRunning = false;
                        return -1;
                    }
                }
            }
            this.connections.remove(realConnection);
        }
    }

    private final int pruneAndGetAllocationCount(RealConnection realConnection, long j) {
        List transmitters = realConnection.getTransmitters();
        int i = 0;
        while (i < transmitters.size()) {
            Reference reference = (Reference) transmitters.get(i);
            if (reference.get() != null) {
                i++;
            } else if (reference != null) {
                TransmitterReference transmitterReference = (TransmitterReference) reference;
                StringBuilder sb = new StringBuilder();
                sb.append("A connection to ");
                sb.append(realConnection.route().address().url());
                sb.append(" was leaked. ");
                sb.append("Did you forget to close a response body?");
                Platform.Companion.get().logCloseableLeak(sb.toString(), transmitterReference.getCallStackTrace());
                transmitters.remove(i);
                realConnection.setNoNewExchanges(true);
                if (transmitters.isEmpty()) {
                    realConnection.setIdleAtNanos$okhttp(j - this.keepAliveDurationNs);
                    return 0;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type okhttp3.internal.connection.Transmitter.TransmitterReference");
            }
        }
        return transmitters.size();
    }

    public final void connectFailed(Route route, IOException iOException) {
        Intrinsics.checkParameterIsNotNull(route, "failedRoute");
        Intrinsics.checkParameterIsNotNull(iOException, "failure");
        if (route.proxy().type() != Type.DIRECT) {
            Address address = route.address();
            address.proxySelector().connectFailed(address.url().uri(), route.proxy().address(), iOException);
        }
        this.routeDatabase.failed(route);
    }

    static {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp ConnectionPool", true));
        executor = threadPoolExecutor;
    }
}
