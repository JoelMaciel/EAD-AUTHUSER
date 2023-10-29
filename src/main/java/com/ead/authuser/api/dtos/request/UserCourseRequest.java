package com.ead.authuser.api.dtos.request;

import com.ead.authuser.domain.models.User;
import com.ead.authuser.domain.models.UserCourse;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class UserCourseRequest {

    private User user;
    @NotNull
    private UUID courseId;

    public static UserCourse toEntity(User user, UUID courseId) {
        return UserCourse.builder()
                .user(user)
                .courseId(courseId)
                .build();
    }
}
