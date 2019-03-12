package com.github.kornilovmikhail.mvpandroidproject

import com.github.kornilovmikhail.mvpandroidproject.data.repo.TempEvents
import com.github.kornilovmikhail.mvpandroidproject.data.entity.Event
import com.github.kornilovmikhail.mvpandroidproject.data.entity.Links
import com.github.kornilovmikhail.mvpandroidproject.presenter.DetailPresenter
import com.github.kornilovmikhail.mvpandroidproject.ui.detail.`DetailView$$State`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import java.util.ArrayList

@RunWith(MockitoJUnitRunner::class)
class DetailPresenterTest {

    @Mock
    lateinit var mockViewState: `DetailView$$State`

    @Mock
    lateinit var mockTempEvents: TempEvents

    @InjectMocks
    @Spy
    lateinit var detailPresenter: DetailPresenter

    @Before
    fun setup() {
        detailPresenter.setViewState(mockViewState)
    }

    @Test
    fun testGetEvent() {
        //Arrange
        val position = 0
        val listEvents = ArrayList<Event>()
        val event = Event(0, "", 0, 0, "", Links(0, "", "", ""))
        listEvents.add(event)
        TempEvents.events = listEvents
        Mockito.doReturn(listEvents).`when`(mockTempEvents).events
        //Act
        detailPresenter.getEvent(position)
        //Assert
        Mockito.verify(mockViewState).setText("", "", "0")
    }
}