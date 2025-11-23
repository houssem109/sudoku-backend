package com.sudoku.demo.model;

public class PuzzleDTO {
    // 9x9 grille, 0 = vide
    private int[][] grid;

    public PuzzleDTO() {}

    public PuzzleDTO(int[][] grid) {
        this.grid = grid;
    }

    public int[][] getGrid() { return grid; }
    public void setGrid(int[][] grid) { this.grid = grid; }
}