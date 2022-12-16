export default class User {
    readonly id: string;
    readonly email: string;
    readonly name: string;
    
    constructor(id: string, email: string, name: string) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    static fromJson(json: any): User {
        return new User(json.id, json.email, json.name);
    }
}