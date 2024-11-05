package ua.yarynych.taskapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import ua.yarynych.taskapi.entity.Task;

@Repository
@EnableJpaRepositories(
        basePackages = "ua.yarynych.taskapi.repository",
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        transactionManagerRef = "primaryTransactionManager"
)
public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByName(String name);
}
