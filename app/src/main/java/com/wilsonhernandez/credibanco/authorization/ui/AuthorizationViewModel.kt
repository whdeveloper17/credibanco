package com.wilsonhernandez.credibanco.authorization.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilsonhernandez.credibanco.authorization.domain.AuthorizationUseCase
import kotlinx.coroutines.launch

class AuthorizationViewModel : ViewModel() {

    private val _id = MutableLiveData<String>()
    val id: LiveData<String> = _id

    private val _commerceCode = MutableLiveData<String>()
    val commerceCode: LiveData<String> = _commerceCode

    private val _terminalCode = MutableLiveData<String>()
    val terminalCode: LiveData<String> = _terminalCode

    private val _amount = MutableLiveData<String>()
    val amount: LiveData<String> = _amount

    private val _card = MutableLiveData<String>()
    val card: LiveData<String> = _card

    private val _isAuthorizationEnable = MutableLiveData<Boolean>()
    val isAuthorizationEnable: LiveData<Boolean> = _isAuthorizationEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSnackbar = MutableLiveData<String>()
    val isSnackbar: LiveData<String> = _isSnackbar

    private val _succesAuthorization= MutableLiveData<Boolean>()
    val succesAuthorization: LiveData<Boolean> = _succesAuthorization
    val authorizationUseCase = AuthorizationUseCase()


    fun onAuthorizationChanged(
        id: String,
        commerceCode: String,
        terminalCode: String,
        amount: String,
        card: String
    ) {
        _id.value = id
        _commerceCode.value = commerceCode
        _terminalCode.value = terminalCode
        _amount.value = amount
        _card.value = card
        _isAuthorizationEnable.value =
            enableAuthorization(id, commerceCode, terminalCode, amount, card)
    }

    fun enableAuthorization(
        id: String,
        commerceCode: String,
        terminalCode: String,
        amount: String,
        card: String
    ) =
        id.isNotEmpty() && commerceCode.isNotEmpty() && terminalCode.isNotEmpty() && amount.isNotEmpty() && card.isNotEmpty()

    @RequiresApi(Build.VERSION_CODES.O)
    fun onAuthorization() {
        _isAuthorizationEnable.value = false
        _isLoading.value = true
        viewModelScope.launch {
            authorizationUseCase(
                id.value!!,
                commerceCode.value!!,
                terminalCode.value!!,
                amount.value!!,
                card.value!!,
                success = {
                    _isAuthorizationEnable.postValue(true)
                    _isLoading.postValue(false)

                    if (it.statusCode == "00"){
                        clearFields()
                        _isAuthorizationEnable.postValue(false)
                        _succesAuthorization.postValue(true)
                    }else{
                        _isSnackbar.postValue("Autorización  erronea")
                        _succesAuthorization.postValue(false)
                    }

                },
                failed = {
                    _isAuthorizationEnable.postValue(true)
                    _isLoading.postValue(false)
                    _isSnackbar.postValue("Autorización erronea")
                })
        }
    }

    fun clearSnackbar() {
        _isSnackbar.postValue(null)
    }

    fun clearSuccesAuthorization() {
        _succesAuthorization.postValue(false)
    }

    private fun clearFields(){
        _id.postValue("")
        _commerceCode.postValue("")
        _terminalCode.postValue("")
        _amount.postValue("")
        _card.postValue("")
    }

}