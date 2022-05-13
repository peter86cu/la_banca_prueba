package software.actionware.fibonacci;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import junit.framework.TestCase;
import software.actionware.fibonacci.controllers.FibonacciController;


import java.util.logging.Logger;

import static org.springframework.http.HttpHeaders.ACCEPT_ENCODING;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest

public class FibonacciApplicationTests extends TestCase{

	private static final String FIBONACCI_80 = "23416728348467685";


	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	private FibonacciController fControlls;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		this.mockMvc=MockMvcBuilders.standaloneSetup(fControlls).build();
	}

	
	@Test
	public void testGetFibonacciPosition_100() throws Exception {
		
		this.mockMvc.perform(
				get("/api/fibonacci/80").header(ACCEPT_ENCODING, APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
				.andExpect(jsonPath("$.result").value(FIBONACCI_80));
		
	}

	
	@Test
	public void testGetFibonacciPosition_Negative() throws Exception {
		
		this.mockMvc.perform(
				get("/api/fibonacci/-3").header(ACCEPT_ENCODING, APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
		
	}

	
	@Test
	public void testGetFibonacciPosition_invalidInteger() throws Exception {
		
		this.mockMvc.perform(
				get("/api/fibonacci/qwer").header(ACCEPT_ENCODING, APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
		
	}

	
	

	
		
}
