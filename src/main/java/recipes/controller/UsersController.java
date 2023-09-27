package recipes.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipes.dto.request.RegisterUserRequest;
import recipes.service.UserService;

/**
 * REST controller
 *
 * <p>
 * This controller provides CRUD operations to work with users
 * <p/>
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/")
public class UsersController {
    UserService userService;

    /**
     * Register a new user
     * @param request - incoming HTTP request with user details
     * @throws recipes.exception.UserAlreadyExistsException if user with such username already exists
     */
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        userService.registerUser(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
