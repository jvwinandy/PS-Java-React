package br.com.banco;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnAllAccounts() {
        ResponseEntity<String> response = restTemplate.getForEntity("/accounts", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext resultContext = JsonPath.parse(response.getBody());

        int bankTransferQuantity = resultContext.read("$.length()");
        assertThat(bankTransferQuantity).isEqualTo(2);

        JSONArray resultIds = resultContext.read("$..idConta");
        assertThat(resultIds).containsExactlyInAnyOrder(1, 2);

        JSONArray resultNames = resultContext.read("$..nomeResponsavel");
        assertThat(resultNames).containsExactlyInAnyOrder("Fulano", "Sicrano");
    }
}
