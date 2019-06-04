/*
 * Copyright. Baby Bill's Software Factory.
 */
package com.babybillssoftwarefactory.applauncherdemo.event.interfaces


//region import directives

import com.babybillssoftwarefactory.applauncherdemo.viewmodel.AppLauncherViewModel

//endregion import directives


/**
 * Callback interface for the individual App cards in the AppLauncher dialog
 *
 * @author Thomas Sunderland
 */
interface AppLauncherCallback {

    /**
     * Callback for when the user selects to launch an App
     *
     * @param appInfo AppInfo object for the App card that was selected
     */
    fun onAppClicked(appInfo: AppLauncherViewModel.App.AppInfo)
}