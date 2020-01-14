package com.distributedcomputingproject.videomanagementservice.entities;

import com.distributedcomputingproject.videomanagementservice.Status;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "The name parameter must not be blank!")
    @Column(unique = true)
    private String name;

    @NotNull(message = "The author parameter must not be blank!")
    private String author;

    @NotNull(message = "The status parameter must not be blank!")
    private Status status;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Transient
    public boolean hasUser(User user){
        return this.user.getId() == user.getId();
    }

    @Transient
    public boolean isAvailable(){
        return this.status == Status.AVAILABLE;
    }

    @Transient
    public boolean isWaitingUpload() { return this.status == Status.WAITING_UPLOAD; }

    @Transient
    public boolean isUploaded() { return this.status == Status.UPLOADED; }
}
