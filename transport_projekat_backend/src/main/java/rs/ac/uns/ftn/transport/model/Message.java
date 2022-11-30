package rs.ac.uns.ftn.transport.model;


import lombok.Data;
import rs.ac.uns.ftn.transport.model.enumerations.MessageType;
import jakarta.persistence.*;

import java.text.SimpleDateFormat;

@Entity
@Table(name = "messages")
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverId")
    private User receiver;

    @Column(name = "message")
    private String message;

    @Column(name = "sentDateTime")
    private SimpleDateFormat sentDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "messageType")
    private MessageType messageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rideId")
    private Ride ride;
}
