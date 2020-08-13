package com.project.service;

import com.project.dao.UserAccountDao;
import com.project.dao.abstraction.UserRoleDao;
import com.project.model.RegistrationUserDTO;
import com.project.model.Role;
import com.project.model.ShoppingCart;
import com.project.model.UserAccount;
import com.project.service.abstraction.SendEmailService;
import com.project.service.abstraction.UserAccountService;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    UserAccountDao userAccountDao;

    UserRoleDao userRoleDao;

    PasswordEncoder encoder;

    HttpSession httpSession;

    SendEmailService sendEmailService;

    @Override
    public UserAccount findUserByToConfirmEmail(String token) {
        try {
            return userAccountDao.findUserByTokenToConfirmEmail(token);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean emailExist(String email) {
        return userAccountDao.findByEmail(email).isPresent();
    }

    @Override
    public Long getCartIdByUserEmail(String email) {
        return userAccountDao.findByEmail(email).get().getCart().getId();
    }

    @Override
    public UserAccount save(RegistrationUserDTO user, String url) throws ConstraintViolationException, MailSendException {
        UserAccount userAccount = UserAccount.builder()
                .email(user.getEmail())
                .password(encoder.encode(user.getPassword()))
                .regDate(Instant.now().getEpochSecond())
                .provider("local")
                .locale(httpSession.getAttribute("LANG").toString())
                .cart(new ShoppingCart())
                .tokenToConfirmEmail(UUID.randomUUID().toString())
                .roles(new Role(1L, "ROLE_USER"))
                .build();

        try {
            if (userAccount != null) {
                sendEmailService.confirmAccount(userAccount, url);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return userAccountDao.save(userAccount);
    }

    @Override
    public UserAccount save1Clickreg(RegistrationUserDTO user, String url) throws ConstraintViolationException {
        UserAccount userAccount = UserAccount.builder()
                .email(user.getEmail())
                .password(encoder.encode(user.getPassword()))
                .regDate(Instant.now().getEpochSecond())
                .provider("local")
                .locale(httpSession.getAttribute("LANG").toString())
                .cart(new ShoppingCart())
                .tokenToConfirmEmail(UUID.randomUUID().toString())
                .roles(new Role(1L, "ROLE_USER"))
                .autoReg(user.isAutoReg())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        try {
            if (userAccount != null) {
                sendEmailService.confirmAccount1ClickReg(userAccount, user.getPassword(), user.getEmail(), url);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return userAccountDao.save(userAccount);
    }

    @Override
    public void setLocaleAndAuthDate(String email, String locale, long lastAuthDate) {
        userAccountDao.setLocaleAndAuthDate(email, locale, lastAuthDate);
    }

    @Override
    public UserAccount save(UserAccount user) {
        return userAccountDao.save(user);
    }

    @Override
    public UserAccount getUserById(Long id) {
        return userAccountDao.findById(id);
    }

    @Override
    public UserAccount update(UserAccount userAccount) {
        userAccountDao.update(userAccount);
        return userAccount;
    }

    public UserAccount findByLogin(String login) throws UsernameNotFoundException, NoResultException {
        return userAccountDao.findByEmail(login).get();
    }

    @Override
    public List<String> getUsersEmails() {
        return userAccountDao.getUsersEmails();
    }

    @Override
    public boolean deleteUser(String email) {
        UserAccount userAccount = findByLogin(email);
        if (userAccount != null) {
            userAccountDao.deleteUserAccount(userAccount);
            return true;
        }
        return false;
    }
}
