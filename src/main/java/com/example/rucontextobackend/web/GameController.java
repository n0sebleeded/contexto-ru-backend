package com.example.rucontextobackend.web;

import com.example.rucontextobackend.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/similarity")
public class GameController {
    private final GameService gameService;
    
    @GetMapping("/{userWord}")
    public ResponseEntity<?> getSimilarity(
           @PathVariable String userWord
    ){
        try{
            Integer similarity = gameService.getWordSimilarity(userWord);
            return new ResponseEntity<>(similarity, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{userWord}/{date}")
    public ResponseEntity<?> getSimilarityByDate(
            @PathVariable String userWord, @PathVariable LocalDate date
    ){
        try{
            Integer similarity = gameService.getWordSimilarity(userWord, date);
            return new ResponseEntity<>(similarity, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
