package com.riverauction.riverauction.feature.review;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CReview;
import com.riverauction.riverauction.api.model.CReviewItem;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.api.service.APISuccessResponse;
import com.riverauction.riverauction.base.BaseActivity;
import com.riverauction.riverauction.eventbus.RiverAuctionEventBus;
import com.riverauction.riverauction.feature.common.ReviewInfoView;
import com.riverauction.riverauction.states.UserStates;
import com.riverauction.riverauction.widget.recyclerview.DividerUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class ReviewList extends BaseActivity implements ReviewListMvpView {

    private static final String EXTRA_PREFIX = "com.riverauction.riverauction.feature.teacher.TeacherDetailActivity.";
    public static final String EXTRA_USER_ID = EXTRA_PREFIX + "extra_user_id";

    private static final String EXTRA_PREFIX2 = "com.riverauction.riverauction.feature.review.ReviewList.";
    public static final String EXTRA_USER_ID2 = EXTRA_PREFIX2 + "extra_user_id";
    public static final String EXTRA_REVIEW_IDX = EXTRA_PREFIX2 + "extra_review_idx";

    @Inject
    ReviewListPresenter presenter;
    @Bind(R.id.basic_info_view) ReviewInfoView basicInfoView;
    @Bind(R.id.shop_recycler_view) RecyclerView recyclerView;
    // 로그인 한 유저
    private CUser user;
    private Integer teacherId;

    private ReviewItemAdapter adapter;
    private List<CReviewItem> reviewItems = Lists.newArrayList();
    @Override
    public int getLayoutResId() {
        return R.layout.activity_reviewlist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //basic profile
        getDataFromBundle(getIntent().getExtras());
        //뷰 리스트
        getActivityComponent().inject(this);
        presenter.attachView(this, this);
        presenter.getUserProfile(teacherId, true);
        user = UserStates.USER.get(stateCtx);
        getSupportActionBar().setTitle(R.string.review_list_title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reviewItems.add(makeReviewItem("2016.12.01", 5, "gogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogo", "bigchoi"));
        reviewItems.add(makeReviewItem("2016.12.01", 5, "gogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogo", "bigchoi"));
        reviewItems.add(makeReviewItem("2016.12.01", 5, "gogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogo", "bigchoi"));
        reviewItems.add(makeReviewItem("2016.12.01", 5, "gogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogo", "bigchoi"));
        reviewItems.add(makeReviewItem("2016.12.01", 5, "gogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogo", "bigchoi"));
        reviewItems.add(makeReviewItem("2016.12.01", 5, "gogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogo", "bigchoi"));
        reviewItems.add(makeReviewItem("2016.12.01", 5, "gogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogo", "bigchoi"));
        reviewItems.add(makeReviewItem("2016.12.01", 5, "gogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogogo", "bigchoi"));

        adapter = new ReviewItemAdapter(reviewItems);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView.ItemDecoration itemDecoration = DividerUtils.getHorizontalDividerItemDecoration(context);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //presenter.getUser(user.getId());
    }

    private void setAdaptor(APISuccessResponse<List<CReview>> response){
        List<CReview> newReview = response.getResult();

    }
    private CReviewItem makeReviewItem(String createAt, Integer rank, String review, String userName) {
        CReviewItem shopItem = new CReviewItem();
        shopItem.setCreateAt(createAt);
        shopItem.setRank(rank);
        shopItem.setReview(review);
        shopItem.setUserName(userName);
        return shopItem;
    }

    // teacherId 를 이전 화면에서 넘겨받는다
    private void getDataFromBundle(Bundle bundle) {
        if (bundle != null) {
            //실제로 티처아이디다
            teacherId = bundle.getInt(EXTRA_USER_ID, -1);
            if (teacherId == -1) {
                throw new IllegalStateException("teacherId must be exist");
            }
        }
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


    public void modifyReviewItem(int reviewIdx) {
        Intent intent = new Intent(context, ReviewWriteActivity.class);
        intent.putExtra(ReviewList.EXTRA_USER_ID2, teacherId);
        //리뷰 idx 수정 필요
        intent.putExtra(ReviewList.EXTRA_REVIEW_IDX, reviewIdx);
        startActivity(intent);
    }
    @Override
    public void successGetReviews(APISuccessResponse<List<CReview>> response, Integer newNextToken) {
        //adapter.setMessageViewResult((List<CReview>) response, newNextToken);
        setAdaptor(response);
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
     * ViewHolder
     */
    public static class ReviewItemHolder extends RecyclerView.ViewHolder {
        public TextView writerView;
        public TextView createView;
        public TextView review;
        public ImageView starRank;
        public ImageView imgModify;
        public RelativeLayout modify_layout;
        public ReviewItemHolder(View itemView) {
            super(itemView);
            writerView = (TextView) itemView.findViewById(R.id.review_writer);
            createView = (TextView) itemView.findViewById(R.id.review_createat);
            review = (TextView) itemView.findViewById(R.id.review_contents);
            starRank =(ImageView)itemView.findViewById(R.id.star_rank);
            imgModify = (ImageView)itemView.findViewById(R.id.img_modify);
        }
    }

    /**
     * Adapter
     */
    private class ReviewItemAdapter extends RecyclerView.Adapter<ReviewList.ReviewItemHolder> {
        private List<CReviewItem> reviewItems;

        public ReviewItemAdapter(List<CReviewItem> reviewItems) {
            this.reviewItems = reviewItems;
        }

        @Override
        public ReviewList.ReviewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ReviewList.ReviewItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_review, parent, false));
        }

        @Override
        public void onBindViewHolder(ReviewList.ReviewItemHolder holder, int position) {
            CReviewItem reviewItem = reviewItems.get(position);

            holder.writerView.setText(reviewItem.getReview());

            //자신의 댓글인 경우에만 노출
            if(reviewItem.getUserId() !=null && reviewItem.getUserId() == user.getId())
            {
                holder.imgModify.setVisibility(View.VISIBLE);
            }else
            {
                //일단 주석 처리
                //holder.imgModify.setVisibility(View.INVISIBLE);
            }

            holder.imgModify.setOnClickListener(v -> {
                // Purchase and Consume
                new AlertDialog.Builder(context)
                        .setTitle(R.string.review_list_modify)
                        .setMessage(R.string.review_modify_dialog_message)
                        .setPositiveButton(R.string.common_button_ok, (dialog, which) -> {
                            modifyReviewItem(reviewItem.getReviewIdx());
                        })
                        .setCancelable(true)
                        .show();
            });

            holder.createView.setText(reviewItem.getCreateAt());
            holder.review.setText(reviewItem.getReview());
            switch (reviewItem.getRank())
            {
                case 1:
                    holder.starRank.setImageResource(R.drawable.star1);
                case 2:
                    holder.starRank.setImageResource(R.drawable.star2);
                case 3:
                    holder.starRank.setImageResource(R.drawable.star3);
                case 4:
                    holder.starRank.setImageResource(R.drawable.star4);
                case 5:
                    holder.starRank.setImageResource(R.drawable.star5);
                case 6:
                    holder.starRank.setImageResource(R.drawable.star6);
                case 7:
                    holder.starRank.setImageResource(R.drawable.star7);
                case 8:
                    holder.starRank.setImageResource(R.drawable.star8);
                case 9:
                    holder.starRank.setImageResource(R.drawable.star9);
                case 10:
                    holder.starRank.setImageResource(R.drawable.star10);
            }
        }

        @Override
        public int getItemCount() {
            return reviewItems.size();
        }
    }
}
