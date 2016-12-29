package com.riverauction.riverauction.feature.consult.write;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.avast.android.dialogs.fragment.ListDialogFragment;
import com.avast.android.dialogs.iface.IListDialogListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.riverauction.riverauction.eventbus.RiverAuctionEventBus;
import com.riverauction.riverauction.eventbus.UploadProfilePhotoEvent;
import com.riverauction.riverauction.feature.consult.BoardView;
import com.riverauction.riverauction.feature.photo.CPhotoInfo;
import com.riverauction.riverauction.feature.photo.PhotoSelector;
import com.riverauction.riverauction.feature.profile.ProfileActivity;
import com.riverauction.riverauction.feature.register.signup.SpinnerItemSelectedListener;
import com.riverauction.riverauction.feature.utils.PermissionUtils;
import com.riverauction.riverauction.states.UserStates;
import com.riverauction.riverauction.widget.spinner.SpinnerAdapter;

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;

import static com.riverauction.riverauction.api.model.CStudentStatus.ELEMENTARY_SCHOOL;
import static com.riverauction.riverauction.api.model.CStudentStatus.HIGH_SCHOOL;
import static com.riverauction.riverauction.api.model.CStudentStatus.KINDERGARTEN;
import static com.riverauction.riverauction.api.model.CStudentStatus.MIDDLE_SCHOOL;
import static com.riverauction.riverauction.api.model.CStudentStatus.ORDINARY;
import static com.riverauction.riverauction.api.model.CStudentStatus.RETRY_UNIVERSITY;
import static com.riverauction.riverauction.api.model.CStudentStatus.UNIVERSITY;

public class BoardWriteActivity extends BaseActivity implements ReviewWriteMvpView, IListDialogListener {
    private final static int REQUEST_SEARCH_LOCATION = 0x01;
    public static final int PERMISSION_STORAGE_CAMERA = 0x01;
    public static final int PERMISSION_STORAGE_GALLERY = 0x02;
    private PhotoSelector photoSelector;
    @Inject
    ReviewWritePresenter presenter;
    private static final int DIALOG_REQUEST_PROFILE_IMAGE = 0;
    // basic
    @Bind(R.id.board_select)
    Spinner boardSpinner;
    @Bind(R.id.category_select)
    Spinner categorySpinner;
    @Bind(R.id.subject)
    EditText subject;
    @Bind(R.id.content)
    EditText content;
    @Bind(R.id.profile_change_photo_container) View changePhotoButton;
    // address 정보
    private CLocation location;
    private CUser user;
    private int reviewIdx;
    private CTeacher teacher;
    private Integer teacherId;
    private Integer CATEGORY;
    private String boardImagePath;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

        boardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                initializeRankSpinner2(position);
                /*
                TextView titleView = (TextView) view.findViewById(R.id.item_spinner_title);
                if (position == 0) {
                    titleView.setTextColor(context.getResources().getColor(R.color.river_auction_greyish));
                } else {
                    titleView.setTextColor(context.getResources().getColor(R.color.river_auction_greyish_brown));
                }
                */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                /*
                TextView titleView = (TextView) view.findViewById(R.id.item_spinner_title);
                if (position == 0) {
                    titleView.setTextColor(context.getResources().getColor(R.color.river_auction_greyish));
                } else {
                    titleView.setTextColor(context.getResources().getColor(R.color.river_auction_greyish_brown));
                }
                */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        presenter.getUserProfile(teacherId, true);

        //수정인 경우
        if (reviewIdx > -1) {
            presenter.getUserReview(reviewIdx);
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        changePhotoButton.setOnClickListener(v -> showProfilePhotoDialog());
    }

    public void showProfilePhotoDialog() {
        String[] items = new String[]{
                getString(R.string.photo_request_from_camera),
                getString(R.string.photo_request_from_android_gallery)
        };
        ListDialogFragment
                .createBuilder(BoardWriteActivity.this, getSupportFragmentManager())
                .setItems(items)
                .setRequestCode(DIALOG_REQUEST_PROFILE_IMAGE)
                .show();
    }

    private void setUserReview(CReview review) {

    }

    // 카테고리 코드
    private void getDataFromBundle(Bundle bundle) {
        if (bundle != null) {
            //카테고리
            CATEGORY = bundle.getInt(BoardView.EXTRA_CATEGORY_ID, -1);
            //Toast.makeText(BoardWriteActivity.this, "CATEGORY=", Toast.LENGTH_SHORT);
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
        //boardSpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener(context));
        boardSpinner.setAdapter(boardSpinnerAdapter);
    }

    private void initializeRankSpinner2(int select1) {
        SpinnerAdapter categorAdapter = new SpinnerAdapter(this, R.layout.item_spinner);
        //categorySpinner.removeAllViews();
        switch (select1) {
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
        //categorySpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener(context));
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

        if (subject.getText().equals("")) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.board_text5)
                    .setMessage(R.string.board_text5)
                    .setPositiveButton(R.string.common_button_confirm, null)
                    .show();
        }
        if (content.getText().equals("")) {
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
            case 1:
                return KINDERGARTEN;
            case 2:
                return ELEMENTARY_SCHOOL;
            case 3:
                return MIDDLE_SCHOOL;
            case 4:
                return HIGH_SCHOOL;
            case 5:
                return UNIVERSITY;
            case 6:
                return RETRY_UNIVERSITY;
            case 7:
                return ORDINARY;
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

    @Override
    public void successPostProfilePhoto(CUser user) {
        UserStates.USER.set(stateCtx, user);
        RiverAuctionEventBus.getEventBus().post(new UploadProfilePhotoEvent());
    }

    @Override
    public boolean failPostProfilePhoto(CErrorCause errorCause) {
        return false;
    }

    @Override
    public boolean failPostPreferences(CErrorCause errorCause) {
        return false;
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("BoardWrite Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data, Bundle bundle) {
        super.onActivityResult(requestCode, resultCode, data, bundle);
        // profile photo change
        CPhotoInfo photoInfo = photoSelector.onActivityResult(requestCode, resultCode, data, bundle);
        if (photoInfo != null) {
            boardImagePath = photoInfo.getPath();
        }

        if (boardImagePath != null) {
            presenter.postProfilePhoto(user.getId(), new File(boardImagePath));
        }
    }

    @Override
    public void onListItemSelected(String value, int position, int requestCode) {
        if (requestCode == DIALOG_REQUEST_PROFILE_IMAGE) {
            if (position == 0) {
                PermissionUtils.checkSelfPermission(this,
                        Build.VERSION.SDK_INT >= 16 ? new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} : new String[]{},
                        PERMISSION_STORAGE_CAMERA, null);
            } else if (position == 1) {
                PermissionUtils.checkSelfPermission(this,
                        Build.VERSION.SDK_INT >= 16 ? new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} : new String[]{},
                        PERMISSION_STORAGE_GALLERY, null);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean granted = PermissionUtils.checkGranted(grantResults);
        if (!granted) {
            PermissionUtils.showDeniedMessage(this, permissions, grantResults);
        }

        switch (requestCode) {
            case PERMISSION_STORAGE_CAMERA:
                if (granted) {
                    photoSelector.requestImageFromCamera();
                }
                break;
            case PERMISSION_STORAGE_GALLERY:
                if (granted) {
                    photoSelector.requestImageFromAndroidGallery();
                }
                break;
        }
    }
}
