package com.example.airpollutionmonitor.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airpollutionmonitor.R
import com.example.airpollutionmonitor.data.model.RecordsItem
import com.example.airpollutionmonitor.databinding.FragmentSearchBinding
import com.example.airpollutionmonitor.ui.AirPollutionListAdapter
import kotlinx.coroutines.*
import timber.log.Timber

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val args: SearchFragmentArgs by navArgs()
    private var airPollutionListAdapter = AirPollutionListAdapter()
    private var airPolluteList:ArrayList<RecordsItem> = ArrayList()
    private var searchJob: Job? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        airPolluteList = args.airPolluteList
        initView()
    }

    private fun initView() {
        with(binding.listSearch) {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = airPollutionListAdapter
        }


        binding.layoutButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        with(binding.layoutEditUrl) {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }

                override fun afterTextChanged(s: Editable?) {
                    searchJob?.cancel()
                    s?.toString().takeIf { !it.isNullOrEmpty() }?.let {
                        searchJob = goSearch(s.toString())
                    } ?: kotlin.run {
                        binding.listSearch.isVisible = false
                        binding.tvSearchMsg.isVisible = true
                        binding.tvSearchMsg.text = getText(R.string.search_site_name_tips)
                    }
                }
            })
        }
    }

    private fun goSearch(search: String) = lifecycleScope.launch(Dispatchers.Default) {
        delay(350)
        if (airPolluteList.isNotEmpty()) {
            airPolluteList.filter {
                it.county.contains(search) || it.sitename.contains(search)
            }.also { records ->
                withContext(Dispatchers.Main) {
                    airPollutionListAdapter.setData(records)
                    if (records.isEmpty()) {
                        binding.tvSearchMsg.text = String.format(getString(R.string.search_empty_message, search))
                        binding.tvSearchMsg.isVisible = true
                        binding.listSearch.isVisible = false
                    } else {
                        binding.tvSearchMsg.isVisible = false
                        binding.listSearch.isVisible = true
                    }
                }
            }
        } else {
            binding.tvSearchMsg.text = String.format(getString(R.string.search_empty_message, search))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}