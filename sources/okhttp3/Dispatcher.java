package okhttp3;

import androidx.core.app.NotificationCompat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.RealCall.AsyncCall;
import okhttp3.internal.Util;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B\u0005¢\u0006\u0002\u0010\u0005J\u0006\u0010\u001e\u001a\u00020\u001fJ\u0019\u0010 \u001a\u00020\u001f2\n\u0010!\u001a\u00060\u001aR\u00020\u001bH\u0000¢\u0006\u0002\b\"J\u0015\u0010#\u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\u001bH\u0000¢\u0006\u0002\b$J\r\u0010\u0002\u001a\u00020\u0003H\u0007¢\u0006\u0002\b%J\u0016\u0010&\u001a\b\u0018\u00010\u001aR\u00020\u001b2\u0006\u0010'\u001a\u00020(H\u0002J)\u0010)\u001a\u00020\u001f\"\u0004\b\u0000\u0010*2\f\u0010+\u001a\b\u0012\u0004\u0012\u0002H*0,2\u0006\u0010!\u001a\u0002H*H\u0002¢\u0006\u0002\u0010-J\u0015\u0010)\u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\u001bH\u0000¢\u0006\u0002\b.J\u0019\u0010)\u001a\u00020\u001f2\n\u0010!\u001a\u00060\u001aR\u00020\u001bH\u0000¢\u0006\u0002\b.J\b\u0010/\u001a\u000200H\u0002J\f\u00101\u001a\b\u0012\u0004\u0012\u00020302J\u0006\u00104\u001a\u00020\u0010J\f\u00105\u001a\b\u0012\u0004\u0012\u00020302J\u0006\u00106\u001a\u00020\u0010R\u0011\u0010\u0002\u001a\u00020\u00038G¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0006R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u0002\n\u0000R*\u0010\n\u001a\u0004\u0018\u00010\t2\b\u0010\b\u001a\u0004\u0018\u00010\t8F@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR&\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u00108F@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R&\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u00108F@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0012\"\u0004\b\u0017\u0010\u0014R\u0018\u0010\u0018\u001a\f\u0012\b\u0012\u00060\u001aR\u00020\u001b0\u0019X\u0004¢\u0006\u0002\n\u0000R\u0018\u0010\u001c\u001a\f\u0012\b\u0012\u00060\u001aR\u00020\u001b0\u0019X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001b0\u0019X\u0004¢\u0006\u0002\n\u0000¨\u00067"}, d2 = {"Lokhttp3/Dispatcher;", "", "executorService", "Ljava/util/concurrent/ExecutorService;", "(Ljava/util/concurrent/ExecutorService;)V", "()V", "()Ljava/util/concurrent/ExecutorService;", "executorServiceOrNull", "<set-?>", "Ljava/lang/Runnable;", "idleCallback", "getIdleCallback", "()Ljava/lang/Runnable;", "setIdleCallback", "(Ljava/lang/Runnable;)V", "maxRequests", "", "getMaxRequests", "()I", "setMaxRequests", "(I)V", "maxRequestsPerHost", "getMaxRequestsPerHost", "setMaxRequestsPerHost", "readyAsyncCalls", "Ljava/util/ArrayDeque;", "Lokhttp3/RealCall$AsyncCall;", "Lokhttp3/RealCall;", "runningAsyncCalls", "runningSyncCalls", "cancelAll", "", "enqueue", "call", "enqueue$okhttp", "executed", "executed$okhttp", "-deprecated_executorService", "findExistingCallWithHost", "host", "", "finished", "T", "calls", "Ljava/util/Deque;", "(Ljava/util/Deque;Ljava/lang/Object;)V", "finished$okhttp", "promoteAndExecute", "", "queuedCalls", "", "Lokhttp3/Call;", "queuedCallsCount", "runningCalls", "runningCallsCount", "okhttp"}, k = 1, mv = {1, 1, 15})
/* compiled from: Dispatcher.kt */
public final class Dispatcher {
    private ExecutorService executorServiceOrNull;
    private Runnable idleCallback;
    private int maxRequests;
    private int maxRequestsPerHost;
    private final ArrayDeque<AsyncCall> readyAsyncCalls;
    private final ArrayDeque<AsyncCall> runningAsyncCalls;
    private final ArrayDeque<RealCall> runningSyncCalls;

    public Dispatcher() {
        this.maxRequests = 64;
        this.maxRequestsPerHost = 5;
        this.readyAsyncCalls = new ArrayDeque<>();
        this.runningAsyncCalls = new ArrayDeque<>();
        this.runningSyncCalls = new ArrayDeque<>();
    }

    public final synchronized int getMaxRequests() {
        return this.maxRequests;
    }

    public final void setMaxRequests(int i) {
        boolean z = true;
        if (i < 1) {
            z = false;
        }
        if (z) {
            synchronized (this) {
                this.maxRequests = i;
                Unit unit = Unit.INSTANCE;
            }
            promoteAndExecute();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("max < 1: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString().toString());
    }

    public final synchronized int getMaxRequestsPerHost() {
        return this.maxRequestsPerHost;
    }

    public final void setMaxRequestsPerHost(int i) {
        boolean z = true;
        if (i < 1) {
            z = false;
        }
        if (z) {
            synchronized (this) {
                this.maxRequestsPerHost = i;
                Unit unit = Unit.INSTANCE;
            }
            promoteAndExecute();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("max < 1: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString().toString());
    }

    public final synchronized Runnable getIdleCallback() {
        return this.idleCallback;
    }

    public final synchronized void setIdleCallback(Runnable runnable) {
        this.idleCallback = runnable;
    }

    public final synchronized ExecutorService executorService() {
        ExecutorService executorService;
        if (this.executorServiceOrNull == null) {
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp Dispatcher", false));
            this.executorServiceOrNull = threadPoolExecutor;
        }
        executorService = this.executorServiceOrNull;
        if (executorService == null) {
            Intrinsics.throwNpe();
        }
        return executorService;
    }

    public Dispatcher(ExecutorService executorService) {
        Intrinsics.checkParameterIsNotNull(executorService, "executorService");
        this();
        this.executorServiceOrNull = executorService;
    }

    public final void enqueue$okhttp(AsyncCall asyncCall) {
        Intrinsics.checkParameterIsNotNull(asyncCall, NotificationCompat.CATEGORY_CALL);
        synchronized (this) {
            this.readyAsyncCalls.add(asyncCall);
            if (!asyncCall.get().getForWebSocket()) {
                AsyncCall findExistingCallWithHost = findExistingCallWithHost(asyncCall.host());
                if (findExistingCallWithHost != null) {
                    asyncCall.reuseCallsPerHostFrom(findExistingCallWithHost);
                }
            }
            Unit unit = Unit.INSTANCE;
        }
        promoteAndExecute();
    }

    private final AsyncCall findExistingCallWithHost(String str) {
        Iterator it = this.runningAsyncCalls.iterator();
        while (it.hasNext()) {
            AsyncCall asyncCall = (AsyncCall) it.next();
            if (Intrinsics.areEqual((Object) asyncCall.host(), (Object) str)) {
                return asyncCall;
            }
        }
        Iterator it2 = this.readyAsyncCalls.iterator();
        while (it2.hasNext()) {
            AsyncCall asyncCall2 = (AsyncCall) it2.next();
            if (Intrinsics.areEqual((Object) asyncCall2.host(), (Object) str)) {
                return asyncCall2;
            }
        }
        return null;
    }

    public final synchronized void cancelAll() {
        Iterator it = this.readyAsyncCalls.iterator();
        while (it.hasNext()) {
            ((AsyncCall) it.next()).get().cancel();
        }
        Iterator it2 = this.runningAsyncCalls.iterator();
        while (it2.hasNext()) {
            ((AsyncCall) it2.next()).get().cancel();
        }
        Iterator it3 = this.runningSyncCalls.iterator();
        while (it3.hasNext()) {
            ((RealCall) it3.next()).cancel();
        }
    }

    private final boolean promoteAndExecute() {
        int i;
        boolean z = true;
        boolean z2 = !Thread.holdsLock(this);
        if (!_Assertions.ENABLED || z2) {
            List arrayList = new ArrayList();
            synchronized (this) {
                Iterator it = this.readyAsyncCalls.iterator();
                Intrinsics.checkExpressionValueIsNotNull(it, "readyAsyncCalls.iterator()");
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    AsyncCall asyncCall = (AsyncCall) it.next();
                    if (this.runningAsyncCalls.size() >= this.maxRequests) {
                        break;
                    } else if (asyncCall.callsPerHost().get() < this.maxRequestsPerHost) {
                        it.remove();
                        asyncCall.callsPerHost().incrementAndGet();
                        Intrinsics.checkExpressionValueIsNotNull(asyncCall, "asyncCall");
                        arrayList.add(asyncCall);
                        this.runningAsyncCalls.add(asyncCall);
                    }
                }
                if (runningCallsCount() <= 0) {
                    z = false;
                }
                Unit unit = Unit.INSTANCE;
            }
            int size = arrayList.size();
            for (i = 0; i < size; i++) {
                ((AsyncCall) arrayList.get(i)).executeOn(executorService());
            }
            return z;
        }
        throw new AssertionError("Assertion failed");
    }

    public final synchronized void executed$okhttp(RealCall realCall) {
        Intrinsics.checkParameterIsNotNull(realCall, NotificationCompat.CATEGORY_CALL);
        this.runningSyncCalls.add(realCall);
    }

    public final void finished$okhttp(AsyncCall asyncCall) {
        Intrinsics.checkParameterIsNotNull(asyncCall, NotificationCompat.CATEGORY_CALL);
        asyncCall.callsPerHost().decrementAndGet();
        finished(this.runningAsyncCalls, asyncCall);
    }

    public final void finished$okhttp(RealCall realCall) {
        Intrinsics.checkParameterIsNotNull(realCall, NotificationCompat.CATEGORY_CALL);
        finished(this.runningSyncCalls, realCall);
    }

    private final <T> void finished(Deque<T> deque, T t) {
        Runnable runnable;
        synchronized (this) {
            if (deque.remove(t)) {
                runnable = this.idleCallback;
                Unit unit = Unit.INSTANCE;
            } else {
                throw new AssertionError("Call wasn't in-flight!");
            }
        }
        if (!promoteAndExecute() && runnable != null) {
            runnable.run();
        }
    }

    public final synchronized List<Call> queuedCalls() {
        List<Call> unmodifiableList;
        Iterable<AsyncCall> iterable = this.readyAsyncCalls;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (AsyncCall asyncCall : iterable) {
            arrayList.add(asyncCall.get());
        }
        unmodifiableList = Collections.unmodifiableList((List) arrayList);
        Intrinsics.checkExpressionValueIsNotNull(unmodifiableList, "Collections.unmodifiable…ncCalls.map { it.get() })");
        return unmodifiableList;
    }

    public final synchronized List<Call> runningCalls() {
        List<Call> unmodifiableList;
        Collection collection = this.runningSyncCalls;
        Iterable<AsyncCall> iterable = this.runningAsyncCalls;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (AsyncCall asyncCall : iterable) {
            arrayList.add(asyncCall.get());
        }
        unmodifiableList = Collections.unmodifiableList(CollectionsKt.plus(collection, (Iterable<? extends T>) (List) arrayList));
        Intrinsics.checkExpressionValueIsNotNull(unmodifiableList, "Collections.unmodifiable…ncCalls.map { it.get() })");
        return unmodifiableList;
    }

    public final synchronized int queuedCallsCount() {
        return this.readyAsyncCalls.size();
    }

    public final synchronized int runningCallsCount() {
        return this.runningAsyncCalls.size() + this.runningSyncCalls.size();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "executorService", imports = {}))
    /* renamed from: -deprecated_executorService reason: not valid java name */
    public final ExecutorService m1017deprecated_executorService() {
        return executorService();
    }
}
