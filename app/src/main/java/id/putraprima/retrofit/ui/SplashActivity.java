package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.services.ApiInterface;
import id.putraprima.retrofit.fragment.FailedLoad;
import id.putraprima.retrofit.fragment.NoInternt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    TextView lblAppName, lblAppTittle, lblAppVersion;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FragmentTransaction fragmentTransaction;
    private static FrameLayout frameLayout;

    public static final String KEY_APPNAME = "appname";
    public static final String KEY_VERSIONAPP = "versionapp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setupLayout();

        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();


        if (checkInternetConnection()) {
            checkAppVersion();
        }
        setAppInfo();
    }

    private void setupLayout() {
        lblAppName = findViewById(R.id.lblAppName);
        lblAppTittle = findViewById(R.id.lblAppTittle);
        lblAppVersion = findViewById(R.id.lblAppVersion);
        frameLayout = findViewById(R.id.frameLayout);


        //Sembunyikan lblAppName dan lblAppVersion pada saat awal dibuka
        lblAppVersion.setVisibility(View.INVISIBLE);
        lblAppName.setVisibility(View.INVISIBLE);
    }

    private boolean checkInternetConnection() {
        //TODO : 1. Implementasikan proses pengecekan koneksi internet, berikan informasi ke user jika tidak terdapat koneksi internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean status = activeNetwork != null && activeNetwork.isConnected();

        if (status){
            Toast.makeText(this, "internet connected", Toast.LENGTH_SHORT).show();
        }else{
            toogleViewFrame();git
            fragmentTransaction.add(R.id.frameLayout, new NoInternt())
                    .setCustomAnimations(R.anim.slide_in_up,R.anim.slide_in_down,R.anim.slide_out_down, R.anim.slide_out_up)
                    .addToBackStack(null)
                    .commit();
        }
        return status;
    }


    private void setAppInfo() {
        //TODO : 5. Implementasikan proses setting app info, app info pada fungsi ini diambil dari shared preferences
        //lblAppVersion dan lblAppName dimunculkan kembali dengan data dari shared preferences
        lblAppVersion.setVisibility(View.VISIBLE);
        lblAppName.setVisibility(View.VISIBLE);

        lblAppName.setText(sharedPreferences.getString(KEY_APPNAME,null));
        lblAppVersion.setText(sharedPreferences.getString(KEY_VERSIONAPP, null));

    }

    private void checkAppVersion() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<AppVersion> call = service.getAppVersion();
        call.enqueue(new Callback<AppVersion>() {
            @Override
            public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
                Toast.makeText(SplashActivity.this, response.body().getApp(), Toast.LENGTH_SHORT).show();
                //Todo : 2. Implementasikan Proses Simpan Data Yang didapat dari Server ke SharedPreferences
                editor.putString(KEY_APPNAME, response.body().getApp()).apply();
                editor.putString(KEY_VERSIONAPP, response.body().getVersion()).apply();

//                //Todo : 3. Implementasikan Proses Pindah Ke MainActivity Jika Proses getAppVersion() sukses
                if (response.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AppVersion> call, Throwable t) {
                Toast.makeText(SplashActivity.this, "Gagal Koneksi Ke Server", Toast.LENGTH_SHORT).show();
                //Todo : 4. Implementasikan Cara Notifikasi Ke user jika terjadi kegagalan koneksi ke server silahkan googling cara yang lain selain menggunakan TOAST
//                Snackbar.make(findViewById(android.R.id.content), "Server error", Snackbar.LENGTH_SHORT).show();;
                toogleViewFrame();
                fragmentTransaction.add(R.id.frameLayout, new FailedLoad())
                        .setCustomAnimations(R.anim.slide_in_up,R.anim.slide_in_down,R.anim.slide_out_down, R.anim.slide_out_up)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    public static void toogleViewFrame(){
        if (frameLayout.getVisibility() == View.INVISIBLE){
            frameLayout.setVisibility(View.VISIBLE);
        }else{
            frameLayout.setVisibility(View.INVISIBLE);
        }
    }
}
