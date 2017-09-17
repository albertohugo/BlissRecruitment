package hugo.alberto.blissrecruitment.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import hugo.alberto.blissrecruitment.Fragments.DetailFragment;
import hugo.alberto.blissrecruitment.Fragments.NoConnectivityFragment;
import hugo.alberto.blissrecruitment.Fragments.QuestionsListFragment;
import hugo.alberto.blissrecruitment.R;

/**
 * Created by alberto.hugo on 16-09-2017.
 * Main activity from Bliss app
 */

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private String parameter="";
    private String value="";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    
        // Get intent, parameter and value
        
        Intent intent = getIntent();
        String data = intent.getDataString();
        if(data!=null){
            String url = "blissrecruitment://questions?";
            String dataFilted = data.replace(url,"");
            String[] split = dataFilted.split("=");
            parameter = split[0].toUpperCase();
            value = split[1];
        }
        
            Fragment fragment = new QuestionsListFragment();
            Bundle bundle = new Bundle();
            // Passing parameter and value to fragment
            bundle.putString("Parameter", parameter);
            bundle.putString("Value", value);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment, fragment, QuestionsListFragment.TAG)
                    .commit();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }
    
    @Override
    public void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();
    }
    
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment);
        if (currentFragment.getTag().equals(DetailFragment.TAG)){
            getSupportFragmentManager().popBackStack();
        } else  if (currentFragment.getTag().equals(NoConnectivityFragment.TAG)){
            
        }else {
            super.onBackPressed();
        }

    }
    
    
    private BroadcastReceiver networkStateReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Broadcast to check connectivity state
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            
            if(isConnected){
                //Connectivity true
                Log.i(TAG, "AH DEBUG " + isConnected);
                getSupportFragmentManager().popBackStack();
            }else{
                //Connectivity false
                Log.i(TAG, "AH DEBUG " + isConnected);
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = NoConnectivityFragment.instantiate(getApplicationContext(), NoConnectivityFragment.class.getName());
                fragmentManager.beginTransaction().add(R.id.fragment, fragment, NoConnectivityFragment.TAG).addToBackStack(null).commit();
            }
            
        }
    };
}
