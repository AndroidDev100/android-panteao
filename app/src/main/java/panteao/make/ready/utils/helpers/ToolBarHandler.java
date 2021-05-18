package panteao.make.ready.utils.helpers;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.app.ActivityOptionsCompat;

import panteao.make.ready.activities.search.ui.ActivitySearch;
import panteao.make.ready.databinding.ActivityDetailBinding;
import panteao.make.ready.databinding.ActivityEpisodeBinding;
import panteao.make.ready.databinding.ActivityHelpBinding;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.R;
import panteao.make.ready.databinding.ActivitySeriesDetailBinding;
import panteao.make.ready.databinding.DetailScreenBinding;
import panteao.make.ready.databinding.EpisodeScreenBinding;
import panteao.make.ready.databinding.FragmentMoreBinding;
import panteao.make.ready.databinding.LiveDetailBinding;
import panteao.make.ready.databinding.LoginBinding;
import panteao.make.ready.databinding.ProfileScreenBinding;
import panteao.make.ready.databinding.ToolbarBinding;

import panteao.make.ready.activities.search.ui.ActivitySearch;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;

@SuppressWarnings("EmptyMethod")
public class ToolBarHandler {
    final Activity activity;
    private long mLastClickTime = 0;


    public ToolBarHandler(Activity context) {
        this.activity = context;
    }

    public void setAction(final ActivityDetailBinding binding) {
        binding.lessButton.setOnClickListener(view -> {
            Logger.i("click", "less");
            //  binding.lessText.setText("Less");
        });
    }

    public void setAction(final LiveDetailBinding binding) {
        binding.lessButton.setOnClickListener(view -> {
            Logger.i("click", "less");
            //  binding.lessText.setText("Less");
        });
    }

    public void setEpisodeAction(final ActivityEpisodeBinding binding) {
        binding.lessButton.setOnClickListener(view -> {
            Logger.i("click", "less");
            //  binding.lessText.setText("Less");
        });
    }

    public void setAction2(final ActivitySeriesDetailBinding binding) {
        binding.lessButton.setOnClickListener(view -> {
            Logger.i("click", "less");
            //  binding.lessText.setText("Less");
        });
    }

    public void setAction(LoginBinding binding) {

       /* binding.signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityLauncher(activity).registerActivity(activity, RegisterActivity.class);
            }
        });
*/
    }

    public void setAction(FragmentMoreBinding binding) {
/*
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityLauncher(activity).loginActivity(activity, LoginActivity.class);
            }
        });
        binding.changepassText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityLauncher(activity).changePassword(activity, ChangePasswordActivity.class);
            }
        });
        binding.profileTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityLauncher(activity).ProfileActivity(activity, ProfileActivity.class);
            }
        });
        binding.notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityLauncher(activity).notificationActivity(activity, NotificationActivity.class);
            }
        });
  */

    }

    public void setAction(final ToolbarBinding toolbar, final String currentActivity) {

        switch (currentActivity) {
            case "home":
                toolbar.backLayout.setVisibility(View.GONE);
                toolbar.screenText.setVisibility(View.GONE);
                toolbar.homeIcon.setVisibility(View.VISIBLE);
                toolbar.llSearchIcon.setVisibility(View.VISIBLE);
                break;
            case "search":
                toolbar.backLayout.setVisibility(View.VISIBLE);
                toolbar.screenText.setVisibility(View.GONE);
                toolbar.homeIcon.setVisibility(View.GONE);
                toolbar.llSearchIcon.setVisibility(View.GONE);
                break;
            case "skip":
                toolbar.backLayout.setVisibility(View.VISIBLE);
                toolbar.screenText.setVisibility(View.GONE);
                toolbar.homeIcon.setVisibility(View.GONE);
                toolbar.llSearchIcon.setVisibility(View.GONE);
                break;
            case "potrait":
                toolbar.backLayout.setVisibility(View.VISIBLE);
                toolbar.screenText.setText("Movies");
                toolbar.homeIcon.setVisibility(View.GONE);
                toolbar.llSearchIcon.setVisibility(View.GONE);
                break;
            case "notification":
                toolbar.backLayout.setVisibility(View.VISIBLE);
                toolbar.screenText.setText("Notification");
                toolbar.titleText.setVisibility(View.VISIBLE);
                toolbar.llSearchIcon.setVisibility(View.GONE);
                toolbar.homeIcon.setVisibility(View.GONE);
                break;
            case "password":
                toolbar.backLayout.setVisibility(View.VISIBLE);
                toolbar.screenText.setText("Set New Password");
                toolbar.llSearchIcon.setVisibility(View.GONE);
                toolbar.homeIcon.setVisibility(View.GONE);
                break;
            case "profile":
                toolbar.backLayout.setVisibility(View.VISIBLE);
                toolbar.screenText.setText("User Profile");
                toolbar.llSearchIcon.setVisibility(View.GONE);
                toolbar.homeIcon.setVisibility(View.GONE);
                break;
            case "detail":
                toolbar.backLayout.setVisibility(View.VISIBLE);
                toolbar.screenText.setText("3 Srikandi");
                toolbar.homeIcon.setVisibility(View.GONE);
                toolbar.llSearchIcon.setVisibility(View.GONE);
                break;
            case "MoreFragment":
                toolbar.backLayout.setVisibility(View.GONE);
                toolbar.screenText.setVisibility(View.GONE);
                toolbar.homeIcon.setVisibility(View.VISIBLE);
                toolbar.llSearchIcon.setVisibility(View.GONE);
                toolbar.searchIcon.setVisibility(View.GONE);
                break;
            case "ForgotPasswordActivity":
                toolbar.backLayout.setVisibility(View.VISIBLE);
                toolbar.titleText.setVisibility(View.VISIBLE);
                toolbar.screenText.setText(activity.getResources().getString(R.string.forgot_password));
                toolbar.mediaRouteButton.setVisibility(View.GONE);
                toolbar.llSearchIcon.setVisibility(View.GONE);
                toolbar.homeIcon.setVisibility(View.GONE);
                break;
            default:
                toolbar.backLayout.setVisibility(View.VISIBLE);
                toolbar.homeIcon.setVisibility(View.GONE);
                break;
        }
        toolbar.llSearchIcon.setOnClickListener(view ->
                {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    new ActivityLauncher(activity).searchActivity(activity, ActivitySearch.class);
                }
        );

        toolbar.backLayout.setOnClickListener(view -> activity.onBackPressed());

    }

    public void setMoreListner(LinearLayout more_text, int id, String title, int flag, int type) {

        more_text.setOnClickListener(view -> {

            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            // new ActivityLauncher(activity).portraitListing(activity, ListingActivity.class, id, title, flag, type);
        });
    }

    public void setHomeAction(ToolbarBinding toolbar, Activity context) {
        toolbar.llSearchIcon.setOnClickListener(view -> {
            //  if (NetworkConnectivity.isOnline(context)) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(context,toolbar.searchIcon,"imageMain");
            Intent in = new Intent(context,ActivitySearch.class);
            context.startActivity(in,activityOptionsCompat.toBundle());
           // new ActivityLauncher(activity).searchActivity(activity, ActivitySearch.class);
            // }
            //       else
            //        new ToastHandler(activity).show(activity.getResources().getString(R.string.no_internet_connection));

        });
    }

    public void setSeriesAction(ActivitySeriesDetailBinding binding) {
    }

    public void profileAction(ProfileScreenBinding binding) {
        binding.toolbar.backLayout.setVisibility(View.VISIBLE);
        binding.toolbar.titleText.setVisibility(View.VISIBLE);
        binding.toolbar.searchIcon.setVisibility(View.GONE);
        binding.toolbar.homeIcon.setVisibility(View.GONE);
        binding.toolbar.screenText.setVisibility(View.VISIBLE);
        binding.toolbar.screenText.setText("My Profile");
    }

    public void setHelpAction(ActivityHelpBinding binding, String title) {
        binding.toolbar.llSearchIcon.setVisibility(View.GONE);
        binding.toolbar.backLayout.setVisibility(View.VISIBLE);
        binding.toolbar.homeIcon.setVisibility(View.GONE);
        binding.toolbar.mediaRouteButton.setVisibility(View.GONE);
        binding.toolbar.titleText.setVisibility(View.VISIBLE);
        binding.toolbar.screenText.setText(title);
    }
}