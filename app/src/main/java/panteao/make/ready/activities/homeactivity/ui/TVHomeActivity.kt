package panteao.make.ready.activities.homeactivity.ui

import android.annotation.SuppressLint
import android.app.ActionBar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.make.constants.Constants
import com.make.enums.ImageType
import panteao.make.ready.R
import panteao.make.ready.SDKConfig
import panteao.make.ready.adapters.tv.TvMenuAdapter
import panteao.make.ready.beanModel.model.MenuModel
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.callbacks.commonCallbacks.ChangableUi
import panteao.make.ready.callbacks.commonCallbacks.DataLoadingListener
import panteao.make.ready.callbacks.commonCallbacks.OnTabBaseFragmentListener
import panteao.make.ready.databinding.ActivityTvMainBinding
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.fragments.home.ui.TVHomeFragment
import panteao.make.ready.tvBaseModels.basemodels.TvBaseBindingActivity
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.ImageHelper
import panteao.make.ready.utils.helpers.NetworkConnectivity
import java.util.*

class TVHomeActivity : TvBaseBindingActivity<ActivityTvMainBinding>(), ChangableUi, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener, DataLoadingListener, OnTabBaseFragmentListener {
    private var firstTime: Boolean = true
    private var currentCardPosition: Int = -1
    private var prevPosition: Int = 1
    val TAG = this.javaClass.name
    private lateinit var fragmentManager: FragmentManager
    private lateinit var bundle: Bundle
    private var active: Fragment? = null
    val buttons: ArrayList<MenuModel> = ArrayList()
    private var selectedPostion: Int = 3
    private var homeFragment: TVHomeFragment? = null
    private var noInternetFragment = NoInternetFragment()
    private var menuDrawables = arrayOf(
            0,
            0,
            0,
            R.drawable.ic_search,
            R.drawable.ic_home,
            R.drawable.ic_free,
            R.drawable.ic_watch_list,
            R.drawable.ic_live_tv,
            R.drawable.profile_icon,
            R.drawable.ic_settings,
            0,
            R.drawable.ic_exit
    )

    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityTvMainBinding {
        return ActivityTvMainBinding.inflate(inflater)
    }

    override fun uichanged(position: Int) {
        val animFadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        if (position == 1) {
            binding?.ui2?.clearAnimation()
            binding?.ui2?.visibility = View.GONE
            binding?.imageView4?.startAnimation(animFadeIn)
            binding?.linearLayout?.startAnimation(animFadeIn)
            binding?.homeFragment?.layoutParams?.height = 640
            binding?.imageView4?.visibility = View.VISIBLE
            binding?.linearLayout?.visibility = View.VISIBLE
        } else if (position == 3) {
            binding?.ui2?.clearAnimation()
            binding?.imageView4?.clearAnimation()
            binding?.linearLayout?.clearAnimation()
            binding?.ui2?.visibility = View.GONE
            binding?.imageView4?.visibility = View.GONE
            binding?.homeFragment?.layoutParams?.height = ActionBar.LayoutParams.MATCH_PARENT
            binding?.linearLayout?.visibility = View.GONE
        } else {
            binding?.ui2?.clearAnimation()
            binding?.ui2?.visibility = View.GONE
            binding?.imageView4?.startAnimation(animFadeIn)
            binding?.linearLayout?.startAnimation(animFadeIn)
            binding?.homeFragment?.layoutParams?.height = 640
            binding?.imageView4?.visibility = View.VISIBLE
            binding?.linearLayout?.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionObserver()
        uichanged(1)
    }

    override fun onBackPressed() {
        if (!Constants.DRAWER_OPEN) {
            openDrawer()
        } else if (Constants.DRAWER_OPEN) {
            closeDrawer()
        }
    }

    private fun connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true)
        } else {
            connectionValidation(false)
        }
    }

    override fun onPause() {
        binding?.menuItems?.clearFocus()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding?.menuItems?.clearFocus()
        Constants.DRAWER_OPEN = false
    }

    private fun connectionValidation(boolean: Boolean) {
        if (boolean) {
            fragmentManager = supportFragmentManager
            homeFragment = TVHomeFragment()
            bundle = Bundle()
            bundle.putString(AppConstants.BUNDLE_TAB_ID, SDKConfig.getInstance().firstTabId)
            homeFragment?.let {
                it.arguments = bundle
                active = it
                fragmentManager.beginTransaction()
                        .add(R.id.home_fragment, it, "")
                        .show(it).commit()
            }
            createMenuRows()
        } else {
            addFragment(
                    noInternetFragment,
                    android.R.id.content,
                    true,
                    ""
            )
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createMenuRows() {
        var menuArray: Array<String> = resources.getStringArray(R.array.menu_titles_with_login)

        for ((index, value) in menuArray.withIndex()) {
            Logger.e("MENU_NAME", value)
            val menuModel = MenuModel()
            if (menuDrawables[index] != 0)
                menuModel.menuIcon = resources.getDrawable(menuDrawables[index])
            menuModel.menuName = value
            menuModel.menuId = index
            buttons.add(menuModel)
        }
        binding?.menuItems?.adapter = TvMenuAdapter(buttons, this)
        binding?.menuItems?.onItemClickListener = this
        binding?.menuItems?.onItemSelectedListener = this
        binding?.menuItems?.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_DOWN) {
                    if (Constants.DRAWER_OPEN) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            return if (binding?.menuItems?.selectedItemPosition == 9) {
                                binding?.menuItems?.setSelection(11)
                                true
                            } else {
                                false
                            }
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            if (binding?.menuItems?.selectedItemPosition == 3) {
                                return true
                            } else {
                                return if (binding?.menuItems?.selectedItemPosition == 11) {
                                    binding?.menuItems?.setSelection(9)
                                    true
                                } else {
                                    false
                                }
                            }
                        }
                    }
                }
                return false
            }
        })
        closeDrawer()
    }

    private fun openDrawer() {
        Constants.DRAWER_OPEN = true
        val params = binding?.biggerDrawerFrame?.layoutParams
        params?.width = AppCommonMethod.dptoPx(this, 200).toInt()
        binding?.biggerDrawerFrame?.layoutParams = params
        binding?.menuItems?.setSelection(selectedPostion)
        binding?.menuItems?.requestFocus()
        binding?.menuItems?.getChildAt(selectedPostion)
                ?.findViewById<View>(R.id.underline)?.visibility = View.INVISIBLE
    }

    private fun closeDrawer() {
        Constants.DRAWER_OPEN = false
        val params = binding?.biggerDrawerFrame?.layoutParams
        params?.width = AppCommonMethod.dptoPx(this, 50).toInt()
        binding?.biggerDrawerFrame?.layoutParams = params
//        homeFragment?.view?.requestFocus()
        if (!firstTime) {
            if (prevPosition != 0 && prevPosition != 1) {
                setColorFilter(
                        binding?.menuItems?.getChildAt(prevPosition)?.findViewById(R.id.label_text)!!,
                        resources.getColor(R.color.greyTextColor)
                )
            } else {
                setColorFilter(
                        binding?.menuItems?.getChildAt(prevPosition)?.findViewById(R.id.label_text)!!,
                        resources.getColor(android.R.color.transparent)
                )
            }
            prevPosition = selectedPostion

            if (prevPosition != 0 && prevPosition != 1) {
                setColorFilter(
                        binding?.menuItems?.getChildAt(prevPosition)?.findViewById(R.id.label_text)!!,
                        resources.getColor(R.color.white)
                )
            } else {
                setColorFilter(
                        binding?.menuItems?.getChildAt(prevPosition)?.findViewById(R.id.label_text)!!,
                        resources.getColor(android.R.color.transparent)
                )
            }
            if (prevPosition != 0 && prevPosition != 1)
                binding?.menuItems?.getChildAt(selectedPostion)
                        ?.findViewById<View>(R.id.underline)?.visibility = View.VISIBLE
        } else {
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                firstTime = false
                binding?.menuItems?.getChildAt(selectedPostion)
                        ?.findViewById<ImageView>(R.id.label_text)
                        ?.setColorFilter(resources.getColor(R.color.white))

                binding?.menuItems?.getChildAt(selectedPostion)
                        ?.findViewById<View>(R.id.underline)?.visibility = View.VISIBLE

                setColorFilter(
                        binding?.menuItems?.getChildAt(0)?.findViewById(R.id.label_text)!!,
                        resources.getColor(android.R.color.transparent)
                )
                val imageView =
                        binding?.menuItems?.getChildAt(1)?.findViewById<ImageView>(R.id.label_text)!!
                setColorFilter(imageView, resources.getColor(R.color.transparent))
            }, 500)

        }
//
//        binding?.biggerDrawerFrame?.setBackgroundColor(
//                resources.getColor(R.color.transBlackColor)
//        )
    }

    private fun setColorFilter(findViewById: ImageView?, color: Int) {
        findViewById?.setColorFilter(color)
    }

    private fun showProgressBarLayout(showLayout: Boolean) {
        if (showLayout) {
            binding?.progressLayout?.visibility = View.VISIBLE
        } else {
            binding?.progressLayout?.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.label -> {
                this.finishAffinity()
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            if (Constants.DRAWER_OPEN) {
                binding?.menuItems?.requestFocus()
            }
        }
        super.onWindowFocusChanged(hasFocus)

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onDataLoading(item: EnveuVideoItemBean, setAsset: Boolean) {
        Logger.e("DataLoading", Gson().toJson(item))
        if (item.imageType.equals(ImageType.LDS) || item.imageType.equals(ImageType.LDS2)) {
            ImageHelper.getInstance(this).loadImageTo(binding?.imageView4, item.posterURL)
        } else {
            ImageHelper.getInstance(this).loadImageTo(binding?.imageView4, item.thumbnailImage)
        }
        binding?.content = item
        if (item.assetCast == null || item.assetGenres == null) {
            binding?.lnCastHeadline?.visibility = View.GONE
            binding?.lnDirectorHeadline?.visibility = View.GONE
        } else {
//            binding?.lnCastHeadline?.visibility = View.VISIBLE
//            binding?.lnDirectorHeadline?.visibility = View.VISIBLE
        }
    }

    override fun onLoadingOfFirstRow() {
    }

    override fun onDataLoaded(boolean: Boolean) {
    }

    override fun onTrailerLoaded(asset: Any?) {
    }

    override fun onCardSelected(position: Int) {
        currentCardPosition = position
    }

    override fun showNoDataFoundView(show: Boolean, msg: String) {

    }

    override fun showProgressBarView(show: Boolean) {
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (currentCardPosition <= 0 && !Constants.DRAWER_OPEN) {
                openDrawer()
                return true
            } else {
                if (Constants.DRAWER_OPEN)
                    return true
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (Constants.DRAWER_OPEN) {
                closeDrawer()
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}