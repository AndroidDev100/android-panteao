package panteao.make.ready.adapters.commonRails;

import android.content.Context;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.BuildConfig;
import panteao.make.ready.R;
import panteao.make.ready.databinding.RowDfpBannerBinding;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;


public class DfpBannerAdapter extends RecyclerView.Adapter<DfpBannerAdapter.ViewHolder> {

    private final Context mContext;
    private final RailCommonData item;
    private final String deviceId;
    private AdManagerAdRequest adRequest;
    private final String adsType;
    final int height;
    final int width;

    public DfpBannerAdapter(Context context, RailCommonData item, String adsType,int heigh,int wdth) {
        this.mContext = context;
        this.item = item;
        this.adsType = adsType;
        height=heigh;
        width=wdth;
        String android_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceId = md5(android_id).toUpperCase();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RowDfpBannerBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_dfp_banner, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.rowDfpBannerBinding.bannerRoot.setVisibility(View.GONE);
        try {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (holder).rowDfpBannerBinding.adMobView.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            RelativeLayout adContainer = holder.rowDfpBannerBinding.adMobView;
            AdManagerAdView adView = new AdManagerAdView(mContext);
            fetchBannerSize(adsType, adView);
            adView.setLayoutParams(params);
            adContainer.addView(adView);
            List<String> testDeviceIds = Arrays.asList(deviceId);

            if (BuildConfig.FLAVOR.equalsIgnoreCase("QA")) {
                RequestConfiguration configuration =
                        new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
                MobileAds.setRequestConfiguration(configuration);
//                AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();

                adRequest.isTestDevice(mContext);
//                adRequest = new AdRequest.Builder().addTestDevice(deviceId).build();
            } else {

                 adRequest = new AdManagerAdRequest.Builder().build();
            }
            adView.loadAd(adRequest);

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    PrintLogging.printLog("AdsLoaded-->" + "success");
                    holder.rowDfpBannerBinding.bannerRoot.setVisibility(View.VISIBLE);
                    // Code to be executed when an ad finishes loading.
                }

                @Override
                public void onAdFailedToLoad(LoadAdError errorCode) {
                    // Code to be executed when an ad request fails.
                    holder.rowDfpBannerBinding.bannerRoot.setVisibility(View.GONE);
                    PrintLogging.printLog("onAdFailedToLoad" + errorCode);

                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

//                @Override
//                public void onAdLeftApplication() {
//                    // Code to be executed when the user has left the app.
//                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }
            });


        } catch (Exception e) {
            PrintLogging.printLog("AdsLoaded-->" + "exception-->"+e.getMessage());

        }
    }


    public void fetchBannerSize(String bannerType, AdManagerAdView adView) {
        AdSize adSize;
        if (bannerType.equalsIgnoreCase(AppConstants.KEY_MREC)) {
            adSize = AdSize.MEDIUM_RECTANGLE;
            adView.setAdUnitId(item.getScreenWidget().getAdID());
            adView.setAdSizes(adSize);
        }
        else if (bannerType.equalsIgnoreCase(AppConstants.KEY_CUS)) {
            adSize=new AdSize(width,height);
            adView.setAdUnitId(item.getScreenWidget().getAdID());
            adView.setAdSizes(adSize);
            /*adSize = AdSize.BANNER;
            adView.setAdUnitId(item.getScreenWidget().getAdID());
            adView.setAdSizes(adSize);*/
        }
        else if (bannerType.equalsIgnoreCase(AppConstants.KEY_BANNER)) {
            adSize = AdSize.BANNER;
            adView.setAdUnitId(item.getScreenWidget().getAdID());
            adView.setAdSizes(adSize);
        } else adSize = AdSize.MEDIUM_RECTANGLE;

    }

    /*private AdSize checkCustomeSize() {
    }*/


    private String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final RowDfpBannerBinding rowDfpBannerBinding;

        ViewHolder(RowDfpBannerBinding squareItemBind) {
            super(squareItemBind.getRoot());
            rowDfpBannerBinding = squareItemBind;

        }

    }


}

