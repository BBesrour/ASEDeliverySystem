class Delivery {
    private id: String;
    private targetCustomerID: String;
    private targetBoxID: String;
    private delivererID: String;

    constructor(id: String, targetCustomerID: String, targetBoxID: String, delivererID: String) {
        this.id = id;
        this.targetCustomerID = targetCustomerID;
        this.targetBoxID = targetBoxID;
        this.delivererID = delivererID;
    }
}