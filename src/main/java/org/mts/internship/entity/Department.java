package org.mts.internship.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "department")
public class Department {

    @Id
    private long Id;

    @NaturalId
    private String name;

    @OneToMany(mappedBy = "department")
    private Set<Worker> workers;

}
