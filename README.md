Overview

This project is a version of the VisiCalc app, which was the first app to come out on a computer! This program creates a spreadsheet similar to a text excel sheet
except there is a limit to how big the sheet can be. The program uses classes, inheritence, objects, and double arrays to build the functionality. This is an OOP-based project. 

This project will automatically create a grid of cells with row = 10, column = 7, and width(of cell) = 9. When this default size is in place, the columns will be labelled A - G. When you run the project, you will be greeted with an introduction and a prompt to enter a command to the terminal. Here are a list of commands that you can type into the program at any given moment:
  
 - Help: This will give you a more in-depth explanation of the project and how to navigate through it. This README will suit you better though. 
 - LOAD <file.txt>: This allows you to load a file within the directory of the project that has a pre-existing set of commands that you want to run. It will not be able to read a file that has anything except for commands on each line because the program assumes you are only loading command-filled files.
 - 

NOTE: Majority of the project setup, including all of the ExcelBase class, GridBase class, UnitTestRunner class, and much more was created by my instructor Jeffery Stride. I will explain in this README the functionality of the classes that I created and worked on, as well as the functionality of the program as a whole. Thank you Mr.Stride!

Implementation

There are 2 main classes that I developed within this program: Grid and ExcelEngine. Additionally, there are 3 subclasses that inherit the Cell class that I also developed. All of these are responsible for creating the excel worksheet and all of it's functionality. The cell class and it's subclasses handle all of the functionality within an invididual cell within the excel spreadsheet, the grid class is responsible for the creation, storage, and access to all of the cells within the spread sheet as a whole, and the ExcelEngine class

Cell Classes
This class and it's subclasses handle all of the functionality within an individual cell apart of the excel spreadsheet. There are 

Notes: 

