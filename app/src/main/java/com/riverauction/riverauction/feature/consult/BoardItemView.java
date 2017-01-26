package com.riverauction.riverauction.feature.consult;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CBoard;
import com.riverauction.riverauction.feature.utils.DataUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BoardItemView extends LinearLayout {
    @Bind(R.id.category_label) ImageView categoryLabel;
    @Bind(R.id.item_summary) TextView itemSummary;
    @Bind(R.id.board_register_id) TextView boardRegisterId;
    @Bind(R.id.register_time) TextView registerTime;
    @Bind(R.id.view_count) TextView viewCount;
    @Bind(R.id.review_cnt) TextView reviewCnt;

    public BoardItemView(Context context) {
        super(context);
        initialize(context);
    }

    public BoardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public BoardItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_item_board_list, this, true);
        ButterKnife.bind(this);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    public void setContent(CBoard board) {
        if (board == null) {
            return;
        }

        if(board.getCategory2Id()!=0){

        }
        itemSummary.setText(board.getContent());
        boardRegisterId.setText(board.getUserid());
        //registerTime.setText(android.text.format.DateUtils.getRelativeTimeSpanString(board.getCreatedAt()));
        viewCount.setText(board.getViewCnt());
        reviewCnt.setText(board.getReplyCnt());

        //setboardStatusAndTime(board);
    }
/*
    private void setLessonStatusAndTime(CBoard board) {
        CLessonStatus boardStatus = board.getStatus();
        if (lessonStatus == null) {
            return;
        }
    }
    */
}
