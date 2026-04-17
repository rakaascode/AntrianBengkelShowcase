package dev.inteiintel.teduhserviceapp.presentation.components

import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.model.OnBoarding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HorizontalPagerViewModel: ViewModel(){
    val _getDataOnBoarding = MutableStateFlow<List<OnBoarding>>(emptyList())
    val getDataOnBoarding : StateFlow<List<OnBoarding>> =_getDataOnBoarding.asStateFlow()


    init {
        loadData()
    }


    fun loadData(){
        _getDataOnBoarding.value = listOf(
            OnBoarding(R.string.title_ob_satu,R.string.title_ob_satu_1, R.string.sub_title_ob_satu,R.drawable.img_ob_satu),
            OnBoarding(R.string.title_ob_dua, R.string.title_ob_dua_1, R.string.sub_title_ob_dua,R.drawable.img_ob_dua),
            OnBoarding(R.string.title_ob_tiga, R.string.title_ob_tiga_1,R.string.sub_title_ob_tiga,R.drawable.img_ob_tiga),
        )
    }


}