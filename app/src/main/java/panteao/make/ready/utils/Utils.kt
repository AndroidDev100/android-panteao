package panteao.make.ready.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import panteao.make.ready.PanteaoApplication
import panteao.make.ready.R
import panteao.make.ready.activities.videoquality.bean.TrackItem
import panteao.make.ready.utils.constants.AppConstants
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object Utils {
    val isInternetConnected: Boolean
        get() {
            val cm =
                PanteaoApplication.getInstance()?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }
//
//    fun animateView(view: View, direction: Int) {
//        var swipeAnimation: Animation? = null
//        when (direction) {
//            AppConstants.ANIMATION_LEFT -> {
//                swipeAnimation = AnimationUtils.loadAnimation(view.context, R.anim.slide_left)
//            }
//            AppConstants.ANIMATION_RIGHT -> {
//                swipeAnimation = AnimationUtils.loadAnimation(view.context, R.anim.slide_right)
//            }
//        }
//        swipeAnimation?.fillAfter = true
//        view.startAnimation(swipeAnimation)
//    }


    fun increaseDialogSize(dialog: Dialog) {
        dialog.window?.attributes.let { it }?.width =
            android.view.WindowManager.LayoutParams.MATCH_PARENT
        dialog.window?.attributes.let { it }?.height =
            android.view.WindowManager.LayoutParams.MATCH_PARENT
    }


    fun stringForTime(timeMs: Long): String {
        var formatBuilder = StringBuilder()
        var formatter = Formatter(formatBuilder, Locale.getDefault())
        val totalSeconds = (timeMs + 500) / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        formatBuilder.setLength(0)
        return if (hours > 0)
            formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        else
            formatter.format("%02d:%02d", minutes, seconds).toString()
    }

    fun isValidEmailAddress(email: String): Boolean {
        val pattern = Pattern.compile(AppConstants.EMAIL_REGEX)
        return pattern.matcher(email).matches()
    }

    fun isValidPasswordSize(password: String): Boolean {
        return (password.length in 4..8)
    }

    fun isValidPasswordPattern(password: String): Boolean {
        return Pattern.compile("[0-9]").matcher(password).find()
    }

    fun hideSoftKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        Objects.requireNonNull(imm).hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun dpToPx(dip: Float): Float {
        val r: Resources = PanteaoApplication.getInstance()?.getResources()!!
        val px: Float = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            r.getDisplayMetrics()
        )
        return px
    }

    fun getFilteredUrl(imageUrl: String, width: Int, Height: Int): String {
        return AppConstants.VIDEO_CLOUD_FRONT_URL + width.toString() + "x" + Height.toString() + AppConstants.FILTER_PLAYER_BANNER + "/" + imageUrl
    }

    fun getDateDDMMMYYYY(milliSeconds: Long): String? {
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    var track1 = false
    var track2 = false
    var track3 = false

    //    fun createTrackList(
//        videoTrackArray: List<Format>,
//        context: Activity
//    ): ArrayList<TrackItem>? {
//        track1 = false
//        track2 = false
//        track3 = false
//        val arrayList: ArrayList<TrackItem> = ArrayList<TrackItem>()
//        try {
//            // mActivity.getResources().getString(R.string.caption_selection)
//            arrayList.add(
//                TrackItem(
//                    context.resources.getString(R.string.auto),
//                    0,
//                    "",
//                    0,
//                    "Auto"
//                )
//            )
//            for (i in videoTrackArray.indices) {
//                val videoTrackInfo = videoTrackArray[i]
////                Log.e(
////                    "tracksVideoBitrate",
////                    videoTrackArray[i].bitrate.toString() + ""
////                )
//                if (videoTrackInfo.height in 270..359 && !track1) {
//                    track1 = true
//                    arrayList.add(
//                        TrackItem(
//                            context.resources.getString(R.string.low),
//                            videoTrackArray[i].bitrate,
//                            "",
//                            i + 1,
//                            "Low"
//                        )
//                    )
//                } else if (videoTrackInfo.height in 540..719 && !track2) {
//                    track2 = true
//                    arrayList.add(
//                        TrackItem(
//                            context.resources.getString(R.string.medium),
//                            videoTrackInfo.bitrate,
//                            "",
//                            i + 1,
//                            "Medium"
//                        )
//                    )
//                } else if (videoTrackInfo.height >= 1080 && !track3) {
//                    track3 = true
//                    arrayList.add(
//                        TrackItem(
//                            context.resources.getString(R.string.high),
//                            videoTrackArray[i].bitrate,
//                            "",
//                            i + 1,
//                            "High"
//                        )
//                    )
//                }
//            }
//        } catch (e: Exception) {
//        }
//        return arrayList
//    }


}
