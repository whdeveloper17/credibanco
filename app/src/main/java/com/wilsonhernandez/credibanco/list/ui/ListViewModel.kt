package com.wilsonhernandez.credibanco.list.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilsonhernandez.credibanco.core.room.AuthorizationDao
import com.wilsonhernandez.credibanco.core.room.entity.AuthorizationEntity
import kotlinx.coroutines.launch

class ListViewModel(val dao: AuthorizationDao) : ViewModel() {
    private val _listAuthorization = MutableLiveData<List<AuthorizationEntity>>()
    val listAuthorization: LiveData<List<AuthorizationEntity>> = _listAuthorization


    fun  getList(){
        viewModelScope.launch {
            dao.getAuthorization().collect {
                _listAuthorization.postValue(it)
            }
        }
    }
}