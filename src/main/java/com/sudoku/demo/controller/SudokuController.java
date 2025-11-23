package com.sudoku.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sudoku.demo.model.PuzzleDTO;
import com.sudoku.demo.service.SudokuService;



@RestController
@RequestMapping("/api/sudoku")
@CrossOrigin(origins = "*")
public class SudokuController {

    private final SudokuService service;

    public SudokuController(SudokuService service) {
        this.service = service;
    }

    // GET puzzle?difficulty=easy|medium|hard
    @GetMapping("/puzzle")
    public ResponseEntity<PuzzleDTO> getPuzzle(@RequestParam(required = false) String difficulty) {
        int[][] puzzle = service.generatePuzzle(difficulty);
        return ResponseEntity.ok(new PuzzleDTO(puzzle));
    }

    // POST solve -> renvoie solution
    @PostMapping("/solve")
    public ResponseEntity<PuzzleDTO> solve(@RequestBody PuzzleDTO payload) {
        int[][] board = payload.getGrid();
        int[][] copy = service.copyBoard(board);
        boolean solved = service.solve(copy);
        if (!solved) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new PuzzleDTO(copy));
    }

    // POST check -> body candidate grid, renvoie { valid: true/false }
    @PostMapping("/check")
    public ResponseEntity<?> check(@RequestBody PuzzleDTO payload) {
        boolean ok = service.checkSolution(payload.getGrid());
        return ResponseEntity.ok(java.util.Map.of("valid", ok));
    }
}