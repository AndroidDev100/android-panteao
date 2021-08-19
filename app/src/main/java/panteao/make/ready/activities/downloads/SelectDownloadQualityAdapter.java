package panteao.make.ready.activities.downloads;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.R;
import panteao.make.ready.databinding.SelectDownloadVideoQualityBinding;
import panteao.make.ready.utils.constants.SharedPrefesConstants;
import panteao.make.ready.utils.helpers.SharedPrefHelper;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.List;


public class SelectDownloadQualityAdapter extends RecyclerView.Adapter<SelectDownloadQualityAdapter.SingleItemRowHolder> {

    private final List<String> downloadQualities;
    private final VideoQualitySelectedListener videoQualitySelectedListener;
    // private int pos = new KsPreferenceKeys(ApplicationMain.getAppContext()).getQualityPosition();
    private Activity activity;
    int pos = -1;
    private KsPreferenceKeys preference;

    public SelectDownloadQualityAdapter(Activity activity, List<String> downloadQualities, VideoQualitySelectedListener videoQualitySelectedListener) {
        preference = KsPreferenceKeys.getInstance();
        pos =  new SharedPrefHelper(activity).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
        this.downloadQualities = downloadQualities;
        this.videoQualitySelectedListener = videoQualitySelectedListener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SelectDownloadVideoQualityBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.select_download_video_quality, viewGroup, false);
        return new SingleItemRowHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull final SingleItemRowHolder viewHolder, final int position) {
        if (pos != -1 && pos == position) {
            viewHolder.notificationItemBinding.rightIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.notificationItemBinding.rightIcon.setVisibility(View.INVISIBLE);
        }
        viewHolder.notificationItemBinding.titleText.setText(downloadQualities.get(position));
        viewHolder.notificationItemBinding.parentLayout.setOnClickListener(view -> {
            pos = position;
            videoQualitySelectedListener.videoQualitySelected(position);
            notifyDataSetChanged();
        });
    }


    @Override
    public int getItemCount() {
        return downloadQualities.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final SelectDownloadVideoQualityBinding notificationItemBinding;

        SingleItemRowHolder(SelectDownloadVideoQualityBinding binding) {
            super(binding.getRoot());
            this.notificationItemBinding = binding;
        }
    }
}
