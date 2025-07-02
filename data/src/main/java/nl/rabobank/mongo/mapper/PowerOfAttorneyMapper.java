package nl.rabobank.mongo.mapper;

import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mongo.document.PowerOfAttorneyDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = AccountMapper.class)
public interface PowerOfAttorneyMapper {

    PowerOfAttorneyMapper INSTANCE = Mappers.getMapper(PowerOfAttorneyMapper.class);

    PowerOfAttorney toPowerOfAttorney(PowerOfAttorneyDocument document);
    List<PowerOfAttorney> toPowerOfAttorneyList(List<PowerOfAttorneyDocument> documents);
    PowerOfAttorneyDocument toPowerOfAttorneyDocument(PowerOfAttorney poa);

}

