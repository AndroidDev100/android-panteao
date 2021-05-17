package panteao.make.ready.fragments.player.ui;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import panteao.make.ready.activities.detail.adapter.AllCommentAdapter;
import panteao.make.ready.activities.detail.ui.DetailActivity;
import panteao.make.ready.activities.detail.ui.EpisodeActivity;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.baseModels.BaseBindingFragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import panteao.make.ready.R;
import panteao.make.ready.beanModel.allComments.ItemsItem;
import panteao.make.ready.databinding.FragmentCommentLayoutBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.utils.constants.AppConstants;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsFragment extends BaseBindingFragment<FragmentCommentLayoutBinding> implements AllCommentAdapter.AllComentClickListener, AlertDialogFragment.AlertDialogListener {

    private String sampleUrl = "https://pbs.twimg.com/profile_images/630285593268752384/iD1MkFQ0.png";
    private int id;
    private String type;
    private ArrayList<ItemsItem> sampleList;
    private Dialog dialog;

    @Override
    protected FragmentCommentLayoutBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentCommentLayoutBinding.inflate(inflater);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // try {
        getCommentsList();
       /* } catch (Exception e) {
        }*/
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public void getCommentsList() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            id = bundle.getInt(AppConstants.BUNDLE_ID_FOR_COMMENTS);
            type = bundle.getString(AppConstants.BUNDLE_TYPE_FOR_COMMENTS);

            sampleList = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                ItemsItem item = new ItemsItem();
                item.setCommentText("Sammple Text Text");
                item.setDateCreated(System.currentTimeMillis());
                item.setId("123");
                item.setImage(sampleUrl);
                item.setStatus(true);
                item.setName("Mani");
                item.setVideoId(123);
                sampleList.add(item);
            }

            setSampleList();
        }
    }


    public void setSampleList() {
        getBinding().commentList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        AllCommentAdapter commentAdapter = new AllCommentAdapter(getActivity(), sampleList, this::onItemClicked);
        getBinding().commentList.setAdapter(commentAdapter);
        getBinding().commentList.bringToFront();

        getBinding().writeComment.setOnClickListener(v -> {
            postCommentClick();
        });
        getBinding().closeComments.setOnClickListener(v -> {

            if (getActivity() instanceof SeriesDetailActivity) {
                ((SeriesDetailActivity) getActivity()).removeCommentFragment();
            } else if (getActivity() instanceof EpisodeActivity) {
//                ((EpisodeActivity) getActivity()).removeCommentFragment();
            } else if (getActivity() instanceof DetailActivity) {
//                ((DetailActivity) getActivity()).removeCommentFragment();
            }

        });

    }


    public void postCommentClick() {

        getBinding().writeComment.setOnClickListener(v -> {

            View view = getLayoutInflater().inflate(R.layout.user_comment, null);
            dialog = new BottomSheetDialog(getActivity());
            dialog.setContentView(view);

            Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            view.findViewById(R.id.tv_comment).setVisibility(View.GONE);
            view.findViewById(R.id.seperator).setVisibility(View.GONE);
            view.findViewById(R.id.input_layout_comment).setVisibility(View.VISIBLE);
            EditText etUserComment = view.findViewById(R.id.evUserComment);
            ImageView ivPostComment = view.findViewById(R.id.tvPostComment);
            CircleImageView ivProfile = view.findViewById(R.id.userImage);

            etUserComment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            dialog.show();


        });


    }


    @Override
    public void onItemClicked(ItemsItem itemValue) {

    }

    @Override
    public void onFinishDialog() {

    }
}
