package panteao.make.ready.activities.purchase.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.R;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.activities.purchase.ui.PurchaseActivity;
import panteao.make.ready.activities.purchase.ui.VodOfferType;
import panteao.make.ready.beanModel.purchaseModel.PurchaseModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {


    private final Context context;
    private final List<PurchaseModel> list;
    private final OnPurchaseItemClick fragmentClickNetwork;
    private int rowIndex = -1;
    private String subscription = "";
    private String tvod = "";
    private boolean isClickable;
    private ArrayList<String> purchasedList;
    boolean fromClick=false;

    public PurchaseAdapter(Context context, List<PurchaseModel> list, OnPurchaseItemClick purchaseActivity) {
        this.context = context;
        this.list = list;
        this.fragmentClickNetwork = purchaseActivity;
        this.purchasedList=new ArrayList<>();

    }


    public void notifyAdapter(List<PurchaseModel> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_purchase, parent, false);

        return new PurchaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int position) {

        try {
            holder.description.setText(list.get(position).getDescription());
            if (list.get(position).getPurchaseOptions().equalsIgnoreCase(VodOfferType.PERPETUAL.name())) {
                holder.title.setText("" + list.get(position).getTitle());
                // holder.description.setText("" + context.getResources().getString(R.string.buy_video));
                holder.currency_price.setText("" + list.get(position).getCurrency() + " " + list.get(position).getPrice());
            }
            if (list.get(position).getPurchaseOptions().equalsIgnoreCase(VodOfferType.RENTAL.name())) {
                holder.title.setText("" + list.get(position).getTitle());
                //holder.description.setText(context.getResources().getString(R.string.rent_for) + " "+list.get(position).getOfferPeriodDuration()+" "+ context.getResources().getString(R.string.day_Rental));
                holder.currency_price.setText("" + list.get(position).getCurrency() + " " + list.get(position).getPrice());
            } else if (list.get(position).getPurchaseOptions().equalsIgnoreCase(VodOfferType.RECURRING_SUBSCRIPTION​.name())) {

                holder.title.setText("" + list.get(position).getTitle());
                //  holder.description.setText("" + context.getResources().getString(R.string.subscribe_to));
                holder.currency_price.setText("" + list.get(position).getCurrency() + " " + list.get(position).getPrice());
                if (list.get(position).getOfferPeriod().equalsIgnoreCase(VodOfferType.WEEKLY.name())) {
                    //  holder.plan_type.setText("" + context.getResources().getString(R.string.weekly_pack));
                } else if (list.get(position).getOfferPeriod().equalsIgnoreCase(VodOfferType.MONTHLY.name())) {
                    // holder.plan_type.setText("" + context.getResources().getString(R.string.montly));
                } else if (list.get(position).getOfferPeriod().equalsIgnoreCase(VodOfferType.QUARTERLY.name())) {
                    //holder.plan_type.setText("" + context.getResources().getString(R.string.quarterly));
                } else if (list.get(position).getOfferPeriod().equalsIgnoreCase(VodOfferType.HALF_YEARLY.name())) {
                    // holder.plan_type.setText("" + context.getResources().getString(R.string.half_yearly));
                } else if (list.get(position).getOfferPeriod().equalsIgnoreCase(VodOfferType.ANNUAL.name())) {
                    // holder.plan_type.setText("" + context.getResources().getString(R.string.annual));
                }


            } else if (list.get(position).getPurchaseOptions().equalsIgnoreCase(VodOfferType.ONE_TIME.name())) {
                holder.title.setText("" + list.get(position).getTitle());
                holder.currency_price.setText("" + list.get(position).getCurrency() + " " + list.get(position).getPrice());
                if (list.get(position).getPeriodType().contains(VodOfferType.DAYS.name())) {
                    // holder.description.setText(context.getResources().getString(R.string.buy_for) + " "+list.get(position).getOfferPeriodDuration()+" "+ context.getResources().getString(R.string.days));
                } else if (list.get(position).getPeriodType().contains(VodOfferType.WEEKS.name())) {
                    //  holder.description.setText(context.getResources().getString(R.string.buy_for) + " "+list.get(position).getOfferPeriodDuration()+" "+ context.getResources().getString(R.string.weeks));
                } else if (list.get(position).getPeriodType().contains(VodOfferType.MONTHS.name())) {
                    // holder.description.setText(context.getResources().getString(R.string.buy_for) + " "+list.get(position).getOfferPeriodDuration()+" "+ context.getResources().getString(R.string.months));
                } else if (list.get(position).getPeriodType().contains(VodOfferType.YEARS.name())) {
                    // holder.description.setText(context.getResources().getString(R.string.buy_for) + " "+list.get(position).getOfferPeriodDuration()+" "+ context.getResources().getString(R.string.years));
                }

            }

        }catch (Exception ignored){

        }


        holder.cardView.setOnClickListener(view -> {
            String option=list.get(position).getPurchaseOptions();
            if(option.equalsIgnoreCase(VodOfferType.RECURRING_SUBSCRIPTION​.name())){
                 if (subscription.equalsIgnoreCase("")){

                 }else {
                     return;
                 }
            }else {
                if (tvod.equalsIgnoreCase("")){

                }else {
                    return;
                }
            }
            boolean isCurrencySupported=false;
            for (int i = 0; i < SDKConfig.getInstance().getSupportedCurrencies().size() ; i++){
                if (SDKConfig.getInstance().getSupportedCurrencies().get(i).equalsIgnoreCase(list.get(position).getCurrency())){
                    isCurrencySupported=true;
                    break;
                }
            }
            if (isCurrencySupported) {
                if (!(((PurchaseActivity) context).isAlreadySubscribed && !list.get(position).getPurchaseOptions().equalsIgnoreCase("TVOD"))){
                resetSelectable(position,subscription,tvod);
            }else {
                    resetAll(holder);
                }
                fragmentClickNetwork.onPurchaseCardClick(true, list.get(position));
                notifyDataSetChanged();
            }else {
                Toast.makeText(context, context.getResources().getString(R.string.not_avail_for_Region), Toast.LENGTH_SHORT).show();
            }

        });


        if (list.get(position).isSelected()) {
            //purchasedList.add(list.get(position).getPurchaseOptions());
            if (!fromClick){
                if(list.get(position).getPurchaseOptions().equalsIgnoreCase(VodOfferType.RECURRING_SUBSCRIPTION​.name())){
                    subscription=list.get(position).getPurchaseOptions();
                }else {
                    tvod=list.get(position).getPurchaseOptions();
                }
                rowIndex = position;
            }

        }

        if (list.get(position).isSelected()) {

            holder.currency_price.setBackground(context.getDrawable(R.drawable.rounded_corner_textview_white));
            holder.currency_price.setTextColor(context.getResources().getColor(R.color.themeColorDark));
            holder.plan_type.setTextColor(context.getResources().getColor(R.color.rounded_button_text_color));
            list.get(position).setSelected(true);
            holder.title.setTextColor(context.getResources().getColor(R.color.rounded_button_text_color));
            holder.description.setTextColor(context.getResources().getColor(R.color.rounded_button_text_color));
            Resources res = context.getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.rounded_color_with_dark_theme);
            holder.cardView.setBackground(drawable);
        } else {

            holder.currency_price.setBackground(context.getDrawable(R.drawable.rounded_corner_textview_blue));
            holder.currency_price.setTextColor(Color.parseColor("#ffffff"));
            holder.plan_type.setTextColor(Color.parseColor("#ffffff"));
            holder.title.setTextColor(context.getResources().getColor(R.color.white));
            holder.description.setTextColor(context.getResources().getColor(R.color.white));
            list.get(position).setSelected(false);
            Resources res = context.getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.rounded_corner_inapp);
            holder.cardView.setBackground(drawable);

        }

    }

    private void resetSelectable(int pos,String subscription,String tvod) {
        rowIndex = pos;
        for (int i = 0; i < list.size(); i++) {
            boolean selected=list.get(i).isSelected();
            String options=list.get(i).getPurchaseOptions();
            if (selected && !subscription.equalsIgnoreCase("") || selected && !tvod.equalsIgnoreCase("")){
                list.get(i).setSelected(true);
            }else {
                if (i == pos) {
                    for (int j=0;j<list.size();j++){
                        String selectedOption=list.get(pos).getPurchaseOptions();
                        String selectedOptions=list.get(j).getPurchaseOptions();
                        if(selectedOption.equalsIgnoreCase(VodOfferType.RECURRING_SUBSCRIPTION​.name())){
                            if(selectedOptions.equalsIgnoreCase(VodOfferType.RECURRING_SUBSCRIPTION​.name())){
                                list.get(j).setSelected(false);
                            }

                        }else {
                            if(selectedOptions.equalsIgnoreCase(VodOfferType.RENTAL.name())){
                                list.get(j).setSelected(false);
                            }
                        }
                    }
                    list.get(i).setSelected(true);
                } else {
                    list.get(i).setSelected(false);
                }
            }
        }
        fromClick=true;
    }

    private void resetAll(PurchaseViewHolder holder) {
        rowIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            {
                list.get(i).setSelected(false);
                holder.currency_price.setBackground(context.getDrawable(R.drawable.rounded_corner_textview_blue));
                holder.currency_price.setTextColor(Color.parseColor("#ffffff"));
                list.get(i).setSelected(false);
                Resources res = context.getResources();
                Drawable drawable = res.getDrawable(R.drawable.rounded_corner_inapp);
                holder.cardView.setBackground(drawable);

            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnPurchaseItemClick {
        void onPurchaseCardClick(boolean click, PurchaseModel planName);
    }

    public class PurchaseViewHolder extends RecyclerView.ViewHolder {
        public final TextView title,description,plan_type;
        // public final CheckBox cbPurchase;
        public final TextView currency_price;
        public final RelativeLayout cardView;
        public ShimmerFrameLayout shimmerFrameLayout;
        public RelativeLayout relativeLayout;


        public PurchaseViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            currency_price = view.findViewById(R.id.currency_price);
            plan_type = view.findViewById(R.id.plan_type);
            cardView = view.findViewById(R.id.cv_tvod);
            // shimmerFrameLayout = (ShimmerFrameLayout) view.findViewById(R.id.sflMockContent);
            // relativeLayout = (RelativeLayout) view.findViewById(R.id.main_lay);
        }
    }

}
