package com.fiveguys.RIA.RIA_Backend.ai.model.component;

import com.fiveguys.RIA.RIA_Backend.pos.model.repository.PosRepository;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AiGenerator {

    private static final String MODEL_NAME = "gemini-2.5-flash";
    private final Client client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiGenerator(@Value("${GEMINI_API_KEY:}") String apiKey) {
        log.info("GEMINI_API_KEY length = {}", apiKey == null ? 0 : apiKey.length());

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("GEMINI_API_KEY 가 비어있어요.");
        }

        this.client = Client.builder()
                            .apiKey(apiKey)
                            .build();
    }

    public Map<String, String> generateReasonsBulk(
            Vip vip,
            List<PosRepository.BrandStats> statsList
    ) {

        String prompt = buildBulkPrompt(vip, statsList);

        GenerateContentResponse res =
                client.models.generateContent(MODEL_NAME, prompt, null);

        String raw = res.text();

        log.info("Gemini Bulk Raw Response: {}", raw);

        return parseJsonToMap(raw);
    }

    private String buildBulkPrompt(
            Vip vip,
            List<PosRepository.BrandStats> statsList
    ) {
        StringBuilder sb = new StringBuilder();

        sb.append("너는 VIP 고객 매출 데이터를 분석해서 브랜드 추천을 만드는 전문가야.\n");
        sb.append("아래 브랜드별 구매 정보를 분석해서, 각 브랜드마다 다음 형식에 맞춘 한 문단의 추천 메시지를 생성해줘.\n\n");

        sb.append("⚠ 반드시 아래 형식을 최대한 따르도록 해:\n");
        sb.append("\"{브랜드명} 브랜드는 총 매출 {총매출}원이고 구매 횟수 {구매횟수}회이니 {스타일}을 좋아하실 것 같습니다. ");
        sb.append("{추천할 브랜드1}, {추천할 브랜드2} 브랜드의 {추천할 상품/스타일}을 추천합니다.\"\n\n");

        sb.append("- 스타일 설명 부분({스타일})에는 고객의 취향을 한 단어 또는 짧은 구로 요약해줘. (예: \"활동적인 스포티한 스타일\", \"럭셔리한 하이엔드 스타일\")\n");
        sb.append("- 추천할 브랜드1, 2는 해당 브랜드와 어울리는 유사 이미지의 브랜드 이름을 사용하거나, ");
        sb.append("적절한 카테고리(예: \"럭셔리 브랜드\", \"스포츠 브랜드\")로 대체해도 괜찮아.\n");
        sb.append("- 반드시 총매출과 구매횟수 숫자는 그대로 문장 안에 포함해.\n");
        sb.append("- 존댓말을 사용하고, 문장은 1~2문장 정도로 유지해.\n\n");

        sb.append("반드시 아래 형식의 JSON만 출력해. 설명 문장이나 코멘트는 출력하지 마:\n");
        sb.append("{\n");
        sb.append("  \"BRAND_NAME\": \"위 형식을 따른 추천 문장\",\n");
        sb.append("  \"다른브랜드명\": \"위 형식을 따른 추천 문장\"\n");
        sb.append("}\n\n");

        sb.append("VIP 고객 이름: ").append(vip.getName()).append("님\n\n");

        sb.append("브랜드별 구매 데이터:\n");
        for (PosRepository.BrandStats stat : statsList) {
            sb.append(String.format(
                    "- 브랜드: %s, 총매출: %s원, 구매횟수: %d회\n",
                    stat.getBrandName(),
                    stat.getTotalAmount().toPlainString(),
                    stat.getPurchaseCount()
            ));
        }

        return sb.toString();
    }


    private Map<String, String> parseJsonToMap(String rawText) {
        try {
            String cleaned = rawText
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            return objectMapper.readValue(
                    cleaned,
                    new TypeReference<Map<String, String>>() {}
            );

        } catch (Exception e) {
            log.error("JSON 파싱 실패: {}", rawText, e);
            throw new IllegalStateException("AI 응답 JSON 파싱 실패", e);
        }
    }
}
