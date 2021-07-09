package panteao.make.ready.utils.helpers.intentlaunchers

import android.app.Activity
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import panteao.make.ready.activities.detailspage.activity.TVInstructorDetailsActivity
import panteao.make.ready.activities.detailspage.activity.TVSeriesDetailActivity
import panteao.make.ready.activities.detailspage.activity.VideoDetailActivity
import panteao.make.ready.activities.homeactivity.ui.HomeActivity
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.utils.constants.AppConstants

class TvActivityLauncher() {

    fun homeScreen(activity: Activity, destination: Class<HomeActivity>) {
        val intent = Intent(activity, destination)
        TaskStackBuilder.create(activity).addNextIntentWithParentStack(intent).startActivities()
    }


    fun detailScreenBrightCove(
        activity: Activity, id: Int, series: String,
        season: String, isPremium: Boolean, detailType: String
    ) {
        val args = Bundle()
        val intent = Intent(activity, VideoDetailActivity::class.java)
        intent.putExtra(AppConstants.SELECTED_ITEM, id)
        intent.putExtra(AppConstants.SELECTED_CONTENT_TYPE, id)
        //Log.e("JSON SENT", Gson().toJson(args))
        activity.startActivity(intent)
    }

    fun seriesDetailScreen(
        activity: Activity,
        seriesId: Int
    ) {
        val intent = Intent(activity, TVSeriesDetailActivity::class.java)
        intent.putExtra(AppConstants.SELECTED_ITEM, seriesId)
        activity.startActivity(intent)
    }

    fun instructorDetailsScreen(
        activity: Activity,
        seriesId: EnveuVideoItemBean
    ) {
        val intent = Intent(activity, TVInstructorDetailsActivity::class.java)
        intent.putExtra(AppConstants.SELECTED_ITEM, seriesId)
        activity.startActivity(intent)
    }

    companion object {
        private var instance: TvActivityLauncher? = null
        fun getInstance(): TvActivityLauncher {
            if (instance == null)
                instance = TvActivityLauncher()
            return instance as TvActivityLauncher
        }
    }


}
