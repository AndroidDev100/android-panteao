package panteao.make.ready.utils.helpers.downloads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kaltura.playkit.PKDrmParams;
import com.kaltura.playkit.PKMediaEntry;
import com.kaltura.playkit.PKMediaSource;
import com.kaltura.tvplayer.KalturaPlayer;
import com.kaltura.tvplayer.OfflineManager;
import com.kaltura.tvplayer.offline.OfflineManagerSettings;

import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import panteao.make.ready.R;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.activities.downloads.SelectDownloadQualityAdapter;
import panteao.make.ready.activities.downloads.VideoQualitySelectedListener;
import panteao.make.ready.databinding.LayoutDownloadQualityBottomSheetBinding;
import panteao.make.ready.utils.constants.SharedPrefesConstants;
import panteao.make.ready.utils.helpers.SharedPrefHelper;

public class KTDownloadHelper {
    Activity zContext;
    private OfflineManager manager;
    KTDownloadEvents ktDownloadEvents;
    public KTDownloadHelper(Activity zContext,KTDownloadEvents ktDownloadEvents) {
        this.zContext=zContext;
        this.ktDownloadEvents=ktDownloadEvents;
        init(zContext);
    }

    private void init(Context zContext) {
        manager = OfflineManager.getInstance(zContext);
        manager.setOfflineManagerSettings(new OfflineManagerSettings().setDefaultHlsAudioBitrateEstimation(64000));
        activateListeners(manager);
        startManager(manager);
    }

    private void startManager(OfflineManager manager) {
        try {
            manager.start(new OfflineManager.ManagerStartCallback() {
                @Override
                public void onStarted() {
                    Log.e("started-->>","Manager start");
                }
            });
        }catch (Exception e){
            Log.e("started-->>","IOException in Offline Manager start");
        }
    }

    private void activateListeners(OfflineManager manager) {
        manager.setAssetStateListener(new OfflineManager.AssetStateListener() {
            @Override
            public void onAssetDownloadFailed(@NonNull String assetId, @NonNull Exception error) {
                ktDownloadEvents.onAssetDownloadFailed(assetId,error);
                Log.w("downloadStatus",assetId+" "+"onAssetDownloadFailed "+error.toString());
               // toastLong("Download of" + error + "failed:" + error);
               // updateItemStatus(assetId);
            }

            @Override
            public void onAssetDownloadComplete(@NonNull String assetId) {
                Log.w("downloadStatus",assetId+" "+"onAssetDownloadComplete");
                ktDownloadEvents.onDownloadPaused(assetId);
               /* log.d("onAssetDownloadComplete");
                log.d("onAssetDownloadComplete:" + (SystemClock.elapsedRealtimeNanos() - startTime));
                toast("Complete");
                updateItemStatus(assetId);*/
            }

            @Override
            public void onAssetDownloadPending(@NonNull String assetId) {
                Log.w("downloadStatus",assetId+" "+"onAssetDownloadPending");
               // updateItemStatus(assetId);
            }

            @Override
            public void onAssetDownloadPaused(@NonNull String assetId) {
                Log.w("downloadStatus",assetId+" "+"onAssetDownloadPaused");
                ktDownloadEvents.onDownloadPaused(assetId);
                /*toast("Paused");
                updateItemStatus(assetId);*/
            }

            @Override
            public void onRegistered(@NonNull String assetId, @NonNull OfflineManager.DrmStatus drmStatus) {
                Log.w("downloadStatus",assetId+" "+"onRegistered");
               /* toast("onRegistered:" + drmStatus.currentRemainingTime + "seconds left");
                updateItemStatus(assetId);*/
            }

            @Override
            public void onRegisterError(@NonNull String assetId, @NonNull Exception error) {
                Log.w("downloadStatus",assetId+" "+"onRegisterError");
                /*toastLong("onRegisterError:" + assetId + " " + error);
                updateItemStatus(assetId);*/
            }

            @Override
            public void onStateChanged(@NonNull String assetId, @NonNull OfflineManager.AssetInfo assetInfo) {
                Log.w("downloadStatus",assetId+" "+"onStateChanged"+"  "+assetInfo.getState());
                if (assetInfo!=null && assetInfo.getState()!=null){
                    Long sizeBytes=assetInfo.getEstimatedSize();

                    if (sizeBytes == null ||sizeBytes <= 0) {

                    }else {
                       Float v= (Float.valueOf(sizeBytes) / (1000*1000));
                        String formattedString = String.format("%.01f", v);
                       Log.w("sizeOfAsset",formattedString+" "+"MB");
                    }

                    //return String.format(Locale.ROOT, "%.3f", (Float.valueOf(sizeBytes) / (1000*1000))) + "mb";

                    ktDownloadEvents.onStateChanged(assetInfo.getState());
                }else {
                    ktDownloadEvents.onStateChanged(null);
                }
               /* toast("onStateChanged");
                updateItemStatus(assetId);*/
            }

            @Override
            public void onAssetRemoved(@NonNull String assetId) {
                Log.w("downloadStatus",assetId+" "+"onAssetRemoved");
               /* toast("onAssetRemoved");
                updateItemStatus(assetId);*/
            }
        });

        manager.setDownloadProgressListener((assetId, bytesDownloaded, totalBytesEstimated, percentDownloaded) -> {
            long progress=bytesDownloaded/1000;
           // Log.e("",""[progress] " + assetId +": " + (bytesDownloaded / 1000) + "/" + (totalBytesEstimated / 1000));
            Log.e("progress",percentDownloaded+"  "+progress);
            ktDownloadEvents.setDownloadProgressListener(percentDownloaded,assetId);
        });


    }
    OfflineManager.AssetInfo assetInf;
    public void startDownload(int position,String kentryid,String title) {
        manager.setKalturaParams(KalturaPlayer.Type.ovp, SDKConfig.PARTNER_ID);
        manager.setKalturaServerUrl(SDKConfig.KALTURA_SERVER_URL);

        OfflineManager.PrepareCallback prepareCallback = new OfflineManager.PrepareCallback() {
            @Override
            public void onPrepared(@NonNull String assetId, @NonNull OfflineManager.AssetInfo assetInfo, @Nullable Map<OfflineManager.TrackType, List<OfflineManager.Track>> selected) {
                assetInf=assetInfo;
                manager.startAssetDownload(assetInfo);
               /* item.setAssetInfo(assetInfo);
                runOnUiThread(() -> {
                    Snackbar.make(assetListView, "Prepared", Snackbar.LENGTH_LONG).setDuration(5000).setAction("Start", v -> doStart(item)).show();
                    assetListView.invalidateViews();
                });*/
            }

            @Override
            public void onPrepareError(@NonNull String assetId, @NonNull Exception error) {
                Log.w("prepaireRelated","onPrepareError-->"+assetId+" "+error.getMessage());
               // toastLong("onPrepareError: " + error);
            }

            @Override
            public void onMediaEntryLoadError(@NonNull Exception error) {
                Log.w("prepaireRelated","onMediaEntryLoadError-->"+" "+error.getMessage());
              //  toastLong("onMediaEntryLoadError: " + error);
            }

            @Override
            public void onMediaEntryLoaded(@NonNull String assetId, @NonNull PKMediaEntry mediaEntry) {
                Log.w("prepaireRelated","onPrepareError-->"+assetId+" "+mediaEntry.toString());
               // reduceLicenseDuration(mediaEntry, 300);
            }

            @Override
            public void onSourceSelected(@NonNull String assetId, @NonNull PKMediaSource source, @Nullable PKDrmParams drmParams) {

            }
        };

        OfflineManager.SelectionPrefs defaultPrefs=createUserPrefrences(position);
        OVPItem ovpItem=new OVPItem(SDKConfig.PARTNER_ID,kentryid,null,title);
        manager.prepareAsset(((KalturaItem) ovpItem).mediaOptions(), defaultPrefs, prepareCallback);

    }

    private OfflineManager.SelectionPrefs createUserPrefrences(int position) {
        Log.w("qualityPosition",position+"");
        OfflineManager.SelectionPrefs defaultPrefs = new OfflineManager.SelectionPrefs();
        if (position==0){
            defaultPrefs.videoBitrate = 1000000;
        }else if (position==1){
            defaultPrefs.videoBitrate = 600000;
        }else if (position==2){
            defaultPrefs.videoBitrate = 450000;
        }else if (position==3){
            defaultPrefs.videoBitrate = 450000;
        }else {
            defaultPrefs.videoBitrate = 150000;
        }

        defaultPrefs.allAudioLanguages = true;
        defaultPrefs.allTextLanguages = true;
        defaultPrefs.allowInefficientCodecs = false;
        return defaultPrefs;
    }

    int selectedVideoQualityPosition=0;
    public void selectVideoQuality(VideoQualitySelectedListener videoQualitySelectedListener) {
        LayoutDownloadQualityBottomSheetBinding binding = DataBindingUtil.inflate(zContext.getLayoutInflater(), R.layout.layout_download_quality_bottom_sheet, null,false);
        BottomSheetDialog dialog = new BottomSheetDialog(zContext);
        dialog.setContentView(binding.getRoot());

        binding.recyclerView.hasFixedSize();
        binding.recyclerView.setNestedScrollingEnabled(false);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(zContext, RecyclerView.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(binding.recyclerView.getContext(),binding.recyclerView.VERTICAL);
        binding.recyclerView.addItemDecoration(dividerItemDecoration);

        String[] downloadQualityList = zContext.getResources().getStringArray(R.array.download_quality);
        List<String> stringList = new ArrayList<String>(Arrays.asList(downloadQualityList));
        stringList.remove(4);


        selectedVideoQualityPosition=0;
        SelectDownloadQualityAdapter downloadQualityAdapter=new SelectDownloadQualityAdapter(zContext, stringList, new VideoQualitySelectedListener() {
            @Override
            public void videoQualitySelected(int position) {
                selectedVideoQualityPosition=position;
            }
        });

        binding.btnStartDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.checkboxMakeDefault.isChecked()){
                    new SharedPrefHelper(zContext).setInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, selectedVideoQualityPosition);
                }
                videoQualitySelectedListener.videoQualitySelected(selectedVideoQualityPosition);
                dialog.dismiss();
            }
        });

        binding.recyclerView.setAdapter(downloadQualityAdapter);
        dialog.show();

    }

    public OfflineManager getManager() {
        return manager;
    }

    public void cancelVideo(String entryId) {
        if (entryId != null && !entryId.equalsIgnoreCase("")) {
            getManager().removeAsset(entryId);
        }

    }

    public void pauseVideo(String entryId) {
        if (entryId != null && !entryId.equalsIgnoreCase("")) {
            manager.pauseAssetDownload(entryId);
            updateItemStatus(entryId);
        }
    }

    private void updateItemStatus(String entryId) {
       manager.getAssetInfo(entryId);
    }

    public void resumeDownload(String entryId) {
        manager.startAssetDownload(manager.getAssetInfo(entryId));
    }

    public void getAssetInfo(String kentry) {
        if (manager!=null && ktDownloadEvents!=null){
            OfflineManager.AssetInfo assetInfo=manager.getAssetInfo(kentry);
            if (assetInfo!=null){
                if (assetInfo.getState()!=null){
                    ktDownloadEvents.initialStatus(assetInfo.getState());
                }
            }
        }
    }
}
