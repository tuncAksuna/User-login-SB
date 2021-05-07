package com.userlogin.registration.registration;

import com.userlogin.registration.appuser.AppUser;
import com.userlogin.registration.appuser.AppUserRole;
import com.userlogin.registration.appuser.AppUserService;
import com.userlogin.registration.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class RegistrationService {
    //when the client sends to request , we want to capture this request

    // Injections..
    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("Invalid email");
            // TODO : Create custom Email exception
        }
        return appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
    }
}

