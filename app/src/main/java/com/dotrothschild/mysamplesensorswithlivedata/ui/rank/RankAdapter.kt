package com.dotrothschild.mysamplesensorswithlivedata.ui.rank

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dotrothschild.mysamplesensorswithlivedata.R
import com.dotrothschild.mysamplesensorswithlivedata.databinding.RecyclerviewItemRankBinding
import com.dotrothschild.mysamplesensorswithlivedata.model.Rank

class RankAdapter (
    private val context: Context,
    private val ranks: List<Rank>,
    private val rankCallback: (Rank, Int) -> Unit
) :
    RecyclerView.Adapter<RankAdapter.RankViewHolder>() {

    inner class RankViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecyclerviewItemRankBinding.bind(view)
        fun bind(rank: Rank, position: Int) {
            binding.apply {
                adapterRankName.text = rank.name
                adapterRankDate.text = rank.date
                adapterRankDescription.text = rank.description
                when (rank.id)                 {
                    1 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.a))) //
                    2 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.b))) //
                    3 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.c))) //
                    4 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.d))) //
                    5 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.e))) //
                    6 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.f))) //
                    7 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.g))) //
                    8 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.h))) //
                    9 -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.i))) //
                    else -> adapterImageRank.setImageDrawable((context.getDrawable(R.drawable.x))) // recruit
                }
                itemSelectView.setBackgroundColor(Color.parseColor("#4E5B31"))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RankViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item_rank, parent, false)
        )


    override fun onBindViewHolder(holder: RankAdapter.RankViewHolder, position: Int) {
        ranks[position].let { rank ->
            holder.bind(rank, position)
            // here goes the onclick event
            holder.binding.cardView.setOnClickListener {
                rankCallback.invoke(rank, position)
            }
        }
    }

    override fun getItemCount() = ranks.size
}