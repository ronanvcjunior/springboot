package br.com.ronan.springboot.util;

import br.com.ronan.springboot.domain.SpringbootUser;

public class SpringbootUserCreator {

    public static SpringbootUser creatSpringbootUserUser() {
        SpringbootUser user = SpringbootUser.builder().name("Usuario").userName("user")
                .password("{bcrypt}$2a$10$HMC3iurdXOce7kcblI1blOcngJAbScCFQ4LeCAEGMmG71Vw6dPSyi")
                .authorities("ROLE_USER").build();

        return user;
    }

    public static SpringbootUser creatSpringbootUserAdmin() {
        SpringbootUser user = SpringbootUser.builder().name("Administrador").userName("admin")
                .password("{bcrypt}$2a$10$HMC3iurdXOce7kcblI1blOcngJAbScCFQ4LeCAEGMmG71Vw6dPSyi")
                .authorities("ROLE_USER, ROLE_ADMIN").build();

        return user;
    }
}
