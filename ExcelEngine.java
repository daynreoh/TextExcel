// Dayn Reoh
// Period 3
// Text Excel Project

import java.io.*;
import java.util.*;

/**
 * The ExcelEngine is the main implementation of the TextExcel program. It
 * implements the requirements established by ExcelBase. Most methods will be
 * private as the only public methods necessary are to processCommand(). This
 * class will handle certain commands that the Grid does not: help and
 * load file.
 */
public class ExcelEngine extends ExcelBase {

   public static void main(String args[]) {

      // Create our Grid object and assign it to the GridBase
      // static field so that we can reference it later on.
      GridBase.grid = new Grid();
      ExcelEngine engine = new ExcelEngine();

      engine.runInputLoop();
   }

   /**
    * Method processCommand.<p>
    * 
    * This method will parse a line that contains a command. It will
    * delegate the command to the Grid if the Grid should handle it. It will call
    * the appropriate method to handle the command.<p>
    *
    * ALL Commands will be distributed to the appropriate methods.
    * Here are the Checkpoint #1 commands: <ul>
    *  <li><b>help</b> : "provides help to the user" </li>
    *  <li><b>print</b> : "returns a string of the printed grid"</li> 
    *  <li><b>rows</b> : "returns the number of rows currently in the 
    *           grid"</li>
    *  <li><b>rows = 5</b> : "resizes the grid to have 5 rows. The
    *           grid contents will be cleared."</li>
    *  <li><b>cols</b> : "returns the number of columns currently in the 
    *           grid"</li>
    *  <li><b>cols = 3</b> : "resizes the grid to have 3 columns. The
    *           grid contents will be cleared."</li>
    *  <li><b>width</b> : "returns the width of an individual cell that
    *           is used when displaying the grid contents."</li>
    *  <li><b>width = 12</b> : "resizes the width of a cell to be 12
    *           characters wide when printing the grid."</li>
    *  <li><b>load file1.txt</b> : "opens the file and processes all
    *           commands in it"</li></ul>
    * 
    * @param command A command to be processed
    * @return The result of the command to be printed.
    */
   public String processCommand(String command) {
      String result = null;

      // TODO: handle help command here...
      if(command.equalsIgnoreCase("help")) {
         return 
            " This is a program that produces a table that has functions\n" + 
            " similar to the Excel application from Office. There are a variety of\n" + 
            " commands that you can enter into the console when running this program.\n" +
            " Print will print out the version of the table that you created, or the\n" +
            " default version if this is your first time running. Rows = #, cols = #,\n" +
            " and width = # all are commands that set the rows, columns, and width of\n" +
            " cells to a customized number based on your preference. If you re-print\n" +
            " your table after setting these values, the table will apply those values.\n" +
            " Please refer to the book button within the projects menu if you are\n" + 
            " struggling with understanding the tasks and behaviors within this project.\n" + 
            " Thanks and goodbye.";
      } else if (command.contains("load")) {
         Scanner parse = new Scanner(command);
         String filename = "";
         while(parse.hasNext()) {
            String next = parse.next();
            if(!next.equals("load")) {
               filename = next;
            }
         }
         try {      
            return loadFile(filename); 
         } catch (FileNotFoundException e) {
            return "Could not find file: ";   
         }
      }

      // handle all file related commands here
      result = handleFileCommands(command);

      // The method handleFileCommands will return null
      // if the input was NOT a command that it handled.
      // if result == null, the command wasn't a file command.
      //     let's see if it is a grid command. Delegate
      //     the command to the Grid object to see if it can handle it.
      if (result == null && GridBase.grid != null) {
         // The GridBase class has a static field, grid, of type GridBase.
         // Ask the grid object to process the command. 
         result = GridBase.grid.processCommand(command);
      }

      // the command is still not handled
      return result;
   }
   
   public String loadFile(String filename) throws FileNotFoundException {
      File file = new File(filename);
      Scanner parse = new Scanner(file);
      while(parse.hasNextLine()) {
         processCommand(parse.nextLine());   
      }
      if(file.exists()) {
         return "File loaded successfully";
      } 
      return "no idk";
   }

   /**
    * Method handleFileCommands.<p>
    * 
    * This method will handle commands related to file processing.<p>
    *
    * Required Commands to handle are: <ul>
    *  <li><b>load [filename]</b> : "opens the file and processes all
    *           commands in it"</li>
    *  <li><b>save [filename]</b> : "gets all properties and values from
    *           the grid and saves them as commands in the file"</li></ul>
    *  @param command The full command to be handled.
    *  @return null if the command was not handled.<br>
    *          A String to print to the user if the command was handled.
    */
   private String handleFileCommands(String command) {
      // TODO: implement me. Determine if the command is a file command.
      // If it is a file command, call the appropriate method. 
      return null;
   }

   /**
    * Method loadFromFile. 
    *
    * This will process the command: load {filename}<p>
    *
    * Call processCommand() for every line in the file. During file processing,
    * there should be no output.
    * @param filename The name/path to the file
    * @return Success or Failure message to display to the user
    */
   private String loadFromFile(String filename) {

      File file = new File(filename);
      String result = "File loaded successfully";
      try {
         // load the file and process the commands in the file
         Scanner reader = new Scanner(file);
         
         // TODO: Student is to read all the commands in the file
         // and process the command. Then, close the Scanner.
         // You may choose to print the result of each command
         // as you process it.
      } catch (FileNotFoundException e) {
         result = "Could not find file: " + file.getAbsolutePath();
      }

      return result;
   }

}
