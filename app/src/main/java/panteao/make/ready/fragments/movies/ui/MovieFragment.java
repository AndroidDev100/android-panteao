package panteao.make.ready.fragments.movies.ui;

import android.os.Bundle;

import panteao.make.ready.fragments.movies.viewModel.MovieFragmentViewModel;
import panteao.make.ready.beanModel.TabsBaseFragment;
import panteao.make.ready.fragments.home.viewModel.HomeFragmentViewModel;
import panteao.make.ready.beanModel.TabsBaseFragment;

public class MovieFragment extends TabsBaseFragment<HomeFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(MovieFragmentViewModel.class);
    }
}