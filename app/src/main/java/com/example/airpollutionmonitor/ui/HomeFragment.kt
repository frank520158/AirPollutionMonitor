package com.example.airpollutionmonitor.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airpollutionmonitor.R
import com.example.airpollutionmonitor.data.model.AirPolluteList
import com.example.airpollutionmonitor.data.model.RecordsItem
import com.example.airpollutionmonitor.databinding.FragmentHomeBinding
import com.example.airpollutionmonitor.ui.widget.HorizontalSpaceItemDecoration
import com.example.airpollutionmonitor.ui.widget.StartLinearSnapHelper
import timber.log.Timber
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel:AirMonitorViewModel by viewModel()

    private var bannerListAdapter = BannerListAdapter()
    private var airPollutionListAdapter = AirPollutionListAdapter()
    private var allAirDataList = mutableListOf<RecordsItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated")
        initView();
        viewModel.airMonitorUiState.observe(viewLifecycleOwner) { state ->
            state.getContentIfNotHandled()?.let {
                binding.rlProgress.isVisible = false
                when(it) {
                    is AirMonitorUiState.Success -> {
                        binding.airRecycler.isVisible = true
                        binding.bannerRecycler.isVisible = true
                        binding.layoutNetworkError.isVisible = false
                        allAirDataList.clear()
                        if (it.originList.isNotEmpty()) {
                            allAirDataList.addAll(it.originList)
                        }
                        bannerListAdapter.setData(it.bannerList)
                        airPollutionListAdapter.setData(it.mainList)
                    }

                    is AirMonitorUiState.Error, is AirMonitorUiState.NetworkError -> {
                        binding.layoutNetworkError.isVisible = true
                        binding.airRecycler.isVisible = false
                        binding.bannerRecycler.isVisible = false
                    }
                }
            }
        }

        loadData()
    }

    private fun initView() {
        with(binding.bannerRecycler) {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

            adapter = bannerListAdapter
            setHasFixedSize(true)
            StartLinearSnapHelper().attachToRecyclerView(this)
            addItemDecoration(HorizontalSpaceItemDecoration(16, 4))
        }

        with(binding.airRecycler) {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = airPollutionListAdapter
        }

        binding.buttonSearch.setOnClickListener {
            val airPolluteList = AirPolluteList()
            airPolluteList.addAll(allAirDataList)
            findNavController().navigate(HomeFragmentDirections.actionToSearchFragment(airPolluteList))
        }

        binding.textRefresh.setOnClickListener {
            binding.rlProgress.isVisible = true
            binding.layoutNetworkError.isVisible = false
            loadData()
        }
    }

    private fun loadData() {
        viewModel.fetchAirMonitorList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}