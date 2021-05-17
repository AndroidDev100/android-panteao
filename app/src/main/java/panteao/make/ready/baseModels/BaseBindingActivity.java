package panteao.make.ready.baseModels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.mediarouter.app.MediaRouteButton;

//import com.google.android.gms.cast.framework.CastButtonFactory;
import panteao.make.ready.R;
import panteao.make.ready.SDKConfig;

public abstract class BaseBindingActivity<B extends ViewDataBinding> extends BaseActivity {

    private B mBinding;

    public abstract B inflateBindingLayout(@NonNull LayoutInflater inflater);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = setupBinding(getLayoutInflater());
        setContentView(mBinding.getRoot());

        MediaRouteButton mediaRouteButton = (MediaRouteButton)mBinding.getRoot().findViewById(R.id.media_route_button);
//        CastButtonFactory.setUpMediaRouteButton(getApplication(), mediaRouteButton);

//        ChromecastManager.getInstance().init(getApplicationContext(),this,mediaRouteButton, new CastContextAttachedListner() {
//            @Override
//            public void onDevicesAvailable() {
//                ChromecastManager.getInstance().addCastListener();
//
//                if(mediaRouteButton!=null) {
//                    mediaRouteButton.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onDevicesUnavailable() {
//                if(mediaRouteButton !=null)
//                    mediaRouteButton.setVisibility(View.GONE);
//                // Toast.makeText(getApplicationContext(),"Device Not Available", Toast.LENGTH_LONG).show();
//            }
//        }, new ChromeCastConnectionListener() {
//            @Override
//            public void onSessionStarted() {
//                if(SDKConfig.ApplicationStatus == "disconnected") {
//                    SDKConfig.ApplicationStatus = "connected";
//                }
//            }
//
//            @Override
//            public void onSessionFailed() {
//
//            }
//        });

    }


    public B getBinding() {
        return mBinding;
    }


    private B setupBinding(@NonNull LayoutInflater inflater) {
        return inflateBindingLayout(inflater);
    }


}
