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

import panteao.make.ready.utils.helpers.downloads.OnDownloadClickInteraction;
import panteao.make.ready.R;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.RowEpisodeListBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.helpers.ImageHelper;

import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.HashMap;
import java.util.List;

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>{
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

    public SeasonAdapter(Activity context, List<EnveuVideoItemBean> videoItemBeans, int id, int currentAssetId, EpisodeItemClick listner) {
        this.context = context;
        this.videoItemBeans = videoItemBeans;
        this.listner = listner;
        this.id = id;
        this.currentAssetId = currentAssetId;
      //  Collections.sort(videoItemBeans, new SortSeasonAdapterItems());
        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();
//        downloadHelper = new DownloadHelper((Activity) context, this);
        onDownloadClickInteraction = (OnDownloadClickInteraction) context;
        buildIndexMap();
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
        }
        if(AppCommonMethod.getCheckBCID(videoItemBeans.get(position).getBrightcoveVideoId())) {
//            downloadHelper.findVideo(videoItemBeans.get(position).getBrightcoveVideoId(), new VideoListener() {
//                @Override
//                public void onVideo(Video video) {
//                    if (SDKConfig.getInstance().isDownloadEnable()){
//                        if (MediaTypeCheck.isMediaTypeSupported(videoItemBeans.get(position).getAssetType())){
//                            if (video.isOfflinePlaybackAllowed()) {
//                                holder.itemBinding.setIsDownloadable(false);
//                                updateDownloadStatus(holder, position);
//                            } else {
//                                holder.itemBinding.setIsDownloadable(false);
//                            }
//                        }
//
//                    }
//                }
//            });
        }
        if (videoItemBeans.get(position).getEpisodeNo() != null && videoItemBeans.get(position).getEpisodeNo() instanceof String && !((String) videoItemBeans.get(position).getEpisodeNo()).equalsIgnoreCase("")) {
            String episodeNum =  (String) videoItemBeans.get(position).getEpisodeNo();
            int eNum = Integer.parseInt(episodeNum);
            holder.itemBinding.titleWithSerialNo.setText(eNum + ". " + videoItemBeans.get(position).getTitle());
        } else {
            holder.itemBinding.titleWithSerialNo.setText(videoItemBeans.get(position).getTitle());

        }
        ImageHelper.getInstance(context).loadListImage(holder.itemBinding.episodeImage, videoItemBeans.get(position).getPosterURL());

        holder.itemBinding.description.setText(videoItemBeans.get(position).getDescription());

        try {
            if (!StringUtils.isNullOrEmpty(String.valueOf(videoItemBeans.get(position).getDuration()))) {

                double d = (double) videoItemBeans.get(position).getDuration();
                long x = (long) d; // x = 1234
                Log.w("episodeTiming",x+"");
                String minutes=AppCommonMethod.calculateTimeinMinutes((x));
                if (!minutes.equalsIgnoreCase("")){
                    minutes=minutes+" "+context.getResources().getString(R.string.minutes);
                    Log.d("time",minutes);
                    holder.itemBinding.duration.setText(minutes);
                }

            } else {
                holder.itemBinding.duration.setText("00:00");
            }
        }catch (Exception ignored){

        }


        if (videoItemBeans.get(position).getId() == currentAssetId) {
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

        holder.itemBinding.mainLay.setOnClickListener(view -> {
            PrintLogging.printLog("", "positionIs" + videoItemBeans.get(position));
            id = videoItemBeans.get(position).getId();
            notifyDataSetChanged();
        });

        holder.itemBinding.downloadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBinding=holder.itemBinding;
                onDownloadClickInteraction.onDownloadClicked(videoItemBeans.get(position).getBrightcoveVideoId(), videoItemBeans.get(position).getEpisodeNo(), this);
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
                onDownloadClickInteraction.onProgressbarClicked(v, this, videoItemBeans.get(position).getBrightcoveVideoId());


            }
        });
        holder.itemBinding.pauseDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemBinding.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.REQUESTED);
                onDownloadClickInteraction.onPauseClicked(videoItemBeans.get(position).getBrightcoveVideoId(), this);

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
                        onDownloadClickInteraction.onDownloadDeleted(enveuVideoItemBean.getBrightcoveVideoId(),enveuVideoItemBean);
                        break;
                    case R.id.my_Download:
                        new ActivityLauncher(context).launchMyDownloads("");
                        break;
                }
                return false;
            }
        });
    }

    private void updateDownloadStatus(SeasonViewHolder holder, int position) {
//        downloadHelper.getCatalog().getVideoDownloadStatus(videoItemBeans.get(position).getBrightcoveVideoId(), new OfflineCallback<DownloadStatus>() {
//            @Override
//            public void onSuccess(DownloadStatus downloadStatus) {
//                Logger.e("SeasonAdapter", String.valueOf(downloadStatus.getCode()));
//                switch (downloadStatus.getCode()) {
//                    case DownloadStatus.STATUS_NOT_QUEUED: {
//                        holder.itemBinding.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.START);
//                    }
//                    break;
//                    case DownloadStatus.STATUS_PENDING:
//                    case DownloadStatus.STATUS_QUEUEING: {
//                        holder.itemBinding.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.DOWNLOADING);
//                    }
//                    break;
//                    case DownloadStatus.STATUS_COMPLETE: {
//                        holder.itemBinding.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.DOWNLOADED);
//                    }
//                    break;
//                    case DownloadStatus.STATUS_CANCELLING: {
//                        holder.itemBinding.videoDownloading.setProgress(0);
//                    }
//                    break;
//                    case DownloadStatus.STATUS_PAUSED: {
//                        holder.itemBinding.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.PAUSE);
//                    }
//                    break;
//                    case DownloadStatus.STATUS_DOWNLOADING: {
//                        holder.itemBinding.setDownloadStatus(panteao.make.ready.enums.DownloadStatus.DOWNLOADING);
//                        holder.itemBinding.videoDownloading.setProgress((float) downloadStatus.getProgress());
//                    }
//                    break;
//
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return videoItemBeans.size();
    }

//    @Override
//    public void onDownloadRequested(@NonNull Video video) {
//        notifyVideoChanged(video.getId());
//    }
//
//    private void notifyVideoDownloadRequested(String videoId) {
//        if (indexMap.containsKey(videoId)) {
//            int index = (int) indexMap.get(videoId);
//            for (EnveuVideoItemBean videoItemBean :
//                    videoItemBeans) {
//                if (videoItemBean.getBrightcoveVideoId().equals(videoId)) {
//                    videoItemBeans.set(index, videoItemBean);
//                    notifyItemChanged(index);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onDownloadStarted(@NonNull Video video, long l, @NonNull Map<String, Serializable> map) {
//        notifyVideoChanged(video.getId());
//    }
//
//    @Override
//    public void onDownloadProgress(@NonNull Video video, @NonNull com.brightcove.player.network.DownloadStatus downloadStatus) {
//        notifyVideoChanged(video.getId());
//
//    }
//
//    @Override
//    public void onDownloadPaused(@NonNull Video video, @NonNull com.brightcove.player.network.DownloadStatus downloadStatus) {
//        notifyVideoChanged(video.getId());
//    }
//
//    @Override
//    public void onDownloadCompleted(@NonNull Video video, @NonNull com.brightcove.player.network.DownloadStatus downloadStatus) {
//        notifyVideoChanged(video.getId());
//    }
//
//    @Override
//    public void onDownloadCanceled(@NonNull Video video) {
//        notifyVideoChanged(video.getId());
//    }
//
//    @Override
//    public void onDownloadDeleted(@NonNull Video video) {
//        notifyVideoChanged(video.getId());
//        onDownloadClickInteraction.onDownloadCompleteClicked(null, this, video.getId());
//    }
//
//    @Override
//    public void onDownloadFailed(@NonNull Video video, @NonNull com.brightcove.player.network.DownloadStatus downloadStatus) {
//
//    }

//    @Override
//    public void downloadVideo(@NonNull Video video) {
//
//    }
//
//    @Override
//    public void pauseVideoDownload(Video video) {
//
//    }
//
//    @Override
//    public void resumeVideoDownload(Video video) {
//
//    }
//
//    @Override
//    public void deleteVideo(@NonNull Video video) {
//
//    }
//
//    @Override
//    public void alreadyDownloaded(@NonNull Video video) {
//
//    }
//
//    @Override
//    public void downloadedVideos(@Nullable List<? extends Video> p0) {
//
//    }
//
//    @Override
//    public void videoFound(Video video) {
//
//    }
//
//    @Override
//    public void downloadStatus(String videoId, DownloadStatus downloadStatus) {
//
//    }

//    @Override
//    public void downloadStatus(String videoId, com.brightcove.player.network.DownloadStatus downloadStatus) {
//
//    }

    public String getEpisodeNumber(String videoId) {
        for (EnveuVideoItemBean enveuVideoItemBean :
                videoItemBeans) {
            if (enveuVideoItemBean.getkEntryId().equals(videoId)) {
                return String.valueOf(enveuVideoItemBean.getEpisodeNo());
            }
        }
        return null;
    }

    public void updateCurrentId(int id) {
        currentAssetId=id;
    }

    public interface EpisodeItemClick {

        void onItemClick(EnveuVideoItemBean assetId, boolean isPremium);

    }

    public class SeasonViewHolder extends RecyclerView.ViewHolder {


        final RowEpisodeListBinding itemBinding;

        SeasonViewHolder(RowEpisodeListBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }
}
