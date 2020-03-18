package id.putraprima.retrofit.api.services;


import id.putraprima.retrofit.api.models.AppVersion;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface{
    @GET("/")
    Call<AppVersion> getAppVersion();
}
