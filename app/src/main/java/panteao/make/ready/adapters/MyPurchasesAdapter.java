package panteao.make.ready.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import panteao.make.ready.R;
import panteao.make.ready.beanModel.responseModels.Item;
import panteao.make.ready.databinding.ItemMyPurchasesContentBinding;

public class MyPurchasesAdapter extends RecyclerView.Adapter<MyPurchasesAdapter.MyPurchasesViewHolder> {
    List<Item> list;
    Context context;
    private ItemMyPurchasesContentBinding binding;

    public MyPurchasesAdapter(Context context, List<Item> list){
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public MyPurchasesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_my_purchases_content, parent, false);
        return new MyPurchasesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPurchasesViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyPurchasesViewHolder extends RecyclerView.ViewHolder {
        public MyPurchasesViewHolder(@NonNull ItemMyPurchasesContentBinding binding) {
            super(binding.getRoot());
        }
        public void bind(Item item) {
            binding.setModel(item);
            binding.executePendingBindings();
        }
    }
}
