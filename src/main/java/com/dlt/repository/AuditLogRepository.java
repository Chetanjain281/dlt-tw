package com.dlt.repository;

import com.dlt.model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
    List<AuditLog> findByOrderIdOrderByTimestampDesc(String orderId);
    List<AuditLog> findByStageOrderByTimestampDesc(String stage);
}
