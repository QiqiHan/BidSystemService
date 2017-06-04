package service;

import VO.UserInfo;

/**
 * Created by zcy on 2017/6/4.
 *
 */
public interface LoginService {
    public UserInfo login(String id, String pwd, String type);
}
