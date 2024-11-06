package csd_assignment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;  // Thêm serialVersionUID để tương thích với serialization

    private String name;
    private String phoneNumber;  // SĐT
    private String password;     // Mật khẩu
    private String hometown;
    private String workStudyPlace;
    private String gender;       // Giới tính
    private String dateOfBirth;  // Ngày sinh

    private List<Person> friends;
    private List<Post> posts;

    public void reSort(){
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));
    }

    public Person(String name, String phoneNumber, String password, String hometown,
                  String workStudyPlace, String gender, String dateOfBirth) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.hometown = hometown;
        this.workStudyPlace = workStudyPlace;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.friends = new ArrayList<>();
        this.posts = new ArrayList<>();
    }

    // Getter methods
    public String getId() { return phoneNumber; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPassword() { return password; }
    public String getHometown() { return hometown; }
    public String getWorkStudyPlace() { return workStudyPlace; }
    public String getGender() { return gender; }
    public String getDateOfBirth() { return dateOfBirth; }
    public List<Person> getFriends() { return friends; }
    public List<Post> getPosts() { return posts; }

    // Setter methods
    public void setName(String name) { this.name = name; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setPassword(String password) { this.password = password; }
    public void setHometown(String hometown) { this.hometown = hometown; }
    public void setWorkStudyPlace(String workStudyPlace) { this.workStudyPlace = workStudyPlace; }
    public void setGender(String gender) { this.gender = gender; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public void changePass(String s){
        setPassword(s);
    }

    public void addFriend(Person p) {
        if (!friends.contains(p)) {
            friends.add(p);
            p.getFriends().add(this);
        }
    }
    public void deleteFriend(Person p){
        if(friends.contains(p)){
            friends.remove(p);
            p.getFriends().remove(this);
        }
    }

    public void addPost(Post post) {
        posts.addFirst(post);
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', phoneNumber='" + phoneNumber + "', gender='" + gender + "', dateOfBirth='" + dateOfBirth + "', hometown='" + hometown + "', workStudyPlace='" + workStudyPlace + "'}";
    }
}
