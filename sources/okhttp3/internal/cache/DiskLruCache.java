package okhttp3.internal.cache;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import kotlin.text.Typography;
import okhttp3.internal.Util;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.platform.Platform;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010)\n\u0002\b\u0007\u0018\u0000 V2\u00020\u00012\u00020\u0002:\u0004VWXYB7\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\b\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\b\u00104\u001a\u000205H\u0002J\b\u00106\u001a\u000205H\u0016J!\u00107\u001a\u0002052\n\u00108\u001a\u000609R\u00020\u00002\u0006\u0010:\u001a\u00020\u0012H\u0000¢\u0006\u0002\b;J\u0006\u0010<\u001a\u000205J \u0010=\u001a\b\u0018\u000109R\u00020\u00002\u0006\u0010>\u001a\u00020$2\b\b\u0002\u0010?\u001a\u00020\u000bH\u0007J\u0006\u0010@\u001a\u000205J\b\u0010A\u001a\u000205H\u0016J\u0017\u0010B\u001a\b\u0018\u00010CR\u00020\u00002\u0006\u0010>\u001a\u00020$H\u0002J\u0006\u0010D\u001a\u000205J\u0006\u0010E\u001a\u00020\u0012J\b\u0010F\u001a\u00020\u0012H\u0002J\b\u0010G\u001a\u00020!H\u0002J\b\u0010H\u001a\u000205H\u0002J\b\u0010I\u001a\u000205H\u0002J\u0010\u0010J\u001a\u0002052\u0006\u0010K\u001a\u00020$H\u0002J\r\u0010L\u001a\u000205H\u0000¢\u0006\u0002\bMJ\u000e\u0010N\u001a\u00020\u00122\u0006\u0010>\u001a\u00020$J\u0019\u0010O\u001a\u00020\u00122\n\u0010P\u001a\u00060%R\u00020\u0000H\u0000¢\u0006\u0002\bQJ\u0006\u00101\u001a\u00020\u000bJ\u0010\u0010R\u001a\f\u0012\b\u0012\u00060CR\u00020\u00000SJ\u0006\u0010T\u001a\u000205J\u0010\u0010U\u001a\u0002052\u0006\u0010>\u001a\u00020$H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u00020\u0012X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u000e\u0010\u001b\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u0004\u0018\u00010!X\u000e¢\u0006\u0002\n\u0000R$\u0010\"\u001a\u0012\u0012\u0004\u0012\u00020$\u0012\b\u0012\u00060%R\u00020\u00000#X\u0004¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'R&\u0010\n\u001a\u00020\u000b2\u0006\u0010(\u001a\u00020\u000b8F@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R\u000e\u0010-\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u000bX\u000e¢\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u000bX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\bX\u0004¢\u0006\b\n\u0000\u001a\u0004\b2\u00103¨\u0006Z"}, d2 = {"Lokhttp3/internal/cache/DiskLruCache;", "Ljava/io/Closeable;", "Ljava/io/Flushable;", "fileSystem", "Lokhttp3/internal/io/FileSystem;", "directory", "Ljava/io/File;", "appVersion", "", "valueCount", "maxSize", "", "executor", "Ljava/util/concurrent/Executor;", "(Lokhttp3/internal/io/FileSystem;Ljava/io/File;IIJLjava/util/concurrent/Executor;)V", "cleanupRunnable", "Ljava/lang/Runnable;", "closed", "", "getClosed$okhttp", "()Z", "setClosed$okhttp", "(Z)V", "getDirectory", "()Ljava/io/File;", "getFileSystem$okhttp", "()Lokhttp3/internal/io/FileSystem;", "hasJournalErrors", "initialized", "journalFile", "journalFileBackup", "journalFileTmp", "journalWriter", "Lokio/BufferedSink;", "lruEntries", "Ljava/util/LinkedHashMap;", "", "Lokhttp3/internal/cache/DiskLruCache$Entry;", "getLruEntries$okhttp", "()Ljava/util/LinkedHashMap;", "value", "getMaxSize", "()J", "setMaxSize", "(J)V", "mostRecentRebuildFailed", "mostRecentTrimFailed", "nextSequenceNumber", "redundantOpCount", "size", "getValueCount$okhttp", "()I", "checkNotClosed", "", "close", "completeEdit", "editor", "Lokhttp3/internal/cache/DiskLruCache$Editor;", "success", "completeEdit$okhttp", "delete", "edit", "key", "expectedSequenceNumber", "evictAll", "flush", "get", "Lokhttp3/internal/cache/DiskLruCache$Snapshot;", "initialize", "isClosed", "journalRebuildRequired", "newJournalWriter", "processJournal", "readJournal", "readJournalLine", "line", "rebuildJournal", "rebuildJournal$okhttp", "remove", "removeEntry", "entry", "removeEntry$okhttp", "snapshots", "", "trimToSize", "validateKey", "Companion", "Editor", "Entry", "Snapshot", "okhttp"}, k = 1, mv = {1, 1, 15})
/* compiled from: DiskLruCache.kt */
public final class DiskLruCache implements Closeable, Flushable {
    public static final long ANY_SEQUENCE_NUMBER = -1;
    public static final String CLEAN = CLEAN;
    public static final Companion Companion = new Companion(null);
    public static final String DIRTY = DIRTY;
    public static final String JOURNAL_FILE = JOURNAL_FILE;
    public static final String JOURNAL_FILE_BACKUP = JOURNAL_FILE_BACKUP;
    public static final String JOURNAL_FILE_TEMP = JOURNAL_FILE_TEMP;
    public static final Regex LEGAL_KEY_PATTERN = new Regex("[a-z0-9_-]{1,120}");
    public static final String MAGIC = MAGIC;
    public static final String READ = READ;
    public static final String REMOVE = REMOVE;
    public static final String VERSION_1 = VERSION_1;
    private final int appVersion;
    private final Runnable cleanupRunnable = new DiskLruCache$cleanupRunnable$1(this);
    private boolean closed;
    private final File directory;
    private final Executor executor;
    private final FileSystem fileSystem;
    /* access modifiers changed from: private */
    public boolean hasJournalErrors;
    /* access modifiers changed from: private */
    public boolean initialized;
    private final File journalFile = new File(this.directory, JOURNAL_FILE);
    private final File journalFileBackup = new File(this.directory, JOURNAL_FILE_BACKUP);
    private final File journalFileTmp = new File(this.directory, JOURNAL_FILE_TEMP);
    /* access modifiers changed from: private */
    public BufferedSink journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<>(0, 0.75f, true);
    private long maxSize;
    /* access modifiers changed from: private */
    public boolean mostRecentRebuildFailed;
    /* access modifiers changed from: private */
    public boolean mostRecentTrimFailed;
    private long nextSequenceNumber;
    /* access modifiers changed from: private */
    public int redundantOpCount;
    private long size;
    private final int valueCount;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J.\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u00020\u00048\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u00020\f8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lokhttp3/internal/cache/DiskLruCache$Companion;", "", "()V", "ANY_SEQUENCE_NUMBER", "", "CLEAN", "", "DIRTY", "JOURNAL_FILE", "JOURNAL_FILE_BACKUP", "JOURNAL_FILE_TEMP", "LEGAL_KEY_PATTERN", "Lkotlin/text/Regex;", "MAGIC", "READ", "REMOVE", "VERSION_1", "create", "Lokhttp3/internal/cache/DiskLruCache;", "fileSystem", "Lokhttp3/internal/io/FileSystem;", "directory", "Ljava/io/File;", "appVersion", "", "valueCount", "maxSize", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: DiskLruCache.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final DiskLruCache create(FileSystem fileSystem, File file, int i, int i2, long j) {
            FileSystem fileSystem2 = fileSystem;
            Intrinsics.checkParameterIsNotNull(fileSystem, "fileSystem");
            File file2 = file;
            Intrinsics.checkParameterIsNotNull(file, "directory");
            boolean z = false;
            if (j > 0) {
                if (i2 > 0) {
                    z = true;
                }
                if (z) {
                    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory("OkHttp DiskLruCache", true));
                    DiskLruCache diskLruCache = new DiskLruCache(fileSystem, file, i, i2, j, threadPoolExecutor);
                    return diskLruCache;
                }
                throw new IllegalArgumentException("valueCount <= 0".toString());
            }
            throw new IllegalArgumentException("maxSize <= 0".toString());
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0018\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0013\b\u0000\u0012\n\u0010\u0002\u001a\u00060\u0003R\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\u000fJ\r\u0010\u0011\u001a\u00020\u000fH\u0000¢\u0006\u0002\b\u0012J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0015\u001a\u00020\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u0018\u0010\u0002\u001a\u00060\u0003R\u00020\u0004X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0016\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u0019"}, d2 = {"Lokhttp3/internal/cache/DiskLruCache$Editor;", "", "entry", "Lokhttp3/internal/cache/DiskLruCache$Entry;", "Lokhttp3/internal/cache/DiskLruCache;", "(Lokhttp3/internal/cache/DiskLruCache;Lokhttp3/internal/cache/DiskLruCache$Entry;)V", "done", "", "getEntry$okhttp", "()Lokhttp3/internal/cache/DiskLruCache$Entry;", "written", "", "getWritten$okhttp", "()[Z", "abort", "", "commit", "detach", "detach$okhttp", "newSink", "Lokio/Sink;", "index", "", "newSource", "Lokio/Source;", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: DiskLruCache.kt */
    public final class Editor {
        private boolean done;
        private final Entry entry;
        final /* synthetic */ DiskLruCache this$0;
        private final boolean[] written;

        public Editor(DiskLruCache diskLruCache, Entry entry2) {
            Intrinsics.checkParameterIsNotNull(entry2, "entry");
            this.this$0 = diskLruCache;
            this.entry = entry2;
            this.written = entry2.getReadable$okhttp() ? null : new boolean[diskLruCache.getValueCount$okhttp()];
        }

        public final Entry getEntry$okhttp() {
            return this.entry;
        }

        public final boolean[] getWritten$okhttp() {
            return this.written;
        }

        public final void detach$okhttp() {
            if (Intrinsics.areEqual((Object) this.entry.getCurrentEditor$okhttp(), (Object) this)) {
                int valueCount$okhttp = this.this$0.getValueCount$okhttp();
                for (int i = 0; i < valueCount$okhttp; i++) {
                    try {
                        this.this$0.getFileSystem$okhttp().delete((File) this.entry.getDirtyFiles$okhttp().get(i));
                    } catch (IOException unused) {
                    }
                }
                this.entry.setCurrentEditor$okhttp(null);
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:16:0x003d, code lost:
            return null;
         */
        public final Source newSource(int i) {
            Source source;
            synchronized (this.this$0) {
                if (!this.done) {
                    source = null;
                    if (this.entry.getReadable$okhttp() && !(!Intrinsics.areEqual((Object) this.entry.getCurrentEditor$okhttp(), (Object) this))) {
                        try {
                            source = this.this$0.getFileSystem$okhttp().source((File) this.entry.getCleanFiles$okhttp().get(i));
                        } catch (FileNotFoundException unused) {
                        }
                    }
                } else {
                    throw new IllegalStateException("Check failed.".toString());
                }
            }
            return source;
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(4:24|25|26|27) */
        /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
            r5 = okio.Okio.blackhole();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x005b, code lost:
            return r5;
         */
        /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0056 */
        public final Sink newSink(int i) {
            synchronized (this.this$0) {
                if (!(!this.done)) {
                    throw new IllegalStateException("Check failed.".toString());
                } else if (!Intrinsics.areEqual((Object) this.entry.getCurrentEditor$okhttp(), (Object) this)) {
                    Sink blackhole = Okio.blackhole();
                    return blackhole;
                } else {
                    if (!this.entry.getReadable$okhttp()) {
                        boolean[] zArr = this.written;
                        if (zArr == null) {
                            Intrinsics.throwNpe();
                        }
                        zArr[i] = true;
                    }
                    Sink faultHidingSink = new FaultHidingSink(this.this$0.getFileSystem$okhttp().sink((File) this.entry.getDirtyFiles$okhttp().get(i)), new DiskLruCache$Editor$newSink$$inlined$synchronized$lambda$1(this, i));
                    return faultHidingSink;
                }
            }
        }

        public final void commit() throws IOException {
            synchronized (this.this$0) {
                if (!this.done) {
                    if (Intrinsics.areEqual((Object) this.entry.getCurrentEditor$okhttp(), (Object) this)) {
                        this.this$0.completeEdit$okhttp(this, true);
                    }
                    this.done = true;
                    Unit unit = Unit.INSTANCE;
                } else {
                    throw new IllegalStateException("Check failed.".toString());
                }
            }
        }

        public final void abort() throws IOException {
            synchronized (this.this$0) {
                if (!this.done) {
                    if (Intrinsics.areEqual((Object) this.entry.getCurrentEditor$okhttp(), (Object) this)) {
                        this.this$0.completeEdit$okhttp(this, false);
                    }
                    this.done = true;
                    Unit unit = Unit.INSTANCE;
                } else {
                    throw new IllegalStateException("Check failed.".toString());
                }
            }
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0016\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010%\u001a\u00020&2\f\u0010'\u001a\b\u0012\u0004\u0012\u00020\u00030(H\u0002J\u001b\u0010)\u001a\u00020*2\f\u0010'\u001a\b\u0012\u0004\u0012\u00020\u00030(H\u0000¢\u0006\u0002\b+J\u0013\u0010,\u001a\b\u0018\u00010-R\u00020\fH\u0000¢\u0006\u0002\b.J\u0015\u0010/\u001a\u00020*2\u0006\u00100\u001a\u000201H\u0000¢\u0006\u0002\b2R\u001a\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR \u0010\n\u001a\b\u0018\u00010\u000bR\u00020\fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\tR\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0019\u001a\u00020\u001aX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\u001f\u001a\u00020 X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$¨\u00063"}, d2 = {"Lokhttp3/internal/cache/DiskLruCache$Entry;", "", "key", "", "(Lokhttp3/internal/cache/DiskLruCache;Ljava/lang/String;)V", "cleanFiles", "", "Ljava/io/File;", "getCleanFiles$okhttp", "()Ljava/util/List;", "currentEditor", "Lokhttp3/internal/cache/DiskLruCache$Editor;", "Lokhttp3/internal/cache/DiskLruCache;", "getCurrentEditor$okhttp", "()Lokhttp3/internal/cache/DiskLruCache$Editor;", "setCurrentEditor$okhttp", "(Lokhttp3/internal/cache/DiskLruCache$Editor;)V", "dirtyFiles", "getDirtyFiles$okhttp", "getKey$okhttp", "()Ljava/lang/String;", "lengths", "", "getLengths$okhttp", "()[J", "readable", "", "getReadable$okhttp", "()Z", "setReadable$okhttp", "(Z)V", "sequenceNumber", "", "getSequenceNumber$okhttp", "()J", "setSequenceNumber$okhttp", "(J)V", "invalidLengths", "Ljava/io/IOException;", "strings", "", "setLengths", "", "setLengths$okhttp", "snapshot", "Lokhttp3/internal/cache/DiskLruCache$Snapshot;", "snapshot$okhttp", "writeLengths", "writer", "Lokio/BufferedSink;", "writeLengths$okhttp", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: DiskLruCache.kt */
    public final class Entry {
        private final List<File> cleanFiles = new ArrayList();
        private Editor currentEditor;
        private final List<File> dirtyFiles = new ArrayList();
        private final String key;
        private final long[] lengths;
        private boolean readable;
        private long sequenceNumber;
        final /* synthetic */ DiskLruCache this$0;

        public Entry(DiskLruCache diskLruCache, String str) {
            Intrinsics.checkParameterIsNotNull(str, "key");
            this.this$0 = diskLruCache;
            this.key = str;
            this.lengths = new long[diskLruCache.getValueCount$okhttp()];
            StringBuilder sb = new StringBuilder(this.key);
            sb.append('.');
            int length = sb.length();
            int valueCount$okhttp = diskLruCache.getValueCount$okhttp();
            for (int i = 0; i < valueCount$okhttp; i++) {
                sb.append(i);
                this.cleanFiles.add(new File(diskLruCache.getDirectory(), sb.toString()));
                sb.append(".tmp");
                this.dirtyFiles.add(new File(diskLruCache.getDirectory(), sb.toString()));
                sb.setLength(length);
            }
        }

        public final String getKey$okhttp() {
            return this.key;
        }

        public final long[] getLengths$okhttp() {
            return this.lengths;
        }

        public final List<File> getCleanFiles$okhttp() {
            return this.cleanFiles;
        }

        public final List<File> getDirtyFiles$okhttp() {
            return this.dirtyFiles;
        }

        public final boolean getReadable$okhttp() {
            return this.readable;
        }

        public final void setReadable$okhttp(boolean z) {
            this.readable = z;
        }

        public final Editor getCurrentEditor$okhttp() {
            return this.currentEditor;
        }

        public final void setCurrentEditor$okhttp(Editor editor) {
            this.currentEditor = editor;
        }

        public final long getSequenceNumber$okhttp() {
            return this.sequenceNumber;
        }

        public final void setSequenceNumber$okhttp(long j) {
            this.sequenceNumber = j;
        }

        public final void setLengths$okhttp(List<String> list) throws IOException {
            Intrinsics.checkParameterIsNotNull(list, "strings");
            if (list.size() == this.this$0.getValueCount$okhttp()) {
                try {
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        this.lengths[i] = Long.parseLong((String) list.get(i));
                    }
                } catch (NumberFormatException unused) {
                    throw invalidLengths(list);
                }
            } else {
                throw invalidLengths(list);
            }
        }

        public final void writeLengths$okhttp(BufferedSink bufferedSink) throws IOException {
            Intrinsics.checkParameterIsNotNull(bufferedSink, "writer");
            for (long writeDecimalLong : this.lengths) {
                bufferedSink.writeByte(32).writeDecimalLong(writeDecimalLong);
            }
        }

        private final IOException invalidLengths(List<String> list) throws IOException {
            StringBuilder sb = new StringBuilder();
            sb.append("unexpected journal line: ");
            sb.append(list);
            throw new IOException(sb.toString());
        }

        public final Snapshot snapshot$okhttp() {
            boolean holdsLock = Thread.holdsLock(this.this$0);
            if (!_Assertions.ENABLED || holdsLock) {
                List<Source> arrayList = new ArrayList<>();
                long[] jArr = (long[]) this.lengths.clone();
                try {
                    int valueCount$okhttp = this.this$0.getValueCount$okhttp();
                    for (int i = 0; i < valueCount$okhttp; i++) {
                        arrayList.add(this.this$0.getFileSystem$okhttp().source((File) this.cleanFiles.get(i)));
                    }
                    Snapshot snapshot = new Snapshot(this.this$0, this.key, this.sequenceNumber, arrayList, jArr);
                    return snapshot;
                } catch (FileNotFoundException unused) {
                    for (Source closeQuietly : arrayList) {
                        Util.closeQuietly((Closeable) closeQuietly);
                    }
                    try {
                        this.this$0.removeEntry$okhttp(this);
                    } catch (IOException unused2) {
                    }
                    return null;
                }
            } else {
                throw new AssertionError("Assertion failed");
            }
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0016\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B-\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ\b\u0010\f\u001a\u00020\rH\u0016J\f\u0010\u000e\u001a\b\u0018\u00010\u000fR\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\u0002\u001a\u00020\u0003R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lokhttp3/internal/cache/DiskLruCache$Snapshot;", "Ljava/io/Closeable;", "key", "", "sequenceNumber", "", "sources", "", "Lokio/Source;", "lengths", "", "(Lokhttp3/internal/cache/DiskLruCache;Ljava/lang/String;JLjava/util/List;[J)V", "close", "", "edit", "Lokhttp3/internal/cache/DiskLruCache$Editor;", "Lokhttp3/internal/cache/DiskLruCache;", "getLength", "index", "", "getSource", "okhttp"}, k = 1, mv = {1, 1, 15})
    /* compiled from: DiskLruCache.kt */
    public final class Snapshot implements Closeable {
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        private final List<Source> sources;
        final /* synthetic */ DiskLruCache this$0;

        public Snapshot(DiskLruCache diskLruCache, String str, long j, List<? extends Source> list, long[] jArr) {
            Intrinsics.checkParameterIsNotNull(str, "key");
            Intrinsics.checkParameterIsNotNull(list, "sources");
            Intrinsics.checkParameterIsNotNull(jArr, "lengths");
            this.this$0 = diskLruCache;
            this.key = str;
            this.sequenceNumber = j;
            this.sources = list;
            this.lengths = jArr;
        }

        public final String key() {
            return this.key;
        }

        public final Editor edit() throws IOException {
            return this.this$0.edit(this.key, this.sequenceNumber);
        }

        public final Source getSource(int i) {
            return (Source) this.sources.get(i);
        }

        public final long getLength(int i) {
            return this.lengths[i];
        }

        public void close() {
            for (Source closeQuietly : this.sources) {
                Util.closeQuietly((Closeable) closeQuietly);
            }
        }
    }

    public final Editor edit(String str) throws IOException {
        return edit$default(this, str, 0, 2, null);
    }

    public DiskLruCache(FileSystem fileSystem2, File file, int i, int i2, long j, Executor executor2) {
        Intrinsics.checkParameterIsNotNull(fileSystem2, "fileSystem");
        Intrinsics.checkParameterIsNotNull(file, "directory");
        Intrinsics.checkParameterIsNotNull(executor2, "executor");
        this.fileSystem = fileSystem2;
        this.directory = file;
        this.appVersion = i;
        this.valueCount = i2;
        this.executor = executor2;
        this.maxSize = j;
    }

    public final FileSystem getFileSystem$okhttp() {
        return this.fileSystem;
    }

    public final File getDirectory() {
        return this.directory;
    }

    public final int getValueCount$okhttp() {
        return this.valueCount;
    }

    public final synchronized long getMaxSize() {
        return this.maxSize;
    }

    public final synchronized void setMaxSize(long j) {
        this.maxSize = j;
        if (this.initialized) {
            this.executor.execute(this.cleanupRunnable);
        }
    }

    public final LinkedHashMap<String, Entry> getLruEntries$okhttp() {
        return this.lruEntries;
    }

    public final boolean getClosed$okhttp() {
        return this.closed;
    }

    public final void setClosed$okhttp(boolean z) {
        this.closed = z;
    }

    public final synchronized void initialize() throws IOException {
        boolean holdsLock = Thread.holdsLock(this);
        if (_Assertions.ENABLED) {
            if (!holdsLock) {
                throw new AssertionError("Assertion failed");
            }
        }
        if (!this.initialized) {
            if (this.fileSystem.exists(this.journalFileBackup)) {
                if (this.fileSystem.exists(this.journalFile)) {
                    this.fileSystem.delete(this.journalFileBackup);
                } else {
                    this.fileSystem.rename(this.journalFileBackup, this.journalFile);
                }
            }
            if (this.fileSystem.exists(this.journalFile)) {
                try {
                    readJournal();
                    processJournal();
                    this.initialized = true;
                    return;
                } catch (IOException e) {
                    Platform platform = Platform.Companion.get();
                    StringBuilder sb = new StringBuilder();
                    sb.append("DiskLruCache ");
                    sb.append(this.directory);
                    sb.append(" is corrupt: ");
                    sb.append(e.getMessage());
                    sb.append(", removing");
                    platform.log(5, sb.toString(), e);
                    delete();
                    this.closed = false;
                } catch (Throwable th) {
                    this.closed = false;
                    throw th;
                }
            }
            rebuildJournal$okhttp();
            this.initialized = true;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:18|19|(1:21)(1:22)|23|24|25) */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        r11.redundantOpCount = r9 - r11.lruEntries.size();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x007c, code lost:
        if (r3.exhausted() == false) goto L_0x007e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x007e, code lost:
        rebuildJournal$okhttp();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0082, code lost:
        r11.journalWriter = newJournalWriter();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0088, code lost:
        r0 = kotlin.Unit.INSTANCE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x008a, code lost:
        kotlin.io.CloseableKt.closeFinally(r1, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x008d, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00c0, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00c1, code lost:
        kotlin.io.CloseableKt.closeFinally(r1, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00c4, code lost:
        throw r2;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x006f */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:26:0x008e=Splitter:B:26:0x008e, B:18:0x006f=Splitter:B:18:0x006f} */
    private final void readJournal() throws IOException {
        String str = ", ";
        Closeable buffer = Okio.buffer(this.fileSystem.source(this.journalFile));
        Throwable th = null;
        BufferedSource bufferedSource = (BufferedSource) buffer;
        String readUtf8LineStrict = bufferedSource.readUtf8LineStrict();
        String readUtf8LineStrict2 = bufferedSource.readUtf8LineStrict();
        String readUtf8LineStrict3 = bufferedSource.readUtf8LineStrict();
        String readUtf8LineStrict4 = bufferedSource.readUtf8LineStrict();
        String readUtf8LineStrict5 = bufferedSource.readUtf8LineStrict();
        boolean z = true;
        if (!(!Intrinsics.areEqual((Object) MAGIC, (Object) readUtf8LineStrict)) && !(!Intrinsics.areEqual((Object) VERSION_1, (Object) readUtf8LineStrict2)) && !(!Intrinsics.areEqual((Object) String.valueOf(this.appVersion), (Object) readUtf8LineStrict3)) && !(!Intrinsics.areEqual((Object) String.valueOf(this.valueCount), (Object) readUtf8LineStrict4))) {
            int i = 0;
            if (readUtf8LineStrict5.length() <= 0) {
                z = false;
            }
            if (!z) {
                while (true) {
                    readJournalLine(bufferedSource.readUtf8LineStrict());
                    i++;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("unexpected journal header: [");
        sb.append(readUtf8LineStrict);
        sb.append(str);
        sb.append(readUtf8LineStrict2);
        sb.append(str);
        sb.append(readUtf8LineStrict4);
        sb.append(str);
        sb.append(readUtf8LineStrict5);
        sb.append(']');
        throw new IOException(sb.toString());
    }

    private final BufferedSink newJournalWriter() throws FileNotFoundException {
        return Okio.buffer((Sink) new FaultHidingSink(this.fileSystem.appendingSink(this.journalFile), new DiskLruCache$newJournalWriter$faultHidingSink$1(this)));
    }

    private final void readJournalLine(String str) throws IOException {
        String str2;
        String str3 = str;
        CharSequence charSequence = str3;
        int indexOf$default = StringsKt.indexOf$default(charSequence, ' ', 0, false, 6, (Object) null);
        String str4 = "unexpected journal line: ";
        if (indexOf$default != -1) {
            int i = indexOf$default + 1;
            int indexOf$default2 = StringsKt.indexOf$default(charSequence, ' ', i, false, 4, (Object) null);
            String str5 = "(this as java.lang.String).substring(startIndex)";
            String str6 = "null cannot be cast to non-null type java.lang.String";
            if (indexOf$default2 == -1) {
                if (str3 != null) {
                    str2 = str3.substring(i);
                    Intrinsics.checkExpressionValueIsNotNull(str2, str5);
                    if (indexOf$default == REMOVE.length() && StringsKt.startsWith$default(str3, REMOVE, false, 2, null)) {
                        this.lruEntries.remove(str2);
                        return;
                    }
                } else {
                    throw new TypeCastException(str6);
                }
            } else if (str3 != null) {
                str2 = str3.substring(i, indexOf$default2);
                Intrinsics.checkExpressionValueIsNotNull(str2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            } else {
                throw new TypeCastException(str6);
            }
            Entry entry = (Entry) this.lruEntries.get(str2);
            if (entry == null) {
                entry = new Entry(this, str2);
                this.lruEntries.put(str2, entry);
            }
            if (indexOf$default2 != -1 && indexOf$default == CLEAN.length() && StringsKt.startsWith$default(str3, CLEAN, false, 2, null)) {
                int i2 = indexOf$default2 + 1;
                if (str3 != null) {
                    String substring = str3.substring(i2);
                    Intrinsics.checkExpressionValueIsNotNull(substring, str5);
                    List split$default = StringsKt.split$default((CharSequence) substring, new char[]{' '}, false, 0, 6, (Object) null);
                    entry.setReadable$okhttp(true);
                    entry.setCurrentEditor$okhttp(null);
                    entry.setLengths$okhttp(split$default);
                } else {
                    throw new TypeCastException(str6);
                }
            } else if (indexOf$default2 == -1 && indexOf$default == DIRTY.length() && StringsKt.startsWith$default(str3, DIRTY, false, 2, null)) {
                entry.setCurrentEditor$okhttp(new Editor(this, entry));
            } else if (!(indexOf$default2 == -1 && indexOf$default == READ.length() && StringsKt.startsWith$default(str3, READ, false, 2, null))) {
                StringBuilder sb = new StringBuilder();
                sb.append(str4);
                sb.append(str3);
                throw new IOException(sb.toString());
            }
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str4);
        sb2.append(str3);
        throw new IOException(sb2.toString());
    }

    private final void processJournal() throws IOException {
        this.fileSystem.delete(this.journalFileTmp);
        Iterator it = this.lruEntries.values().iterator();
        while (it.hasNext()) {
            Object next = it.next();
            Intrinsics.checkExpressionValueIsNotNull(next, "i.next()");
            Entry entry = (Entry) next;
            int i = 0;
            if (entry.getCurrentEditor$okhttp() == null) {
                int i2 = this.valueCount;
                while (i < i2) {
                    this.size += entry.getLengths$okhttp()[i];
                    i++;
                }
            } else {
                entry.setCurrentEditor$okhttp(null);
                int i3 = this.valueCount;
                while (i < i3) {
                    this.fileSystem.delete((File) entry.getCleanFiles$okhttp().get(i));
                    this.fileSystem.delete((File) entry.getDirtyFiles$okhttp().get(i));
                    i++;
                }
                it.remove();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00c7, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        kotlin.io.CloseableKt.closeFinally(r0, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00cb, code lost:
        throw r2;
     */
    public final synchronized void rebuildJournal$okhttp() throws IOException {
        BufferedSink bufferedSink = this.journalWriter;
        if (bufferedSink != null) {
            bufferedSink.close();
        }
        Closeable buffer = Okio.buffer(this.fileSystem.sink(this.journalFileTmp));
        Throwable th = null;
        BufferedSink bufferedSink2 = (BufferedSink) buffer;
        bufferedSink2.writeUtf8(MAGIC).writeByte(10);
        bufferedSink2.writeUtf8(VERSION_1).writeByte(10);
        bufferedSink2.writeDecimalLong((long) this.appVersion).writeByte(10);
        bufferedSink2.writeDecimalLong((long) this.valueCount).writeByte(10);
        bufferedSink2.writeByte(10);
        for (Entry entry : this.lruEntries.values()) {
            if (entry.getCurrentEditor$okhttp() != null) {
                bufferedSink2.writeUtf8(DIRTY).writeByte(32);
                bufferedSink2.writeUtf8(entry.getKey$okhttp());
                bufferedSink2.writeByte(10);
            } else {
                bufferedSink2.writeUtf8(CLEAN).writeByte(32);
                bufferedSink2.writeUtf8(entry.getKey$okhttp());
                entry.writeLengths$okhttp(bufferedSink2);
                bufferedSink2.writeByte(10);
            }
        }
        Unit unit = Unit.INSTANCE;
        CloseableKt.closeFinally(buffer, th);
        if (this.fileSystem.exists(this.journalFile)) {
            this.fileSystem.rename(this.journalFile, this.journalFileBackup);
        }
        this.fileSystem.rename(this.journalFileTmp, this.journalFile);
        this.fileSystem.delete(this.journalFileBackup);
        this.journalWriter = newJournalWriter();
        this.hasJournalErrors = false;
        this.mostRecentRebuildFailed = false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005d, code lost:
        return r0;
     */
    public final synchronized Snapshot get(String str) throws IOException {
        Intrinsics.checkParameterIsNotNull(str, "key");
        initialize();
        checkNotClosed();
        validateKey(str);
        Entry entry = (Entry) this.lruEntries.get(str);
        if (entry == null) {
            return null;
        }
        Intrinsics.checkExpressionValueIsNotNull(entry, "lruEntries[key] ?: return null");
        if (!entry.getReadable$okhttp()) {
            return null;
        }
        Snapshot snapshot$okhttp = entry.snapshot$okhttp();
        if (snapshot$okhttp == null) {
            return null;
        }
        this.redundantOpCount++;
        BufferedSink bufferedSink = this.journalWriter;
        if (bufferedSink == null) {
            Intrinsics.throwNpe();
        }
        bufferedSink.writeUtf8(READ).writeByte(32).writeUtf8(str).writeByte(10);
        if (journalRebuildRequired()) {
            this.executor.execute(this.cleanupRunnable);
        }
    }

    public static /* synthetic */ Editor edit$default(DiskLruCache diskLruCache, String str, long j, int i, Object obj) throws IOException {
        if ((i & 2) != 0) {
            j = ANY_SEQUENCE_NUMBER;
        }
        return diskLruCache.edit(str, j);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0029, code lost:
        return null;
     */
    public final synchronized Editor edit(String str, long j) throws IOException {
        Intrinsics.checkParameterIsNotNull(str, "key");
        initialize();
        checkNotClosed();
        validateKey(str);
        Entry entry = (Entry) this.lruEntries.get(str);
        if (j == ANY_SEQUENCE_NUMBER || (entry != null && entry.getSequenceNumber$okhttp() == j)) {
            if ((entry != null ? entry.getCurrentEditor$okhttp() : null) != null) {
                return null;
            }
            if (!this.mostRecentTrimFailed) {
                if (!this.mostRecentRebuildFailed) {
                    BufferedSink bufferedSink = this.journalWriter;
                    if (bufferedSink == null) {
                        Intrinsics.throwNpe();
                    }
                    bufferedSink.writeUtf8(DIRTY).writeByte(32).writeUtf8(str).writeByte(10);
                    bufferedSink.flush();
                    if (this.hasJournalErrors) {
                        return null;
                    }
                    if (entry == null) {
                        entry = new Entry(this, str);
                        this.lruEntries.put(str, entry);
                    }
                    Editor editor = new Editor(this, entry);
                    entry.setCurrentEditor$okhttp(editor);
                    return editor;
                }
            }
            this.executor.execute(this.cleanupRunnable);
            return null;
        }
    }

    public final synchronized long size() throws IOException {
        initialize();
        return this.size;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0128, code lost:
        return;
     */
    public final synchronized void completeEdit$okhttp(Editor editor, boolean z) throws IOException {
        Intrinsics.checkParameterIsNotNull(editor, "editor");
        Entry entry$okhttp = editor.getEntry$okhttp();
        if (Intrinsics.areEqual((Object) entry$okhttp.getCurrentEditor$okhttp(), (Object) editor)) {
            if (z && !entry$okhttp.getReadable$okhttp()) {
                int i = this.valueCount;
                int i2 = 0;
                while (i2 < i) {
                    boolean[] written$okhttp = editor.getWritten$okhttp();
                    if (written$okhttp == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!written$okhttp[i2]) {
                        editor.abort();
                        StringBuilder sb = new StringBuilder();
                        sb.append("Newly created entry didn't create value for index ");
                        sb.append(i2);
                        throw new IllegalStateException(sb.toString());
                    } else if (!this.fileSystem.exists((File) entry$okhttp.getDirtyFiles$okhttp().get(i2))) {
                        editor.abort();
                        return;
                    } else {
                        i2++;
                    }
                }
            }
            int i3 = this.valueCount;
            for (int i4 = 0; i4 < i3; i4++) {
                File file = (File) entry$okhttp.getDirtyFiles$okhttp().get(i4);
                if (!z) {
                    this.fileSystem.delete(file);
                } else if (this.fileSystem.exists(file)) {
                    File file2 = (File) entry$okhttp.getCleanFiles$okhttp().get(i4);
                    this.fileSystem.rename(file, file2);
                    long j = entry$okhttp.getLengths$okhttp()[i4];
                    long size2 = this.fileSystem.size(file2);
                    entry$okhttp.getLengths$okhttp()[i4] = size2;
                    this.size = (this.size - j) + size2;
                }
            }
            this.redundantOpCount++;
            entry$okhttp.setCurrentEditor$okhttp(null);
            BufferedSink bufferedSink = this.journalWriter;
            if (bufferedSink == null) {
                Intrinsics.throwNpe();
            }
            if (!entry$okhttp.getReadable$okhttp()) {
                if (!z) {
                    this.lruEntries.remove(entry$okhttp.getKey$okhttp());
                    bufferedSink.writeUtf8(REMOVE).writeByte(32);
                    bufferedSink.writeUtf8(entry$okhttp.getKey$okhttp());
                    bufferedSink.writeByte(10);
                    bufferedSink.flush();
                    if (this.size > this.maxSize || journalRebuildRequired()) {
                        this.executor.execute(this.cleanupRunnable);
                    }
                }
            }
            entry$okhttp.setReadable$okhttp(true);
            bufferedSink.writeUtf8(CLEAN).writeByte(32);
            bufferedSink.writeUtf8(entry$okhttp.getKey$okhttp());
            entry$okhttp.writeLengths$okhttp(bufferedSink);
            bufferedSink.writeByte(10);
            if (z) {
                long j2 = this.nextSequenceNumber;
                this.nextSequenceNumber = 1 + j2;
                entry$okhttp.setSequenceNumber$okhttp(j2);
            }
            bufferedSink.flush();
            this.executor.execute(this.cleanupRunnable);
        } else {
            throw new IllegalStateException("Check failed.".toString());
        }
    }

    /* access modifiers changed from: private */
    public final boolean journalRebuildRequired() {
        int i = this.redundantOpCount;
        return i >= 2000 && i >= this.lruEntries.size();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0030, code lost:
        return r7;
     */
    public final synchronized boolean remove(String str) throws IOException {
        Intrinsics.checkParameterIsNotNull(str, "key");
        initialize();
        checkNotClosed();
        validateKey(str);
        Entry entry = (Entry) this.lruEntries.get(str);
        if (entry == null) {
            return false;
        }
        Intrinsics.checkExpressionValueIsNotNull(entry, "lruEntries[key] ?: return false");
        boolean removeEntry$okhttp = removeEntry$okhttp(entry);
        if (removeEntry$okhttp && this.size <= this.maxSize) {
            this.mostRecentTrimFailed = false;
        }
    }

    public final boolean removeEntry$okhttp(Entry entry) throws IOException {
        Intrinsics.checkParameterIsNotNull(entry, "entry");
        Editor currentEditor$okhttp = entry.getCurrentEditor$okhttp();
        if (currentEditor$okhttp != null) {
            currentEditor$okhttp.detach$okhttp();
        }
        int i = this.valueCount;
        for (int i2 = 0; i2 < i; i2++) {
            this.fileSystem.delete((File) entry.getCleanFiles$okhttp().get(i2));
            this.size -= entry.getLengths$okhttp()[i2];
            entry.getLengths$okhttp()[i2] = 0;
        }
        this.redundantOpCount++;
        BufferedSink bufferedSink = this.journalWriter;
        if (bufferedSink == null) {
            Intrinsics.throwNpe();
        }
        bufferedSink.writeUtf8(REMOVE).writeByte(32).writeUtf8(entry.getKey$okhttp()).writeByte(10);
        this.lruEntries.remove(entry.getKey$okhttp());
        if (journalRebuildRequired()) {
            this.executor.execute(this.cleanupRunnable);
        }
        return true;
    }

    private final synchronized void checkNotClosed() {
        if (!(!this.closed)) {
            throw new IllegalStateException("cache is closed".toString());
        }
    }

    public synchronized void flush() throws IOException {
        if (this.initialized) {
            checkNotClosed();
            trimToSize();
            BufferedSink bufferedSink = this.journalWriter;
            if (bufferedSink == null) {
                Intrinsics.throwNpe();
            }
            bufferedSink.flush();
        }
    }

    public final synchronized boolean isClosed() {
        return this.closed;
    }

    public synchronized void close() throws IOException {
        Entry[] entryArr;
        if (this.initialized) {
            if (!this.closed) {
                Collection values = this.lruEntries.values();
                Intrinsics.checkExpressionValueIsNotNull(values, "lruEntries.values");
                Object[] array = values.toArray(new Entry[0]);
                if (array != null) {
                    for (Entry entry : (Entry[]) array) {
                        if (entry.getCurrentEditor$okhttp() != null) {
                            Editor currentEditor$okhttp = entry.getCurrentEditor$okhttp();
                            if (currentEditor$okhttp == null) {
                                Intrinsics.throwNpe();
                            }
                            currentEditor$okhttp.abort();
                        }
                    }
                    trimToSize();
                    BufferedSink bufferedSink = this.journalWriter;
                    if (bufferedSink == null) {
                        Intrinsics.throwNpe();
                    }
                    bufferedSink.close();
                    this.journalWriter = null;
                    this.closed = true;
                    return;
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
            }
        }
        this.closed = true;
    }

    public final void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            Object next = this.lruEntries.values().iterator().next();
            Intrinsics.checkExpressionValueIsNotNull(next, "lruEntries.values.iterator().next()");
            removeEntry$okhttp((Entry) next);
        }
        this.mostRecentTrimFailed = false;
    }

    public final void delete() throws IOException {
        close();
        this.fileSystem.deleteContents(this.directory);
    }

    public final synchronized void evictAll() throws IOException {
        Entry[] entryArr;
        initialize();
        Collection values = this.lruEntries.values();
        Intrinsics.checkExpressionValueIsNotNull(values, "lruEntries.values");
        Object[] array = values.toArray(new Entry[0]);
        if (array != null) {
            for (Entry entry : (Entry[]) array) {
                Intrinsics.checkExpressionValueIsNotNull(entry, "entry");
                removeEntry$okhttp(entry);
            }
            this.mostRecentTrimFailed = false;
        } else {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
    }

    private final void validateKey(String str) {
        if (!LEGAL_KEY_PATTERN.matches(str)) {
            StringBuilder sb = new StringBuilder();
            sb.append("keys must match regex [a-z0-9_-]{1,120}: \"");
            sb.append(str);
            sb.append(Typography.quote);
            throw new IllegalArgumentException(sb.toString().toString());
        }
    }

    public final synchronized Iterator<Snapshot> snapshots() throws IOException {
        initialize();
        return new DiskLruCache$snapshots$1<>(this);
    }
}
