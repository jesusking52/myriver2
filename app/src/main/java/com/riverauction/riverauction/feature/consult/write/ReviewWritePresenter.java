package com.riverauction.riverauction.feature.consult.write;

import android.content.Context;

import com.riverauction.riverauction.api.model.CReview;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.api.service.auth.request.BoardWriteRequest;
import com.riverauction.riverauction.base.BasePresenter;
import com.riverauction.riverauction.data.DataManager;
import com.riverauction.riverauction.rxjava.APISubscriber;
import com.riverauction.riverauction.rxjava.DialogSubscriber;

import java.io.File;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ReviewWritePresenter extends BasePresenter<BoardWriteMvpView> {
    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    ReviewWritePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(BoardWriteMvpView mvpView, Context context) {
        super.attachView(mvpView, context);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public void writeBoard(Integer userId, BoardWriteRequest request) {
        checkViewAttached();
        if (userId == null) {
            return;
        }

        subscription = dataManager.postBoardRegist(userId, request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DialogSubscriber<>(new APISubscriber<Boolean>() {

                    @Override
                    public void onNext(Boolean user) {
                        super.onNext(user);
                        getMvpView().successRegist(user);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failRegist(getErrorCause(e));
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
                        getMvpView().successModify(boardRegist);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failModify(getErrorCause(e));
                    }
                }, context));

    }

    public void getUserProfile(Integer userId, Boolean phoneNumber) {
        checkViewAttached();
        if (userId == null) {
            return;
        }

        subscription = dataManager.getUserProfile(userId, phoneNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DialogSubscriber<>(new APISubscriber<CUser>() {

                    @Override
                    public void onNext(CUser user) {
                        super.onNext(user);
                        getMvpView().successGetUser(user);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failGetUser(getErrorCause(e));
                    }
                }, context));
    }

    public void getUserReview(Integer reviewIdx) {
        checkViewAttached();


        subscription = dataManager.getReview(reviewIdx)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DialogSubscriber<>(new APISubscriber<CReview>() {

                    @Override
                    public void onNext(CReview review) {
                        super.onNext(review);
                        getMvpView().successGetReview(review );
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failGetUser(getErrorCause(e));
                    }
                }, context));
    }


    public void postProfilePhoto(Integer userId, File localFile) {
        checkViewAttached();

        subscription = dataManager.postProfilePhoto(userId, localFile)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DialogSubscriber<>(new APISubscriber<CUser>() {

                    @Override
                    public void onNext(CUser user) {
                        super.onNext(user);
                        getMvpView().successPostProfilePhoto(user);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failPostPreferences(getErrorCause(e));
                    }
                }, context));
    }

}
