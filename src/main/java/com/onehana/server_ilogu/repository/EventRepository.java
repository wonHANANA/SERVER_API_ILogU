package com.onehana.server_ilogu.repository;

import com.onehana.server_ilogu.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
