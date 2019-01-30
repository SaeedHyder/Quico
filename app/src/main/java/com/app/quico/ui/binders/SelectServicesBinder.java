package com.app.quico.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.entities.AllServicesEnt;
import com.app.quico.entities.ServicesEnt;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.app.quico.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectServicesBinder extends RecyclerViewBinder<AllServicesEnt> {

    private DockActivity dockActivity;
    private BasePreferenceHelper prefHelper;
    private ImageLoader imageLoader;
    private RecyclerClickListner clickListner;

    public SelectServicesBinder(DockActivity dockActivity, RecyclerClickListner clickListner) {
        super(R.layout.row_item_select_services);
        this.dockActivity = dockActivity;
        this.clickListner = clickListner;
        this.imageLoader = ImageLoader.getInstance();

    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(AllServicesEnt entity, int position, Object viewHolder, Context context) {

        final ViewHolder holder = (ViewHolder) viewHolder;

        if(entity.isSelected()){
            holder.ivSelected.setVisibility(View.VISIBLE);
        }else{
            holder.ivSelected.setVisibility(View.GONE);
        }

        holder.txtTitle.setText(entity.getName());

        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListner.onClick(entity, position);
            }
        });


    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_title)
        AnyTextView txtTitle;
        @BindView(R.id.iv_selected)
        ImageView ivSelected;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
