public class Square {
    private int squareName;
    private Piece occupant;
    private String unicode;

    public Square(int squareName, Piece occupant){
        this.squareName = squareName;
        this.occupant = occupant;
        if(this.occupant == null){
            this.unicode = " - ";
        }
        else {
            this.unicode = this.occupant.getUnicode();
        }
    }

    public void vacate(){
        this.unicode = " - ";
        this.occupant = null;
    }

    public void occupy(Piece movingPiece, Piece capturedPiece){
        if(!(this.occupant == null)){
            capturedPiece = this.occupant;
            this.occupant.captured();
            }
        else{
            capturedPiece = null;
        }
        this.occupant = movingPiece;
        this.unicode = movingPiece.getUnicode();
    }

    public void setOccupant(Piece piece){
        this.occupant = piece;
        if(!(piece == null)){
        this.unicode = piece.getUnicode();
        }
        else{
            this.unicode = " - ";
        }
    }

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
