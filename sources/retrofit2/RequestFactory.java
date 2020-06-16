package retrofit2;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import kotlin.coroutines.Continuation;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.OPTIONS;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.QueryName;
import retrofit2.http.Tag;
import retrofit2.http.Url;

final class RequestFactory {
    private final HttpUrl baseUrl;
    @Nullable
    private final MediaType contentType;
    private final boolean hasBody;
    @Nullable
    private final Headers headers;
    final String httpMethod;
    private final boolean isFormEncoded;
    final boolean isKotlinSuspendFunction;
    private final boolean isMultipart;
    private final Method method;
    private final ParameterHandler<?>[] parameterHandlers;
    @Nullable
    private final String relativeUrl;

    static final class Builder {
        private static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
        private static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);
        private static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{([a-zA-Z][a-zA-Z0-9_-]*)\\}");
        @Nullable
        MediaType contentType;
        boolean gotBody;
        boolean gotField;
        boolean gotPart;
        boolean gotPath;
        boolean gotQuery;
        boolean gotQueryMap;
        boolean gotQueryName;
        boolean gotUrl;
        boolean hasBody;
        @Nullable
        Headers headers;
        @Nullable
        String httpMethod;
        boolean isFormEncoded;
        boolean isKotlinSuspendFunction;
        boolean isMultipart;
        final Method method;
        final Annotation[] methodAnnotations;
        final Annotation[][] parameterAnnotationsArray;
        @Nullable
        ParameterHandler<?>[] parameterHandlers;
        final Type[] parameterTypes;
        @Nullable
        String relativeUrl;
        @Nullable
        Set<String> relativeUrlParamNames;
        final Retrofit retrofit;

        Builder(Retrofit retrofit3, Method method2) {
            this.retrofit = retrofit3;
            this.method = method2;
            this.methodAnnotations = method2.getAnnotations();
            this.parameterTypes = method2.getGenericParameterTypes();
            this.parameterAnnotationsArray = method2.getParameterAnnotations();
        }

        /* access modifiers changed from: 0000 */
        public RequestFactory build() {
            for (Annotation parseMethodAnnotation : this.methodAnnotations) {
                parseMethodAnnotation(parseMethodAnnotation);
            }
            if (this.httpMethod != null) {
                if (!this.hasBody) {
                    if (this.isMultipart) {
                        throw Utils.methodError(this.method, "Multipart can only be specified on HTTP methods with request body (e.g., @POST).", new Object[0]);
                    } else if (this.isFormEncoded) {
                        throw Utils.methodError(this.method, "FormUrlEncoded can only be specified on HTTP methods with request body (e.g., @POST).", new Object[0]);
                    }
                }
                int length = this.parameterAnnotationsArray.length;
                this.parameterHandlers = new ParameterHandler[length];
                int i = length - 1;
                int i2 = 0;
                while (true) {
                    boolean z = true;
                    if (i2 >= length) {
                        break;
                    }
                    ParameterHandler<?>[] parameterHandlerArr = this.parameterHandlers;
                    Type type = this.parameterTypes[i2];
                    Annotation[] annotationArr = this.parameterAnnotationsArray[i2];
                    if (i2 != i) {
                        z = false;
                    }
                    parameterHandlerArr[i2] = parseParameter(i2, type, annotationArr, z);
                    i2++;
                }
                if (this.relativeUrl == null && !this.gotUrl) {
                    throw Utils.methodError(this.method, "Missing either @%s URL or @Url parameter.", this.httpMethod);
                } else if (!this.isFormEncoded && !this.isMultipart && !this.hasBody && this.gotBody) {
                    throw Utils.methodError(this.method, "Non-body HTTP method cannot contain @Body.", new Object[0]);
                } else if (this.isFormEncoded && !this.gotField) {
                    throw Utils.methodError(this.method, "Form-encoded method must contain at least one @Field.", new Object[0]);
                } else if (!this.isMultipart || this.gotPart) {
                    return new RequestFactory(this);
                } else {
                    throw Utils.methodError(this.method, "Multipart method must contain at least one @Part.", new Object[0]);
                }
            } else {
                throw Utils.methodError(this.method, "HTTP method annotation is required (e.g., @GET, @POST, etc.).", new Object[0]);
            }
        }

        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof DELETE) {
                parseHttpMethodAndPath("DELETE", ((DELETE) annotation).value(), false);
            } else if (annotation instanceof GET) {
                parseHttpMethodAndPath("GET", ((GET) annotation).value(), false);
            } else if (annotation instanceof HEAD) {
                parseHttpMethodAndPath("HEAD", ((HEAD) annotation).value(), false);
            } else if (annotation instanceof PATCH) {
                parseHttpMethodAndPath("PATCH", ((PATCH) annotation).value(), true);
            } else if (annotation instanceof POST) {
                parseHttpMethodAndPath("POST", ((POST) annotation).value(), true);
            } else if (annotation instanceof PUT) {
                parseHttpMethodAndPath("PUT", ((PUT) annotation).value(), true);
            } else if (annotation instanceof OPTIONS) {
                parseHttpMethodAndPath("OPTIONS", ((OPTIONS) annotation).value(), false);
            } else if (annotation instanceof HTTP) {
                HTTP http = (HTTP) annotation;
                parseHttpMethodAndPath(http.method(), http.path(), http.hasBody());
            } else if (annotation instanceof retrofit2.http.Headers) {
                String[] value = ((retrofit2.http.Headers) annotation).value();
                if (value.length != 0) {
                    this.headers = parseHeaders(value);
                } else {
                    throw Utils.methodError(this.method, "@Headers annotation is empty.", new Object[0]);
                }
            } else {
                String str = "Only one encoding annotation is allowed.";
                if (annotation instanceof Multipart) {
                    if (!this.isFormEncoded) {
                        this.isMultipart = true;
                        return;
                    }
                    throw Utils.methodError(this.method, str, new Object[0]);
                } else if (!(annotation instanceof FormUrlEncoded)) {
                } else {
                    if (!this.isMultipart) {
                        this.isFormEncoded = true;
                        return;
                    }
                    throw Utils.methodError(this.method, str, new Object[0]);
                }
            }
        }

        private void parseHttpMethodAndPath(String str, String str2, boolean z) {
            String str3 = this.httpMethod;
            if (str3 == null) {
                this.httpMethod = str;
                this.hasBody = z;
                if (!str2.isEmpty()) {
                    int indexOf = str2.indexOf(63);
                    if (indexOf != -1 && indexOf < str2.length() - 1) {
                        String substring = str2.substring(indexOf + 1);
                        if (PARAM_URL_REGEX.matcher(substring).find()) {
                            throw Utils.methodError(this.method, "URL query string \"%s\" must not have replace block. For dynamic query parameters use @Query.", substring);
                        }
                    }
                    this.relativeUrl = str2;
                    this.relativeUrlParamNames = parsePathParameters(str2);
                    return;
                }
                return;
            }
            throw Utils.methodError(this.method, "Only one HTTP method is allowed. Found: %s and %s.", str3, str);
        }

        private Headers parseHeaders(String[] strArr) {
            okhttp3.Headers.Builder builder = new okhttp3.Headers.Builder();
            for (String str : strArr) {
                int indexOf = str.indexOf(58);
                if (indexOf == -1 || indexOf == 0 || indexOf == str.length() - 1) {
                    throw Utils.methodError(this.method, "@Headers value must be in the form \"Name: Value\". Found: \"%s\"", str);
                }
                String substring = str.substring(0, indexOf);
                String trim = str.substring(indexOf + 1).trim();
                if ("Content-Type".equalsIgnoreCase(substring)) {
                    try {
                        this.contentType = MediaType.get(trim);
                    } catch (IllegalArgumentException e) {
                        throw Utils.methodError(this.method, e, "Malformed content type: %s", trim);
                    }
                } else {
                    builder.add(substring, trim);
                }
            }
            return builder.build();
        }

        @Nullable
        private ParameterHandler<?> parseParameter(int i, Type type, @Nullable Annotation[] annotationArr, boolean z) {
            ParameterHandler<?> parameterHandler;
            if (annotationArr != null) {
                parameterHandler = null;
                for (Annotation parseParameterAnnotation : annotationArr) {
                    ParameterHandler<?> parseParameterAnnotation2 = parseParameterAnnotation(i, type, annotationArr, parseParameterAnnotation);
                    if (parseParameterAnnotation2 != null) {
                        if (parameterHandler == null) {
                            parameterHandler = parseParameterAnnotation2;
                        } else {
                            throw Utils.parameterError(this.method, i, "Multiple Retrofit annotations found, only one allowed.", new Object[0]);
                        }
                    }
                }
            } else {
                parameterHandler = null;
            }
            if (parameterHandler != null) {
                return parameterHandler;
            }
            if (z) {
                try {
                    if (Utils.getRawType(type) == Continuation.class) {
                        this.isKotlinSuspendFunction = true;
                        return null;
                    }
                } catch (NoClassDefFoundError unused) {
                }
            }
            throw Utils.parameterError(this.method, i, "No Retrofit annotation found.", new Object[0]);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0041, code lost:
            if ("android.net.Uri".equals(((java.lang.Class) r11).getName()) != false) goto L_0x004f;
         */
        @Nullable
        private ParameterHandler<?> parseParameterAnnotation(int i, Type type, Annotation[] annotationArr, Annotation annotation) {
            String str = "@Path parameters may not be used with @Url.";
            if (annotation instanceof Url) {
                validateResolvableType(i, type);
                if (this.gotUrl) {
                    throw Utils.parameterError(this.method, i, "Multiple @Url method annotations found.", new Object[0]);
                } else if (this.gotPath) {
                    throw Utils.parameterError(this.method, i, str, new Object[0]);
                } else if (this.gotQuery) {
                    throw Utils.parameterError(this.method, i, "A @Url parameter must not come after a @Query.", new Object[0]);
                } else if (this.gotQueryName) {
                    throw Utils.parameterError(this.method, i, "A @Url parameter must not come after a @QueryName.", new Object[0]);
                } else if (this.gotQueryMap) {
                    throw Utils.parameterError(this.method, i, "A @Url parameter must not come after a @QueryMap.", new Object[0]);
                } else if (this.relativeUrl == null) {
                    this.gotUrl = true;
                    if (!(type == HttpUrl.class || type == String.class || type == URI.class)) {
                        if (type instanceof Class) {
                        }
                        throw Utils.parameterError(this.method, i, "@Url must be okhttp3.HttpUrl, String, java.net.URI, or android.net.Uri type.", new Object[0]);
                    }
                    return new RelativeUrl(this.method, i);
                } else {
                    throw Utils.parameterError(this.method, i, "@Url cannot be used with @%s URL", this.httpMethod);
                }
            } else if (annotation instanceof Path) {
                validateResolvableType(i, type);
                if (this.gotQuery) {
                    throw Utils.parameterError(this.method, i, "A @Path parameter must not come after a @Query.", new Object[0]);
                } else if (this.gotQueryName) {
                    throw Utils.parameterError(this.method, i, "A @Path parameter must not come after a @QueryName.", new Object[0]);
                } else if (this.gotQueryMap) {
                    throw Utils.parameterError(this.method, i, "A @Path parameter must not come after a @QueryMap.", new Object[0]);
                } else if (this.gotUrl) {
                    throw Utils.parameterError(this.method, i, str, new Object[0]);
                } else if (this.relativeUrl != null) {
                    this.gotPath = true;
                    Path path = (Path) annotation;
                    String value = path.value();
                    validatePathName(i, value);
                    int i2 = i;
                    Path path2 = new Path(this.method, i2, value, this.retrofit.stringConverter(type, annotationArr), path.encoded());
                    return path2;
                } else {
                    throw Utils.parameterError(this.method, i, "@Path can only be used with relative url on @%s", this.httpMethod);
                }
            } else {
                String str2 = "<String>)";
                String str3 = " must include generic type (e.g., ";
                if (annotation instanceof Query) {
                    validateResolvableType(i, type);
                    Query query = (Query) annotation;
                    String value2 = query.value();
                    boolean encoded = query.encoded();
                    Class rawType = Utils.getRawType(type);
                    this.gotQuery = true;
                    if (Iterable.class.isAssignableFrom(rawType)) {
                        if (type instanceof ParameterizedType) {
                            return new Query(value2, this.retrofit.stringConverter(Utils.getParameterUpperBound(0, (ParameterizedType) type), annotationArr), encoded).iterable();
                        }
                        Method method2 = this.method;
                        StringBuilder sb = new StringBuilder();
                        sb.append(rawType.getSimpleName());
                        sb.append(str3);
                        sb.append(rawType.getSimpleName());
                        sb.append(str2);
                        throw Utils.parameterError(method2, i, sb.toString(), new Object[0]);
                    } else if (!rawType.isArray()) {
                        return new Query(value2, this.retrofit.stringConverter(type, annotationArr), encoded);
                    } else {
                        return new Query(value2, this.retrofit.stringConverter(boxIfPrimitive(rawType.getComponentType()), annotationArr), encoded).array();
                    }
                } else if (annotation instanceof QueryName) {
                    validateResolvableType(i, type);
                    boolean encoded2 = ((QueryName) annotation).encoded();
                    Class rawType2 = Utils.getRawType(type);
                    this.gotQueryName = true;
                    if (Iterable.class.isAssignableFrom(rawType2)) {
                        if (type instanceof ParameterizedType) {
                            return new QueryName(this.retrofit.stringConverter(Utils.getParameterUpperBound(0, (ParameterizedType) type), annotationArr), encoded2).iterable();
                        }
                        Method method3 = this.method;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(rawType2.getSimpleName());
                        sb2.append(str3);
                        sb2.append(rawType2.getSimpleName());
                        sb2.append(str2);
                        throw Utils.parameterError(method3, i, sb2.toString(), new Object[0]);
                    } else if (!rawType2.isArray()) {
                        return new QueryName(this.retrofit.stringConverter(type, annotationArr), encoded2);
                    } else {
                        return new QueryName(this.retrofit.stringConverter(boxIfPrimitive(rawType2.getComponentType()), annotationArr), encoded2).array();
                    }
                } else {
                    String str4 = "Map must include generic types (e.g., Map<String, String>)";
                    if (annotation instanceof QueryMap) {
                        validateResolvableType(i, type);
                        Class rawType3 = Utils.getRawType(type);
                        this.gotQueryMap = true;
                        if (Map.class.isAssignableFrom(rawType3)) {
                            Type supertype = Utils.getSupertype(type, rawType3, Map.class);
                            if (supertype instanceof ParameterizedType) {
                                ParameterizedType parameterizedType = (ParameterizedType) supertype;
                                Type parameterUpperBound = Utils.getParameterUpperBound(0, parameterizedType);
                                if (String.class == parameterUpperBound) {
                                    return new QueryMap(this.method, i, this.retrofit.stringConverter(Utils.getParameterUpperBound(1, parameterizedType), annotationArr), ((QueryMap) annotation).encoded());
                                }
                                Method method4 = this.method;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("@QueryMap keys must be of type String: ");
                                sb3.append(parameterUpperBound);
                                throw Utils.parameterError(method4, i, sb3.toString(), new Object[0]);
                            }
                            throw Utils.parameterError(this.method, i, str4, new Object[0]);
                        }
                        throw Utils.parameterError(this.method, i, "@QueryMap parameter type must be Map.", new Object[0]);
                    } else if (annotation instanceof Header) {
                        validateResolvableType(i, type);
                        String value3 = ((Header) annotation).value();
                        Class rawType4 = Utils.getRawType(type);
                        if (Iterable.class.isAssignableFrom(rawType4)) {
                            if (type instanceof ParameterizedType) {
                                return new Header(value3, this.retrofit.stringConverter(Utils.getParameterUpperBound(0, (ParameterizedType) type), annotationArr)).iterable();
                            }
                            Method method5 = this.method;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(rawType4.getSimpleName());
                            sb4.append(str3);
                            sb4.append(rawType4.getSimpleName());
                            sb4.append(str2);
                            throw Utils.parameterError(method5, i, sb4.toString(), new Object[0]);
                        } else if (!rawType4.isArray()) {
                            return new Header(value3, this.retrofit.stringConverter(type, annotationArr));
                        } else {
                            return new Header(value3, this.retrofit.stringConverter(boxIfPrimitive(rawType4.getComponentType()), annotationArr)).array();
                        }
                    } else if (annotation instanceof HeaderMap) {
                        if (type == Headers.class) {
                            return new Headers(this.method, i);
                        }
                        validateResolvableType(i, type);
                        Class rawType5 = Utils.getRawType(type);
                        if (Map.class.isAssignableFrom(rawType5)) {
                            Type supertype2 = Utils.getSupertype(type, rawType5, Map.class);
                            if (supertype2 instanceof ParameterizedType) {
                                ParameterizedType parameterizedType2 = (ParameterizedType) supertype2;
                                Type parameterUpperBound2 = Utils.getParameterUpperBound(0, parameterizedType2);
                                if (String.class == parameterUpperBound2) {
                                    return new HeaderMap(this.method, i, this.retrofit.stringConverter(Utils.getParameterUpperBound(1, parameterizedType2), annotationArr));
                                }
                                Method method6 = this.method;
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("@HeaderMap keys must be of type String: ");
                                sb5.append(parameterUpperBound2);
                                throw Utils.parameterError(method6, i, sb5.toString(), new Object[0]);
                            }
                            throw Utils.parameterError(this.method, i, str4, new Object[0]);
                        }
                        throw Utils.parameterError(this.method, i, "@HeaderMap parameter type must be Map.", new Object[0]);
                    } else if (annotation instanceof Field) {
                        validateResolvableType(i, type);
                        if (this.isFormEncoded) {
                            Field field = (Field) annotation;
                            String value4 = field.value();
                            boolean encoded3 = field.encoded();
                            this.gotField = true;
                            Class rawType6 = Utils.getRawType(type);
                            if (Iterable.class.isAssignableFrom(rawType6)) {
                                if (type instanceof ParameterizedType) {
                                    return new Field(value4, this.retrofit.stringConverter(Utils.getParameterUpperBound(0, (ParameterizedType) type), annotationArr), encoded3).iterable();
                                }
                                Method method7 = this.method;
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append(rawType6.getSimpleName());
                                sb6.append(str3);
                                sb6.append(rawType6.getSimpleName());
                                sb6.append(str2);
                                throw Utils.parameterError(method7, i, sb6.toString(), new Object[0]);
                            } else if (!rawType6.isArray()) {
                                return new Field(value4, this.retrofit.stringConverter(type, annotationArr), encoded3);
                            } else {
                                return new Field(value4, this.retrofit.stringConverter(boxIfPrimitive(rawType6.getComponentType()), annotationArr), encoded3).array();
                            }
                        } else {
                            throw Utils.parameterError(this.method, i, "@Field parameters can only be used with form encoding.", new Object[0]);
                        }
                    } else if (annotation instanceof FieldMap) {
                        validateResolvableType(i, type);
                        if (this.isFormEncoded) {
                            Class rawType7 = Utils.getRawType(type);
                            if (Map.class.isAssignableFrom(rawType7)) {
                                Type supertype3 = Utils.getSupertype(type, rawType7, Map.class);
                                if (supertype3 instanceof ParameterizedType) {
                                    ParameterizedType parameterizedType3 = (ParameterizedType) supertype3;
                                    Type parameterUpperBound3 = Utils.getParameterUpperBound(0, parameterizedType3);
                                    if (String.class == parameterUpperBound3) {
                                        Converter stringConverter = this.retrofit.stringConverter(Utils.getParameterUpperBound(1, parameterizedType3), annotationArr);
                                        this.gotField = true;
                                        return new FieldMap(this.method, i, stringConverter, ((FieldMap) annotation).encoded());
                                    }
                                    Method method8 = this.method;
                                    StringBuilder sb7 = new StringBuilder();
                                    sb7.append("@FieldMap keys must be of type String: ");
                                    sb7.append(parameterUpperBound3);
                                    throw Utils.parameterError(method8, i, sb7.toString(), new Object[0]);
                                }
                                throw Utils.parameterError(this.method, i, str4, new Object[0]);
                            }
                            throw Utils.parameterError(this.method, i, "@FieldMap parameter type must be Map.", new Object[0]);
                        }
                        throw Utils.parameterError(this.method, i, "@FieldMap parameters can only be used with form encoding.", new Object[0]);
                    } else if (annotation instanceof Part) {
                        validateResolvableType(i, type);
                        if (this.isMultipart) {
                            Part part = (Part) annotation;
                            this.gotPart = true;
                            String value5 = part.value();
                            Class rawType8 = Utils.getRawType(type);
                            if (value5.isEmpty()) {
                                String str5 = "@Part annotation must supply a name or use MultipartBody.Part parameter type.";
                                if (Iterable.class.isAssignableFrom(rawType8)) {
                                    if (!(type instanceof ParameterizedType)) {
                                        Method method9 = this.method;
                                        StringBuilder sb8 = new StringBuilder();
                                        sb8.append(rawType8.getSimpleName());
                                        sb8.append(str3);
                                        sb8.append(rawType8.getSimpleName());
                                        sb8.append(str2);
                                        throw Utils.parameterError(method9, i, sb8.toString(), new Object[0]);
                                    } else if (MultipartBody.Part.class.isAssignableFrom(Utils.getRawType(Utils.getParameterUpperBound(0, (ParameterizedType) type)))) {
                                        return RawPart.INSTANCE.iterable();
                                    } else {
                                        throw Utils.parameterError(this.method, i, str5, new Object[0]);
                                    }
                                } else if (rawType8.isArray()) {
                                    if (MultipartBody.Part.class.isAssignableFrom(rawType8.getComponentType())) {
                                        return RawPart.INSTANCE.array();
                                    }
                                    throw Utils.parameterError(this.method, i, str5, new Object[0]);
                                } else if (MultipartBody.Part.class.isAssignableFrom(rawType8)) {
                                    return RawPart.INSTANCE;
                                } else {
                                    throw Utils.parameterError(this.method, i, str5, new Object[0]);
                                }
                            } else {
                                StringBuilder sb9 = new StringBuilder();
                                sb9.append("form-data; name=\"");
                                sb9.append(value5);
                                sb9.append("\"");
                                Headers of = Headers.of("Content-Disposition", sb9.toString(), "Content-Transfer-Encoding", part.encoding());
                                String str6 = "@Part parameters using the MultipartBody.Part must not include a part name in the annotation.";
                                if (Iterable.class.isAssignableFrom(rawType8)) {
                                    if (type instanceof ParameterizedType) {
                                        Type parameterUpperBound4 = Utils.getParameterUpperBound(0, (ParameterizedType) type);
                                        if (!MultipartBody.Part.class.isAssignableFrom(Utils.getRawType(parameterUpperBound4))) {
                                            return new Part(this.method, i, of, this.retrofit.requestBodyConverter(parameterUpperBound4, annotationArr, this.methodAnnotations)).iterable();
                                        }
                                        throw Utils.parameterError(this.method, i, str6, new Object[0]);
                                    }
                                    Method method10 = this.method;
                                    StringBuilder sb10 = new StringBuilder();
                                    sb10.append(rawType8.getSimpleName());
                                    sb10.append(str3);
                                    sb10.append(rawType8.getSimpleName());
                                    sb10.append(str2);
                                    throw Utils.parameterError(method10, i, sb10.toString(), new Object[0]);
                                } else if (rawType8.isArray()) {
                                    Class boxIfPrimitive = boxIfPrimitive(rawType8.getComponentType());
                                    if (!MultipartBody.Part.class.isAssignableFrom(boxIfPrimitive)) {
                                        return new Part(this.method, i, of, this.retrofit.requestBodyConverter(boxIfPrimitive, annotationArr, this.methodAnnotations)).array();
                                    }
                                    throw Utils.parameterError(this.method, i, str6, new Object[0]);
                                } else if (!MultipartBody.Part.class.isAssignableFrom(rawType8)) {
                                    return new Part(this.method, i, of, this.retrofit.requestBodyConverter(type, annotationArr, this.methodAnnotations));
                                } else {
                                    throw Utils.parameterError(this.method, i, str6, new Object[0]);
                                }
                            }
                        } else {
                            throw Utils.parameterError(this.method, i, "@Part parameters can only be used with multipart encoding.", new Object[0]);
                        }
                    } else if (annotation instanceof PartMap) {
                        validateResolvableType(i, type);
                        if (this.isMultipart) {
                            this.gotPart = true;
                            Class rawType9 = Utils.getRawType(type);
                            if (Map.class.isAssignableFrom(rawType9)) {
                                Type supertype4 = Utils.getSupertype(type, rawType9, Map.class);
                                if (supertype4 instanceof ParameterizedType) {
                                    ParameterizedType parameterizedType4 = (ParameterizedType) supertype4;
                                    Type parameterUpperBound5 = Utils.getParameterUpperBound(0, parameterizedType4);
                                    if (String.class == parameterUpperBound5) {
                                        Type parameterUpperBound6 = Utils.getParameterUpperBound(1, parameterizedType4);
                                        if (!MultipartBody.Part.class.isAssignableFrom(Utils.getRawType(parameterUpperBound6))) {
                                            return new PartMap(this.method, i, this.retrofit.requestBodyConverter(parameterUpperBound6, annotationArr, this.methodAnnotations), ((PartMap) annotation).encoding());
                                        }
                                        throw Utils.parameterError(this.method, i, "@PartMap values cannot be MultipartBody.Part. Use @Part List<Part> or a different value type instead.", new Object[0]);
                                    }
                                    Method method11 = this.method;
                                    StringBuilder sb11 = new StringBuilder();
                                    sb11.append("@PartMap keys must be of type String: ");
                                    sb11.append(parameterUpperBound5);
                                    throw Utils.parameterError(method11, i, sb11.toString(), new Object[0]);
                                }
                                throw Utils.parameterError(this.method, i, str4, new Object[0]);
                            }
                            throw Utils.parameterError(this.method, i, "@PartMap parameter type must be Map.", new Object[0]);
                        }
                        throw Utils.parameterError(this.method, i, "@PartMap parameters can only be used with multipart encoding.", new Object[0]);
                    } else if (annotation instanceof Body) {
                        validateResolvableType(i, type);
                        if (this.isFormEncoded || this.isMultipart) {
                            throw Utils.parameterError(this.method, i, "@Body parameters cannot be used with form or multi-part encoding.", new Object[0]);
                        } else if (!this.gotBody) {
                            try {
                                Converter requestBodyConverter = this.retrofit.requestBodyConverter(type, annotationArr, this.methodAnnotations);
                                this.gotBody = true;
                                return new Body(this.method, i, requestBodyConverter);
                            } catch (RuntimeException e) {
                                throw Utils.parameterError(this.method, e, i, "Unable to create @Body converter for %s", type);
                            }
                        } else {
                            throw Utils.parameterError(this.method, i, "Multiple @Body method annotations found.", new Object[0]);
                        }
                    } else if (!(annotation instanceof Tag)) {
                        return null;
                    } else {
                        validateResolvableType(i, type);
                        Class rawType10 = Utils.getRawType(type);
                        int i3 = i - 1;
                        while (i3 >= 0) {
                            ParameterHandler<?> parameterHandler = this.parameterHandlers[i3];
                            if (!(parameterHandler instanceof Tag) || !((Tag) parameterHandler).cls.equals(rawType10)) {
                                i3--;
                            } else {
                                Method method12 = this.method;
                                StringBuilder sb12 = new StringBuilder();
                                sb12.append("@Tag type ");
                                sb12.append(rawType10.getName());
                                sb12.append(" is duplicate of parameter #");
                                sb12.append(i3 + 1);
                                sb12.append(" and would always overwrite its value.");
                                throw Utils.parameterError(method12, i, sb12.toString(), new Object[0]);
                            }
                        }
                        return new Tag(rawType10);
                    }
                }
            }
        }

        private void validateResolvableType(int i, Type type) {
            if (Utils.hasUnresolvableType(type)) {
                throw Utils.parameterError(this.method, i, "Parameter type must not include a type variable or wildcard: %s", type);
            }
        }

        private void validatePathName(int i, String str) {
            if (!PARAM_NAME_REGEX.matcher(str).matches()) {
                throw Utils.parameterError(this.method, i, "@Path parameter name must match %s. Found: %s", PARAM_URL_REGEX.pattern(), str);
            } else if (!this.relativeUrlParamNames.contains(str)) {
                throw Utils.parameterError(this.method, i, "URL \"%s\" does not contain \"{%s}\".", this.relativeUrl, str);
            }
        }

        static Set<String> parsePathParameters(String str) {
            Matcher matcher = PARAM_URL_REGEX.matcher(str);
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            while (matcher.find()) {
                linkedHashSet.add(matcher.group(1));
            }
            return linkedHashSet;
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.Class<?>, code=java.lang.Class, for r1v0, types: [java.lang.Class<?>, java.lang.Class] */
        private static Class<?> boxIfPrimitive(Class cls) {
            if (Boolean.TYPE == cls) {
                return Boolean.class;
            }
            if (Byte.TYPE == cls) {
                return Byte.class;
            }
            if (Character.TYPE == cls) {
                return Character.class;
            }
            if (Double.TYPE == cls) {
                return Double.class;
            }
            if (Float.TYPE == cls) {
                return Float.class;
            }
            if (Integer.TYPE == cls) {
                return Integer.class;
            }
            if (Long.TYPE == cls) {
                return Long.class;
            }
            if (Short.TYPE == cls) {
                cls = Short.class;
            }
            return cls;
        }
    }

    static RequestFactory parseAnnotations(Retrofit retrofit, Method method2) {
        return new Builder(retrofit, method2).build();
    }

    RequestFactory(Builder builder) {
        this.method = builder.method;
        this.baseUrl = builder.retrofit.baseUrl;
        this.httpMethod = builder.httpMethod;
        this.relativeUrl = builder.relativeUrl;
        this.headers = builder.headers;
        this.contentType = builder.contentType;
        this.hasBody = builder.hasBody;
        this.isFormEncoded = builder.isFormEncoded;
        this.isMultipart = builder.isMultipart;
        this.parameterHandlers = builder.parameterHandlers;
        this.isKotlinSuspendFunction = builder.isKotlinSuspendFunction;
    }

    /* access modifiers changed from: 0000 */
    public Request create(Object[] objArr) throws IOException {
        ParameterHandler<?>[] parameterHandlerArr = this.parameterHandlers;
        int length = objArr.length;
        if (length == parameterHandlerArr.length) {
            RequestBuilder requestBuilder = new RequestBuilder(this.httpMethod, this.baseUrl, this.relativeUrl, this.headers, this.contentType, this.hasBody, this.isFormEncoded, this.isMultipart);
            if (this.isKotlinSuspendFunction) {
                length--;
            }
            ArrayList arrayList = new ArrayList(length);
            for (int i = 0; i < length; i++) {
                arrayList.add(objArr[i]);
                parameterHandlerArr[i].apply(requestBuilder, objArr[i]);
            }
            return requestBuilder.get().tag(Invocation.class, new Invocation(this.method, arrayList)).build();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Argument count (");
        sb.append(length);
        sb.append(") doesn't match expected count (");
        sb.append(parameterHandlerArr.length);
        sb.append(")");
        throw new IllegalArgumentException(sb.toString());
    }
}
