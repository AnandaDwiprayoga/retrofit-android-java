package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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

public class ProfilActivity extends AppCompatActivity {
    public static final String KEY_TOKEN = "key_token";

    private TextView tvNama;
    private TextView tvEmail;
    private TextView tvId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        tvNama      = findViewById(R.id.tvName);
        tvEmail     = findViewById(R.id.tvEmail);
        tvId        = findViewById(R.id.tvId);

        if (getIntent().getStringExtra(KEY_TOKEN) != null){
            requestData(getIntent().getStringExtra(KEY_TOKEN));
        }
    }

    private void requestData(String token) {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<ProfileResponse> call = service.getProfile(token);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()){
                    DataProfile profile = response.body().getData();

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
}
