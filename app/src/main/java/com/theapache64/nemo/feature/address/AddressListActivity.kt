package com.theapache64.nemo.feature.address

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.theapache64.nemo.R
import com.theapache64.nemo.databinding.ActivityAddressListBinding
import com.theapache64.nemo.feature.base.BaseActivity

class AddressListActivity :
    BaseActivity<ActivityAddressListBinding, AddressListViewModel>(R.layout.activity_address_list) {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, AddressListActivity::class.java).apply {
                // data goes here
            }
        }
    }

    override val viewModel: AddressListViewModel by viewModels()

    override fun onCreate() {
    }

}