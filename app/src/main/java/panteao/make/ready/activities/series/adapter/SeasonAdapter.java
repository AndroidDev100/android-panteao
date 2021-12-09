package panteao.make.ready.activities.series.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kaltura.tvplayer.OfflineManager;

import org.jetbrains.annotations.NotNull;

import panteao.make.ready.activities.instructor.ui.InstructorActivity;
import panteao.make.ready.beanModelV3.playListModelV2.Thumbnail;
import panteao.make.ready.enums.DownloadStatus;
import panteao.make.ready.enums.KalturaImageType;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.config.ImageLayer;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.downloads.KTDownloadEvents;
import panteao.make.ready.utils.helpers.downloads.KTDownloadHelper;
import panteao.make.ready.utils.helpers.downloads.OnDownloadClickInteraction;
import panteao.make.ready.R;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.RowEpisodeListBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.helpers.ImageHelper;

import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.downloads.downloadUtil.DownloadUtils;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.HashMap;
import java.util.List;

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder> implements KTDownloadEvents {
    private final Activity context;
    private List<EnveuVideoItemBean> videoItemBeans;
    private EpisodeItemClick listner;
    private int id;
    private KsPreferenceKeys preference;
    private boolean isLogin;
    private int currentAssetId;
//    private DownloadHelper downloadHelper;
    private HashMap indexMap = new HashMap<String, Integer>();
    private OnDownloadClickInteraction onDownloadClickInteraction;
    private String minutes = "";
    KTDownloadHelper downloadHelper;

    public SeasonAdapter(Activity context, List<EnveuVideoItemBean> videoItemBeans, int id, int currentAssetId, EpisodeItemClick listner) {
        this.context = context;
        this.videoItemBeans = videoItemBeans;
        this.listner = listner;
        this.id = id;
        this.currentAssetId = currentAssetId;
      //  Collections.sort(videoItemBeans, new SortSeasonAdapterItems());
        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();
        downloadHelper = new KTDownloadHelper(context, this);
        onDownloadClickInteraction = (OnDownloadClickInteraction) context;
        buildIndexMap();
    }

    public void refreshDownloadHelper() {
        downloadHelper = new KTDownloadHelper(context, this);
    }

    public List<EnveuVideoItemBean> getAdapterList(){
        return videoItemBeans;
    }

    public List<EnveuVideoItemBean> getSeasonEpisodes() {
        return videoItemBeans;
    }

    private void buildIndexMap() {
        indexMap.clear();
        if (videoItemBeans != null) {
            int index = 0;
            for (EnveuVideoItemBean videoItemBean :
                    videoItemBeans) {
                indexMap.put(videoItemBean.getkEntryId(), index);
                index++;
            }
            notifyDataSetChanged();
        }
    }

    private void notifyVideoChanged(String videoId) {
        if (indexMap.containsKey(videoId)) {
            int index = (int) indexMap.get(videoId);
            for (EnveuVideoItemBean videoItemBean :
                    videoItemBeans) {
                if (videoItemBean.getkEntryId().equals(videoId)) {
                    videoItemBeans.set(index, videoItemBean);
                    notifyItemChanged(index);
                }
            }
        }
    }

    @NonNull
    @Override
    public SeasonAdapter.SeasonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        RowEpisodeListBinding itemBinding;
        itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.row_episode_list, viewGroup, false);
        return new SeasonAdapter.SeasonViewHolder(itemBinding);
    }

    RowEpisodeListBinding clickBinding;
    @Override
    public void onBindViewHolder(@NonNull SeasonAdapter.SeasonViewHolder holder, int position) {
        if (videoItemBeans.get(position) != null) {
            holder.itemBinding.setPlaylistItem(videoItemBeans.get(position));
            try {
                if (context instanceof InstructorActivity){
                    holder.itemBinding.setIsDownloadable(false);
                }else {
                    holder.itemBinding.setIsDownloadable(true);
                }
            }catch (Exception e){

            }

        }
        Log.e("statusDown","innnnn");
        setDownloadStatus(holder,position,videoItemBeans.get(position));
        if(AppCommonMethod.getCheckBCID(videoItemBeans.get(position).getBrightcoveVideoId())) {

        }
        if (videoItemBeans.get(position).getEpisodeNo() != null && videoItemBeans.get(position).getEpisodeNo() instanceof String && !((String) videoItemBeans.get(position).getEpisodeNo()).equalsIgnoreCase("")) {
            String episodeNum =  (String) videoItemBeans.get(position).getEpisodeNo();
            int eNum = Integer.parseInt(episodeNum);
            holder.itemBinding.titleWithSerialNo.setText(eNum + ". " + videoItemBeans.get(position).getTitle());
        } else {
            holder.itemBinding.titleWithSerialNo.setText(videoItemBeans.get(position).getTitle());

        }
        HashMap<String, Thumbnail> crousalImages = videoItemBeans.get(position).getImages();
        KalturaImageType imageType = KalturaImageType.LANDSCAPE;
        videoItemBeans.get(position).setPosterURL(ImageLayer.getInstance().getFilteredImage(crousalImages, imageType, 640, 360));
        ImageHelper.getInstance(context).loadListImage(holder.itemBinding.episodeImage, videoItemBeans.get(position).getPosterURL());

        holder.itemBinding.description.setText(videoItemBeans.get(position).getDescription());

        try {
            if (videoItemBeans.get(position).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries()) ||
                    videoItemBeans.get(position).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getTutorial())){
                holder.itemBinding.duration.setVisibility(View.GONE);
            }else {
                holder.itemBinding.duration.setVisibility(View.VISIBLE);
            }
            if (!StringUtils.isNullOrEmpty(String.valueOf(videoItemBeans.get(position).getDuration()))) {

//                double d = (double) videoItemBeans.get(position).getDuration();
//                long x = (long) d; // x = 1234
                Log.w("episodeTiming",videoItemBeans.get(position).getDuration()+"");
                 minutes=AppCommonMethod.calculateTimeinMinutes(videoItemBeans.get(position).getDuration());
                if (!minutes.contains("s")){
                   // minutes=minutes+" "+context.getResources().getString(R.string.minutes);
                    Log.d("time",minutes);
                    holder.itemBinding.duration.setText(minutes);
                }else {
                    holder.itemBinding.duration.setText(minutes);
                }

            } else {
                holder.itemBinding.duration.setText("00:00");
            }
        }catch (Exception ignored){
            Log.w("OnBindViewException",ignored);
        }


        if (videoItemBeans.get(position).getId() == currentAssetId && isPlayerStart) {
            holder.itemBinding.nowPlaying.setVisibility(View.VISIBLE);
            holder.itemBinding.playIcon.setVisibility(View.GONE);
        } else {
            holder.itemBinding.playIcon.setVisibility(View.VISIBLE);
            holder.itemBinding.nowPlaying.setVisibility(View.GONE);

        }
        holder.itemBinding.episodeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoItemBeans.get(position).getId() == currentAssetId) {
                    return;
                }
                listner.onItemClick(videoItemBeans.get(position), videoItemBeans.get(position).isPremium());
            }
        });

        holder.itemBinding.itemMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoItemBeans.get(position).getId() == currentAssetId) {
                    return;
                }
                listner.onItemClick(videoItemBeans.get(position), videoItemBeans.get(position).isPremium());
            }
        });


       /* holder.itemBinding.mainLay.setOnClickListener(view -> {
            PrintLogging.printLog("", "positionIs" + videoItemBeans.get(position));
            id = videoItemBeans.get(position).getId();
            notifyDataSetChanged();
        });*/

        holder.itemBinding.downloadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    clickBinding=holder.itemBinding;
                    onDownloadClickInteraction.onDownloadClicked(videoItemBeans.get(position).getkEntryId(), videoItemBeans.get(position).getEpisodeNo(), context);
            }
        });
        holder.itemBinding.videoDownloaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDownloadedVideo(v, videoItemBeans.get(position), position);
            }
        });
        holder.itemBinding.videoDownloading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDownloadClickInteraction.onProgressbarClicked(v, this, videoItemBeans.get(position).getkEntryId());
            }
        });

        holder.itemBinding.videoDownloadedFailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDownloadedVideo(v, videoItemBeans.get(position), position);
            }
        });
        holder.itemBinding.pauseDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemBinding.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.REQUESTED);
                //itemStatusChanged(videoItemBeans.get(position).getkEntryId());
                onDownloadClickInteraction.onPauseClicked(videoItemBeans.get(position).getkEntryId(), this);

            }
        });
    }

    public void holdHolder() {
        if (clickBinding!=null){
//            clickBinding.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.REQUESTED);
        }
    }

    private void deleteDownloadedVideo(View view, EnveuVideoItemBean enveuVideoItemBean, int position) {
        AppCommonMethod.showPopupMenu(context, view, R.menu.delete_menu, new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_download:
//                        downloadHelper.deleteVideo(enveuVideoItemBean.getBrightcoveVideoId());
                        onDownloadClickInteraction.onDownloadDeleted(enveuVideoItemBean.getkEntryId(),enveuVideoItemBean);
                        break;
                }
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return videoItemBeans.size();
    }



    public String getEpisodeNumber(String videoId) {
        for (EnveuVideoItemBean enveuVideoItemBean :
                videoItemBeans) {
            if (enveuVideoItemBean.getkEntryId().equals(videoId)) {
                return String.valueOf(enveuVideoItemBean.getEpisodeNo());
            }
        }
        return null;
    }
   // KTDownloadHelper downloadHelpr;
    public void setDownloadHelpr(KTDownloadHelper downloadHelper) {
        if (downloadHelper!=null){
           // this.downloadHelpr=downloadHelper;
        }
    }

    List<EnveuVideoItemBean> videoItemBeansList;
    public void notifyDataSetChanged(int position, String findAssetId, List<EnveuVideoItemBean> videoItemBeans) {
        videoItemBeansList=videoItemBeans;
        for (int i=0;i<videoItemBeans.size();i++){
            notifyItemChanged(i,PAY3);
        }

    }

    public void downloadCompletChanged(int position, String findAssetId, List<EnveuVideoItemBean> videoItemBeans) {
        videoItemBeansList=videoItemBeans;
        for (int i=0;i<videoItemBeans.size();i++){
            if (findAssetId.equalsIgnoreCase(videoItemBeans.get(i).getkEntryId())){
                notifyItemChanged(i,PAY4);
            }
        }

    }

    public void downloadStatusChanged(int position, String findAssetId, List<EnveuVideoItemBean> videoItemBeans) {
        videoItemBeansList=videoItemBeans;
        for (int i=0;i<videoItemBeans.size();i++){
            if (findAssetId.equalsIgnoreCase(videoItemBeans.get(i).getkEntryId())){
                notifyItemChanged(i,PAY5);
            }
        }

    }


    String PAY3="payload3";
    String PAY4="payload4";
    String PAY5="payload5";
    @Override
    public void onBindViewHolder(@NonNull SeasonViewHolder holder, int position, @NonNull List<Object> payloads) {
        Log.e("statusDown","onBind");
        if (payloads!=null && payloads.size()>0){
            for (int i=0;i<payloads.size();i++){
                if (payloads.get(i).equals(PAY3)){
                    if (downloadHelper!=null){
                        Log.e("statusDown","ouut");
                        holder.itemBinding.setDownloadStatus(DownloadStatus.REQUESTED);
                    }

                }else if (payloads.get(i).equals(PAY4)){
                    holder.itemBinding.setDownloadStatus(DownloadStatus.DOWNLOADED);
                }
                else if (payloads.get(i).equals(PAY5)){
                    DownloadUtils.INSTANCE.setDownloadStatus(holder.itemBinding,position,videoItemBeans.get(position),downloadHelper);
                }
            }
        }else {
            super.onBindViewHolder(holder, position, payloads);
        }

    }

    public void itemStatusChanged(String assetId) {
        for (int i = 0; i < videoItemBeans.size(); i++) {
            if (assetId.equalsIgnoreCase(videoItemBeans.get(i).getkEntryId())) {
                notifyItemChanged(i);
            }
        }
    }

    public void downloadStatusChanged(List<EnveuVideoItemBean> adapterList) {
        try {
            for (int i = 0; i < videoItemBeans.size(); i++) {
                if (videoItemBeans.get(i).getkEntryId()!=null && !videoItemBeans.get(i).getkEntryId().equalsIgnoreCase("")) {
                    OfflineManager.AssetInfo info=downloadHelper.getManager().getAssetInfo(videoItemBeans.get(i).getkEntryId());
                    Log.w("infoValue-->",info+"");
                    if (info!=null){

                    }else {
                        notifyItemChanged(i,PAY3);
                    }
                }
            }
        }catch (Exception ignored){

        }
    }

    public void notifySingleItem(String getkEntryId) {
        for (int i = 0; i < videoItemBeans.size(); i++) {
            if (getkEntryId!=null && !getkEntryId.equalsIgnoreCase("") && videoItemBeans.get(i).getkEntryId().equalsIgnoreCase(getkEntryId)) {
                notifyItemChanged(i,PAY3);
            }
        }
    }


    public class SeasonViewHolder extends RecyclerView.ViewHolder {


        final RowEpisodeListBinding itemBinding;

        SeasonViewHolder(RowEpisodeListBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

    public void updateCurrentId(int id) {
        currentAssetId=id;
    }

    String findAssetId = "";
    @Override
    public void setDownloadProgressListener(float progress, String assetId) {
        Log.w("downloadPro->>progres->",assetId+" "+onDownloadClickInteraction+" "+downloadHelper);
        if (!findAssetId.equalsIgnoreCase(assetId)) {
            findAssetId = assetId;

        }
        if (onDownloadClickInteraction!=null){
            onDownloadClickInteraction.fromAdapterDownloadProgress(progress,assetId);
        }
       // itemChanged(assetId);
    }

    String assetID="";
    int position=0;
    public void itemChanged(String assetId) {

            Log.w("Condition--1",assetId+" "+position);
            if (assetID.equalsIgnoreCase(assetId)){
                Log.w("Condition--11",assetId);
                notifyItemChanged(position);
            }else {
                for (int i = 0; i < videoItemBeans.size(); i++) {
                    if (assetId.equalsIgnoreCase(videoItemBeans.get(i).getkEntryId())) {
                        Log.w("Condition--2",assetId+"   "+i);
                        assetID=videoItemBeans.get(i).getkEntryId();
                        position=i;
                        notifyItemChanged(i);
                    }
                }
            }


    }

    @Override
    public void onDownloadPaused(@NonNull @NotNull String assetId) {
        if (context!=null && !context.isFinishing()){
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    itemStatusChanged(assetId);
                }
            });
        }
        if (onDownloadClickInteraction!=null){
            onDownloadClickInteraction.fromAdapterPaused(assetId);
        }
    }

    @Override
    public void initialStatus(@NonNull @NotNull OfflineManager.AssetDownloadState state) {

    }

    @Override
    public void onStateChanged(@NonNull @NotNull OfflineManager.AssetDownloadState state,String assetId) {
        Log.w("onStateChanged",state.toString());
        if (context!=null && !context.isFinishing()){
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    itemStatusChanged(assetId);
                }
            });
        }
        if (onDownloadClickInteraction!=null){
            onDownloadClickInteraction.fromAdapterStatus(state,assetId);
        }

    }

    @Override
    public void onAssetDownloadComplete(@NonNull @NotNull String assetId) {
        if (context!=null && !context.isFinishing()){
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    itemStatusChanged(assetId);
                }
            });
        }
    }

    @Override
    public void onAssetDownloadFailed(@NonNull @NotNull String assetId, Exception e) {
       // itemChanged(assetId);
    }

    public interface EpisodeItemClick {

        void onItemClick(EnveuVideoItemBean assetId, boolean isPremium);

    }


    private void setDownloadStatus(SeasonViewHolder holder, int position, EnveuVideoItemBean enveuVideoItemBean) {
       Log.w("statusDown","condition--"+position+" "+enveuVideoItemBean.getkEntryId()+" "+ downloadHelper);
        if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus()){
            if (downloadHelper!=null){
                DownloadUtils.INSTANCE.setDownloadStatus(holder.itemBinding,position,enveuVideoItemBean,downloadHelper);
            }else {
                holder.itemBinding.setDownloadStatus(DownloadStatus.START);
            }
        }else {
            holder.itemBinding.setDownloadStatus(DownloadStatus.START);
        }
    }

    boolean isPlayerStart=false;
    public void isPlayerStart(boolean b) {
        isPlayerStart=b;
    }


}
