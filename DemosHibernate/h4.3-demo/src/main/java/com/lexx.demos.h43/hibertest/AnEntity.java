package com.edifecs.hibertest;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class contains
 *
 * @author Alexandru Corghencea <a href="c-alexandru.corghencea@edifecs.com">
 *         c-alexandru.corghencea@edifecs.com</a>
 */
@Entity(name = "an_entity")
@Table(name = "an_entity")
public class AnEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "name")
    private String name;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
