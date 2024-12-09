package org.shift;

import org.shift.dao.PersonDao;
import org.shift.entity.Person;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DaoRunner {
    public static void main(String[] args) {
//        save();
//        delete();
//        getById();
//        update();
        getAll();
    }

    private static void getAll() {
        PersonDao personDao = PersonDao.getInstance();
        List<Person> all = personDao.findAll();
        all.forEach(System.out::println);
    }

    private static void update() {
        PersonDao personDao = PersonDao.getInstance();
        Optional<Person> person = personDao.findById(1);
        person.ifPresent(value -> {
            value.setAddress("new address");
            personDao.update(value);
        });

        Optional<Person> updatedPerson = personDao.findById(1);
        updatedPerson.ifPresentOrElse(System.out::println, () -> System.out.println("Person not found"));
    }

    private static void getById() {
        PersonDao personDao = PersonDao.getInstance();
        Optional<Person> person = personDao.findById(10);
        person.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Person not found")
        );
    }

    private static void delete() {
        PersonDao personDao = PersonDao.getInstance();
        boolean deleted = personDao.delete(1);
        System.out.println(deleted);
    }

    private static void save() {
        PersonDao personDao = PersonDao.getInstance();
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Smith");
        person.setAddress("Street 1");
        person.setBirthDate(LocalDate.of(1990, 1, 1));

        Person savedPerson = personDao.save(person);
        System.out.println(person);
    }
}
