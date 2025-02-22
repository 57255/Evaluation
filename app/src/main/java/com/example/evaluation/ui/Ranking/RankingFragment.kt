package com.example.evaluation.ui.Ranking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.evaluation.adapter.ViewpagerAdapter
import com.example.evaluation.databinding.FragmentRankingBinding
import com.google.android.material.tabs.TabLayoutMediator


class RankingFragment : Fragment() {
    private var _binding: FragmentRankingBinding? = null

    private val binding get() = _binding!!
    private lateinit var adapter:ViewpagerAdapter
    private val fragments=ArrayList<Fragment>()
    private val tabs=ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        fragments.add(EvaluationFragment())
        fragments.add(ExamFragment())
        tabs.add("全员测评")
        tabs.add("业绩考核")
        adapter= ViewpagerAdapter(fragments,this)
        binding.viewpager2.adapter=adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TabLayoutMediator(binding.tabLayout, binding.viewpager2.viewPager2) { tab, position ->
            tab.text =tabs[position]
        }.attach()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}