package panteao.make.ready.activities.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.R;
import panteao.make.ready.beanModel.KeywordList;
import panteao.make.ready.databinding.KeywordItemBinding;
import panteao.make.ready.utils.cropImage.helpers.Logger;

import java.util.List;

import panteao.make.ready.beanModel.KeywordList;

@SuppressWarnings("ALL")
public class RecentListAdapter extends RecyclerView.Adapter<RecentListAdapter.KeywordItemHolder> {
    private final KeywordItemHolderListener listener;
    private List<KeywordList> list;

    public RecentListAdapter(Context context, List searchedKeywords, KeywordItemHolderListener listener) {

        this.list = searchedKeywords;
        this.listener = listener;
    }

    public void notifyKeywordAdapter(List searchedKeywords) {
        this.list = searchedKeywords;
        notifyDataSetChanged();
        // PrintLogging.printLog("", "here is list" + this.list);
    }


    @NonNull
    @Override
    public KeywordItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        KeywordItemBinding keywordItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.keyword_item, viewGroup, false);

        return new KeywordItemHolder(keywordItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull KeywordItemHolder viewHolder, int i) {
        try {
            final int pos = i;
            viewHolder.keywordItemBinding.setRowItem(list.get(i));
            viewHolder.keywordItemBinding.rootView.setOnClickListener(view -> listener.onItemClicked(list.get(pos)));
            //PrintLogging.printLog("", "SearchValue" + list.get(i).getKeywords());
        } catch (Exception ex) {
            Logger.e("RecentListAdpater", "" + ex.toString());
        }

    }

    @Override
    public int getItemCount() {
        int listLimit = 5;
        if (list.size() >= listLimit)
            return listLimit;
        else
            return list.size();
    }


    public interface KeywordItemHolderListener {
        void onItemClicked(KeywordList itemValue);
    }

    public class KeywordItemHolder extends RecyclerView.ViewHolder {

        final KeywordItemBinding keywordItemBinding;

        public KeywordItemHolder(@NonNull KeywordItemBinding binding) {
            super(binding.getRoot());
            this.keywordItemBinding = binding;
        }

    }
}

