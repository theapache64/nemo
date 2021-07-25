package com.theapache64.nemo.feature.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.theapache64.nemo.data.local.table.addresses.AddressEntity
import com.theapache64.nemo.data.repository.AddressRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by theapache64 : Oct 07 Wed,2020 @ 20:50
 */
@HiltViewModel
class AddressListViewModel @Inject constructor(
    private val addressRepo: AddressRepo
) : BaseViewModel(), AddressesAdapter.Callback {
    private val _addressList = MutableLiveData<List<AddressEntity>>()
    val addressList: LiveData<List<AddressEntity>> = _addressList

    init {
        viewModelScope.launch {
            _addressList.value = addressRepo.getAddresses()
        }
    }

    override fun onAddressClicked(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onEditAddressClicked(position: Int) {
        TODO("Not yet implemented")
    }
}