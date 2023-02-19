package com.mockingbird.tmall.pojo;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
    private int id;

    private String name;
    private String password;
    private String salt;

    @Transient
    private String anonymousName;

    public String getAnonymousName(){
        if(null!=anonymousName)
            return anonymousName;
        if(null==name)
            anonymousName = null;
        else if(name.length()<=1)
            anonymousName = "*";
        else if(name.length()==2)
            anonymousName = name.substring(0,1) +"*";
        else {
            char[] cs =name.toCharArray();
            for (int i = 1; i < cs.length-1; i++) {
                cs[i]='*';
            }
            anonymousName = new String(cs);
        }
        return anonymousName;
    }
}
