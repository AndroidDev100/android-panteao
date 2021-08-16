package panteao.make.ready.activities.myprofile

import android.content.Intent
import android.os.Bundle
import panteao.make.ready.R
import panteao.make.ready.activities.homeactivity.ui.TVHomeActivity
import panteao.make.ready.fragments.LogOutDialogFragment
import panteao.make.ready.tvBaseModels.basemodels.TVBaseActivity
import panteao.make.ready.utils.helpers.SharedPrefHelper
import panteao.make.ready.utils.helpers.database.preferences.UserPreference

class TVMyProfileAcitivity : TVBaseActivity(), LogOutDialogFragment.onFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
    }

    override fun onExitClicked() {
        UserPreference.instance.clear()
//        SharedPrefHelper.getInstance()?.clear()
        SharedPrefHelper(this).clearRecentSearches()
        startActivity(Intent(this@TVMyProfileAcitivity, TVHomeActivity::class.java))
        finish()
    }

    override fun onCancelClicked() {

    }
}