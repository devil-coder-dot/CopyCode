package okhttp3.internal.cache;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "it", "Ljava/io/IOException;", "invoke"}, k = 3, mv = {1, 1, 15})
/* compiled from: DiskLruCache.kt */
final class DiskLruCache$newJournalWriter$faultHidingSink$1 extends Lambda implements Function1<IOException, Unit> {
    final /* synthetic */ DiskLruCache this$0;

    DiskLruCache$newJournalWriter$faultHidingSink$1(DiskLruCache diskLruCache) {
        this.this$0 = diskLruCache;
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((IOException) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(IOException iOException) {
        Intrinsics.checkParameterIsNotNull(iOException, "it");
        boolean holdsLock = Thread.holdsLock(this.this$0);
        if (!_Assertions.ENABLED || holdsLock) {
            this.this$0.hasJournalErrors = true;
            return;
        }
        throw new AssertionError("Assertion failed");
    }
}
