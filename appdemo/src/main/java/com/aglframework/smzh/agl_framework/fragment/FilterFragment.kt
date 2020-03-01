package com.aglframework.smzh.agl_framework.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.aglframework.smzh.agl_framework.R
import com.aglframework.smzh.agl_framework.activity.CameraActivity
import com.aglframework.smzh.agl_framework.adapter.FilterListAdapter
import com.aglframework.smzh.agl_framework.data.Filter
import com.aglframework.smzh.agl_framework.data.FilterDataManager
import com.aglframework.smzh.agl_framework.databinding.FragmentFilterListBinding
import com.aglframework.smzh.agl_framework.viewmodel.FilterViewModel

class FilterFragment : Fragment(), FilterListAdapter.FilterApplyListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private lateinit var dataBinding: FragmentFilterListBinding
    lateinit var viewModel: FilterViewModel
    private val adapter: FilterListAdapter by lazy { FilterListAdapter(FilterDataManager.get().getFilterList(), this) }
    private lateinit var listener: FilterListAdapter.FilterApplyListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is CameraActivity) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter_list, container, false)
        viewModel = ViewModelProviders.of(this).get(FilterViewModel::class.java)
        dataBinding.viewmodel = viewModel
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        dataBinding.recyclerView.adapter = adapter
        dataBinding.filterLevel.setOnSeekBarChangeListener(this)
        dataBinding.hideBtn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        v?.id.let {
            when (it) {
                R.id.hideBtn -> {
                    listener.hideFilterFragment()
                }
                else -> {

                }
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (listener is CameraActivity) {
            listener.onIntensityChange(progress)
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onFilterApply(filter: Filter) {
        if (listener is CameraActivity) {
            listener.onFilterApply(filter)
        }
    }

}