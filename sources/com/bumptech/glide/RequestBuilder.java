package com.bumptech.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.ErrorRequestCoordinator;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.request.ThumbnailRequestCoordinator;
import com.bumptech.glide.request.target.PreloadTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.signature.AndroidResourceSignature;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class RequestBuilder<TranscodeType> extends BaseRequestOptions<RequestBuilder<TranscodeType>> implements Cloneable, ModelTypes<RequestBuilder<TranscodeType>> {
    protected static final RequestOptions DOWNLOAD_ONLY_OPTIONS = ((RequestOptions) ((RequestOptions) ((RequestOptions) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).priority(Priority.LOW)).skipMemoryCache(true));
    private final Context context;
    private RequestBuilder<TranscodeType> errorBuilder;
    private final Glide glide;
    private final GlideContext glideContext;
    private boolean isDefaultTransitionOptionsSet;
    private boolean isModelSet;
    private boolean isThumbnailBuilt;
    private Object model;
    private List<RequestListener<TranscodeType>> requestListeners;
    private final RequestManager requestManager;
    private Float thumbSizeMultiplier;
    private RequestBuilder<TranscodeType> thumbnailBuilder;
    private final Class<TranscodeType> transcodeClass;
    private TransitionOptions<?, ? super TranscodeType> transitionOptions;

    /* renamed from: com.bumptech.glide.RequestBuilder$1 reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType;
        static final /* synthetic */ int[] $SwitchMap$com$bumptech$glide$Priority;

        /* JADX WARNING: Can't wrap try/catch for region: R(27:0|(2:1|2)|3|(2:5|6)|7|9|10|11|(2:13|14)|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Can't wrap try/catch for region: R(29:0|1|2|3|(2:5|6)|7|9|10|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Can't wrap try/catch for region: R(30:0|1|2|3|5|6|7|9|10|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0044 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x004e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0058 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x006d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0083 */
        static {
            int[] iArr = new int[Priority.values().length];
            $SwitchMap$com$bumptech$glide$Priority = iArr;
            try {
                iArr[Priority.LOW.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$bumptech$glide$Priority[Priority.NORMAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$bumptech$glide$Priority[Priority.HIGH.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$bumptech$glide$Priority[Priority.IMMEDIATE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            int[] iArr2 = new int[ScaleType.values().length];
            $SwitchMap$android$widget$ImageView$ScaleType = iArr2;
            iArr2[ScaleType.CENTER_CROP.ordinal()] = 1;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_INSIDE.ordinal()] = 2;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_CENTER.ordinal()] = 3;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_START.ordinal()] = 4;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_END.ordinal()] = 5;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_XY.ordinal()] = 6;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER.ordinal()] = 7;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.MATRIX.ordinal()] = 8;
        }
    }

    protected RequestBuilder(Glide glide2, RequestManager requestManager2, Class<TranscodeType> cls, Context context2) {
        this.isDefaultTransitionOptionsSet = true;
        this.glide = glide2;
        this.requestManager = requestManager2;
        this.transcodeClass = cls;
        this.context = context2;
        this.transitionOptions = requestManager2.getDefaultTransitionOptions(cls);
        this.glideContext = glide2.getGlideContext();
        initRequestListeners(requestManager2.getDefaultRequestListeners());
        apply((BaseRequestOptions<?>) requestManager2.getDefaultRequestOptions());
    }

    protected RequestBuilder(Class<TranscodeType> cls, RequestBuilder<?> requestBuilder) {
        this(requestBuilder.glide, requestBuilder.requestManager, cls, requestBuilder.context);
        this.model = requestBuilder.model;
        this.isModelSet = requestBuilder.isModelSet;
        apply((BaseRequestOptions<?>) requestBuilder);
    }

    private void initRequestListeners(List<RequestListener<Object>> list) {
        for (RequestListener addListener : list) {
            addListener(addListener);
        }
    }

    public RequestBuilder<TranscodeType> apply(BaseRequestOptions<?> baseRequestOptions) {
        Preconditions.checkNotNull(baseRequestOptions);
        return (RequestBuilder) super.apply(baseRequestOptions);
    }

    public RequestBuilder<TranscodeType> transition(TransitionOptions<?, ? super TranscodeType> transitionOptions2) {
        this.transitionOptions = (TransitionOptions) Preconditions.checkNotNull(transitionOptions2);
        this.isDefaultTransitionOptionsSet = false;
        return this;
    }

    public RequestBuilder<TranscodeType> listener(RequestListener<TranscodeType> requestListener) {
        this.requestListeners = null;
        return addListener(requestListener);
    }

    public RequestBuilder<TranscodeType> addListener(RequestListener<TranscodeType> requestListener) {
        if (requestListener != null) {
            if (this.requestListeners == null) {
                this.requestListeners = new ArrayList();
            }
            this.requestListeners.add(requestListener);
        }
        return this;
    }

    public RequestBuilder<TranscodeType> error(RequestBuilder<TranscodeType> requestBuilder) {
        this.errorBuilder = requestBuilder;
        return this;
    }

    public RequestBuilder<TranscodeType> thumbnail(RequestBuilder<TranscodeType> requestBuilder) {
        this.thumbnailBuilder = requestBuilder;
        return this;
    }

    public RequestBuilder<TranscodeType> thumbnail(RequestBuilder<TranscodeType>... requestBuilderArr) {
        RequestBuilder<TranscodeType> requestBuilder = null;
        if (requestBuilderArr == null || requestBuilderArr.length == 0) {
            return thumbnail(null);
        }
        for (int length = requestBuilderArr.length - 1; length >= 0; length--) {
            RequestBuilder<TranscodeType> requestBuilder2 = requestBuilderArr[length];
            if (requestBuilder2 != null) {
                requestBuilder = requestBuilder == null ? requestBuilder2 : requestBuilder2.thumbnail(requestBuilder);
            }
        }
        return thumbnail(requestBuilder);
    }

    public RequestBuilder<TranscodeType> thumbnail(float f) {
        if (f < 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
        }
        this.thumbSizeMultiplier = Float.valueOf(f);
        return this;
    }

    public RequestBuilder<TranscodeType> load(Object obj) {
        return loadGeneric(obj);
    }

    private RequestBuilder<TranscodeType> loadGeneric(Object obj) {
        this.model = obj;
        this.isModelSet = true;
        return this;
    }

    public RequestBuilder<TranscodeType> load(Bitmap bitmap) {
        return loadGeneric(bitmap).apply((BaseRequestOptions<?>) RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
    }

    public RequestBuilder<TranscodeType> load(Drawable drawable) {
        return loadGeneric(drawable).apply((BaseRequestOptions<?>) RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
    }

    public RequestBuilder<TranscodeType> load(String str) {
        return loadGeneric(str);
    }

    public RequestBuilder<TranscodeType> load(Uri uri) {
        return loadGeneric(uri);
    }

    public RequestBuilder<TranscodeType> load(File file) {
        return loadGeneric(file);
    }

    public RequestBuilder<TranscodeType> load(Integer num) {
        return loadGeneric(num).apply((BaseRequestOptions<?>) RequestOptions.signatureOf(AndroidResourceSignature.obtain(this.context)));
    }

    @Deprecated
    public RequestBuilder<TranscodeType> load(URL url) {
        return loadGeneric(url);
    }

    public RequestBuilder<TranscodeType> load(byte[] bArr) {
        RequestBuilder<TranscodeType> loadGeneric = loadGeneric(bArr);
        if (!loadGeneric.isDiskCacheStrategySet()) {
            loadGeneric = loadGeneric.apply((BaseRequestOptions<?>) RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
        }
        return !loadGeneric.isSkipMemoryCacheSet() ? loadGeneric.apply((BaseRequestOptions<?>) RequestOptions.skipMemoryCacheOf(true)) : loadGeneric;
    }

    public RequestBuilder<TranscodeType> clone() {
        RequestBuilder<TranscodeType> requestBuilder = (RequestBuilder) super.clone();
        requestBuilder.transitionOptions = requestBuilder.transitionOptions.clone();
        return requestBuilder;
    }

    public <Y extends Target<TranscodeType>> Y into(Y y) {
        return into(y, null, Executors.mainThreadExecutor());
    }

    /* access modifiers changed from: 0000 */
    public <Y extends Target<TranscodeType>> Y into(Y y, RequestListener<TranscodeType> requestListener, Executor executor) {
        return into(y, requestListener, this, executor);
    }

    private <Y extends Target<TranscodeType>> Y into(Y y, RequestListener<TranscodeType> requestListener, BaseRequestOptions<?> baseRequestOptions, Executor executor) {
        Preconditions.checkNotNull(y);
        if (this.isModelSet) {
            Request buildRequest = buildRequest(y, requestListener, baseRequestOptions, executor);
            Request request = y.getRequest();
            if (!buildRequest.isEquivalentTo(request) || isSkipMemoryCacheWithCompletePreviousRequest(baseRequestOptions, request)) {
                this.requestManager.clear((Target<?>) y);
                y.setRequest(buildRequest);
                this.requestManager.track(y, buildRequest);
                return y;
            }
            if (!((Request) Preconditions.checkNotNull(request)).isRunning()) {
                request.begin();
            }
            return y;
        }
        throw new IllegalArgumentException("You must call #load() before calling #into()");
    }

    private boolean isSkipMemoryCacheWithCompletePreviousRequest(BaseRequestOptions<?> baseRequestOptions, Request request) {
        return !baseRequestOptions.isMemoryCacheable() && request.isComplete();
    }

    public ViewTarget<ImageView, TranscodeType> into(ImageView imageView) {
        BaseRequestOptions baseRequestOptions;
        Util.assertMainThread();
        Preconditions.checkNotNull(imageView);
        if (!isTransformationSet() && isTransformationAllowed() && imageView.getScaleType() != null) {
            switch (AnonymousClass1.$SwitchMap$android$widget$ImageView$ScaleType[imageView.getScaleType().ordinal()]) {
                case 1:
                    baseRequestOptions = clone().optionalCenterCrop();
                    break;
                case 2:
                    baseRequestOptions = clone().optionalCenterInside();
                    break;
                case 3:
                case 4:
                case 5:
                    baseRequestOptions = clone().optionalFitCenter();
                    break;
                case 6:
                    baseRequestOptions = clone().optionalCenterInside();
                    break;
            }
        }
        baseRequestOptions = this;
        return (ViewTarget) into(this.glideContext.buildImageViewTarget(imageView, this.transcodeClass), null, baseRequestOptions, Executors.mainThreadExecutor());
    }

    @Deprecated
    public FutureTarget<TranscodeType> into(int i, int i2) {
        return submit(i, i2);
    }

    public FutureTarget<TranscodeType> submit() {
        return submit(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public FutureTarget<TranscodeType> submit(int i, int i2) {
        RequestFutureTarget requestFutureTarget = new RequestFutureTarget(i, i2);
        return (FutureTarget) into(requestFutureTarget, requestFutureTarget, Executors.directExecutor());
    }

    public Target<TranscodeType> preload(int i, int i2) {
        return into((Y) PreloadTarget.obtain(this.requestManager, i, i2));
    }

    public Target<TranscodeType> preload() {
        return preload(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    @Deprecated
    public <Y extends Target<File>> Y downloadOnly(Y y) {
        return getDownloadOnlyRequest().into(y);
    }

    @Deprecated
    public FutureTarget<File> downloadOnly(int i, int i2) {
        return getDownloadOnlyRequest().submit(i, i2);
    }

    /* access modifiers changed from: protected */
    public RequestBuilder<File> getDownloadOnlyRequest() {
        return new RequestBuilder(File.class, this).apply((BaseRequestOptions<?>) DOWNLOAD_ONLY_OPTIONS);
    }

    private Priority getThumbnailPriority(Priority priority) {
        int i = AnonymousClass1.$SwitchMap$com$bumptech$glide$Priority[priority.ordinal()];
        if (i == 1) {
            return Priority.NORMAL;
        }
        if (i == 2) {
            return Priority.HIGH;
        }
        if (i == 3 || i == 4) {
            return Priority.IMMEDIATE;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("unknown priority: ");
        sb.append(getPriority());
        throw new IllegalArgumentException(sb.toString());
    }

    private Request buildRequest(Target<TranscodeType> target, RequestListener<TranscodeType> requestListener, BaseRequestOptions<?> baseRequestOptions, Executor executor) {
        return buildRequestRecursive(new Object(), target, requestListener, null, this.transitionOptions, baseRequestOptions.getPriority(), baseRequestOptions.getOverrideWidth(), baseRequestOptions.getOverrideHeight(), baseRequestOptions, executor);
    }

    /* JADX WARNING: type inference failed for: r15v0 */
    /* JADX WARNING: type inference failed for: r4v0, types: [com.bumptech.glide.request.RequestCoordinator] */
    /* JADX WARNING: type inference failed for: r1v7 */
    /* JADX WARNING: type inference failed for: r15v2 */
    /* JADX WARNING: type inference failed for: r4v1 */
    /* JADX WARNING: type inference failed for: r0v4, types: [com.bumptech.glide.request.ErrorRequestCoordinator] */
    /* JADX WARNING: type inference failed for: r4v2 */
    /* JADX WARNING: type inference failed for: r15v3 */
    /* JADX WARNING: type inference failed for: r4v3 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 5 */
    private Request buildRequestRecursive(Object obj, Target<TranscodeType> target, RequestListener<TranscodeType> requestListener, RequestCoordinator requestCoordinator, TransitionOptions<?, ? super TranscodeType> transitionOptions2, Priority priority, int i, int i2, BaseRequestOptions<?> baseRequestOptions, Executor executor) {
        ? r15;
        ? r4;
        if (this.errorBuilder != null) {
            ? errorRequestCoordinator = new ErrorRequestCoordinator(obj, requestCoordinator);
            r15 = errorRequestCoordinator;
            r4 = errorRequestCoordinator;
        } else {
            Object obj2 = obj;
            r15 = 0;
            r4 = requestCoordinator;
        }
        Request buildThumbnailRequestRecursive = buildThumbnailRequestRecursive(obj, target, requestListener, r4, transitionOptions2, priority, i, i2, baseRequestOptions, executor);
        if (r15 == 0) {
            return buildThumbnailRequestRecursive;
        }
        int overrideWidth = this.errorBuilder.getOverrideWidth();
        int overrideHeight = this.errorBuilder.getOverrideHeight();
        if (Util.isValidDimensions(i, i2) && !this.errorBuilder.isValidOverride()) {
            overrideWidth = baseRequestOptions.getOverrideWidth();
            overrideHeight = baseRequestOptions.getOverrideHeight();
        }
        int i3 = overrideWidth;
        int i4 = overrideHeight;
        RequestBuilder<TranscodeType> requestBuilder = this.errorBuilder;
        ErrorRequestCoordinator errorRequestCoordinator2 = r15;
        errorRequestCoordinator2.setRequests(buildThumbnailRequestRecursive, requestBuilder.buildRequestRecursive(obj, target, requestListener, errorRequestCoordinator2, requestBuilder.transitionOptions, requestBuilder.getPriority(), i3, i4, this.errorBuilder, executor));
        return errorRequestCoordinator2;
    }

    private Request buildThumbnailRequestRecursive(Object obj, Target<TranscodeType> target, RequestListener<TranscodeType> requestListener, RequestCoordinator requestCoordinator, TransitionOptions<?, ? super TranscodeType> transitionOptions2, Priority priority, int i, int i2, BaseRequestOptions<?> baseRequestOptions, Executor executor) {
        Priority priority2;
        Object obj2 = obj;
        RequestCoordinator requestCoordinator2 = requestCoordinator;
        Priority priority3 = priority;
        RequestBuilder<TranscodeType> requestBuilder = this.thumbnailBuilder;
        if (requestBuilder != null) {
            if (!this.isThumbnailBuilt) {
                TransitionOptions<?, ? super TranscodeType> transitionOptions3 = requestBuilder.isDefaultTransitionOptionsSet ? transitionOptions2 : requestBuilder.transitionOptions;
                if (this.thumbnailBuilder.isPrioritySet()) {
                    priority2 = this.thumbnailBuilder.getPriority();
                } else {
                    priority2 = getThumbnailPriority(priority3);
                }
                Priority priority4 = priority2;
                int overrideWidth = this.thumbnailBuilder.getOverrideWidth();
                int overrideHeight = this.thumbnailBuilder.getOverrideHeight();
                if (Util.isValidDimensions(i, i2) && !this.thumbnailBuilder.isValidOverride()) {
                    overrideWidth = baseRequestOptions.getOverrideWidth();
                    overrideHeight = baseRequestOptions.getOverrideHeight();
                }
                int i3 = overrideWidth;
                int i4 = overrideHeight;
                ThumbnailRequestCoordinator thumbnailRequestCoordinator = new ThumbnailRequestCoordinator(obj2, requestCoordinator2);
                Object obj3 = obj;
                Target<TranscodeType> target2 = target;
                RequestListener<TranscodeType> requestListener2 = requestListener;
                ThumbnailRequestCoordinator thumbnailRequestCoordinator2 = thumbnailRequestCoordinator;
                Request obtainRequest = obtainRequest(obj3, target2, requestListener2, baseRequestOptions, thumbnailRequestCoordinator, transitionOptions2, priority, i, i2, executor);
                this.isThumbnailBuilt = true;
                RequestBuilder<TranscodeType> requestBuilder2 = this.thumbnailBuilder;
                Request request = obtainRequest;
                Request buildRequestRecursive = requestBuilder2.buildRequestRecursive(obj3, target2, requestListener2, thumbnailRequestCoordinator2, transitionOptions3, priority4, i3, i4, requestBuilder2, executor);
                this.isThumbnailBuilt = false;
                thumbnailRequestCoordinator2.setRequests(request, buildRequestRecursive);
                return thumbnailRequestCoordinator2;
            }
            throw new IllegalStateException("You cannot use a request as both the main request and a thumbnail, consider using clone() on the request(s) passed to thumbnail()");
        } else if (this.thumbSizeMultiplier == null) {
            return obtainRequest(obj, target, requestListener, baseRequestOptions, requestCoordinator, transitionOptions2, priority, i, i2, executor);
        } else {
            ThumbnailRequestCoordinator thumbnailRequestCoordinator3 = new ThumbnailRequestCoordinator(obj2, requestCoordinator2);
            Target<TranscodeType> target3 = target;
            RequestListener<TranscodeType> requestListener3 = requestListener;
            ThumbnailRequestCoordinator thumbnailRequestCoordinator4 = thumbnailRequestCoordinator3;
            TransitionOptions<?, ? super TranscodeType> transitionOptions4 = transitionOptions2;
            int i5 = i;
            int i6 = i2;
            Executor executor2 = executor;
            thumbnailRequestCoordinator3.setRequests(obtainRequest(obj, target3, requestListener3, baseRequestOptions, thumbnailRequestCoordinator4, transitionOptions4, priority, i5, i6, executor2), obtainRequest(obj, target3, requestListener3, baseRequestOptions.clone().sizeMultiplier(this.thumbSizeMultiplier.floatValue()), thumbnailRequestCoordinator4, transitionOptions4, getThumbnailPriority(priority3), i5, i6, executor2));
            return thumbnailRequestCoordinator3;
        }
    }

    private Request obtainRequest(Object obj, Target<TranscodeType> target, RequestListener<TranscodeType> requestListener, BaseRequestOptions<?> baseRequestOptions, RequestCoordinator requestCoordinator, TransitionOptions<?, ? super TranscodeType> transitionOptions2, Priority priority, int i, int i2, Executor executor) {
        Context context2 = this.context;
        GlideContext glideContext2 = this.glideContext;
        return SingleRequest.obtain(context2, glideContext2, obj, this.model, this.transcodeClass, baseRequestOptions, i, i2, priority, target, requestListener, this.requestListeners, requestCoordinator, glideContext2.getEngine(), transitionOptions2.getTransitionFactory(), executor);
    }
}
