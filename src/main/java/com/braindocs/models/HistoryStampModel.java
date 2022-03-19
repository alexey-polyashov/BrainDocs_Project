package com.braindocs.models;

import com.braindocs.models.users.UserModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "operations_history")
@Data
@NoArgsConstructor
public class HistoryStampModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createTime;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserModel author;

    @Column(name = "change_type")
    private String changeType;
}
