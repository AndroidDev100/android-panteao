package panteao.make.ready.activities.listing.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import panteao.make.ready.repository.series.SeriesRepository;
import panteao.make.ready.beanModel.responseModels.series.season.SeasonResponse;
import panteao.make.ready.beanModel.responseModels.series.season.SeasonResponse;

public class ListingViewModel extends AndroidViewModel {

    public ListingViewModel(@NonNull Application application) {
        super(application);

    }

   /* public LiveData<PlaylistRailData> getRailData(int playlistId, int page, int length, BaseCategory baseCategory){
        return ListingRepository.getInstance().getRailData(playlistId,page,length);
    }*/


    public LiveData<SeasonResponse> getVOD(int seriesID, int pageNo, int length) {
        return SeriesRepository.getInstance().getVOD(seriesID, pageNo, length);
    }

    public LiveData<SeasonResponse> getSeasonDetail(int seasonId, int pageNo, int length) {
        return SeriesRepository.getInstance().getSeasonDetail(seasonId, pageNo, length);
    }
}
