package com.josedlpozo.galileo.flow

import android.app.Application
import android.content.Context
import android.view.View
import arrow.core.fix
import arrow.core.getOrElse
import com.josedlpozo.galileo.flow.model.Created
import com.josedlpozo.galileo.core.GalileoItem
import com.josedlpozo.galileo.flow.FlowEventTry.flowLifeCycleCallback
import java.text.SimpleDateFormat
import java.util.*

class FlowGalileoItem(private val context: Context) : GalileoItem {

    init {
        (context.applicationContext as Application).unregisterActivityLifecycleCallbacks(
            flowLifeCycleCallback
        )
        (context.applicationContext as Application).registerActivityLifecycleCallbacks(
            flowLifeCycleCallback
        )
    }

    private val view: View by lazy { FlowView(context) }

    override fun view(): View = view

    override val name: String = "Flow"

    override val icon: Int = R.drawable.ic_activity

    override fun snapshot(): String = FlowEventTry.useCase.get().fix().map {
        val formatter = SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault())
        it.joinToString("\n", transform = {
            "${formatter.format(it.created)} ${it.name.toUpperCase()} ${it.activityName}" + when (it) {
                is Created -> "\n" + it.extras.joinToString(
                    "\n",
                    transform = { "${it.key} --> ${it.value}" }) + "\n"
                else -> ""
            } + "\n"
        })
    }.getOrElse { "" }
}