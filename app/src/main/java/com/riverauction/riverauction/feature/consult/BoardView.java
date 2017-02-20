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
import com.riverauction.riverauction.api.model.CBoard;
import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CLesson;
import com.riverauction.riverauction.api.model.CLessonBidding;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.api.model.CUserType;
import com.riverauction.riverauction.api.service.board.params.GetBoardsParams;
import com.riverauction.riverauction.base.BaseFrameLayout;
import com.riverauction.riverauction.eventbus.BoardFilterEvent;
import com.riverauction.riverauction.eventbus.RiverAuctionEventBus;
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
    private GetBoardsParams.Builder builder;
    @Bind(R.id.filter_button) View filterButton;
    @Bind(R.id.board_active_empty_button) TextView boardWriteButton;
    @Bind(R.id.board_sliding_tabs) SlidingTabLayout slidingTabLayout;
    @Bind(R.id.board_view_pager) ViewPager viewPager;

    private BoardPagerAdapter adapter;
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
        builder = createGetBoardsParamsBuilder();
        makeViewPagerSlidingTabLayout();
        MainTabTracker.registerTabCallback(this, 5, getContext());

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
    public void onEventMainThread(final BoardFilterEvent event) {
        builder = event.getBuilder();

        if(builder.category_id==1)
        {
            adapter.clearBoard(1);
            getBoardList(1,builder.build());
        }else if(builder.category_id==2) {
            adapter.clearBoard(2);
            getBoardList(2,builder.build());
        }else if(builder.category_id==3) {
            adapter.clearBoard(3);
            getBoardList(3,builder.build());
        }

    }

    private List<BoardPagerItem> makeTabPagerItems() {
        List<BoardPagerItem> tabPagerItems = Lists.newArrayList();
        tabPagerItems.add(new BoardPagerItem(R.string.consult_tab_school, 0));
        tabPagerItems.add(new BoardPagerItem(R.string.consult_tab_study, 1));
        tabPagerItems.add(new BoardPagerItem(R.string.consult_tab_worry, 2));
        return tabPagerItems;
    }

    private void makeViewPagerSlidingTabLayout() {
        adapter = new BoardPagerAdapter(makeTabPagerItems());
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
            adapter.clearBoard(1);
            adapter.clearBoard(2);
            adapter.clearBoard(3);
            getBoardList(1,null);
            getBoardList(2,null);
            getBoardList(3,null);
        } else if (CUserType.STUDENT == user.getType()) {
            adapter.clearBoard(1);
            adapter.clearBoard(2);
            adapter.clearBoard(3);
            getBoardList(1,null);
            getBoardList(2,null);
            getBoardList(3,null);
        }
    }

    @Override
    public void onLeaveTab() {
    }

    private void getActiveBoardList(Integer nextToken) {
        //presenter.getActiveBoard(user.getId(), nextToken);
    }


    private void getBoardList(Integer boardCategory,GetBoardsParams params) {
        if (params!=null) {
            presenter.getBoardList(boardCategory, params);
        }else{
            builder = createGetBoardsParamsBuilder();
            builder.setreply_idx(0);
            presenter.getBoardList(boardCategory, builder.build());
        }
    }

      @Override
      public void successBoardList(Integer boardid, List<CBoard> board, Integer nextToken) {
          adapter.setBoardViewResult(boardid, board, nextToken);
      }

      @Override
      public boolean failGetBoardList(Integer boardid, CErrorCause errorCause) {
          adapter.setBoardViewError( boardid);
          return false;
      }

      @Override
      public void loadingGetBoardList(Integer boardid) {
          adapter.setBoardViewLoading(boardid);
      }

    /**
     * 비어있는 GetBoardsParams.Builder 를 만든다
     */
    private GetBoardsParams.Builder createGetBoardsParamsBuilder() {
        return new GetBoardsParams.Builder();
    }
    /**
     * View Pager 의 Adapter
     */
    private class BoardPagerAdapter extends PagerAdapter implements SlidingTabLayout.TabCustomViewProvider, SlidingTabLayout.TabCustomLayoutParamsProvider {

        private BoardActiveStudentView boardActiveStudentView;
        private BoardActiveTeacherView boardActiveTeacherView;
        private BoardListView boardListView;
        private BoardListView boardListView2;
        private BoardListView boardListView3;
        private List<BoardPagerItem> tabPagerItems;

        public BoardPagerAdapter(List<BoardPagerItem> tabPagerItems) {
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
                    boardListView = new BoardListView(getContext()) {
                        @Override
                        public void loadMore(Integer nextToken) {
                            getBoardList(1,builder.build());

                        }
                    };
                    view = boardListView;
                    getBoardList(1,null);
                    break;
                }
                case 1: {
                    boardListView2 = new BoardListView(getContext()) {
                        @Override
                        public void loadMore(Integer nextToken) {
                            getBoardList(2,builder.build());
                        }
                    };
                    view = boardListView2;
                    getBoardList(2,null);
                    break;
                }

                case 2: {
                    boardListView3 = new BoardListView(getContext()) {
                        @Override
                        public void loadMore(Integer nextToken) {
                            getBoardList(3,builder.build());
                        }
                    };
                    view = boardListView3;
                    getBoardList(3,null);
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
        public void setBoardViewLoading(Integer boardid) {

            if (boardid == 1) {
                boardListView.setLoading();
            }
            else if (boardid == 2) {
                boardListView2.setLoading();
            }
            else if (boardid == 3) {
                boardListView3.setLoading();
            }
        }
        public void setBoardViewResult(Integer boardid, List<CBoard> board, Integer nextToken) {

            if (boardid == 1) {
                boardListView.setContent(board, nextToken);
            }
            else if (boardid == 2) {
                boardListView2.setContent(board, nextToken);
            }
            else if (boardid == 3) {
                boardListView3.setContent(board, nextToken);
            }
        }
        public void setBoardViewError(Integer boardid) {

            if (boardid == 1) {
                boardListView.setError();
            }
            else if (boardid == 2) {
                boardListView2.setError();
            }
            else if (boardid == 3) {
                boardListView3.setError();
            }
        }
        public void clearBoard(Integer boardid) {
            if (boardid == 1) {
                boardListView.clear();
            }
            else if (boardid == 2) {
                boardListView2.clear();
            }
            else if (boardid == 3) {
                boardListView3.clear();
            }
        }
    }
}
