package com.tests.chess.engine.board;

import com.chess.engine.board.Board;
import org.junit.Test;

public class BoardTest {

    @Test
    public void initialBoard(){
        final Board board = Board.createStandardBoard();
        assertEquals(board.currentPlayer().getLegalMoves().size(), 20);
        
    }
}