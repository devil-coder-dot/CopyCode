package okio;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0005H\u0016J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\u0018\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"okio/Pipe$sink$1", "Lokio/Sink;", "timeout", "Lokio/Timeout;", "close", "", "flush", "write", "source", "Lokio/Buffer;", "byteCount", "", "jvm"}, k = 1, mv = {1, 1, 11})
/* compiled from: Pipe.kt */
public final class Pipe$sink$1 implements Sink {
    final /* synthetic */ Pipe this$0;
    private final Timeout timeout = new Timeout();

    Pipe$sink$1(Pipe pipe) {
        this.this$0 = pipe;
    }

    public void write(Buffer buffer, long j) {
        Intrinsics.checkParameterIsNotNull(buffer, "source");
        Sink sink = null;
        synchronized (this.this$0.getBuffer$jvm()) {
            if (!this.this$0.getSinkClosed$jvm()) {
                while (true) {
                    if (j <= 0) {
                        break;
                    }
                    Sink foldedSink$jvm = this.this$0.getFoldedSink$jvm();
                    if (foldedSink$jvm != null) {
                        sink = foldedSink$jvm;
                        break;
                    } else if (!this.this$0.getSourceClosed$jvm()) {
                        long maxBufferSize$jvm = this.this$0.getMaxBufferSize$jvm() - this.this$0.getBuffer$jvm().size();
                        if (maxBufferSize$jvm == 0) {
                            this.timeout.waitUntilNotified(this.this$0.getBuffer$jvm());
                        } else {
                            long min = Math.min(maxBufferSize$jvm, j);
                            this.this$0.getBuffer$jvm().write(buffer, min);
                            j -= min;
                            Buffer buffer$jvm = this.this$0.getBuffer$jvm();
                            if (buffer$jvm != null) {
                                buffer$jvm.notifyAll();
                            } else {
                                throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
                            }
                        }
                    } else {
                        throw new IOException("source is closed");
                    }
                }
                Unit unit = Unit.INSTANCE;
            } else {
                throw new IllegalStateException("closed".toString());
            }
        }
        if (sink != null) {
            Pipe pipe = this.this$0;
            Timeout timeout2 = sink.timeout();
            Timeout timeout3 = pipe.sink().timeout();
            long timeoutNanos = timeout2.timeoutNanos();
            timeout2.timeout(Timeout.Companion.minTimeout(timeout3.timeoutNanos(), timeout2.timeoutNanos()), TimeUnit.NANOSECONDS);
            if (timeout2.hasDeadline()) {
                long deadlineNanoTime = timeout2.deadlineNanoTime();
                if (timeout3.hasDeadline()) {
                    timeout2.deadlineNanoTime(Math.min(timeout2.deadlineNanoTime(), timeout3.deadlineNanoTime()));
                }
                try {
                    sink.write(buffer, j);
                } finally {
                    timeout2.timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                    if (timeout3.hasDeadline()) {
                        timeout2.deadlineNanoTime(deadlineNanoTime);
                    }
                }
            } else {
                if (timeout3.hasDeadline()) {
                    timeout2.deadlineNanoTime(timeout3.deadlineNanoTime());
                }
                try {
                    sink.write(buffer, j);
                } finally {
                    timeout2.timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                    if (timeout3.hasDeadline()) {
                        timeout2.clearDeadline();
                    }
                }
            }
        }
    }

    public void flush() {
        Sink sink = null;
        synchronized (this.this$0.getBuffer$jvm()) {
            if (!this.this$0.getSinkClosed$jvm()) {
                Sink foldedSink$jvm = this.this$0.getFoldedSink$jvm();
                if (foldedSink$jvm != null) {
                    sink = foldedSink$jvm;
                } else if (this.this$0.getSourceClosed$jvm()) {
                    if (this.this$0.getBuffer$jvm().size() > 0) {
                        throw new IOException("source is closed");
                    }
                }
                Unit unit = Unit.INSTANCE;
            } else {
                throw new IllegalStateException("closed".toString());
            }
        }
        if (sink != null) {
            Pipe pipe = this.this$0;
            Timeout timeout2 = sink.timeout();
            Timeout timeout3 = pipe.sink().timeout();
            long timeoutNanos = timeout2.timeoutNanos();
            timeout2.timeout(Timeout.Companion.minTimeout(timeout3.timeoutNanos(), timeout2.timeoutNanos()), TimeUnit.NANOSECONDS);
            if (timeout2.hasDeadline()) {
                long deadlineNanoTime = timeout2.deadlineNanoTime();
                if (timeout3.hasDeadline()) {
                    timeout2.deadlineNanoTime(Math.min(timeout2.deadlineNanoTime(), timeout3.deadlineNanoTime()));
                }
                try {
                    sink.flush();
                } finally {
                    timeout2.timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                    if (timeout3.hasDeadline()) {
                        timeout2.deadlineNanoTime(deadlineNanoTime);
                    }
                }
            } else {
                if (timeout3.hasDeadline()) {
                    timeout2.deadlineNanoTime(timeout3.deadlineNanoTime());
                }
                try {
                    sink.flush();
                } finally {
                    timeout2.timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                    if (timeout3.hasDeadline()) {
                        timeout2.clearDeadline();
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0057, code lost:
        if (r0 == null) goto L_0x00ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0059, code lost:
        r1 = r11.this$0;
        r2 = r0.timeout();
        r1 = r1.sink().timeout();
        r3 = r2.timeoutNanos();
        r2.timeout(okio.Timeout.Companion.minTimeout(r1.timeoutNanos(), r2.timeoutNanos()), java.util.concurrent.TimeUnit.NANOSECONDS);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0082, code lost:
        if (r2.hasDeadline() == false) goto L_0x00bf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0084, code lost:
        r5 = r2.deadlineNanoTime();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x008c, code lost:
        if (r1.hasDeadline() == false) goto L_0x009d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x008e, code lost:
        r2.deadlineNanoTime(java.lang.Math.min(r2.deadlineNanoTime(), r1.deadlineNanoTime()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00af, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00b0, code lost:
        r2.timeout(r3, java.util.concurrent.TimeUnit.NANOSECONDS);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00b9, code lost:
        if (r1.hasDeadline() != false) goto L_0x00bb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00bb, code lost:
        r2.deadlineNanoTime(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00be, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00c3, code lost:
        if (r1.hasDeadline() == false) goto L_0x00cc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00c5, code lost:
        r2.deadlineNanoTime(r1.deadlineNanoTime());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00de, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00df, code lost:
        r2.timeout(r3, java.util.concurrent.TimeUnit.NANOSECONDS);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00e8, code lost:
        if (r1.hasDeadline() != false) goto L_0x00ea;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00ea, code lost:
        r2.clearDeadline();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00ed, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00ee, code lost:
        return;
     */
    public void close() {
        Sink sink = null;
        synchronized (this.this$0.getBuffer$jvm()) {
            if (!this.this$0.getSinkClosed$jvm()) {
                Sink foldedSink$jvm = this.this$0.getFoldedSink$jvm();
                if (foldedSink$jvm != null) {
                    sink = foldedSink$jvm;
                } else {
                    if (this.this$0.getSourceClosed$jvm()) {
                        if (this.this$0.getBuffer$jvm().size() > 0) {
                            throw new IOException("source is closed");
                        }
                    }
                    this.this$0.setSinkClosed$jvm(true);
                    Buffer buffer$jvm = this.this$0.getBuffer$jvm();
                    if (buffer$jvm != null) {
                        buffer$jvm.notifyAll();
                    } else {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.Object");
                    }
                }
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    public Timeout timeout() {
        return this.timeout;
    }
}
