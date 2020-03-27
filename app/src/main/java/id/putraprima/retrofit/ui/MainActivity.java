package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.AppVersion;
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
        if(validateInput()){
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

                        finishAffinity();

                        Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
                        intent.putExtra(KEY_TOKEN, result.getToken());
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, "Failed to post server", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(etEmail.getText())){
            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(etPassword.getText())){
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
            Toast.makeText(this, "Your email address is invalid", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    public void goToRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
