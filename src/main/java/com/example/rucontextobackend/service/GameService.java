package com.example.rucontextobackend.service;

import com.example.rucontextobackend.domain.Word;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GameService {
    private final WordService wordService;
    
    @Value("${SCRIPT_PATH}")
    private String scriptPath;
    
    @Value("${PYTHON_PATH}")
    private String pythonPath;
    
    @PostConstruct
    public void checkDayWord() {
        try {
            wordService.getWordByDate(LocalDate.now());
        } catch (NoSuchElementException noSuchElementException) {
            try {
                wordService.setNewWord();
            } catch (InstanceAlreadyExistsException instanceAlreadyExistsException){
                throw new RuntimeException("Unsuccessful attempt to replace the set word:" +
                        " the word for the day has already been set.");
            }
            
        }
    }
    
    public Integer getWordSimilarity(
            String userWord
    ) {
        Word wordToGuess = wordService.getNewestWord();
        
        try {
            String result = runPythonScript(
                    userWord, wordToGuess.getValue())
                    .replaceAll("\n","");
            
            if(result.equals("No word")){
                throw new NoSuchElementException("Bad word");
            }
            
            if(result.equals("Some error")){
                throw new RuntimeException("Some error with model");
            }
            return Integer.valueOf(result);
        } catch (Exception exception){
            throw new RuntimeException("Failed to get similarity:" +
                    exception.getLocalizedMessage());
        }
    }
    
    public Integer getWordSimilarity(
            String userWord,
            LocalDate date
    ) {
        Word wordToGuess = wordService.getWordByDate(date);
        
        try {
            String result = runPythonScript(
                    userWord, wordToGuess.getValue());
            
            if(result.equals("No word")){
                throw new NoSuchElementException("Bad word");
            }
            
            if(result.equals("Some error")){
                throw new RuntimeException("Some error with model");
            }
            
            return Integer.valueOf(result);
        } catch (Exception exception){
            throw new RuntimeException("Failed to get similarity:" +
                    exception.getLocalizedMessage());
        }
    }
    
    private String runPythonScript(
            String... args
    ) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        command.add(pythonPath);
        command.add(scriptPath);
        command.addAll(Arrays.asList(args));
        
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Script error, exit code: " + exitCode);
        }
        
        return output.toString();
    }
}
