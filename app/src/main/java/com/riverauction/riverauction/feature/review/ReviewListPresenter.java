package com.riverauction.riverauction.feature.review;

import android.content.Context;

import com.riverauction.riverauction.api.model.CReview;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.api.service.APISuccessResponse;
import com.riverauction.riverauction.api.service.auth.request.TeacherReviewRequest;
import com.riverauction.riverauction.base.BasePresenter;
import com.riverauction.riverauction.data.DataManager;
import com.riverauction.riverauction.rxjava.APISubscriber;
import com.riverauction.riverauction.rxjava.DialogSubscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ReviewListPresenter extends BasePresenter<ReviewListMvpView> {

    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    ReviewListPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(ReviewListMvpView mvpView, Context context) {
        super.attachView(mvpView, context);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public void getReviews(Integer userId, Integer orderby, Integer newNextToken) {
        checkViewAttached();
        if (newNextToken == null) {
            return;
        }

        subscription = dataManager.getReviews(userId, orderby, newNextToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new APISubscriber<APISuccessResponse<List<CReview>>>() {

                    @Override
                    public void onNext(APISuccessResponse<List<CReview>> response) {
                        super.onNext(response);
                        getMvpView().successGetReviews(response, response.getNextToken());
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failGetReviews(getErrorCause(e));
                    }
                });
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


    public void deleteReview(Integer userId, TeacherReviewRequest reviewId) {
        checkViewAttached();
        if (userId == null) {
            return;
        }

        subscription = dataManager.deleteReview(userId, reviewId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DialogSubscriber<>(new APISubscriber<Boolean>() {

                    @Override
                    public void onNext(Boolean result) {
                        super.onNext(result);
                        getMvpView().successDeleteReview(result);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failDeleteReview(getErrorCause(e));
                    }
                }, context));

    }
}
