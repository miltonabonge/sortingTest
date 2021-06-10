package com.naveen.sampleapp.ui.sort

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.naveen.sampleapp.R
import com.naveen.sampleapp.model.Item

class SortByActivity : AppCompatActivity() {

    private lateinit var viewModel: SortByViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private val itemsListMain = mutableListOf<Item>()
    private lateinit var itemAdapter: SortItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sort_by)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.itemRecyclerView)
        itemAdapter = SortItemAdapter(itemsListMain)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            setHasFixedSize(true)
            adapter = itemAdapter
        }
        viewModel = ViewModelProvider(this).get(SortByViewModel::class.java)
        addObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_sortByListId -> {
                itemsListMain.sortBy { it.listId }
                itemAdapter.notifyDataSetChanged()
                recyclerView.smoothScrollToPosition(0)
                true
            }
            R.id.action_sortByName -> {
                itemsListMain.sortBy { it.name }
                itemAdapter.notifyDataSetChanged()
                recyclerView.smoothScrollToPosition(0)
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