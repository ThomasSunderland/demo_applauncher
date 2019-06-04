/*
 * Copyright. Baby Bill's Software Factory.
 */
package com.babybillssoftwarefactory.applauncherdemo.view.fragments


//region import directives

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.babybillssoftwarefactory.applauncherdemo.databinding.FragmentDashboardBinding
import com.babybillssoftwarefactory.applauncherdemo.viewmodel.DashboardViewModel

//endregion import directives


/**
 * Encapsulates our Dashboard Fragment
 *
 * @author Thomas Sunderland
 */
class DashboardFragment : BaseFragment() {

    //region lifecycle overrides

    /**
     * Called to have the fragment instantiate its user interface view. This is optional, and non-graphical fragments can return
     * null (which is the default implementation). This will be called between onCreate(Bundle) and onActivityCreated(Bundle).
     *
     * If you return a View from here, you will later be called in onDestroyView() when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not
     * add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     *
     * @return    Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // inflate the view
        val binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // set the view model
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        binding.viewmodel = viewModel as DashboardViewModel

        // set the fragment as the lifecycle owner of the view model
        binding.lifecycleOwner = this

        // return to caller
        return binding.root
    }
    //endregion lifecycle overrides
}
