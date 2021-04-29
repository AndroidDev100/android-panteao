package panteao.make.ready.baseModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.make.baseCollection.baseCategoryModel.BaseCategory;

import java.util.List;


public abstract class HomeBaseViewModel extends AndroidViewModel {
    protected HomeBaseViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract LiveData<List<BaseCategory>> getAllCategories();

    public abstract void resetObject();

}
