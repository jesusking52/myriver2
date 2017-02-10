package com.riverauction.riverauction.feature.common.review;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CTeacher;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.feature.common.ReviewInfoView2;
import com.riverauction.riverauction.feature.photo.PhotoSelector;
import com.riverauction.riverauction.feature.review.ReviewList;
import com.riverauction.riverauction.feature.review.ReviewWriteActivity;
import com.riverauction.riverauction.feature.teacher.TeacherDetailActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewDialog extends Dialog  {
    @Bind(R.id.close_button) View closeButton;
    @Bind(R.id.confirm_button) View confirmButton;
    private PhotoSelector photoSelector;
    @Bind(R.id.basic_info_view) ReviewInfoView2 basicInfoView;
    @Bind(R.id.profile_university) TextView profileuniversity;
    private CTeacher teacher;
    private CUser user2;
    public ReviewDialog(Context context,CUser CUser) {
        super(context);
        initialize(context);
        setContent(CUser);
        user2 = CUser;
    }

    public ReviewDialog(Context context, int theme) {
        super(context, theme);
        initialize(context);
    }

    private void setContent(CUser user) {
        if (user == null) {
            return;
        }
        teacher = user.getTeacher();
        //profilePhotoView.loadProfileImage(user);
        //userNameView.setText(user.getName());
        basicInfoView.setContent2(user, true);
        profileuniversity.setText(teacher.getUniversity());
    }
    protected ReviewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initialize(context);
    }

    private void initialize(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_review);
        ButterKnife.bind(this);

        closeButton.setOnClickListener(v -> dismiss());

        confirmButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReviewWriteActivity.class);
            intent.putExtra(TeacherDetailActivity.EXTRA_USER_ID, user2.getId());
            context.startActivity(intent);
        });
    }

}
