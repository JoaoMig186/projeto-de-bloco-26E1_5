package com.infnet.service;

import com.infnet.dtos.UserLoginDTO;
import com.infnet.dtos.UserRegisterDTO;
import com.infnet.exception.AuthorizationException;
import com.infnet.exception.EmailAlreadyRegisteredException;
import com.infnet.exception.ForbiddenAuthorizationException;
import com.infnet.exception.UserInactiveException;
import com.infnet.kafka.KafkaService;
import com.infnet.metrics.UserMetrics;
import com.infnet.model.CustomerProfile;
import com.infnet.model.User;
import com.infnet.model.enums.Role;
import com.infnet.model.enums.Status;
import com.infnet.repository.CustomerProfileRepository;
import com.infnet.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository repository;
    private PasswordEncoder encoder;
    private JwtService jwtService;
    private CustomerProfileRepository customerRepository;
    private GeocodeService geocodeService;
    private UserMetrics metrics;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public User registerUser(UserRegisterDTO dto, Role role) {
        repository.findUserByEmailIgnoreCase(dto.email()).ifPresent(user -> {
            throw new EmailAlreadyRegisteredException(("Email already registered"));
        });

        String passwordHash = encoder.encode(dto.password());

        User user = dto.toUserDomain(dto,role);
        user.setPassword(passwordHash);
        repository.save(user);

        if(role == Role.CUSTOMER){
            log.info("Novo Usuário " +  user.getEmail() + " do tipo Cliente adicionado");
            registerCustomerProfile(user,dto.address());
        }
        if(role == Role.STORE_OWNER){
            log.info("Novo Usuário " + user.getEmail() + " do tipo Lojista adicionado");
            metrics.incrementTotalStoreOwners();
            metrics.incrementTotalActiveStoreOwners();
        }
        metrics.incrementTotalUsers();

        return user;
    }

    private void registerCustomerProfile(User user, String address){
        CustomerProfile customer = new CustomerProfile(
            user,
            address
        );

        metrics.incrementTotalCustomers();
        metrics.incrementTotalPendingGeocodeCustomers();

        geocodeService.getCustomerGeocode(user.getId(),address);
        customerRepository.save(customer);
    }

    public String userAuthLogin(UserLoginDTO dto) {
        User user = repository.findUserByEmailIgnoreCase(dto.email()).orElseThrow(AuthorizationException::new);

        if(!encoder.matches(dto.password(),user.getPassword())){
            throw new AuthorizationException();
        }

        if(user.getStatus() == Status.INACTIVE){
            log.warn("Usuário " + user.getEmail() + " está inativo tentando acessar o sistema");
            throw new UserInactiveException();
        }

        return jwtService.generateToken(user);
    }

    public void adminValidation(String token){
        Claims claims = jwtService.validateToken(token.split(" ")[1]);
        String tokenRole = claims.get("role",String.class);
        if(Role.valueOf(tokenRole) != Role.ADMIN){
            log.warn("Usuário " + claims.getSubject() + " tentando acessar partes do sistema fora do seu escopo");
            throw new ForbiddenAuthorizationException("Forbidden request for User role");
        }
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(String token, Long id) {
        Claims claims = jwtService.validateToken(token.split(" ")[1]);
        Long userId = claims.get("id", Long.class);
        String tokenRole = claims.get("role",String.class);
        if(Objects.equals(userId, id) || Role.valueOf(tokenRole) == Role.ADMIN){
            return repository.findById(id).get();
        }else {
            log.warn("Usuário " + claims.getSubject() + " tentando acessar partes do sistema fora do seu escopo");
            throw new ForbiddenAuthorizationException("Forbidden request for the User");
        }
    }

    public List<CustomerProfile> getAllCustomers() {
        return customerRepository.findAll();
    }

    public CustomerProfile getCustomerById(String token, Long id) {
        Claims claims = jwtService.validateToken(token.split(" ")[1]);
        Long userId = claims.get("id", Long.class);
        String tokenRole = claims.get("role",String.class);
        if(Objects.equals(userId, id) || Role.valueOf(tokenRole) == Role.ADMIN){
            return customerRepository.findById(id).get();
        }else {
            log.warn("Usuário " + claims.getSubject() + " tentando acessar partes do sistema fora do seu escopo");
            throw new ForbiddenAuthorizationException("Forbidden request for the User");
        }
    }

    @Transactional
    public void updateUserStatus(String token, Long id, Status status) {
        Claims claims = jwtService.validateToken(token.split(" ")[1]);
        Long userId = claims.get("id", Long.class);
        String tokenRole = claims.get("role", String.class);

        if (Objects.equals(id, userId) || Role.valueOf(tokenRole) == Role.ADMIN) {
            User user = repository.findById(id).get();
            user.setStatus(status);
            Role userRole = user.getRole();

            if (userRole == Role.CUSTOMER) {
                CustomerProfile customerProfile = customerRepository.findById(id).get();
                customerProfile.setStatus(status);
                if(customerProfile.getStatus() != Status.PENDING_GEOCODE) {
                    switch (status) {
                        case ACTIVE:
                            log.info("Usuário " + user.getEmail() + " do tipo Cliente ativado novamente");
                            metrics.decrementTotalInactiveCustomers();
                            metrics.incrementTotalActiveCustomers();
                        case INACTIVE:
                            log.info("Usuário " + user.getEmail() + " do tipo Cliente foi desativado");
                            metrics.decrementTotalActiveCustomers();
                            metrics.incrementTotalInactiveCustomers();
                    }
                }
            }

            if (userRole == Role.STORE_OWNER) {
                switch (status) {
                    case ACTIVE:
                        log.info("Usuário " + user.getEmail() + " do tipo Lojista foi ativado novamente");
                        metrics.decrementTotalInactiveStoreOwners();
                        metrics.incrementTotalActiveStoreOwners();
                    case INACTIVE:
                        log.info("Usuário " + user.getEmail() + " do tipo Lojista foi desativado");
                        metrics.decrementTotalActiveStoreOwners();
                        metrics.incrementTotalInactiveStoreOwners();
                }
            }

        }else{
            log.warn("Usuário " + claims.getSubject() + " tentando acessar partes do sistema fora do seu escopo");
            throw new ForbiddenAuthorizationException("Forbidden request for the User");
        }
    }

}