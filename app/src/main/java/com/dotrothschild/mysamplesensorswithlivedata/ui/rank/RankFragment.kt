package com.dotrothschild.mysamplesensorswithlivedata.ui.rank

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotrothschild.mysamplesensorswithlivedata.databinding.FragmentRankBinding
import com.dotrothschild.mysamplesensorswithlivedata.flattenToList
import com.dotrothschild.mysamplesensorswithlivedata.model.Rank
import com.dotrothschild.mysamplesensorswithlivedata.ui.AppViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class RankFragment : Fragment() {
    private var _binding: FragmentRankBinding? = null
    private val binding get() = _binding!!
    private val appViewModel: AppViewModel by activityViewModels()


    private lateinit var viewModel: RankViewModel
    private lateinit var adapter: RankAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[RankViewModel::class.java]
        _binding = FragmentRankBinding.inflate(inflater, container, false)

        val scope = MainScope()
        scope.launch {
            setupView(appViewModel.rankRepository.ranks.flattenToList())
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