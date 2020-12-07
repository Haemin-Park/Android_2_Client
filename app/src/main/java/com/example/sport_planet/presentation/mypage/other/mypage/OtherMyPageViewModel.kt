package com.example.sport_planet.presentation.mypage.other.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sport_planet.data.response.mypage.HistoryResponse
import com.example.sport_planet.presentation.base.BaseViewModel
import com.example.sport_planet.remote.RemoteDataSourceImpl
import com.example.sport_planet.util.applySchedulers

class OtherMyPageViewModel : BaseViewModel() {

    private val _historyResponse = MutableLiveData<HistoryResponse>()
    val historyResponse: LiveData<HistoryResponse> get() = _historyResponse

    fun getMyProfile(userId: Long) {
        compositeDisposable.add(
            RemoteDataSourceImpl().getViewHistory(userId)
                .applySchedulers()
                .doOnSubscribe { isLoading.onNext(true) }
                .doAfterTerminate { isLoading.onNext(false) }
                .subscribe({
                    if (it.success) {
                        _historyResponse.value = it
                    }
                }, {})
        )
    }
}