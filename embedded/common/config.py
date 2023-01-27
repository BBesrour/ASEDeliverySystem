import json


class Config:
    def __init__(self, config_file_name: str):
        with open(config_file_name) as f:
            config = json.load(f)
        self.box_id = config["box_id"]
        self.box_name = config["box_name"]
        self.street_address = config["street_address"]
        self.auth_server_address = config["auth_server_address"]
        self.delivery_server_address = config["delivery_server_address"]
        self.admin_token = config["admin_token"]
