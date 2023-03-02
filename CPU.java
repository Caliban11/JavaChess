import java.util.Random;
public class CPU {
    public static void CPUmove(String color, Piece[] whitePieces, Square[] squareArray){
        if(color.equals("white")){
            do{
                int cpuPieceValue = 0;
                for(Piece piece : whitePieces){
                    cpuPieceValue = pieceValue(piece) - 1;
                    int captureValue = 0;
                    for(int move : piece.getLegalMoves(squareArray)){
                        if(move == -1){
                            captureValue = -89;
                        }
                        if(!(squareArray[move] == null)){
                            captureValue = pieceValue(squareArray[move].getOccupant());
                        }
                        else{
                            captureValue = cpuPieceValue;
                        }
                    }
                }
            }while(true);
        }
        else{ //For black

        }
    }

    public static int maxIndex(int[] array){
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

    public static int max(int[] array){
        int max = array[0];
        for(int i = 1; i < array.length; i++){
            if(array[i] > max){
                max = array[i];
            }
        }
        return max;
    }

    public static int pieceValue(Piece piece){
        switch(piece.getType()){
            case "pawn": return 1;
            case "knight": return 3;
            case "bishop": return 3;
            case "rook": return 5;
            case "queen": return 9;
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
