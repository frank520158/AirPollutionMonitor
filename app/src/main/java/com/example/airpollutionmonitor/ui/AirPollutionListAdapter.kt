package com.example.airpollutionmonitor.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.airpollutionmonitor.R
import com.example.airpollutionmonitor.data.model.RecordsItem
import com.example.airpollutionmonitor.databinding.ItemAirListBinding

class AirPollutionListAdapter : RecyclerView.Adapter<AirPollutionListAdapter.AirItemListViewHolder>() {
    private val airItemList: MutableList<RecordsItem> = mutableListOf()

    fun setData(data: List<RecordsItem>) {
        val diffCallback = AirPollutionDiffCallback(airItemList.toList(), data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        airItemList.clear()
        airItemList.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirItemListViewHolder {
        return AirItemListViewHolder(
            ItemAirListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AirItemListViewHolder, position: Int) {
        val item = airItemList[position]

        holder.tvSiteId.text = item.siteId
        holder.tvSiteName.text = item.sitename
        holder.tvPmValue.text = item.pm2_5
        holder.tvCounty.text = item.county

        if (item.status == "良好") {
            holder.btnControl.isVisible = false
            holder.tvStatus.text = holder.itemView.resources.getText(R.string.status_good)
        } else {
            holder.btnControl.isVisible = true
            holder.btnControl.setOnClickListener {
                val content = "Your click ${item.sitename} info."
                Toast.makeText(holder.itemView.context, content, Toast.LENGTH_SHORT).show()
            }
            holder.tvStatus.text = item.status
        }
    }

    override fun getItemCount(): Int = airItemList.size
    
    inner class AirItemListViewHolder(binding: ItemAirListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvSiteId = binding.tvSiteId
        val tvSiteName = binding.tvSiteName
        val tvPmValue = binding.tvPmValue
        val tvCounty = binding.tvCounty
        val tvStatus = binding.tvStatus
        val btnControl = binding.itemBtnControl
    }
}


class AirPollutionDiffCallback(
    private val oldList: List<RecordsItem>,
    private val newList: List<RecordsItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].siteId == newList[newItemPosition].siteId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}