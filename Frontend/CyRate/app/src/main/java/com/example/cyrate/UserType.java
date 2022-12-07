package com.example.cyrate;


/**
 * Enums used to determine the classification of
 * a User type
 */
public enum UserType {
    GUEST,              //guest
    BASIC_USER,         //normal
    BUSINESS_OWNER,
    ADMIN;

    @Override
    public String toString(){
        String s;
        switch (this){
            case GUEST:
                s = "guest";
                break;
            case BUSINESS_OWNER:
                s = "owner";
                break;
            case ADMIN:
                s = "admin";
                break;
            case BASIC_USER:
            default:
                s = "normal";
                break;
        }
        return s;
    }

    /**
     * returns the UserType from the corresponding String as translated to String from UserType.toString()
     * @param type
     * @return
     */
    public static UserType fromString(String type){
        System.out.println(type);
        switch(type){
            case "guest":
                return GUEST;
            case "owner":
                return BUSINESS_OWNER;
            case "admin":
                return ADMIN;
            default:
                return BASIC_USER;
        }
    }
}


