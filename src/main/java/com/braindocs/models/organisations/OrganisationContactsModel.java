package com.braindocs.models.organisations;


import com.braindocs.models.ContactTypeModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

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
    @ColumnDefault("false")
    private Boolean marked;
}
