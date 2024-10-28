package com.rspk.grocerylistapp.di

import com.rspk.grocerylistapp.firebase.FireBaseAccount
import com.rspk.grocerylistapp.firebase.FireBaseDatabase
import com.rspk.grocerylistapp.firebase.FireBaseStorage
import com.rspk.grocerylistapp.firebase.impl.FireBaseAccountImpl
import com.rspk.grocerylistapp.firebase.impl.FireBaseDatabaseImpl
import com.rspk.grocerylistapp.firebase.impl.FireBaseStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ClassConnectModule {

    @Binds abstract fun bindFireBaseAccount(impl: FireBaseAccountImpl): FireBaseAccount

    @Binds abstract fun bindFireBaseDatabase(impl: FireBaseDatabaseImpl): FireBaseDatabase

    @Binds abstract fun bindFireBaseStorage(impl: FireBaseStorageImpl): FireBaseStorage
}