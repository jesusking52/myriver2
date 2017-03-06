package com.riverauction.riverauction.feature.profile;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.avast.android.dialogs.iface.IListDialogListener;
import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CErrorCause;
import com.riverauction.riverauction.api.model.CUser;
import com.riverauction.riverauction.base.BaseActivity;
import com.riverauction.riverauction.feature.main.MainActivity;
import com.riverauction.riverauction.feature.utils.PermissionUtils;

import javax.inject.Inject;

public class PrivateActivity extends BaseActivity implements ProfileMvpView, IListDialogListener {
    private static final int DIALOG_REQUEST_PROFILE_IMAGE = 0;
    public static final int PERMISSION_STORAGE_CAMERA = 0x01;
    public static final int PERMISSION_STORAGE_GALLERY = 0x02;

    @Inject ProfilePresenter presenter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_private;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        presenter.attachView(this, this);

        getSupportActionBar().setTitle("개인정보 취급 방침");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean granted = PermissionUtils.checkGranted(grantResults);
        if (!granted) {
            PermissionUtils.showDeniedMessage(this, permissions, grantResults);
        }

    }

    @Override
    public void successSignOut(Boolean result) {
        MainActivity.restartApplication(context);
    }

    @Override
    public boolean failSignOut(CErrorCause errorCause) {
        return false;
    }

    @Override
    public void successPostPreferences(CUser user) {
    }

    @Override
    public boolean failPostPreferences(CErrorCause errorCause) {
        return false;
    }

    @Override
    public void successPostProfilePhoto(CUser user) {
    }

    @Override
    public boolean failPostProfilePhoto(CErrorCause errorCause) {
        return false;
    }

    @Override
    public void successDropOut(Boolean boardRegist) {
    }

    @Override
    public boolean failDropOut(CErrorCause errorCause) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onListItemSelected(String value, int number, int requestCode) {

    }
}
