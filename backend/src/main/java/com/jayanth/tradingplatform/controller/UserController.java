package com.jayanth.tradingplatform.controller;

import com.jayanth.tradingplatform.request.ForgotPasswordTokenRequest;
import com.jayanth.tradingplatform.domain.Verificationtype;
import com.jayanth.tradingplatform.model.ForgotPasswordToken;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.model.VerifcationCode;
import com.jayanth.tradingplatform.request.ResetPasswordRequest;
import com.jayanth.tradingplatform.response.ApiResponse;
import com.jayanth.tradingplatform.response.AuthResponse;
import com.jayanth.tradingplatform.service.EmailService;
import com.jayanth.tradingplatform.service.ForgotPasswordService;
import com.jayanth.tradingplatform.service.UserService;
import com.jayanth.tradingplatform.service.VerificationCodeService;
import com.jayanth.tradingplatform.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOTP(@RequestHeader("Authorization") String jwt, @PathVariable Verificationtype verificationType) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        VerifcationCode verifcationCode = verificationCodeService.getVerificationCode(user.getId());

        if(verifcationCode == null){
            verifcationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        }
        if(verificationType.equals(Verificationtype.Email)){
            emailService.sendVerificationOTPEmail(user.getEmail(), verifcationCode.getOtp());
        }


        return new ResponseEntity<String>("verification OTP sent successfully", HttpStatus.OK);
    }

    @PatchMapping("/api/users/enable-two/verifyOTP/{otp}")
    public ResponseEntity<User> EnableTwoFactorAuth(@RequestHeader("Authorization") String jwt, @PathVariable String otp) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        VerifcationCode verifcationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        String sendTo = verifcationCode.getVerificationtype().equals(Verificationtype.Email)?
                    verifcationCode.getEmail():verifcationCode.getMobileNumber();
        boolean isVerified = verifcationCode.getOtp().equals(otp);

        if(isVerified){
            User updatedUser = userService.enableTwoFactorAuthentication(verifcationCode.getVerificationtype(), sendTo, user);
            verificationCodeService.deleteVerificationCode(verifcationCode);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }

        throw new Exception("wrong otp");
    }

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOTP(@RequestHeader("Authorization") String jwt,
                                                              @RequestBody ForgotPasswordTokenRequest request) throws Exception {
       User user = userService.findUserProfileByEmail(request.getSendTo());
       String otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());

        if(token == null){
            token = forgotPasswordService.createToken(user,id, otp, request.getVerificationtype(), request.getSendTo());

        }
        if(request.getVerificationtype().equals(Verificationtype.Email)){
            emailService.sendVerificationOTPEmail(
                    user.getEmail(), token.getOtp());
        }
        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("Password reset OTP sent successfully");


        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPasswordOTP(@RequestParam String id,
                                                 @RequestHeader("Authorization") String jwt,
                                                 @RequestBody ResetPasswordRequest request) throws Exception {

        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);

        boolean isVerified = forgotPasswordToken.getOtp().equals(request.getOtp());
        if(isVerified){
            userService.updatePassword(forgotPasswordToken.getUser(), request.getPassword());
            ApiResponse response = new ApiResponse();
            response.setMessage("password updated successfully");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new Exception("wrong otp");
    }





}
