import java.io.*;
import java.util.*;

/**
 * The Grid class will hold all the cells. It allows access to the cells via the
 * public methods. It will create a display String for the whole grid and process
 * many commands that update the cells. These command will include
 * sorting a range of cells and saving the grid contents to a file.
 *
 */
public class Grid extends GridBase {

   // These are called instance fields.
   // They are scoped to the instance of this Grid object.
   // Use them to keep track of the count of columns, rows and cell width.
   // They are initialized to the prescribed default values.
   // Notice that there is no "static" here.
   private Cell[][] matrix;
   private int colCount = 7;
   private int rowCount = 10;
   private int cellWidth = 9;
   
   // This is the only constructor because we only create a Grid object once. When
   // we create the grid object, we want to also create the matrix that is responsible for
   // holding the values we need to print to our grid.
   public Grid() {
      createMatrix();
   }

   /**
    * This processes a user command.
    * <p>
    * Checkpoint #1 commands are:
    * <ul>
    * <li><b>[cell] = [expression]</b> : "set the cell's expression, which may simply
    *  be a value such as 5, or it may more complicated such as: 'a3 * b3'."</li>
    * <li><b>[cell]</b> : "get the cell's expression, NOT the cell's value"</li>
    * <li><b>value [cell]</b> : "get the cell value"</li>
    * <li><b>print</b> : "render a text based version of the matrix"</li>
    * <li><b>clear</b> : "empty out the entire matrix"</li>
    * <li><b>clear [cell]</b> : "empty out a single cell"</li>
    * <li><b>sorta [range]</b> : "sort the range in ascending order"</li>
    * <li><b>sortd [range]</b> : "sort the range in descending order"</li>
    * <li><b>width = [value]</b> : "set the cell width"</li>
    * <li><b>width</b> : "get the cell width"</li>
    * <li><b>rows = [value]</b> : "set the row count"</li>
    * <li><b>cols = [value]</b> : "set the column count"</li>
    * <li><b>rows</b> : "get the row count"</li>
    * <li><b>cols</b> : "get the column count"</li>
    * </ul>
    * 
    * @param command The command to be processed.
    * @return The results of the command as a string.
    */
    
   // This is the main method involved in processing all commands within the TextExcel project. It takes the 
   // input from the console and determines what to do with the information based on key words that exist within
   // each command call.
   public String processCommand(String command) {
      String result = null;
      command = command.toLowerCase();
     
      if(command.contains("=")) { 
         return setInstanceFields(command);
      } else if(command.contains("expr") || command.contains("display") || command.contains("value")) {
         return outputDisplayCommands(command);
      } else if(command.contains("sum") || command.contains("avg")) {
         return processSumAndAvg(command);
      } else if(command.length() < 4) {
         return specialExprValue(command);
      } else if(command.equals("print")) { 
         return renderMatrix();
      } else if(command.contains("clear")) {
         return determineClear(command); 
      } else if(command.contains("save")) {
         
         // try catch prevents the FileNotFound exception from throwing and allows
         // the program to test if the command is unknown or malformed since this else if statement
         // isn't 100% going to return a value.
         try {
            return beginFileSave(command);
         } catch (FileNotFoundException e) {
            System.out.println("This file cannot be found");
         }
      } else {
         return getValues(command);
      }
 
      if (result == null)
         result = "unknown or malformed command: " + command;
 
      return result;
   }
   
   // This is a helper method of the save function. It takes in the command and splits it so that it 
   // can create a new PrintStream object with the file name as it's parameter. It calls saveToFile to print
   // out all the necessary values within the new PrintStream file.
   public String beginFileSave(String command) throws FileNotFoundException {
      String[] file = command.split(" ");
      PrintStream writer = new PrintStream(file[1]);
      return saveToFile(writer);
   }
   
   // This is another helper method of the save function. Takes in the Printstream object and writes all the necessary values
   // that need to be saved to the new file.
   public String saveToFile(PrintStream writer) throws FileNotFoundException {
      writer.printf("rows = %d\n", rowCount);
      writer.printf("cols = %d\n", colCount);
      writer.printf("width = %d\n", cellWidth);
      for(int rows = 0; rows < matrix.length; rows++) {
         
         for(int cols = 0; cols < matrix[0].length; cols++) {
            
            if(!matrix[rows][cols].getExpression().equals(""))
            writer.println(getCellLocation(rows, cols) + " = " + 
               matrix[rows][cols].getExpression() + " ");
         }
      }
      return "";
   }
   
   // The helper method for the clear function. It handles both clear and clear an individual value functions.
   // It tests the command length to determine this then creates a new Cell value when clearing an individual cell. 
   public String determineClear(String command) {
      String[] tokens = command.split(" ");
      if(tokens.length == 1) {
         createMatrix();
         return "";
      } else {
         int rows = getRowValue(tokens[1]);
         int cols = getColumnValue(tokens[1]);
         matrix[rows][cols] = new Cell();
         return "";
      }
   }
   
   // A helper method that takes in the converted row and column from our matrix function
   // and converts them to the readable version that was initially inputted into the console. 
   // "a1" or "b3" instead of "04" "35"
   public String getCellLocation(int row, int col) {
      String alphabet = "abcdefghijklmnopqrstuvwxyz";
      String columns = "" + alphabet.charAt(col);
      String rows = ""  + (row + 1);
      return columns + rows;
   }
   
   // This method handles all potential return statements when calling the special expression
   // value of just the cellLocation, "a1". It uses if statements and specific attributes of each type of 
   // cell to determine the value that needs to be returned based on the Cell type at this location
   public String specialExprValue(String command) {
      int rowNumber = getRowValue(command);
      int colNumber = getColumnValue(command);
      
      // text cell
      if(matrix[rowNumber][colNumber].getValue() == 0.0) {
         
         // date cell
         if(matrix[rowNumber][colNumber].getExpression().contains("/")) {
            return matrix[rowNumber][colNumber].getExpression();
            
         // text cell   
         } else {
            return "\"" +  matrix[rowNumber][colNumber] + "\"";
         }   
         
      // Sum or avg
      } else if(matrix[rowNumber][colNumber].getExpression().contains("sum") ||
         matrix[rowNumber][colNumber].getExpression().contains("avg")) {
         return matrix[rowNumber][colNumber].getExpression();
         
      // cell with other cell's locations called
      } else if(isCellCall(command)) {
         return matrix[rowNumber][colNumber].getExpression();   
      } else {
         return "" + (int) matrix[rowNumber][colNumber].getValue();
      }
   }
   
   // A helper method to the expr command and expr shorthand command, "a1". This handles the final scenario
   // that determines if the expression of the cell has a call to another cell type within it's contents. If it
   // does then it has a different expression return than a normal NumberCell.
   public boolean isCellCall(String command) {
      int rowNumber = getRowValue(command);
      int colNumber = getColumnValue(command);
      boolean flag = false;
      String expression = matrix[rowNumber][colNumber].getExpression();
      for(int index = 0; index < expression.length() - 1; index++) {
         
         // if there is a spot within the command where the indices go from letter to number
         // this represents a cellLocation, "a1" b3" "d8"
         if(Character.isLetter(expression.charAt(index))) {
            if(Character.isDigit(expression.charAt(index + 1))) {
               flag = true;
            }   
         }
      }
      return flag;
   }
   
   // This method handles all the commands involved with getting the instance field values
   // It accesses the values by calling this.(Instance Field) since they're all private. 
   public String getValues(String command) {
      if(command.equalsIgnoreCase("width")) {
         return "" + this.cellWidth;
      } else if(command.equalsIgnoreCase("cols")) {
         return "" + this.colCount;
      } else if(command.equalsIgnoreCase("rows")) {
         return "" + this.rowCount;
      } else {
         return "";
      }
   
   }
   
   // This is a helper method to the processCommand method that has determined that the command
   // contains expr, display, or value. From there a variety of methods are called that handles the 
   // functionality of each.
   public String outputDisplayCommands(String command) {
      if(command.contains("expr")) {
         return getExpressionValues(command);
      } else if(command.contains("display")) {
         return getDisplayValues(command);
      } else {
         return getValueValues(command);   
      }     
   }
   
   // Method that returns the value associated with the display command based on the Cell type at
   // the specific location. If the value of the matix is zero (textcell), then the toString() method returns
   // the correct display. If not, then the getValue() command handles all the numbercell cases
   public String getDisplayValues(String command) {
      String[] tokens = command.split(" ");
      int rowNumber = getRowValue(tokens[1]);
      int colNumber = getColumnValue(tokens[1]);
      if(matrix[rowNumber][colNumber].getValue() == 0.0) {
         return matrix[rowNumber][colNumber].toString();           
      } else {
         return "" + matrix[rowNumber][colNumber];       
      }
   }
   
   // This method handles all the expr calls and the different values that could be returned based
   // on the Cell type. It uses specific attributes of each Cell type to determine what needs to be returned.
   public String getExpressionValues(String command) {
      String[] tokens = command.split(" ");
      int rowNumber = getRowValue(tokens[1]);
      int colNumber = getColumnValue(tokens[1]);
      
      // text cell
      if(matrix[rowNumber][colNumber].getValue() == 0.0) {
         
         // date cell
         if(matrix[rowNumber][colNumber].getExpression().contains("/")) {
            return matrix[rowNumber][colNumber].getExpression();
         
         // text cell
         } else {
            return "\"" +  matrix[rowNumber][colNumber] + "\"";
         }
         
      // sum or avg
      } else if(matrix[rowNumber][colNumber].getExpression().contains("sum") ||
         matrix[rowNumber][colNumber].getExpression().contains("avg") ) {
         return matrix[rowNumber][colNumber].getExpression();
         
      // is a call to another cell location
      } else if(isCellCall(command)) {
         return matrix[rowNumber][colNumber].getExpression();   
      
      // number cell
      } else {
         return "" + (int) Double.parseDouble(matrix[rowNumber][colNumber].toString());
      } 
   
   }
   
   // Method that handles all the value calls. It uses specific value-based attributes that
   // separates the different Cell types. If the specific Cell called is a numbercell, then the value
   // is getValue(), but if not then the value is 0.0
   public String getValueValues(String command) {
      boolean flag = false;
      String[] tokens = command.split(" ");
      int rowNumber = getRowValue(tokens[1]);
      int colNumber = getColumnValue(tokens[1]);
      String arrayExpression = "" + matrix[rowNumber][colNumber]; 
      for(int index = 0; index < arrayExpression.length(); index++) {
         if(Character.isDigit(arrayExpression.charAt(index))) {
            flag = true;
         }      
      }
      
      // true means the Cell is a NumberCell, false means TextCell or DateCell
      if(flag) {
         return "" + matrix[rowNumber][colNumber].getValue();
      } else {
         return "0.0";
      } 
   }
   
   // this is a helper method of ProcessCommand that is called when there is an
   // equal sign in the input from console. If rows, cols, or width also wasnt apart of the input
   // then this method sets the cell expression wihtin the matrix
   public String setInstanceFields(String command) {
      if(command.contains("rows")) {
         setRows(command);
         return "" + this.rowCount;
      } else if(command.contains("cols")) {
         setCols(command); 
         return "" + this.colCount;  
      } else if(command.contains("width")) {
         setWidth(command);
         return "" + this.cellWidth;   
      } else {
      
         // sets the cell expressions
         return setCellExpression(command);
      } 
   }
   
   // This method is responsible for separating out the expression of the input command from console
   // and determining if the specific expression applies to a NumberCell, TextCell, or DateCell. Once this is 
   // determined specific methods are called to create objects of that type and insert them into matrix.
   public String setCellExpression(String command) {
      boolean flag = true;
      int sum = 0;
      String[] tokens = command.split(" ");
      String expression = tokens[2];
      
      // Finds the true expression from the input
      for(int element = 3; element < tokens.length; element++) {
         expression += " " + tokens[element];
      }
      
      // gets the cellLocation in terms of matrix
      int rowNumber = getRowValue(tokens[0]);
      int colNumber = getColumnValue(tokens[0]);
      char[] expressionChars = expression.toCharArray();
      for(char characters: expressionChars) {
         if(Character.isDigit(characters)) {
            flag = false;  
         } else if(characters == '/') {
            sum++; 
            
         // handles special TextCell case, where there are numbers in the cell
         // but if there are quotes in the start and beginning then it is a TextCell  
         } else if(characters == '\"') {
            flag = true;
         }
      }
      
      if(flag) {
         setTextCell(expression, rowNumber, colNumber); 
      } else if(!flag && sum == 2) {
         setDateCell(expression, rowNumber, colNumber);
      } else { 
         setNumberCell(expression, rowNumber, colNumber);
      }
      return "";
   }
   
   // This method sets the specific matrix location with a TextCell. This method is called
   // once it is determined that this specific location belongs to a TextCell. It creates a new TextCell object
   // and assigns the expression from the command into the setExpression() method that belongs to the Cell class.
   public void setTextCell(String expression, int row, int col) {
      try {
         TextCell text = new TextCell();
         text.setExpression(expression);
         matrix[row][col] = text;
      } catch (NullPointerException e) {
         System.out.println("you are trying to fill a null value");   
      }
   }
   
   // This method sets the specific matrix location with a DateCell. This is called because the 
   // specific matrix location is determined to be of type DateCell. It creates a new DateCell object and sets
   // the expression of the Cell class by using the expression from the console input.
   public void setDateCell(String expression, int row, int col) {
      try {
         DateCell date = new DateCell();
         date.setExpression(expression);
         matrix[row][col] = date;
      } catch (NullPointerException e) {
         System.out.println("you are trying to fill a null value");
      }
   }
   
   // This method sets the specific matrix location with a NumberCell. This is called because the
   // specific matrix location is determined to be of type NumberCell. It creates a new NumberCell object and inserts it
   // at the specific matrix location. 
   public void setNumberCell(String expression, int row, int col) {
      try {
         NumberCell number = new NumberCell();
         number.setExpression(expression);
         matrix[row][col] = number;
      } catch (NullPointerException e) {
         System.out.println("you are trying to fill a null value");
      }
   }
   
   // This method allows the instance field rowCount to be changed. If the desired
   // value is greater than the instance field, then a new matrix needs to be
   // created.
   public void setRows(String command) {
      Scanner parse = new Scanner(command);
      while(!parse.hasNextInt()) {
         parse.next();
      } 
      int rowCount = parse.nextInt();
      this.rowCount = rowCount; 
      
      createMatrix();
   }
   
   // This method allows the instance field colCount to be changed. If the desired
   // value is greater than the instance field, then a new matrix needs to be
   // created.
   public void setCols(String command) {
      Scanner input = new Scanner(command);
      while(!input.hasNextInt()) {
         input.next();
      } 
      int colCount = input.nextInt();
      this.colCount = colCount;
      
      createMatrix();
   }  
   
   // This method allows the instance field cellWidth to be changed
   // from the console. 
   public void setWidth(String command) {
      Scanner input = new Scanner(command);
      while(!input.hasNextInt()) {
         input.next();
      } 
      int cellWidth = input.nextInt();
      this.cellWidth = cellWidth;
   }
   
   // This method is the main method responsible for printing and 
   // creating the formatting involved in the "print" command. This is 
   // the direct method called from "print"
   public String renderMatrix() {
     
      String grid = "";
      
      // prints out the columns 
      grid += getColumnHeaders();
      
      // prints out the rows
      grid += getRows();
      return grid;
   }
   
   // This method is responsible for creating the formatting
   // for the rows of the matrix based on the rowCount instance field.
   public String getRows() {
      String temporary = "";
      
      for(int row = 1; row <= rowCount; row++) {
         temporary += addGridLines(); 
         
         if(row < 10) {
            temporary += addCellSpaces(2) + row + " |";
         } else {
            temporary += addCellSpaces(1) + row + " |";
         }
         
         for(int column = 0; column < colCount; column++) {
         
            // where we add in blank spaces for each row 
            String value = matrix[row-1][column] + "";
            int remainingSpaces = cellWidth - value.length();
            
            // if the cell value is larger than the cell width, this truncates the 
            // cell value
            if(remainingSpaces < 0) {
               value = value.substring(0, cellWidth);
            }
            temporary += addCellSpaces(remainingSpaces) + value + "|";
         }
         temporary += "\n";
      }
      temporary += addGridLines();
      return temporary;
   } 
   
   // This method is responsible for creating the formatting 
   // for the columns and headers of the matrix based on the colCount
   // instance field.
   public String getColumnHeaders() {
      String temporary = "";
      String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";  
       
      // the column for numbers is always going to be 4
      temporary += "    |";
   
      for(int column = 0; column < colCount; column++) {
         String spaces = addCellSpaces(cellWidth/2);
         String evenSpaces = addCellSpaces(cellWidth/2 - 1);
         if(cellWidth % 2 == 0) {
            temporary += spaces + alphabet.charAt(column) + evenSpaces + "|";
         } else {
            temporary += spaces + alphabet.charAt(column) + spaces + "|";
         }
      }
      temporary += "\n";
      return temporary;
   }
   
   // this is a matrix formatting helper method that creates the lines
   // that separate each of the rows. It uses the column count and cell width
   // to determine how long it needs to be and how many "-" in between "+"
   public String addGridLines() {
      String line = "----+";
      
      // deals with the spaces for the letters columns
      for(int width = 0; width < colCount; width++) {
         
         for(int spaces = 0; spaces < cellWidth; spaces++) {
            line += "-";
         }
         line += "+";
      }
      line += "\n";
      return line;
   }
   
   // This is a helper method for the matrix formatting that
   // adds spaces within the cell based on how many you need in a given scenario,
   // denoted by "tester"
   public String addCellSpaces(int tester) {
      String cellSpaces = "";
      for(int spaces = 0; spaces < tester; spaces++) {
         cellSpaces += " ";   
      }
      return cellSpaces;
   }
   
   // This method instantiates the matrix, all of type cell, and 
   // allows for the rest of the values to be inserted into the array.
   public void createMatrix() {
      this.matrix = new Cell[this.rowCount][this.colCount];
      for(int row = 0; row < this.rowCount; row++) {
         
         for(int col = 0; col < this.colCount; col++) {
            matrix[row][col] = new Cell();
         }
      }
   
   }
   
   // A method that takes in the cellLocation from the console "a1", and 
   // converts the column value into a number so the matrix can better access this location
   public int getColumnValue(String cellLocation) {
      String location = cellLocation.toLowerCase();
      char column = location.charAt(0);
      String alphabet = "abcdefghijklmnopqrstuvwxyz";
      int columnValue = alphabet.indexOf(column);
      return columnValue;
   }
   
   // A method that takes in the cellLocation from the console "a1", and 
   // converts the row value into a number so the matrix can better access this location
   public int getRowValue(String cellLocation) {
      String row = cellLocation.substring(1);
      int rowValue = Integer.parseInt(row) - 1;
      return rowValue;
   }
   
   // This method handles the calculations involved in the SUM and AVG commands. It determines the 
   // range of cells and adds each cell value to a sum variable while also keeping track of every cell parsed.
   // Based on the command call, this method will return the sum or average of the specified range.
   public String processSumAndAvg(String command) {
      String[] tokens = command.split(" ");
      String first = tokens[2];
      String second = tokens[4];
      double sum = 0;
      int cells = 0;
      
      // if letters are same -> one column, multiple rows
      // if numbers are same -> one row, multiple columns
      if(first.charAt(0) == second.charAt(0)) {
         int col = getColumnValue(first);
         for(int rows = getRowValue(first); rows <= getRowValue(second); rows++) {
            
            if(matrix[rows][col].toString().equals("")) {
               sum += 0;
               cells++;
            } else {
               sum += Double.parseDouble(matrix[rows][col].toString());
               cells++;
            }
         }
      } else {
         int row = getRowValue(first);
         for(int cols = getColumnValue(first); cols <= getColumnValue(second); cols++) {
            
            if(matrix[row][cols].toString().equals("")) {
               sum += 0;
               cells++;
            } else {
               sum += Double.parseDouble(matrix[row][cols].toString());
               cells++;
            }
         }
      }
      
      // If the command is SUM
      if(tokens[1].equals("sum")) {
         return "" + sum;
         
      // If the command is AVG
      } else {
         return "" + (sum/cells);
      }
   }

   
}
