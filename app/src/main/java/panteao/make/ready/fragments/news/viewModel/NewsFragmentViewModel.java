package panteao.make.ready.fragments.news.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import panteao.make.ready.baseModels.HomeBaseViewModel;
import panteao.make.ready.repository.home.HomeFragmentRepository;
import panteao.make.ready.utils.constants.AppConstants;

import java.util.List;

import panteao.make.ready.baseModels.HomeBaseViewModel;


public class NewsFragmentViewModel extends HomeBaseViewModel {

    public NewsFragmentViewModel(@NonNull Application application) {
        super(application);

    }

    @Override
    public LiveData<List<BaseCategory>> getAllCategories() {

        return HomeFragmentRepository.getInstance().getCategories(AppConstants.ORIGINAL_ENVEU);
    }


    @Override
    public void resetObject() {

    }
}
