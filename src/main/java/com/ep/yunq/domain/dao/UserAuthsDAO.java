package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.User;
import com.ep.yunq.domain.entity.UserAuths;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthsDAO extends JpaRepository<UserAuths,Integer> {

    UserAuths findById(int id);

    UserAuths findByIdentityTypeAndIdentifier(String identityType,String identifier);

}
