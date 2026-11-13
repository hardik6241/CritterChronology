package com.critter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.critter.entity.Customer;
import com.critter.entity.Employee;
import com.critter.entity.Pet;
import com.critter.entity.Schedule;
import com.critter.model.EmployeeSkill;
import com.critter.model.PetType;
import com.critter.service.PetService;
import com.critter.service.ScheduleService;
import com.critter.service.UserService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CritterApplicationIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private PetService petService;
    @Autowired
    private ScheduleService scheduleService;

    @Test
    void exposesCustomerPetEmployeeAvailabilityAndScheduleEndpoints() throws Exception {
        JsonNode customer = objectMapper.readTree(mockMvc.perform(post("/user/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Casey\",\"phoneNumber\":\"555-0110\",\"address\":\"1 Main St\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn().getResponse().getContentAsString());
        long customerId = customer.get("id").asLong();
        mockMvc.perform(get("/user/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(customerId));
        mockMvc.perform(get("/user/customer/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Casey"));

        JsonNode pet = objectMapper.readTree(mockMvc.perform(post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Scout\",\"type\":\"DOG\",\"ownerId\":" + customerId
                                + ",\"birthDate\":\"2020-04-02\",\"notes\":\"Friendly\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerId").value(customerId))
                .andReturn().getResponse().getContentAsString());
        long petId = pet.get("id").asLong();
        mockMvc.perform(get("/pet/{petId}", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerId").value(customerId));
        mockMvc.perform(get("/pet/owner/{ownerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(petId));
        mockMvc.perform(get("/pet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(petId));
        mockMvc.perform(post("/user/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":" + customerId
                                + ",\"name\":\"Casey Updated\",\"phoneNumber\":\"555-0110\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.petIds[0]").value(petId));
        mockMvc.perform(get("/pet/owner/{ownerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(petId));

        JsonNode employee = objectMapper.readTree(mockMvc.perform(post("/user/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Jamie\",\"phoneNumber\":\"555-0120\","
                                + "\"skills\":[\"WALKING\"],\"daysAvailable\":[\"MONDAY\"]}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
        long employeeId = employee.get("id").asLong();
        mockMvc.perform(get("/user/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(employeeId));
        mockMvc.perform(get("/user/employee/{employeeId}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jamie"));
        mockMvc.perform(post("/user/employee/availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"date\":\"2026-11-16\",\"skills\":[\"WALKING\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(employeeId));
        mockMvc.perform(post("/user/employee/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"TUESDAY\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.daysAvailable[0]").value("TUESDAY"));

        mockMvc.perform(post("/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"date\":\"2026-11-16\",\"petIds\":[" + petId
                                + "],\"employeeIds\":[" + employeeId + "],\"activities\":[\"WALKING\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.petIds[0]").value(petId))
                .andExpect(jsonPath("$.employeeIds[0]").value(employeeId));
        mockMvc.perform(get("/schedule/customer/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].petIds[0]").value(petId));
        mockMvc.perform(get("/schedule/pet/{petId}", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber());
        mockMvc.perform(get("/schedule/employee/{employeeId}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber());
        mockMvc.perform(get("/schedule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber());
    }

    @Test
    void createsCustomerAndFindsPetsByOwner() {
        Customer customer = new Customer();
        customer.setName("Sam");
        customer.setPhoneNumber("555-0100");
        Customer savedCustomer = userService.saveCustomer(customer, null);

        Pet pet = new Pet();
        pet.setName("Milo");
        pet.setType(PetType.CAT);
        petService.savePet(pet, savedCustomer.getId());

        List<Pet> pets = petService.findPetsByOwner(savedCustomer.getId());
        assertEquals(1, pets.size());
        assertEquals(savedCustomer.getId(), pets.get(0).getCustomer().getId());
        assertEquals(1, userService.getCustomer(savedCustomer.getId()).getPets().size());
    }

    @Test
    void addsExistingPetsToCustomer() {
        Pet pet = new Pet();
        pet.setName("Nori");
        pet.setType(PetType.FISH);
        Pet savedPet = petService.savePet(pet, null);

        Customer customer = new Customer();
        customer.setName("Morgan");
        Customer savedCustomer = userService.saveCustomer(customer, List.of(savedPet.getId()));

        assertEquals(savedCustomer.getId(), petService.getPet(savedPet.getId()).getCustomer().getId());
        assertEquals(List.of(savedPet.getId()),
                userService.getCustomer(savedCustomer.getId()).getPets().stream()
                        .map(Pet::getId).collect(java.util.stream.Collectors.toList()));
    }

    @Test
    void findsEmployeesByRequiredSkillsAndAvailability() {
        Employee employee = new Employee();
        employee.setName("Alex");
        employee.setSkills(Set.of(EmployeeSkill.FEEDING, EmployeeSkill.WALKING));
        employee.setDaysAvailable(Set.of(DayOfWeek.MONDAY));
        Employee savedEmployee = userService.saveEmployee(employee);

        assertEquals(1, userService.findAvailableEmployees(
                Set.of(EmployeeSkill.FEEDING), LocalDate.of(2026, 11, 16)).size());
        assertTrue(userService.findAvailableEmployees(
                Set.of(EmployeeSkill.MEDICATING), LocalDate.of(2026, 11, 16)).isEmpty());
        assertTrue(userService.findAvailableEmployees(
                Set.of(EmployeeSkill.FEEDING), LocalDate.of(2026, 11, 17)).isEmpty());

        userService.setEmployeeAvailability(Set.of(DayOfWeek.TUESDAY), savedEmployee.getId());
        assertEquals(Set.of(DayOfWeek.TUESDAY),
                userService.getEmployee(savedEmployee.getId()).getDaysAvailable());
    }

    @Test
    void schedulesPetsAndEmployeesAndFindsScheduleByRelatedEntities() {
        Customer customer = new Customer();
        customer.setName("Taylor");
        Customer savedCustomer = userService.saveCustomer(customer, null);

        Pet pet = new Pet();
        pet.setName("Pip");
        pet.setType(PetType.DOG);
        Pet savedPet = petService.savePet(pet, savedCustomer.getId());

        Employee employee = new Employee();
        employee.setName("Jordan");
        Employee savedEmployee = userService.saveEmployee(employee);

        Schedule schedule = new Schedule();
        schedule.setDate(LocalDate.of(2026, 11, 16));
        schedule.setActivities(Set.of(EmployeeSkill.WALKING));
        Schedule savedSchedule = scheduleService.saveSchedule(
                schedule, Set.of(savedPet.getId()), Set.of(savedEmployee.getId()));

        assertEquals(1, scheduleService.getSchedulesForPet(savedPet.getId()).size());
        assertEquals(1, scheduleService.getSchedulesForEmployee(savedEmployee.getId()).size());
        assertEquals(1, scheduleService.getSchedulesForCustomer(savedCustomer.getId()).size());
        assertEquals(Set.of(savedEmployee.getId()),
                scheduleService.getAllSchedules().get(0).getEmployees().stream()
                        .map(Employee::getId).collect(java.util.stream.Collectors.toSet()));
        assertEquals(savedSchedule.getId(), scheduleService.getAllSchedules().get(0).getId());
    }
}
