package com.qah.kiosk.service.utils;

import java.time.LocalDate;

public class TimeRange {
	private LocalDate eventStartDate;
	private LocalDate eventEndDate;
	private TimeRangeType range;
	
	private TimeRange(TimeRangeType range, LocalDate eventDate) {
		if(range.equals(TimeRangeType.BEFORE)) {
			this.eventEndDate = eventDate;
		} else {
			this.eventStartDate = eventDate;
		}
		this.range = range;

	}
	
	private TimeRange() {
		this(null, null, null);
	}
	
	private TimeRange(TimeRangeType type) {
		this(type, null, null);
	}
	
	private TimeRange(TimeRangeType range, LocalDate eventStartDate, LocalDate eventEndDate) {
		this.range = range;
		this.eventStartDate = eventStartDate;
		this.eventEndDate = eventEndDate;

	}
	
	public void setEventStartDate(LocalDate eventStartDate) {
		this.eventStartDate = eventStartDate;
			
	}
	
	public LocalDate getEventStartDate() {
		return this.eventStartDate;
	}

	
	public void setEventEndDate(LocalDate eventEndDate) {
		this.eventEndDate = eventEndDate;	
	}

	public LocalDate getEventEndDate() {
		return this.eventEndDate;
	}
	
	public TimeRangeType getTimeRangeType() {
		return this.range;
	}
	
	public void setTimeRangeType(TimeRangeType type) {
		this.range = type; 
	}
	
	/*
	 * Use this method to create a TimeRangeType that will match 
	 * events based on their startDate:
	 * 
	 * 1. events that start after the given date
	 * 2. events that end before the given date
	 */
	static public TimeRange createTimeRange(LocalDate startDate, LocalDate endDate) {
		// BETWEEN, AFTER, BEFORE, ALL
		if(startDate != null) {
			// BETWEEN, AFTER

			if(endDate != null) {
				// BETWEEN	(or ERROR)
				if(endDate.isAfter(startDate)) {
					// BETWEEN
					return new TimeRange(TimeRangeType.BETWEEN, startDate, endDate);
				}
				
				//ERROR
				return new TimeRange(TimeRangeType.ERROR, startDate, endDate);

						
			}
					
			// AFTER
			return new TimeRange(TimeRangeType.AFTER, startDate);

		} else if(endDate != null) {
			// BEFORE
			return new TimeRange(TimeRangeType.BEFORE, endDate); 
		} else {
			// ALL
			return new TimeRange(TimeRangeType.ALL);
		}
		
	}
}
