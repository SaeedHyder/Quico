package com.app.quico.ui.binders;

import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.activities.MainActivity;
import com.app.quico.entities.CitiesEnt;
import com.app.quico.entities.LocationEnt;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.interfaces.AreaInterface;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.viewbinders.abstracts.ExpandableListViewBinder;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AreaExpendableBinder extends ExpandableListViewBinder<CitiesEnt, ArrayList<LocationEnt>> implements AreaInterface {

    DockActivity context;
    private BasePreferenceHelper preferenceHelper;
    private MainActivity mainActivity;
    private AreaInterface clickListner;


    public AreaExpendableBinder(DockActivity context, BasePreferenceHelper prefHelper, MainActivity mainActivity, AreaInterface clickListner) {

        super(R.layout.row_item_area_parent, R.layout.row_item_area_child);
        this.context = context;
        this.preferenceHelper = prefHelper;
        this.mainActivity = mainActivity;
        this.clickListner = clickListner;
    }


    @Override
    public BaseGroupViewHolder createGroupViewHolder(View view) {
        return new parentViewHolder(view);
    }

    @Override
    public childViewHolder createChildViewHolder(View view) {
        return new childViewHolder(view);
    }


    @Override
    public void bindGroupView(CitiesEnt entity, int position, int grpPosition, View view, Activity activity,boolean isExpended) {

        parentViewHolder parentViewHolder = (parentViewHolder) view.getTag();

        parentViewHolder.txtTitle.setText(entity.getLocation());

        if (isExpended) {
             parentViewHolder.downArrow.setRotation(180);
        } else {
             parentViewHolder.downArrow.setRotation(360);
        }

    }


    @Override
    public void bindChildView(ArrayList<LocationEnt> entity, int position, int grpPosition, View view, Activity activity) {

        childViewHolder childViewHolder = (childViewHolder) view.getTag();

        childViewHolder.rvSubAreas.BindRecyclerView(new SubAreaBinder(context, this), entity,
                new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                , new DefaultItemAnimator());


    }


    @Override
    public void selectArea(Object entity, int position) {
        clickListner.selectArea(entity, position);
    }

    @Override
    public void selectService(String selectedIds, String names) {

    }



    static class parentViewHolder extends BaseGroupViewHolder {
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;
        @BindView(R.id.downArrow)
        ImageView downArrow;


        parentViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public static class childViewHolder extends BaseGroupViewHolder {
        @BindView(R.id.rv_subAreas)
        CustomRecyclerView rvSubAreas;

        childViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
