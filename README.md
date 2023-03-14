# JavaChess
CMSC-150 Final Project  
This project runs local chess games, and also includes a computer opponent. It uses the Scanner, Arrays, and Random classes. I created the classes for the squares, pieces, and CPU myself, and also created the methods which run the game.

The CPU opponent is not fully functional - it doesn't know how to get out of check.  
Neither version features castling, draws, en passant, or pawn promotion yet.  


To make a move, first input the square that the piece you would like to move currently occupies.  
Second, input the square that you would like to move that piece to.  

For both inputs, use the board notation shown below:  

      Board notation  
8  
7  
6  
5  
4  
3  
2  
1  
 a  b  c  d  e  f  g  h

The board is from the perspective of the white pieces; when playing the black pieces, your pieces will still be displayed at the top.  

Ex.  
If you would like to play 1.e4, first input "e2" (the square the pawn currently occupies), and then "e4" (the square you are moving to).  
If you would like to play 1...Nf6, first input "g8" (the square the knight currently occupies), and then "f6" (the square you are moving to).  
