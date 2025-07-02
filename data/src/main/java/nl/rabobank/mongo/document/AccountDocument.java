package nl.rabobank.mongo.document;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import nl.rabobank.mongo.model.AccountType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "accounts")
public class AccountDocument {
    @Id
    private String id;

    @Indexed(unique = true)
    private String accountNumber;
    private String accountHolderName;
    @PositiveOrZero
    private Double balance;
    private AccountType accountType;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
