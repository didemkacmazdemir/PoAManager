package nl.rabobank.mongo.repository;

import nl.rabobank.mongo.document.AccountDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<AccountDocument, String> {
    Optional<AccountDocument> findByAccountNumber(String accountNumber);
}
