package com.app.quico.fragments.abstracts;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.andreabaccega.formedittextvalidator.Validator;
import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.activities.MainActivity;
import com.app.quico.global.WebServiceConstants;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.helpers.GPSTracker;
import com.app.quico.helpers.ServiceHelper;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.LoadingListener;
import com.app.quico.interfaces.webServiceResponseLisener;
import com.app.quico.retrofit.WebService;
import com.app.quico.retrofit.WebServiceFactory;
import com.app.quico.ui.views.AnyEditTextView;
import com.app.quico.ui.views.TitleBar;
import com.google.i18n.phonenumbers.PhoneNumberUtil;


public abstract class BaseFragment extends Fragment implements webServiceResponseLisener {
	
	protected Handler handler = new Handler();
    private PhoneNumberUtil phoneUtil;


	protected BasePreferenceHelper prefHelper;

	protected  WebService webService;
	protected  WebService headerWebService;
	protected ServiceHelper serviceHelper;

	protected GPSTracker mGpsTracker;

	protected  DockActivity myDockActivity;
	//private DockActivity activity;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		prefHelper = new BasePreferenceHelper(getContext());

		mGpsTracker = new GPSTracker(getDockActivity());

		if (webService == null) {
			webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(getDockActivity(), WebServiceConstants.Local_SERVICE_URL);
		}
		if (headerWebService == null) {
			headerWebService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptorandheader(getDockActivity(), WebServiceConstants.Local_SERVICE_URL);
		}

		if (serviceHelper == null){
			serviceHelper = new ServiceHelper(this,getDockActivity(),webService,prefHelper);
		}

		myDockActivity = getDockActivity();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	//	setTitleBar( ((MainActivity) getDockActivity()).titleBar );
		getMainActivity().closeDrawer();

	}
	public void fragmentResume() {
		setTitleBar(((MainActivity) getDockActivity()).titleBar);

	}
	@Override
	public void onAttach( Activity activity ) {
		super.onAttach( activity );
	}
	
	protected void createClient() {
		// webService = WebServiceFactory.getInstanceWithBasicGsonConversion();
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		if ( getDockActivity().getWindow() != null )
			if ( getDockActivity().getWindow().getDecorView() != null )
				UIHelper.hideSoftKeyboard( getDockActivity(), getDockActivity()
						.getWindow().getDecorView() );
		
	}

	public void loadingStarted() {
		
		if ( getParentFragment() != null )
			((LoadingListener) getParentFragment()).onLoadingStarted();
		else
			getDockActivity().onLoadingStarted();
		
		isLoading = true;
	}


	
	public void loadingFinished() {
		
		if ( getParentFragment() != null )
			((LoadingListener) getParentFragment()).onLoadingFinished();
		else if ( getDockActivity() != null )
			getDockActivity().onLoadingFinished();
		
		isLoading = false;
		// else
		// ( (LoadingListener) super.getParentFragment() ).onLoadingFinished();
	}
	//it will gives us instance of DockActivity
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		myDockActivity = (DockActivity)context;
	}
	@Override
	public void ResponseSuccess(Object result, String Tag, String message) {

	}

	@Override
	public void ResponseFailure(String tag) {

	}
	protected DockActivity getDockActivity() {

		return myDockActivity;
		
	}
	
	protected MainActivity getMainActivity() {
		return (MainActivity) getActivity();
	}
	
	protected TitleBar getTitleBar() {
		if(getMainActivity()!=null && getMainActivity().titleBar!=null){
			return getMainActivity().titleBar;
		}else{
			return null;
		}

	}
	
	public String getTitleName() {
		return "";
	}
	
	/**
	 * This is called in the end to modify titlebar. after all changes.
	 *
	 * @param
	 */
	public void setTitleBar( TitleBar titleBar ) {
		titleBar.showTitleBar();
		// titleBar.refreshListener();
	}
	
	/**
	 * Gets the preferred height for each item in the ListView, in pixels, after
	 * accounting for screen density. ImageLoader uses this value to resize
	 * thumbnail images to match the ListView item height.
	 *
	 * @return The preferred height in pixels, based on the current theme.
	 */
	protected int getListPreferredItemHeight() {
		final TypedValue typedValue = new TypedValue();
		
		// Resolve list item preferred height theme attribute into typedValue
		getActivity().getTheme().resolveAttribute(
				android.R.attr.listPreferredItemHeight, typedValue, true );
		
		// Create a new DisplayMetrics object
		final DisplayMetrics metrics = new android.util.DisplayMetrics();
		
		// Populate the DisplayMetrics
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics( metrics );
		
		// Return theme value based on DisplayMetrics
		return (int) typedValue.getDimension( metrics );
	}
	
	protected String getStringTrimed( AnyEditTextView edtView ) {
		return edtView.getText().toString().trim();
	}
	
	/**
	 * This generic method to add validator to a text view should be used
	 * FormEditText
	 * 
	 * Usage : Takes Array of AnyEditTextView ;
	 * 
	 * @return void
	 */
	protected void addEmptyStringValidator( AnyEditTextView... allFields ) {
		
		for ( AnyEditTextView field : allFields ) {
			field.addValidator( new EmptyStringValidator() );
		}
		
	}
	
	protected void notImplemented() {
		UIHelper.showLongToastInCenter( getActivity(), "Coming Soon" );
	}
	
	protected void serverNotFound() {
		UIHelper.showLongToastInCenter( getActivity(),
				"Unable to connect to the server, "
						+ "are you connected to the internet?" );
	}
	
	/**
	 * This generic null string validator to be used FormEditText
	 * 
	 * Usage : formEditText.addValicator(new EmptyStringValidator);
	 * 
	 * @return Boolean and setError on respective field.
	 */
	protected class EmptyStringValidator extends Validator {
		
		public EmptyStringValidator() {
			super( "The field must not be empty" );
		}
		
		@Override
		public boolean isValid( EditText et ) {
			return et.getText().toString().trim().length() >= 1;
		}
		
	}
	
	/**
	 * Trigger when receives broadcasts from device to check wifi connectivity
	 * using connectivity manager
	 * 
	 * Usage : registerBroadcastReceiver() on resume of activity to receive
	 * notifications where needed and unregisterBroadcastReceiver() when not
	 * needed.
	 * 
	 * @return The connectivity of wifi/mobile carrier connectivity.
	 * 
	 */
	
	protected BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive( Context context, Intent intent ) {
			
			boolean isWifiConnected = false;
			boolean isMobileConnected = false;
			
			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService( Context.CONNECTIVITY_SERVICE );
			
			NetworkInfo networkInfo = connMgr
					.getNetworkInfo( ConnectivityManager.TYPE_WIFI );
			
			if ( networkInfo != null )
				isWifiConnected = networkInfo.isConnected();
			
			networkInfo = connMgr
					.getNetworkInfo( ConnectivityManager.TYPE_MOBILE );
			
			if ( networkInfo != null )
				isMobileConnected = networkInfo.isConnected();
			
			Log.d( "NETWORK STATUS", "wifi==" + isWifiConnected + " & mobile=="
					+ isMobileConnected );
		}
	};
	
	private boolean isLoading;
	
	protected void finishLoading() {
		getActivity().runOnUiThread( new Runnable() {
			
			@Override
			public void run() {
				loadingFinished();
			}
		} );
	}
	
	protected boolean checkLoading() {
		if ( isLoading ) {
			UIHelper.showLongToastInCenter( getActivity(),
					R.string.message_wait );
			return false;
		} else {
			return true;
		}
		
	}
	public String getResString(int id){
		return getDockActivity().getResources().getString(id);
	}

	protected void setEditTextFocus(AnyEditTextView textFocus) {
		InputMethodManager imm = (InputMethodManager) getDockActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null)
			imm.showSoftInput(textFocus, InputMethodManager.SHOW_IMPLICIT);
	}

	protected void setEditTextFocus(EditText textFocus) {
		InputMethodManager imm = (InputMethodManager) getDockActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null)
			imm.showSoftInput(textFocus, InputMethodManager.SHOW_IMPLICIT);
	}





	public class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
		@Override
		public CharSequence getTransformation(CharSequence source, View view) {
			return source;
		}
	}

	protected String getUserId(String senderId, String receiverId, String myId) {
		if (myId.equals(senderId)) {
			return receiverId;
		} else {
			return senderId;
		}
	}

}
