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
    void shouldReturnAllBankTransfer() throws IOException {
        ResponseEntity<String> response = restTemplate.getForEntity("/transfers", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext resultContext = JsonPath.parse(response.getBody());

        int bankTransferQuantity = resultContext.read("$.length()");
        assertThat(bankTransferQuantity).isEqualTo(6);

        DocumentContext expectedContext = JsonPath.parse(new File("src/test/resources/br.com.banco/bankTransfersExpected.json"));

        JSONArray resultIds = resultContext.read("$..id");
        JSONArray expectedIds = expectedContext.read("$..id");
        assertThat(resultIds).containsExactlyInAnyOrder(expectedIds.toArray());

//        JSONArray resultTimestamps = resultContext.read("$..dataTransferencia");
//        JSONArray expectedTimestamps = expectedContext.read("$..dataTransferencia");
//        List<ZonedDateTime>

        //        ZonedDateTime timestamp = ZonedDateTime.parse(documentContext.read("$.dataTransferencia"));
//        assertThat(timestamp).isEqualTo(ZonedDateTime.parse("2019-01-01T12:00:00+03"));

        JSONArray resultValues = resultContext.read("$..valor");
        JSONArray expectedValues = expectedContext.read("$..valor");
        assertThat(resultValues).containsExactlyInAnyOrder(expectedValues.toArray());

        JSONArray resultTypes = resultContext.read("$..tipo");
        JSONArray expectedTypes = expectedContext.read("$..tipo");
        assertThat(resultTypes).containsExactlyInAnyOrder(expectedTypes.toArray());

        JSONArray resultOperatorNames = resultContext.read("$..nomeOperadorTransacao");
        JSONArray expectedOperatorNames = expectedContext.read("$..nomeOperadorTransacao");
        assertThat(resultOperatorNames).containsExactlyInAnyOrder(expectedOperatorNames.toArray());

        JSONArray resultAccountIds = resultContext.read("$..contaId");
        JSONArray expectedAccountIds = expectedContext.read("$..contaId");
        assertThat(resultAccountIds).containsExactlyInAnyOrder(expectedAccountIds.toArray());
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
        String url = "/transfers?startTime=2019-01-01T00:00:00&endTime=2019-05-05T08:12:45";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        int bankTransferQuantity = documentContext.read("$.length()");
        assertThat(bankTransferQuantity).isEqualTo(3);
    }
}
