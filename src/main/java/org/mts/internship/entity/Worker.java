package org.mts.internship.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "worker")
public class Worker {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NaturalId
    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
