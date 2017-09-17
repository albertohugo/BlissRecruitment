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
 * Detail Screen
 */

public class DetailFragment extends Fragment {
    public static final String TAG = "DetailFragment";
    private Question questionSelected;
    private TextView title;
    private TextView subtitle;
    private ImageView img;
    private RadioButton rbn;
    public DetailFragment() {
    }
    
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        
        title = rootView.findViewById(R.id.title);
        subtitle = rootView.findViewById(R.id.subtitle);
        img = rootView.findViewById(R.id.imageView);
        
        // Receive Question object by bundle
        Bundle bundle = this.getArguments();
        if (getArguments() != null) {
            questionSelected =  bundle.getParcelable("Question");
        }
    
        title.setText(questionSelected.id +". "+questionSelected.question);
        subtitle.setText(getString(R.string.publish_at) + " " + Utils.formateDate(questionSelected.published_at));
        Picasso.with(getActivity()).load(questionSelected.thumb_url).placeholder( R.drawable.progress_animation ).into(img);
        
        //Create radio group and fill qwith choices object
        RadioGroup rgp = new RadioGroup(getActivity());
    
        for (Choices choices : questionSelected.choices) {
            rbn = new RadioButton(getActivity());
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonLayoutParams.setMargins(300, 50, 50, 0);
            rbn.setLayoutParams(buttonLayoutParams);
            rbn.setTextSize(20);
            rbn.setText(choices.choice+" ("+choices.votes+")");
            rgp.addView(rbn);
        }
    
        //Event get radio button select
        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                rbn = rootView.findViewById(selectedId);
            }
        });
    
        LinearLayout linearChoices = rootView.findViewById(R.id.linearChoices);
        linearChoices.addView(rgp);
    
        FloatingActionButton shareButton = rootView.findViewById(R.id.fab2);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText txtEmail = new EditText(getActivity());
                final AlertDialog dialog =
                        new AlertDialog.Builder(getActivity())
                                .setTitle(getString(R.string.email))
                                .setMessage("")
                                .setView(txtEmail)
                                .setPositiveButton(getActivity().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        try {
                                            String email= txtEmail.getText().toString();
                                            String url = "blissrecruitment://questions?question_id="+questionSelected.id;
                                
                                            final Retrofit retrofit = new Retrofit.Builder()
                                                    .addConverterFactory(GsonConverterFactory.create()).baseUrl(ShareService.BASE_URL)
                                                    .build();
                                
                                            ShareService service = retrofit.create(ShareService.class);
                                
                                            Call<Post> call = service.loadDetail(email, url);
                                            call.enqueue(new Callback<Post>() {
                                                @Override
                                                public void onResponse(Call<Post> call, Response<Post> response) {
                                                    Log.i(TAG, "AH DEBUG " + response.headers());
                                                    Log.i(TAG, "AH DEBUG " + response.code());
                                                    Log.i(TAG, "AH DEBUG " + response.body());
                                                    Log.i(TAG, "AH DEBUG " + response.message());
                                                    Log.i(TAG, "AH DEBUG  "+ response.raw().toString());
                                                    if(response.code()==200){
                                                        Toast.makeText(getActivity(), getString(R.string.shared), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                    
                                                @Override
                                                public void onFailure(Call<Post> call, Throwable t) {
                                                    Log.i(TAG, "AH DEBUG " + "data not found");
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .setNegativeButton(getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                                .create();
    
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                dialog.show();
            }
        });
        

    FloatingActionButton sendButton = rootView.findViewById(R.id.fab1);
        sendButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create()).baseUrl(UpdateQuestionService.BASE_URL)
                    .build();
    
            UpdateQuestionService service = retrofit.create(UpdateQuestionService.class);
    
            Call<Question> call = service.updateQuestion(Integer.parseInt(questionSelected.id), questionSelected);
            call.enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Call<Question> call, Response<Question> response) {
                    Log.i(TAG, "AH DEBUG " + response.headers());
                    Log.i(TAG, "AH DEBUG " + response.code());
                    Log.i(TAG, "AH DEBUG " + response.body());
                    Log.i(TAG, "AH DEBUG " + response.message());
                    Log.i(TAG, "AH DEBUG  "+ response.raw().toString());
    
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    
                    Fragment fragment = new QuestionsListFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment, fragment, QuestionsListFragment.TAG)
                            .commit();
                }
        
                @Override
                public void onFailure(Call<Question> call, Throwable t) {
                    Log.i(TAG, "AH DEBUG " + "data not found");
                }
            });
        }
    });
        
    return rootView;
}
}