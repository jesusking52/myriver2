package com.riverauction.riverauction.feature.consult.write;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CLocation;
import com.riverauction.riverauction.api.model.CReview;
import com.riverauction.riverauction.api.model.CStudentStatus;
import com.riverauction.riverauction.api.model.CTeacher;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.api.model.CUserType;
import com.riverauction.riverauction.api.service.auth.request.StudentBasicInformationRequest;
import com.riverauction.riverauction.api.service.auth.request.TeacherReviewRequest;
import com.riverauction.riverauction.base.BaseActivity;
import com.riverauction.riverauction.feature.consult.BoardView;
import com.riverauction.riverauction.feature.photo.PhotoSelector;
import com.riverauction.riverauction.states.UserStates;
import com.riverauction.riverauction.widget.spinner.SpinnerAdapter;

import javax.inject.Inject;

import butterknife.Bind;

import static com.riverauction.riverauction.api.model.CStudentStatus.ELEMENTARY_SCHOOL;
import static com.riverauction.riverauction.api.model.CStudentStatus.HIGH_SCHOOL;
import static com.riverauction.riverauction.api.model.CStudentStatus.KINDERGARTEN;
import static com.riverauction.riverauction.api.model.CStudentStatus.MIDDLE_SCHOOL;
import static com.riverauction.riverauction.api.model.CStudentStatus.ORDINARY;
import static com.riverauction.riverauction.api.model.CStudentStatus.RETRY_UNIVERSITY;
import static com.riverauction.riverauction.api.model.CStudentStatus.UNIVERSITY;

public class BoardWriteActivity extends BaseActivity implements ReviewWriteMvpView {
    private final static int REQUEST_SEARCH_LOCATION = 0x01;
    private PhotoSelector photoSelector;
    @Inject
    ReviewWritePresenter presenter;

    // basic
    @Bind(R.id.board_select) Spinner boardSpinner;
    @Bind(R.id.category_select) Spinner categorySpinner;
    @Bind(R.id.subject) EditText subject;
    @Bind(R.id.content) EditText content;
    // address 정보
    private CLocation location;
    private CUser user;
    private int reviewIdx;
    private CTeacher teacher;
    private Integer teacherId;
    private Integer CATEGORY;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_board_write;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromBundle(getIntent().getExtras());//변수 전달

        getActivityComponent().inject(this);
        presenter.attachView(this, this);
        user = UserStates.USER.get(stateCtx);

        getSupportActionBar().setTitle(R.string.board_write_button);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // basic
        initializeRankSpinner1();
        setCategory(CATEGORY);//카테고리 선택
        initializeRankSpinner2(CATEGORY);

        //boardSpinner.setOnClickListener(v -> initializeRankSpinner2(boardSpinner.getSelectedItemPosition()));
        presenter.getUserProfile(teacherId, true);

        //수정인 경우
        if(reviewIdx>-1)
        {
            presenter.getUserReview(reviewIdx);
        }

    }

    private  void setUserReview(CReview review){

    }

    // 카테고리 코드
    private void getDataFromBundle(Bundle bundle) {
        if (bundle != null) {
            //카테고리
            CATEGORY = bundle.getInt(BoardView.EXTRA_CATEGORY_ID, -1);
            Toast.makeText(BoardWriteActivity.this, "CATEGORY=", Toast.LENGTH_SHORT);
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

    private void setCategory(int point) {
        boardSpinner.setSelection(point);
    }

    private void initializeRankSpinner1() {
        SpinnerAdapter boardSpinnerAdapter = new SpinnerAdapter(this, R.layout.item_spinner);
        boardSpinnerAdapter.addItem(getString(R.string.board_spinner1));
        boardSpinnerAdapter.addItem(getString(R.string.consult_tab_school));
        boardSpinnerAdapter.addItem(getString(R.string.consult_tab_study));
        boardSpinnerAdapter.addItem(getString(R.string.consult_tab_worry));
        boardSpinner.setAdapter(boardSpinnerAdapter);
    }

    private void initializeRankSpinner2(int select1) {
        SpinnerAdapter categorAdapter = new SpinnerAdapter(this, R.layout.item_spinner);
        //categorySpinner.removeAllViews();
        switch (select1)
        {
            case 1:
                categorAdapter.addItem(getString(R.string.board_spinner2));
                categorAdapter.addItem(getString(R.string.board_spinner21));
                categorAdapter.addItem(getString(R.string.board_spinner22));
                categorAdapter.addItem(getString(R.string.board_spinner23));
                categorAdapter.addItem(getString(R.string.board_spinner24));
            break;
            case 2:
                categorAdapter.addItem(getString(R.string.board_spinner30));
                categorAdapter.addItem(getString(R.string.board_spinner31));
                categorAdapter.addItem(getString(R.string.board_spinner32));
                categorAdapter.addItem(getString(R.string.board_spinner33));
                break;
            case 3:
                categorAdapter.addItem(getString(R.string.board_spinner40));
                categorAdapter.addItem(getString(R.string.board_spinner41));
                categorAdapter.addItem(getString(R.string.board_spinner42));
                break;
        }

        categorySpinner.setAdapter(categorAdapter);
    }

    private boolean isValidCheckBasic() {
        if (boardSpinner.getSelectedItemPosition() == 0) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.board_spinner3)
                    .setMessage(R.string.board_spinner3)
                    .setPositiveButton(R.string.common_button_confirm, null)
                    .show();
            return false;
        }
        if (categorySpinner.getSelectedItemPosition() == 0) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.board_spinner4)
                    .setMessage(R.string.board_spinner4)
                    .setPositiveButton(R.string.common_button_confirm, null)
                    .show();
            return false;
        }

        if(subject.getText().equals("")){
            new AlertDialog.Builder(context)
                    .setTitle(R.string.board_text5)
                    .setMessage(R.string.board_text5)
                    .setPositiveButton(R.string.common_button_confirm, null)
                    .show();
        }
        if(content.getText().equals("")){
            new AlertDialog.Builder(context)
                    .setTitle(R.string.board_text6)
                    .setMessage(R.string.board_text6)
                    .setPositiveButton(R.string.common_button_confirm, null)
                    .show();
        }

        return true;
    }

    private TeacherReviewRequest buildReviewRequest() {
        return new TeacherReviewRequest.Builder()
                .setRank(boardSpinner.getSelectedItem().toString())
                .setReview(subject.getText().toString())
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
        switch (boardSpinner.getSelectedItemPosition()) {
            case 1 : return KINDERGARTEN;
            case 2 : return ELEMENTARY_SCHOOL;
            case 3 : return MIDDLE_SCHOOL;
            case 4 : return HIGH_SCHOOL;
            case 5 : return UNIVERSITY;
            case 6 : return RETRY_UNIVERSITY;
            case 7 : return ORDINARY;
        }

        return null;
    }

    @Override
    public void successPatchUser(CUser user) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean failPatchUser(CErrorCause errorCause) {
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
