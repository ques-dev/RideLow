package ftn.uns.ac.rs.transport.model;

import jakarta.persistence.*;

import java.text.SimpleDateFormat;

@Entity
@Table(name = "Panic")
public class Panic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CurrentRideId")
    private Ride currentRide;

    @Column(name = "DateTime")
    private SimpleDateFormat dateTime;

    @Column(name = "Reason")
    private String reason;
}
