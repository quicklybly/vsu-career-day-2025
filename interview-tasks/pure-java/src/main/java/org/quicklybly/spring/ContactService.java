package org.quicklybly.spring;

import java.util.ArrayList;
import java.util.List;

@Bean
public class ContactService {
    static public String KT = "TOPIC";
    static public int ONPAGE = 10;

    public ContactRepository repo;
    public KafkaTemplate<String, String> kafka;

    private long id = 0;
    private String title;

    void save(Long a, String b) {
        id = a;
        title = b;
        var contact = new Contact(id, title);
        repo.save(contact);
        kafka.send(KT, id + " " + title);
    }

    public List<Contact> getPage(int page) {
        List<Contact> contacts = (List<Contact>) repo.findAll();
        var res = new ArrayList<Contact>();
        for (int i = 0; i < ONPAGE; i++) {
            Contact c = contacts.get(i + page * ONPAGE);
            res.add(c);
        }
        System.out.println("retrieved " + res.size() + " contacts" + " from page " + page);
        return res;
    }
}
