package com.markoid.parky.settings.presentation.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.markoid.parky.core.presentation.extensions.layoutInflater
import com.markoid.parky.databinding.ItemExclusionZoneBinding
import com.markoid.parky.home.data.entities.ExclusionZoneEntity
import com.markoid.parky.settings.presentation.callbacks.ExclusionZonesAdapterCallback

class ExclusionZonesAdapter : RecyclerView.Adapter<ExclusionZonesAdapter.ItemViewHolder>() {

    private val exclusionZoneEntities: MutableList<ExclusionZoneEntity> = mutableListOf()

    private var mListener: ExclusionZonesAdapterCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(ItemExclusionZoneBinding.inflate(parent.layoutInflater, parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setData(exclusionZoneEntities[position])
    }

    override fun getItemCount(): Int = exclusionZoneEntities.size

    fun setExclusionZones(zoneEntities: List<ExclusionZoneEntity>) {
        exclusionZoneEntities.clear()
        exclusionZoneEntities.addAll(zoneEntities.toList())
        notifyDataSetChanged()
    }

    fun setOnAdapterClickListener(listener: ExclusionZonesAdapterCallback) {
        this.mListener = listener
    }

    inner class ItemViewHolder(
        private val binding: ItemExclusionZoneBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setData(zoneEntity: ExclusionZoneEntity) = with(binding) {
            exclusionZoneName.text = zoneEntity.name
            exclusionZoneColor.text = zoneEntity.color.getLocalizedValue(root.resources)
            deleteBtn.setOnClickListener { mListener?.onDeleteExclusionZone(zoneEntity.id) }
            editBtn.setOnClickListener { mListener?.onEditExclusionZone(zoneEntity) }
        }
    }
}
