package com.make.brightcovelibrary

import android.os.Bundle

import com.brightcove.player.model.MediaFormat
import com.brightcove.player.offline.MediaDownloadable

import java.util.ArrayList

/**
 * A download util class to select audio and caption tracks
 */
object BrightcoveDownloadUtil {

    private val TAG = BrightcoveDownloadUtil::class.java.simpleName

    /**
     * An example method that adds the following to the bundle:
     *
     * 1.  The main audio track, or, if it does not exist, the first audio track in the AUDIO_LANGUAGES array.
     * 2.  An "alternate" audio track, selected as the first audio track from the unselected audio tracks, if there are any remaining.
     * 3.  The first caption track in the CAPTION_LANGUAGES array as the "default" caption language track
     * 4.  An "alternate" caption track, selected as the first caption from the unselected caption tracks, if there are any remaining.
     *
     * @param mediaDownloadable - The MediaDownloadable object
     * @param bundle            - The app bundle
     */
    fun selectMediaFormatTracksAvailable(mediaDownloadable: MediaDownloadable, bundle: Bundle) {
        var didListChange: Boolean

        val audio = bundle.getParcelableArrayList<MediaFormat>(MediaDownloadable.AUDIO_LANGUAGES)
        var indexMain = -1
        val newAudio = ArrayList<MediaFormat>()

        // Check to see if there are audio tracks
        if (audio != null && audio.size > 0) {
//            Log.v(TAG, "Adding the \"main\" audio track.")
            //First let's find the index of the audio with role main
            val roles = bundle.getStringArrayList(MediaDownloadable.AUDIO_LANGUAGE_ROLES)
            for (i in roles!!.indices) {
                if ("main".equals(roles[i], ignoreCase = true)) {
                    indexMain = i
                    break
                }
            }

            //If indexMain equals -1, there was no main so we'll pick the first one
            if (indexMain == -1) {
                indexMain = 0
            }

            //Select main
            newAudio.add(audio[indexMain])
        }

        // Now select the "extra" audio track
        // In an effort to avoid over-complication of the flow of this demonstration app, we make an assumption here
        // that the end user has selected the first of the remaining audio tracks that is not the "main" audio track
        // (if more than one audio track is present)
        if (audio!!.size > 1) {
//            Log.v(TAG, "Alternate audio track download allowed for this video. Adding an \"alternate\" audio track")
            if (indexMain == 0) {
                //The first audio is also the main, so let's pick the second audio
                newAudio.add(audio[1])
            } else {
                //We'll pick the first audio as the extra
                newAudio.add(audio[0])
            }
        } else {
//            Log.v(TAG, "Alternate audio track download allowed, but there were no \"alternate\" audio tracks to select.")
        }
        bundle.putParcelableArrayList(MediaDownloadable.AUDIO_LANGUAGES, newAudio)
        didListChange = true

        // All captions are considered "extra" tracks for download
        // As with the alternate audio track selection above, we make an assumption here that the end user has selected the
        // first caption track as the "default" caption language, and the second caption track as the "alternate" (if more than
        // one caption track is present)
        val captions = bundle.getParcelableArrayList<MediaFormat>(MediaDownloadable.CAPTIONS)
//        Log.v(TAG, "Captions array size: " + captions!!.size)
        if (captions != null && captions.size > 0) {
            val newCaptions = ArrayList<MediaFormat>()
//            Log.v(TAG, "Adding the first caption track as the \"default\" caption track.")
            newCaptions.add(captions[0])

            if (captions.size > 1) {
//                Log.v(TAG, "Adding the first of the remaining caption tracks as the \"alternate\" caption track.")
                newCaptions.add(captions[1])
            } else {
//                Log.v(TAG, "Captions size is not GT 1, no alternate captions will be downloaded, even though allowed.")
            }

            bundle.putParcelableArrayList(MediaDownloadable.CAPTIONS, newCaptions)
            didListChange = true
        }

        // Why are we checking if the list has changed?
        // Because if we always set the bundle to the MediaDownloadble, we're telling it to download every audio track and
        // every caption track.
        if (didListChange) {
            mediaDownloadable.configurationBundle = bundle
        }
    }
}
