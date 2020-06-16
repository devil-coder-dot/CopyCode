package okhttp3;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u0007"}, d2 = {"okhttp3/Dns$Companion$SYSTEM$1", "Lokhttp3/Dns;", "lookup", "", "Ljava/net/InetAddress;", "hostname", "", "okhttp"}, k = 1, mv = {1, 1, 15})
/* compiled from: Dns.kt */
public final class Dns$Companion$SYSTEM$1 implements Dns {
    Dns$Companion$SYSTEM$1() {
    }

    public List<InetAddress> lookup(String str) {
        Intrinsics.checkParameterIsNotNull(str, "hostname");
        try {
            InetAddress[] allByName = InetAddress.getAllByName(str);
            Intrinsics.checkExpressionValueIsNotNull(allByName, "InetAddress.getAllByName(hostname)");
            return ArraysKt.toList((T[]) allByName);
        } catch (NullPointerException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Broken system behaviour for dns lookup of ");
            sb.append(str);
            UnknownHostException unknownHostException = new UnknownHostException(sb.toString());
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }
}
