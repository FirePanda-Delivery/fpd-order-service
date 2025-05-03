package ru.diplom.fpd.order.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.sql.Time;
import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"order\"")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_SEQUENCE")
    @SequenceGenerator(name = "ORDER_SEQUENCE", sequenceName = "order_id_sequence", allocationSize = 1)
    private long id;

   @Column(nullable = false)
    private Long restaurantId;

   @Column(nullable = false)
    private Long userId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    private Set<OrderProduct> productList;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = true)
    private Long courierId;

    @Column(nullable = false)
    private Time timeStart;

    @Column
    private Time timeEnd;


    @Column(nullable = false)
    private String address;

    @Column
    private String city;

    @Column
    private String restaurantAddress;

}


