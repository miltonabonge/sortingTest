package com.naveen.sampleapp.ui.grouped

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.naveen.sampleapp.R
import com.naveen.sampleapp.model.GroupedItem


class GroupedActivity : AppCompatActivity() {

    private lateinit var viewModel: GroupedViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private val itemsListMain = mutableListOf<GroupedItem>()
    private lateinit var itemAdapter: GroupedItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grouped)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.itemRecyclerView)
        itemAdapter = GroupedItemAdapter(itemsListMain)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
            adapter = itemAdapter
        }
        viewModel = ViewModelProvider(this).get(GroupedViewModel::class.java)
        addObserver()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addObserver() {
        viewModel.isLoading.observe(this, {
            if (it) {
                progressBar.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        })

        viewModel.itemsListLiveData.observe(this, {
            it?.let {
                itemsListMain.clear()
                itemsListMain.addAll(it)
                itemAdapter.notifyDataSetChanged()
            }
        })

        viewModel.isError.observe(this, {
            it?.let {
                if (it.isNotEmpty()) {
                    val alertDialog: AlertDialog = this.let { activity ->
                        val builder = AlertDialog.Builder(activity)
                        builder.setMessage(it)
                        builder.apply {
                            setPositiveButton(
                                "Cancel"
                            ) { dialog, _ ->
                                // User clicked OK button
                                dialog.dismiss()
                            }
                        }
                        builder.create()
                    }
                    alertDialog.show()
                }
            }
        })
    }
}