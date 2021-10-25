package panteao.make.ready.utils.helpers.downloads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
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

import java.io.IOException;
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
import panteao.make.ready.activities.downloads.WifiPreferenceListener;
import panteao.make.ready.activities.purchase.TVODENUMS;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.LayoutDownloadQualityBottomSheetBinding;
import panteao.make.ready.databinding.WifiDialogBinding;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.SharedPrefesConstants;
import panteao.make.ready.utils.helpers.SharedPrefHelper;
import panteao.make.ready.utils.helpers.downloads.db.DBExecuter;
import panteao.make.ready.utils.helpers.downloads.db.DownloadDataBase;
import panteao.make.ready.utils.helpers.downloads.db.DownloadItemEntity;
import panteao.make.ready.utils.helpers.downloads.downloadListing.DownloadStateListener;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

public class KTDownloadHelper {
    Activity zContext;
    private OfflineManager manager;
    KTDownloadEvents ktDownloadEvents;
    ManagerStart managerStartListener;
    DownloadDataBase db;

    public KTDownloadHelper(Activity zContext) {
        this.zContext=zContext;
        db=DownloadDataBase.getInstance(zContext);
        init(zContext);
    }

    public KTDownloadHelper(Activity zContext,ManagerStart managerStart) {
        this.zContext=zContext;
        this.managerStartListener=managerStart;
        db=DownloadDataBase.getInstance(zContext);
        init(zContext);
    }

    public KTDownloadHelper(Activity zContext,KTDownloadEvents ktDownloadEvents) {
        this.zContext=zContext;
        this.ktDownloadEvents=ktDownloadEvents;
        db=DownloadDataBase.getInstance(zContext);
        init(zContext);
    }

    private void init(Context zContext) {
        manager = OfflineManager.getInstance(zContext);
        manager.setOfflineManagerSettings(new OfflineManagerSettings().setDefaultHlsAudioBitrateEstimation(64000));
        activateListeners(manager);
        startManager(manager);
    }

    public void startManager(OfflineManager manager) {
        try {
            manager.start(new OfflineManager.ManagerStartCallback() {
                @Override
                public void onStarted() {
                    try {
                        if (managerStartListener!=null){
                            if (manager!=null){
                                managerStartListener.managerStarted();
                            }
                        }
                        Log.e("started-->>","Manager start");
                    }catch (Exception e){

                    }
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
                if (ktDownloadEvents!=null){
                    ktDownloadEvents.onAssetDownloadFailed(assetId,error);
                }
                Log.w("downloadStatus",assetId+" "+"onAssetDownloadFailed "+error.toString());
               // toastLong("Download of" + error + "failed:" + error);
               // updateItemStatus(assetId);
            }

            @Override
            public void onAssetDownloadComplete(@NonNull String assetId) {
                Log.w("downloadStatus",assetId+" "+"onAssetDownloadComplete");
                Log.w("downloadVideo 6",assetId);
                if (ktDownloadEvents!=null){
                    ktDownloadEvents.onDownloadPaused(assetId);
                    ktDownloadEvents.onAssetDownloadComplete(assetId);
                }
            }

            @Override
            public void onAssetDownloadPending(@NonNull String assetId) {
                Log.w("downloadStatus 5",assetId+" "+"onAssetDownloadPending");
               // updateItemStatus(assetId);
            }

            @Override
            public void onAssetDownloadPaused(@NonNull String assetId) {
                Log.w("downloadStatus",assetId+" "+"onAssetDownloadPaused");
                Log.w("downloadVideo 4",assetId);
                if (ktDownloadEvents!=null){
                    ktDownloadEvents.onDownloadPaused(assetId);
                }

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
                Log.w("downloadVideo 3",assetId);
                try {
                    if (assetInfo!=null && assetInfo.getState()!=null){
                        Long sizeBytes=assetInfo.getEstimatedSize();

                        if (sizeBytes == null ||sizeBytes <= 0) {

                        }else {
                            Float v= (Float.valueOf(sizeBytes) / (1000*1000));
                            String formattedString = String.format("%.01f", v);
                            if (formattedString!=null && !formattedString.equalsIgnoreCase("")){
                                Log.w("sizeOfAsset",formattedString+" "+"MB");
                                double val=Double.parseDouble(formattedString);
                                Log.w("sizeOfAsset",val+" "+"MB");
                                if (val>1024){
                                    double val2=val/1024;
                                    Float v2= (float)val2;
                                    // String gbValue=String.valueOf(val2);
                                    String fva=String.format("%.01f", v2);
                                    Log.w("sizeOfAsset",fva+" "+"GB");
                                }else {
                                    Log.w("sizeOfAsset",formattedString+" "+"MB");
                                }

                            }

                        }

                    }
                }catch (Exception e){

                }

                try {
                    if (assetInfo!=null && assetInfo.getState()!=null){

                        //return String.format(Locale.ROOT, "%.3f", (Float.valueOf(sizeBytes) / (1000*1000))) + "mb";
                        if (ktDownloadEvents!=null){
                            ktDownloadEvents.onStateChanged(assetInfo.getState());
                        }

                    }else {
                        if (ktDownloadEvents!=null){
                            ktDownloadEvents.onStateChanged(null);
                        }

                    }
               /* toast("onStateChanged");
                updateItemStatus(assetId);*/
                }catch (Exception ignored){

                }

            }

            @Override
            public void onAssetRemoved(@NonNull String assetId) {
                Log.w("downloadStatus",assetId+" "+"onAssetRemoved");
                Log.w("downloadVideo 2",assetId);
               /* toast("onAssetRemoved");
                updateItemStatus(assetId);*/
            }
        });

        manager.setDownloadProgressListener((assetId, bytesDownloaded, totalBytesEstimated, percentDownloaded) -> {
            long progress=bytesDownloaded/1000;
           // Log.e("",""[progress] " + assetId +": " + (bytesDownloaded / 1000) + "/" + (totalBytesEstimated / 1000));
            Log.e("progress 1",percentDownloaded+"  "+progress);
            if (ktDownloadEvents!=null){
                Log.e("progress 2",percentDownloaded+"  "+progress);
                ktDownloadEvents.setDownloadProgressListener(percentDownloaded,assetId);
            }
        });


    }
    OfflineManager.AssetInfo assetInf;
    public void startDownload(int position,String kentryid,String title,String assetType,String seriesID,String seriesName,String imageURL,String episodeNumber,int seasonNumber,String seriesImageURL) {
        manager.setKalturaParams(KalturaPlayer.Type.ovp, SDKConfig.PARTNER_ID);
        manager.setKalturaServerUrl(SDKConfig.KALTURA_SERVER_URL);

        OfflineManager.PrepareCallback prepareCallback = new OfflineManager.PrepareCallback() {
            @Override
            public void onPrepared(@NonNull String assetId, @NonNull OfflineManager.AssetInfo assetInfo, @Nullable Map<OfflineManager.TrackType, List<OfflineManager.Track>> selected) {
                assetInf=assetInfo;
                manager.startAssetDownload(assetInfo);
                storeAssetInDB(title,kentryid,assetType,seriesID,seriesName,imageURL,episodeNumber,seasonNumber,seriesImageURL);
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

    private void storeAssetInDB(String title, String kentryid, String assetType,String seriesID,String seriesName,String imageURL,String episodeNumber,int seasonNumber,String seriesImageURL) {
        String expiryTimeStamp=AppCommonMethod.getCurrentDateTimeStamp(2);
        Log.w("expiryTimeStamp",expiryTimeStamp);
        if (assetType.equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())){
            if (seriesID!=null && !seriesID.equalsIgnoreCase("")){
                checkSeriesEpisodes(title,kentryid,assetType,seriesID,seriesName,imageURL,episodeNumber,seasonNumber,seriesImageURL,expiryTimeStamp);
            }else {
                DownloadItemEntity downloadItemEntity=new DownloadItemEntity(title,assetType,false,"",expiryTimeStamp,kentryid,
                        -1,"","",seriesName,imageURL, AppCommonMethod.getCurrentTimeStamp(),"",count);
                db.downloadDao().insertDownloadItem(downloadItemEntity);
            }

        }else   if (assetType.equalsIgnoreCase(MediaTypeConstants.getInstance().getChapter())){
            if (seriesID!=null && !seriesID.equalsIgnoreCase("")){
               // int episodesCount=numberOfEpisodes(seriesID);
                checkTutorialChapters(title,kentryid,assetType,seriesID,seriesName,imageURL,episodeNumber,seasonNumber,seriesImageURL,expiryTimeStamp);
            }else {
                DownloadItemEntity downloadItemEntity=new DownloadItemEntity(title,assetType,true,"",expiryTimeStamp,kentryid,
                        -1,"","",seriesName,imageURL, AppCommonMethod.getCurrentTimeStamp(),seriesImageURL,count);
                db.downloadDao().insertDownloadItem(downloadItemEntity);
            }

        }else {
            DownloadItemEntity downloadItemEntity=new DownloadItemEntity(title,assetType,false,"",expiryTimeStamp,kentryid,
                    -1,"","",seriesName,imageURL, AppCommonMethod.getCurrentTimeStamp(),"",count);
            db.downloadDao().insertDownloadItem(downloadItemEntity);
        }
    }

    private void checkTutorialChapters(String title, String kentryid, String assetType, String seriesID, String seriesName, String imageURL,String episodeNumber,int seasonNumber,String seriesImageURL,String expiryTimeStamp) {
        DBExecuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (db!=null) {
                    List<DownloadItemEntity> downloadItemEntityList = db.downloadDao().loadChaptersByTID(seriesID);
                    if (downloadItemEntityList.size() > 0) {
                        DownloadItemEntity downloadItemEntity=new DownloadItemEntity(title,assetType,true,"",expiryTimeStamp,kentryid,
                                -1,seriesID,"",seriesName,imageURL, AppCommonMethod.getCurrentTimeStamp(),seriesImageURL,downloadItemEntityList.size()+1);
                        db.downloadDao().insertDownloadItem(downloadItemEntity);
                    } else {
                        DownloadItemEntity downloadItemEntity=new DownloadItemEntity(title,assetType,true,"",expiryTimeStamp,kentryid,
                                -1,seriesID,"",seriesName,imageURL, AppCommonMethod.getCurrentTimeStamp(),seriesImageURL,count);
                        db.downloadDao().insertDownloadItem(downloadItemEntity);
                    }
                }else {
                    DownloadItemEntity downloadItemEntity=new DownloadItemEntity(title,assetType,true,"",expiryTimeStamp,kentryid,
                            -1,seriesID,"",seriesName,imageURL, AppCommonMethod.getCurrentTimeStamp(),seriesImageURL,count);
                    db.downloadDao().insertDownloadItem(downloadItemEntity);
                }
            }
        });


    }


    private void checkSeriesEpisodes(String title, String kentryid, String assetType, String seriesID, String seriesName, String imageURL,String episodeNumber,int seasonNumber,String seriesImageURL,String expiryTimeStamp) {
        DBExecuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (db!=null) {
                   List<DownloadItemEntity> downloadItemEntityList = db.downloadDao().loadEpisodesBySeriesID(seriesID,seasonNumber);
                   Log.w("numberOfEpisodes",downloadItemEntityList.size()+"");
                    if (downloadItemEntityList.size() > 0) {
                        DownloadItemEntity downloadItemEntity=new DownloadItemEntity(title,assetType,true,"",expiryTimeStamp,kentryid,
                                seasonNumber,seriesID,episodeNumber,seriesName,imageURL, AppCommonMethod.getCurrentTimeStamp(),seriesImageURL,downloadItemEntityList.size()+1);
                        db.downloadDao().insertDownloadItem(downloadItemEntity);
                    } else {
                        DownloadItemEntity downloadItemEntity=new DownloadItemEntity(title,assetType,true,"",expiryTimeStamp,kentryid,
                                seasonNumber,seriesID,episodeNumber,seriesName,imageURL, AppCommonMethod.getCurrentTimeStamp(),seriesImageURL,count);
                        db.downloadDao().insertDownloadItem(downloadItemEntity);
                    }
                }else {
                    DownloadItemEntity downloadItemEntity=new DownloadItemEntity(title,assetType,true,"",expiryTimeStamp,kentryid,
                            seasonNumber,seriesID,episodeNumber,seriesName,imageURL, AppCommonMethod.getCurrentTimeStamp(),seriesImageURL,count);
                    db.downloadDao().insertDownloadItem(downloadItemEntity);
                }
            }
        });


    }
    int count=0;
    public int numberOfEpisodes(String seriesID) {
        DBExecuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

            }
        });
       return count;
    }

    private OfflineManager.SelectionPrefs createUserPrefrences(int position) {
        Log.w("qualityPosition",position+"");
        OfflineManager.SelectionPrefs defaultPrefs = new OfflineManager.SelectionPrefs();
        if (position==0){
            defaultPrefs.videoBitrate = 2000000;
        }else if (position==1){
            defaultPrefs.videoBitrate = 1000000;
        }else if (position==2){
            defaultPrefs.videoBitrate = 100000;
        }else if (position==3){
            defaultPrefs.videoBitrate = 100000;
        }else {
            defaultPrefs.videoBitrate = 60000;
        }

        defaultPrefs.allAudioLanguages = true;
        defaultPrefs.allTextLanguages = true;
        defaultPrefs.allowInefficientCodecs = false;
        return defaultPrefs;
    }

    int selectedVideoQualityPosition=0;
    int clicked=-1;
    int downloadQualitySelection=0;
    List<String> stringList= null;
    public void selectVideoQuality(String typeofTVOD,VideoQualitySelectedListener videoQualitySelectedListener) {
        LayoutDownloadQualityBottomSheetBinding binding = DataBindingUtil.inflate(zContext.getLayoutInflater(), R.layout.layout_download_quality_bottom_sheet, null,false);
        BottomSheetDialog dialog = new BottomSheetDialog(zContext);
        dialog.setContentView(binding.getRoot());

        binding.recyclerView.hasFixedSize();
        binding.recyclerView.setNestedScrollingEnabled(false);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(zContext, RecyclerView.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(binding.recyclerView.getContext(),binding.recyclerView.VERTICAL);
        binding.recyclerView.addItemDecoration(dividerItemDecoration);
        if (typeofTVOD.equalsIgnoreCase("")){
            String[] downloadQualityList = zContext.getResources().getStringArray(R.array.download_quality);
            stringList = new ArrayList<String>(Arrays.asList(downloadQualityList));
            stringList.remove(3);

        }else {
            if (typeofTVOD.equalsIgnoreCase(TVODENUMS.___sd.name())){
                String[] downloadQualityList = zContext.getResources().getStringArray(R.array.download_quality_sd);
                stringList = new ArrayList<String>(Arrays.asList(downloadQualityList));
            }else if (typeofTVOD.equalsIgnoreCase(TVODENUMS.___hd.name())){
                String[] downloadQualityList = zContext.getResources().getStringArray(R.array.download_quality_hd);
                stringList = new ArrayList<String>(Arrays.asList(downloadQualityList));
            }else if (typeofTVOD.equalsIgnoreCase(TVODENUMS.___uhd.name())){
                String[] downloadQualityList = zContext.getResources().getStringArray(R.array.download_quality);
                stringList = new ArrayList<String>(Arrays.asList(downloadQualityList));
                stringList.remove(3);
            }
        }


        selectedVideoQualityPosition=0;
        clicked=-1;
        SelectDownloadQualityAdapter downloadQualityAdapter=new SelectDownloadQualityAdapter(zContext, stringList, new VideoQualitySelectedListener() {
            @Override
            public void videoQualitySelected(int position) {
                if (stringList.get(position).equalsIgnoreCase("SD")){
                    selectedVideoQualityPosition=2;
                }else if (stringList.get(position).equalsIgnoreCase("HD")){
                    selectedVideoQualityPosition=1;
                }else if (stringList.get(position).equalsIgnoreCase("UHD")){
                    selectedVideoQualityPosition=0;
                }else {
                    selectedVideoQualityPosition=-1;
                }
                clicked=position;
            }
        });

        binding.btnStartDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clicked>-1){
                    if (binding.checkboxMakeDefault.isChecked()){
                        new SharedPrefHelper(zContext).setInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, selectedVideoQualityPosition);
                    }
                    videoQualitySelectedListener.videoQualitySelected(selectedVideoQualityPosition);
                    dialog.dismiss();
                }else {
                    zContext.runOnUiThread(() -> { Toast.makeText(zContext, "Selection Required!", Toast.LENGTH_LONG).show();});
                }

            }
        });

        binding.recyclerView.setAdapter(downloadQualityAdapter);
        dialog.show();

    }

    public void changeWifiSetting(WifiPreferenceListener videoQualitySelectedListener) {
        WifiDialogBinding binding = DataBindingUtil.inflate(zContext.getLayoutInflater(), R.layout.wifi_dialog, null,false);
        BottomSheetDialog dialog = new BottomSheetDialog(zContext);
        dialog.setContentView(binding.getRoot());

        String[] downloadQualityList = zContext.getResources().getStringArray(R.array.download_quality);
        List<String> stringList = new ArrayList<String>(Arrays.asList(downloadQualityList));
        stringList.remove(3);

        if (KsPreferenceKeys.getInstance().getDownloadOverWifi()==1){
            binding.switchDownload.setChecked(true);
        }

        binding.btnStartDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.switchDownload.isChecked()) {
                    KsPreferenceKeys.getInstance().setDownloadOverWifi(1);
                }else {
                    KsPreferenceKeys.getInstance().setDownloadOverWifi(0);
                }

                videoQualitySelectedListener.actionP(KsPreferenceKeys.getInstance().getDownloadOverWifi());
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public OfflineManager getManager() {
        return manager;
    }

    public void cancelVideo(String entryId) {
        try {
            DBExecuter.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (entryId != null && !entryId.equalsIgnoreCase("")) {
                            // Log.w("sortedChapters",entryId+" ");
                            // Log.w("downloadVideo 1",entryId);
                            updateDataBase(entryId);
                        }

                    }catch (Exception ignored){
                        Log.w("sortedChapters",ignored.toString()+" ");
                    }

                }
            });

        }catch (Exception ignored){
            Log.w("sortedChapters",ignored.toString()+" ");
        }

    }

    private void updateDataBase(String entryI) {
        try {
            DBExecuter.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        DownloadItemEntity entity=getAssetFromDB(entryI);
                        getManager().removeAsset(entryI);
                        removeFromDB(entryI);
                        getAllEpdFromDB(entity.getSeriesId(),entity.getSeasonNumber());
                        if (zContext!=null){
                            if(zContext instanceof Activity){
                                zContext.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                                        },10);
                                    }
                                });
                            }
                        }


                    }catch (Exception ignored){
                        Log.w("sortedChapters",ignored.toString()+" ");
                    }

                }
            });

        }catch (Exception ignored){
            Log.w("sortedChapters",ignored.toString()+" ");
        }
    }

    public void pauseVideo(String entryId) {
        DBExecuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (entryId != null && !entryId.equalsIgnoreCase("")) {
                    manager.pauseAssetDownload(entryId);
                    updateItemStatus(entryId);
                }
            }
        });
    }

    private void updateItemStatus(String entryId) {
       manager.getAssetInfo(entryId);
    }

    public void resumeDownload(String entryId) {
        manager.startAssetDownload(manager.getAssetInfo(entryId));
    }

    public void updateDownload(List<? extends DownloadItemEntity> it) {
        try {
            DBExecuter.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (it != null && it.size()>0) {
                        if (db!=null){
                            try {
                                DownloadItemEntity entity=it.get(0);
                                Log.w("sortedChapters",entity.getEpisodesCount()+" "+it.size());
                                entity.setEpisodesCount(it.size());
                                db.downloadDao().updateDownloadItem(entity);
                            }catch (Exception e){

                            }
                        }
                    }
                }
            });
        }catch (Exception ignored){

        }
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

    public DownloadItemEntity getAssetFromDB(String kentryid) {
            if (db!=null) {
                List<DownloadItemEntity> downloadItemEntity = db.downloadDao().loadAllDownloads();
                Log.w("sortedChapters 2",downloadItemEntity.size()+"");
                if (downloadItemEntity.size() > 0) {
                    for (int i = 0; i < downloadItemEntity.size(); i++) {
                        String entryId = downloadItemEntity.get(i).getEntryId();
                        Log.w("sortedChapters 2",entryId+"-->in"+ kentryid);
                        if (entryId != null && entryId.equalsIgnoreCase(kentryid)) {
                            Log.w("sortedChapters 2",entryId+"-->in");

                            return downloadItemEntity.get(i);
                        } else {
                            //return null;
                        }
                    }
                } else {
                   // return null;
                }
            }else {
               // return null;
            }
        return null;
    }

    public MutableLiveData<List<DownloadItemEntity>> getAllAssetFromDB() {
        MutableLiveData<List<DownloadItemEntity>> allDataFromDB=new MutableLiveData<>();
        DBExecuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (db!=null) {
                    List<DownloadItemEntity> downloadItemEntityList = db.downloadDao().loadAllDownloads();
                    if (downloadItemEntityList.size() > 0) {
                        allDataFromDB.postValue(downloadItemEntityList);
                    } else {
                        allDataFromDB.postValue(new ArrayList<>());
                    }
                }else {
                    allDataFromDB.postValue(new ArrayList<>());
                }
            }
        });
       return allDataFromDB;
    }

    public MutableLiveData<List<DownloadItemEntity>> getAllEpisodesFromDB(String seriesId,int seasonNumber) {
        MutableLiveData<List<DownloadItemEntity>> allDataFromDB=new MutableLiveData<>();
        DBExecuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (db!=null) {
                    List<DownloadItemEntity> downloadItemEntityList = db.downloadDao().loadEpisodesBySeriesID(seriesId,seasonNumber);
                    if (downloadItemEntityList.size() > 0) {
                        allDataFromDB.postValue(downloadItemEntityList);
                    } else {
                        allDataFromDB.postValue(new ArrayList<>());
                    }
                }else {
                    allDataFromDB.postValue(new ArrayList<>());
                }
            }
        });
        return allDataFromDB;
    }




    private void removeFromDB(String entryIds) {
        if (db!=null){
            DBExecuter.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (db!=null) {
                        List<DownloadItemEntity> downloadItemEntity = db.downloadDao().loadAllDownloads();
                        if (downloadItemEntity.size() > 0) {
                            for (int i = 0; i < downloadItemEntity.size(); i++) {
                                String entryId = downloadItemEntity.get(i).getEntryId();
                                if (entryId != null && entryId.equalsIgnoreCase(entryIds)) {
                                    db.downloadDao().deleteDownloadItem(downloadItemEntity.get(i));
                                    Log.w("sortedChapters","deletefromdb");
                                    break;
                                } else {
                                    Log.w("Dowmload","entryid not exists in DB");
                                }
                            }
                        }
                    }else {
                        Log.w("Dowmload","DB instance is null");
                    }
                }
            });

        }
    }
    DownloadStateListener listener;
    public void getAssetDownloadState(String kentry, KTDownloadHelper downloadHelper,DownloadStateListener listener) {
        this.listener=listener;
        if (manager!=null && ktDownloadEvents!=null){
            OfflineManager.AssetInfo assetInfo=manager.getAssetInfo(kentry);
            if (assetInfo!=null){
                if (assetInfo.getState()!=null){
                    Log.d("stateOfAsset",assetInfo.getEstimatedSize()+"   "+assetInfo.getBytesDownloaded());
                    long percentage=assetInfo.getBytesDownloaded()*100/assetInfo.getEstimatedSize();
                    Log.d("stateOfAsset",percentage+"%");
                    float per=percentage;
                    Log.d("stateOfAsset",percentage+"%");
                    listener.downloadState(assetInfo.getState().name(),per,getDownloadedSize(assetInfo.getEstimatedSize()));
                }else {
                    Log.d("stateOfAsset","null");
                }
            }else {
                Log.d("stateOfAsset","null");
                if (downloadHelper!=null){
                    downloadHelper.removeFromDB(kentry);
                }
            }
        }
    }

    private String getDownloadedSize(long estimatedSize) {
        String size="";
        try {
            Long sizeBytes=estimatedSize;

            if (sizeBytes == null ||sizeBytes <= 0) {

            }else {
                Float v = (Float.valueOf(sizeBytes) / (1000 * 1000));
                String sizeM = String.format("%.01f", v);

                double val = Double.parseDouble(sizeM);
                Log.w("sizeOfAsset", val + " " + "MB");
                if (val > 1024) {
                    double val2 = val / 1024;
                    Float v2 = (float) val2;
                    // String gbValue=String.valueOf(val2);
                    String fva = String.format("%.01f", v2);
                    size=fva+" GB";
                    Log.w("sizeOfAsset", fva + " " + "GB");
                    Log.w("sizeOfAsset", size + " " + "MB");
                }else {
                    size=sizeM +" MB";
                }
            }
        }catch (Exception ignored){

        }

        return size;
    }


    public void deleteAllExpiredVideos() {
        try {
            ArrayList<DownloadItemEntity> idsForRemove=new ArrayList<DownloadItemEntity>();
            DBExecuter.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (db!=null) {
                        List<DownloadItemEntity> downloadItemEntityList = db.downloadDao().loadAllDownloads();
                        if (downloadItemEntityList.size() > 0) {
                            for (int i=0;i<downloadItemEntityList.size();i++){
                                if (downloadItemEntityList.get(i).getExpiryDate()!=null && !downloadItemEntityList.get(i).getExpiryDate().equalsIgnoreCase("")){
                                    String currentTimeStamp=downloadItemEntityList.get(i).getExpiryDate();
                                    long DBexpiry=Long.parseLong(currentTimeStamp);
                                    Long expiryTimeStamp=AppCommonMethod.getCurrentTimeStamp();
                                    Log.w("timestamps",expiryTimeStamp+"  "+DBexpiry);
                                    if (expiryTimeStamp>DBexpiry){
                                        Log.w("removeIds",downloadItemEntityList.get(i).getEntryId());
                                        try {
                                            if (manager!=null){
                                                manager.removeAsset(downloadItemEntityList.get(i).getEntryId());
                                            }
                                        }catch (Exception e){

                                        }

                                        idsForRemove.add(downloadItemEntityList.get(i));
                                    }
                                }
                            }
                            db.downloadDao().deleteExpireIDs(idsForRemove);
                        } else {

                        }
                    }else {

                    }
                }
            });
        }catch (Exception ignored){
            Log.w("sortedChapters",ignored.toString()+" ");
        }
    }

    public void getAllEpdFromDB(String seriesId,int seasonNumber) {
        try {
            DBExecuter.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (db!=null) {
                        try {
                            List<DownloadItemEntity> downloadItemEntityList = db.downloadDao().loadEpisodesBySeriesID(seriesId, seasonNumber);
                            Log.w("sortedChapters",downloadItemEntityList.size()+" ");
                            if (downloadItemEntityList.size() > 0) {
                                  Log.w("sortedChapters",downloadItemEntityList.get(0).getAssetType()+" ");
                                if (!downloadItemEntityList.get(0).getAssetType().equalsIgnoreCase("chapter")) {
                                    updateDownload(AppCommonMethod.getSortedChapters(new ArrayList<DownloadItemEntity>(downloadItemEntityList)));
                                } else {
                                    updateDownload(AppCommonMethod.getSortedChapters(new ArrayList<DownloadItemEntity>(downloadItemEntityList)));
                                }
                            } else {

                            }
                        }catch (Exception e){
                            Log.w("sortedChapters 2",e.toString());
                        }
                    }else {

                    }
                }
            });

        }catch (Exception ignored){
            Log.w("sortedChapters 3",ignored.toString());
        }
    }

    public void deleteAllVideos() {
        try {
            ArrayList<DownloadItemEntity> idsForRemove=new ArrayList<DownloadItemEntity>();
            DBExecuter.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (db!=null) {
                        List<DownloadItemEntity> downloadItemEntityList = db.downloadDao().loadAllDownloads();
                        if (downloadItemEntityList.size() > 0) {
                            try {
                                for (int i=0;i<downloadItemEntityList.size();i++){
                                    if (downloadItemEntityList.get(i).getExpiryDate()!=null && !downloadItemEntityList.get(i).getExpiryDate().equalsIgnoreCase("")){
                                        Log.w("removeIds",downloadItemEntityList.get(i).getEntryId());
                                        try {
                                            if (manager!=null){
                                                manager.removeAsset(downloadItemEntityList.get(i).getEntryId());
                                            }
                                        }catch (Exception e){
                                            Log.w("sortedChapters 5",e.toString());
                                        }

                                        idsForRemove.add(downloadItemEntityList.get(i));
                                    }
                                }
                                db.downloadDao().deleteExpireIDs(idsForRemove);
                            }catch (Exception e){
                                Log.w("sortedChapters 6",e.toString());
                            }

                        } else {

                        }
                    }else {

                    }
                }
            });
        }catch (Exception ignored){
            Log.w("sortedChapters 6",ignored.toString());
        }
    }

/*series download Handling*/
public void startSeriesDownload(int position,int seasonNumber,List<EnveuVideoItemBean> seasonEpisodesList) {
    manager.setKalturaParams(KalturaPlayer.Type.ovp, SDKConfig.PARTNER_ID);
    manager.setKalturaServerUrl(SDKConfig.KALTURA_SERVER_URL);

    OfflineManager.PrepareCallback prepareCallback = new OfflineManager.PrepareCallback() {
        @Override
        public void onPrepared(@NonNull String assetId, @NonNull OfflineManager.AssetInfo assetInfo, @Nullable Map<OfflineManager.TrackType, List<OfflineManager.Track>> selected) {
            assetInf=assetInfo;
            manager.startAssetDownload(assetInfo);
            Log.w("downloadAssetAdded","onPrepareSuccess-->"+assetId+"  "+seasonEpisodesList.size());
           // storeAssetInDB(title,kentryid,assetType,seriesID,seriesName,imageURL,episodeNumber,seasonNumber,seriesImageURL);
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


    for (int i = 0;i<seasonEpisodesList.size();i++){
        try {
            String kentryid=seasonEpisodesList.get(i).getkEntryId();
            Log.w("downloadData-->>",kentryid);
            if (kentryid!=null && !kentryid.equalsIgnoreCase("")){
                OfflineManager.SelectionPrefs defaultPrefs=createUserPrefrences(position);
                OVPItem ovpItem=new OVPItem(SDKConfig.PARTNER_ID,kentryid,null,seasonEpisodesList.get(i).getTitle());
                manager.prepareAsset(((KalturaItem) ovpItem).mediaOptions(), defaultPrefs, prepareCallback);
            }
        }catch (Exception e){

        }

    }
   }

}
