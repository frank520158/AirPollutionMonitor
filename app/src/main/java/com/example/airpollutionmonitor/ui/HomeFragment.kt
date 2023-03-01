package com.example.airpollutionmonitor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airpollutionmonitor.data.model.AirPolluteList
import com.example.airpollutionmonitor.data.model.RecordsItem
import com.example.airpollutionmonitor.databinding.FragmentHomeBinding
import com.example.airpollutionmonitor.ui.widget.HorizontalSpaceItemDecoration
import com.example.airpollutionmonitor.ui.widget.StartLinearSnapHelper
import com.example.airpollutionmonitor.util.safeNavigate
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


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
        observeData()
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
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        binding.buttonSearch.setOnClickListener {
            val airPolluteList = AirPolluteList()
            airPolluteList.addAll(allAirDataList)
            findNavController().safeNavigate(HomeFragmentDirections.actionToSearchFragment(airPolluteList))
        }

        binding.textRefresh.setOnClickListener {
            binding.rlProgress.isVisible = true
            binding.layoutNetworkError.isVisible = false
            loadData()
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.statusFlow.collect{
                    when(it) {
                        is AirMonitorUiState.Success -> {
                            binding.rlProgress.isVisible = false
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

                        is AirMonitorUiState.Error, is AirMonitorUiState.NetworkError,-> {
                            binding.rlProgress.isVisible = false
                            binding.layoutNetworkError.isVisible = true
                            binding.airRecycler.isVisible = false
                            binding.bannerRecycler.isVisible = false
                        }

                        is AirMonitorUiState.Loading -> {
                            binding.rlProgress.isVisible = true
                        }
                    }
                }
            }
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