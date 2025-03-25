package com.dcool.springchatexample.api.users;

import com.dcool.springchatexample.domain.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository
    extends
    JpaRepository<User, String>,
    JpaSpecificationExecutor<User> {



    Optional<User> findByUserIdAndUserNameIsNotNull(String userId);

}