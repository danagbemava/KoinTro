package com.nexus.kointro.shared.data_source

import android.content.Context
import com.nexus.kointro.shared.models.MyObjectBox
import io.objectbox.BoxStore

object LocalDataSource {

    lateinit var store: BoxStore
        private set

    fun initializeStore(context: Context) {
        store = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
    }
}