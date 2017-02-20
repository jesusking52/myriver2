package com.riverauction.riverauction.feature.photo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jhcompany.android.libs.preference.StateCtx;
import com.jhcompany.android.libs.utils.Lists2;
import com.riverauction.riverauction.R;
import com.riverauction.riverauction.RiverAuctionApplication;
import com.riverauction.riverauction.api.model.CImage;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.eventbus.RiverAuctionEventBus;
import com.riverauction.riverauction.eventbus.UploadProfilePhotoEvent;
import com.riverauction.riverauction.glide.GlideImage;
import com.riverauction.riverauction.states.UserStates;

import java.util.List;

public class BoardImageView extends ImageView {
    private CUser user;

    public BoardImageView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public BoardImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public BoardImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        setClickable(true);
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                RiverAuctionEventBus.getEventBus().register(BoardImageView.this);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                RiverAuctionEventBus.getEventBus().unregister(BoardImageView.this);
            }
        });
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final UploadProfilePhotoEvent event) {
        StateCtx stateCtx = RiverAuctionApplication.getApplication().getComponent().stateCtx();
        CUser user = UserStates.USER.get(stateCtx);
        if (null == user || null == this.user || null == user.getId() || !user.getId().equals(this.user.getId())) {
            return;
        }
        loadProfileImage(user);
    }

    public void loadProfileImage(final CUser user) {
        this.user = user;

        if (user == null || Lists2.isNullOrEmpty(user.getProfilePhotos())) {
            Glide.with(getContext()).load(R.drawable.im_empty_profile)
                    .asBitmap()
                    .into(this);
            return;
        }

        // Glide 가 placeholder 를 transform 해주지 않는다.
        // https://github.com/bumptech/glide/issues/317
        Glide.with(getContext())
                .load(GlideImage.from(user.getProfilePhotos()))
                .asBitmap()
                .into(this);
    }
    public void loadProfileImage(final List<CImage> cImageList) {
        // Glide 가 placeholder 를 transform 해주지 않는다.
        // https://github.com/bumptech/glide/issues/317
        Glide.with(getContext())
                .load(GlideImage.from( cImageList))
                .asBitmap()
                .into(this);
    }
}
