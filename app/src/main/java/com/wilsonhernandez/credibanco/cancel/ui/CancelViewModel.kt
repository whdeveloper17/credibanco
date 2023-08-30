package com.wilsonhernandez.credibanco.cancel.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilsonhernandez.credibanco.cancel.domain.CancelUseCase
import com.wilsonhernandez.credibanco.core.room.AuthorizationDao
import com.wilsonhernandez.credibanco.core.room.entity.AuthorizationEntity
import kotlinx.coroutines.launch

class CancelViewModel(val dao: AuthorizationDao):ViewModel() {
    private val _listAuthorization = MutableLiveData<List<AuthorizationEntity>>()
    val listAuthorization: LiveData<List<AuthorizationEntity>> = _listAuthorization

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSnackbar = MutableLiveData<String>()
    val isSnackbar: LiveData<String> = _isSnackbar

    val cancelUseCase = CancelUseCase()
    fun getListStatusApproved() {
        viewModelScope.launch {
            dao.getAuthorizedEntities().collect {
                _listAuthorization.postValue(it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onCancelAuthorization(item: AuthorizationEntity) {

        viewModelScope.launch {
            cancelUseCase(
                item.receiptId,
                item.rrn,
                item.commerceCode,
                item.terminalCode,
                success = {

                    if (it.statusCode == "00") {
                        viewModelScope.launch {
                            dao.updateEntityState(item.id,false)
                            _isSnackbar.postValue("Anulacion  exitosa")
                        }

                    } else {
                        _isSnackbar.postValue("Anulacion  erronea")

                    }

                },
                failed = {
                    _isSnackbar.postValue("Anulacion erronea")
                })
        }
    }

    fun clearSnackbar() {
        _isSnackbar.postValue(null)
    }
}