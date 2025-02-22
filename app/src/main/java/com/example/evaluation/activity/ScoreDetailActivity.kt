package com.example.evaluation.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.evaluation.ScoreDetail.EvaluationDetailFragment
import com.example.evaluation.ScoreDetail.ExamDetailFragment
import com.example.evaluation.adapter.ViewpagerAdapter
import com.example.evaluation.adapter.ViewpagerAdapter2
import com.example.evaluation.databinding.ActivityScoreDetailBinding
import com.example.evaluation.ui.Ranking.EvaluationFragment
import com.example.evaluation.ui.Ranking.ExamFragment
import com.google.android.material.tabs.TabLayoutMediator

class ScoreDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScoreDetailBinding
    private lateinit var adapter: ViewpagerAdapter2
    private val fragments=ArrayList<Fragment>()
    private val tabs=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreDetailBinding.inflate(layoutInflater)
        fragments.add(EvaluationDetailFragment())
        fragments.add(ExamDetailFragment())
        tabs.add("全员测评")
        tabs.add("业绩考核")
        adapter= ViewpagerAdapter2(fragments,this)
        binding.viewpager3.adapter=adapter
        setContentView(binding.root)
        setAndroidNativeLightStatusBar(this, true)
        TabLayoutMediator(binding.tabLayout1, binding.viewpager3.viewPager2) { tab, position ->
            tab.text =tabs[position]
        }.attach()
    }
    private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        } else {
            decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        }
    }
}