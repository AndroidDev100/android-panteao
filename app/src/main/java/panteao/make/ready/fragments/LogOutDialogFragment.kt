package panteao.make.ready.fragments

import android.content.Context
import android.os.Bundle
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.leanback.widget.GuidedAction
import panteao.make.ready.R
import panteao.make.ready.utils.constants.AppConstants


class LogOutDialogFragment : GuidedStepSupportFragment() {
    var onFragmentInteraction: onFragmentInteractionListener? = null
    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        return GuidanceStylist.Guidance(
            getString(R.string.sign_out),
            getString(R.string.logout_confirmation),
            getString(R.string.blank),
            activity?.getDrawable(R.drawable.makereadytv_logo)
        )
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        super.onCreateActions(actions, savedInstanceState)
        actions.add(
            GuidedAction.Builder()
                .id(AppConstants.GUIDED_EXIT)
                .title(getString(R.string.guidedstep_ok))
                .build()
        )
        actions.add(
            GuidedAction.Builder()
                .id(AppConstants.GUIDED_CANCEL)
                .title(getString(R.string.guidedstep_cancel))
                .build()
        )

    }

    override fun onGuidedActionClicked(action: GuidedAction?) {
        super.onGuidedActionClicked(action)
        when (action?.id) {
            AppConstants.GUIDED_EXIT -> {
                onFragmentInteraction?.onExitClicked()
            }
            AppConstants.GUIDED_CANCEL -> {

            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity !is onFragmentInteractionListener) {
//            Toast.makeText(activity, "Activity Must implement onFragmentInteraction", Toast.LENGTH_LONG).show()
            view?.setBackgroundColor(getResources().getColor(R.color.black_theme_color))
        } else {
            onFragmentInteraction = activity as onFragmentInteractionListener
        }
    }


    interface onFragmentInteractionListener {
        fun onExitClicked()
        fun onCancelClicked()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            LogOutDialogFragment()
    }
}
