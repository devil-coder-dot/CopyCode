package com.cosafe.android.utils.retrofit;

import com.cosafe.android.models.GetAllDownloadData;
import com.cosafe.android.models.GetUserID;
import com.cosafe.android.models.SaveUserDetails;
import com.cosafe.android.models.SubmitFormResponse;
import com.cosafe.android.models.getAllupdateflag;
import com.cosafe.android.models.gson.CheckVersionObject;
import com.cosafe.android.models.gson.GetOtpResponse;
import com.cosafe.android.models.gson.VerifyOtpResponse;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("CoSafe_key/versiondetail")
    Call<CheckVersionObject> checkAppVersion(@Header("mobile") String str, @Header("authkey") String str2, @Query("deviceType") String str3);

    @GET("CoSafe_key/infecteddata")
    Call<GetAllDownloadData> getAllDownloadData(@Header("mobile") String str, @Header("authkey") String str2, @Query("limit") String str3, @Query("offset") String str4);

    @GET("CoSafe_key/updateflag")
    Call<getAllupdateflag> getAllupdateflag(@Header("mobile") String str, @Header("authkey") String str2, @Query("mobile") String str3, @Query("flag") String str4, @Query("infectedmobile") String str5, @Query("flagType") String str6);

    @GET("CoSafe_key/getByUserMobile")
    Call<GetUserID> getMyUserID(@Header("mobile") String str, @Header("authkey") String str2, @Query("mobile") String str3);

    @GET("CoSafe_key/sendotp")
    Call<GetOtpResponse> getOTP(@Query("mobileNo") String str);

    @POST("CoSafe_key/saveUserDetails")
    Call<SaveUserDetails> saveUserDetails(@Header("mobile") String str, @Header("authkey") String str2, @Body JsonObject jsonObject);

    @POST("CoSafe_key/saveQuestionAnswer")
    Call<SubmitFormResponse> submitQnA(@Header("mobile") String str, @Header("authkey") String str2, @Body JsonObject jsonObject);

    @GET("CoSafe_key/verifiedotp")
    Call<VerifyOtpResponse> verifyOTP(@Query("mobile") String str, @Query("otp") String str2, @Query("deviceId") String str3);
}
