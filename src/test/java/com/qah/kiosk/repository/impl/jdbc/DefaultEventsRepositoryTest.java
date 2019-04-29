package com.qah.kiosk.repository.impl.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qah.kiosk.config.MainApp;
import com.qah.kiosk.entity.Event;
import com.qah.kiosk.util.EventType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=MainApp.class)
public class DefaultEventsRepositoryTest {
	
	@Autowired
	private DefaultEventsRepository repo;
	
	private static List<Event> events = new ArrayList<>();
	
	static {
		Event ev = new Event();
		ev.setDefaultDataLang("en");
		ev.setEventEndDate(LocalDate.of(2019, 3, 30));
		ev.setEventStartDate(LocalDate.of(2019, 3, 28));
		ev.setEventType("CA-CO");
		
		Event ev2 = new Event();
		ev2.setDefaultDataLang("en");
		ev2.setEventEndDate(LocalDate.of(2019, 3, 31));
		ev2.setEventStartDate(LocalDate.of(2019, 3, 29));
		ev2.setEventType("CA-CO");
		
		Event ev3 = new Event();
		ev3.setDefaultDataLang("sp");
		ev3.setEventEndDate(LocalDate.of(2019, 3, 30));
		ev3.setEventStartDate(LocalDate.of(2019, 3, 28));
		ev3.setEventType("CA-CO");
		
		Event ev4 = new Event();
		ev4.setDefaultDataLang("en");
		ev4.setEventEndDate(LocalDate.of(2019, 3, 22));
		ev4.setEventStartDate(LocalDate.of(2019, 3, 20));
		ev4.setEventType("CA-CO");
		
		Event ev5 = new Event();
		ev5.setDefaultDataLang("en");
		ev5.setEventEndDate(LocalDate.of(2019, 3, 30));
		ev5.setEventStartDate(LocalDate.of(2019, 3, 27));
		ev5.setEventType("CA-CO");
		
		Event ev6 = new Event();
		ev6.setDefaultDataLang("en");
		ev6.setEventEndDate(LocalDate.of(2019, 3, 31));
		ev6.setEventStartDate(LocalDate.of(2019, 3, 26));
		ev6.setEventType("CA-CO");
		
		events.add(ev);
		events.add(ev2);
		events.add(ev3);
		events.add(ev4);
		events.add(ev5);
		events.add(ev6);
		
		/*
		 * -  -  -  -  -  -  -  -  28 -  30 -
		 * -  -  -  -  -  -  -  -  -  29 -  31
		 * -  -  -  -  -  -  -  -  28 -  30 -
		 * 20 -  22 -  -  -  -  -  -  -  -  -
		 * -  -  -  -  -  -  -  27 -  -  30 -
		 * -  -  -  -  -  -  26 -  -  -  -  31
		 * 
		 */
		
	}
	
	@Before
	public void clearDataBase() {
		repo.deleteAllEvents();
	}
	
	@Test
	public void givenNullEventType_thenGetEventsBetween_returns5() {

		repo.saveEventsList(events);
		
		LocalDate start = LocalDate.of(2019, 3, 27);
		LocalDate end = LocalDate.of(2019, 3, 30);
		
		Assert.assertEquals(5, repo.getEventsBetween(start, end, null, null).size());
		
	}
	
	@Test
	public void givenNullEventType_thenGetEventsBetween_returns1() {

		repo.saveEventsList(events);
		
		LocalDate start = LocalDate.of(2019, 3, 23);
		LocalDate end = LocalDate.of(2019, 3, 26);
		
		Assert.assertEquals(1, repo.getEventsBetween(start, end, null, null).size());
		
	}
	
	@Test
	public void givenNullEventType_thenGetEventsBetween_returns0() {

		repo.saveEventsList(events);
		
		LocalDate start = LocalDate.of(2019, 3, 23);
		LocalDate end = LocalDate.of(2019, 3, 24);
		
		Assert.assertEquals(0, repo.getEventsBetween(start, end, null, null).size());
		
	}
	

	@Test
	public void givenNonNullEventType_thenGetEventsBetween_Returns5() {

		repo.saveEventsList(events);
		
		LocalDate start = LocalDate.of(2019, 3, 27);
		LocalDate end = LocalDate.of(2019, 3, 30);
		
		Assert.assertEquals(5, repo.getEventsBetween(start, end, null, new EventType("CA-CO")).size());
		
	}

	
	@Test
	public void givenNullEventType_thenGetEventsAfter_Returns5() {
		repo.saveEventsList(events);
		
		LocalDate after = LocalDate.of(2019, 3, 29);
		
		Assert.assertEquals(5, repo.getEventsAfter(after, null, null).size());
	}
	
	@Test
	public void givenNullEventType_thenGetEventsAfter_Returns2() {
		repo.saveEventsList(events);
		
		LocalDate after = LocalDate.of(2019, 3, 30);
		
		Assert.assertEquals(5, repo.getEventsAfter(after, null, null).size());
	}
	
	@Test
	public void givenNonNullEventType_thenGetEventsAfter_Returns2() {
		repo.saveEventsList(events);
		
		LocalDate after = LocalDate.of(2019, 3, 30);
		
		Assert.assertEquals(5, repo.getEventsAfter(after, null, new EventType("CA-CO")).size());
	}
	
	@Test
	public void givenNullEventType_thenGetEventsBefore_Returns2() {
		repo.saveEventsList(events);
		
		LocalDate before = LocalDate.of(2019, 3, 27);
		
		Assert.assertEquals(2, repo.getEventsBefore(before, null, null).size());
	}
	
	@Test
	public void givenNullEventType_thenGetEventsBefore_Returns5() {
		repo.saveEventsList(events);
		
		LocalDate before = LocalDate.of(2019, 3, 29);
		
		Assert.assertEquals(5, repo.getEventsBefore(before, null, null).size());
	}
	
	@Test
	public void givenNonNullEventType_thenGetEventsBefore_Returns2() {
		repo.saveEventsList(events);
		
		LocalDate before = LocalDate.of(2019, 3, 27);
		
		Assert.assertEquals(2, repo.getEventsBefore(before, null, new EventType("CA-CO")).size());
	}
	
	@Test
	public void givenNonNullEventType_thenGetEventsAllDates_Returns6() {
		repo.saveEventsList(events);
		
		
		Assert.assertEquals(6, repo.getEventsAllDates(null, new EventType("CA-CO")).size());
	}
	
	@Test
	public void givenNullEventType_thenGetEventsAllDates_Returns6() {
		repo.saveEventsList(events);
		
		
		Assert.assertEquals(6, repo.getEventsAllDates(null, new EventType("CA-CO")).size());
	}
	
	
	// make sure an empty List is returned if no events are found...
	
	@Test
	public void givenNoEvents_thenGetEventsAllDates_ReturnsEmptyList() {

		Assert.assertTrue(repo.getEventsAllDates(null, new EventType("CA-CO")).size() == 0);
	}
	
	@Test
	public void givenNoEvents_thenGetEventsBefore_ReturnsEmptyList() {
		
		Assert.assertTrue(repo.getEventsBefore(LocalDate.of(1019, 3, 30), null, null).size() == 0);
	}
	
	@Test
	public void givenNoEvents_thenGetEventsAfter_ReturnsEmptyList() {
		
		Assert.assertTrue(repo.getEventsAfter(LocalDate.of(1019, 3, 30), null, null).size() == 0);
	}
	
	@Test
	public void givenNoEvents_thenGetEventsBetween_ReturnsEmptyList() {
		
		Assert.assertTrue(repo.getEventsBetween(LocalDate.of(1019, 3, 30), LocalDate.of(2017, 3, 30),  null, null).size() == 0);
	}
}
