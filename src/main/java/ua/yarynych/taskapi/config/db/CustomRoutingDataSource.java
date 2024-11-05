package ua.yarynych.taskapi.config.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * CustomRoutingDataSource is an extension of AbstractRoutingDataSource that
 * determines the current data source based on the availability of the primary
 * H2 database. If the H2 database is available, it routes requests to it;
 * otherwise, it defaults to the PostgreSQL database.
 */
public class CustomRoutingDataSource extends AbstractRoutingDataSource {

    private static final Logger logger = LoggerFactory.getLogger(CustomRoutingDataSource.class);

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Determines the current lookup key for the data source.
     *
     * @return the data source lookup key ("H2" or "PostgreSQL") based on the availability
     *         of the primary H2 database. Returns null if no suitable source is found.
     */
    @Override
    protected Object determineCurrentLookupKey() {
        try {
            DataSource primaryDataSource = applicationContext.getBean("primaryH2DataSource", DataSource.class);
            try (Connection connection = primaryDataSource.getConnection()) {
                if (connection != null) {
                    logger.info("Using primary H2 data source.");
                    return "H2";
                }
            }
        } catch (SQLException e) {
            logger.warn("Primary H2 data source is unavailable, falling back to PostgreSQL.", e);
            return "PostgreSQL";
        }

        logger.error("No suitable data source found.");
        return null;
    }
}
