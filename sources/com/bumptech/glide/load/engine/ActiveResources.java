package com.bumptech.glide.load.engine;

import android.os.Process;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

final class ActiveResources {
    final Map<Key, ResourceWeakReference> activeEngineResources;
    private volatile DequeuedResourceCallback cb;
    private final boolean isActiveResourceRetentionAllowed;
    private volatile boolean isShutdown;
    private ResourceListener listener;
    private final Executor monitorClearedResourcesExecutor;
    private final ReferenceQueue<EngineResource<?>> resourceReferenceQueue;

    interface DequeuedResourceCallback {
        void onResourceDequeued();
    }

    static final class ResourceWeakReference extends WeakReference<EngineResource<?>> {
        final boolean isCacheable;
        final Key key;
        Resource<?> resource;

        ResourceWeakReference(Key key2, EngineResource<?> engineResource, ReferenceQueue<? super EngineResource<?>> referenceQueue, boolean z) {
            super(engineResource, referenceQueue);
            this.key = (Key) Preconditions.checkNotNull(key2);
            this.resource = (!engineResource.isMemoryCacheable() || !z) ? null : (Resource) Preconditions.checkNotNull(engineResource.getResource());
            this.isCacheable = engineResource.isMemoryCacheable();
        }

        /* access modifiers changed from: 0000 */
        public void reset() {
            this.resource = null;
            clear();
        }
    }

    ActiveResources(boolean z) {
        this(z, Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(final Runnable runnable) {
                return new Thread(new Runnable() {
                    public void run() {
                        Process.setThreadPriority(10);
                        runnable.run();
                    }
                }, "glide-active-resources");
            }
        }));
    }

    ActiveResources(boolean z, Executor executor) {
        this.activeEngineResources = new HashMap();
        this.resourceReferenceQueue = new ReferenceQueue<>();
        this.isActiveResourceRetentionAllowed = z;
        this.monitorClearedResourcesExecutor = executor;
        executor.execute(new Runnable() {
            public void run() {
                ActiveResources.this.cleanReferenceQueue();
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void setListener(ResourceListener resourceListener) {
        synchronized (resourceListener) {
            synchronized (this) {
                this.listener = resourceListener;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void activate(Key key, EngineResource<?> engineResource) {
        ResourceWeakReference resourceWeakReference = (ResourceWeakReference) this.activeEngineResources.put(key, new ResourceWeakReference(key, engineResource, this.resourceReferenceQueue, this.isActiveResourceRetentionAllowed));
        if (resourceWeakReference != null) {
            resourceWeakReference.reset();
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void deactivate(Key key) {
        ResourceWeakReference resourceWeakReference = (ResourceWeakReference) this.activeEngineResources.remove(key);
        if (resourceWeakReference != null) {
            resourceWeakReference.reset();
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001a, code lost:
        return r0;
     */
    public synchronized EngineResource<?> get(Key key) {
        ResourceWeakReference resourceWeakReference = (ResourceWeakReference) this.activeEngineResources.get(key);
        if (resourceWeakReference == null) {
            return null;
        }
        EngineResource<?> engineResource = (EngineResource) resourceWeakReference.get();
        if (engineResource == null) {
            cleanupActiveReference(resourceWeakReference);
        }
    }

    /* access modifiers changed from: 0000 */
    public void cleanupActiveReference(ResourceWeakReference resourceWeakReference) {
        synchronized (this) {
            this.activeEngineResources.remove(resourceWeakReference.key);
            if (resourceWeakReference.isCacheable) {
                if (resourceWeakReference.resource != null) {
                    EngineResource engineResource = new EngineResource(resourceWeakReference.resource, true, false, resourceWeakReference.key, this.listener);
                    this.listener.onResourceReleased(resourceWeakReference.key, engineResource);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void cleanReferenceQueue() {
        while (!this.isShutdown) {
            try {
                cleanupActiveReference((ResourceWeakReference) this.resourceReferenceQueue.remove());
                DequeuedResourceCallback dequeuedResourceCallback = this.cb;
                if (dequeuedResourceCallback != null) {
                    dequeuedResourceCallback.onResourceDequeued();
                }
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setDequeuedResourceCallback(DequeuedResourceCallback dequeuedResourceCallback) {
        this.cb = dequeuedResourceCallback;
    }

    /* access modifiers changed from: 0000 */
    public void shutdown() {
        this.isShutdown = true;
        Executor executor = this.monitorClearedResourcesExecutor;
        if (executor instanceof ExecutorService) {
            com.bumptech.glide.util.Executors.shutdownAndAwaitTermination((ExecutorService) executor);
        }
    }
}
