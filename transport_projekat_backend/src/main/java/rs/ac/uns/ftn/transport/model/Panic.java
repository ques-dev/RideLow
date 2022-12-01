package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@Entity
@Table(name = "panics")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
