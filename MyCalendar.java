package calendar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Handles methods for loading events to a file, viewing the calendar, creating
 * events, going to a specific date, viewing all stored events, deleteing
 * events, quiting while saving currently added events to Events.txt.
 * 
 * @author Anthony Vo
 * 
 */
enum MONTHS
{
	January, February, March, April, May, June, July, August, September, October, November, December;
}

enum DAYS
{
	Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
}

public class MyCalendar
{
	MONTHS[] arrayOfMonths = MONTHS.values();
	DAYS[] arrayOfDays = DAYS.values();
	TreeMap<Long, Event> events;
	GregorianCalendar cal;
	boolean initalScreen;
	ArrayList<ChangeListener> listeners;

	public MyCalendar()
	{
		events = new TreeMap<Long, Event>();
		cal = new GregorianCalendar();
		initalScreen = true;
		listeners = new ArrayList<ChangeListener>();
		userInput("L", "", "", "", "");
	}

	public void attach(ChangeListener l)
	{
		listeners.add(l);
	}

	public void update()
	{

		for (ChangeListener l : listeners)
		{
			l.stateChanged(new ChangeEvent(this));
		}
	}

	

	/**
	 * begins the console simulation
	 */
	public void userInput(String fui, String uiv, String uiv2, String inD, String dD)
	{
		String firstUserInput = fui;
		String userInputV = uiv;
		String userInputV2 = uiv2;
		String inputD = inD;
		String dateD = dD;
		File fout = new File("Events.txt");

		Scanner in = new Scanner(System.in);
		System.out.print("Today: \n");
		printMonth(cal);
		System.out.println("Select one of the following options: " + "[L]oad   [V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
		// relies on userInput1
		if (firstUserInput.equals("C") || (firstUserInput.equals("L")))
		{
			if (firstUserInput.equals("L"))
			{

				try
				{
					Scanner fin = new Scanner(fout);
					while (fin.hasNextLine())
					{
						createEvent(fin, firstUserInput);
					}
					System.out.println("Successfully loaded events from Events.txt.");
				} catch (FileNotFoundException e)
				{
					System.out.println("This is the first run.");
					firstUserInput = "C";
					createEvent(in, firstUserInput);
				}
			} else if (firstUserInput.equals("C"))
			{
				createEvent(in, firstUserInput);
			}
		} else if (firstUserInput.equals("V") || (firstUserInput.equals("G")))
		{
			// relies on userInputV
			if (firstUserInput.equals("G"))
			{
				System.out.println("Enter the date MM/DD/YYYY.");
				String date = in.nextLine();
				date = date.replaceAll("[\\W]", "");
				date = date.trim();
				int m = Integer.parseInt(date.substring(0, 2));
				int d = Integer.parseInt(date.substring(2, 4));
				int y = Integer.parseInt(date.substring(4, 8));
				cal.set(y, m - 1, d);

				userInputV = "D";
			} else
			{
				System.out.println("[D]ay view or [M]view ? ");
				// relies on userInputV
			}
			if ((userInputV.equals("D")) || (firstUserInput.equals("G")))
			{

				printEventsForDay(cal);
				System.out.println("\n[P]revious or [N]ext or [M]ain menu ? ");

				// relies on userInputV2
				if (userInputV2.equals("P"))
				{
					cal.add(Calendar.DAY_OF_WEEK, -1);
					printEventsForDay(cal);
				} else if (userInputV2.equals("N"))
				{
					cal.add(Calendar.DAY_OF_WEEK, 1);
					printEventsForDay(cal);
				} else if (userInputV2.equals("M"))
				{
					// originally break;
				}
				System.out.println("\n[P]revious or [N]ext or [M]ain menu ? ");

			} else if (userInputV.equals("M"))
			{
				// indent based on day of week, new line on Saturday
				printMonth(cal);
				System.out.println("\n[P]revious or [N]ext or [M]ain menu ? ");

				if (userInputV2.equals("P"))
				{
					cal.add(Calendar.MONTH, -1);
					printMonth(cal);
				} else if (userInputV2.equals("N"))
				{
					cal.add(Calendar.MONTH, 1);
					printMonth(cal);
				} else if (userInputV2.equals("M"))
				{
					// originally break;
				}
				System.out.println("\n[P]revious or [N]ext or [M]ain menu ? ");
			}

		} else if (firstUserInput.equals("E"))
		{
			if (events.isEmpty())
			{
				System.out.println("There are no events scheduled");
			} else
			{
				for (Long k : events.keySet())
				{
					System.out.println(events.get(k).year);
					System.out.print(arrayOfDays[events.get(k).startTime.get(Calendar.DAY_OF_WEEK) - 1].toString()
							+ " "
							+ arrayOfMonths[events.get(k).startTime.get(Calendar.MONTH)].toString()
							+ " "
							+ events.get(k).day
							+ " "
							+ events.get(k).startTime.get(Calendar.HOUR_OF_DAY)
							+ ":"
							+ ((events.get(k).startTime.get(Calendar.MINUTE) < 10) ? "0" + events.get(k).startTime.get(Calendar.MINUTE)
									: events.get(k).startTime.get(Calendar.MINUTE))
							+ " "
							+ ((events.get(k).endTime == null) ? "" : "- "
									+ events.get(k).endTime.get(Calendar.HOUR_OF_DAY)
									+ ":"
									+ ((events.get(k).endTime.get(Calendar.MINUTE) < 10) ? "0" + events.get(k).endTime.get(Calendar.MINUTE)
											: events.get(k).endTime.get(Calendar.MINUTE))) + " " + events.get(k).title);
					System.out.println();
				}
				System.out.println();
			}

		} else if (firstUserInput.equals("D"))
		{
			System.out.println("[S]elected or [A]ll ? ");

			if (inputD.equals("A"))
			{
				events.clear();
			} else if (inputD.equals("S"))
			{
				System.out.println("Enter the date MM/DD/YYYY.");
				// relies on String dateD;
				dateD = dateD.replaceAll("[\\W]", "");
				int m = Integer.parseInt(dateD.substring(0, 2));
				int d = Integer.parseInt(dateD.substring(2, 4));
				int y = Integer.parseInt(dateD.substring(4, 8));
				ArrayList<Long> keys = new ArrayList<Long>();
				for (Long k : events.keySet())
				{
					if (events.get(k).month == m && events.get(k).day == d && events.get(k).year == y)
					{
						keys.add(k);
					}

				}
				for (Long k : keys)
				{
					events.remove(k);
				}

			}
		} else if (firstUserInput.equals("Q"))
		{
			try
			{
				PrintWriter fin = new PrintWriter(fout);
				for (Long k : events.keySet())
				{
					fin.println(events.get(k).title);
					fin.println(((events.get(k).month < 10) ? "0" + events.get(k).month : events.get(k).month) + ""
							+ ((events.get(k).day < 10) ? "0" + events.get(k).day : events.get(k).day) + events.get(k).year);
					fin.println(((events.get(k).startTime.get(Calendar.HOUR_OF_DAY) < 10) ? "0" + events.get(k).startTime.get(Calendar.HOUR_OF_DAY) : events
							.get(k).startTime.get(Calendar.HOUR_OF_DAY))
							+ ""
							+ ((events.get(k).startTime.get(Calendar.MINUTE) < 10) ? "0" + events.get(k).startTime.get(Calendar.MINUTE)
									: events.get(k).startTime.get(Calendar.MINUTE))
							+ ""
							+ ((events.get(k).endTime == null) ? "" : ((events.get(k).endTime.get(Calendar.HOUR_OF_DAY) < 10) ? "0"
									+ events.get(k).endTime.get(Calendar.HOUR_OF_DAY) : events.get(k).endTime.get(Calendar.HOUR_OF_DAY))
									+ ""
									+ ((events.get(k).endTime.get(Calendar.MINUTE) < 10) ? "0" + events.get(k).endTime.get(Calendar.MINUTE)
											: events.get(k).endTime.get(Calendar.MINUTE))));
				}
				fin.close();

			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			System.out.println("Goodbye.");

			System.out.println("Select one of the following options: " + "[L]oad   [V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");

		}
		in.close();
	}

	public String printEventsForDay(Calendar c)
	{
		String toReturn = "";
		toReturn += arrayOfDays[c.get(Calendar.DAY_OF_WEEK) - 1];
		toReturn += " ";
		toReturn += arrayOfMonths[c.get(Calendar.MONTH)];
		toReturn += " ";
		toReturn += c.get(Calendar.DAY_OF_MONTH);
		toReturn += ", ";
		toReturn += c.get(Calendar.YEAR);
		toReturn += "\n";

		// for debugging
		String key1 = c.get(Calendar.YEAR) + "" + (((c.get(Calendar.MONTH) + 1) < 10) ? "0" + c.get(Calendar.MONTH) + 1 : (c.get(Calendar.MONTH) + 1)) + ""
				+ (((c.get(Calendar.DAY_OF_MONTH)) < 10) ? "0" + c.get(Calendar.DAY_OF_MONTH) : (c.get(Calendar.DAY_OF_MONTH))) + "";
		String key2 = c.get(Calendar.YEAR) + "" + (((c.get(Calendar.MONTH) + 1) < 10) ? "0" + c.get(Calendar.MONTH) + 1 : (c.get(Calendar.MONTH) + 1)) + ""
				+ (((c.get(Calendar.DAY_OF_MONTH) + 1) < 10) ? "0" + c.get(Calendar.DAY_OF_MONTH) + 1 : (c.get(Calendar.DAY_OF_MONTH) + 1)) + "";

		// use treemap's submap to find events in same day
		SortedMap<Long, Event> subMapStore = events.subMap(Long.parseLong(c.get(Calendar.YEAR) + ""
				+ (((c.get(Calendar.MONTH) + 1) < 10) ? "0" + (c.get(Calendar.MONTH) + 1) : (c.get(Calendar.MONTH) + 1)) + ""
				+ (((c.get(Calendar.DAY_OF_MONTH)) < 10) ? "0" + c.get(Calendar.DAY_OF_MONTH) : (c.get(Calendar.DAY_OF_MONTH))) + ""), 
				Long.parseLong(c.get(Calendar.YEAR) + ""
				+ (((c.get(Calendar.MONTH) + 1) < 10) ? "0" + (c.get(Calendar.MONTH) + 1) : (c.get(Calendar.MONTH) + 1)) + ""
				+ (((c.get(Calendar.DAY_OF_MONTH) + 1) < 10) ? "0" + (c.get(Calendar.DAY_OF_MONTH) + 1) : (c.get(Calendar.DAY_OF_MONTH) + 1)) + ""));

		// print events in the day

		for (Long k : subMapStore.keySet())
		{
			toReturn += subMapStore.get(k).title
					+ " "
					+ subMapStore.get(k).startTime.get(Calendar.HOUR_OF_DAY)
					+ ":"
					+ ((subMapStore.get(k).startTime.get(Calendar.MINUTE) < 10) ? "0" + subMapStore.get(k).startTime.get(Calendar.MINUTE)
							: subMapStore.get(k).startTime.get(Calendar.MINUTE))
					+ " "
					+ ((subMapStore.get(k).endTime == null) ? "" : "- "
							+ subMapStore.get(k).endTime.get(Calendar.HOUR_OF_DAY)
							+ ":"
							+ ((subMapStore.get(k).endTime.get(Calendar.MINUTE) < 10) ? "0" + subMapStore.get(k).endTime.get(Calendar.MINUTE) : subMapStore
									.get(k).endTime.get(Calendar.MINUTE)));
			toReturn += "\n";
		}
		return toReturn;
	}

	public void printMonth(Calendar c)
	{
		GregorianCalendar temp = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		System.out.println("      " + arrayOfMonths[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR));
		int[] month = new int[c.getActualMaximum(Calendar.DAY_OF_MONTH)];
		int i = temp.get(Calendar.DAY_OF_WEEK) - 1;
		for (int y = 0; y < temp.get(Calendar.DAY_OF_WEEK) - 1; y++)
		{
			System.out.printf("  0 ");
		}
		String key1 = "";
		String key2 = "";
		for (int x = 0; x < month.length; x++)
		{

			if (arrayOfDays[i].toString().equals("Sunday"))
			{
				System.out.println();
			}
			// for debugging
			key1 = (((c.get(Calendar.MONTH) + 1) < 10) ? "0" + (c.get(Calendar.MONTH) + 1) : (c.get(Calendar.MONTH) + 1)) + ""
					+ (((x + 1) < 10) ? "0" + (x + 1) : (x + 1)) + "" + c.get(Calendar.YEAR);
			key2 = (((c.get(Calendar.MONTH) + 1) < 10) ? "0" + (c.get(Calendar.MONTH) + 1) : (c.get(Calendar.MONTH) + 1)) + ""
					+ (((x + 1) < 10) ? "0" + (x + 2) : (x + 2)) + "" + c.get(Calendar.YEAR);
			// checks if the date has an event
			SortedMap<Long, Event> subMapStore = events.subMap(Long.parseLong(c.get(Calendar.YEAR) + ""
					+ (((c.get(Calendar.MONTH) + 1) < 10) ? "0" + (c.get(Calendar.MONTH) + 1) : (c.get(Calendar.MONTH) + 1)) + ""
					+ (((x + 1) < 10) ? "0" + (x + 1) : (x + 1)) +"00000000"),
					Long.parseLong(c.get(Calendar.YEAR) + "" + (((c.get(Calendar.MONTH) + 1) < 10) ? "0" + (c.get(Calendar.MONTH) + 1) : (c.get(Calendar.MONTH) + 1)) + ""
							+ (((x + 2) < 10) ? "0" + (x + 2) : (x + 2)) +"00000000"));

			if ((initalScreen == true) && ((x + 1) < 10) && (c.get(Calendar.DAY_OF_MONTH) == (x + 1)))
			{
				System.out.print(" [" + (x + 1) + "]");
				initalScreen = false;
			} else if ((initalScreen == true) && (c.get(Calendar.DAY_OF_MONTH) == (x + 1)))
			{
				System.out.print("[" + (x + 1) + "]");
				initalScreen = false;
			}

			else
			{
				if (!(subMapStore.isEmpty()))
				{
					if ((x + 1) < 10)
					{
						System.out.print(" [" + (x + 1) + "]");
					} else
					{
						System.out.print("[" + (x + 1) + "]");
					}

				} else if ((x + 1) < 10)
				{
					System.out.print("  " + (x + 1) + " ");

				} else
				{
					System.out.print(" " + (x + 1) + " ");
				}
			}
			if (i == 6)
			{
				i = 0;
			} else
			{
				i++;
			}
		}

		System.out.println();

	}
	/**
	 * Creates an event and stores it in the events array.
	 * @param title
	 * @param date
	 * @param T
	 * @return true if successful, false if not
	 */
	public boolean createEvent(String title, String date, String T)
	{
		date = date.replaceAll("[\\W]", "");

		int m = Integer.parseInt(date.substring(0, 2));
		int d = Integer.parseInt(date.substring(2, 4));
		int y = Integer.parseInt(date.substring(4, 8));

		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();

		T = T.replaceAll("[\\W]", "");
		T = T.trim();
		if (T.length() <= 4)
		{	T=T+"0000";
			
			startTime.set(y, m - 1, d, Integer.parseInt(T.substring(0, 2)), Integer.parseInt(T.substring(2, 4)));
			endTime = null;
		} else
		{
			
			startTime.set(y, m - 1, d, Integer.parseInt(T.substring(0, 2)), Integer.parseInt(T.substring(2, 4)));
			endTime.set(y, m - 1, d, Integer.parseInt(T.substring(4, 6)), Integer.parseInt(T.substring(6, 8)));
		}
		//testing
		System.out.println("from "+Long.parseLong(
				cal.get(Calendar.YEAR) + "" + (((cal.get(Calendar.MONTH) + 1) < 10) ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)) + ""
						+ (((cal.get(Calendar.DAY_OF_MONTH)) < 10) ? "0" + cal.get(Calendar.DAY_OF_MONTH) : (cal.get(Calendar.DAY_OF_MONTH))) + ""+T.substring(0, 4)+"0000"));
		System.out.println("to   "+cal.get(Calendar.YEAR) + "" + (((cal.get(Calendar.MONTH) + 1) < 10) ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)) + ""
				+ (((cal.get(Calendar.DAY_OF_MONTH) + 1) < 10) ? "0" + (cal.get(Calendar.DAY_OF_MONTH)) : (cal.get(Calendar.DAY_OF_MONTH)))+T.substring(0, 8)
				+ "");
		System.out.println(T);
		// use treemap's submap to find events that have time conflicts in same day
		SortedMap<Long, Event> subMapStore = events.subMap(Long.parseLong(
				cal.get(Calendar.YEAR) + "" + (((cal.get(Calendar.MONTH) + 1) < 10) ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)) + ""
						+ (((cal.get(Calendar.DAY_OF_MONTH)) < 10) ? "0" + cal.get(Calendar.DAY_OF_MONTH) : (cal.get(Calendar.DAY_OF_MONTH))) + ""+T.substring(0, 4)+"0000"),
				Long.parseLong(cal.get(Calendar.YEAR) + "" + (((cal.get(Calendar.MONTH) + 1) < 10) ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)) + ""
						+ (((cal.get(Calendar.DAY_OF_MONTH) ) < 10) ? "0" + (cal.get(Calendar.DAY_OF_MONTH) ) : (cal.get(Calendar.DAY_OF_MONTH)))+T.substring(4, 8)+"0000"
						+ ""));
		if (!(subMapStore.isEmpty()))
		{
			Long[] keys = new Long[subMapStore.size()];
			subMapStore.keySet().toArray(keys);
			for (int x = keys.length - 1; x >= 0; x--)
			{	Long k = keys[x];
				System.out.println(k);
			}
			System.out.println(date.substring(4, 8) + date.substring(0, 2) + date.substring(2, 4) + T.substring(0, 8));
			return false;
		}
		Event eStore = new Event(title, m, d, y, startTime, endTime);
		// adds to events while creating key
		
		events.put(Long.parseLong(date.substring(4, 8) + date.substring(0, 2) + date.substring(2, 4) + T.substring(0, 8)), eStore);
		return true;
	}

	public void createEvent(Scanner in, String userIn)
	{
		String title = "";
		String date = "";
		String T = "";

		if (userIn.equals("L"))
		{
			title = in.nextLine();
			date = in.nextLine();
			T = in.nextLine();
		} else
		{
			System.out.println("Please enter a title, date, starting time, and ending time.");
			System.out.println("Enter the title.");
			// relies on title
			System.out.println("Enter the date MM/DD/YYYY.");
			// relies on date
			System.out.println("Enter the starting time and ending time.");
			// relies on T
		}
		date = date.replaceAll("[\\W]", "");

		int m = Integer.parseInt(date.substring(0, 2));
		int d = Integer.parseInt(date.substring(2, 4));
		int y = Integer.parseInt(date.substring(4, 8));

		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();

		T = T.replaceAll("[\\W]", "");
		T = T.trim();
		if (T.length() <= 4)
		{	T=T+"0000";
			while ((Integer.parseInt(T.substring(0, 2)) > 24) || (Integer.parseInt(T.substring(2, 4)) > 60) || (Integer.parseInt(T.substring(0, 2)) < 0)
					|| (Integer.parseInt(T.substring(2, 4)) < 0))
			{
				System.out.println("Please enter valid starting and/or ending times.");
				T = in.nextLine();
				T = T.replaceAll("[\\W]", "");
			}
			startTime.set(y, m - 1, d, Integer.parseInt(T.substring(0, 2)), Integer.parseInt(T.substring(2, 4)));
			endTime = null;
		} else
		{
			while ((Integer.parseInt(T.substring(0, 2)) > 24) || (Integer.parseInt(T.substring(2, 4)) > 60) || (Integer.parseInt(T.substring(0, 2)) < 0)
					|| (Integer.parseInt(T.substring(2, 4)) < 0) || (Integer.parseInt(T.substring(4, 6)) > 24) || (Integer.parseInt(T.substring(6, 8)) > 60)
					|| (Integer.parseInt(T.substring(4, 6)) < 0) || (Integer.parseInt(T.substring(6, 8)) < 0))
			{
				System.out.println("Please enter valid starting and/or ending times.");
				T = in.nextLine();
				T = T.replaceAll("[\\W]", "");
			}
			startTime.set(y, m - 1, d, Integer.parseInt(T.substring(0, 2)), Integer.parseInt(T.substring(2, 4)));
			endTime.set(y, m - 1, d, Integer.parseInt(T.substring(4, 6)), Integer.parseInt(T.substring(6, 8)));
		}

		Event eStore = new Event(title, m, d, y, startTime, endTime);
		// adds to events while creating key
		
		events.put(Long.parseLong(date.substring(4, 8) + date.substring(0, 2) + date.substring(2, 4) + T.substring(0, 8)), eStore);

	}

}