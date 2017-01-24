package com.riverauction.riverauction.feature.consult;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CBoard;
import com.riverauction.riverauction.common.LoadMoreRecyclerViewAdapter;
import com.riverauction.riverauction.feature.MoreLoadable;
import com.riverauction.riverauction.widget.StatusView;
import com.riverauction.riverauction.widget.recyclerview.DividerUtils;

import java.util.List;

public abstract class BoardListView extends StatusView implements MoreLoadable {

    private BoardAdapter adapter;

    private List<CBoard> boards = Lists.newArrayList();
    private Integer nextToken;

    public BoardListView(Context context) {
        super(context);
        initialize(context);
    }

    public BoardListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public BoardListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        TextView emptyView = (TextView) inflater.inflate(R.layout.status_empty_view, this, false);
        emptyView.setText(R.string.my_lesson_history_empty_view);
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

        findViewById(R.id.error_retry_button).setOnClickListener(v -> BoardListView.this.loadMore(null));

        adapter = new BoardAdapter(boards);
        RecyclerView mRecyclerView = (RecyclerView) getResultView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView.ItemDecoration itemDecoration = DividerUtils.getHorizontalDividerItemDecoration(context);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    public void setContent(List<CBoard> newBoard, Integer newNextToken) {
        if (boards.size() == 0 && newBoard.size() == 0) {
            showEmptyView();
            return;
        }
        showResultView();

        boards.addAll(newBoard);
        nextToken = newNextToken;

        adapter.setNextToken(nextToken);
        adapter.setErrorLoadMore(false);
        adapter.notifyDataSetChanged();
    }

    public void setError() {
        if (boards.size() == 0) {
            showErrorView();
        } else {
            // 더 불러오기 실패
            adapter.setErrorLoadMore(true);
            adapter.notifyItemChanged(boards.size());
        }
    }

    public void setLoading() {
        if (boards.size() == 0) {
            showLoadingView();
        }
    }

    public void clear() {
        boards.clear();
        nextToken = null;
    }

    public static class BoardHolder extends RecyclerView.ViewHolder {
        public BoardItemView boardItemView;

        public BoardHolder(View itemView) {
            super(itemView);
            boardItemView = (BoardItemView) itemView;
        }
    }

    private class BoardAdapter extends LoadMoreRecyclerViewAdapter {
        private static final int TYPE_ITEM = 1;

        public BoardAdapter(List<CBoard> lessons) {
            Preconditions.checkNotNull(lessons);
            this.items = lessons;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewItemHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                return new BoardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_list, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewItemHolder(RecyclerView.ViewHolder holder, int position) {
            CBoard board = boards.get(position);
            BoardHolder boardHolder = ((BoardHolder) holder);
            boardHolder.boardItemView.setContent(board);
            boardHolder.boardItemView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), BoardDetailActivity.class);
                intent.putExtra(BoardDetailActivity.EXTRA_LESSON_ID, board.getUserid());
                intent.putExtra(BoardDetailActivity.EXTRA_OWNER_ID, board.getUserid());
                getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemViewItemType(int position) {
            return TYPE_ITEM;
        }

        @Override
        public void loadMore(Integer nextToken) {
            BoardListView.this.loadMore(nextToken);
        }
    }
}
