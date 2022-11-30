package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.Data;

import java.text.SimpleDateFormat;

@Entity
@Table(name = "panics")
@Data
public class Panic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currentRideId")
    private Ride currentRide;

    @Column(name = "dateTime")
    private SimpleDateFormat dateTime;

    @Column(name = "reason")
    private String reason;
}
