package ua.yarynych.taskapi.config.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataSourceConfigTest {

    @InjectMocks
    private DataSourceConfig dataSourceConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPrimaryH2DataSource() {
        // Act
        DataSource dataSource = dataSourceConfig.primaryH2DataSource();

        // Assert
        assertNotNull(dataSource);
    }

    @Test
    void testSecondaryPostgreDataSource() {
        // Act
        DataSource dataSource = dataSourceConfig.secondaryPostgreDataSource();

        // Assert
        assertNotNull(dataSource);
    }

    @Test
    void testDataSource() {
        // Act
        DataSource dataSource = dataSourceConfig.dataSource();

        // Assert
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof CustomRoutingDataSource, "The data source should be an instance of CustomRoutingDataSource");
    }
}
