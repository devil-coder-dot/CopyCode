package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import android.os.DeadObjectException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.SimpleClientAdapter;

public final class zaah implements zabd {
    /* access modifiers changed from: private */
    public final zabe zaft;
    private boolean zafu = false;

    public zaah(zabe zabe) {
        this.zaft = zabe;
    }

    public final void begin() {
    }

    public final void onConnected(Bundle bundle) {
    }

    public final void zaa(ConnectionResult connectionResult, Api<?> api, boolean z) {
    }

    public final <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(T t) {
        return execute(t);
    }

    /* JADX WARNING: type inference failed for: r0v11, types: [com.google.android.gms.common.api.Api$SimpleClient] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    public final <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(T t) {
        try {
            this.zaft.zaee.zahf.zab(t);
            zaaw zaaw = this.zaft.zaee;
            Client client = (Client) zaaw.zagz.get(t.getClientKey());
            Preconditions.checkNotNull(client, "Appropriate Api was not requested.");
            if (client.isConnected() || !this.zaft.zahp.containsKey(t.getClientKey())) {
                if (client instanceof SimpleClientAdapter) {
                    client = ((SimpleClientAdapter) client).getClient();
                }
                t.run(client);
                return t;
            }
            t.setFailedResult(new Status(17));
            return t;
        } catch (DeadObjectException unused) {
            this.zaft.zaa((zabf) new zaai(this, this));
        }
    }

    public final boolean disconnect() {
        if (this.zafu) {
            return false;
        }
        if (this.zaft.zaee.zaax()) {
            this.zafu = true;
            for (zacm zabv : this.zaft.zaee.zahe) {
                zabv.zabv();
            }
            return false;
        }
        this.zaft.zaf(null);
        return true;
    }

    public final void connect() {
        if (this.zafu) {
            this.zafu = false;
            this.zaft.zaa((zabf) new zaaj(this, this));
        }
    }

    public final void onConnectionSuspended(int i) {
        this.zaft.zaf(null);
        this.zaft.zaht.zab(i, this.zafu);
    }

    /* access modifiers changed from: 0000 */
    public final void zaam() {
        if (this.zafu) {
            this.zafu = false;
            this.zaft.zaee.zahf.release();
            disconnect();
        }
    }
}
