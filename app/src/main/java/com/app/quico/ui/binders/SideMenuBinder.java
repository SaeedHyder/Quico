package com.app.quico.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.entities.SideMenuEnt;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.app.quico.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SideMenuBinder extends RecyclerViewBinder<SideMenuEnt> {

    private DockActivity dockActivity;
    private BasePreferenceHelper prefHelper;
    private ImageLoader imageLoader;
    private RecyclerClickListner clickListner;

    public SideMenuBinder(DockActivity dockActivity, BasePreferenceHelper prefHelper, RecyclerClickListner clickListner) {
        super(R.layout.row_item_sidemenu);
        this.dockActivity = dockActivity;
        this.prefHelper = prefHelper;
        this.imageLoader = ImageLoader.getInstance();
        this.clickListner = clickListner;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(SideMenuEnt entity, int position, Object viewHolder, Context context) {

        final ViewHolder holder = (ViewHolder) viewHolder;

        if(entity.isSelected()){
            holder.txtItemName.setText(entity.getTitle());
            holder.txtItemName.setTextColor(dockActivity.getResources().getColor(R.color.white));
            holder.icon.setImageResource(entity.getSelectedImage());
            holder.llItem.setBackground(dockActivity.getResources().getDrawable(R.drawable.sidemenu_box));
        }else{
            holder.txtItemName.setText(entity.getTitle());
            holder.txtItemName.setTextColor(dockActivity.getResources().getColor(R.color.black));
            holder.icon.setImageResource(entity.getUnSelectedImage());
            holder.llItem.setBackgroundColor(dockActivity.getResources().getColor(R.color.transparent));
        }



        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListner.onClick(entity, position);
            }
        });

    }

    static class ViewHolder extends BaseViewHolder{
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.txt_item_name)
        AnyTextView txtItemName;
        @BindView(R.id.ll_item)
        LinearLayout llItem;
        @BindView(R.id.ll_mainFrame)
        RelativeLayout llMainFrame;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
