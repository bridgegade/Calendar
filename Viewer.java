package calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SortedMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
/**
 * Viewer portion of MVS model, is a JFrame and ChangeListener.
 * @author anthony
 *
 */
public class Viewer extends JFrame implements ChangeListener
{
	private MyCalendar myCalendar;
	private JPanel dayView;
	private JPanel monthView;
	private Controller con;
	/**
	 * Constructor, initializes the controller object and MyCalendar object. Also adds buttons from 
	 * the controller and displays the month view and day view.
	 * @param c
	 * @param controller
	 */
	public Viewer(MyCalendar c, Controller controller)
	{	con = controller;
		myCalendar = c;
		setLocation(100, 200);
		setLayout(new BorderLayout());

		monthView = new JPanel();
		monthView.setPreferredSize(new Dimension(1000, 800));
		drawMonth(myCalendar.cal, monthView);

		dayView = new JPanel();
		dayView.setLayout(new BorderLayout());
		
		

		initializewDayView(myCalendar.cal, dayView);


		

		
		
		add(con.nextPrevious, BorderLayout.NORTH);
		
		add(con.createQuit, BorderLayout.SOUTH);
		add(monthView, BorderLayout.WEST);
		add(dayView, BorderLayout.EAST);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1500, 600);
		setVisible(true);
	}
	/**
	 * Draws the month with each day being a clickable JLabel.
	 * @param c
	 * @param panel
	 */
	public void drawMonth(GregorianCalendar c, JPanel panel)
	{
		GridLayout grid = new GridLayout(0, 7);

		panel.setLayout(grid);
		panel.setBounds(0, 0, 400, 300);
		GregorianCalendar temp = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);

		int[] month = new int[c.getActualMaximum(Calendar.DAY_OF_MONTH)];
		panel.add(new JLabel("  " + myCalendar.arrayOfMonths[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR)));
		for (int x = 0; x < 6; x++)
		{
			JLabel day = new JLabel("    ");
			panel.add(day);
		}
		for (int x = 0; x < 7; x++)
		{
			JLabel day = new JLabel("  " + myCalendar.arrayOfDays[x].toString().substring(0, 1) + " ");
			panel.add(day);
		}
		for (int y = 0; y < temp.get(Calendar.DAY_OF_WEEK) - 1; y++)
		{
			JLabel day = new JLabel("    ");

			panel.add(day);
		}

		for (int x = 0; x < month.length; x++)
		{ // checks if the date has an event
			SortedMap<Long, Event> subMapStore = myCalendar.events.subMap(Long.parseLong(
					c.get(Calendar.YEAR) + "" + (((c.get(Calendar.MONTH) + 1) < 10) ? "0" + (c.get(Calendar.MONTH) + 1) : (c.get(Calendar.MONTH) + 1)) + ""
							+ (((x + 1) < 10) ? "0" + (x + 1) : (x + 1)) + "00000000"),
					Long.parseLong(c.get(Calendar.YEAR) + "" + (((c.get(Calendar.MONTH) + 1) < 10) ? "0" + (c.get(Calendar.MONTH) + 1) : (c.get(Calendar.MONTH) + 1)) + ""
							+ (((x + 2) < 10) ? "0" + (x + 2) : (x + 2)) +"00000000"));

			JLabel day = new JLabel(" " + Integer.toString(x + 1) + " ");
			day.setBorder(new LineBorder(Color.black));
			GregorianCalendar tempC = new GregorianCalendar();
			if ((tempC.get(Calendar.DAY_OF_MONTH) == (x + 1)) && (tempC.get(Calendar.YEAR) == c.get(Calendar.YEAR))
					&& (tempC.get(Calendar.MONTH) == c.get(Calendar.MONTH)))
			{
				day.setBackground(Color.LIGHT_GRAY);
				day.setOpaque(true);
			}
			if ((myCalendar.cal.get(Calendar.DAY_OF_MONTH) == (x + 1)) && (myCalendar.cal.get(Calendar.YEAR) == c.get(Calendar.YEAR))
					&& (myCalendar.cal.get(Calendar.MONTH) == myCalendar.cal.get(Calendar.MONTH)))
			{
				day.setBackground(Color.cyan);
				day.setOpaque(true);
			}
			if (!(subMapStore.isEmpty()))
			{
				day.setBackground(Color.pink);
				day.setOpaque(true);
			}
			day.addMouseListener(con.listener(x + 1));
			panel.add(day);
		}

		System.out.println();

	}

	
	/**
	 * Adds the day to a day view panel and decorates it with a JScrollPane.
	 * @param c
	 * @param panel
	 */
	public void initializewDayView(Calendar c, JPanel panel)
	{
		panel.add(con.day, BorderLayout.NORTH);
		con.daySelected.setPreferredSize(new Dimension(300, 100));
		JScrollPane scroll = new JScrollPane(con.daySelected);

		panel.add(scroll);

	}
	
	@Override
	public void stateChanged(ChangeEvent e)
	{
		if(myCalendar.end==true){
			dispose();
		}
		monthView.removeAll();
		repaint();
		monthView.revalidate();
		
		drawMonth(myCalendar.cal, monthView);
	}

}