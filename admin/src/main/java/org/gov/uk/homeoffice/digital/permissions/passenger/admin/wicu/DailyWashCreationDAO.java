package org.gov.uk.homeoffice.digital.permissions.passenger.admin.wicu;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindFields;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DailyWashCreationDAO {

    @SqlQuery("SELECT * FROM daily_wash_view where creation_time::date >= current_date - :days ORDER BY creation_time DESC, type")
    @RegisterRowMapper(DailyWashCreationDAO.DailyWashViewMapper.class)
    List<DailyWashView> getLatest(@Bind("days") int days);

    @SqlQuery("SELECT * FROM daily_wash_creation where id = :id")
    @RegisterRowMapper(DailyWashCreationDAO.DailyWashCreationMapper.class)
    DailyWashCreation get(@Bind("id") long id);

    @SqlQuery("SELECT * FROM daily_wash_creation where uuid = :uuid")
    @RegisterRowMapper(DailyWashCreationDAO.DailyWashCreationMapper.class)
    DailyWashCreation get(@Bind("uuid") UUID uuid);

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO daily_wash_creation " +
            "(uuid, creation_time, rows, creator_username, document_check_filename, name_check_filename, full_name) VALUES " +
            "(:uuid, :creationTime, :rows, :creatorUsername, :documentCheckFilename, :nameCheckFilename, :fullName)")
    @RegisterRowMapper(DailyWashCreationDAO.DailyWashCreationMapper.class)
    DailyWashCreation save(@BindFields DailyWashCreation dailyWashCreation);

    class DailyWashCreationMapper implements RowMapper<DailyWashCreation> {
        @Override
        public DailyWashCreation map(ResultSet r, StatementContext ctx) throws SQLException {
            return new DailyWashCreation(
                    r.getLong("id"),
                    (UUID) r.getObject("uuid"),
                    r.getTimestamp("creation_time").toInstant(),
                    r.getInt("rows"),
                    r.getString("creator_username"),
                    r.getString("document_check_filename"),
                    r.getString("name_check_filename"),
                    r.getString("full_name"));
        }
    }

    class DailyWashViewMapper implements RowMapper<DailyWashView> {
        @Override
        public DailyWashView map(ResultSet r, StatementContext ctx) throws SQLException {
            return new DailyWashView(
                    (UUID) r.getObject("uuid"),
                    r.getString("type"),
                    r.getString("filename"),
                    r.getTimestamp("creation_time").toInstant(),
                    r.getString("creator_username"),
                    r.getString("creator_full_name"),
                    r.getInt("rows"),
                    Optional.ofNullable(r.getTimestamp("download_time")).map(t -> t.toInstant()).orElse(null),
                    r.getString("username"),
                    r.getString("download_full_name"));
        }
    }

}
