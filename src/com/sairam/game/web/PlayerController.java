package com.sairam.game.web;

import com.sairam.game.Player;
import com.sairam.game.PlayerDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerDao playerDao = new PlayerDao();

    // =========================================================
    // PLAYER LOGIN
    // =========================================================

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        try {

            if (request == null ||
                    request.username == null ||
                    request.password == null) {

                return ResponseEntity
                        .badRequest()
                        .body(new ApiResponse(
                                false,
                                "Username and password are required."
                        ));
            }

            Player player =
                    playerDao.loginPlayer(
                            request.username,
                            request.password
                    );

            return ResponseEntity.ok(
                    new LoginResponse(
                            true,
                            "Login successful.",
                            player.getName(),
                            player.getCredits()
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(
                            false,
                            e.getMessage()
                    ));
        }
    }

    // =========================================================
    // LOGIN REQUEST
    // =========================================================

    public static class LoginRequest {

        public String username;
        public String password;

        public LoginRequest() {
        }
    }

    // =========================================================
    // LOGIN RESPONSE
    // =========================================================

    public static class LoginResponse {

        public boolean success;
        public String message;
        public String username;
        public int credits;

        public LoginResponse(
                boolean success,
                String message,
                String username,
                int credits) {

            this.success = success;
            this.message = message;
            this.username = username;
            this.credits = credits;
        }
    }

    // =========================================================
    // GENERIC API RESPONSE
    // =========================================================

    public static class ApiResponse {

        public boolean success;
        public String message;

        public ApiResponse(
                boolean success,
                String message) {

            this.success = success;
            this.message = message;
        }
    }
}