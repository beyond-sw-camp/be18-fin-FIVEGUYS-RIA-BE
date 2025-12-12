package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.dto.response;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.entity.Revenue;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.StoreTenantMap;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ContractCompleteResponseDto {
    // 계약 완료 시

    private final Long contractId;

    private final Contract.Status status;

    private final RelatedRecords relatedRecords;

    private final LocalDateTime updatedAt;

    @Getter
    @Builder
    public static class RelatedRecords {
        private final Long proposalId;
        private final Proposal.Status proposalStatus;
        private final Long estimateId;
        private final Estimate.Status estimateStatus;
        private final Long revenueId;
        private final Revenue.Status revenueStatus;
        private final List<StoreRecord> stores;

        @Getter
        @Builder
        public static class StoreRecord {
            private final Long storeId;
            private final String storeName;
            private final StoreTenantMap.Status status;
        }
    }
}