
/*
   * This class is responsible for representing all cell values that are dates. 
   * For a cell to be a date it has to have an input of 2 digits then a slash, two 
   * more digits then a slash, and finally 4(or 2) digits (representation of a year). 
   * This class takes in the date cell values and changes the display to have the month 
   * in letters, then the days then the year with a comma before it. It will use date 
   * formatting classes within Java’s files to determine which numbers match up with 
   * the corresponding month. 
*/

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

public class DateCell extends Cell {
   
   // This method is responsible for the standard output when 
   // calling a DateCell object, and calls getDisplay() which
   // writes out the day in a formal formatting manner
   public String toString() {
      return getDisplay();
   }
   
   // This method is responsible for converting the traditional expression from the console
   // and formatting it into a formal month date, year style with actual words. Ex: Jan 8, 2021
   // It uses the SimpleDateFormat class to do this and can take in multiple original expressions:
   // mm/dd/yyyy or mm/dd/yy
   public String getDisplay() {
         String singlePattern = "MMM d, yyyy";
         String doublePattern = "MMM dd, yyyy"; 
         String expression = getExpression();
         
         // if the day count is more than 10 then the format looks different
         if(multipleDays()) {
            SimpleDateFormat dateString = new SimpleDateFormat(doublePattern);
            Date date = new Date(expression);
            String dateFormat = dateString.format(date);
            return dateFormat;
          
         // if the day count is less than 10
         } else {
            SimpleDateFormat dateString = new SimpleDateFormat(singlePattern);
            Date date = new Date(expression);
            String dateFormat = dateString.format(date); 
            return dateFormat;
         }
   }
   
   // A helper method of getDisplay() that determines if the expression (original date from console)
   // has a day value within the date that is greater than 10. When 2 numbers represent that day within
   // the date, the formatting is different.
   public boolean multipleDays() {
      String expression = getExpression();
      String newDate = expression.substring(expression.indexOf("/") + 1);
      newDate = newDate.substring(0, newDate.indexOf("/"));
      if(newDate.charAt(0) == '0' || newDate.length() == 1) {
         return false;
      } else {
         return true;
      }
   }
}     