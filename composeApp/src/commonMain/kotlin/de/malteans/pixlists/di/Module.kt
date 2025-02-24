package de.malteans.pixlists.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import de.malteans.pixlists.list.data.database.DatabaseFactory
import de.malteans.pixlists.list.data.database.PixDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<PixDatabase>().pixDao }
}