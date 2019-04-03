package com.app.quico.helpers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created on 5/24/2017.
 */

public class DialogHelper {
    private Dialog dialog;
    private Context context;
    private ImageLoader imageLoader;
    private RadioGroup rg;


    public DialogHelper(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
    }


    public Dialog toastDialoge(String text) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(R.layout.toast_dialoge);
        AnyTextView textView = (AnyTextView) dialog.findViewById(R.id.txt_text);
        textView.setText(text);
        return this.dialog;
    }

      public Dialog initlogout(View.OnClickListener yesListner, View.OnClickListener noListner) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(R.layout.dialoge_logout);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(yesListner);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_No);
        btnNo.setOnClickListener(noListner);
        return this.dialog;
    }

    public Dialog commonDialoge(View.OnClickListener yesListner,String Title,String Description) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(R.layout.dialoge_logout);
        Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
        AnyTextView txtTitle = (AnyTextView) dialog.findViewById(R.id.txt_Logout);
        AnyTextView txtDescription = (AnyTextView) dialog.findViewById(R.id.txt_logout_text);
        btnYes.setOnClickListener(yesListner);
        txtTitle.setText(Title);
        txtDescription.setText(Description);
        Button btnNo = (Button) dialog.findViewById(R.id.btn_No);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return this.dialog;
    }


    /*public Dialog forgotPassDialoge(View.OnClickListener listener, String heading, String text, int image) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(R.layout.dialge_forgot_password);
        Button btn = (Button) dialog.findViewById(R.id.bn_back);
        btn.setOnClickListener(listener);
        AnyTextView textView = (AnyTextView) dialog.findViewById(R.id.txt_text);
        AnyTextView txtHeading = (AnyTextView) dialog.findViewById(R.id.txt_heading);
        ImageView imageIcon = (ImageView) dialog.findViewById(R.id.image);
        txtHeading.setText(heading);
        textView.setText(text);
        imageIcon.setImageResource(image);
        return this.dialog;
    }

    public Dialog bookingDialoge(View.OnClickListener listener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(R.layout.dialoge_booking);
        Button btn = (Button) dialog.findViewById(R.id.bn_back);
        btn.setOnClickListener(listener);
        return this.dialog;
    }
*/

  /*  public Dialog shareDialoge(View.OnClickListener listener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(R.layout.dialoge_share);
        AnyTextView txtText = (AnyTextView) dialog.findViewById(R.id.txtText);
        AnyTextView txtTwitter = (AnyTextView) dialog.findViewById(R.id.txtTwitter);
        AnyTextView txtMessenger = (AnyTextView) dialog.findViewById(R.id.txtMessenger);
        AnyTextView txtFacebook = (AnyTextView) dialog.findViewById(R.id.txtFacebook);
        AnyTextView txtEmail = (AnyTextView) dialog.findViewById(R.id.txtEmail);
        AnyTextView txtGooglePlus = (AnyTextView) dialog.findViewById(R.id.txtGooglePlus);
        ImageView cross = (ImageView) dialog.findViewById(R.id.cross);

        txtText.setOnClickListener(listener);
        txtTwitter.setOnClickListener(listener);
        txtMessenger.setOnClickListener(listener);
        txtFacebook.setOnClickListener(listener);
        txtEmail.setOnClickListener(listener);
        txtGooglePlus.setOnClickListener(listener);

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        return this.dialog;
    }*/

  /*  public Dialog changeUserDialoge(DockActivity dockActivity) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(R.layout.dialoge_change_user);
        CustomRecyclerView rvChangeUser = (CustomRecyclerView) dialog.findViewById(R.id.rv_change_user);
        ImageView cross = (ImageView) dialog.findViewById(R.id.cross);

        ArrayList<String> collection = new ArrayList<>();
        collection.add("");
        collection.add("");
        collection.add("");
        collection.add("");

     *//*   rvChangeUser.BindRecyclerView(new ChangeUserBinder(dockActivity), collection,
                new LinearLayoutManager(dockActivity, LinearLayoutManager.VERTICAL, false)
                , new DefaultItemAnimator());*//*

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        return this.dialog;
    }


*/



    public Dialog cameraPicker(View.OnClickListener cameraListner, View.OnClickListener galleryListner) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(R.layout.dialoge_camera_options);
        Button btnCamera = (Button) dialog.findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(cameraListner);
        Button btnGallery = (Button) dialog.findViewById(R.id.btn_gallery);
        btnGallery.setOnClickListener(galleryListner);
        return this.dialog;
    }


    public void showDialog() {

        dialog.show();
    }

    public void setCancelable(boolean isCancelable) {
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelable);
    }

    public void hideDialog() {
        dialog.dismiss();
    }
}
