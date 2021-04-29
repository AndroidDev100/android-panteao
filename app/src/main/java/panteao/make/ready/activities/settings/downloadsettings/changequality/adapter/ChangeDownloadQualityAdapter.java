package panteao.make.ready.activities.settings.downloadsettings.changequality.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.R;
import panteao.make.ready.databinding.ChangeLanguageItemBinding;
import panteao.make.ready.utils.constants.SharedPrefesConstants;
import panteao.make.ready.utils.helpers.SharedPrefHelper;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.List;


public class ChangeDownloadQualityAdapter extends RecyclerView.Adapter<ChangeDownloadQualityAdapter.SingleItemRowHolder> {

    private final List<String> downloadQualities;
    // private int pos = new KsPreferenceKeys(ApplicationMain.getAppContext()).getQualityPosition();
    private Activity activity;
    int pos = 0;
    private KsPreferenceKeys preference;

    public ChangeDownloadQualityAdapter(Activity activity, List<String> downloadQualities) {
        preference = KsPreferenceKeys.getInstance();
        pos = preference.getAppPrefLanguagePos();
        this.downloadQualities = downloadQualities;
        this.activity = activity;
        pos = new SharedPrefHelper(activity).getInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 4);
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        ChangeLanguageItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.change_language_item, viewGroup, false);

        return new SingleItemRowHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull final SingleItemRowHolder viewHolder, final int position) {
        if (pos == position) {
            viewHolder.notificationItemBinding.rightIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.notificationItemBinding.rightIcon.setVisibility(View.INVISIBLE);
        }
        viewHolder.notificationItemBinding.titleText.setText(downloadQualities.get(position));

        viewHolder.notificationItemBinding.parentLayout.setOnClickListener(view -> {
            pos = position;
            new SharedPrefHelper(activity).setInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, position);
            notifyDataSetChanged();
        });
    }


    @Override
    public int getItemCount() {
        return downloadQualities.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final ChangeLanguageItemBinding notificationItemBinding;

        SingleItemRowHolder(ChangeLanguageItemBinding binding) {
            super(binding.getRoot());
            this.notificationItemBinding = binding;
        }
    }
}
