package kotlin.collections;

import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequenceScope;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003H@¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "T", "Lkotlin/sequences/SequenceScope;", "", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 15})
@DebugMetadata(c = "kotlin.collections.SlidingWindowKt$windowedIterator$1", f = "SlidingWindow.kt", i = {0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4}, l = {33, 39, 46, 52, 55}, m = "invokeSuspend", n = {"$this$iterator", "gap", "buffer", "skip", "e", "$this$iterator", "gap", "buffer", "skip", "$this$iterator", "gap", "buffer", "e", "$this$iterator", "gap", "buffer", "$this$iterator", "gap", "buffer"}, s = {"L$0", "I$0", "L$1", "I$1", "L$2", "L$0", "I$0", "L$1", "I$1", "L$0", "I$0", "L$1", "L$2", "L$0", "I$0", "L$1", "L$0", "I$0", "L$1"})
/* compiled from: SlidingWindow.kt */
final class SlidingWindowKt$windowedIterator$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super List<? extends T>>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Iterator $iterator;
    final /* synthetic */ boolean $partialWindows;
    final /* synthetic */ boolean $reuseBuffer;
    final /* synthetic */ int $size;
    final /* synthetic */ int $step;
    int I$0;
    int I$1;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    private SequenceScope p$;

    SlidingWindowKt$windowedIterator$1(int i, int i2, Iterator it, boolean z, boolean z2, Continuation continuation) {
        this.$step = i;
        this.$size = i2;
        this.$iterator = it;
        this.$reuseBuffer = z;
        this.$partialWindows = z2;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        SlidingWindowKt$windowedIterator$1 slidingWindowKt$windowedIterator$1 = new SlidingWindowKt$windowedIterator$1(this.$step, this.$size, this.$iterator, this.$reuseBuffer, this.$partialWindows, continuation);
        slidingWindowKt$windowedIterator$1.p$ = (SequenceScope) obj;
        return slidingWindowKt$windowedIterator$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((SlidingWindowKt$windowedIterator$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Regions count limit reached
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
        	at java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
        	at jadx.core.ProcessClass.process(ProcessClass.java:35)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
        */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0089  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00b7  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00bb  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00eb A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0100  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x013c  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0147  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x00c5 A[SYNTHETIC] */
    public final java.lang.Object invokeSuspend(java.lang.Object r14) {
        /*
            r13 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r13.label
            r2 = 5
            r3 = 4
            r4 = 3
            r5 = 2
            r6 = 1
            if (r1 == 0) goto L_0x0068
            if (r1 == r6) goto L_0x0055
            if (r1 == r5) goto L_0x0048
            if (r1 == r4) goto L_0x0034
            if (r1 == r3) goto L_0x0024
            if (r1 != r2) goto L_0x001c
            java.lang.Object r0 = r13.L$1
            kotlin.collections.RingBuffer r0 = (kotlin.collections.RingBuffer) r0
            goto L_0x004c
        L_0x001c:
            java.lang.IllegalStateException r14 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r14.<init>(r0)
            throw r14
        L_0x0024:
            java.lang.Object r1 = r13.L$1
            kotlin.collections.RingBuffer r1 = (kotlin.collections.RingBuffer) r1
            int r4 = r13.I$0
            java.lang.Object r5 = r13.L$0
            kotlin.sequences.SequenceScope r5 = (kotlin.sequences.SequenceScope) r5
            kotlin.ResultKt.throwOnFailure(r14)
            r14 = r13
            goto L_0x0168
        L_0x0034:
            java.lang.Object r1 = r13.L$3
            java.util.Iterator r1 = (java.util.Iterator) r1
            java.lang.Object r5 = r13.L$1
            kotlin.collections.RingBuffer r5 = (kotlin.collections.RingBuffer) r5
            int r7 = r13.I$0
            java.lang.Object r8 = r13.L$0
            kotlin.sequences.SequenceScope r8 = (kotlin.sequences.SequenceScope) r8
            kotlin.ResultKt.throwOnFailure(r14)
            r14 = r13
            goto L_0x0132
        L_0x0048:
            java.lang.Object r0 = r13.L$1
            java.util.ArrayList r0 = (java.util.ArrayList) r0
        L_0x004c:
            java.lang.Object r0 = r13.L$0
            kotlin.sequences.SequenceScope r0 = (kotlin.sequences.SequenceScope) r0
            kotlin.ResultKt.throwOnFailure(r14)
            goto L_0x0187
        L_0x0055:
            java.lang.Object r1 = r13.L$3
            java.util.Iterator r1 = (java.util.Iterator) r1
            java.lang.Object r2 = r13.L$1
            java.util.ArrayList r2 = (java.util.ArrayList) r2
            int r3 = r13.I$0
            java.lang.Object r4 = r13.L$0
            kotlin.sequences.SequenceScope r4 = (kotlin.sequences.SequenceScope) r4
            kotlin.ResultKt.throwOnFailure(r14)
            r7 = r13
            goto L_0x00b3
        L_0x0068:
            kotlin.ResultKt.throwOnFailure(r14)
            kotlin.sequences.SequenceScope r14 = r13.p$
            int r1 = r13.$step
            int r7 = r13.$size
            int r1 = r1 - r7
            if (r1 < 0) goto L_0x00ec
            java.util.ArrayList r2 = new java.util.ArrayList
            int r3 = r13.$size
            r2.<init>(r3)
            r3 = 0
            java.util.Iterator r4 = r13.$iterator
            r7 = r13
            r12 = r4
            r4 = r14
            r14 = r1
            r1 = r12
        L_0x0083:
            boolean r8 = r1.hasNext()
            if (r8 == 0) goto L_0x00c5
            java.lang.Object r8 = r1.next()
            if (r3 <= 0) goto L_0x0092
            int r3 = r3 + -1
            goto L_0x0083
        L_0x0092:
            r2.add(r8)
            int r9 = r2.size()
            int r10 = r7.$size
            if (r9 != r10) goto L_0x0083
            r7.L$0 = r4
            r7.I$0 = r14
            r7.L$1 = r2
            r7.I$1 = r3
            r7.L$2 = r8
            r7.L$3 = r1
            r7.label = r6
            java.lang.Object r3 = r4.yield(r2, r7)
            if (r3 != r0) goto L_0x00b2
            return r0
        L_0x00b2:
            r3 = r14
        L_0x00b3:
            boolean r14 = r7.$reuseBuffer
            if (r14 == 0) goto L_0x00bb
            r2.clear()
            goto L_0x00c3
        L_0x00bb:
            java.util.ArrayList r14 = new java.util.ArrayList
            int r2 = r7.$size
            r14.<init>(r2)
            r2 = r14
        L_0x00c3:
            r14 = r3
            goto L_0x0083
        L_0x00c5:
            r1 = r2
            java.util.Collection r1 = (java.util.Collection) r1
            boolean r1 = r1.isEmpty()
            r1 = r1 ^ r6
            if (r1 == 0) goto L_0x0187
            boolean r1 = r7.$partialWindows
            if (r1 != 0) goto L_0x00db
            int r1 = r2.size()
            int r6 = r7.$size
            if (r1 != r6) goto L_0x0187
        L_0x00db:
            r7.L$0 = r4
            r7.I$0 = r14
            r7.L$1 = r2
            r7.I$1 = r3
            r7.label = r5
            java.lang.Object r14 = r4.yield(r2, r7)
            if (r14 != r0) goto L_0x0187
            return r0
        L_0x00ec:
            kotlin.collections.RingBuffer r5 = new kotlin.collections.RingBuffer
            int r7 = r13.$size
            r5.<init>(r7)
            java.util.Iterator r7 = r13.$iterator
            r8 = r14
            r14 = r13
            r12 = r7
            r7 = r1
            r1 = r12
        L_0x00fa:
            boolean r9 = r1.hasNext()
            if (r9 == 0) goto L_0x0138
            java.lang.Object r9 = r1.next()
            r5.add(r9)
            boolean r10 = r5.isFull()
            if (r10 == 0) goto L_0x00fa
            boolean r10 = r14.$reuseBuffer
            if (r10 == 0) goto L_0x0115
            r10 = r5
            java.util.List r10 = (java.util.List) r10
            goto L_0x011f
        L_0x0115:
            java.util.ArrayList r10 = new java.util.ArrayList
            r11 = r5
            java.util.Collection r11 = (java.util.Collection) r11
            r10.<init>(r11)
            java.util.List r10 = (java.util.List) r10
        L_0x011f:
            r14.L$0 = r8
            r14.I$0 = r7
            r14.L$1 = r5
            r14.L$2 = r9
            r14.L$3 = r1
            r14.label = r4
            java.lang.Object r9 = r8.yield(r10, r14)
            if (r9 != r0) goto L_0x0132
            return r0
        L_0x0132:
            int r9 = r14.$step
            r5.removeFirst(r9)
            goto L_0x00fa
        L_0x0138:
            boolean r1 = r14.$partialWindows
            if (r1 == 0) goto L_0x0187
            r1 = r5
            r4 = r7
            r5 = r8
        L_0x013f:
            int r7 = r1.size()
            int r8 = r14.$step
            if (r7 <= r8) goto L_0x016e
            boolean r7 = r14.$reuseBuffer
            if (r7 == 0) goto L_0x014f
            r7 = r1
            java.util.List r7 = (java.util.List) r7
            goto L_0x0159
        L_0x014f:
            java.util.ArrayList r7 = new java.util.ArrayList
            r8 = r1
            java.util.Collection r8 = (java.util.Collection) r8
            r7.<init>(r8)
            java.util.List r7 = (java.util.List) r7
        L_0x0159:
            r14.L$0 = r5
            r14.I$0 = r4
            r14.L$1 = r1
            r14.label = r3
            java.lang.Object r7 = r5.yield(r7, r14)
            if (r7 != r0) goto L_0x0168
            return r0
        L_0x0168:
            int r7 = r14.$step
            r1.removeFirst(r7)
            goto L_0x013f
        L_0x016e:
            r3 = r1
            java.util.Collection r3 = (java.util.Collection) r3
            boolean r3 = r3.isEmpty()
            r3 = r3 ^ r6
            if (r3 == 0) goto L_0x0187
            r14.L$0 = r5
            r14.I$0 = r4
            r14.L$1 = r1
            r14.label = r2
            java.lang.Object r14 = r5.yield(r1, r14)
            if (r14 != r0) goto L_0x0187
            return r0
        L_0x0187:
            kotlin.Unit r14 = kotlin.Unit.INSTANCE
            return r14
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.SlidingWindowKt$windowedIterator$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
