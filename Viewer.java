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

public class Viewer extends JFrame implements ChangeListener
{
	private MyCalendar myCalendar;
	private JPanel dayView;
	private JTextArea daySelected;
	private JLabel day;
	private JPanel monthView;
	private JFrame createEvent;
	JTextField title;
	JTextField date;
	JTextField startingTime;
	JTextField endingTime;
	JLabel status;
	String time;

	public Viewer(MyCalendar c, Controller con)
	{
		myCalendar = c;
		setLocation(100, 200);
		setLayout(new BorderLayout());

		monthView = new JPanel();
		monthView.setPreferredSize(new Dimension(1000, 800));
		drawMonth(myCalendar.cal, monthView);

		dayView = new JPanel();
		dayView.setLayout(new BorderLayout());
		daySelected = new JTextArea();
		daySelected.setRows(24);
		day = new JLabel();

		initializewDayView(myCalendar.cal, dayView);

		JPanel nextPrevious = new JPanel();
		JButton next = new JButton(">");
		next.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent a)
			{

				myCalendar.userInput("V", "M", "N", "", "");

				myCalendar.update();

			}

		});
		JButton previous = new JButton("<");
		previous.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent a)
			{

				myCalendar.userInput("V", "M", "P", "", "");

				myCalendar.update();

			}

		});
		createEvent = new JFrame();
		createEvent.setLayout(new FlowLayout());
		createEvent.setSize(400, 200);
		
		title = new JTextField("Untitled Event");
		date = new JTextField((((myCalendar.cal.get(Calendar.MONTH) + 1) < 10) ? "0" + (myCalendar.cal.get(Calendar.MONTH) + 1)
				: (myCalendar.cal.get(Calendar.MONTH) + 1))
				+ "/"
				+ ((myCalendar.cal.get(Calendar.DAY_OF_MONTH) < 10) ? "0" + (myCalendar.cal.get(Calendar.DAY_OF_MONTH))
						: (myCalendar.cal.get(Calendar.DAY_OF_MONTH))) + "/" + myCalendar.cal.get(Calendar.YEAR));
		startingTime = new JTextField("Starting Time");
		endingTime = new JTextField("Ending Time");
	
		status = new JLabel("");
		JPanel createQuit = new JPanel();
		JButton create = new JButton("Create");
		
		create.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				date.setText((((myCalendar.cal.get(Calendar.MONTH) + 1) < 10) ? "0" + (myCalendar.cal.get(Calendar.MONTH) + 1) : (myCalendar.cal
						.get(Calendar.MONTH) + 1))
						+ "/"
						+ ((myCalendar.cal.get(Calendar.DAY_OF_MONTH) < 10) ? "0" + (myCalendar.cal.get(Calendar.DAY_OF_MONTH)) : (myCalendar.cal
								.get(Calendar.DAY_OF_MONTH))) + "/" + myCalendar.cal.get(Calendar.YEAR));

				createEvent.setVisible(true);
			}

		});
		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (startingTime.getText().length() < 5)
				{
					startingTime.setText("0" + startingTime.getText());
				}
				if (endingTime.getText().length() < 5)
				{
					endingTime.setText("0" + endingTime.getText());
				}
				if (!(myCalendar.createEvent(title.getText(), date.getText(), startingTime.getText() + endingTime.getText())))
				{
					status.setText("There is a time conflict, please enter different times.");

				} else
				{
					myCalendar.update();
					
					createEvent.dispose();
				}
			}

		});
		
		createEvent.add(title);
		createEvent.add(date);
		createEvent.add(startingTime);
		createEvent.add(endingTime);
		createEvent.add(save);
		createEvent.add(status);
		createEvent.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JButton quit = new JButton("Quit & Save");
		quit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e)
			{
				myCalendar.userInput("Q", "", "", "", "");
				dispose();
			}
			
		});
		
		nextPrevious.add(previous);
		nextPrevious.add(next);
		add(nextPrevious, BorderLayout.NORTH);
		createQuit.add(create);
		createQuit.add(quit);
		add(createQuit, BorderLayout.SOUTH);
		add(monthView, BorderLayout.WEST);
		add(dayView, BorderLayout.EAST);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1500, 600);
		setVisible(true);
	}

	public void drawMonth(Calendar c, JPanel panel)
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
			if (!(subMapStore.isEmpty()))
			{
				day.setBackground(Color.pink);
				day.setOpaque(true);
			}
			day.addMouseListener(listener(x + 1));
			panel.add(day);
		}

		System.out.println();

	}

	public MouseListener listener(final int x)
	{
		return new MouseAdapter()
		{
			public void mousePressed(MouseEvent event)
			{
				daySelected.setText("");
				for (int x = 1; x < 13; x++)
				{
					daySelected.append(x + " AM\n");
				}
				for (int x = 1; x < 13; x++)
				{
					daySelected.append(x + " PM\n");
				}
				myCalendar.cal.set(Calendar.DATE, x);

				String toReturn = "";
				toReturn += myCalendar.arrayOfDays[myCalendar.cal.get(Calendar.DAY_OF_WEEK) - 1];
				toReturn += " ";
				toReturn += myCalendar.arrayOfMonths[myCalendar.cal.get(Calendar.MONTH)];
				toReturn += " ";
				toReturn += myCalendar.cal.get(Calendar.DAY_OF_MONTH);
				toReturn += ", ";
				toReturn += myCalendar.cal.get(Calendar.YEAR);
				toReturn += "\n";
				day.setText("  " + toReturn);
				System.out.println(daySelected.getLineCount());
				try
				{
					for(Long i:myCalendar.events.keySet()){
						System.out.println(i);
					}
					// use treemap's submap to find events in same day
					SortedMap<Long, Event> subMapStore = myCalendar.events.subMap(Long.parseLong(
							myCalendar.cal.get(Calendar.YEAR)
									+ ""
									+ (((myCalendar.cal.get(Calendar.MONTH) + 1) < 10) ? "0" + (myCalendar.cal.get(Calendar.MONTH) + 1) : (myCalendar.cal
											.get(Calendar.MONTH) + 1))
									+ ""
									+ (((myCalendar.cal.get(Calendar.DAY_OF_MONTH)) < 10) ? "0" + myCalendar.cal.get(Calendar.DAY_OF_MONTH) : (myCalendar.cal
											.get(Calendar.DAY_OF_MONTH))) + ""+"00000000"),
							Long.parseLong(myCalendar.cal.get(Calendar.YEAR)
									+ ""
									+ (((myCalendar.cal.get(Calendar.MONTH) + 1) < 10) ? "0" + (myCalendar.cal.get(Calendar.MONTH) + 1) : (myCalendar.cal
											.get(Calendar.MONTH) + 1))
									+ ""
									+ (((myCalendar.cal.get(Calendar.DAY_OF_MONTH) + 1) < 10) ? "0" + (myCalendar.cal.get(Calendar.DAY_OF_MONTH) + 1)
											: (myCalendar.cal.get(Calendar.DAY_OF_MONTH) + 1)) +"00000000"));

					// print events in the day
					for(Long i:subMapStore.keySet()){
						System.out.println(i);
					}
					Long[] keys = new Long[subMapStore.size()];
					subMapStore.keySet().toArray(keys);
					for (int x = keys.length - 1; x >= 0; x--)
					{
						Long k = keys[x];
						daySelected.insert(
								"	"
										+ subMapStore.get(k).title
										+ " "
										+ subMapStore.get(k).startTime.get(Calendar.HOUR_OF_DAY)
										+ ":"
										+ ((subMapStore.get(k).startTime.get(Calendar.MINUTE) < 10) ? "0" + subMapStore.get(k).startTime.get(Calendar.MINUTE)
												: subMapStore.get(k).startTime.get(Calendar.MINUTE))
										+ " "
										+ ((subMapStore.get(k).endTime == null) ? "" : "- "
												+ subMapStore.get(k).endTime.get(Calendar.HOUR_OF_DAY)
												+ ":"
												+ ((subMapStore.get(k).endTime.get(Calendar.MINUTE) < 10) ? "0"
														+ subMapStore.get(k).endTime.get(Calendar.MINUTE) : subMapStore.get(k).endTime.get(Calendar.MINUTE)))
										+ "\n", daySelected.getLineStartOffset(subMapStore.get(k).startTime.get(Calendar.HOUR_OF_DAY)));

					}

				} catch (BadLocationException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				myCalendar.update();
			}
		};
	}

	public void initializewDayView(Calendar c, JPanel panel)
	{
		panel.add(day, BorderLayout.NORTH);
		daySelected.setPreferredSize(new Dimension(300, 100));
		JScrollPane scroll = new JScrollPane(daySelected);

		panel.add(scroll);

	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		// TODO Auto-generated method stub
		monthView.removeAll();
		repaint();
		monthView.revalidate();
		
		drawMonth(myCalendar.cal, monthView);
	}

}
