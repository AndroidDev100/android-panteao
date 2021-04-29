package panteao.make.ready.activities.redeemcoupon.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import panteao.make.ready.repository.redeemCoupon.RedeemCouponLayer;
import panteao.make.ready.repository.redeemCoupon.RedeemModel;

public class RedeemViewModel extends AndroidViewModel {
    public RedeemViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<RedeemModel> redeemCoupon(String token, String coupon) {
        return RedeemCouponLayer.getInstance().redeemCoupon(token, coupon);
    }
}
