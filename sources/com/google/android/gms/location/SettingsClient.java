package com.google.android.gms.location;

import android.app.Activity;
import android.content.Context;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.internal.ApiExceptionMapper;
import com.google.android.gms.common.api.internal.StatusExceptionMapper;
import com.google.android.gms.common.internal.PendingResultUtil;
import com.google.android.gms.tasks.Task;

public class SettingsClient extends GoogleApi<NoOptions> {
    public SettingsClient(Activity activity) {
        super(activity, LocationServices.API, null, (StatusExceptionMapper) new ApiExceptionMapper());
    }

    public SettingsClient(Context context) {
        super(context, LocationServices.API, null, (StatusExceptionMapper) new ApiExceptionMapper());
    }

    public Task<LocationSettingsResponse> checkLocationSettings(LocationSettingsRequest locationSettingsRequest) {
        return PendingResultUtil.toResponseTask(LocationServices.SettingsApi.checkLocationSettings(asGoogleApiClient(), locationSettingsRequest), new LocationSettingsResponse());
    }
}
