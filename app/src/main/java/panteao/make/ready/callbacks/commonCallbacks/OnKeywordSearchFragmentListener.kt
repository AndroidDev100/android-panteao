package panteao.make.ready.callbacks.commonCallbacks

interface OnKeywordSearchFragmentListener {
    fun showNoDataFoundView(show: Boolean, msg: String)
    fun showProgressBarView(show: Boolean)
    fun searchResultsFound(searchKeyword: String)
}