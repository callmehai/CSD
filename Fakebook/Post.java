package csd_assignment;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Post implements Serializable {
    private static final long serialVersionUID = 1L;

    private String content;
    private String timestamp;
    private String user;

    public Post(String content,String poster) {
        this.content = content;
        this.timestamp = getFormattedTimestamp(); // Lấy thời gian hiện tại
        this.user=poster;
    }

    public String getContent() { return content; }
    public String getTimestamp() { return timestamp; }
    public String getName() { return user; }


    // Định dạng thời gian theo kiểu: Nov/06/2024 15:55 UTC+7
    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7")); // Đặt múi giờ UTC+7

        return sdf.format(new Date()) + " UTC+7";
    }

    public void update(String C,String T, String U){
        content=C;
        timestamp=T;
        user=U;
    }
}
