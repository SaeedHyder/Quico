package com.app.quico.ui.binders;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.entities.Chat.DocumentDetail;
import com.app.quico.entities.Chat.ThreadMsgesEnt;
import com.app.quico.global.AppConstants;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.helpers.DateHelper;
import com.app.quico.interfaces.ChatAttachmentInterface;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.viewbinders.abstracts.RecyclerViewBinder;
import com.app.quico.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChatBinder extends RecyclerViewBinder<ThreadMsgesEnt> {

    private DockActivity dockActivity;
    private BasePreferenceHelper prefHelper;
    private ImageLoader imageLoader;
    private RecyclerClickListner clickListner;
    private ChatAttachmentInterface chatClickInterface;
    private ArrayList<DocumentDetail> image;
    private ArrayList<DocumentDetail> pdfFiles;
    private boolean isSender;

    public ChatBinder(DockActivity dockActivity, BasePreferenceHelper prefHelper, RecyclerClickListner clickListner, ChatAttachmentInterface chatClickInterface) {
        super(R.layout.row_item_chat);
        this.dockActivity = dockActivity;
        this.prefHelper = prefHelper;
        this.imageLoader = ImageLoader.getInstance();
        this.clickListner = clickListner;
        this.chatClickInterface = chatClickInterface;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(ThreadMsgesEnt entity, int position, Object viewHolder, Context context) {

        final ViewHolder holder = (ViewHolder) viewHolder;

        isSender = checkIsSender(entity);
        if (entity != null) {
            if (!isSender) {

                holder.rlLeft.setVisibility(View.VISIBLE);
                holder.rlRight.setVisibility(View.GONE);

                if (entity.getLatitude() != null && entity.getLongitude() != null && entity.getMap_url() != null) {

                    holder.llLeftText.setVisibility(View.GONE);
                    holder.cvImageLeft.setVisibility(View.GONE);
                    holder.cvMapLeft.setVisibility(View.VISIBLE);
                    holder.cvPdfLeft.setVisibility(View.GONE);

                    if (entity.getMap_url() != null && !entity.getMap_url().equals("")) {
                       Picasso.get().load(entity.getMap_url()).placeholder(R.drawable.map_placeholder).into(holder.ivMapLeft);
                    }
                    if (entity.getLatitude() != null && !entity.getLatitude().equals("") && entity.getLongitude() != null && !entity.getLongitude().equals("")) {
                        holder.txtMapAddressLeft.setVisibility(View.VISIBLE);
                        holder.txtMapDateLeft.setVisibility(View.VISIBLE);
                    //    holder.txtMapAddressLeft.setText(dockActivity.getCurrentAddress(Double.parseDouble(entity.getLatitude()), Double.parseDouble(entity.getLongitude())));
                        holder.txtMapAddressLeft.setText(entity.getAddress());
                        holder.txtMapDateLeft.setText(DateHelper.getChatMessageTime(entity.getCreatedAt()));
                    } else {
                        holder.txtMapAddressLeft.setVisibility(View.GONE);
                        holder.txtMapDateLeft.setVisibility(View.GONE);
                    }


                } else if (entity.getDocumentDetail() != null && entity.getDocumentDetail().size() > 0) {

                    image = new ArrayList<>();
                    pdfFiles = new ArrayList<>();
                    if(entity.getDocumentDetail().size()>0) {
                        for (DocumentDetail item : entity.getDocumentDetail()) {
                            if (item.getType().equals(AppConstants.PDF)) {
                                pdfFiles.add(item);
                            } else if (item.getType().equals(AppConstants.photo)) {
                                image.add(item);
                            }
                        }

                        if (image.size() > 0 && pdfFiles.size() > 0) {

                            holder.cvImageLeft.setVisibility(View.VISIBLE);
                            holder.cvPdfLeft.setVisibility(View.VISIBLE);
                            holder.cvMapLeft.setVisibility(View.GONE);
                            holder.llLeftText.setVisibility(View.GONE);

                            holder.txtLeftCount.setText(String.valueOf(image.size()));
                          //   imageLoader.displayImage(image.get(0).getThumbUrl(), holder.ivImageLeft);
                            Picasso.get().load(image.get(0).getThumbUrl()).fit().centerCrop().placeholder(R.drawable.placeholder_thumb).into(holder.ivImageLeft);
                            holder.txtImageDateLeft.setText(DateHelper.getChatMessageTime(entity.getCreatedAt()));

                            if (entity.getMessage() != null && !entity.getMessage().equals("")) {
                                holder.txtImageMessageLeft.setVisibility(View.VISIBLE);
                                holder.txtImageDateLeft.setVisibility(View.VISIBLE);
                                holder.txtImageMessageLeft.setText(entity.getMessage());

                            } else {
                                holder.txtImageMessageLeft.setVisibility(View.GONE);
                               // holder.txtImageDateLeft.setVisibility(View.GONE);
                            }


                            holder.txtPdfCountLeft.setText(String.valueOf(pdfFiles.size()));
                            holder.txtPdfDateLeft.setText(DateHelper.getChatMessageTime(entity.getCreatedAt()));
                            if (entity.getMessage() != null && !entity.getMessage().equals("")) {
                                holder.txtPdfMessageLeft.setVisibility(View.VISIBLE);
                                holder.txtPdfDateLeft.setVisibility(View.VISIBLE);
                                holder.txtPdfMessageLeft.setText(entity.getMessage());

                            } else {
                                holder.txtPdfMessageLeft.setVisibility(View.GONE);
                              //  holder.txtPdfDateLeft.setVisibility(View.GONE);
                            }

                        } else if (image.size() > 0 && pdfFiles.size() <= 0) {
                            holder.cvImageLeft.setVisibility(View.VISIBLE);
                            holder.cvPdfLeft.setVisibility(View.GONE);
                            holder.cvMapLeft.setVisibility(View.GONE);
                            holder.llLeftText.setVisibility(View.GONE);

                            holder.txtLeftCount.setText(String.valueOf(image.size()));
                           //    imageLoader.displayImage(image.get(0).getThumbUrl(), holder.ivImageLeft);
                            Picasso.get().load(image.get(0).getThumbUrl()).fit().centerCrop().placeholder(R.drawable.placeholder_thumb).into(holder.ivImageLeft);
                            holder.txtImageDateLeft.setText(DateHelper.getChatMessageTime(entity.getCreatedAt()));

                            if (entity.getMessage() != null && !entity.getMessage().equals("")) {
                                holder.txtImageMessageLeft.setVisibility(View.VISIBLE);
                                holder.txtImageDateLeft.setVisibility(View.VISIBLE);
                                holder.txtImageMessageLeft.setText(entity.getMessage());

                            } else {
                                holder.txtImageMessageLeft.setVisibility(View.GONE);
                                //holder.txtImageDateLeft.setVisibility(View.GONE);
                            }
                        } else if (pdfFiles.size() > 0 && image.size() <= 0) {
                            holder.cvImageLeft.setVisibility(View.GONE);
                            holder.cvPdfLeft.setVisibility(View.VISIBLE);
                            holder.cvMapLeft.setVisibility(View.GONE);
                            holder.llLeftText.setVisibility(View.GONE);

                            holder.txtPdfCountLeft.setText(String.valueOf(pdfFiles.size()));
                            holder.txtPdfDateLeft.setText(DateHelper.getChatMessageTime(entity.getCreatedAt()));

                            if (entity.getMessage() != null && !entity.getMessage().equals("")) {
                                holder.txtPdfMessageLeft.setVisibility(View.VISIBLE);
                                holder.txtPdfDateLeft.setVisibility(View.VISIBLE);
                                holder.txtPdfMessageLeft.setText(entity.getMessage());

                            } else {
                                holder.txtPdfMessageLeft.setVisibility(View.GONE);
                              //  holder.txtPdfDateLeft.setVisibility(View.GONE);
                            }
                        }

                    }
                } else if (entity.getMessage() != null && !entity.getMessage().equals("")) {
                    holder.llLeftText.setVisibility(View.VISIBLE);
                    holder.cvImageLeft.setVisibility(View.GONE);
                    holder.cvMapLeft.setVisibility(View.GONE);
                    holder.cvPdfLeft.setVisibility(View.GONE);

                    holder.txtLeft.setText(entity.getMessage());
                    holder.txtLeftDate.setText(DateHelper.getChatMessageTime(entity.getCreatedAt()));
                } else {
                    holder.llLeftText.setVisibility(View.GONE);
                    holder.cvImageLeft.setVisibility(View.GONE);
                    holder.cvMapLeft.setVisibility(View.GONE);
                    holder.cvPdfLeft.setVisibility(View.GONE);
                }


            } else {

                holder.rlLeft.setVisibility(View.GONE);
                holder.rlRight.setVisibility(View.VISIBLE);

                if (entity.getLatitude() != null && entity.getLongitude() != null && entity.getMap_url() != null) {

                    holder.llRightText.setVisibility(View.GONE);
                    holder.cvImageRight.setVisibility(View.GONE);
                    holder.cvMapRight.setVisibility(View.VISIBLE);
                    holder.cvPdfRight.setVisibility(View.GONE);

                    if (entity.getMap_url() != null && !entity.getMap_url().equals("")) {
                       Picasso.get().load(entity.getMap_url()).placeholder(R.drawable.map_placeholder).into(holder.ivMapRight);
                    }
                    if (entity.getLatitude() != null && !entity.getLatitude().equals("") && entity.getLongitude() != null && !entity.getLongitude().equals("")) {
                        holder.txtMapAddressRight.setVisibility(View.VISIBLE);
                        holder.txtMapDateRight.setVisibility(View.VISIBLE);
                       // holder.txtMapAddressRight.setText(dockActivity.getCurrentAddress(Double.parseDouble(entity.getLatitude()), Double.parseDouble(entity.getLongitude())));
                        holder.txtMapAddressRight.setText(entity.getAddress());
                        holder.txtMapDateRight.setText(DateHelper.getChatMessageTime(entity.getCreatedAt()));
                    } else {
                        holder.txtMapAddressRight.setVisibility(View.GONE);
                      //  holder.txtMapDateRight.setVisibility(View.GONE);
                    }


                } else if ( entity.getDocumentDetail() != null && entity.getDocumentDetail().size() > 0) {

                    image = new ArrayList<>();
                    pdfFiles = new ArrayList<>();
                    if(entity.getDocumentDetail().size()>0) {
                        for (DocumentDetail item : entity.getDocumentDetail()) {
                            if (item.getType().equals(AppConstants.PDF)) {
                                pdfFiles.add(item);
                            } else if (item.getType().equals(AppConstants.photo)) {
                                image.add(item);
                            }
                        }
                        if (image.size() > 0 && pdfFiles.size() > 0) {

                            holder.cvImageRight.setVisibility(View.VISIBLE);
                            holder.cvPdfRight.setVisibility(View.VISIBLE);
                            holder.llRightText.setVisibility(View.GONE);
                            holder.cvMapRight.setVisibility(View.GONE);


                            holder.txtRightCount.setText(String.valueOf(image.size()));
                       //    imageLoader.displayImage(image.get(0).getThumbUrl(), holder.ivImageRight);
                            Picasso.get().load(image.get(0).getThumbUrl()).fit().centerCrop().placeholder(R.drawable.placeholder_thumb).into(holder.ivImageRight);
                            holder.txtImageDateRight.setText(DateHelper.getChatMessageTime(entity.getCreatedAt()));

                            if (entity.getMessage() != null && !entity.getMessage().equals("")) {
                                holder.txtImageMessageRight.setVisibility(View.VISIBLE);
                                holder.txtImageDateRight.setVisibility(View.VISIBLE);
                                holder.txtImageMessageRight.setText(entity.getMessage());


                            } else {
                                holder.txtImageMessageRight.setVisibility(View.GONE);
                           //     holder.txtImageDateRight.setVisibility(View.GONE);
                            }


                            holder.txtPdfCountRight.setText(String.valueOf(pdfFiles.size()));
                            holder.txtPdfDateRight.setText(DateHelper.getChatMessageTime(entity.getCreatedAt()));

                            if (entity.getMessage() != null && !entity.getMessage().equals("")) {
                                holder.txtPdfMessageRight.setVisibility(View.VISIBLE);
                                holder.txtPdfDateRight.setVisibility(View.VISIBLE);
                                holder.txtPdfMessageRight.setText(entity.getMessage());

                            } else {
                                holder.txtPdfMessageRight.setVisibility(View.GONE);
                            //    holder.txtPdfDateRight.setVisibility(View.GONE);
                            }

                        } else if (image.size() > 0 && pdfFiles.size() <= 0) {
                            holder.cvImageRight.setVisibility(View.VISIBLE);
                            holder.cvPdfRight.setVisibility(View.GONE);
                            holder.llRightText.setVisibility(View.GONE);
                            holder.cvMapRight.setVisibility(View.GONE);

                            holder.txtRightCount.setText(String.valueOf(image.size()));
                            Picasso.get().load(image.get(0).getThumbUrl()).fit().centerCrop().placeholder(R.drawable.placeholder_thumb).into(holder.ivImageRight);
                        //    imageLoader.displayImage(image.get(0).getThumbUrl(), holder.ivImageRight);
                            holder.txtImageDateRight.setText(DateHelper.getChatMessageTime(entity.getCreatedAt()));

                            if (entity.getMessage() != null && !entity.getMessage().equals("")) {
                                holder.txtImageMessageRight.setVisibility(View.VISIBLE);
                                holder.txtImageDateRight.setVisibility(View.VISIBLE);
                                holder.txtImageMessageRight.setText(entity.getMessage());

                            } else {
                                holder.txtImageMessageRight.setVisibility(View.GONE);
                             //   holder.txtImageDateRight.setVisibility(View.GONE);
                            }
                        } else if (pdfFiles.size() > 0 && image.size() <= 0) {
                            holder.cvImageRight.setVisibility(View.GONE);
                            holder.cvPdfRight.setVisibility(View.VISIBLE);
                            holder.llRightText.setVisibility(View.GONE);
                            holder.cvMapRight.setVisibility(View.GONE);

                            holder.txtPdfCountRight.setText(String.valueOf(pdfFiles.size()));
                            holder.txtPdfDateRight.setText(DateHelper.getChatMessageTime(entity.getCreatedAt()));

                            if (entity.getMessage() != null && !entity.getMessage().equals("")) {
                                holder.txtPdfMessageRight.setVisibility(View.VISIBLE);
                                holder.txtPdfDateRight.setVisibility(View.VISIBLE);
                                holder.txtPdfMessageRight.setText(entity.getMessage());

                            } else {
                                holder.txtPdfMessageRight.setVisibility(View.GONE);
                              //  holder.txtPdfDateRight.setVisibility(View.GONE);
                            }
                        }

                    }
                } else if (entity.getMessage() != null && !entity.getMessage().equals("")) {

                    holder.llRightText.setVisibility(View.VISIBLE);
                    holder.cvImageRight.setVisibility(View.GONE);
                    holder.cvMapRight.setVisibility(View.GONE);
                    holder.cvPdfRight.setVisibility(View.GONE);

                    holder.txtRight.setText(entity.getMessage());
                    holder.txtRightDate.setText(DateHelper.getChatMessageTime(entity.getCreatedAt()));
                } else {
                    holder.llRightText.setVisibility(View.GONE);
                    holder.cvImageRight.setVisibility(View.GONE);
                    holder.cvMapRight.setVisibility(View.GONE);
                    holder.cvPdfRight.setVisibility(View.GONE);
                }
            }

        }

        holder.cvImageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chatClickInterface.onImageClick(entity, position);
            }
        });

        holder.cvImageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chatClickInterface.onImageClick(entity, position);
            }
        });

        holder.cvMapLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chatClickInterface.onLocationClick(entity, position);

            }
        });
        holder.cvMapRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chatClickInterface.onLocationClick(entity, position);

            }
        });

        holder.cvPdfLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chatClickInterface.onFileCLick(entity, position);
            }
        });
        holder.cvPdfRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chatClickInterface.onFileCLick(entity, position);

            }
        });

    }


    public String getUserId(String senderId, String receiverId, String myId) {
        if (myId.equals(senderId)) {
            return receiverId;
        } else {
            return senderId;
        }
    }

    public boolean checkIsSender(ThreadMsgesEnt entity) {
        if (String.valueOf(entity.getSenderId()).equals(String.valueOf(prefHelper.getUser().getUser().getId()))) {
            return true;
        } else if (String.valueOf(entity.getReceiverId()).equals(String.valueOf(prefHelper.getUser().getUser().getId()))) {
            return false;
        } else {
            return false;
        }
    }


    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_left)
        AnyTextView txtLeft;
        @BindView(R.id.txt_left_date)
        AnyTextView txtLeftDate;
        @BindView(R.id.ll_left_text)
        LinearLayout llLeftText;
        @BindView(R.id.iv_image_left)
        ImageView ivImageLeft;
        @BindView(R.id.txt_left_count)
        AnyTextView txtLeftCount;
        @BindView(R.id.txt_image_message_left)
        AnyTextView txtImageMessageLeft;
        @BindView(R.id.txt_image_date_left)
        AnyTextView txtImageDateLeft;
        @BindView(R.id.cv_image_left)
        CardView cvImageLeft;
        @BindView(R.id.iv_pdf_left)
        ImageView ivPdfLeft;
        @BindView(R.id.txt_pdf_count_left)
        AnyTextView txtPdfCountLeft;
        @BindView(R.id.txt_pdf_message_left)
        AnyTextView txtPdfMessageLeft;
        @BindView(R.id.txt_pdf_date_left)
        AnyTextView txtPdfDateLeft;
        @BindView(R.id.cv_pdf_left)
        CardView cvPdfLeft;
        @BindView(R.id.iv_map_left)
        ImageView ivMapLeft;
        @BindView(R.id.txt_map_address_left)
        AnyTextView txtMapAddressLeft;
        @BindView(R.id.txt_map_date_left)
        AnyTextView txtMapDateLeft;
        @BindView(R.id.cv_map_left)
        CardView cvMapLeft;
        @BindView(R.id.rl_left)
        RelativeLayout rlLeft;
        @BindView(R.id.txt_right)
        AnyTextView txtRight;
        @BindView(R.id.txt_right_date)
        AnyTextView txtRightDate;
        @BindView(R.id.ll_right_text)
        LinearLayout llRightText;
        @BindView(R.id.iv_image_right)
        ImageView ivImageRight;
        @BindView(R.id.txt_right_count)
        AnyTextView txtRightCount;
        @BindView(R.id.txt_image_message_right)
        AnyTextView txtImageMessageRight;
        @BindView(R.id.txt_image_date_right)
        AnyTextView txtImageDateRight;
        @BindView(R.id.cv_image_right)
        CardView cvImageRight;
        @BindView(R.id.iv_pdf_right)
        ImageView ivPdfRight;
        @BindView(R.id.txt_pdf_count_right)
        AnyTextView txtPdfCountRight;
        @BindView(R.id.txt_pdf_message_right)
        AnyTextView txtPdfMessageRight;
        @BindView(R.id.txt_pdf_date_right)
        AnyTextView txtPdfDateRight;
        @BindView(R.id.cv_pdf_right)
        CardView cvPdfRight;
        @BindView(R.id.iv_map_right)
        ImageView ivMapRight;
        @BindView(R.id.txt_map_address_right)
        AnyTextView txtMapAddressRight;
        @BindView(R.id.txt_map_date_right)
        AnyTextView txtMapDateRight;
        @BindView(R.id.cv_map_right)
        CardView cvMapRight;
        @BindView(R.id.rl_right)
        RelativeLayout rlRight;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
