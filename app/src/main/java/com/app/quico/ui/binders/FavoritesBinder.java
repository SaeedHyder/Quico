package com.app.quico.ui.binders;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.entities.CompanyEnt;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRatingBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FavoritesBinder extends RecyclerViewBinder<CompanyEnt> {

    private DockActivity dockActivity;
    private BasePreferenceHelper prefHelper;
    private ImageLoader imageLoader;
    private RecyclerClickListner clickListner;

    public FavoritesBinder(DockActivity dockActivity, BasePreferenceHelper prefHelper, RecyclerClickListner clickListner) {
        super(R.layout.row_item_service_listing);
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
    public void bindView(CompanyEnt entity, int position, Object viewHolder, Context context) {

        final ViewHolder holder = (ViewHolder) viewHolder;

        if (entity != null) {
           /* if (entity.getIsFeature() == 1) {
                holder.featured.setVisibility(View.VISIBLE);
            } else {
                holder.featured.setVisibility(View.GONE);
            }*/
            holder.txtName.setText(entity.getName() + "");
            holder.txtRating.setText(entity.getReviewCount() + " " + dockActivity.getResources().getString(R.string.reviews));
            holder.rbParlourRating.setScore(entity.getAvgRate());

            holder.mainFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListner.onClick(entity, position);
                }
            });

            imageLoader.displayImage(entity.getIconUrl(), holder.logo);
        }

    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.logo)
        ImageView logo;
        @BindView(R.id.txt_name)
        AnyTextView txtName;
        @BindView(R.id.rbParlourRating)
        CustomRatingBar rbParlourRating;
        @BindView(R.id.txt_rating)
        AnyTextView txtRating;
        @BindView(R.id.featured)
        AnyTextView featured;
        @BindView(R.id.mainFrame)
        CardView mainFrame;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
