package com.wilsonhernandez.credibanco.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilsonhernandez.credibanco.core.room.AuthorizationDao
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(val dao: AuthorizationDao): ViewModel() {

    init {
        getCountAuthorization()
    }
    private val _countAuthorization = MutableLiveData<String>()
    val countAuthorization: LiveData<String> = _countAuthorization


     fun getCountAuthorization(){

             /*viewModelScope.launch {
                 dao.countAuthorization().onEach { value ->
                     if (value.isNullOrEmpty()) {
                         _countAuthorization.postValue("")
                     } else {
                         _countAuthorization.postValue(value)
                     }
                 }.launchIn(viewModelScope)
             }*/

       }

}