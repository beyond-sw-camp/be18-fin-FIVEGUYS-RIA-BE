package com.fiveguys.RIA.RIA_Backend.storage.model.service;

import com.fiveguys.RIA.RIA_Backend.storage.model.dto.StorageResponse;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StorageService {

    Page<StorageResponse> getStorages(Pageable pageable, User currentUser);

}
