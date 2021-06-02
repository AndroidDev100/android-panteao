package panteao.make.ready.callbacks.commonCallbacks

interface OnPopularSearchInteractionListener {
    fun showNoDataFoundView(show: Boolean, msg: String)
    fun showProgressBarView(show: Boolean)
    fun noPopularSearchesFound()
    fun onRecentSearchClicked(recentSearchText: String)
    fun onUpdateRecentSearches(recentSearchText: String)
}