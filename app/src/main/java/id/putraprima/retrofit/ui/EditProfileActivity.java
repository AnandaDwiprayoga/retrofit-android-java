package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.DataProfile;
import id.putraprima.retrofit.api.models.ProfileResponse;
import id.putraprima.retrofit.api.models.UpdateProfileRequest;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.putraprima.retrofit.ui.ProfilActivity.KEY_TOKEN;

public class EditProfileActivity extends AppCompatActivity {

    public static final String KEY_PROFILE = "key_profile";
    public static final String KEY_PROFILE_UPDATED = "key_profile_updated";

    private EditText etName;
    private EditText etEmail;

    private String token;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        
        DataProfile profile = (DataProfile) getIntent().getSerializableExtra(KEY_PROFILE);
        token = getIntent().getStringExtra(KEY_TOKEN);

        if (profile!= null){
            etName.setText(profile.getName());
            etEmail.setText(profile.getEmail());
        }
    }

    public void changeProfile(View view) {
        if (validateInput()){
            UpdateProfileRequest request = new UpdateProfileRequest(etEmail.getText().toString(), etName.getText().toString(), token);

            ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
            Call<ProfileResponse> call = service.updateProfile(request);

            call.enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                    if (response.isSuccessful()){
                        Intent intent = new Intent();
                        intent.putExtra(KEY_PROFILE_UPDATED, response.body().getData());
                        setResult(RESULT_OK, intent);
                        finish();
                    }else{
                        Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    Toast.makeText(EditProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(etName.getText())){
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
            return false;   
        }else if(TextUtils.isEmpty(etEmail.getText())){
            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
            Toast.makeText(this, "Enter your valid email", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }
}
