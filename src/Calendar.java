public class Calendar {

	public static void main(String[] args) {
		CalendarModel cm = new CalendarModel();
		ListView list = new ListView(cm);
		CalendarView cv = new CalendarView(cm, list);
		
		cm.attach(cv);
		cm.attach(list);
	}

}
