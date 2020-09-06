package com.theapache64.nemo.feature.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.theapache64.twinkill.ui.activities.base.BaseAppCompatActivity
import com.theapache64.twinkill.utils.extensions.snackBar
import com.theapache64.twinkill.utils.extensions.toast

/**
 * Created by theapache64 : Jul 26 Sun,2020 @ 21:57
 */
abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes
    private val layoutId: Int
) : BaseAppCompatActivity() {

    companion object {
        private const val KEY_IS_DEBUG_ACTIVITY = "is_debug_activity"
    }

    protected lateinit var binding: B
    abstract val viewModel: VM
    abstract fun onCreate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = DataBindingUtil.setContentView<B>(
            this,
            layoutId
        )

        // Watching for snackbar message
        viewModel.snackBarMsg.observe(this, Observer {
            when (it) {
                is String -> {
                    binding.root.snackBar(it)
                }
                is Int -> {
                    binding.root.snackBar(it)
                }
                else -> throw IllegalArgumentException("snackBarMsg should be either Int or String")
            }
        })

        // Watching for toast message
        viewModel.toastMsg.observe(this, Observer {
            when (it) {
                is String -> {
                    toast(it)
                }
                is Int -> {
                    toast(it)
                }
                else -> throw IllegalArgumentException("toastMsg should be either Int or String")
            }
        })

        onCreate()
    }

    fun isDebugActivity(): Boolean {
        return intent.getBooleanExtra(KEY_IS_DEBUG_ACTIVITY, false)
    }
}