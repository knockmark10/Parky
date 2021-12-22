package com.markoid.parky.home.presentation.adapters

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.markoid.parky.R
import com.markoid.parky.core.presentation.extensions.latLng
import com.markoid.parky.databinding.ItemParkingHistoryBinding
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.data.entities.isActive
import com.markoid.parky.home.presentation.callbacks.ParkingHistoryAdapterCallback
import com.markoid.parky.position.presentation.extensions.format

class ParkingHistoryAdapter : RecyclerView.Adapter<ParkingHistoryAdapter.ViewHolder>() {

    private val history: MutableList<ParkingSpotEntity> = mutableListOf()

    private var mListener: ParkingHistoryAdapterCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemParkingHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setParkingSpotInfo(history[position])
    }

    override fun getItemCount(): Int = history.size

    fun setParkingSpotHistory(historyList: List<ParkingSpotEntity>) {
        history.clear()
        history.addAll(historyList)
        notifyDataSetChanged()
    }

    fun flush() {
        history.clear()
        notifyDataSetChanged()
    }

    fun setAdapterListener(listener: ParkingHistoryAdapterCallback) {
        this.mListener = listener
    }

    fun deleteParkingSpot(spot: ParkingSpotEntity) {
        if (history.remove(spot)) {
            notifyItemRemoved(history.indexOf(spot))
            notifyItemRangeChanged(history.indexOf(spot), itemCount)
        }
        if (history.isEmpty()) mListener?.onDisplayEmptyState()
    }

    fun getActiveSpot(): ParkingSpotEntity? = history.firstOrNull { it.status.isActive }

    inner class ViewHolder(
        private val binding: ItemParkingHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val res: Resources
            get() = binding.root.resources

        fun setParkingSpotInfo(spot: ParkingSpotEntity) = with(binding) {
            statusIcon.background = if (spot.status.isActive) getDrawable(R.drawable.bg_active_spot)
            else getDrawable(R.drawable.bg_archived_spot)
            status.text = spot.status.name
            parkingType.text = spot.parkingType.getValue(res)
            coordinates.text = spot.latLng.format(5)
            address.text = spot.address
            actionDelete.isVisible = spot.status.isActive
            actionDelete.setOnClickListener { mListener?.onRequestDeleteParkingSpot(spot) }
            carPhoto.isVisible = spot.photo != null
            carPhoto.setImageURI(spot.photo)
            if (spot.status.isActive)
                binding.root.setOnClickListener { mListener?.onGoToUserLocation() }
        }

        private fun getDrawable(@DrawableRes id: Int): Drawable? =
            ContextCompat.getDrawable(binding.root.context, id)
    }
}
