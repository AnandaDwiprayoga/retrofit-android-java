package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Error;
import id.putraprima.retrofit.api.models.ErrorResponse;
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
//        if(validateInput()){
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
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), ErrorResponse.class);

                        Error error = errorResponse.getError();

                        if (error.getEmail() != null && error.getPassword() != null && error.getName() != null){
                            showError(error.getName());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showError(error.getEmail());

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            showError(error.getPassword());
                                        }
                                    },2500);
                                }
                            },2500);
                        }else if (error.getName() != null && error.getEmail() != null){
                            showErrorDua(error.getName(), error.getEmail());
                        }else if (error.getName() != null && error.getPassword() != null){
                            showErrorDua(error.getName(), error.getPassword());
                        }else if (error.getEmail() != null && error.getPassword() != null){
                            showErrorDua(error.getEmail(), error.getPassword());
                        }else{
                            showError(error.getName());
                            showError(error.getEmail());
                            showError(error.getPassword());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
//        }
    }

//    private boolean validateInput() {
//        if (TextUtils.isEmpty(etName.getText())){
//            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
//            return false;
//        }else if (TextUtils.isEmpty(etEmail.getText())) {
//            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
//            return false;
//        }else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
//            Toast.makeText(this, "Your email address invalid", Toast.LENGTH_SHORT).show();
//            return false;
//        }else if (TextUtils.isEmpty(etPassword.getText()) || etPassword.getText().length() > 8) {
//            Log.d("length", "length: " + String.valueOf(etPassword.getText().length()));
//            Toast.makeText(this, "Enter your password min 8 character", Toast.LENGTH_SHORT).show();
//            return false;
//        }else if (TextUtils.isEmpty(etRePassword.getText()) || etRePassword.getText().length() > 8) {
//            Toast.makeText(this, "Enter your Re-password min 8 character", Toast.LENGTH_SHORT).show();
//            return false;
//        }else if (!TextUtils.equals(etPassword.getText().toString(), etRePassword.getText().toString())){
//            Toast.makeText(this, "your password not matches", Toast.LENGTH_SHORT).show();
//            return false;
//        }else{
//            return true;
//        }
//
//
//    }

    private void showError(String[] msg) {
        String message = "";
        if (msg != null) {
            for (int i = 0; i < msg.length; i++) {
                message += msg[i];

                //untuk memberikan spasi jika isi dari response lebih dari error lebih dari satu
                if (i + 1 != msg.length) {
                    message += "\n";
                }
            }
            Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void showErrorDua(String[] msg1, String[] msg2){
        showError(msg1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showError(msg2);
            }
        },2500);
    }
}

