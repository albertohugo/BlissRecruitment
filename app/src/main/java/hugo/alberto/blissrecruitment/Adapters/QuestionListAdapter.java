package hugo.alberto.blissrecruitment.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hugo.alberto.blissrecruitment.Interfaces.OnLoadMoreListener;
import hugo.alberto.blissrecruitment.Models.Questions;
import hugo.alberto.blissrecruitment.R;


public class QuestionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    private List<Questions> questions;
    
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private Context ctx;
    
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView line1, line2;
        
        public MyViewHolder(View view) {
            super(view);
            line1 = (TextView) view.findViewById(R.id.line1);
            line2 = (TextView) view.findViewById(R.id.line2);
        }
    }
    
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        
        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }
    
    
    public QuestionListAdapter(List<Questions> questions, RecyclerView recyclerView, Context ctx) {
        this.questions = questions;
        this.ctx = ctx;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_list, parent, false);
            return new MyViewHolder(itemView);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(itemView);
        }
        return null;
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            Questions question = questions.get(position);
            MyViewHolder myHolder = (MyViewHolder) holder;
            myHolder.line1.setText(question.id + ". " + question.question);
            myHolder.line2.setText(ctx.getString(R.string.publish_at) + " " + formateDate(question.published_at));
            
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
        
      
    }
    
    @Override
    public int getItemCount() {
        return questions.size();
    }
    
    
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }
    
    @Override
    public int getItemViewType(int position) {
        return questions.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    public void setLoaded() {
        isLoading = false;
    }
    
    public static String formateDate(String dateString) {
        Date date;
        String formattedDate = "";
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(dateString);
            formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return formattedDate;
    }
}