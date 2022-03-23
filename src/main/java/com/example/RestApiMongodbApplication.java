package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class RestApiMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiMongodbApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(
            StudentRepository repository,
            MongoTemplate mongoTemplate){
        return args -> {
            Address address = new Address(
                    "Mexico",
                    "Mexico",
                    "57000"
            );

            String email = "mel@mail.com";

            Student student = new Student(
                 "Melissa",
                 "Ramirez",
                 email,
                 Gender.FEMALE,
                 address,
                 List.of("Computer Science", "Maths"),
                 BigDecimal.TEN,
                    LocalDateTime.now()
            );
            //solo como referencia
            //usingMongoTemplateQuery(repository, mongoTemplate, email, student);

            //metodo mas sencillo usando Optional en la  interface StudentRepository
            repository.findStudentByEmail(email).
                    ifPresentOrElse(s -> {
                        System.out.println(s + " already exist");
                    }, ()->{
                        System.out.println("Inserting student " + student);
                        repository.insert(student);
                    });

        };
    }

/**
    private void //usingMongoTemplateQuery(StudentRepository repository, MongoTemplate mongoTemplate, String email, Student student) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        List<Student> students = mongoTemplate.find(query, Student.class);

        if(students.size() > 1){
            throw new IllegalStateException(
                    "Found many students with email" + email);
        }
        if(students.isEmpty()){
            System.out.println("Inserting student " + student);
            repository.insert(student);
        } else {
            System.out.println(student + " already exist");
        }
    }
*/

}
