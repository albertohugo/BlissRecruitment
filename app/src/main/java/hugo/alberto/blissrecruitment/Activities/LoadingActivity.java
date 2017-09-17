package hugo.alberto.blissrecruitment.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import hugo.alberto.blissrecruitment.Interfaces.HealthService;
import hugo.alberto.blissrecruitment.Models.Health;
import hugo.alberto.blissrecruitment.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alberto.hugo on 16-09-2017.
 * Splach activity
 * Sreen that check server health
 */

public class LoadingActivity extends Activity {
    private static final String TAG = "LoadingActivity";
    private LinearLayout linearLoading;
    private LinearLayout linearReloading;
    private Button buttonRetry;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
    
        linearLoading = findViewById(R.id.linear_loading);
        linearReloading = findViewById(R.id.linear_reloading);
        buttonRetry = findViewById(R.id.button_retry);
        
        CallHealthService();
    
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLoading.setVisibility(View.VISIBLE);
                linearReloading.setVisibility(View.GONE);
                CallHealthService();
            }
        });
        
    }
    
    private void CallHealthService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HealthService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        HealthService serviceR = retrofit.create(HealthService.class);
        Call<Health> requestCatalogR = serviceR.status();
        
        requestCatalogR.enqueue(new Callback<Health>() {
            @Override
            public void onResponse(Call<Health> call, Response<Health> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "AH DEBUG Response:" + response.isSuccessful());
                } else {
                    if(response.code()==200){
                        finish();
                        Intent intent = new Intent();
                        intent.setClass(LoadingActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else{
                        linearLoading.setVisibility(View.GONE);
                        buttonRetry.setText(String.valueOf(response.body().status));
                        linearReloading.setVisibility(View.VISIBLE);
                    }
                }
            }
        
            @Override
            public void onFailure(Call<Health> call, Throwable t) {
            
            }
        });
    }
    
}