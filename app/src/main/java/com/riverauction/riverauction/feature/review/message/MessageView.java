package com.riverauction.riverauction.feature.review.message;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CReview;
import com.riverauction.riverauction.common.LoadMoreRecyclerViewAdapter;
import com.riverauction.riverauction.feature.MoreLoadable;
import com.riverauction.riverauction.widget.StatusView;
import com.riverauction.riverauction.widget.recyclerview.DividerUtils;

import java.util.List;

public abstract class MessageView extends StatusView implements MoreLoadable {
    private MessageAdapter adapter;

    private List<CReview> reviews = Lists.newArrayList();
    private Integer nextToken;

    public MessageView(Context context) {
        super(context);
        initialize(context);
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        TextView emptyView = (TextView) inflater.inflate(R.layout.status_empty_view, this, false);
        emptyView.setText(R.string.message_empty_view);
        addView(emptyView);
        setEmptyView(emptyView);

        View errorView = inflater.inflate(R.layout.status_error_view, this, false);
        addView(errorView);
        setErrorView(errorView);

        View loadingView = inflater.inflate(R.layout.status_loading_view, this, false);
        addView(loadingView);
        setLoadingView(loadingView);

        View recyclerView = inflater.inflate(R.layout.recycler_view, this, false);
        addView(recyclerView);
        setResultView(recyclerView);

        findViewById(R.id.error_retry_button).setOnClickListener(v -> MessageView.this.loadMore(null));

        adapter = new MessageAdapter(reviews);
        RecyclerView mRecyclerView = (RecyclerView) getResultView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView.ItemDecoration itemDecoration = DividerUtils.getHorizontalDividerItemDecoration(context);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    public void setContent(List<CReview> newReviews, Integer newNextToken) {
        if (reviews.size() == 0 && newReviews.size() == 0) {
            showEmptyView();
            return;
        }
        showResultView();

        reviews.addAll(newReviews);
        nextToken = newNextToken;

        adapter.setNextToken(nextToken);
        adapter.setErrorLoadMore(false);
        adapter.notifyDataSetChanged();
    }

    public void setError() {
        if (reviews.size() == 0) {
            showErrorView();
        } else {
            // 더 불러오기 실패
            adapter.setErrorLoadMore(true);
            adapter.notifyItemChanged(reviews.size());
        }
    }

    public void setLoading() {
        if (reviews.size() == 0) {
            showLoadingView();
        }
    }

    public static class ReviewItemHolder extends RecyclerView.ViewHolder {
        public TextView contentView;
        public TextView createdAtView;

        public ReviewItemHolder(View itemView) {
            super(itemView);
            contentView = (TextView) itemView.findViewById(R.id.item_notification_content);
            createdAtView = (TextView) itemView.findViewById(R.id.item_notification_created_at);
        }
    }

    private class MessageAdapter extends LoadMoreRecyclerViewAdapter {
        private static final int TYPE_ITEM = 1;

        public MessageAdapter(List<CReview> notifications) {
            Preconditions.checkNotNull(notifications);
            this.items = notifications;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewItemHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                return new ReviewItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_item, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewItemHolder(RecyclerView.ViewHolder holder, int position) {
            CReview creview = reviews.get(position);
            ReviewItemHolder reviewItemHolder = ((ReviewItemHolder) holder);
            reviewItemHolder.contentView.setText(ReviewComposer.style(getContext(), creview.getRank().toString(), ""));
            reviewItemHolder.createdAtView.setText(DateUtils.getRelativeTimeSpanString(creview.getCreatedAt()));
        }

        @Override
        public int getItemViewItemType(int position) {
            return TYPE_ITEM;
        }

        @Override
        public void loadMore(Integer nextToken) {
            MessageView.this.loadMore(nextToken);
        }
    }
}
