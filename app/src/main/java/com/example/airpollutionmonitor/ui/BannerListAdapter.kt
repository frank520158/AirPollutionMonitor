package com.example.airpollutionmonitor.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.airpollutionmonitor.data.model.RecordsItem
import com.example.airpollutionmonitor.databinding.ItemBannerListBinding

class BannerListAdapter : RecyclerView.Adapter<BannerListAdapter.BannerItemViewHolder>() {

    private val airItemList: MutableList<RecordsItem> = mutableListOf()

    fun setData(data: List<RecordsItem>) {
        val diffCallback = AirPollutionDiffCallback(airItemList.toList(), data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        airItemList.clear()
        airItemList.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BannerListAdapter.BannerItemViewHolder {
        return BannerItemViewHolder(
            ItemBannerListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BannerItemViewHolder, position: Int) {
        val item = airItemList[position]
        holder.tvSiteId.text = item.siteId
        holder.tvSiteName.text = item.sitename
        holder.tvPmValue.text = item.pm2_5
        holder.tvCounty.text = item.county
        holder.tvStatus.text = item.status
    }

    override fun getItemCount(): Int = airItemList.size

    inner class BannerItemViewHolder(binding: ItemBannerListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvSiteId = binding.tvSiteId
        val tvSiteName = binding.tvSiteName
        val tvPmValue = binding.tvPmValue
        val tvCounty = binding.tvCounty
        val tvStatus = binding.tvStatus
    }
}