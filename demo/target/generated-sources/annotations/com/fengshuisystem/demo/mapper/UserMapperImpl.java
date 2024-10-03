package com.fengshuisystem.demo.mapper;

import com.fengshuisystem.demo.dto.reponse.RoleResponse;
import com.fengshuisystem.demo.dto.reponse.UserResponse;
import com.fengshuisystem.demo.dto.request.UserCreationRequest;
import com.fengshuisystem.demo.dto.request.UserUpdateRequest;
import com.fengshuisystem.demo.entity.Role;
import com.fengshuisystem.demo.entity.User;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-03T14:07:25+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( request.getUsername() );
        user.password( request.getPassword() );
        user.email( request.getEmail() );
        user.dob( request.getDob() );
        user.phoneNumber( request.getPhoneNumber() );
        user.avatar( request.getAvatar() );
        user.gender( request.getGender() );
        user.code( request.getCode() );
        if ( request.getStatus() != null ) {
            user.status( request.getStatus() );
        }
        user.createDate( request.getCreateDate() );

        return user.build();
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        if ( user.getId() != null ) {
            userResponse.id( String.valueOf( user.getId() ) );
        }
        userResponse.username( user.getUsername() );
        userResponse.password( user.getPassword() );
        userResponse.email( user.getEmail() );
        userResponse.dob( user.getDob() );
        userResponse.phoneNumber( user.getPhoneNumber() );
        userResponse.avatar( user.getAvatar() );
        userResponse.gender( user.getGender() );
        userResponse.code( user.getCode() );
        userResponse.status( user.isStatus() );
        userResponse.createDate( user.getCreateDate() );
        userResponse.roles( roleSetToRoleResponseSet( user.getRoles() ) );

        return userResponse.build();
    }

    @Override
    public void updateUser(User user, UserUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        user.setUsername( request.getUsername() );
        user.setPassword( request.getPassword() );
        user.setEmail( request.getEmail() );
        user.setDob( request.getDob() );
        user.setPhoneNumber( request.getPhoneNumber() );
        user.setAvatar( request.getAvatar() );
        user.setGender( request.getGender() );
        user.setCode( request.getCode() );
        if ( request.getStatus() != null ) {
            user.setStatus( request.getStatus() );
        }
    }

    protected RoleResponse roleToRoleResponse(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleResponse.RoleResponseBuilder roleResponse = RoleResponse.builder();

        roleResponse.id( role.getId() );
        roleResponse.name( role.getName() );

        return roleResponse.build();
    }

    protected Set<RoleResponse> roleSetToRoleResponseSet(Set<Role> set) {
        if ( set == null ) {
            return null;
        }

        Set<RoleResponse> set1 = new LinkedHashSet<RoleResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Role role : set ) {
            set1.add( roleToRoleResponse( role ) );
        }

        return set1;
    }
}
