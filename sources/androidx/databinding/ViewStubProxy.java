package androidx.databinding;

import android.view.View;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;

public class ViewStubProxy {
    /* access modifiers changed from: private */
    public ViewDataBinding mContainingBinding;
    /* access modifiers changed from: private */
    public OnInflateListener mOnInflateListener;
    private OnInflateListener mProxyListener;
    /* access modifiers changed from: private */
    public View mRoot;
    /* access modifiers changed from: private */
    public ViewDataBinding mViewDataBinding;
    /* access modifiers changed from: private */
    public ViewStub mViewStub;

    public ViewStubProxy(ViewStub viewStub) {
        AnonymousClass1 r0 = new OnInflateListener() {
            public void onInflate(ViewStub viewStub, View view) {
                ViewStubProxy.this.mRoot = view;
                ViewStubProxy viewStubProxy = ViewStubProxy.this;
                viewStubProxy.mViewDataBinding = DataBindingUtil.bind(viewStubProxy.mContainingBinding.mBindingComponent, view, viewStub.getLayoutResource());
                ViewStubProxy.this.mViewStub = null;
                if (ViewStubProxy.this.mOnInflateListener != null) {
                    ViewStubProxy.this.mOnInflateListener.onInflate(viewStub, view);
                    ViewStubProxy.this.mOnInflateListener = null;
                }
                ViewStubProxy.this.mContainingBinding.invalidateAll();
                ViewStubProxy.this.mContainingBinding.forceExecuteBindings();
            }
        };
        this.mProxyListener = r0;
        this.mViewStub = viewStub;
        viewStub.setOnInflateListener(r0);
    }

    public void setContainingBinding(ViewDataBinding viewDataBinding) {
        this.mContainingBinding = viewDataBinding;
    }

    public boolean isInflated() {
        return this.mRoot != null;
    }

    public View getRoot() {
        return this.mRoot;
    }

    public ViewDataBinding getBinding() {
        return this.mViewDataBinding;
    }

    public ViewStub getViewStub() {
        return this.mViewStub;
    }

    public void setOnInflateListener(OnInflateListener onInflateListener) {
        if (this.mViewStub != null) {
            this.mOnInflateListener = onInflateListener;
        }
    }
}
