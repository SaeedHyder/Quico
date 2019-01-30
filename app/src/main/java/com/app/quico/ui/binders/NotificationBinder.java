package com.app.quico.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.entities.NotificationEnt;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.helpers.DateHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.app.quico.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationBinder extends RecyclerViewBinder<NotificationEnt> {

    private DockActivity dockActivity;
    private BasePreferenceHelper prefHelper;
    private ImageLoader imageLoader;
    private RecyclerClickListner clickListner;

    public NotificationBinder(DockActivity dockActivity, BasePreferenceHelper prefHelper, RecyclerClickListner clickListner) {
        super(R.layout.row_item_notificaiton);
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
    public void bindView(NotificationEnt entity, int position, Object viewHolder, Context context) {

        final ViewHolder holder = (ViewHolder) viewHolder;

        //imageLoader.displayImage(entity, holder.logo);
        holder.txtDetail.setText(entity.getMessage()+"");
        holder.txt_date.setText(DateHelper.getLocalTimeDate(entity.getCreatedAt()));

        holder.mainFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListner.onClick(entity, position);
            }
        });

    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.logo)
        ImageView logo;
        @BindView(R.id.txt_detail)
        AnyTextView txtDetail;
        @BindView(R.id.txt_date)
        AnyTextView txt_date;
        @BindView(R.id.mainFrame)
        LinearLayout mainFrame;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
