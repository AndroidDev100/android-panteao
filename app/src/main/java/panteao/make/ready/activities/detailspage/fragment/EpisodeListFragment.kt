package panteao.make.ready.activities.detailspage.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.HeadersSupportFragment
import androidx.leanback.widget.*
import panteao.make.ready.R
import panteao.make.ready.activities.detailspage.SeasonSelectionManager
import panteao.make.ready.activities.detailspage.listeners.BottomFragmentListener
import panteao.make.ready.activities.detailspage.listeners.OnFocusListener
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.cardlayout.cardpresenter.SeriesCardPresenter
import panteao.make.ready.tvBaseModels.basemodels.TVBaseFragment
import java.util.*


class EpisodeListFragment : TVBaseFragment(), OnItemViewClickedListener {


    private var rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

    private lateinit var gridPresenter: ListRowPresenter
    private lateinit var mBackgroundManager: BackgroundManager
    private var mGridPresenter: Presenter? = null
    private var gridRowAdapter: ArrayObjectAdapter? = null
    private lateinit var seasonEpisodeData: ArrayList<EnveuVideoItemBean>
    private var mListenerOn: OnFocusListener? = null
    private var mBottomFragmentListener: BottomFragmentListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gridPresenter = ListRowPresenter(FocusHighlight.ZOOM_FACTOR_NONE, false)
        mBackgroundManager = BackgroundManager.getInstance(activity)
        onItemViewClickedListener = this
        getSeriesEpisode()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListenerOn = context as OnFocusListener
        mBottomFragmentListener = context as BottomFragmentListener
    }

    override fun onCreateHeadersSupportFragment(): HeadersSupportFragment {
        super.setHeadersState(HEADERS_DISABLED)
        return super.onCreateHeadersSupportFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.MyMaterialTheme)
        val localInflater = inflater.cloneInContext(contextThemeWrapper)
        return super.onCreateView(localInflater, container, savedInstanceState)
    }

    private fun getSeriesEpisode() {
        seasonEpisodeData = SeasonSelectionManager.getInstance().seasonEpisodeList

        setRows(seasonEpisodeData)

        adapter = rowsAdapter
    }

    private fun setRows(items: List<EnveuVideoItemBean?>?) {
        mGridPresenter = SeriesCardPresenter()
        gridRowAdapter = ArrayObjectAdapter(mGridPresenter)
        gridRowAdapter?.addAll(0, items)
        if (gridRowAdapter?.size() == 0) {
            val gridHeaderItem = HeaderItem(rowsAdapter.size().toLong(), "")
            val listRow = ListRow(gridHeaderItem, gridRowAdapter)
            rowsAdapter.add(listRow)
        } else {
            val gridHeader = HeaderItem(rowsAdapter.size().toLong(), "Episode")
            val listRow = ListRow(gridHeader, gridRowAdapter)
            rowsAdapter.add(listRow)
        }

    }


    override fun onItemClicked(
        p0: Presenter.ViewHolder?,
        asset: Any?,
        p2: RowPresenter.ViewHolder?,
        p3: Row?
    ) {
        if (asset is EnveuVideoItemBean) {
            mBottomFragmentListener?.onEpisodeClicked(asset.id)
        }
    }


    fun onKeypadClicked(keyCode: Int) {
        view?.let {
            if (it.hasFocus()) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    mListenerOn?.showFragment(true)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        view?.isFocusableInTouchMode = false
        view?.isFocusable = false

    }

    override fun onDetach() {
        super.onDetach()
        mListenerOn = null
        mBottomFragmentListener = null
    }

    companion object {
        fun newInstance() = EpisodeListFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}