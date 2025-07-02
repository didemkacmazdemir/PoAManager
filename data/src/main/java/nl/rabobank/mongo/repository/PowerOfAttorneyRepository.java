package nl.rabobank.mongo.repository;

import nl.rabobank.mongo.document.PowerOfAttorneyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PowerOfAttorneyRepository extends MongoRepository<PowerOfAttorneyDocument, String> {
    List<PowerOfAttorneyDocument> findAllByGranteeName(String granteeName);
    boolean existsByGrantorNameAndGranteeNameAndAccountAccountNumber(
            String grantorName,
            String granteeName,
            String accountNumber
    );
}