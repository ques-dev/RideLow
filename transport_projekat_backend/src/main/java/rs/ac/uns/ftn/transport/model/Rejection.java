package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.Data;

import java.text.SimpleDateFormat;

@Entity
@Table(name = "rejections")
@Data
public class Rejection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rideId")
    private Ride ride;

    @Column(name = "reason")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "dateTime")
    private SimpleDateFormat dateTime;
}
