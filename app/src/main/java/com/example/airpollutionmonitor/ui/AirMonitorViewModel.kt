package com.example.airpollutionmonitor.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airpollutionmonitor.data.model.RecordsItem
import com.example.airpollutionmonitor.data.repository.AirPollutionRepository
import com.example.airpollutionmonitor.util.ApiResultWrapper
import com.example.airpollutionmonitor.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class AirMonitorViewModel(
    private val airPollutionRepository: AirPollutionRepository,
) : ViewModel() {

    private val DATA_THRESHOLD_VALUE = 30

    private val _airMonitorUiState = MutableLiveData<Event<AirMonitorUiState>>()
    val airMonitorUiState: LiveData<Event<AirMonitorUiState>>
        get() = _airMonitorUiState

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
                val event = Event(AirMonitorUiState.Success(originDataList, bannerDataList, mainDataList))
                _airMonitorUiState.postValue(event)
            }
            is ApiResultWrapper.Failure -> {
                _airMonitorUiState.postValue(
                    Event(AirMonitorUiState.Error(response.error ?: ""))
                )
            }
            is ApiResultWrapper.NetworkError -> {
                _airMonitorUiState.postValue(
                    Event(AirMonitorUiState.NetworkError(response.toString()))
                )
            }
        }
    }
}

sealed class AirMonitorUiState {
    data class Success(val originList:List<RecordsItem>, val bannerList: List<RecordsItem>, val mainList: List<RecordsItem>) : AirMonitorUiState()
    data class Error(val msg: String) : AirMonitorUiState()
    data class NetworkError(val msg: String) : AirMonitorUiState()
}