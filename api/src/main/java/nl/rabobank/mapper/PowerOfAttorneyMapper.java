package nl.rabobank.mapper;

import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.model.request.PowerOfAttorneyRequest;
import nl.rabobank.model.response.PowerOfAttorneyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PowerOfAttorneyMapper {
    PowerOfAttorneyMapper INSTANCE = Mappers.getMapper(PowerOfAttorneyMapper.class);
    PowerOfAttorney toDomainPowerOfAttorney(PowerOfAttorneyRequest request);
    @Mapping(target = "accountNumber", expression = "java(powerOfAttorney.getAccount() != null ? powerOfAttorney.getAccount().getAccountNumber() : null)")
    PowerOfAttorneyResponse toDomainPowerOfAttorneyResponse(PowerOfAttorney powerOfAttorney);

}
