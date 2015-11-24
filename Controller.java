package calendar;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.SortedMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
/**
 * Controller portion of MVC pattern.
 * @author anthony
 *
 */
public class Controller
{
	MyCalendar myCalendar;
	JTextArea daySelected;
	JPanel nextPrevious;
	JButton next;
	JButton previous;
	JFrame createEvent;
	JTextField title;
	JTextField date;
	JTextField startingTime;
	JTextField endingTime;
	JLabel status;
	JPanel createQuit;
	JButton save;
	JButton quit;
	JLabel day;
	
	/**
	 * Constructor that creates buttons for a viewer to add and defines their ActionListeners.
	 * Also pops up a frame when create is clicked with its own set of buttons.
	 * @param c
	 */
	public Controller(MyCalendar c)
	{	
		myCalendar = c;
		nextPrevious = new JPanel();
		next = new JButton(">");
		next.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent a)
			{

				myCalendar.userInput("V", "M", "N", "", "");
				myCalendar.cal.set(Calendar.DATE, 1);
				drawDay();
				

			}

		});
		previous = new JButton("<");
		previous.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent a)
			{
				
				myCalendar.userInput("V", "M", "P", "", "");
				myCalendar.cal.set(Calendar.DATE, 1);
				drawDay();
				

			}

		});
		nextPrevious.add(previous);
		nextPrevious.add(next);

		title = new JTextField("");
		title.setPreferredSize(new Dimension(350, 20));
		date = new JTextField((((myCalendar.cal.get(Calendar.MONTH) + 1) < 10) ? "0" + (myCalendar.cal.get(Calendar.MONTH) + 1)
				: (myCalendar.cal.get(Calendar.MONTH) + 1))
				+ "/"
				+ ((myCalendar.cal.get(Calendar.DAY_OF_MONTH) < 10) ? "0" + (myCalendar.cal.get(Calendar.DAY_OF_MONTH))
						: (myCalendar.cal.get(Calendar.DAY_OF_MONTH))) + "/" + myCalendar.cal.get(Calendar.YEAR));
		startingTime = new JTextField("");
		startingTime.setPreferredSize(new Dimension(100, 20));
		endingTime = new JTextField("");
		endingTime.setPreferredSize(new Dimension(100, 20));
		status = new JLabel("");
		createQuit = new JPanel();

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
		save = new JButton("Save");
		save.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{	String startStore = startingTime.getText();
				String endStore = endingTime.getText();
				if (startingTime.getText().length() < 7)
				{
					startingTime.setText("0" + startingTime.getText());
				}
				if (endingTime.getText().length() < 7)
				{
					endingTime.setText("0" + endingTime.getText());
					
				}
				if(startingTime.getText().substring(5, 7).equals("pm")){
					
					startingTime.setText(startingTime.getText().substring(0, 5));
					startingTime.setText(startingTime.getText().replaceAll("[\\W]", ""));
					int store = Integer.parseInt(startingTime.getText());
					store += 1200;
					System.out.println(store);
					startingTime.setText(Integer.toString(store));
					
				}
				
				if(endingTime.getText().substring(5, 7).equals("pm")){
					
					endingTime.setText(endingTime.getText().substring(0, 5));
					endingTime.setText(endingTime.getText().replaceAll("[\\W]", ""));
					int store = Integer.parseInt(endingTime.getText());
					store += 1200;
					System.out.println(store);
					endingTime.setText(Integer.toString(store));
				}
				if (!(myCalendar.createEvent(title.getText(), date.getText(), startingTime.getText() + endingTime.getText())))
				{	endingTime.setText(endStore);
					startingTime.setText(startStore);
					status.setText("There is a time conflict, please enter different times.");
					
				} else
				{	startingTime.setText("");
					endingTime.setText("");
					title.setText("");
					myCalendar.update();

					createEvent.dispose();
				}
			}

		});
		quit = new JButton("Quit & Save");
		quit.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				myCalendar.userInput("Q", "", "", "", "");
				myCalendar.update();
			}

		});
		
		daySelected = new JTextArea();
		daySelected.setRows(24);
		
		day = new JLabel();
			
		createQuit.add(create);
		createQuit.add(quit);

		createEvent = new JFrame();
		createEvent.setLayout(new FlowLayout());
		createEvent.setSize(400, 200);

		JLabel t = new JLabel("Title: ");
		JLabel d = new JLabel("Date: ");
		JLabel sT = new JLabel("Starting Time: ");
		JLabel eT = new JLabel("Ending Time: ");
		createEvent.add(t);
		createEvent.add(title);
		createEvent.add(d);
		createEvent.add(date);
		createEvent.add(sT);
		createEvent.add(startingTime);
		createEvent.add(eT);
		createEvent.add(endingTime);
		createEvent.add(save);
		createEvent.add(status);
		createEvent.setAlwaysOnTop(true);
		createEvent.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		drawDay();

	}
	/**
	 * Draws the day's events based on the current selected day.
	 */
	public void drawDay(){
		daySelected.setText("");
		for (int x = 1; x < 13; x++)
		{
			daySelected.append(x + " AM\n");
		}
		for (int x = 1; x < 13; x++)
		{
			daySelected.append(x + " PM\n");
		}
		
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
		{	// for debugging
			System.out.println("events's keys loaded: ");
			for (Long i : myCalendar.events.keySet())
			{
				System.out.println(i);
			}
			// use treemap's submap to find events in same day
			SortedMap<Long, Event> subMapStore = myCalendar.events.subMap(Long.parseLong(myCalendar.cal.get(Calendar.YEAR)
					+ ""
					+ (((myCalendar.cal.get(Calendar.MONTH) + 1) < 10) ? "0" + (myCalendar.cal.get(Calendar.MONTH) + 1) : (myCalendar.cal
							.get(Calendar.MONTH) + 1))
					+ ""
					+ (((myCalendar.cal.get(Calendar.DAY_OF_MONTH)) < 10) ? "0" + myCalendar.cal.get(Calendar.DAY_OF_MONTH) : (myCalendar.cal
							.get(Calendar.DAY_OF_MONTH))) + "" + "00000000"), Long.parseLong(myCalendar.cal.get(Calendar.YEAR)
					+ ""
					+ (((myCalendar.cal.get(Calendar.MONTH) + 1) < 10) ? "0" + (myCalendar.cal.get(Calendar.MONTH) + 1) : (myCalendar.cal
							.get(Calendar.MONTH) + 1))
					+ ""
					+ (((myCalendar.cal.get(Calendar.DAY_OF_MONTH) + 1) < 10) ? "0" + (myCalendar.cal.get(Calendar.DAY_OF_MONTH) + 1) : (myCalendar.cal
							.get(Calendar.DAY_OF_MONTH) + 1)) + "00000000"));

			// print events in the day
			for (Long i : subMapStore.keySet())
			{
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
								+ ((subMapStore.get(k).startTime.get(Calendar.HOUR_OF_DAY)>12)? subMapStore.get(k).startTime.get(Calendar.HOUR_OF_DAY)-12:subMapStore.get(k).startTime.get(Calendar.HOUR_OF_DAY))
								+ ":"
								+ ((subMapStore.get(k).startTime.get(Calendar.MINUTE) < 10) ? "0" + subMapStore.get(k).startTime.get(Calendar.MINUTE)
										: subMapStore.get(k).startTime.get(Calendar.MINUTE))+((subMapStore.get(k).startTime.get(Calendar.HOUR_OF_DAY)>12)?"pm":"am")
								+ " "
								+ ((subMapStore.get(k).endTime == null) ? "" : "- "
										+ ((subMapStore.get(k).endTime.get(Calendar.HOUR_OF_DAY)>12)? subMapStore.get(k).endTime.get(Calendar.HOUR_OF_DAY)-12:subMapStore.get(k).endTime.get(Calendar.HOUR_OF_DAY))
										+ ":"
										+ ((subMapStore.get(k).endTime.get(Calendar.MINUTE) < 10) ? "0"
												+ subMapStore.get(k).endTime.get(Calendar.MINUTE) : subMapStore.get(k).endTime.get(Calendar.MINUTE)))+((subMapStore.get(k).endTime.get(Calendar.HOUR_OF_DAY)>12)?"pm":"am")
								+ "\n", daySelected.getLineStartOffset(subMapStore.get(k).startTime.get(Calendar.HOUR_OF_DAY)));

			}

		} catch (BadLocationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createEvent.dispose();
		myCalendar.update();
	}
	/**
	 * Creates a MouseListener that draws the day based on the day clicked which is passed as a variable x.
	 * @param x
	 * @return
	 */
	public MouseListener listener(final int x)
	{
		return new MouseAdapter()
		{
			public void mousePressed(MouseEvent event)
			{	
				
				myCalendar.cal.set(Calendar.DATE, x);

				drawDay();
			}
		};
	}
}
