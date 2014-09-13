// This is a program that will solve a 15X15 slide puzzle
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

        // populate the root node with puzzle
        node root = new node();
        root.move = "Start";
        for (int i =0; i < 4; i++) {
            for (int q = 0; q < 4; q++) {
                root.state[i][q] = Integer.parseInt(args[4*i + q]);
                if(root.state[i][q] == 0){
                        root.blankRow = i;
                        root.blankCol = q;
                }
            }
        }

        System.out.println("BFS is working please wait..."); 
        try{
            node sol = breadthFind(root);
            if(sol != null){
                System.out.println("BFS found it!");
                printSolution(sol, true);
            }
        }catch(OutOfMemoryError e){
            System.out.println("Can't find solution- BFS ran out of memory :(");
        }     
        System.out.println("---------------------------------------------\nIDDFS is working please wait...");
        try{
            node sol = iterDepthFind(root);
            if(sol != null){
                System.out.println("IDDFS found it!");
                printSolution(sol, true);
            }
        }catch(OutOfMemoryError e){
            System.out.println("Can't find solution- IDDFS ran out of memory :(");
        }

    }
    // A method that uses breadth first search to find a solution
    public static node breadthFind(node root){
        ArrayList<String> savedStates = new ArrayList<String>();
        LinkedList<node> queue = new LinkedList<node>();
        queue.add(root);

        while(!queue.isEmpty()){
            node cur = queue.removeFirst();
            String curState = makeStateID(cur);

            if(goalTest(cur))
                return cur;
            else if( !savedStates.contains(curState) ){
                savedStates.add(curState);
                evaluateChildren(cur);

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
        // should never happen unless puzzle is not valid
        return null;
    }

    // A method that uses iterative deepening depth first search
    public static node iterDepthFind(node root){
        int depthLevel = 0;
        
        while(depthLevel < Integer.MAX_VALUE){
            ArrayList<String> savedStates = new ArrayList<String>();
            LinkedList<node> stack = new LinkedList<node>();
            stack.add(root);

            while(!stack.isEmpty()){
                node cur = stack.removeLast();
                String curState = makeStateID(cur);

                if(goalTest(cur))
                    return cur;
                else if(!savedStates.contains(curState) && cur.level < depthLevel){
                    savedStates.add(curState);
                    evaluateChildren(cur);

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
        // should never happen unless puzzle is not valid
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
            curNode.left.move = "Left";
            curNode.left.level = curNode.level + 1;
            curNode.left.blankCol = curNode.blankCol - 1;
            curNode.left.blankRow = curNode.blankRow;
            curNode.left.parent = curNode;
            curNode.left.state = makeState(curNode.state, curNode.blankRow, curNode.blankCol, 'L');
        }
        if(curNode.blankCol < 3){
            curNode.right = new node();
            curNode.right.move = "Right";
            curNode.right.level = curNode.level + 1;
            curNode.right.blankCol = curNode.blankCol + 1;
            curNode.right.blankRow = curNode.blankRow;
            curNode.right.parent = curNode;     
            curNode.right.state = makeState(curNode.state, curNode.blankRow, curNode.blankCol, 'R');

        }
        if(curNode.blankRow > 0){
            curNode.up = new node();
            curNode.up.move = "Up";
            curNode.up.level = curNode.level + 1;
            curNode.up.blankCol = curNode.blankCol;
            curNode.up.blankRow = curNode.blankRow - 1;
            curNode.up.parent = curNode;
            curNode.up.state = makeState(curNode.state, curNode.blankRow, curNode.blankCol, 'U');
        
        }
         if(curNode.blankRow < 3){
            curNode.down = new node();
            curNode.down.move = "Down";
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

    // checks to see if the node has the correct state(goal)
    public static boolean goalTest(node curNode){
        int [][] goalState = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,0}};
        return Arrays.deepEquals(curNode.state, goalState);    
    }

    // print the puzzle in a pretty way
    public static void printPuzzle(node curNode){
        if(curNode == null)
            return;

        System.out.println(curNode.move);
        for(int i = 0; i < 4; i++){
            for(int q: curNode.state[i])
                System.out.printf("%3d  ",q);
            System.out.println();
        }
        System.out.println();
    }

    // print the puzzle in a ugly way
    public static void printPuzzleUgly(node curNode){
        if(curNode == null)
            return;

        for(int i = 0; i < 4; i++){
            for(int q: curNode.state[i])
                System.out.printf("%1d ",q);
        }
        System.out.print("  " + curNode.move + "\n");
    }

    // traverses the tree backwards to print the solution
    public static void printSolution(node solution, boolean ugly){
        LinkedList<node> solutionPath = new LinkedList<node>();
        node cur = solution;
        while(cur != null){
            solutionPath.addFirst(cur);
            cur = cur.parent;
        }

        System.out.println("This is the solution:");

        while(!solutionPath.isEmpty()){
            if(!ugly)
                printPuzzle(solutionPath.removeFirst());
                printPuzzleUgly(solutionPath.removeFirst());
        }
    }
}
