package com.github.kornilovmikhail.mvpandroidproject.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.github.kornilovmikhail.mvpandroidproject.App
import com.github.kornilovmikhail.mvpandroidproject.data.entity.Event
import com.github.kornilovmikhail.mvpandroidproject.presenter.MainPresenter
import com.github.kornilovmikhail.mvpandroidproject.ui.detail.DetailsActivity
import com.github.kornilovmikhail.mvpandroidproject.ui.list.adapter.EventAdapter
import com.github.kornilovmikhail.mvpandroidproject.R
import com.github.kornilovmikhail.mvpandroidproject.di.event.component.DaggerEventComponent
import com.github.kornilovmikhail.mvpandroidproject.di.event.module.EventModule
import com.github.kornilovmikhail.mvpandroidproject.di.app.module.DataNetModule
import com.github.kornilovmikhail.mvpandroidproject.di.event.module.PresenterModule
import com.github.kornilovmikhail.mvpandroidproject.ui.list.MainView
import com.github.kornilovmikhail.mvpandroidproject.ui.list.OnScrollListener
import com.github.kornilovmikhail.mvpandroidproject.ui.list.dialog.PaginationDialog
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(), MainView {
    private var eventsAdapter: EventAdapter? = null
    @Inject
    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    @ProvidePresenter
    fun getPresenter(): MainPresenter = mainPresenter

    companion object {
        const val EXTRA_POSITION: String = "position"
        const val NAME_SHAREDPREFS: String = "PaginationPreferences"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerEventComponent.builder()
            .appComponent(App.getAppComponents())
            .eventModule(EventModule())
            .presenterModule(PresenterModule())
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
        mainPresenter.initSharedPreferences(getSharedPreferences(NAME_SHAREDPREFS, Context.MODE_PRIVATE))
        mainPresenter.getEvents(0)
    }

    private fun setupViews() {
        rv_events.layoutManager = LinearLayoutManager(this)
        rv_events.addOnScrollListener(OnScrollListener(
            rv_events.layoutManager as LinearLayoutManager
        ) {
            mainPresenter.getEvents(it)
        })
        setSupportActionBar(main_toolbar as Toolbar?)
    }

    override fun displayEvents(listEvents: List<Event>) {
        if (eventsAdapter == null) {
            eventsAdapter = EventAdapter(listEvents) {
                mainPresenter.eventClick(it)
            }
            rv_events.adapter = eventsAdapter
        }
        eventsAdapter?.submitList(listEvents)
    }

    override fun displaySuccess() {
        Toast.makeText(this, getString(R.string.server_events_success), Toast.LENGTH_SHORT).show()
    }

    override fun displayError() {
        Toast.makeText(this, getString(R.string.server_events_error), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar() {
        main_progressBar.visibility = ProgressBar.VISIBLE
    }

    override fun hideProgressBar() {
        main_progressBar.visibility = ProgressBar.INVISIBLE
    }

    override fun detachOnScrollListeners() = rv_events.clearOnScrollListeners()

    override fun navigateToMain(position: Int) {
        val intent = Intent(this@MainActivity, DetailsActivity::class.java)
        intent.putExtra(EXTRA_POSITION, position)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_pagination -> createDialog()
        }
        return true
    }

    private fun createDialog() {
        val dialog = PaginationDialog()
        dialog.show(supportFragmentManager, "custom")
    }
}