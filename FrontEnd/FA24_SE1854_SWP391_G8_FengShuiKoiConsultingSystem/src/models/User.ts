import Role from "./Role";

class User {
    id?: number;
    username?: string;
    password?: string;
    fullname?: string;
    email?: string;
    phoneNumber?: string; 
    gender?: string;
    avatar?: string; 
    dob?: Date;
    code?: string;
    noPassword?: boolean;
    status?: string;
    createdDate?: Date;
    createdBy?: string;
    updatedDate?: Date;
    updatedBy?: string;
    roles?: Role[];
    constructor(
        id?: number,
        username?: string,
        password?: string,
        fullname?: string,
        email?: string,
        phoneNumber?: string,
        gender?: string,
        avatar?: string,
        dob?: Date,
        code?: string,
        status?: string,
        noPassword?: boolean,
        createdDate?: Date,
        createdBy?: string,
        updatedDate?: Date,
        updatedBy?: string,
        roles?: Role[]

    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.avatar = avatar;
        this.dob = dob;
        this.code = code;
        this.status = status;
        this.noPassword = noPassword;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.updatedDate = updatedDate;
        this.updatedBy = updatedBy;
        this.roles = roles;
    }
}

export default User;
