package com.dotrothschild.mysamplesensorswithlivedata.ui.other

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.dotrothschild.mysamplesensorswithlivedata.GetBottomMenuFragment
import com.dotrothschild.mysamplesensorswithlivedata.databinding.FragmentOtherBinding
import com.dotrothschild.mysamplesensorswithlivedata.flattenToList
import com.dotrothschild.mysamplesensorswithlivedata.model.Rank
import com.dotrothschild.mysamplesensorswithlivedata.ui.AppViewModel
import com.dotrothschild.mysamplesensorswithlivedata.ui.rank.RankAdapter
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class OtherFragment : Fragment() {
    private var _binding: FragmentOtherBinding? = null
    private val binding get() = _binding!!
    private val appViewModel: AppViewModel by activityViewModels()


    private lateinit var viewModel: OtherViewModel
    private lateinit var adapter: RankAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[OtherViewModel::class.java]
        _binding = FragmentOtherBinding.inflate(inflater, container, false)

        val scope = MainScope()
        scope.launch {
            setupView(appViewModel.otherRepository.others.flattenToList())
        }
        return binding.root
    }

    private fun setupView(flatList: List<Rank>) {

        binding.apply {
            // topLayout.setBackgroundResource(R.drawable.cardview_bg)
            adapter = RankAdapter(
                requireContext(),
                flatList
            )
            { rank, _ ->
                appViewModel.rank = rank
            }
            recyclerView.adapter = adapter
            binding.apply {
                recyclerView.visibility = View.VISIBLE
                adapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(0)
            }
        } // end binding apply
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}