package com.app.quico.ui.binders;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.entities.Chat.DocumentDetail;
import com.app.quico.entities.ProjectDetail;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.app.quico.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PdfBinder extends RecyclerViewBinder<DocumentDetail> {

    private DockActivity dockActivity;
    private BasePreferenceHelper prefHelper;
    private ImageLoader imageLoader;
    private RecyclerClickListner clickListner;

    public PdfBinder(DockActivity dockActivity, BasePreferenceHelper prefHelper, RecyclerClickListner clickListner) {
        super(R.layout.row_item_pdf);
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
    public void bindView(DocumentDetail entity, int position, Object viewHolder, Context context) {

        final ViewHolder holder = (ViewHolder) viewHolder;

            holder.txtPdfMessage.setText(entity.getFileUrl());
            holder.txtPdfMessage.setSelected(true);


            Picasso.get().load(R.drawable.pdf_placeholder_2).fit().centerCrop().placeholder(R.drawable.placeholder_thumb).into(holder.ivPdf);


            holder.cvPdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListner.onClick(entity,position);
                }
            });


    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_pdf_left)
        ImageView ivPdf;
        @BindView(R.id.txt_pdf_message_left)
        AnyTextView txtPdfMessage;
        @BindView(R.id.cv_pdf_left)
        CardView cvPdf;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
