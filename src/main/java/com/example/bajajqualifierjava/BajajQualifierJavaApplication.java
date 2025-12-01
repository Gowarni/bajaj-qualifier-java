package com.example.bajajqualifierjava;

import com.example.bajajqualifierjava.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BajajQualifierJavaApplication implements CommandLineRunner {

    @Autowired
    private WebhookService webhookService;

    public static void main(String[] args) {
        SpringApplication.run(BajajQualifierJavaApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // This runs automatically after app startup
        webhookService.initWebhook();
        
        String finalQuery =
                "SELECT\n" +
                "    d.DEPARTMENT_NAME,\n" +
                "    AVG(TIMESTAMPDIFF(YEAR, e.DOB, CURDATE())) AS AVERAGE_AGE,\n" +
                "    SUBSTRING_INDEX(\n" +
                "        GROUP_CONCAT(CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME)\n" +
                "                     ORDER BY e.FIRST_NAME, e.LAST_NAME\n" +
                "                     SEPARATOR ', '),\n" +
                "        ', ',\n" +
                "        10\n" +
                "    ) AS EMPLOYEE_LIST\n" +
                "FROM DEPARTMENT d\n" +
                "JOIN EMPLOYEE e\n" +
                "  ON e.DEPARTMENT = d.DEPARTMENT_ID\n" +
                "JOIN (\n" +
                "    SELECT DISTINCT EMP_ID\n" +
                "    FROM PAYMENTS\n" +
                "    WHERE AMOUNT > 70000\n" +
                ") p\n" +
                "  ON p.EMP_ID = e.EMP_ID\n" +
                "GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME\n" +
                "ORDER BY d.DEPARTMENT_ID DESC;";
        
        webhookService.submitFinalQuery(finalQuery);
    }
}

