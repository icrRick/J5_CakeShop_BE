package com.fpoly.java5.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "full_name", nullable = false, columnDefinition = "NVARCHAR(50)")
    private String fullName;

    @Column(name = "phone", nullable = false, columnDefinition = "VARCHAR(10)")
    private String phone;

    @Column(name = "address", nullable = false, columnDefinition = "NVARCHAR(50)")
    private String address;

    @Column(name = "create_at", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createAt;
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private OrderStatus orderStatus;
}
