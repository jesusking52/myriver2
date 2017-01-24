package com.riverauction.riverauction.feature.consult;

import android.content.Context;

import com.riverauction.riverauction.api.model.CBoard;
import com.riverauction.riverauction.api.service.APISuccessResponse;
import com.riverauction.riverauction.api.service.auth.request.BoardWriteRequest;
import com.riverauction.riverauction.base.BasePresenter;
import com.riverauction.riverauction.data.DataManager;
import com.riverauction.riverauction.rxjava.APISubscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class BoardDetailPresenter extends BasePresenter<BoardDetailMvpView> {

    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    BoardDetailPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(BoardDetailMvpView mvpView, Context context) {
        super.attachView(mvpView, context);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public void getBoardDetail(Integer boardId) {
        checkViewAttached();
        if (boardId == null) {
            return;
        }

/*
        subscription = dataManager.getLesson(lessonId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new APISubscriber<CBoard>() {

                    @Override
                    public void onNext(CBoard board) {
                        super.onNext(board);
                        getMvpView().successGetBoard(board);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failGetBoard(getErrorCause(e));
                    }
                });
                */
    }

    public void getBoardReply(Integer boardId,Integer userId) {
        if (userId == null) {
            return;
        }
        checkViewAttached();
        subscription = dataManager.getBoardReply(boardId, userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new APISubscriber<APISuccessResponse<List<CBoard>>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        getMvpView().loadingGetReplyList(boardId);
                    }

                    @Override
                    public void onNext(APISuccessResponse<List<CBoard>> response) {
                        super.onNext(response);
                        getMvpView().successGetReplyList(boardId, response);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failGetReplyList(boardId, getErrorCause(e));
                    }
                });
    }

    public void postBoardRegist(Integer boardId, BoardWriteRequest request) {
        checkViewAttached();
        if (boardId == null) {
            return;
        }

/*
        subscription = dataManager.postLessonFavorites(lessonId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DialogSubscriber<>(new APISubscriber<CBoard>() {

                    @Override
                    public void onNext(CBoard boardRegist) {
                        super.onNext(boardRegist);
                        getMvpView().successRegistReply(boardRegist);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failRegistReply(getErrorCause(e));
                    }
                }, context));
  */
    }

    public void postBoardModify(Integer boardId, BoardWriteRequest request) {
        checkViewAttached();
        if (boardId == null) {
            return;
        }

/*
        subscription = dataManager.deleteLessonFavorites(lessonId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DialogSubscriber<>(new APISubscriber<CBoard>() {

                    @Override
                    public void onNext(CBoard boardRegist) {
                        super.onNext(boardRegist);
                        getMvpView().successModifyReply(boardRegist);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failModifyReply(getErrorCause(e));
                    }
                }, context));
  */
    }

    public void deleteBoard(Integer boardId, Integer replyId) {
        checkViewAttached();

/*
        subscription = dataManager.cancelLesson(lessonId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DialogSubscriber<>(new APISubscriber<Void>() {

                    @Override
                    public void onNext(Void result) {
                        super.onNext(result);
                        getMvpView().successDeleteReply();
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failDeleteReply(getErrorCause(e));
                    }

                });
  */
    }


}
