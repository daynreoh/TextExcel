
public class NumberCell extends Cell {

/* 
   * This class handles the representation of the cells that have numbers in it. 
   * This class will be able to compute the value of expressions that are considered complex
   * with multiple operators and calls to different cellLocations. Additionally, this class also 
   * can handle SUM or AVG command calls and determines the value of the range given.  
*/

   // This method is responsible for assigning the output of the specific
   // NumberCell attached. It will return the string version of getValue() which 
   // helps with the display and value commands 
   public String toString() {
      return getValue() + ""; 
   }
   
   // This method is responsible for getting the value of any given NumberCell. It handles special types
   // such as SUM, AVG, and complex arithmetic operations with multiple operators and calls to other cell locations.
   public double getValue() {
      
      if(getExpression().contains("sum") || getExpression().contains("avg")) {
         return Double.parseDouble(GridBase.grid.processCommand(getExpression()));
      }
      
      String[] tokens = GridBase.grid.smartSplit(getExpression());
      
      // turns all the calls to other cellLocations to numeric values
      convertTokensToValues(tokens);
      
      // if not an expression
      if(tokens.length == 1) {
         return convertValueToDouble(tokens);
         
      // if is an expression
      } else {
         return computeExpression(tokens);
      }
   }
   
   // This is a getValue helper method that takes in the expression and converts any calls to other
   // cellLocations into their respective numeric values. It does this by calling the value command of the
   // specific cellLocation and replacing the output with the cell location in the tokens array
   public void convertTokensToValues(String[] tokens) {
      for(int element = 0; element < tokens.length; element++) {
         if(isCellLocation(tokens[element])) {
            String newValue = GridBase.grid.processCommand("value " + tokens[element]);
            tokens[element] = newValue;
         }         
      }
   }
   
   // This is a helper method for convertTokensToValues which determines if the token in question
   // is a call to another cellLocation. It does this by determining if the first character is equal to a letter,
   // indicating that another location because the NumberCell expressions do not noramlly contain letters.
   public boolean isCellLocation(String element) {
      boolean flag = false;
      String alphabet = "abcdefghijklmnopqrstuvwxyz";
      for(int index = 0; index < alphabet.length(); index++) {
         if(element.charAt(0) == alphabet.charAt(index)) {
            flag = true;
         }
      }
      return flag; 
   }
   
   // A helper method of getValue(), this is called when the expression is just 1 numeric value.
   // it converts this value to a double and returns it. It can also be called to add an expression of numeric
   // values and convert them to doubles.
   public double convertValueToDouble(String[] tokens) {
      double sum = 0.0;
      for(int element = 0; element < tokens.length; element++) {
         sum += Double.parseDouble(tokens[element]);   
      }
      return sum;
   }
   
   // A helper method of getValue(), this method handles the arithmetic involed in solving
   // a multiple operator expression. It does not use operator precendece, but instead just solves
   // the problem based on which operator comes first in the expression string.
   public double computeExpression(String[] tokens) {
      double sum = Double.parseDouble(tokens[1]);
      
      // tokens.length - 1 makes sure we aren't parsing the final ")"
      for(int element = 2; element < tokens.length - 1; element+=2) {   
         String operator = tokens[element];
         double secondValue = Double.parseDouble(tokens[element+1]);
         
         // determineOperator returns the value of these two numbers based on the operator
         sum = determineOperator(operator, sum, secondValue); 
      }
      return sum; 
   
   }
   
   // This method is a helper method of computeExpression and it takes in 2 numeric values and an operator and 
   // depending on what the operator is, computes the value of the operator acting on the numbers. It includes adding,
   // subtracting, multiplying, and dividing
   public double determineOperator(String operator, double sum, double secondValue) {
      if(operator.equals("+")) {
         return sum + secondValue;
      } else if(operator.equals("-")) {
         return sum - secondValue;
      } else if(operator.equals("*")) {
         return sum * secondValue;
      } else {
         return sum / secondValue;
      }
   }
   
}