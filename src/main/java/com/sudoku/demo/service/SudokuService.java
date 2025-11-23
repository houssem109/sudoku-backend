package com.sudoku.demo.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class SudokuService {

    private final Random rand = new Random();

    // Génère un puzzle en créant une grille complète puis en supprimant des cases
    public int[][] generatePuzzle(String difficulty) {
        int[][] board = new int[9][9];
        fillBoard(board);

        String diff = (difficulty == null) ? "easy" : difficulty.toLowerCase();

        int removals = switch (diff) {
            case "beginner" -> 25;  // very easy (more numbers shown)
            case "easy"     -> 35;
            case "medium"   -> 45;
            case "hard"     -> 55;
            case "expert"   -> 60;  // fewer clues
            case "master"   -> 63;
            case "extreme"  -> 66;  // VERY few clues
            default         -> 40;
        };

        removeNumbers(board, removals);
        return board;
    }

    // Vérifie si une grille est complètement et correctement résolue
    public boolean checkSolution(int[][] candidate) {
        // Check rows/cols/blocks and filled
        for (int i = 0; i < 9; i++) {
            boolean[] row = new boolean[10];
            boolean[] col = new boolean[10];
            for (int j = 0; j < 9; j++) {
                int rv = candidate[i][j];
                int cv = candidate[j][i];
                if (rv < 1 || rv > 9 || row[rv]) return false;
                row[rv] = true;
                if (cv < 1 || cv > 9 || col[cv]) return false;
                col[cv] = true;
            }
        }
        for (int bi = 0; bi < 9; bi += 3) {
            for (int bj = 0; bj < 9; bj += 3) {
                boolean[] block = new boolean[10];
                for (int i = 0; i < 3; i++) for (int j = 0; j < 3; j++) {
                    int v = candidate[bi+i][bj+j];
                    if (v < 1 || v > 9 || block[v]) return false;
                    block[v] = true;
                }
            }
        }
        return true;
    }

    // Solveur (backtracking). Retourne true si solved in place.
    public boolean solve(int[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    for (int val = 1; val <= 9; val++) {
                        if (isSafe(board, r, c, val)) {
                            board[r][c] = val;
                            if (solve(board)) return true;
                            board[r][c] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    // --- Helpers ---

    private void fillBoard(int[][] board) {
        // Fill using backtracking but with randomized order
        solveRandom(board);
    }

    private boolean solveRandom(int[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    List<Integer> nums = new ArrayList<>();
                    for (int i = 1; i <= 9; i++) nums.add(i);
                    Collections.shuffle(nums, rand);
                    for (int n : nums) {
                        if (isSafe(board, r, c, n)) {
                            board[r][c] = n;
                            if (solveRandom(board)) return true;
                            board[r][c] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private void removeNumbers(int[][] board, int removals) {
        int removed = 0;
        while (removed < removals) {
            int r = rand.nextInt(9);
            int c = rand.nextInt(9);
            if (board[r][c] != 0) {
                board[r][c] = 0;
                removed++;
            }
        }
    }

    private boolean isSafe(int[][] board, int r, int c, int val) {
        for (int i = 0; i < 9; i++) {
            if (board[r][i] == val || board[i][c] == val) return false;
        }
        int br = (r/3)*3, bc = (c/3)*3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[br+i][bc+j] == val) return false;
        return true;
    }

    // Retourne une copie deep
    public int[][] copyBoard(int[][] b) {
        int[][] c = new int[9][9];
        for (int i = 0; i < 9; i++) System.arraycopy(b[i], 0, c[i], 0, 9);
        return c;
    }
}
