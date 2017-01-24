package com.riverauction.riverauction.feature.review;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CLocation;
import com.riverauction.riverauction.api.model.CReview;
import com.riverauction.riverauction.api.model.CStudent;
import com.riverauction.riverauction.api.model.CStudentStatus;
import com.riverauction.riverauction.api.model.CTeacher;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.api.model.CUserType;
import com.riverauction.riverauction.api.service.auth.request.StudentBasicInformationRequest;
import com.riverauction.riverauction.api.service.auth.request.TeacherReviewRequest;
import com.riverauction.riverauction.base.BaseActivity;
import com.riverauction.riverauction.feature.photo.PhotoSelector;
import com.riverauction.riverauction.feature.photo.ProfileImageView;
import com.riverauction.riverauction.feature.teacher.TeacherDetailActivity;
import com.riverauction.riverauction.states.UserStates;
import com.riverauction.riverauction.widget.spinner.SpinnerAdapter;

import javax.inject.Inject;

import butterknife.Bind;

public class ReviewWriteActivity extends BaseActivity implements ReviewWriteMvpView {
    private final static int REQUEST_SEARCH_LOCATION = 0x01;

    private static final String EXTRA_PREFIX = "com.riverauction.riverauction.feature.teacher.TeacherDetailActivity.";
    public static final String EXTRA_USER_ID = EXTRA_PREFIX + "extra_user_id";

    private static final String EXTRA_PREFIX2 = "com.riverauction.riverauction.feature.review.ReviewList.";
    public static final String EXTRA_USER_ID2 = EXTRA_PREFIX2 + "extra_user_id";
    public static final String EXTRA_REVIEW_IDX = EXTRA_PREFIX2 + "extra_review_idx";
    private PhotoSelector photoSelector;
    private String profileImagePath;
    @Inject
    ReviewWritePresenter presenter;

    // basic
    @Bind(R.id.review_rank) Spinner reviewRankSpinner;
    @Bind(R.id.review) EditText review;
    @Bind(R.id.profile_photo_view) ProfileImageView profilePhotoView;
    @Bind(R.id.profile_user_name) TextView userNameView;
    @Bind(R.id.profile_university) TextView profileuniversity;
    // address 정보
    private CLocation location;
    private CUser user;
    private int reviewIdx;
    private CTeacher teacher;
    private Integer teacherId;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_review_write;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromBundle(getIntent().getExtras());
        getActivityComponent().inject(this);
        presenter.attachView(this, this);
        user = UserStates.USER.get(stateCtx);

        getSupportActionBar().setTitle(R.string.sign_up_basic_info_action_bar_title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // basic
        initializeRankSpinner();
        presenter.getUserProfile(teacherId, true);

        //수정인 경우
        if(reviewIdx>0)
        {
            presenter.getUserReview(reviewIdx);
        }

    }

    private  void setUserReview(CReview review){

    }

    // teacherId 를 이전 화면에서 넘겨받는다
    private void getDataFromBundle(Bundle bundle) {
        if (bundle != null) {
            //실제로 티처아이디다
            teacherId = bundle.getInt(TeacherDetailActivity.EXTRA_USER_ID, -1);

            if (teacherId == -1) {
                //리뷰리스트에서 온 경우
                teacherId = bundle.getInt(EXTRA_USER_ID2, -1);
                reviewIdx = bundle.getInt(EXTRA_REVIEW_IDX, -1);
                if (teacherId == -1) {
                    throw new IllegalStateException("teacherId must be exist");
                }
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

    /**
     * User data 로 초기화
     * @param user
     */
    private void setRank(CUser user) {
        CStudent student = user.getStudent();
        CStudentStatus studentStatus = student.getStudentStatus();
        switch (studentStatus) {
            case KINDERGARTEN: {
                reviewRankSpinner.setSelection(1);
                break;
            }
            case ELEMENTARY_SCHOOL: {
                reviewRankSpinner.setSelection(2);
                break;
            }
            case MIDDLE_SCHOOL: {
                reviewRankSpinner.setSelection(3);
                break;
            }
            case HIGH_SCHOOL: {
                reviewRankSpinner.setSelection(4);
                break;
            }
            case UNIVERSITY: {
                reviewRankSpinner.setSelection(5);
                break;
            }
            case RETRY_UNIVERSITY: {
                reviewRankSpinner.setSelection(6);
                break;
            }
            case ORDINARY: {
                reviewRankSpinner.setSelection(7);
                break;
            }
        }
/*
        handler.postDelayed(() -> {
            Integer grade = student.getGrade();
            if (grade != null && grade > 0) {
                gradeSpinner.setSelection(grade);
            }
        }, 500);

        CStudentDepartmentType studentDepartmentType = student.getDepartment();
        switch (studentDepartmentType) {
            case LIBERAL_ARTS: {
                departmentTypeSpinner.setSelection(1);
                break;
            }
            case NATURAL_SCIENCES: {
                departmentTypeSpinner.setSelection(2);
                break;
            }
            case ART_MUSIC_PHYSICAL: {
                departmentTypeSpinner.setSelection(3);
                break;
            }
            case COMMERCIAL_AND_TECHNICAL: {
                departmentTypeSpinner.setSelection(4);
                break;
            }
            case NONE: {
                departmentTypeSpinner.setSelection(5);
                break;
            }
        }
*/
        //location = user.getLocation();
        //review.setText("");
    }

    private void initializeRankSpinner() {
        SpinnerAdapter reviewRankSpinnerAdapter = new SpinnerAdapter(this, R.layout.item_spinner);
        reviewRankSpinnerAdapter.addItem(getString(R.string.review_rank_hint));
        reviewRankSpinnerAdapter.addItem(getString(R.string.review_rank_0_5));
        reviewRankSpinnerAdapter.addItem(getString(R.string.review_rank_1));
        reviewRankSpinnerAdapter.addItem(getString(R.string.review_rank_1_5));
        reviewRankSpinnerAdapter.addItem(getString(R.string.review_rank_2));
        reviewRankSpinnerAdapter.addItem(getString(R.string.review_rank_2_5));
        reviewRankSpinnerAdapter.addItem(getString(R.string.review_rank_3));
        reviewRankSpinner.setAdapter(reviewRankSpinnerAdapter);
    }

    private boolean isValidCheckBasic() {
        if (reviewRankSpinner.getSelectedItemPosition() == 0) {
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
        String ranking = reviewRankSpinner.getSelectedItem().toString();
        return new TeacherReviewRequest.Builder()
                .setRank("3")
                .setTeacherid(teacherId.toString())
                .setReview(review.getText().toString())
                .build();
    }

    private StudentBasicInformationRequest buildStudentBasicInformationRequest() {
        StudentBasicInformationRequest.Builder builder = new StudentBasicInformationRequest.Builder()
                .setType(CUserType.STUDENT)
                .setStudentStatus(getStudentStatus());
                //.setGrade(getGrade())
                //.setDepartment(getDepartmentType());

        if (location != null) {
            builder.setLocation(location);
        }
        return builder.build();
    }

    private CStudentStatus getStudentStatus() {
        switch (reviewRankSpinner.getSelectedItemPosition()) {
            case 1 : return CStudentStatus.KINDERGARTEN;
            case 2 : return CStudentStatus.ELEMENTARY_SCHOOL;
            case 3 : return CStudentStatus.MIDDLE_SCHOOL;
            case 4 : return CStudentStatus.HIGH_SCHOOL;
            case 5 : return CStudentStatus.UNIVERSITY;
            case 6 : return CStudentStatus.RETRY_UNIVERSITY;
            case 7 : return CStudentStatus.ORDINARY;
        }

        return null;
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
        profilePhotoView.loadProfileImage(user);
        userNameView.setText(user.getName());
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
}
