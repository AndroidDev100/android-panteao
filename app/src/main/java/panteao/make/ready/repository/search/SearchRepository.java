package panteao.make.ready.repository.search;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import panteao.make.ready.beanModel.popularSearch.ResponsePopularSearch;
import panteao.make.ready.modelClasses.ItemsItem;
import panteao.make.ready.modelClasses.PageInfo;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.networking.search.Data;
import panteao.make.ready.networking.search.MultiRequestAPI;
import panteao.make.ready.networking.search.ResponseSearch;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import io.realm.Realm;
//import io.realm.RealmConfiguration;
//import io.realm.RealmResults;

public class SearchRepository {
    private static SearchRepository instance;
    ResponsePopularSearch popularSearchResponse;
    private Context context;
    // private List<KeywordList> sortedRecent;
    // private Realm realm;
    private List<RailCommonData> mModel;
    private List<EnveuVideoItemBean> enveuVideoItemBeans;

    private SearchRepository() {
    }

   /* private void initRealm() {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
    }

    public void addValueDb(Context context, String value) {
        getSortedValueDb(context);

        if (sortedRecent.size() > 0) {
            if (findValueDb(value)) {

            } else {
                checkLimit(context);
                insertValueDb(value);
            }
        } else {
            insertValueDb(value);
        }
    }
*/
   /* public boolean findValueDb(String value) {
        boolean check = false;
        for (int i = 0; i < sortedRecent.size(); i++) {
            if (sortedRecent.get(i).getKeywords().equalsIgnoreCase(value))
                check = true;
        }
        return check;
    }*/

    /*public boolean checkLimit(Context context) {
        if (sortedRecent.size() == 5) {
            RealmResults<SearchedKeywords> results = realm.where(SearchedKeywords.class).findAll();
            int deletePos = 0;
            for (int i = 0; i < results.size(); i++) {
                if (results.get(deletePos).getTimeStamp() > results.get(i).getTimeStamp())
                    deletePos = i;
            }
            this.context = context;
            realm.beginTransaction();
            SearchedKeywords key = results.get(deletePos);
            key.deleteFromRealm();
            realm.commitTransaction();
            getSortedValueDb(context);
            return true;
        } else return false;

    }

    public void insertValueDb(String value) {
        realm.beginTransaction();
        SearchedKeywords keyword = realm.createObject(SearchedKeywords.class);
        keyword.setKeyords(value);
        keyword.setTimeStamp(System.currentTimeMillis());
        realm.commitTransaction();
    }*/

    public static SearchRepository getInstance() {
        if (instance == null) {
            instance = new SearchRepository();
        }
        return (instance);
    }

    /*  public void getSortedValueDb(Context context) {
          this.context = context;
          initRealm();
          sortedRecent = new ArrayList<>();
          RealmResults<SearchedKeywords> realmResults = realm.where(SearchedKeywords.class).findAll();
          List<SearchedKeywords> list = new ArrayList<SearchedKeywords>();
          //get all record from DB
          for (SearchedKeywords student : realmResults) {
              list.add(student);
          }
          // set all record from realm object to custom object
          ArrayList<KeywordList> realmSearchedList = new ArrayList<>();
          if (list.size() > 0) {
              for (int i = 0; i < list.size(); i++) {
                  KeywordList item = new KeywordList();
                  item.setTimeStamp(list.get(i).getTimeStamp());
                  item.setKeywords(list.get(i).getKeyords());
                  realmSearchedList.add(item);
              }
          }
          //sort list
          Collections.sort(realmSearchedList, new SortList());
          sortedRecent.addAll(realmSearchedList);
      }


      public void deleteValueDb(Context context) {
          this.context = context;
          initRealm();
          realm.beginTransaction();
          RealmResults<SearchedKeywords> results = realm.where(SearchedKeywords.class).findAll();
          results.deleteAllFromRealm();
          realm.commitTransaction();
      }

  */
 /*   public LiveData<List<KeywordList>> sizeRecentSearchList(Context context) {
        final MutableLiveData<List<KeywordList>> connection = new MutableLiveData<>();
        getSortedValueDb(context);
        connection.postValue(sortedRecent);
        return connection;
    }


    public LiveData<List<KeywordList>> addRecentAndHitApit(Context context, String searchValue) {
        final MutableLiveData<List<KeywordList>> connection = new MutableLiveData<>();
        addValueDb(context, searchValue);
        getSortedValueDb(context);
        connection.postValue(sortedRecent);
        return connection;
    }


    public LiveData<List<KeywordList>> deleteAllKeywords(Context context) {
        final MutableLiveData<List<KeywordList>> connection = new MutableLiveData<>();
        deleteValueDb(context);
        getSortedValueDb(context);

        connection.postValue(sortedRecent);
        return connection;
    }
*/
    public LiveData<ResponsePopularSearch> getSearchKeyword(String keyword, int size, int page) {

        MutableLiveData<ResponsePopularSearch> responseKeyword = new MutableLiveData<>();
        popularSearchResponse = new ResponsePopularSearch();
        ApiInterface endpoint = RequestConfig.getClientHeader().create(ApiInterface.class);
        try {
            keyword= URLEncoder.encode(keyword, "UTF-8");
        }catch (Exception e){

        }
        Call<ResponsePopularSearch> call = endpoint.getSearchKeyword(keyword, size, page);
        call.enqueue(new Callback<ResponsePopularSearch>() {
            @Override
            public void onResponse(@NonNull Call<ResponsePopularSearch> call, @NonNull Response<ResponsePopularSearch> response) {
                if (response.body() != null) {

                    popularSearchResponse = response.body();
                    popularSearchResponse.setStatus(true);
                } else {
                    popularSearchResponse.setStatus(false);
                }

                responseKeyword.postValue(popularSearchResponse);
            }

            @Override
            public void onFailure(@NonNull Call<ResponsePopularSearch> call, @NonNull Throwable t) {

            }
        });


        return responseKeyword;
    }

    public LiveData<ResponsePopularSearch> getPopularSearch(int size, int page) {
        MutableLiveData<ResponsePopularSearch> responsePopular = new MutableLiveData<>();
        popularSearchResponse = new ResponsePopularSearch();
        ApiInterface endpoint = RequestConfig.getClientHeader().create(ApiInterface.class);
        Call<ResponsePopularSearch> call = endpoint.getPopularSearch(size, page);

        call.enqueue(new Callback<ResponsePopularSearch>() {
            @Override
            public void onResponse(@NonNull Call<ResponsePopularSearch> call, @NonNull Response<ResponsePopularSearch> response) {
                if (response.body() != null) {
                    popularSearchResponse = response.body();
                    popularSearchResponse.setStatus(true);

                } else {
                    popularSearchResponse.setStatus(false);
                }
                responsePopular.postValue(popularSearchResponse);
            }

            @Override
            public void onFailure(@NonNull Call<ResponsePopularSearch> call, @NonNull Throwable t) {

                popularSearchResponse.setStatus(false);

            }
        });

        return responsePopular;
    }





    public LiveData<ResponsePopularSearch> getAllSearchSeries(String keyword, int size, int page) {
        MutableLiveData<ResponsePopularSearch> responsePopular = new MutableLiveData<>();
        popularSearchResponse = new ResponsePopularSearch();
        MultiRequestAPI backendApi = RequestConfig.getClientHeader().create(MultiRequestAPI.class);
        try {
            keyword= URLEncoder.encode(keyword, "UTF-8");
        }catch (Exception e){

        }
        Call<ResponsePopularSearch> call = backendApi.getSearchSeries(keyword, "SERIES", size, page);

        call.enqueue(new Callback<ResponsePopularSearch>() {
            @Override
            public void onResponse(@NonNull Call<ResponsePopularSearch> call, @NonNull Response<ResponsePopularSearch> response) {
                if (response.body() != null) {
                    popularSearchResponse = response.body();
                    popularSearchResponse.setStatus(true);

                } else {
                    popularSearchResponse.setStatus(false);
                }
                responsePopular.postValue(popularSearchResponse);
            }

            @Override
            public void onFailure(@NonNull Call<ResponsePopularSearch> call, @NonNull Throwable t) {

                popularSearchResponse.setStatus(false);

            }
        });

        return responsePopular;
    }


    public LiveData<ResponseSearch> getSearchSeriesData(String type, String keyword, int size, int page) {

        MutableLiveData<ResponseSearch> responsePopular = new MutableLiveData<>();
        popularSearchResponse = new ResponsePopularSearch();
        MultiRequestAPI backendApi = RequestConfig.getClientHeader().create(MultiRequestAPI.class);
        try {
            keyword= URLEncoder.encode(keyword, "UTF-8");
        }catch (Exception e){

        }
        Call<ResponsePopularSearch> call = backendApi.getSearchSeries(keyword, "SERIES", size, page);
        call.enqueue(new Callback<ResponsePopularSearch>() {
            @Override
            public void onResponse(@NonNull Call<ResponsePopularSearch> all, @NonNull Response<ResponsePopularSearch> response) {
                ResponseSearch temp = new ResponseSearch();

                if (response.code() == 200) {

                    Data data = new Data();
                    PageInfo pageInfo = new PageInfo();
                    //  data.getPageInfo().setField(response.body().getData().getPageInfo().getField());
                    // data.getPageInfo().setPage(response.body().getData().getPageInfo().getPage());
                    // data.getPageInfo().setPages(response.body().getData().getPageInfo().getPages());
                    // data.getPageInfo().setPerpage(response.body().getData().getPageInfo().getPerpage());
                    //  data.getPageInfo().setSort(response.body().getData().getPageInfo().getSort());

                    pageInfo.setTotal(response.body().getData().getPageInfo().getTotal());
                    data.setPageInfo(pageInfo);
                    List<ItemsItem> items = new ArrayList<>();
                    for (int i = 0; i < response.body().getData().getItems().size(); i++) {
                        ItemsItem tempItem = new ItemsItem();
                        tempItem.setId(response.body().getData().getItems().get(i).getId());
                        tempItem.setAssetType(response.body().getData().getItems().get(i).getType());
                        tempItem.setStatus(response.body().getData().getItems().get(i).getStatus());
                        tempItem.setPortraitImage(response.body().getData().getItems().get(i).getPicture());
                        tempItem.setTitle(response.body().getData().getItems().get(i).getName());
                        tempItem.setEpisodesCount(response.body().getData().getItems().get(i).getEpisodesCount());
                        tempItem.setSeasonCount(response.body().getData().getItems().get(i).getSeasonCount());

                        items.add(tempItem);
                    }
                    data.setItems(items);
                    //  temp.setDebugMessage(response.body().getDebugMessage());
                    temp.setData(data);
                    temp.setStatus(true);
                    temp.setResponseCode(200);
                } else {
                    temp.setStatus(false);
                }
                responsePopular.postValue(temp);
            }

            @Override
            public void onFailure(@NonNull Call<ResponsePopularSearch> call, @NonNull Throwable t) {
                responsePopular.postValue(new ResponseSearch());
            }
        });

        return responsePopular;
    }


}
