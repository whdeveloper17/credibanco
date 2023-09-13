package com.wilsonhernandez.credibanco.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilsonhernandez.credibanco.domain.CancelUseCase
import com.wilsonhernandez.credibanco.data.database.dao.CrediBancoDao
import com.wilsonhernandez.credibanco.data.database.entities.TransactionsEntity
import com.wilsonhernandez.credibanco.data.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CancelViewModel @Inject constructor(private val databaseRepository: DatabaseRepository,private val cancelUseCase: CancelUseCase):ViewModel() {
    private val _listAuthorization = MutableLiveData<List<TransactionsEntity>>()
    val listAuthorization: LiveData<List<TransactionsEntity>> = _listAuthorization

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSnackbar = MutableLiveData<String>()
    val isSnackbar: LiveData<String> = _isSnackbar

    fun getListStatusApproved() {
        viewModelScope.launch {
            databaseRepository.getListTransactionsApproved().collect {
                _listAuthorization.postValue(it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onCancelAuthorization(item: TransactionsEntity) {

        viewModelScope.launch {
            cancelUseCase(
                item.receiptId,
                item.rrn,
                item.commerceCode,
                item.terminalCode,
                success = {

                    if (it.statusCode == "00") {
                        viewModelScope.launch {
                            databaseRepository.cancelTransactions(item.id,false)
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