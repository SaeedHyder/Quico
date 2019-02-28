package com.app.quico.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.entities.AttachmentEnt;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.interfaces.AttachmentInterface;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.quico.global.AppConstants.PDF;

public class AttachmentBinder extends RecyclerViewBinder<AttachmentEnt> {

    private DockActivity dockActivity;
    private BasePreferenceHelper prefHelper;
    private ImageLoader imageLoader;
    private RecyclerClickListner clickListner;
    private AttachmentInterface crossClick;

    public AttachmentBinder(DockActivity dockActivity, BasePreferenceHelper prefHelper, RecyclerClickListner clickListner,AttachmentInterface crossClick) {
        super(R.layout.row_item_attachment);
        this.dockActivity = dockActivity;
        this.prefHelper = prefHelper;
        this.imageLoader = ImageLoader.getInstance();
        this.clickListner = clickListner;
        this.crossClick=crossClick;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(AttachmentEnt entity, int position, Object viewHolder, Context context) {

        final ViewHolder holder = (ViewHolder) viewHolder;
        if (entity != null) {
            if(entity.getType().equals(PDF)) {
           //     Picasso.with(dockActivity).load(new File(entity.getAttahcment())).placeholder(R.drawable.pdf_blue).into(holder.ivAttachment);
                holder.ivAttachment.setImageResource(R.drawable.pdf_placeholder_2);
            }else{
                Glide.with(dockActivity).load(entity.getBitmapImage()).into(holder.ivAttachment);
            }
        }
        holder.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crossClick.onCrossClick(entity,position);
            }
        });


    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_attachment)
        ImageView ivAttachment;
        @BindView(R.id.iv_cross)
        ImageView ivCross;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
