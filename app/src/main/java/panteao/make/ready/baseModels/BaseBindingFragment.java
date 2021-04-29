package panteao.make.ready.baseModels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;


public abstract class BaseBindingFragment<B extends ViewDataBinding> extends BaseFragment {
    private B mBinding;


    protected abstract B inflateBindingLayout(@NonNull LayoutInflater inflater);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = setupBinding(inflater);
        return mBinding.getRoot();
    }


    public B getBinding() {
        return mBinding;
    }


    private B setupBinding(@NonNull LayoutInflater inflater) {
        return inflateBindingLayout(inflater);
    }
}
