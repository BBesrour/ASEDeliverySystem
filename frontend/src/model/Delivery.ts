class Delivery {
    readonly id: string;
    readonly targetCustomerID: string;
    readonly targetBoxID: string;
    readonly delivererID: string;

    constructor(id: string, targetCustomerID: string, targetBoxID: string, delivererID: string) {
        this.id = id;
        this.targetCustomerID = targetCustomerID;
        this.targetBoxID = targetBoxID;
        this.delivererID = delivererID;
    }

    static fromJson(json: any): Delivery {
        return new Delivery(json.id, json.targetCustomerID, json.targetBoxID, json.delivererID);
    }
}