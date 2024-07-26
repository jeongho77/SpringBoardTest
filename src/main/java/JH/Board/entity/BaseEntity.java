package JH.Board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreationTimestamp //생성됐을대 시간
    @Column(updatable = false) //수정시에 관여하지않겟다 오로지 생성때만
    private LocalDateTime createdTime;

    @UpdateTimestamp //업데이트 되었을때 시간
    @Column(insertable=false) //인설트를 할때는 시간을 관여하지않겠따
    private LocalDateTime updatedTime;
}
