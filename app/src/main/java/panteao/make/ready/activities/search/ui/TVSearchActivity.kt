package panteao.make.ready.activities.search.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.speech.SpeechRecognizer
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.leanback.widget.SearchBar
import androidx.leanback.widget.SearchEditText
import androidx.leanback.widget.SpeechOrbView
import panteao.make.ready.activities.search.ui.fragment.KeyWordSearchFragment
import panteao.make.ready.R
import panteao.make.ready.activities.search.ui.fragment.PopularSearchFragment
import panteao.make.ready.activities.search.ui.fragment.RecentSearchesFragment
import panteao.make.ready.callbacks.commonCallbacks.DataUpdateCallBack
import panteao.make.ready.callbacks.commonCallbacks.OnKeywordSearchFragmentListener
import panteao.make.ready.callbacks.commonCallbacks.OnPopularSearchInteractionListener
import panteao.make.ready.databinding.ActivityTVSearchBinding
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.tvBaseModels.basemodels.TvBaseBindingActivity
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.SharedPrefHelper

class TVSearchActivity : TvBaseBindingActivity<ActivityTVSearchBinding>(), View.OnClickListener,
    NoInternetFragment.OnFragmentInteractionListener, OnKeywordSearchFragmentListener,
    OnPopularSearchInteractionListener, AdapterView.OnItemClickListener {
    private var dataUpdateCallBack: DataUpdateCallBack? = null
    var keyWordSearchFragment = KeyWordSearchFragment()
    private var popularSearchFragment = PopularSearchFragment()
    private var noInternetFragment = NoInternetFragment()
    private var recentSearchesFragment = RecentSearchesFragment()
    private val items = ArrayList<String>()

    private lateinit var itemsAdapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionObserver()
    }

    private fun connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true)
        } else {
            connectionValidation(false)
        }
    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            val mSpeechOrbView: SpeechOrbView =
                binding.searchView.findViewById(R.id.lb_search_bar_speech_orb)
            mSpeechOrbView.visibility = View.INVISIBLE
            val searchEditText: SearchEditText =
                binding.searchView.findViewById(R.id.lb_search_text_editor)
            searchEditText.requestFocus()
//            mSpeechOrbView.setOnClickListener {
//                if (ContextCompat.checkSelfPermission(
//                                this,
//                                Manifest.permission.RECORD_AUDIO
//                        ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    ActivityCompat.requestPermissions(
//                            this, arrayOf(Manifest.permission.RECORD_AUDIO),
//                            10001
//                    );
//                } else {
//                    binding.searchView.startRecognition()
//                }
//            }
            supportFragmentManager.beginTransaction()
                .add(R.id.recent_searches_layout, recentSearchesFragment, "RecentSearches")
                .commit()
            binding.searchView.setSpeechRecognizer(
                SpeechRecognizer.createSpeechRecognizer(
                    this
                )
            )
            binding.searchView.setSearchBarListener(object :
                SearchBar.SearchBarListener {
                override fun onSearchQueryChange(query: String?) {

                }

                override fun onSearchQuerySubmit(query: String?) {
                    startSearching(query!!)
                    SharedPrefHelper(this@TVSearchActivity).addRecentSearch(query)
                    items.clear()
                    SharedPrefHelper(this@TVSearchActivity).recentSearches?.let {
                        items.addAll(it)
                        if (items.size > 0) {
                            binding.recentSearchesLayout.visibility = View.VISIBLE
                            binding.recentSearchesText.visibility = View.VISIBLE
                        }
                        recentSearchesFragment.notifyDataSetChanged()
                    }
                }

                override fun onKeyboardDismiss(query: String?) {

                }
            })
            loadDataFromModel()
        } else {
            addFragment(
                noInternetFragment,
                android.R.id.content,
                true,
                AppConstants.TAG_NO_INTERNET_FRAGMENT
            )
        }
    }


    private fun startSearching(tempString: String) {
        if (tempString.isNotEmpty()) {
            if (!tempString.isBlank() && tempString.length > 2) {
//                binding.recentSearchesLayout.visibility = View.GONE
                val fragment = supportFragmentManager.findFragmentByTag(AppConstants.KEYWORD_SEARCH)
                if (fragment == null) {
                    val bundle = Bundle()
                    bundle.putString(AppConstants.SEARCH_STRING_KEY, "$tempString")
                    keyWordSearchFragment.arguments = bundle
                    binding.popularSearches.visibility = View.GONE
                    binding.noDataFound.visibility = View.GONE
                    showFragment(keyWordSearchFragment, AppConstants.KEYWORD_SEARCH)
                } else {
                    binding.popularSearches.visibility = View.GONE
                    binding.noDataFound.visibility = View.GONE
                    dataUpdateCallBack = keyWordSearchFragment
                    showFragment(keyWordSearchFragment, AppConstants.KEYWORD_SEARCH)
                    (dataUpdateCallBack as KeyWordSearchFragment).onDataClick("$tempString".trim())
                    hideFragment(popularSearchFragment, AppConstants.POPULAR_SEARCH)
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        } else {
            binding.popularSearches.visibility = View.VISIBLE
            hideFragment(keyWordSearchFragment, AppConstants.KEYWORD_SEARCH)
            showFragment(popularSearchFragment, AppConstants.POPULAR_SEARCH)
            Handler().postDelayed(Runnable {
                binding.noDataFound.visibility = View.GONE
            }, 1000)
        }
    }

    private fun loadDataFromModel() {
        addFragment(
            keyWordSearchFragment,
            R.id.keyword_search_result_fragment,
            true,
            AppConstants.KEYWORD_SEARCH,
            true
        )
        addFragment(
            popularSearchFragment,
            R.id.search_result_fragment,
            true,
            AppConstants.POPULAR_SEARCH,
            true
        )
        binding.popularSearches.visibility = View.VISIBLE
        hideFragment(keyWordSearchFragment, AppConstants.KEYWORD_SEARCH)
        Handler().postDelayed({
            showFragment(popularSearchFragment, AppConstants.POPULAR_SEARCH)
            binding.noDataFound.visibility = View.GONE
        }, 500)
        SharedPrefHelper(this).recentSearches?.let { items.addAll(it) }
        itemsAdapter = ArrayAdapter(this, R.layout.recent_search_item, items)
        if (items.isEmpty() || items.size == 0) {
            binding.recentSearchesText.visibility = View.GONE
            binding.recentSearchesLayout.visibility = View.GONE
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (binding.recentSearchesLayout.focusedChild != null || binding.recentSearchesLayout.hasFocus()) {
                recentSearchesFragment.onUpkeyClicked()
            } else {
                keyWordSearchFragment.onUpKeyClicked()
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (binding.recentSearchesLayout.focusedChild != null) {
                recentSearchesFragment.onDownKeyClicked()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun showProgressBarView(show: Boolean) {
        if (show) {
            binding.progressBar.visibility == View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun noPopularSearchesFound() {
        binding.popularSearches.visibility = View.GONE
    }

    override fun onRecentSearchClicked(recentSearchText: String) {
        if (recentSearchText == getString(R.string.clear_searches)) {
            SharedPrefHelper(this).clearRecentSearches()
            binding.recentSearchesLayout.visibility = View.GONE
            binding.recentSearchesText.visibility = View.GONE
        } else {
            binding.searchView.findViewById<EditText>(R.id.lb_search_text_editor)
                .setText(recentSearchText)
            startSearching(recentSearchText)
        }
    }

    override fun onUpdateRecentSearches(recentSearchText: String) {

    }

    override fun searchResultsFound(searchKeyword: String) {

    }

    override fun showNoDataFoundView(show: Boolean, msg: String) {
        if (show) {
            if (binding.popularSearches.visibility == View.VISIBLE) {
                binding.noDataFound.visibility = View.GONE
                binding.keywordSearchResultFragment.requestFocus()
                binding.searchResultFragment.requestFocus()
            } else {
                binding.noDataFound.visibility = View.VISIBLE
                binding.noDataFound.text = msg
                hideFragment(keyWordSearchFragment, AppConstants.KEYWORD_SEARCH)
                hideFragment(popularSearchFragment, AppConstants.POPULAR_SEARCH)
            }
        } else {
            binding.noDataFound.text = msg
            binding.noDataFound.visibility = View.GONE
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        getBinding().searchEditText.text = items[position]
//        startSearching(getBinding().searchEditText.text.toString())

    }

    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityTVSearchBinding {
        return ActivityTVSearchBinding.inflate(inflater)
    }

    override fun onClick(v: View?) {

    }

    override fun onFragmentInteraction() {

    }
}