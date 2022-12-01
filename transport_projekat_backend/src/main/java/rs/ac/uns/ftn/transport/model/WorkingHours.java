package rs.ac.uns.ftn.transport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

//TODO: Pitacu da li vozac ima fiksno radno vreme, i da li se generise svaki dan posebno. Isto cu ga pitati da li ima smisla
// da radno vreme ima id

@Entity
@Table(name = "workingHours")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "shiftStart")
    private SimpleDateFormat shiftStart;

    @Column(name = "shiftEnd")
    private SimpleDateFormat shiftEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driverId")
    private Driver driver;
}
