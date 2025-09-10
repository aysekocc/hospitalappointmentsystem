package com.aysekoc.hospitalappointmantsystem;


import com.aysekoc.hospitalappointmantsystem.controllers.AppointmentController;
import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.AppointmentService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.CreateAppointment;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentService appointmentService;

    @Test
    @WithMockUser(roles = "USER")
    void createAppointment_shouldReturnCreated() throws Exception {
        mockMvc.perform(post("/api/v1/appointment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"doctor\":1,\"user\":2,\"hospitalId\":3," +
                                "\"startedDate\":\"2025-09-10T10:00:00\"," +
                                "\"endedDate\":\"2025-09-10T11:00:00\"}"))
                .andExpect(status().isCreated());

        Mockito.verify(appointmentService).createAppointment(any(CreateAppointment.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAppointments_shouldReturnOk() throws Exception {
        Mockito.when(appointmentService.getAppointments(0, 10))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0));

        mockMvc.perform(get("/api/v1/appointment")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void findById_shouldReturnOk() throws Exception {
        Appointment appointment = new Appointment();
        Mockito.when(appointmentService.findById(1L))
                .thenReturn(Optional.of(appointment));

        mockMvc.perform(get("/api/v1/appointment/list/id")
                        .param("appointmentId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void findByStartDate_shouldReturnOk() throws Exception {
        Mockito.when(appointmentService.findByStartDate(any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/appointment/list/startdate")
                        .param("startDate", "2025-09-10T10:00:00"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void findByEndDate_shouldReturnOk() throws Exception {
        Mockito.when(appointmentService.findByEndDate(any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/appointment/list/enddate")
                        .param("endDate", "2025-09-10T11:00:00"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteAppointment_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/v1/appointment/1"))
                .andExpect(status().isOk());

        Mockito.verify(appointmentService).deleteAppointment(1L);
    }

}