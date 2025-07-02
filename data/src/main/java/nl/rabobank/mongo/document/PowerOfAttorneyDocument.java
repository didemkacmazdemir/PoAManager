package nl.rabobank.mongo.document;

import lombok.Getter;
import lombok.Setter;
import nl.rabobank.account.Account;
import nl.rabobank.authorizations.Authorization;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "power_of_attorneys")
public class PowerOfAttorneyDocument {
    @Id
    private String id;
    @Indexed
    private String granteeName;
    @Indexed
    private String grantorName;
    private Authorization authorization;
    private Account account;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
