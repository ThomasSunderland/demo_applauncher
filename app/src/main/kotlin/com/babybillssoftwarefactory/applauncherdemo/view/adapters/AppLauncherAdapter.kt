/*
 * Copyright. Baby Bill's Software Factory.
 */
package com.babybillssoftwarefactory.applauncherdemo.view.adapters


//region import directives

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.babybillssoftwarefactory.applauncherdemo.R
import com.babybillssoftwarefactory.applauncherdemo.event.interfaces.AppLauncherCallback
import com.babybillssoftwarefactory.applauncherdemo.util.logging.Logger
import com.babybillssoftwarefactory.applauncherdemo.viewmodel.AppLauncherViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_app.view.*

//endregion import directives


/**
 * AppLauncher adapter for presenting apps that can be launched
 *
 * @author Thomas Sunderland
 */
class AppLauncherAdapter(var apps: List<AppLauncherViewModel.App>,
                         private val clickCallback: AppLauncherCallback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //region inner classes

    /**
     * AppLauncher ViewHolder
     */
    inner class AppLauncherViewHolder(val view: View,
                                      private val clickCallback: AppLauncherCallback) : RecyclerView.ViewHolder(view) {
        fun bind(app: AppLauncherViewModel.App) {
            try {
                // first set the icon to null (this fixes the issue where the wrong image sometimes displays due to recycling behavior)
                view.app_icon.setImageDrawable(null)

                when (app) {
                    is AppLauncherViewModel.App.AppInfo -> {
                        // app card
                        view.app_card.setOnClickListener { clickCallback.onAppClicked(app) }

                        // app icon
                        view.app_icon.setImageDrawable(app.icon)

                        // app text
                        view.app_text.text = app.label
                    }
                }

                // required for marquee effect on long strings
                view.app_text.isSelected = true
            } catch (ex: Exception) {
                Logger.w(ex)
            }
        }
    }
    //endregion inner classes


    //region overrides

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppLauncherViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.card_app, parent, false), clickCallback)
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link android.widget.ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     *
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        try {
            (viewHolder as AppLauncherViewHolder).bind(apps[position])
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount() = apps.size

    /**
     * Called when a view created by this adapter has been recycled.
     *
     * @param holder The ViewHolder for the view being recycled
     */
    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        try {
            // call base class implementation
            super.onViewRecycled(holder)

            // cancel any pending requests to load an image
            Picasso.get().cancelRequest((holder as AppLauncherViewHolder).view.app_icon)
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }
    //endregion overrides
}