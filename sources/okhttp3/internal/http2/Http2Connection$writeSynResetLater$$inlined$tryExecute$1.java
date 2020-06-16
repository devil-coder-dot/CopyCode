package okhttp3.internal.http2;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"}, d2 = {"<anonymous>", "", "run", "okhttp3/internal/Util$execute$1"}, k = 3, mv = {1, 1, 15})
/* compiled from: Util.kt */
public final class Http2Connection$writeSynResetLater$$inlined$tryExecute$1 implements Runnable {
    final /* synthetic */ ErrorCode $errorCode$inlined;
    final /* synthetic */ String $name;
    final /* synthetic */ int $streamId$inlined;
    final /* synthetic */ Http2Connection this$0;

    public Http2Connection$writeSynResetLater$$inlined$tryExecute$1(String str, Http2Connection http2Connection, int i, ErrorCode errorCode) {
        this.$name = str;
        this.this$0 = http2Connection;
        this.$streamId$inlined = i;
        this.$errorCode$inlined = errorCode;
    }

    public final void run() {
        String str = this.$name;
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "currentThread");
        String name = currentThread.getName();
        currentThread.setName(str);
        try {
            this.this$0.writeSynReset$okhttp(this.$streamId$inlined, this.$errorCode$inlined);
        } catch (IOException e) {
            this.this$0.failConnection(e);
        } catch (Throwable th) {
            currentThread.setName(name);
            throw th;
        }
        currentThread.setName(name);
    }
}
