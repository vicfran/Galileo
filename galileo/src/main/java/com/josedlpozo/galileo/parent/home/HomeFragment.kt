/*
 * Copyright (C) 2018 josedlpozo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.josedlpozo.galileo.parent.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.core.GalileoItem
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

internal class HomeFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    var items: List<GalileoItem> = listOf()

    private lateinit var viewModel: HomeViewModel
    private lateinit var bottomBar: BottomNavigationView
    private lateinit var container: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.galileo_home_fragment, container, false)
        bottomBar = view.findViewById(R.id.navigation)
        this.container = view.findViewById(R.id.container)
        setHasOptionsMenu(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        viewModel.start(items)

        viewModel.items.observe(this, Observer { items ->
            if (items == null || items.isEmpty()) return@Observer

            bottomBar.menu.clear()
            items.map {
                bottomBar.menu.add(it.name).setIcon(it.icon)
            }

            container.removeAllViews()
            container.addView(items.first().view())

            bottomBar.setOnNavigationItemSelectedListener { item ->
                container.removeAllViews()
                items.find { it.name == item.title }?.let {
                    container.addView(it.view())
                }
                true
            }
        })

        viewModel.shareText.observe(this, Observer {
            if (it == null) return@Observer

            share(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.share -> {
            context?.let {
                val format = SimpleDateFormat("dd_MM_yyyy_HH:mm:ss", Locale.getDefault())
                val formattedDate = format.format(Date())
                viewModel.share(File(it.filesDir, "Galileo_$formattedDate.txt"))
            }
            true
        }
        else -> false
    }

    private fun share(file: File) {
        context?.let {
            val path = FileProvider.getUriForFile(it, "${it.applicationInfo.packageName}.com.josedlpozo.galileo.provider", file)
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "message/rfc822"
            emailIntent.putExtra(Intent.EXTRA_STREAM, path)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Galileo Report ${Date()}")
            startActivity(Intent.createChooser(emailIntent, "Send report"))
        }
    }
}
