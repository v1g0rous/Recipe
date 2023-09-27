package recipes.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.exception.UserAlreadyExistsException;
import recipes.dto.request.RegisterUserRequest;
import recipes.entity.UserEntity;
import recipes.repository.UserRepository;
import recipes.adapter.UserEntityAdapter;

/**
 * Service class for managing users
 * <p>
 * This class provides methods for managing users.
 * </p>
 */
@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    /**
     * Used for user authentication - to lookup if such user exists in database
     * when user authenticates with his username
     * @param username the username identifying the user whose data is required.
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found in DB"));

        return new UserEntityAdapter(userEntity);
    }

    /**
     * Creates new user in DB
     * @param request - incoming HTTP request with new user details
     * @throws UserAlreadyExistsException if user with the same username already exists in DB
     */
    public void registerUser(RegisterUserRequest request) {
        String username = request.getEmail();

        boolean userAlreadyExists = userRepository.existsByUsername(username);

        if (userAlreadyExists) {
            throw new UserAlreadyExistsException("Username already exists in DB");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(userEntity);
    }

    /**
     * Retrieve user by specified username
     * @param username - username
     * @return user entity from DB
     * @throws UsernameNotFoundException if user with specified username doesn't exist
     */
    public UserEntity findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found in DB"));
    }
}
