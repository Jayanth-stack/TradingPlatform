package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.config.JwtProvider;
import com.jayanth.tradingplatform.domain.Verificationtype;
import com.jayanth.tradingplatform.model.TwoFactorAuth;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UserRepository userRepository;

    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);

        if(user == null) {
            throw new Exception("user not found");
        }
        return user;
    }

    @Override
    public User findUserProfileByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);

        if(user == null) {
            throw new Exception("user not found");
        }
        return user;
    }

    @Override
    public User findUserById(Long userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new Exception("User Not found");
        }else {
            return user.get();
        }
    }

    @Override
    public User enableTwoFactorAuthentication(Verificationtype verificationtype, String SendTo, User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verificationtype);
        user.setTwoFactorAuth(twoFactorAuth);

        return userRepository.save(user);
    }


    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
