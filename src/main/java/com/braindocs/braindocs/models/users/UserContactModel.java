package com.braindocs.braindocs.models.users;

import com.braindocs.braindocs.models.ContactTypeModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="user_contacts")
@Data
@NoArgsConstructor
public class UserContactModel {

    @Column(name="user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="type")
    private ContactTypeModel type;

    @Column(name="present")
    private String present;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "marked")
    private Boolean marked;
}
