package com.edifecs.hibertest;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by c-ionnmoro on 19-Nov-16.
 */
@Entity(name = "test_entity_a")
@Table(name = "test_entity_a")
public class TestEntityA implements Serializable {
    @Id
    @Column
    private long id;

    @Column
    private String name;

    public TestEntityA() {
    }

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