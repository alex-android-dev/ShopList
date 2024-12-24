package com.example.shoplist.domain

class AddShopItemUseCase(private val shopItemRepository: ShopListRepository) {

    fun addShopItem(shopItem: ShopItem) {
        shopItemRepository.addShopItem(shopItem)
    }

}