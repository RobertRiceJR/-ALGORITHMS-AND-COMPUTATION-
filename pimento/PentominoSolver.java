import java.util.ArrayList;
import java.util.Scanner;

public class PentominoSolver  {

    private static final int NUM_PIECES = 12; // F, I, L, N, P, T, U, V, W, X, Y, Z

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) {
            System.err.println("Please provide an integer n in {3, 4, 5, 6}.");
            return;
        }
        int n = sc.nextInt();
        sc.close();

        int rows, cols;
        switch (n) {
            case 3:
                rows = 3;
                cols = 20;
                break;
            case 4:
                rows = 4;
                cols = 15;
                break;
            case 5:
                rows = 5;
                cols = 12;
                break;
            case 6:
                rows = 6;
                cols = 10;
                break;
            default:
                System.err.println("n must be 3, 4, 5, or 6.");
                return;
        }

        int numCells = rows * cols;
        int numCols = NUM_PIECES + numCells; 

        ArrayList<ArrayList<Integer>> matrix =
        buildExactCoverMatrix(rows, cols, PentominoPieces.SHAPES);

    
        DLX dlx = new DLX(numCols, matrix);
        dlx.run();

        int solutions = dlx.getNumberOfSolutions();
        System.out.println(solutions);
    }


    private static ArrayList<ArrayList<Integer>> buildExactCoverMatrix(
            int rows, int cols, int[][][] shapes) {

        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        int numCells = rows * cols;

        // for each piece
        for (int pieceIndex = 0; pieceIndex < shapes.length; pieceIndex++) {
            int[][] orientations = shapes[pieceIndex];

            // for each orientation of piece
            for (int[] orient : orientations) {
                // orient has length 8: (x1,y1,x2,y2,x3,y3,x4,y4), origin is (0,0)

                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {

                        int[] cellIndices = new int[5];
                        // origin at (c, r)
                        cellIndices[0] = r * cols + c;

                        boolean legal = true;
                        // remaining four squares
                        for (int k = 0; k < orient.length; k += 2) {
                            int dx = orient[k];
                            int dy = orient[k + 1];
                            int rr = r + dy;
                            int cc = c + dx;

                            if (rr < 0 || rr >= rows || cc < 0 || cc >= cols) {
                                legal = false;
                                break;
                            }

                            int cellIndex = rr * cols + cc;
                            cellIndices[1 + (k / 2)] = cellIndex;
                        }

                        if (!legal) {
                            continue;
                        }

                        // build row
                        ArrayList<Integer> row = new ArrayList<>(6);
                        // piece constraint
                        row.add(pieceIndex);

                        // and cell constraints
                        for (int idx = 0; idx < 5; idx++) {
                            int cellCol = NUM_PIECES + cellIndices[idx];
                            row.add(cellCol);
                        }

                        matrix.add(row);
                    }
                }
            }
        }

        return matrix;
    }
}
