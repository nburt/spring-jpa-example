package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;
import java.util.Random;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PersonRepository repository;

    @Test
    public void testCreate() throws Exception {
        MockHttpServletRequestBuilder request = post("/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Nate\"}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Nate")));

        verify(this.repository).save(any(Person.class));
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        MockHttpServletRequestBuilder request = post("/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"\",\"lastName\":\"\"}");

        this.mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]", equalTo("firstName: may not be empty")))
                .andExpect(jsonPath("$.errors[1]", equalTo("lastName: may not be empty")));
    }

    @Test
    public void testList() throws Exception {
        Long id = new Random().nextLong();
        Person person = new Person();
        person.setFirstName("Nate");
        person.setId(id);

        when(this.repository.findAll()).thenReturn(Collections.singletonList(person));

        MockHttpServletRequestBuilder request = get("/people")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(id)))
                .andExpect(jsonPath("$[0].firstName", equalTo("Nate")));
    }

}
