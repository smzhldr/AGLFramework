package com.aglframework.smzh.agl_framework.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aglframework.smzh.agl_framework.R
import com.aglframework.smzh.agl_framework.data.Filter
import kotlinx.android.synthetic.main.item_filter_list.view.*

class FilterListAdapter(private val items: List<Filter>, private val listener: FilterApplyListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var currentHolder: FilterHolder? = null
    private var selectPosition = 0

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_filter_list, p0, false)
        return FilterHolder(view)
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val filter = items[p1]
        if (p0 is FilterHolder) {
            p0.bind(filter, p1, listener)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class FilterHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(filter: Filter, position: Int, listener: FilterApplyListener) {
            view.run {
                image.setImageResource(filter.previewUrl)
                text.text = filter.showName
                if (position == selectPosition) {
                    image_select_bg?.visibility = View.VISIBLE
                    currentHolder = this@FilterHolder
                } else {
                    image_select_bg?.visibility = View.GONE
                }
            }

            view.setOnClickListener {
                if (position != selectPosition) {
                    listener.onFilterApply(filter)
                    selectPosition = position
                    currentHolder?.view?.image_select_bg?.visibility = View.GONE
                    currentHolder = this
                    currentHolder?.view?.image_select_bg?.visibility = View.VISIBLE
                }
            }
        }
    }

    interface FilterApplyListener {

        fun onFilterApply(filter: Filter)

        fun onIntensityChange(intensity: Int) {}

        fun hideFilterFragment() = 0
    }
}