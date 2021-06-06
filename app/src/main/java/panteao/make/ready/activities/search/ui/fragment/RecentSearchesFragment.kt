package panteao.make.ready.activities.search.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import panteao.make.ready.callbacks.commonCallbacks.OnPopularSearchInteractionListener
import panteao.make.ready.R
import panteao.make.ready.activities.search.ui.TVSearchActivity
import panteao.make.ready.cardlayout.cardpresenter.RecentSearchesCardPresenter
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.utils.cropImage.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.SharedPrefHelper

class RecentSearchesFragment : VerticalGridSupportFragment(), OnItemViewClickedListener,
    NoInternetFragment.OnFragmentInteractionListener, OnItemViewSelectedListener {
    private var mOnPopularSearchInteractionListener: OnPopularSearchInteractionListener? = null
    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnPopularSearchInteractionListener = context as TVSearchActivity
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
        setOnItemViewSelectedListener(this)
    }

    private fun setupFragment() {
        val gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_NONE, false)
        gridPresenter.numberOfColumns = NUM_COLUMNS
        setGridPresenter(gridPresenter)
       updateRecentSearches()
    }

    private fun updateRecentSearches() {
        val mAdapter = ArrayObjectAdapter(RecentSearchesCardPresenter())
        val languageList = ArrayList<String>()
        var i = 1;
        while (i < 6) {
            if (SharedPrefHelper(requireContext()).getRecentSearches()!!.size >= i) {
                languageList.add(
                        SharedPrefHelper(requireContext())
                                .recentSearches!![SharedPrefHelper(requireContext())
                                        .recentSearches!!.size - i]
                );
                i++
            } else {
                break
            }
        }
        languageList.add(getString(R.string.clear_searches))
        mAdapter.addAll(0, languageList)
        adapter = mAdapter
    }

    override fun onItemClicked(
        p0: Presenter.ViewHolder?,
        contentItem: Any?,
        p2: RowPresenter.ViewHolder?,
        p3: Row?
    ) {
        mOnPopularSearchInteractionListener?.onRecentSearchClicked(contentItem.toString())
    }


    override fun onFragmentInteraction() {
        connectionObserver()
    }

    interface OnFragmentInteractionListener {
        fun nodataFound(show: Boolean)
    }


    companion object {

        private val NUM_COLUMNS = 6
    }

    override fun onItemSelected(
        itemViewHolder: Presenter.ViewHolder?,
        item: Any?,
        rowViewHolder: RowPresenter.ViewHolder?,
        row: Row?
    ) {

    }

    fun onUpkeyClicked() {
        requireActivity().findViewById<SearchBar>(R.id.search_view).findViewById<EditText>(R.id.lb_search_text_editor).requestFocus()
    }
    fun onDownKeyClicked() {
        requireActivity().findViewById<FrameLayout>(R.id.keyword_search_result_fragment).requestFocus()
    }
    fun notifyDataSetChanged() {
        updateRecentSearches()
    }
}