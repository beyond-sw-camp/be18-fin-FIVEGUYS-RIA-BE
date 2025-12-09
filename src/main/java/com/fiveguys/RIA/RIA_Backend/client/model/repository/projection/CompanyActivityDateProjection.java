package com.fiveguys.RIA.RIA_Backend.client.model.repository.projection;

import java.time.LocalDateTime;

public interface CompanyActivityDateProjection {

  Long getClientCompanyId();

  LocalDateTime getLatestAt();
}
