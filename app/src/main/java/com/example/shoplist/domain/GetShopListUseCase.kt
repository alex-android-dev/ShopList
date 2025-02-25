package com.example.shoplist.domain

import androidx.lifecycle.LiveData
import javax.inject.Inject

class GetShopListUseCase @Inject constructor(private val shopItemRepository: ShopListRepository) {

    fun getShopList(): LiveData<List<ShopItem>> {
        return shopItemRepository.getShopList()
    }

}