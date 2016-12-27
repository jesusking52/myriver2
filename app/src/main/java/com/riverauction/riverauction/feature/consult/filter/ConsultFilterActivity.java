package com.riverauction.riverauction.feature.consult.filter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.riverauction.riverauction.R;
import com.riverauction.riverauction.base.BaseActivity;
import com.riverauction.riverauction.feature.consult.BoardView;

import javax.inject.Inject;

import butterknife.Bind;

public class ConsultFilterActivity extends BaseActivity implements ConsultFilterMvpView {

    private Integer CATEGORY;
    @Inject
    ConsultFilterPresenter presenter;
    @Bind(R.id.category1) View category1;
    @Bind(R.id.category2) View category2;
    @Bind(R.id.category3) View category3;
    //내가 쓴글
    @Bind(R.id.filter_available_subjects_container) View myContainer;
    @Bind(R.id.filter_available_my_icon) View myIconView;
    //대학
    @Bind(R.id.filter_university) View univContainer;
    @Bind(R.id.filter_univ_icon) View univIconView;
    //고등학교
    @Bind(R.id.filter_high_container) View highContainer;
    @Bind(R.id.filter_high_icon) View highIconView;
    //중학교
    @Bind(R.id.filter_middle_container) View middleContainer;
    @Bind(R.id.filter_middle_icon) View middleIconView;
    //유학
    @Bind(R.id.filter_overseas_container) View overseasContainer;
    @Bind(R.id.filter_overseas_icon) View overseasIconView;
    //인문
    @Bind(R.id.filter_human_container) View humanContainer;
    @Bind(R.id.filter_human_icon) View humanIconView;
    //자연
    @Bind(R.id.filter_nature_container) View natureContainer;
    @Bind(R.id.filter_nature_icon) View natureIconView;
    //기타
    @Bind(R.id.filter_other_container) View otherContainer;
    @Bind(R.id.filter_other_icon) View otherIconView;
    //학생
    @Bind(R.id.filter_student_container) View studentContainer;
    @Bind(R.id.filter_student_icon) View studentIconView;
    //학부모
    @Bind(R.id.filter_parents_container) View parentsContainer;
    @Bind(R.id.filter_parents_icon) View parentsIconView;
    @Bind(R.id.searchtext) EditText searchText;

/*
    private List<CSubject> selectedSubjects = Lists.newArrayList();

    @Bind(R.id.filter_price_container) View priceContainer;
    @Bind(R.id.filter_price_icon) View priceIconView;
    @Bind(R.id.filter_price_view) TextView priceView;
    private Integer highPrice;
    private Integer lowPrice;

    @Bind(R.id.filter_grade_container) View universityContainer;
    @Bind(R.id.filter_grade_icon) View universityIconView;
    @Bind(R.id.filter_grade_view) TextView universityView;
    private List<String> universities = Lists.newArrayList();

    @Bind(R.id.filter_location_container) View locationContainer;
    @Bind(R.id.filter_location_icon) View locationIconView;
    @Bind(R.id.filter_location_view) TextView locationView;
    private Integer minZoneCode;
    private Integer maxZoneCode;

    @Bind(R.id.filter_gender_container) View genderContainer;
    @Bind(R.id.filter_gender_icon) View genderIconView;
    @Bind(R.id.filter_gender_view) TextView genderView;
    private CGender preferredGender;
*/
    @Bind(R.id.filter_apply_button) View applyButton;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_consult_filter;
    }

    // 카테고리 코드
    private void getDataFromBundle(Bundle bundle) {
        if (bundle != null) {
            //카테고리
            CATEGORY = bundle.getInt(BoardView.EXTRA_CATEGORY_ID, -1);
            //Toast.makeText(BoardWriteActivity.this, "CATEGORY=", Toast.LENGTH_SHORT);

            switch (CATEGORY){
                case 1:
                    category1.setVisibility(View.VISIBLE);
                    category2.setVisibility(View.GONE);
                    category3.setVisibility(View.GONE);
                    break;
                case 2:
                    category1.setVisibility(View.GONE);
                    category2.setVisibility(View.VISIBLE);
                    category3.setVisibility(View.GONE);
                    break;
                case 3:
                    category1.setVisibility(View.GONE);
                    category2.setVisibility(View.GONE);
                    category3.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        presenter.attachView(this, this);
        getDataFromBundle(getIntent().getExtras());//변수 전달
        getSupportActionBar().setTitle(R.string.filter_action_bar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myContainer.setOnClickListener(v -> {
            if( myIconView.isSelected())
                myIconView.setSelected(false);
            else
                myIconView.setSelected(true);
        });

        univContainer.setOnClickListener(v -> {
            if( univIconView.isSelected())
                univIconView.setSelected(false);
            else
                univIconView.setSelected(true);
        });

        highContainer.setOnClickListener(v -> {
            if( highIconView.isSelected())
                highIconView.setSelected(false);
            else
                highIconView.setSelected(true);
        });

        middleContainer.setOnClickListener(v -> {
            if( middleIconView.isSelected())
                middleIconView.setSelected(false);
            else
                middleIconView.setSelected(true);
        });

        overseasContainer.setOnClickListener(v -> {
            if( overseasIconView.isSelected())
                overseasIconView.setSelected(false);
            else
                overseasIconView.setSelected(true);
        });

        humanContainer.setOnClickListener(v -> {
            if( humanIconView.isSelected())
                humanIconView.setSelected(false);
            else
                humanIconView.setSelected(true);
        });

        natureContainer.setOnClickListener(v -> {
            if( natureIconView.isSelected())
                natureIconView.setSelected(false);
            else
                natureIconView.setSelected(true);
        });

        otherContainer.setOnClickListener(v -> {
            if( otherIconView.isSelected())
                otherIconView.setSelected(false);
            else
                otherIconView.setSelected(true);
        });

        studentContainer.setOnClickListener(v -> {
            if( studentIconView.isSelected())
                studentIconView.setSelected(false);
            else
                studentIconView.setSelected(true);
        });

        parentsContainer.setOnClickListener(v -> {
            if( parentsIconView.isSelected())
                parentsIconView.setSelected(false);
            else
                parentsIconView.setSelected(true);
        });

        applyButton.setOnClickListener(v -> {
            search();
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter_initialization, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_filter_initialization) {
            // 초기화
            myIconView.setSelected(false);
            univIconView.setSelected(false);
            highIconView.setSelected(false);
            middleIconView.setSelected(false);
            overseasIconView.setSelected(false);
            humanIconView.setSelected(false);
            natureIconView.setSelected(false);
            otherIconView.setSelected(false);
            studentIconView.setSelected(false);
            parentsIconView.setSelected(false);
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

    /**
     * filter option 이 하나라도 걸려있는지 확인
     */
    private boolean hasFilterOption() {
        if (myIconView.isSelected()==false&& univIconView.isSelected()==false && highIconView.isSelected()==false &&
                middleIconView.isSelected()==false && overseasIconView.isSelected()==false && humanIconView.isSelected()==false&& natureIconView.isSelected()==false &&
        otherIconView.isSelected()==false && studentIconView.isSelected()==false && parentsIconView.isSelected()==false && searchText.length()==0) {
            // 필터 하나도 설정 안함
            return false;
        }

        return true;
    }

    private void search() {
        /*
        GetTeachersParams.Builder builder = new GetTeachersParams.Builder();
        builder.setSubjects(DataUtils.convertSubjectsToIds(selectedSubjects));
        builder.setLessThanPrice(highPrice);
        builder.setMoreThanPrice(lowPrice);
        builder.setUniversities(universities);
        builder.setMinZipCode(minZoneCode);
        builder.setMaxZipCode(maxZoneCode);
        builder.setGender(preferredGender);
        subjectsIconView.isSelected();
        RiverAuctionEventBus.getEventBus().post(new TeacherFilterEvent(builder));
        */
    }
}
