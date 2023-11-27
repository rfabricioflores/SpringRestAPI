package se.fabricioflores.springrestapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import se.fabricioflores.springrestapi.dto.AddUserReq;
import se.fabricioflores.springrestapi.model.Role;
import se.fabricioflores.springrestapi.model.User;
import se.fabricioflores.springrestapi.repo.RoleRepo;
import se.fabricioflores.springrestapi.service.IUserService;

import java.util.List;

@Component
public class InitialData implements CommandLineRunner {
    private final IUserService userService;
    private final RoleRepo roleRepo;
    private static final Logger logger = LoggerFactory.getLogger(InitialData.class);

    @Autowired
    public InitialData(IUserService userService, RoleRepo roleRepo) {
        this.userService = userService;
        this.roleRepo = roleRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        var optUserRole = roleRepo.findRoleByName("USER");
        var optAdminRole = roleRepo.findRoleByName("ADMIN");

        if (optUserRole.isEmpty()) {
            roleRepo.save(new Role("USER"));
            logger.info("Created USER role");
        }
        if (optAdminRole.isEmpty()) {
            roleRepo.save(new Role("ADMIN"));
            logger.info("Created ADMIN role");
        }

        try {
            User user = userService.save(
                    new AddUserReq(
                            "admin",
                            "hello",
                            List.of("ADMIN")
                    )
            );
            logger.info("Created default admin user with username: " + user.getUsername());
        } catch (RuntimeException ignored) {
        }
    }
}
