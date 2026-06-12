package com.kopite.fd.admin.dataimport.dto.request;

import com.kopite.fd.admin.dataimport.application.command.SyncFootballDnaDataCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SyncFootballDnaDataRequest {

    @NotBlank
    @Size(max = 20)
    private String dataVersion;

    public SyncFootballDnaDataCommand toCommand() {
        return new SyncFootballDnaDataCommand(dataVersion);
    }
}
