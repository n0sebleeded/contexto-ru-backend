package com.example.rucontextobackend.repository;

import com.example.rucontextobackend.domain.Word;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WordRepository extends CrudRepository<Word,Long> {
    Optional<Word> findWordByDate(LocalDate date);
}
