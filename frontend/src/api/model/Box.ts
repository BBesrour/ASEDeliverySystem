export default class Box {
    readonly id: string;
    readonly name: string;
    readonly address: string;
    readonly numberOfItems: string;
    readonly status: string;

    constructor(id: string, name: string, address: string, numberOfItems: string, status: string) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.numberOfItems = numberOfItems;
        this.status = status;
    }

    static fromJson(json: any): Box {
        return new Box(json.id, json.name, json.address, json.numberOfItems, json.status);
    }
}