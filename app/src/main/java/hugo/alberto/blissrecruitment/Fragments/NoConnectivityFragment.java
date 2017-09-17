package hugo.alberto.blissrecruitment.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import hugo.alberto.blissrecruitment.Interfaces.ShareService;
import hugo.alberto.blissrecruitment.Interfaces.UpdateQuestionService;
import hugo.alberto.blissrecruitment.Misc.Utils;
import hugo.alberto.blissrecruitment.Models.Choices;
import hugo.alberto.blissrecruitment.Models.Post;
import hugo.alberto.blissrecruitment.Models.Question;
import hugo.alberto.blissrecruitment.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alberto.hugo on 16-09-2017.
 */

public class NoConnectivityFragment extends Fragment {
    public static final String TAG = "NoConnectivityFragment";
    private TextView title;
    
    public NoConnectivityFragment() {
    }
    
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_no_connectivity, container, false);
        
        title = rootView.findViewById(R.id.title);
       
        return rootView;
}
    
}