package okhttp3.internal.http;

import com.cosafe.android.utils.Constants;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.Proxy.Type;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RouteException;
import okhttp3.internal.connection.Transmitter;
import okhttp3.internal.http2.ConnectionShutdownException;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001a\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0002J\u0010\u0010\u000e\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0012H\u0002J(\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u0006H\u0002J\u0018\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u0006H\u0002J\u0018\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u001d\u001a\u00020\u001cH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lokhttp3/internal/http/RetryAndFollowUpInterceptor;", "Lokhttp3/Interceptor;", "client", "Lokhttp3/OkHttpClient;", "(Lokhttp3/OkHttpClient;)V", "buildRedirectRequest", "Lokhttp3/Request;", "userResponse", "Lokhttp3/Response;", "method", "", "followUpRequest", "route", "Lokhttp3/Route;", "intercept", "chain", "Lokhttp3/Interceptor$Chain;", "isRecoverable", "", "e", "Ljava/io/IOException;", "requestSendStarted", "recover", "transmitter", "Lokhttp3/internal/connection/Transmitter;", "userRequest", "requestIsOneShot", "retryAfter", "", "defaultDelay", "Companion", "okhttp"}, k = 1, mv = {1, 1, 15})
/* compiled from: RetryAndFollowUpInterceptor.kt */
public final class RetryAndFollowUpInterceptor implements Interceptor {
    public static final Companion Companion = new Companion(null);
    private static final int MAX_FOLLOW_UPS = 20;
    private final OkHttpClient client;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lokhttp3/internal/http/RetryAndFollowUpInterceptor$Companion;", "", "()V", "MAX_FOLLOW_UPS", "", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: RetryAndFollowUpInterceptor.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public RetryAndFollowUpInterceptor(OkHttpClient okHttpClient) {
        Intrinsics.checkParameterIsNotNull(okHttpClient, "client");
        this.client = okHttpClient;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00c4, code lost:
        r1.exchangeDoneDueToException();
     */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0055 A[SYNTHETIC] */
    public Response intercept(Chain chain) throws IOException {
        Route route;
        Request followUpRequest;
        Intrinsics.checkParameterIsNotNull(chain, "chain");
        Request request = chain.request();
        RealInterceptorChain realInterceptorChain = (RealInterceptorChain) chain;
        Transmitter transmitter = realInterceptorChain.transmitter();
        Response response = null;
        int i = 0;
        while (true) {
            transmitter.prepareToConnect(request);
            if (!transmitter.isCanceled()) {
                try {
                    Response proceed = realInterceptorChain.proceed(request, transmitter, null);
                    if (response != null) {
                        proceed = proceed.newBuilder().priorResponse(response.newBuilder().body(null).build()).build();
                    }
                    response = proceed;
                    Exchange exchange = response.exchange();
                    if (exchange != null) {
                        RealConnection connection = exchange.connection();
                        if (connection != null) {
                            route = connection.route();
                            followUpRequest = followUpRequest(response, route);
                            if (followUpRequest != null) {
                                if (exchange != null && exchange.isDuplex()) {
                                    transmitter.timeoutEarlyExit();
                                }
                                return response;
                            }
                            RequestBody body = followUpRequest.body();
                            if (body != null && body.isOneShot()) {
                                return response;
                            }
                            ResponseBody body2 = response.body();
                            if (body2 != null) {
                                Util.closeQuietly((Closeable) body2);
                            }
                            if (transmitter.hasExchange() && exchange != null) {
                                exchange.detachWithViolence();
                            }
                            i++;
                            if (i <= 20) {
                                request = followUpRequest;
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Too many follow-up requests: ");
                                sb.append(i);
                                throw new ProtocolException(sb.toString());
                            }
                        }
                    }
                    route = null;
                    followUpRequest = followUpRequest(response, route);
                    if (followUpRequest != null) {
                    }
                } catch (RouteException e) {
                    if (!recover(e.getLastConnectException(), transmitter, false, request)) {
                        throw e.getFirstConnectException();
                    }
                } catch (IOException e2) {
                    if (!recover(e2, transmitter, !(e2 instanceof ConnectionShutdownException), request)) {
                        throw e2;
                    }
                } catch (Throwable th) {
                    transmitter.exchangeDoneDueToException();
                    throw th;
                }
            } else {
                throw new IOException("Canceled");
            }
        }
    }

    private final boolean recover(IOException iOException, Transmitter transmitter, boolean z, Request request) {
        if (!this.client.retryOnConnectionFailure()) {
            return false;
        }
        if ((!z || !requestIsOneShot(iOException, request)) && isRecoverable(iOException, z) && transmitter.canRetry()) {
            return true;
        }
        return false;
    }

    private final boolean requestIsOneShot(IOException iOException, Request request) {
        RequestBody body = request.body();
        return (body != null && body.isOneShot()) || (iOException instanceof FileNotFoundException);
    }

    private final boolean isRecoverable(IOException iOException, boolean z) {
        boolean z2 = false;
        if (iOException instanceof ProtocolException) {
            return false;
        }
        if (iOException instanceof InterruptedIOException) {
            if ((iOException instanceof SocketTimeoutException) && !z) {
                z2 = true;
            }
            return z2;
        } else if ((!(iOException instanceof SSLHandshakeException) || !(iOException.getCause() instanceof CertificateException)) && !(iOException instanceof SSLPeerUnverifiedException)) {
            return true;
        } else {
            return false;
        }
    }

    private final Request followUpRequest(Response response, Route route) throws IOException {
        int code = response.code();
        String method = response.request().method();
        if (code == 307 || code == 308) {
            if (!(!Intrinsics.areEqual((Object) method, (Object) "GET")) || !(!Intrinsics.areEqual((Object) method, (Object) "HEAD"))) {
                return buildRedirectRequest(response, method);
            }
            return null;
        } else if (code == 401) {
            return this.client.authenticator().authenticate(route, response);
        } else {
            if (code == 503) {
                Response priorResponse = response.priorResponse();
                if ((priorResponse == null || priorResponse.code() != 503) && retryAfter(response, Integer.MAX_VALUE) == 0) {
                    return response.request();
                }
                return null;
            } else if (code == 407) {
                if (route == null) {
                    Intrinsics.throwNpe();
                }
                if (route.proxy().type() == Type.HTTP) {
                    return this.client.proxyAuthenticator().authenticate(route, response);
                }
                throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
            } else if (code != 408) {
                switch (code) {
                    case 300:
                    case Constants.REQUEST_BLUETOOTH_PERMISSION /*301*/:
                    case 302:
                    case 303:
                        return buildRedirectRequest(response, method);
                    default:
                        return null;
                }
            } else if (!this.client.retryOnConnectionFailure()) {
                return null;
            } else {
                RequestBody body = response.request().body();
                if (body != null && body.isOneShot()) {
                    return null;
                }
                Response priorResponse2 = response.priorResponse();
                if ((priorResponse2 == null || priorResponse2.code() != 408) && retryAfter(response, 0) <= 0) {
                    return response.request();
                }
                return null;
            }
        }
    }

    private final Request buildRedirectRequest(Response response, String str) {
        RequestBody requestBody = null;
        if (!this.client.followRedirects()) {
            return null;
        }
        String header$default = Response.header$default(response, "Location", null, 2, null);
        if (header$default != null) {
            HttpUrl resolve = response.request().url().resolve(header$default);
            if (resolve != null) {
                if (!Intrinsics.areEqual((Object) resolve.scheme(), (Object) response.request().url().scheme()) && !this.client.followSslRedirects()) {
                    return null;
                }
                Builder newBuilder = response.request().newBuilder();
                if (HttpMethod.permitsRequestBody(str)) {
                    boolean redirectsWithBody = HttpMethod.INSTANCE.redirectsWithBody(str);
                    if (HttpMethod.INSTANCE.redirectsToGet(str)) {
                        newBuilder.method("GET", null);
                    } else {
                        if (redirectsWithBody) {
                            requestBody = response.request().body();
                        }
                        newBuilder.method(str, requestBody);
                    }
                    if (!redirectsWithBody) {
                        newBuilder.removeHeader("Transfer-Encoding");
                        newBuilder.removeHeader("Content-Length");
                        newBuilder.removeHeader("Content-Type");
                    }
                }
                if (!Util.canReuseConnectionFor(response.request().url(), resolve)) {
                    newBuilder.removeHeader("Authorization");
                }
                return newBuilder.url(resolve).build();
            }
        }
        return null;
    }

    private final int retryAfter(Response response, int i) {
        String header$default = Response.header$default(response, "Retry-After", null, 2, null);
        if (header$default == null) {
            return i;
        }
        if (!new Regex("\\d+").matches(header$default)) {
            return Integer.MAX_VALUE;
        }
        Integer valueOf = Integer.valueOf(header$default);
        Intrinsics.checkExpressionValueIsNotNull(valueOf, "Integer.valueOf(header)");
        return valueOf.intValue();
    }
}
