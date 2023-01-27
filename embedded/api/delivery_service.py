import requests

from api.box import Box
from api.user import User
from ase_io.card_content import CardContent
from common.config import Config
from main import config


class DeliveryService:
    def __init__(self, config: Config):
        self.config = config

    def _get(self, url: str, params=None):
        return requests.get(
            self.config.delivery_server_address + url,
            params=params,
            headers={"Authorization": f"Bearer {self.config.delivery_server_access_token}"}
        )

    def _post(self, url: str, data=None):
        return requests.post(
            self.config.delivery_server_address + url,
            data=data,
            headers={"Authorization": f"Bearer {self.config.delivery_server_access_token}"}
        )

    def _put(self, url: str, data=None):
        return requests.put(
            self.config.delivery_server_address + url,
            data=data,
            headers={"Authorization": f"Bearer {self.config.delivery_server_access_token}"}
        )

    def get_box(self, box_id: int) -> Box | None:
        """Return the box with the given id."""
        box_response = self._get(f"/box/{box_id}").json()
        if box_response.status_code == 404:
            return None
        if box_response.status_code != 200:
            raise Exception(f"Failed to get box: {box_response.text}")
        if box_response.text:
            return Box.from_json(box_response.json())
        return None

    def get_user(self, card_token: str) -> User | None:
        """Return the user with the given card token."""
        user_response = self._get(f"/user/token-to-user", {"token": card_token})
        if user_response.status_code == 404:
            return None
        if user_response.status_code != 200:
            raise Exception(f"Failed to get user: {user_response.text}")
        if user_response.text:
            return User.from_json(user_response.json())
        return None

    def authenticate(self, card_content: CardContent):
        """Return True whether the card token is valid for this box."""
        card_token = card_content.token
        user = self.get_user(card_token)
        box = self.get_box(self.config.box_id)
        return user is not None and user.id in (box.assigned_to, box.assigned_customer)

    def send_box_closed_event(self):
        """Send a box closed event to the delivery server."""
        self._post(f"/box/{self.config.box_id}/closed")
