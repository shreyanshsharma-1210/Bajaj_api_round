package com.example.demo.service;

import com.example.demo.dto.BFHLRequest;
import com.example.demo.dto.BFHLResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BFHLServiceImplTest {

    private BFHLServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BFHLServiceImpl();
    }

    @Test
    @DisplayName("Example A: mixed alphabets, numbers, special chars")
    void testExampleA() {
        BFHLRequest request = new BFHLRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));
        BFHLResponse response = service.process(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getOddNumbers()).containsExactly("1");
        assertThat(response.getEvenNumbers()).containsExactlyInAnyOrder("334", "4");
        assertThat(response.getAlphabets()).containsExactlyInAnyOrder("A", "R");
        assertThat(response.getSpecialCharacters()).containsExactly("$");
        assertThat(response.getSum()).isEqualTo("339");
    }

    @Test
    @DisplayName("Example B: multiple alphabets for concat_string")
    void testExampleB() {
        BFHLRequest request = new BFHLRequest(Arrays.asList("A", "ABCD", "DOE"));
        BFHLResponse response = service.process(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getAlphabets()).containsExactlyInAnyOrder("A", "ABCD", "DOE");
        assertThat(response.getOddNumbers()).isEmpty();
        assertThat(response.getEvenNumbers()).isEmpty();
        assertThat(response.getSum()).isEqualTo("0");
        // AABCDDOE reversed = EODDBCAA -> alternating caps
        assertThat(response.getConcatString()).isEqualTo("EoDdCbAa");
    }

    @Test
    @DisplayName("Example C: numbers only")
    void testExampleC() {
        BFHLRequest request = new BFHLRequest(Arrays.asList("2", "4", "6", "3", "9"));
        BFHLResponse response = service.process(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getEvenNumbers()).containsExactlyInAnyOrder("2", "4", "6");
        assertThat(response.getOddNumbers()).containsExactlyInAnyOrder("3", "9");
        assertThat(response.getAlphabets()).isEmpty();
        assertThat(response.getSpecialCharacters()).isEmpty();
        assertThat(response.getSum()).isEqualTo("24");
        assertThat(response.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("Edge case: empty data array")
    void testEmptyInput() {
        BFHLRequest request = new BFHLRequest(Collections.emptyList());
        BFHLResponse response = service.process(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getOddNumbers()).isEmpty();
        assertThat(response.getEvenNumbers()).isEmpty();
        assertThat(response.getAlphabets()).isEmpty();
        assertThat(response.getSpecialCharacters()).isEmpty();
        assertThat(response.getSum()).isEqualTo("0");
        assertThat(response.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("Edge case: null data list")
    void testNullData() {
        BFHLRequest request = new BFHLRequest(null);
        BFHLResponse response = service.process(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getSum()).isEqualTo("0");
    }

    @Test
    @DisplayName("Edge case: special characters only")
    void testSpecialCharsOnly() {
        BFHLRequest request = new BFHLRequest(Arrays.asList("$", "@", "#", "!"));
        BFHLResponse response = service.process(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getSpecialCharacters()).containsExactly("$", "@", "#", "!");
        assertThat(response.getAlphabets()).isEmpty();
        assertThat(response.getOddNumbers()).isEmpty();
        assertThat(response.getEvenNumbers()).isEmpty();
        assertThat(response.getSum()).isEqualTo("0");
        assertThat(response.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("Alphabets must be returned in uppercase")
    void testAlphabetsUppercase() {
        BFHLRequest request = new BFHLRequest(Arrays.asList("a", "b", "z"));
        BFHLResponse response = service.process(request);

        assertThat(response.getAlphabets()).containsExactly("A", "B", "Z");
    }

    @Test
    @DisplayName("sum must be returned as string")
    void testSumIsString() {
        BFHLRequest request = new BFHLRequest(Arrays.asList("10", "20"));
        BFHLResponse response = service.process(request);

        assertThat(response.getSum()).isInstanceOf(String.class);
        assertThat(response.getSum()).isEqualTo("30");
    }

    @Test
    @DisplayName("user_id must contain FULL_NAME and date")
    void testUserIdFormat() {
        BFHLRequest request = new BFHLRequest(Collections.emptyList());
        BFHLResponse response = service.process(request);

        assertThat(response.getUserId()).isEqualTo("shreyansh_sharma_12102005");
    }

    @Test
    @DisplayName("concat_string: single char input")
    void testConcatStringSingleChar() {
        BFHLRequest request = new BFHLRequest(List.of("a"));
        BFHLResponse response = service.process(request);

        assertThat(response.getConcatString()).isEqualTo("A");
    }
}
