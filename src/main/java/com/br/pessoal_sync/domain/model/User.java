package com.br.pessoal_sync.domain.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", unique = true)
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome não deve exceder 100 caracteres")
    private String name;
    @Column(name = "email", unique = true)
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;
    @Column(name = "cpf", unique = true)
    @NotBlank(message = "O CPF é obrigatório")
    @Size(min = 14, max = 14, message = "CPF inválido")
    private String cpf;
    @Column(name = "active")
    private boolean active;
    @CreationTimestamp
    private Instant created_at;
    @UpdateTimestamp
    private Instant updated_at;

    public User() {}

    public User(String name, String email, String cpf, boolean active, Instant created_at, Instant updated_at) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.active = active;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean getIsActive() {
        return active;
    }

    public void setIsActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Instant created_at) {
        this.created_at = created_at;
    }

    public Instant getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(Instant updated_at) {
        this.updated_at = updated_at;
    }

}
