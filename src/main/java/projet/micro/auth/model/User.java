package projet.micro.auth.model;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class User 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    
    //Pour que le mot de passse n'apparaît pas dans les données JSON (pour XML @XmlTransient)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(fetch=FetchType.EAGER)
    private Collection<Role> roles=new ArrayList<>();
}