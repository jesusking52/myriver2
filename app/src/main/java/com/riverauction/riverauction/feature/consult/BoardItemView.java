package com.riverauction.riverauction.feature.consult;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CLesson;
import com.riverauction.riverauction.api.model.CLessonStatus;

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

    public void setContent(CLesson lesson) {
        if (lesson == null || lesson.getOwner() == null) {
            return;
        }
        /*
        if (!Strings.isNullOrEmpty(lesson.getName()) || lesson.getGender() != null) {
            String nameAndGenderString = DataUtils.convertToAnonymousName(lesson.getName()) + " " + DataUtils.convertGenderToShortString(getContext(), lesson.getGender());
            nameAndGenderView.setText(nameAndGenderString);
        }

        String availableSubjectString = DataUtils.convertSubjectToString(lesson.getAvailableSubjects());
        if (!Strings.isNullOrEmpty(availableSubjectString)) {
            availableSubjectsView.setText(availableSubjectString);
        }
        if (lesson.hasAddress()) {
            locationView.setText(DataUtils.convertLocationToString(lesson.getLocation()));
        }

        if (lesson.getPreferredPrice() != null) {
            preferredPriceView.setText(getResources().getString(R.string.item_lesson_preferred_price, lesson.getPreferredPrice()));
        }
*/
        setLessonStatusAndTime(lesson);
    }

    private void setLessonStatusAndTime(CLesson lesson) {
        CLessonStatus lessonStatus = lesson.getStatus();
        if (lessonStatus == null) {
            return;
        }

    }
}
