package com.app.quico.fragments;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.app.quico.R;
import com.app.quico.entities.UpdateProfileResponse;
import com.app.quico.entities.UserEnt;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.CameraHelper;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.ImageSetter;
import com.app.quico.ui.views.AutoCompleteLocation;
import com.app.quico.ui.views.TitleBar;
import com.google.android.gms.location.places.Place;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.app.quico.global.WebServiceConstants.UpdateProfileService;

public class EditProfileFragment extends BaseFragment implements ImageSetter {
    private static final String TAG = "EditProfileFragment";
    @BindView(R.id.edt_username)
    TextInputEditText edtUsername;
    @BindView(R.id.edt_email)
    TextInputEditText edtEmail;
    @BindView(R.id.edt_phone)
    TextInputEditText edtPhone;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    Unbinder unbinder;
    @BindView(R.id.edt_address)
    AutoCompleteLocation edtAddress;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.btnUploadImage)
    RelativeLayout btnUploadImage;
    @BindView(R.id.Countrypicker)
    CountryCodePicker Countrypicker;


    private PhoneNumberUtil phoneUtil;
    private ImageLoader imageLoader;
    private File profilePic;
    private String profilePath;
    private Double latitude;
    private Double longitude;
    private String Address;

    public static EditProfileFragment newInstance() {
        Bundle args = new Bundle();

        EditProfileFragment fragment = new EditProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (prefHelper.isSocialLogin()) {
            edtPhone.setFocusable(true);
            edtPhone.setFocusableInTouchMode(true);
            edtEmail.setFocusable(false);
            edtEmail.setFocusableInTouchMode(false);

        } else {
            edtPhone.setFocusable(false);
            edtPhone.setFocusableInTouchMode(false);
            Countrypicker.setCcpClickable(false);
            edtEmail.setFocusable(true);
            edtEmail.setFocusableInTouchMode(true);
        }
        phoneUtil = PhoneNumberUtil.getInstance();
        edtPhone.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        getMainActivity().setImageSetter(this);
        autoCompleteListner();
        setData();

    }

    private void autoCompleteListner() {

        edtAddress.setAutoCompleteTextListener(new AutoCompleteLocation.AutoCompleteLocationListener() {
            @Override
            public void onTextClear() {

            }

            @Override
            public void onItemSelected(Place selectedPlace) {
                latitude = selectedPlace.getLatLng().latitude;
                longitude = selectedPlace.getLatLng().longitude;
                Address = selectedPlace.getAddress().toString();
            }
        });
    }

    private void setData() {

        if (prefHelper.getUser() != null && prefHelper.getUser().getUser() != null) {
            edtUsername.setText(prefHelper.getUser().getUser().getName() + "");
            edtEmail.setText(prefHelper.getUser().getUser().getEmail() + "");

            if (prefHelper.getUser().getUser().getCountryCode() != null && !prefHelper.getUser().getUser().getCountryCode().equals("") &&
                    prefHelper.getUser().getUser().getPhone() != null && !prefHelper.getUser().getUser().getPhone().equals("")) {
                Countrypicker.setCountryForPhoneCode(Integer.parseInt(prefHelper.getUser().getUser().getCountryCode()));
                edtPhone.setText(prefHelper.getUser().getUser().getPhone() + "");
            }

            if (prefHelper.getUser().getUser().getDetails().getImageUrl() != null && !prefHelper.getUser().getUser().getDetails().getImageUrl().equals("")) {
                profilePath = prefHelper.getUser().getUser().getDetails().getImageUrl();
                //   imageLoader.displayImage(prefHelper.getUser().getUser().getDetails().getImageUrl(), profileImage);
                Picasso.get().load(prefHelper.getUser().getUser().getDetails().getImageUrl()).placeholder(R.drawable.placeholder).into(profileImage);
            }
            if (prefHelper.getUser().getUser().getDetails().getAddress() != null && !prefHelper.getUser().getUser().getDetails().getAddress().equals("") && !prefHelper.getUser().getUser().getDetails().getAddress().equals("null")) {
                edtAddress.setText(prefHelper.getUser().getUser().getDetails().getAddress());
                latitude = Double.valueOf(prefHelper.getUser().getUser().getDetails().getLatitude());
                longitude = Double.valueOf(prefHelper.getUser().getUser().getDetails().getLongitude());
            }
        }
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.edit_profile));
    }


    private boolean isvalidated() {
        if (edtUsername.getText().toString().trim().isEmpty() || edtUsername.getText().toString().trim().length() < 3) {
            edtUsername.setError(getString(R.string.enter_username));
            if (edtUsername.requestFocus()) {
                setEditTextFocus(edtUsername);
            }
            return false;
        } else if (!prefHelper.isSocialLogin() && (edtEmail.getText() == null || edtEmail.getText().toString().trim().isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches())) {
            edtEmail.setError(getString(R.string.enter_valid_email));
            if (edtEmail.requestFocus()) {
                setEditTextFocus(edtEmail);
            }
            return false;
        } else if (edtAddress.getText().toString().trim().isEmpty() || edtAddress.getText().toString().trim().length() < 3) {
            edtAddress.setError(getString(R.string.enter_address));
            if (edtAddress.requestFocus()) {
                setEditTextFocus(edtAddress);
            }
            return false;
        } else if (edtPhone.getText().toString().trim().isEmpty() || edtPhone.getText().toString().length() < 3) {
            edtPhone.setError(getString(R.string.enter_phonenumber));
            if (edtPhone.requestFocus()) {
                setEditTextFocus(edtPhone);
            }
            return false;
        } else if (prefHelper.isSocialLogin() && !isPhoneNumberValid()) {
            edtPhone.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
            //updateHint();
            if (edtPhone.requestFocus()) {
                setEditTextFocus(edtPhone);
            }
            return false;
        } else if (profilePath == null) {
            UIHelper.showShortToastInDialoge(getDockActivity(), getString(R.string.upload_image));
            return false;
        } else
            return true;

    }


    @OnClick({R.id.btnUploadImage, R.id.btn_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnUploadImage:
                requestCameraPermission();
                break;
            case R.id.btn_edit:
                if (isvalidated()) {
                    MultipartBody.Part filePart;
                    if (profilePic != null) {
                        filePart = MultipartBody.Part.createFormData("image",
                                profilePic.getName(), RequestBody.create(MediaType.parse("image/*"), profilePic));
                    } else {
                        filePart = MultipartBody.Part.createFormData("image", "",
                                RequestBody.create(MediaType.parse("*/*"), ""));
                    }
                    serviceHelper.enqueueCall(headerWebService.updateProfile(
                            RequestBody.create(MediaType.parse("text/plain"), prefHelper.getUser().getUser().getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), edtUsername.getText().toString()),
                            RequestBody.create(MediaType.parse("text/plain"), edtEmail.getText().toString()),
                            RequestBody.create(MediaType.parse("text/plain"), Countrypicker.getSelectedCountryCodeWithPlus().toString()),
                            RequestBody.create(MediaType.parse("text/plain"), edtPhone.getText().toString()),
                            RequestBody.create(MediaType.parse("text/plain"), edtAddress.getText().toString()),
                            RequestBody.create(MediaType.parse("text/plain"), latitude != null ? String.valueOf(latitude) : "0.0"),
                            RequestBody.create(MediaType.parse("text/plain"), longitude != null ? String.valueOf(longitude) : "0.0"),
                            filePart
                    ), UpdateProfileService);

                  /*  UpdateProfile updateProfile = new UpdateProfile(prefHelper.getUser().getUser().getId() + "", edtUsername.getText().toString(), edtEmail.getText().toString()
                            , prefHelper.getUser().getUser().getCountryCode(), prefHelper.getUser().getUser().getPhone(), edtAddress.getText().toString(), latitude != null ? String.valueOf(latitude) : "0.0"
                            , longitude != null ? String.valueOf(longitude) : "0.0", profilePath);

                    serviceHelper.enqueueCall(headerWebService.updateProfile(prefHelper.getUser().getUser().getId() + "", updateProfile), UpdateProfileService);*/

                }
                break;
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag) {
            case UpdateProfileService:
                UpdateProfileResponse entity = (UpdateProfileResponse) result;
                UserEnt userEnt = prefHelper.getUser();
                userEnt.getUser().setName(entity.getName() + "");
                userEnt.getUser().setEmail(entity.getEmail() + "");
                userEnt.getUser().setPhone(entity.getPhone() + "");
                userEnt.getUser().setCountryCode(entity.getCountryCode() + "");
                userEnt.getUser().getDetails().setAddress(entity.getDetails().getAddress() + "");
                userEnt.getUser().getDetails().setLatitude(entity.getDetails().getLatitude() + "");
                userEnt.getUser().getDetails().setLongitude(entity.getDetails().getLongitude() + "");
                userEnt.getUser().getDetails().setImage(entity.getDetails().getImage() + "");
                userEnt.getUser().getDetails().setImageUrl(entity.getDetails().getImageUrl() + "");

                prefHelper.putUser(userEnt);
                UIHelper.showShortToastInCenter(getDockActivity(),getResString(R.string.profile_updated_successfully));
                getDockActivity().popFragment();
                break;
        }
    }

    @Override
    public void setImage(String imagePath) {
        try {
            profilePic = new Compressor(getDockActivity()).compressToFile(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        profilePath = imagePath;
        imageLoader.displayImage("file:///" + imagePath, profileImage);

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
                            CameraHelper.uploadPhotoDialog(getMainActivity());
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

    private boolean isPhoneNumberValid() {

        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(edtPhone.getText().toString(), Countrypicker.getSelectedCountryNameCode());
            if (phoneUtil.isValidNumber(number)) {
                return true;
            } else {

                return false;
            }
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
            edtPhone.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
            return false;

        }
    }



}
