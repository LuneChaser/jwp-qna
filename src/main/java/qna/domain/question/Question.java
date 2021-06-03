package qna.domain.question;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Where;

import qna.CannotDeleteException;
import qna.domain.BaseEntity;
import qna.domain.answer.Answer;
import qna.domain.answer.Answers;
import qna.domain.deletehistory.DeleteHistories;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.user.User;

@Entity
@Where(clause = "deleted = false")
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Contents contents;


    @Embedded
    private Answers answers = new Answers(new ArrayList<>());

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id",
        foreignKey = @ForeignKey(name = "fk_question_writer")
    )
    private User writer;

    @Embedded
    private Deleted deleted = new Deleted(false);

    protected Question() { }

    public Question(String title, String contents) {
        this(null, new Title(title), new Contents(contents));
    }

    public Question(Long id, Title title, Contents contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question(Long id, String title, String contents) {
        this(id, new Title(title), new Contents(contents));
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public Answers getAnswers() {
        return answers;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted.value == true;
    }

    public void setDeleted(Deleted deleted) {
        this.deleted = deleted;
    }

    public void setDeleted(boolean deleted) {
        this.setDeleted(new Deleted(deleted));
    }

    public DeleteHistories deletedBy(User user) throws CannotDeleteException {
        if (this.isOwner(user) == false) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        this.setDeleted(true);
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(getId(), user, LocalDateTime.now());

        Answers answers = this.answers;
        DeleteHistories deleteHistories = answers.deletedBy(user);
        deleteHistories.add(deleteHistory);
        return deleteHistories;
    }
}