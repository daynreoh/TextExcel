Overview

This project is a version of the VisiCalc app, which was the first app to come out on a computer! This program creates a spreadsheet similar to a text excel sheet
except there is a limit to how big the sheet can be. The program uses classes, inheritence, objects, and double arrays to build the functionality. This is an OOP-based project. 

This project will automatically create a grid of cells with row = 10, column = 7, and width(of cell) = 9. When this default size is in place, the columns will be labelled A - G, rows will be labelled 1-10, and each cell will have a max char value of 9 before truncation. When you run the project, you will be greeted with an introduction and a prompt to enter a command to the terminal. Here are a list of commands that you can type into the program at any given moment:
  
 - HELP: This will give you a more in-depth explanation of the project and how to navigate through it. This README will suit you better though. 
 - LOAD (file.txt): This allows you to load a file within the directory of the project that has a pre-existing set of commands that you want to run. It will not be able to read a file that has anything except for commands on each line because the program assumes you are only loading command-filled files.
 - (column)(row) [ex A5]: This will return the value of the cell that it was set to initially. With text cells, this command will include the quotation marks.
 - (column)(row) = *value* [ex B4 = 7.5, G10 = "hello"]: this will set the value, display, and expression of the cell that you specified and will occupy it's spot in the grid. This command can override already inputted values and can be an integer, character, string, or date.
 - (column)(row) = (math expression) [ex C4 = ( A3 - 14 / 7 + 8 * 3.14 )]: Any cell can also compute a mathematical expression with the 4 operators, + - / *, and any calls to previous cells like A3. This computed value will replace the value in the cell the user specifies. When entering math expressions, make sure there are parenthesis encompasing the expression and that every value is followed by an operator and vice versa, as in the example I showed above.
 - CLEAR (column)(row) [ex CLEAR A1]: this will clear the value of the cell the user specifies.
 - PRINT: this will print the entire grid or text excel sheet at any point. All cells that you have set values to will be displayed in their correct location with their display value. 
 - SAVE 
 - SUM
 - AVG
 - CLEAR: this will clear the entire grid and set the values of each cell to NULL.
 - QUIT: this will exit you out of the program.
  
NOTE: Majority of the project's background setup, including all of the ExcelBase class, GridBase class, UnitTestRunner class, and much more was created by my instructor Jeffery Stride. I will explain in this README the functionality of the classes that I created and worked on, as well as the functionality of the program as a whole (which is mainly what I developed). Thank you Mr.Stride!

Implementation

There are 3 main classes that I developed within this program: Cell, Grid, and ExcelEngine. Additionally, there are 3 subclasses that inherit the Cell class that I also developed. All of these are responsible for creating the excel worksheet and all of it's functionality. The cell class and it's subclasses handle all of the functionality within an invididual cell within the excel spreadsheet, the grid class is responsible for the creation, storage, and access to all of the cells within the spread sheet as a whole, and the ExcelEngine class handles the main method, and the help and load file commands.

Cell Classes (Cell, DateCell, NumberCell, TextCell)
  
This class and it's subclasses handle all of the functionality within an individual cell apart of the excel spreadsheet. There are three subclasses that represent 3 different ways to occupy a cell's value: DateCell, TextCell, NumberCell. DateCell is responsible for handling all date formats that look something like this 5/21/1989. The program considers any input value that doesn't have quotation marks, and has more than one "/" character to be a DateCell. TextCell is responsible for any cell with text in it which is determined if the value has any quotation marks within the input string. NumberCell is responsible for cell values that have integer or decimal representations. Like I said, these classes are all subclasses of Class, so they inherit all of the functions that Cell has. However, each of these classes has unique methods that display the values within the cell, so the majority ofthe Cell class methods are overwritten. 
  
It is worthwhile to note that there is one private variable in the Cell class that represents the expression taken in from the terminal when assigning the specified cells value. For privacy purposes and to ensure that the class is understood at a high-level, there are getter and setter methods to access this expression. Also, the NumberCell class is quite complicated because it includes all the functions necessary to evaluate multi-operator expressions, including calls to other cell values. The basic functionality for computing this is by converting the input expression into an array with each token being a character that is separated by white spaces (white space is the delimeter). When computing expressions, the program will not use PEMDAS unfortunately. Also, when evaluating the complex expressions the program uses lazy evaluation, meaning the expression avoids being evaluated until absolutely necessary, so the code and functionality is cleaner.
  
Grid Class

This class is in charge of handling and distributing the functions necessary to run the commands that the user requested at the command-line. Each command has a unique set of tools used to execute the features that are involved to the respective command, some commands overlap functions. Since the Grid class is in charge of executing the commands, it uses the public functions from other classes in order to do so. Additionally, this class overwrites the default constructor and calls a function that creates a 2D array which is used to store all the data points of the excel grid during the programs execution. The constructor is only called once because there is only one Grid class object used during program execution so the 2D array will be instantiated using the default row and column values of 10 and 7, respectively. This means that when you change the value of row and column, it will only change the printed representation of the excel sheet but won't change the max amount of values that the 2D array can store, therefore making this excel sheet limited in size.

ExcelEngine Class

This is the main setup class for this project. It is responsible for instantiating the Grid and ExcelEngine objects used during each execution of the program. It also is an extension of ExcelBase which is a class that takes in the input string by the user from the terminal and processes it into a public function. processCommand() is the function signature and it returns a string of the current command inputted so that the Grid class can call the function to get the inputted string and interpret the command from there. Addtionally, it loops the "Enter: " statement that is printed so that the user can input as many commands as they want until they type "quit". There are two commands that are dealt with in the ExcelEngine class. The first is the "HELP" command and the second is the "LOAD" command which loads a file and processes it's contents (assuming it's contents only contain commands for the program to run). Finally, this class has the main function in it, where it instantiates the Grid object and the ExcelEngine object during every execution. These objects are only created once.

Extra Notes
 - There is a testing command that was implemented by my instructor, Jeffery Stride. The command is prompted by "test <"name of test">" and will test a pre-determined set of cases, including both normal and edge cases. The most useful test to run and examine is "test final" which will display all the commands and different expression and Cell representations it can take. This test will give you a better idea as to the complexity and diversity of this project so I recommend running it!
 - Often times when running the program manually without using pre-made tests or loading files that have commands in them, the user will encounter bugs. These bugs are normally in the form of various exceptions and errors at run-time. These aren't actually bugs because the program works how it was designed to work but they are similar to boundaries that the text excel sheet has. When getting an error it is likely that you did some sort of command combination that was illegal, an expression that couldn't be interpreted, or a msitake in the syntax of each command when typing it in the terminal. An example of this would be a command such as
c2 = (a1 + b1), where a1 = 10 and b1 = "hello". The program will not be able to user integer arithmetic when using strings even though the string has an ASCII value. This is because b1 is represented as a TextCell while a1 is represented as a NumberCell. 
 - Attached below is a UML diagram of the classes which shows the implementation and inheritence within the program. I created this.


<img width="995" alt="Text Excel UML Diagram" src="https://user-images.githubusercontent.com/89550841/150904842-e6c6db6d-b07c-4bc5-b38b-ace9f7c0f1c2.png">




Thank you for reading this and have fun using TextExcel!

- Dayn 


  
  


