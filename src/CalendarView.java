import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class CalendarView implements ChangeListener {

	public enum DAYS {
		Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
	}
	public enum MONTHS {
		January, February, March, April, May, June, July, August, September, October, November, December;
	}

	private CalendarModel cm;
	private DAYS[] arrayOfDays = DAYS.values();
	private MONTHS[] arrayOfMonths = MONTHS.values();
	private int dayBefore = -1;
	private int daysInMonth;
	private ListView list;

	JPanel container = new JPanel();
	private JFrame frame = new JFrame("Calendar");
	private JPanel monthViewPanel = new JPanel();
	private JLabel monthLabel = new JLabel();
	private JButton create = new JButton("Create");
	private JButton nextDay = new JButton(">>");
	private JButton prevDay = new JButton("<<");
	private JTextPane dayTextPane = new JTextPane();
	private ArrayList<JButton> dButtons = new ArrayList<JButton>();
	private JPanel header = new JPanel();

	
	public CalendarView(CalendarModel cm, ListView list) {
		this.cm = cm;
		this.list =list;
		daysInMonth = cm.getMaxDays();
		monthViewPanel.setLayout(new GridLayout(0, 7));
		dayTextPane.setPreferredSize(new Dimension(200, 150));
		dayTextPane.setEditable(false);

		createDayButtons();
		emptyButtons();
		addDayButtons();
		list.showDate(cm.getSelectedDay());
		highlightSelectedDate(cm.getSelectedDay() - 1);

		
		JSpinner spinner = new JSpinner();
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "#");
		spinner.setEditor(editor);
		spinner.setValue(2019);
		spinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				Integer val = (Integer)spinner.getValue();
				if(val > cm.getCurrentYear()) {
					cm.nextYear();
				}
				else
					cm.prevYear();
			}
			
		});
		JTextPane currentDate = new JTextPane();
		currentDate.setEditable(false);
		
		currentDate.setText("Todays Date: " + arrayOfMonths[cm.getCurrentMonth()] + "-" + cm.getDay() + "-" + cm.getCurrentYear() );
		header.add(monthLabel, BorderLayout.NORTH);
		header.add(spinner);
		header.add(currentDate);
		
		
		
		JPanel monthView = new JPanel();
		monthView.setLayout(new BorderLayout());
		monthLabel.setText(arrayOfMonths[cm.getCurrentMonth()] + " " + cm.getCurrentYear());
		
		monthView.add(new JLabel("         S                M                T                 W                T                 F                 S"), BorderLayout.CENTER);
		monthView.add(monthViewPanel, BorderLayout.SOUTH);
		JPanel dayViewPanel = new JPanel();
		dayViewPanel.setLayout(new GridBagLayout());
		
		nextDay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cm.nextMonth();
			}
		});
		prevDay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cm.prevMonth();
			}
		});
		

		frame.setLayout(new FlowLayout());
		frame.add(header);
		container.add(prevDay);
		container.add(monthView);
		container.add(nextDay);
		frame.setPreferredSize(new Dimension(700, 305));
		frame.add(container);
		frame.add(currentDate);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (cm.hasMonthChanged() || cm.yearChanged()) {
			daysInMonth = cm.getMaxDays();
			dButtons.clear();
			monthViewPanel.removeAll();
			monthLabel.setText(arrayOfMonths[cm.getCurrentMonth()] + " " + cm.getCurrentYear());
			createDayButtons();
			emptyButtons();
			addDayButtons();
			dayBefore = -1;
			cm.resetMonthChanged();
			cm.resetYear();
			frame.pack();
			frame.repaint();
		} else {
			list.showDate(cm.getSelectedDay());
			highlightSelectedDate(cm.getSelectedDay() - 1);
		}
	}

	private void showDate(final int d) {
		cm.setDay(d);
		String dayOfWeek = arrayOfDays[cm.getDayOfWeek(d) - 1] + "";
		String date = (cm.getCurrentMonth() + 1) + "/" + d + "/" + cm.getCurrentYear();
		String events = "";

		if (cm.hasEvent(date)) {
			events += cm.getEvents(date);
		}
		dayTextPane.setText(dayOfWeek + " " + date + "\n" + events);
		dayTextPane.setCaretPosition(0);
	}

	private void highlightSelectedDate(int d) {
		Border border = new LineBorder(Color.RED, 2);
		dButtons.get(d).setBorder(border);

		if (dayBefore != -1) {
			dButtons.get(dayBefore).setBorder(new JButton().getBorder());
		}
		dayBefore = d;
	}

	private void createDayButtons() {
		for (int i = 1; i <= daysInMonth; i++) {
			final int d = i;
			JButton day = new JButton(Integer.toString(d));
			day.setBackground(Color.WHITE);
	
			day.addActionListener(new ActionListener() {
	
				@Override
				public void actionPerformed(ActionEvent arg0) {
					list.showDate(d);
					
					highlightSelectedDate(d - 1);
					create.setEnabled(true);
					nextDay.setEnabled(true);
					prevDay.setEnabled(true);
				}
			});
			dButtons.add(day);
		}
	}

	private void addDayButtons() {
		for (JButton d : dButtons) {
			monthViewPanel.add(d);
		}
	}

	private void emptyButtons() {
		for (int j = 1; j < cm.getDayOfWeek(1); j++) {
			JButton blank = new JButton();
			blank.setEnabled(false);
			monthViewPanel.add(blank);
		}
	}
}
