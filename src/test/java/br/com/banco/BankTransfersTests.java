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

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankTransfersTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnABankTransfer() {
        ResponseEntity<String> response = restTemplate.getForEntity("/transfers/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(1);

        ZonedDateTime timestamp = ZonedDateTime.parse(documentContext.read("$.dataTransferencia"));
        assertThat(timestamp).isEqualTo(ZonedDateTime.parse("2019-01-01T12:00:00+03"));

        Double transferValue = documentContext.read("$.valor");
        assertThat(transferValue).isEqualTo(30895.46);

        String transferType = documentContext.read("$.tipo");
        assertThat(transferType).isEqualTo("DEPOSITO");

        String transferOperatorName = documentContext.read("$.nomeOperadorTransacao");
        assertThat(transferOperatorName).isNull();

        int accountId = documentContext.read("$.contaId");
        assertThat(accountId).isEqualTo(1);
    }

    @Test
    void shouldNotReturnABankTransfer() {
        ResponseEntity<String> response = restTemplate.getForEntity("/transfers/99999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnAllBankTransfers() throws IOException {
        ResponseEntity<String> response = restTemplate.getForEntity("/transfers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext resultContext = JsonPath.parse(response.getBody());

        int bankTransferQuantity = resultContext.read("$.length()");
        assertThat(bankTransferQuantity).isEqualTo(100);

        DocumentContext expectedContext = JsonPath.parse(new File("src/test/resources/br.com.banco/bankTransfersExpected.json"));

        JSONArray resultIds = resultContext.read("$..id");
        JSONArray expectedIds = expectedContext.read("$..id");
        assertThat(Arrays.copyOf(resultIds.toArray(), 6)).containsExactlyInAnyOrder(expectedIds.toArray());

        JSONArray resultValues = resultContext.read("$..valor");
        JSONArray expectedValues = expectedContext.read("$..valor");
        assertThat(Arrays.copyOf(resultValues.toArray(), 6)).containsExactlyInAnyOrder(expectedValues.toArray());

        JSONArray resultTypes = resultContext.read("$..tipo");
        JSONArray expectedTypes = expectedContext.read("$..tipo");
        assertThat(Arrays.copyOf(resultTypes.toArray(), 6)).containsExactlyInAnyOrder(expectedTypes.toArray());

        JSONArray resultOperatorNames = resultContext.read("$..nomeOperadorTransacao");
        JSONArray expectedOperatorNames = expectedContext.read("$..nomeOperadorTransacao");
        assertThat(Arrays.copyOf(resultOperatorNames.toArray(), 6)).containsExactlyInAnyOrder(expectedOperatorNames.toArray());

        JSONArray resultAccountIds = resultContext.read("$..contaId");
        JSONArray expectedAccountIds = expectedContext.read("$..contaId");
        assertThat(Arrays.copyOf(resultAccountIds.toArray(), 6)).containsExactlyInAnyOrder(expectedAccountIds.toArray());
    }

    @Test
    void shouldFilterByNomeOperadorTransacaoNameExists() {
        ResponseEntity<String> response = restTemplate.getForEntity("/transfers?operatorName=Beltrano", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        int bankTransferQuantity = documentContext.read("$.length()");
        assertThat(bankTransferQuantity).isEqualTo(1);
    }

    @Test
    void shouldFilterByNomeOperadorTransacaoNameDoesntExist() {
        ResponseEntity<String> response = restTemplate.getForEntity("/transfers?operatorName=Ciclano", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        int bankTransferQuantity = documentContext.read("$.length()");
        assertThat(bankTransferQuantity).isEqualTo(0);
    }

    @Test
    void shouldReturnResultsBetweenDateExists() {
        String url = "/transfers?&startTime=2019-01-01T00:00:00.000Z&endTime=2019-05-05T08:12:00.450Z";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        int bankTransferQuantity = documentContext.read("$.length()");
        assertThat(bankTransferQuantity).isEqualTo(3);
    }

    @Test
    void shouldReturnResultsBetweenDateDoesntExists() {
        String url = "/transfers?&startTime=2023-07-02T03:00:00.000Z&endTime=2023-07-20T03:00:00.000Z";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        int bankTransferQuantity = documentContext.read("$.length()");
        assertThat(bankTransferQuantity).isEqualTo(0);
    }

    @Test
    void shouldFilterByAccountId1() {
        ResponseEntity<String> response = restTemplate.getForEntity("/transfers?accountId=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        int bankTransferQuantity = documentContext.read("$.length()");
        assertThat(bankTransferQuantity).isEqualTo(53);
    }

    @Test
    void shouldFilterByAccountId2() {
        ResponseEntity<String> response = restTemplate.getForEntity("/transfers?accountId=2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        int bankTransferQuantity = documentContext.read("$.length()");
        assertThat(bankTransferQuantity).isEqualTo(47);
    }
}
