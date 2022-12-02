package rs.ac.uns.ftn.transport.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.transport.model.Document;
import rs.ac.uns.ftn.transport.repository.DocumentRepository;
import rs.ac.uns.ftn.transport.service.interfaces.IDocumentService;

@Service
public class DocumentServiceImpl implements IDocumentService {
    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document save(Document document) {
        return documentRepository.save(document);
    }
}