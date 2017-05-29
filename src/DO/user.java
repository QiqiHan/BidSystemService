package DO;

public class user {
    private Integer userid;

    private String username;

    private String password;

    private String sex;

    private String job;

    private Integer age;

    public user(Integer userid, String username, String password, String sex, String job, Integer age) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.job = job;
        this.age = age;
    }

    public user() {
        super();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job == null ? null : job.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}