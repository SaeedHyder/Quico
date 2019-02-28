package com.app.quico.ui.binders;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.entities.Chat.ChatThreadEnt;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.helpers.DateHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.app.quico.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class MyChatThreadBinder extends RecyclerViewBinder<ChatThreadEnt> {

    private DockActivity dockActivity;
    private BasePreferenceHelper prefHelper;
    private ImageLoader imageLoader;
    private RecyclerClickListner clickListner;

    public MyChatThreadBinder(DockActivity dockActivity, BasePreferenceHelper prefHelper, RecyclerClickListner clickListner) {
        super(R.layout.row_item_mychat_thread);
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
    public void bindView(ChatThreadEnt entity, int position, Object viewHolder, Context context) {

        final ViewHolder holder = (ViewHolder) viewHolder;
        if (entity != null) {
            holder.txtName.setText(entity.getCompanyDetail().getName());
            holder.txtTime.setText(DateHelper.getChatMessageTime(entity.getUpdatedAt()));

            if(entity.getMessage()!=null && !entity.getMessage().equals("") && !entity.getMessage().isEmpty() ) {
                holder.txtDetail.setVisibility(View.VISIBLE);
                holder.txtDetail.setText(entity.getMessage());
            }else{
                holder.txtDetail.setVisibility(View.GONE);
            }

            if (entity.getIsMessage() != null && !entity.getIsMessage().equals("") && !entity.getIsMessage().equals("0")) {
                holder.txtBadge.setVisibility(View.VISIBLE);
                holder.txtBadge.setText(entity.getIsMessage());
            } else {
                holder.txtBadge.setVisibility(View.GONE);
            }
        }


        holder.mainFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListner.onClick(entity, position);
            }
        });

        if (entity.getCompanyDetail().getIconUrl() != null) {
            Picasso.get().load(entity.getCompanyDetail().getIconUrl()).placeholder(R.drawable.placeholder_thumb).into(holder.logo);
        }

    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.logo)
        CircleImageView logo;
        @BindView(R.id.txt_name)
        AnyTextView txtName;
        @BindView(R.id.txt_detail)
        AnyTextView txtDetail;
        @BindView(R.id.txt_time)
        AnyTextView txtTime;
        @BindView(R.id.txtBadge)
        AnyTextView txtBadge;
        @BindView(R.id.mainFrame)
        CardView mainFrame;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
