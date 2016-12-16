package com.riverauction.riverauction.feature.consult;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

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

    @Inject
    BoardPresenter presenter;

    @Bind(R.id.filter_button) View filterButton;
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
    protected void initialize(Context context) {
        super.initialize(context);
        RiverAuctionApplication.getApplication().getComponent().inject(this);
        presenter.attachView(this, context);
        user = UserStates.USER.get(stateCtx);

        makeViewPagerSlidingTabLayout();
        MainTabTracker.registerTabCallback(this, 1, getContext());
        filterButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ConsultFilterActivity.class);
            getContext().startActivity(intent);
        });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.view_board;
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
        getActiveLessonAndLessonBiddings();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final SelectTeacherEvent event) {
        adapter.clearActiveLessonsAndBidding();
        getActiveLessonAndLessonBiddings();
        adapter.clearHistory();
        getHistoryList(null);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final PostBiddingEvent event) {
        adapter.clearActiveLessons();
        getActiveLessonList(null);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final CancelEvent event) {
        adapter.clearActiveLessonsAndBidding();
        getActiveLessonAndLessonBiddings();
        adapter.clearHistory();
        getHistoryList(null);
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
            adapter.clearHistory();
            getHistoryList(null);
        } else if (CUserType.STUDENT == user.getType()) {
            adapter.clearActiveLessonsAndBidding();
            getActiveLessonAndLessonBiddings();
            adapter.clearHistory();
            getHistoryList(null);
        }
    }

    @Override
    public void onLeaveTab() {
    }

    private void getActiveLessonList(Integer nextToken) {
        presenter.getActiveLessons(user.getId(), nextToken);
    }

    private void getActiveLessonAndLessonBiddings() {
        presenter.getActiveLessonAndLessonBiddings(user.getId());
    }

    private void getHistoryList(Integer nextToken) {
        presenter.getHistoryLessons(user.getId(), nextToken);
    }

    @Override
    public void successGetActiveLessonAndBiddings(CLesson lesson, List<CLessonBidding> lessonBiddingList, Integer nextToken, int totalCount) {
        adapter.setActiveViewStudentResult(lesson, lessonBiddingList, nextToken, totalCount);
    }

    @Override
    public void successGetLessonBiddings(List<CLessonBidding> lessonBiddingList, Integer nextToken) {
        adapter.setActiveViewStudentResult(lessonBiddingList, nextToken);
    }

    @Override
    public boolean failGetLessonBiddings(CErrorCause errorCause) {
        adapter.setActiveViewStudentError();
        return false;
    }

    @Override
    public void loadingGetActiveLessonList() {
        adapter.setActiveViewStudentLoading();
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
    public void successGetHistoryList(List<CLesson> lessons, Integer nextToken) {
        adapter.setHistoryViewResult(lessons, nextToken);
    }

    @Override
    public boolean failGetHistoryList(CErrorCause errorCause) {
        adapter.setHistoryViewError();
        return false;
    }

    @Override
    public void loadingGetHistoryList() {
        adapter.setHistoryViewLoading();
    }

    @Override
    public void successCancelLesson(CLesson lesson) {
        adapter.clearActiveLessonsAndBidding();
        getActiveLessonAndLessonBiddings();
        adapter.clearHistory();
        getHistoryList(null);
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
                    if (CUserType.STUDENT == user.getType()) {
                        boardActiveStudentView = new BoardActiveStudentView(getContext()) {
                            @Override
                            public void firstLoad(Integer userId) {
                                getActiveLessonAndLessonBiddings();
                            }

                            @Override
                            public void loadMore(Integer lessonId, Integer nextToken) {
                                presenter.getLessonBiddings(lessonId, nextToken);
                            }
                        };
                        boardActiveStudentView.setOnMyLessonButtonClickListener(new BoardActiveStudentView.OnMyLessonButtonClickListener() {
                            @Override
                            public void makeLesson() {
                                Intent intent = new Intent(getContext(), BoardWriteActivity.class);
                                getContext().startActivity(intent);
                            }

                            @Override
                            public void cancelLesson(CLesson lesson) {
                                new AlertDialog.Builder(getContext())
                                        .setTitle(R.string.menu_lesson_cancel)
                                        .setMessage(R.string.my_lesson_cancel_lesson_button)
                                        .setPositiveButton(R.string.common_button_confirm, (dialog, which) -> {
                                            presenter.cancelLesson(lesson.getId());
                                        })
                                        .show();
                            }
                        });
                        view = boardActiveStudentView;
                        getActiveLessonAndLessonBiddings();
                    } else {
                        boardActiveTeacherView = new BoardActiveTeacherView(getContext()) {
                            @Override
                            public void loadMore(Integer nextToken) {
                                getActiveLessonList(nextToken);
                            }
                        };
                        boardActiveTeacherView.setOnFindLessonClickListener(() -> {
                            // TODO: tab 이동
                        });
                        view = boardActiveTeacherView;
                        getActiveLessonList(null);
                    }
                    break;
                }
                case 1: {
                    if (CUserType.STUDENT == user.getType()) {
                        boardActiveStudentView = new BoardActiveStudentView(getContext()) {
                            @Override
                            public void firstLoad(Integer userId) {
                                getActiveLessonAndLessonBiddings();
                            }

                            @Override
                            public void loadMore(Integer lessonId, Integer nextToken) {
                                presenter.getLessonBiddings(lessonId, nextToken);
                            }
                        };
                        boardActiveStudentView.setOnMyLessonButtonClickListener(new BoardActiveStudentView.OnMyLessonButtonClickListener() {
                            @Override
                            public void makeLesson() {
                                Intent intent = new Intent(getContext(), BoardWriteActivity.class);
                                getContext().startActivity(intent);
                            }

                            @Override
                            public void cancelLesson(CLesson lesson) {
                                new AlertDialog.Builder(getContext())
                                        .setTitle(R.string.menu_lesson_cancel)
                                        .setMessage(R.string.my_lesson_cancel_lesson_button)
                                        .setPositiveButton(R.string.common_button_confirm, (dialog, which) -> {
                                            presenter.cancelLesson(lesson.getId());
                                        })
                                        .show();
                            }
                        });
                        view = boardActiveStudentView;
                        getActiveLessonAndLessonBiddings();
                    } else {
                        boardActiveTeacherView = new BoardActiveTeacherView(getContext()) {
                            @Override
                            public void loadMore(Integer nextToken) {
                                getActiveLessonList(nextToken);
                            }
                        };
                        boardActiveTeacherView.setOnFindLessonClickListener(() -> {
                            // TODO: tab 이동
                        });
                        view = boardActiveTeacherView;
                        getActiveLessonList(null);
                    }
                    break;
                }
                case 2: {
                    if (CUserType.STUDENT == user.getType()) {
                        boardActiveStudentView = new BoardActiveStudentView(getContext()) {
                            @Override
                            public void firstLoad(Integer userId) {
                                getActiveLessonAndLessonBiddings();
                            }

                            @Override
                            public void loadMore(Integer lessonId, Integer nextToken) {
                                presenter.getLessonBiddings(lessonId, nextToken);
                            }
                        };
                        boardActiveStudentView.setOnMyLessonButtonClickListener(new BoardActiveStudentView.OnMyLessonButtonClickListener() {
                            @Override
                            public void makeLesson() {
                                Intent intent = new Intent(getContext(), BoardWriteActivity.class);
                                getContext().startActivity(intent);
                            }

                            @Override
                            public void cancelLesson(CLesson lesson) {
                                new AlertDialog.Builder(getContext())
                                        .setTitle(R.string.menu_lesson_cancel)
                                        .setMessage(R.string.my_lesson_cancel_lesson_button)
                                        .setPositiveButton(R.string.common_button_confirm, (dialog, which) -> {
                                            presenter.cancelLesson(lesson.getId());
                                        })
                                        .show();
                            }
                        });
                        view = boardActiveStudentView;
                        getActiveLessonAndLessonBiddings();
                    } else {
                        boardActiveTeacherView = new BoardActiveTeacherView(getContext()) {
                            @Override
                            public void loadMore(Integer nextToken) {
                                getActiveLessonList(nextToken);
                            }
                        };
                        boardActiveTeacherView.setOnFindLessonClickListener(() -> {
                            // TODO: tab 이동
                        });
                        view = boardActiveTeacherView;
                        getActiveLessonList(null);
                    }
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
        public void setHistoryViewLoading() {
            if (myLessonHistoryView != null) {
                myLessonHistoryView.setLoading();
            }
        }
        public void setHistoryViewResult(List<CLesson> lessons, Integer nextToken) {
            if (myLessonHistoryView != null) {
                myLessonHistoryView.setContent(lessons, nextToken);
            }
        }
        public void setHistoryViewError() {
            if (myLessonHistoryView != null) {
                myLessonHistoryView.setError();
            }
        }
        public void clearHistory() {
            if (myLessonHistoryView != null) {
                myLessonHistoryView.clear();
            }
        }
    }
}
