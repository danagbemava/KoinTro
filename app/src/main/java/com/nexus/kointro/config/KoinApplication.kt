package com.nexus.kointro.config

import android.app.Application
import com.nexus.kointro.config.di.appModule
import com.nexus.kointro.shared.data_source.LocalDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class KoinApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // initialize database
        LocalDataSource.initializeStore(this)

        // start dependency injection
        // this is done after setting up the datastore
        // because the modules will need access to the database
        startKoin {
            androidLogger()
            androidContext(this@KoinApplication)
            modules(appModule)
        }
    }
}