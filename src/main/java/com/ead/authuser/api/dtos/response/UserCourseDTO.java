package com.ead.authuser.api.dtos.response;

import com.ead.authuser.domain.models.User;
import com.ead.authuser.domain.models.UserCourse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCourseDTO {

    private UUID id;
    private UUID courseId;
    private UserDTO user;


    public static UserCourseDTO toDTO(UserCourse userCourse) {
        return UserCourseDTO.builder()
                .id(userCourse.getId())
                .user(UserDTO.toDTO(userCourse.getUser()))
                .courseId(userCourse.getCourseId())
                .build();
    }

}
