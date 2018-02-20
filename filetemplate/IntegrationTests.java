package ${PACKAGE_NAME};

import ${PACKAGE_NAME}.pojo.Car;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import sun.reflect.annotation.ExceptionProxy;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration testing , by runnung the server
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ${NAME} {

	@Autowired
	TestRestTemplate testRestTemplate;

	@Test
	public void contextLoads() {
	}



	@Test
	public void test() throws Exception{
		//arrange


		//act
		ResponseEntity<Car> response = testRestTemplate.getForEntity("/cars/prius",Car.class);


		//
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getName()).isEqualTo("prius");
		assertThat(response.getBody().getType()).isEqualTo("toyota");




	}
}
