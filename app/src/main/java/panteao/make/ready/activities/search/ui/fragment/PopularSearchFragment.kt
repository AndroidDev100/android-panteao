package panteao.make.ready.activities.search.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.Observer
import com.make.baseCollection.baseCategoryModel.BaseCategory
import panteao.make.ready.callbacks.commonCallbacks.OnPopularSearchInteractionListener
import panteao.make.ready.R
import panteao.make.ready.SDKConfig
import panteao.make.ready.activities.search.ui.TVSearchActivity
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.cardlayout.cardpresenter.PopularSearchCardPresenter
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.RailInjectionHelper


class PopularSearchFragment : VerticalGridSupportFragment(), OnItemViewClickedListener,
    NoInternetFragment.OnFragmentInteractionListener {


    private var mOnPopularSearchInteractionListener: OnPopularSearchInteractionListener? = null
    var channelId: String? = null
    private lateinit var railInjectionHelper:RailInjectionHelper
//    private lateinit var viewModel: SearchViewModel

    companion object {
        private val TAG = PopularSearchFragment::class.java.simpleName
        private val NUM_COLUMNS = 4
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnPopularSearchInteractionListener = context as TVSearchActivity
        railInjectionHelper=RailInjectionHelper(requireActivity().application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        connectionObserver()
    }

    private fun connectionObserver() {
        activity?.applicationContext?.let {
            if (NetworkConnectivity.isOnline(it)) {
                connectionValidation(true)
            } else {
                connectionValidation(false)
            }
        }
    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            setupEventListeners()
            setupFragment()
        } else {
            val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(android.R.id.content, NoInternetFragment())
            fragmentTransaction?.commit()
        }
    }

    override fun onFragmentInteraction() {
        connectionObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.context.setTheme(R.style.CustomBrowseTheme)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupEventListeners() {
        onItemViewClickedListener = this
    }

    private fun setupFragment() {
        val gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_NONE, false)
        gridPresenter.numberOfColumns =
            NUM_COLUMNS
        setGridPresenter(gridPresenter)
        mOnPopularSearchInteractionListener?.showNoDataFoundView(false, "")
        mOnPopularSearchInteractionListener?.showProgressBarView(true)
        val mAdapter = ArrayObjectAdapter(PopularSearchCardPresenter())
        activity?.let { fragmentActivity ->
            railInjectionHelper.getPlayListDetailsWithPagination(
                fragmentActivity,
                SDKConfig.getInstance().popularSearchId,
                0,
                5,
                BaseCategory()
            ).observe(this, Observer {
                if (it != null) {
                    if (it.enveuVideoItemBeans != null || it.enveuVideoItemBeans!!.size <= 0) {
                        mOnPopularSearchInteractionListener?.noPopularSearchesFound()
                    } else {
                        mAdapter.addAll(0, it.getEnveuVideoItemBeans())
                    }
                } else {
                    activity?.applicationContext?.let {
                        if (!NetworkConnectivity.isOnline(it)) {
                            Toast.makeText(
                                activity,
                                resources.getString(R.string.no_internet_connection),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                mOnPopularSearchInteractionListener?.showProgressBarView(false)
                mOnPopularSearchInteractionListener?.showNoDataFoundView(false, "")
            })

        }

        adapter = mAdapter
    }

    override fun onItemClicked(
        p0: Presenter.ViewHolder?,
        asset: Any?,
        p2: RowPresenter.ViewHolder?,
        p3: Row?
    ) {

//        if (asset is EnveuVideoItemBean) {
//            context?.let { context ->
//                if (asset.assetType.equals(MediaTypeConstants.getInstance().series)) {
//                    AppCommonMethod.launchDetailScreen(
//                        context,
//                        0L,
//                        AppConstants.Series,
//                        asset.id,
//                        "",
//                        "",
//                        "0",
//                        false
//                    )
//                } else {
//                    if (asset.assetType != null) {
//                        if (asset.season == null) {
//                            asset.assetType?.let { assetType ->
//                                asset.series?.let { series ->
//                                    AppCommonMethod.launchDetailScreen(
//                                        context,
//                                        0L,
//                                        assetType,
//                                        asset.id,
//                                        series,
//                                        "-1",
//                                        "0",
//                                        asset.isPremium
//                                    )
//                                }
//                            }
//                        } else if (asset.series == null) {
//                            asset.assetType?.let { assetType ->
//                                AppCommonMethod.launchDetailScreen(
//                                    context,
//                                    0L,
//                                    assetType,
//                                    asset.id,
//                                    " ",
//                                    " ",
//                                    "0",
//                                    asset.isPremium
//                                )
//                            }
//                        } else {
//                            asset.assetType?.let { assetType ->
//                                asset.series?.let { series ->
//                                    asset.season?.let { season ->
//                                        AppCommonMethod.launchDetailScreen(
//                                            context,
//                                            0L,
//                                            assetType,
//                                            asset.id,
//                                            series,
//                                            season,
//                                            "0",
//                                            asset.isPremium
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    } else {
//                        AppCommonMethod.launchDetailScreen(
//                            context,
//                            0L,
//                            AppConstants.Video,
//                            asset.id,
//                            "",
//                            "",
//                            "0",
//                            asset.isPremium
//                        )
//                    }
//                }
//            }
//        }
    }

    override fun onDetach() {
        super.onDetach()
        mOnPopularSearchInteractionListener = null
    }
}