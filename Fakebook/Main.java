
package csd_assignment;
import java.io.*;
import java.util.*;

public class Main {

    private static final String DATA_FILE = "data.dat";  // Tên file lưu trữ dữ liệu
    private static Person loggedInPerson = null; // Người dùng hiện tại đang đăng nhập

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Person> users = loadUsers(); // Tải dữ liệu người dùng từ file

//        for(Person p: users.values()){
//            for(Post P: p.getPosts()){
//                P.update(P.getContent(),P.getTimestamp(),p.getName());
//            }
//            p.reSort();
//        }

        SocialMedia graph = new SocialMedia(users);

        boolean running = true;

        while (running) {
            System.out.println("=== Fakebook ===");

            if (loggedInPerson == null) {

                System.out.println("1. Đăng nhập");
                System.out.println("2. Đăng ký");
                System.out.println("3. Thoát");
                System.out.print("Chọn chức năng: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Đọc bỏ newline

                switch (choice) {
                    case 1: // Đăng nhập
                        System.out.print("Nhập số điện thoại: ");
                        String loginPhone = scanner.nextLine();
                        System.out.print("Nhập mật khẩu: ");
                        String loginPassword = scanner.nextLine();

                        // Kiểm tra đăng nhập
                        Person loggedIn = users.get(loginPhone);
                        if (loggedIn != null && loggedIn.getPassword().equals(loginPassword)) {
                            loggedInPerson = loggedIn;
                            System.out.println("Đăng nhập thành công! Chào mừng, " + loggedInPerson.getName());
                        } else {
                            System.out.println("SĐT hoặc mật khẩu không chính xác.");
                        }
                        break;

                    case 2: // Đăng ký
                        System.out.print("Nhập số điện thoại: ");
                        String phone = scanner.nextLine();
                        System.out.print("Nhập mật khẩu: ");
                        String password = scanner.nextLine();

                        if(phone.isEmpty() || password.isEmpty()){
                            System.out.println("Không được bỏ trống trường nào");
                            break;
                        }

                        // Kiểm tra nếu đã có người dùng với số điện thoại này chưa
                        if (users.containsKey(phone)) {
                            System.out.println("Số điện thoại đã tồn tại.");
                        } else {
                            // Tạo mới người dùng
                            Person newPerson = new Person("Account " + phone, phone, password, "None", "None", "None", "None");
                            graph.addPerson(newPerson);
                            users.put(phone, newPerson);
                            System.out.println("Đăng ký thành công!");

                            // Đăng nhập tự động vào tài khoản vừa đăng ký
                            loggedInPerson = newPerson;
                        }
                        break;

                    case 3:
                        running = false;
                        System.out.println("Đã thoát Fakebook.");
                        break;

                    default:
                        System.out.println("Lựa chọn không hợp lệ.");
                }
            }// Chưa đăng nhập
            else {

                System.out.println("--- Xin chào "+loggedInPerson.getName()+" ---");
                System.out.println("1. Mở Newfeed");
                System.out.println("2. Đăng bài");
                System.out.println("3. Xem danh sách bạn bè");
                System.out.println("4. Xem gợi ý kết bạn");
                System.out.println("5. Xem trang cá nhân");
                System.out.println("6. Sửa đổi thông tin cá nhân");
                System.out.println("7. Đổi mật khẩu");
                System.out.println("8. Đăng xuất");
                System.out.println("9. Thoát");
                System.out.print("Chọn chức năng: ");

                int choice = scanner.nextInt();
                scanner.nextLine();  // Đọc bỏ newline

                switch (choice) {
                    case 1: // Newfeeds
                        displayFeed(loggedInPerson);
                        break;
                    case 2: //Post bài
                        newPost(loggedInPerson,scanner);
                        break;
                    case 3: // Xem list friend
                        displayFriends(loggedInPerson);
                        break;

                    case 4: // Xem gợi ý kết bạn
                        displayPotentialFriend(loggedInPerson,graph,scanner);
                        break;

                    case 5: // Xem trang cá nhân
                        displayPersonalProfile(loggedInPerson);
                        break;

                    case 6: // Sửa thông tin cá nhân
                        editPersonalProfile(loggedInPerson, scanner);
                        saveUsers(users);
                        break;
                    case 7: // Đổi pass
                        changePass(loggedInPerson, scanner);
                        saveUsers(users);
                        break;
                    case 8:
                        loggedInPerson = null;
                        System.out.println("Đăng xuất thành công.");
                        break;

                    case 9:
                        running = false;
                        System.out.println("Đã thoát Fakebook.");
                        break;

                    default:
                        System.out.println("Lựa chọn không hợp lệ.");
                }
            }//Đã đăng nhập

            saveUsers(users);
            System.out.println("Press enter to continue");
            scanner.nextLine();
        }

    }

    private static void displayFeed(Person person) {
        System.out.println("\n--- Your Newfeed ---");
        List<Post> allPosts = new ArrayList<>();

        // Thu thập bài đăng của chính người đó và bạn bè
        allPosts.addAll(person.getPosts());
        for (Person friend : person.getFriends()) {
            allPosts.addAll(friend.getPosts());
        }

        // Sắp xếp bài đăng theo thời gian
        allPosts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));

        // Hiển thị các bài đăng đã sắp xếp
        for (Post post : allPosts) {
            System.out.println("Time: " + post.getTimestamp());
            System.out.println("Account: " + post.getName());
            System.out.println("Status: " + post.getContent());
            System.out.println("==========================");
        }
    }// show newfeed

    private static void newPost(Person person,Scanner scanner){
        System.out.print(person.getName()+" à, bạn đang nghĩ gì thế?(Bỏ trống để thoát): ");
        String status = scanner.nextLine();
        if(status.isEmpty()) return;
        person.addPost(new Post(status,person.getName()));
    }

    private static void displayFriends(Person person) {
        System.out.println("\n--- Danh sách bạn bè ---");
        List<Person> friends = person.getFriends();
        for (int i = 0; i < friends.size(); i++) {
            System.out.println((i + 1) + ". " + friends.get(i).getName());
        }
        if(friends.isEmpty()) return;
        System.out.print("Chọn số để xem trang cá nhân của bạn bè [type 0 to continue]: ");
        int friendIndex = new Scanner(System.in).nextInt() - 1;
        if(friendIndex==-1){
            System.out.print("Chọn số để xoá bạn bè [type 0 to continue]: ");
            int id = new Scanner(System.in).nextInt() - 1;
            if(id==-1) return;
            if (id >= 0 && id < friends.size()) {
                System.out.println("Đã xoá bạn bè với "+friends.get(id).getName());
                person.deleteFriend(friends.get(id));
            } else {
                System.out.println("Số không hợp lệ.");
            }

            return;
        }
        if (friendIndex >= 0 && friendIndex < friends.size()) {
            displayPersonalProfile(friends.get(friendIndex));
        } else {
            System.out.println("Số không hợp lệ.");
        }
    }// show friends

    private static void displayPotentialFriend(Person person, SocialMedia graph, Scanner scanner){

        List<Person> suggestedFriends = graph.findPotentialFriends(person);

        System.out.println("Gợi ý bạn bè: ");
        for (int i = 0; i < suggestedFriends.size(); i++) {
            System.out.println((i + 1) + ". " + suggestedFriends.get(i).getName());
        }
        System.out.print("Bạn muốn kết bạn với bao nhiêu người [type 0 to exit]: ");
        int num = scanner.nextInt();
        if(num==0) return;

        if(num>suggestedFriends.size()){
            System.out.println("Số người để kết bạn không đủ.");
        }
        else{
            System.out.println("Nhập số thứ tự của "+num+" người bạn muốn kết bạn: ");
            for(int i=1;i<=num;i++){
                System.out.print("Người thứ "+i+": ");
                int id = scanner.nextInt();
                if(id>=1 && id<=num){
                    person.addFriend(suggestedFriends.get(id-1));
                    System.out.println("Đã kết bạn với "+suggestedFriends.get(id-1).getName());
                }
                else{
                    System.out.println("Số thứ tự không hợp lệ");
                }
            }

            scanner.nextLine();
        }
    }

    private static void displayPersonalProfile(Person person) {
        System.out.println("\n--- Trang cá nhân ---");
        System.out.println("Tên: " + person.getName());
        System.out.println("Số điện thoại: " + person.getPhoneNumber());
        System.out.println("Quê quán: " + person.getHometown());
        System.out.println("Nơi làm việc/học tập: " + person.getWorkStudyPlace());
        System.out.println("Giới tính: " + person.getGender());
        System.out.println("Ngày sinh: " + person.getDateOfBirth());

        System.out.println("Bạn bè: ");
        System.out.println();
        for (Person friend : person.getFriends()) {
            System.out.println("- " + friend.getName());
        }

        System.out.println("Các bài đăng: ");
        System.out.println();
        for (Post post : person.getPosts()) {
            System.out.println("Time: " + post.getTimestamp());
            System.out.println("Account: " + post.getName());
            System.out.println("Status: " + post.getContent());
            System.out.println("==========================");
        }
    }// show info

    private static void editPersonalProfile(Person person, Scanner scanner) {
        System.out.println("\n--- Sửa đổi thông tin cá nhân ---");

        // Sửa tên
        System.out.print("Nhập tên mới (bỏ trống nếu không thay đổi): ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            person.setName(newName);
            System.out.println("Tên đã được cập nhật thành công!");
        }

        // Sửa quê quán
        System.out.print("Nhập quê quán mới (bỏ trống nếu không thay đổi): ");
        String newHometown = scanner.nextLine();
        if (!newHometown.isEmpty()) {
            person.setHometown(newHometown);
            System.out.println("Quê quán đã được cập nhật thành công!");
        }

        // Sửa nơi làm việc / học tập
        System.out.print("Nhập nơi làm việc / học tập mới (bỏ trống nếu không thay đổi): ");
        String newWorkStudyPlace = scanner.nextLine();
        if (!newWorkStudyPlace.isEmpty()) {
            person.setWorkStudyPlace(newWorkStudyPlace);
            System.out.println("Nơi làm việc / học tập đã được cập nhật thành công!");
        }

        // Sửa giới tính
        System.out.print("Nhập giới tính mới (bỏ trống nếu không thay đổi): ");
        String newGender = scanner.nextLine();
        if (!newGender.isEmpty()) {
            person.setGender(newGender);
            System.out.println("Giới tính đã được cập nhật thành công!");
        }

        // Sửa ngày sinh
        System.out.print("Nhập ngày sinh mới (DD/MM/YYYY) (bỏ trống nếu không thay đổi): ");
        String newDateOfBirth = scanner.nextLine();
        if (!newDateOfBirth.isEmpty()) {
            person.setDateOfBirth(newDateOfBirth);
            System.out.println("Ngày sinh đã được cập nhật thành công!");
        }
    }// đổi info

    private static void changePass(Person person, Scanner scanner){
        System.out.print("Nhập mật khẩu cũ: ");
        String oldPass = scanner.nextLine();

        System.out.print("Nhập mật khẩu mới: ");
        String newPass1 = scanner.nextLine();

        System.out.print("Xác nhận lại mật khẩu mới: ");
        String newPass2 = scanner.nextLine();

        if(oldPass.equals(person.getPassword())){
            if(newPass1.equals(newPass2)){
                person.changePass(newPass1);
                System.out.println("Đổi mật khẩu thành công");
            }
            else{
                System.out.println("Mật khẩu mới không trùng khớp");
            }
        }
        else{
            System.out.println("Mật khẩu cũ không đúng.");
        }
    }// đổi pass

    private static void saveUsers(Map<String, Person> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi lưu dữ liệu.");
        }
    }//save file

    private static Map<String, Person> loadUsers() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (Map<String, Person>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new HashMap<>();
    }//load file
}
