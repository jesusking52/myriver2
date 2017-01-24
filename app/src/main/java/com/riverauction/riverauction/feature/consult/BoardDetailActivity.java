package com.riverauction.riverauction.feature.consult;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CBoard;
import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CLessonStatus;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.api.service.APISuccessResponse;
import com.riverauction.riverauction.api.service.auth.request.BoardWriteRequest;
import com.riverauction.riverauction.base.BaseActivity;
import com.riverauction.riverauction.eventbus.RiverAuctionEventBus;
import com.riverauction.riverauction.eventbus.SelectTeacherEvent;
import com.riverauction.riverauction.feature.photo.ProfileImageView;
import com.riverauction.riverauction.states.UserStates;
import com.riverauction.riverauction.widget.recyclerview.DividerUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class BoardDetailActivity extends BaseActivity implements BoardDetailMvpView {
    private static final String EXTRA_PREFIX = "com.riverauction.riverauction.feature.lesson.BoardDetailActivity.";
    public static final String EXTRA_LESSON_ID = EXTRA_PREFIX + "extra_lesson_id";
    public static final String EXTRA_OWNER_ID = EXTRA_PREFIX + "extra_owner_id";
    private static final int REQUEST_POST_BIDDING = 0x01;

    @Inject
    BoardDetailPresenter presenter;
    @Bind(R.id.boardContents) TextView boardContent;
    @Bind(R.id.item_summary) TextView itemsummary;
    @Bind(R.id.board_register_id) TextView registId;
    @Bind(R.id.register_time) TextView registTime;
    @Bind(R.id.view_count) TextView viewCnt;
    @Bind(R.id.modify) View modify;
    @Bind(R.id.delete) View delete;
    @Bind(R.id.reply) View reply;
    @Bind(R.id.noReply) View noReply;
    @Bind(R.id.category_label) ImageView categoryLabel;

    @Bind(R.id.boardModify) View boardModify;
    @Bind(R.id.reply_container) View replyContainer;
    @Bind(R.id.replyLayout) View replyLayout;
    @Bind(R.id.item_teacher_profile_image2) ProfileImageView profile;
    private Integer boardId;
    private Integer ownerId;
    // 로그인 된 유저
    private CUser me;
    private  CBoard board;
    private Integer CATEGORY;
    private Integer nextToken;
    //여기부터 추가
    List<CBoard> boardList;
    private MenuItem likeMenuItem;


    private BoardDetailActivity.BoardItemAdapter adapter;
    private List<CBoard> boardItems = Lists.newArrayList();
    @Bind(R.id.shop_recycler_view) RecyclerView recyclerView;
    static final String SKU_PURCHASED = "android.test.purchased";
    static final String SKU_CANCELED = "android.test.canceled";
    static final String SKU_REFUNDED = "android.test.refunded";
    private static final String SKU_ITEM_UNAVAILABLE = "android.test.item_unavailable";
    public static final String MODES = "MODE";
    public static final String IDX = "IDX";
    @Override
    public int getLayoutResId() {
        return R.layout.activity_board_detail;
    }

    private CBoard makeShopItem(String contents, long createAt, String title, String teacherId) {
        CBoard shopItem = new CBoard();
        shopItem.setContent(contents);
        shopItem.setCreatedAt(createAt);
        //shopItem.setTitle(title);
        shopItem.setTeacherid(teacherId);
        return shopItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RiverAuctionEventBus.getEventBus().register(this);
        getDataFromBundle(getIntent().getExtras());
        me = UserStates.USER.get(stateCtx);
        getActivityComponent().inject(this);
        presenter.attachView(this, this);

        getSupportActionBar().setTitle(R.string.board_write_title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter.getBoardDetail(boardId);
        presenter.getBoardReply(boardId,me.getId());
        //여기부터 추가
        boardItems.add(makeShopItem("내용무내용무내용무내용무내용무내용무내용무내용무내용무내용무내용무내용무", 3, "나의 답변1", "행"));
        boardItems.add(makeShopItem("내용무내용무내용무내용무내용무내용무내용무내용무내용무내용무내용무내용무", 3, "나의 답변1", "행"));
        boardItems.add(makeShopItem("내용무내용무내용무내용무내용무내용무내용무내용무내용무내용무내용무내용무", 3, "나의 답변1", "행"));
        boardItems.add(makeShopItem("내용무내용무내용무내용무내용무내용무내용무내용무내용무내용무내용무내용무", 3, "나의 답변1", "행"));
        boardItems.add(makeShopItem("내용무내용무내용무내용무내용무내용무내용무내용무내용무내용무내용무내용무", 3, "나의 답변1", "행"));

        adapter = new BoardItemAdapter(boardItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView.ItemDecoration itemDecoration = DividerUtils.getHorizontalDividerItemDecoration(context);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        noReply.setVisibility(View.GONE);
        //답글
        reply.setOnClickListener(v -> {
            profile.loadProfileImage(me);
            replyLayout.setVisibility(View.VISIBLE);
            noReply.setVisibility(View.GONE);
            replyContainer.setVisibility(View.GONE);
            setLikeMenuItem(true, board);
        });
        //수정하기
        modify.setOnClickListener(v -> {
            replyLayout.setVisibility(View.GONE);
            noReply.setVisibility(View.GONE);
            replyContainer.setVisibility(View.GONE);
            boardContent.setVisibility(View.GONE);
            boardModify.setVisibility(View.VISIBLE);
            setLikeMenuItem(false, board);
        });
        //삭제하기
        delete.setOnClickListener(v -> {
            // Purchase and Consume
            new AlertDialog.Builder(context)
                    .setTitle(R.string.review_delete)
                    .setMessage(R.string.board_delete)
                    .setPositiveButton(R.string.common_button_ok, (dialog, which) -> {
                        // 삭제 태움
                        presenter.deleteBoard(boardId,0);
                    })
                    .setCancelable(true)
                    .show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favorite, menu);
        likeMenuItem = menu.getItem(0);
        likeMenuItem.setActionView(R.layout.action_layout_board);
        likeMenuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLikeMenuItem(boolean isRegist, CBoard board) {
        if (likeMenuItem == null) {
            return;
        }
     
        likeMenuItem.setVisible(true);
        TextView favoriteTextView = (TextView) likeMenuItem.getActionView().findViewById(R.id.favorite_text_view);
        if (isRegist) {
            favoriteTextView.setText(R.string.board_regist);
            likeMenuItem.getActionView().setOnClickListener(item -> {
                // 등록
                postRegistDialog(board);
            });
        } else {
            favoriteTextView.setText(R.string.review_list_modify);
            likeMenuItem.getActionView().setOnClickListener(item -> {
                //수정
                postModifyDialog(board);
            });
        }
    
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data, Bundle bundle) {
        super.onActivityResult(requestCode, resultCode, data, bundle);
        if (REQUEST_POST_BIDDING == requestCode && RESULT_OK == resultCode) {
            presenter.getBoardDetail(boardId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        if (RiverAuctionEventBus.getEventBus().isRegistered(this)) {
            RiverAuctionEventBus.getEventBus().unregister(this);
        }
    }

    // boardId, ownerId 를 이전 화면에서 넘겨받는다
    private void getDataFromBundle(Bundle bundle) {
        if (bundle != null) {
            CATEGORY = bundle.getInt(BoardView.EXTRA_CATEGORY_ID, -1);
            boardId = bundle.getInt(EXTRA_LESSON_ID, -1);
            if (boardId == -1) {
                throw new IllegalStateException("boardId must be exist");
            }

            ownerId = bundle.getInt(EXTRA_OWNER_ID, -1);
            if (ownerId == -1) {
                throw new IllegalStateException("ownerId must be exist");
            }
        }
    }

    /**
     * 등록
     */
    private void postRegistDialog(CBoard board) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.board_regist)
                .setMessage(R.string.board_regist_message)
                .setPositiveButton(R.string.common_button_ok, (dialog, which) -> {
                    BoardWriteRequest request = new BoardWriteRequest.Builder().setBoardIdx(boardId).setCategoryId(CATEGORY).setTeacherid(me.getId().toString()).build();
                    presenter.postBoardRegist(board.getBoardIdx(), request);
                })
                .setNegativeButton(R.string.common_button_no, null)
                .show();
    }

    /**
     * 수정
     */
    private void postModifyDialog(CBoard board) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.review_list_modify)
                .setMessage(R.string.board_modify_content)
                .setPositiveButton(R.string.common_button_ok, (dialog, which) -> {
                    BoardWriteRequest request = new BoardWriteRequest.Builder().setBoardIdx(board.getBoardIdx()).setReplyIdx(board.getReplyIdx()).setTeacherid(me.getId().toString()).build();
                    presenter.postBoardModify(boardId, request);
                })
                .setNegativeButton(R.string.common_button_no, null)
                .show();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final SelectTeacherEvent event) {
        // 선생님이 선택됨
        //board.setStatus(CLessonStatus.FINISHED);
        setContent(board);
    }

    private void setContent(CBoard board) {
        if (board == null) {
            return;
        }
        this.board = board;
        if(board.getContent().length()>20)
            itemsummary.setText(board.getContent().substring(0,20)+"...");
        else
            itemsummary.setText(board.getContent());

        boardContent.setText(board.getContent());
        registId.setText(board.getBoardIdx());
        registTime.setText(DateUtils.getRelativeTimeSpanString(board.getCreatedAt()));
        viewCnt.setText(board.getViewCnt());
        switch (board.getCategory2Id()){
            case 21:
                categoryLabel.setImageResource(R.drawable.grade_university);
                break;
            case 22:
                categoryLabel.setImageResource(R.drawable.grade_high);
                break;
            case 23:
                categoryLabel.setImageResource(R.drawable.grade_middle);
                break;
            case 24:
                categoryLabel.setImageResource(R.drawable.grade_global);
                break;
            case 31:
                categoryLabel.setImageResource(R.drawable.literature);
                break;
            case 32:
                categoryLabel.setImageResource(R.drawable.nature);
                break;
            case 33:
                categoryLabel.setImageResource(R.drawable.other);
                break;
            case 41:
                categoryLabel.setImageResource(R.drawable.category_student);
                break;
            case 42:
                categoryLabel.setImageResource(R.drawable.category_parents);
                break;

        }
/*
        showOrHideButtons();

        setLessonStatus(lesson.getStatus());


        biddingCountView.setText(getString(R.string.common_person_count_unit, lesson.getBiddingsCount()));
        biddingCountContainer.setOnClickListener(v -> {
            if (me.equals(lesson.getOwner())) {
                Intent intent = new Intent(context, MyLessonDetailSelectListActivity.class);
                intent.putExtra(EXTRA_LESSON, ParcelableWrappers.wrap(lesson));
                startActivity(intent);
            }
        });

        basicInfoView.setContent(lesson);
        lessonInfoView.setContent(lesson);
        descriptionView.setText(lesson.getDescription());
*/
    }

    private void setReply(APISuccessResponse<List<CBoard>> response) {

        List<CBoard> boardNewList = response.getResult();
        Integer newNextToken = response.getNextToken();
        if (boardNewList.size() == 0 && boardNewList.size() == 0) {
            //statusView.showEmptyView();
            return;
        }
        //statusView.showResultView();

        boardList.addAll(boardNewList);
        nextToken = newNextToken;

        //adapter.setNextToken(nextToken);
        //adapter.setErrorLoadMore(false);
        adapter = new BoardItemAdapter(boardNewList);
        adapter.notifyDataSetChanged();
    }

    private void setLessonStatus(CLessonStatus lessonStatus) {
        /*
        biddingStatusView.setVisibility(View.GONE);
        dealingStatusView.setVisibility(View.GONE);
        canceledStatusView.setVisibility(View.GONE);
        finishedStatusView.setVisibility(View.GONE);
        switch (lessonStatus) {
            case BIDDING: {
                biddingStatusView.setVisibility(View.VISIBLE);
                remainTimeView.setText(DataUtils.convertRemainTimeToString(context, lesson.getExpiresIn()));
                remainTimeView.setTextColor(getResources().getColor(R.color.river_auction_greyish_brown_two));
                break;
            }
            case DEALING: {
                dealingStatusView.setVisibility(View.VISIBLE);
                remainTimeView.setText(DataUtils.convertRemainTimeToString(context, lesson.getExpiresIn()));
                remainTimeView.setTextColor(getResources().getColor(R.color.river_auction_greyish_brown_two));
                break;
            }
            case CANCELED: {
                canceledStatusView.setVisibility(View.VISIBLE);
                canceledCreatedAtView.setText(DateUtils.getRelativeTimeSpanString(lesson.getCreatedAt()));
                remainTimeView.setText("00:00");
                remainTimeView.setTextColor(getResources().getColor(R.color.river_auction_greyish));
                break;
            }
            case FINISHED: {
                finishedStatusView.setVisibility(View.VISIBLE);
                finishedCreatedAtView.setText(DateUtils.getRelativeTimeSpanString(lesson.getCreatedAt()));
                remainTimeView.setText("00:00");
                remainTimeView.setTextColor(getResources().getColor(R.color.river_auction_greyish));
                break;
            }
        }
        */
    }

    private void showOrHideButtons() {
        /*
        if (CUserType.STUDENT == me.getType()) {
            // 학생일 경우
            if (lesson.getOwner().getId().equals(me.getId())) {
                // 내가 경매 만든 사람이면 취소하기 보여줌 (경매중 or 매칭중)
                if (lesson.getStatus() == CLessonStatus.BIDDING || lesson.getStatus() == CLessonStatus.DEALING) {
                    biddingButton.setVisibility(View.GONE);
                    biddingCancelButton.setVisibility(View.VISIBLE);
                    biddingButtonDummyView.setVisibility(View.VISIBLE);
                } else {
                    biddingButton.setVisibility(View.GONE);
                    biddingCancelButton.setVisibility(View.GONE);
                    biddingButtonDummyView.setVisibility(View.GONE);
                }
            } else {
                biddingButton.setVisibility(View.GONE);
                biddingCancelButton.setVisibility(View.GONE);
                biddingButtonDummyView.setVisibility(View.GONE);
            }
        } else {
            // 선생님일 경우
            // 해당 경매를 입찰했으면 액션바의 입찰하기를 안보여준다
            if (lesson.getIsBid() != null && lesson.getIsBid()) {
                biddingButton.setVisibility(View.GONE);
                biddingCancelButton.setVisibility(View.GONE);
                biddingButtonDummyView.setVisibility(View.GONE);
            } else {
                biddingCancelButton.setVisibility(View.GONE);
                biddingButton.setVisibility(View.VISIBLE);
                biddingButtonDummyView.setVisibility(View.VISIBLE);
            }
        }
        biddingButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostBiddingActivity.class);
            intent.putExtra(PostBiddingActivity.EXTRA_LESSON_PREFERRED_PRICE, (int) lesson.getPreferredPrice());
            intent.putExtra(PostBiddingActivity.EXTRA_LESSON_ID, boardId);
            startActivityForResult(intent, REQUEST_POST_BIDDING);
        });
        biddingCancelButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.menu_lesson_cancel)
                    .setMessage(R.string.my_lesson_cancel_lesson_button)
                    .setPositiveButton(R.string.common_button_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.cancelLesson(boardId);
                        }
                    })
                    .show();
        });
        */
    }


    @Override
    public void successGetBoard(CBoard board) {
        setContent(board);
    }
    @Override
    public boolean failGetBoard(CErrorCause errorCause) {
        return false;
    }

    @Override
    public void loadingGetReplyList(Integer boardId) {

    }

    @Override
    public void successGetReplyList(Integer boardId, APISuccessResponse<List<CBoard>> response) {
        setReply(response);
    }

    @Override
    public boolean failGetReplyList(Integer boardId, CErrorCause errorCause) {
        return false;
    }

    @Override
    public void successModifyReply(CBoard boardRegist) {

    }

    @Override
    public boolean failModifyReply(CErrorCause errorCause) {
        return false;
    }

    @Override
    public void successDeleteReply(){}

    @Override
    public boolean failDeleteReply(CErrorCause errorCause) {
        return false;
    }

    @Override
    public void successRegistReply(CBoard boardRegist){}

    @Override
    public boolean failRegistReply(CErrorCause errorCause) {
        return false;
    }

    /**
     * ViewHolder
     */
    public static class ReplyItemHolder extends RecyclerView.ViewHolder {
        public TextView replytitle;
        public TextView replyContent;
        public TextView replyTime;
        public ProfileImageView profileImageView;
        public EditText editContent;
        public ImageView modify;
        public ImageView delete;
        public View modifylayout;
        public ReplyItemHolder(View itemView) {
            super(itemView);
            profileImageView = (ProfileImageView) itemView.findViewById(R.id.item_teacher_profile_image);
            replytitle = (TextView) itemView.findViewById(R.id.reply_title);
            replyContent = (TextView) itemView.findViewById(R.id.reply_content);
            editContent = (EditText) itemView.findViewById(R.id.reply_edit_content);
            replyTime = (TextView) itemView.findViewById(R.id.reply_time);
            modifylayout = (View) itemView.findViewById(R.id.modifylayout);
            modify = (ImageView) itemView.findViewById(R.id.modify);
            delete = (ImageView) itemView.findViewById(R.id.delete);
        }
    }

    /**
     * Adapter
     */
    private class BoardItemAdapter extends RecyclerView.Adapter<BoardDetailActivity.ReplyItemHolder> {
        private List<CBoard> boardItems;

        public BoardItemAdapter(List<CBoard> boardItems) {
            this.boardItems = boardItems;
        }

        @Override
        public ReplyItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ReplyItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_reply, parent, false));
        }

        public void modifyReply(Integer replyIdx) {
        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
            String payload = "";

            //mHelper.launchPurchaseFlow(this, skuId, RC_REQUEST, mPurchaseFinishedListener, payload);
        }

        @Override
        public void onBindViewHolder(ReplyItemHolder holder, int position) {
            CBoard boardItem = boardItems.get(position);
            holder.replyContent.setText(boardItem.getContent());
            holder.replytitle.setText("하드코딩");
            holder.replyTime.setText(DateUtils.getRelativeTimeSpanString(boardItem.getCreatedAt()));

            //if(me.getId().equals(boardItem.getUserid()) ){
                holder.modifylayout.setVisibility(View.VISIBLE);
                holder.modify.setOnClickListener(v -> {
                    replyLayout.setVisibility(View.GONE);
                    noReply.setVisibility(View.GONE);
                    replyContainer.setVisibility(View.GONE);
                    boardContent.setVisibility(View.GONE);
                    boardModify.setVisibility(View.VISIBLE);
                    setLikeMenuItem(false, boardItem);
                });

                holder.delete.setOnClickListener(v -> {
                    // Purchase and Consume
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.review_delete)
                            .setMessage(R.string.board_delete2)
                            .setPositiveButton(R.string.common_button_ok, (dialog, which) -> {
                                presenter.deleteBoard(boardItem.getBoardIdx(), boardItem.getReplyIdx());
                            })
                            .setCancelable(true)
                            .show();
                });
                /*
            }else
            {
                holder.modifylayout.setVisibility(View.GONE);
            }
            */
            holder.profileImageView.loadProfileImage(me);
        }

        @Override
        public int getItemCount() {
            return boardItems.size();
        }
    }
}
