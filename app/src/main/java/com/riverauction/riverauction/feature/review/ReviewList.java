package com.riverauction.riverauction.feature.review;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.riverauction.riverauction.R;
import com.riverauction.riverauction.RiverAuctionConstant;
import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CReview;
import com.riverauction.riverauction.api.model.CShopItem;
import com.riverauction.riverauction.api.model.CTeacher;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.api.service.APISuccessResponse;
import com.riverauction.riverauction.base.BaseActivity;
import com.riverauction.riverauction.eventbus.RiverAuctionEventBus;
import com.riverauction.riverauction.feature.common.ReviewInfoView;
import com.riverauction.riverauction.feature.profile.shop.ShopActivity;
import com.riverauction.riverauction.states.UserStates;
import com.riverauction.riverauction.widget.recyclerview.DividerUtils;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class ReviewList extends BaseActivity implements ReviewListMvpView {

    private static final String EXTRA_PREFIX = "com.riverauction.riverauction.feature.teacher.TeacherDetailActivity.";
    public static final String EXTRA_USER_ID = EXTRA_PREFIX + "extra_user_id";
    @Inject
    ReviewListPresenter presenter;
    @Bind(R.id.basic_info_view) ReviewInfoView basicInfoView;
    @Bind(R.id.shop_recycler_view) RecyclerView recyclerView;
    // 로그인 한 유저
    private CUser user;
    private CTeacher teacher;
    private Integer userId;

    private ShopItemAdapter adapter;
    private List<CShopItem> shopItems = Lists.newArrayList();
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
        presenter.getUserProfile(userId, true);
        user = UserStates.USER.get(stateCtx);
        getSupportActionBar().setTitle(R.string.review_list_title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shopItems.add(makeShopItem(RiverAuctionConstant.SKU_ID_COST3000, 3, null, 3000));
        shopItems.add(makeShopItem(RiverAuctionConstant.SKU_ID_COST5000, 5, null, 5000));
        shopItems.add(makeShopItem(RiverAuctionConstant.SKU_ID_COST10000, 11, "10% 보너스", 10000));
        shopItems.add(makeShopItem(RiverAuctionConstant.SKU_ID_COST20000, 23, "15% 보너스", 20000));
        shopItems.add(makeShopItem(RiverAuctionConstant.SKU_ID_COST50000, 60, "20% 보너스", 50000));

        adapter = new ShopItemAdapter(shopItems);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView.ItemDecoration itemDecoration = DividerUtils.getHorizontalDividerItemDecoration(context);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //presenter.getUser(user.getId());
    }

    private CShopItem makeShopItem(String skuId, Integer count, String bonus, Integer price) {
        CShopItem shopItem = new CShopItem();
        shopItem.setSkuId(skuId);
        shopItem.setCount(count);
        shopItem.setBonusDescription(bonus);
        shopItem.setPrice(price);
        return shopItem;
    }

    // userId 를 이전 화면에서 넘겨받는다
    private void getDataFromBundle(Bundle bundle) {
        if (bundle != null) {
            //실제로 티처아이디다
            userId = bundle.getInt(EXTRA_USER_ID, -1);
            if (userId == -1) {
                throw new IllegalStateException("userId must be exist");
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

    @Override
    public void successGetReviews(APISuccessResponse<List<CReview>> response, Integer newNextToken) {
        //adapter.setMessageViewResult((List<CReview>) response, newNextToken);
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


    /**
     * ViewHolder
     */
    public static class ShopItemHolder extends RecyclerView.ViewHolder {
        public TextView countView;
        public TextView bonusDescriptionView;
        public TextView priceButton;

        public ShopItemHolder(View itemView) {
            super(itemView);

            countView = (TextView) itemView.findViewById(R.id.item_shop_count);
            bonusDescriptionView = (TextView) itemView.findViewById(R.id.item_shop_bonus_description);
            priceButton = (TextView) itemView.findViewById(R.id.item_shop_price_button);
        }
    }

    /**
     * Adapter
     */
    private class ShopItemAdapter extends RecyclerView.Adapter<ShopActivity.ShopItemHolder> {
        private List<CShopItem> shopItems;

        public ShopItemAdapter(List<CShopItem> shopItems) {
            this.shopItems = shopItems;
        }

        @Override
        public ShopActivity.ShopItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ShopActivity.ShopItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_shop, parent, false));
        }

        @Override
        public void onBindViewHolder(ShopActivity.ShopItemHolder holder, int position) {
            CShopItem shopItem = shopItems.get(position);
            /*
            if (shopItem.getSkuId().equals(SKU_PURCHASED)) {
                holder.countView.setText("결제 성공하는 테스트");
            } else if (shopItem.getSkuId().equals(SKU_CANCELED)) {
                holder.countView.setText("결제 실패하는 테스트");
            } else if (shopItem.getSkuId().equals(SKU_REFUNDED)) {
                holder.countView.setText("결제 환불된거 테스트");
            } else if (shopItem.getSkuId().equals(SKU_ITEM_UNAVAILABLE)) {
                holder.countView.setText("결제 불가능아이템 테스트");
            } else {
                holder.countView.setText(getString(R.string.common_shop_item_count_unit, shopItem.getCount()));
            }
            */
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String formattedString = formatter.format(shopItem.getPrice());
            holder.priceButton.setText(getString(R.string.common_shop_item_price_unit, formattedString));
            holder.priceButton.setOnClickListener(v -> {
                // Purchase and Consume
                new AlertDialog.Builder(context)
                        .setTitle(R.string.shop_purchase_dialog_title)
                        .setMessage(R.string.shop_purchase_dialog_message)
                        .setPositiveButton(R.string.common_button_ok, (dialog, which) -> {
                           // purchaseItem(shopItem.getSkuId());
                        })
                        .setCancelable(true)
                        .show();
            });

            if (!Strings.isNullOrEmpty(shopItem.getBonusDescription())) {
                holder.bonusDescriptionView.setText(shopItem.getBonusDescription());
                holder.bonusDescriptionView.setVisibility(View.VISIBLE);
            } else {
                holder.bonusDescriptionView.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return shopItems.size();
        }
    }
}
