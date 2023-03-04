public class Piece {
    private String type;
    private String color;
    private int position;
    private int[] legalMoves;
    private int[] captureMoves; //For pawns - not all legal moves are capture moves (affects checks, checkmate)
    private String unicode;
    private boolean hasMoved; //Checkmate checks break this

    public Piece(String type, String color, int position, String unicode){  //Constructor
        this.type = type;   //"pawn", "knight", etc.
        this.color = color;
        this.position = position; //0-63 (This program is one-dimensional)
        this.legalMoves = new int[28]; //New arrays have all values set to -1 to denote illegal moves
        clearArray(this.legalMoves);
        this.captureMoves = new int[28]; //8?
        clearArray(this.captureMoves);

        this.hasMoved = false; //Not functional, relevant for castling rights
        this.unicode = unicode; //Print code to give to the square the piece is on
    }

    //Get methods
    public boolean getHasMoved(){
        return this.hasMoved;
    }
    public int getPosition(){
        return this.position;
    }
    public String getColor(){
        return this.color;
    }
    public String getType(){
        return this.type;
    }
    public int[] getLegalMoves(Square[] squareArray){
        return legalMoves(squareArray);
    }
    public int[] getCaptureMoves(Square[] squareArray){
        legalMoves(squareArray);
        return this.captureMoves;
    }
    public String getUnicode(){
        return this.unicode;
    }

    public String toString(){ //For debugging, never actually print a piece
        return this.type + " " + this.color;
    }

    public int[] clearArray(int[] array){ //Sets all values in an array to -1
        for(int i = 0; i < array.length; i++){
            array[i] = -1;
        }
        return array;
    }

    public void captured(){ //Captured pieces have position -1
        this.position = -1;
    }

    public void place(int toSquare){ //Manually set position
        this.position = toSquare;
    }

    public void move(int fromSquare, int toSquare, Square[] squareArray, Piece[] movingPiece, Piece[] capturedPiece){ //Move method, arguments determine movement, allow piece trackers to be updated
        this.hasMoved = true;
        this.position = toSquare;   //Setting position
        squareArray[fromSquare].vacate(); //Clearing square that was moved from
        squareArray[toSquare].occupy(movingPiece, capturedPiece); //Occupying square being moved to
    }

    public int[] legalMoves(Square[] squareArray){ //Finding legal moves for each piece
        clearArray(this.legalMoves);    //Clearing arrays (setting everything to -1)
        clearArray(this.captureMoves);
        if(!(this.position == -1)){ //If the piece isn't captured,
            if(this.type.equals("pawn")){   //If it's a pawn,
                if(this.color == "white"){ //If it's white (going towards 8th rank)
                    int position1 = this.position + 8; //Forward one square
                    int position2 = this.position + 16; // Forward two squares
                    int position3 = this.position + 7; // Leftward capture
                    int position4 = this.position + 9; //Rightward capture

                    if(position1 >= 0 && position1 < 64){ //If potential move is to an existing square
                        String squareStatus1 = squareArray[position1].getSquareStatus(); //Status is color of occupying piece
                        if(squareStatus1.equals("open")){ //Move is legal if square is open
                            this.legalMoves[0] = position1; //Move added to legal moves array (NOT capture moves array)
                        }
                    }
                    if(position1 >= 0 && position1 < 64 && position2 >= 0 && position2 < 64){ //If potential square exists
                        String squareStatus1 = squareArray[position1].getSquareStatus();
                        String squareStatus2 = squareArray[position2].getSquareStatus();
                        if(squareStatus1.equals("open") && squareStatus2.equals("open") && this.position > 7 && this.position < 16){ //If both this square and square in front are open, move is legal
                            this.legalMoves[1] = position2;
                        }
                    }
                    if(position3 >= 0 && position3 < 64){
                        String squareStatus3 = squareArray[position3].getSquareStatus();
                        if(squareStatus3.equals("black") && !(this.position % 8 == 0)){ //If black is diagonal, pawn can capture (Position can't be divisible by 8 because the pawn would be on the a-file and shouldn't be able to teleport to the h-file to capture)
                            this.legalMoves[2] = position3;
                            this.captureMoves[0] = position3; //Also adding to capture moves array (King will be in check if pawn sees it diagonally)
                        }
                    }
                    if(position4 >= 0 && position4 < 64){
                        String squareStatus4 = squareArray[position4].getSquareStatus();
                        if(squareStatus4.equals("black") && !((this.position + 1) % 8 == 0)){ //Capture (Pawn can't be on h-file and teleport to a-file, so position + 1 cannot be divisible by 8)
                            this.legalMoves[3] = position4;
                            this.captureMoves[1] = position4; //Adding to capture moves as well
                        }
                    }
                    return this.legalMoves;
                }
                else{                              //For black, movement is reversed, everything else is same with colors reversed
                    int position1 = this.position - 8; //Forward one square
                    int position2 = this.position - 16; // Forward two squares
                    int position3 = this.position - 9; // Leftward capture
                    int position4 = this.position - 7; //Rightward capture

                    if(position1 >= 0 && position1 < 64){
                        String squareStatus1 = squareArray[position1].getSquareStatus();
                        if(squareStatus1.equals("open")){
                            this.legalMoves[0] = position1;
                        }
                    }
                    if(position1 >= 0 && position1 < 64 && position2 >=0 && position2 < 64){
                        String squareStatus1 = squareArray[position1].getSquareStatus();
                        String squareStatus2 = squareArray[position2].getSquareStatus();
                        if(squareStatus1.equals("open") && squareStatus2.equals("open") && this.position > 47 && this.position < 56){
                            this.legalMoves[1] = position2;
                        }
                    }
                    if(position3 >= 0 && position3 < 64){
                        String squareStatus3 = squareArray[position3].getSquareStatus();
                        if(squareStatus3.equals("white") && !(this.position % 8 == 0)){
                            this.legalMoves[2] = position3;
                            this.captureMoves[0] = position3;
                        }
                    }
                    if(position4 >= 0 && position4 < 64){
                        String squareStatus4 = squareArray[position4].getSquareStatus();
                        if(squareStatus4.equals("white") && !((this.position + 1) % 8 == 0)){
                            this.legalMoves[3] = position4;
                            this.captureMoves[1] = position4;
                        }
                    }
                    return this.legalMoves;
                }
            }


            else if(this.type.equals("knight")){    //Legal knight moves
                int[] positionArray = new int[8];
                positionArray[0] = this.position + 15; //Forward two, left one
                positionArray[1] = this.position + 17; //Forward two, right one
                positionArray[2] = this.position + 6;  //Forward one, left two
                positionArray[3] = this.position + 10; //Forward one, right two
                positionArray[4] = this.position - 10; //Back one, left two
                positionArray[5] = this.position - 6;  // Back one, right two
                positionArray[6] = this.position - 17; // Back two, left one
                positionArray[7] = this.position - 15; //Back two, right one
                String[] squareStatusArray = new String[8];

                if(this.color == "white"){  //For white,
                    for(int i = 0; i < 8; i++){
                        if(positionArray[i] > -1 && positionArray[i] < 64){ //If the potential move is to an existing square,
                            squareStatusArray[i] = squareArray[positionArray[i]].getSquareStatus(); //Get the status of that square, add to array
                        }
                        else{
                            squareStatusArray[i] = "white"; //Otherwise, set the status to white so the knight can't move there
                        }

                        if(!squareStatusArray[i].equals("white") && positionArray[i] > -1 && positionArray[i] < 64){ //If the square isn't occupied by white, it's legal
                            this.legalMoves[i] = positionArray[i]; //Add to legal moves
                        }
                    }
                }
                else{   //For black, same thing with colors reversed
                    for(int i = 0; i < 8; i++){
                        if(positionArray[i] > -1 && positionArray[i] < 64){
                            squareStatusArray[i] = squareArray[positionArray[i]].getSquareStatus();
                        }
                        else{
                            squareStatusArray[i] = "black";
                        }

                        if(!squareStatusArray[i].equals("black") && positionArray[i] > -1 && positionArray[i] < 64){
                            this.legalMoves[i] = positionArray[i];
                        }
                    }
                }
                this.captureMoves = this.legalMoves; //Knight legal moves are also it's capture moves (Same for all other pieces except pawn (King? Castling isn't a capture move but isn't implemented yet... Think is irrelevant because rook watches the square anyway))
                return this.legalMoves;
            }

            else if(this.type.equals("bishop")){ //Bishop moves
                for(int i = 0; i < 14; i++){
                this.legalMoves[i] = diagonalLegality(squareArray)[i]; //Bishop moves determined by checking diagonal, run by a method
                }
                this.captureMoves = this.legalMoves; //Capture moves are legal moves
                return this.legalMoves;
            }

            else if(this.type.equals("rook")){ //Rook moves
                for(int i = 0; i < 14; i++){
                this.legalMoves[i] = orthogonalLegality(squareArray)[i]; //Rook moves are determined by checking files, ranks, run by a method
                }
                this.captureMoves = this.legalMoves;
                return this.legalMoves;
            }

            else if(this.type.equals("queen")){ //Queen moves
                int[] diagonalArray = diagonalLegality(squareArray); //Queen moves are both diagonal and orthogonal, run by methods to set each half of legal moves array (28 is maximumm possible queen moves)
                int[] orthogonalArray = orthogonalLegality(squareArray);

                for (int i = 0; i < 14; i++){
                    this.legalMoves[i] = diagonalArray[i];
                }
                for(int i = 14; i < 28; i++){
                    this.legalMoves[i] = orthogonalArray[i - 14];
                }
                this.captureMoves = this.legalMoves;
                return this.legalMoves;
            }

            else if(this.type.equals("king")){ //King moves (Still need to implement castling)
                int[] positionArray = new int[8];
                positionArray[0] = this.position + 1; //Right
                positionArray[1] = this.position - 1; //Left
                positionArray[2] = this.position + 8; //Forward
                positionArray[3] = this.position - 8; //Back
                positionArray[4] = this.position + 9; //Forward and right
                positionArray[5] = this.position + 7; //Forward and left
                positionArray[6] = this.position - 7; //Back and right
                positionArray[7] = this.position - 9; //Back and left
                String[] squareStatusArray = new String[8];

                if(this.color == "white"){ //Same checks as for pawns moving forward
                    for(int i = 0; i < 8; i++){
                        if(positionArray[i] > -1 && positionArray[i] < 64){
                        squareStatusArray[i] = squareArray[positionArray[i]].getSquareStatus();
                        }
                        else{
                            squareStatusArray[i] = "white";
                        }

                        if(!squareStatusArray[i].equals("white") && positionArray[i] > -1 && positionArray[i] < 64){ //And no check
                            this.legalMoves[i] = positionArray[i];
                        }
                    }
                }
                else{   //For black
                    for(int i = 0; i < 8; i++){
                        if(positionArray[i] > -1 && positionArray[i] < 64){
                            squareStatusArray[i] = squareArray[positionArray[i]].getSquareStatus();
                        }
                        else{
                            squareStatusArray[i] = "black";
                        }
                        
                        if(!squareStatusArray[i].equals("black") && positionArray[i] > -1 && positionArray[i] < 64){ //And no check
                            this.legalMoves[i] = positionArray[i];
                        }
                    }
                }
                //if(blackPieces[15].getHasMoved() == false && blackPieces[8].getHasMoved() == false && blackPieces[12].getHasMoved() == false){
                    //Castling
                //}
                return this.legalMoves;
            }
        }
        this.captureMoves = this.legalMoves;
        return this.legalMoves;
    }

    public int[] diagonalLegality(Square[] squareArray){ //Diagonal moves (Used for bishops, queens)
        int phantomMoveCount = 0;
        int phantomPosition = this.position;
        int[] diagonalLegalMovesArray = new int[14];
        clearArray(diagonalLegalMovesArray);
        
        while(phantomPosition < 56 && !((phantomPosition + 1) % 8 == 0)){ //Moving forward and right. Stops when bishop is on h-file or 8th rank.
            phantomPosition += 9;
            if((this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("white")) || (this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("black"))){
                phantomPosition = 7; //Break out of loop immediately and don't register square as legal if you run into your own piece (either white into white or black into black)
            }
            else {
                diagonalLegalMovesArray[phantomMoveCount] = phantomPosition; //Register phantom position as legal if your own piece isn't blocking
                phantomMoveCount++;
            }
            
            if(this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("black")){
                phantomPosition = 7; //Break out of while loop if square is occupied by opponent (move is still registered as legal)
            }
            else if(this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("white")){
                phantomPosition = 7; //Break out of while loop if square is occupied by opponent (move is still registered as legal)
            }
        }

        phantomPosition = this.position;
        while(phantomPosition < 56 && !(phantomPosition % 8 == 0)){ //Moving forward and left. Stops when bishop is on a-file or 8th rank, or when the current square is occupied.
            phantomPosition += 7;
            if((this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("white")) || (this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("black"))){
                phantomPosition = 8; //Break out of loop immediately and don't register square as legal if you run into your own piece (either white into white or black into black)
            }
            else {
                diagonalLegalMovesArray[phantomMoveCount] = phantomPosition; //Register phantom position as legal if your own piece isn't blocking
                phantomMoveCount++;
            }
            
            if(this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("black")){
                phantomPosition = 8; //Break out of while loop if square is occupied by opponent (move is still registered as legal)
            }
            else if(this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("white")){
                phantomPosition = 8; //Break out of while loop if square is occupied by opponent (move is still registered as legal)
            }
        }

        phantomPosition = this.position;
        while(phantomPosition > 7 && !((phantomPosition + 1) % 8 == 0)){ //Moving back and right. Stops when bishop is on h-file or 1st rank, or when the current square is occupied.
            phantomPosition -= 7;
            if((this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("white")) || (this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("black"))){
                phantomPosition = 7; //Break out of loop immediately and don't register square as legal if you run into your own piece (either white into white or black into black)
            }
            else {
                diagonalLegalMovesArray[phantomMoveCount] = phantomPosition; //Register phantom position as legal if your own piece isn't blocking
                phantomMoveCount++;
            }
            
            if(this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("black")){
                phantomPosition = 7; //Break out of while loop if square is occupied by opponent (move is still registered as legal)
            }
            else if(this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("white")){
                phantomPosition = 7; //Break out of while loop if square is occupied by opponent (move is still registered as legal)
            }
        }

        phantomPosition = this.position;
        while(phantomPosition > 7 && !(phantomPosition % 8 == 0)){ //Moving back and left. Stops when bishop is on a-file or 1st rank, or when the current square is occupied.
            phantomPosition -= 9;
            if((this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("white")) || (this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("black"))){
                phantomPosition = 8; //Break out of loop immediately and don't register square as legal if you run into your own piece (either white into white or black into black)
            }
            else {
                diagonalLegalMovesArray[phantomMoveCount] = phantomPosition; //Register phantom position as legal if your own piece isn't blocking
                phantomMoveCount++;
            }
            
            if(this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("black")){
                phantomPosition = 8; //Break out of while loop if square is occupied by opponent (move is still registered as legal)
            }
            else if(this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("white")){
                phantomPosition = 8; //Break out of while loop if square is occupied by opponent (move is still registered as legal)
            }
        }
        return diagonalLegalMovesArray;
    }

    public int[] orthogonalLegality(Square[] squareArray){ //Orthogonal moves (Used for rooks, queens)
        int phantomPosition = this.position;
        int phantomMoveCount = 0;
        int[] orthogonalLegalMovesArray = new int[14];
        clearArray(orthogonalLegalMovesArray);
        
        while(!((phantomPosition + 1) % 8 == 0)){   //Moving right. Similar process to diagonalLegality
            phantomPosition ++;
            if((this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("white")) || (this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("black"))){
                phantomPosition = 7;
            }
            else {
                orthogonalLegalMovesArray[phantomMoveCount] = phantomPosition;
                phantomMoveCount++;
            }

            if(this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("black")){
                phantomPosition = 7;
            }
            else if(this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("white")){
                phantomPosition = 7;
            }
        }

        phantomPosition = this.position;                 
        while(!(phantomPosition % 8 == 0)){         //Moving left
            phantomPosition --;
            if((this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("white")) || (this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("black"))){
                phantomPosition = 8;
            }
            else {
                orthogonalLegalMovesArray[phantomMoveCount] = phantomPosition;
                phantomMoveCount++;
            }

            if(this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("black")){
                phantomPosition = 8;
            }
            else if(this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("white")){
                phantomPosition = 8;
            }
        }

        phantomPosition = this.position;
        while(phantomPosition < 56){            //Moving forward
            phantomPosition += 8;
            if((this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("white")) || (this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("black"))){
                phantomPosition = 56;
            }
            else {
                orthogonalLegalMovesArray[phantomMoveCount] = phantomPosition;
                phantomMoveCount++;
            }

            if(this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("black")){
                phantomPosition = 56;
            }
            else if(this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("white")){
                phantomPosition = 56;
            }
        }

        phantomPosition = this.position;
        while(phantomPosition > 7){         //Moving back
            phantomPosition -= 8;
            if((this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("white")) || (this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("black"))){
                phantomPosition = 7;
            }
            else {
                orthogonalLegalMovesArray[phantomMoveCount] = phantomPosition;
                phantomMoveCount++;
            }

            if(this.color.equals("white") && squareArray[phantomPosition].getSquareStatus().equals("black")){
                phantomPosition = 7;
            }
            else if(this.color.equals("black") && squareArray[phantomPosition].getSquareStatus().equals("white")){
                phantomPosition = 7;
            }
        }
        return orthogonalLegalMovesArray;
    }
}
