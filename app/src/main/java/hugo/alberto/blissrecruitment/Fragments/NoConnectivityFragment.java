package hugo.alberto.blissrecruitment.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import hugo.alberto.blissrecruitment.R;

/**
 * Created by alberto.hugo on 16-09-2017.
 * No connectivity Screen
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