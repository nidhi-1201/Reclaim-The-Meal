package com.parth.grabthemealv3;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class ResetController {

    @Autowired
            //user database table object
    UserRepo user;
    @Autowired
    ResetPasswordRepo resetPassword;
    @RequestMapping("reset")
    public String reset()
    {
        //reset password link
        return "forgotPassword.html";
    }

    //exectue when user submits reset password form
    @RequestMapping("/resetPassword")
    public String resetPassword(@RequestParam String eid)
    {
        Iterable<UserModel> allData = user.findAll();
        Iterator<UserModel> allDataIterator = allData.iterator();
        int flag=0;
        String userEmail;
        int userId=-1;
        System.out.println("Inside the function");
        while(allDataIterator.hasNext())
        {
            UserModel Obj=allDataIterator.next();
            userEmail = Obj.getEmail();
            userId = Obj.getUid();
            if(userEmail.equals(eid))
            {
                flag=1;
                break;
            }
        }
        if(flag==1) {
            //generating random id
            String token = UUID.randomUUID().toString();
            Iterable<ResetPasswordModel> itr = resetPassword.findAll();
            Iterator<ResetPasswordModel> allToks = itr.iterator();
            int cnt = 0;
            while (allToks.hasNext()) {
                ResetPasswordModel mod = allToks.next();
                if (mod.getUid() == userId) {
                    System.out.println("inside if 1");
                    cnt = 1;
                }
            }
            if (cnt == 1) {
                resetPassword.deleteById(userId);
            }
            System.out.println("Outside the while");
            Date date = new Date();
            Timestamp currentTime = new Timestamp(date.getTime() + TimeUnit.MINUTES.toMillis(5));
            ResetPasswordModel rpm = new ResetPasswordModel();
            rpm.setToken(token);
            rpm.setUid(userId);
            rpm.setExipryDate(currentTime);
            resetPassword.save(rpm);
            String resetLink = "http://localhost:8080/setNewPassword?token="+token;
            System.out.print(resetLink);
        }
        return "forgotPassword.html";
    }

    @RequestMapping("/setNewPassword")
    public ModelAndView setNewPassword(@RequestParam String token, HttpServletRequest req)
    {
        ModelAndView mv=new ModelAndView();
        if(token.isEmpty())
        {
            //Error
            mv.addObject("user",null);
            mv.setViewName("forgotPassword.html");
            return mv;
        }
        else {
            Iterable<ResetPasswordModel> tokItr = resetPassword.findAll();
            Iterator<ResetPasswordModel> toksItr = tokItr.iterator();
            boolean flag;
            ResetPasswordModel rpmObj = null;
            while (toksItr.hasNext()) {
                rpmObj = toksItr.next();
                if ((rpmObj.getToken()).equals(token)) {
                    break;
                }
            }
            if (rpmObj == null) {
                mv.addObject("user",null);
                mv.setViewName("forgotPassword.html");
                return mv;
            } else {
                Date date = new Date();
                Timestamp currentTime = new Timestamp(date.getTime());
                if (currentTime.compareTo(rpmObj.getExipryDate()) > 0) {
                    //expired
                    mv.addObject("user",null);
                    mv.setViewName("forgotPassword.html");
                    return mv;
                } else {
                    // continue to reset password page
                    HttpSession sess = req.getSession();
                    sess.setAttribute("uid",rpmObj.getUid());
                    System.out.println(req.getSession().getAttribute("uid"));
                    mv.addObject("user",rpmObj.getUid());
                    mv.setViewName("resetPassword.html");
                    return mv;
                }
            }
        }
    }

    @RequestMapping("/submitPassword")
    public String submitPassword(@RequestParam String pass,@RequestParam String userid,HttpServletRequest req)
    {
        System.out.println(pass); //password to be stored in appropriate database
        System.out.println(userid); //userid whose password needs to be changed
        return "resetPassword.html";
    }
}
