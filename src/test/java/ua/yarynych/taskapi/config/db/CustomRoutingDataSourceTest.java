package ua.yarynych.taskapi.config.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomRoutingDataSourceTest {

    @InjectMocks
    private CustomRoutingDataSource customRoutingDataSource;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private DataSource primaryDataSource;

    @Mock
    private Connection connection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDetermineCurrentLookupKeyPrimaryDataSourceAvailable() throws SQLException {
        // Arrange
        when(applicationContext.getBean("primaryH2DataSource", DataSource.class)).thenReturn(primaryDataSource);
        when(primaryDataSource.getConnection()).thenReturn(connection);

        // Act
        Object result = customRoutingDataSource.determineCurrentLookupKey();

        // Assert
        assertEquals("H2", result);
        verify(connection).close(); // Ensure the connection is closed
    }

    @Test
    void testDetermineCurrentLookupKeyPrimaryDataSourceUnavailable() throws SQLException {
        // Arrange
        when(applicationContext.getBean("primaryH2DataSource", DataSource.class)).thenReturn(primaryDataSource);
        when(primaryDataSource.getConnection()).thenThrow(new SQLException("Connection failed"));

        // Act
        Object result = customRoutingDataSource.determineCurrentLookupKey();

        // Assert
        assertEquals("PostgreSQL", result);
    }

    @Test
    void testDetermineCurrentLookupKeyNoSuitableDataSource() throws SQLException {
        // Arrange
        when(applicationContext.getBean("primaryH2DataSource", DataSource.class)).thenReturn(primaryDataSource);
        when(primaryDataSource.getConnection()).thenThrow(new SQLException("Connection failed"));

        // Act
        Object result = customRoutingDataSource.determineCurrentLookupKey();

        // Assert
        assertEquals("PostgreSQL", result);
        verifyNoMoreInteractions(connection);
    }
}