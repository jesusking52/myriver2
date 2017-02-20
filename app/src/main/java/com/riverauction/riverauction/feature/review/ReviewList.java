package com.riverauction.riverauction.feature.review;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.jhcompany.android.libs.utils.ParcelableWrappers;
import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CReview;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.api.service.APISuccessResponse;
import com.riverauction.riverauction.api.service.auth.request.TeacherReviewRequest;
import com.riverauction.riverauction.base.BaseActivity;
import com.riverauction.riverauction.base.BaseApplication;
import com.riverauction.riverauction.eventbus.RiverAuctionEventBus;
import com.riverauction.riverauction.feature.common.ReviewInfoView;
import com.riverauction.riverauction.feature.teacher.TeacherDetailActivity;
import com.riverauction.riverauction.feature.utils.DataUtils;
import com.riverauction.riverauction.states.UserStates;
import com.riverauction.riverauction.widget.recyclerview.DividerUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class ReviewList extends BaseActivity implements ReviewListMvpView {

    private static final String EXTRA_PREFIX = "com.riverauction.riverauction.feature.teacher.TeacherDetailActivity.";
    public static final String EXTRA_USER_ID = EXTRA_PREFIX + "extra_user_id";

    private static final String EXTRA_PREFIX2 = "com.riverauction.riverauction.feature.review.ReviewList.";
    public static final String EXTRA_REVIEW_IDX = EXTRA_PREFIX2 + "extra_review_idx";

    @Inject
    ReviewListPresenter presenter;
    @Bind(R.id.basic_info_view) ReviewInfoView basicInfoView;
    @Bind(R.id.shop_recycler_view) RecyclerView recyclerView;
    @Bind(R.id.review_list_count) TextView reviewCount;
    @Bind(R.id.recent_Btn) ImageView recentBtn;
    @Bind(R.id.rate_Btn) ImageView rateBtn;
    @Bind(R.id.riview_button) TextView riviewbutton;
    // 로그인 한 유저
    private CUser user;
    private Integer teacherId;

    private ReviewItemAdapter adapter;
    private List<CReview> reviewItems = Lists.newArrayList();
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
        presenter.getReviews(teacherId, 0, 0);
        recentBtn.setOnClickListener(v -> {
            // 최신순
            adapter.reviewItems.clear();
            presenter.getReviews(teacherId, 0, 0);
            recentBtn.setImageResource(R.drawable.recent_focus);
            rateBtn.setImageResource(R.drawable.rate);
            Toast.makeText(this, "최신순으로 정렬하였습니다.", Toast.LENGTH_SHORT).show();
        });
        rateBtn.setOnClickListener(v -> {
            // 평점순 정렬
            adapter.reviewItems.clear();
            presenter.getReviews(teacherId, 1, 0);
            recentBtn.setImageResource(R.drawable.recent);
            rateBtn.setImageResource(R.drawable.rate_focus);
            Toast.makeText(this, "평점순으로 정렬하였습니다.", Toast.LENGTH_SHORT).show();
        });

        riviewbutton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReviewWriteActivity.class);
            intent.putExtra(TeacherDetailActivity.EXTRA_USER_ID, teacherId);

            startActivity(intent);
            //Toast.makeText(this, "test2", Toast.LENGTH_SHORT).show();
        });
    }

    private void setAdaptor(APISuccessResponse<List<CReview>> response){
        List<CReview> newReview = response.getResult();
        for(int i=0;i<newReview.size();i++)
        {
            CReview review = newReview.get(i);
            reviewItems.add(makeReviewItem(review.getReviewIdx(),review.getCreatedAt(), review.getRank(), review.getReview(), review.getName(), review.getUserid()));
        }

        adapter = new ReviewItemAdapter(reviewItems);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView.ItemDecoration itemDecoration = DividerUtils.getHorizontalDividerItemDecoration(context);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private CReview makeReviewItem(Integer reviewIdx, long createAt, Integer rank, String review, String userName, String userId) {
        CReview shopItem = new CReview();
        shopItem.setCreatedAt(createAt);//
        shopItem.setRank(rank);
        shopItem.setReviewIdx(reviewIdx);
        shopItem.setReview(review);
        shopItem.setName(userName);
        shopItem.setUserid(userId);
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


    public void modifyReviewItem(CReview reviewItem) {
        Intent intent = new Intent(context, ReviewWriteActivity.class);
        intent.putExtra(TeacherDetailActivity.EXTRA_USER_ID, teacherId);
        intent.putExtra(ReviewWriteActivity.EXTRA_REVIEW, ParcelableWrappers.wrap(reviewItem));
        startActivity(intent);
    }
    @Override
    public void successGetReviews(APISuccessResponse<List<CReview>> response, Integer newNextToken) {
        List<CReview> newReview = response.getResult();
        reviewCount.setText("총 "+newReview.size()+"개의 리뷰");
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

    @Override
    public void successDeleteReview(Boolean result) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean failDeleteReview(CErrorCause errorCause) {
        setResult(RESULT_OK);
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseApplication.getActivityManager().resumeActivity(this);
        if(adapter != null) {
            adapter.reviewItems.clear();
            presenter.getReviews(teacherId, 0, 0);
        }
        LOGGER.debug("onResume()");
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
        public ImageView imgDelete;
        public RelativeLayout modify_layout;
        public ReviewItemHolder(View itemView) {
            super(itemView);
            writerView = (TextView) itemView.findViewById(R.id.review_writer);
            createView = (TextView) itemView.findViewById(R.id.review_createat);
            review = (TextView) itemView.findViewById(R.id.review_contents);
            starRank =(ImageView)itemView.findViewById(R.id.star_rank);
            imgModify = (ImageView)itemView.findViewById(R.id.img_modify);
            imgDelete = (ImageView)itemView.findViewById(R.id.img_delete);
            modify_layout= (RelativeLayout)itemView.findViewById(R.id.modify_layout);
        }
    }

    /**
     * Adapter
     */
    private class ReviewItemAdapter extends RecyclerView.Adapter<ReviewList.ReviewItemHolder> {
        private List<CReview> reviewItems;

        public ReviewItemAdapter(List<CReview> reviewItems) {
            this.reviewItems = reviewItems;
        }

        @Override
        public ReviewList.ReviewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ReviewList.ReviewItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_review, parent, false));
        }

        private TeacherReviewRequest buildReviewRequest(String reviewidx) {
            return new TeacherReviewRequest.Builder()
                    .setReviewidx(reviewidx)
                    .setTeacherid(teacherId.toString())
                    .build();
        }

        @Override
        public void onBindViewHolder(ReviewList.ReviewItemHolder holder, int position) {
            CReview reviewItem = reviewItems.get(position);
            holder.writerView.setText(DataUtils.convertToAnonymousName(reviewItem.getName())+"님의 리뷰");

            //자신의 댓글인 경우에만 노출
            if(reviewItem.getUserid() !=null && Integer.parseInt(reviewItem.getUserid()) == user.getId())
            {
                holder.modify_layout.setVisibility(View.VISIBLE);
            }else
            {
                //일단 주석 처리
                holder.modify_layout.setVisibility(View.INVISIBLE);
            }

            holder.imgDelete.setOnClickListener(v -> {
                // Purchase and Consume
                new AlertDialog.Builder(context)
                        .setTitle(R.string.review_delete)
                        .setMessage(R.string.board_delete2)
                        .setPositiveButton(R.string.common_button_ok, (dialog, which) -> {

                            presenter.deleteReview(user.getId(),buildReviewRequest(reviewItem.getReviewIdx().toString()));
                        })
                        .setNegativeButton(R.string.common_button_cancel, null)
                        .setCancelable(true)
                        .show();
            });

            holder.imgModify.setOnClickListener(v -> {
                // Purchase and Consume
                new AlertDialog.Builder(context)
                        .setTitle(R.string.review_list_modify)
                        .setMessage(R.string.review_modify_dialog_message)
                        .setPositiveButton(R.string.common_button_ok, (dialog, which) -> {
                            modifyReviewItem(reviewItem);
                        })
                        .setCancelable(true)
                        .show();
            });

            holder.createView.setText(DateUtils.getRelativeTimeSpanString(reviewItem.getCreatedAt()));
            holder.review.setText(reviewItem.getReview());

            if(reviewItem.getRank().equals(0)) {
                holder.starRank.setImageResource(R.drawable.star1);
            }else if(reviewItem.getRank().equals(1)) {
                holder.starRank.setImageResource(R.drawable.star1);
            }else if(reviewItem.getRank().equals(2)) {
                holder.starRank.setImageResource(R.drawable.star2);
            }else if(reviewItem.getRank().equals(3)) {
                holder.starRank.setImageResource(R.drawable.star3);
            }else if(reviewItem.getRank().equals(4)) {
                holder.starRank.setImageResource(R.drawable.star4);
            }else if(reviewItem.getRank().equals(5)) {
                holder.starRank.setImageResource(R.drawable.star5);
            }else if(reviewItem.getRank().equals(6)) {
                holder.starRank.setImageResource(R.drawable.star6);
            }else if(reviewItem.getRank().equals(7)) {
                holder.starRank.setImageResource(R.drawable.star7);
            }else if(reviewItem.getRank().equals(8)) {
                holder.starRank.setImageResource(R.drawable.star8);
            }else if(reviewItem.getRank().equals(9)) {
                holder.starRank.setImageResource(R.drawable.star9);
            }else if(reviewItem.getRank().equals(10)) {
                holder.starRank.setImageResource(R.drawable.star10);
            }
        }

        @Override
        public int getItemCount() {
            return reviewItems.size();
        }
    }
}
