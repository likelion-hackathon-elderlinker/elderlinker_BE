package com.example.elderlinker.User;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProfileForm {
    private String newPhoneNumber;
    private String newPassword;
    private String confirmNewPassword;

    // getter, setter 메서드
    public String getNewPhoneNumber() {
        return newPhoneNumber;
    }

    public void setNewPhoneNumber(String newPhoneNumber) {
        this.newPhoneNumber = newPhoneNumber;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
