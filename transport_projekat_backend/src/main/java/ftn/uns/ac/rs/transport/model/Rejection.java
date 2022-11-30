package ftn.uns.ac.rs.transport.model;

import jakarta.persistence.*;

import java.text.SimpleDateFormat;

@Entity
@Table(name = "Rejection")
public class Rejection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RideId")
    private Ride ride;

    @Column(name = "Reason")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId")
    private User user;

    @Column(name = "DateTime")
    private SimpleDateFormat dateTime;
}
