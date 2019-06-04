/*
 * Copyright. Baby Bill's Software Factory.
 */
package com.babybillssoftwarefactory.applauncherdemo.util.logging


//region import directives

import android.util.Log

import androidx.annotation.CheckResult

//endregion import directives


/**
 * Logging helper class
 *
 * @author Thomas Sunderland
 */
object Logger {

    //region constants

    /**
     * Used as a fallback during logging
     */
    private val TAG = Logger::class.java.simpleName
    //endregion constants


    //region private methods

    /**
     * Retrieves the caller class name to use as the Logcat tag during logging operations
     *
     * @return Caller class name
     */
    private val callerClassName: String
        @CheckResult
        get() {
            var callerClassName = Logger::class.java.simpleName

            try {
                val stackTraceElements = Thread.currentThread().stackTrace
                for (i in 1 until stackTraceElements.size) {
                    val stackTraceElement = stackTraceElements[i]
                    if (stackTraceElement.className != Logger::class.java.name && stackTraceElement.className.indexOf("java.lang.Thread") != 0) {
                        callerClassName = stackTraceElement.className.substring(
                                stackTraceElement.className.lastIndexOf('.') + 1)
                        break
                    }
                }
            } catch (ex: Exception) {
                Log.w(TAG, ex)
            }

            return callerClassName
        }
    //endregion private methods


    //region public methods

    /**
     * Logs a debug message to logcat
     *
     * @param message Message to write to the log
     */
    fun d(message: String) {
        try {
            Log.d(callerClassName, message)
        } catch (ex: Exception) {
            Log.w(TAG, ex)
        }

    }

    /**
     * Logs an informational message to logcat
     *
     * @param message Message to write to the log
     */
    fun i(message: String) {
        try {
            Log.i(callerClassName, message)
        } catch (ex: Exception) {
            Log.w(TAG, ex)
        }

    }

    /**
     * Logs a warning to logcat
     *
     * @param throwable Exception to write to the log
     */
    fun w(throwable: Throwable) {
        try {
            Log.w(callerClassName, throwable)
        } catch (ex: Exception) {
            Log.w(TAG, ex)
        }

    }
    //endregion public methods
}
