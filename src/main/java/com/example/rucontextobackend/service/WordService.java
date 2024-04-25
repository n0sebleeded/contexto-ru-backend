package com.example.rucontextobackend.service;

import com.example.rucontextobackend.domain.Word;
import com.example.rucontextobackend.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.io.*;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordService {
    private final WordRepository wordRepository;
    
    @Value("${WORDS_PATH}")
    private String inputPath;
    
    @Value("${WORKED_WORDS_PATH}")
    private String outputPath;
    
    public Word getWordByDate(
            LocalDate date
    ) throws NoSuchElementException {
        return wordRepository.findWordByDate(date)
                .orElseThrow(() -> new NoSuchElementException("There was no word that day"));
    }
    
    public Word getNewestWord() throws NoSuchElementException {
        return wordRepository.findWordByDate(LocalDate.now())
                .orElseThrow(() -> new NoSuchElementException("There was no word that day"));
    }
    
    
    public void setNewWord() throws
            InstanceAlreadyExistsException,
            RuntimeException {
        
        if(wordRepository.findWordByDate(LocalDate.now()).isPresent()) {
            throw new InstanceAlreadyExistsException("The word has already been added to date");
        }

        try {
            String line = readFirstLineFromFile(inputPath)
                    .orElseThrow(() -> new IOException("The file line is empty"));
            writeToOutputFile(line, outputPath);
            removeFirstLineFromFile(inputPath);
            
            wordRepository.save(
                    Word.builder()
                            .date(LocalDate.now())
                            .value(line)
                            .build()
            );
        } catch (IOException exception){
            throw new RuntimeException("Problems reading or writing to a file");
        }
    }
    
    private Optional<String> readFirstLineFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return Optional.ofNullable(reader.readLine());
        }
    }
    
    private void writeToOutputFile(String content, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }
    
    private void removeFirstLineFromFile(String filePath) throws IOException {
        // Создание временного файла для записи оставшихся строк
        String tempFilePath = filePath + ".tmp";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath))) {
            reader.readLine(); // Пропуск первой строки
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
        
        // Переименование временного файла на место исходного
        java.io.File tempFile = new java.io.File(tempFilePath);
        java.io.File originalFile = new java.io.File(filePath);
        tempFile.renameTo(originalFile);
    }
}
