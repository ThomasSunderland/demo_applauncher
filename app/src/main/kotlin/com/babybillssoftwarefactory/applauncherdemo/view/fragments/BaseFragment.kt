/*
 * Copyright. Baby Bill's Software Factory.
 */
package com.babybillssoftwarefactory.applauncherdemo.view.fragments


//region import directives

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.babybillssoftwarefactory.applauncherdemo.util.logging.Logger
import com.babybillssoftwarefactory.applauncherdemo.navigation.NavigationCommand
import com.babybillssoftwarefactory.applauncherdemo.viewmodel.BaseViewModel

//endregion import directives


/**
 * BaseFragment that all concrete Fragments extend from
 *
 * @author Thomas Sunderland
 */
abstract class BaseFragment : Fragment() {

    //region data members

    /**
     * Reference to our view model
     */
    protected lateinit var viewModel: BaseViewModel
    //endregion data members


    //region overrides

    /**
     * Called when the fragment's activity has been created and this fragment's view hierarchy instantiated. It can be
     * used to do final initialization once these pieces are in place, such as retrieving views or restoring state.
     *
     * It is also useful for fragments that use {@link #setRetainInstance(boolean)} to retain their instance, as this
     * callback tells the fragment when it is fully associated with the new activity instance. This is called after
     * {@link #onCreateView} and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // log entry
        Logger.i("Entered onActivityCreated(${activity ?: "Unknown"})")

        try {
            // call base class implementation
            super.onActivityCreated(savedInstanceState)

            // observe navigation commands
            viewModel.navigationCommand.observe(this, Observer { navigationCommand ->
                when (navigationCommand) {
                    is NavigationCommand.To -> {
                        findNavController().navigate(navigationCommand.directions)
                    }
                    is NavigationCommand.Up -> {
                        findNavController().navigateUp()
                    }
                }
            })
        } catch (ex: Exception) {
            Logger.w(ex)
        } finally {
            // log exit
            Logger.i("Exiting onActivityCreated(...)")
        }
    }
    //endregion overrides
}
