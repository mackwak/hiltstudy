package com.example.uppmanageapp1.shop

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ShopModule {
    @Binds
    @Singleton
    abstract fun bindShopRepository(
        impl: DefaultShopRepository
    ): ShopRepository
}

