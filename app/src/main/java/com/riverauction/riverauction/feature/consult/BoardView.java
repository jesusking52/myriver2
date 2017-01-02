package com.riverauction.riverauction.feature.consult;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.jhcompany.android.libs.utils.DisplayUtils;
import com.riverauction.riverauction.R;
import com.riverauction.riverauction.RiverAuctionApplication;
import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CLesson;
import com.riverauction.riverauction.api.model.CLessonBidding;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.api.model.CUserType;
import com.riverauction.riverauction.base.BaseFrameLayout;
import com.riverauction.riverauction.eventbus.CancelEvent;
import com.riverauction.riverauction.eventbus.MakeBiddingEvent;
import com.riverauction.riverauction.eventbus.PostBiddingEvent;
import com.riverauction.riverauction.eventbus.RiverAuctionEventBus;
import com.riverauction.riverauction.eventbus.SelectTeacherEvent;
import com.riverauction.riverauction.feature.consult.filter.ConsultFilterActivity;
import com.riverauction.riverauction.feature.consult.write.BoardWriteActivity;
import com.riverauction.riverauction.feature.main.MainTabTracker;
import com.riverauction.riverauction.feature.main.MainTabTrackerListener;
import com.riverauction.riverauction.states.UserStates;
import com.riverauction.riverauction.widget.slidingtab.SlidingTabLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class BoardView extends BaseFrameLayout implements BoardMvpView, MainTabTrackerListener {

    private static final String EXTRA_PREFIX = "com.riverauction.riverauction.feature.consult.BoardView.";
    public static final String EXTRA_CATEGORY_ID = EXTRA_PREFIX + "category";
    @Inject
    BoardPresenter presenter;

    @Bind(R.id.filter_button) View filterButton;
    @Bind(R.id.board_active_empty_button) TextView boardWriteButton;
    @Bind(R.id.my_lesson_sliding_tabs) SlidingTabLayout slidingTabLayout;
    @Bind(R.id.my_lesson_view_pager) ViewPager viewPager;

    private MyLessonPagerAdapter adapter;
    private CUser user;

    public BoardView(Context context) {
        super(context);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.view_board;
    }

    @Override
    protected void initialize(Context context) {
        super.initialize(context);
        RiverAuctionApplication.getApplication().getComponent().inject(this);
        presenter.attachView(this, context);
        user = UserStates.USER.get(stateCtx);

        makeViewPagerSlidingTabLayout();
        MainTabTracker.registerTabCallback(this, 1, getContext());
        filterButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ConsultFilterActivity.class);
            if(viewPager.getCurrentItem()==0)
                intent.putExtra(EXTRA_CATEGORY_ID, 1);
            else  if(viewPager.getCurrentItem()==1)
                intent.putExtra(EXTRA_CATEGORY_ID, 2);
            else
                intent.putExtra(EXTRA_CATEGORY_ID, 3);
            getContext().startActivity(intent);
        });

        if (CUserType.TEACHER == user.getType()) {
            boardWriteButton.setVisibility(GONE);
        }
        boardWriteButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), BoardWriteActivity.class);
            if(viewPager.getCurrentItem()==0)
                intent.putExtra(EXTRA_CATEGORY_ID, 1);
            else  if(viewPager.getCurrentItem()==1)
                intent.putExtra(EXTRA_CATEGORY_ID, 2);
            else
                intent.putExtra(EXTRA_CATEGORY_ID, 3);

            getContext().startActivity(intent);
        });

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        RiverAuctionEventBus.getEventBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.detachView();
        if (RiverAuctionEventBus.getEventBus().isRegistered(this)) {
            RiverAuctionEventBus.getEventBus().unregister(this);
        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final MakeBiddingEvent event) {
        adapter.clearActiveLessonsAndBidding();
       // getActiveLessonAndLessonBiddings();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final SelectTeacherEvent event) {
        adapter.clearActiveLessonsAndBidding();
        //getActiveLessonAndLessonBiddings();
        adapter.clearHistory(1);
        adapter.clearHistory(2);
        adapter.clearHistory(3);
        getHistoryList(1,null);
        getHistoryList(2,null);
        getHistoryList(3,null);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final CancelEvent event) {
        adapter.clearActiveLessonsAndBidding();
        //getActiveLessonAndLessonBiddings();
        adapter.clearHistory(1);
        adapter.clearHistory(2);
        adapter.clearHistory(3);
        getHistoryList(1,null);
        getHistoryList(2,null);
        getHistoryList(3,null);
    }


    @SuppressWarnings("unused")
    public void onEventMainThread(final PostBiddingEvent event) {
        adapter.clearActiveLessons();
        getActiveLessonList(null);
    }


    private List<MyLessonTabPagerItem> makeTabPagerItems() {
        List<MyLessonTabPagerItem> tabPagerItems = Lists.newArrayList();
        tabPagerItems.add(new MyLessonTabPagerItem(R.string.consult_tab_school, 0));
        tabPagerItems.add(new MyLessonTabPagerItem(R.string.consult_tab_study, 1));
        tabPagerItems.add(new MyLessonTabPagerItem(R.string.consult_tab_worry, 2));
        return tabPagerItems;
    }

    private void makeViewPagerSlidingTabLayout() {
        adapter = new MyLessonPagerAdapter(makeTabPagerItems());
        viewPager.setAdapter(adapter);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(android.R.color.transparent));
        slidingTabLayout.setDividerColors(android.R.color.transparent);
        slidingTabLayout.setTabCustomViewProvider(adapter);
        slidingTabLayout.setTabCustomLayoutParamsProvider(adapter);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public void onEnterTab() {

        if (CUserType.TEACHER == user.getType()) {
            adapter.clearActiveLessons();
            getActiveLessonList(null);
            adapter.clearHistory(1);
            adapter.clearHistory(2);
            adapter.clearHistory(3);
            getHistoryList(1,null);
            getHistoryList(2,null);
            getHistoryList(3,null);
        } else if (CUserType.STUDENT == user.getType()) {
            adapter.clearActiveLessonsAndBidding();
            getActiveLessonList(null);
            //getActiveLessonAndLessonBiddings();
            adapter.clearHistory(1);
            adapter.clearHistory(2);
            adapter.clearHistory(3);
            getHistoryList(1,null);
            getHistoryList(2,null);
            getHistoryList(3,null);
        }

    }

    @Override
    public void onLeaveTab() {
    }

    private void getActiveLessonList(Integer nextToken) {
        presenter.getActiveLessons(user.getId(), nextToken);
    }
/*
    private void getActiveLessonAndLessonBiddings() {
        presenter.getActiveLessonAndLessonBiddings(user.getId());
    }
    */
    private void getHistoryList(Integer boardCategory,Integer nextToken) {
        presenter.getHistoryLessons(boardCategory,user.getId(), nextToken);
    }


    @Override
    public void successGetActiveList(List<CLesson> lessons, Integer nextToken) {
        adapter.setActiveViewResult(lessons, nextToken);
    }

    @Override
    public boolean failGetActiveList(CErrorCause errorCause) {
        adapter.setActiveViewError();
        return false;
    }

    @Override
    public void loadingGetActiveList() {
        adapter.setActiveViewLoading();
    }

      @Override
      public void successGetHistoryList(Integer boardid, List<CLesson> lessons, Integer nextToken) {
          adapter.setHistoryViewResult(boardid,lessons, nextToken);
      }

      @Override
      public boolean failGetHistoryList(Integer boardid, CErrorCause errorCause) {
          adapter.setHistoryViewError( boardid);
          return false;
      }

      @Override
      public void loadingGetHistoryList(Integer boardid) {
          adapter.setHistoryViewLoading(boardid);
      }

    @Override
    public void successCancelLesson(CLesson lesson) {

        adapter.clearHistory(1);
        getHistoryList(1,null);
        getHistoryList(2,null);
        getHistoryList(3,null);
    }

    @Override
    public boolean failCancelLesson(CErrorCause errorCause) {
        return false;
    }

    /**
     * View Pager 의 Adapter
     */
    private class MyLessonPagerAdapter extends PagerAdapter implements SlidingTabLayout.TabCustomViewProvider, SlidingTabLayout.TabCustomLayoutParamsProvider {

        private BoardActiveStudentView boardActiveStudentView;
        private BoardActiveTeacherView boardActiveTeacherView;
        private MyLessonHistoryView myLessonHistoryView;
        private MyLessonHistoryView myLessonHistoryView2;
        private MyLessonHistoryView myLessonHistoryView3;
        private List<MyLessonTabPagerItem> tabPagerItems;

        public MyLessonPagerAdapter(List<MyLessonTabPagerItem> tabPagerItems) {
            this.tabPagerItems = tabPagerItems;
        }

        @Override
        public int getCount() {
            return tabPagerItems.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;

            switch (position) {
                case 0: {
                    myLessonHistoryView = new MyLessonHistoryView(getContext()) {
                        @Override
                        public void loadMore(Integer nextToken) {
                            getHistoryList(1,nextToken);

                        }
                    };
                    view = myLessonHistoryView;
                    getHistoryList(1,null);
                    break;
                }
                case 1: {
                    myLessonHistoryView2 = new MyLessonHistoryView(getContext()) {
                        @Override
                        public void loadMore(Integer nextToken) {
                            getHistoryList(2,nextToken);
                        }
                    };
                    view = myLessonHistoryView2;
                    getHistoryList(2,null);
                    break;
                }

                case 2: {
                    myLessonHistoryView3 = new MyLessonHistoryView(getContext()) {
                        @Override
                        public void loadMore(Integer nextToken) {
                            getHistoryList(3,nextToken);
                        }
                    };
                    view = myLessonHistoryView3;
                    getHistoryList(3,null);
                    break;
                }

            }

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
            return tabPagerItems.get(position).createTabView(getContext());
        }

        @Override
        public ViewGroup.LayoutParams getLayoutParams() {
            int tabViewWidth = DisplayUtils.getDisplayWidthRaw(getContext()) / getCount() - DisplayUtils.getPixelFromDP(getContext(), 6);
            return new ViewGroup.LayoutParams(tabViewWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        // student
        public void setActiveViewStudentLoading() {
            if (boardActiveStudentView != null) {
                boardActiveStudentView.setLoading();
            }
        }
        public void setActiveViewStudentResult(CLesson lessons, List<CLessonBidding> biddings, Integer nextToken, int totalCount) {
            if (boardActiveStudentView != null) {
                boardActiveStudentView.setContent(lessons, biddings, nextToken, totalCount);
            }
        }
        public void setActiveViewStudentResult(List<CLessonBidding> biddings, Integer nextToken) {
            if (boardActiveStudentView != null) {
                boardActiveStudentView.setContent(biddings, nextToken);
            }
        }
        public void setActiveViewStudentError() {
            if (boardActiveStudentView != null) {
                boardActiveStudentView.setError();
            }
        }
        public void clearActiveLessonsAndBidding() {
            if (boardActiveStudentView != null) {
                boardActiveStudentView.clear();
            }
        }

        // active
        public void setActiveViewLoading() {
            if (boardActiveTeacherView != null) {
                boardActiveTeacherView.setLoading();
            }
        }
        public void setActiveViewResult(List<CLesson> lessons, Integer nextToken) {
            if (boardActiveTeacherView != null) {
                boardActiveTeacherView.setContent(lessons, nextToken);
            }
        }
        public void setActiveViewError() {
            if (boardActiveTeacherView != null) {
                boardActiveTeacherView.setError();
            }
        }
        public void clearActiveLessons() {
            if (boardActiveTeacherView != null) {
                boardActiveTeacherView.clear();
            }
        }

        // history
        public void setHistoryViewLoading(Integer boardid) {

            if (boardid == 1) {
                myLessonHistoryView.setLoading();
            }
            else if (boardid == 2) {
                myLessonHistoryView2.setLoading();
            }
            else if (boardid == 3) {
                myLessonHistoryView3.setLoading();
            }
        }
        public void setHistoryViewResult(Integer boardid, List<CLesson> lessons, Integer nextToken) {

            if (boardid == 1) {
                myLessonHistoryView.setContent(lessons, nextToken);
            }
            else if (boardid == 2) {
                myLessonHistoryView2.setContent(lessons, nextToken);
            }
            else if (boardid == 3) {
                myLessonHistoryView3.setContent(lessons, nextToken);
            }
        }
        public void setHistoryViewError(Integer boardid) {

            if (boardid == 1) {
                myLessonHistoryView.setError();
            }
            else if (boardid == 2) {
                myLessonHistoryView2.setError();
            }
            else if (boardid == 3) {
                myLessonHistoryView3.setError();
            }
        }
        public void clearHistory(Integer boardid) {
            if (boardid == 1) {
                myLessonHistoryView.clear();
            }
            else if (boardid == 2) {
                myLessonHistoryView2.clear();
            }
            else if (boardid == 3) {
                myLessonHistoryView3.clear();
            }
        }
    }
}
