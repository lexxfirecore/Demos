package com.blogspot.aknowakowski;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="task")
@Table(name="task")
public class Task {
    @Id
    @GeneratedValue
    @Column(name="task_id")
    Long id;

    @Column(name="name")
    String name;

    public Task(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Task() {
    }

    @Column(name="description")
    String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}