package com.riverauction.riverauction.feature.consult;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CBoard;

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
        itemSummary.setText(board.getSubject());
        //String nameAndGenderString = DataUtils.convertToAnonymousName(user.getName()) + " " + DataUtils.convertGenderToShortString(getContext(), user.getGender());
        boardRegisterId.setText(board.getUserid());
        registerTime.setText(android.text.format.DateUtils.getRelativeTimeSpanString(board.getCreatedAt()));
        if(board.getViewCnt() == null)
            viewCount.setText("조회수:0");
        else
            viewCount.setText("조회수:"+board.getViewCnt().toString());

        if(board.getReplyCnt() == null)
            reviewCnt.setText("0");
        else
            reviewCnt.setText(board.getReplyCnt().toString());

        switch (board.getCategory2Id()){
            case 11:
                categoryLabel.setImageResource(R.drawable.grade_university);
                break;
            case 12:
                categoryLabel.setImageResource(R.drawable.grade_high);
                break;
            case 13:
                categoryLabel.setImageResource(R.drawable.grade_middle);
                break;
            case 14:
                categoryLabel.setImageResource(R.drawable.grade_global);
                break;
            case 21:
                categoryLabel.setImageResource(R.drawable.literature);
                break;
            case 22:
                categoryLabel.setImageResource(R.drawable.nature);
                break;
            case 23:
                categoryLabel.setImageResource(R.drawable.other);
                break;
            case 31:
                categoryLabel.setImageResource(R.drawable.category_student);
                break;
            case 32:
                categoryLabel.setImageResource(R.drawable.category_parents);
                break;

        }
    }
}
