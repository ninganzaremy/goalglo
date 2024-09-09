package com.goalglo.services;

import com.goalglo.aws.AwsSesService;
import com.goalglo.common.ResourceNotFoundException;
import com.goalglo.config.SecretConfig;
import com.goalglo.dto.AppointmentDTO;
import com.goalglo.entities.Appointment;
import com.goalglo.entities.ServiceEntity;
import com.goalglo.entities.TimeSlot;
import com.goalglo.entities.User;
import com.goalglo.repositories.AppointmentRepository;
import com.goalglo.repositories.ServiceRepository;
import com.goalglo.tokens.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

   private final AppointmentRepository appointmentRepository;
   private final TimeSlotService timeSlotService;
   private final UserService userService;
   private final ServiceRepository serviceRepository;
   private final JwtUtils jwtUtils;
   private final AwsSesService awsSesService;
   private final EmailTemplateService emailTemplateService;
   private final SecretConfig secretConfig;


   @Autowired
   public AppointmentService(AppointmentRepository appointmentRepository, TimeSlotService timeSlotService,
                             UserService userService, ServiceRepository serviceRepository, JwtUtils jwtUtils, AwsSesService awsSesService, SecretConfig secretConfig, EmailTemplateService emailTemplateService) {
      this.appointmentRepository = appointmentRepository;
      this.timeSlotService = timeSlotService;
      this.userService = userService;
      this.serviceRepository = serviceRepository;
      this.jwtUtils = jwtUtils;
      this.awsSesService = awsSesService;
      this.secretConfig = secretConfig;
      this.emailTemplateService = emailTemplateService;


   }

   /**
    * Books an appointment for a logged-in user.
    *
    * @param appointmentDTO The appointment details.
    * @param timeSlotId     The ID of the time slot to book.
    * @param authentication The authentication object containing the logged-in user's details.
    * @return The booked appointment details.
    * @throws IllegalStateException If the time slot is already booked.
    */
   public AppointmentDTO bookAppointment(AppointmentDTO appointmentDTO, UUID timeSlotId,
                                         Authentication authentication) {
      // Check if the time slot is available
      TimeSlot timeSlot = timeSlotService.findTimeSlotById(timeSlotId)
         .orElseThrow(() -> new ResourceNotFoundException("TimeSlot not found"));

      if (timeSlot.isBooked()) {
         throw new IllegalStateException("Time slot is already booked");
      }
      String username = (authentication != null) ? authentication.getName() : null;

      User user;

      if (username != null) {
         // Find the logged-in user
         user = userService.findByUsernameOrEmail(username);
      } else {
         // Handle non-logged-in users by finding or creating a user by email
         user = userService.findOrCreateUserByEmail(appointmentDTO.getEmail(), appointmentDTO);
      }

      // Create the appointment and link it to the user and time slot
      Appointment appointment = new Appointment(appointmentDTO);
      appointment.setUser(user);
      appointment.setTimeSlot(timeSlot);

      // Set the service entity if available in the DTO
      if (appointmentDTO.getServiceId() != null) {
         ServiceEntity service = serviceRepository.findById(appointmentDTO.getServiceId())
            .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
         appointment.setService(service);
      }
      // Save the appointment
      appointment = appointmentRepository.save(appointment);

      sendBookingConfirmationEmail(user, appointment);

      timeSlotService.bookSlot(timeSlotId, appointment);

      return new AppointmentDTO(appointment);
   }

   /**
    * Sends a booking confirmation email to the user.
    *
    * @param user        The user to send the email to.
    * @param appointment The appointment details.
    */
   private void sendBookingConfirmationEmail(User user, Appointment appointment) {
      String bookingConfirmationEmailTemplate = secretConfig.getEmail().getTemplates().getBookingConfirmationEmail();
      String domain = secretConfig.getActiveDomain();
      String companyEmail = secretConfig.getContact().getCompanyEmail();
      String companyPhoneNumber = secretConfig.getContact().getCompanyPhoneNumber();

      String subject = emailTemplateService.getSubjectByTemplateName(bookingConfirmationEmailTemplate);
      String body = emailTemplateService.getBodyByTemplateName(bookingConfirmationEmailTemplate)
         .replace("{service_name}", appointment.getService().getName())
         .replace("{booking_date}", appointment.getStartTime().toLocalDate().toString())
         .replace("{booking_time}", appointment.getStartTime().toLocalTime().toString())
         .replace("{link_to_registration}", domain + "/register")
         .replace("{company_email}", companyEmail)
         .replace("{company_phone_number}", companyPhoneNumber);

      awsSesService.sendEmail(user.getEmail(), subject, body);
   }

   /**
    * Finds an appointment by its UUID.
    *
    * @param id The UUID of the appointment to find.
    * @return An Optional containing the appointment if found, or an empty Optional
    *         if not.
    */
   public Optional<AppointmentDTO> findAppointmentById(UUID id) {
      return appointmentRepository.findById(id).map(AppointmentDTO::new);
   }

   /**
    * Finds all appointments associated with a specific user.
    *
    * @param username The UUID of the user.
    * @return A list of appointments associated with the user.
    */
   public List<AppointmentDTO> findUserAppointments(Authentication authentication) {

      return appointmentRepository.findByUserId(getCurrentUser(authentication).getId()).stream()
         .map(this::convertToDTO)
         .collect(Collectors.toList());
   }

   /**
    * Updates an existing appointment with new information.
    *
    * @param id The UUID of the appointment to update.
    * @return An Optional containing the updated appointment if found and updated,
    *         or an empty Optional if not.
    */
   public Optional<AppointmentDTO> updateAppointment(UUID id, AppointmentDTO appointmentDTO,
                                                     Authentication authentication) {
      return appointmentRepository.findById(id)
         .filter(appointment -> appointment.getUser().getId().equals(getCurrentUser(authentication).getId()))
         .map(appointment -> {
            updateAppointmentFields(appointment, appointmentDTO);
            return convertToDTO(appointmentRepository.save(appointment));
         });
   }

   /**
    * Cancels an existing appointment.
    *
    * @param id The UUID of the appointment to cancel.
    * @return true if the appointment was found and canceled, false otherwise.
    */
   public boolean cancelAppointment(UUID id, Authentication authentication) {

      return appointmentRepository.findById(id)
         .filter(appointment -> appointment.getUser().getId().equals(getCurrentUser(authentication).getId()))
         .map(appointment -> {
            appointment.setStatus("Canceled");
            appointmentRepository.save(appointment);
            timeSlotService.cancelBooking(appointment.getTimeSlot().getId());
            return true;
         })
         .orElse(false);
   }

   /**
    * Retrieves all appointments.
    *
    * @return A list of all appointments.
    */
   public List<AppointmentDTO> findAllAppointments() {
      return appointmentRepository.findAllWithDetails().stream()
         .map(this::convertToDTO)
         .collect(Collectors.toList());
   }

   /**
    * Converts an appointment entity to an appointment DTO.
    *
    * @param appointment The appointment entity to convert.
    * @return The corresponding appointment DTO.
    */
   private AppointmentDTO convertToDTO(Appointment appointment) {
      AppointmentDTO dto = new AppointmentDTO();
      dto.setId(appointment.getId());
      dto.setUserId(appointment.getUser().getId());
      dto.setTimeSlotId(appointment.getTimeSlot().getId());
      dto.setServiceId(appointment.getService() != null ? appointment.getService().getId() : null);
      dto.setUserName(appointment.getUser().getFirstName() + " " + appointment.getUser().getLastName());

      dto.setStatus(appointment.getStatus());
      dto.setNotes(appointment.getNotes());
      dto.setServiceName(appointment.getService().getName());
      dto.setStartTime(appointment.getStartTime());
      dto.setEndTime(appointment.getEndTime());
      return dto;
   }

   /**
    * Updates the fields of an appointment entity based on the provided DTO.
    *
    * @param appointment The appointment entity to update.
    * @param dto         The DTO containing the new values.
    */
   private void updateAppointmentFields(Appointment appointment, AppointmentDTO dto) {
      appointment.setStatus(dto.getStatus());
      appointment.setNotes(dto.getNotes());
      appointment.setStartTime(dto.getStartTime());
      appointment.setEndTime(dto.getEndTime());
   }


   /**
    * Updates the status of an existing appointment.
    *
    * @param id The UUID of the appointment to update.
    * @return An Optional containing the updated appointment if found and updated,
    * or an empty Optional if not.
    */
   @Transactional
   public AppointmentDTO updateAppointmentStatus(UUID id, String newStatus) {
      Appointment appointment = appointmentRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

      // Check if the status is actually changing
      if (!appointment.getStatus().equalsIgnoreCase(newStatus)) {
         String oldStatus = appointment.getStatus();
         appointment.setStatus(newStatus);

         boolean isNewlyRejected = (newStatus.equalsIgnoreCase("Denied") || newStatus.equalsIgnoreCase("Canceled"))
            && !oldStatus.equalsIgnoreCase("Denied") && !oldStatus.equalsIgnoreCase("Canceled");

         if (isNewlyRejected) {
            timeSlotService.cancelBooking(appointment.getTimeSlot().getId());
         } else if (newStatus.equalsIgnoreCase("Accepted")
            && (oldStatus.equalsIgnoreCase("Denied") || oldStatus.equalsIgnoreCase("Canceled"))) {
            timeSlotService.bookSlot(appointment.getTimeSlot().getId(), appointment);
         }

         // Save the updated appointment
         appointment = appointmentRepository.save(appointment);
      }

      return convertToDTO(appointment);
   }

   /**
    * Retrieves the current user based on the authentication object.
    *
    * @param authentication The authentication object containing the current user's information.
    * @return The User object representing the current user.
    * @throws RuntimeException If the user is not found.
    */
   private User getCurrentUser(Authentication authentication) {
      return jwtUtils.getCurrentUser(authentication)
         .orElseThrow(() -> new RuntimeException("User not found"));
   }
}