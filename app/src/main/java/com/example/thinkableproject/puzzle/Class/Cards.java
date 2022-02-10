package com.example.thinkableproject.puzzle.Class;

public class Cards {
    private int[][] board;
    private int n, m;
    private boolean result;

    public Cards(int N, int M) {
        n = N; m = M;
        board = new int [n][m];
    }

    public void getNewCards() {
        board = new int [n][m];
        int boardX, boardY;
        for (int i = 0; i < (n * m - 1); i++) {
            board[i % n][i / m] = 0;
        } // init all cards to 0;
        for (int i = 0; i < (n * m); i++) {
            boardX = (int)(Math.random() * n);
            boardY = (int)(Math.random() * m);
            while(!(board[boardX][boardY] == 0)) { // if card already assign , try to find another card who's still 0.
                boardX = (int)(Math.random() * n);
                boardY = (int)(Math.random() * m);
            }
            board[boardX][boardY] = i; // assigning all cards the 0-8 numbers
        }
    }
    public void moveCards(int boardX, int boardY){
        int X0 = -1, Y0 = -1;
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                if (board[i][j] == 0) {
                    X0 = i;
                    Y0 = j;
                }
        result = false;
        if (X0 == boardX || Y0 == boardY) // if you clicked on button in the right row/column same as the 0
            if (!(X0 == boardX && Y0 == boardY)) { // if you didn't click on the 0 button
                if (X0 == boardX) // if you clicked on button on the same Y axis with 0
                    if (Y0 < boardY) // if the button bellow 0
                        for (int i = Y0 + 1; i <= boardY; i++) // number of moves.
                            board[boardX][i - 1] = board[boardX][i]; // moving the buttons
                    else // if the button above 0
                        for (int i = Y0; i > boardY; i--) // num of moves
                            board[boardX][i] = board[boardX][i - 1]; // moving the buttons

                if (Y0 == boardY) // check the same just for the x axis.
                    if (X0 < boardX)
                        for (int i = X0 + 1; i <= boardX; i++)
                            board[i-1][boardY] = board[i][boardY];
                    else
                        for (int i = X0; i > boardX; i--)
                            board[i][boardY] = board[i - 1][boardY];

                board[boardX][boardY] = 0; // placing the 0 on the button we clicked
                result = true;
            }
    }

    public boolean resultMove() {

        return result;
    }

    public boolean finished(int N, int M){
        boolean finish = false; //flag
        if (board[N - 1][M - 1] == 0) { // first check the 0 is at the end
            int a = 0;
            int b = 1; // counter
            for (int i = 0; i < N; i++)
                for (int j = 0; j < M; j++) {
                    a++;
                    if (board[i][j] == a) b++;
                }
            if (b == (N * M)) finish = true; // if b count every single card game finished.
        }
        return finish;
    }

    public int getValueBoard(int i, int j) {
        return board[i][j];
    }

}
