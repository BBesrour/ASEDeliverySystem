export default class Delivery {
    readonly id: string | null;
    readonly targetCustomerID: string;
    readonly targetBoxID: string;
    readonly delivererID: string;
    readonly active: boolean;
    readonly status: string;

    constructor(id: string | null, targetCustomerID: string, targetBoxID: string, delivererID: string, status: string, active: boolean) {
        this.id = id;
        this.targetCustomerID = targetCustomerID;
        this.targetBoxID = targetBoxID;
        this.delivererID = delivererID;
        this.status = status;
        this.active = active;
    }

    static fromJson(json: any): Delivery {
        return new Delivery(json.id, json.targetCustomerID, json.targetBoxID, json.delivererID, json.status, json.active);
    }
}