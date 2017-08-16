package jp.co.example;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import jp.co.example.model.EmployeeWithSectionNameRes;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class engscheduleApplicationTests {

    @Autowired
    private TestRestTemplate template;

	@Test
	public void contextLoads() throws Exception {
		// トップレベルをたたいて、200 OK を返すか。
        ResponseEntity<String> res = template.getForEntity("/", String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
	}



	@Test
	public void ネイティブSQLで問い合わせ() {
		//localhost:8080/test/employees/jpanotuse/{id}
		int id = 1;
        ResponseEntity<List<EmployeeWithSectionNameRes>> res = template.exchange(
                "/test/employees/jpanotuse/{id}"
        		, HttpMethod.GET
        		, null
        		, new ParameterizedTypeReference<List<EmployeeWithSectionNameRes>>() {}
        		,id);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<EmployeeWithSectionNameRes> body = res.getBody();
        //assertThat(res.getBody()).containsOnly(customer1, customer3);
	}

}
