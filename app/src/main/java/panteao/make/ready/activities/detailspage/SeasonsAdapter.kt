package panteao.make.ready.activities.detailspage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import panteao.make.ready.R
import panteao.make.ready.activities.detailspage.listeners.SeasonsClickListener
import panteao.make.ready.beanModel.selectedSeason.SelectedSeasonModel
import panteao.make.ready.databinding.LayoutSeasonItemBinding


class SeasonsAdapter(
    private val seasonsList: ArrayList<SelectedSeasonModel>,
    private val mListener: SeasonsClickListener
) : RecyclerView.Adapter<SeasonsAdapter.MyViewHolder>() {

    lateinit var seasonItemBinding: LayoutSeasonItemBinding


    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        seasonItemBinding.episodeTitle.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mListener.onSeasonClick(seasonsList.get(position).selectedId)
            }
        })

        seasonItemBinding.episodeTitle.text = seasonsList.get(position).list
    }

    override fun onCreateViewHolder(parent: ViewGroup, item: Int): SeasonsAdapter.MyViewHolder {
        seasonItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_season_item,
            parent,
            false
        )
        return MyViewHolder(seasonItemBinding.root)
    }

    override fun getItemCount(): Int {
        return seasonsList.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}