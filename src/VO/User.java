package VO;

import java.io.Serializable;

/**
 * Created by H77 on 2017/5/28.
 */
public class User implements Serializable{
    private static final long serialVersionUID = 7247714666080613254L;
    private String name;
    private String sex;

    public User(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }
    private User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
