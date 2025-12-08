package com.fiveguys.RIA.RIA_Backend.campaign.contract.model.component;

import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.Contract;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.entity.StoreContractMap;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.repository.ContractRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.contract.model.repository.StoreContractMapRepository;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.repository.EstimateRepository;
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
import com.fiveguys.RIA.RIA_Backend.common.exception.errorcode.ContractErrorCode;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.entity.Store;
import com.fiveguys.RIA.RIA_Backend.facility.store.model.repository.StoreRepository;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ContractLoader {

    private final ProjectRepository projectRepository;
    private final PipelineRepository pipelineRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ClientCompanyRepository clientCompanyRepository;
    private final StoreRepository storeRepository;
    private final ContractRepository contractRepository;
    private final EstimateRepository estimateRepository;
    private final ProposalRepository proposalRepository;
    private final StoreContractMapRepository storeContractMapRepository;

    public ClientCompany loadCompany(Long id) {
        return clientCompanyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ContractErrorCode.CLIENT_COMPANY_NOT_FOUND));
    }

    public Client loadClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new CustomException(ContractErrorCode.CLIENT_NOT_FOUND));
    }

    public Project loadProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new CustomException(ContractErrorCode.PROJECT_NOT_FOUND));
    }

    public User loadUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ContractErrorCode.USER_NOT_FOUND));
    }

    public Estimate loadEstimate(Long id) {
        return estimateRepository.findById(id)
                .orElseThrow(() -> new CustomException(ContractErrorCode.ESTIMATE_NOT_FOUND));
    }

    public Store loadStore(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ContractErrorCode.STORE_NOT_FOUND));
    }

    public Contract loadContract(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new CustomException(ContractErrorCode.CONTRACT_NOT_FOUND));
    }

    public Estimate loadEstimateByContract(Contract contract) {
        if (contract.getEstimate() == null) {
            return null;
        }
        Long estimateId = contract.getEstimate().getEstimateId();
        if (estimateId == null) {
            return null;
        }
        return estimateRepository.findById(estimateId).orElse(null);
    }

    public Proposal loadProposalByContract(Contract contract) {
        if (contract.getEstimate() == null || contract.getEstimate().getProposal() == null) {
            return null;
        }
        Long proposalId = contract.getEstimate().getProposal().getProposalId();
        if (proposalId == null) {
            return null;
        }
        return proposalRepository.findById(proposalId).orElse(null);
    }

    public List<StoreContractMap> loadStoreMapsByContract(Contract contract) {
        return storeContractMapRepository.findByContract_ContractId(contract.getContractId());
    }

    public List<Estimate> loadEstimatesByProject(Project project) {
        if (project == null) {
            return List.of();
        }
        return estimateRepository.findByProject(project);
    }

    public List<Proposal> loadProposalsByProject(Project project) {
        if (project == null) {
            return List.of();
        }
        return proposalRepository.findByProject(project);
    }

    public List<Contract> loadContractsByProject(Project project) {
        if (project == null) {
            return List.of();
        }
        return contractRepository.findByProject(project);
    }
}
