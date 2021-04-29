package panteao.make.ready.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import panteao.make.ready.beanModel.beanModel.SingleItemModel;
import panteao.make.ready.R;

import java.util.ArrayList;

public class HeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public HeaderAdapter(Context context, ArrayList<SingleItemModel> itemsList) {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_demo_grid, null);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {


        public SingleItemRowHolder(View view) {
            super(view);


        }
    }
}
