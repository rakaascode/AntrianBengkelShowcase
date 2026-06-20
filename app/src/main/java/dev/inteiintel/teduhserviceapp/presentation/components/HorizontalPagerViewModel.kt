package dev.inteiintel.teduhserviceapp.presentation.components

import androidx.lifecycle.ViewModel
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.model.OnBoardingModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel untuk layar pager onboarding (komponen percobaan/development).
 *
 * Memuat data onboarding tiga halaman dan mengeksposnya sebagai [StateFlow].
 * Digunakan oleh [HorizontalPager] composable di `presentation/components`.
 *
 * @see dev.inteiintel.teduhserviceapp.presentation.components.HorizontalPager
 * @see dev.inteiintel.teduhserviceapp.data.model.OnBoardingModel
 */
class HorizontalPagerViewModel : ViewModel() {

    /** @suppress Dibiarkan internal karena ini adalah komponen percobaan. */
    val _getDataOnBoardingModel = MutableStateFlow<List<OnBoardingModel>>(emptyList())
    val getDataOnBoardingModel: StateFlow<List<OnBoardingModel>> = _getDataOnBoardingModel.asStateFlow()


    init {
        loadData()
    }


    fun loadData(){
        _getDataOnBoardingModel.value = listOf(
            OnBoardingModel(R.string.title_ob_satu,R.string.title_ob_satu_1, R.string.sub_title_ob_satu,R.drawable.img_ob_satu),
            OnBoardingModel(R.string.title_ob_dua, R.string.title_ob_dua_1, R.string.sub_title_ob_dua,R.drawable.img_ob_dua),
            OnBoardingModel(R.string.title_ob_tiga, R.string.title_ob_tiga_1,R.string.sub_title_ob_tiga,R.drawable.img_ob_tiga),
        )
    }


}