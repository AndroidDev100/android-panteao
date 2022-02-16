package panteao.make.ready.fragments.more.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.PanteaoApplication;
import panteao.make.ready.R;
import panteao.make.ready.callbacks.commonCallbacks.MoreItemClickListener;
import panteao.make.ready.databinding.MoreItemBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.DrawableHelper;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.List;

public class MoreListAdapter extends RecyclerView.Adapter<MoreListAdapter.ViewHolder> {
    final MoreItemClickListener itemClickListener;
    private final List<String> itemsList;
    private final Activity mContext;
    private final boolean islogin;
    private long mLastClickTime;
    boolean isDownloadEnable;

    public MoreListAdapter(Activity context, List<String> itemsList, MoreItemClickListener call, boolean islogin, boolean isDownloadEnable) {
        this.itemsList = itemsList;
        this.mContext = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        this.itemClickListener = call;
        this.islogin = islogin;
        this.isDownloadEnable = isDownloadEnable;
    }

    @NonNull
    @Override
    public MoreListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MoreItemBinding moreItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.more_item, viewGroup, false);
        return new ViewHolder(moreItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.moreItemBinding.moreListTitle.setText("" + itemsList.get(i));
        setIcons(holder.moreItemBinding.moreListIcon, i, holder.moreItemBinding, itemsList.get(i));
    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void setIcons(ImageView v, int i, MoreItemBinding binding, String menuName) {

        if (!isDownloadEnable) {
            if (islogin) {

                if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {

                    switch (i) {
                        case 0:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.change_password, v);
                            break;
                        case 1:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.profile_icon, v);
                            break;
                        case 2:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.watch_list, v);
                            break;
                        case 3:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.watchhistory_icon, v);
                            break;
//                        case 4:
//                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.ic_save_alt, v);
//                            break;
                        case 4:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.membership_icon, v);
                            break;
                        case 5:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.my_purchase_icon, v);
                            break;
//                        case 5: //Coupon
//                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.redeem_coupon, v);
//                            break;
                        case 6:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.setting_icon, v);
                            break;
                        case 7:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.t_and_c_more, v);
                            break;
                        case 8:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.privacy_icon, v);
                            break;
                        case 9:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.about_us, v);
                            break;
                        case 10:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.log_out, v);
                            break;
//                    case 15:
//                        callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.veryfy_user, v);
//                        binding.verifyAccount.setVisibility(View.VISIBLE);
//                        break;
//                    case 16:
//                        callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.notification, v);
//                        break;


                        default:

                            break;
                    }
                } else {

                    switch (i) {
                        case 0:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.change_password, v);
                            break;
                        case 1:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.profile_icon, v);
                            break;
                        case 2:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.watch_list, v);
                            break;
                        case 3:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.watchhistory_icon, v);
                            break;
                    /*case 4:
                        callDrawableHelper(mContext, R.color.icons_color, R.drawable.ic_save_alt, v);
                        break;*/
                        case 4:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.membership_icon, v);
                            break;
//                        case 5:
//                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.redeem_coupon, v);
//                            break;
                        case 5:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.my_purchase_icon, v);
                            break;
                        case 6:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.setting_icon, v);
                            break;
                        case 7:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.t_and_c_more, v);
                            break;

                        case 8:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.privacy_icon, v);
                            break;
                        case 9:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.about_us, v);
                            break;
                        case 10:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.log_out, v);
                            break;
//                    case 15:
//                        callDrawableHelper(mContext, R.color.icons_color, R.drawable.veryfy_user, v);
//                        binding.verifyAccount.setVisibility(View.VISIBLE);
//                        break;
//
//                    case 16:
//                        callDrawableHelper(mContext, R.color.icons_color, R.drawable.notification, v);
//                        break;


                        default:

                            break;
                    }
                }
            } else {
                if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
                    switch (i) {
                        case 0:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.change_password, v);
                            break;
                        case 1:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.profile_icon, v);
                            break;
                        case 2:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.watch_list, v);
                            break;
                        case 3:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.watchhistory_icon, v);
                            break;
//                        case 4:
//                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.ic_save_alt, v);
//                            break;

                        case 4:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.membership_icon, v);
                            break;
//                        case 6://coupon
//                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.redeem_coupon, v);
//                            break;
                        case 5:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.membership_icon, v);
                            break;
                        case 6:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.my_purchase_icon, v);
                            break;
                        case 7:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.setting_icon, v);
                            break;

                        case 8:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.t_and_c_more, v);
                            break;

                        case 9:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.privacy_icon, v);
                            break;
                        case 10:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.about_us, v);
                            break;

                        default:
                            //new AppCommonMethods((HomeActivity) mContext).setImageVersion(v, R.drawable.setting_icon_more, false, mContext.getResources().getColor(R.color.more_item_title));

                            break;
                    }
                } else {
                    switch (i) {
                        case 0:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.change_password, v);
                            break;
                        case 1:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.profile_icon, v);
                            break;
                        case 2:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.watch_list, v);
                            break;

                        case 3:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.watchhistory_icon, v);
                            break;
                        case 4:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.membership_icon, v);
                            break;
//                        case 5:
//                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.redeem_coupon, v);
//                            break;
                        case 5:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.my_purchase_icon, v);
                            break;
                        case 6:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.setting_icon, v);
                            break;

                        case 7:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.t_and_c_more, v);
                            break;

                        case 8:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.privacy_icon, v);
                            break;
                        case 9:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.about_us, v);
                            break;

                        default:
                            //new AppCommonMethods((HomeActivity) mContext).setImageVersion(v, R.drawable.setting_icon_more, false, mContext.getResources().getColor(R.color.more_item_title));

                            break;
                    }
                }
            }

        } else {
            if (islogin) {

                if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {

                    switch (i) {
                        case 0:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.change_password, v);
                            break;
                        case 1:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.profile_icon, v);
                            break;
                        case 2:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.watch_list, v);
                            break;
                        case 3:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.watchhistory_icon, v);
                            break;
//                        case 4:
//                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.ic_save_alt, v);
//                            break;
                        case 4:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.membership_icon, v);
                            break;
                        case 5:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.my_purchase_icon, v);
                            break;
//                        case 6: //Coupon
//                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.redeem_coupon, v);
//                            break;
                        case 6:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.setting_icon, v);
                            break;
                        case 7:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.t_and_c_more, v);
                            break;
                        case 8:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.privacy_icon, v);
                            break;
                        case 9:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.about_us, v);
                            break;
                        case 10:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.log_out, v);
                            break;
//                    case 15:
//                        callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.veryfy_user, v);
//                        binding.verifyAccount.setVisibility(View.VISIBLE);
//                        break;
//                    case 16:
//                        callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.notification, v);
//                        break;
                        default:

                            break;
                    }
                } else {

                    switch (i) {
                        case 0:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.change_password, v);
                            break;
                        case 1:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.profile_icon, v);
                            break;
                        case 2:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.watch_list, v);
                            break;
                        case 3:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.watchhistory_icon, v);
                            break;

//                        case 4:
//                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.my_downloads_icon_more, v);
//                            break;
                    /*case 4:
                        callDrawableHelper(mContext, R.color.icons_color, R.drawable.ic_save_alt, v);
                        break;*/
                        case 4:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.membership_icon, v);
                            break;
//                        case 6:
//                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.redeem_coupon, v);
//                            break;
                        case 5:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.my_purchase_icon, v);
                            break;

                        case 6:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.setting_icon, v);
                            break;
                        case 7:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.t_and_c_more, v);
                            break;

                        case 8:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.privacy_icon, v);
                            break;

                        case 9:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.about_us, v);
                            break;
                        case 10:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.log_out, v);
                            break;
//                    case 15:
//                        callDrawableHelper(mContext, R.color.icons_color, R.drawable.veryfy_user, v);
//                        binding.verifyAccount.setVisibility(View.VISIBLE);
//                        break;
//
//                    case 16:
//                        callDrawableHelper(mContext, R.color.icons_color, R.drawable.notification, v);
//                        break;
                        default:

                            break;
                    }
                }
            } else {
                if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
                    switch (i) {
                        case 0:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.change_password, v);
                            break;
                        case 1:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.profile_icon, v);
                            break;
                        case 2:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.watch_list, v);
                            break;
                        case 3:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.watchhistory_icon, v);
                            break;
//                        case 4:
//                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.ic_save_alt, v);
//                            break;

                        case 4:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.membership_icon, v);
                            break;
//                        case 6://coupon
//                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.redeem_coupon, v);
//                            break;
                        case 5:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.my_purchase_icon, v);
                            break;
                        case 6:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.setting_icon, v);
                            break;

                        case 7:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.t_and_c_more, v);
                            break;

                        case 8:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.privacy_icon, v);
                            break;
                        case 9:
                            callDrawableHelper(mContext, R.color.more_icon_color, R.drawable.about_us, v);
                            break;

                        default:
                            //new AppCommonMethods((HomeActivity) mContext).setImageVersion(v, R.drawable.setting_icon_more, false, mContext.getResources().getColor(R.color.more_item_title));

                            break;
                    }
                } else {
                    switch (i) {
                        case 0:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.change_password, v);
                            break;
                        case 1:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.profile_icon, v);
                            break;
                        case 2:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.watch_list, v);
                            break;

                        case 3:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.watchhistory_icon, v);
                            break;
//                        case 4:
//                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.my_downloads_icon_more, v);
//                            break;
                        case 4:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.membership_icon, v);
                            break;
//                        case 6:
//                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.redeem_coupon, v);
//                            break;
                        case 5:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.my_purchase_icon, v);
                            break;
                        case 6:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.setting_icon, v);
                            break;

                        case 7:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.t_and_c_more, v);
                            break;

                        case 8:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.privacy_icon, v);
                            break;
                        case 9:
                            callDrawableHelper(mContext, R.color.icons_color, R.drawable.about_us, v);
                            break;

                        default:
                            //new AppCommonMethods((HomeActivity) mContext).setImageVersion(v, R.drawable.setting_icon_more, false, mContext.getResources().getColor(R.color.more_item_title));

                            break;
                    }
                }
            }

        }


    }

    public void callDrawableHelper(Context context, int mColor, int mDrawable, ImageView imageView) {
        DrawableHelper
                .withContext(context)
                .withColor(mColor)
                .withDrawable(mDrawable)
                .tint()
                .applyTo(imageView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final MoreItemBinding moreItemBinding;

        public ViewHolder(MoreItemBinding view) {
            super(view.getRoot());
            moreItemBinding = view;
            moreItemBinding.getRoot().setOnClickListener(view1 -> {
                if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी")) {
                    AppCommonMethod.updateLanguage("th", PanteaoApplication.getInstance());
                } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                    AppCommonMethod.updateLanguage("en", PanteaoApplication.getInstance());
                }
                Logger.e("Caption", itemsList.get(getLayoutPosition()) + mContext.getResources().getString(R.string.redeem_coupon));
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (itemsList.get(getLayoutPosition()).equalsIgnoreCase(mContext.getResources().getString(R.string.change_password))) {
                    itemClickListener.onClick(mContext.getResources().getString(R.string.change_password));
                } else if (itemsList.get(getLayoutPosition()).equalsIgnoreCase(mContext.getResources().getString(R.string.profile))) {
                    itemClickListener.onClick(mContext.getResources().getString(R.string.profile));
                } else if (itemsList.get(getLayoutPosition()).equalsIgnoreCase(mContext.getResources().getString(R.string.setting_title))) {
                    itemClickListener.onClick(mContext.getResources().getString(R.string.setting_title));

                } else if (itemsList.get(getLayoutPosition()).equalsIgnoreCase(mContext.getResources().getString(R.string.my_purchases))) {
                    itemClickListener.onClick(mContext.getResources().getString(R.string.my_purchases));

                }else if (itemsList.get(getLayoutPosition()).equalsIgnoreCase(mContext.getResources().getString(R.string.term_condition))) {
                    itemClickListener.onClick(mContext.getResources().getString(R.string.term_condition));
                } else if (itemsList.get(getLayoutPosition()).equalsIgnoreCase(mContext.getResources().getString(R.string.notification))) {
                    itemClickListener.onClick(mContext.getResources().getString(R.string.notification));
                } else if (itemsList.get(getLayoutPosition()).equalsIgnoreCase(mContext.getResources().getString(R.string.privacy_policy))) {
                    itemClickListener.onClick(mContext.getResources().getString(R.string.privacy_policy));
                } else if (itemsList.get(getLayoutPosition()).equalsIgnoreCase(mContext.getResources().getString(R.string.about_us))) {
                    itemClickListener.onClick(mContext.getResources().getString(R.string.about_us));
                } else if (itemsList.get(getLayoutPosition()).equalsIgnoreCase(mContext.getResources().getString(R.string.verify_user))) {
                    itemClickListener.onClick(mContext.getResources().getString(R.string.verify_user));
                } else if (itemsList.get(getLayoutPosition()).equalsIgnoreCase(mContext.getResources().getString(R.string.my_watchlist))) {
                    itemClickListener.onClick(mContext.getResources().getString(R.string.my_watchlist));
                } else if (itemsList.get(getLayoutPosition()).equalsIgnoreCase(mContext.getResources().getString(R.string.my_history))) {
                    itemClickListener.onClick(mContext.getResources().getString(R.string.my_history));
                } else if (itemsList.get(getLayoutPosition()).equalsIgnoreCase(mContext.getResources().getString(R.string.my_downloads))) {
                    itemClickListener.onClick(mContext.getResources().getString(R.string.my_downloads));
                } else if (itemsList.get(getLayoutPosition()).equalsIgnoreCase(mContext.getResources().getString(R.string.sign_out))) {
                    itemClickListener.onClick(mContext.getResources().getString(R.string.sign_out));
                } else if (itemsList.get(getLayoutPosition()).equalsIgnoreCase(mContext.getResources().getString(R.string.membership_plan))) {
                    itemClickListener.onClick(mContext.getResources().getString(R.string.membership_plan));
                } else if (itemsList.get(getLayoutPosition()).equalsIgnoreCase(mContext.getResources().getString(R.string.redeem_coupon))) {
                    itemClickListener.onClick(mContext.getResources().getString(R.string.redeem_coupon));
                }

            });
        }

    }

}
