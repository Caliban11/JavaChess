import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;

public class Chess {                                //Need to add castling, pawn promotion, en passant, draws/stalemate
    public static int fromSquare = -1;              //Variables to track square being moved from, to
    public static int toSquare = -1;

    public static Square[] squareArray;             //Arrays of squares, pieces to be used within methods
    public static Piece[] pieceArray;
    public static Piece[] whitePieces;
    public static Piece[] blackPieces;
    public static Piece whiteKing;                  //Tracking kings for check, checkmate
    public static Piece blackKing;
    public static Piece[] movingPiece = new Piece[1];   //Tracking the piece last moved, piece captured on last move (if applicable, otherwise null)
    public static Piece[] capturedPiece = new Piece[1];

    public static void main(String[] args){

        whitePieces = new Piece[16];                    //Creating piece arrays
        blackPieces = new Piece[16];
        pieceArray = place();                           //place method returns array of all pieces, creates 32 pieces, puts into white and black arrays
        squareArray = createBoard();                    //Create board creates 64 square objects, returns them in an array
        whiteKing = pieceArray[30];                     //Tracking kings
        blackKing = pieceArray[31];

        movingPiece[0] = null;                          //Tracking moving, captured (if applicable) piece
        capturedPiece[0] = null;

        Scanner input = new Scanner(System.in);
        boolean valid = false;
        String select;
        do{                                             //Taking input to determine local or computer game, which color if playing computer
            select = "";
            System.out.print("Play locally [l] or against computer [c]? ");
            select = input.nextLine();
            if(select.equals("l") || select.equals("c")){
                valid = true;
            }
            else{
                System.out.println("Invalid input. Try again.");
            }
        }while(valid == false);

        String chosenColor = "";
        if(select.equals("c")){
            do{
                valid = false;
                System.out.print("Play white [w] or black [b]? ");
                chosenColor = input.nextLine();
                if(chosenColor.equals("w") || chosenColor.equals("b")){
                    valid = true;
                }
                else{
                    System.out.println("Invalid input. Try again.");
                }
            }while(valid == false);
        }

        if(select.equals("l")){
            localGame();    //Method for running a local game
        }
        else{
            vsComputer(chosenColor);    //Method for running a game against the computer (chosenColor argument gives computer opposite piece array of user's color)
        }
    }

    public static void vsComputer(String chosenColor){  //Still needs stalemate check
        if(chosenColor.equals("w")){
           CPU computer = new CPU("black", blackPieces); //Computer object created with opposite piece array of user
            do{
                whiteMove(); //User moves first if selected white, looks for checkmate
                if(isCheckmateBlack() == true){
                    printPosition();
                    System.out.println("Checkmate. 1 - 0");
                    return;
                }                                    
                computer.CPUmove(squareArray, movingPiece, capturedPiece);  //CPU moves, looks for checkmate
                if(isCheckmateWhite() == true){
                    printPosition();
                    System.out.println("Checkmate. 0 - 1");
                    return;
                }
            }while(true);
        }
        else{
            CPU computer = new CPU("white", whitePieces);    //Reversed order for CPU playing white
            do{
                computer.CPUmove(squareArray, movingPiece, capturedPiece);
                if(isCheckmateBlack() == true){
                    printPosition();
                    System.out.println("Checkmate. 1 - 0");
                    return;
                }
                blackMove();
                if(isCheckmateWhite() == true){
                    printPosition();
                    System.out.println("Checkmate. 0 - 1");
                    return;
                }
            }while(true);
        }
    }

    public static void localGame(){
        do{
            whiteMove();                        //User playing white moves first, checkmate scan
            if(isCheckmateBlack() == true){
                printPosition();
                System.out.println("Checkmate. 1 - 0");
                return;
            }
            //Stalemate check (Still needs to be implented)
            blackMove();                         //User playing black moves, checkmate scan
            if(isCheckmateWhite() == true){
                    printPosition();
                    System.out.println("Checkmate. 0 - 1");
                    return;
                }
            //Stalemate check

        } while(true);
    }

    public static void whiteMove(){ //For user playing white,
        printPosition();            //Position is printed
        boolean legal = true;
        do{
            System.out.println("White to play.");   //Prompt user for move with "move" method until move is input which does not leave white in check
            legal = true;
            move("white");

            if(isCheckWhite()){
                legal = false;
                rewind(fromSquare, toSquare);   //If white's still in check, the move was illegal, so rewind the move, prompt again

                System.out.println("Oops! You hung your king. Please try again.");
            }
        } while(legal == false);
            
    }

    public static void blackMove(){ //Identical to whiteMove with colors reversed
        printPosition();
        boolean legal = true;
        do{
            System.out.println("Black to play.");
            legal = true;
            move("black");

            if(isCheckBlack()){
                legal = false;
                rewind(fromSquare, toSquare);

                System.out.println("Oops! You hung your king. Please try again.");
            }
        } while(legal == false);
    }

    public static void move(String color){          //Method for prompting a user move
        Scanner input = new Scanner(System.in);

        movingPiece[0] = null;                      //Reset moving piece tracker
        fromSquare = currentPosition(color);        //Run method getting input for the square being moved from

        //System.out.println(Arrays.toString(movingPiece[0].getLegalMoves(squareArray))); (FOR DEBUGGING - display legal moves array for selected piece)

        toSquare = intendedPosition(color, fromSquare); //Method getting input for square being moved to

        movingPiece[0].move(fromSquare, toSquare, squareArray, movingPiece, capturedPiece); //Moves piece from current position to intended position
    }

    public static boolean isCheckWhite(){                       //Check scan for white
        for(Piece piece: blackPieces){                          //For every black piece,
            for(int move : piece.getCaptureMoves(squareArray)){ //For every legal move (actually every move, illegal moves have value -1, where captured pieces go, so the king can't be there),
                if(move == whiteKing.getPosition()){            //If the legal move is the same as the white king's position,
                    return true;                                //It's check
                }
            }
        }
        return false;                                           //Otherwise, no check
    }
    public static boolean isCheckBlack(){                       //Identical to isCheckwhite with reversed colors
        for(Piece piece: whitePieces){
            for(int move : piece.getCaptureMoves(squareArray)){
                if(move == blackKing.getPosition()){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isCheckmateWhite(){   //Scanning for whether white is checkmated
        if(isCheckWhite()){                      //If white is in check,
            for(Piece piece : whitePieces){      //For all white pieces,
                fromSquare = piece.getPosition();
                movingPiece[0] = piece;

                for(int move : piece.getLegalMoves(squareArray)){   //For every legal move,
                    if(!(move == -1)){
                        piece.move(fromSquare, move, squareArray, movingPiece, capturedPiece); //Play the mve

                        if(isCheckWhite()){             //If it's still check, rewind the move
                            rewind(fromSquare, move);
                        }
                        else{                           //If white isn't in check anymore, rewind the move, return false because white has a move to get out of check
                            rewind(fromSquare, move);
                            return false;
                        }
                    }
                }
            }
        }
        else{   //If white isn't in check it isn't checkmate
            return false;
        }
        return true;    //If none of white's moves get out of check, it's checkmate
    }

    public static boolean isCheckmateBlack(){   //Identical to isCheckmateWhite with reversed colors
        if(isCheckBlack()){
            for(Piece piece : blackPieces){
                fromSquare = piece.getPosition();
                movingPiece[0] = piece;

                for(int move : piece.getLegalMoves(squareArray)){
                    if(!(move == -1)){
                        piece.move(fromSquare, move, squareArray, movingPiece, capturedPiece);

                        if(isCheckBlack()){
                           rewind(fromSquare, move);
                        }
                        else{
                            rewind(fromSquare, move);
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

    public static void rewind(int fromSquare, int move){    //Method for rewinding previous move (used for user inputs which leave them in check, checkmate checks)
        movingPiece[0].place(fromSquare);                   //Place the piece last moved on the square it came from
        squareArray[fromSquare].setOccupant(movingPiece[0]); //Set that square's occupant to the moving piece

        if(!(capturedPiece[0] == null)){    //If a piece was captured,
            capturedPiece[0].place(move);   //Place it on the square that was moved to
            squareArray[move].setOccupant(capturedPiece[0]);    //Set that square's occupant to the formerly captured piece
        }
        else{
            squareArray[move].setOccupant(null);    //If there wasn't a piece captured, set the square that was moved to's occupant to null
        }
    }
        
    public static void printPosition(){ //Method for printing position to terminal
        for(int multiplier = 8; multiplier > 0; multiplier --){ //Starting with multiplier 8, decrementing
            for(int i = (multiplier - 1)*8; i < multiplier*8; i++){ //Increment through 8 times (this goes from 56-63, then 48-55, and so on, so that the back ranks are printed first)
                Square square = squareArray[i];                 //Print square's position code
                System.out.print(square.getUnicode());
            }
            System.out.println();
        }
    }

    public static int currentPosition(String color){    //Getting user input for moving piece's position
        Scanner input = new Scanner(System.in);
        String fromSquareString;
        boolean validInput;
        int fromSquare1 = -1;

        do{
            validInput = true;
            System.out.print("Current position of moving piece: ");
            fromSquareString = input.nextLine();
            //int rankInt = (int)fromSquareString.charAt(1) - 48;

            //Checking to see whether the input is an actual square on the board
            if(fromSquareString.equals(null) || !(fromSquareString.length() == 2) || fileToInt(fromSquareString.charAt(0)) == 0 || (int)fromSquareString.charAt(1) - 48 < 1 || (int)fromSquareString.charAt(1) - 48 > 8){
                validInput = false;
                System.out.println("Incorrect notation. Please try again.");
            }
            else{ //If it is, check whether the selected piece has any legal moves
                int fileValue = fileToInt(fromSquareString.charAt(0)) - 1; //File adds 0 to position for a-file, 1 for b-file, etc.

                int rankCharConvert = (int)fromSquareString.charAt(1) - 48; //-48 to line up unicode number with integer
                int rankValue = (rankCharConvert - 1) * 8; //Rank adds 0 for 1st rank, 8 for 2nd, 16 for 3rd, etc.
                        
                fromSquare1 = fileValue + rankValue;    //Selected square is sum of file and rank

                validInput = false;
                if(color.equals("white")){
                    for(Piece piece : whitePieces){ //For every white piece
                        int piecePosition = piece.getPosition();
                        if(piecePosition == fromSquare1){   //If the position's the same as the input,
                            int[] legalMoves = piece.legalMoves(squareArray);

                            for(int move : legalMoves){ //If any of the moves are legal and on the board,
                                if(move >= 0 && move < 64){
                                    validInput = true;  //Set boolean to true
                                    break;
                                }
                            }
                            movingPiece[0] = piece; //Set moving piece to selected piece
                        }
                    }
                }
                else{   //Same process for black pieces
                    for(Piece piece : blackPieces){
                        int piecePosition = piece.getPosition();
                        if(piecePosition == fromSquare1){
                            int[] legalMoves = piece.legalMoves(squareArray);

                            for(int move : legalMoves){
                                if(move >= 0 && move < 64){ 
                                    validInput = true;
                                    break;
                                }
                            }
                            movingPiece[0] = piece;
                        }
                    }
                }
                if(validInput == false){    //If the input isn't valid, no pieces matched the input and had legal moves. Prompt again.
                    System.out.println("No legal moves from that square. Please try again.");
                }
            }
        } while(!validInput);
        return fromSquare1;
    }

    public static int intendedPosition(String color, int fromSquare){ //Getting user input for intended position of moving piece
        Scanner input = new Scanner(System.in);
        //String toSquareString;
        boolean validInput;
        int toSquare1 = -1;

        do{
            validInput = false;
            System.out.print("Intended position of moving piece: ");
            String toSquareString = input.nextLine();
            //int rankInt = (int)toSquareString.charAt(1) - 48;
            //System.out.println(rankInt);
            //System.out.println(toSquareString);

            //Checking whether input square is actually a square
            if(toSquareString.equals(null) || !(toSquareString.length() == 2) || (fileToInt(toSquareString.charAt(0)) == 0 || (int)toSquareString.charAt(1) - 48 < 1 || (int)toSquareString.charAt(1) - 48 > 8)){
                validInput = false;
                System.out.println("Incorrect notation. Please try again.");
            }
            else{
                int fileValue = fileToInt(toSquareString.charAt(0)) - 1;    //Same process as in currentPosition to determine square int from input like "a1" or "d7"

                int rankCharConvert = (int)toSquareString.charAt(1) - 48; //-48 to line up unicode number with integer
                int rankValue = (rankCharConvert - 1) * 8;
                            
                toSquare1 = fileValue + rankValue;


                for(int legalMove : movingPiece[0].getLegalMoves(squareArray)){     //If input square is one of the legal moves of the moving piece, valid input is true
                    if(legalMove == toSquare1){
                        validInput = true;
                        break;
                        }
                }

                if (!validInput){   //Otherwise, it isn't legal, prompt another move
                    System.out.println("That is not a legal move. Please try again.");
                }
            }
        }
        while(!validInput);
        return toSquare1;
    }

    public static int fileToInt(char file){ //Converting file letters to int
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

    public static Piece[] place(){  //Creates the 32 piece objects, puts them into appropriate arrays
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

    public static Square[] createBoard(){       //Initializing 64 square objects
        Square[] squareArray = new Square[64];

        for(int i = 0; i < 16; i++){            //First 16 squares are occupied by white pieces
            Square square = new Square(i, whitePieces[i]);
            squareArray[i] = square;
        }
        for(int i = 16; i < 48; i++){           //Next 32 are open
            Square square = new Square(i, null);
            squareArray[i] = square;
        }
        for(int i = 48; i < 64; i++){           //Last 16 are occupied by black pieces
            Square square = new Square(i, blackPieces[i - 48]);
            squareArray[i] = square;
        }
        return squareArray;                     //Returns array of all squares
    }

}
