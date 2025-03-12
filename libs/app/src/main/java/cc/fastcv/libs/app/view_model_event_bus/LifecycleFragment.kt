package cc.fastcv.libs.app.view_model_event_bus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.viewModels

class LifecycleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel: LifecycleViewModel =
            ViewModelProvider(this).get(LifecycleViewModel::class.java)

        val viewModel1: LifecycleViewModel by activityViewModels()

        val viewModel2: LifecycleViewModel by activityViewModels()

//        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                return LifecycleViewModel() as T
//            }
//
//        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onDetach() {
        super.onDetach()
    }
}