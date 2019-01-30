package com.app.quico.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.entities.LocationEnt;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.interfaces.AreaInterface;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.app.quico.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SubAreaBinder extends RecyclerViewBinder<LocationEnt> {

    private DockActivity dockActivity;
    private BasePreferenceHelper prefHelper;
    private ImageLoader imageLoader;
    private AreaInterface clickListner;

    public SubAreaBinder(DockActivity dockActivity,AreaInterface clickListner) {
        super(R.layout.row_item_subareas);
        this.dockActivity = dockActivity;
        this.clickListner=clickListner;
        this.imageLoader = ImageLoader.getInstance();

    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(LocationEnt entity, int position, Object viewHolder, Context context) {

        final ViewHolder holder = (ViewHolder) viewHolder;

        holder.txtTitle.setText(entity.getLocation());

        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListner.selectArea(entity,position);
            }
        });


    }

    static class ViewHolder extends BaseViewHolder{
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
