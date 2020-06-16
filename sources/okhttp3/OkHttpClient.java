package okhttp3;

import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs;
import java.net.Proxy;
import java.net.ProxySelector;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Call.Factory;
import okhttp3.Interceptor.Chain;
import okhttp3.internal.Util;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.proxy.NullProxySelector;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.OkHostnameVerifier;
import okhttp3.internal.ws.RealWebSocket;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000Ø\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\b\u0016\u0018\u0000 p2\u00020\u00012\u00020\u00022\u00020\u0003:\u0002opB\u0007\b\u0016¢\u0006\u0002\u0010\u0004B\u000f\b\u0000\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\r\u0010\b\u001a\u00020\tH\u0007¢\u0006\u0002\bLJ\u000f\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0007¢\u0006\u0002\bMJ\r\u0010\u000e\u001a\u00020\u000fH\u0007¢\u0006\u0002\bNJ\r\u0010\u0014\u001a\u00020\u0015H\u0007¢\u0006\u0002\bOJ\r\u0010\u0017\u001a\u00020\u000fH\u0007¢\u0006\u0002\bPJ\r\u0010\u0018\u001a\u00020\u0019H\u0007¢\u0006\u0002\bQJ\u0013\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cH\u0007¢\u0006\u0002\bRJ\r\u0010\u001f\u001a\u00020 H\u0007¢\u0006\u0002\bSJ\r\u0010\"\u001a\u00020#H\u0007¢\u0006\u0002\bTJ\r\u0010%\u001a\u00020&H\u0007¢\u0006\u0002\bUJ\r\u0010(\u001a\u00020)H\u0007¢\u0006\u0002\bVJ\r\u0010+\u001a\u00020,H\u0007¢\u0006\u0002\bWJ\r\u0010.\u001a\u00020,H\u0007¢\u0006\u0002\bXJ\r\u0010/\u001a\u000200H\u0007¢\u0006\u0002\bYJ\u0013\u00102\u001a\b\u0012\u0004\u0012\u0002030\u001cH\u0007¢\u0006\u0002\bZJ\u0013\u00104\u001a\b\u0012\u0004\u0012\u0002030\u001cH\u0007¢\u0006\u0002\b[J\b\u0010\\\u001a\u00020\u0006H\u0016J\u0010\u0010]\u001a\u00020^2\u0006\u0010_\u001a\u00020`H\u0016J\u0018\u0010a\u001a\u00020b2\u0006\u0010_\u001a\u00020`2\u0006\u0010c\u001a\u00020dH\u0016J\r\u00105\u001a\u00020\u000fH\u0007¢\u0006\u0002\beJ\u0013\u00106\u001a\b\u0012\u0004\u0012\u0002070\u001cH\u0007¢\u0006\u0002\bfJ\u000f\u00108\u001a\u0004\u0018\u000109H\u0007¢\u0006\u0002\bgJ\r\u0010;\u001a\u00020\tH\u0007¢\u0006\u0002\bhJ\r\u0010<\u001a\u00020=H\u0007¢\u0006\u0002\biJ\r\u0010?\u001a\u00020\u000fH\u0007¢\u0006\u0002\bjJ\r\u0010@\u001a\u00020,H\u0007¢\u0006\u0002\bkJ\r\u0010A\u001a\u00020BH\u0007¢\u0006\u0002\blJ\r\u0010D\u001a\u00020EH\u0007¢\u0006\u0002\bmJ\r\u0010H\u001a\u00020\u000fH\u0007¢\u0006\u0002\bnR\u0013\u0010\b\u001a\u00020\t8G¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\nR\u0015\u0010\u000b\u001a\u0004\u0018\u00010\f8G¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\rR\u0013\u0010\u000e\u001a\u00020\u000f8G¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0010R\u0015\u0010\u0011\u001a\u0004\u0018\u00010\u00128G¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0013R\u0013\u0010\u0014\u001a\u00020\u00158G¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0016R\u0013\u0010\u0017\u001a\u00020\u000f8G¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0010R\u0013\u0010\u0018\u001a\u00020\u00198G¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u001aR\u0019\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001c8G¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001eR\u0013\u0010\u001f\u001a\u00020 8G¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010!R\u0013\u0010\"\u001a\u00020#8G¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010$R\u0013\u0010%\u001a\u00020&8G¢\u0006\b\n\u0000\u001a\u0004\b%\u0010'R\u0013\u0010(\u001a\u00020)8G¢\u0006\b\n\u0000\u001a\u0004\b(\u0010*R\u0013\u0010+\u001a\u00020,8G¢\u0006\b\n\u0000\u001a\u0004\b+\u0010-R\u0013\u0010.\u001a\u00020,8G¢\u0006\b\n\u0000\u001a\u0004\b.\u0010-R\u0013\u0010/\u001a\u0002008G¢\u0006\b\n\u0000\u001a\u0004\b/\u00101R\u0019\u00102\u001a\b\u0012\u0004\u0012\u0002030\u001c8G¢\u0006\b\n\u0000\u001a\u0004\b2\u0010\u001eR\u0019\u00104\u001a\b\u0012\u0004\u0012\u0002030\u001c8G¢\u0006\b\n\u0000\u001a\u0004\b4\u0010\u001eR\u0013\u00105\u001a\u00020\u000f8G¢\u0006\b\n\u0000\u001a\u0004\b5\u0010\u0010R\u0019\u00106\u001a\b\u0012\u0004\u0012\u0002070\u001c8G¢\u0006\b\n\u0000\u001a\u0004\b6\u0010\u001eR\u0015\u00108\u001a\u0004\u0018\u0001098G¢\u0006\b\n\u0000\u001a\u0004\b8\u0010:R\u0013\u0010;\u001a\u00020\t8G¢\u0006\b\n\u0000\u001a\u0004\b;\u0010\nR\u0013\u0010<\u001a\u00020=8G¢\u0006\b\n\u0000\u001a\u0004\b<\u0010>R\u0013\u0010?\u001a\u00020\u000f8G¢\u0006\b\n\u0000\u001a\u0004\b?\u0010\u0010R\u0013\u0010@\u001a\u00020,8G¢\u0006\b\n\u0000\u001a\u0004\b@\u0010-R\u0013\u0010A\u001a\u00020B8G¢\u0006\b\n\u0000\u001a\u0004\bA\u0010CR\u0011\u0010D\u001a\u00020E8G¢\u0006\u0006\u001a\u0004\bD\u0010FR\u0010\u0010G\u001a\u0004\u0018\u00010EX\u0004¢\u0006\u0002\n\u0000R\u0013\u0010H\u001a\u00020\u000f8G¢\u0006\b\n\u0000\u001a\u0004\bH\u0010\u0010R\u0015\u0010I\u001a\u0004\u0018\u00010J8G¢\u0006\b\n\u0000\u001a\u0004\bI\u0010K¨\u0006q"}, d2 = {"Lokhttp3/OkHttpClient;", "", "Lokhttp3/Call$Factory;", "Lokhttp3/WebSocket$Factory;", "()V", "builder", "Lokhttp3/OkHttpClient$Builder;", "(Lokhttp3/OkHttpClient$Builder;)V", "authenticator", "Lokhttp3/Authenticator;", "()Lokhttp3/Authenticator;", "cache", "Lokhttp3/Cache;", "()Lokhttp3/Cache;", "callTimeoutMillis", "", "()I", "certificateChainCleaner", "Lokhttp3/internal/tls/CertificateChainCleaner;", "()Lokhttp3/internal/tls/CertificateChainCleaner;", "certificatePinner", "Lokhttp3/CertificatePinner;", "()Lokhttp3/CertificatePinner;", "connectTimeoutMillis", "connectionPool", "Lokhttp3/ConnectionPool;", "()Lokhttp3/ConnectionPool;", "connectionSpecs", "", "Lokhttp3/ConnectionSpec;", "()Ljava/util/List;", "cookieJar", "Lokhttp3/CookieJar;", "()Lokhttp3/CookieJar;", "dispatcher", "Lokhttp3/Dispatcher;", "()Lokhttp3/Dispatcher;", "dns", "Lokhttp3/Dns;", "()Lokhttp3/Dns;", "eventListenerFactory", "Lokhttp3/EventListener$Factory;", "()Lokhttp3/EventListener$Factory;", "followRedirects", "", "()Z", "followSslRedirects", "hostnameVerifier", "Ljavax/net/ssl/HostnameVerifier;", "()Ljavax/net/ssl/HostnameVerifier;", "interceptors", "Lokhttp3/Interceptor;", "networkInterceptors", "pingIntervalMillis", "protocols", "Lokhttp3/Protocol;", "proxy", "Ljava/net/Proxy;", "()Ljava/net/Proxy;", "proxyAuthenticator", "proxySelector", "Ljava/net/ProxySelector;", "()Ljava/net/ProxySelector;", "readTimeoutMillis", "retryOnConnectionFailure", "socketFactory", "Ljavax/net/SocketFactory;", "()Ljavax/net/SocketFactory;", "sslSocketFactory", "Ljavax/net/ssl/SSLSocketFactory;", "()Ljavax/net/ssl/SSLSocketFactory;", "sslSocketFactoryOrNull", "writeTimeoutMillis", "x509TrustManager", "Ljavax/net/ssl/X509TrustManager;", "()Ljavax/net/ssl/X509TrustManager;", "-deprecated_authenticator", "-deprecated_cache", "-deprecated_callTimeoutMillis", "-deprecated_certificatePinner", "-deprecated_connectTimeoutMillis", "-deprecated_connectionPool", "-deprecated_connectionSpecs", "-deprecated_cookieJar", "-deprecated_dispatcher", "-deprecated_dns", "-deprecated_eventListenerFactory", "-deprecated_followRedirects", "-deprecated_followSslRedirects", "-deprecated_hostnameVerifier", "-deprecated_interceptors", "-deprecated_networkInterceptors", "newBuilder", "newCall", "Lokhttp3/Call;", "request", "Lokhttp3/Request;", "newWebSocket", "Lokhttp3/WebSocket;", "listener", "Lokhttp3/WebSocketListener;", "-deprecated_pingIntervalMillis", "-deprecated_protocols", "-deprecated_proxy", "-deprecated_proxyAuthenticator", "-deprecated_proxySelector", "-deprecated_readTimeoutMillis", "-deprecated_retryOnConnectionFailure", "-deprecated_socketFactory", "-deprecated_sslSocketFactory", "-deprecated_writeTimeoutMillis", "Builder", "Companion", "okhttp"}, k = 1, mv = {1, 1, 15})
/* compiled from: OkHttpClient.kt */
public class OkHttpClient implements Cloneable, Factory, WebSocket.Factory {
    public static final Companion Companion = new Companion(null);
    /* access modifiers changed from: private */
    public static final List<ConnectionSpec> DEFAULT_CONNECTION_SPECS = Util.immutableListOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT);
    /* access modifiers changed from: private */
    public static final List<Protocol> DEFAULT_PROTOCOLS = Util.immutableListOf(Protocol.HTTP_2, Protocol.HTTP_1_1);
    private final Authenticator authenticator;
    private final Cache cache;
    private final int callTimeoutMillis;
    private final CertificateChainCleaner certificateChainCleaner;
    private final CertificatePinner certificatePinner;
    private final int connectTimeoutMillis;
    private final ConnectionPool connectionPool;
    private final List<ConnectionSpec> connectionSpecs;
    private final CookieJar cookieJar;
    private final Dispatcher dispatcher;
    private final Dns dns;
    private final EventListener.Factory eventListenerFactory;
    private final boolean followRedirects;
    private final boolean followSslRedirects;
    private final HostnameVerifier hostnameVerifier;
    private final List<Interceptor> interceptors;
    private final List<Interceptor> networkInterceptors;
    private final int pingIntervalMillis;
    private final List<Protocol> protocols;
    private final Proxy proxy;
    private final Authenticator proxyAuthenticator;
    private final ProxySelector proxySelector;
    private final int readTimeoutMillis;
    private final boolean retryOnConnectionFailure;
    private final SocketFactory socketFactory;
    /* access modifiers changed from: private */
    public final SSLSocketFactory sslSocketFactoryOrNull;
    private final int writeTimeoutMillis;
    private final X509TrustManager x509TrustManager;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000ì\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u000f\b\u0010\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B\u0005¢\u0006\u0002\u0010\u0005J<\u0010\u0001\u001a\u00020\u00002*\b\u0004\u0010\u0001\u001a#\u0012\u0017\u0012\u00150\u0001¢\u0006\u000f\b\u0001\u0012\n\b\u0001\u0012\u0005\b\b(\u0001\u0012\u0005\u0012\u00030\u00010\u0001H\b¢\u0006\u0003\b\u0001J\u0010\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020]J<\u0010\u0001\u001a\u00020\u00002*\b\u0004\u0010\u0001\u001a#\u0012\u0017\u0012\u00150\u0001¢\u0006\u000f\b\u0001\u0012\n\b\u0001\u0012\u0005\b\b(\u0001\u0012\u0005\u0012\u00030\u00010\u0001H\b¢\u0006\u0003\b\u0001J\u0010\u0010\u0001\u001a\u00020\u00002\u0007\u0010\u0001\u001a\u00020]J\u000e\u0010\u0006\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u0007J\u0007\u0010\u0001\u001a\u00020\u0003J\u0010\u0010\f\u001a\u00020\u00002\b\u0010\f\u001a\u0004\u0018\u00010\rJ\u0012\u0010\u0012\u001a\u00020\u00002\b\u0010\u0001\u001a\u00030 \u0001H\u0007J\u001a\u0010\u0012\u001a\u00020\u00002\b\u0010¡\u0001\u001a\u00030¢\u00012\b\u0010£\u0001\u001a\u00030¤\u0001J\u000e\u0010\u001e\u001a\u00020\u00002\u0006\u0010\u001e\u001a\u00020\u001fJ\u0012\u0010$\u001a\u00020\u00002\b\u0010\u0001\u001a\u00030 \u0001H\u0007J\u001a\u0010$\u001a\u00020\u00002\b\u0010¡\u0001\u001a\u00030¢\u00012\b\u0010£\u0001\u001a\u00030¤\u0001J\u000e\u0010'\u001a\u00020\u00002\u0006\u0010'\u001a\u00020(J\u0014\u0010-\u001a\u00020\u00002\f\u0010-\u001a\b\u0012\u0004\u0012\u00020/0.J\u000e\u00104\u001a\u00020\u00002\u0006\u00104\u001a\u000205J\u000e\u0010:\u001a\u00020\u00002\u0006\u0010:\u001a\u00020;J\u000e\u0010@\u001a\u00020\u00002\u0006\u0010@\u001a\u00020AJ\u0011\u0010¥\u0001\u001a\u00020\u00002\b\u0010¥\u0001\u001a\u00030¦\u0001J\u000e\u0010F\u001a\u00020\u00002\u0006\u0010F\u001a\u00020GJ\u000e\u0010L\u001a\u00020\u00002\u0006\u0010L\u001a\u00020MJ\u000f\u0010R\u001a\u00020\u00002\u0007\u0010§\u0001\u001a\u00020MJ\u000e\u0010U\u001a\u00020\u00002\u0006\u0010U\u001a\u00020VJ\f\u0010[\u001a\b\u0012\u0004\u0012\u00020]0\\J\f\u0010_\u001a\b\u0012\u0004\u0012\u00020]0\\J\u0012\u0010a\u001a\u00020\u00002\b\u0010\u0001\u001a\u00030 \u0001H\u0007J\u001a\u0010a\u001a\u00020\u00002\b\u0010¨\u0001\u001a\u00030¢\u00012\b\u0010£\u0001\u001a\u00030¤\u0001J\u0014\u0010d\u001a\u00020\u00002\f\u0010d\u001a\b\u0012\u0004\u0012\u00020e0.J\u0010\u0010h\u001a\u00020\u00002\b\u0010h\u001a\u0004\u0018\u00010iJ\u000e\u0010n\u001a\u00020\u00002\u0006\u0010n\u001a\u00020\u0007J\u000e\u0010q\u001a\u00020\u00002\u0006\u0010q\u001a\u00020rJ\u0012\u0010w\u001a\u00020\u00002\b\u0010\u0001\u001a\u00030 \u0001H\u0007J\u001a\u0010w\u001a\u00020\u00002\b\u0010¡\u0001\u001a\u00030¢\u00012\b\u0010£\u0001\u001a\u00030¤\u0001J\u000e\u0010z\u001a\u00020\u00002\u0006\u0010z\u001a\u00020MJ\u000e\u0010}\u001a\u00020\u00002\u0006\u0010}\u001a\u00020~J\u0013\u0010©\u0001\u001a\u00020\u00002\b\u0010©\u0001\u001a\u00030\u0001H\u0007J\u001b\u0010©\u0001\u001a\u00020\u00002\b\u0010©\u0001\u001a\u00030\u00012\b\u0010ª\u0001\u001a\u00030\u0001J\u0013\u0010\u0001\u001a\u00020\u00002\b\u0010\u0001\u001a\u00030 \u0001H\u0007J\u001b\u0010\u0001\u001a\u00020\u00002\b\u0010¡\u0001\u001a\u00030¢\u00012\b\u0010£\u0001\u001a\u00030¤\u0001R\u001a\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001c\u0010\f\u001a\u0004\u0018\u00010\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001c\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u001a\u0010\u001e\u001a\u00020\u001fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u001a\u0010$\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u0015\"\u0004\b&\u0010\u0017R\u001a\u0010'\u001a\u00020(X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R \u0010-\u001a\b\u0012\u0004\u0012\u00020/0.X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b0\u00101\"\u0004\b2\u00103R\u001a\u00104\u001a\u000205X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b6\u00107\"\u0004\b8\u00109R\u001a\u0010:\u001a\u00020;X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b<\u0010=\"\u0004\b>\u0010?R\u001a\u0010@\u001a\u00020AX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bB\u0010C\"\u0004\bD\u0010ER\u001a\u0010F\u001a\u00020GX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bH\u0010I\"\u0004\bJ\u0010KR\u001a\u0010L\u001a\u00020MX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bN\u0010O\"\u0004\bP\u0010QR\u001a\u0010R\u001a\u00020MX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bS\u0010O\"\u0004\bT\u0010QR\u001a\u0010U\u001a\u00020VX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bW\u0010X\"\u0004\bY\u0010ZR\u001a\u0010[\u001a\b\u0012\u0004\u0012\u00020]0\\X\u0004¢\u0006\b\n\u0000\u001a\u0004\b^\u00101R\u001a\u0010_\u001a\b\u0012\u0004\u0012\u00020]0\\X\u0004¢\u0006\b\n\u0000\u001a\u0004\b`\u00101R\u001a\u0010a\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bb\u0010\u0015\"\u0004\bc\u0010\u0017R \u0010d\u001a\b\u0012\u0004\u0012\u00020e0.X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bf\u00101\"\u0004\bg\u00103R\u001c\u0010h\u001a\u0004\u0018\u00010iX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bj\u0010k\"\u0004\bl\u0010mR\u001a\u0010n\u001a\u00020\u0007X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bo\u0010\t\"\u0004\bp\u0010\u000bR\u001c\u0010q\u001a\u0004\u0018\u00010rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bs\u0010t\"\u0004\bu\u0010vR\u001a\u0010w\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bx\u0010\u0015\"\u0004\by\u0010\u0017R\u001a\u0010z\u001a\u00020MX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b{\u0010O\"\u0004\b|\u0010QR\u001d\u0010}\u001a\u00020~X\u000e¢\u0006\u0011\n\u0000\u001a\u0005\b\u0010\u0001\"\u0006\b\u0001\u0010\u0001R\"\u0010\u0001\u001a\u0005\u0018\u00010\u0001X\u000e¢\u0006\u0012\n\u0000\u001a\u0006\b\u0001\u0010\u0001\"\u0006\b\u0001\u0010\u0001R\u001d\u0010\u0001\u001a\u00020\u0013X\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u0001\u0010\u0015\"\u0005\b\u0001\u0010\u0017R\"\u0010\u0001\u001a\u0005\u0018\u00010\u0001X\u000e¢\u0006\u0012\n\u0000\u001a\u0006\b\u0001\u0010\u0001\"\u0006\b\u0001\u0010\u0001¨\u0006«\u0001"}, d2 = {"Lokhttp3/OkHttpClient$Builder;", "", "okHttpClient", "Lokhttp3/OkHttpClient;", "(Lokhttp3/OkHttpClient;)V", "()V", "authenticator", "Lokhttp3/Authenticator;", "getAuthenticator$okhttp", "()Lokhttp3/Authenticator;", "setAuthenticator$okhttp", "(Lokhttp3/Authenticator;)V", "cache", "Lokhttp3/Cache;", "getCache$okhttp", "()Lokhttp3/Cache;", "setCache$okhttp", "(Lokhttp3/Cache;)V", "callTimeout", "", "getCallTimeout$okhttp", "()I", "setCallTimeout$okhttp", "(I)V", "certificateChainCleaner", "Lokhttp3/internal/tls/CertificateChainCleaner;", "getCertificateChainCleaner$okhttp", "()Lokhttp3/internal/tls/CertificateChainCleaner;", "setCertificateChainCleaner$okhttp", "(Lokhttp3/internal/tls/CertificateChainCleaner;)V", "certificatePinner", "Lokhttp3/CertificatePinner;", "getCertificatePinner$okhttp", "()Lokhttp3/CertificatePinner;", "setCertificatePinner$okhttp", "(Lokhttp3/CertificatePinner;)V", "connectTimeout", "getConnectTimeout$okhttp", "setConnectTimeout$okhttp", "connectionPool", "Lokhttp3/ConnectionPool;", "getConnectionPool$okhttp", "()Lokhttp3/ConnectionPool;", "setConnectionPool$okhttp", "(Lokhttp3/ConnectionPool;)V", "connectionSpecs", "", "Lokhttp3/ConnectionSpec;", "getConnectionSpecs$okhttp", "()Ljava/util/List;", "setConnectionSpecs$okhttp", "(Ljava/util/List;)V", "cookieJar", "Lokhttp3/CookieJar;", "getCookieJar$okhttp", "()Lokhttp3/CookieJar;", "setCookieJar$okhttp", "(Lokhttp3/CookieJar;)V", "dispatcher", "Lokhttp3/Dispatcher;", "getDispatcher$okhttp", "()Lokhttp3/Dispatcher;", "setDispatcher$okhttp", "(Lokhttp3/Dispatcher;)V", "dns", "Lokhttp3/Dns;", "getDns$okhttp", "()Lokhttp3/Dns;", "setDns$okhttp", "(Lokhttp3/Dns;)V", "eventListenerFactory", "Lokhttp3/EventListener$Factory;", "getEventListenerFactory$okhttp", "()Lokhttp3/EventListener$Factory;", "setEventListenerFactory$okhttp", "(Lokhttp3/EventListener$Factory;)V", "followRedirects", "", "getFollowRedirects$okhttp", "()Z", "setFollowRedirects$okhttp", "(Z)V", "followSslRedirects", "getFollowSslRedirects$okhttp", "setFollowSslRedirects$okhttp", "hostnameVerifier", "Ljavax/net/ssl/HostnameVerifier;", "getHostnameVerifier$okhttp", "()Ljavax/net/ssl/HostnameVerifier;", "setHostnameVerifier$okhttp", "(Ljavax/net/ssl/HostnameVerifier;)V", "interceptors", "", "Lokhttp3/Interceptor;", "getInterceptors$okhttp", "networkInterceptors", "getNetworkInterceptors$okhttp", "pingInterval", "getPingInterval$okhttp", "setPingInterval$okhttp", "protocols", "Lokhttp3/Protocol;", "getProtocols$okhttp", "setProtocols$okhttp", "proxy", "Ljava/net/Proxy;", "getProxy$okhttp", "()Ljava/net/Proxy;", "setProxy$okhttp", "(Ljava/net/Proxy;)V", "proxyAuthenticator", "getProxyAuthenticator$okhttp", "setProxyAuthenticator$okhttp", "proxySelector", "Ljava/net/ProxySelector;", "getProxySelector$okhttp", "()Ljava/net/ProxySelector;", "setProxySelector$okhttp", "(Ljava/net/ProxySelector;)V", "readTimeout", "getReadTimeout$okhttp", "setReadTimeout$okhttp", "retryOnConnectionFailure", "getRetryOnConnectionFailure$okhttp", "setRetryOnConnectionFailure$okhttp", "socketFactory", "Ljavax/net/SocketFactory;", "getSocketFactory$okhttp", "()Ljavax/net/SocketFactory;", "setSocketFactory$okhttp", "(Ljavax/net/SocketFactory;)V", "sslSocketFactoryOrNull", "Ljavax/net/ssl/SSLSocketFactory;", "getSslSocketFactoryOrNull$okhttp", "()Ljavax/net/ssl/SSLSocketFactory;", "setSslSocketFactoryOrNull$okhttp", "(Ljavax/net/ssl/SSLSocketFactory;)V", "writeTimeout", "getWriteTimeout$okhttp", "setWriteTimeout$okhttp", "x509TrustManagerOrNull", "Ljavax/net/ssl/X509TrustManager;", "getX509TrustManagerOrNull$okhttp", "()Ljavax/net/ssl/X509TrustManager;", "setX509TrustManagerOrNull$okhttp", "(Ljavax/net/ssl/X509TrustManager;)V", "addInterceptor", "block", "Lkotlin/Function1;", "Lokhttp3/Interceptor$Chain;", "Lkotlin/ParameterName;", "name", "chain", "Lokhttp3/Response;", "-addInterceptor", "interceptor", "addNetworkInterceptor", "-addNetworkInterceptor", "build", "duration", "Ljava/time/Duration;", "timeout", "", "unit", "Ljava/util/concurrent/TimeUnit;", "eventListener", "Lokhttp3/EventListener;", "followProtocolRedirects", "interval", "sslSocketFactory", "trustManager", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: OkHttpClient.kt */
    public static final class Builder {
        private Authenticator authenticator;
        private Cache cache;
        private int callTimeout;
        private CertificateChainCleaner certificateChainCleaner;
        private CertificatePinner certificatePinner;
        private int connectTimeout;
        private ConnectionPool connectionPool;
        private List<ConnectionSpec> connectionSpecs;
        private CookieJar cookieJar;
        private Dispatcher dispatcher;
        private Dns dns;
        private EventListener.Factory eventListenerFactory;
        private boolean followRedirects;
        private boolean followSslRedirects;
        private HostnameVerifier hostnameVerifier;
        private final List<Interceptor> interceptors;
        private final List<Interceptor> networkInterceptors;
        private int pingInterval;
        private List<? extends Protocol> protocols;
        private Proxy proxy;
        private Authenticator proxyAuthenticator;
        private ProxySelector proxySelector;
        private int readTimeout;
        private boolean retryOnConnectionFailure;
        private SocketFactory socketFactory;
        private SSLSocketFactory sslSocketFactoryOrNull;
        private int writeTimeout;
        private X509TrustManager x509TrustManagerOrNull;

        public Builder() {
            this.dispatcher = new Dispatcher();
            this.connectionPool = new ConnectionPool();
            this.interceptors = new ArrayList();
            this.networkInterceptors = new ArrayList();
            this.eventListenerFactory = Util.asFactory(EventListener.NONE);
            this.retryOnConnectionFailure = true;
            this.authenticator = Authenticator.NONE;
            this.followRedirects = true;
            this.followSslRedirects = true;
            this.cookieJar = CookieJar.NO_COOKIES;
            this.dns = Dns.SYSTEM;
            this.proxyAuthenticator = Authenticator.NONE;
            SocketFactory socketFactory2 = SocketFactory.getDefault();
            Intrinsics.checkExpressionValueIsNotNull(socketFactory2, "SocketFactory.getDefault()");
            this.socketFactory = socketFactory2;
            this.connectionSpecs = OkHttpClient.Companion.getDEFAULT_CONNECTION_SPECS$okhttp();
            this.protocols = OkHttpClient.Companion.getDEFAULT_PROTOCOLS$okhttp();
            this.hostnameVerifier = OkHostnameVerifier.INSTANCE;
            this.certificatePinner = CertificatePinner.DEFAULT;
            this.connectTimeout = 10000;
            this.readTimeout = 10000;
            this.writeTimeout = 10000;
        }

        public final Dispatcher getDispatcher$okhttp() {
            return this.dispatcher;
        }

        public final void setDispatcher$okhttp(Dispatcher dispatcher2) {
            Intrinsics.checkParameterIsNotNull(dispatcher2, "<set-?>");
            this.dispatcher = dispatcher2;
        }

        public final ConnectionPool getConnectionPool$okhttp() {
            return this.connectionPool;
        }

        public final void setConnectionPool$okhttp(ConnectionPool connectionPool2) {
            Intrinsics.checkParameterIsNotNull(connectionPool2, "<set-?>");
            this.connectionPool = connectionPool2;
        }

        public final List<Interceptor> getInterceptors$okhttp() {
            return this.interceptors;
        }

        public final List<Interceptor> getNetworkInterceptors$okhttp() {
            return this.networkInterceptors;
        }

        public final EventListener.Factory getEventListenerFactory$okhttp() {
            return this.eventListenerFactory;
        }

        public final void setEventListenerFactory$okhttp(EventListener.Factory factory) {
            Intrinsics.checkParameterIsNotNull(factory, "<set-?>");
            this.eventListenerFactory = factory;
        }

        public final boolean getRetryOnConnectionFailure$okhttp() {
            return this.retryOnConnectionFailure;
        }

        public final void setRetryOnConnectionFailure$okhttp(boolean z) {
            this.retryOnConnectionFailure = z;
        }

        public final Authenticator getAuthenticator$okhttp() {
            return this.authenticator;
        }

        public final void setAuthenticator$okhttp(Authenticator authenticator2) {
            Intrinsics.checkParameterIsNotNull(authenticator2, "<set-?>");
            this.authenticator = authenticator2;
        }

        public final boolean getFollowRedirects$okhttp() {
            return this.followRedirects;
        }

        public final void setFollowRedirects$okhttp(boolean z) {
            this.followRedirects = z;
        }

        public final boolean getFollowSslRedirects$okhttp() {
            return this.followSslRedirects;
        }

        public final void setFollowSslRedirects$okhttp(boolean z) {
            this.followSslRedirects = z;
        }

        public final CookieJar getCookieJar$okhttp() {
            return this.cookieJar;
        }

        public final void setCookieJar$okhttp(CookieJar cookieJar2) {
            Intrinsics.checkParameterIsNotNull(cookieJar2, "<set-?>");
            this.cookieJar = cookieJar2;
        }

        public final Cache getCache$okhttp() {
            return this.cache;
        }

        public final void setCache$okhttp(Cache cache2) {
            this.cache = cache2;
        }

        public final Dns getDns$okhttp() {
            return this.dns;
        }

        public final void setDns$okhttp(Dns dns2) {
            Intrinsics.checkParameterIsNotNull(dns2, "<set-?>");
            this.dns = dns2;
        }

        public final Proxy getProxy$okhttp() {
            return this.proxy;
        }

        public final void setProxy$okhttp(Proxy proxy2) {
            this.proxy = proxy2;
        }

        public final ProxySelector getProxySelector$okhttp() {
            return this.proxySelector;
        }

        public final void setProxySelector$okhttp(ProxySelector proxySelector2) {
            this.proxySelector = proxySelector2;
        }

        public final Authenticator getProxyAuthenticator$okhttp() {
            return this.proxyAuthenticator;
        }

        public final void setProxyAuthenticator$okhttp(Authenticator authenticator2) {
            Intrinsics.checkParameterIsNotNull(authenticator2, "<set-?>");
            this.proxyAuthenticator = authenticator2;
        }

        public final SocketFactory getSocketFactory$okhttp() {
            return this.socketFactory;
        }

        public final void setSocketFactory$okhttp(SocketFactory socketFactory2) {
            Intrinsics.checkParameterIsNotNull(socketFactory2, "<set-?>");
            this.socketFactory = socketFactory2;
        }

        public final SSLSocketFactory getSslSocketFactoryOrNull$okhttp() {
            return this.sslSocketFactoryOrNull;
        }

        public final void setSslSocketFactoryOrNull$okhttp(SSLSocketFactory sSLSocketFactory) {
            this.sslSocketFactoryOrNull = sSLSocketFactory;
        }

        public final X509TrustManager getX509TrustManagerOrNull$okhttp() {
            return this.x509TrustManagerOrNull;
        }

        public final void setX509TrustManagerOrNull$okhttp(X509TrustManager x509TrustManager) {
            this.x509TrustManagerOrNull = x509TrustManager;
        }

        public final List<ConnectionSpec> getConnectionSpecs$okhttp() {
            return this.connectionSpecs;
        }

        public final void setConnectionSpecs$okhttp(List<ConnectionSpec> list) {
            Intrinsics.checkParameterIsNotNull(list, "<set-?>");
            this.connectionSpecs = list;
        }

        public final List<Protocol> getProtocols$okhttp() {
            return this.protocols;
        }

        public final void setProtocols$okhttp(List<? extends Protocol> list) {
            Intrinsics.checkParameterIsNotNull(list, "<set-?>");
            this.protocols = list;
        }

        public final HostnameVerifier getHostnameVerifier$okhttp() {
            return this.hostnameVerifier;
        }

        public final void setHostnameVerifier$okhttp(HostnameVerifier hostnameVerifier2) {
            Intrinsics.checkParameterIsNotNull(hostnameVerifier2, "<set-?>");
            this.hostnameVerifier = hostnameVerifier2;
        }

        public final CertificatePinner getCertificatePinner$okhttp() {
            return this.certificatePinner;
        }

        public final void setCertificatePinner$okhttp(CertificatePinner certificatePinner2) {
            Intrinsics.checkParameterIsNotNull(certificatePinner2, "<set-?>");
            this.certificatePinner = certificatePinner2;
        }

        public final CertificateChainCleaner getCertificateChainCleaner$okhttp() {
            return this.certificateChainCleaner;
        }

        public final void setCertificateChainCleaner$okhttp(CertificateChainCleaner certificateChainCleaner2) {
            this.certificateChainCleaner = certificateChainCleaner2;
        }

        public final int getCallTimeout$okhttp() {
            return this.callTimeout;
        }

        public final void setCallTimeout$okhttp(int i) {
            this.callTimeout = i;
        }

        public final int getConnectTimeout$okhttp() {
            return this.connectTimeout;
        }

        public final void setConnectTimeout$okhttp(int i) {
            this.connectTimeout = i;
        }

        public final int getReadTimeout$okhttp() {
            return this.readTimeout;
        }

        public final void setReadTimeout$okhttp(int i) {
            this.readTimeout = i;
        }

        public final int getWriteTimeout$okhttp() {
            return this.writeTimeout;
        }

        public final void setWriteTimeout$okhttp(int i) {
            this.writeTimeout = i;
        }

        public final int getPingInterval$okhttp() {
            return this.pingInterval;
        }

        public final void setPingInterval$okhttp(int i) {
            this.pingInterval = i;
        }

        public Builder(OkHttpClient okHttpClient) {
            Intrinsics.checkParameterIsNotNull(okHttpClient, "okHttpClient");
            this();
            this.dispatcher = okHttpClient.dispatcher();
            this.connectionPool = okHttpClient.connectionPool();
            CollectionsKt.addAll((Collection<? super T>) this.interceptors, (Iterable<? extends T>) okHttpClient.interceptors());
            CollectionsKt.addAll((Collection<? super T>) this.networkInterceptors, (Iterable<? extends T>) okHttpClient.networkInterceptors());
            this.eventListenerFactory = okHttpClient.eventListenerFactory();
            this.retryOnConnectionFailure = okHttpClient.retryOnConnectionFailure();
            this.authenticator = okHttpClient.authenticator();
            this.followRedirects = okHttpClient.followRedirects();
            this.followSslRedirects = okHttpClient.followSslRedirects();
            this.cookieJar = okHttpClient.cookieJar();
            this.cache = okHttpClient.cache();
            this.dns = okHttpClient.dns();
            this.proxy = okHttpClient.proxy();
            this.proxySelector = okHttpClient.proxySelector();
            this.proxyAuthenticator = okHttpClient.proxyAuthenticator();
            this.socketFactory = okHttpClient.socketFactory();
            this.sslSocketFactoryOrNull = okHttpClient.sslSocketFactoryOrNull;
            this.x509TrustManagerOrNull = okHttpClient.x509TrustManager();
            this.connectionSpecs = okHttpClient.connectionSpecs();
            this.protocols = okHttpClient.protocols();
            this.hostnameVerifier = okHttpClient.hostnameVerifier();
            this.certificatePinner = okHttpClient.certificatePinner();
            this.certificateChainCleaner = okHttpClient.certificateChainCleaner();
            this.callTimeout = okHttpClient.callTimeoutMillis();
            this.connectTimeout = okHttpClient.connectTimeoutMillis();
            this.readTimeout = okHttpClient.readTimeoutMillis();
            this.writeTimeout = okHttpClient.writeTimeoutMillis();
            this.pingInterval = okHttpClient.pingIntervalMillis();
        }

        public final Builder dispatcher(Dispatcher dispatcher2) {
            Intrinsics.checkParameterIsNotNull(dispatcher2, "dispatcher");
            Builder builder = this;
            builder.dispatcher = dispatcher2;
            return builder;
        }

        public final Builder connectionPool(ConnectionPool connectionPool2) {
            Intrinsics.checkParameterIsNotNull(connectionPool2, "connectionPool");
            Builder builder = this;
            builder.connectionPool = connectionPool2;
            return builder;
        }

        public final List<Interceptor> interceptors() {
            return this.interceptors;
        }

        public final Builder addInterceptor(Interceptor interceptor) {
            Intrinsics.checkParameterIsNotNull(interceptor, "interceptor");
            Builder builder = this;
            builder.interceptors.add(interceptor);
            return builder;
        }

        /* renamed from: -addInterceptor reason: not valid java name */
        public final Builder m1088addInterceptor(Function1<? super Chain, Response> function1) {
            Intrinsics.checkParameterIsNotNull(function1, "block");
            okhttp3.Interceptor.Companion companion = Interceptor.Companion;
            return addInterceptor(new OkHttpClient$Builder$addInterceptor$$inlined$invoke$1(function1));
        }

        public final List<Interceptor> networkInterceptors() {
            return this.networkInterceptors;
        }

        public final Builder addNetworkInterceptor(Interceptor interceptor) {
            Intrinsics.checkParameterIsNotNull(interceptor, "interceptor");
            Builder builder = this;
            builder.networkInterceptors.add(interceptor);
            return builder;
        }

        /* renamed from: -addNetworkInterceptor reason: not valid java name */
        public final Builder m1089addNetworkInterceptor(Function1<? super Chain, Response> function1) {
            Intrinsics.checkParameterIsNotNull(function1, "block");
            okhttp3.Interceptor.Companion companion = Interceptor.Companion;
            return addNetworkInterceptor(new OkHttpClient$Builder$addNetworkInterceptor$$inlined$invoke$1(function1));
        }

        public final Builder eventListener(EventListener eventListener) {
            Intrinsics.checkParameterIsNotNull(eventListener, "eventListener");
            Builder builder = this;
            builder.eventListenerFactory = Util.asFactory(eventListener);
            return builder;
        }

        public final Builder eventListenerFactory(EventListener.Factory factory) {
            Intrinsics.checkParameterIsNotNull(factory, "eventListenerFactory");
            Builder builder = this;
            builder.eventListenerFactory = factory;
            return builder;
        }

        public final Builder retryOnConnectionFailure(boolean z) {
            Builder builder = this;
            builder.retryOnConnectionFailure = z;
            return builder;
        }

        public final Builder authenticator(Authenticator authenticator2) {
            Intrinsics.checkParameterIsNotNull(authenticator2, "authenticator");
            Builder builder = this;
            builder.authenticator = authenticator2;
            return builder;
        }

        public final Builder followRedirects(boolean z) {
            Builder builder = this;
            builder.followRedirects = z;
            return builder;
        }

        public final Builder followSslRedirects(boolean z) {
            Builder builder = this;
            builder.followSslRedirects = z;
            return builder;
        }

        public final Builder cookieJar(CookieJar cookieJar2) {
            Intrinsics.checkParameterIsNotNull(cookieJar2, "cookieJar");
            Builder builder = this;
            builder.cookieJar = cookieJar2;
            return builder;
        }

        public final Builder cache(Cache cache2) {
            Builder builder = this;
            builder.cache = cache2;
            return builder;
        }

        public final Builder dns(Dns dns2) {
            Intrinsics.checkParameterIsNotNull(dns2, "dns");
            Builder builder = this;
            builder.dns = dns2;
            return builder;
        }

        public final Builder proxy(Proxy proxy2) {
            Builder builder = this;
            builder.proxy = proxy2;
            return builder;
        }

        public final Builder proxySelector(ProxySelector proxySelector2) {
            Intrinsics.checkParameterIsNotNull(proxySelector2, "proxySelector");
            Builder builder = this;
            builder.proxySelector = proxySelector2;
            return builder;
        }

        public final Builder proxyAuthenticator(Authenticator authenticator2) {
            Intrinsics.checkParameterIsNotNull(authenticator2, "proxyAuthenticator");
            Builder builder = this;
            builder.proxyAuthenticator = authenticator2;
            return builder;
        }

        public final Builder socketFactory(SocketFactory socketFactory2) {
            Intrinsics.checkParameterIsNotNull(socketFactory2, "socketFactory");
            Builder builder = this;
            if (!(socketFactory2 instanceof SSLSocketFactory)) {
                builder.socketFactory = socketFactory2;
                return builder;
            }
            throw new IllegalArgumentException("socketFactory instanceof SSLSocketFactory".toString());
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "Use the sslSocketFactory overload that accepts a X509TrustManager.")
        public final Builder sslSocketFactory(SSLSocketFactory sSLSocketFactory) {
            Intrinsics.checkParameterIsNotNull(sSLSocketFactory, "sslSocketFactory");
            Builder builder = this;
            builder.sslSocketFactoryOrNull = sSLSocketFactory;
            builder.certificateChainCleaner = Platform.Companion.get().buildCertificateChainCleaner(sSLSocketFactory);
            return builder;
        }

        public final Builder sslSocketFactory(SSLSocketFactory sSLSocketFactory, X509TrustManager x509TrustManager) {
            Intrinsics.checkParameterIsNotNull(sSLSocketFactory, "sslSocketFactory");
            Intrinsics.checkParameterIsNotNull(x509TrustManager, "trustManager");
            Builder builder = this;
            builder.sslSocketFactoryOrNull = sSLSocketFactory;
            builder.certificateChainCleaner = CertificateChainCleaner.Companion.get(x509TrustManager);
            builder.x509TrustManagerOrNull = x509TrustManager;
            return builder;
        }

        public final Builder connectionSpecs(List<ConnectionSpec> list) {
            Intrinsics.checkParameterIsNotNull(list, "connectionSpecs");
            Builder builder = this;
            builder.connectionSpecs = Util.toImmutableList(list);
            return builder;
        }

        public final Builder protocols(List<? extends Protocol> list) {
            Intrinsics.checkParameterIsNotNull(list, "protocols");
            Builder builder = this;
            List mutableList = CollectionsKt.toMutableList((Collection<? extends T>) list);
            boolean z = false;
            if (mutableList.contains(Protocol.H2_PRIOR_KNOWLEDGE) || mutableList.contains(Protocol.HTTP_1_1)) {
                if (!mutableList.contains(Protocol.H2_PRIOR_KNOWLEDGE) || mutableList.size() <= 1) {
                    z = true;
                }
                if (!z) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("protocols containing h2_prior_knowledge cannot use other protocols: ");
                    sb.append(mutableList);
                    throw new IllegalArgumentException(sb.toString().toString());
                } else if (!(!mutableList.contains(Protocol.HTTP_1_0))) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("protocols must not contain http/1.0: ");
                    sb2.append(mutableList);
                    throw new IllegalArgumentException(sb2.toString().toString());
                } else if (mutableList == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.List<okhttp3.Protocol?>");
                } else if (!mutableList.contains(null)) {
                    mutableList.remove(Protocol.SPDY_3);
                    List<? extends Protocol> unmodifiableList = Collections.unmodifiableList(list);
                    Intrinsics.checkExpressionValueIsNotNull(unmodifiableList, "Collections.unmodifiableList(protocols)");
                    builder.protocols = unmodifiableList;
                    return builder;
                } else {
                    throw new IllegalArgumentException("protocols must not contain null".toString());
                }
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("protocols must contain h2_prior_knowledge or http/1.1: ");
                sb3.append(mutableList);
                throw new IllegalArgumentException(sb3.toString().toString());
            }
        }

        public final Builder hostnameVerifier(HostnameVerifier hostnameVerifier2) {
            Intrinsics.checkParameterIsNotNull(hostnameVerifier2, "hostnameVerifier");
            Builder builder = this;
            builder.hostnameVerifier = hostnameVerifier2;
            return builder;
        }

        public final Builder certificatePinner(CertificatePinner certificatePinner2) {
            Intrinsics.checkParameterIsNotNull(certificatePinner2, "certificatePinner");
            Builder builder = this;
            builder.certificatePinner = certificatePinner2;
            return builder;
        }

        public final Builder callTimeout(long j, TimeUnit timeUnit) {
            Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
            Builder builder = this;
            builder.callTimeout = Util.checkDuration("timeout", j, timeUnit);
            return builder;
        }

        public final Builder callTimeout(Duration duration) {
            Intrinsics.checkParameterIsNotNull(duration, "duration");
            Builder builder = this;
            builder.callTimeout = Util.checkDuration("timeout", duration.toMillis(), TimeUnit.MILLISECONDS);
            return builder;
        }

        public final Builder connectTimeout(long j, TimeUnit timeUnit) {
            Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
            Builder builder = this;
            builder.connectTimeout = Util.checkDuration("timeout", j, timeUnit);
            return builder;
        }

        public final Builder connectTimeout(Duration duration) {
            Intrinsics.checkParameterIsNotNull(duration, "duration");
            Builder builder = this;
            builder.connectTimeout = Util.checkDuration("timeout", duration.toMillis(), TimeUnit.MILLISECONDS);
            return builder;
        }

        public final Builder readTimeout(long j, TimeUnit timeUnit) {
            Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
            Builder builder = this;
            builder.readTimeout = Util.checkDuration("timeout", j, timeUnit);
            return builder;
        }

        public final Builder readTimeout(Duration duration) {
            Intrinsics.checkParameterIsNotNull(duration, "duration");
            Builder builder = this;
            builder.readTimeout = Util.checkDuration("timeout", duration.toMillis(), TimeUnit.MILLISECONDS);
            return builder;
        }

        public final Builder writeTimeout(long j, TimeUnit timeUnit) {
            Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
            Builder builder = this;
            builder.writeTimeout = Util.checkDuration("timeout", j, timeUnit);
            return builder;
        }

        public final Builder writeTimeout(Duration duration) {
            Intrinsics.checkParameterIsNotNull(duration, "duration");
            Builder builder = this;
            builder.writeTimeout = Util.checkDuration("timeout", duration.toMillis(), TimeUnit.MILLISECONDS);
            return builder;
        }

        public final Builder pingInterval(long j, TimeUnit timeUnit) {
            Intrinsics.checkParameterIsNotNull(timeUnit, "unit");
            Builder builder = this;
            builder.pingInterval = Util.checkDuration("interval", j, timeUnit);
            return builder;
        }

        public final Builder pingInterval(Duration duration) {
            Intrinsics.checkParameterIsNotNull(duration, "duration");
            Builder builder = this;
            builder.pingInterval = Util.checkDuration("timeout", duration.toMillis(), TimeUnit.MILLISECONDS);
            return builder;
        }

        public final OkHttpClient build() {
            return new OkHttpClient(this);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002R\u001a\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0007¨\u0006\u000f"}, d2 = {"Lokhttp3/OkHttpClient$Companion;", "", "()V", "DEFAULT_CONNECTION_SPECS", "", "Lokhttp3/ConnectionSpec;", "getDEFAULT_CONNECTION_SPECS$okhttp", "()Ljava/util/List;", "DEFAULT_PROTOCOLS", "Lokhttp3/Protocol;", "getDEFAULT_PROTOCOLS$okhttp", "newSslSocketFactory", "Ljavax/net/ssl/SSLSocketFactory;", "trustManager", "Ljavax/net/ssl/X509TrustManager;", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: OkHttpClient.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final List<Protocol> getDEFAULT_PROTOCOLS$okhttp() {
            return OkHttpClient.DEFAULT_PROTOCOLS;
        }

        public final List<ConnectionSpec> getDEFAULT_CONNECTION_SPECS$okhttp() {
            return OkHttpClient.DEFAULT_CONNECTION_SPECS;
        }

        /* access modifiers changed from: private */
        public final SSLSocketFactory newSslSocketFactory(X509TrustManager x509TrustManager) {
            try {
                SSLContext newSSLContext = Platform.Companion.get().newSSLContext();
                newSSLContext.init(null, new TrustManager[]{x509TrustManager}, null);
                SSLSocketFactory socketFactory = newSSLContext.getSocketFactory();
                Intrinsics.checkExpressionValueIsNotNull(socketFactory, "sslContext.socketFactory");
                return socketFactory;
            } catch (GeneralSecurityException e) {
                throw new AssertionError("No System TLS", e);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0139  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0156  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x01ae  */
    public OkHttpClient(Builder builder) {
        ProxySelector proxySelector2;
        List<Interceptor> list;
        boolean z;
        Intrinsics.checkParameterIsNotNull(builder, "builder");
        this.dispatcher = builder.getDispatcher$okhttp();
        this.connectionPool = builder.getConnectionPool$okhttp();
        this.interceptors = Util.toImmutableList(builder.getInterceptors$okhttp());
        this.networkInterceptors = Util.toImmutableList(builder.getNetworkInterceptors$okhttp());
        this.eventListenerFactory = builder.getEventListenerFactory$okhttp();
        this.retryOnConnectionFailure = builder.getRetryOnConnectionFailure$okhttp();
        this.authenticator = builder.getAuthenticator$okhttp();
        this.followRedirects = builder.getFollowRedirects$okhttp();
        this.followSslRedirects = builder.getFollowSslRedirects$okhttp();
        this.cookieJar = builder.getCookieJar$okhttp();
        this.cache = builder.getCache$okhttp();
        this.dns = builder.getDns$okhttp();
        this.proxy = builder.getProxy$okhttp();
        if (builder.getProxy$okhttp() != null) {
            proxySelector2 = NullProxySelector.INSTANCE;
        } else {
            proxySelector2 = builder.getProxySelector$okhttp();
            if (proxySelector2 == null) {
                proxySelector2 = ProxySelector.getDefault();
            }
            if (proxySelector2 == null) {
                proxySelector2 = NullProxySelector.INSTANCE;
            }
        }
        this.proxySelector = proxySelector2;
        this.proxyAuthenticator = builder.getProxyAuthenticator$okhttp();
        this.socketFactory = builder.getSocketFactory$okhttp();
        this.connectionSpecs = builder.getConnectionSpecs$okhttp();
        this.protocols = builder.getProtocols$okhttp();
        this.hostnameVerifier = builder.getHostnameVerifier$okhttp();
        this.callTimeoutMillis = builder.getCallTimeout$okhttp();
        this.connectTimeoutMillis = builder.getConnectTimeout$okhttp();
        this.readTimeoutMillis = builder.getReadTimeout$okhttp();
        this.writeTimeoutMillis = builder.getWriteTimeout$okhttp();
        this.pingIntervalMillis = builder.getPingInterval$okhttp();
        if (builder.getSslSocketFactoryOrNull$okhttp() == null) {
            Iterable iterable = this.connectionSpecs;
            if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
                Iterator it = iterable.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (((ConnectionSpec) it.next()).isTls()) {
                            z = false;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            z = true;
            if (!z) {
                this.x509TrustManager = Platform.Companion.get().platformTrustManager();
                Platform.Companion.get().configureTrustManager(this.x509TrustManager);
                Companion companion = Companion;
                X509TrustManager x509TrustManager2 = this.x509TrustManager;
                if (x509TrustManager2 == null) {
                    Intrinsics.throwNpe();
                }
                this.sslSocketFactoryOrNull = companion.newSslSocketFactory(x509TrustManager2);
                okhttp3.internal.tls.CertificateChainCleaner.Companion companion2 = CertificateChainCleaner.Companion;
                X509TrustManager x509TrustManager3 = this.x509TrustManager;
                if (x509TrustManager3 == null) {
                    Intrinsics.throwNpe();
                }
                this.certificateChainCleaner = companion2.get(x509TrustManager3);
                if (this.sslSocketFactoryOrNull != null) {
                    Platform.Companion.get().configureSslSocketFactory(this.sslSocketFactoryOrNull);
                }
                this.certificatePinner = builder.getCertificatePinner$okhttp().withCertificateChainCleaner$okhttp(this.certificateChainCleaner);
                list = this.interceptors;
                String str = "null cannot be cast to non-null type kotlin.collections.List<okhttp3.Interceptor?>";
                if (list != null) {
                    throw new TypeCastException(str);
                } else if (!list.contains(null)) {
                    List<Interceptor> list2 = this.networkInterceptors;
                    if (list2 == null) {
                        throw new TypeCastException(str);
                    } else if (!(!list2.contains(null))) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Null network interceptor: ");
                        sb.append(this.networkInterceptors);
                        throw new IllegalStateException(sb.toString().toString());
                    } else {
                        return;
                    }
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Null interceptor: ");
                    sb2.append(this.interceptors);
                    throw new IllegalStateException(sb2.toString().toString());
                }
            }
        }
        this.sslSocketFactoryOrNull = builder.getSslSocketFactoryOrNull$okhttp();
        this.certificateChainCleaner = builder.getCertificateChainCleaner$okhttp();
        this.x509TrustManager = builder.getX509TrustManagerOrNull$okhttp();
        if (this.sslSocketFactoryOrNull != null) {
        }
        this.certificatePinner = builder.getCertificatePinner$okhttp().withCertificateChainCleaner$okhttp(this.certificateChainCleaner);
        list = this.interceptors;
        String str2 = "null cannot be cast to non-null type kotlin.collections.List<okhttp3.Interceptor?>";
        if (list != null) {
        }
    }

    public Object clone() {
        return super.clone();
    }

    public final Dispatcher dispatcher() {
        return this.dispatcher;
    }

    public final ConnectionPool connectionPool() {
        return this.connectionPool;
    }

    public final List<Interceptor> interceptors() {
        return this.interceptors;
    }

    public final List<Interceptor> networkInterceptors() {
        return this.networkInterceptors;
    }

    public final EventListener.Factory eventListenerFactory() {
        return this.eventListenerFactory;
    }

    public final boolean retryOnConnectionFailure() {
        return this.retryOnConnectionFailure;
    }

    public final Authenticator authenticator() {
        return this.authenticator;
    }

    public final boolean followRedirects() {
        return this.followRedirects;
    }

    public final boolean followSslRedirects() {
        return this.followSslRedirects;
    }

    public final CookieJar cookieJar() {
        return this.cookieJar;
    }

    public final Cache cache() {
        return this.cache;
    }

    public final Dns dns() {
        return this.dns;
    }

    public final Proxy proxy() {
        return this.proxy;
    }

    public final ProxySelector proxySelector() {
        return this.proxySelector;
    }

    public final Authenticator proxyAuthenticator() {
        return this.proxyAuthenticator;
    }

    public final SocketFactory socketFactory() {
        return this.socketFactory;
    }

    public final SSLSocketFactory sslSocketFactory() {
        SSLSocketFactory sSLSocketFactory = this.sslSocketFactoryOrNull;
        if (sSLSocketFactory != null) {
            return sSLSocketFactory;
        }
        throw new IllegalStateException("CLEARTEXT-only client");
    }

    public final X509TrustManager x509TrustManager() {
        return this.x509TrustManager;
    }

    public final List<ConnectionSpec> connectionSpecs() {
        return this.connectionSpecs;
    }

    public final List<Protocol> protocols() {
        return this.protocols;
    }

    public final HostnameVerifier hostnameVerifier() {
        return this.hostnameVerifier;
    }

    public final CertificatePinner certificatePinner() {
        return this.certificatePinner;
    }

    public final CertificateChainCleaner certificateChainCleaner() {
        return this.certificateChainCleaner;
    }

    public final int callTimeoutMillis() {
        return this.callTimeoutMillis;
    }

    public final int connectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }

    public final int readTimeoutMillis() {
        return this.readTimeoutMillis;
    }

    public final int writeTimeoutMillis() {
        return this.writeTimeoutMillis;
    }

    public final int pingIntervalMillis() {
        return this.pingIntervalMillis;
    }

    public OkHttpClient() {
        this(new Builder());
    }

    public Call newCall(Request request) {
        Intrinsics.checkParameterIsNotNull(request, "request");
        return RealCall.Companion.newRealCall(this, request, false);
    }

    public WebSocket newWebSocket(Request request, WebSocketListener webSocketListener) {
        Intrinsics.checkParameterIsNotNull(request, "request");
        Intrinsics.checkParameterIsNotNull(webSocketListener, CastExtraArgs.LISTENER);
        RealWebSocket realWebSocket = new RealWebSocket(request, webSocketListener, new Random(), (long) this.pingIntervalMillis);
        realWebSocket.connect(this);
        return realWebSocket;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "dispatcher", imports = {}))
    /* renamed from: -deprecated_dispatcher reason: not valid java name */
    public final Dispatcher m1070deprecated_dispatcher() {
        return this.dispatcher;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "connectionPool", imports = {}))
    /* renamed from: -deprecated_connectionPool reason: not valid java name */
    public final ConnectionPool m1067deprecated_connectionPool() {
        return this.connectionPool;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "interceptors", imports = {}))
    /* renamed from: -deprecated_interceptors reason: not valid java name */
    public final List<Interceptor> m1076deprecated_interceptors() {
        return this.interceptors;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "networkInterceptors", imports = {}))
    /* renamed from: -deprecated_networkInterceptors reason: not valid java name */
    public final List<Interceptor> m1077deprecated_networkInterceptors() {
        return this.networkInterceptors;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "eventListenerFactory", imports = {}))
    /* renamed from: -deprecated_eventListenerFactory reason: not valid java name */
    public final EventListener.Factory m1072deprecated_eventListenerFactory() {
        return this.eventListenerFactory;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "retryOnConnectionFailure", imports = {}))
    /* renamed from: -deprecated_retryOnConnectionFailure reason: not valid java name */
    public final boolean m1084deprecated_retryOnConnectionFailure() {
        return this.retryOnConnectionFailure;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "authenticator", imports = {}))
    /* renamed from: -deprecated_authenticator reason: not valid java name */
    public final Authenticator m1062deprecated_authenticator() {
        return this.authenticator;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "followRedirects", imports = {}))
    /* renamed from: -deprecated_followRedirects reason: not valid java name */
    public final boolean m1073deprecated_followRedirects() {
        return this.followRedirects;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "followSslRedirects", imports = {}))
    /* renamed from: -deprecated_followSslRedirects reason: not valid java name */
    public final boolean m1074deprecated_followSslRedirects() {
        return this.followSslRedirects;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "cookieJar", imports = {}))
    /* renamed from: -deprecated_cookieJar reason: not valid java name */
    public final CookieJar m1069deprecated_cookieJar() {
        return this.cookieJar;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "cache", imports = {}))
    /* renamed from: -deprecated_cache reason: not valid java name */
    public final Cache m1063deprecated_cache() {
        return this.cache;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "dns", imports = {}))
    /* renamed from: -deprecated_dns reason: not valid java name */
    public final Dns m1071deprecated_dns() {
        return this.dns;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "proxy", imports = {}))
    /* renamed from: -deprecated_proxy reason: not valid java name */
    public final Proxy m1080deprecated_proxy() {
        return this.proxy;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "proxySelector", imports = {}))
    /* renamed from: -deprecated_proxySelector reason: not valid java name */
    public final ProxySelector m1082deprecated_proxySelector() {
        return this.proxySelector;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "proxyAuthenticator", imports = {}))
    /* renamed from: -deprecated_proxyAuthenticator reason: not valid java name */
    public final Authenticator m1081deprecated_proxyAuthenticator() {
        return this.proxyAuthenticator;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "socketFactory", imports = {}))
    /* renamed from: -deprecated_socketFactory reason: not valid java name */
    public final SocketFactory m1085deprecated_socketFactory() {
        return this.socketFactory;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "sslSocketFactory", imports = {}))
    /* renamed from: -deprecated_sslSocketFactory reason: not valid java name */
    public final SSLSocketFactory m1086deprecated_sslSocketFactory() {
        return sslSocketFactory();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "connectionSpecs", imports = {}))
    /* renamed from: -deprecated_connectionSpecs reason: not valid java name */
    public final List<ConnectionSpec> m1068deprecated_connectionSpecs() {
        return this.connectionSpecs;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "protocols", imports = {}))
    /* renamed from: -deprecated_protocols reason: not valid java name */
    public final List<Protocol> m1079deprecated_protocols() {
        return this.protocols;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "hostnameVerifier", imports = {}))
    /* renamed from: -deprecated_hostnameVerifier reason: not valid java name */
    public final HostnameVerifier m1075deprecated_hostnameVerifier() {
        return this.hostnameVerifier;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "certificatePinner", imports = {}))
    /* renamed from: -deprecated_certificatePinner reason: not valid java name */
    public final CertificatePinner m1065deprecated_certificatePinner() {
        return this.certificatePinner;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "callTimeoutMillis", imports = {}))
    /* renamed from: -deprecated_callTimeoutMillis reason: not valid java name */
    public final int m1064deprecated_callTimeoutMillis() {
        return this.callTimeoutMillis;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "connectTimeoutMillis", imports = {}))
    /* renamed from: -deprecated_connectTimeoutMillis reason: not valid java name */
    public final int m1066deprecated_connectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "readTimeoutMillis", imports = {}))
    /* renamed from: -deprecated_readTimeoutMillis reason: not valid java name */
    public final int m1083deprecated_readTimeoutMillis() {
        return this.readTimeoutMillis;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "writeTimeoutMillis", imports = {}))
    /* renamed from: -deprecated_writeTimeoutMillis reason: not valid java name */
    public final int m1087deprecated_writeTimeoutMillis() {
        return this.writeTimeoutMillis;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "pingIntervalMillis", imports = {}))
    /* renamed from: -deprecated_pingIntervalMillis reason: not valid java name */
    public final int m1078deprecated_pingIntervalMillis() {
        return this.pingIntervalMillis;
    }
}
