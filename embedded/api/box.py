class Box:
    id: str
    name: str
    assigned_customer: str
    address: str

    def __init__(self, id: str, name: str, assigned_customer: str, address: str):
        self.id = id
        self.name = name
        self.assigned_customer = assigned_customer
        self.address = address

    def __str__(self):
        return f"Box(id={self.id}, name={self.name}, assigned_customer={self.assigned_customer}, address={self.address})"

    @staticmethod
    def from_json(json: dict):
        return Box(json["id"], json["name"], json["assignedCustomer"], json["address"])
