package com.softserveinc.ita.homeproject.homedata.specifications;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecification extends EntitySpecification<User> {

    public Specification<User> getUsers(String search, String sort) {
        return super.getEntity(search, sort);
    }
}
