package rs.ac.uns.ftn.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.transport.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

}
