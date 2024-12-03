package com.example.encurtadordeurl.Entities;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface UrlMapper {

    UrlDTO convert(Url url);

    @Mapping(target = "id", ignore = true)
    Url convert(UrlDTO urlDTO);


}
