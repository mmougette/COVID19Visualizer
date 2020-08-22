package application;

////////////////////////////////////////////////////////////
//
//Title: COVID19 Visualizer
//
//Files: Country.java, Main.java
//
//Author: Maxwell Mougette, mougette@wisc.edu
//
///////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.List;

/**
 * store information about the number of infections in a given country
 * 
 * @author mougette
 *
 */
public class Country {
  public String name; // Name of Country
  public int population;
  public List<DayNode> days; // List of days to be included in the graph

  /**
   * Initializes an instance of the Country class with its name and list of cases per day
   * 
   * @param name - name of country whose data is to be stored
   */
  public Country(String name, int population) {
    this.name = name;
    this.population = population;
    days = new ArrayList<DayNode>();
  }

  /**
   * Consists of the date and number of cases per day for any given country
   * 
   * @author mougette
   *
   */
  public class DayNode {
    public String date; // date of a particular day
    public int numCases; // number of covid cases on that day
    public int numDeaths; // number of covid deaths on that day

    /**
     * Initializes a DayNode instance with its date and number of cases
     * 
     * @param date
     * @param numCases
     */
    DayNode(String date, int numCases) {
      this.date = date;
      this.numCases = numCases;
      this.numDeaths = 0; // TempValue
    }
    
    public void addDeaths(int numDeaths) {
      this.numDeaths = numDeaths;
    }
  }

  /**
   * adds a day to the list of days with infections, from oldest to the most recent
   * 
   * @param day - day to be added
   * @throws NullPointerException     - thrown if day is null
   * @throws IllegalArgumentException - thrown if day is not null but argument is invalid
   */
  public void addDay(DayNode day)
      throws NullPointerException, IllegalArgumentException {
    int addIndex = days.size(); // index at which day is to be added

    if (day == null) { // null case
      throw new NullPointerException("day is invalid");
    }

    if (days.size() == 0) { // adds first element of the list
      days.add(day);
    } else {
      for (int i = 0; i < days.size(); ++i) { // adds any element other than the
                                              // first at the right index
        if (daysComparator(day, days.get(i)) == 0) {
          throw new IllegalArgumentException("Days cannot be repeated");
        } else if (daysComparator(day, days.get(i)) < 0) {
          addIndex = i;
          break;
        } else {
          continue;
        }
      }
      days.add(addIndex, day); // adds day at right index
    }
  }
  
  /**
   * Add a day without a DayNode
   * @param date - Date to be added
   * @param numCases - Number of cases on said day
   */
  public void addDay(String date, int numCases) {
    DayNode n = new DayNode(date, numCases);
    addDay(n);
  }

  /**
   * Compares dates and returns a value based on which day is older
   * 
   * @param day1 - 1st day to be compared
   * @param day2 - 2nd day to be compared
   * @return -> -1, if day1 is older than day2 0, if day1 and day2 are the same 1, if day1 is more
   *         recently than day2
   * @throws IllegalArgumentException - thrown if arguments are not valid
   */
  private int daysComparator(DayNode day1, DayNode day2)
      throws IllegalArgumentException {

    // Splits date from "mm/dd/yyyy" into its indivdual parts
    String[] day1Details = day1.date.split("/");
    String[] day2Details = day2.date.split("/");

    if (day1Details.length != 3 || day2Details.length != 3) { // throws exception
      throw new IllegalArgumentException("Days cannot be compared");
    }

    // Switches postions of mm and dd within the split array for ease of comparison
    String temp1 = day1Details[0];
    day1Details[0] = day1Details[1];
    day1Details[1] = temp1;

    String temp2 = day2Details[0];
    day2Details[0] = day2Details[1];
    day2Details[1] = temp2;

    for (int i = 2; i > -1; --i) { // Compares the two day by testing year first,
                                   // month second and day third
      if (Integer.parseInt(day1Details[i].trim()) == Integer
          .parseInt(day2Details[i].trim())) {
        continue;
      } else if (Integer.parseInt(day1Details[i].trim()) < Integer
          .parseInt(day2Details[i].trim())) {
        return -1;
      } else if (Integer.parseInt(day1Details[i].trim()) > Integer
          .parseInt(day2Details[i].trim())) {
        return 1;
      }
    }

    return 0;
  }

  /**
   * removes a day from the list
   * 
   * @param day - day to be removed
   */
  public void removeDay(DayNode day) throws NullPointerException {
    if (day == null) {
      throw new NullPointerException("day is invalid");
    }

    days.remove(day);
  }

  /**
   * Getter for the days list
   * 
   * @return - List containing the days with infections for a country
   */
  public List<DayNode> getDays() {
    return days;
  }
  
  /**
   * Getter for the days list
   * 
   * @return - name of country
   */
  public String getName() {
    return name;
  }
}
