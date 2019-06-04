/**
 * Copyright. Baby Bill's Software Factory.
 */
package com.babybillssoftwarefactory.applauncherdemo


//region import directives

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.babybillssoftwarefactory.applauncherdemo.util.logging.Logger

//endregion import directives


/**
 * Application class - main entry point of the App
 *
 * @author Thomas Sunderland
 */
class AppLauncherDemo : Application() {

    //region companion object

    /**
     * Static Application Context reference
     */
    companion object {

        /**
         * Reference to our application context
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
    //endregion companion object


    //region lifecycle overrides

    /**
     * Called when the application is starting, before any activity, service, or
     * receiver objects (excluding content providers) have been created.
     */
    override fun onCreate() {
        // log entry
        Logger.i("Entered onCreate()")

        try {
            // call into base class implementation
            super.onCreate()

            // set our context field
            context = applicationContext
        } catch (ex: Exception) {
            Logger.w(ex)
        } finally {
            // log exit
            Logger.i("Exiting onCreate()")
        }
    }
    //endregion lifecycle overrides
}