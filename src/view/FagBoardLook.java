package view;

import gamifier.view.GridLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import model.FagBoard;

import static javafx.scene.shape.StrokeType.CENTERED;
import static javafx.scene.shape.StrokeType.INSIDE;
import static model.FagBoard.GRID_SIZE;

public class FagBoardLook extends GridLook {

    // The array of rectangle composing the grid
    private Rectangle[][] cells;

    /**
     * The visual to be used for the board
     * @param size     The size of the board in pixels.
     * @param fagBoard A ref. to FAG board.
     */
    public FagBoardLook(int size, FagBoard fagBoard) {
        // NB: To have more liberty in the design, GridLook does not compute the cell size from the dimension of the GameElement parameter.
        // If we create the GRID_SIZExGRID_SIZE board by adding a border of 10 pixels, with cells occupying all the available surface,
        // then, cells have a size of (size-20)/GRID_SIZE
        super(size, size, (size-20)/GRID_SIZE, (size-20)/GRID_SIZE, 10, "0X000000", fagBoard);
        cells = new Rectangle[GRID_SIZE][GRID_SIZE];

        // Create the rectangles.
        for (int row=0; row<GRID_SIZE; row++) {
            for (int col=0; col<GRID_SIZE; col++) {
                Color color;
                if (((row<2) && (col<2)) || ((row>4) && (col<2)) ||
                    ((row<2) && (col>4)) || ((row>4) && (col>4))) {
                    color = Color.WHITE;
                }
                else {
                    color = Color.BURLYWOOD;
                }
                cells[row][col] = new Rectangle(cellWidth, cellHeight, color);
                cells[row][col].setX(col*cellWidth+borderWidth);
                cells[row][col].setY(row*cellHeight+borderWidth);
                addShape(cells[row][col]);

                // Add a small circle in the middle of the cell
                if ((cells[row][col].getFill() == Color.BURLYWOOD)) {
                    double centerX = cells[row][col].getX() + cellWidth / 2;
                    double centerY = cells[row][col].getY() + cellHeight / 2;
                    Circle circle = new Circle(centerX, centerY, 4);
                    circle.setFill(Color.BLUE);
                    addShape(circle);
                }
            }

        }

        // Draw the possible directions among cells
        drawGraph(fagBoard);
    }

    /**
     * Draw the graph that depicts the possible movements (i.e. the network
     * connectivity) in between the cells of the cross shaped board.
     * @param fagBoard A ref. to FAG board.
     */
    private void drawGraph(FagBoard fagBoard) {
        FagBoard.Graph graph = fagBoard.getGraph();
        for (int r=0; r<GRID_SIZE; r++) {
            for (int c=0; c<GRID_SIZE; c++) {
                FagBoard.Cell src = new FagBoard.Cell(r, c);
                int linearId = r*GRID_SIZE + c;
                int dstCells = graph.adjList.get(linearId).size();
                if (dstCells != 0) {
                    boolean DEBUG_TRACE=false;
                    if (DEBUG_TRACE) System.out.print("Vertex [" + src.row + "," + src.col + "] ->");
                    for (int j = 0; j < dstCells; j++) {
                        FagBoard.Cell dst = graph.adjList.get(linearId).get(j);
                        if (DEBUG_TRACE) System.out.print(" [" + dst.row + "," + dst.col + "]");
                        drawEdge(src, dst);
                    }
                    if (DEBUG_TRACE) System.out.println("");

                }
            }
        }
    }

    /**
     * Draws an edge of the connectivity graph
     * @param src The vertex source of the edge as a board cell.
     * @param dst The vertex destination of the edge as a board cell.
     */
    private void drawEdge(FagBoard.Cell src, FagBoard.Cell dst) {
        double startX = cells[src.row][src.col].getX() + cellWidth/2;
        double startY = cells[src.row][src.col].getY() + cellHeight/2;
        double endX   = cells[dst.row][dst.col].getX() + cellWidth/2;
        double endY   = cells[dst.row][dst.col].getY() + cellHeight/2;
        Line line = new Line(startX, startY, endX, endY);
        line.setStrokeWidth(2);
        line.setStroke(Color.BLUE);
        addShape(line);
    }

    @Override
    public void onChange() {
        // If a pawn is selected, reachableCells changes. Thus, the look of the board must also change.
        FagBoard board = (FagBoard)element;
        boolean[][] reach = board.getReachableCells();
        for (int i=0; i<GRID_SIZE; i++) {
            for (int j=0; j<GRID_SIZE; j++) {
                if (reach[i][j]) {
                    cells[i][j].setStrokeWidth(2);
                    //cells[i][j].setStrokeMiterLimit(1);
                    cells[i][j].setStrokeType(INSIDE);
                    cells[i][j].setStroke(Color.BLACK);
                    //cells[i][j].getStrokeDashArray().addAll(2.0);
                } else {
                    cells[i][j].setStrokeWidth(0);
                    cells[i][j].setSmooth(false);
                }
            }
        }
    }
}
