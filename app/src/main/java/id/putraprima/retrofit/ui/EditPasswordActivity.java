package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.UpdatePasswordRequest;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.putraprima.retrofit.ui.ProfilActivity.KEY_TOKEN;

public class EditPasswordActivity extends AppCompatActivity {

    private EditText etPassword;
    private EditText etPasswordAgain;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        etPassword = findViewById(R.id.etPassword);
        etPasswordAgain = findViewById(R.id.etPasswordAgain);

        token = getIntent().getStringExtra(KEY_TOKEN);
    }

    public void changePassword(View view) {
        if(validateInput()){
            UpdatePasswordRequest request = new UpdatePasswordRequest(etPassword.getText().toString(), etPasswordAgain.getText().toString(), token);

            ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
            Call<Void> call = service.updatePassword(request);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(EditPasswordActivity.this, "Password has been change", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }else{
                        Toast.makeText(EditPasswordActivity.this, "failed to change password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(EditPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(etPassword.getText())){
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
            return false;
        }else if(TextUtils.isEmpty(etPasswordAgain.getText())){
            Toast.makeText(this, "Enter your password again", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!TextUtils.equals(etPassword.getText().toString(), etPasswordAgain.getText().toString())){
            Toast.makeText(this, "Your password didn't match", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }
}
