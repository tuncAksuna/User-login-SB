package com.userlogin.registration.appuser;

import com.userlogin.registration.registration.token.ConfirmationToken;
import com.userlogin.registration.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    /**
     * UserDetailsService login işleminden önce kullanıcıyı nasıl bulacağımızı , kullanıcının paswword'una erişim sahibi olmayı sağlar
     * Örneğin Atlassion Crowd identiy management sisteminde kullanıcılarımızı tutsaydık(artık bütün kullanıcılar ve şifreleri Atlassian Crowd'da depolanır)
     * Veritabanı tablolarımızda değil. O zaman UserDetailService kullanamaz onun yerine bir class tanımlayıp AuthenticationProvide interfacesini
     * implemente ederdik ve Authentication metodumuzu yazardık.
     */

    private final static String USER_NOT_FOUND_MSG = "User with email %s not found in the system";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));

        /* bu metod username'e göre buluyor ancak biz email ile giriş yaptırıyoruz username ile değil
         bunun için StudentRepository interfacesini oluşturduk ve email'e göre kullanıcıyı databasede aradık en son repoyu buraya inject ederek kullandık */
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        if (userExists) {
            throw new IllegalStateException("Email already taken");
            // TODO : CREATE CUSTOM - E-MAIL ALREADY EXCEPTION -
        }

        // Bilgileri giren kişi databasede yoksa yeni bir kullanıcı yarat databasede ve şifresini encrypt et ve doğrulama emaili gönder
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString(); // Bir tane random token yarattık
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15), // token'ın yaratıldığı zamanın üzerine 15 dk ekledik
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        // TODO : SEND E-MAIL
        return token;
    }
}
