package com.example.shoplist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoplist.domain.AddShopItemUseCase
import com.example.shoplist.domain.EditShopItemUseCase
import com.example.shoplist.domain.GetShopItemUseCase
import com.example.shoplist.domain.ShopItem
import kotlinx.coroutines.launch
import javax.inject.Inject

const val EMPTY_STRING = ""

class ShopItemViewModel @Inject constructor(
    private val getShopItemUseCase: GetShopItemUseCase,
    private val addShopItemUseCase: AddShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase,
) : ViewModel() {

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen


    fun getShopItem(shopItemId: Int) {
        viewModelScope.launch {
            _shopItem.value = getShopItemUseCase.getShopItemById(shopItemId)
        }
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        viewModelScope.launch {
            val name = parseName(inputName)
            val count = parseCount(inputCount)
            val fieldsValid = validateInput(name, count)
            if (fieldsValid) {
                val shopItem = ShopItem(name, count, true)
                addShopItemUseCase.addShopItem(shopItem)
                finishWork()
            }
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        viewModelScope.launch {
            val name = parseName(inputName)
            val count = parseCount(inputCount)
            val fieldsValid = validateInput(name, count)
            if (fieldsValid) {
                shopItem.value?.let {
                    editShopItemUseCase.editShopItem(
                        it.copy(
                            name = name,
                            count = count,
                        )
                    )
                    finishWork()
                }
            }
        }
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: EMPTY_STRING
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}