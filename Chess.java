import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;
public class Chess {                                //Need to add castling, pawn promotion, en passant, draws/stalemate, computer opponent
    public static int fromSquareCapture = -1;
    public static int toSquareCapture = -1;
    public static Square[] squareArray;
    public static Piece[] pieceArray;
    public static Piece[] whitePieces;
    public static Piece[] blackPieces;
    public static Piece whiteKing;
    public static Piece blackKing;
    public static Piece movingPiece[];
    public static Piece capturedPiece[];
    public static void main(String[] args){
        
        whitePieces = new Piece[16];
        blackPieces = new Piece[16];
        pieceArray = place();
        squareArray = createBoard();
        whiteKing = pieceArray[30];
        blackKing = pieceArray[31];

        movingPiece = new Piece[1];
        capturedPiece = new Piece[1];


            do{
                printPosition();
                boolean legal = true;
                do{
                System.out.println("White to play.");
                legal = true;
                move("white");

                if(isCheckWhite()){
                    legal = false;
                    movingPiece[0].place(fromSquareCapture);            //Resetting pieces, square occupants

                    if(!(capturedPiece == null)){
                    capturedPiece[0].place(toSquareCapture);
                    }

                    squareArray[fromSquareCapture].setOccupant(movingPiece[0]);
                    squareArray[toSquareCapture].setOccupant(capturedPiece[0]);

                    System.out.println("Oops! You hung your king. Please try again.");
                }
                } while(legal == false);
                if(isCheckmateBlack() == true){
                    printPosition();
                    System.out.println("Checkmate. 1 - 0");
                    return;
                }

                //Checkmate/stalemate check

                printPosition();
                do{
                System.out.println("Black to play.");
                legal = true;
                move("black");

                if(isCheckBlack()){
                    legal = false;
                    movingPiece[0].place(fromSquareCapture);

                    if(!(capturedPiece == null)){
                    capturedPiece[0].place(toSquareCapture);
                    squareArray[toSquareCapture].setOccupant(capturedPiece[0]);
                    }
                    else{
                        squareArray[toSquareCapture].setOccupant(null);
                    }

                    squareArray[fromSquareCapture].setOccupant(movingPiece[0]);
                    

                    System.out.println("Oops! You hung your king. Please try again.");
                }
                } while(legal == false);
                if(isCheckmateWhite() == true){
                    printPosition();
                    System.out.println("Checkmate. 0 - 1");
                    return;
                }

                //Checkmate/stalemate check
            } while(true);      //Not checkmate or stalemate?
        }

    public static void move(String color){
        Scanner input = new Scanner(System.in);

        movingPiece[0] = null;
        int fromSquare = currentPosition(color);

        //System.out.println(Arrays.toString(movingPiece[0].getLegalMoves(squareArray)));

        int toSquare = intendedPosition(color, fromSquare);

        movingPiece[0].move(fromSquare, toSquare, squareArray, movingPiece[0], capturedPiece[0]);
    }

    public static boolean isCheckWhite(){
        for(Piece piece: blackPieces){
            for(int move : piece.getCaptureMoves(squareArray)){
                if(move == whiteKing.getPosition()){
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isCheckBlack(){
        for(Piece piece: whitePieces){
            for(int move : piece.getCaptureMoves(squareArray)){
                if(move == blackKing.getPosition()){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isCheckmateWhite(){
        if(isCheckWhite()){
            for(Piece piece : whitePieces){
                int fromSquare = piece.getPosition();
                movingPiece[0] = piece;

                for(int move : piece.getLegalMoves(squareArray)){
                    if(!(move == -1)){
                        piece.move(fromSquare, move, squareArray, movingPiece[0], capturedPiece[0]);

                        if(isCheckWhite()){
                            //piece.rewind();
                            movingPiece[0].place(fromSquare);

                            if(!(capturedPiece[0] == null)){
                                capturedPiece[0].place(move);
                                squareArray[move].setOccupant(capturedPiece[0]);
                            }
                            else{
                                squareArray[move].setOccupant(null);
                            }
                            squareArray[fromSquare].setOccupant(movingPiece[0]);
                        }
                        else{
                            return false;
                        }
                    }
                }
            }
        }
        else{
            return false;
        }
        return true;
    }

    public static boolean isCheckmateBlack(){
        if(isCheckBlack()){
            for(Piece piece : blackPieces){
                int fromSquare = piece.getPosition();
                movingPiece[0] = piece;

                for(int move : piece.getLegalMoves(squareArray)){
                    if(!(move == -1)){
                        piece.move(fromSquare, move, squareArray, movingPiece[0], capturedPiece[0]);

                        if(isCheckBlack()){
                            //piece.rewind();
                            movingPiece[0].place(fromSquare);

                            if(!(capturedPiece[0] == null)){
                                capturedPiece[0].place(move);
                                squareArray[move].setOccupant(capturedPiece[0]);
                            }
                            else{
                                squareArray[move].setOccupant(null);
                            }
                            squareArray[fromSquare].setOccupant(movingPiece[0]);
                        }
                        else{
                            return false;
                        }
                    }
                }
            }
        }
        else{
            return false;
        }
    return true;
    }
        
    public static void printPosition(){
        for(int multiplier = 8; multiplier > 0; multiplier --){
            for(int i = (multiplier - 1)*8; i < multiplier*8; i++){
                Square square = squareArray[i];
                System.out.print(square.getUnicode());
                //System.out.printf("%4d", square.getUnicode());
                //System.out.print("piece");
            }
            System.out.println();
        }
    }

    public static int currentPosition(String color){
        Scanner input = new Scanner(System.in);
        String fromSquareString;
        boolean validInput;
        int fromSquare = -1;

        do{
            validInput = true;
            System.out.print("Current position of moving piece: ");
            fromSquareString = input.nextLine();
            int rankInt = (int)fromSquareString.charAt(1) - 48;

            
            if(fromSquareString.equals(null) || !(fromSquareString.length() == 2) || fileToInt(fromSquareString.charAt(0)) == 0 || rankInt < 1 || rankInt > 8){
                validInput = false;
                System.out.println("Incorrect notation. Please try again.");
            }
            else{
                int fileValue = fileToInt(fromSquareString.charAt(0)) - 1;

                int rankCharConvert = (int)fromSquareString.charAt(1) - 48; //-48 to line up unicode number with integer
                int rankValue = (rankCharConvert - 1) * 8;
                        
                fromSquare = fileValue + rankValue;

                validInput = false;
                if(color.equals("white")){
                    for(Piece piece : whitePieces){
                        int piecePosition = piece.getPosition();
                        if(piecePosition == fromSquare){
                            int[] legalMoves = piece.legalMoves(squareArray);

                            for(int move : legalMoves){
                                if(move >= 0 && move < 64){        //Ensure legalMovesArray uses "-1" as empty?
                                    validInput = true;
                                    break;
                                }
                            }
                            movingPiece[0] = piece;
                        }
                    }
                }
                else{
                    for(Piece piece : blackPieces){
                        int piecePosition = piece.getPosition();
                        if(piecePosition == fromSquare){
                            int[] legalMoves = piece.legalMoves(squareArray);

                            for(int move : legalMoves){
                                if(move >= 0 && move < 64){        //Ensure legalMovesArray uses "-1" as empty?
                                    validInput = true;
                                    break;
                                }
                            }
                            movingPiece[0] = piece;
                        }
                    }
                }
                if(validInput == false){
                    System.out.println("No legal moves from that square. Please try again.");
                }
            }
        } while(!validInput);
        fromSquareCapture = fromSquare;
        return fromSquare;
    }

    public static int intendedPosition(String color, int fromSquare){
        Scanner input = new Scanner(System.in);
        //String toSquareString;
        boolean validInput;
        int toSquare = -1;

        do{
            validInput = false;
            System.out.print("Intended position of moving piece: ");
            String toSquareString = input.nextLine();
            int rankInt = (int)toSquareString.charAt(1) - 48;
            //System.out.println(rankInt);
            //System.out.println(toSquareString);


            if(toSquareString.equals(null) || !(toSquareString.length() == 2) || (fileToInt(toSquareString.charAt(0)) == 0 || rankInt < 1 || rankInt > 8)){
                validInput = false;
                System.out.println("Incorrect notation. Please try again.");
            }
            else{
                int fileValue = fileToInt(toSquareString.charAt(0)) - 1;

                int rankCharConvert = (int)toSquareString.charAt(1) - 48; //-48 to line up unicode number with integer
                int rankValue = (rankCharConvert - 1) * 8;
                            
                toSquare = fileValue + rankValue;


                for(int legalMove : movingPiece[0].getLegalMoves(squareArray)){
                    if(legalMove == toSquare){
                        validInput = true;
                        break;
                        }
                }

                if (!validInput){
                    System.out.println("That is not a legal move. Please try again.");
                }
            }
        }
        while(!validInput);
        toSquareCapture = toSquare;
        return toSquare;
    }


    public static int fileToInt(char file){
        switch(file){
            case 'a': return 1;
            case 'b': return 2;
            case 'c': return 3;
            case 'd': return 4;
            case 'e': return 5;
            case 'f': return 6;
            case 'g': return 7;
            case 'h': return 8;
        }
        return 0;
    }

    public static Piece[] place(){
        Piece whitePawnA = new Piece("pawn", "white", 8, " P ");  Piece blackPawnA = new Piece("pawn", "black", 48, " P*");
        Piece whitePawnB = new Piece("pawn", "white", 9, " P ");  Piece blackPawnB = new Piece("pawn", "black", 49, " P*");
        Piece whitePawnC = new Piece("pawn", "white", 10, " P "); Piece blackPawnC = new Piece("pawn", "black", 50, " P*");
        Piece whitePawnD = new Piece("pawn", "white", 11, " P "); Piece blackPawnD = new Piece("pawn", "black", 51, " P*");
        Piece whitePawnE = new Piece("pawn", "white", 12, " P "); Piece blackPawnE = new Piece("pawn", "black", 52, " P*");
        Piece whitePawnF = new Piece("pawn", "white", 13, " P "); Piece blackPawnF = new Piece("pawn", "black", 53, " P*");
        Piece whitePawnG = new Piece("pawn", "white", 14, " P "); Piece blackPawnG = new Piece("pawn", "black", 54," P*");
        Piece whitePawnH = new Piece("pawn", "white", 15, " P "); Piece blackPawnH = new Piece("pawn", "black", 55, " P*");

        Piece whiteKnightB = new Piece("knight", "white", 1, " N "); Piece whiteKnightG = new Piece("knight", "white", 6,  " N ");
        Piece blackKnightB = new Piece("knight", "black", 57, " N*"); Piece blackKnightG = new Piece("knight", "black", 62," N*");

        Piece whiteBishopC = new Piece("bishop", "white", 2, " B "); Piece whiteBishopF = new Piece("bishop", "white", 5, " B ");
        Piece blackBishopC = new Piece("bishop", "black", 58, " B*"); Piece blackBishopF = new Piece("bishop", "black", 61," B*");

        Piece whiteRookA = new Piece("rook", "white", 0, " R "); Piece whiteRookH = new Piece("rook", "white", 7, " R ");
        Piece blackRookA = new Piece("rook", "black", 56, " R*"); Piece blackRookH = new Piece("rook", "black", 63, " R*");

        Piece whiteQueen = new Piece("queen", "white", 3, " Q "); Piece blackQueen = new Piece("queen", "black", 59, " Q*");
        Piece whiteKing = new Piece("king", "white", 4, " K "); Piece blackKing = new Piece("king", "black", 60, " K*");

        Piece[] pieceArray = {whitePawnA, whitePawnB, whitePawnC, whitePawnD, whitePawnE, whitePawnF, whitePawnG, whitePawnH, blackPawnA, blackPawnB, blackPawnC, blackPawnD, blackPawnE, blackPawnF, blackPawnG, blackPawnH,
        whiteKnightB, whiteKnightG, blackKnightB, blackKnightG, whiteBishopC, whiteBishopF, blackBishopC, blackBishopF, whiteRookA, whiteRookH, blackRookA, blackRookH, whiteQueen, blackQueen, whiteKing, blackKing};
        
        Piece [] whitePieces2 = {whiteRookA, whiteKnightB, whiteBishopC, whiteQueen, whiteKing, whiteBishopF, whiteKnightG, whiteRookH, whitePawnA, whitePawnB, whitePawnC, whitePawnD, whitePawnE, whitePawnF, whitePawnG, whitePawnH};
        Piece [] blackPieces2 = {blackPawnA, blackPawnB, blackPawnC, blackPawnD, blackPawnE, blackPawnF, blackPawnG, blackPawnH, blackRookA, blackKnightB, blackBishopC, blackQueen, blackKing, blackBishopF, blackKnightG, blackRookH};

        for(int i = 0; i < 16; i++){
            whitePieces[i] = whitePieces2[i];
            blackPieces[i] = blackPieces2[i];
        }

        return pieceArray;

        
    }

    public static Square[] createBoard(){
        Square[] squareArray = new Square[64];

        for(int i = 0; i < 16; i++){
            Square square = new Square(i, whitePieces[i]);
            squareArray[i] = square;
        }
        for(int i = 16; i < 48; i++){
            Square square = new Square(i, null);
            squareArray[i] = square;
        }
        for(int i = 48; i < 64; i++){
            Square square = new Square(i, blackPieces[i - 48]);
            squareArray[i] = square;
        }
        return squareArray;
    }

}
