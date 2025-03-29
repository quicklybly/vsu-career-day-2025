package org.quicklybly.spring;

import java.util.Collection;

public interface ContactRepository {

    void save(Contact contact);
    Collection<Contact> findAll();
}
