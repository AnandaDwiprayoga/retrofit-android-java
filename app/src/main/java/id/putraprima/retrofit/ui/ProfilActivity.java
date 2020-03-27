package id.putraprima.retrofit.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.DataProfile;
import id.putraprima.retrofit.api.models.ProfileResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.putraprima.retrofit.ui.EditProfileActivity.KEY_PROFILE;
import static id.putraprima.retrofit.ui.EditProfileActivity.KEY_PROFILE_UPDATED;

public class ProfilActivity extends AppCompatActivity {
    public static final String KEY_TOKEN = "key_token";
    public static final int CODE_UPDATE_PROFILE = 100;
    public static final int CODE_UPDATE_PASSWORD = 101;

    private TextView tvNama;
    private TextView tvEmail;
    private TextView tvId;

    private DataProfile profile;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        tvNama      = findViewById(R.id.tvName);
        tvEmail     = findViewById(R.id.tvEmail);
        tvId        = findViewById(R.id.tvId);

        if (getIntent().getStringExtra(KEY_TOKEN) != null){
            token = getIntent().getStringExtra(KEY_TOKEN);
            requestData(token);
        }
    }

    private void requestData(String token) {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<ProfileResponse> call = service.getProfile(token);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()){
                    profile = response.body().getData();

                    tvNama.setText(profile.getName());
                    tvEmail.setText(profile.getEmail());

                    String id = "ID : <b>"+ profile.getId() +"</b>";

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvId.setText(Html.fromHtml(id, Html.FROM_HTML_MODE_COMPACT));
                    }else{
                        tvId.setText(Html.fromHtml(id));
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(ProfilActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleLogout(View view) {
        startActivity(new Intent(ProfilActivity.this, MainActivity.class));
        finish();
    }

    public void handleChangeProfile(View view) {
        Intent intent = new Intent(ProfilActivity.this, EditProfileActivity.class);
        intent.putExtra(KEY_PROFILE, profile);
        intent.putExtra(KEY_TOKEN, token);
        startActivityForResult(intent, CODE_UPDATE_PROFILE);
    }

    public void handleChangePassword(View view) {
        Intent intent = new Intent(ProfilActivity.this, EditPasswordActivity.class);
        intent.putExtra(KEY_TOKEN, token);
        startActivityForResult(intent, CODE_UPDATE_PASSWORD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == CODE_UPDATE_PROFILE){
                profile = (DataProfile) data.getSerializableExtra(KEY_PROFILE_UPDATED);

                tvNama.setText(profile.getName());
                tvEmail.setText(profile.getEmail());
            }else if (requestCode == CODE_UPDATE_PASSWORD){
                finishAffinity();

                startActivity(new Intent(ProfilActivity.this, MainActivity.class));
            }
        }
    }
}
