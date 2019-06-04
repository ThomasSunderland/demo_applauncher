/*
 * Copyright. Baby Bill's Software Factory.
 */
package com.babybillssoftwarefactory.applauncherdemo.model


//region import directives

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.babybillssoftwarefactory.applauncherdemo.AppLauncherDemo
import com.babybillssoftwarefactory.applauncherdemo.configuration.AppLauncherConfiguration
import com.babybillssoftwarefactory.applauncherdemo.constants.APP_LAUNCHER_CONFIGURATION_FILE
import com.babybillssoftwarefactory.applauncherdemo.event.AppLauncherEvents
import com.babybillssoftwarefactory.applauncherdemo.event.interfaces.IEventHandler
import com.babybillssoftwarefactory.applauncherdemo.util.logging.Logger
import com.google.gson.Gson
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.util.*

//endregion import directives


/**
 * Encapsulates the raw data that is used within the App Launcher
 *
 * @author Thomas Sunderland
 */
class AppLauncherModel : IEventHandler, Observable() {

    //region data members

    /**
     * Collection of apps that can be launched (based on file configuration)
     */
    val appLauncherApps = MutableLiveData<List<String>>(
        readAppLauncherConfigurationFromFile().apps
    )

    /**
     * Flag indicating whether or not apps should be launched in floating (or freeform) mode
     * Note: this is only supported on Android Nougat and above and only on some devices
     */
    val appLauncherFloatApps = MutableLiveData<Boolean>(
        readAppLauncherConfigurationFromFile().floatApps
    )
    //endregion data members


    //region event handlers

    /**
     * Process AppLauncher related events and update our observables as necessary
     *
     * @param appLauncherEvent Instance of AppLauncherEvents (CREATE, MODIFY, or DELETE)
     */
    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onEvent(appLauncherEvent: AppLauncherEvents) {
        try {
            when (appLauncherEvent) {
                is AppLauncherEvents.AppLauncherFileConfigurationCreatedEvent -> {
                    Logger.i("Received AppLauncherEvents.AppLauncherFileConfigurationCreatedEvent")

                    // read the newly created app launcher file based configuration
                    val appLauncherConfiguration = readAppLauncherConfigurationFromFile()

                    // update our observables
                    appLauncherFloatApps.postValue(appLauncherConfiguration.floatApps)
                    appLauncherApps.postValue(appLauncherConfiguration.apps)
                }

                is AppLauncherEvents.AppLauncherFileConfigurationModifiedEvent -> {
                    Logger.i("Received AppLauncherEvents.AppLauncherFileConfigurationModifiedEvent")

                    // read the modified app launcher file based configuration
                    val appLauncherConfiguration = readAppLauncherConfigurationFromFile()

                    // update our observables
                    appLauncherFloatApps.postValue(appLauncherConfiguration.floatApps)
                    appLauncherApps.postValue(appLauncherConfiguration.apps)
                }

                is AppLauncherEvents.AppLauncherFileConfigurationDeletedEvent -> {
                    Logger.i("Received AppLauncherEvents.AppLauncherFileConfigurationDeletedEvent")

                    // clear out our observables
                    appLauncherFloatApps.postValue(false)
                    appLauncherApps.postValue(emptyList())
                }
            }
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }
    //endregion event handlers


    //region private methods

    /**
     * Reads the app launcher configuration from the file system
     *
     * @return Instance of AppLauncherConfiguration
     */
    private fun readAppLauncherConfigurationFromFile(): AppLauncherConfiguration {
        // log entry
        Logger.i("Entered readAppLauncherConfigurationFromFile()")

        // return value
        var appLauncherConfiguration = AppLauncherConfiguration(emptyList(), false)

        try {
            // read the configuration file into a string
            val appLauncherFileConfiguration = File(
                AppLauncherDemo.context.getExternalFilesDir(null)
                    ?.absolutePath, APP_LAUNCHER_CONFIGURATION_FILE
            ).readText()

            // deserialize the string into an object
            if (!TextUtils.isEmpty(appLauncherFileConfiguration)) {
                appLauncherConfiguration = Gson().fromJson<AppLauncherConfiguration>(
                    appLauncherFileConfiguration, AppLauncherConfiguration::class.java
                )
            }
        } catch (ex: Exception) {
            Logger.w(ex)
        } finally {
            // log exit
            Logger.i("Exiting readAppLauncherConfigurationFromFile()")
        }

        // return to caller
        return appLauncherConfiguration
    }
    //endregion private methods
}