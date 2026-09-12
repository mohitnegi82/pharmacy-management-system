package com.pharmacy.pms.config;

import com.pharmacy.pms.model.OTCMedicine;
import com.pharmacy.pms.model.Role;
import com.pharmacy.pms.model.User;
import com.pharmacy.pms.repository.OTCMedicineRepository;
import com.pharmacy.pms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Creates a default ADMIN pharmacist account + sample OTC reference data on first run.
// Login -> username: admin | password: admin123
// CHANGE THIS PASSWORD before submitting/deploying the project.
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OTCMedicineRepository otcMedicineRepository;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Default Admin")
                    .email("admin@pharmacy.local")
                    .role(Role.ADMIN)
                    .active(true)
                    .build();
            userRepository.save(admin);
            System.out.println(">>> Default admin created -> username: admin | password: admin123");
        }

        if (otcMedicineRepository.count() == 0) {
            otcMedicineRepository.saveAll(java.util.List.of(
                    OTCMedicine.builder().name("Paracetamol 250mg Syrup").category("Fever")
                            .minAge(2).maxAge(12).dosage("5-10ml every 6 hours (max 4 doses/day)")
                            .notes("Avoid if allergic to paracetamol. Consult if fever persists >3 days.").priority(1).build(),
                    OTCMedicine.builder().name("Paracetamol 500mg Tablet").category("Fever")
                            .minAge(13).maxAge(65).dosage("1 tablet every 6 hours (max 4/day)")
                            .notes("Avoid with liver conditions or alcohol use.").priority(1).build(),
                    OTCMedicine.builder().name("Ibuprofen 200mg Tablet").category("Pain Relief")
                            .minAge(13).maxAge(65).dosage("1 tablet every 8 hours after food")
                            .notes("Avoid on empty stomach and in asthma/ulcer patients.").priority(1).build(),
                    OTCMedicine.builder().name("Paracetamol 500mg Tablet").category("Pain Relief")
                            .minAge(13).maxAge(65).dosage("1 tablet every 6 hours (max 4/day)")
                            .notes("Safer alternative to NSAIDs for mild pain.").priority(2).build(),
                    OTCMedicine.builder().name("Cetirizine 10mg Tablet").category("Allergy")
                            .minAge(13).maxAge(65).dosage("1 tablet once daily")
                            .notes("May cause mild drowsiness.").priority(1).build(),
                    OTCMedicine.builder().name("Cetirizine Syrup 5mg/5ml").category("Allergy")
                            .minAge(2).maxAge(12).dosage("5ml once daily")
                            .notes("Consult doctor if under 2 years.").priority(1).build(),
                    OTCMedicine.builder().name("Dextromethorphan Cough Syrup").category("Cold & Cough")
                            .minAge(6).maxAge(65).dosage("10ml every 6-8 hours")
                            .notes("Not for children under 6. Avoid with MAO inhibitors.").priority(1).build(),
                    OTCMedicine.builder().name("Saline Nasal Drops").category("Cold & Cough")
                            .minAge(0).maxAge(5).dosage("2-3 drops each nostril as needed")
                            .notes("Safe for infants; no medication interaction.").priority(1).build(),
                    OTCMedicine.builder().name("ORS (Oral Rehydration Salts)").category("Digestive")
                            .minAge(0).maxAge(65).dosage("1 sachet dissolved in 1L water, sip through the day")
                            .notes("First-line for diarrhea/dehydration at any age.").priority(1).build(),
                    OTCMedicine.builder().name("Antacid Tablet (Calcium Carbonate)").category("Digestive")
                            .minAge(13).maxAge(65).dosage("1-2 tablets as needed after meals")
                            .notes("Avoid overuse; consult if symptoms persist >2 weeks.").priority(2).build()
            ));
            System.out.println(">>> Sample OTC reference medicines seeded.");
        }
    }
}
