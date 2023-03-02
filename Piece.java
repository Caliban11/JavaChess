public class Piece {
    private String type;
    private String color;
    private int position;
    private int[] legalMoves;
    private int[] captureMoves; //For pawns
    private String unicode;
    private boolean hasMoved; //Checkmate checks break this

    public Piece(String type, String color, int position, String unicode){
        this.type = type;
        this.color = color;
        this.position = position;
        this.legalMoves = new int[28];
        clearArray(this.legalMoves);
        this.captureMoves = new int[8];
        clearArray(this.captureMoves);

        this.hasMoved = false;
        this.unicode = unicode;
    }

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

    public String toString(){
        return this.type + " " + this.color;
    }

     public int[] clearArray(int[] array){
        for(int i = 0; i < array.length; i++){
            array[i] = -1;
        }
        return array;
     }

     public void captured(){
        this.position = -1;
     }

     public void place(int toSquare){
        this.position = toSquare;
     }

    public void move(int fromSquare, int toSquare, Square[] squareArray, Piece[] movingPiece, Piece[] capturedPiece){
        this.hasMoved = true;
        this.position = toSquare;
        squareArray[fromSquare].vacate();
        squareArray[toSquare].occupy(movingPiece, capturedPiece);
     }


     public int[] legalMoves(Square[] squareArray){
        clearArray(this.legalMoves);
        clearArray(this.captureMoves);
        if(!(this.position == -1)){
            if(this.type.equals("pawn")){
                if(this.color == "white"){
                    int position1 = this.position + 8; //Forward one square
                    int position2 = this.position + 16; // Forward two squares
                    int position3 = this.position + 7; // Leftward capture
                    int position4 = this.position + 9; //Rightward capture

                    if(position1 >= 0 && position1 < 64){
                        String squareStatus1 = squareArray[position1].getSquareStatus();
                        if(squareStatus1.equals("open")){ //And no check
                            this.legalMoves[0] = position1;
                        }
                    }
                    if(position1 >= 0 && position1 < 64 && position2 >= 0 && position2 < 64){
                        String squareStatus1 = squareArray[position1].getSquareStatus();
                        String squareStatus2 = squareArray[position2].getSquareStatus();
                        if(squareStatus1.equals("open") && squareStatus2.equals("open") && this.position > 7 && this.position < 16){
                            this.legalMoves[1] = position2;
                        }
                    }
                    if(position3 >= 0 && position3 < 64){
                        String squareStatus3 = squareArray[position3].getSquareStatus();
                        if(squareStatus3.equals("black") && !(this.position % 8 == 0)){
                            this.legalMoves[2] = position3;
                            this.captureMoves[0] = position3;
                        }
                    }
                    if(position4 >= 0 && position4 < 64){
                        String squareStatus4 = squareArray[position4].getSquareStatus();
                        if(squareStatus4.equals("black") && !((this.position + 1) % 8 == 0)){
                            this.legalMoves[3] = position4;
                            this.captureMoves[1] = position4;
                        }
                    }
                    return this.legalMoves;
                }
                else{                              //For black
                    int position1 = this.position - 8; //Forward one square
                    int position2 = this.position - 16; // Forward two squares
                    int position3 = this.position - 9; // Leftward capture
                    int position4 = this.position - 7; //Rightward capture

                    if(position1 >= 0 && position1 < 64){
                        String squareStatus1 = squareArray[position1].getSquareStatus();
                        if(squareStatus1.equals("open")){ //And no check
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


            else if(this.type.equals("knight")){
                int[] positionArray = new int[8];
                positionArray[0] = this.position + 15; //Forward two, left one        //Might have to clear position array between calling this method
                positionArray[1] = this.position + 17; //Forward two, right one
                positionArray[2] = this.position + 6;  //Forward one, left two
                positionArray[3] = this.position + 10; //Forward one, right two
                positionArray[4] = this.position - 10; //Back one, left two
                positionArray[5] = this.position - 6;  // Back one, right two
                positionArray[6] = this.position - 17; // Back two, left one
                positionArray[7] = this.position - 15; //Back two, right one
                String[] squareStatusArray = new String[8];

                if(this.color == "white"){
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
                this.captureMoves = this.legalMoves;
                return this.legalMoves;
            }

            else if(this.type.equals("bishop")){
                for(int i = 0; i < 14; i++){
                this.legalMoves[i] = diagonalLegality(squareArray)[i];
                }
                this.captureMoves = this.legalMoves;
                return this.legalMoves;
            }

            else if(this.type.equals("rook")){
                for(int i = 0; i < 14; i++){
                this.legalMoves[i] = orthogonalLegality(squareArray)[i];
                }
                this.captureMoves = this.legalMoves;
                return this.legalMoves;
            }

            else if(this.type.equals("queen")){
                int[] diagonalArray = diagonalLegality(squareArray);
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

            else if(this.type.equals("king")){
                int[] positionArray = new int[8];
                positionArray[0] = this.position + 1; //Right      //Might have to clear position array between calling this method
                positionArray[1] = this.position - 1; //Left
                positionArray[2] = this.position + 8; //Forward
                positionArray[3] = this.position - 8; //Back
                positionArray[4] = this.position + 9; //Forward and right
                positionArray[5] = this.position + 7; //Forward and left
                positionArray[6] = this.position - 7; //Back and right
                positionArray[7] = this.position - 9; //Back and left
                String[] squareStatusArray = new String[8];

                if(this.color == "white"){
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

    public int[] diagonalLegality(Square[] squareArray){
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

    public int[] orthogonalLegality(Square[] squareArray){
        int phantomPosition = this.position;
        int phantomMoveCount = 0;
        int[] orthogonalLegalMovesArray = new int[14];
        clearArray(orthogonalLegalMovesArray);
        
        while(!((phantomPosition + 1) % 8 == 0)){   //Moving right
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
