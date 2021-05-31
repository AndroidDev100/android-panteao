package panteao.make.ready.activities.search.ui;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import panteao.make.ready.activities.detail.ui.DetailActivity;
import panteao.make.ready.activities.search.adapter.CategoriedSearchAdapter;
import panteao.make.ready.activities.search.adapter.CommonSearchAdapter;
import panteao.make.ready.activities.search.adapter.RecentListAdapter;
import panteao.make.ready.activities.search.viewmodel.SearchViewModel;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.adapters.CommonShimmerAdapter;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.beanModel.KeywordList;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModel.popularSearch.ItemsItem;
import panteao.make.ready.callbacks.commonCallbacks.SearchClickCallbacks;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.NetworkConnectivity;
import panteao.make.ready.utils.helpers.AppPreference;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.R;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.ActivitySearchBinding;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.helpers.RailInjectionHelper;
import panteao.make.ready.utils.helpers.RecyclerAnimator;
import panteao.make.ready.utils.helpers.ToastHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

//import com.webstreamindonesia.nonton.db.search.SearchedKeywords;


@SuppressWarnings("StatementWithEmptyBody")
public class ActivitySearch extends BaseBindingActivity<ActivitySearchBinding> implements SearchClickCallbacks, RecentListAdapter.KeywordItemHolderListener, SearchView.OnQueryTextListener {

    private SearchViewModel viewModel;
    //    private List<SearchedKeywords> mList;
    private CategoriedSearchAdapter searchAdapter;
    private List<RailCommonData> model;
    private long mLastClickTime = 0;
    private boolean isShimmer;
    private RailCommonData railCommonData;
    private RailInjectionHelper railInjectionHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(ActivitySearch.this).get(SearchViewModel.class);
        clickListner();
        connectionObserver();
        getBinding().toolbar.searchView.setOnQueryTextListener(this);
        AppCommonMethod.trackFcmEvent("Search","",ActivitySearch.this,0);
        KsPreferenceKeys.getInstance().setScreenName("Search");


    }

    boolean searchResult = true;

    private void clickListner() {
        getBinding().noResult.setVisibility(View.GONE);
        getBinding().noResult1.setVisibility(View.GONE);
        hitApiPopularSearch();
        setRecyclerProperties(getBinding().recentSearchRecycler);
        setRecentSearchAdapter("");

        getBinding().toolbar.backButton.setOnClickListener(view -> onBackPressed());
        getBinding().toolbar.clearText.setOnClickListener(view -> onBackPressed());

//        getBinding().toolbar.searchText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
//            if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
//                //  searchKeyWord(getBinding().toolbar.searchText.getText().toString().trim());
//                if (NetworkConnectivity.isOnline(ActivitySearch.this)) {
//                    if ((getBinding().toolbar.searchText.getText().toString().trim().length() > 0)) {
//                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
//                            return true;
//                        }
//                        mLastClickTime = SystemClock.elapsedRealtime();
//
//                        hideSoftKeyboard(getBinding().toolbar.searchText);
//                        try {
//                            AppCommonMethod.trackSearchFcmEvent(ActivitySearch.this, getBinding().toolbar.searchText.getText().toString().trim());
//                        } catch (Exception e) {
//
//                        }
//                        if (searchResult) {
//                            searchResult = false;
//
//                            hitApiSearchKeyword(getBinding().toolbar.searchText.getText().toString().trim());
//                            //  hitApiSearchSeries(getBinding().toolbar.searchText.getText().toString().trim());
//                        }
//                        //hitApiSearchSeries(getBinding().toolbar.searchText.getText().toString().trim());
//                    } else {
//                        getBinding().toolbar.searchText.setText("");
//                    }
//                } else {
//                    new ToastHandler(ActivitySearch.this).show(ActivitySearch.this.getResources().getString(R.string.no_internet_connection));
//                }
//
//            }
//
//            return false;
//        });
    }

    private void searchKeyWord(String trim) {

    }

    private void setRecentSearchAdapter(String s) {
        Gson gson = new Gson();
        String json = AppPreference.getInstance(this).getRecentSearchList();
//        String json = KsPreferenceKeys.getInstance().getRecentSearchList();
        if (json.isEmpty()) {
            getBinding().llRecentSearchLayout.setVisibility(View.GONE);
        } else {
            getBinding().llRecentSearchLayout.setVisibility(View.VISIBLE);
            Type type = new TypeToken<List<KeywordList>>() {
            }.getType();
            List<KeywordList> arrPackageData = gson.fromJson(json, type);

            Collections.reverse(arrPackageData);
            if (arrPackageData.size() > 0) {
                RecentListAdapter recentListAdapter = new RecentListAdapter(ActivitySearch.this, arrPackageData, ActivitySearch.this);
                getBinding().recentSearchRecycler.setAdapter(recentListAdapter);
            }
        }

        getBinding().deleteKeywords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeletion();
            }
        });
    }

    private void confirmDeletion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySearch.this, R.style.AlertDialogStyle);
        builder.setMessage(this.getResources().getString(R.string.delete_history_confirmation))
                .setCancelable(true)
                .setPositiveButton(this.getResources().getString(R.string.delete), (dialog, id) -> {
                    AppPreference.getInstance(ActivitySearch.this).setRecentSearchList("");
                    getBinding().llRecentSearchLayout.setVisibility(View.GONE);
                    dialog.cancel();
                })
                .setNegativeButton(this.getResources().getString(R.string.cancel), (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
        Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        bn.setTextColor(ContextCompat.getColor(ActivitySearch.this, R.color.white));
        Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        bp.setTextColor(ContextCompat.getColor(ActivitySearch.this, R.color.moretitlecolor));

    }


    private void hitApiSearchKeyword(String searchKeyword) {
        model = new ArrayList<>();
        callShimmer(getBinding().searchResultRecycler);
        setRecyclerProperties(getBinding().searchResultRecycler);
         AppCommonMethod.trackFcmCustomEvent(ActivitySearch.this, AppConstants.SEARCH,"","","",0," ",0,"",0,0,searchKeyword,"","","");

        getBinding().rootView.setVisibility(View.GONE);
        getBinding().noResult.setVisibility(View.GONE);
        getBinding().noResult1.setVisibility(View.GONE);
        // getBinding().progressBar.setVisibility(View.VISIBLE);
        getBinding().llSearchResultLayout.setVisibility(View.VISIBLE);
        railInjectionHelper.getSearch(searchKeyword, 4, 0).observe(ActivitySearch.this, data -> {
            searchResult = true;
            if (Objects.requireNonNull(data).size() > 0) {
                try {
                    getBinding().noResult.setVisibility(View.GONE);
                    getBinding().noResult1.setVisibility(View.GONE);
                    getBinding().rootView.setVisibility(View.GONE);
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getPageTotal() > 0) {
                            if (data.get(i).getStatus()) {
                                RailCommonData temp = new RailCommonData();
                                temp.setEnveuVideoItemBeans(data.get(i).getEnveuVideoItemBeans());
                                if (data.get(i).getEnveuVideoItemBeans().size() > 0) {
                                    temp.setAssetType(data.get(i).getEnveuVideoItemBeans().get(0).getAssetType());
                                    temp.setStatus(true);
                                    if (data.get(i).getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getShow())) {
                                        temp.setLayoutType(0);
                                    } else if (data.get(i).getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())) {
                                        temp.setLayoutType(1);
                                    } else if (data.get(i).getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getMovie())) {
                                        temp.setLayoutType(2);
                                    } else if (data.get(i).getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries())) {
                                        temp.setLayoutType(3);
                                    } else if (data.get(i).getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getLive())) {
                                        temp.setLayoutType(4);
                                    }else if (data.get(i).getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getLive())) {
                                        temp.setLayoutType(5);
                                    }else if (data.get(i).getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getLive())) {
                                        temp.setLayoutType(6);
                                    }else if (data.get(i).getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getLive())) {
                                        temp.setLayoutType(7);
                                    }else if (data.get(i).getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getLive())) {
                                        temp.setLayoutType(8);
                                    }
                                    temp.setSearchKey(searchKeyword);
                                    temp.setTotalCount(data.get(i).getPageTotal());
                                    model.add(temp);
//                                    Log.d("Searchterm",temp.getSearchKey());
//                                    AppCommonMethod.trackFcmCustomEvent(ActivitySearch.this, AppConstants.SEARCH,"Search","","","",0," ",0,"",0,0,temp.getSearchKey(),"");

                                }

                            }
                        }
                    }
                } catch (Exception e) {

                }

                if (model.size() > 0) {
                    new RecyclerAnimator(this).animate(getBinding().searchResultRecycler);
                    searchAdapter = new CategoriedSearchAdapter(ActivitySearch.this, model, ActivitySearch.this);
                    getBinding().searchResultRecycler.setAdapter(searchAdapter);
                } else {
                    getBinding().noResult.setVisibility(View.VISIBLE);
                    getBinding().noResult1.setVisibility(View.VISIBLE);
                    getBinding().rootView.setVisibility(View.VISIBLE);
                    getBinding().llSearchResultLayout.setVisibility(View.GONE);
                }
            } else {
                getBinding().noResult.setVisibility(View.VISIBLE);
                getBinding().noResult1.setVisibility(View.VISIBLE);
                getBinding().rootView.setVisibility(View.VISIBLE);
                getBinding().llSearchResultLayout.setVisibility(View.GONE);
            }
            createRecentSearch(searchKeyword);
            getBinding().progressBar.setVisibility(View.GONE);
        });


    }

    private void createRecentSearch(String searchKeyword) {
        if (searchKeyword.equalsIgnoreCase("")) {
            return;
        }
        KeywordList keywordList = new KeywordList();
        keywordList.setKeywords(searchKeyword);
        keywordList.setTimeStamp(AppCommonMethod.getCurrentTimeStamp());
        if (AppPreference.getInstance(this).getRecentSearchList().equalsIgnoreCase("")) {
            List<KeywordList> list = new ArrayList<>();
            list.add(keywordList);
            Gson gson = new Gson();
            String json = gson.toJson(list);
            AppPreference.getInstance(this).setRecentSearchList(json);
            setRecentSearchAdapter("");
        } else {
            Gson gson = new Gson();
            String json = AppPreference.getInstance(this).getRecentSearchList();
            if (json.isEmpty()) {

            } else {
                Type type = new TypeToken<List<KeywordList>>() {
                }.getType();
                List<KeywordList> arrPackageData = gson.fromJson(json, type);
                if (json.contains(searchKeyword)) {
                    return;
                }
                List<KeywordList> newL = new ArrayList<>(arrPackageData);
                if (newL.size() < 5) {
                    newL.add(keywordList);
                } else {
                    newL.remove(0);
                    newL.add(keywordList);
                }

                String jsons = gson.toJson(newL);
                AppPreference.getInstance(this).setRecentSearchList(jsons);
                Collections.reverse(newL);
                if (newL.size() > 0) {
                    RecentListAdapter recentListAdapter = new RecentListAdapter(ActivitySearch.this, newL, ActivitySearch.this);
                    getBinding().recentSearchRecycler.setAdapter(recentListAdapter);
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean containsName(final List<KeywordList> list, final String name) {
        return list.stream().map(KeywordList::getKeywords).filter(name::equals).findFirst().isPresent();
    }



   /* public void setNoResultLayout() {
        getBinding().noResult.setVisibility(View.VISIBLE);
        getBinding().rootView.setVisibility(View.VISIBLE);
        getBinding().llSearchResultLayout.setVisibility(View.GONE);
        searchAdapter.clearList(true);
    }*/


    String popularSearchId = "";

    private void hitApiPopularSearch() {
        railInjectionHelper = ViewModelProviders.of(this).get(RailInjectionHelper.class);
        if (!SDKConfig.getInstance().getPopularSearchId().equalsIgnoreCase("")) {
            railInjectionHelper.getPlayListDetailsWithPagination(this, SDKConfig.getInstance().getPopularSearchId(), 0, 5, null).observe(this, playlistRailData -> {
                if (Objects.requireNonNull(playlistRailData) != null) {
                    setUiComponents(playlistRailData);
                }
            });
        }

    }

    private void setUiComponents(RailCommonData jsonObject) {
        isShimmer = false;
        if (jsonObject.getStatus() && jsonObject.getEnveuVideoItemBeans()!=null && jsonObject.getEnveuVideoItemBeans().size()>0) {
            getBinding().tvPopularSearch.setVisibility(View.VISIBLE);
        } else {
            getBinding().popularSearchRecycler.setVisibility(View.GONE);
        }

        getBinding().popularSearchRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        //new RecyclerAnimator(this).animate(getBinding().popularSearchRecycler);
        getBinding().popularSearchRecycler.setAdapter(new CommonSearchAdapter(ActivitySearch.this, jsonObject, this));
    }

    private void setRecyclerProperties(RecyclerView recyclerView) {
        recyclerView.hasFixedSize();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            UIinitialization();
            //pDialog.call();
            //  loadDataFromModel();
        } else {
            noConnectionLayout();
        }
    }


    private void UIinitialization() {
        getBinding().searchLayout.setVisibility(View.VISIBLE);
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().tvPopularSearch.setVisibility(View.GONE);
        getBinding().toolbar.searchView.onActionViewExpanded();
        getBinding().toolbar.searchView.requestFocus();
        callShimmer(getBinding().popularSearchRecycler);

        clickListner();
    }


    private void callShimmer(RecyclerView recyclerView) {
        isShimmer = true;
        CommonShimmerAdapter adapterPurchase = new CommonShimmerAdapter(this, true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterPurchase);
    }

    private void noConnectionLayout() {
        getBinding().searchLayout.setVisibility(View.GONE);

        getBinding().progressBar.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.retryTxt.setOnClickListener(view -> connectionObserver());
    }


    @Override
    public ActivitySearchBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySearchBinding.inflate(inflater);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onEnveuItemClicked(EnveuVideoItemBean itemValue) {
        AppCommonMethod.trackFcmEvent("Content Screen","", getApplicationContext(),0);


        try {
//            AppCommonMethod.trackFcmEvent(itemValue.getTitle(), itemValue.getAssetType(), ActivitySearch.this, 0);
        } catch (Exception e) {

        }
        if (AppCommonMethod.getCheckBCID(itemValue.getBrightcoveVideoId())) {
            Long getVideoId = Long.parseLong(itemValue.getBrightcoveVideoId());
            AppCommonMethod.launchDetailScreen(this, getVideoId, itemValue.getAssetType(), itemValue.getId(), "0", false);
        }
    }

    @Override
    public void onShowAllItemClicked(RailCommonData itemValue) {

        if (itemValue != null && itemValue.getStatus()) {
            new ActivityLauncher(ActivitySearch.this).resultActivityBundle(ActivitySearch.this, ActivityResults.class, itemValue.getAssetType(), itemValue.getSearchKey(), itemValue.getTotalCount());

        }
    }

    @Override
    public void onPopularSearchItemClicked(ItemsItem itemValue) {
        try {
//            AppCommonMethod.trackFcmEvent(itemValue.getName(), itemValue.getType(), ActivitySearch.this, 0);
        } catch (Exception e) {

        }
        if (itemValue.getType().equalsIgnoreCase("SERIES")) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            if (NetworkConnectivity.isOnline(ActivitySearch.this)) {
                new ActivityLauncher(ActivitySearch.this).seriesDetailScreen(ActivitySearch.this, SeriesDetailActivity.class, itemValue.getId());
            } else {
                new ToastHandler(ActivitySearch.this).show(ActivitySearch.this.getResources().getString(R.string.no_internet_connection));
            }
        } else if (itemValue.getType().equalsIgnoreCase("SEASON")) {
        } else {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            if (NetworkConnectivity.isOnline(ActivitySearch.this)) {
                new ActivityLauncher(ActivitySearch.this).detailScreen(ActivitySearch.this, DetailActivity.class, itemValue.getId(), "0", false);
            } else
                new ToastHandler(ActivitySearch.this).show(ActivitySearch.this.getResources().getString(R.string.no_internet_connection));
        }

    }

    @Override
    public void onItemClicked(KeywordList itemValue) {
        if (NetworkConnectivity.isOnline(ActivitySearch.this)) {
            hideSoftKeyboard(getBinding().toolbar.searchText);
            try {
                AppCommonMethod.trackSearchFcmEvent(ActivitySearch.this, itemValue.getKeywords().trim());
            } catch (Exception e) {

            }
            if (searchResult) {
                searchResult = false;
                getBinding().toolbar.searchView.setQuery(itemValue.getKeywords().trim(), true);
//                getBinding().toolbar.searchText.setText(itemValue.getKeywords().trim());
//                hitApiSearchKeyword(itemValue.getKeywords().trim());
            }
            //hitApiSearchSeries(getBinding().toolbar.searchText.getText().toString().trim());

        } else {
            new ToastHandler(ActivitySearch.this).show(ActivitySearch.this.getResources().getString(R.string.no_internet_connection));
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (NetworkConnectivity.isOnline(ActivitySearch.this)) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return true;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            hideSoftKeyboard(getBinding().toolbar.searchText);
            try {
                AppCommonMethod.trackSearchFcmEvent(ActivitySearch.this, query.trim());
            } catch (Exception e) {

            }
            if (query!=null && query.trim().length()>2){
                hitApiSearchKeyword(query.trim());
            }

            //  hitApiSearchSeries(getBinding().toolbar.searchText.getText().toString().trim());
            //hitApiSearchSeries(getBinding().toolbar.searchText.getText().toString().trim());

        } else {
            new ToastHandler(ActivitySearch.this).show(ActivitySearch.this.getResources().getString(R.string.no_internet_connection));
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}

