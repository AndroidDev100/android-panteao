package panteao.make.ready.fragments.news.ui;

import android.os.Bundle;

import panteao.make.ready.fragments.news.viewModel.NewsFragmentViewModel;
import panteao.make.ready.beanModel.TabsBaseFragment;
import panteao.make.ready.fragments.home.viewModel.HomeFragmentViewModel;
import panteao.make.ready.beanModel.TabsBaseFragment;

public class NewsFragment extends TabsBaseFragment<HomeFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(NewsFragmentViewModel.class);
    }
}