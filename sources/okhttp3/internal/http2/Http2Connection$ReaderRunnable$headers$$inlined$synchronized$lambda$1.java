package okhttp3.internal.http2;

import java.io.IOException;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.http2.Http2Connection.ReaderRunnable;
import okhttp3.internal.platform.Platform;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0004"}, d2 = {"<anonymous>", "", "run", "okhttp3/internal/Util$execute$1", "okhttp3/internal/http2/Http2Connection$ReaderRunnable$$special$$inlined$execute$1"}, k = 3, mv = {1, 1, 15})
/* compiled from: Util.kt */
public final class Http2Connection$ReaderRunnable$headers$$inlined$synchronized$lambda$1 implements Runnable {
    final /* synthetic */ List $headerBlock$inlined;
    final /* synthetic */ boolean $inFinished$inlined;
    final /* synthetic */ String $name;
    final /* synthetic */ Http2Stream $newStream$inlined;
    final /* synthetic */ Http2Stream $stream$inlined;
    final /* synthetic */ int $streamId$inlined;
    final /* synthetic */ ReaderRunnable this$0;

    public Http2Connection$ReaderRunnable$headers$$inlined$synchronized$lambda$1(String str, Http2Stream http2Stream, ReaderRunnable readerRunnable, Http2Stream http2Stream2, int i, List list, boolean z) {
        this.$name = str;
        this.$newStream$inlined = http2Stream;
        this.this$0 = readerRunnable;
        this.$stream$inlined = http2Stream2;
        this.$streamId$inlined = i;
        this.$headerBlock$inlined = list;
        this.$inFinished$inlined = z;
    }

    public final void run() {
        String str = this.$name;
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
        String name = currentThread.getName();
        currentThread.setName(str);
        try {
            this.this$0.this$0.getListener$okhttp().onStream(this.$newStream$inlined);
        } catch (IOException e) {
            Platform platform = Platform.Companion.get();
            StringBuilder sb = new StringBuilder();
            sb.append("Http2Connection.Listener failure for ");
            sb.append(this.this$0.this$0.getConnectionName$okhttp());
            platform.log(4, sb.toString(), e);
            try {
                this.$newStream$inlined.close(ErrorCode.PROTOCOL_ERROR, e);
            } catch (IOException unused) {
            }
        } catch (Throwable th) {
            currentThread.setName(name);
            throw th;
        }
        currentThread.setName(name);
    }
}
