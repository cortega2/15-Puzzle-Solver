// This is a program that will solve a 15X15 slide puzzle
// Author: Carlos Ortega for UIC CS411 class
import java.util.LinkedList;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.*;

public  class puzzleSolver {
    private static class node{
        int level = 0;
        int [][] state = new int [4][4];
        int blankRow;
        int blankCol;
        String move = "";
        node parent = null;
        node up = null;
        node down = null;
        node left = null;
        node right = null;
    }

    private static class solutionData {
        int expandedNodes = 0;
        node solutionNode = null;
        long memoryUsed;
        long totalTime;
        String path = "";
        String startingBoard = "";
    }

    public static void main(String [] args){
        // Error check
         if(args.length <= 0){
            System.out.println("No puzzle was provided");
            return;
        }
        else if(args.length != 16){
            System.out.println("Puzzle does not contain correct number of numbers");
            return;
        }


        System.gc();
        System.out.println("\nBFS:"); 
        try{
            // populate the root node with puzzle
            String board = "";
            node root = new node();
            root.move = "Start";
            for (int i =0; i < 4; i++) {
                for (int q = 0; q < 4; q++) {
                    root.state[i][q] = Integer.parseInt(args[4*i + q]);
                    board = board + root.state[i][q] +" ";
                    if(root.state[i][q] == 0){
                            root.blankRow = i;
                            root.blankCol = q;
                    }
                }
            }

            solutionData breadthSolution = breadthFind(root, board);
            
            if(breadthSolution != null)
                printSolutionData(breadthSolution);
            else
                System.out.println("Can't find solution- A*h1 ran out of memory :(");

        }catch(OutOfMemoryError e){
            System.out.println("Can't find solution- BFS ran out of memory :(");
        }

        System.gc();
        System.out.println("\nIDDFS:");
        try{
            // populate the root node with puzzle
            String board = "";
            node root = new node();
            root.move = "Start";
            for (int i =0; i < 4; i++) {
                for (int q = 0; q < 4; q++) {
                    root.state[i][q] = Integer.parseInt(args[4*i + q]);
                    board = board + root.state[i][q] +" ";
                    if(root.state[i][q] == 0){
                            root.blankRow = i;
                            root.blankCol = q;
                    }
                }
            }

            solutionData solution = iterDepthFind(root, board);
            
            if(solution != null)
                printSolutionData(solution);
            else
                System.out.println("Can't find solution- A*h1 ran out of memory :(");

        }catch(OutOfMemoryError e){
            System.out.println("Can't find solution- IDDFS ran out of memory :(");
        }

        System.gc();
        System.out.println("\nA*h1:");
        try{
            // populate the root node with puzzle
            String board = "";
            node root = new node();
            root.move = "Start";
            for (int i =0; i < 4; i++) {
                for (int q = 0; q < 4; q++) {
                    root.state[i][q] = Integer.parseInt(args[4*i + q]);
                    board = board + root.state[i][q] +" ";
                    if(root.state[i][q] == 0){
                            root.blankRow = i;
                            root.blankCol = q;
                    }
                }
            }

            solutionData solution = aStarH1(root, board);
            
            if(solution != null)
                printSolutionData(solution);
            else
                System.out.println("Can't find solution- A*h1 ran out of memory :(");

        }catch(OutOfMemoryError e){
            System.out.println("Can't find solution- A*h1 ran out of memory :(");
        }

        System.gc();
        System.out.println("\nA*h2:");
        try{
            // populate the root node with puzzle
            String board = "";
            node root = new node();
            root.move = "Start";
            for (int i =0; i < 4; i++) {
                for (int q = 0; q < 4; q++) {
                    root.state[i][q] = Integer.parseInt(args[4*i + q]);
                    board = board + root.state[i][q] +" ";
                    if(root.state[i][q] == 0){
                            root.blankRow = i;
                            root.blankCol = q;
                    }
                }
            }

            solutionData solution = aStarH2(root, board);
            
            if(solution != null)
                printSolutionData(solution);
            else
                System.out.println("Can't find solution- A*h2 ran out of memory :(");

        }catch(OutOfMemoryError e){
            System.out.println("Can't find solution- A*h2 ran out of memory :(");
        }
    }

    // solution using the second heuristic. This method calls getDistanceSum()
    public static solutionData aStarH2(node root, String board){
        ArrayList<node> unexpandedNodes = new ArrayList<node>();
        unexpandedNodes.add(root);

        solutionData breadthSolution = new solutionData();
        int expandedNodes = 0;
        long startTime = System.currentTimeMillis();

        node cur = null;
        while(expandedNodes < Integer.MAX_VALUE){
            // get node with lowest f(n) = g(n) + h(n) result
            int minVal = Integer.MAX_VALUE;
            for (int i = 0; i < unexpandedNodes.size() ; i++) {
                node tmp = unexpandedNodes.get(i);
                if(getDistanceSum(tmp) + tmp.level < minVal){
                    minVal = getDistanceSum(tmp) + tmp.level;
                    cur = tmp;
                }
            }
            unexpandedNodes.remove(cur);

            if (getDistanceSum(cur) == 0){
                breadthSolution.expandedNodes = expandedNodes;
                breadthSolution.solutionNode = cur;
                breadthSolution.startingBoard = board;
                breadthSolution.path = getPath(cur);

                Runtime runtime = Runtime.getRuntime();
                breadthSolution.memoryUsed = (runtime.totalMemory() - runtime.freeMemory())/(1024);
                breadthSolution.totalTime = System.currentTimeMillis() - startTime;
                
                return breadthSolution;
            }
            else{
                evaluateChildren(cur);
                expandedNodes++;

                if(cur.left != null)
                    unexpandedNodes.add(cur.left);
                if(cur.right != null)
                    unexpandedNodes.add(cur.right);
                if(cur.up != null)
                    unexpandedNodes.add(cur.up);
                if(cur.down != null)
                    unexpandedNodes.add(cur.down);
            }
        }
        return null;
    }

    // method that usis the first heuristic to get the solution. it calls getMissplacedTiles()
    public static solutionData aStarH1(node root, String board){
        ArrayList<node> unexpandedNodes = new ArrayList<node>();
        unexpandedNodes.add(root);

        solutionData breadthSolution = new solutionData();
        int expandedNodes = 0;
        long startTime = System.currentTimeMillis();

        node cur = null;
        while(expandedNodes < Integer.MAX_VALUE){
            // get node with lowest f(n) = g(n) + h(n) result
            int minVal = Integer.MAX_VALUE;
            for (int i = 0; i < unexpandedNodes.size() ; i++) {
                node tmp = unexpandedNodes.get(i);
                if(getMissplacedTiles(tmp) + tmp.level < minVal){
                    minVal = getMissplacedTiles(tmp) + tmp.level;
                    cur = tmp;
                }
            }
            unexpandedNodes.remove(cur);

            if (getMissplacedTiles(cur) == 0){
                breadthSolution.expandedNodes = expandedNodes;
                breadthSolution.solutionNode = cur;
                breadthSolution.startingBoard = board;
                breadthSolution.path = getPath(cur);

                Runtime runtime = Runtime.getRuntime();
                breadthSolution.memoryUsed = (runtime.totalMemory() - runtime.freeMemory())/(1024);
                breadthSolution.totalTime = System.currentTimeMillis() - startTime;
                
                return breadthSolution;
            }
            else{
                evaluateChildren(cur);
                expandedNodes++;

                if(cur.left != null)
                    unexpandedNodes.add(cur.left);
                if(cur.right != null)
                    unexpandedNodes.add(cur.right);
                if(cur.up != null)
                    unexpandedNodes.add(cur.up);
                if(cur.down != null)
                    unexpandedNodes.add(cur.down);
            }
        }
        return null;
    }

    // A method that uses breadth first search to find a solution
    public static solutionData breadthFind(node root, String board){
        LinkedList<node> queue = new LinkedList<node>();
        queue.add(root);
        
        solutionData breadthSolution = new solutionData();
        int expandedNodes = 0;
        long startTime = System.currentTimeMillis();

        while(!queue.isEmpty() && expandedNodes <= Integer.MAX_VALUE){
            node cur = queue.removeFirst();
            String curState = makeStateID(cur);

            if(goalTest(cur)){
                breadthSolution.expandedNodes = expandedNodes;
                breadthSolution.solutionNode = cur;
                breadthSolution.startingBoard = board;
                breadthSolution.path = getPath(cur);

                Runtime runtime = Runtime.getRuntime();
                breadthSolution.memoryUsed = (runtime.totalMemory() - runtime.freeMemory())/(1024);
                breadthSolution.totalTime = System.currentTimeMillis() - startTime;
                
                return breadthSolution;
            }
            else {
                // expands node
                evaluateChildren(cur);
                expandedNodes++;

                if(cur.left != null)
                    queue.add(cur.left);
                if(cur.right != null)
                    queue.add(cur.right);
                if(cur.up != null)
                    queue.add(cur.up);
                if(cur.down != null)
                    queue.add(cur.down);
            }
        }
        // should never happen unless puzzle is not valid or ran out of memory
        return null;
    }

    // A method that uses iterative deepening depth first search
    public static solutionData iterDepthFind(node root, String board){
        int depthLevel = 0;

        solutionData breadthSolution = new solutionData();
        int expandedNodes = 0;
        long startTime = System.currentTimeMillis();
        
        while(depthLevel < Integer.MAX_VALUE){
            // ArrayList<String> savedStates = new ArrayList<String>();
            LinkedList<node> stack = new LinkedList<node>();
            stack.add(root);

            while(!stack.isEmpty()){
                node cur = stack.removeLast();
                String curState = makeStateID(cur);

                if(goalTest(cur)){
                    breadthSolution.expandedNodes = expandedNodes;
                    breadthSolution.solutionNode = cur;
                    breadthSolution.startingBoard = board;
                    breadthSolution.path = getPath(cur);

                    Runtime runtime = Runtime.getRuntime();
                    breadthSolution.memoryUsed = (runtime.totalMemory() - runtime.freeMemory())/(1024);
                    breadthSolution.totalTime = System.currentTimeMillis() - startTime;
                    
                    return breadthSolution;
                }
                else if(cur.level < depthLevel){
                    // savedStates.add(curState);
                    evaluateChildren(cur);
                    expandedNodes++;

                    if(cur.left != null)
                        stack.add(cur.left);
                    if(cur.right != null)
                        stack.add(cur.right);
                    if(cur.up != null)
                        stack.add(cur.up);
                    if(cur.down != null)
                        stack.add(cur.down);
                }
            }
            depthLevel++;
        }
        // should never happen unless puzzle is not valid or ran out of memory
        return null;
    }

    // helper function that creates a unique id for each state to be later stored
    public static String makeStateID(node curNode){
        String id = "";
        for(int i = 0; i < 4; i++){
            for (int q = 0; q < 4; q++)
                id = id + ((char) (65 + curNode.state[i][q]));
        }
        return id;
    }

    // check to see if the children are possible legal moves
    public static void evaluateChildren(node curNode){
        if(curNode.blankCol > 0){
            curNode.left = new node();
            curNode.left.move = "L";
            curNode.left.level = curNode.level + 1;
            curNode.left.blankCol = curNode.blankCol - 1;
            curNode.left.blankRow = curNode.blankRow;
            curNode.left.parent = curNode;
            curNode.left.state = makeState(curNode.state, curNode.blankRow, curNode.blankCol, 'L');
        }
        if(curNode.blankCol < 3){
            curNode.right = new node();
            curNode.right.move = "R";
            curNode.right.level = curNode.level + 1;
            curNode.right.blankCol = curNode.blankCol + 1;
            curNode.right.blankRow = curNode.blankRow;
            curNode.right.parent = curNode;     
            curNode.right.state = makeState(curNode.state, curNode.blankRow, curNode.blankCol, 'R');

        }
        if(curNode.blankRow > 0){
            curNode.up = new node();
            curNode.up.move = "U";
            curNode.up.level = curNode.level + 1;
            curNode.up.blankCol = curNode.blankCol;
            curNode.up.blankRow = curNode.blankRow - 1;
            curNode.up.parent = curNode;
            curNode.up.state = makeState(curNode.state, curNode.blankRow, curNode.blankCol, 'U');
        
        }
         if(curNode.blankRow < 3){
            curNode.down = new node();
            curNode.down.move = "D";
            curNode.down.level = curNode.level + 1;
            curNode.down.blankCol = curNode.blankCol;
            curNode.down.blankRow = curNode.blankRow + 1;
            curNode.down.parent = curNode;
            curNode.down.state = makeState(curNode.state, curNode.blankRow, curNode.blankCol, 'D');
        }       
    }
    
    // returns a new state(matrix) from the current state given the location of the blank and given
    // the direction up = U, down = D, left = L, right = R
    // does not have error checking, must be done BEFORE calling the function
    public static int[][] makeState(int[][] curState, int blankY, int blankX, char move){
        int [][] newState = new int [4][];
        for(int i = 0; i < 4; i++)
            newState[i] = curState[i].clone();

        if(move == 'U'){
            newState[blankY][blankX] = newState[blankY - 1][blankX];
            newState[blankY - 1][blankX] = 0;
        }
        else if(move == 'D'){
            newState[blankY][blankX] = newState[blankY + 1][blankX];
            newState[blankY + 1][blankX] = 0;
        }
        else if(move == 'L'){
            newState[blankY][blankX] = newState[blankY][blankX - 1];
            newState[blankY][blankX - 1] = 0;
        }
        else{
            newState[blankY][blankX] = newState[blankY][blankX + 1];
            newState[blankY][blankX + 1] = 0;
        }
        return newState;
    }

    // compare the goal state and cur state and return the manhattan sum of a the misplace tiles
    public static int getDistanceSum(node curNode){
        int manhatSum = 0;
        for (int row = 0; row < 4 ; row++) {
            for (int col = 0; col < 4 ; col++ ) {
                int x;
                int y;
                int val = curNode.state[row][col];
                
                if(val >= 1 && val <= 4){
                    y = 0;
                    x = val - 1;
                }
                else if(val >= 5 && val <= 8){
                    y = 1;
                    x = val - 5;
                }
                else if(val >= 9 && val <= 12){
                    y = 2;
                    x = val - 9;
                }
                else if(val >= 13 && val <= 15){
                    y = 3;
                    x = val - 13;
                }
                else{
                    y = 3;
                    x = 3;
                }

                manhatSum = manhatSum + Math.abs(row - y) + Math.abs(col - x);
            }
        }
        return manhatSum;
    }

    // compares the goal state and cur state and returns the number of missplace tiles
    public static int getMissplacedTiles(node curNode){
        int [][] goalState = {{1,2,3,4},{5,6,7,8},{9, 10,11,12},{13,14,15,0}};
        int tileCounter = 0;
        for (int row = 0; row<4; row++){
            for (int col = 0; col < 4; col++){
                if(goalState[row][col] != curNode.state[row][col])
                    tileCounter++;
            }
        }
        return tileCounter;
    }

    // checks to see if the node has the correct state(goal)
    public static boolean goalTest(node curNode){
        int [][] goalState = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,0}};
        return Arrays.deepEquals(curNode.state, goalState);    
    }

    // returns a string that shows the path that needs to be taken to solve the puzzle
    public static String getPath(node solution){
        LinkedList<node> solutionPath = new LinkedList<node>();
        node cur = solution;
        String path = "";
        while(cur != null){
            solutionPath.addFirst(cur);
            cur = cur.parent;
        }
        while(!solutionPath.isEmpty()){
            node tmp = solutionPath.removeFirst();
            if(tmp.move != "Start")
                path = path + tmp.move;
        }
        return path;
    }

    // helper method that simply prints the solution and information regarding the solution
    public static void printSolutionData(solutionData solution){
        System.out.println(solution.startingBoard + "  Moves:" + solution.path.length() + "  Steps:" + solution.path);
        System.out.println("Memory: " + solution.memoryUsed +"kb   Time: "+ solution.totalTime + "ms   Expanded Nodes:" + solution.expandedNodes);
    }
}