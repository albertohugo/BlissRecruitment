package hugo.alberto.blissrecruitment.Fragments;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hugo.alberto.blissrecruitment.Adapters.QuestionListAdapter;
import hugo.alberto.blissrecruitment.Interfaces.ListAllQuestionsService;
import hugo.alberto.blissrecruitment.Interfaces.OnLoadMoreListener;
import hugo.alberto.blissrecruitment.Models.Questions;
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
    
    private static final String TAG = "retrofit2_gson";
    private RecyclerView recycle;
    private Button btRetry;
    private ArrayList<String> listItems;
    private List<Questions> listQ;
    private int visibleThreshold = 10;
    private TextInputEditText edtt_search;
    private QuestionListAdapter mAdapter = null;
    private ImageButton btClear;
    
    public QuestionsListFragment() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    
    
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        
      
        
        edtt_search = rootView.findViewById(R.id.et_search);
        recycle = rootView.findViewById(R.id.recycler_view);
        btClear = rootView.findViewById(R.id.clear_filter);
        listItems = new ArrayList<>();
        listQ = new ArrayList<>();
    
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtt_search.setText("");
            }
        });
    
    
        Retrofit retrofitL = new Retrofit.Builder()
                .baseUrl(ListAllQuestionsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    
        ListAllQuestionsService serviceL = retrofitL.create(ListAllQuestionsService.class);
        Call<List<Questions>> requestCatalogL = serviceL.getQuestions();
    
        requestCatalogL.enqueue(new Callback<List<Questions>>() {
            @Override
            public void onResponse(Call<List<Questions>> call, final Response<List<Questions>> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "AH DEBUG Error" + response.code());
                } else {
                    Log.i(TAG, "AH DEBUG "+ String.valueOf(response.code()));
                    Log.i(TAG, "AH DEBUG "+ String.valueOf(response.body()));
                    Log.i(TAG, "AH DEBUG "+ String.valueOf(response.body().get(1).question));
                
                    for (int i = 0; i <visibleThreshold  ; i++) {
                        Questions q =response.body().get(i);
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
                                Questions qs =response.body().get(i);
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
                                Questions q =response.body().get(j);
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
                                            Questions q =response.body().get(i);
                                            listQ.add(q);
                                        }
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    mAdapter.setLoaded();
                                }
                            }, 5000);
                        } else {
                            Snackbar.make(getView(), getString(R.string.loading_completed), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                });
            
            }
        
            @Override
            public void onFailure(Call<List<Questions>> call, Throwable t) {
                Log.i(TAG, "AH DEBUG Error onFailure "+ t);
            }
        });
    
    
    
    
        return rootView;
      
    }
    
    
}
