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
            List<PosRepository.BrandProductStats> statsList
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
            List<PosRepository.BrandProductStats> statsList
    ) {
        StringBuilder sb = new StringBuilder();

        sb.append("너는 VIP 고객의 매출 데이터를 분석해 브랜드+상품 추천 문장을 만드는 전문가다.\n");
        sb.append("아래 브랜드별, 상품별 구매 정보를 보고, 각 (브랜드, 상품)마다 한 문단의 추천 문장을 만들어라.\n\n");

        sb.append("문장은 다음 형식을 따른다:\n");
        sb.append("\"고객님은 <b>{브랜드명}</b>의 <b>{상품명}</b> 카테고리에서 총 매출 {총매출}원, 구매 횟수 {구매횟수}회를 기록하셨으며 {스타일}을 좋아하실 것 같습니다. ");
        sb.append("<b>{추천브랜드1}</b>의 <b>{추천상품1}</b>, <b>{추천브랜드2}</b>의 <b>{추천상품2}</b>를 함께 추천합니다.\"\n\n");

        sb.append("규칙:\n");
        sb.append("- {추천브랜드1}, {추천브랜드2}는 원본 브랜드와 반드시 다른 브랜드여야 한다.\n");
        sb.append("- 추천 상품명은 각 브랜드별로 1개씩 총 2개를 생성한다.\n");
        sb.append("- 추천 상품명은 반드시 '실제로 존재할 법한 구체적인 상품명'으로 작성한다. (예: '레더 카드홀더', '버클 레더 벨트')\n");
        sb.append("- 광범위한 카테고리명(예: '패션 잡화', '액세서리')은 사용 금지.\n");
        sb.append("- 원본 브랜드의 대표 패턴·시그니처(GG, Monogram 등)는 추천상품에 사용 금지.\n\n");

        sb.append("추천 상품명 생성 기준:\n");
        sb.append("- 추천 상품명은 해당 추천 브랜드의 실제 제품 라인(Series/Line)의 스타일을 참고해 생성한다.\n");
        sb.append("- 예: BURBERRY = Vintage Check, TB Monogram / BALENCIAGA = Hourglass, Le Cagole, Cash, Everyday Series 등.\n");
        sb.append("- 단, 실제 모델명을 그대로 쓰지 말고, 그 라인에서 영감을 받은 '그럴듯한 실존형 상품명'으로 표현한다.\n");
        sb.append("- 예: \\\"Vintage Check Leather Card Holder\\\", \\\"Hourglass Metal Keyring\\\" 등.\n\n");

        sb.append("출력 형식:\n");
        sb.append("- 반드시 JSON만 출력할 것.\n");
        sb.append("- key 형식은 \"브랜드명|상품명\".\n");
        sb.append("- value는 생성된 추천 문장.\n\n");

        sb.append("브랜드+상품별 구매 데이터:\n");
        for (PosRepository.BrandProductStats stat : statsList) {
            sb.append(String.format(
                    "- 브랜드: %s, 상품: %s, 총매출: %s원, 구매횟수: %d회\n",
                    stat.getBrandName(),
                    stat.getProductName(),
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
