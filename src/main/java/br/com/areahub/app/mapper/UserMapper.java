package br.com.areahub.app.mapper;

import br.com.areahub.app.dto.UserResponseDTO;
import br.com.areahub.app.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserResponseDTO userResponseDTO(User user);
}
