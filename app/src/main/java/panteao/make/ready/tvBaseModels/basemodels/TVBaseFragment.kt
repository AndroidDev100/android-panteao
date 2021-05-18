package panteao.make.ready.tvBaseModels.basemodels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.BrowseSupportFragment


open class TVBaseFragment : BrowseSupportFragment(),
    BrowseSupportFragment.MainFragmentAdapterProvider {
    private val mMainFragmentAdapter = BrowseSupportFragment.MainFragmentAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        inflater.context.setTheme(R.style.CustomBrowseTheme)
        return super.onCreateView(inflater, container, savedInstanceState)

    }
    override fun getMainFragmentAdapter(): MainFragmentAdapter<*> {
        return mMainFragmentAdapter
    }

    private lateinit var viewModel: Any

    fun addFragment(activity: FragmentActivity?, fragment: Fragment, addToBackStack: Boolean) {
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        if (addToBackStack) {
            fragmentTransaction?.replace(android.R.id.content, fragment)?.addToBackStack(null)
        } else {
            fragmentTransaction?.replace(android.R.id.content, fragment)
        }
        fragmentTransaction?.commit()
    }
}