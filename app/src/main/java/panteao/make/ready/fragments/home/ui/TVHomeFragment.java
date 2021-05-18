package panteao.make.ready.fragments.home.ui;

import android.os.Bundle;

import panteao.make.ready.beanModel.TabsBaseFragment;
import panteao.make.ready.fragments.home.viewModel.HomeFragmentViewModel;
import panteao.make.ready.tvBaseModels.basemodels.TabBaseTVFragment;

public class TVHomeFragment extends TabBaseTVFragment<HomeFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(HomeFragmentViewModel.class);
    }
}