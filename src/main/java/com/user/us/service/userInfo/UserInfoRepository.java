package com.user.us.service.userInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo , Long> {

    UserInfo findByUsername(String username);

}
