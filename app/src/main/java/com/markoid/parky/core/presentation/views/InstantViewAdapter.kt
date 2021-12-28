package com.markoid.parky.core.presentation.views

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

class InstantViewAdapter
constructor(
    context: Context,
    @LayoutRes layoutId: Int,
    private val values: List<String>,
    @IdRes resourceId: Int = 0
) : ArrayAdapter<String>(context, layoutId, resourceId, values), Filterable {

    override fun getFilter(): Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            filterResults.values = values
            filterResults.count = values.size
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if ((results?.count ?: 0) > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}
