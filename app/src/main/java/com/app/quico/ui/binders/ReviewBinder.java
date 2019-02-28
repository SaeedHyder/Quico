package com.app.quico.ui.binders;

import android.content.Context;
import android.view.View;

import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.entities.ReviewsDetail;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.helpers.DateHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRatingBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class ReviewBinder extends RecyclerViewBinder<ReviewsDetail> {

    private DockActivity dockActivity;
    private BasePreferenceHelper prefHelper;
    private ImageLoader imageLoader;
    private RecyclerClickListner clickListner;

    public ReviewBinder(DockActivity dockActivity, BasePreferenceHelper prefHelper, RecyclerClickListner clickListner) {
        super(R.layout.row_item_reviews);
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
    public void bindView(ReviewsDetail entity, int position, Object viewHolder, Context context) {

        final ViewHolder holder = (ViewHolder) viewHolder;
        if (entity != null ) {
            if (entity.getUserDetail() != null && entity.getUserDetail().getDetails() != null && entity.getUserDetail().getDetails().getImageUrl() != null) {
                    Picasso.get().load(entity.getUserDetail().getDetails().getImageUrl()).placeholder(R.drawable.placeholder_thumb).into(holder.userImage);
            }
            if (entity.getUserDetail().getName() != null) {
                holder.txtName.setText(entity.getUserDetail().getName());
                holder.txtName.setSelected(true);
            }

            holder.txtDetail.setText(entity.getReview());
            holder.rbRating.setScore(entity.getRate());
            holder.txtDate.setText(DateHelper.getReviewsDateFormat(entity.getCreatedAt()));
        }


    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_name)
        AnyTextView txtName;
        @BindView(R.id.userImage)
        CircleImageView userImage;
        @BindView(R.id.txt_date)
        AnyTextView txtDate;
        @BindView(R.id.rbRating)
        CustomRatingBar rbRating;
        @BindView(R.id.txt_detail)
        AnyTextView txtDetail;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
