package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

/**
 * When you make a move the transition from one board to another and the information that you want to carry
 */
public class MoveTransition {
    // things we want to capture in a move transition
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;


    public MoveTransition(final Board transitionBoard, final Move move, final MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus(){
        return this.moveStatus;
    }

    public Board getTransitionBoard(){
        return this.transitionBoard;
    }
}
