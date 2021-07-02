package panteao.make.ready.player.tracks;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class TrackItemAdapter extends ArrayAdapter<TracksItem> {

    private Context context;
    private TracksItem[] trackItems;


    public TrackItemAdapter(Context context, int textViewResourceId, TracksItem[] trackItems) {
        super(context, textViewResourceId, trackItems);
        this.context = context;
        this.trackItems = trackItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(trackItems[position].getTrackName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(trackItems[position].getTrackName());
        return label;
    }

    @Override
    public int getCount() {
        return trackItems.length;
    }
}
