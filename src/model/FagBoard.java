package model;

import gamifier.model.GameElement;
import gamifier.model.GameStageModel;
import gamifier.model.GridElement;
import javafx.geometry.Point2D;

import java.awt.Point;
import java.util.*;

import static java.lang.Integer.MAX_VALUE;


/**
 * This class represents the board of the FoxAndGeese game.
 *
 * <p>
 * Note: In Gamifier, each game element (pawns, boards, texts, buttons, ...)
 * must be represented by a subclass of GameElement.
 * </p>
 */
public class FagBoard extends GridElement {

    public final static String boardName = "board";
    public final static int    GRID_SIZE = 7;
    public final static int    BOARD_CELLS = (GRID_SIZE*GRID_SIZE)-16; // 33
    public final static int    GEESE_COUNT = (GRID_SIZE*2)-1; // 13

    public static final int    FOX_TEAM   = 0;
    public static final int    GEESE_TEAM = 1;

    /**
     * A cell representing a grid location in (row, column) coordinates.
     */
    public static class Cell {

        /**
         * The ROW coordinate of this Cell.
         */
        public int row;
        /**
         * The COLUMN coordinate of this Cell.
         */
        public int col;
        /**
         * Constructs and initializes a default cell at coordinates (0,0).
         */
        public Cell() {
            this.row = 0;
            this.col = 0;
        }
        /**
         * Constructs and initializes a cell with the same location as
         * the specified Cell object.
         * @param       c A cell
         */
        public Cell(Cell c) {
            this.row = c.row;
            this.col = c.col;
        }
        /**
         * Constructs and initializes a cell at the specified (row,col) location.
         * @param row The ROW coordinate of the newly constructed Cell.
         * @param col The COLUMN coordinate of the newly constructed Cell.
         */
        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }
        /**
         * Returns the ROW coordinate of this Cell.
         */
        public int getRow() {
            return this.row;
        }
        /**
         * Returns the COLUMN coordinate of this Cell.
         */
        public int getCol() {
            return this.col;
        }
        /**
         * Returns the location of this Cell.
         * @return      A copy of this cell, at the same location
         */
        public Cell getLocation() {
            return new Cell(getRow(), getCol());
        }
        /**
         * Sets the location of the cell to the specified location.
         * @param  c  A cell, the new location for this cell.
         */
        public void setLocation(Cell c) {
            this.row = c.getRow();
            this.col = c.getCol();
        }

        /**
         * Determines whether two cells are equal. Two cells are equal if
         * their row and column are the same.
         * @param  c The cell to compare to.
         * @return True if the cell to be compared is has the same row and
         *  column values. False otherwise.
         */
        public boolean equals(Cell c) {
            return (getRow() == c.getRow()) && (getCol() == c.getCol());
        }

        @Override
        public boolean equals(Object obj) {
            return (this.equals((Cell) obj));
        }

        /**
         * Returns a string representation the 'row' and 'col' coordinates of
         * this cell.
         * @return  A string representation of this cell.
         */
        public String toString() {
            return getClass().getName() + "[" + row + "," + col + "]";
        }
    }

    // A class to store the edges of the Fag graph
    class Edge {
        Cell src, dst;
        Edge(Cell src, Cell dst) {
            this.src = src;
            this.dst = dst;
        }
    }

    /**
     * A graph to store the possible movements (i.e. the network connectivity)
     * in between the cells of the cross shaped board of the Fox&Geese game.
     */
    public class Graph {
        final private int nrVertices = GRID_SIZE*GRID_SIZE;
        private int nrEdges;

        // Define adjacency list as list of cells
        public List<List<Cell>> adjList = new ArrayList<>();
        // Graph Constructor
        public Graph(List<Edge> edges) {
            nrEdges = edges.size();
            // Adjacency list memory allocation
            // Note: We allocate one adjacent list per cell of the grid in order
            //  to be able to access the list with a linear index.
            for (int i = 0; i < GRID_SIZE*GRID_SIZE; i++)
                adjList.add(i, new ArrayList<>());
            // Add edges to the graph
            for (Edge e : edges) {
                // Allocate a new cell in adjacency list from src to dst
                int linearId = e.src.getRow()*GRID_SIZE+e.src.getCol();
                adjList.get(linearId).add(new Cell(e.dst));
            }
        }

        /***
         * Returns the total number of edges in the graph.
         */
        public int getNrEdges() {
            return nrEdges;
        }
        /***
         * Returns the total number of vertices in the graph.
         */
        public int getNrVertices() {
            return nrVertices;
        }

        /**
         * Returns the numbers of edges connected to a vertex (i.e. a Cell).
         * @param cell The cell of interest.
         */
        public int getNrEdges(Cell cell) {
            int linearId = cell.row*GRID_SIZE + cell.col;
            return graph.adjList.get(linearId).size();
        }
        /**
         * Returns the vertices connected to a given Cell.
         * @param cell The Cell of interest.
         * @return A list of Cells.
         */
        public List<Cell> getAdjList(Cell cell) {
            int linearId = cell.row*GRID_SIZE + cell.col;
            return graph.adjList.get(linearId);
        }
    }

    // Define edges of the graph
    List<Edge> edges = Arrays.asList(
            // ROW-0 | COL-2
            new Edge(new Cell(0,2), new Cell(0,3)),
            new Edge(new Cell(0,2), new Cell(1,2)),
            new Edge(new Cell(0,2), new Cell(1,3)),
            // ROW-0 | COL-3
            new Edge(new Cell(0,3), new Cell(0,2)),
            new Edge(new Cell(0,3), new Cell(0,4)),
            new Edge(new Cell(0,3), new Cell(1,3)),
            // ROW-0 | COL-4
            new Edge(new Cell(0,4), new Cell(0,3)),
            new Edge(new Cell(0,4), new Cell(1,3)),
            new Edge(new Cell(0,4), new Cell(1,4)),
            // ROW-1 | COL-2
            new Edge(new Cell(1,2), new Cell(0,2)),
            new Edge(new Cell(1,2), new Cell(1,3)),
            new Edge(new Cell(1,2), new Cell(2,2)),
            // ROW-1 | COL-3
            new Edge(new Cell(1,3), new Cell(0,2)),
            new Edge(new Cell(1,3), new Cell(0,3)),
            new Edge(new Cell(1,3), new Cell(0,4)),
            new Edge(new Cell(1,3), new Cell(1,2)),
            new Edge(new Cell(1,3), new Cell(1,4)),
            new Edge(new Cell(1,3), new Cell(2,2)),
            new Edge(new Cell(1,3), new Cell(2,3)),
            new Edge(new Cell(1,3), new Cell(2,4)),
            // ROW-1 | COL-4
            new Edge(new Cell(1,4), new Cell(0,4)),
            new Edge(new Cell(1,4), new Cell(1,3)),
            new Edge(new Cell(1,4), new Cell(2,4)),
            // ROW-2 | COL-0
            new Edge(new Cell(2,0), new Cell(2,1)),
            new Edge(new Cell(2,0), new Cell(3,0)),
            new Edge(new Cell(2,0), new Cell(3,1)),
            // ROW-2 | COL-1
            new Edge(new Cell(2,1), new Cell(2,0)),
            new Edge(new Cell(2,1), new Cell(2,2)),
            new Edge(new Cell(2,1), new Cell(3,1)),
            // ROW-2 | COL-2
            new Edge(new Cell(2,2), new Cell(1,2)),
            new Edge(new Cell(2,2), new Cell(1,3)),
            new Edge(new Cell(2,2), new Cell(2,1)),
            new Edge(new Cell(2,2), new Cell(2,3)),
            new Edge(new Cell(2,2), new Cell(3,1)),
            new Edge(new Cell(2,2), new Cell(3,2)),
            new Edge(new Cell(2,2), new Cell(3,3)),
            // ROW-2 | COL-3
            new Edge(new Cell(2,3), new Cell(1,3)),
            new Edge(new Cell(2,3), new Cell(2,2)),
            new Edge(new Cell(2,3), new Cell(2,4)),
            new Edge(new Cell(2,3), new Cell(3,3)),
            // ROW-2 | COL-4
            new Edge(new Cell(2,4), new Cell(1,3)),
            new Edge(new Cell(2,4), new Cell(1,4)),
            new Edge(new Cell(2,4), new Cell(2,3)),
            new Edge(new Cell(2,4), new Cell(2,5)),
            new Edge(new Cell(2,4), new Cell(3,3)),
            new Edge(new Cell(2,4), new Cell(3,4)),
            new Edge(new Cell(2,4), new Cell(3,5)),
            // ROW-2 | COL-5
            new Edge(new Cell(2,5), new Cell(2,4)),
            new Edge(new Cell(2,5), new Cell(2,6)),
            new Edge(new Cell(2,5), new Cell(3,5)),
            // ROW-2 | COL-6
            new Edge(new Cell(2,6), new Cell(2,5)),
            new Edge(new Cell(2,6), new Cell(3,5)),
            new Edge(new Cell(2,6), new Cell(3,6)),
            // ROW-3 | COL-0
            new Edge(new Cell(3,0), new Cell(2,0)),
            new Edge(new Cell(3,0), new Cell(3,1)),
            new Edge(new Cell(3,0), new Cell(4,0)),
            // ROW-3 | COL-1
            new Edge(new Cell(3,1), new Cell(2,0)),
            new Edge(new Cell(3,1), new Cell(2,1)),
            new Edge(new Cell(3,1), new Cell(2,2)),
            new Edge(new Cell(3,1), new Cell(3,0)),
            new Edge(new Cell(3,1), new Cell(3,2)),
            new Edge(new Cell(3,1), new Cell(4,0)),
            new Edge(new Cell(3,1), new Cell(4,1)),
            new Edge(new Cell(3,1), new Cell(4,2)),
            // ROW-3 | COL-2
            new Edge(new Cell(3,2), new Cell(2,2)),
            new Edge(new Cell(3,2), new Cell(3,1)),
            new Edge(new Cell(3,2), new Cell(3,3)),
            new Edge(new Cell(3,2), new Cell(4,2)),
            // ROW-3 | COL-3
            new Edge(new Cell(3,3), new Cell(2,2)),
            new Edge(new Cell(3,3), new Cell(2,3)),
            new Edge(new Cell(3,3), new Cell(2,4)),
            new Edge(new Cell(3,3), new Cell(3,2)),
            new Edge(new Cell(3,3), new Cell(3,4)),
            new Edge(new Cell(3,3), new Cell(4,2)),
            new Edge(new Cell(3,3), new Cell(4,3)),
            new Edge(new Cell(3,3), new Cell(4,4)),
            // ROW-3 | COL-4
            new Edge(new Cell(3,4), new Cell(2,4)),
            new Edge(new Cell(3,4), new Cell(3,3)),
            new Edge(new Cell(3,4), new Cell(3,5)),
            new Edge(new Cell(3,4), new Cell(4,4)),
            // ROW-3 | COL-5
            new Edge(new Cell(3,5), new Cell(2,4)),
            new Edge(new Cell(3,5), new Cell(2,5)),
            new Edge(new Cell(3,5), new Cell(2,6)),
            new Edge(new Cell(3,5), new Cell(3,4)),
            new Edge(new Cell(3,5), new Cell(3,6)),
            new Edge(new Cell(3,5), new Cell(4,4)),
            new Edge(new Cell(3,5), new Cell(4,5)),
            new Edge(new Cell(3,5), new Cell(4,6)),
            // ROW-3 | COL-6
            new Edge(new Cell(3,6), new Cell(2,6)),
            new Edge(new Cell(3,6), new Cell(3,5)),
            new Edge(new Cell(3,6), new Cell(4,6)),
            // ROW-4 | COL-0
            new Edge(new Cell(4,0), new Cell(3,0)),
            new Edge(new Cell(4,0), new Cell(3,1)),
            new Edge(new Cell(4,0), new Cell(4,1)),
            // ROW-4 | COL-1
            new Edge(new Cell(4,1), new Cell(3,1)),
            new Edge(new Cell(4,1), new Cell(4,0)),
            new Edge(new Cell(4,1), new Cell(4,2)),
            // ROW-4 | COL-2
            new Edge(new Cell(4,2), new Cell(3,1)),
            new Edge(new Cell(4,2), new Cell(3,2)),
            new Edge(new Cell(4,2), new Cell(3,3)),
            new Edge(new Cell(4,2), new Cell(4,1)),
            new Edge(new Cell(4,2), new Cell(4,3)),
            new Edge(new Cell(4,2), new Cell(5,2)),
            new Edge(new Cell(4,2), new Cell(5,3)),
            // ROW-4 | COL-3
            new Edge(new Cell(4,3), new Cell(3,3)),
            new Edge(new Cell(4,3), new Cell(4,2)),
            new Edge(new Cell(4,3), new Cell(4,4)),
            new Edge(new Cell(4,3), new Cell(5,3)),
            // ROW-4 | COL-4
            new Edge(new Cell(4,4), new Cell(3,3)),
            new Edge(new Cell(4,4), new Cell(3,4)),
            new Edge(new Cell(4,4), new Cell(3,5)),
            new Edge(new Cell(4,4), new Cell(4,3)),
            new Edge(new Cell(4,4), new Cell(4,5)),
            new Edge(new Cell(4,4), new Cell(5,3)),
            new Edge(new Cell(4,4), new Cell(5,4)),
            // ROW-4 | COL-5
            new Edge(new Cell(4,5), new Cell(3,5)),
            new Edge(new Cell(4,5), new Cell(4,4)),
            new Edge(new Cell(4,5), new Cell(4,6)),
            // ROW-4 | COL-6
            new Edge(new Cell(4,6), new Cell(3,5)),
            new Edge(new Cell(4,6), new Cell(3,6)),
            new Edge(new Cell(4,6), new Cell(4,5)),
            // ROW-5 | COL-2
            new Edge(new Cell(5,2), new Cell(4,2)),
            new Edge(new Cell(5,2), new Cell(5,3)),
            new Edge(new Cell(5,2), new Cell(6,2)),
            // ROW-5 | COL-3
            new Edge(new Cell(5,3), new Cell(4,2)),
            new Edge(new Cell(5,3), new Cell(4,3)),
            new Edge(new Cell(5,3), new Cell(4,4)),
            new Edge(new Cell(5,3), new Cell(5,2)),
            new Edge(new Cell(5,3), new Cell(5,4)),
            new Edge(new Cell(5,3), new Cell(6,2)),
            new Edge(new Cell(5,3), new Cell(6,3)),
            new Edge(new Cell(5,3), new Cell(6,4)),
            // ROW-5 | COL-4
            new Edge(new Cell(5,4), new Cell(4,4)),
            new Edge(new Cell(5,4), new Cell(5,3)),
            new Edge(new Cell(5,4), new Cell(6,4)),
            // ROW-6 | COL-2
            new Edge(new Cell(6,2), new Cell(5,2)),
            new Edge(new Cell(6,2), new Cell(5,3)),
            new Edge(new Cell(6,2), new Cell(6,3)),
            // ROW-6 | COL-3
            new Edge(new Cell(6,3), new Cell(5,3)),
            new Edge(new Cell(6,3), new Cell(6,2)),
            new Edge(new Cell(6,3), new Cell(6,4)),
            // ROW-6 | COL-4
            new Edge(new Cell(6,4), new Cell(5,3)),
            new Edge(new Cell(6,4), new Cell(5,4)),
            new Edge(new Cell(6,4), new Cell(6,3))
    );

    public Graph getGraph() {
        return graph;
   }

    // A graph of to keep track of the allowed movements on the board
    private Graph graph;

    /**
     * Basic constructor.
     * <p>
     *
     * @param x      The x location in pane space (in pixels).
     * @param y      The y location in pane space (in pixels).
     * @param gameStageModel The game stage that owns this board-game.
     */
    public FagBoard(int x, int y, GameStageModel gameStageModel) {
        // Create a grid-element for the FoxAndGeese board-game
        super(boardName, x, y, GRID_SIZE, GRID_SIZE, gameStageModel);
        resetReachableCells(false);
        // Instantiate and construct the graph of allowed movements
        this.graph = new Graph(edges);
    }

    /**
     * Retrieve the row and column location of the Fox.
     * @return The location of the Fox as a cell.
     */
    public Cell getFoxLocation() {
        for (int r = 0; r < nbRows; r++) {
            for (int c = 0; c < nbCols; c++) {
                FagPawn pawn = (FagPawn) getElement(r, c);
                if ((pawn != null) && pawn.isFox()) {
                    // DEBUG System.out.println(">>> The fox is at r=" + r + " c=" + c);
                    return new Cell(r, c);
                }
            }
        }
        return null;
    }

    /**
     * Retrieve the row and column location of the Geese.
     * @return The location of the Geese as a list of cells.
     */
    public List<Cell> getGeeseLocations() {
        List<Cell> locations = new ArrayList<>();
        for (int r = 0; r < nbRows; r++) {
            for (int c = 0; c < nbCols; c++) {
                FagPawn pawn = (FagPawn) getElement(r, c);
                if ((pawn != null) && pawn.isGoose()) {
                    // DEBUG System.out.println(">>> The fox is at r=" + r + " c=" + c);
                    locations.add(new Cell(r, c));
                }
            }
        }
        return locations;
    }

    /**
     * Updates the 2D array of booleans which flags the cells that are reachable
     * by a pawn whose grid coordinates are 'row' and 'col'.
     * <pr>
     * @param row The row number of the pawn.
     * @param col The col number of the pawn.
     *
     * @see GridElement#reachableCells
     */
    public void setReachableCells(int row, int col) {
        resetReachableCells(false);
        List<Cell> reachable = computeReachableCells(row, col);
        if (reachable != null) {
            for(Cell c : reachable) {
                reachableCells[c.row][c.col] = true;
            }
        }
        lookChanged = true;
    }

    /**
     * Compute the cells which are reachable from the current pawn position.
     * @param row The row number of the pawn.
     * @param col The col number of the pawn.
     * @return A list of reachable cells.
     */
    public List<Cell> computeReachableCells(int row, int col) {
        FagPawn pawn = (FagPawn)getElement(row,col);
        if (pawn == null) return null;

        if (pawn.isFox()) {
            return reachableForFox(row, col);
        } else {
            return reachableForGoose(row, col);
        }
    }

    /**
     * Compute the cells which are reachable from a goose pawn .
     * @param row The row number of the goose pawn.
     * @param col The col number of the goose pawn.
     * @return A list of reachable cells.
     */
    private List<Cell> reachableForGoose(int row, int col) {
        List<Cell> list = new ArrayList<>();

        FagPawn pawn = (FagPawn) getElement(row,col);
        if (!pawn.isGoose()) {
            // TODO
            System.out.println("ERROR - Cell[" + row + "," + col + "] does not contain a goose.");
            return null;
        }

        // Move South
        if((row < GRID_SIZE-1) && (isEmptyAt(row+1, col))) {
            list.add(new Cell(row+1, col));
        }
        // Move East
        if((col < GRID_SIZE-1) && (isEmptyAt(row, col+1))) {
            list.add(new Cell(row, col+1));
        }
        // Move West
        if((col > 0) && (isEmptyAt(row, col-1))) {
            list.add(new Cell(row, col-1));
        }
        return list;
    }

    /**
     * Compute the cells which are reachable from a goose pawn .
     * @param goose The cell coordinates of the goose pawn.
     * @return A list of reachable cells.
     */
    public List<Cell> reachableForGoose(Cell goose) {
        return reachableForGoose(goose.row, goose.col);
    }

    /**
     * Compute the cells which are reachable from a Fox pawn.
     * @param row The row number of the Fox pawn.
     * @param col The col number of the Fox pawn.
     * @return A list of reachable cells.
     */
    public List<Cell> reachableForFox(int row, int col) {
        List<Cell> reachableCells   = new ArrayList<>();
        Cell       fox              = new Cell(row, col);
        List<Cell> adjFoxNodes      = graph.getAdjList(fox);
        boolean    visitedNodes[][] = new boolean[GRID_SIZE][GRID_SIZE];

        // Always mark the Fox as a visited node
        visitedNodes[fox.row][fox.col] = true;

        for (int j=0; j<adjFoxNodes.size(); j++) {
            Cell neighborCell = adjFoxNodes.get(j);
            if (isEmptyAt(neighborCell.row, neighborCell.col)) {
                // Search empty neighbor cells
                reachableCells.add(neighborCell);
            }
            else {
                // Search for cells reachable by jumps
                List<Cell> jumptoCells = reachableForJump(fox, visitedNodes);
                reachableCells.addAll(jumptoCells);
            }
        }

        boolean DEBUG_TRACE=true;
        if (DEBUG_TRACE) {
            System.out.print("List of reachable cells for Fox at [" + row + "," + col + "] -> ");
            ListIterator<Cell> listItor = reachableCells.listIterator();
            while (listItor.hasNext()) {
                Cell cell = listItor.next();
                System.out.print(" [" + cell.row + "," + cell.col + "]");
            }
            if (reachableCells.isEmpty()) {
                System.out.println("[]");
            }
            else {
                System.out.println("");
            }
        }

        // Game is over if Fox cannot move anymore
        if (reachableCells.isEmpty()) {
            // Set the winner
            this.getModel().setIdWinner(GEESE_TEAM);
            // Stop de the game
            this.getModel().stopGame();
        }
        return reachableCells;
    }

    /**
     * Compute the cells which are reachable from a fox pawn.
     * @param fox The cell coordinates of the fox pawn.
     * @return A list of reachable cells.
     */
    private List<Cell> reachableForFox(Cell fox) {
        return (reachableForFox(fox.row, fox.col));
    }

    /**
     * Traverse the graph and retrieve the list of cells that can be reached by
     * the Fox while jumping over Geese.
     * @param fox     The coordinates of the Fox.
     * @param visited The nodes that were already visited.
     * @return A list of Cells.
     *
     * @note: This method implements a Breadth-first-search (BFS) traversal
     *  technique that begins with the Goose as root node.
     */
    public List<Cell> reachableForJump(Cell fox, boolean visited[][]) {
        List<Cell>  adjFoxNodes    = graph.getAdjList(fox);
        List<Cell>  reachableCells = new ArrayList<>();
        Queue<Cell> nodesQueue     = new LinkedList<Cell>(); // BFS queue

        // Search for cells reachable by jumps
        for (int f=0; f<adjFoxNodes.size(); f++) {
            Cell neighborFoxCell = adjFoxNodes.get(f);
            if (!isEmptyAt(neighborFoxCell.row, neighborFoxCell.col) &&
                 (!visited[neighborFoxCell.row][neighborFoxCell.col])) {
                FagPawn pawn = (FagPawn) this.getElement(neighborFoxCell.row, neighborFoxCell.col);
                if (pawn.isGoose()) {
                    // Always insert root node into BFS queue because node is being visited
                    nodesQueue.add(neighborFoxCell);
                    visited[neighborFoxCell.row][neighborFoxCell.col] = true;

                    while (nodesQueue.size() != 0) {
                        // Deque head entry from queue and process it
                        Cell goose = nodesQueue.poll();
                        // System.out.println("RootGeeseNode = " + goose.toString());

                        // Get all adjacent nodes of the current Geese and process them
                        List<Cell> adjGooseNodes = graph.getAdjList(goose);

                        for (int g = 0; g < adjGooseNodes.size(); g++) {
                            Cell neighborGooseCell = adjGooseNodes.get(g);
                            if (isEmptyAt(neighborGooseCell.row, neighborGooseCell.col)) {
                                if (areAdjacent(fox, goose, neighborGooseCell)) {
                                    // A jump over the Goose is possible for the Fox
                                    reachableCells.add(neighborGooseCell);
                                    // System.out.println("\tAdd reachable for jump = " + neighborGooseCell.toString());
                                    // Take the jump and update visited
                                    Cell nextFox = new Cell(neighborGooseCell);
                                    visited[goose.row][goose.col] = true;
                                    // Continue searching by recurrence
                                    List<Cell> jumptoCells = reachableForJump(nextFox, visited);
                                    reachableCells.addAll(jumptoCells);
                                }
                            }
                        }
                    }
                }
            }
        }
        return reachableCells;
    }

    /**
     * Check if three cells are aligned and adjacent in a row, a column or a diagonal.
     * @param c1 The first cell
     * @param c2 The second cell
     * @param c3 The third cell
     * @return True if adjacent.
     */
    public boolean areAdjacent(Cell c1, Cell c2, Cell c3) {
        if ((manhattanDistance(c1,c2) == 1) && (manhattanDistance(c2,c3) == 1) &&
                (euclideanDistance(c1,c3) == 2.0) ) {
            // HORIZONTALLY or VERTICALLY aligned
            return true;
        }
        else if ((euclideanDistance(c1,c2) < 2.0) && (euclideanDistance(c2,c3) < 2.0) &&
                (manhattanDistance(c1,c3) == 4)) {
            // DIAGONALLY aligned
            return true;
        }
        return false;
    }

    /**
     * Computes the  Euclidean distance between two cells
     * @param c1 The first cell
     * @param c2 The second cell
     * @return The distance
     */
    public double euclideanDistance(Cell c1, Cell c2) {
        double deltaC = c2.col - c1.col;
        double deltaR = c2.row - c1.row;
        double distance = Math.sqrt(deltaC*deltaC + deltaR*deltaR);
        return distance;
    }

    /**
     * Computes the Manhattan distance between two cells
     * @param c1 The first cell
     * @param c2 The second cell
     * @return The distance
     */
    public int manhattanDistance(Cell c1, Cell c2) {
        return (Math.abs(c2.row - c1.row) + Math.abs(c2.col - c1.col));
    }

    /**
     * Retrieve the goose that is the further away from the fox.
     * @param fox   The fox location.
     * @param geese A list of goose locations.
     * @return A Cell.
     *
     * @note: This method uses the Manhattan distance between the fox and goose.
     *  If Two or more geese have the same distance, a random one is returned.
     */
    public Cell maxDistance(FagBoard.Cell fox, List<FagBoard.Cell> geese) {
        int longestDistance = 0;
        FagBoard.Cell gooseLoc = new FagBoard.Cell();
        for (FagBoard.Cell goose: geese) {
            int distance = this.manhattanDistance(fox, goose);
            if (distance >= longestDistance) {
                longestDistance = distance;
                gooseLoc.setLocation(goose);
            }
        }
        return gooseLoc;
    }

    /**
     * Collects the geese which can move.
     * @return A list of cells containing movable Geese.
     */
    public List<FagBoard.Cell> getMovableGeese() {
        List<FagBoard.Cell> movableGeese = new ArrayList<>();
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                FagPawn boardElement = (FagPawn) this.getElement(row, col);
                if ((boardElement != null) && (boardElement.isGoose())) {
                    // Get the reachable cells
                    List<FagBoard.Cell> reachable = this.computeReachableCells(row, col);
                    if (reachable.size() != 0) {
                        FagBoard.Cell cell = new FagBoard.Cell(row, col);
                        movableGeese.add(cell);
                    }
                }
            }
        }
        return movableGeese;
    }

    /**
     * Retrieves the Geese that are taken if Fox is moved to [rowDst,colDst].
     * @param foxElement The Fox game element.
     * @param rowDst The cell row destination of the Fox.
     * @param colDst The cell column destination of the Fox.
     * @return A list of eaten Geese.
     */
    public List<GameElement> getTakenPawns(GameElement foxElement, int rowDst, int colDst) {
        int[]               cell = getElementCell(foxElement);
        List<Cell>          eatenGeese = new ArrayList<>();
        List<GameElement>   eatenElements = new ArrayList<>();
        boolean             visitedNodes[][] = new boolean[GRID_SIZE][GRID_SIZE];

        // Define Fox source and destinations as Cells
        Cell foxSrc = new Cell(cell[0], cell[1]);
        Cell foxDst = new Cell(rowDst, colDst);

        // Always mark the Fox as a visited node
        visitedNodes[foxSrc.row][foxSrc.col] = true;

        eatenGeese = getEatableGeese(foxSrc, foxDst, visitedNodes);

        // Turn Cells into Elements
        for(int g = 0; g < eatenGeese.size(); g++) {
            Cell goose = eatenGeese.get(g);
            System.out.println("Eaten Goose = " + goose.toString());
            eatenElements.add(g, getElement(goose.row, goose.col));
        }
        return eatenElements;
    }

    /**
     * Traverses the graph and retrieves the list of cells containing a Goose
     * which can can be eaten by the Fox while jumping over Geese.
     * @param foxSrc  The initial coordinates of the Fox.
     * @param foxDst  The final coordinates of the Fox.
     * @param visited The nodes that were already visited.
     * @return A list of Cells.
     *
     * @note: This method implements a Depth-first-search (DFS) traversal
     *  technique that begins with the Fox source location as root node.
     */
    public List<Cell> getEatableGeese(Cell foxSrc, Cell foxDst, boolean visited[][]) {

        List<Cell> eatenCells = new ArrayList<>();

        Cell       fox = new Cell(foxSrc);
        List<Cell> adjFoxNodes = graph.getAdjList(fox);

        boolean    done = false;

        final boolean DEBUG = true;
        String methodName = "[" + new Throwable().getStackTrace()[0].getClassName() + "." +
                new Throwable().getStackTrace()[0].getMethodName() + "()] ";

        if (DEBUG) System.out.println(methodName + "Current fox position is " + fox);

        for (Cell neighborFoxCell : adjFoxNodes) {
            // Search for adjacent cells that are reachable by the fox with a jump
            if (!isEmptyAt(neighborFoxCell.row, neighborFoxCell.col) &&
               (!visited[neighborFoxCell.row][neighborFoxCell.col])) {
                FagPawn pawn = (FagPawn) this.getElement(neighborFoxCell.row, neighborFoxCell.col);
                if (pawn.isGoose()) {
                    Cell goose = new Cell(neighborFoxCell);
                    // Get all adjacent goose nodes and process them
                    List<Cell> adjGooseNodes = graph.getAdjList(goose);
                    for (int g = 0; g < adjGooseNodes.size(); g++) {
                        Cell neighborGooseCell = adjGooseNodes.get(g);
                        if (isEmptyAt(neighborGooseCell.row, neighborGooseCell.col)) {
                            if (areAdjacent(fox, goose, neighborGooseCell)) {
                                // A jump over the goose is possible for the fox
                                // Take the jump, update fox and visited
                                eatenCells.add(goose);
                                if (DEBUG) System.out.println(methodName + "\tEaten cells = " + eatenCells);
                                //OBSOLETE fox.setLocation(neighborGooseCell);
                                visited[goose.row][goose.col] = true;
                                if (neighborGooseCell.equals(foxDst)) {
                                    // Stop condition: When we reach the Fox destination cell
                                    return eatenCells;
                                }
                                else {
                                    // Continue searching by recurrence
                                    Cell savedFox = new Cell(fox);
                                    fox.setLocation(neighborGooseCell);
                                    List<Cell> tempCells = getEatableGeese(fox, foxDst, visited);
                                    if (tempCells.isEmpty()) {
                                        // OBSOLETE eatenCells.clear();
                                        // Remove last element of the list
                                        eatenCells.remove(eatenCells.size() - 1);
                                        if (DEBUG) System.out.println(methodName + "\tClear tmp Geese");
                                        // Restore previous fox position
                                        fox.setLocation(savedFox);
                                        if (DEBUG) System.out.println(methodName + "\tRestore fox position to " + fox);
                                    } else {
                                        if (DEBUG) System.out.println(methodName + "\tAdd tmp cells = " + tempCells);
                                        eatenCells.addAll(tempCells);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return eatenCells;
    }

    /***
    private findEatableGeese(Cell foxSrc, Cell foxDst, boolean visited[][], List<Cell> eatenCells) {

        Cell       fox = new Cell(foxSrc);
        List<Cell> adjFoxNodes = graph.getAdjList(fox);

        boolean    done = false;

        final boolean DEBUG = true;
        String methodName = "[" + new Throwable().getStackTrace()[0].getClassName() + "." +
                new Throwable().getStackTrace()[0].getMethodName() + "()] ";

        if (DEBUG) System.out.println(methodName + "Current fox position is " + fox);

        for (Cell neighborFoxCell : adjFoxNodes) {
            // Search for adjacent cells that are reachable by the fox with a jump
            if (!isEmptyAt(neighborFoxCell.row, neighborFoxCell.col) &&
                    (!visited[neighborFoxCell.row][neighborFoxCell.col])) {
                FagPawn pawn = (FagPawn) this.getElement(neighborFoxCell.row, neighborFoxCell.col);
                if (pawn.isGoose()) {
                    Cell goose = new Cell(neighborFoxCell);
                    // Get all adjacent goose nodes and process them
                    List<Cell> adjGooseNodes = graph.getAdjList(goose);
                    for (int g = 0; g < adjGooseNodes.size(); g++) {
                        Cell neighborGooseCell = adjGooseNodes.get(g);
                        if (isEmptyAt(neighborGooseCell.row, neighborGooseCell.col)) {
                            if (areAdjacent(fox, goose, neighborGooseCell)) {
                                // A jump over the goose is possible for the fox
                                // Take the jump, update fox and visited
                                eatenCells.add(goose);
                                if (DEBUG) System.out.println(methodName + "\tEaten cells = " + eatenCells);
                                //OBSOLETE fox.setLocation(neighborGooseCell);
                                visited[goose.row][goose.col] = true;
                                if (neighborGooseCell.equals(foxDst)) {
                                    // Stop condition: When we reach the Fox destination cell
                                    return eatenCells;
                                }
                                else {
                                    // Continue searching by recurrence
                                    Cell savedFox = new Cell(fox);
                                    fox.setLocation(neighborGooseCell);
                                    List<Cell> tempCells = getEatableGeese(fox, foxDst, visited);
                                    if (tempCells.isEmpty()) {
                                        eatenCells.clear();
                                        if (DEBUG) System.out.println(methodName + "\tClear tmp Geese");
                                        // Restore previous fox position
                                        fox.setLocation(savedFox);
                                        if (DEBUG) System.out.println(methodName + "\tRestore fox position to " + fox);
                                    } else {
                                        if (DEBUG) System.out.println(methodName + "\tAdd tmp cells = " + tempCells);
                                        eatenCells.addAll(tempCells);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    ***/

    /**
     * Retrieve the Geese which are at risk of being eaten by the Fox
     * @param fox      The location of t he fox.
     * @return A list of Cells.
     */
    public List<FagBoard.Cell> getGeeseAtRisk(FagBoard.Cell fox) {
        List<FagBoard.Cell> geeseAtRisk = new ArrayList<>();
        List<FagBoard.Cell> reachableForJump = this.reachableForJump(fox, new boolean[GRID_SIZE][GRID_SIZE]);
        System.out.println("They are " + reachableForJump.size() + " geese at risk");

        for (FagBoard.Cell foxDst : reachableForJump) {
            FagBoard.Cell foxSrc = new FagBoard.Cell(fox);
            boolean visited[][] = new boolean[GRID_SIZE][GRID_SIZE];
            List<FagBoard.Cell> eatableGeese = this.getEatableGeese(foxSrc, foxDst, visited);
            // Skip duplicates
            for (FagBoard.Cell goose : eatableGeese) {
                if (!geeseAtRisk.contains(goose)) {
                    System.out.println("Found a new goose at risk: " + goose.toString() + " | FoxSrc=" + foxSrc.toString() + " FoxDst=" + foxDst);
                    geeseAtRisk.add(goose);
                }
            }
        }
        return geeseAtRisk;
    }

    public GameElement getTakenPawn(GameElement foxElement, int rowDest, int colDest) {
        System.out.println("WARNING - This method is OBSOLETE ; It was replaced by getTakenPawns()");
        return null;
    }

}
