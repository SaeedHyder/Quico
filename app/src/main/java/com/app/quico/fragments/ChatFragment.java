package com.app.quico.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.quico.R;
import com.app.quico.entities.AttachmentEnt;
import com.app.quico.entities.Chat.ChatThreadEnt;
import com.app.quico.entities.Chat.DocumentDetail;
import com.app.quico.entities.Chat.ThreadMsgesEnt;
import com.app.quico.entities.CompanyDetail;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.global.AppConstants;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.AttachmentInterface;
import com.app.quico.interfaces.ChatAttachmentInterface;
import com.app.quico.interfaces.LoadMoreListener;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.AttachmentBinder;
import com.app.quico.ui.binders.ChatBinder;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.xw.repo.XEditText;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;
import droidninja.filepicker.utils.Orientation;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.app.quico.global.AppConstants.PDF;
import static com.app.quico.global.AppConstants.PHOTO;
import static com.app.quico.global.AppConstants.chatPush;
import static com.app.quico.global.WebServiceConstants.ChatThreadsPaging;
import static com.app.quico.global.WebServiceConstants.SendLocationMsg;
import static com.app.quico.global.WebServiceConstants.SendMessage;
import static com.app.quico.global.WebServiceConstants.ThreadDetail;
import static com.app.quico.global.WebServiceConstants.ThreadMsges;
import static droidninja.filepicker.FilePickerConst.REQUEST_CODE_DOC;
import static droidninja.filepicker.FilePickerConst.REQUEST_CODE_PHOTO;

public class ChatFragment extends BaseFragment implements RecyclerClickListner, AttachmentInterface, ChatAttachmentInterface {
    private static final String TAG = "ChatFragment";
    private static final String LIST_STATE_KEY = "LIST_STATE_KEY";
    @BindView(R.id.rv_chat)
    CustomRecyclerView rvChat;
    Unbinder unbinder;
    @BindView(R.id.btn_attachMedia)
    AnyTextView btnAttachMedia;
    @BindView(R.id.btn_attachDocument)
    AnyTextView btnAttachDocument;
    @BindView(R.id.btn_attachLocation)
    AnyTextView btnAttachLocation;
    @BindView(R.id.ll_attachment)
    LinearLayout llAttachment;
    @BindView(R.id.edt_message)
    XEditText edtMessage;
    @BindView(R.id.btnAttachment)
    ImageView btnAttachment;
    @BindView(R.id.btnSend)
    ImageView btnSend;
    @BindView(R.id.rv_attachments)
    CustomRecyclerView rvAttachments;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;


    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private int MAX_ATTACHMENT_COUNT = 5;

    private ArrayList<String> photoPaths = new ArrayList<>();
    private ArrayList<String> docPaths = new ArrayList<>();
    private ArrayList<AttachmentEnt> allAttachments = new ArrayList<>();

    private static String threadId;
    private static String companyId;
    private static String companyDetailKey = "companyDetailKey";
    private ChatThreadEnt entity;
    private CompanyDetail companyDetail;

    private LinearLayoutManager linearLayoutManager;
    boolean canCallForMore = true;
    boolean isOnCall;
    int currentPageNumber = 0;
    int totalCount = 20;
    boolean firstTime = true;
    protected BroadcastReceiver broadcastReceiver;


    public static ChatFragment newInstance(String id) {
        Bundle args = new Bundle();
        threadId = id;
        companyId = "";
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ChatFragment newInstance(String Id, CompanyDetail detail) {
        Bundle args = new Bundle();
        threadId = "";
        companyId = Id;
        args.putString(companyDetailKey, new Gson().toJson(detail));
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDockActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (getArguments() != null) {
            String jsonString = getArguments().getString(companyDetailKey);
            if (jsonString != null) {
                companyDetail = new Gson().fromJson(jsonString, CompanyDetail.class);
            }
        }
        if (companyId != null && !companyId.isEmpty() && !companyId.equals("")) {
            entity = new ChatThreadEnt();
            entity.setCompanyId(Integer.parseInt(companyId));
            if (companyDetail != null && companyDetail.getName() != null && !companyDetail.getName().equals("") && !companyDetail.getName().isEmpty()) {
                entity.setCompanyDetail(companyDetail);
            }

            if (getTitleBar() != null) {
                if (entity != null && entity.getCompanyDetail() != null && entity.getCompanyDetail().getName() != null) {
                    getTitleBar().setSubHeading(entity.getCompanyDetail().getName());
                } else {
                    getTitleBar().setSubHeading("");
                }
            }

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onNotificationReceived();

        currentPageNumber = 0;
        firstTime = true;
        canCallForMore = true;
        setAttachmentData();

        if (threadId != null && !threadId.isEmpty() && !threadId.equals("")) {
            rvChat.setVisibility(View.VISIBLE);
            txtNoData.setVisibility(View.GONE);

            serviceHelper.enqueueCall(headerWebService.getThreadDetail(threadId), ThreadDetail);
            serviceHelper.enqueueCall(headerWebService.getThreadMsges(threadId, currentPageNumber, totalCount), ThreadMsges);
        } else {
            rvChat.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
        }

        getDockActivity().getSupportFragmentManager().addOnBackStackChangedListener(getListener());


    }


    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getDockActivity().getSupportFragmentManager();

                if (manager != null) {
                    BaseFragment currFrag = (BaseFragment) manager.findFragmentById(getDockActivity().getDockFrameLayoutId());
                    if (currFrag != null) {
                        if (currFrag instanceof ChatFragment) {
                            if (getTitleBar() != null) {
                                if (entity != null && entity.getCompanyDetail() != null && entity.getCompanyDetail().getName() != null) {

                                    getTitleBar().setSubHeading(entity.getCompanyDetail().getName());
                                } else {
                                    getTitleBar().setSubHeading("");
                                }
                            }
                        }
                    }

                }
            }
        };

        return result;
    }

    private void setAttachmentData() {

        allAttachments = new ArrayList<>();

        rvAttachments.BindRecyclerView(new AttachmentBinder(getDockActivity(), prefHelper, this, this), allAttachments,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false)
                , new DefaultItemAnimator());

    }

    private void updateAttachmentData() {


        if (photoPaths != null && docPaths != null && (photoPaths.size() > 0 || docPaths.size() > 0)) {
            allAttachments = new ArrayList<>();

            for (String item : photoPaths) {
                try {
                    allAttachments.add(new AttachmentEnt(PHOTO, new Compressor(getDockActivity()).compressToBitmap(new File(item)), item));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (String item : docPaths) {
                allAttachments.add(new AttachmentEnt(PDF, item));
            }

            rvAttachments.clearList();
            rvAttachments.addAll(allAttachments);
            rvAttachments.notifyDataSetChanged();

        } else {
            rvAttachments.setVisibility(View.GONE);
        }

    }


    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag) {

            case SendLocationMsg:
                edtMessage.getText().clear();
                llAttachment.setVisibility(View.GONE);
                rvAttachments.setVisibility(View.GONE);
                docPaths = new ArrayList<>();
                photoPaths = new ArrayList<>();
                allAttachments = new ArrayList<>();

                UIHelper.showShortToastInCenter(getDockActivity(), "Location sent successfully");

                ThreadMsgesEnt locationData = (ThreadMsgesEnt) result;
                if (rvChat.getList() != null && rvChat.getList().size() > 0) {
                    rvChat.addStart(locationData);
                } else {
                    firstTime = true;
                    serviceHelper.enqueueCall(headerWebService.getThreadMsges(locationData.getThreadId() + "", currentPageNumber, totalCount), ThreadMsges);
                }

                break;

            case SendMessage:
                edtMessage.getText().clear();
                llAttachment.setVisibility(View.GONE);
                rvAttachments.setVisibility(View.GONE);
                docPaths = new ArrayList<>();
                photoPaths = new ArrayList<>();
                allAttachments = new ArrayList<>();

                ThreadMsgesEnt messageData = (ThreadMsgesEnt) result;
                if (rvChat.getList() != null && rvChat.getList().size() > 0) {
                    rvChat.addStart(messageData);
                } else {
                    firstTime = true;
                    serviceHelper.enqueueCall(headerWebService.getThreadMsges(messageData.getThreadId() + "", currentPageNumber, totalCount), ThreadMsges);

                }

                UIHelper.showShortToastInCenter(getDockActivity(), "Message sent successfully");
                break;

            case ThreadDetail:
                ChatThreadEnt threaDetail = (ChatThreadEnt) result;
                entity = threaDetail;
                prefHelper.setChatThreadId(entity.getId() + "");
                if (getTitleBar() != null) {
                    if (entity != null && entity.getCompanyDetail() != null && entity.getCompanyDetail().getName() != null) {
                        getTitleBar().setSubHeading(entity.getCompanyDetail().getName());
                    } else {
                        getTitleBar().setSubHeading("");
                    }
                }
                break;

            case ThreadMsges:
                ArrayList<ThreadMsgesEnt> data = (ArrayList<ThreadMsgesEnt>) result;


                if (data != null && data.size() > 0) {

                    rvChat.setVisibility(View.VISIBLE);
                    txtNoData.setVisibility(View.GONE);

                    if (firstTime) {
                        linearLayoutManager = new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, true);
                        rvChat.clearList();
                        rvChat.BindRecyclerView(new ChatBinder(getDockActivity(), prefHelper, this, this), data, linearLayoutManager, new DefaultItemAnimator());
                        rvChat.setNestedScrollingEnabled(false);
                        firstTime = false;
                    } else {

                        rvChat.addAllStart(data);
                        rvChat.notifyItemRangeChanged(linearLayoutManager.findLastVisibleItemPosition(), rvChat.getList().size());
                    }

                    onLoadMoreListner();

                } else {
                    rvChat.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
                }


                setTouchListner(0);
                break;

            case ChatThreadsPaging:

                ArrayList<ThreadMsgesEnt> dataPaging = (ArrayList<ThreadMsgesEnt>) result;

                isOnCall = false;
                if (dataPaging.size() > 0) {
                    rvChat.addAllStart(dataPaging);
                    rvChat.notifyItemRangeChanged(linearLayoutManager.findLastVisibleItemPosition(), rvChat.getList().size());

                } else {
                    canCallForMore = false;
                }
                break;


        }
    }

    private void onLoadMoreListner() {

        if (rvChat.getAdapter() != null) {
            rvChat.getAdapter().setOnLoadMoreListener(new LoadMoreListener() {
                @Override
                public void onLoadMoreItem(int position) {
                    if (canCallForMore) {
                        //  if (!isOnCall) {
                        currentPageNumber = currentPageNumber + 1;
                        isOnCall = true;
                        serviceHelper.enqueueCall(headerWebService.getThreadMsges(threadId, currentPageNumber, totalCount), ChatThreadsPaging);
                        // }
                    }
                }
            });
        }
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();

    }

    @Override
    public void onClick(Object entity, int position) {

    }


    @OnClick({R.id.btn_attachMedia, R.id.btn_attachDocument, R.id.btn_attachLocation, R.id.btnAttachment, R.id.btnSend, R.id.edt_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_attachMedia:
                requestCameraPermission();

                break;
            case R.id.btn_attachDocument:
                requestExternalPermission();

                break;
            case R.id.btn_attachLocation:
                requestLocationPermission();
                break;
            case R.id.btnAttachment:
            /*    Animation anim = AnimationUtils.loadAnimation(getDockActivity(), R.anim.scale_up);
                llAttachment.setAnimation(anim);*/
                llAttachment.setVisibility(View.VISIBLE);

                break;
            case R.id.btnSend:

                if (allAttachments != null && allAttachments.size() > 0) {
                    sendMediaMsg();
                } else {
                    if (isValidate())
                        sendMessageService();
                }
                break;

            case R.id.edt_message:

                break;
        }
    }

    private void sendMediaMsg() {

        ArrayList<MultipartBody.Part> files = new ArrayList<>();
        ArrayList<MultipartBody.Part> thumbnails = new ArrayList<>();
        ArrayList<MultipartBody.Part> type = new ArrayList<>();


        for (AttachmentEnt item : allAttachments) {

            if (item.getAttahcment() != null && item.getType()!=null) {
                files.add(MultipartBody.Part.createFormData("file[]",
                        item.getAttahcment(), RequestBody.create(MediaType.parse("multipart/form-data"),
                                new File(item.getAttahcment()))));

                thumbnails.add(MultipartBody.Part.createFormData("thumb_nail[]",
                        item.getAttahcment(), RequestBody.create(MediaType.parse("multipart/form-data"),
                                new File(item.getAttahcment()))));

                type.add(MultipartBody.Part.createFormData("type[]", item.getType()));
            }

        }

        if (entity != null) {
            RequestBody company_id = RequestBody.create(MediaType.parse("text/plain"), entity.getCompanyId() != null ? entity.getCompanyId() + "" : "");
            RequestBody receiver_id = RequestBody.create(MediaType.parse("text/plain"), entity.getCompanyDetail().getUserId() != null ? entity.getCompanyDetail().getUserId() + "" + "" : "");
            RequestBody message = RequestBody.create(MediaType.parse("text/plain"), edtMessage.getText().toString());

            serviceHelper.enqueueCall(headerWebService.sendMediaMessage(company_id, receiver_id, message, files, thumbnails, type), SendMessage);
        } else {
            UIHelper.showShortToastInCenter(getDockActivity(), "Reload page please....");
        }

    }


    private void setTouchListner(final int position) {
        edtMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvChat.scrollToPosition(position);
                        }
                    }, 1000);
                }
                return false;
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getDockActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void requestLocationPermission() {
        Dexter.withActivity(getDockActivity())
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {

                            openLocationSelector();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            requestLocationPermission();

                        } else if (report.getDeniedPermissionResponses().size() > 0) {
                            requestLocationPermission();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "Grant LocationEnt Permission to processed");
                        openSettings();
                    }
                })

                .onSameThread()
                .check();


    }

    private void requestCameraPermission() {
        Dexter.withActivity(getDockActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            onPickPhoto();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            requestCameraPermission();

                        } else if (report.getDeniedPermissionResponses().size() > 0) {
                            requestCameraPermission();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "Grant Camera And Storage Permission to processed");
                        openSettings();
                    }
                })

                .onSameThread()
                .check();


    }

    private void requestExternalPermission() {
        Dexter.withActivity(getDockActivity())
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            onPickDoc();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            requestCameraPermission();

                        } else if (report.getDeniedPermissionResponses().size() > 0) {
                            requestCameraPermission();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "Grant Storage Permission to processed");
                        openSettings();
                    }
                })

                .onSameThread()
                .check();


    }

    private void openSettings() {

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        Uri uri = Uri.fromParts("package", getDockActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void openLocationSelector() {

        try {
           /* Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(getDockActivity());*/
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(getDockActivity()), PLACE_AUTOCOMPLETE_REQUEST_CODE);
            //this.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public void onPickPhoto() {

        String[] images = {".jpg,.png,.jpeg"};
        int maxCount = MAX_ATTACHMENT_COUNT - docPaths.size();
        if ((photoPaths.size() + docPaths.size()) == MAX_ATTACHMENT_COUNT) {
            Toast.makeText(getDockActivity(), "Cannot select more than " + MAX_ATTACHMENT_COUNT + " items", Toast.LENGTH_SHORT).show();
            llAttachment.setVisibility(View.GONE);
        } else {
            FilePickerBuilder.getInstance()
                    .setMaxCount(maxCount)
                    .setSelectedFiles(photoPaths)
                    .setActivityTheme(R.style.LibAppTheme)
                    .setActivityTitle("Please select media")
                    .enableVideoPicker(false)
                    .addFileSupport("Images", images)
                    .enableCameraSupport(true)
                    .showGifs(false)
                    .showFolderView(false)
                    .enableSelectAll(false)
                    .enableImagePicker(true)
                    .setCameraPlaceholder(R.drawable.custom_camera)
                    .withOrientation(Orientation.UNSPECIFIED)
                    .pickPhoto(this, REQUEST_CODE_PHOTO);
        }

    }

    public void onPickDoc() {
        String[] pdfs = {".pdf"};

        int maxCount = MAX_ATTACHMENT_COUNT - photoPaths.size();
        if ((docPaths.size() + photoPaths.size()) == MAX_ATTACHMENT_COUNT) {
            Toast.makeText(getDockActivity(), "Cannot select more than " + MAX_ATTACHMENT_COUNT + " items",
                    Toast.LENGTH_SHORT).show();
            llAttachment.setVisibility(View.GONE);
        } else {
            FilePickerBuilder.getInstance()
                    .setMaxCount(maxCount)
                    .setSelectedFiles(docPaths)
                    .enableVideoPicker(false)
                    .enableCameraSupport(false)
                    .showGifs(false)
                    .setActivityTheme(R.style.LibAppTheme)
                    .setActivityTitle("Please select doc")
                    .addFileSupport("PDF", pdfs, R.drawable.pdf_blue)
                    .enableDocSupport(false)
                    .enableSelectAll(false)
                    .sortDocumentsBy(SortingTypes.name)
                    .withOrientation(Orientation.UNSPECIFIED)
                    .pickFile(this, REQUEST_CODE_DOC);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    llAttachment.setVisibility(View.GONE);
                    rvAttachments.setVisibility(View.VISIBLE);
                    photoPaths = new ArrayList<>();
                    photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));


                    updateAttachmentData();


                }
                break;

            case REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    llAttachment.setVisibility(View.GONE);
                    rvAttachments.setVisibility(View.VISIBLE);
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));

                    updateAttachmentData();

                }

                break;

        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(getDockActivity(), data);
                if (place != null) {
                    llAttachment.setVisibility(View.GONE);
                    sendLocationService(place);
                    Log.i(TAG, "Place: " + place.getName());
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getDockActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }

    private void sendLocationService(Place place) {

        if (entity != null) {
            serviceHelper.enqueueCall(headerWebService.sendLocation(entity.getCompanyId() != null ? entity.getCompanyId() + "" : "", entity.getCompanyDetail().getUserId() != null ? entity.getCompanyDetail().getUserId() + "" : "",
                    place.getLatLng().latitude + "", place.getLatLng().longitude + ""), SendLocationMsg);
        } else {
            UIHelper.showShortToastInCenter(getDockActivity(), "Reload page please....");
        }

    }

    private void sendMessageService() {

        if (entity != null) {
            serviceHelper.enqueueCall(headerWebService.sendMessage(entity.getCompanyId() != null ? entity.getCompanyId() + "" : "",
                    entity.getCompanyDetail().getUserId() != null ? entity.getCompanyDetail().getUserId() + "" : "", edtMessage.getText().toString()), SendMessage);
        } else {
            UIHelper.showShortToastInCenter(getDockActivity(), "Reload page please....");
        }


    }

    private boolean isValidate() {
        if (edtMessage.getText().toString() == null || edtMessage.getText().toString().equals("") || edtMessage.getText().toString().isEmpty()) {
            edtMessage.setError(getString(R.string.write_something_here));
            if (edtMessage.requestFocus()) {
                setEditTextFocus(edtMessage);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onCrossClick(Object entity, int position) {

        AttachmentEnt attachmentEnt = (AttachmentEnt) entity;

        if (attachmentEnt.getType().equals(PDF)) {
            docPaths.remove(attachmentEnt.getAttahcment());
        } else {
            photoPaths.remove(position);
        }
        updateAttachmentData();
    }

    @Override
    public void onImageClick(Object entity, int position) {
        ThreadMsgesEnt data = (ThreadMsgesEnt) entity;
        ArrayList<DocumentDetail> image = new ArrayList<>();

        for (DocumentDetail item : data.getDocumentDetail()) {
            if (item.getType().equals(AppConstants.photo)) {
                image.add(item);
            }
        }

        if (image != null && image.size() > 0) {
            UIHelper.hideSoftKeyboard(getDockActivity(), edtMessage);
            getDockActivity().addDockableFragment(ChatImageViewerFragment.newInstance(image), "ProjectImagesFragment");
        }
    }

    @Override
    public void onFileCLick(Object entity, int position) {
        ThreadMsgesEnt data = (ThreadMsgesEnt) entity;

        ArrayList<DocumentDetail> pdfFiles = new ArrayList<>();

        for (DocumentDetail item : data.getDocumentDetail()) {
            if (item.getType().equals(AppConstants.PDF)) {
                pdfFiles.add(item);
            }
        }

        if (pdfFiles != null && pdfFiles.size() > 0) {
            UIHelper.hideSoftKeyboard(getDockActivity(), edtMessage);
            getDockActivity().addDockableFragment(PdfGalleryFragement.newInstance(pdfFiles), "PdfReaderFragment");
        }
    }

    @Override
    public void onLocationClick(Object entity, int position) {
        ThreadMsgesEnt data = (ThreadMsgesEnt) entity;

        if (data.getLatitude() != null && data.getLongitude() != null) {
            String geoUri = "http://maps.google.com/maps?q=loc:" + data.getLatitude() + "," + data.getLongitude();
            Intent intentAddress = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            getDockActivity().startActivity(intentAddress);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.PUSH_NOTIFICATION));


    }

    @Override
    public void onPause() {

        LocalBroadcastManager.getInstance(getDockActivity()).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    private void onNotificationReceived() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(AppConstants.PUSH_NOTIFICATION)) {
                    Bundle bundle = intent.getExtras();

                    if (bundle != null) {
                        String Type = bundle.getString("actionType");
                        String id = bundle.getString("redId");

                        if (Type != null && Type.equals(chatPush)) {
                            firstTime = true;
                            serviceHelper.enqueueCall(headerWebService.getThreadMsges(id, 0, totalCount), ThreadMsges, false);
                        }
                    }
                }
            }
        };
    }


}
