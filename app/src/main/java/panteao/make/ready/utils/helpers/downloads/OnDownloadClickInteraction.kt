package panteao.make.ready.utils.helpers.downloads

import android.view.View

interface OnDownloadClickInteraction {
    //videoId null in case of click from UserInteractionFragment
    fun onDownloadClicked(videoId: String, position: Any, source: Any)

    fun onProgressbarClicked(view: View, source: Any, videoId: String)

    fun onDownloadCompleteClicked(view: View, source: Any, videoId: String)

    fun onPauseClicked(videoId: String, source: Any)

    fun onDownloadDeleted(videoId: String, source: Any)
}