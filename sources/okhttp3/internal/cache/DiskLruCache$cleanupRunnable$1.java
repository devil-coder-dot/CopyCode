package okhttp3.internal.cache;

import kotlin.Metadata;
import kotlin.Unit;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 15})
/* compiled from: DiskLruCache.kt */
final class DiskLruCache$cleanupRunnable$1 implements Runnable {
    final /* synthetic */ DiskLruCache this$0;

    DiskLruCache$cleanupRunnable$1(DiskLruCache diskLruCache) {
        this.this$0 = diskLruCache;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        r4.this$0.mostRecentRebuildFailed = true;
        r4.this$0.journalWriter = okio.Okio.buffer(okio.Okio.blackhole());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004b, code lost:
        return;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x001b */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x0034 */
    public final void run() {
        synchronized (this.this$0) {
            if (this.this$0.initialized && !this.this$0.getClosed$okhttp()) {
                this.this$0.trimToSize();
                this.this$0.mostRecentTrimFailed = true;
                if (this.this$0.journalRebuildRequired()) {
                    this.this$0.rebuildJournal$okhttp();
                    this.this$0.redundantOpCount = 0;
                }
                Unit unit = Unit.INSTANCE;
                return;
            }
        }
    }
}
