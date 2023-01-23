export type UserRole = "ROLE_CUSTOMER" | "ROLE_DELIVERER" | "ROLE_DISPATCHER";

export default class User {
    readonly id: string | null;
    readonly email: string;
    readonly password: string | null;
    readonly name: string;
    readonly role: UserRole;
    
    constructor(id: string | null, email: string, password: string | null, name: string, role: UserRole) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    static fromJson(json: any): User {
        return new User(json.id, json.email, null, json.name, json.role);
    }
}