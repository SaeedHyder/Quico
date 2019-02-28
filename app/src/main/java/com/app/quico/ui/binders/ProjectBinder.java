package com.app.quico.ui.binders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.entities.ProjectDetail;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.app.quico.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;


import butterknife.BindView;
import butterknife.ButterKnife;


public class ProjectBinder extends RecyclerViewBinder<ProjectDetail> {

    private DockActivity dockActivity;
    private BasePreferenceHelper prefHelper;
    private ImageLoader imageLoader;
    private RecyclerClickListner clickListner;

    public ProjectBinder(DockActivity dockActivity, BasePreferenceHelper prefHelper, RecyclerClickListner clickListner) {
        super(R.layout.row_item_project);
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
    public void bindView(ProjectDetail entity, int position, Object viewHolder, Context context) {

        final ViewHolder holder = (ViewHolder) viewHolder;
        if(entity!=null) {
            if (entity.getIconUrl() != null && !entity.getIconUrl().equals("") && !entity.getIconUrl().isEmpty()) {
                //imageLoader.displayImage(entity.getIconUrl(), holder.image);
                Picasso.get().load(entity.getIconUrl()).placeholder(R.drawable.placeholder).into(holder.image);
            }

            if(entity.getProjectName()!=null && !entity.getProjectName().equals("")){
                holder.txtTitle.setText(entity.getProjectName());
            }
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListner.onClick(entity, position);
            }
        });


    }


    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.txtTitle)
        AnyTextView txtTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
