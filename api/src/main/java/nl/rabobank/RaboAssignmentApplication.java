package nl.rabobank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import nl.rabobank.mongo.config.MongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "nl.rabobank.mongo.repository")
@Import(MongoConfiguration.class)
public class RaboAssignmentApplication
{
    public static void main(final String[] args)
    {
        SpringApplication.run(RaboAssignmentApplication.class, args);
    }
}
