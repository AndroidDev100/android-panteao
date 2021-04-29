package panteao.make.ready.fragments.movies.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import panteao.make.ready.baseModels.HomeBaseViewModel;
import panteao.make.ready.repository.home.HomeFragmentRepository;
import panteao.make.ready.utils.constants.AppConstants;

import java.util.List;

import panteao.make.ready.baseModels.HomeBaseViewModel;


public class MovieFragmentViewModel extends HomeBaseViewModel {

    public MovieFragmentViewModel(@NonNull Application application) {
        super(application);
    }


    @Override
    public LiveData<List<BaseCategory>> getAllCategories() {

        return HomeFragmentRepository.getInstance().getCategories(AppConstants.HOME_ENVEU);
    }


    @Override
    public void resetObject() {

    }
}