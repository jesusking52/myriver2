package com.riverauction.riverauction.feature.review;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;
import com.jhcompany.android.libs.utils.DisplayUtils;
import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CReview;
import com.riverauction.riverauction.api.model.CTeacher;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.api.model.CUserFavorite;
import com.riverauction.riverauction.api.service.APISuccessResponse;
import com.riverauction.riverauction.base.BaseActivity;
import com.riverauction.riverauction.eventbus.MakeBiddingEvent;
import com.riverauction.riverauction.eventbus.RiverAuctionEventBus;
import com.riverauction.riverauction.feature.common.ReviewInfoView;
import com.riverauction.riverauction.feature.mylesson.MyLessonTabPagerItem;
import com.riverauction.riverauction.feature.notification.favorite.FavoriteStudentView;
import com.riverauction.riverauction.feature.notification.favorite.FavoriteTeacherView;
import com.riverauction.riverauction.feature.review.message.MessageView;
import com.riverauction.riverauction.widget.slidingtab.SlidingTabLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class ReviewList_BAK extends BaseActivity implements ReviewListMvpView {

    private static final String EXTRA_PREFIX = "com.riverauction.riverauction.feature.teacher.TeacherDetailActivity.";
    public static final String EXTRA_USER_ID = EXTRA_PREFIX + "extra_user_id";
    @Inject
    ReviewListPresenter presenter;
    //@Bind(R.id.notification_sliding_tabs) SlidingTabLayout slidingTabLayout;
    @Bind(R.id.notification_view_pager) ViewPager viewPager;
    // basic
    @Bind(R.id.basic_info_view) ReviewInfoView basicInfoView;
    private ReviewListAdapter adapter;
    // 로그인 한 유저
    private CUser user;
    private CTeacher teacher;
    private Integer userId;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_reviewlist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //basic profile
        /*
        getDataFromBundle(getIntent().getExtras());
        //뷰 리스트
        RiverAuctionEventBus.getEventBus().register(this);
        getActivityComponent().inject(this);
        presenter.attachView(this, this);
        presenter.getUserProfile(userId, true);
        user = UserStates.USER.get(stateCtx);
        getSupportActionBar().setTitle(R.string.review_list_title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        */
    }

    // userId 를 이전 화면에서 넘겨받는다
    private void getDataFromBundle(Bundle bundle) {
        if (bundle != null) {
            //실제로 티처아이디다
            userId = bundle.getInt(EXTRA_USER_ID, -1);
            if (userId == -1) {
                throw new IllegalStateException("userId must be exist");
            }
        }
    }


    private void makeViewPagerSlidingTabLayout() {
        adapter = new ReviewListAdapter();
        viewPager.setAdapter(adapter);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        if (RiverAuctionEventBus.getEventBus().isRegistered(this)) {
            RiverAuctionEventBus.getEventBus().unregister(this);
        }
    }

    private void setContent(CUser user2) {
        if (user2 == null) {
            return;
        }
        basicInfoView.setContent(user2);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final MakeBiddingEvent event) {
        adapter.setStudentFavoriteViewLoading();
        presenter.getReviews(user.getId(), null);
    }

    @Override
    public void successGetReviews(APISuccessResponse<List<CReview>> response, Integer newNextToken) {
        adapter.setMessageViewResult((List<CReview>) response, newNextToken);
    }

    @Override
    public boolean failGetReviews(CErrorCause errorCause) {
        return false;
    }

    @Override
    public void successGetUser(CUser user) {
        setContent(user);
    }

    @Override
    public boolean failGetUser(CErrorCause errorCause) {
        return false;
    }

    /**
     * View Pager 의 Adapter
     */
    private class ReviewListAdapter extends PagerAdapter implements SlidingTabLayout.TabCustomViewProvider, SlidingTabLayout.TabCustomLayoutParamsProvider {

        private MessageView messageView;
        private FavoriteStudentView favoriteStudentView;
        private FavoriteTeacherView favoriteTeacherView;
        private List<MyLessonTabPagerItem> tabPagerItems = Lists.newArrayList();
        public ReviewListAdapter() {

        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            messageView = new MessageView(ReviewList_BAK.this) {
                @Override
                public void loadMore(Integer nextToken) {
                    presenter.getReviews(user.getId(), nextToken);
                }
            };
            messageView.setLoading();
            presenter.getReviews(user.getId(), null);
            view = messageView;

            if (view != null) {
                container.addView(view);
            }

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public View getTabCustomView(int position) {
            return tabPagerItems.get(position).createTabView(context);
        }

        @Override
        public ViewGroup.LayoutParams getLayoutParams() {
            int tabViewWidth = DisplayUtils.getDisplayWidthRaw(context) / getCount() - DisplayUtils.getPixelFromDP(context, 6);
            return new ViewGroup.LayoutParams(tabViewWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        // message
        public void setMessageViewLoading() {
            messageView.setLoading();
        }

        public void setMessageViewResult(List<CReview> revieww, Integer nextToken) {
            messageView.setContent(revieww, nextToken);
        }
        public void setMessageViewError() {
            messageView.setError();
        }

        // notification - student
        public void setStudentFavoriteViewLoading() {
            favoriteStudentView.setLoading();
        }
        public void setStudentFavoriteViewResult(List<CUserFavorite> favorites, Integer nextToken) {
            favoriteStudentView.setContent(favorites, nextToken);
        }
        public void setStudentFavoriteViewError() {
            favoriteStudentView.setError();
        }


    }
}
