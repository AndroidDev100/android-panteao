package panteao.make.ready.activities.search.adapter;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.callbacks.commonCallbacks.SearchClickCallbacks;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.R;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.databinding.RowSearchCategoryBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;

import java.util.List;

public class CategoriedSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RowSearchAdapter.RowSearchListener {
    private final Context context;
    private final List<RailCommonData> list;
    private final SearchClickCallbacks listener;

    private long mLastClickTime = 0;

    public CategoriedSearchAdapter(Context context, List<RailCommonData> demoList, SearchClickCallbacks listener) {
        this.context = context;
        this.list = demoList;
        this.listener = listener;
    }


    public void clearList(boolean clearList) {
        if (clearList) {
            this.list.clear();
            notifyDataSetChanged();
        }


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowSearchCategoryBinding binding;
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.row_search_category, parent, false);
//        if (viewType == 0) {
//            return new ShowTypeViewHolder(binding);
//        } else if (viewType == 1) {
//            return new EpisodeTypeViewHolder(binding);
//        } else if (viewType == 2) {
//            return new MovieTypeViewHolder(binding);
//        } else if (viewType == 3) {
//            return new SeriesTypeViewHolder(binding);
//        } else if (viewType == 4) {
//            return new LiveTypeViewHolder(binding);
//        } else if (viewType == 5) {
//            return new LiveTypeViewHolder(binding);
//        } else if (viewType == 6) {
//            return new LiveTypeViewHolder(binding);
//        } else if (viewType == 7) {
//            return new LiveTypeViewHolder(binding);
//        } else if (viewType == 8) {
//            return new LiveTypeViewHolder(binding);
//        } else {
//            return new ArticleTypeViewHolder(binding);
//        }
        return new MovieTypeViewHolder(binding);

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
        final int position = pos;
        final List<EnveuVideoItemBean> singleSectionItems = list.get(position).getEnveuVideoItemBeans();
        RowSearchAdapter itemListDataAdapter1 = new RowSearchAdapter(context, singleSectionItems, true, this);
        MovieTypeViewHolder movieTypeViewHolder = (MovieTypeViewHolder) viewHolder;
        setRecyclerProperties(movieTypeViewHolder.binding.recyclerView);
        movieTypeViewHolder.binding.recyclerView.setAdapter(itemListDataAdapter1);
        String header = "";
        switch (getItemViewType(pos)) {
            case 0:
                header = MediaTypeConstants.getInstance().getInstructor();
                break;
            case 1:
                header = context.getString(R.string.heading_series);
                break;
            case 2:
                header = context.getString(R.string.heading_episodes);
                break;
            case 3:
                header = MediaTypeConstants.getInstance().getTutorial();
                break;
            case 4:
                header = MediaTypeConstants.getInstance().getChapter();
                break;
            case 5:
                header = context.getString(R.string.heading_shows);
                break;

        }
        if (list.get(position).getTotalCount() == 1)
            movieTypeViewHolder.binding.tvTitle.setText(header + " - " + list.get(position).getTotalCount() + " " + context.getResources().getString(R.string.result_caps));
        else
            movieTypeViewHolder.binding.tvTitle.setText(header + " - " + list.get(position).getTotalCount() + " " + context.getResources().getString(R.string.results_caps));

        if (list.get(position).getTotalCount() < 5) {
            movieTypeViewHolder.binding.showAllSearch.setVisibility(View.GONE);
        }
        movieTypeViewHolder.binding.showAllSearch.setOnClickListener(view -> callResultActivity(list.get(position)));

//        if (viewHolder instanceof MovieTypeViewHolder) {
//            ((MovieTypeViewHolder) viewHolder).binding.recyclerView.setAdapter(itemListDataAdapter1);
//            if (list.get(position).getTotalCount() == 1)
//                ((MovieTypeViewHolder) viewHolder).binding.tvTitle.setText(context.getString(R.string.heading_movies) + " - " + list.get(position).getTotalCount() + " " + context.getResources().getString(R.string.result_caps));
//            else
//                ((MovieTypeViewHolder) viewHolder).binding.tvTitle.setText(context.getString(R.string.heading_movies) + " - " + list.get(position).getTotalCount() + " " + context.getResources().getString(R.string.results_caps));
//
//            if (list.get(position).getTotalCount() < 5) {
//                ((MovieTypeViewHolder) viewHolder).binding.showAllSearch.setVisibility(View.GONE);
//            }
//            ((MovieTypeViewHolder) viewHolder).binding.showAllSearch.setOnClickListener(view -> callResultActivity(list.get(position)));
//        } else if (viewHolder instanceof EpisodeTypeViewHolder) {
//            try {
//                setRecyclerProperties(((EpisodeTypeViewHolder) viewHolder).binding.recyclerView);
//                ((EpisodeTypeViewHolder) viewHolder).binding.recyclerView.setAdapter(itemListDataAdapter1);
//
//                if (list.get(position).getTotalCount() == 1)
//                    ((EpisodeTypeViewHolder) viewHolder).binding.tvTitle.setText(context.getString(R.string.heading_episodes) + " - " + list.get(position).getTotalCount() + " " + context.getResources().getString(R.string.result_caps));
//                else
//                    ((EpisodeTypeViewHolder) viewHolder).binding.tvTitle.setText(context.getString(R.string.heading_episodes) + " - " + list.get(position).getTotalCount() + " " + context.getResources().getString(R.string.results_caps));
//
//                if (list.get(position).getTotalCount() < 5) {
//                    ((EpisodeTypeViewHolder) viewHolder).binding.showAllSearch.setVisibility(View.GONE);
//                }
//
//
//                ((EpisodeTypeViewHolder) viewHolder).binding.showAllSearch.setOnClickListener(view -> callResultActivity(list.get(position)));
//
//            } catch (ClassCastException e) {
//                Logger.e("CatedSearchAdapter", "" + e.toString());
//            }
//        } else if (viewHolder instanceof ShowTypeViewHolder) {
//            try {
//                setRecyclerProperties(((ShowTypeViewHolder) viewHolder).binding.recyclerView);
//                ((ShowTypeViewHolder) viewHolder).binding.recyclerView.setAdapter(itemListDataAdapter1);
//
//                if (list.get(position).getTotalCount() == 1)
//                    ((ShowTypeViewHolder) viewHolder).binding.tvTitle.setText(context.getString(R.string.heading_shows) + " - " + list.get(position).getTotalCount() + " " + context.getResources().getString(R.string.result_caps));
//                else
//                    ((ShowTypeViewHolder) viewHolder).binding.tvTitle.setText(context.getString(R.string.heading_shows) + " - " + list.get(position).getTotalCount() + " " + context.getResources().getString(R.string.results_caps));
//
//                if (list.get(position).getTotalCount() < 5) {
//                    ((ShowTypeViewHolder) viewHolder).binding.showAllSearch.setVisibility(View.GONE);
//                }
//                // ((ShowTypeViewHolder) viewHolder).binding.showAllSearch.setOnClickListener(view -> callResultActivity(list.get(position)));
//
//            } catch (ClassCastException e) {
//                Logger.e("CatedSearchAdapter", "" + e.toString());
//            }
//        } else if (viewHolder instanceof SeriesTypeViewHolder) {
//            try {
//                setRecyclerProperties(((SeriesTypeViewHolder) viewHolder).binding.recyclerView);
//                ((SeriesTypeViewHolder) viewHolder).binding.recyclerView.setAdapter(itemListDataAdapter1);
//
//
//                if (list.get(position).getTotalCount() == 1)
//                    ((SeriesTypeViewHolder) viewHolder).binding.tvTitle.setText(context.getString(R.string.heading_series) + " - " + list.get(position).getTotalCount() + " " + context.getResources().getString(R.string.result_caps));
//                else
//                    ((SeriesTypeViewHolder) viewHolder).binding.tvTitle.setText(context.getString(R.string.heading_series) + " - " + list.get(position).getTotalCount() + " " + context.getResources().getString(R.string.results_caps));
//
//
//                if (list.get(position).getTotalCount() < 5) {
//                    ((SeriesTypeViewHolder) viewHolder).binding.showAllSearch.setVisibility(View.GONE);
//                }
//
//                ((SeriesTypeViewHolder) viewHolder).binding.showAllSearch.setOnClickListener(view -> callResultActivity(list.get(position)));
//                //((SeriesTypeViewHolder) viewHolder).binding.showAllSearch.setOnClickListener(view -> callResultActivity(list.get(position)));
//
//            } catch (ClassCastException e) {
//                Logger.e("CatedSearchAdapter", "" + e.toString());
//            }
//        } else if (viewHolder instanceof LiveTypeViewHolder) {
//            try {
//                setRecyclerProperties(((LiveTypeViewHolder) viewHolder).binding.recyclerView);
//                ((LiveTypeViewHolder) viewHolder).binding.recyclerView.setAdapter(itemListDataAdapter1);
//
//                if (list.get(position).getTotalCount() == 1)
//                    ((LiveTypeViewHolder) viewHolder).binding.tvTitle.setText(context.getString(R.string.heading_live) + " - " + list.get(position).getTotalCount() + " " + context.getResources().getString(R.string.result_caps));
//                else
//                    ((LiveTypeViewHolder) viewHolder).binding.tvTitle.setText(context.getString(R.string.heading_live) + " - " + list.get(position).getTotalCount() + " " + context.getResources().getString(R.string.results_caps));
//
//
//                if (list.get(position).getTotalCount() < 5) {
//                    ((LiveTypeViewHolder) viewHolder).binding.showAllSearch.setVisibility(View.GONE);
//                }
//
//                ((LiveTypeViewHolder) viewHolder).binding.showAllSearch.setOnClickListener(view -> callResultActivity(list.get(position)));
//                //((SeriesTypeViewHolder) viewHolder).binding.showAllSearch.setOnClickListener(view -> callResultActivity(list.get(position)));
//
//            } catch (ClassCastException e) {
//                Logger.e("CatedSearchAdapter", "" + e.toString());
//            }
//        } else if (viewHolder instanceof ArticleTypeViewHolder) {
//            try {
//                setRecyclerProperties(((ArticleTypeViewHolder) viewHolder).binding.recyclerView);
//                ((ArticleTypeViewHolder) viewHolder).binding.recyclerView.setAdapter(itemListDataAdapter1);
//
//
//                if (list.get(position).getTotalCount() == 1)
//                    ((ArticleTypeViewHolder) viewHolder).binding.tvTitle.setText(context.getResources().getString(R.string.article) + " - " + list.get(position).getTotalCount() + " " + context.getResources().getString(R.string.result_caps));
//                else
//                    ((ArticleTypeViewHolder) viewHolder).binding.tvTitle.setText(context.getResources().getString(R.string.article) + " - " + list.get(position).getTotalCount() + " " + context.getResources().getString(R.string.results_caps));
//
//
//                if (list.get(position).getTotalCount() < 5) {
//                    ((ArticleTypeViewHolder) viewHolder).binding.showAllSearch.setVisibility(View.GONE);
//                }
//
//                ((ArticleTypeViewHolder) viewHolder).binding.showAllSearch.setOnClickListener(view -> callResultActivity(list.get(position)));
//                //((SeriesTypeViewHolder) viewHolder).binding.showAllSearch.setOnClickListener(view -> callResultActivity(list.get(position)));
//
//            } catch (ClassCastException e) {
//                Logger.e("CatedSearchAdapter", "" + e.toString());
//            }
//        }


    }

    private void callResultActivity(RailCommonData model) {
        listener.onShowAllItemClicked(model);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getLayoutType();
    }


    private void setRecyclerProperties(RecyclerView recyclerView) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class EpisodeTypeViewHolder extends RecyclerView.ViewHolder {

        EpisodeTypeViewHolder(RowSearchCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        final RowSearchCategoryBinding binding;
    }

    class LiveTypeViewHolder extends RecyclerView.ViewHolder {

        LiveTypeViewHolder(RowSearchCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        final RowSearchCategoryBinding binding;
    }


    class SeriesTypeViewHolder extends RecyclerView.ViewHolder {

        SeriesTypeViewHolder(RowSearchCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        final RowSearchCategoryBinding binding;
    }

    class ArticleTypeViewHolder extends RecyclerView.ViewHolder {

        ArticleTypeViewHolder(RowSearchCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        final RowSearchCategoryBinding binding;
    }


    class MovieTypeViewHolder extends RecyclerView.ViewHolder {

        MovieTypeViewHolder(RowSearchCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        final RowSearchCategoryBinding binding;
    }


    class ShowTypeViewHolder extends RecyclerView.ViewHolder {

        ShowTypeViewHolder(RowSearchCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        final RowSearchCategoryBinding binding;
    }


    public interface ShowAllItemListener {
        void onItemClicked(RailCommonData itemValue);
    }

    @Override
    public void onRowItemClicked(EnveuVideoItemBean itemValue) {
        Log.d("categoryAdpater", "itemClick");
        AppCommonMethod.trackFcmEvent("Content Screen", "", context, 0);
//        AppCommonMethod.trackFcmCustomEvent(context, AppConstants.CONTENT_SELECT,itemValue.getAssetType(), itemValue.getSeriesId(), itemValue.getName() + "", 0, itemValue.getTitle(),0, itemValue.getId() + "", 0, 0, "", "");


        try {
//            AppCommonMethod.trackFcmEvent(itemValue.getTitle(), itemValue.getAssetType(), context, 0);
        } catch (Exception e) {

        }
        if (AppCommonMethod.getCheckKEntryId(itemValue.getkEntryId())) {
            String getVideoId = itemValue.getkEntryId();
            PrintLogging.printLog("", "SearchAssetType-->>" + itemValue.getAssetType());
            AppCommonMethod.launchDetailScreen(context, getVideoId, itemValue.getAssetType(), itemValue.getId(), "0", false,itemValue);
        } else {
            AppCommonMethod.launchDetailScreen(context, "", itemValue.getAssetType(), itemValue.getId(), "0", false,itemValue);

        }
    }
}
