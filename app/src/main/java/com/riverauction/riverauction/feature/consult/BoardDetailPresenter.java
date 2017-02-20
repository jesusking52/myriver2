package com.riverauction.riverauction.feature.consult;

import android.content.Context;

import com.riverauction.riverauction.api.model.CBoard;
import com.riverauction.riverauction.api.service.APISuccessResponse;
import com.riverauction.riverauction.api.service.auth.request.BoardWriteRequest;
import com.riverauction.riverauction.api.service.board.params.GetBoardsParams;
import com.riverauction.riverauction.base.BasePresenter;
import com.riverauction.riverauction.data.DataManager;
import com.riverauction.riverauction.rxjava.APISubscriber;
import com.riverauction.riverauction.rxjava.DialogSubscriber;

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

    public void getBoardDetail(Integer categoryId, GetBoardsParams params) {

        checkViewAttached();
        subscription = dataManager.getBoards(categoryId, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new APISubscriber<APISuccessResponse<List<CBoard>>>() {

                    @Override
                    public void onNext(APISuccessResponse<List<CBoard>> result) {
                        super.onNext(result);
                        getMvpView().successBoardList(categoryId, result.getResult(), result.getNextToken());
                    }

                    @Override
                    public boolean onErrors(Throwable e) {

                        return getMvpView().failGetBoardList(categoryId, getErrorCause(e));
                    }
                });
    }

    public void getBoardReply(Integer categoryId, GetBoardsParams params) {
        if (categoryId == null) {
            return;
        }
        checkViewAttached();
        subscription = dataManager.getBoards(categoryId, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new APISubscriber<APISuccessResponse<List<CBoard>>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        getMvpView().loadingGetReplyList(categoryId);
                    }

                    @Override
                    public void onNext(APISuccessResponse<List<CBoard>> result) {
                        super.onNext(result);
                        getMvpView().successGetReplyList(categoryId, result.getResult(), result.getNextToken());
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failGetReplyList(categoryId, getErrorCause(e));
                    }
                });
    }

    public void postBoardRegist(Integer userId, BoardWriteRequest request) {
        checkViewAttached();
        if (userId == null) {
            return;
        }

        subscription = dataManager.postBoardRegist(userId, request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DialogSubscriber<>(new APISubscriber<Boolean>() {

                    @Override
                    public void onNext(Boolean boardRegist) {
                        super.onNext(boardRegist);
                        getMvpView().successRegistReply(boardRegist);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failRegistReply(getErrorCause(e));
                    }
                }, context));

    }

    public void postBoardModify(Integer userId, BoardWriteRequest request) {
        checkViewAttached();
        if (userId == null) {
            return;
        }
        subscription = dataManager.postBoardModify(userId, request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DialogSubscriber<>(new APISubscriber<Boolean>() {

                    @Override
                    public void onNext(Boolean boardRegist) {
                        super.onNext(boardRegist);
                        getMvpView().successModifyReply(boardRegist);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failModifyReply(getErrorCause(e));
                    }
                }, context));

    }

    public void deleteBoard(Integer userId, BoardWriteRequest request) {
        checkViewAttached();

        if (userId == null) {
            return;
        }

        subscription = dataManager.deleteBoard(userId, request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DialogSubscriber<>(new APISubscriber<Boolean>() {

                    @Override
                    public void onNext(Boolean result) {
                        super.onNext(result);
                        getMvpView().successDelete(result);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failDeleteReply(getErrorCause(e));
                    }
                }, context));

    }


    public void deleteBoardReply(Integer userId, BoardWriteRequest request) {
        checkViewAttached();

        if (userId == null) {
            return;
        }

        subscription = dataManager.deleteBoard(userId, request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DialogSubscriber<>(new APISubscriber<Boolean>() {

                    @Override
                    public void onNext(Boolean result) {
                        super.onNext(result);
                        getMvpView().successDeleteReply(result);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failDeleteReply(getErrorCause(e));
                    }
                }, context));

    }
}
