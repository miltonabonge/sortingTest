package com.naveen.sampleapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.naveen.sampleapp.ui.filter.FilterActivity
import com.naveen.sampleapp.ui.grouped.GroupedActivity
import com.naveen.sampleapp.ui.sort.SortByActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

    }

    fun onGroupedClicked(view: View) {
        startActivity(Intent(this, GroupedActivity::class.java))
    }

    fun onSortClicked(view: View) {
        startActivity(Intent(this, SortByActivity::class.java))
    }

    fun onFilterClick(view: View) {
        startActivity(Intent(this, FilterActivity::class.java))
    }
}