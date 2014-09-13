PuzzleSolver
============
Author: Carlos Ortega
Class: CS411

About:
------
puzzleSolver is a program written in java that will attempt to solve a 4X4 slide puzzle.  
It does this by first using BFS and then using IDDFS.

Run:
----
To run the program must first be compiled.  

To do this you type:  
javac puzzleSolver.java  

To run you have to provide the 16 numbers, 0 means a blank, as an argument  
*Ex:*  
java puzzleSolver 1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15


This corresponds to the following puzzle:  
|1 |0 |2 |4 |  
|5 |7 |3 |8 |  
|9 |6 |11|12|  
|13|10|14|15|  

Output:
-------
The program will then out put the solution for both algorithms  
*Ex:*  
BFS is working please wait...  
BFS found it!    
This is the solution:  
1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15   Start  
1 2 0 4 5 7 3 8 9 6 11 12 13 10 14 15   Right  
1 2 3 4 5 7 0 8 9 6 11 12 13 10 14 15   Down  
1 2 3 4 5 0 7 8 9 6 11 12 13 10 14 15   Left  
1 2 3 4 5 6 7 8 9 0 11 12 13 10 14 15   Down  
1 2 3 4 5 6 7 8 9 10 11 12 13 0 14 15   Down  
1 2 3 4 5 6 7 8 9 10 11 12 13 14 0 15   Right  
1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0   Right  
---------------------------------------------  
IDDFS is working please wait...  
IDDFS found it!  
This is the solution:  
1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15   Start  
1 2 0 4 5 7 3 8 9 6 11 12 13 10 14 15   Right  
1 2 3 4 5 7 0 8 9 6 11 12 13 10 14 15   Down  
1 2 3 4 5 0 7 8 9 6 11 12 13 10 14 15   Left  
1 2 3 4 5 6 7 8 9 0 11 12 13 10 14 15   Down  
1 2 3 4 5 6 7 8 9 10 11 12 13 0 14 15   Down  
1 2 3 4 5 6 7 8 9 10 11 12 13 14 0 15   Right  
1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0   Right
