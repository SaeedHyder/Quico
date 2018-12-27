package com.app.quico.helpers;

import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;


import com.app.quico.R;
import com.app.quico.activities.DockActivity;
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


    public BottomSheetDialogHelper(DockActivity context, CoordinatorLayout mainParent, int LayoutID) {
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
    }

    public void initSortingDialoge(){

        AnyTextView btnDone = (AnyTextView) dialog.findViewById(R.id.btn_done);
        CustomRecyclerView rvSorting = (CustomRecyclerView) dialog.findViewById(R.id.rv_sorting);

        ArrayList<String> collection = new ArrayList<>();
        collection.add("Featured");
        collection.add("Number of Reviews");
        collection.add("Rating");
        collection.add("Nearest");

        rvSorting.BindRecyclerView(new SortBinder(context), collection,
                new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                , new DefaultItemAnimator());

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showShortToastInDialoge(context, context.getResources().getString(R.string.will_be_implemented));
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
