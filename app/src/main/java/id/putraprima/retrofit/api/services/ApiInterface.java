package id.putraprima.retrofit.api.services;


import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.models.ProfileResponse;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.models.UpdatePasswordRequest;
import id.putraprima.retrofit.api.models.UpdateProfileRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface{
    @GET("/")
    Call<AppVersion> getAppVersion();

    @POST("/api/auth/login")
    Call<LoginResponse> postLogin(@Body LoginRequest request);

    @POST("/api/auth/register")
    Call<Void> postRegister(@Body RegisterRequest request);

    @GET("/api/auth/me")
    Call<ProfileResponse> getProfile(@Query("token") String token);

    @PATCH("/api/account/profile")
    Call<ProfileResponse> updateProfile(@Body UpdateProfileRequest updateProfileRequest);

    @PATCH("/api/account/password")
    Call<Void> updatePassword(@Body UpdatePasswordRequest updatePasswordRequest);
}
