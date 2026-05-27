package com.fake.tinkoff.repository;

import com.fake.tinkoff.model.Payment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    public PaymentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Payment payment) {
        jdbcTemplate.update(
                "INSERT INTO payments (payment_id, order_id, terminal_key, amount, status, notification_url) " +
                        "VALUES (?, ?, ?, ?, ?, ?) " +
                        "ON CONFLICT (payment_id) DO UPDATE SET status = EXCLUDED.status",
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getTerminalKey(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getNotificationUrl()
        );
    }

    public Payment findById(String paymentId) {
        List<Payment> result = jdbcTemplate.query(
                "SELECT * FROM payments WHERE payment_id = ?",
                new PaymentRowMapper(),
                paymentId
        );
        return result.isEmpty() ? null : result.get(0);
    }

    public List<Payment> findByOrderId(String orderId) {
        return jdbcTemplate.query(
                "SELECT * FROM payments WHERE order_id = ?",
                new PaymentRowMapper(),
                orderId
        );
    }

    private static class PaymentRowMapper implements RowMapper<Payment> {
        @Override
        public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Payment payment = new Payment(
                    rs.getString("payment_id"),
                    rs.getString("order_id"),
                    rs.getString("terminal_key"),
                    rs.getLong("amount"),
                    rs.getString("notification_url")
            );
            payment.setStatus(rs.getString("status"));
            return payment;
        }
    }
}