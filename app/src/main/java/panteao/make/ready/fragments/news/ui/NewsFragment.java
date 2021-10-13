package panteao.make.ready.fragments.news.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import panteao.make.ready.PanteaoApplication;
import panteao.make.ready.R;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.activities.homeactivity.viewmodel.HomeViewModel;
import panteao.make.ready.activities.membershipplans.ui.MemberShipPlanActivity;
import panteao.make.ready.activities.notification.ui.NotificationActivity;
import panteao.make.ready.activities.profile.ui.ProfileActivityNew;
import panteao.make.ready.activities.redeemcoupon.RedeemCouponActivity;
import panteao.make.ready.activities.settings.ActivitySettings;
import panteao.make.ready.activities.usermanagment.ui.ChangePasswordActivity;
import panteao.make.ready.activities.watchList.ui.WatchListActivity;
import panteao.make.ready.baseModels.BaseBindingFragment;
import panteao.make.ready.beanModel.AppUserModel;
import panteao.make.ready.beanModel.configBean.ResponseConfig;
import panteao.make.ready.beanModel.responseModels.RegisterSignUpModels.DataResponseRegister;
import panteao.make.ready.beanModel.userProfile.UserProfileResponse;
import panteao.make.ready.callbacks.commonCallbacks.MoreItemClickListener;
import panteao.make.ready.databinding.FragmentMoreBinding;
import panteao.make.ready.databinding.MyDownloadsHomeFragmentBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.fragments.more.adapter.MoreListAdapter;
import panteao.make.ready.fragments.more.ui.MoreFragment;
import panteao.make.ready.fragments.news.viewModel.NewsFragmentViewModel;
import panteao.make.ready.beanModel.TabsBaseFragment;
import panteao.make.ready.fragments.home.viewModel.HomeFragmentViewModel;
import panteao.make.ready.beanModel.TabsBaseFragment;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.constants.SharedPrefesConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.CheckInternetConnection;
import panteao.make.ready.utils.helpers.NetworkConnectivity;
import panteao.make.ready.utils.helpers.SharedPrefHelper;
import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends BaseBindingFragment<MyDownloadsHomeFragmentBinding>{


    @Override
    public MyDownloadsHomeFragmentBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return MyDownloadsHomeFragmentBinding.inflate(inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
