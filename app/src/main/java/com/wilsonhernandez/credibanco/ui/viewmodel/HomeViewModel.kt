package com.wilsonhernandez.credibanco.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wilsonhernandez.credibanco.data.database.dao.CrediBancoDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val dao: CrediBancoDao): ViewModel() {

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