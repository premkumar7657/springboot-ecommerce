package com.prem.ecommerce.Model;


import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
public class Roles {

    
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)

   @Column(name = "role_id")
   private Long roleId;

   @ToString.Exclude
   @Enumerated(EnumType.STRING)
   @Column(name = "role_name", length = 20)
   private AppRole roleName;

   @Getter
   @Setter
   @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST},
               fetch = FetchType.EAGER)
   @JoinTable( name = "user_role",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
   ) 
   @ToString.Exclude
   private Set<User> roles = new HashSet<>();

    

}
