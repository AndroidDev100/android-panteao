package panteao.make.ready.fragments.more.viewModel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.make.baseCollection.baseCategoryModel.BaseCategory;
import panteao.make.ready.baseModels.HomeBaseViewModel;
import panteao.make.ready.repository.more.MoreFragmentRepository;
import panteao.make.ready.beanModel.params.ParamBean;
import panteao.make.ready.databinding.FragmentMoreBinding;

import java.util.List;

import panteao.make.ready.baseModels.HomeBaseViewModel;


public class MoreFragmentViewModel extends HomeBaseViewModel {

    public MoreFragmentViewModel(@NonNull Application application, ParamBean mParam, Activity context, FragmentMoreBinding binding) {
        super(application);
        MoreFragmentRepository.getInstance().setAdapter(mParam, context, binding);
    }

    @Override
    public LiveData<List<BaseCategory>> getAllCategories() {
        return null;
    }

    @Override
    public void resetObject() {

    }
}