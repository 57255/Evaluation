package com.example.evaluation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.evaluation.activity.ScoreDetailActivity
import com.example.evaluation.ui.Ranking.EvaluationFragment

class ViewpagerAdapter(private val fragments:ArrayList<Fragment>, fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments.get(position)
    }
}