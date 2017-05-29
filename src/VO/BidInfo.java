package VO;

import java.util.List;

/**
 * Created by H77 on 2017/5/28.
 */
public class BidInfo {

    String hotelName;
    String hotelType;
    String roomType;
    String actualPrice;
    String roomPrice;
    int num;
    List<String> facilitys;
    public BidInfo(String hotelName, String hotelType, String roomType, String actualPrice, String roomPrice, int num, List<String> facilitys) {
        this.hotelName = hotelName;
        this.hotelType = hotelType;
        this.roomType = roomType;
        this.actualPrice = actualPrice;
        this.roomPrice = roomPrice;
        this.num = num;
        this.facilitys = facilitys;
    }
    public BidInfo(){}

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<String> getFacilitys() {
        return facilitys;
    }

    public void setFacilitys(List<String> facilitys) {
        this.facilitys = facilitys;
    }


}
