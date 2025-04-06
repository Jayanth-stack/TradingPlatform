package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.domain.Verificationtype;
import com.jayanth.tradingplatform.model.User;

public interface UserService {

    public User findUserProfileByJwt( String jwt) throws Exception;
    public User findUserProfileByEmail( String email) throws Exception;

    public User findUserById(Long userId) throws Exception;
    public User enableTwoFactorAuthentication(Verificationtype verificationtype, String SendTo, User user);

    User updatePassword(User user, String newPassword);



}
