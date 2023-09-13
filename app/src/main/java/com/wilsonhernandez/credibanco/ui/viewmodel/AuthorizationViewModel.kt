package com.wilsonhernandez.credibanco.ui.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilsonhernandez.credibanco.domain.AuthorizationUseCase
import com.wilsonhernandez.credibanco.data.database.entities.TransactionsEntity
import com.wilsonhernandez.credibanco.data.repository.DatabaseRepository
import com.wilsonhernandez.credibanco.settings.SettingsUtil
import com.wilsonhernandez.credibanco.util.NetworkConnectivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    val  context: Context,
    val databaseRepository: DatabaseRepository,
    val authorizationUseCase: AuthorizationUseCase,
    val settingsUtil: SettingsUtil,
    val networkConnectivity: NetworkConnectivity
) : ViewModel() {

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

    private val _succesAuthorization = MutableLiveData<Boolean>()
    val succesAuthorization: LiveData<Boolean> = _succesAuthorization

    private val _alertInternet = MutableLiveData<Boolean>()
    val alertInternet: LiveData<Boolean> = _alertInternet


    init {
        getInstallationId()
    }

    fun onAuthorizationChanged(
        id: String,
        commerceCode: String,
        terminalCode: String,
        amount: String,
        card: String
    ) {
        setValue(id, commerceCode, terminalCode, amount, card)
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
        id.isNotEmpty() && commerceCode.isNotEmpty() && terminalCode.isNotEmpty() && amount.isNotEmpty() && card.isNotEmpty() && card.length == 16

    @RequiresApi(Build.VERSION_CODES.O)
    fun onAuthorization(
        id: String,
        commerceCode: String,
        terminalCode: String,
        amount: String,
        card: String
    ) {
        if (!networkConnectivity.checkNetworkConnectivity()){
            _alertInternet.postValue(true)
            return
        }
        _isAuthorizationEnable.value = false
        _isLoading.value = true
        viewModelScope.launch {
            authorizationUseCase(
                id,
                commerceCode,
                terminalCode,
                amount,
                card,
                success = {
                    _isAuthorizationEnable.postValue(true)
                    _isLoading.postValue(false)

                    if (it.statusCode == "00") {
                        val authorizationEntity = TransactionsEntity(
                            receiptId = it.receiptId,
                            rrn = it.rrn,
                            statusCode = it.statusCode,
                            commerceCode = commerceCode,
                            terminalCode = terminalCode,
                            statusDescription = it.statusDescription
                        )
                        clearFields()

                        viewModelScope.launch {
                            databaseRepository.setTransaction(authorizationEntity)
                        }
                        _succesAuthorization.postValue(true)
                        _isAuthorizationEnable.postValue(false)

                    } else {
                        _isSnackbar.postValue("Autorización  erronea")
                        _succesAuthorization.postValue(false)
                    }

                },
                failed = {
                    _isAuthorizationEnable.postValue(true)
                    _isLoading.postValue(false)
                    _isSnackbar.postValue("Autorización erronea")
                    _succesAuthorization.postValue(false)
                })
        }
    }

    fun clearSnackbar() {
        _isSnackbar.postValue(null)
    }

    fun clearSuccesAuthorization() {
        _succesAuthorization.postValue(false)
    }

    private fun clearFields() {
        _commerceCode.postValue("")
        _terminalCode.postValue("")
        _amount.postValue("")
        _card.postValue("")
    }

    fun getInstallationId() {
        _id.postValue(settingsUtil.getAndroidId(context))
    }

    fun setValue(
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
    }

    fun enableAlertInternet(){
        _alertInternet.postValue(false)
    }
}