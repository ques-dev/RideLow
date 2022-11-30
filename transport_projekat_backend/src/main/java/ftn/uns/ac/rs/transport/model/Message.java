package ftn.uns.ac.rs.transport.model;


import ftn.uns.ac.rs.transport.model.enumerations.MessageType;
import jakarta.persistence.*;

import java.text.SimpleDateFormat;

@Entity
@Table(name = "Message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SenderId")
    private User senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReceiverId")
    private User receiverId;

    @Column(name = "Message")
    private String message;

    @Column(name = "SentDateTime")
    private SimpleDateFormat sentDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "MessageType")
    private MessageType messageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RideId")
    private Ride ride;
}
