package ftn.uns.ac.rs.transport.model;

import jakarta.persistence.*;

import java.text.SimpleDateFormat;

//TODO:Pitacu za zivotni ve sta predstavlja

@Entity
@Table(name = "UserActivation")
public class UserActivation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId")
    private User user;

    @Column(name = "DateCreated")
    private SimpleDateFormat dateCreated;

    @Column(name = "ValidUntil")
    private SimpleDateFormat validUntil;
}
