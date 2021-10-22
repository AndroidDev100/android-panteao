package panteao.make.ready.activities.videoquality.ui;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import panteao.make.ready.R;
import panteao.make.ready.activities.videoquality.adapter.VideoQualityAdapter;
import panteao.make.ready.activities.videoquality.bean.TrackItem;
import panteao.make.ready.activities.videoquality.callBack.NotificationItemClickListner;
import panteao.make.ready.activities.videoquality.viewModel.VideoQualityViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.databinding.VideoQualityActivityBinding;
import panteao.make.ready.utils.helpers.NetworkConnectivity;

import java.util.ArrayList;

import panteao.make.ready.activities.videoquality.adapter.VideoQualityAdapter;
import panteao.make.ready.activities.videoquality.bean.TrackItem;
import panteao.make.ready.activities.videoquality.callBack.NotificationItemClickListner;
import panteao.make.ready.activities.videoquality.viewModel.VideoQualityViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;


public class VideoQualityActivity extends BaseBindingActivity<VideoQualityActivityBinding> implements NotificationItemClickListner {
    private VideoQualityViewModel viewModel;
    private VideoQualityAdapter notificationAdapter;

    @Override
    public VideoQualityActivityBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return VideoQualityActivityBinding.inflate(inflater);


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callModel();
        connectionObserver();
        toolBar();
        //new ToolBarHandler(this).setVideoQuality(getBinding(),"Streaming Settings");

    }
    private void toolBar() {
        getBinding().toolbar.mediaRouteButton.setVisibility(View.GONE);
        getBinding().toolbar.llSearchIcon.setVisibility(View.GONE);
        getBinding().toolbar.backLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.titleText.setVisibility(View.VISIBLE);

        getBinding().toolbar.screenText.setText(getResources().getString(R.string.streaming_settings));

        getBinding().toolbar.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void callModel() {
        viewModel = new ViewModelProvider(this).get(VideoQualityViewModel.class);
    }

    private void connectionObserver() {

        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private ArrayList<TrackItem> arrayList;

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            arrayList = viewModel.getQualityList(getResources());
            uiInitialization();
            setAdapter();


        } else {
            noConnectionLayout();
        }


    }

    private void setAdapter() {
        notificationAdapter = new VideoQualityAdapter(VideoQualityActivity.this, arrayList, VideoQualityActivity.this);
        getBinding().recyclerview.setAdapter(notificationAdapter);
    }

    private void uiInitialization() {
        getBinding().recyclerview.hasFixedSize();
        getBinding().recyclerview.setNestedScrollingEnabled(false);
        getBinding().recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(String id, String status) {
        notificationAdapter.notifyDataSetChanged();
    }
}