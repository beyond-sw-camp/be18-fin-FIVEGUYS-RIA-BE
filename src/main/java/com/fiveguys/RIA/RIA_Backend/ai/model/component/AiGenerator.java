package com.fiveguys.RIA.RIA_Backend.ai.model.component;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class AiGenerator {

    private static final String MODEL_NAME = "gemini-2.5-flash";
    private final Client client;

    public AiGenerator(@Value("${GEMINI_API_KEY:}") String apiKey) {

        log.info("GEMINI_API_KEY length = {}", apiKey == null ? 0 : apiKey.length());

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("GEMINI_API_KEY 가 비어있어요! .env 또는 Run 설정을 확인해줘.");
        }

        this.client = Client.builder()
                            .apiKey(apiKey)
                            .build();
    }

    public String generateReason(String vipName,
                                 String brandName,
                                 BigDecimal totalAmount,
                                 long purchaseCount) {

        String prompt = """
                너는 VIP 고객 매출 분석가야.
                아래 정보를 보고, 왜 이 브랜드를 VIP에게 추천하고 유사한 브랜드도 추천해줘 한글로 번역해줘.
                너무 길게 쓰지 말고 1~2문장 정도로만.

                - 고객 이름: %s
                - 브랜드 이름: %s
                - 총 매출액: %s원
                - 구매 횟수: %d회
                """
                .formatted(vipName, brandName, totalAmount.toPlainString(), purchaseCount);

        GenerateContentResponse res =
                client.models.generateContent(MODEL_NAME, prompt, null);

        return res.text();
    }
}
