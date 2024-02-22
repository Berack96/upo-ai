package net.berack.upo.ai.problem1;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static net.berack.upo.ai.problem1.Puzzle8.Move.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class TestPuzzle {

    private int[] goalArray = new int[] {0,1,2,3,4,5,6,7,8};

    @Test
    public void testContructors() {
        var puzzle = new Puzzle8(goalArray);

        for(var i = 0; i < goalArray.length; i++)
            assertEquals(goalArray[i], puzzle.get(i), "Error in initialization");

        var puzzle2 = new Puzzle8(goalArray);
        assertEquals(puzzle, puzzle2, "Error in equality");

    }

    @Test
    public void testMoves() {
        var puzzle = new Puzzle8(goalArray);

        var moves = new Puzzle8.Move[] {RIGHT, RIGHT, DOWN, LEFT, LEFT, DOWN, RIGHT, RIGHT, UP};
        var avail = new Puzzle8.Move[][] {
            {DOWN, RIGHT},
            {DOWN, LEFT, RIGHT},
            {DOWN, LEFT},
            {UP, DOWN, LEFT},
            {UP, DOWN, LEFT, RIGHT},
            {UP, DOWN, RIGHT},
            {UP, RIGHT},
            {UP, LEFT, RIGHT},
            {UP, LEFT}
        };
        var after = new int[][] {
            {1,0,2,3,4,5,6,7,8},
            {1,2,0,3,4,5,6,7,8},
            {1,2,5,3,4,0,6,7,8},
            {1,2,5,3,0,4,6,7,8},
            {1,2,5,0,3,4,6,7,8},
            {1,2,5,6,3,4,0,7,8},
            {1,2,5,6,3,4,7,0,8},
            {1,2,5,6,3,4,7,8,0},
            {1,2,5,6,3,0,7,8,4}
        };

        for(var i = 0; i < moves.length; i++) {
            var valid = puzzle.availableMoves();

            Arrays.sort(avail[i]);
            Arrays.sort(valid);
            assertArrayEquals(avail[i], valid, "Available moves not corrected at iteration " + i);

            puzzle.move(moves[i]);
            assertEquals(new Puzzle8(after[i]), puzzle, "Puzzle Equality not corrected at iteration " + i);
        }
    }

    @Test
    public void testMoveInit() {
        var puzzle = new Puzzle8(goalArray);
        var moves = new Puzzle8.Move[] {RIGHT, DOWN, LEFT, UP, DOWN, RIGHT, UP, LEFT};

        for(var move : moves) puzzle = new Puzzle8(puzzle, move);
        assertEquals(new Puzzle8(goalArray), puzzle, "After useless moves the puzzle must be the same as before!");
    }

    @Test
    public void testSolvable() {
        var solvable = new Puzzle8(5,2,8,4,1,7,0,3,6);
        var unsolvable = new Puzzle8(1,2,3,4,5,6,0,8,7);

        assertTrue(solvable.isSolvable());
        assertFalse(unsolvable.isSolvable());

        var goal = new Puzzle8(0,1,2,3,4,5,6,7,8);

        solvable = new Puzzle8(1,0,2,3,4,5,6,7,8);
        unsolvable = new Puzzle8(2,1,0,3,4,5,6,7,8);

        assertTrue(solvable.isSolvable(goal));
        assertFalse(unsolvable.isSolvable(goal));
    }

    @Test
    public void testSolveSimple() {
        var goal = new Puzzle8(goalArray);

        assertEquals(0, goal.solve(goal).size());

        var puzzle = new Puzzle8(1,0,2,3,4,5,6,7,8);
        var solution = new Puzzle8.Move[] {LEFT};
        var actual = puzzle.solve(goal).toArray(new Puzzle8.Move[0]);

        assertArrayEquals(solution, actual);

        for(var move : actual) puzzle.move(move);
        assertEquals(new Puzzle8(goalArray), puzzle);
    }

    @Test
    public void testSolveSimple2() {
        var puzzle = new Puzzle8(1,2,5,4,0,8,3,6,7);
        var goal = new Puzzle8(goalArray);
        var solution = new Puzzle8.Move[] {LEFT, DOWN, RIGHT, RIGHT, UP, UP, LEFT, LEFT};
        var actual = puzzle.solve(goal).toArray(new Puzzle8.Move[0]);

        assertArrayEquals(solution, actual);

        for(var move : actual) puzzle.move(move);
        assertEquals(new Puzzle8(goalArray), puzzle);
    }

    @Test
    public void testSolve() {
        var puzzle = new Puzzle8(3,5,6,1,2,4,0,7,8);
        var puzzleGoal = new Puzzle8(1,2,3,4,5,6,7,8,0);
        var actual = puzzle.solve(puzzleGoal).toArray(new Puzzle8.Move[0]);

        for(var move : actual) puzzle.move(move);
        assertEquals(puzzleGoal, puzzle);
    }

    @Test
    public void testSolveRand() {
        var goal = new Puzzle8(Puzzle8.DEFAULT_GOAL);

        var puzzle = new Puzzle8();
        while(!puzzle.isSolvable(goal)) puzzle.shuffle();

        var actions = puzzle.solve(goal);
        for(var move : actions) puzzle.move(move);

        assertEquals(puzzle, goal);
    }

    @Test
    public void testSolveHard() {
        var goal = new Puzzle8(Puzzle8.DEFAULT_GOAL);
        var puzzle = new Puzzle8(0,5,2,4,1,7,3,8,6);
        var actions = puzzle.solve().toArray(new Puzzle8.Move[0]);
        for(var move : actions) puzzle.move(move);
        assertEquals(puzzle, goal);

        puzzle = new Puzzle8(0,8,3,7,5,2,6,1,4);
        actions = puzzle.solve().toArray(new Puzzle8.Move[0]);
        for(var move : actions) puzzle.move(move);
        assertEquals(puzzle, goal);
    }

    @Test
    public void testShuffleBlankPosition() {
        var puzzle = new Puzzle8(Puzzle8.DEFAULT_GOAL);
        var tot = Puzzle8.LENGTH * Puzzle8.LENGTH;

        for(var i = 0; i < 1000; i++) {
            puzzle.shuffle();

            var blank = 10;
            for(var j = 0; j < tot; j++)
                if(puzzle.get(j) == 0)
                    blank = j;

            assertEquals(blank, puzzle.getBlankPosition());
        }
    }
}
