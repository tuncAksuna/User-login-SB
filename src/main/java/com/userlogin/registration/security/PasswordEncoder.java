package com.userlogin.registration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoder {

    //Spring security tercih ettiğimiz şifre karma algoritmasını tahmin edemez. Bundan dolayı başka
    // bir @Bean,bir password encoder belirtmemiz gerekir burada yaptığımız gibi
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
        /**
         * IN THE DATABASE :
         * email : abc@gmail.com
         * password : {bcrypt}$2y$12$6t86Rpr3llMANhCUt26oUen2WhvXr/A89Xo9zJion8W7gWgZ/zA0C
         * şeklinde "veritabanı tablosunda" şifreyi Encodelayacak
         * */
    }
    /**
     * Spring security şunları yapacak :
     * Şifreleri okuyacak ve prefix({bcrytp})'i kaldıracak
     * PasswordEncoder'lı password satırını depolanmış(stored) olanlar ile karşılaştıracak
     */

}
