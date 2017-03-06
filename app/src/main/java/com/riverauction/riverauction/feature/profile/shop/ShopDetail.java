package com.riverauction.riverauction.feature.profile.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.riverauction.riverauction.BuildConfig;
import com.riverauction.riverauction.R;
import com.riverauction.riverauction.RiverAuctionConstant;
import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CReceipt;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.base.BaseActivity;
import com.riverauction.riverauction.inapppurchase.util.IabHelper;
import com.riverauction.riverauction.inapppurchase.util.IabResult;
import com.riverauction.riverauction.inapppurchase.util.Inventory;
import com.riverauction.riverauction.inapppurchase.util.Purchase;
import com.riverauction.riverauction.states.UserStates;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;

import static com.riverauction.riverauction.feature.profile.shop.ShopActivity.BASIC_PRICE;
import static com.riverauction.riverauction.feature.profile.shop.ShopActivity.PRODUCT_NAME;
import static com.riverauction.riverauction.feature.profile.shop.ShopActivity.PRODUCT_PRICE;
import static com.riverauction.riverauction.feature.profile.shop.ShopActivity.SKU_ID;

public class ShopDetail extends BaseActivity implements ShopMvpView {
    // Debug tag, for logging
    static final String TAG = "ShopActivity";

    static final String SKU_PURCHASED = "android.test.purchased";
    static final String SKU_CANCELED = "android.test.canceled";
    static final String SKU_REFUNDED = "android.test.refunded";
    static final String SKU_ITEM_UNAVAILABLE = "android.test.item_unavailable";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    // The helper object
    IabHelper mHelper;
    @Inject ShopPresenter presenter;

    @Bind(R.id.product_name) TextView productName;
    @Bind(R.id.shop_my_item_during) TextView during;
    @Bind(R.id.price) TextView price;
    @Bind(R.id.purchaseButton) TextView purchaseButton;
    @Bind(R.id.shop_my_item_during2) TextView during2;

    private boolean isUseYn= false;
    private CUser user;
    private String skuId;
    private Integer product_name;
    private Integer product_price;
    private String basic_price;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_shop_detail;
    }


    // userId 를 이전 화면에서 넘겨받는다
    private void getDataFromBundle(Bundle bundle) {
        if (bundle != null) {
            skuId = bundle.getString(SKU_ID, "");
            product_name = bundle.getInt(PRODUCT_NAME, -1);
            product_price = bundle.getInt(PRODUCT_PRICE, -1);
            basic_price =  bundle.getString(BASIC_PRICE, "");
            if (skuId == "") {
                throw new IllegalStateException("skuId must be exist");
            }
            productName.setText("서비스 이용권 "+product_name.toString()+"개월");
            during.setText(product_name.toString()+"개월");

            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String formattedString = formatter.format(product_price);
            price.setText(basic_price+"(20%) "+formattedString+"원(VAT포함)");

            Date startDate = new Date();
            //startDate.setTime(Long.parseLong(serviceStart));
            Date endDate = new Date();


            int mMonth = startDate.getMonth();
            endDate.setMonth(mMonth + Integer.parseInt(product_name.toString()));

            SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일");
            String str = dayTime.format(new Date(startDate.getTime()));

            during2.setText(str +"~"+ DateUtils.getRelativeTimeSpanString(endDate.getTime()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromBundle(getIntent().getExtras());
        getActivityComponent().inject(this);
        presenter.attachView(this, this);

        user = UserStates.USER.get(stateCtx);

        getSupportActionBar().setTitle("이용권 구매");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mHelper = new IabHelper(this, RiverAuctionConstant.IN_APP_BILLING_KEY);
        mHelper.enableDebugLogging(true);
        mHelper.startSetup(result -> {
            Log.d(TAG, "Setup finished.");
            if (!result.isSuccess()) {
                // Oh noes, there was a problem.
                complain(getString(R.string.common_error_message_unknown));
                return;
            }
            if (mHelper == null) {
                return;
            }

            // IAB is fully set up. Now, let's get an inventory of stuff we own.
            mHelper.queryInventoryAsync(mGotInventoryListener);
        });

        presenter.getUser(user.getId());

        purchaseButton.setOnClickListener(v -> {
            if(!isUseYn) {
                // Purchase and Consume
                new AlertDialog.Builder(context)
                        .setTitle(R.string.shop_purchase_dialog_title)
                        .setMessage(R.string.shop_purchase_dialog_message)
                        .setPositiveButton(R.string.common_button_ok, (dialog, which) -> {
                            purchaseItem(skuId);


                        })
                        .setCancelable(true)
                        .show();
            }else
            {
                Toast.makeText(context, "이미 서비스를 이용 중입니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            if (BuildConfig.DEBUG) {
                consumeAsyncInInventory(inventory, SKU_PURCHASED);
                consumeAsyncInInventory(inventory, SKU_CANCELED);
                consumeAsyncInInventory(inventory, SKU_REFUNDED);
                consumeAsyncInInventory(inventory, SKU_ITEM_UNAVAILABLE);
            }
            //consumeAsyncInInventory(inventory, RiverAuctionConstant.SKU_ID_COST3000);
            //consumeAsyncInInventory(inventory, RiverAuctionConstant.SKU_ID_COST5000);
            consumeAsyncInInventory(inventory, RiverAuctionConstant.SKU_ID_COST13600);
            consumeAsyncInInventory(inventory, RiverAuctionConstant.SKU_ID_COST19200);
            consumeAsyncInInventory(inventory, RiverAuctionConstant.SKU_ID_COST23200);
            //consumeAsyncInInventory(inventory, RiverAuctionConstant.SKU_ID_COST10000);
            //consumeAsyncInInventory(inventory, RiverAuctionConstant.SKU_ID_COST20000);
            //consumeAsyncInInventory(inventory, RiverAuctionConstant.SKU_ID_COST50000);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    private void consumeAsyncInInventory(Inventory inventory, String skuId) {
        Purchase gasPurchase = inventory.getPurchase(skuId);
        if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
            Log.d(TAG, "We have gas. Consuming it.");
            mHelper.consumeAsync(inventory.getPurchase(skuId), mConsumeFinishedListener);
            return;
        }
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                if (result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED) {
                    Log.d(TAG, "ITEM_ALREADY_OWNED - start consuming this item");
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                }
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain(getString(R.string.common_error_message_unknown));
                return;
            }

            Log.d(TAG, "Purchase successful.");
            mHelper.consumeAsync(purchase, mConsumeFinishedListener);
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");
                presenter.purchaseCoin(new CReceipt(purchase));
            } else {
                complain(getString(R.string.common_error_message_unknown));
            }
            Log.d(TAG, "End consumption flow.");
        }
    };

    public void purchaseItem(String skuId) {
        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        mHelper.launchPurchaseFlow(this, skuId, RC_REQUEST, mPurchaseFinishedListener, payload);
    }

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            Intent intent = new Intent(context, ShopActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data, Bundle bundle) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }


    @Override
    public void successGetUser(CUser user) {
        this.user = user;
        UserStates.USER.set(stateCtx, this.user);
    }

    @Override
    public boolean failGetUser(CErrorCause errorCause) {
        return false;
    }

    @Override
    public void successPurchaseCoin(Boolean result) {
        presenter.getUser(user.getId());
        Toast.makeText(context, R.string.shop_purchase_dialog_success_title, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, ShopActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean failPurchaseCoin(CErrorCause errorCause) {
        if (CErrorCause.WRONG_RECEIPT == errorCause) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.shop_purchase_dialog_title)
                    .setMessage(R.string.common_error_message_unknown)
                    .setPositiveButton(R.string.common_button_ok, null)
                    .show();
            return true;
        }
        return false;
    }

}
