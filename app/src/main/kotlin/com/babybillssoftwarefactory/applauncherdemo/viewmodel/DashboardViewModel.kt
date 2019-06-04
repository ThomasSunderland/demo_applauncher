/*
 * Copyright. Baby Bill's Software Factory.
 */
package com.babybillssoftwarefactory.applauncherdemo.viewmodel


//region import directives

import com.babybillssoftwarefactory.applauncherdemo.util.logging.Logger
import com.babybillssoftwarefactory.applauncherdemo.view.fragments.DashboardFragmentDirections

//endregion import directives


/**
 * ViewModel for the Dashboard screen
 *
 * @author Thomas Sunderland
 */
class DashboardViewModel : BaseViewModel() {

    //region action handlers

    /**
     * Handler for when to show the App Launcher dialog
     */
    fun handleActionShowAppLauncher() {
        // log entry
        Logger.i("Entered handleActionShowAppLauncher()")

        try {
            navigate(DashboardFragmentDirections.actionDashboardToAppLauncher())
        } catch (ex: Exception) {
            Logger.w(ex)
        } finally {
            // log exit
            Logger.i("Exiting handleActionShowAppLauncher()")
        }
    }
    //endregion action handlers
}
