/*
 * Copyright. Baby Bill's Software Factory.
 */
package com.babybillssoftwarefactory.applauncherdemo.view.dialogs


//region import directives

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.babybillssoftwarefactory.applauncherdemo.R
import com.babybillssoftwarefactory.applauncherdemo.databinding.DialogAppLauncherBinding
import com.babybillssoftwarefactory.applauncherdemo.service.AppLauncherFileMonitor
import com.babybillssoftwarefactory.applauncherdemo.util.logging.Logger
import com.babybillssoftwarefactory.applauncherdemo.view.adapters.AppLauncherAdapter
import com.babybillssoftwarefactory.applauncherdemo.viewmodel.AppLauncherViewModel
import com.babybillssoftwarefactory.applauncherdemo.AppLauncherDemo.Companion.context as AppContext

//endregion import directives


/**
 * Encapsulates the app launcher dialog
 *
 * @author Thomas Sunderland
 */
class AppLauncherDialog : DialogFragment() {

    //region data members

    /**
     * Reference to the ViewModel for this View
     */
    private lateinit var viewModel: AppLauncherViewModel
    //endregion data members


    //region lifecycle overrides

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null. This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>The default implementation looks for an {@link ContentView} annotation, inflating
     * and returning that layout. If the annotation is not found or has an invalid layout resource
     * id, this method returns null.
     *
     * <p>It is recommended to <strong>only</strong> inflate the layout in this method and move
     * logic that operates on the returned View to {@link #onViewCreated(View, Bundle)}.
     *
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // request that no title be displayed
        // note: the issue of the title bar showing only appears to come into play on Lollipop and below
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        // inflate the view
        val binding = DialogAppLauncherBinding.inflate(inflater, container, false)

        // set the view model
        viewModel = ViewModelProviders.of(this).get(AppLauncherViewModel::class.java)
        binding.viewmodel = viewModel

        // configure recycler view
        configureRecyclerView(binding)

        // set the fragment as the lifecycle owner of the view model
        binding.lifecycleOwner = this

        // return to caller
        return binding.root
    }

    /**
     * Called when the fragment's activity has been created and this fragment's view hierarchy
     * instantiated. It can be used to do final initialization once these pieces are in place, such
     * as retrieving views or restoring state.
     *
     * This is called after {@link #onCreateView} and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        try {
            // call base class implementation
            super.onActivityCreated(savedInstanceState)

            // setup dialog animations
            // note: animates up from bottom of the screen to the center of screen on open and
            // animates down from the center of screen through the bottom of the screen on close
            dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimationUpDown

            // start the file monitoring service
            AppContext.startService(Intent(AppContext, AppLauncherFileMonitor::class.java))
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }
    //endregion lifecycle overrides


    //region DialogInterface.OnDismissListener interface implementation

    /**
     * This method will be invoked when the dialog is dismissed.
     *
     * @param dialog The dialog that was dismissed will be passed into the method
     */
    override fun onDismiss(dialog: DialogInterface) {
        try {
            // stop the file monitoring service
            AppLauncherFileMonitor.stopFileMonitoring()
            AppContext.stopService(Intent(AppContext, AppLauncherFileMonitor::class.java))

            // call base class implementation
            super.onDismiss(dialog)
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }
    //endregion DialogInterface.OnDismissListener interface implementation


    //region private methods

    /**
     * Configures our recycler view
     * @param binding Data binding reference
     */
    private fun configureRecyclerView(binding: DialogAppLauncherBinding) {
        try {
            binding.appContainer.apply {
                try {
                    // use a grid layout
                    layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.dialog_app_launcher_grid_columns))

                    // initialize our adapter
                    adapter = AppLauncherAdapter(listOf(), viewModel)

                    // start observing changes to the collection of app links that can be launched
                    viewModel.appLauncherApps.observe(this@AppLauncherDialog.viewLifecycleOwner,
                            Observer<List<AppLauncherViewModel.App>> {
                                try {
                                    // update number of columns based on number of apps (if 3 or less wide)
                                    layoutManager = if (it.size <= 3) GridLayoutManager(context, Math.max(1, it.size))
                                    else GridLayoutManager(context, resources.getInteger(R.integer.dialog_app_launcher_grid_columns))

                                    // update data set with current collection of apps
                                    (adapter as AppLauncherAdapter).apply {
                                        apps = it.sortedBy { app -> app.label.toLowerCase() }
                                        notifyDataSetChanged()
                                    }
                                } catch (ex: Exception) {
                                    Logger.w(ex)
                                }
                            })
                } catch (ex: Exception) {
                    Logger.w(ex)
                }
            }
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }
    //endregion private methods
}