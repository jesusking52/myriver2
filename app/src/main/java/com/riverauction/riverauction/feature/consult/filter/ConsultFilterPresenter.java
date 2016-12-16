package com.riverauction.riverauction.feature.consult.filter;

import android.content.Context;

import com.riverauction.riverauction.base.BasePresenter;
import com.riverauction.riverauction.data.DataManager;

import javax.inject.Inject;

import rx.Subscription;

public class ConsultFilterPresenter extends BasePresenter<ConsultFilterMvpView> {
    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    ConsultFilterPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(ConsultFilterMvpView mvpView, Context context) {
        super.attachView(mvpView, context);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
