
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ListView implements ChangeListener{
	
	public enum MONTHS {
		January, February, March, April, May, June, July, August, September, October, November, December;
	}
	
	public enum DAYS {
		Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
	}
	
	
	private CalendarModel cm;
	private DAYS[] arrayofDays = DAYS.values();
	private MONTHS[] arrayOfMonths = MONTHS.values();
	private JFrame frame = new JFrame("Calendar");
	private DefaultListModel<String>  lbm = new DefaultListModel<String>();
	private JList list = new JList(lbm);
	private JScrollPane scroller;
	private JLabel dateLabel = new JLabel();
	private int selected;
	JTextField eventText = new JTextField(25);
	
	
	public ListView(CalendarModel cm)
	{
		
		this.cm = cm;
		JPanel buttonPanel = new JPanel();
		JButton add = new JButton("Add Task");
		JButton delete = new JButton("Delete Task");
		JButton edit = new JButton("Edit Task");
		JButton export = new JButton("Export");
		
		buttonPanel.add(add);
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createEvent();
			}
			
		});
		buttonPanel.add(delete);
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteEvent();
			}	
		});
		buttonPanel.add(edit);
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String str = eventText.getText();
				editEvent(str);
			}
			
		});
		buttonPanel.add(export);
		export.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					PrintWriter output = new PrintWriter(arrayOfMonths[cm.getCurrentMonth()] + "-" + cm.getDay() + "-" + cm.getCurrentYear()+".txt");
					output.println("Tasks for: " + arrayOfMonths[cm.getCurrentMonth()] + "-" + cm.getDay() + "-" + cm.getCurrentYear() + "\n");
					for(int i = 0; i < lbm.size(); i++) {
						output.println(lbm.get(i));
					}
					output.flush();
					output.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				
			}
			
		});

		frame.setLayout(new FlowLayout());
		list.setLayout(new FlowLayout());
		
		list.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				selected = list.getSelectedIndex();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		dateLabel.setText((arrayOfMonths[cm.getCurrentMonth()] + "-" + cm.getDay() + "-" + cm.getCurrentYear()));
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setPreferredSize(new Dimension(300,200));
		frame.add(dateLabel);
		JScrollPane spane = new JScrollPane();
		spane.setViewportView(list);
		spane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(spane);
		frame.add(panel);
		frame.add(eventText);
		frame.add(buttonPanel);
		frame.setPreferredSize(new Dimension(440, 370));
		frame.setLocation(710, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
		
		
		
	}
	
	private void createEvent() {
		cm.createEvent(eventText.getText());	
		eventText.setText("");
		showDate(cm.getSelectedDay());
		};
		
	private void deleteEvent() {
		try {
		cm.removeEvent(selected);
		showDate(cm.getSelectedDay());
		}
		catch (Exception e) {};
		
	}
	
	private void editEvent(String str) {
		try {
		cm.editEvent(selected, str);
		showDate(cm.getSelectedDay());
		}
		catch (Exception e) {};
	}
	
	public void showDate(final int d) {
		cm.setDay(d);
		dateLabel.setText((arrayofDays[cm.getDayOfWeek(d) - 1] + ", " + arrayOfMonths[cm.getCurrentMonth()] + "-" + cm.getDay() + "-" + cm.getCurrentYear()));
		String date = (cm.getCurrentMonth() + 1) + "/" + d + "/" + cm.getCurrentYear();
		ArrayList<String> eventsMap = new ArrayList<>();
		ArrayList<CalendarModel.Event> eventMaps = new ArrayList<CalendarModel.Event>();
		lbm.clear();
		if (cm.hasEvent(date)) {
			cm.printEvent(date);
			eventMaps = cm.getEvents(date);
			for(int i = 0; i < eventMaps.size(); i++) {
				eventsMap.add(eventMaps.get(i).toString());
			}
			
			for(int j = 0; j < eventsMap.size(); j++) {
				lbm.addElement(eventsMap.get(j));
			}
		}
	}
	
	
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}


}
