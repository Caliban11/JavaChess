import java.util.Random;
import java.util.Arrays;

public class CPU {
    private Piece[] pieces;
    private String color;
    
    public CPU(String color, Piece[] pieces){ //CPU is still in progress, not functional (Teleports pawns onto your back rank)
        this.color = color;
        this.pieces = pieces;
    }

    public void CPUmove(Square[] squareArray, Piece[] movingPiece, Piece[] capturedPiece){ //To determine move, CPU will see which move gives it the best net piece value (Assuming if it captures then it's piece will be captured). Currently teleports pawns, also does not have a way of getting out of check yet
        int cpuPieceValue = 0;
        int i = 0; int j = 0;
        int captureValue = 0;

        int[] captureValues = new int[28];
        int[] bestMoveEvals = new int[16];
        int[] bestMoves = new int[16];
        
        for(Piece piece : this.pieces){
            cpuPieceValue = pieceValue(piece) - 1;
            j = 0;
            for(int move : piece.getLegalMoves(squareArray)){
                if(move == -1){
                    captureValue = -89;
                }
                else if(!(squareArray[move] == null)){
                    captureValue = pieceValue(squareArray[move].getOccupant());
                }
                else{
                    captureValue = cpuPieceValue;
                }
                captureValues[j] = captureValue - cpuPieceValue;
                j++;
            }
            bestMoveEvals[i] = max(captureValues);
            bestMoves[i] = maxIndex(captureValues);
            i++;
        }
        Piece selectedPiece;
        if(max(bestMoveEvals) > 0){
            selectedPiece = this.pieces[maxIndex(bestMoveEvals)];
            movingPiece[0] = selectedPiece;
        }
        else{
            Random rand = new Random();
            int randInt;
            do{
                randInt = rand.nextInt(16);

            }while(bestMoveEvals[randInt] < - 20);
            selectedPiece = this.pieces[randInt];
            movingPiece[0] = selectedPiece;
        }

        int selectedMove = selectedPiece.getLegalMoves(squareArray)[bestMoves[maxIndex(bestMoveEvals)]];

        System.out.println(Arrays.toString(bestMoveEvals));
        System.out.println(Arrays.toString(bestMoves));
        //System.out.println();

        selectedPiece.move(selectedPiece.getPosition(), selectedMove, squareArray, movingPiece, capturedPiece);

        System.out.println("I played " + selectedPiece.getType() + " " + selectedMove);
    }

    public static int maxIndex(int[] array){ //Array function to find index of max value
        int max = array[0];
        int index = 0;
        for(int i = 1; i < array.length; i++){
            if(array[i] > max){
                max = array[i];
                index = i;
            }
        }
        return index;
    }

    public static int max(int[] array){ //Array function to find max value
        int max = array[0];
        for(int i = 1; i < array.length; i++){
            if(array[i] > max){
                max = array[i];
            }
        }
        return max;
    }

    public static int pieceValue(Piece piece){ //Setting values of pieces
        if(piece != null){
            switch(piece.getType()){
                case "pawn": return 1;
                case "knight": return 3;
                case "bishop": return 3;
                case "rook": return 5;
                case "queen": return 9;
            }
        }
        return 0;
    }

    public static void encouragement(){
        //Random rand = new Random();
        //int mateIn = rand.nextInt(1, 75);
        //int lineIndex = rand.nextInt(1);
        //System.out.println("You are already dead. I have mate in " + mateIn + ".");
        //String[] encouragement = {"Too weak, too slow. ", "Oops, you hung your king.", "You are idoit.", "Foolish mortal.", "Flesh is weak."};
        //System.out.println(encouragement[lineIndex]);
    }
}
