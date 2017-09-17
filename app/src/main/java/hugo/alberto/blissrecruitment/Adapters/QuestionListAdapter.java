package hugo.alberto.blissrecruitment.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.List;
import hugo.alberto.blissrecruitment.Fragments.QuestionsListFragment;
import hugo.alberto.blissrecruitment.Interfaces.ItemClickListener;
import hugo.alberto.blissrecruitment.Interfaces.OnLoadMoreListener;
import hugo.alberto.blissrecruitment.Misc.Utils;
import hugo.alberto.blissrecruitment.Models.Question;
import hugo.alberto.blissrecruitment.R;

/**
 * Created by alberto.hugo on 16-09-2017.
 * RecyclerViewAdapter to Question list screen
 */

public class QuestionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    private List<Question> questions;
    
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private Context ctx;
    
    public static class  MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
       
        public TextView line1, line2;
        private ItemClickListener clickListener;
    
        public MyViewHolder(View view) {
            super(view);
            line1 =  view.findViewById(R.id.line1);
            line2 =  view.findViewById(R.id.line2);
            view.setTag(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }
    
        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }
        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }
    
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        
        public LoadingViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBar1);
        }
    }
    
    
    public QuestionListAdapter(List<Question> questions, RecyclerView recyclerView, Context ctx) {
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
            Question question = questions.get(position);
            MyViewHolder myHolder = (MyViewHolder) holder;
            myHolder.line1.setText(question.id + ". " + question.question);
            myHolder.line2.setText(ctx.getString(R.string.publish_at) + " " + Utils.formateDate(question.published_at));
            myHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (isLongClick) {
                        //Toast.makeText(ctx, "#" + position + " - " + questions.get(position).id + " (Long click)", Toast.LENGTH_SHORT).show();
                    } else {
                        QuestionsListFragment.openDetail(questions.get(position), ctx);
                    }
                }
            });
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
    
    
    
    
}