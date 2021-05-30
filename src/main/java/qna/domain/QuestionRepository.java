package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDeletedFalse();
    Optional<Question> findByIdAndDeletedFalse(Long id);
    void deleteByTitle(String contents);
    List<Question> findByWriterId(Long id);
}