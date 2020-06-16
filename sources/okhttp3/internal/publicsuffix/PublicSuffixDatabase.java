package okhttp3.internal.publicsuffix;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.IDN;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;
import okhttp3.internal.Util;
import okhttp3.internal.platform.Platform;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import okio.Source;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B\u0005¢\u0006\u0002\u0010\u0002J\u001c\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0002J\u0010\u0010\u000e\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000f\u001a\u00020\fJ\b\u0010\u0010\u001a\u00020\u0011H\u0002J\b\u0010\u0012\u001a\u00020\u0011H\u0002J\u0016\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X.¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lokhttp3/internal/publicsuffix/PublicSuffixDatabase;", "", "()V", "listRead", "Ljava/util/concurrent/atomic/AtomicBoolean;", "publicSuffixExceptionListBytes", "", "publicSuffixListBytes", "readCompleteLatch", "Ljava/util/concurrent/CountDownLatch;", "findMatchingRule", "", "", "domainLabels", "getEffectiveTldPlusOne", "domain", "readTheList", "", "readTheListUninterruptibly", "setListBytes", "Companion", "okhttp"}, k = 1, mv = {1, 1, 15})
/* compiled from: PublicSuffixDatabase.kt */
public final class PublicSuffixDatabase {
    public static final Companion Companion = new Companion(null);
    private static final char EXCEPTION_MARKER = '!';
    private static final List<String> PREVAILING_RULE = CollectionsKt.listOf("*");
    public static final String PUBLIC_SUFFIX_RESOURCE = "publicsuffixes.gz";
    private static final byte[] WILDCARD_LABEL = {(byte) 42};
    /* access modifiers changed from: private */
    public static final PublicSuffixDatabase instance = new PublicSuffixDatabase();
    private final AtomicBoolean listRead = new AtomicBoolean(false);
    private byte[] publicSuffixExceptionListBytes;
    /* access modifiers changed from: private */
    public byte[] publicSuffixListBytes;
    private final CountDownLatch readCompleteLatch = new CountDownLatch(1);

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\f\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\r\u001a\u00020\fJ)\u0010\u000e\u001a\u0004\u0018\u00010\u0007*\u00020\n2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002¢\u0006\u0002\u0010\u0013R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lokhttp3/internal/publicsuffix/PublicSuffixDatabase$Companion;", "", "()V", "EXCEPTION_MARKER", "", "PREVAILING_RULE", "", "", "PUBLIC_SUFFIX_RESOURCE", "WILDCARD_LABEL", "", "instance", "Lokhttp3/internal/publicsuffix/PublicSuffixDatabase;", "get", "binarySearch", "labels", "", "labelIndex", "", "([B[[BI)Ljava/lang/String;", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: PublicSuffixDatabase.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final PublicSuffixDatabase get() {
            return PublicSuffixDatabase.instance;
        }

        /* access modifiers changed from: private */
        public final String binarySearch(byte[] bArr, byte[][] bArr2, int i) {
            int i2;
            boolean z;
            int i3;
            int and;
            byte[] bArr3 = bArr;
            int length = bArr3.length;
            String str = null;
            int i4 = 0;
            while (i4 < length) {
                int i5 = (i4 + length) / 2;
                while (i5 > -1 && bArr3[i5] != ((byte) 10)) {
                    i5--;
                }
                int i6 = i5 + 1;
                int i7 = 1;
                while (true) {
                    i2 = i6 + i7;
                    if (bArr3[i2] == ((byte) 10)) {
                        break;
                    }
                    i7++;
                }
                int i8 = i2 - i6;
                int i9 = i;
                boolean z2 = false;
                int i10 = 0;
                int i11 = 0;
                while (true) {
                    if (z2) {
                        i3 = 46;
                        z = false;
                    } else {
                        z = z2;
                        i3 = Util.and(bArr2[i9][i10], 255);
                    }
                    and = i3 - Util.and(bArr3[i6 + i11], 255);
                    if (and == 0) {
                        i11++;
                        i10++;
                        if (i11 == i8) {
                            break;
                        } else if (bArr2[i9].length != i10) {
                            z2 = z;
                        } else if (i9 == ((Object[]) bArr2).length - 1) {
                            break;
                        } else {
                            i9++;
                            z2 = true;
                            i10 = -1;
                        }
                    } else {
                        break;
                    }
                }
                if (and >= 0) {
                    if (and <= 0) {
                        int i12 = i8 - i11;
                        int length2 = bArr2[i9].length - i10;
                        for (int i13 = i9 + 1; i13 < ((Object[]) bArr2).length; i13++) {
                            length2 += bArr2[i13].length;
                        }
                        if (length2 >= i12) {
                            if (length2 <= i12) {
                                Charset charset = StandardCharsets.UTF_8;
                                Intrinsics.checkExpressionValueIsNotNull(charset, "UTF_8");
                                return new String(bArr3, i6, i8, charset);
                            }
                        }
                    }
                    i4 = i2 + 1;
                }
                length = i6 - 1;
            }
            return str;
        }
    }

    public static final /* synthetic */ byte[] access$getPublicSuffixListBytes$p(PublicSuffixDatabase publicSuffixDatabase) {
        byte[] bArr = publicSuffixDatabase.publicSuffixListBytes;
        if (bArr == null) {
            Intrinsics.throwUninitializedPropertyAccessException("publicSuffixListBytes");
        }
        return bArr;
    }

    public final String getEffectiveTldPlusOne(String str) {
        int i;
        int i2;
        String str2 = str;
        Intrinsics.checkParameterIsNotNull(str2, "domain");
        String unicode = IDN.toUnicode(str);
        Intrinsics.checkExpressionValueIsNotNull(unicode, "unicodeDomain");
        List split$default = StringsKt.split$default((CharSequence) unicode, new char[]{'.'}, false, 0, 6, (Object) null);
        List findMatchingRule = findMatchingRule(split$default);
        if (split$default.size() == findMatchingRule.size() && ((String) findMatchingRule.get(0)).charAt(0) != '!') {
            return null;
        }
        if (((String) findMatchingRule.get(0)).charAt(0) == '!') {
            i2 = split$default.size();
            i = findMatchingRule.size();
        } else {
            i2 = split$default.size();
            i = findMatchingRule.size() + 1;
        }
        return SequencesKt.joinToString$default(SequencesKt.drop(CollectionsKt.asSequence(StringsKt.split$default((CharSequence) str2, new char[]{'.'}, false, 0, 6, (Object) null)), i2 - i), ".", null, null, 0, null, null, 62, null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0102, code lost:
        if (r12 != null) goto L_0x0109;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x011a, code lost:
        if (r0 != null) goto L_0x0121;
     */
    private final List<String> findMatchingRule(List<String> list) {
        String str;
        String str2;
        String str3;
        List<String> list2;
        List<String> list3;
        if (this.listRead.get() || !this.listRead.compareAndSet(false, true)) {
            try {
                this.readCompleteLatch.await();
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
            }
        } else {
            readTheListUninterruptibly();
        }
        if (this.publicSuffixListBytes != null) {
            int size = list.size();
            byte[][] bArr = new byte[size][];
            int i = 0;
            while (i < size) {
                String str4 = (String) list.get(i);
                Charset charset = StandardCharsets.UTF_8;
                Intrinsics.checkExpressionValueIsNotNull(charset, "UTF_8");
                if (str4 != null) {
                    byte[] bytes = str4.getBytes(charset);
                    Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
                    bArr[i] = bytes;
                    i++;
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
            }
            byte[][] bArr2 = bArr;
            String str5 = null;
            int length = bArr2.length;
            int i2 = 0;
            while (true) {
                str = "publicSuffixListBytes";
                if (i2 >= length) {
                    str2 = str5;
                    break;
                }
                Companion companion = Companion;
                byte[] bArr3 = this.publicSuffixListBytes;
                if (bArr3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException(str);
                }
                str2 = companion.binarySearch(bArr3, bArr2, i2);
                if (str2 != null) {
                    break;
                }
                i2++;
            }
            Object[] objArr = (Object[]) bArr2;
            if (objArr.length > 1) {
                byte[][] bArr4 = (byte[][]) objArr.clone();
                int length2 = ((Object[]) bArr4).length - 1;
                int i3 = 0;
                while (true) {
                    if (i3 >= length2) {
                        break;
                    }
                    bArr4[i3] = WILDCARD_LABEL;
                    Companion companion2 = Companion;
                    byte[] bArr5 = this.publicSuffixListBytes;
                    if (bArr5 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException(str);
                    }
                    str3 = companion2.binarySearch(bArr5, bArr4, i3);
                    if (str3 != null) {
                        break;
                    }
                    i3++;
                }
            }
            str3 = str5;
            if (str3 != null) {
                int length3 = objArr.length - 1;
                int i4 = 0;
                while (true) {
                    if (i4 >= length3) {
                        break;
                    }
                    Companion companion3 = Companion;
                    byte[] bArr6 = this.publicSuffixExceptionListBytes;
                    if (bArr6 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("publicSuffixExceptionListBytes");
                    }
                    String access$binarySearch = companion3.binarySearch(bArr6, bArr2, i4);
                    if (access$binarySearch != null) {
                        str5 = access$binarySearch;
                        break;
                    }
                    i4++;
                }
            }
            if (str5 != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(EXCEPTION_MARKER);
                sb.append(str5);
                return StringsKt.split$default(sb.toString(), new char[]{'.'}, false, 0, 6, (Object) null);
            } else if (str2 == null && str3 == null) {
                return PREVAILING_RULE;
            } else {
                if (str2 != null) {
                    list2 = StringsKt.split$default((CharSequence) str2, new char[]{'.'}, false, 0, 6, (Object) null);
                }
                list2 = CollectionsKt.emptyList();
                if (str3 != null) {
                    list3 = StringsKt.split$default((CharSequence) str3, new char[]{'.'}, false, 0, 6, (Object) null);
                }
                list3 = CollectionsKt.emptyList();
                if (list2.size() <= list3.size()) {
                    list2 = list3;
                }
                return list2;
            }
        } else {
            throw new IllegalStateException("Unable to load publicsuffixes.gz resource from the classpath.".toString());
        }
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0029 */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    private final void readTheListUninterruptibly() {
        boolean z;
        z = false;
        while (true) {
            readTheList();
            if (!z) {
                break;
            }
            try {
                Thread.currentThread().interrupt();
            } catch (InterruptedIOException ) {
                Thread.interrupted();
                z = true;
            } catch (IOException e) {
                Platform.Companion.get().log(5, "Failed to read public suffix list", e);
                if (z) {
                    Thread.currentThread().interrupt();
                }
                return;
            } catch (Throwable th) {
                if (z) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
            Thread.interrupted();
            z = true;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0058, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0059, code lost:
        kotlin.io.CloseableKt.closeFinally(r1, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005c, code lost:
        throw r2;
     */
    private final void readTheList() throws IOException {
        byte[] bArr = null;
        InputStream resourceAsStream = PublicSuffixDatabase.class.getResourceAsStream(PUBLIC_SUFFIX_RESOURCE);
        if (resourceAsStream != null) {
            Closeable buffer = Okio.buffer((Source) new GzipSource(Okio.source(resourceAsStream)));
            Throwable th = null;
            BufferedSource bufferedSource = (BufferedSource) buffer;
            byte[] readByteArray = bufferedSource.readByteArray((long) bufferedSource.readInt());
            byte[] readByteArray2 = bufferedSource.readByteArray((long) bufferedSource.readInt());
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(buffer, th);
            synchronized (this) {
                if (readByteArray == null) {
                    Intrinsics.throwNpe();
                }
                this.publicSuffixListBytes = readByteArray;
                if (readByteArray2 == null) {
                    Intrinsics.throwNpe();
                }
                this.publicSuffixExceptionListBytes = readByteArray2;
                Unit unit2 = Unit.INSTANCE;
            }
            this.readCompleteLatch.countDown();
        }
    }

    public final void setListBytes(byte[] bArr, byte[] bArr2) {
        Intrinsics.checkParameterIsNotNull(bArr, "publicSuffixListBytes");
        Intrinsics.checkParameterIsNotNull(bArr2, "publicSuffixExceptionListBytes");
        this.publicSuffixListBytes = bArr;
        this.publicSuffixExceptionListBytes = bArr2;
        this.listRead.set(true);
        this.readCompleteLatch.countDown();
    }
}
