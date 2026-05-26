package com.example.demo.service;

import com.example.demo.dto.BFHLRequest;
import com.example.demo.dto.BFHLResponse;
import com.example.demo.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BFHLServiceImpl implements BFHLService {

    @Override
    public BFHLResponse process(BFHLRequest request) {
        log.info("Processing BFHL request with {} items",
                request.getData() == null ? 0 : request.getData().size());

        List<String> data = request.getData() == null ? new ArrayList<>() : request.getData();

        List<String> oddNumbers = new ArrayList<>();
        List<String> evenNumbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();
        long numericSum = 0;
        StringBuilder allAlphabets = new StringBuilder();

        for (String token : data) {
            if (token == null || token.isEmpty()) {
                continue;
            }
            if (isNumeric(token)) {
                long value = Long.parseLong(token);
                numericSum += value;
                if (value % 2 == 0) {
                    evenNumbers.add(token);
                } else {
                    oddNumbers.add(token);
                }
            } else if (isAlphaOnly(token)) {
                alphabets.add(token.toUpperCase());
                allAlphabets.append(token);
            } else {
                specialCharacters.add(token);
            }
        }

        String concatString = buildConcatString(allAlphabets.toString());
        String userId = buildUserId();

        BFHLResponse response = BFHLResponse.builder()
                .isSuccess(true)
                .userId(userId)
                .email(Constants.EMAIL)
                .rollNumber(Constants.ROLL_NUMBER)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialCharacters)
                .sum(String.valueOf(numericSum))
                .concatString(concatString)
                .build();

        log.info("BFHL processing complete. userId={}, sum={}", userId, numericSum);
        return response;
    }

    private boolean isNumeric(String token) {
        try {
            Long.parseLong(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isAlphaOnly(String token) {
        return token.chars().allMatch(Character::isLetter);
    }

    /**
     * Concatenate all alphabetical characters from input (preserving order),
     * reverse the full string, then apply alternating caps (upper, lower, upper...).
     *
     * Example: ["A","ABCD","DOE"]
     *   Concat  : AABCDDOE
     *   Reverse : EODDDCBAA -> wait, correct: AABCDDOE reversed = EODDBCBAA
     *   Actually: AABCDDOE reversed = EODDBCAA -> let's compute char by char
     *   A A B C D D O E  -> reversed -> E O D D C B A A
     *   Alternating caps -> EoDdCbAa
     */
    private String buildConcatString(String raw) {
        if (raw == null || raw.isEmpty()) {
            return "";
        }
        String reversed = new StringBuilder(raw).reverse().toString();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            if (i % 2 == 0) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }

    private String buildUserId() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern(Constants.USER_ID_DATE_FORMAT));
        return Constants.FULL_NAME + "_" + date;
    }
}
