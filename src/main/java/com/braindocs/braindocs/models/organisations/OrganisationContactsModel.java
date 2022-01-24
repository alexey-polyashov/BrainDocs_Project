package com.braindocs.braindocs.models.organisations;


import com.braindocs.braindocs.models.ContactTypeModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="organisation_contacts")
@Data
@NoArgsConstructor
public class OrganisationContactsModel {

    @Column(name="organisation")
    private Long organisation;

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
