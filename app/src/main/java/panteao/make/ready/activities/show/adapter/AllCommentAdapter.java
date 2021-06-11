package panteao.make.ready.activities.show.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.R;
import panteao.make.ready.beanModel.AppUserModel;
import panteao.make.ready.beanModel.allComments.ItemsItem;
import panteao.make.ready.databinding.RowCommentsBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;


import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AllCommentAdapter extends RecyclerView.Adapter<AllCommentAdapter.SingleItemRowHolder> implements AlertDialogFragment.AlertDialogListener {
    private final Context context;
    private final List<ItemsItem> list;
    private final KsPreferenceKeys preference;
    private final AllComentClickListener listener;
    private boolean isLogin;
    private ItemsItem itemsItem;

    public AllCommentAdapter(Context context, List<ItemsItem> list, AllComentClickListener listener) {
        this.listener = listener;
        this.context = context;
        this.list = list;
        preference = KsPreferenceKeys.getInstance();
    }


    public void notifyAdapter(List<ItemsItem> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addItemToList(ItemsItem item) {
        this.list.add(0, item);
        notifyDataSetChanged();
    }


    public void clearList() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void deleteComment(String id) {

        int posDelete = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equalsIgnoreCase(id)) {
                posDelete = i;
                break;
            }
        }
        if (posDelete < list.size())
            list.remove(posDelete);
        notifyItemRemoved(posDelete);
        notifyItemRangeChanged(posDelete, list.size());
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public AllCommentAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        RowCommentsBinding itemBinding;
        itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.row_comments, viewGroup, false);

        return new SingleItemRowHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllCommentAdapter.SingleItemRowHolder viewHolder, final int position) {


        String commentorName = list.get(position).getName();
        String commentDate = getDate(list.get(position).getDateCreated());

        StringBuilder stringBuilder = new StringBuilder(commentorName + "  " + commentDate);

        Spannable WordtoSpan = new SpannableString(stringBuilder);
        WordtoSpan.setSpan(new ForegroundColorSpan(Color.WHITE), 0, commentorName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        WordtoSpan.setSpan(new RelativeSizeSpan(1.25f), 0, commentorName.length(), 0);
        viewHolder.itemBinding.tvCommentTitle.setText(WordtoSpan);
        viewHolder.itemBinding.tvCommentDetail.setText(list.get(position).getCommentText());

        Logger.e("", "comment text" + list.get(position).getCommentText());
        try {
            AppCommonMethod.setProfilePic(preference, context, list.get(position).getImage(), viewHolder.itemBinding.itemImage);
        } catch (NullPointerException e) {
            e.toString();
        }

        AppUserModel signInResponseModel = AppUserModel.getInstance();
        isLogin = preference.getAppPrefLoginStatus();


        viewHolder.itemBinding.clRoot.setOnLongClickListener(v -> {

            if (isLogin) {
                if (Integer.valueOf(signInResponseModel.getId()) == list.get(position).getUserId()) {
                    confirmDelete(list.get(position));
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.cannot_delete_this_comment), Toast.LENGTH_SHORT).show();
                }
            }


            return true;
        });


    }


    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String monthname = (String) android.text.format.DateFormat.format("MMM", cal);
        String monthNumber = (String) DateFormat.format("dd", cal);
        String year = (String) DateFormat.format("yyyy", cal);
        if (monthNumber.length() < 2) {
            monthNumber = "0" + monthNumber;
        }
        return monthNumber + " " + monthname + ", " + year;
    }


    @Override
    public int getItemCount() {

        return list.size();
    }

    private void confirmDelete(ItemsItem item) {
        itemsItem = item;
        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        AlertDialogFragment alertDialog = AlertDialogFragment.newInstance(context.getResources().getString(R.string.delete_comment), context.getResources().getString(R.string.delete_confirmation),context.getResources().getString(R.string.ok),context.getResources().getString(R.string.cancel));
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    public void onFinishDialog() {
        listener.onItemClicked(itemsItem);

    }

    public interface AllComentClickListener {
        void onItemClicked(ItemsItem itemValue);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final RowCommentsBinding itemBinding;

        SingleItemRowHolder(RowCommentsBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }
}



