package panteao.make.ready.player.trailor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import panteao.make.ready.R;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.databinding.ActivityPlayerBinding;
import panteao.make.ready.fragments.player.ui.PlayerControlsFragment;
import panteao.make.ready.player.kalturaPlayer.KalturaFragment;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.NetworkConnectivity;

public class PlayerActivity extends BaseBindingActivity<ActivityPlayerBinding> implements KalturaFragment.OnPlayerInteractionListener{

    private KalturaFragment fragment;
   // private RailCommonData railData;
  //  private String assetURL, programName = "";
    //private WindowFocusCallback windowFocusListner;
    private Boolean isLivePlayer = false;
    private String entryId = "";
    long bookmarkPosition = 0l;
    private KalturaFragment playerfragment;
    private PlayerControlsFragment playerControlsFragment;
    boolean fromTrailor=false;

    @Override
    public ActivityPlayerBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityPlayerBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        connectionObserver();
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            setPlayerFragment();
        }
    }


    //initialize player fragment
    private void setPlayerFragment() {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Bundle args = new Bundle();

            if (getIntent().getExtras() != null) {
                entryId = getIntent().getStringExtra(AppConstants.ENTRY_ID);
                if (entryId != "") {
                    args.putString(AppConstants.ENTRY_ID, entryId);
                    args.putLong("bookmark_position",bookmarkPosition);
                    args.putBoolean("from_trailor",true);

                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                playerfragment = new KalturaFragment();
                playerfragment.setArguments(args);
                transaction.replace(R.id.player_root, playerfragment);
                transaction.addToBackStack(null);
                transaction.commit();


            }
    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            ViewGroup.LayoutParams params2 = getBinding().playerLayout.getLayoutParams();
//            params2.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            params2.height = ViewGroup.LayoutParams.MATCH_PARENT;
//            getBinding().playerLayout.requestLayout();
            playerfragment.onConfigurationChanged(newConfig);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            ViewGroup.LayoutParams params2 = getBinding().playerLayout.getLayoutParams();
//            params2.width = 0;
//            params2.height = 0;
//            getBinding().playerLayout.requestLayout();
            playerfragment.onConfigurationChanged(newConfig);

        }

    }


    private void checkAutoRotation() {
        if (android.provider.Settings.System.getInt(getApplicationContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
//        if (fragment!=null) {
//            fragment.exitPlayerOnBackPressed();
//        }
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void bingeWatchCall(String entryID) {

    }

    @Override
    public void onPlayerStart() {

    }

    @Override
    public void onBookmarkCall(int currentPosition) {

    }

    @Override
    public void onBookmarkFinish() {

    }

    @Override
    public void onCurrentPosition(long currentPosition) {

    }
}
