/*
 * Copyright. Baby Bill's Software Factory.
 */
package com.babybillssoftwarefactory.applauncherdemo.view.activities


//region import directives

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.babybillssoftwarefactory.applauncherdemo.R
import com.babybillssoftwarefactory.applauncherdemo.util.logging.Logger

//endregion import directives


/**
 * Main Activity for the Demo App (Launching Activity)
 *
 * @author Thomas Sunderland
 */
class MainActivity : AppCompatActivity() {

    //region lifecycle overrides

    /**
     * Called when the activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            // call base class implementation
            super.onCreate(savedInstanceState)

            // inflate layout
            setContentView(R.layout.activity_main)
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }
    //endregion lifecycle overrides
}
