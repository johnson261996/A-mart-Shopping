package android.mendonca.averdapp.model;

import java.io.Serializable;

/**
 * Created by Lincoln on 07/01/16.
 */
public class User implements Serializable {
    String id, name, email,type,uname,phone,phone2,addrs,addrs2;

    public User() {
    }

    public User(String id, String name, String email,String type,String uname,String phone,String phone2,String addrs,String addrs2) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.type= type;
        this.uname=uname;
        this.phone=phone;
        this.phone2=phone2;
        this.addrs=addrs;
        this.addrs2=addrs2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsrName(){return uname;}

    public void setUsrName(String uname){ this.uname = uname;}

    public String getPhone(){return phone;}

    public void setPhone(String phone){this.phone=phone;}

    public String getPhone2(){return phone2;}

    public void setPhone2(String phone2){this.phone2=phone2;}

    public String getAddrs(){return addrs;}

    public void setAddrs(String addrs){this.addrs=addrs;}

    public String getAddrs2(){return addrs2;}

    public void setAddrs2(String addrs2){this.addrs2=addrs2;}
}
