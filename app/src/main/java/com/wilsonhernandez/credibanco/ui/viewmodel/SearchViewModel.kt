package com.wilsonhernandez.credibanco.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilsonhernandez.credibanco.data.database.entities.TransactionsEntity
import com.wilsonhernandez.credibanco.data.repository.DatabaseRepository
import com.wilsonhernandez.credibanco.domain.CancelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val databaseRepository: DatabaseRepository,private val cancelUseCase: CancelUseCase):ViewModel() {
    private val _listTransaction = MutableLiveData<List<TransactionsEntity>>()
    val listTransaction: LiveData<List<TransactionsEntity>> = _listTransaction

    private var listTransactionTotal :List<TransactionsEntity> ?= null

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSnackbar = MutableLiveData<String>()
    val isSnackbar: LiveData<String> = _isSnackbar
    fun  getList(){
        viewModelScope.launch {
            val list= databaseRepository.getListTransactions()
            _listTransaction.postValue(list)
            listTransactionTotal=list
        }
    }

    fun getListFilter(searchText:String){
        _listTransaction.value=null
        val filter = listTransactionTotal?.filter { it.receiptId.contains(searchText,ignoreCase = true)}
        _listTransaction.value=filter
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
                            getList()
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