package androidx.databinding;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import androidx.databinding.CallbackRegistry.NotifierCallback;
import androidx.databinding.Observable.OnPropertyChangedCallback;
import androidx.databinding.ObservableList.OnListChangedCallback;
import androidx.databinding.ObservableMap.OnMapChangedCallback;
import androidx.databinding.library.R;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.Lifecycle.State;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.viewbinding.ViewBinding;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public abstract class ViewDataBinding extends BaseObservable implements ViewBinding {
    private static final int BINDING_NUMBER_START = 8;
    public static final String BINDING_TAG_PREFIX = "binding_";
    private static final CreateWeakListener CREATE_LIST_LISTENER = new CreateWeakListener() {
        public WeakListener create(ViewDataBinding viewDataBinding, int i) {
            return new WeakListListener(viewDataBinding, i).getListener();
        }
    };
    private static final CreateWeakListener CREATE_LIVE_DATA_LISTENER = new CreateWeakListener() {
        public WeakListener create(ViewDataBinding viewDataBinding, int i) {
            return new LiveDataListener(viewDataBinding, i).getListener();
        }
    };
    private static final CreateWeakListener CREATE_MAP_LISTENER = new CreateWeakListener() {
        public WeakListener create(ViewDataBinding viewDataBinding, int i) {
            return new WeakMapListener(viewDataBinding, i).getListener();
        }
    };
    private static final CreateWeakListener CREATE_PROPERTY_LISTENER = new CreateWeakListener() {
        public WeakListener create(ViewDataBinding viewDataBinding, int i) {
            return new WeakPropertyListener(viewDataBinding, i).getListener();
        }
    };
    private static final int HALTED = 2;
    private static final int REBIND = 1;
    private static final NotifierCallback<OnRebindCallback, ViewDataBinding, Void> REBIND_NOTIFIER = new NotifierCallback<OnRebindCallback, ViewDataBinding, Void>() {
        public void onNotifyCallback(OnRebindCallback onRebindCallback, ViewDataBinding viewDataBinding, int i, Void voidR) {
            if (i != 1) {
                if (i == 2) {
                    onRebindCallback.onCanceled(viewDataBinding);
                } else if (i == 3) {
                    onRebindCallback.onBound(viewDataBinding);
                }
            } else if (!onRebindCallback.onPreBind(viewDataBinding)) {
                viewDataBinding.mRebindHalted = true;
            }
        }
    };
    private static final int REBOUND = 3;
    /* access modifiers changed from: private */
    public static final OnAttachStateChangeListener ROOT_REATTACHED_LISTENER;
    static int SDK_INT;
    private static final boolean USE_CHOREOGRAPHER;
    /* access modifiers changed from: private */
    public static final ReferenceQueue<ViewDataBinding> sReferenceQueue = new ReferenceQueue<>();
    protected final DataBindingComponent mBindingComponent;
    private Choreographer mChoreographer;
    private ViewDataBinding mContainingBinding;
    private final FrameCallback mFrameCallback;
    private boolean mInLiveDataRegisterObserver;
    private boolean mIsExecutingPendingBindings;
    private LifecycleOwner mLifecycleOwner;
    private WeakListener[] mLocalFieldObservers;
    private OnStartListener mOnStartListener;
    /* access modifiers changed from: private */
    public boolean mPendingRebind;
    private CallbackRegistry<OnRebindCallback, ViewDataBinding, Void> mRebindCallbacks;
    /* access modifiers changed from: private */
    public boolean mRebindHalted;
    /* access modifiers changed from: private */
    public final Runnable mRebindRunnable;
    /* access modifiers changed from: private */
    public final View mRoot;
    private Handler mUIThreadHandler;

    private interface CreateWeakListener {
        WeakListener create(ViewDataBinding viewDataBinding, int i);
    }

    protected static class IncludedLayouts {
        public final int[][] indexes;
        public final int[][] layoutIds;
        public final String[][] layouts;

        public IncludedLayouts(int i) {
            this.layouts = new String[i][];
            this.indexes = new int[i][];
            this.layoutIds = new int[i][];
        }

        public void setIncludes(int i, String[] strArr, int[] iArr, int[] iArr2) {
            this.layouts[i] = strArr;
            this.indexes[i] = iArr;
            this.layoutIds[i] = iArr2;
        }
    }

    private static class LiveDataListener implements Observer, ObservableReference<LiveData<?>> {
        LifecycleOwner mLifecycleOwner;
        final WeakListener<LiveData<?>> mListener;

        public LiveDataListener(ViewDataBinding viewDataBinding, int i) {
            this.mListener = new WeakListener<>(viewDataBinding, i, this);
        }

        public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
            LiveData liveData = (LiveData) this.mListener.getTarget();
            if (liveData != null) {
                if (this.mLifecycleOwner != null) {
                    liveData.removeObserver(this);
                }
                if (lifecycleOwner != null) {
                    liveData.observe(lifecycleOwner, this);
                }
            }
            this.mLifecycleOwner = lifecycleOwner;
        }

        public WeakListener<LiveData<?>> getListener() {
            return this.mListener;
        }

        public void addListener(LiveData<?> liveData) {
            LifecycleOwner lifecycleOwner = this.mLifecycleOwner;
            if (lifecycleOwner != null) {
                liveData.observe(lifecycleOwner, this);
            }
        }

        public void removeListener(LiveData<?> liveData) {
            liveData.removeObserver(this);
        }

        public void onChanged(Object obj) {
            ViewDataBinding binder = this.mListener.getBinder();
            if (binder != null) {
                binder.handleFieldChange(this.mListener.mLocalFieldId, this.mListener.getTarget(), 0);
            }
        }
    }

    private interface ObservableReference<T> {
        void addListener(T t);

        WeakListener<T> getListener();

        void removeListener(T t);

        void setLifecycleOwner(LifecycleOwner lifecycleOwner);
    }

    static class OnStartListener implements LifecycleObserver {
        final WeakReference<ViewDataBinding> mBinding;

        private OnStartListener(ViewDataBinding viewDataBinding) {
            this.mBinding = new WeakReference<>(viewDataBinding);
        }

        @OnLifecycleEvent(Event.ON_START)
        public void onStart() {
            ViewDataBinding viewDataBinding = (ViewDataBinding) this.mBinding.get();
            if (viewDataBinding != null) {
                viewDataBinding.executePendingBindings();
            }
        }
    }

    protected static abstract class PropertyChangedInverseListener extends OnPropertyChangedCallback implements InverseBindingListener {
        final int mPropertyId;

        public PropertyChangedInverseListener(int i) {
            this.mPropertyId = i;
        }

        public void onPropertyChanged(Observable observable, int i) {
            if (i == this.mPropertyId || i == 0) {
                onChange();
            }
        }
    }

    private static class WeakListListener extends OnListChangedCallback implements ObservableReference<ObservableList> {
        final WeakListener<ObservableList> mListener;

        public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        }

        public WeakListListener(ViewDataBinding viewDataBinding, int i) {
            this.mListener = new WeakListener<>(viewDataBinding, i, this);
        }

        public WeakListener<ObservableList> getListener() {
            return this.mListener;
        }

        public void addListener(ObservableList observableList) {
            observableList.addOnListChangedCallback(this);
        }

        public void removeListener(ObservableList observableList) {
            observableList.removeOnListChangedCallback(this);
        }

        public void onChanged(ObservableList observableList) {
            ViewDataBinding binder = this.mListener.getBinder();
            if (binder != null) {
                ObservableList observableList2 = (ObservableList) this.mListener.getTarget();
                if (observableList2 == observableList) {
                    binder.handleFieldChange(this.mListener.mLocalFieldId, observableList2, 0);
                }
            }
        }

        public void onItemRangeChanged(ObservableList observableList, int i, int i2) {
            onChanged(observableList);
        }

        public void onItemRangeInserted(ObservableList observableList, int i, int i2) {
            onChanged(observableList);
        }

        public void onItemRangeMoved(ObservableList observableList, int i, int i2, int i3) {
            onChanged(observableList);
        }

        public void onItemRangeRemoved(ObservableList observableList, int i, int i2) {
            onChanged(observableList);
        }
    }

    private static class WeakListener<T> extends WeakReference<ViewDataBinding> {
        protected final int mLocalFieldId;
        private final ObservableReference<T> mObservable;
        private T mTarget;

        public WeakListener(ViewDataBinding viewDataBinding, int i, ObservableReference<T> observableReference) {
            super(viewDataBinding, ViewDataBinding.sReferenceQueue);
            this.mLocalFieldId = i;
            this.mObservable = observableReference;
        }

        public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
            this.mObservable.setLifecycleOwner(lifecycleOwner);
        }

        public void setTarget(T t) {
            unregister();
            this.mTarget = t;
            if (t != null) {
                this.mObservable.addListener(t);
            }
        }

        public boolean unregister() {
            boolean z;
            T t = this.mTarget;
            if (t != null) {
                this.mObservable.removeListener(t);
                z = true;
            } else {
                z = false;
            }
            this.mTarget = null;
            return z;
        }

        public T getTarget() {
            return this.mTarget;
        }

        /* access modifiers changed from: protected */
        public ViewDataBinding getBinder() {
            ViewDataBinding viewDataBinding = (ViewDataBinding) get();
            if (viewDataBinding == null) {
                unregister();
            }
            return viewDataBinding;
        }
    }

    private static class WeakMapListener extends OnMapChangedCallback implements ObservableReference<ObservableMap> {
        final WeakListener<ObservableMap> mListener;

        public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        }

        public WeakMapListener(ViewDataBinding viewDataBinding, int i) {
            this.mListener = new WeakListener<>(viewDataBinding, i, this);
        }

        public WeakListener<ObservableMap> getListener() {
            return this.mListener;
        }

        public void addListener(ObservableMap observableMap) {
            observableMap.addOnMapChangedCallback(this);
        }

        public void removeListener(ObservableMap observableMap) {
            observableMap.removeOnMapChangedCallback(this);
        }

        public void onMapChanged(ObservableMap observableMap, Object obj) {
            ViewDataBinding binder = this.mListener.getBinder();
            if (binder != null && observableMap == this.mListener.getTarget()) {
                binder.handleFieldChange(this.mListener.mLocalFieldId, observableMap, 0);
            }
        }
    }

    private static class WeakPropertyListener extends OnPropertyChangedCallback implements ObservableReference<Observable> {
        final WeakListener<Observable> mListener;

        public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        }

        public WeakPropertyListener(ViewDataBinding viewDataBinding, int i) {
            this.mListener = new WeakListener<>(viewDataBinding, i, this);
        }

        public WeakListener<Observable> getListener() {
            return this.mListener;
        }

        public void addListener(Observable observable) {
            observable.addOnPropertyChangedCallback(this);
        }

        public void removeListener(Observable observable) {
            observable.removeOnPropertyChangedCallback(this);
        }

        public void onPropertyChanged(Observable observable, int i) {
            ViewDataBinding binder = this.mListener.getBinder();
            if (binder != null && ((Observable) this.mListener.getTarget()) == observable) {
                binder.handleFieldChange(this.mListener.mLocalFieldId, observable, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public abstract void executeBindings();

    public abstract boolean hasPendingBindings();

    public abstract void invalidateAll();

    /* access modifiers changed from: protected */
    public abstract boolean onFieldChange(int i, Object obj, int i2);

    public abstract boolean setVariable(int i, Object obj);

    static {
        int i = VERSION.SDK_INT;
        SDK_INT = i;
        USE_CHOREOGRAPHER = i >= 16;
        if (VERSION.SDK_INT < 19) {
            ROOT_REATTACHED_LISTENER = null;
        } else {
            ROOT_REATTACHED_LISTENER = new OnAttachStateChangeListener() {
                public void onViewDetachedFromWindow(View view) {
                }

                public void onViewAttachedToWindow(View view) {
                    ViewDataBinding.getBinding(view).mRebindRunnable.run();
                    view.removeOnAttachStateChangeListener(this);
                }
            };
        }
    }

    protected ViewDataBinding(DataBindingComponent dataBindingComponent, View view, int i) {
        this.mRebindRunnable = new Runnable() {
            public void run() {
                synchronized (this) {
                    ViewDataBinding.this.mPendingRebind = false;
                }
                ViewDataBinding.processReferenceQueue();
                if (VERSION.SDK_INT < 19 || ViewDataBinding.this.mRoot.isAttachedToWindow()) {
                    ViewDataBinding.this.executePendingBindings();
                    return;
                }
                ViewDataBinding.this.mRoot.removeOnAttachStateChangeListener(ViewDataBinding.ROOT_REATTACHED_LISTENER);
                ViewDataBinding.this.mRoot.addOnAttachStateChangeListener(ViewDataBinding.ROOT_REATTACHED_LISTENER);
            }
        };
        this.mPendingRebind = false;
        this.mRebindHalted = false;
        this.mBindingComponent = dataBindingComponent;
        this.mLocalFieldObservers = new WeakListener[i];
        this.mRoot = view;
        if (Looper.myLooper() == null) {
            throw new IllegalStateException("DataBinding must be created in view's UI Thread");
        } else if (USE_CHOREOGRAPHER) {
            this.mChoreographer = Choreographer.getInstance();
            this.mFrameCallback = new FrameCallback() {
                public void doFrame(long j) {
                    ViewDataBinding.this.mRebindRunnable.run();
                }
            };
        } else {
            this.mFrameCallback = null;
            this.mUIThreadHandler = new Handler(Looper.myLooper());
        }
    }

    protected ViewDataBinding(Object obj, View view, int i) {
        this(checkAndCastToBindingComponent(obj), view, i);
    }

    private static DataBindingComponent checkAndCastToBindingComponent(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof DataBindingComponent) {
            return (DataBindingComponent) obj;
        }
        throw new IllegalArgumentException("The provided bindingComponent parameter must be an instance of DataBindingComponent. See  https://issuetracker.google.com/issues/116541301 for details of why this parameter is not defined as DataBindingComponent");
    }

    /* access modifiers changed from: protected */
    public void setRootTag(View view) {
        view.setTag(R.id.dataBinding, this);
    }

    /* access modifiers changed from: protected */
    public void setRootTag(View[] viewArr) {
        for (View tag : viewArr) {
            tag.setTag(R.id.dataBinding, this);
        }
    }

    public static int getBuildSdkInt() {
        return SDK_INT;
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        WeakListener[] weakListenerArr;
        LifecycleOwner lifecycleOwner2 = this.mLifecycleOwner;
        if (lifecycleOwner2 != lifecycleOwner) {
            if (lifecycleOwner2 != null) {
                lifecycleOwner2.getLifecycle().removeObserver(this.mOnStartListener);
            }
            this.mLifecycleOwner = lifecycleOwner;
            if (lifecycleOwner != null) {
                if (this.mOnStartListener == null) {
                    this.mOnStartListener = new OnStartListener();
                }
                lifecycleOwner.getLifecycle().addObserver(this.mOnStartListener);
            }
            for (WeakListener weakListener : this.mLocalFieldObservers) {
                if (weakListener != null) {
                    weakListener.setLifecycleOwner(lifecycleOwner);
                }
            }
        }
    }

    public LifecycleOwner getLifecycleOwner() {
        return this.mLifecycleOwner;
    }

    public void addOnRebindCallback(OnRebindCallback onRebindCallback) {
        if (this.mRebindCallbacks == null) {
            this.mRebindCallbacks = new CallbackRegistry<>(REBIND_NOTIFIER);
        }
        this.mRebindCallbacks.add(onRebindCallback);
    }

    public void removeOnRebindCallback(OnRebindCallback onRebindCallback) {
        CallbackRegistry<OnRebindCallback, ViewDataBinding, Void> callbackRegistry = this.mRebindCallbacks;
        if (callbackRegistry != null) {
            callbackRegistry.remove(onRebindCallback);
        }
    }

    public void executePendingBindings() {
        ViewDataBinding viewDataBinding = this.mContainingBinding;
        if (viewDataBinding == null) {
            executeBindingsInternal();
        } else {
            viewDataBinding.executePendingBindings();
        }
    }

    private void executeBindingsInternal() {
        if (this.mIsExecutingPendingBindings) {
            requestRebind();
        } else if (hasPendingBindings()) {
            this.mIsExecutingPendingBindings = true;
            this.mRebindHalted = false;
            CallbackRegistry<OnRebindCallback, ViewDataBinding, Void> callbackRegistry = this.mRebindCallbacks;
            if (callbackRegistry != null) {
                callbackRegistry.notifyCallbacks(this, 1, null);
                if (this.mRebindHalted) {
                    this.mRebindCallbacks.notifyCallbacks(this, 2, null);
                }
            }
            if (!this.mRebindHalted) {
                executeBindings();
                CallbackRegistry<OnRebindCallback, ViewDataBinding, Void> callbackRegistry2 = this.mRebindCallbacks;
                if (callbackRegistry2 != null) {
                    callbackRegistry2.notifyCallbacks(this, 3, null);
                }
            }
            this.mIsExecutingPendingBindings = false;
        }
    }

    protected static void executeBindingsOn(ViewDataBinding viewDataBinding) {
        viewDataBinding.executeBindingsInternal();
    }

    /* access modifiers changed from: 0000 */
    public void forceExecuteBindings() {
        executeBindings();
    }

    public void unbind() {
        WeakListener[] weakListenerArr;
        for (WeakListener weakListener : this.mLocalFieldObservers) {
            if (weakListener != null) {
                weakListener.unregister();
            }
        }
    }

    static ViewDataBinding getBinding(View view) {
        if (view != null) {
            return (ViewDataBinding) view.getTag(R.id.dataBinding);
        }
        return null;
    }

    public View getRoot() {
        return this.mRoot;
    }

    /* access modifiers changed from: private */
    public void handleFieldChange(int i, Object obj, int i2) {
        if (!this.mInLiveDataRegisterObserver && onFieldChange(i, obj, i2)) {
            requestRebind();
        }
    }

    /* access modifiers changed from: protected */
    public boolean unregisterFrom(int i) {
        WeakListener weakListener = this.mLocalFieldObservers[i];
        if (weakListener != null) {
            return weakListener.unregister();
        }
        return false;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002a, code lost:
        if (USE_CHOREOGRAPHER == false) goto L_0x0034;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x002c, code lost:
        r2.mChoreographer.postFrameCallback(r2.mFrameCallback);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0034, code lost:
        r2.mUIThreadHandler.post(r2.mRebindRunnable);
     */
    public void requestRebind() {
        ViewDataBinding viewDataBinding = this.mContainingBinding;
        if (viewDataBinding != null) {
            viewDataBinding.requestRebind();
        } else {
            LifecycleOwner lifecycleOwner = this.mLifecycleOwner;
            if (lifecycleOwner == null || lifecycleOwner.getLifecycle().getCurrentState().isAtLeast(State.STARTED)) {
                synchronized (this) {
                    if (!this.mPendingRebind) {
                        this.mPendingRebind = true;
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public Object getObservedField(int i) {
        WeakListener weakListener = this.mLocalFieldObservers[i];
        if (weakListener == null) {
            return null;
        }
        return weakListener.getTarget();
    }

    private boolean updateRegistration(int i, Object obj, CreateWeakListener createWeakListener) {
        if (obj == null) {
            return unregisterFrom(i);
        }
        WeakListener weakListener = this.mLocalFieldObservers[i];
        if (weakListener == null) {
            registerTo(i, obj, createWeakListener);
            return true;
        } else if (weakListener.getTarget() == obj) {
            return false;
        } else {
            unregisterFrom(i);
            registerTo(i, obj, createWeakListener);
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public boolean updateRegistration(int i, Observable observable) {
        return updateRegistration(i, observable, CREATE_PROPERTY_LISTENER);
    }

    /* access modifiers changed from: protected */
    public boolean updateRegistration(int i, ObservableList observableList) {
        return updateRegistration(i, observableList, CREATE_LIST_LISTENER);
    }

    /* access modifiers changed from: protected */
    public boolean updateRegistration(int i, ObservableMap observableMap) {
        return updateRegistration(i, observableMap, CREATE_MAP_LISTENER);
    }

    /* access modifiers changed from: protected */
    public boolean updateLiveDataRegistration(int i, LiveData<?> liveData) {
        this.mInLiveDataRegisterObserver = true;
        try {
            return updateRegistration(i, liveData, CREATE_LIVE_DATA_LISTENER);
        } finally {
            this.mInLiveDataRegisterObserver = false;
        }
    }

    /* access modifiers changed from: protected */
    public void ensureBindingComponentIsNotNull(Class<?> cls) {
        if (this.mBindingComponent == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Required DataBindingComponent is null in class ");
            sb.append(getClass().getSimpleName());
            sb.append(". A BindingAdapter in ");
            sb.append(cls.getCanonicalName());
            sb.append(" is not static and requires an object to use, retrieved from the DataBindingComponent. If you don't use an inflation method taking a DataBindingComponent, use DataBindingUtil.setDefaultComponent or make all BindingAdapter methods static.");
            throw new IllegalStateException(sb.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void registerTo(int i, Object obj, CreateWeakListener createWeakListener) {
        if (obj != null) {
            WeakListener weakListener = this.mLocalFieldObservers[i];
            if (weakListener == null) {
                weakListener = createWeakListener.create(this, i);
                this.mLocalFieldObservers[i] = weakListener;
                LifecycleOwner lifecycleOwner = this.mLifecycleOwner;
                if (lifecycleOwner != null) {
                    weakListener.setLifecycleOwner(lifecycleOwner);
                }
            }
            weakListener.setTarget(obj);
        }
    }

    protected static ViewDataBinding bind(Object obj, View view, int i) {
        return DataBindingUtil.bind(checkAndCastToBindingComponent(obj), view, i);
    }

    protected static Object[] mapBindings(DataBindingComponent dataBindingComponent, View view, int i, IncludedLayouts includedLayouts, SparseIntArray sparseIntArray) {
        Object[] objArr = new Object[i];
        mapBindings(dataBindingComponent, view, objArr, includedLayouts, sparseIntArray, true);
        return objArr;
    }

    protected static boolean parse(String str, boolean z) {
        return str == null ? z : Boolean.parseBoolean(str);
    }

    protected static byte parse(String str, byte b) {
        try {
            return Byte.parseByte(str);
        } catch (NumberFormatException unused) {
            return b;
        }
    }

    protected static short parse(String str, short s) {
        try {
            return Short.parseShort(str);
        } catch (NumberFormatException unused) {
            return s;
        }
    }

    protected static int parse(String str, int i) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    protected static long parse(String str, long j) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException unused) {
            return j;
        }
    }

    protected static float parse(String str, float f) {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException unused) {
            return f;
        }
    }

    protected static double parse(String str, double d) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException unused) {
            return d;
        }
    }

    protected static char parse(String str, char c) {
        return (str == null || str.isEmpty()) ? c : str.charAt(0);
    }

    protected static int getColorFromResource(View view, int i) {
        if (VERSION.SDK_INT >= 23) {
            return view.getContext().getColor(i);
        }
        return view.getResources().getColor(i);
    }

    protected static ColorStateList getColorStateListFromResource(View view, int i) {
        if (VERSION.SDK_INT >= 23) {
            return view.getContext().getColorStateList(i);
        }
        return view.getResources().getColorStateList(i);
    }

    protected static Drawable getDrawableFromResource(View view, int i) {
        if (VERSION.SDK_INT >= 21) {
            return view.getContext().getDrawable(i);
        }
        return view.getResources().getDrawable(i);
    }

    protected static <T> T getFromArray(T[] tArr, int i) {
        if (tArr == null || i < 0 || i >= tArr.length) {
            return null;
        }
        return tArr[i];
    }

    protected static <T> void setTo(T[] tArr, int i, T t) {
        if (tArr != null && i >= 0 && i < tArr.length) {
            tArr[i] = t;
        }
    }

    protected static boolean getFromArray(boolean[] zArr, int i) {
        if (zArr == null || i < 0 || i >= zArr.length) {
            return false;
        }
        return zArr[i];
    }

    protected static void setTo(boolean[] zArr, int i, boolean z) {
        if (zArr != null && i >= 0 && i < zArr.length) {
            zArr[i] = z;
        }
    }

    protected static byte getFromArray(byte[] bArr, int i) {
        if (bArr == null || i < 0 || i >= bArr.length) {
            return 0;
        }
        return bArr[i];
    }

    protected static void setTo(byte[] bArr, int i, byte b) {
        if (bArr != null && i >= 0 && i < bArr.length) {
            bArr[i] = b;
        }
    }

    protected static short getFromArray(short[] sArr, int i) {
        if (sArr == null || i < 0 || i >= sArr.length) {
            return 0;
        }
        return sArr[i];
    }

    protected static void setTo(short[] sArr, int i, short s) {
        if (sArr != null && i >= 0 && i < sArr.length) {
            sArr[i] = s;
        }
    }

    protected static char getFromArray(char[] cArr, int i) {
        if (cArr == null || i < 0 || i >= cArr.length) {
            return 0;
        }
        return cArr[i];
    }

    protected static void setTo(char[] cArr, int i, char c) {
        if (cArr != null && i >= 0 && i < cArr.length) {
            cArr[i] = c;
        }
    }

    protected static int getFromArray(int[] iArr, int i) {
        if (iArr == null || i < 0 || i >= iArr.length) {
            return 0;
        }
        return iArr[i];
    }

    protected static void setTo(int[] iArr, int i, int i2) {
        if (iArr != null && i >= 0 && i < iArr.length) {
            iArr[i] = i2;
        }
    }

    protected static long getFromArray(long[] jArr, int i) {
        if (jArr == null || i < 0 || i >= jArr.length) {
            return 0;
        }
        return jArr[i];
    }

    protected static void setTo(long[] jArr, int i, long j) {
        if (jArr != null && i >= 0 && i < jArr.length) {
            jArr[i] = j;
        }
    }

    protected static float getFromArray(float[] fArr, int i) {
        if (fArr == null || i < 0 || i >= fArr.length) {
            return 0.0f;
        }
        return fArr[i];
    }

    protected static void setTo(float[] fArr, int i, float f) {
        if (fArr != null && i >= 0 && i < fArr.length) {
            fArr[i] = f;
        }
    }

    protected static double getFromArray(double[] dArr, int i) {
        if (dArr == null || i < 0 || i >= dArr.length) {
            return 0.0d;
        }
        return dArr[i];
    }

    protected static void setTo(double[] dArr, int i, double d) {
        if (dArr != null && i >= 0 && i < dArr.length) {
            dArr[i] = d;
        }
    }

    protected static <T> T getFromList(List<T> list, int i) {
        if (list == null || i < 0 || i >= list.size()) {
            return null;
        }
        return list.get(i);
    }

    protected static <T> void setTo(List<T> list, int i, T t) {
        if (list != null && i >= 0 && i < list.size()) {
            list.set(i, t);
        }
    }

    protected static <T> T getFromList(SparseArray<T> sparseArray, int i) {
        if (sparseArray == null || i < 0) {
            return null;
        }
        return sparseArray.get(i);
    }

    protected static <T> void setTo(SparseArray<T> sparseArray, int i, T t) {
        if (sparseArray != null && i >= 0 && i < sparseArray.size()) {
            sparseArray.put(i, t);
        }
    }

    protected static <T> T getFromList(LongSparseArray<T> longSparseArray, int i) {
        if (longSparseArray == null || i < 0) {
            return null;
        }
        return longSparseArray.get((long) i);
    }

    protected static <T> void setTo(LongSparseArray<T> longSparseArray, int i, T t) {
        if (longSparseArray != null && i >= 0 && i < longSparseArray.size()) {
            longSparseArray.put((long) i, t);
        }
    }

    protected static <T> T getFromList(androidx.collection.LongSparseArray<T> longSparseArray, int i) {
        if (longSparseArray == null || i < 0) {
            return null;
        }
        return longSparseArray.get((long) i);
    }

    protected static <T> void setTo(androidx.collection.LongSparseArray<T> longSparseArray, int i, T t) {
        if (longSparseArray != null && i >= 0 && i < longSparseArray.size()) {
            longSparseArray.put((long) i, t);
        }
    }

    protected static boolean getFromList(SparseBooleanArray sparseBooleanArray, int i) {
        if (sparseBooleanArray == null || i < 0) {
            return false;
        }
        return sparseBooleanArray.get(i);
    }

    protected static void setTo(SparseBooleanArray sparseBooleanArray, int i, boolean z) {
        if (sparseBooleanArray != null && i >= 0 && i < sparseBooleanArray.size()) {
            sparseBooleanArray.put(i, z);
        }
    }

    protected static int getFromList(SparseIntArray sparseIntArray, int i) {
        if (sparseIntArray == null || i < 0) {
            return 0;
        }
        return sparseIntArray.get(i);
    }

    protected static void setTo(SparseIntArray sparseIntArray, int i, int i2) {
        if (sparseIntArray != null && i >= 0 && i < sparseIntArray.size()) {
            sparseIntArray.put(i, i2);
        }
    }

    protected static long getFromList(SparseLongArray sparseLongArray, int i) {
        if (sparseLongArray == null || i < 0) {
            return 0;
        }
        return sparseLongArray.get(i);
    }

    protected static void setTo(SparseLongArray sparseLongArray, int i, long j) {
        if (sparseLongArray != null && i >= 0 && i < sparseLongArray.size()) {
            sparseLongArray.put(i, j);
        }
    }

    protected static <K, T> T getFrom(Map<K, T> map, K k) {
        if (map == null) {
            return null;
        }
        return map.get(k);
    }

    protected static <K, T> void setTo(Map<K, T> map, K k, T t) {
        if (map != null) {
            map.put(k, t);
        }
    }

    protected static void setBindingInverseListener(ViewDataBinding viewDataBinding, InverseBindingListener inverseBindingListener, PropertyChangedInverseListener propertyChangedInverseListener) {
        if (inverseBindingListener != propertyChangedInverseListener) {
            if (inverseBindingListener != null) {
                viewDataBinding.removeOnPropertyChangedCallback((PropertyChangedInverseListener) inverseBindingListener);
            }
            if (propertyChangedInverseListener != null) {
                viewDataBinding.addOnPropertyChangedCallback(propertyChangedInverseListener);
            }
        }
    }

    protected static int safeUnbox(Integer num) {
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    protected static long safeUnbox(Long l) {
        if (l == null) {
            return 0;
        }
        return l.longValue();
    }

    protected static short safeUnbox(Short sh) {
        if (sh == null) {
            return 0;
        }
        return sh.shortValue();
    }

    protected static byte safeUnbox(Byte b) {
        if (b == null) {
            return 0;
        }
        return b.byteValue();
    }

    protected static char safeUnbox(Character ch) {
        if (ch == null) {
            return 0;
        }
        return ch.charValue();
    }

    protected static double safeUnbox(Double d) {
        if (d == null) {
            return 0.0d;
        }
        return d.doubleValue();
    }

    protected static float safeUnbox(Float f) {
        if (f == null) {
            return 0.0f;
        }
        return f.floatValue();
    }

    protected static boolean safeUnbox(Boolean bool) {
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    /* access modifiers changed from: protected */
    public void setContainedBinding(ViewDataBinding viewDataBinding) {
        if (viewDataBinding != null) {
            viewDataBinding.mContainingBinding = this;
        }
    }

    protected static Object[] mapBindings(DataBindingComponent dataBindingComponent, View[] viewArr, int i, IncludedLayouts includedLayouts, SparseIntArray sparseIntArray) {
        Object[] objArr = new Object[i];
        for (View mapBindings : viewArr) {
            mapBindings(dataBindingComponent, mapBindings, objArr, includedLayouts, sparseIntArray, true);
        }
        return objArr;
    }

    /* JADX WARNING: Removed duplicated region for block: B:69:0x00fe  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x010b A[SYNTHETIC] */
    private static void mapBindings(DataBindingComponent dataBindingComponent, View view, Object[] objArr, IncludedLayouts includedLayouts, SparseIntArray sparseIntArray, boolean z) {
        int i;
        boolean z2;
        int i2;
        int i3;
        boolean z3;
        int i4;
        DataBindingComponent dataBindingComponent2 = dataBindingComponent;
        View view2 = view;
        IncludedLayouts includedLayouts2 = includedLayouts;
        SparseIntArray sparseIntArray2 = sparseIntArray;
        if (getBinding(view) == null) {
            Object tag = view.getTag();
            String str = tag instanceof String ? (String) tag : null;
            String str2 = "layout";
            int i5 = 1;
            if (z && str != null && str.startsWith(str2)) {
                int lastIndexOf = str.lastIndexOf(95);
                if (lastIndexOf > 0) {
                    int i6 = lastIndexOf + 1;
                    if (isNumeric(str, i6)) {
                        i4 = parseTagInt(str, i6);
                        if (objArr[i4] == null) {
                            objArr[i4] = view2;
                        }
                        if (includedLayouts2 == null) {
                            i4 = -1;
                        }
                        z2 = true;
                        i = i4;
                    }
                }
                i4 = -1;
                z2 = false;
                i = i4;
            } else if (str == null || !str.startsWith(BINDING_TAG_PREFIX)) {
                z2 = false;
                i = -1;
            } else {
                int parseTagInt = parseTagInt(str, BINDING_NUMBER_START);
                if (objArr[parseTagInt] == null) {
                    objArr[parseTagInt] = view2;
                }
                if (includedLayouts2 == null) {
                    parseTagInt = -1;
                }
                i = parseTagInt;
                z2 = true;
            }
            if (!z2) {
                int id = view.getId();
                if (id > 0 && sparseIntArray2 != null) {
                    int i7 = sparseIntArray2.get(id, -1);
                    if (i7 >= 0 && objArr[i7] == null) {
                        objArr[i7] = view2;
                    }
                }
            }
            if (view2 instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view2;
                int childCount = viewGroup.getChildCount();
                int i8 = 0;
                int i9 = 0;
                while (i8 < childCount) {
                    View childAt = viewGroup.getChildAt(i8);
                    if (i >= 0 && (childAt.getTag() instanceof String)) {
                        String str3 = (String) childAt.getTag();
                        if (str3.endsWith("_0") && str3.startsWith(str2) && str3.indexOf(47) > 0) {
                            int findIncludeIndex = findIncludeIndex(str3, i9, includedLayouts2, i);
                            if (findIncludeIndex >= 0) {
                                int i10 = findIncludeIndex + 1;
                                int i11 = includedLayouts2.indexes[i][findIncludeIndex];
                                int i12 = includedLayouts2.layoutIds[i][findIncludeIndex];
                                int findLastMatching = findLastMatching(viewGroup, i8);
                                if (findLastMatching == i8) {
                                    objArr[i11] = DataBindingUtil.bind(dataBindingComponent2, childAt, i12);
                                } else {
                                    int i13 = (findLastMatching - i8) + i5;
                                    View[] viewArr = new View[i13];
                                    for (int i14 = 0; i14 < i13; i14++) {
                                        viewArr[i14] = viewGroup.getChildAt(i8 + i14);
                                    }
                                    objArr[i11] = DataBindingUtil.bind(dataBindingComponent2, viewArr, i12);
                                    i8 += i13 - 1;
                                }
                                i3 = i8;
                                i2 = i10;
                                z3 = true;
                                if (z3) {
                                    mapBindings(dataBindingComponent, childAt, objArr, includedLayouts, sparseIntArray, false);
                                }
                                i8 = i3 + 1;
                                i9 = i2;
                                i5 = 1;
                            }
                        }
                    }
                    i3 = i8;
                    i2 = i9;
                    z3 = false;
                    if (z3) {
                    }
                    i8 = i3 + 1;
                    i9 = i2;
                    i5 = 1;
                }
            }
        }
    }

    private static int findIncludeIndex(String str, int i, IncludedLayouts includedLayouts, int i2) {
        CharSequence subSequence = str.subSequence(str.indexOf(47) + 1, str.length() - 2);
        String[] strArr = includedLayouts.layouts[i2];
        int length = strArr.length;
        while (i < length) {
            if (TextUtils.equals(subSequence, strArr[i])) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private static int findLastMatching(ViewGroup viewGroup, int i) {
        String str = (String) viewGroup.getChildAt(i).getTag();
        String substring = str.substring(0, str.length() - 1);
        int length = substring.length();
        int childCount = viewGroup.getChildCount();
        for (int i2 = i + 1; i2 < childCount; i2++) {
            View childAt = viewGroup.getChildAt(i2);
            String str2 = childAt.getTag() instanceof String ? (String) childAt.getTag() : null;
            if (str2 != null && str2.startsWith(substring)) {
                if (str2.length() == str.length() && str2.charAt(str2.length() - 1) == '0') {
                    return i;
                }
                if (isNumeric(str2, length)) {
                    i = i2;
                }
            }
        }
        return i;
    }

    private static boolean isNumeric(String str, int i) {
        int length = str.length();
        if (length == i) {
            return false;
        }
        while (i < length) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
            i++;
        }
        return true;
    }

    private static int parseTagInt(String str, int i) {
        int i2 = 0;
        while (i < str.length()) {
            i2 = (i2 * 10) + (str.charAt(i) - '0');
            i++;
        }
        return i2;
    }

    /* access modifiers changed from: private */
    public static void processReferenceQueue() {
        while (true) {
            Reference poll = sReferenceQueue.poll();
            if (poll == null) {
                return;
            }
            if (poll instanceof WeakListener) {
                ((WeakListener) poll).unregister();
            }
        }
    }

    protected static <T extends ViewDataBinding> T inflateInternal(LayoutInflater layoutInflater, int i, ViewGroup viewGroup, boolean z, Object obj) {
        return DataBindingUtil.inflate(layoutInflater, i, viewGroup, z, checkAndCastToBindingComponent(obj));
    }
}
