package com.ead.authuser.mappers.users;

import com.ead.authuser.configs.CentralMapperConfig;
import com.ead.authuser.dtos.UserRequest;
import com.ead.authuser.mappers.dates.DateMapper;
import com.ead.authuser.models.UserModel;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(config = CentralMapperConfig.class, uses = DateMapper.class)
public interface UserMapper {

  void updateUserFromDto(UserRequest.Update dto, @MappingTarget UserModel user);

  @Mappings({
    @Mapping(target = "created", source = "created", qualifiedByName = "formatLocalDateTime"),
    @Mapping(target = "updated", source = "updated", qualifiedByName = "formatLocalDateTime")
  })
  UserRequest.UserResponse toResponse(UserModel model);

  List<UserRequest.UserResponse> toDtoList(List<UserModel> userModelList);

  UserModel toEntity(UserRequest.Registration userDto);
}
