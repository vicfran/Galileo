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
package com.josedlpozo.galileo.flow.adapter

import androidx.core.content.ContextCompat
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.josedlpozo.galileo.flow.R
import com.josedlpozo.galileo.flow.model.Created
import com.josedlpozo.galileo.flow.model.FlowEvent
import com.josedlpozo.galileo.flow.model.toModel
import com.josedlpozo.galileo.flow.padding
import com.josedlpozo.galileo.flow.tint

internal class FlowEventAdapter(private val flowEvents: List<FlowEvent>,
                                private val onClick: (FlowEvent) -> Unit) : RecyclerView.Adapter<FlowEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowEventViewHolder =
            FlowEventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false), onClick)

    override fun getItemCount(): Int = flowEvents.size

    override fun onBindViewHolder(holder: FlowEventViewHolder, position: Int) {
        holder.bind(flowEvents[position])
    }
}

internal class FlowEventViewHolder(view: View, private val onClick: (FlowEvent) -> Unit) : RecyclerView.ViewHolder(view) {

    private val textTitle: TextView by lazy { view.findViewById<TextView>(R.id.textTitle) }
    private val textSubtitle: TextView by lazy { view.findViewById<TextView>(R.id.textSubtitle) }
    private val textCaption: TextView by lazy { view.findViewById<TextView>(R.id.textCaption) }

    fun bind(flowEvent: FlowEvent) = with(flowEvent.toModel()) {
        textTitle.text = name
        textSubtitle.text = activityName
        textCaption.visibility = View.VISIBLE

        val tintedDrawable = AppCompatResources.getDrawable(itemView.context, background)?.tint(ContextCompat.getColor(itemView.context, color))
        textTitle.setBackgroundDrawable(tintedDrawable)

        textTitle.padding(R.dimen.galileo_margin_small)

        when(flowEvent) {
            is Created -> if (flowEvent.extras.isEmpty()) textCaption.visibility = View.GONE else textCaption.text = "${flowEvent.extras.size} items"
            else -> textCaption.visibility = View.GONE
        }
        itemView.setOnClickListener {
            if (flowEvent is Created && flowEvent.extras.isNotEmpty()) onClick(flowEvent)
        }
    }
}