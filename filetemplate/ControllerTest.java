package ${PACKAGE_NAME};


import ${PACKAGE_NAME}.businesslogic.CarService;
import ${PACKAGE_NAME}.pojo.Car;
import ${PACKAGE_NAME}.services.CarController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CarController.class)
public class ${NAME} {


    @Autowired
    MockMvc mockMvc;


    @MockBean
    CarService carService;


    @Test
    public void getcar_ShouldReturnCar() throws Exception
    {
        //Mocking the carService object with @MockBean
        //And returning the mock value
        given(carService.getCarDetails("prius")).willReturn(Car.builder().name("prius").type("toyota").build());


        mockMvc.perform(MockMvcRequestBuilders.get("/cars/prius"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("name").value("prius"))
        .andExpect(jsonPath("type").value("toyota"));

    }



}
