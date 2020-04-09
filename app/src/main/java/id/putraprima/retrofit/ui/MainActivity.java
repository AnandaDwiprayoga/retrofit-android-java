package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.models.Error;
import id.putraprima.retrofit.api.models.ErrorResponse;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.putraprima.retrofit.ui.ProfilActivity.KEY_TOKEN;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private TextView tvMainName;
    private TextView tvMainVersion;

    public static final String KEY_APP = "app_Key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail         = findViewById(R.id.edtEmail);
        etPassword      = findViewById(R.id.edtPassword);
        tvMainName      = findViewById(R.id.mainTxtAppName);
        tvMainVersion   = findViewById(R.id.mainTxtAppVersion);

        AppVersion appVersion = (AppVersion) getIntent().getSerializableExtra(KEY_APP);
        if (appVersion != null){
            tvMainName.setText(appVersion.getApp());
            tvMainVersion.setText(appVersion.getVersion());
        }
    }

    public void login(View view) {
//        if(validateInput()){
            LoginRequest request = new LoginRequest(etEmail.getText().toString(), etPassword.getText().toString());
            
            ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
            Call<LoginResponse> call = service.postLogin(request);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()){
                        LoginResponse result = response.body();

                        Toast.makeText(MainActivity.this, "Token : " +  result.getToken(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "Token type : " +  result.getToken_type(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "Expires  : " +  result.getExpires_in() + "", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
                        intent.putExtra(KEY_TOKEN, result.getToken());
                        startActivity(intent);
                    }else{
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(),ErrorResponse.class);
                        Error error = errorResponse.getError();

                        if (error.getEmail() != null && error.getPassword() != null){
                            showError(error.getEmail());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showError(error.getPassword());
                                }
                            },3000);
                        }else{
                            showError(error.getEmail());
                            showError(error.getPassword());
                        }

//                        Toast.makeText(MainActivity.this, error.getError().getEmail()[0], Toast.LENGTH_SHORT).show();
//                        Toast.makeText(MainActivity.this, error.getError().getPassword()[0], Toast.LENGTH_SHORT).show();
//                        Toast.makeText(MainActivity.this, "Failed to post server", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
//        }
    }

    private void showError(String[] msg) {
        String message = "";
        if (msg != null) {
            for (int i = 0; i < msg.length; i++) {
                message += msg[i];

                //untuk memberikan spasi jika isi dari response lebih dari error lebih dari satu
                if (i+1 != msg.length){
                    message += "\n";
                }
            }
            Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }


//    private boolean validateInput() {
//        if (TextUtils.isEmpty(etEmail.getText())){
//            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (TextUtils.isEmpty(etPassword.getText())){
//            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
//            return false;
//        }else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
//            Toast.makeText(this, "Your email address is invalid", Toast.LENGTH_SHORT).show();
//            return false;
//        }else {
//            return true;
//        }
//    }

    public void goToRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
