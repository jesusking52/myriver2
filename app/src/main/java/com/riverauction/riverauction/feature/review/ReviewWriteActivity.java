package com.riverauction.riverauction.feature.review;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.jhcompany.android.libs.utils.ParcelableWrappers;
import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CLocation;
import com.riverauction.riverauction.api.model.CReview;
import com.riverauction.riverauction.api.model.CTeacher;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.api.service.auth.request.TeacherReviewRequest;
import com.riverauction.riverauction.base.BaseActivity;
import com.riverauction.riverauction.feature.common.ReviewInfoView2;
import com.riverauction.riverauction.feature.common.SimpleRatingBar;
import com.riverauction.riverauction.feature.photo.PhotoSelector;
import com.riverauction.riverauction.feature.teacher.TeacherDetailActivity;
import com.riverauction.riverauction.states.UserStates;

import javax.inject.Inject;

import butterknife.Bind;

public class ReviewWriteActivity extends BaseActivity implements ReviewWriteMvpView {
    private final static int REQUEST_SEARCH_LOCATION = 0x01;
    private static final String EXTRA_PREFIX = "com.riverauction.riverauction.feature.teacher.TeacherDetailActivity.";
    public static final String EXTRA_USER_ID = EXTRA_PREFIX + "extra_user_id";
    public static final String EXTRA_REVIEW = EXTRA_PREFIX + "extra_review";
    private static final String EXTRA_PREFIX2 = "com.riverauction.riverauction.feature.review.ReviewList.";
    public static final String EXTRA_USER_ID2 = EXTRA_PREFIX2 + "extra_user_id";
    public static final String EXTRA_REVIEW_IDX = EXTRA_PREFIX2 + "extra_review_idx";
    private PhotoSelector photoSelector;
    @Inject
    ReviewWritePresenter presenter;

    // basic
    @Bind(R.id.review) EditText review;
    @Bind(R.id.basic_info_view) ReviewInfoView2 basicInfoView;
    @Bind(R.id.profile_university) TextView profileuniversity;
    @Bind(R.id.ratingBar1) SimpleRatingBar ratingBar1;
    // address 정보
    private CLocation location;
    private CUser user;
    private int reviewIdx;
    private CTeacher teacher;
    private CReview cReview;
    private Integer teacherId;
    private  boolean isModify=false;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_review_write;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // basic
        getDataFromBundle(getIntent().getExtras());
        getActivityComponent().inject(this);
        presenter.attachView(this, this);
        user = UserStates.USER.get(stateCtx);

        getSupportActionBar().setTitle(R.string.review_title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter.getUserProfile(teacherId, true);
    }

    private  void setUserReview(CReview review){

    }

    // teacherId 를 이전 화면에서 넘겨받는다
    private void getDataFromBundle(Bundle bundle) {
        if (bundle != null) {

            Parcelable parcelable = bundle.getParcelable(EXTRA_REVIEW);
            if (parcelable != null) {
                cReview = ParcelableWrappers.unwrap(parcelable);
                teacherId = bundle.getInt(TeacherDetailActivity.EXTRA_USER_ID, -1);
                review.setText(cReview.getReview());
                float rank = (float) cReview.getRank();
                rank = rank /2;
                ratingBar1.setRating(rank);
                //reviewRankSpinner.setSelection(cReview.getRank());
                isModify = true;
            }else
            {
                teacherId = bundle.getInt(TeacherDetailActivity.EXTRA_USER_ID, -1);
                isModify = false;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_common_confirm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_common_confirm) {
            if (isValidCheckBasic()) {

                if(isModify)
                    presenter.modifyReview(user.getId(), buildReviewRequest());
                else
                    presenter.writeReview(user.getId(), buildReviewRequest());

            }
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data, Bundle bundle) {
        if (RESULT_OK != resultCode) {
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private boolean isValidCheckBasic() {
        if (ratingBar1.getRating() == 0) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.review_rank_validate)
                    .setMessage(R.string.review_rank_validate)
                    .setPositiveButton(R.string.common_button_confirm, null)
                    .show();
            return false;
        }
        if(review.getText().equals("")){
            new AlertDialog.Builder(context)
                    .setTitle(R.string.review_review_validate)
                    .setMessage(R.string.review_review_validate)
                    .setPositiveButton(R.string.common_button_confirm, null)
                    .show();
        }
        return true;
    }

    private TeacherReviewRequest buildReviewRequest() {
        //int ranking = reviewRankSpinner.getSelectedItemPosition();
        float rank = ratingBar1.getRating()*2;
        Integer ran = new Integer((int)rank);

        String reviewIdx = "0";
        if(cReview != null)
            reviewIdx = cReview.getReviewIdx().toString();

        return new TeacherReviewRequest.Builder()
                .setReviewidx(reviewIdx)
                .setRank(ran.toString())
                .setTeacherid(teacherId.toString())
                .setReview(review.getText().toString())
                .setName(user.getName())
                .build();
    }

    @Override
    public void successWriteReview(Boolean user) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean failWriteReview(CErrorCause errorCause) {
        return false;
    }

    @Override
    public void successGetUser(CUser user) {
        setContent(user);
    }



    private void setContent(CUser user) {
        if (user == null) {
            return;
        }
        teacher = user.getTeacher();
        photoSelector = new PhotoSelector(this);
        //profilePhotoView.loadProfileImage(user);
        //userNameView.setText(user.getName());
        basicInfoView.setContent(user);
        profileuniversity.setText(teacher.getUniversity());
    }

    @Override
    public boolean failGetUser(CErrorCause errorCause) {
        return false;
    }

    @Override
    public void successGetReview(CReview review) {
       setUserReview(review);
    }

    @Override
    public boolean failGetReview(CErrorCause errorCause) {
        return false;
    }

    @Override
    public void successModifyReview(Boolean result) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean failModifyReview(CErrorCause errorCause) {
        return false;
    }
}
