/* 
   * This class is responsible for handling all cell values that include text. 
   * These values are not being acted on, so it is just the input text 
   * that is being displayed as a value within the specified cell. For this reason 
   * the only responsibility within this Class is to change the formatting by removing 
   * the quotes from the String when it is on display. 
*/

public class TextCell extends Cell {
   
   // this method is responsible for representing the standard value of a TextCell
   // when it is called. This method changes the original expression by removing the quotation
   // marks from the front and back of string.
   public String toString() {
   String label = getExpression();
   return label.substring(1, label.length() - 1);
   }
}