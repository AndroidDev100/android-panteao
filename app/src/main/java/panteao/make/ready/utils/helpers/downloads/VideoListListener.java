package panteao.make.ready.utils.helpers.downloads;


import androidx.annotation.NonNull;

import com.brightcove.player.model.Video;
import com.brightcove.player.network.DownloadStatus;
import panteao.make.ready.fragments.player.ui.UserInteractionFragment;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import panteao.make.ready.fragments.player.ui.UserInteractionFragment;


/**
 * The contract of listener that can handle user interactions on a video item display.
 */
public interface VideoListListener {
    /**
     * This method will be called by the {@link UserInteractionFragment} when the user touches the
     * Download button. The handler is expected to initiate download of the specified video.
     *
     * @param video reference to the video.
     */
    void downloadVideo(@NonNull Video video);

    void pauseVideoDownload(@NonNull Video video);

    void resumeVideoDownload(@NonNull Video video);

    void deleteVideo(@NonNull Video video);

    void alreadyDownloaded(@NonNull Video video);

    void downloadedVideos(@Nullable List<? extends Video> p0);

    void videoFound(Video video);
    void downloadStatus(String videoId,DownloadStatus downloadStatus);
}