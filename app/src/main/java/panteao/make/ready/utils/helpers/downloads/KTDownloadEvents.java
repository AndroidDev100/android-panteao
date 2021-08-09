package panteao.make.ready.utils.helpers.downloads;

import androidx.annotation.NonNull;

import com.kaltura.tvplayer.OfflineManager;

public interface KTDownloadEvents {
    void setDownloadProgressListener(float progress,String assetId);
    void onDownloadPaused(@NonNull String assetId);
    void initialStatus(@NonNull OfflineManager.AssetDownloadState state);
    void onStateChanged(@NonNull OfflineManager.AssetDownloadState state);
    void onAssetDownloadComplete(@NonNull String assetId);
    void onAssetDownloadFailed(@NonNull String assetId,Exception e);
    default void updateAdapter(int type){

    }
}
