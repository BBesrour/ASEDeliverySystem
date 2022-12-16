export default class Delivery {
    readonly id: string | null;
    readonly targetCustomerID: string;
    readonly targetBoxID: string;
    readonly delivererID: string;
    readonly isActive: boolean;
    readonly status: string;

    constructor(id: string | null, targetCustomerID: string, targetBoxID: string, delivererID: string, status: string, isActive: boolean) {
        this.id = id;
        this.targetCustomerID = targetCustomerID;
        this.targetBoxID = targetBoxID;
        this.delivererID = delivererID;
        this.status = status;
        this.isActive = isActive;
    }

    static fromJson(json: any): Delivery {
        return new Delivery(json.id, json.targetCustomerID, json.targetBoxID, json.delivererID, json.status, json.isActive);
    }
}