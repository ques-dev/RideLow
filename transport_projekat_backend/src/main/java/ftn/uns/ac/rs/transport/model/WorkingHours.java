package ftn.uns.ac.rs.transport.model;

import jakarta.persistence.*;
import java.text.SimpleDateFormat;

//TODO: Pitacu da li vozac ima fiksno radno vreme, i da li se generise svaki dan posebno. Isto cu ga pitati da li ima smisla
// da radno vreme ima id

@Entity
@Table(name = "WorkingHours")
public class WorkingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ShiftStart")
    private SimpleDateFormat shiftStart;

    @Column(name = "ShiftEnd")
    private SimpleDateFormat shiftEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DriverId")
    private Driver driver;
}
