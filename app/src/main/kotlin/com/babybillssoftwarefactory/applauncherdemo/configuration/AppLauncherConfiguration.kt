/*
 * Copyright. Baby Bill's Software Factory.
 */
package com.babybillssoftwarefactory.applauncherdemo.configuration


//region import directives

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//endregion import directives


/**
 * Encapsulates an example App Launcher configuration
 *
 * @author Thomas Sunderland
 */
data class AppLauncherConfiguration(
    @Expose @SerializedName("Apps") val apps: List<String>,
    @Expose @SerializedName("FloatApps") val floatApps: Boolean
)
