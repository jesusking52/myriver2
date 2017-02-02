package com.riverauction.riverauction.feature.common.review;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import com.riverauction.riverauction.R;
import com.riverauction.riverauction.api.model.CDayOfWeekType;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewDialog extends Dialog {
    @Bind(R.id.close_button) View closeButton;
    @Bind(R.id.confirm_button) View confirmButton;

    public ReviewDialog(Context context) {
        super(context);
        //this.onSelectedDayOfWeeksListener = onSelectedDayOfWeeksListener;
        initialize(context);
    }

    public ReviewDialog(Context context, int theme) {
        super(context, theme);
        initialize(context);
    }

    protected ReviewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initialize(context);
    }

    private void initialize(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_review);
        ButterKnife.bind(this);

        closeButton.setOnClickListener(v -> dismiss());

        confirmButton.setOnClickListener(v -> {

        });
    }

    public interface OnSelectedDayOfWeeksListener {
        void getSelectedDayOfWeeks(List<CDayOfWeekType> selectedDayOfWeekTypes);
    }
}
