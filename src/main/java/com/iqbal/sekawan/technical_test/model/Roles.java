package com.iqbal.sekawan.technical_test.model;

import com.iqbal.sekawan.technical_test.statval.ERole;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_role")
@Builder
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_seq")
    @SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq", allocationSize = 1)
    private String id;

    @Enumerated(EnumType.STRING)
    private ERole part;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ERole getPart() {
        return part;
    }

    public void setPart(ERole part) {
        this.part = part;
    }
}
