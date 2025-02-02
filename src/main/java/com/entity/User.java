package com.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.persistence.Table;
import java.awt.print.Book;
import java.io.Serializable;
import java.util.List;

@SqlResultSetMapping(
        name = "BookMapping",
        entities = @EntityResult(
                entityClass = User.class,
                fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "username", column = "username"),
                        @FieldResult(name = "title", column = "areas"),
                        @FieldResult(name = "publishingDate", column = "role"),
                        @FieldResult(name = "publisher", column = "publisherid")}))

@NamedQueries({
        @NamedQuery(name = "getAllUsers", query = "FROM User as usr ORDER BY usr.id"),
//        @NamedQuery(name = "getUsersNotForArea", query = "FROM User usr WHERE usr.")
})

@NamedNativeQueries({
        @NamedNativeQuery(name = "getUsersNotForArea", query = "SELECT *\n" +
                "FROM users " +
                "WHERE id NOT IN (" +
                "  SELECT junction.user_fk" +
                "  FROM areas_users as junction" +
                "  WHERE area_fk = (?)" +
                ")", resultClass = User.class)

})
@Entity
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Table(name = "users", schema = "public", catalog = "PocketOrder")
public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private List<Area> areas;

    private UserRole role;

    @Id
    @SequenceGenerator(name="pk_sequence",sequenceName="users_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.AUTO ,generator="pk_sequence")
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username", nullable = false, length = 20)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    @Basic
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "psswrd", nullable = false, length = 40)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    @ManyToOne
    @JoinColumn(name = "role_fk", referencedColumnName = "id", nullable = false)
    public UserRole getRole() {
        return this.role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Transient
    private boolean isAdmin(){
        if (this.getRole().getName() == "floormanager"){
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", areas=" + areas +
                ", Role=" + this.role +
                '}';
    }
}
