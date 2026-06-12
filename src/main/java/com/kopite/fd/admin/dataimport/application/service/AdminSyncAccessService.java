package com.kopite.fd.admin.dataimport.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminSyncAccessService {

    public static final String ADMIN_SYNC_TOKEN_HEADER = "X-Admin-Sync-Token";

    private final String adminSyncToken;

    public AdminSyncAccessService(
            @Value("${admin.football-dna-data.sync.token:}") String adminSyncToken
    ) {
        this.adminSyncToken = adminSyncToken;
    }

    public boolean isAllowed(String providedToken) {
        return adminSyncToken != null
                && !adminSyncToken.isBlank()
                && providedToken != null
                && adminSyncToken.equals(providedToken);
    }
}
