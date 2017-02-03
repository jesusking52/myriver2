package com.riverauction.riverauction.feature.common.review;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CReview;
import com.riverauction.riverauction.api.model.CTeacher;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.base.BaseActivity;
import com.riverauction.riverauction.feature.common.ReviewInfoView2;
import com.riverauction.riverauction.feature.photo.PhotoSelector;
import com.riverauction.riverauction.feature.review.ReviewWriteMvpView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewDialog extends Dialog  {
    @Bind(R.id.close_button) View closeButton;
    @Bind(R.id.confirm_button) View confirmButton;
    private PhotoSelector photoSelector;
    @Bind(R.id.basic_info_view) ReviewInfoView2 basicInfoView;
    @Bind(R.id.profile_university) TextView profileuniversity;
    private CTeacher teacher;
    public ReviewDialog(Context context,CUser CUser) {
        super(context);
        initialize(context);
        setContent(CUser);
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
        basicInfoView.setContent(user);
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

        });
    }

}
