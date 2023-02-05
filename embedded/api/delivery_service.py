from typing import Union
import requests

from api.box import Box
from api.user import User
from ase_io.card_content import CardContent
from common.config import Config


class DeliveryService:
    def __init__(self, config: Config):
        self.config = config

    def _get(self, url: str, params=None) -> requests.Response:
        return requests.get(
            self.config.delivery_server_address + url,
            params=params,
            headers={"Authorization": f"Bearer {self.config.admin_token}"}
        )

    def get_csrf_token(self) -> str:
        """Get the CSRF token from the delivery server."""
        response = self._get("/api/delivery/csrf")
        if response.status_code != 200:
            raise Exception(f"Failed to get CSRF token: {response.text}")
        print("Got CSRF token:", response.json())
        return response.json()["token"]

    def _post(self, url: str, json_data=None) -> requests.Response:
        return requests.post(
            self.config.delivery_server_address + url,
            json=json_data,
            headers={
                "Authorization": f"Bearer {self.config.admin_token}",
                "X-XSRF-Token": self.get_csrf_token()
            },
            cookies={"XSRF-TOKEN": self.get_csrf_token()}
        )

    def _put(self, url: str, json_data=None) -> requests.Response:
        return requests.put(
            self.config.delivery_server_address + url,
            json=json_data,
            headers={
                "Authorization": f"Bearer {self.config.admin_token}",
                "X-XSRF-Token": self.get_csrf_token()
            },
            cookies={"XSRF-TOKEN": self.get_csrf_token()}
        )

    def get_box(self, box_id: int) -> Union[Box, None]:
        """Return the box with the given id."""
        box_response = self._get(f"/api/delivery/boxes/box", {"id": box_id})
        if box_response.status_code == 404:
            return None
        if box_response.status_code != 200:
            raise Exception(f"Failed to get box: {box_response.text}")
        if box_response.text:
            return Box.from_json(box_response.json())
        return None

    def authenticate(self, card_content: CardContent):
        """Return True whether the card token is valid for this box."""
        card_token = card_content.token
        response = self._post(f"/api/delivery/boxes/{self.config.box_id}/authenticate", {"userToken": card_token})
        if response.status_code == 200:
            print(f"User with token {card_token} authenticated")
            return True
        else:
            print(f"User with token {card_token} not authenticated: {response.text}")
            return False

    def send_box_closed_event(self, user_token: str):
        """Send a box closed event to the delivery server for a certain user."""
        print("Sending box closed event")
        resp = self._post(f"/api/delivery/boxes/{self.config.box_id}/close", {"userToken": user_token})
        if resp.status_code != 200:
            raise Exception(f"Failed to send box closed event: {resp.text}")
        else:
            print("Box closed event sent")
