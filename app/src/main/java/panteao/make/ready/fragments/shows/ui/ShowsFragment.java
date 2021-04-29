package panteao.make.ready.fragments.shows.ui;

import android.os.Bundle;

import panteao.make.ready.fragments.shows.viewModel.ShowsFragmentViewModel;
import panteao.make.ready.beanModel.TabsBaseFragment;
import panteao.make.ready.fragments.home.viewModel.HomeFragmentViewModel;
import panteao.make.ready.beanModel.TabsBaseFragment;

public class ShowsFragment extends TabsBaseFragment<HomeFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(ShowsFragmentViewModel.class);
    }
}