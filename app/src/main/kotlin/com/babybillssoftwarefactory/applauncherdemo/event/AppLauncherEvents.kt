/*
 * Copyright. Baby Bill's Software Factory.
 */
package com.babybillssoftwarefactory.applauncherdemo.event

/**
 * Encapsulates App Launcher related events
 *
 * @author Thomas Sunderland
 */
sealed class AppLauncherEvents {

    /**
     * Raised when the app launcher configuration file is created
     */
    object AppLauncherFileConfigurationCreatedEvent : AppLauncherEvents()

    /**
     * Raised when the app launcher configuration file is modified
     */
    object AppLauncherFileConfigurationModifiedEvent : AppLauncherEvents()

    /**
     * Raised when the app launcher configuration file is deleted
     */
    object AppLauncherFileConfigurationDeletedEvent : AppLauncherEvents()
}
