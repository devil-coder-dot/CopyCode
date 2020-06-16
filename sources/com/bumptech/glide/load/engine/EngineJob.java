package com.bumptech.glide.load.engine;

import androidx.core.util.Pools.Pool;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.pool.FactoryPools.Poolable;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

class EngineJob<R> implements Callback<R>, Poolable {
    private static final EngineResourceFactory DEFAULT_FACTORY = new EngineResourceFactory();
    private final GlideExecutor animationExecutor;
    final ResourceCallbacksAndExecutors cbs;
    DataSource dataSource;
    private DecodeJob<R> decodeJob;
    private final GlideExecutor diskCacheExecutor;
    private final EngineJobListener engineJobListener;
    EngineResource<?> engineResource;
    private final EngineResourceFactory engineResourceFactory;
    GlideException exception;
    private boolean hasLoadFailed;
    private boolean hasResource;
    private boolean isCacheable;
    private volatile boolean isCancelled;
    private Key key;
    private boolean onlyRetrieveFromCache;
    private final AtomicInteger pendingCallbacks;
    private final Pool<EngineJob<?>> pool;
    private Resource<?> resource;
    private final ResourceListener resourceListener;
    private final GlideExecutor sourceExecutor;
    private final GlideExecutor sourceUnlimitedExecutor;
    private final StateVerifier stateVerifier;
    private boolean useAnimationPool;
    private boolean useUnlimitedSourceGeneratorPool;

    private class CallLoadFailed implements Runnable {
        private final ResourceCallback cb;

        CallLoadFailed(ResourceCallback resourceCallback) {
            this.cb = resourceCallback;
        }

        public void run() {
            synchronized (this.cb.getLock()) {
                synchronized (EngineJob.this) {
                    if (EngineJob.this.cbs.contains(this.cb)) {
                        EngineJob.this.callCallbackOnLoadFailed(this.cb);
                    }
                    EngineJob.this.decrementPendingCallbacks();
                }
            }
        }
    }

    private class CallResourceReady implements Runnable {
        private final ResourceCallback cb;

        CallResourceReady(ResourceCallback resourceCallback) {
            this.cb = resourceCallback;
        }

        public void run() {
            synchronized (this.cb.getLock()) {
                synchronized (EngineJob.this) {
                    if (EngineJob.this.cbs.contains(this.cb)) {
                        EngineJob.this.engineResource.acquire();
                        EngineJob.this.callCallbackOnResourceReady(this.cb);
                        EngineJob.this.removeCallback(this.cb);
                    }
                    EngineJob.this.decrementPendingCallbacks();
                }
            }
        }
    }

    static class EngineResourceFactory {
        EngineResourceFactory() {
        }

        public <R> EngineResource<R> build(Resource<R> resource, boolean z, Key key, ResourceListener resourceListener) {
            EngineResource engineResource = new EngineResource(resource, z, true, key, resourceListener);
            return engineResource;
        }
    }

    static final class ResourceCallbackAndExecutor {
        final ResourceCallback cb;
        final Executor executor;

        ResourceCallbackAndExecutor(ResourceCallback resourceCallback, Executor executor2) {
            this.cb = resourceCallback;
            this.executor = executor2;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ResourceCallbackAndExecutor)) {
                return false;
            }
            return this.cb.equals(((ResourceCallbackAndExecutor) obj).cb);
        }

        public int hashCode() {
            return this.cb.hashCode();
        }
    }

    static final class ResourceCallbacksAndExecutors implements Iterable<ResourceCallbackAndExecutor> {
        private final List<ResourceCallbackAndExecutor> callbacksAndExecutors;

        ResourceCallbacksAndExecutors() {
            this(new ArrayList(2));
        }

        ResourceCallbacksAndExecutors(List<ResourceCallbackAndExecutor> list) {
            this.callbacksAndExecutors = list;
        }

        /* access modifiers changed from: 0000 */
        public void add(ResourceCallback resourceCallback, Executor executor) {
            this.callbacksAndExecutors.add(new ResourceCallbackAndExecutor(resourceCallback, executor));
        }

        /* access modifiers changed from: 0000 */
        public void remove(ResourceCallback resourceCallback) {
            this.callbacksAndExecutors.remove(defaultCallbackAndExecutor(resourceCallback));
        }

        /* access modifiers changed from: 0000 */
        public boolean contains(ResourceCallback resourceCallback) {
            return this.callbacksAndExecutors.contains(defaultCallbackAndExecutor(resourceCallback));
        }

        /* access modifiers changed from: 0000 */
        public boolean isEmpty() {
            return this.callbacksAndExecutors.isEmpty();
        }

        /* access modifiers changed from: 0000 */
        public int size() {
            return this.callbacksAndExecutors.size();
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            this.callbacksAndExecutors.clear();
        }

        /* access modifiers changed from: 0000 */
        public ResourceCallbacksAndExecutors copy() {
            return new ResourceCallbacksAndExecutors(new ArrayList(this.callbacksAndExecutors));
        }

        private static ResourceCallbackAndExecutor defaultCallbackAndExecutor(ResourceCallback resourceCallback) {
            return new ResourceCallbackAndExecutor(resourceCallback, Executors.directExecutor());
        }

        public Iterator<ResourceCallbackAndExecutor> iterator() {
            return this.callbacksAndExecutors.iterator();
        }
    }

    EngineJob(GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, GlideExecutor glideExecutor4, EngineJobListener engineJobListener2, ResourceListener resourceListener2, Pool<EngineJob<?>> pool2) {
        this(glideExecutor, glideExecutor2, glideExecutor3, glideExecutor4, engineJobListener2, resourceListener2, pool2, DEFAULT_FACTORY);
    }

    EngineJob(GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, GlideExecutor glideExecutor4, EngineJobListener engineJobListener2, ResourceListener resourceListener2, Pool<EngineJob<?>> pool2, EngineResourceFactory engineResourceFactory2) {
        this.cbs = new ResourceCallbacksAndExecutors();
        this.stateVerifier = StateVerifier.newInstance();
        this.pendingCallbacks = new AtomicInteger();
        this.diskCacheExecutor = glideExecutor;
        this.sourceExecutor = glideExecutor2;
        this.sourceUnlimitedExecutor = glideExecutor3;
        this.animationExecutor = glideExecutor4;
        this.engineJobListener = engineJobListener2;
        this.resourceListener = resourceListener2;
        this.pool = pool2;
        this.engineResourceFactory = engineResourceFactory2;
    }

    /* access modifiers changed from: 0000 */
    public synchronized EngineJob<R> init(Key key2, boolean z, boolean z2, boolean z3, boolean z4) {
        this.key = key2;
        this.isCacheable = z;
        this.useUnlimitedSourceGeneratorPool = z2;
        this.useAnimationPool = z3;
        this.onlyRetrieveFromCache = z4;
        return this;
    }

    public synchronized void start(DecodeJob<R> decodeJob2) {
        this.decodeJob = decodeJob2;
        (decodeJob2.willDecodeFromCache() ? this.diskCacheExecutor : getActiveSourceExecutor()).execute(decodeJob2);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void addCallback(ResourceCallback resourceCallback, Executor executor) {
        this.stateVerifier.throwIfRecycled();
        this.cbs.add(resourceCallback, executor);
        boolean z = true;
        if (this.hasResource) {
            incrementPendingCallbacks(1);
            executor.execute(new CallResourceReady(resourceCallback));
        } else if (this.hasLoadFailed) {
            incrementPendingCallbacks(1);
            executor.execute(new CallLoadFailed(resourceCallback));
        } else {
            if (this.isCancelled) {
                z = false;
            }
            Preconditions.checkArgument(z, "Cannot add callbacks to a cancelled EngineJob");
        }
    }

    /* access modifiers changed from: 0000 */
    public void callCallbackOnResourceReady(ResourceCallback resourceCallback) {
        try {
            resourceCallback.onResourceReady(this.engineResource, this.dataSource);
        } catch (Throwable th) {
            throw new CallbackException(th);
        }
    }

    /* access modifiers changed from: 0000 */
    public void callCallbackOnLoadFailed(ResourceCallback resourceCallback) {
        try {
            resourceCallback.onLoadFailed(this.exception);
        } catch (Throwable th) {
            throw new CallbackException(th);
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void removeCallback(ResourceCallback resourceCallback) {
        boolean z;
        this.stateVerifier.throwIfRecycled();
        this.cbs.remove(resourceCallback);
        if (this.cbs.isEmpty()) {
            cancel();
            if (!this.hasResource) {
                if (!this.hasLoadFailed) {
                    z = false;
                    if (z && this.pendingCallbacks.get() == 0) {
                        release();
                    }
                }
            }
            z = true;
            release();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean onlyRetrieveFromCache() {
        return this.onlyRetrieveFromCache;
    }

    private GlideExecutor getActiveSourceExecutor() {
        if (this.useUnlimitedSourceGeneratorPool) {
            return this.sourceUnlimitedExecutor;
        }
        return this.useAnimationPool ? this.animationExecutor : this.sourceExecutor;
    }

    /* access modifiers changed from: 0000 */
    public void cancel() {
        if (!isDone()) {
            this.isCancelled = true;
            this.decodeJob.cancel();
            this.engineJobListener.onEngineJobCancelled(this, this.key);
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized boolean isCancelled() {
        return this.isCancelled;
    }

    private boolean isDone() {
        return this.hasLoadFailed || this.hasResource || this.isCancelled;
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0046, code lost:
        r5.engineJobListener.onEngineJobComplete(r5, r0, r2);
        r0 = r1.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0053, code lost:
        if (r0.hasNext() == false) goto L_0x0068;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0055, code lost:
        r1 = (com.bumptech.glide.load.engine.EngineJob.ResourceCallbackAndExecutor) r0.next();
        r1.executor.execute(new com.bumptech.glide.load.engine.EngineJob.CallResourceReady(r5, r1.cb));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0068, code lost:
        decrementPendingCallbacks();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x006b, code lost:
        return;
     */
    public void notifyCallbacksOfResult() {
        synchronized (this) {
            this.stateVerifier.throwIfRecycled();
            if (this.isCancelled) {
                this.resource.recycle();
                release();
            } else if (this.cbs.isEmpty()) {
                throw new IllegalStateException("Received a resource without any callbacks to notify");
            } else if (!this.hasResource) {
                this.engineResource = this.engineResourceFactory.build(this.resource, this.isCacheable, this.key, this.resourceListener);
                this.hasResource = true;
                ResourceCallbacksAndExecutors copy = this.cbs.copy();
                incrementPendingCallbacks(copy.size() + 1);
                Key key2 = this.key;
                EngineResource<?> engineResource2 = this.engineResource;
            } else {
                throw new IllegalStateException("Already have resource");
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void incrementPendingCallbacks(int i) {
        Preconditions.checkArgument(isDone(), "Not yet complete!");
        if (this.pendingCallbacks.getAndAdd(i) == 0 && this.engineResource != null) {
            this.engineResource.acquire();
        }
    }

    /* access modifiers changed from: 0000 */
    public void decrementPendingCallbacks() {
        EngineResource<?> engineResource2;
        synchronized (this) {
            this.stateVerifier.throwIfRecycled();
            Preconditions.checkArgument(isDone(), "Not yet complete!");
            int decrementAndGet = this.pendingCallbacks.decrementAndGet();
            Preconditions.checkArgument(decrementAndGet >= 0, "Can't decrement below 0");
            if (decrementAndGet == 0) {
                engineResource2 = this.engineResource;
                release();
            } else {
                engineResource2 = null;
            }
        }
        if (engineResource2 != null) {
            engineResource2.release();
        }
    }

    private synchronized void release() {
        if (this.key != null) {
            this.cbs.clear();
            this.key = null;
            this.engineResource = null;
            this.resource = null;
            this.hasLoadFailed = false;
            this.isCancelled = false;
            this.hasResource = false;
            this.decodeJob.release(false);
            this.decodeJob = null;
            this.exception = null;
            this.dataSource = null;
            this.pool.release(this);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void onResourceReady(Resource<R> resource2, DataSource dataSource2) {
        synchronized (this) {
            this.resource = resource2;
            this.dataSource = dataSource2;
        }
        notifyCallbacksOfResult();
    }

    public void onLoadFailed(GlideException glideException) {
        synchronized (this) {
            this.exception = glideException;
        }
        notifyCallbacksOfException();
    }

    public void reschedule(DecodeJob<?> decodeJob2) {
        getActiveSourceExecutor().execute(decodeJob2);
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002f, code lost:
        r4.engineJobListener.onEngineJobComplete(r4, r1, null);
        r0 = r2.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003d, code lost:
        if (r0.hasNext() == false) goto L_0x0052;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003f, code lost:
        r1 = (com.bumptech.glide.load.engine.EngineJob.ResourceCallbackAndExecutor) r0.next();
        r1.executor.execute(new com.bumptech.glide.load.engine.EngineJob.CallLoadFailed(r4, r1.cb));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0052, code lost:
        decrementPendingCallbacks();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0055, code lost:
        return;
     */
    public void notifyCallbacksOfException() {
        synchronized (this) {
            this.stateVerifier.throwIfRecycled();
            if (this.isCancelled) {
                release();
            } else if (this.cbs.isEmpty()) {
                throw new IllegalStateException("Received an exception without any callbacks to notify");
            } else if (!this.hasLoadFailed) {
                this.hasLoadFailed = true;
                Key key2 = this.key;
                ResourceCallbacksAndExecutors copy = this.cbs.copy();
                incrementPendingCallbacks(copy.size() + 1);
            } else {
                throw new IllegalStateException("Already failed once");
            }
        }
    }

    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }
}
