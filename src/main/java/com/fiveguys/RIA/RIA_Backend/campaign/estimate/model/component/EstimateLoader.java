package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.StoreEstimateMap;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.repository.EstimateRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.repository.StoreEstimateMapRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.repository.PipelineRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.repository.ProjectRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.entity.Proposal;
import com.fiveguys.RIA.RIA_Backend.campaign.proposal.model.repository.ProposalRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.Client;
import com.fiveguys.RIA.RIA_Backend.client.model.entity.ClientCompany;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientCompanyRepository;
import com.fiveguys.RIA.RIA_Backend.client.model.repository.ClientRepository;
import com.fiveguys.RIA.RIA_Backend.common.exception.CustomException;
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.EstimateErrorCode;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.repository.StoreRepository;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EstimateLoader {

    private final ProjectRepository projectRepository;
    private final PipelineRepository pipelineRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ClientCompanyRepository clientCompanyRepository;
    private final StoreRepository storeRepository;
    private final EstimateRepository estimateRepository;
    private final ProposalRepository proposalRepository;
    private final StoreEstimateMapRepository storeEstimateMapRepository;

    public Project loadProject(Long id) {
        if (id == null) return null;
        return projectRepository.findById(id)
                .orElseThrow(() -> new CustomException(EstimateErrorCode.PROJECT_NOT_FOUND));
    }

    public Pipeline loadPipeline(Long id) {
        if (id == null) return null;
        return pipelineRepository.findById(id)
                .orElseThrow(() -> new CustomException(EstimateErrorCode.PIPELINE_NOT_FOUND));
    }

    public User loadUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(EstimateErrorCode.USER_NOT_FOUND));
    }

    public ClientCompany loadCompany(Long id) {
        return clientCompanyRepository.findById(id)
                .orElseThrow(() -> new CustomException(EstimateErrorCode.CLIENT_COMPANY_NOT_FOUND));
    }

    public Client loadClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new CustomException(EstimateErrorCode.CLIENT_NOT_FOUND));
    }

    public Store loadStore(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(EstimateErrorCode.STORE_NOT_FOUND));
    }



    public Proposal loadProposal(Long id) {
        if (id == null) return null;

        return proposalRepository.findById(id)
                .orElseThrow(() -> new CustomException(EstimateErrorCode.PROPOSAL_NOT_FOUND));
    }

    public Estimate loadEstimate(Long id) {
        return estimateRepository.findById(id)
                .orElseThrow(() -> new CustomException(EstimateErrorCode.ESTIMATE_NOT_FOUND));
    }

    public Estimate loadEstimateDetail(Long id) {
        return estimateRepository.findDetailById(id)
                .orElseThrow(() -> new CustomException(EstimateErrorCode.ESTIMATE_NOT_FOUND));
    }

    public StoreEstimateMap loadStoreEstimateMap(Long id) {
        return storeEstimateMapRepository.findById(id)
                .orElseThrow(() -> new CustomException(EstimateErrorCode.STORE_ESTIMATE_MAP_NOT_FOUND));
    }
}
