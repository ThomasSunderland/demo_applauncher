/*
 * Copyright. Baby Bill's Software Factory.
 */
package com.babybillssoftwarefactory.applauncherdemo.viewmodel


//region import directives

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.babybillssoftwarefactory.applauncherdemo.navigation.NavigationCommand
import com.babybillssoftwarefactory.applauncherdemo.util.SingleLiveEvent

//endregion import directives


/**
 * BaseViewModel from which all concrete ViewModel classes extend
 *
 * @author Thomas Sunderland
 */
abstract class BaseViewModel : ViewModel() {

    //region data members

    /**
     * Navigation command - SingleLiveEvent
     */
    private val _navigationCommand: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()

    /**
     * Observable navigation data
     */
    val navigationCommand: LiveData<NavigationCommand>
        get() = _navigationCommand
    //endregion data members


    //region methods

    /**
     * Helper method for navigating
     */
    fun navigate(navDirections: NavDirections) {
        _navigationCommand.postValue(NavigationCommand.To(navDirections))
    }

    /**
     * Helper method for navigating up
     */
    fun navigateUp() {
        _navigationCommand.postValue(NavigationCommand.Up)
    }
    //endregion methods
}