package panteao.make.ready.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import panteao.make.ready.R;
import panteao.make.ready.beanModel.responseModels.Item;
import panteao.make.ready.databinding.ItemMyPurchasesContentBinding;
import panteao.make.ready.databinding.RowEpisodeListBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;

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
        return new MyPurchasesAdapter.MyPurchasesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPurchasesViewHolder holder, int position) {

        try {
            holder.itemBinding.tvName.setText(list.get(position).getOfferTitle());
            if (list.get(position).getSubscriptionOfferType()!=null && !list.get(position).getSubscriptionOfferType().equalsIgnoreCase("")){
                holder.itemBinding.offerTypeValue.setText(list.get(position).getSubscriptionOfferType());
                if (list.get(position).getSubscriptionExpiryDate()!=null && !list.get(position).getSubscriptionExpiryDate().equalsIgnoreCase("")){
                    holder.itemBinding.tvDate.setText(AppCommonMethod.getMyPurchaseTimeTodate(Double.parseDouble(list.get(position).getSubscriptionExpiryDate())).toString());
                }else {
                    holder.itemBinding.dateLayout.setVisibility(View.GONE);
                }
            }else if (list.get(position).getVoDOfferType()!=null && !list.get(position).getVoDOfferType().equalsIgnoreCase("")){
                if (list.get(position).getRentalExpiryDate()!=null && !list.get(position).getRentalExpiryDate().equalsIgnoreCase("")){
                    holder.itemBinding.tvDate.setText(AppCommonMethod.getMyPurchaseTimeTodate(Double.parseDouble(list.get(position).getRentalExpiryDate())).toString());
                }else {
                    holder.itemBinding.dateLayout.setVisibility(View.GONE);
                }

                holder.itemBinding.offerTypeValue.setText(list.get(position).getVoDOfferType());
            }

            holder.itemBinding.priceValue.setText(list.get(position).getOrderCurrency()+" "+list.get(position).getOrderAmount());
            holder.itemBinding.paymentModeValue.setText(list.get(position).getPaymentProvider());
        }catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyPurchasesViewHolder extends RecyclerView.ViewHolder {
        final ItemMyPurchasesContentBinding itemBinding;

        MyPurchasesViewHolder(ItemMyPurchasesContentBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }
}
