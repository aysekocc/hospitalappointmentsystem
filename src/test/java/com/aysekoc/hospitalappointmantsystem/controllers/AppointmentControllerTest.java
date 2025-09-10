package com.aysekoc.hospitalappointmantsystem.controllers;

import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.AppointmentService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.AppointmentListUserDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.CreateAppointment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username="user1", roles={"USER"})
    void createAppointment_asUser_shouldReturn201() throws Exception {
        CreateAppointment createAppointment = new CreateAppointment();
        createAppointment.setStartedDate(LocalDateTime.now());
        createAppointment.setEndedDate(LocalDateTime.now().plusHours(1));
        createAppointment.setDoctor(1L);
        createAppointment.setUser(1L);
        createAppointment.setHospitalId(1L);
        createAppointment.setStatus("Pending");

        mockMvc.perform(post("/api/v1/appointment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAppointment)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAppointments_withoutAuth_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v1/appointment"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="doctor1", roles={"DOCTOR"})
    void getAppointments_asDoctor_shouldReturn200() throws Exception {
        Mockito.when(appointmentService.getAppointments(0,10)).thenReturn(org.springframework.data.domain.Page.empty());

        mockMvc.perform(get("/api/v1/appointment"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="user1", roles={"USER"})
    void deleteAppointment_asUser_shouldReturn200() throws Exception {
        Mockito.doNothing().when(appointmentService).deleteAppointment(1L);

        mockMvc.perform(delete("/api/v1/appointment/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="user1", roles={"USER"})
    void userList_asUser_shouldReturn200() throws Exception {
        AppointmentListUserDto dto = new AppointmentListUserDto();
        Mockito.when(appointmentService.userList(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/appointment/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="doctor1", roles={"DOCTOR"})
    void doctorList_asDoctor_shouldReturn200() throws Exception {
        Mockito.when(appointmentService.doctorList(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/appointment/doctor/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="user1", roles={"USER"})
    void findById_asUser_shouldReturn200() throws Exception {
        Appointment appt = new Appointment();
        Mockito.when(appointmentService.findById(1L)).thenReturn(Optional.of(appt));

        mockMvc.perform(get("/api/v1/appointment/list/id")
                        .param("appointmentId", "1"))
                .andExpect(status().isOk());
    }

    // ✅ Yeni test: /list/startdate
    @Test
    @WithMockUser(username="user1", roles={"USER"})
    void findByStartDate_asUser_shouldReturn200() throws Exception {
        LocalDateTime startDate = LocalDateTime.now();
        Mockito.when(appointmentService.findByStartDate(startDate)).thenReturn(List.of(new Appointment()));

        mockMvc.perform(get("/api/v1/appointment/list/startdate")
                        .param("startDate", startDate.toString()))
                .andExpect(status().isOk());
    }

    // ✅ Yeni test: /list/enddate
    @Test
    @WithMockUser(username="user1", roles={"USER"})
    void findByEndDate_asUser_shouldReturn200() throws Exception {
        LocalDateTime endDate = LocalDateTime.now();
        Mockito.when(appointmentService.findByEndDate(endDate)).thenReturn(List.of(new Appointment()));

        mockMvc.perform(get("/api/v1/appointment/list/enddate")
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk());
    }

    // ✅ Yeni test: /my-appointments
    @Test
    @WithMockUser(username="user1", roles={"USER"})
    void getMyAppointments_asUser_shouldReturn200() throws Exception {
        Mockito.when(appointmentService.findByUsername("user1")).thenReturn(List.of(new Appointment()));

        mockMvc.perform(get("/api/v1/appointment/my-appointments"))
                .andExpect(status().isOk());
    }
}
