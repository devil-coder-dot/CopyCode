package com.google.android.gms.common.internal.service;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.Api.ClientKey;

public final class Common {
    public static final Api<NoOptions> API;
    public static final ClientKey<zai> CLIENT_KEY = new ClientKey<>();
    private static final AbstractClientBuilder<zai, NoOptions> zaph;
    public static final zac zapi = new zad();

    static {
        zab zab = new zab();
        zaph = zab;
        API = new Api<>("Common.API", zab, CLIENT_KEY);
    }
}
