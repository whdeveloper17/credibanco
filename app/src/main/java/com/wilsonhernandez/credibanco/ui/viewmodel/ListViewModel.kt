package com.wilsonhernandez.credibanco.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilsonhernandez.credibanco.data.database.dao.CrediBancoDao
import com.wilsonhernandez.credibanco.data.database.entities.TransactionsEntity
import com.wilsonhernandez.credibanco.data.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val databaseRepository: DatabaseRepository) : ViewModel() {
    private val _listTransaction = MutableLiveData<List<TransactionsEntity>>()
    val listTransaction: LiveData<List<TransactionsEntity>> = _listTransaction


    fun  getList(){
        viewModelScope.launch {
            databaseRepository.getListTransactionsObserver().collect {
                _listTransaction.postValue(it)
            }
        }
    }
}