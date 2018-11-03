package com.josedlpozo.galileo.preferator.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.ScrollView
import com.josedlpozo.galileo.R
import com.josedlpozo.galileo.items.GalileoItem
import com.josedlpozo.galileo.preferator.data.PreferatorDataSource
import com.josedlpozo.galileo.preferator.model.PreferatorConfig
import com.josedlpozo.galileo.preferator.model.Preferences
import com.josedlpozo.galileo.preferator.presenter.PreferatorPresenter

class PreferatorView @JvmOverloads internal constructor(context: Context, val attr: AttributeSet? = null, defStyleAttr: Int = 0)
    : ScrollView(context, attr, defStyleAttr), PreferatorPresenter.View, GalileoItem {

    override val name: String = "Preferator"

    override val view: View = this

    override val icon: Int = R.drawable.ic_developer_board

    override fun snapshot(): String = presenter.snapshot()

    var config: PreferatorConfig = PreferatorConfig()
        set(value) {
            presenter.config = value
        }

    private val presenter: PreferatorPresenter by lazy { PreferatorPresenter(this, PreferatorDataSource(context)) }

    private val linearLayout = LinearLayout(context).apply {
        orientation = VERTICAL
    }

    init {
        addView(linearLayout)
    }

    override fun render(preferences: Preferences) {
        linearLayout.removeAllViews()
        preferences.items.map {
            val detailView = PreferatorDetailView(context)
            detailView.render(it)
            linearLayout.addView(detailView)
        }
    }
}