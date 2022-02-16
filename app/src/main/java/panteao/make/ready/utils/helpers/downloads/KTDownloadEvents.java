package panteao.make.ready.utils.helpers.downloads;

import androidx.annotation.NonNull;

import com.kaltura.tvplayer.OfflineManager;

public interface KTDownloadEvents {
    void setDownloadProgressListener(float progress,String assetId);
    void onDownloadPaused(@NonNull String assetId);
    void initialStatus();
    void onStateChanged(@NonNull OfflineManager.AssetDownloadState state,String assetId);
    void onAssetDownloadComplete(@NonNull String assetId);
    void onAssetDownloadFailed();
    default void updateAdapter(int type){

    }
}
