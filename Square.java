public class Square {
    private int squareName; //Squares have and int 0-63 denoting which square they are (a1 is square 0, a8 is square 7, h8 is square 63)
    private Piece occupant; //Squares have an occupant (null if open)
    private String unicode; //Squares have something to print (matches print code of occupant)

    public Square(int squareName, Piece occupant){ //Constructor
        this.squareName = squareName;
        this.occupant = occupant;
        if(this.occupant == null){ //If no occupant, print code is dash
            this.unicode = " - ";
        }
        else {
            this.unicode = this.occupant.getUnicode(); //If occupied, inherits print code from occupant
        }
    }

    public void vacate(){ //Vacating square sets print code to dash, occupant to null
        this.unicode = " - ";
        this.occupant = null;
    }

    public void occupy(Piece[] movingPiece, Piece[] capturedPiece){ //Occupying square
        if(!(this.occupant == null)){   //If square is occupied currently,
            capturedPiece[0] = this.occupant; //Captured piece tracker is set to current occupant
            this.occupant.captured(); //Current occupant is captured (position moved to -1)
            }
        else{
            capturedPiece[0] = null;    //If no occupant, captured piece tracker remains null
        }
        this.occupant = movingPiece[0]; //Current occupant set to piece moving in
        this.unicode = movingPiece[0].getUnicode(); //Print code inherited from occupant
    }

    public void setOccupant(Piece piece){   //Method for manually setting occupant
        this.occupant = piece;  //Occupant is argument of method
        if(!(piece == null)){
            this.unicode = piece.getUnicode(); //Setting print code
        }
        else{
            this.unicode = " - ";   //If argument is null, print code is dashes
        }
    }

    //Get methods
    public int getSquareName(){
        return this.squareName;
    }
    public String getSquareStatus(){
        if(this.occupant == null){
            return "open";
        }
        return this.occupant.getColor();
    }
    public String getUnicode(){
        return this.unicode;
    }
    public Piece getOccupant(){
        return this.occupant;
    }
}
