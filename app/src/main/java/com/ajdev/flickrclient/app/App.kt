package com.ajdev.flickrclient.app

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import com.ajdev.flickrclient.app.di.AppInjectionModules
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber
import timber.log.Timber.DebugTree


class App : Application(), KodeinAware {

    override val kodein = ConfigurableKodein()

    override fun onCreate() {
        super.onCreate()

        setupTimber()

        setupDependencyInjection()
    }

    private fun setupTimber() {
        Timber.plant(DebugTree())
    }

    private fun setupDependencyInjection() {
        kodein.apply {
            mutable = true

            clear()

            addImport(AppInjectionModules.module)

            addImport(Kodein.Module(javaClass.name) {
                bind<ContentResolver>() with singleton { this@App.contentResolver }
                bind<Context>() with singleton { this@App }
                bind<Application>() with singleton { this@App }
            })
        }
    }
}