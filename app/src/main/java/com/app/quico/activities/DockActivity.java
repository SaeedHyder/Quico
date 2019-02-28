package com.app.quico.activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.app.quico.BaseApplication;
import com.app.quico.R;
import com.app.quico.fragments.HomeFragment;
import com.app.quico.fragments.ServiceDetailFragment;
import com.app.quico.fragments.SideMenuFragment;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.interfaces.LoadingListener;
import com.app.quico.residemenu.ResideMenu;
import com.app.quico.ui.dialogs.DialogFactory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * This class is marked abstract so that it can pair with Dockable Fragments
 * only. All Classes extending this will inherit this functionality of
 * interaction with menus.
 */
public abstract class DockActivity extends AppCompatActivity implements
        LoadingListener {

    public static final String KEY_FRAG_FIRST = "firstFrag";

    public abstract int getDockFrameLayoutId();

    BaseFragment baseFragment;

    protected BasePreferenceHelper prefHelper;

    public SideMenuFragment sideMenuFragment;

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {

        }

        @Override
        public void closeMenu() {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefHelper = new BasePreferenceHelper(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    public void replaceDockableFragment(BaseFragment frag) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(getDockFrameLayoutId(), frag);
        transaction
                .addToBackStack(
                        getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commitAllowingStateLoss();


    }

    public void replaceDockableFragment(BaseFragment frag, String Tag) {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        transaction.replace(getDockFrameLayoutId(), frag);
        transaction.addToBackStack(getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST : null).commitAllowingStateLoss();


    }

    public void replaceDockableFragment(BaseFragment frag, String Tag, boolean animation) {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(getDockFrameLayoutId(), frag);
        transaction
                .addToBackStack(
                        getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commitAllowingStateLoss();


    }


    public void replaceDockableFragment(BaseFragment frag, boolean isAnimate) {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        // if ( isAnimate )
        // if ( !(frag instanceof DashboardFragment) ) {
        // // transaction.setCustomAnimations( R.anim.push_right_in,
        // // R.anim.push_right_out, R.anim.push_left_in,
        // // R.anim.push_left_out );
        // }

        transaction.replace(getDockFrameLayoutId(), frag);
        transaction
                .addToBackStack(
                        getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();
    }

    public void addDockableFragment(BaseFragment frag, String Tag) {

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.add(getDockFrameLayoutId(), frag);
        transaction
                .addToBackStack(
                        getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commitAllowingStateLoss();


    }


    public void addAndShowDialogFragment(
            android.support.v4.app.DialogFragment dialog) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        dialog.show(transaction, "tag");

    }

    public void prepareAndShowDialog(DialogFragment frag, String TAG,
                                     BaseFragment fragment) {
        FragmentTransaction transaction = fragment.getChildFragmentManager()
                .beginTransaction();
        Fragment prev = fragment.getChildFragmentManager().findFragmentByTag(
                TAG);

        if (prev != null)
            transaction.remove(prev);

        transaction.addToBackStack(null);
        frag.show(transaction, TAG);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else
            DialogFactory.createQuitDialog(this,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DockActivity.this.finish();

                        }
                    }, R.string.message_quit).show();
    }

    public BaseFragment getLastAddedFragment() {
        return baseFragment;
    }

    public void emptyBackStack() {
        //popBackStackTillEntry( 0 );
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(i);
            if (entry != null && (!(entry instanceof HomeFragment)) && (!(entry instanceof SideMenuFragment))) {
                getSupportFragmentManager().popBackStack(entry.getId(),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

        }
    }

    /**
     * @param entryIndex is the index of fragment to be popped to, for example the
     *                   first fragment will have a index 0;
     */
    public void popBackStackTillEntry(int entryIndex) {
        if (getSupportFragmentManager() == null)
            return;
        if (getSupportFragmentManager().getBackStackEntryCount() <= entryIndex)
            return;
        BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(
                entryIndex);
        if (entry != null) {
            try {
                getSupportFragmentManager().popBackStack(entry.getId(),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } catch (IllegalStateException ignored) {
                // There's no way to avoid getting this if saveInstanceState has already been called.
            }

        }
    }

    public void popFragment() {
        if (getSupportFragmentManager() == null)
            return;
        getSupportFragmentManager().popBackStack();
    }


    public void UpdateCompantData() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        }
        FragmentManager manager = getDockActivity().getSupportFragmentManager();
        if (manager != null) {
            Fragment currFrag = manager.findFragmentById(getDockActivity().getDockFrameLayoutId());
            if (currFrag != null) {
                if (currFrag instanceof ServiceDetailFragment) {
                    ((ServiceDetailFragment) currFrag).updateReviews();
                }
            }
        }
    }

    public abstract void onMenuItemActionCalled(int actionId, String data);

    public abstract void setSubHeading(String subHeadText);

    public abstract boolean isLoggedIn();

    public abstract void hideHeaderButtons(boolean leftBtn, boolean rightBtn);

    public BaseApplication getMainApplication() {
        return (BaseApplication) getApplication();
    }

    public ResideMenu.OnMenuListener getMenuListener() {
        return menuListener;
    }

    public DisplayImageOptions getImageLoaderRoundCornerTransformation(int raduis) {
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.placeholder_thumb)
                .showImageOnFail(R.drawable.placeholder_thumb).resetViewBeforeLoading(true)
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .displayer(new RoundedBitmapDisplayer(raduis))
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    public DockActivity getDockActivity() {
        return (DockActivity) this;
    }

    public String getCurrentAddress(double lat, double lng) {
        try {


            Geocoder geocoder;
            List<Address> addresses;
            String address = "";
            String country="";

            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            }
// String city = addresses.get(0).getLocality();
// String state = addresses.get(0).getAdminArea();
            if (addresses.size() > 0) {
                country = addresses.get(0).getCountryName();
            }
// String postalCode = addresses.get(0).getPostalCode();
// String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            return address + ", " + country;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
