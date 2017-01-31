package com.riverauction.riverauction.feature.consult;

import android.content.Context;

import com.riverauction.riverauction.api.model.CBoard;
import com.riverauction.riverauction.api.service.APISuccessResponse;
import com.riverauction.riverauction.api.service.board.params.GetBoardsParams;
import com.riverauction.riverauction.base.BasePresenter;
import com.riverauction.riverauction.data.DataManager;
import com.riverauction.riverauction.rxjava.APISubscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class BoardPresenter extends BasePresenter<BoardMvpView> {

    private final DataManager dataManager;
    private Subscription subscription;
    @Inject
    BoardPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(BoardMvpView mvpView, Context context) {
        super.attachView(mvpView, context);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }


    public void getBoardList(Integer categoryId, GetBoardsParams params) {
        /*
        if (params == null) {
            return;
        }
        */
        checkViewAttached();
        subscription = dataManager.getBoards(categoryId, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new APISubscriber<APISuccessResponse<List<CBoard>>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        getMvpView().loadingGetBoardList(categoryId);
                    }

                    @Override
                    public void onNext(APISuccessResponse<List<CBoard>> result) {
                        super.onNext(result);
                        getMvpView().successBoardList(categoryId,result.getResult(), result.getNextToken());
                    }

                    @Override
                    public boolean onErrors(Throwable e) {

                        return getMvpView().failGetBoardList(categoryId,getErrorCause(e));
                    }
                });
    }


}
