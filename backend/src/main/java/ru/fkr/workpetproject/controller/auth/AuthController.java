package ru.fkr.workpetproject.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fkr.workpetproject.dao.dto.auth.AuthResponseDto;
import ru.fkr.workpetproject.dao.dto.auth.LoginRequestDto;
import ru.fkr.workpetproject.dao.entity.User;
import ru.fkr.workpetproject.repository.UserRepository;
import ru.fkr.workpetproject.service.auth.JwtService;
import ru.fkr.workpetproject.service.auth.PasswordService;


@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {

        User user = userRepository.findByLogin(request.getUsername());

        // Пользователь не найден
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Неверный логин или пароль");
        }

        // Проверка пароля
        String requestPasswordHash = passwordService.md5(request.getPassword());
        if (!user.getPassword().equals(requestPasswordHash)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Неверный логин или пароль");
        }

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponseDto(token, user.getLogin(), user.getRole().getRoleName()));
    }
}
