package com.app.quico.helpers;

import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;


import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.global.AppConstants;
import com.app.quico.interfaces.BottomSheetClickListner;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.SortBinder;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.ExpandedBottomSheetBehavior;

import java.util.ArrayList;

/**
 * Created on 6/30/2017.
 */

public class BottomSheetDialogHelper {
    ExpandedBottomSheetBehavior bottomSheetBehavior;
    private NestedScrollView dialog;
    private DockActivity context;
    private CoordinatorLayout mainParent;
    private BottomSheetClickListner bottomSheetClickListner;


    public BottomSheetDialogHelper(DockActivity context, CoordinatorLayout mainParent, int LayoutID, BottomSheetClickListner bottomSheetClickListner) {
        this.context = context;
        this.mainParent = mainParent;
        LayoutInflater inflater = LayoutInflater.from(context);
        dialog = (NestedScrollView) inflater.inflate(LayoutID, null, false);
        mainParent.addView(dialog);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) dialog.getLayoutParams();
        params.setBehavior(new ExpandedBottomSheetBehavior());
        dialog.requestLayout();
        bottomSheetBehavior = (ExpandedBottomSheetBehavior) ExpandedBottomSheetBehavior.from(dialog);
        //  bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setAllowUserDragging(false);
        bottomSheetBehavior.setPeekHeight(0);
        this.bottomSheetClickListner = bottomSheetClickListner;
    }

    public void initSortingDialoge(String selectedType) {

        AnyTextView btnDone = (AnyTextView) dialog.findViewById(R.id.btn_done);
        RadioButton featuredBtn = (RadioButton) dialog.findViewById(R.id.featuredBtn);
        RadioButton ReviewsBtn = (RadioButton) dialog.findViewById(R.id.ReviewsBtn);
        RadioButton ratingBtn = (RadioButton) dialog.findViewById(R.id.ratingBtn);
        RadioButton nearestBtn = (RadioButton) dialog.findViewById(R.id.nearestBtn);
        CustomRecyclerView rvSorting = (CustomRecyclerView) dialog.findViewById(R.id.rv_sorting);

        if (selectedType != null && !selectedType.equals("") && !selectedType.isEmpty()) {
            if (selectedType.equals(AppConstants.FeaturedSort)) {
                featuredBtn.setChecked(true);
            } else if (selectedType.equals(AppConstants.ReviewsSort)) {
                ReviewsBtn.setChecked(true);
            } else if (selectedType.equals(AppConstants.RatingSort)) {
                ratingBtn.setChecked(true);
            } else if (selectedType.equals(AppConstants.NearestSort)) {
                nearestBtn.setChecked(true);
            }
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (featuredBtn.isChecked()) {
                    bottomSheetClickListner.onClick(AppConstants.FeaturedSort);
                } else if (ReviewsBtn.isChecked()) {
                    bottomSheetClickListner.onClick(AppConstants.ReviewsSort);
                } else if (ratingBtn.isChecked()) {
                    bottomSheetClickListner.onClick(AppConstants.RatingSort);
                } else if (nearestBtn.isChecked()) {
                    bottomSheetClickListner.onClick(AppConstants.NearestSort);
                }
                hideDialog();
            }
        });

    }

    public void showDialog() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setPeekHeight((int) context.getResources().getDimension(R.dimen.x100));
        bottomSheetBehavior.setHideable(false);

    }

    public void hideDialog() {
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        RemoveDialog();

    }

    private void RemoveDialog() {
        mainParent.removeView(dialog);
    }


}
