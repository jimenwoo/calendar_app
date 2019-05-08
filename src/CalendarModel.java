import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CalendarModel{

	private int maxDays;
	private int maxYear;
	private int currentDay;
	private int currentMonth;
	private HashMap<String, ArrayList<Event>> eventMap = new HashMap<>();
	private ArrayList<ChangeListener> listeners = new ArrayList<>();
	private GregorianCalendar cal = new GregorianCalendar();
	private boolean monthChanged = false;
	private boolean yearChanged = false;
	
	

	public CalendarModel() {
		maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		currentDay = cal.get(Calendar.DATE);
	}

	public void attach(ChangeListener l) {
		listeners.add(l);
	}

	public void update() {
		for (ChangeListener l : listeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}
	
	public void setDay(int day) {
		currentDay = day;
	}
	
	public int getDay() {
		return currentDay;
	}

	public int getSelectedDay() {
		return currentDay;
	}

	public int getDayOfWeek(int i) {
		cal.set(Calendar.DAY_OF_MONTH, i);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public int getMaxDays() {
		return maxDays;
	}

	public void nextDay() {
		currentDay++;
		if (currentDay > cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			nextMonth();
			currentDay = 1;
		}
		update();
	}

	public void prevDay() {
		currentDay--;
		if (currentDay < 1) {
			prevMonth();
			currentDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		update();
	}

	public int getCurrentYear() {
		return cal.get(Calendar.YEAR);
	}
	
	public void nextYear() {
		cal.add(Calendar.YEAR, 1);
		yearChanged = true;
		yearChanged();
		update();
	}
	
	public void prevYear() {
		cal.add(Calendar.YEAR, -1);
		yearChanged = true;
		yearChanged();
		update();
	}
	
	public void resetYear() {
		this.yearChanged = false;
	}

	public int getCurrentMonth() {
		return cal.get(Calendar.MONTH);
	}

	public void nextMonth() {
		cal.add(Calendar.MONTH, 1);
		maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		monthChanged = true;
		update();
	}
	
	public void prevMonth() {
		cal.add(Calendar.MONTH, -1);
		maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		monthChanged = true;
		update();
	}
	
	public boolean hasMonthChanged() {
		return monthChanged;
	}
	
	public boolean yearChanged() {
		return yearChanged;
	}
	
	public void resetMonthChanged() {
		monthChanged = false;
	}
	
	public void createEvent(String title) {
		String date = (cal.get(Calendar.MONTH) + 1) + "/" + currentDay + "/" + cal.get(Calendar.YEAR);
		Event e = new Event(title, date);
		ArrayList<Event> eventArray = new ArrayList<>();
		if (hasEvent(e.date)) {
			eventArray = eventMap.get(date);
		}
		eventArray.add(e);
		eventMap.put(date, eventArray);
	}
	

	public Boolean hasEvent(String date) {
		return eventMap.containsKey(date);
	}

	
	public ArrayList<Event> getEvents(String date) {
		ArrayList<String> eventsArray = new ArrayList<String>();
		ArrayList<Event> eventArray = eventMap.get(date);
		
		String events = "";
		for (Event e : eventArray) {
			eventsArray.add(e.toString());
		}
		return eventArray;
	}
	
	public void printEvent(String date) {
		ArrayList<Event> eventArray = eventMap.get(date);
	}
	
	public void removeEvent(int selected) {
		String date = (cal.get(Calendar.MONTH) + 1) + "/" + currentDay + "/" + cal.get(Calendar.YEAR);
		eventMap.get(date).remove(eventMap.get(date).get(selected));
		
	}
	
	public void editEvent(int selected, String str) {
		String date = (cal.get(Calendar.MONTH) + 1) + "/" + currentDay + "/" + cal.get(Calendar.YEAR);
		Event e = new Event(str, date);
		eventMap.get(date).set(selected, e);
	}
	
	static class Event implements Serializable {

		private String title;
		private String date;
		private String start;
		private String end;

		private Event(String title, String date) {
			this.title = title;
			this.date = date;
		}
		
		public String toString() {

			return title;
		}
	}
}
