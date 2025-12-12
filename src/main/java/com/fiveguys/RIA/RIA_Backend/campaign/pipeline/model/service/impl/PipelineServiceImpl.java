package com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.auth.service.CustomUserDetails;
import com.fiveguys.RIA.RIA_Backend.auth.service.PermissionValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.component.PipelineLoader;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.component.PipelineValidator;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.request.PipelineStageUpdateRequestDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.dto.response.PipelineStageUpdateResponseDto;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.service.PipelineService;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.component.ProjectLoader;
import com.fiveguys.RIA.RIA_Backend.campaign.project.model.entity.Project;
import java.time.LocalDateTime;

import com.fiveguys.RIA.RIA_Backend.event.contract.ContractNotificationEvent;
import com.fiveguys.RIA.RIA_Backend.event.project.ProjectNotificationEvent;
import com.fiveguys.RIA.RIA_Backend.notification.model.entity.NotificationTargetAction;
import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PipelineServiceImpl implements PipelineService {

  private final PipelineLoader pipelineLoader;
  private final PipelineValidator pipelineValidator;
  private final PermissionValidator permissionValidator;
  private final ProjectLoader projectLoader;

  private final ApplicationEventPublisher eventPublisher;

  
  // 파이프라인 스테이지 수동 변경
  @Override
  @Transactional
  public PipelineStageUpdateResponseDto updateStage(
      Long pipelineId,
      PipelineStageUpdateRequestDto dto,
      CustomUserDetails userDetails
  ) {

    // 1. Pipeline + Project 로드
    Pipeline pipeline = pipelineLoader.loadPipelineWithProject(pipelineId);
    Project project = pipeline.getProject();

    // 2. 권한 검증
    permissionValidator.validateOwnerOrLeadOrAdmin(
        project.getSalesManager(),
        userDetails
    );

    // 3. 단계 검증
    pipelineValidator.validateTargetStage(pipeline, dto.getTargetStageNo());

    int previousStage = pipeline.getCurrentStage();

    // 4. Domain update
    pipeline.updateStage(dto.getTargetStageNo());

    // 이벤트
    User sender = projectLoader.loadUser(userDetails.getUserId());
    User receiver = project.getSalesManager();

    String roleName = switch (sender.getRole().getRoleName()) {
      case ROLE_ADMIN -> "관리자";
      case ROLE_SALES_LEAD -> "영업팀장";
      case ROLE_SALES_MEMBER -> "영업사원";
    };

    eventPublisher.publishEvent(
            new ProjectNotificationEvent(
                    this,
                    sender.getId(),
                    sender.getName(),
                    roleName,
                    receiver.getId(),
                    project,
                    NotificationTargetAction.STAGE_UPDATED
            ));

    // 5. 응답
    return PipelineStageUpdateResponseDto.builder()
        .pipelineId(pipeline.getPipelineId())
        .previousStage(previousStage)
        .currentStage(pipeline.getCurrentStage())
        .updatedBy(userDetails.getUserId())
        .updatedAt(LocalDateTime.now())
        .build();
  }
}
