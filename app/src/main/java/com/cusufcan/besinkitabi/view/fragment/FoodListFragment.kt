package com.cusufcan.besinkitabi.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cusufcan.besinkitabi.adapter.FoodAdapter
import com.cusufcan.besinkitabi.databinding.FragmentFoodListBinding
import com.cusufcan.besinkitabi.viewmodel.FoodListViewModel

class FoodListFragment : Fragment() {
    private var _binding: FragmentFoodListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FoodListViewModel

    private val foodAdapter = FoodAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FoodListViewModel::class.java]
        viewModel.refreshData()

        binding.foodRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.foodRecyclerView.setHasFixedSize(true)
        binding.foodRecyclerView.adapter = foodAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.foodRecyclerView.visibility = View.GONE
            binding.foodErrorMessage.visibility = View.GONE
            binding.foodLoading.visibility = View.VISIBLE
            viewModel.refreshDataFromInternet()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.foods.observe(viewLifecycleOwner) {
            binding.foodRecyclerView.visibility = View.VISIBLE
            foodAdapter.updateFoodList(it)
        }

        viewModel.foodErrorMessage.observe(viewLifecycleOwner) {
            if (it) {
                binding.foodErrorMessage.visibility = View.VISIBLE
                binding.foodRecyclerView.visibility = View.GONE
            } else {
                binding.foodErrorMessage.visibility = View.GONE
            }
        }

        viewModel.foodLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.foodErrorMessage.visibility = View.GONE
                binding.foodRecyclerView.visibility = View.GONE
                binding.foodLoading.visibility = View.VISIBLE
            } else {
                binding.foodLoading.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}