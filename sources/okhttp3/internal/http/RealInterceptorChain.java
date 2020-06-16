package okhttp3.internal.http;

import androidx.core.app.NotificationCompat;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.Transmitter;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001BU\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\n\u0012\u0006\u0010\u0010\u001a\u00020\n\u0012\u0006\u0010\u0011\u001a\u00020\n¢\u0006\u0002\u0010\u0012J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u0014\u001a\u00020\nH\u0016J\n\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016J\u0006\u0010\u0007\u001a\u00020\bJ\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u000b\u001a\u00020\fH\u0016J \u0010\u0017\u001a\u00020\u00182\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bJ\b\u0010\u0019\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\fH\u0016J\u0006\u0010\u0005\u001a\u00020\u0006J\u0018\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0018\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0018\u0010\u001f\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\b\u0010 \u001a\u00020\nH\u0016R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000¨\u0006!"}, d2 = {"Lokhttp3/internal/http/RealInterceptorChain;", "Lokhttp3/Interceptor$Chain;", "interceptors", "", "Lokhttp3/Interceptor;", "transmitter", "Lokhttp3/internal/connection/Transmitter;", "exchange", "Lokhttp3/internal/connection/Exchange;", "index", "", "request", "Lokhttp3/Request;", "call", "Lokhttp3/Call;", "connectTimeout", "readTimeout", "writeTimeout", "(Ljava/util/List;Lokhttp3/internal/connection/Transmitter;Lokhttp3/internal/connection/Exchange;ILokhttp3/Request;Lokhttp3/Call;III)V", "calls", "connectTimeoutMillis", "connection", "Lokhttp3/Connection;", "proceed", "Lokhttp3/Response;", "readTimeoutMillis", "withConnectTimeout", "timeout", "unit", "Ljava/util/concurrent/TimeUnit;", "withReadTimeout", "withWriteTimeout", "writeTimeoutMillis", "okhttp"}, k = 1, mv = {1, 1, 15})
/* compiled from: RealInterceptorChain.kt */
public final class RealInterceptorChain implements Chain {
    private final Call call;
    private int calls;
    private final int connectTimeout;
    private final Exchange exchange;
    private final int index;
    private final List<Interceptor> interceptors;
    private final int readTimeout;
    private final Request request;
    private final Transmitter transmitter;
    private final int writeTimeout;

    public RealInterceptorChain(List<? extends Interceptor> list, Transmitter transmitter2, Exchange exchange2, int i, Request request2, Call call2, int i2, int i3, int i4) {
        Intrinsics.checkParameterIsNotNull(list, "interceptors");
        Intrinsics.checkParameterIsNotNull(transmitter2, "transmitter");
        Intrinsics.checkParameterIsNotNull(request2, "request");
        Intrinsics.checkParameterIsNotNull(call2, NotificationCompat.CATEGORY_CALL);
        this.interceptors = list;
        this.transmitter = transmitter2;
        this.exchange = exchange2;
        this.index = i;
        this.request = request2;
        this.call = call2;
        this.connectTimeout = i2;
        this.readTimeout = i3;
        this.writeTimeout = i4;
    }

    public Connection connection() {
        Exchange exchange2 = this.exchange;
        return exchange2 != null ? exchange2.connection() : null;
    }

    public int connectTimeoutMillis() {
        return this.connectTimeout;
    }

    public Chain withConnectTimeout(int i, TimeUnit timeUnit) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        RealInterceptorChain realInterceptorChain = new RealInterceptorChain(this.interceptors, this.transmitter, this.exchange, this.index, this.request, this.call, Util.checkDuration("timeout", (long) i, timeUnit), this.readTimeout, this.writeTimeout);
        return realInterceptorChain;
    }

    public int readTimeoutMillis() {
        return this.readTimeout;
    }

    public Chain withReadTimeout(int i, TimeUnit timeUnit) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        RealInterceptorChain realInterceptorChain = new RealInterceptorChain(this.interceptors, this.transmitter, this.exchange, this.index, this.request, this.call, this.connectTimeout, Util.checkDuration("timeout", (long) i, timeUnit), this.writeTimeout);
        return realInterceptorChain;
    }

    public int writeTimeoutMillis() {
        return this.writeTimeout;
    }

    public Chain withWriteTimeout(int i, TimeUnit timeUnit) {
        Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
        RealInterceptorChain realInterceptorChain = new RealInterceptorChain(this.interceptors, this.transmitter, this.exchange, this.index, this.request, this.call, this.connectTimeout, this.readTimeout, Util.checkDuration("timeout", (long) i, timeUnit));
        return realInterceptorChain;
    }

    public final Transmitter transmitter() {
        return this.transmitter;
    }

    public final Exchange exchange() {
        Exchange exchange2 = this.exchange;
        if (exchange2 == null) {
            Intrinsics.throwNpe();
        }
        return exchange2;
    }

    public Call call() {
        return this.call;
    }

    public Request request() {
        return this.request;
    }

    public Response proceed(Request request2) {
        Intrinsics.checkParameterIsNotNull(request2, "request");
        return proceed(request2, this.transmitter, this.exchange);
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0040  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0124  */
    public final Response proceed(Request request2, Transmitter transmitter2, Exchange exchange2) throws IOException {
        boolean z;
        Intrinsics.checkParameterIsNotNull(request2, "request");
        Intrinsics.checkParameterIsNotNull(transmitter2, "transmitter");
        if (this.index < this.interceptors.size()) {
            boolean z2 = true;
            this.calls++;
            Exchange exchange3 = this.exchange;
            if (exchange3 != null) {
                RealConnection connection = exchange3.connection();
                if (connection == null) {
                    Intrinsics.throwNpe();
                }
                if (!connection.supportsUrl(request2.url())) {
                    z = false;
                    String str = "network interceptor ";
                    if (!z) {
                        String str2 = " must call proceed() exactly once";
                        if (this.exchange == null || this.calls <= 1) {
                            RealInterceptorChain realInterceptorChain = new RealInterceptorChain(this.interceptors, transmitter2, exchange2, this.index + 1, request2, this.call, this.connectTimeout, this.readTimeout, this.writeTimeout);
                            Interceptor interceptor = (Interceptor) this.interceptors.get(this.index);
                            Response intercept = interceptor.intercept(realInterceptorChain);
                            String str3 = "interceptor ";
                            if (intercept != null) {
                                if (exchange2 == null || this.index + 1 >= this.interceptors.size() || realInterceptorChain.calls == 1) {
                                    if (intercept.body() == null) {
                                        z2 = false;
                                    }
                                    if (z2) {
                                        return intercept;
                                    }
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(str3);
                                    sb.append(interceptor);
                                    sb.append(" returned a response with no body");
                                    throw new IllegalStateException(sb.toString().toString());
                                }
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(str);
                                sb2.append(interceptor);
                                sb2.append(str2);
                                throw new IllegalStateException(sb2.toString().toString());
                            }
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(str3);
                            sb3.append(interceptor);
                            sb3.append(" returned null");
                            throw new NullPointerException(sb3.toString());
                        }
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(str);
                        sb4.append((Interceptor) this.interceptors.get(this.index - 1));
                        sb4.append(str2);
                        throw new IllegalStateException(sb4.toString().toString());
                    }
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(str);
                    sb5.append((Interceptor) this.interceptors.get(this.index - 1));
                    sb5.append(" must retain the same host and port");
                    throw new IllegalStateException(sb5.toString().toString());
                }
            }
            z = true;
            String str4 = "network interceptor ";
            if (!z) {
            }
        } else {
            throw new AssertionError();
        }
    }
}
