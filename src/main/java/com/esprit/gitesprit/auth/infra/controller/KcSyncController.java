package com.esprit.gitesprit.auth.infra.controller;

import com.esprit.gitesprit.auth.domain.service.KcSyncService;
import com.esprit.gitesprit.auth.infra.dto.request.KcSyncRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/kc")
@RequiredArgsConstructor
@Tag(name = "Keycloak Sync", description = "Endpoints for synchronizing data from Keycloak")
public class KcSyncController {

    private final KcSyncService kcSyncService;

    /**
     * This endpoint is used to sync users from Keycloak to the application's database. please make
     * sure to update the custom keycloak event listener if you change the api endpoint. see : <a
     * href="https://github.com/Oussemasahbeni/keycloak-custom-event-listener">custom-event-listener
     * </a>
     */
    @PostMapping("/sync")
    @Operation(
            summary = "Synchronize Keycloak User",
            description = "Synchronizes user data from Keycloak to the application database.")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "User synchronization successful",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = Boolean.class))),
                @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<Boolean> sync(@RequestBody KcSyncRequest request) {
        return ResponseEntity.ok(kcSyncService.syncUser(request));
    }
}
