/*
 * Copyright. Baby Bill's Software Factory.
 */
package com.babybillssoftwarefactory.applauncherdemo.viewmodel


//region import directives

import android.app.ActivityOptions
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.babybillssoftwarefactory.applauncherdemo.R
import com.babybillssoftwarefactory.applauncherdemo.event.interfaces.AppLauncherCallback
import com.babybillssoftwarefactory.applauncherdemo.model.AppLauncherModel
import com.babybillssoftwarefactory.applauncherdemo.util.logging.Logger
import com.babybillssoftwarefactory.applauncherdemo.AppLauncherDemo.Companion.context as AppContext

//endregion import directives


/**
 * Encapsulates the view model that drives the presentation of data within the App Launcher dialog
 *
 * @author Thomas Sunderland
 */
class AppLauncherViewModel : ViewModel(), AppLauncherCallback {

    //region data members

    /**
     * Reference to the underlying data model
     */
    private val dataModel = AppLauncherModel()

    /**
     * Collection of apps that can be launched (originating from Stack device configuration)
     */
    val appLauncherApps = MediatorLiveData<List<App.AppInfo>>().apply {
        addSource(dataModel.appLauncherApps) { value = extractAppLaunchInfo(it) }
    }

    /**
     * Label to show on the App Launcher dialog indicating how many apps + app links are configured
     */
    val appLauncherLabel = MediatorLiveData<String>().apply {
        addSource(dataModel.appLauncherApps) {
            value = AppContext.getString(R.string.dialog_app_launcher_apps_available, it?.size ?: 0)
        }
    }
    //endregion data members


    //region initialization blocks

    init {
        // register data model for event bus events
        dataModel.register()
    }
    //endregion initialization blocks


    //region ViewModel overrides

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        // log entry
        Logger.i("Entered onCleared()")

        try {
            // unregister data model from event bus events
            dataModel.unregister()

            // call into base class implementation
            super.onCleared()
        } catch (ex: Exception) {
            Logger.w(ex)
        } finally {
            // log exit
            Logger.i("Exiting onCleared()")
        }
    }
    //endregion ViewModel overrides


    //region inner classes

    /**
     * Abstract App class that all App types capable of being launched extend from
     */
    sealed class App {

        //region data members

        /**
         * Presentable label of the app
         */
        abstract val label: String
        //endregion data members


        //region App extension classes

        /**
         * Encapsulates the data required for each app that can be launched so
         * that we can present the app launcher and be able to launch the app
         */
        data class AppInfo(val packageName: String, override val label: String,
                           val icon: Drawable, val launchIntent: Intent?) : App()
        //endregion App extension classes
    }
    //endregion inner classes


    //region AppLauncherCallback interface implementation

    /**
     * Handler for when the user tries to launch one of the apps
     */
    override fun onAppClicked(appInfo: App.AppInfo) {
        // log entry
        Logger.i("Entered onAppClicked() click event handler")

        try {
            // if we're running on Android Nougat or above then optionally float any launched apps on top of this app
            val activityOptions =
                if (dataModel.appLauncherFloatApps.value == true && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ActivityOptions.makeBasic().apply {
                        launchBounds = Rect(0, 0, 480, 320) // could make this smarter
                    }
                } else null

            Logger.i("User launched the ${appInfo.label} app (package: ${appInfo.packageName})")
            AppContext.startActivity(appInfo.launchIntent?.apply {
                addFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_MULTIPLE_TASK)
            }, activityOptions?.toBundle())
        } catch (ex: Exception) {
            Logger.w(ex)
        } finally {
            // log exit
            Logger.i("Exiting onAppClicked() click event handler")
        }
    }
    //endregion AppLauncherCallback interface implementation


    //region private methods

    /**
     * Examines each package in the collection and pulls the launching information out for each
     * one (also de-duplicates the passed in list and sorts it by app name)
     */
    private fun extractAppLaunchInfo(appPackages: List<String>?): List<App.AppInfo> {
        // return value
        val apps = mutableListOf<App.AppInfo>()

        try {
            appPackages?.apply {
                distinctBy { it }.forEach { appPackage -> // de-duplicate
                    try {
                        val packageManager = AppContext.packageManager

                        // extract the app meta data
                        val appInfo = packageManager.getApplicationInfo(appPackage, PackageManager.GET_META_DATA)

                        // extract the app name
                        val appLabel = packageManager.getApplicationLabel(appInfo)

                        // extract the app icon
                        val appIcon = packageManager.getApplicationIcon(appInfo)

                        // extract the app launch intent
                        val appLaunchIntent = packageManager.getLaunchIntentForPackage(appPackage)
                        appLaunchIntent?.addCategory(CATEGORY_LAUNCHER)

                        // add the application info to our collection
                        apps.add(App.AppInfo(appPackage, appLabel.toString(), appIcon, appLaunchIntent))
                    } catch (nameNotFoundException: PackageManager.NameNotFoundException) {
                        Logger.i("Package '$appPackage' not found on device, skipping")
                    }
                }

                // now let's sort the apps by app label
                apps.sortBy { it.label.toLowerCase() }
            }
        } catch (ex: Exception) {
            Logger.w(ex)
        }

        // return to caller
        return apps
    }
    //endregion private methods
}