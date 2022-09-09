package com.mealfit.common.event.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealfit.common.event.domain.EventEntry;
import com.mealfit.common.event.domain.EventRepository;
import com.mealfit.common.event.domain.EventStatus;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcEventRepository implements EventRepository {

    private ObjectMapper objectMapper;
    private JdbcTemplate jdbcTemplate;

    public JdbcEventRepository(ObjectMapper objectMapper, JdbcTemplate jdbcTemplate) {
        this.objectMapper = objectMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public EventEntry save(Object event) {
        EventEntry eventEntry = new EventEntry(event.getClass().getName(), "application/json",
              toJson(event));

        jdbcTemplate.update("INSERT INTO event_entry" +
                    "(event_type, content_type, payload, status, event_timestamp)" +
                    "VALUES(?, ?, ?, ?, ?)",
              ps -> {
                  ps.setString(1, eventEntry.getType());
                  ps.setString(2, eventEntry.getContentType());
                  ps.setString(3, eventEntry.getPayload());
                  ps.setString(4, eventEntry.getStatus().toString());
                  ps.setTimestamp(5, new Timestamp(eventEntry.getTimeStamp()));
              }
        );

        return eventEntry;
    }

    @Override
    public void updateStatus(long id, EventStatus status) {
        jdbcTemplate.update(
              "UPDATE event_entry SET status = ? WHERE id = ?",
              ps -> {
                  ps.setString(1, status.toString());
                  ps.setLong(2, id);
              }
        );
    }


    private String toJson(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EventEntry> get(long offset, long limit) {
        return jdbcTemplate.query(
              "SELECT * FROM event_entry ORDER BY id ASC limit ?, ?",
              ps -> {
                  ps.setLong(1, offset);
                  ps.setLong(2, limit);
              },
              (rs, rowNum) -> {
                  return new EventEntry(
                        rs.getLong("id"),
                        rs.getString("event_type"),
                        rs.getString("content_type"),
                        rs.getString("payload"),
                        rs.getString("status"),
                        rs.getTimestamp("event_timestamp").getTime());
              }
        );
    }
}
