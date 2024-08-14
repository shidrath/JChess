package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {8};

    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.PAWN, piecePosition, pieceAlliance,true);
    }

    public Pawn(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE){
            final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset); //if its black it adds 8 and if its  white it gives -8
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }

            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){ // pawn moves one tile forward

                if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
                } else {
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }
            } else if(currentCandidateOffset == 16 && this.isFirstMove() &&       // pawn jumps two tiles after on the first attempt
                    (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack() ||
                    (BoardUtils.SEVENTH_ROW[this.piecePosition]&& this.getPieceAlliance().isWhite()))){
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection()*8);
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                   !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new PawnJump(board,this,candidateDestinationCoordinate));
                }
            } else if(currentCandidateOffset == 7 &&
            !((BoardUtils.EIGHT_COLUMN[this.piecePosition]&& this.pieceAlliance.isWhite()) || //if you are not white and the pawn is in eight column
                              (BoardUtils.FIRST_COLUMN[this.piecePosition]&&this.pieceAlliance.isBlack()))){ //if you are not black and the pawn is in first column
                    if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){ // if there is oponents pawn there
                        final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                            if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                                legalMoves.add( new PawnPromotion (new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                            } else {
                                legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                            }
                        }
                    } else if(board.getEnPassantPawn() != null){
                        if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))){
                            final Piece pieceOnCandidate = board.getEnPassantPawn();
                            if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                                legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                            }
                        }
                    }
            }else if(currentCandidateOffset == 9 &&
                    !((BoardUtils.EIGHT_COLUMN[this.piecePosition]&& this.pieceAlliance.isBlack()) || //if you are not black and the pawn is in eight column
                    (BoardUtils.FIRST_COLUMN[this.piecePosition]&&this.pieceAlliance.isWhite()))){ // if you are white and the pawn is n the first column
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                            legalMoves.add( new PawnPromotion (new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));

                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }else if(board.getEnPassantPawn() != null){
                    if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))){
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }

        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }

    public Piece getPromotionPiece(){
        return new Queen(this.piecePosition,this.pieceAlliance, false);
    }
}
