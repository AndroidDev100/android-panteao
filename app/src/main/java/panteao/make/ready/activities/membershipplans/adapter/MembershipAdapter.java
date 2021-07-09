package panteao.make.ready.activities.membershipplans.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.R;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.activities.purchase.ui.VodOfferType;
import panteao.make.ready.beanModel.purchaseModel.PurchaseModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.cropImage.helpers.Logger;

import java.util.List;

public class MembershipAdapter extends RecyclerView.Adapter<MembershipAdapter.PurchaseViewHolder> {


    private final Context context;
    private final List<PurchaseModel> list;
    private final OnPurchaseItemClick fragmentClickNetwork;
    private int rowIndex = -1;
    private List<String> subscribedPlan;
    private boolean isClickable;
    private String localeCurrency;

    public MembershipAdapter(Context context, List<PurchaseModel> list, OnPurchaseItemClick purchaseActivity, boolean isClickable, String localeCurrency) {
        this.context = context;
        this.list = list;
        this.fragmentClickNetwork = purchaseActivity;
        //this.subscribedPlan=subscribedPlans;
        this.isClickable = isClickable;
        this.localeCurrency = localeCurrency;
    }


    public void notifyAdapter(List<PurchaseModel> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.membership_plan_row_item, parent, false);

        return new PurchaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int position) {
        if (list.get(position).isSelected()) {
            rowIndex = position;
        }

            try {
                if (list.get(position).getPurchaseOptions().equalsIgnoreCase(VodOfferType.RECURRING_SUBSCRIPTION​.name())) {

                    holder.title.setText("" + list.get(position).getTitle());
                    if (list.get(position).getDescription() != null) {
                        holder.description.setText(list.get(position).getDescription().toString());
                    } else {
                        holder.description.setText("");
                    }
                    //  holder.plan_type.setText(View.GONE);
                    holder.currency_price.setText(list.get(position).getPrice());
                    if (list.get(position).getOfferPeriod().equalsIgnoreCase(VodOfferType.WEEKLY.name())) {
                        //   holder.plan_type.setText("" + context.getResources().getString(R.string.weekly_pack));
                    } else if (list.get(position).getOfferPeriod().equalsIgnoreCase(VodOfferType.MONTHLY.name())) {
                        //  holder.plan_type.setText("" + context.getResources().getString(R.string.montly));
                    } else if (list.get(position).getOfferPeriod().equalsIgnoreCase(VodOfferType.QUARTERLY.name())) {
                        //  holder.plan_type.setText("" + context.getResources().getString(R.string.quarterly));
                    } else if (list.get(position).getOfferPeriod().equalsIgnoreCase(VodOfferType.HALF_YEARLY.name())) {
                        //  holder.plan_type.setText("" + context.getResources().getString(R.string.half_yearly));
                    } else if (list.get(position).getOfferPeriod().equalsIgnoreCase(VodOfferType.ANNUAL.name())) {
                        // holder.plan_type.setText("" + context.getResources().getString(R.string.annual));
                    }

                }

                if (!isClickable && (rowIndex == position)) {
                    //  holder.title.setText(context.getResources().getString(R.string.buy_subscription));
                } else {
                    //  holder.title.setText(context.getResources().getString(R.string.buy_subscription));
                }
                Logger.d("priceIs", "" + list.get(position).getPrice());

                if (list.get(position).getExpiryDate() > 0) {
                    holder.planExpLay.setVisibility(View.VISIBLE);
                    holder.planExpiryDateTxt.setText(AppCommonMethod.getDateFromTimeStamp(list.get(position).getExpiryDate()).toString());
                } else {
                    holder.planExpLay.setVisibility(View.GONE);
                }

            }catch (Exception ignored){

            }


       /* if (list.get(position).getPurchaseOptions().equalsIgnoreCase("free")) {
            String price = list.get(position).getPrice();
            holder.charges.setVisibility(View.GONE);
            holder.tvPrice.setText("0");
            holder.idr.setText("" + localeCurrency);

            holder.title.setText(context.getResources().getString(R.string.subscribe_to));
            holder.boldtext.setText(context.getResources().getString(R.string.free_plan));
            if (isClickable) {
                holder.title.setText(context.getResources().getString(R.string.subscribed_to));
            } else {
                holder.title.setText(context.getResources().getString(R.string.subscribe_to));
            }
            Logger.d("priceIs", "" + list.get(position).getPrice());

        } else if (list.get(position).getPurchaseOptions().equalsIgnoreCase("Monthly")) {

            holder.idr.setText("" + localeCurrency);
            holder.tvPrice.setText("" + list.get(position).getPrice());
            holder.charges.setText(context.getResources().getString(R.string.per_Month));
            holder.boldtext.setText(context.getResources().getString(R.string.monthly_pack));
            if (!isClickable && (rowIndex == position)) {
                holder.title.setText(context.getResources().getString(R.string.subscribed_to));
            } else {
                holder.title.setText(context.getResources().getString(R.string.subscribe_to));
            }

        } else if (list.get(position).getPurchaseOptions().equalsIgnoreCase("Weekly Pack")) {
            holder.idr.setText("" + localeCurrency);

            holder.tvPrice.setText("" + list.get(position).getPrice());
            holder.charges.setText(context.getResources().getString(R.string.per_Week));
            holder.boldtext.setText(context.getResources().getString(R.string.weekly_pack));
            if (!isClickable && (rowIndex == position)) {
                holder.title.setText(context.getResources().getString(R.string.subscribed_to));
            } else {
                holder.title.setText(context.getResources().getString(R.string.subscribe_to));
            }
        }*//*else if (list.get(position).getPurchaseOptions().equalsIgnoreCase("Daily")) {

            holder.title.setText(context.getResources().getString(R.string.subscribed_daily_pack));
            holder.tvPrice.setText("" + list.get(position).getPrice());
            holder.charges.setText(context.getResources().getString(R.string.per_day));
            holder.boldtext.setText(context.getResources().getString(R.string.daily_pack));

        }*/


        holder.cardView.setOnClickListener(view -> {
            if (isClickable) {
                boolean isCurrencySupported=false;
                for (int i = 0 ; i < SDKConfig.getInstance().getSupportedCurrencies().size() ; i++){
                    if (SDKConfig.getInstance().getSupportedCurrencies().get(i).equalsIgnoreCase(list.get(position).getCurrency())){
                        isCurrencySupported=true;
                        break;
                    }
                }
                if (isCurrencySupported){
                    fragmentClickNetwork.onPurchaseCardClick(true, 0, list.get(position).getPurchaseOptions(),list.get(position));
                    resetSelectable(position);
                    notifyDataSetChanged();
                }else {
                    Toast.makeText(context, "This feature is not available in your region", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (rowIndex == position) {

            holder.currency_price.setBackground(context.getDrawable(R.drawable.rounded_corner_textview_white));
            holder.currency_price.setTextColor(context.getResources().getColor(R.color.themeColorDark));
          //  holder.plan_type.setTextColor(context.getResources().getColor(R.color.rounded_button_text_color));
            list.get(position).setSelected(true);
            holder.title.setTextColor(context.getResources().getColor(R.color.rounded_button_text_color));
            holder.description.setTextColor(context.getResources().getColor(R.color.rounded_button_text_color));
            holder.planPurchasedTxt.setTextColor(context.getResources().getColor(R.color.rounded_button_text_color));
            holder.planExpiryTxt.setTextColor(context.getResources().getColor(R.color.rounded_button_text_color));
            holder.planExpiryDateTxt.setTextColor(context.getResources().getColor(R.color.rounded_button_text_color));
            if (!isClickable){
               // holder.description.setText("" + context.getResources().getString(R.string.subscribed_to));
                holder.planPurchasedLay.setVisibility(View.VISIBLE);
                holder.description.setVisibility(View.GONE);
            }else {
               // holder.description.setText("" + context.getResources().getString(R.string.subscribe_to));
                holder.description.setVisibility(View.VISIBLE);

            }
            Resources res = context.getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.rounded_color_with_dark_theme);
            holder.cardView.setBackground(drawable);

        } else {

            holder.currency_price.setBackground(context.getDrawable(R.drawable.rounded_corner_textview_blue));
            holder.currency_price.setTextColor(Color.parseColor("#ffffff"));
           // holder.plan_type.setTextColor(Color.parseColor("#ffffff"));
            holder.description.setVisibility(View.VISIBLE);
            holder.title.setTextColor(context.getResources().getColor(R.color.white));
            holder.description.setTextColor(context.getResources().getColor(R.color.white));
            holder.planPurchasedTxt.setTextColor(context.getResources().getColor(R.color.white));
            holder.planExpiryTxt.setTextColor(context.getResources().getColor(R.color.white));
            holder.planExpiryDateTxt.setTextColor(context.getResources().getColor(R.color.white));
          //  holder.description.setText("" + context.getResources().getString(R.string.subscribe_to));
            list.get(position).setSelected(false);
            Resources res = context.getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.rounded_corner_inapp);
            holder.cardView.setBackground(drawable);
            holder.planPurchasedLay.setVisibility(View.INVISIBLE);
        }



    }

    private void resetSelectable(int pos) {
        rowIndex = pos;
        for (int i = 0; i < list.size(); i++) {
            if (i == pos) {
                list.get(i).setSelected(true);
            } else {
                list.get(i).setSelected(false);
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnPurchaseItemClick {
        void onPurchaseCardClick(boolean click, int position, String planName,PurchaseModel purchaseModel);
    }

    public class PurchaseViewHolder extends RecyclerView.ViewHolder {
        public final TextView title,description;
        // public final CheckBox cbPurchase;
        public final TextView currency_price;
        public final RelativeLayout cardView;
        public ShimmerFrameLayout shimmerFrameLayout;
        public RelativeLayout relativeLayout;
        public LinearLayout planExpLay;
        public TextView planExpiryTxt;
        public TextView planExpiryDateTxt;
        public LinearLayout planPurchasedLay;
        public TextView planPurchasedTxt;


        public PurchaseViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            currency_price = view.findViewById(R.id.currency_price);
            cardView = view.findViewById(R.id.cv_tvod);
            planExpLay= view.findViewById(R.id.planExpLay);
            planExpiryTxt=view.findViewById(R.id.planExpiryTxt);
            planExpiryDateTxt=view.findViewById(R.id.planExpiryDateTxt);
            planPurchasedLay=view.findViewById(R.id.planPurchasedLay);
            planPurchasedTxt=view.findViewById(R.id.planPurchasedTxt);
        }
    }

}
