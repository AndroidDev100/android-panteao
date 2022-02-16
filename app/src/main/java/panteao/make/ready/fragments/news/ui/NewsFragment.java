package panteao.make.ready.fragments.news.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import panteao.make.ready.baseModels.BaseBindingFragment;
import panteao.make.ready.databinding.MyDownloadsHomeFragmentBinding;

public class NewsFragment extends BaseBindingFragment<MyDownloadsHomeFragmentBinding>{


    @Override
    public MyDownloadsHomeFragmentBinding inflateBindingLayout() {
        return MyDownloadsHomeFragmentBinding.inflate(inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
