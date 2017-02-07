package com.riverauction.riverauction.feature.main;

import android.content.Context;

import com.riverauction.riverauction.api.model.CMyTeacher;
import com.riverauction.riverauction.api.model.CSubjectGroup;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.api.service.APISuccessResponse;
import com.riverauction.riverauction.base.BasePresenter;
import com.riverauction.riverauction.data.DataManager;
import com.riverauction.riverauction.rxjava.APISubscriber;
import com.riverauction.riverauction.rxjava.DialogSubscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    MainPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(MainMvpView mvpView, Context context) {
        super.attachView(mvpView, context);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
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

    public void getSubjectGroups() {
        checkViewAttached();

        subscription = dataManager.getSubjectGroups()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new APISubscriber<List<CSubjectGroup>>() {

                    @Override
                    public void onNext(List<CSubjectGroup> subjectGroups) {
                        super.onNext(subjectGroups);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        // 무시한다
                        return true;
                    }
                });
    }


    public void getMyTeacher(Integer userId, Integer type) {
        checkViewAttached();
        if (userId == null) {
            return;
        }

        subscription = dataManager.getMyTeacher(userId, type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new APISubscriber<APISuccessResponse<List<CMyTeacher>>>() {

                    @Override
                    public void onNext(APISuccessResponse<List<CMyTeacher>> response) {
                        super.onNext(response);
                        getMvpView().successGetMyTeacher(response);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failGetMyTeacher(getErrorCause(e));
                    }
                });
    }

    public void confirmMyTeacher(Integer userId, Integer teacherId) {
        checkViewAttached();
        if (userId == null) {
            return;
        }

        subscription = dataManager.confirmMyTeacher(userId, teacherId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new APISubscriber<Boolean>() {

                    @Override
                    public void onNext(Boolean response) {
                        super.onNext(response);
                        getMvpView().successConfirmMyTeacher(response);
                    }

                    @Override
                    public boolean onErrors(Throwable e) {
                        return getMvpView().failConfirmMyTeacher(getErrorCause(e));
                    }
                });
    }
}
