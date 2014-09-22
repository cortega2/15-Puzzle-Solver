PuzzleSolver
============
Author: Carlos Ortega
Class: CS411

About:
------
puzzleSolver is a program written in java that will attempt to solve a 4X4 slide puzzle.  
It does this by first using BFS and then using IDDFS, and finally using A* with two different heuristics.

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

keep in mind that the puzzle has to be solvable  


Output:
-------
The program will then out put the solution for the four algorithms  
*Ex:*  
BFS:
1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15   Moves:7  Steps:RDLDDRR
Memory: 8624kb   Time: 42ms   Expanded Nodes:2642

IDDFS:
1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15   Moves:7  Steps:RDLDDRR
Memory: 7981kb   Time: 23ms   Expanded Nodes:1489

A*h1:
1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15   Moves:7  Steps:RDLDDRR
Memory: 886kb   Time: 0ms   Expanded Nodes:7

A*h2:
1 0 2 4 5 7 3 8 9 6 11 12 13 10 14 15   Moves:7  Steps:RDLDDRR
Memory: 886kb   Time: 0ms   Expanded Nodes:7
