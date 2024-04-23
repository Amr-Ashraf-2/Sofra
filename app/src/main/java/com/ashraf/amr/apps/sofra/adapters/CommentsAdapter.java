package com.ashraf.amr.apps.sofra.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.model.general.comments.CommentsData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {


    private Context context;
    private Activity activity;
    private List<CommentsData> commentsData = new ArrayList<>();

    public CommentsAdapter(Context context, Activity activity, List<CommentsData> commentsData) {
        this.context = context;
        this.activity = activity;
        this.commentsData = commentsData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comments,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        holder.ItemCommentsTvName.setText(commentsData.get(position).getClient().getName());
        holder.ItemCommentsTvComment.setText(commentsData.get(position).getComment());

        switch (commentsData.get(position).getRate()) {
            case "5":
                holder.ItemCommentsIvReview.setImageResource(R.drawable.ic_in_love_regular);
                break;
            case "4":
                holder.ItemCommentsIvReview.setImageResource(R.drawable.ic_laugh_beam_regular);
                break;
            case "3":
                holder.ItemCommentsIvReview.setImageResource(R.drawable.ic_smile_regular);
                break;
            case "2":
                holder.ItemCommentsIvReview.setImageResource(R.drawable.ic_sad_regular);
                break;
            case "1":
                holder.ItemCommentsIvReview.setImageResource(R.drawable.ic_angry_regular);
                break;
        }
    }

    private void setAction(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return commentsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Item_Comments_Tv_Name)
        TextView ItemCommentsTvName;
        @BindView(R.id.Item_Comments_Tv_Comment)
        TextView ItemCommentsTvComment;
        @BindView(R.id.Item_Comments_Iv_Review)
        ImageView ItemCommentsIvReview;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
