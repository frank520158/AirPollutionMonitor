package com.example.airpollutionmonitor.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airpollutionmonitor.data.model.RecordsItem
import com.example.airpollutionmonitor.data.repository.AirPollutionRepository
import com.example.airpollutionmonitor.util.ApiResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class AirMonitorViewModel(
    private val airPollutionRepository: AirPollutionRepository,
) : ViewModel() {

    private val DATA_THRESHOLD_VALUE = 30

    private val _statusFlow: MutableStateFlow<AirMonitorUiState> = MutableStateFlow(AirMonitorUiState.Loading)
    val statusFlow: StateFlow<AirMonitorUiState> = _statusFlow.asStateFlow()

    fun fetchAirMonitorList() = viewModelScope.launch(Dispatchers.IO) {
        when (val response = airPollutionRepository.getAirMonitorData()) {
            is ApiResultWrapper.Success -> {
                val originDataList = mutableListOf<RecordsItem>()
                val bannerDataList = mutableListOf<RecordsItem>()
                val mainDataList = mutableListOf<RecordsItem>()

                response.value.records?.run {
                    forEach {item->
                        if (item.pm2_5.isNotEmpty() && item.pm2_5.toInt() > DATA_THRESHOLD_VALUE) {
                            mainDataList.add(item)
                        } else {
                            bannerDataList.add(item)
                        }
                    }
                    originDataList.addAll(this)
                }
                Timber.d("banner size: ${bannerDataList.size}, main size:${mainDataList.size}")
                _statusFlow.value = AirMonitorUiState.Success(originDataList, bannerDataList, mainDataList)
            }
            is ApiResultWrapper.Failure -> {
                _statusFlow.value = AirMonitorUiState.Error(response.error ?: "")
            }
            is ApiResultWrapper.NetworkError -> {
                _statusFlow.value = AirMonitorUiState.NetworkError(response.toString())
            }
        }
    }
}

sealed class AirMonitorUiState {
    data class Success(val originList:List<RecordsItem>, val bannerList: List<RecordsItem>, val mainList: List<RecordsItem>) : AirMonitorUiState()
    data class Error(val msg: String) : AirMonitorUiState()
    data class NetworkError(val msg: String) : AirMonitorUiState()
    object Loading : AirMonitorUiState()
}