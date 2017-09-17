package hugo.alberto.blissrecruitment.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hugo.alberto.blissrecruitment.Activities.MainActivity;
import hugo.alberto.blissrecruitment.Adapters.QuestionListAdapter;
import hugo.alberto.blissrecruitment.Interfaces.ListAllQuestionsService;
import hugo.alberto.blissrecruitment.Interfaces.OnLoadMoreListener;
import hugo.alberto.blissrecruitment.Interfaces.ShareService;
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
 * A placeholder fragment containing a simple view.
 */

public class QuestionsListFragment extends Fragment {
    
    public static final String TAG = "QuestionsListFragment";
    private RecyclerView recycle;
    private List<Question> listQ;
    private int visibleThreshold = 10;
    public TextInputEditText edtt_search;
    private QuestionListAdapter mAdapter = null;
    private ImageButton btClear;
    private String parameter ="";
    private String value="";
    
    public QuestionsListFragment() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    
    
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
       
        edtt_search = rootView.findViewById(R.id.et_search);
        recycle = rootView.findViewById(R.id.recycler_view);
        btClear = rootView.findViewById(R.id.clear_filter);
        listQ = new ArrayList<>();
    
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtt_search.setText("");
            }
        });
        
        Bundle bundle = this.getArguments();
        if (getArguments() != null) {
            parameter =  bundle.getString("Parameter");
            value =  bundle.getString("Value");
        }
        
        if(parameter.equals("QUESTION_FILTER")){
            edtt_search.setText(value);
        }
        
        
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
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
                                            String url = "blissrecruitment://questions?question_filter=" + edtt_search.getText().toString();
                                            
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
        
        Retrofit retrofitL = new Retrofit.Builder()
                .baseUrl(ListAllQuestionsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    
        ListAllQuestionsService serviceL = retrofitL.create(ListAllQuestionsService.class);
        Call<List<Question>> requestCatalogL = serviceL.getQuestions();
    
        requestCatalogL.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, final Response<List<Question>> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "AH DEBUG Error" + response.code());
                } else {
                    Log.i(TAG, "AH DEBUG "+ String.valueOf(response.body().get(1).question));
    
                 
                    
                    for (int i = 0; i <visibleThreshold  ; i++) {
                        if(parameter.equals("QUESTION_ID")){
                            if (!value.equals("")) {
                                if (response.body().get(i).id.equals(value)) {
                                    sendDetails(response.body().get(i), getActivity());
                                    return;
                                }
                            }
                        }
                        Question q =response.body().get(i);
                        listQ.add(q);
                    }
                
                }
                final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recycle.setLayoutManager(mLayoutManager);
                mAdapter = new QuestionListAdapter(listQ, recycle, getActivity());
                recycle.setAdapter(mAdapter);
            
                edtt_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                        if(!cs.toString().equals("")){
                            listQ.clear();
                            for (int i = 0; i <response.body().size()  ; i++) {
                                Question qs =response.body().get(i);
                                String id = String.valueOf(qs.id);
                                if(id.contains(cs.toString())||qs.question.contains(cs.toString())){
                                    if(listQ.size()<visibleThreshold){
                                        listQ.add(qs);
                                    }
                                }
                            }
                        }else{
                            listQ.clear();
                            for (int j = 0; j <visibleThreshold  ; j++) {
                                Question q =response.body().get(j);
                                listQ.add(q);
                            }
                        }
                        mAdapter = new QuestionListAdapter(listQ, recycle, getActivity());
                        recycle.setAdapter(mAdapter);
                    }
                
                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
                
                    @Override
                    public void afterTextChanged(Editable arg0) {}
                });
    
    
                if (parameter.equals("QUESTION_FILTER")){
                    edtt_search.setText(value);
                }
            
                mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        if (listQ.size() < response.body().size()) {
                            listQ.add(null);
                            mAdapter.notifyItemInserted(listQ.size() - 1);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    listQ.remove(listQ.size() - 1);
                                    mAdapter.notifyItemRemoved(listQ.size());
                                
                                    //Generating more data
                                    int index = listQ.size(); // TODO change to  response.body()
                                    int end = index + visibleThreshold;
                                    for (int i = index; i < end; i++) {
                                        if(response.body().size()>i){
                                            Question q =response.body().get(i);
                                            listQ.add(q);
                                        }
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    mAdapter.setLoaded();
                                }
                            }, 5000);
                        } else {
                            //Snackbar.make(getView(), getString(R.string.loading_completed), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }
                });
                
            }
        
            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.i(TAG, "AH DEBUG Error onFailure "+ t);
            }
        });
    
        
        return rootView;
        
    }
        
    public static void sendDetails(Question question, Context ctx) {
        FragmentManager fragmentManager = ((MainActivity) ctx).getSupportFragmentManager();
        Fragment fragment = NoConnectivityFragment.instantiate(ctx, DetailFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putParcelable("Question", question);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().add(R.id.fragment, fragment, DetailFragment.TAG).addToBackStack(null).commit();
        
    }
    
    
   
}
