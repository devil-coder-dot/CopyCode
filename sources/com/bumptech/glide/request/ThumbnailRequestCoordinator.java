package com.bumptech.glide.request;

import com.bumptech.glide.request.RequestCoordinator.RequestState;

public class ThumbnailRequestCoordinator implements RequestCoordinator, Request {
    private volatile Request full;
    private RequestState fullState = RequestState.CLEARED;
    private boolean isRunningDuringBegin;
    private final RequestCoordinator parent;
    private final Object requestLock;
    private volatile Request thumb;
    private RequestState thumbState = RequestState.CLEARED;

    public ThumbnailRequestCoordinator(Object obj, RequestCoordinator requestCoordinator) {
        this.requestLock = obj;
        this.parent = requestCoordinator;
    }

    public void setRequests(Request request, Request request2) {
        this.full = request;
        this.thumb = request2;
    }

    public boolean canSetImage(Request request) {
        boolean z;
        synchronized (this.requestLock) {
            z = parentCanSetImage() && (request.equals(this.full) || this.fullState != RequestState.SUCCESS);
        }
        return z;
    }

    private boolean parentCanSetImage() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator == null || requestCoordinator.canSetImage(this);
    }

    public boolean canNotifyStatusChanged(Request request) {
        boolean z;
        synchronized (this.requestLock) {
            z = parentCanNotifyStatusChanged() && request.equals(this.full) && !isResourceSet();
        }
        return z;
    }

    public boolean canNotifyCleared(Request request) {
        boolean z;
        synchronized (this.requestLock) {
            z = parentCanNotifyCleared() && request.equals(this.full) && this.fullState != RequestState.PAUSED;
        }
        return z;
    }

    private boolean parentCanNotifyCleared() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator == null || requestCoordinator.canNotifyCleared(this);
    }

    private boolean parentCanNotifyStatusChanged() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator == null || requestCoordinator.canNotifyStatusChanged(this);
    }

    public boolean isAnyResourceSet() {
        boolean z;
        synchronized (this.requestLock) {
            if (!parentIsAnyResourceSet()) {
                if (!isResourceSet()) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002c, code lost:
        return;
     */
    public void onRequestSuccess(Request request) {
        synchronized (this.requestLock) {
            if (request.equals(this.thumb)) {
                this.thumbState = RequestState.SUCCESS;
                return;
            }
            this.fullState = RequestState.SUCCESS;
            if (this.parent != null) {
                this.parent.onRequestSuccess(this);
            }
            if (!this.thumbState.isComplete()) {
                this.thumb.clear();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001f, code lost:
        return;
     */
    public void onRequestFailed(Request request) {
        synchronized (this.requestLock) {
            if (!request.equals(this.full)) {
                this.thumbState = RequestState.FAILED;
                return;
            }
            this.fullState = RequestState.FAILED;
            if (this.parent != null) {
                this.parent.onRequestFailed(this);
            }
        }
    }

    private boolean parentIsAnyResourceSet() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator != null && requestCoordinator.isAnyResourceSet();
    }

    public void begin() {
        synchronized (this.requestLock) {
            this.isRunningDuringBegin = true;
            try {
                if (!(this.fullState == RequestState.SUCCESS || this.thumbState == RequestState.RUNNING)) {
                    this.thumbState = RequestState.RUNNING;
                    this.thumb.begin();
                }
                if (this.isRunningDuringBegin && this.fullState != RequestState.RUNNING) {
                    this.fullState = RequestState.RUNNING;
                    this.full.begin();
                }
            } finally {
                this.isRunningDuringBegin = false;
            }
        }
    }

    public void clear() {
        synchronized (this.requestLock) {
            this.isRunningDuringBegin = false;
            this.fullState = RequestState.CLEARED;
            this.thumbState = RequestState.CLEARED;
            this.thumb.clear();
            this.full.clear();
        }
    }

    public void pause() {
        synchronized (this.requestLock) {
            if (!this.thumbState.isComplete()) {
                this.thumbState = RequestState.PAUSED;
                this.thumb.pause();
            }
            if (!this.fullState.isComplete()) {
                this.fullState = RequestState.PAUSED;
                this.full.pause();
            }
        }
    }

    public boolean isRunning() {
        boolean z;
        synchronized (this.requestLock) {
            z = this.fullState == RequestState.RUNNING;
        }
        return z;
    }

    public boolean isComplete() {
        boolean z;
        synchronized (this.requestLock) {
            z = this.fullState == RequestState.SUCCESS;
        }
        return z;
    }

    private boolean isResourceSet() {
        boolean z;
        synchronized (this.requestLock) {
            if (this.fullState != RequestState.SUCCESS) {
                if (this.thumbState != RequestState.SUCCESS) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    public boolean isCleared() {
        boolean z;
        synchronized (this.requestLock) {
            z = this.fullState == RequestState.CLEARED;
        }
        return z;
    }

    public boolean isEquivalentTo(Request request) {
        if (!(request instanceof ThumbnailRequestCoordinator)) {
            return false;
        }
        ThumbnailRequestCoordinator thumbnailRequestCoordinator = (ThumbnailRequestCoordinator) request;
        if (this.full == null) {
            if (thumbnailRequestCoordinator.full != null) {
                return false;
            }
        } else if (!this.full.isEquivalentTo(thumbnailRequestCoordinator.full)) {
            return false;
        }
        if (this.thumb == null) {
            if (thumbnailRequestCoordinator.thumb != null) {
                return false;
            }
        } else if (!this.thumb.isEquivalentTo(thumbnailRequestCoordinator.thumb)) {
            return false;
        }
        return true;
    }
}
