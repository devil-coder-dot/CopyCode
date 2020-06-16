package com.bumptech.glide.request;

import com.bumptech.glide.request.RequestCoordinator.RequestState;

public final class ErrorRequestCoordinator implements RequestCoordinator, Request {
    private volatile Request error;
    private RequestState errorState = RequestState.CLEARED;
    private final RequestCoordinator parent;
    private volatile Request primary;
    private RequestState primaryState = RequestState.CLEARED;
    private final Object requestLock;

    public ErrorRequestCoordinator(Object obj, RequestCoordinator requestCoordinator) {
        this.requestLock = obj;
        this.parent = requestCoordinator;
    }

    public void setRequests(Request request, Request request2) {
        this.primary = request;
        this.error = request2;
    }

    public void begin() {
        synchronized (this.requestLock) {
            if (this.primaryState != RequestState.RUNNING) {
                this.primaryState = RequestState.RUNNING;
                this.primary.begin();
            }
        }
    }

    public void clear() {
        synchronized (this.requestLock) {
            this.primaryState = RequestState.CLEARED;
            this.primary.clear();
            if (this.errorState != RequestState.CLEARED) {
                this.errorState = RequestState.CLEARED;
                this.error.clear();
            }
        }
    }

    public void pause() {
        synchronized (this.requestLock) {
            if (this.primaryState == RequestState.RUNNING) {
                this.primaryState = RequestState.PAUSED;
                this.primary.pause();
            }
            if (this.errorState == RequestState.RUNNING) {
                this.errorState = RequestState.PAUSED;
                this.error.pause();
            }
        }
    }

    public boolean isRunning() {
        boolean z;
        synchronized (this.requestLock) {
            if (this.primaryState != RequestState.RUNNING) {
                if (this.errorState != RequestState.RUNNING) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    public boolean isComplete() {
        boolean z;
        synchronized (this.requestLock) {
            if (this.primaryState != RequestState.SUCCESS) {
                if (this.errorState != RequestState.SUCCESS) {
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
            z = this.primaryState == RequestState.CLEARED && this.errorState == RequestState.CLEARED;
        }
        return z;
    }

    public boolean isEquivalentTo(Request request) {
        if (!(request instanceof ErrorRequestCoordinator)) {
            return false;
        }
        ErrorRequestCoordinator errorRequestCoordinator = (ErrorRequestCoordinator) request;
        if (!this.primary.isEquivalentTo(errorRequestCoordinator.primary) || !this.error.isEquivalentTo(errorRequestCoordinator.error)) {
            return false;
        }
        return true;
    }

    public boolean canSetImage(Request request) {
        boolean z;
        synchronized (this.requestLock) {
            z = parentCanSetImage() && isValidRequest(request);
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
            z = parentCanNotifyStatusChanged() && isValidRequest(request);
        }
        return z;
    }

    public boolean canNotifyCleared(Request request) {
        boolean z;
        synchronized (this.requestLock) {
            z = parentCanNotifyCleared() && isValidRequest(request);
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

    private boolean isValidRequest(Request request) {
        return request.equals(this.primary) || (this.primaryState == RequestState.FAILED && request.equals(this.error));
    }

    public boolean isAnyResourceSet() {
        boolean z;
        synchronized (this.requestLock) {
            if (!parentIsAnyResourceSet()) {
                if (!isComplete()) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    private boolean parentIsAnyResourceSet() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator != null && requestCoordinator.isAnyResourceSet();
    }

    public void onRequestSuccess(Request request) {
        synchronized (this.requestLock) {
            if (request.equals(this.primary)) {
                this.primaryState = RequestState.SUCCESS;
            } else if (request.equals(this.error)) {
                this.errorState = RequestState.SUCCESS;
            }
            if (this.parent != null) {
                this.parent.onRequestSuccess(this);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002e, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001f, code lost:
        return;
     */
    public void onRequestFailed(Request request) {
        synchronized (this.requestLock) {
            if (!request.equals(this.error)) {
                this.primaryState = RequestState.FAILED;
                if (this.errorState != RequestState.RUNNING) {
                    this.errorState = RequestState.RUNNING;
                    this.error.begin();
                }
            } else {
                this.errorState = RequestState.FAILED;
                if (this.parent != null) {
                    this.parent.onRequestFailed(this);
                }
            }
        }
    }
}
