export default class Box {
  readonly id: string | null;
  name: string;
  address: string;
  numberOfItems: string;
  readonly assigned_to: string;
  readonly assigned_customers: string[];
  readonly key: string;

  constructor(
    id: string | null,
    name: string,
    address: string,
    numberOfItems: string,
    assigned_to: string,
    assigned_customers: string[],
    key: string
  ) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.numberOfItems = numberOfItems;
    this.assigned_to = assigned_to;
    this.assigned_customers = assigned_customers;
    this.key = key;
  }

  static fromJson(json: any): Box {
    return new Box(
      json.id,
      json.name,
      json.address,
      json.numberOfItems,
      json.assigned_to,
      json.assigned_customers,
      json.key
    );
  }
}
