package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName      = findViewById(R.id.etName);
        etEmail     = findViewById(R.id.etEmail);
        etPassword  = findViewById(R.id.etPassword);
        etRePassword= findViewById(R.id.etPasswordAgain);

    }

    public void submitRegister(View view) {
        if(validateInput()){
            RegisterRequest request = new RegisterRequest(
                    etName.getText().toString(),
                    etEmail.getText().toString(),
                    etPassword.getText().toString(),
                    etRePassword.getText().toString()
            );

            ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
            Call<Void> call = service.postRegister(request);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(etName.getText())){
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etEmail.getText())) {
            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
            Toast.makeText(this, "Your email address invalid", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etPassword.getText()) || etPassword.getText().length() > 8) {
            Log.d("length", "length: " + String.valueOf(etPassword.getText().length()));
            Toast.makeText(this, "Enter your password min 8 character", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etRePassword.getText()) || etRePassword.getText().length() > 8) {
            Toast.makeText(this, "Enter your Re-password min 8 character", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!TextUtils.equals(etPassword.getText().toString(), etRePassword.getText().toString())){
            Toast.makeText(this, "your password not matches", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }


    }
}
