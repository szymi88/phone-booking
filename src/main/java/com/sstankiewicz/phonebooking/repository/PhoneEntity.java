package com.sstankiewicz.phonebooking.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="PHONES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneEntity{
    @Id
    private int id;
    private String phoneName;
    private int stockCount;

}
