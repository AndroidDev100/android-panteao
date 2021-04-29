package panteao.make.ready.activities.search.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import panteao.make.ready.networking.search.ResponseSearch;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModel.popularSearch.ResponsePopularSearch;
import panteao.make.ready.layersV2.SearchLayer;
import panteao.make.ready.repository.search.SearchRepository;
import panteao.make.ready.layersV2.SearchLayer;

public class SearchViewModel extends AndroidViewModel {
    private final SearchRepository searchRepository;

    /*public LiveData<List<KeywordList>> getSearchList(Context context)
    {
        return searchRepository.sizeRecentSearchList(context);
    }*/


    public SearchViewModel(@NonNull Application application) {
        super(application);
        searchRepository = SearchRepository.getInstance();

    }

/*

    public LiveData<List<KeywordList>> deleteKeywords(Context context)
    {
        return searchRepository.deleteAllKeywords(context);
    }*/

    public LiveData<ResponsePopularSearch> hitApiKeywordSearch(String keyWord, int size, int page) {
        return searchRepository.getSearchKeyword(keyWord, size, page);
    }

    public LiveData<ResponsePopularSearch> getPopularSearch(int size, int page) {
        return searchRepository.getPopularSearch(size, page);
    }


    public LiveData<RailCommonData> getSearchSingleCategory(String type, String keyword, int size, int page) {
        return SearchLayer.getInstance().getSingleCategorySearch(type, keyword, size, page);
    }

    public LiveData<ResponseSearch> getSearchSeries(String keyword, int size, int page) {
        return searchRepository.getSearchSeriesData("SERIES", keyword, size, page);
    }

    public LiveData<ResponsePopularSearch> getAllSeries(String keyword, int size, int page) {
        return searchRepository.getAllSearchSeries(keyword, size, page);
    }
}
