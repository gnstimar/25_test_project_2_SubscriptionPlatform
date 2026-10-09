package se.lexicon.subscriptionapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import se.lexicon.subscriptionapi.domain.constant.Role;
import se.lexicon.subscriptionapi.domain.constant.ServiceType;
import se.lexicon.subscriptionapi.domain.entity.Customer;
import se.lexicon.subscriptionapi.domain.entity.Operator;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.repository.CustomerRepository;
import se.lexicon.subscriptionapi.repository.OperatorRepository;
import se.lexicon.subscriptionapi.repository.PlanRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlanRepository planRepository;
    private final OperatorRepository operatorRepository;

    @Override
    public void run(String... args) throws Exception {
        seedAdminUser();
        seedRegularUser();
        if (operatorRepository.count() > 0) {
            IO.println("Database already seeded with operators and plans. Skipping data seeding.");
            return;
        }
        IO.println("Starting data seeding for operators and plans...");

        Operator telia = new Operator();
        telia.setName("Telia Operator");

        Operator telenor = new Operator();
        telenor.setName("Telenor Operator");

        operatorRepository.saveAll(List.of(telia, telenor));

        Plan teliaFiber100 = new Plan();
        teliaFiber100.setName("Telia Fiber 100");
        teliaFiber100.setPrice(new BigDecimal("100.00"));
        teliaFiber100.setServiceType(ServiceType.INTERNET);
        teliaFiber100.setDataLimit(100);
        teliaFiber100.setActive(true);
        teliaFiber100.setOperator(telia);

        Plan teliaFiber50 = new Plan();
        teliaFiber50.setName("Telia Fiber 50");
        teliaFiber50.setPrice(new BigDecimal("50.00"));
        teliaFiber50.setServiceType(ServiceType.INTERNET);
        teliaFiber50.setDataLimit(50);
        teliaFiber50.setActive(true);
        teliaFiber50.setOperator(telia);

        Plan teliaInactivePlan = new Plan();
        teliaInactivePlan.setName("Telia Inactive Plan");
        teliaInactivePlan.setPrice(new BigDecimal("50.00"));
        teliaInactivePlan.setServiceType(ServiceType.INTERNET);
        teliaInactivePlan.setDataLimit(null);
        teliaInactivePlan.setActive(false);
        teliaInactivePlan.setOperator(telia);

        Plan teliaMobile10 = new Plan();
        teliaMobile10.setName("Telia Mobile 10");
        teliaMobile10.setPrice(new BigDecimal("100.00"));
        teliaMobile10.setServiceType(ServiceType.MOBILE);
        teliaMobile10.setDataLimit(10);
        teliaMobile10.setActive(true);
        teliaMobile10.setOperator(telia);

        Plan telenorFiber200 = new Plan();
        telenorFiber200.setName("Telenor Fiber 200");
        telenorFiber200.setPrice(new BigDecimal("200.00"));
        telenorFiber200.setServiceType(ServiceType.INTERNET);
        telenorFiber200.setDataLimit(200);
        telenorFiber200.setActive(true);
        telenorFiber200.setOperator(telenor);

        Plan telenorFiberOld = new Plan();
        telenorFiberOld.setName("Telenor Fiber Old");
        telenorFiberOld.setPrice(new BigDecimal("200.00"));
        telenorFiberOld.setServiceType(ServiceType.INTERNET);
        telenorFiberOld.setDataLimit(null);
        telenorFiberOld.setActive(false);
        telenorFiberOld.setOperator(telenor);

        Plan telenorMobile = new Plan();
        telenorMobile.setName("Telenor Mobile");
        telenorMobile.setPrice(new BigDecimal("200.00"));
        telenorMobile.setServiceType(ServiceType.MOBILE);
        telenorMobile.setDataLimit(10);
        telenorMobile.setActive(true);
        telenorMobile.setOperator(telenor);

        planRepository.saveAll(List.of(
                teliaFiber100, teliaFiber50, teliaInactivePlan, teliaMobile10, telenorFiber200, telenorFiberOld, telenorMobile
        ));

        IO.println("Data seeding completed successfully. Inserted: 2 operators and 7 plans.");
    }

    private void seedAdminUser() {
        String adminEmail = "admin@example.com";
        if (!customerRepository.existsByEmail(adminEmail)) {
            Customer admin = new Customer();
            admin.setEmail(adminEmail);
            admin.setFirstName("Admin");
            admin.setLastName("Adminson");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER));
            customerRepository.save(admin);
            System.out.println("[DATA_SEED] Admin user created: " + adminEmail);
        }
    }

    private void seedRegularUser() {
        String userEmail = "user@example.com";
        if (!customerRepository.existsByEmail(userEmail)) {
            Customer user = new Customer();
            user.setEmail(userEmail);
            user.setFirstName("User");
            user.setLastName("Userson");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRoles(Set.of(Role.ROLE_USER));
            customerRepository.save(user);
            System.out.println("[DATA_SEED] Regular user created: " + userEmail);
        }
    }

}
